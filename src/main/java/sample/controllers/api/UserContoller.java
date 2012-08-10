package sample.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sample.model.User;
import sample.service.Store;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class UserContoller {
    Store tStore;
    RestAuthLayer authLayer;

    @Autowired
    public UserContoller(Store tStore, RestAuthLayer authLayer){
        this.tStore = tStore;
        this.authLayer = authLayer;
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
    List<User> getFollowersJson(@PathVariable String userID, @RequestParam(required = false) String count, @RequestParam(required = false) String max_id){
        return tStore.getFollowers(userID, count, max_id);
    }

    @RequestMapping(value = "/users/{follower_id}/followings/{followee_id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    Hashtable<String, String> addFollowings(@PathVariable String follower_id, @PathVariable String followee_id, HttpServletRequest request){
        authLayer.isAuthorised(follower_id, request);

        Hashtable hs = new Hashtable<String, String>();
        tStore.addFollowing(followee_id, follower_id);
        hs.put("status","success");
        return hs;
    }

    @RequestMapping(value = "/users/{follower_id}/followings/{followee_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    Hashtable<String, String> deleteFollowings(@PathVariable String follower_id, @PathVariable String followee_id, HttpServletRequest request){
        authLayer.isAuthorised(follower_id, request);

        Hashtable hs = new Hashtable<String, String>();
        tStore.deleteFollowing(followee_id, follower_id);
        hs.put("status","success");
        return hs;
    }

    @RequestMapping(value = "/users/{userID}/followings", method = RequestMethod.GET)
    @ResponseBody
    List<User> getFollowings(@PathVariable String userID, @RequestParam(required = false) String count, @RequestParam(required = false) String max_id){
        return tStore.getFollowings(userID, count, max_id);
    }

    @RequestMapping(value = "/users/{userID}", method = RequestMethod.GET)
    @ResponseBody
    User getUserJson(@PathVariable String userID, @RequestParam(required = false) String callerUserID, HttpServletResponse response){
        User user = tStore.getUser(userID, callerUserID);
        if (user == null)
            throw new ResourceNotFoundException();
        else {
            response.setHeader("Cache-Control", "public, max-age=36000"); // HTTP 1.1
            return user;
        }
    }

    @RequestMapping(value = "/users/{userID}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    Hashtable<String, String> updateUser(@PathVariable String userID,
                                         @RequestParam(required = false) String username,
                                         @RequestParam(required = false) String email,
                                         @RequestParam(required = false) String name,
                                         @RequestParam(required = false) String description,
                                         @RequestParam(required = false) String old_password,
                                         @RequestParam(required = false) String new_password)
    {
        User user = tStore.updateUser(userID,username, email, name, description, old_password, new_password);
        Hashtable hs = new Hashtable<String, String>();
        if (user == null)
            hs.put("status", "failed");
        else
            hs.put("status","success");
        return hs;
    }
}
