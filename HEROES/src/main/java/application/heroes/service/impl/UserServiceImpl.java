package application.heroes.service.impl;

import application.heroes.domain.entities.Hero;
import application.heroes.domain.entities.User;
import application.heroes.repository.UserRepository;
import application.heroes.service.HeroService;
import application.heroes.service.UserService;
import application.heroes.service.serviceModels.UserServiceModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;



    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean register(UserServiceModel userServiceModel) {
        User user = this.modelMapper.map(userServiceModel,User.class);
        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        try {
            this.userRepository.saveAndFlush(user);
            return true;
        }catch (IllegalArgumentException ex){
            return false;
        }
    }

    @Override
    public UserServiceModel login(UserServiceModel userServiceModel) {
        User user = this.userRepository.findByUsernameAndPassword(userServiceModel.getUsername(),
                DigestUtils.sha256Hex(userServiceModel.getPassword()));

        if (user == null){
            return null;
        }
        return this.modelMapper.map(user,UserServiceModel.class);
    }

    @Override
    public boolean addHeroToUser(String username, Hero hero) {
        User user = this.userRepository.findByUsername(username);
        if (user == null){
            return false;
        }else {
            user.setHero(hero);
            this.userRepository.saveAndFlush(user);
            return true;
        }
    }

    @Override
    public User findUserByName(String name) {
      return  this.userRepository.findByUsername(name);
    }

    @Override
    public UserServiceModel getUserProfile(String username) {
        return this.modelMapper.map(this.userRepository.findByUsername(username),UserServiceModel.class);
    }
}
