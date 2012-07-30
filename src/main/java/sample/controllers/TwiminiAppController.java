package sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import sample.service.TwiminiStore;


/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 18/7/12
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/twimini") //TODO: need to remove this prefix
public class TwiminiAppController {
    TwiminiStore tStore;

    @Autowired
    public TwiminiAppController(TwiminiStore tStore){
        this.tStore = tStore;
    }

    @RequestMapping("/index")
    ModelAndView indexPage(){
        ModelAndView mv = new ModelAndView("index");
        return mv;
    }

    @RequestMapping("/home")
    ModelAndView HomePage(){
        ModelAndView mv = new ModelAndView("home");
        return mv;
    }

    @RequestMapping("/profile")
    ModelAndView ProfilePage(){
        ModelAndView mv = new ModelAndView("profile");
        mv.addObject("username", "UserName to be added");
        return mv;
    }
}
