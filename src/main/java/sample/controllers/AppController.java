package sample.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 18/7/12
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class AppController {

    @RequestMapping("/")
    ModelAndView indexPage(){
        ModelAndView mv = new ModelAndView("index");
        return mv;
    }

    @RequestMapping("/twimini/home")
    ModelAndView HomePage(){
        ModelAndView mv = new ModelAndView("home");
        return mv;
    }

    @RequestMapping("/profile")
    ModelAndView SelfProfilePage(){
        ModelAndView mv = new ModelAndView("profile");
        return mv;
    }

    @RequestMapping("/profile/{userID}")
    ModelAndView ProfilePage(){
        ModelAndView mv = new ModelAndView("profile");
        return mv;
    }

    @RequestMapping("/twimini/settings")
    ModelAndView SettingsPage(){
        ModelAndView mv = new ModelAndView("settings");
        return mv;
    }

    @RequestMapping("/search")
    ModelAndView SearchPage(){
        ModelAndView mv = new ModelAndView("search");
        return mv;
    }
}
