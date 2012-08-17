package sample.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sample.controllers.api.exceptions.ResourceNotFoundException;
import sample.model.User;
import sample.service.ApiExceptionResolver;
import sample.service.UserStore;
import sample.utilities.MD5Encoder;

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
public class UserContoller extends ApiExceptionResolver{
    UserStore userStore;
    RestAuthLayer authLayer;
    MD5Encoder md5Encoder;
    static String baseGravatarImageUrl = "http://www.gravatar.com/avatar/";

    @Autowired
    public UserContoller(UserStore userStore, RestAuthLayer authLayer, MD5Encoder md5Encoder){
        this.userStore = userStore;
        this.authLayer = authLayer;
        this.md5Encoder = md5Encoder;
    }

    @RequestMapping(value = "/users/{userID}/search", method = RequestMethod.GET)
    @ResponseBody
    List<User> getSearchJson(@RequestParam String q, @PathVariable String userID){
        return userStore.searchForUsers(q, userID);
    }

    @RequestMapping(value = "/users/{userID}", method = RequestMethod.GET)
    @ResponseBody
    User getUserJson(@PathVariable String userID,
                     @RequestParam(required = false) String callerUserID,
                     HttpServletResponse response){
        User user = userStore.getUser(userID, callerUserID);
        if (user == null)
            throw new ResourceNotFoundException();
        else {
            response.setHeader("Cache-Control", "public, max-age=36000"); // HTTP 1.1
            return user;
        }
    }

    @RequestMapping(value = "/users/{userID}/followers", method = RequestMethod.GET)
    @ResponseBody
    List<User> getFollowersJson(@PathVariable String userID,
                                @RequestParam(required = false) String count,
                                @RequestParam(required = false) String max_id,
                                @RequestParam(required = false) String callerUserID){
        return userStore.getFollowers(userID, count, max_id, callerUserID);
    }

    @RequestMapping(value = "/users/{userID}/followers", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> newFollowerJson(@PathVariable String userID,@RequestParam int following){
        Hashtable hs = new Hashtable<String, String>();
        userStore.addFollowing(String.valueOf(following), userID);
        hs.put("status","success");
        return hs;
    }

    @RequestMapping(value = "/users/{userID}/followings", method = RequestMethod.GET)
    @ResponseBody
    List<User> getFollowings(@PathVariable String userID, @RequestParam(required = false) String count, @RequestParam(required = false) String max_id, @RequestParam(required = false) String callerUserID){
        return userStore.getFollowings(userID, count, max_id, callerUserID);
    }

    @RequestMapping(value = "/users/{follower_id}/followings/{followee_id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    Hashtable<String, String> addFollowings(@PathVariable String follower_id, @PathVariable String followee_id, HttpServletRequest request){
        authLayer.isAuthorised(follower_id, request);

        Hashtable hs = new Hashtable<String, String>();
        userStore.addFollowing(followee_id, follower_id);
        hs.put("status","success");
        return hs;
    }

    @RequestMapping(value = "/users/{follower_id}/followings/{followee_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    Hashtable<String, String> deleteFollowings(@PathVariable String follower_id, @PathVariable String followee_id, HttpServletRequest request){
        authLayer.isAuthorised(follower_id, request);

        Hashtable hs = new Hashtable<String, String>();
        userStore.deleteFollowing(followee_id, follower_id);
        hs.put("status","success");
        return hs;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> registerJson(@RequestParam String username,
                                           @RequestParam String email,
                                           @RequestParam String password){
        String image_url = baseGravatarImageUrl.concat(md5Encoder.encodeString(email));
        return userStore.addUser(username, email, password, image_url);
    }

    @RequestMapping(value = "/update_password", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> updateUserPassword(@RequestParam String userID,
                                                 @RequestParam(required = false) String old_password,
                                                 @RequestParam(required = false) String new_password,
                                                 HttpServletRequest request){

        authLayer.isAuthorised(userID, request);
        return userStore.updateUserPassword(userID, old_password, new_password);
    }

    @RequestMapping(value = "/update_account", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> updateUserAccount(@RequestParam String userID,
                                                @RequestParam(required = false) String username,
                                                @RequestParam(required = false) String email,
                                                HttpServletRequest request){

        authLayer.isAuthorised(userID, request);
        return userStore.updateUserAccount(userID, username, email);
    }

    @RequestMapping(value = "/update_profile", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> updateUserProfile(@RequestParam String userID,
                                                @RequestParam(required = false) String name,
                                                @RequestParam(required = false) String description,
                                                HttpServletRequest request){

        authLayer.isAuthorised(userID, request);
        return userStore.updateUserProfile(userID, name, description);
    }

}
