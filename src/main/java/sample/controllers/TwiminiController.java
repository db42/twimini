package sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sample.service.TwiminiStore;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 18/7/12
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class TwiminiController {
    TwiminiStore tStore;
    int userID;

    @Autowired
    public TwiminiController(TwiminiStore tStore){
        this.tStore = tStore;
    }

    @RequestMapping(value = "/newpost.json", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> newPostJson(@RequestParam String post){
    // post, userID
        Hashtable hs = new Hashtable<String, String>();
        tStore.addPost(userID, post);
        hs.put("status","Added Successfully");
        return hs;
    }


    @RequestMapping(value = "/newfollower.json", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> newFollowerJson(@RequestParam int following){
        Hashtable hs = new Hashtable<String, String>();
        tStore.addFollower(following, userID);
        hs.put("status","Added Successfully");
        return hs;
    }

    @RequestMapping(value = "/posts.json", method = RequestMethod.GET)
    @ResponseBody
    List<Map<String, String>> getPostsJson(){
        List<Map<String, Object>> posts =  tStore.getPosts(userID);

    }



}
