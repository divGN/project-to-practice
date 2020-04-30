package application.heroes.web.controller;

import application.heroes.domain.bindingModels.CreateHeroBindingModel;
import application.heroes.service.HeroService;
import application.heroes.service.serviceModels.HeroServiceModel;
import application.heroes.service.serviceModels.UserServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class HeroController {
    private final HeroService heroService;
    private final ModelMapper modelMapper;

    @Autowired
    public HeroController(HeroService heroService, ModelMapper modelMapper) {
        this.heroService = heroService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/heroes/create")
    public ModelAndView createHero(ModelAndView modelAndView){
        modelAndView.setViewName("hero/create-hero");
        return modelAndView;
    }

    @PostMapping("/heroes/create")
    public ModelAndView createHeroConfirm(@ModelAttribute  CreateHeroBindingModel model,
                                          ModelAndView modelAndView, HttpSession session){
        String username = (String) session.getAttribute("username");
        HeroServiceModel hero = this.heroService.createHero(username, this.modelMapper.map(model, HeroServiceModel.class));

        if (hero == null){
            modelAndView.setViewName("redirect:hero/create-hero");
        }else {
            modelAndView.setViewName("redirect:/home/home-with-created-hero");
        }
        return modelAndView;
    }

    @GetMapping("/heroes/details/{name}")
    public ModelAndView heroDetails(@PathVariable("name") String name ,ModelAndView modelAndView, HttpSession session){
        String username = (String) session.getAttribute("username");
        HeroServiceModel hero = this.heroService.findHeroByUser(username);
        session.setAttribute("name",hero.getName());
        session.setAttribute("level",hero.getLevel());
        session.setAttribute("strength",hero.getStrength());
        session.setAttribute("stamina",hero.getStamina());
        session.setAttribute("attack",hero.getAttack());
        session.setAttribute("gender",hero.getGender());
        session.setAttribute("defence",hero.getDefence());
        modelAndView.setViewName("hero/hero-details");
        return modelAndView;
    }

    public ModelAndView confirmHeroDetails( ModelAndView modelAndView){

        return modelAndView;
    }

}
