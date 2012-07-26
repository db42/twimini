package sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sample.model.Post;
import sample.model.User;
import sample.service.TwiminiStore;

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
public class TwiminiRestContoller {
    TwiminiStore tStore;

    @Autowired
    public TwiminiRestContoller(TwiminiStore tStore){
        this.tStore = tStore;
    }

    @RequestMapping(value = "/users/{userID}/posts", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    Hashtable<String, String> newPostJson(@PathVariable String userID, @RequestParam String post, HttpServletResponse response){
        Hashtable hs = new Hashtable<String, String>();
        Post p = tStore.addPost(userID, post);
        response.setHeader("Location","/posts/"+p.getId());

        hs.put("user_id", p.getUser_id());
        hs.put("id", p.getId());
        hs.put("post" ,p.getPost());
        hs.put("timestamp", p.getTimestamp());
        return hs;
        /*hs.put("status","success");
        return hs;*/
    }

    @RequestMapping(value = "/users/{userID}/followers", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> newFollowerJson(@PathVariable String userID,@RequestParam int following){
        Hashtable hs = new Hashtable<String, String>();
        tStore.addFollower(following, userID);
        hs.put("status","success");
        return hs;
    }

    @RequestMapping(value = "/users/{userID}/posts", method = RequestMethod.GET)
    @ResponseBody
    List<Post> getPostsJson(@PathVariable String userID){
        List<Post> posts = tStore.getPosts(userID);
        if (posts.isEmpty())
            throw new ResourceNotFoundException();
        else
            return posts;

    }

    @RequestMapping(value = "/posts/{postID}", method = RequestMethod.GET)
    @ResponseBody
    Post getPostJson(@PathVariable String postID){
        Post post = tStore.getPost(postID);
        if (post == null)
            throw new ResourceNotFoundException();
        else
            return post;

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

    @RequestMapping(value = "/users/{userID}/posts/feed", method = RequestMethod.GET)
    @ResponseBody
    List<Post> getSubscribedPostsJson(@PathVariable String userID){
        return tStore.getSubscribedPosts(userID);
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
