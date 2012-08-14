package sample.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sample.model.Post;
import sample.service.PostStore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 30/7/12
 * Time: 8:31 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class PostController {
    PostStore tPostStore;
    RestAuthLayer authLayer;

    @Autowired
    public PostController(PostStore tPostStore, RestAuthLayer authLayer){
        this.tPostStore = tPostStore;
        this.authLayer = authLayer;
    }

    @RequestMapping(value = "/users/{userID}/posts", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    Post newPostJson(@PathVariable String userID, @RequestParam String post, HttpServletRequest request, HttpServletResponse response) throws IOException {
        authLayer.isAuthorised(userID, request);

        Post p = tPostStore.addPost(userID, post, null, null);
        response.setHeader("Location","/posts/"+p.getId());

        return p;
    }

    @RequestMapping(value = "/users/{userID}/posts/repost/{postID}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    Post rePostJson(@PathVariable String userID, @PathVariable String postID, HttpServletRequest request, HttpServletResponse response) throws IOException {
        authLayer.isAuthorised(userID, request);

        Post p = tPostStore.rePost(userID, postID);
        response.setHeader("Location","/posts/"+p.getId());

        return p;
    }

   @RequestMapping(value = "/users/{userID}/posts", method = RequestMethod.GET)
    @ResponseBody
    List<Post> getPostsJson(@PathVariable String userID, @RequestParam(required = false) String since_id, @RequestParam(required = false) String count, @RequestParam(required = false) String max_id){
        List<Post> posts = tPostStore.getPosts(userID, since_id, count, max_id);
        if (posts == null)
            throw new ResourceNotFoundException();
        else
            return posts;

    }

    @RequestMapping(value = "/posts/{postID}", method = RequestMethod.GET)
    @ResponseBody
    Post getPostJson(@PathVariable String postID){
        Post post = tPostStore.getPost(postID);
        if (post == null)
            throw new ResourceNotFoundException();
        else
            return post;

    }

    @RequestMapping(value = "/users/{userID}/posts/feed", method = RequestMethod.GET)
    @ResponseBody
    List<Post> getSubscribedPostsJson(@PathVariable String userID, @RequestParam(required = false) String since_id, @RequestParam(required = false) String count, @RequestParam(required = false) String max_id, HttpServletRequest request){
        authLayer.isAuthorised(userID, request);

        return tPostStore.getSubscribedPosts(userID, since_id, count, max_id);
    }
}
