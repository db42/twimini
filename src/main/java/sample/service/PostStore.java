package sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import sample.model.Post;
import sample.model.PostRowMapper;
import sample.utilities.MD5Encoder;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 14/8/12
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class PostStore {
    SimpleJdbcTemplate jdbcTemplate;
    MD5Encoder md5Encoder;
    UserStore userStore;
    String defaultPostCount = "20";

    @Autowired
    public PostStore(SimpleJdbcTemplate jdbcTemplate, MD5Encoder md5Encoder, UserStore userStore){
        this.jdbcTemplate = jdbcTemplate;
        this.md5Encoder = md5Encoder;
        this.userStore = userStore;
    }

    public Post addPost(String userID, String post, String postID, String authorID) {
        PostRowMapper postRowMapper = new PostRowMapper();
        if (postID == null)
            jdbcTemplate.update("INSERT INTO posts (user_id, post) VALUES (?,?)", userID, post);
        else
            try{
                jdbcTemplate.update("INSERT INTO posts (user_id, post, rtwt_id, author_id) VALUES (?,?,?,?)", userID, post, postID, authorID);
            } catch (DuplicateKeyException e){
                return null;
            }

        jdbcTemplate.update("UPDATE users SET num_tweets=num_tweets+1 where id=?", userID);

        return (Post) jdbcTemplate.queryForObject("SELECT * FROM posts WHERE user_id=" + userID +
                " AND post=\""+post+"\" order by id desc limit 1", postRowMapper);
    }

    public Post rePost(String userID, String postID) {
        Post post = getPost(postID);
        return addPost(userID, post.getPost(), postID, Integer.toString(post.getUser_id()));
    }

    public Post getPost(String postID) {
        PostRowMapper postRowMapper = new PostRowMapper();
        try{
            return (Post) jdbcTemplate.queryForObject("select * from posts where posts.id=" + postID, postRowMapper);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Post> getPosts(String userID, String since_id, String count, String max_id){
        if (!userStore.validateUserById(userID))
            return null;

        PostRowMapper postRowMapper = new PostRowMapper();
        String query;
        if (count== null)
            count = defaultPostCount;
        if (since_id == null && max_id == null)
            query = "SELECT * from posts where user_id=" +userID + " ORDER BY posts.id DESC LIMIT "+ count;
        else if (max_id == null)
            query = "SELECT * from posts where user_id=" +userID +" AND posts.id>"+since_id + " ORDER BY posts.id DESC LIMIT "+ count;
        else
            query = "SELECT * from posts where user_id=" +userID +" AND posts.id<"+max_id + " ORDER BY posts.id DESC LIMIT "+ count;

        return (List< Post>) jdbcTemplate.query(query, postRowMapper);
    }

    public List<Post> getSubscribedPosts(String userID, String since_id, String count, String max_id) {
        PostRowMapper postRowMapper = new PostRowMapper();
        String query;
        if (count== null)
            count = defaultPostCount;

        if (since_id == null && max_id == null)
            query = "select posts.id, posts.user_id, posts.post, posts.time, posts.rtwt_id, posts.author_id from followers INNER JOIN posts ON followers.user_id=posts.user_id" +
                    " WHERE followers.follower=" + userID + " AND posts.time<followers.unfollow_time ORDER BY posts.id DESC LIMIT "+ count;
        else if (max_id == null)
            query = "select posts.id, posts.user_id, posts.post, posts.time, posts.rtwt_id, posts.author_id from followers INNER JOIN posts ON followers.user_id=posts.user_id" +
                    " WHERE followers.follower=" + userID + " AND posts.time<followers.unfollow_time AND posts.id >" + since_id +" ORDER BY posts.id DESC LIMIT "+ count;
        else
            query = "select posts.id, posts.user_id, posts.post, posts.time, posts.rtwt_id, posts.author_id from followers INNER JOIN posts ON followers.user_id=posts.user_id" +
                    " WHERE followers.follower=" + userID + " AND posts.time<followers.unfollow_time AND posts.id <" + max_id +" ORDER BY posts.id DESC LIMIT "+ count;

        List<Post> subscribedPosts = jdbcTemplate.query(query, postRowMapper);
        for (Post post: subscribedPosts){
            try{
                query = "select posts.id from posts where posts.rtwt_id="+post.getId()+" AND posts.user_id="+userID;
                jdbcTemplate.queryForInt(query);
                post.setReposted(true);
            }
            catch (EmptyResultDataAccessException e){
            }
        }
        return subscribedPosts;
    }
}
