package sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sample.model.User;
import sample.service.Store;

import javax.servlet.http.HttpSession;
import java.util.Hashtable;

@Controller
public class UserController {
    Store tStore;

    @Autowired
    public UserController(Store tStore) {this.tStore = tStore;}

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String loginForm() {
        return "index";
    }

   @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> registerJson(@RequestParam String name, @RequestParam String email, @RequestParam String password){
        User user = tStore.addUser(name, email, password);
        Hashtable hs = new Hashtable<String, String>();
        if (user == null)
            hs.put("status", "failed");
        else
            hs.put("status","success");
        return hs;
    }

    @RequestMapping(value = "/update_password", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> updateUserPassword(@RequestParam String userID,
                                         @RequestParam(required = false) String old_password,
                                         @RequestParam(required = false) String new_password)
    {
        User user = tStore.updateUserPassword(userID, old_password, new_password);
        Hashtable hs = new Hashtable<String, String>();
        if (user == null)
            hs.put("status", "failed");
        else
            hs.put("status","success");
        return hs;
    }

    @RequestMapping(value = "/update_account", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> updateUserAccount(@RequestParam String userID,
                                                 @RequestParam(required = false) String username,
                                                 @RequestParam(required = false) String email)
    {
        User user = tStore.updateUserAccount(userID, username, email);
        Hashtable hs = new Hashtable<String, String>();
        if (user == null)
            hs.put("status", "failed");
        else
            hs.put("status","success");
        return hs;
    }

    @RequestMapping(value = "/update_profile", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> updateUserProfile(@RequestParam String userID,
                                                 @RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String description)
    {
        User user = tStore.updateUserProfile(userID, name, description);
        Hashtable hs = new Hashtable<String, String>();
        if (user == null)
            hs.put("status", "failed");
        else
            hs.put("status","success");
        return hs;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> login(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              HttpSession session) {

        Hashtable hs = new Hashtable<String, String>();
        long userID = 0;

        //user can login with email or username
        User user;
        if (email.contains("@"))
            user = tStore.getUserByEmail(email, password);
        else
            user = tStore.getUserByUsername(email, password);

        // add md5 function for password check
        if (user == null) {
            hs.put("status", "failed");
            return hs;
        }
        userID = (Integer) user.getId();

        session.setAttribute("email", email);
        session.setAttribute("userID", userID);

        hs.put("status", "success");
        hs.put("userID", user.getId());
        hs.put("password", password);
        return hs;
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}