package sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import sample.model.Post;
import sample.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 18/7/12
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class TwiminiStore {
    SimpleJdbcTemplate jdbcTemplate;
    private final ThreadLocal<Long> userID;

    @Autowired
    public TwiminiStore(SimpleJdbcTemplate jdbcTemplate, @Qualifier("userID") ThreadLocal<Long> userID){
        this.jdbcTemplate = jdbcTemplate;
        this.userID = userID;
    }

    public Post addPost(String userID, String post) {
        PostRowMapper postRowMapper = new PostRowMapper();
        jdbcTemplate.update("INSERT INTO posts (user_id, post) VALUES (?,?)", userID, post);
        return (Post) jdbcTemplate.queryForObject("SELECT * FROM posts WHERE user_id=" + userID +
                " AND post=\""+post+"\" order by time desc limit 1", postRowMapper);
    }

    public List<Post> getPosts(String userID){
        PostRowMapper postRowMapper = new PostRowMapper();
        List<Post> posts = (List< Post>) jdbcTemplate.query("SELECT * from posts where user_id=" + userID,
                postRowMapper);

        return posts;

   }
    //TODO: return status
    public void addFollower(int following, String userID) {
        jdbcTemplate.update("INSERT INTO followers (user_id, follower) VALUES (?,?)", following, userID);
    }

    public void addUser(String name, String email, String password) {
        jdbcTemplate.update("INSERT INTO users (username, email, password) VALUES (?,?,?)", name, email, password);
//        jdbcTemplate.update("INSERT INTO users (username, email, password) VALUES (?,?,SHA1(?))", name, email, password);
    }

    public User getUser(String userID){
        UserRowMapper userRowMapper = new UserRowMapper();
        try{
            User user = (User) jdbcTemplate.queryForObject("select * from users where id=" + userID, userRowMapper);
            return user;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public User getUserByUserID(String userID, String password) {
        UserRowMapper userRowMapper = new UserRowMapper();
        try{
            User user = (User) jdbcTemplate.queryForObject("select * from users where id=" + userID + " and password=\""+ password + "\"", userRowMapper);
            return user;  //To change body of created methods use File | Settings | File Templates.
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }
    public User getUserByEmail(String email, String password) {
        UserRowMapper userRowMapper = new UserRowMapper();
        try{
            System.out.println("select * from users where email=\"" + email + "\" and password=\""+ password + "\"");
            User user = (User) jdbcTemplate.queryForObject("select * from users where email=\"" + email + "\" and password=\""+ password + "\"", userRowMapper);
            return user;  //To change body of created methods use File | Settings | File Templates.
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<User> getFollowers(String userID) {
        UserRowMapper userRowMapper = new UserRowMapper();
        List<User> followers = jdbcTemplate.query("select * from users where id in (select follower from followers where user_id="+ userID +")", userRowMapper);
        return followers;
    }

    public List<User> getFollowings(String userID) {
        UserRowMapper userRowMapper = new UserRowMapper();
        List<User> followings = jdbcTemplate.query("select * from users where id in (select user_id from followers where follower="+ userID +")", userRowMapper);
        return followings;
    }

    public List<Post> getSubscribedPosts(String userID) {
        PostRowMapper postRowMapper = new PostRowMapper();
        List<Post> subscribedPosts = jdbcTemplate.query("select * from posts where user_id in (select user_id from followers where follower=" + userID +")", postRowMapper);
        return subscribedPosts;
    }

    public Post getPost(String postID) {
        PostRowMapper postRowMapper = new PostRowMapper();
        try{
            Post post = (Post) jdbcTemplate.queryForObject("select * from posts where id=" + postID, postRowMapper);
            return post;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }
}

class UserRowMapper implements RowMapper {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("password"));
        return user;
    }
}

class PostRowMapper implements RowMapper {

    @Override
    public Post mapRow(ResultSet resultSet, int i) throws SQLException {
        Post post = new Post(resultSet.getInt("id"), resultSet.getInt("user_id"), resultSet.getString("post"), resultSet.getTimestamp("time"));
        return post;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
