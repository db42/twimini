package sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sample.service.UserStore;
import sample.utilities.MD5Encoder;

import java.util.Hashtable;

@Controller
public class UserController {
    UserStore userStore;
    MD5Encoder md5Encoder;

    static String baseImageUrl = "http://www.gravatar.com/avatar/";

    @Autowired
    public UserController(UserStore UserStore, MD5Encoder md5Encoder) {
        this.userStore = UserStore;
        this.md5Encoder = md5Encoder;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> registerJson(@RequestParam String username, @RequestParam String email, @RequestParam String password){
        String image_url = baseImageUrl.concat(md5Encoder.encodeString(email)); //generate gravatar image url
        Hashtable hs = userStore.addUser(username, email, password, image_url);
        return hs;
    }

    @RequestMapping(value = "/update_password", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> updateUserPassword(@RequestParam String userID,
                                         @RequestParam(required = false) String old_password,
                                         @RequestParam(required = false) String new_password){

        Hashtable hs = userStore.updateUserPassword(userID, old_password, new_password);
        return hs;
    }

    @RequestMapping(value = "/update_account", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> updateUserAccount(@RequestParam String userID,
                                                 @RequestParam(required = false) String username,
                                                 @RequestParam(required = false) String email){

        Hashtable hs = userStore.updateUserAccount(userID, username, email);
        return hs;
    }

    @RequestMapping(value = "/update_profile", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> updateUserProfile(@RequestParam String userID,
                                                 @RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String description){

        Hashtable hs = userStore.updateUserProfile(userID, name, description);
        return hs;
    }
}