package sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sample.model.User;
import sample.service.TwiminiStore;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    TwiminiStore tStore;

    @Autowired
    public UserController(TwiminiStore tStore) {this.tStore = tStore;}

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String loginForm() {
        return "index";
    }

   @RequestMapping(value = "/register", method = RequestMethod.POST)
   public ModelAndView registerJson(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String password,
                             HttpSession session){
       tStore.addUser(name, email, password);
       return login(email, password, session);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              HttpSession session) {

        ModelAndView mv = new ModelAndView("/index");
        long userID;
        try {
            User user = tStore.getUser(email);

            // add md5 function for password check
            if (!user.getPassword().equals(password)) {
                mv.addObject("message", "Invalid password.");
                return mv;
            }
            userID = (Integer) user.getId();
            session.setAttribute("email", email);
            session.setAttribute("userID", userID);
        } catch (EmptyResultDataAccessException e) {
            mv.addObject("message", "No such user exists!");
        }
        mv.setViewName("redirect:twimini/profile");
        return mv;
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}