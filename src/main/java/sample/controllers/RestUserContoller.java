package sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sample.model.Post;
import sample.model.User;
import sample.service.TwiminiStore;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 23/7/12
 * Time: 4:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class RestUserContoller {
    TwiminiStore tStore;

    @Autowired
    public RestUserContoller(TwiminiStore tStore){
        this.tStore = tStore;
    }

    @RequestMapping(value = "/users/{userID}/followers", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> newFollowerJson(@PathVariable String userID,@RequestParam int following){
        Hashtable hs = new Hashtable<String, String>();
        tStore.addFollower(following, userID);
        hs.put("status","success");
        return hs;
    }

    @RequestMapping(value = "/users/{userID}/followers", method = RequestMethod.GET)
    @ResponseBody
    List<User> getFollowersJson(@PathVariable String userID){
        return tStore.getFollowers(userID);
    }

    @RequestMapping(value = "/users/{userID}/followings", method = RequestMethod.GET)
    @ResponseBody
    List<User> getFollowings(@PathVariable String userID){
        return tStore.getFollowings(userID);
    }

    @RequestMapping(value = "/users/{userID}", method = RequestMethod.GET)
    @ResponseBody
    User getUserJson(@PathVariable String userID){
        User user = tStore.getUser(userID);
        if (user == null)
            throw new ResourceNotFoundException();
        else
            return user;
    }
}
