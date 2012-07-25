package sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sample.model.Post;
import sample.model.User;
import sample.service.TwiminiStore;

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
public class TwiminiRestContoller {
    TwiminiStore tStore;

    @Autowired
    public TwiminiRestContoller(TwiminiStore tStore){
        this.tStore = tStore;
    }

    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> newPostJson(@RequestParam String post){
        Hashtable hs = new Hashtable<String, String>();
        Post p = tStore.addPost(post);
        hs.put("user_id", p.getUser_id());
        hs.put("id", p.getId());
        hs.put("post" ,p.getPost());
        hs.put("timestamp", p.getTimestamp());
        return hs;
        /*hs.put("status","success");
        return hs;*/
    }

    @RequestMapping(value = "/followers", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> newFollowerJson(@RequestParam int following){
        Hashtable hs = new Hashtable<String, String>();
        tStore.addFollower(following);
        hs.put("status","success");
        return hs;
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    @ResponseBody
    List<Post> getPostsJson(){
        return tStore.getPosts();
    }

    @RequestMapping(value = "/followers", method = RequestMethod.GET)
    @ResponseBody
    List<User> getFollowersJson(){
        return tStore.getFollowers();
    }

    @RequestMapping(value = "/followings", method = RequestMethod.GET)
    @ResponseBody
    List<User> getFollowings(){
        return tStore.getFollowings();
    }

    @RequestMapping(value = "/posts/feed", method = RequestMethod.GET)
    @ResponseBody
    List<Post> getSubscribedPostsJson(){
        return tStore.getSubscribedPosts();
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
