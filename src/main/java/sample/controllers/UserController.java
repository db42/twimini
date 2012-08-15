package sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sample.model.User;
import sample.service.UserStore;

import java.util.Hashtable;

@Controller
public class UserController {
    UserStore tUserStore;

    @Autowired
    public UserController(UserStore tUserStore) {this.tUserStore = tUserStore;}

   @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> registerJson(@RequestParam String name, @RequestParam String email, @RequestParam String password){
        User user = tUserStore.addUser(name, email, password);
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
        User user = tUserStore.updateUserPassword(userID, old_password, new_password);
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
        User user = tUserStore.updateUserAccount(userID, username, email);
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
        boolean status = tUserStore.updateUserProfile(userID, name, description);
        Hashtable hs = new Hashtable<String, String>();
        if (status)
            hs.put("status","success");
        else
            hs.put("status", "failed");
        return hs;
    }

}