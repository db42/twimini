package sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import sample.model.Post;
import sample.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created on IntelliJ IDEA.
 * User: dushyant
 * Date: 18/7/12
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class Store {
    SimpleJdbcTemplate jdbcTemplate;
    private final ThreadLocal<Long> userID;

    @Autowired
    public Store(SimpleJdbcTemplate jdbcTemplate, @Qualifier("userID") ThreadLocal<Long> userID){
        this.jdbcTemplate = jdbcTemplate;
        this.userID = userID;
    }

    public Post addPost(String userID, String post) {
        PostRowMapper postRowMapper = new PostRowMapper();
        jdbcTemplate.update("INSERT INTO posts (user_id, post) VALUES (?,?)", userID, post);
        return (Post) jdbcTemplate.queryForObject("SELECT * FROM posts INNER JOIN users on posts.user_id=users.id WHERE user_id=" + userID +
                " AND post=\""+post+"\" order by time desc limit 1", postRowMapper);
    }

    public List<Post> getPosts(String userID, String since_id, String count){
        if (getUser(userID) == null)
            return null;

        PostRowMapper postRowMapper = new PostRowMapper();
        String query;
        if (count== null)
            count = "20";
        if (since_id == null)
            query = "SELECT * from posts INNER JOIN users on posts.user_id=users.id where user_id=" +userID + " ORDER BY posts.time DESC LIMIT "+ count;
        else
            query = "SELECT * from posts INNER JOIN users on posts.user_id=users.id where user_id=" +userID +" AND posts.id>"+since_id + " ORDER BY posts.time DESC LIMIT "+ count;

        List<Post> posts = (List< Post>) jdbcTemplate.query(query, postRowMapper);

        return posts;

   }
    //TODO: return status
    public void addFollower(int following, String userID) {
        jdbcTemplate.update("INSERT INTO followers (user_id, follower) VALUES (?,?)", following, userID);
    }

    public void removeFollower(int following, String userID) {
        jdbcTemplate.update("UPDATE followers SET unfollow_time = NOW() where user_id=? AND follower=?", following, userID);
    }


    public User addUser(String username, String email, String password) {
        //todo : make sure username and password are unique
        try {
            jdbcTemplate.update("INSERT INTO users (username, email, password) VALUES (?,?,?)",username, email, password);
            UserRowMapper userRowMapper = new UserRowMapper();
            User user = (User)jdbcTemplate.queryForObject("SELECT * from users where username=\"" + username + "\"", userRowMapper);
            return user;
        }
        catch (DuplicateKeyException e){
            return null;
        }
    }

    public User updateUser(String userID, String username, String email, String name, String description, String old_password, String new_password) {
        User user = this.getUserByUserID(userID, old_password);
        if (user!=null)
            jdbcTemplate.update("UPDATE users SET username=?, email=?, password=?, name=?, description=?",username, email, new_password, name, description);
        return user;
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

    public User getUser(String userID, String callerUserID){
        UserRowMapper userRowMapper = new UserRowMapper();
        FollowRowMapper followRowMapper = new FollowRowMapper();
        try{
//            User user = (User) jdbcTemplate.queryForObject("select * from users INNER JOIN followers on users.id=followers.user_id where users.id=" + userID + " AND followers.follower="+callerUserID, userRowMapper);
            User user = (User) jdbcTemplate.queryForObject("select * from users where id=" + userID, userRowMapper);
            try{
                Boolean follow = (Boolean) jdbcTemplate.queryForObject("select * from followers where user_id="+userID +" AND follower="+callerUserID, followRowMapper);
                user.setFollowed(follow);
            }
            catch (EmptyResultDataAccessException e){
                user.setFollowed(false);
            }
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
            return user;
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
            return user;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public User getUserByUsername(String username, String password) {
        UserRowMapper userRowMapper = new UserRowMapper();
        try{
            System.out.println("select * from users where username=\"" + username + "\" and password=\""+ password + "\"");
            User user = (User) jdbcTemplate.queryForObject("select * from users where username=\"" + username + "\" and password=\""+ password + "\"", userRowMapper);
            return user;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<User> getFollowers(String userID) {
        UserRowMapper userRowMapper = new UserRowMapper();
        List<User> followers = jdbcTemplate.query("select * from users where id in (select follower from followers where user_id="+ userID +" AND unfollow_time > NOW())", userRowMapper);
        return followers;
    }

    public List<User> getFollowings(String userID) {
        UserRowMapper userRowMapper = new UserRowMapper();
        List<User> followings = jdbcTemplate.query("select * from users where id in (select user_id from followers where follower="+ userID +" AND unfollow_time > NOW())", userRowMapper);
        return followings;
    }

    public List<Post> getSubscribedPosts(String userID, String since_id, String count) {
        PostRowMapper postRowMapper = new PostRowMapper();
        String query;
        if (count== null)
                count = "20";
        if (since_id == null)
             query = "select users.username, users.email, posts.id, posts.user_id, posts.post, posts.time from posts, followers, users  where posts.user_id = users.id AND followers.follower=" + userID +
                                                            " AND posts.user_id=followers.user_id AND posts.time<followers.unfollow_time ORDER BY posts.time DESC LIMIT "+ count;
        else
            query = "select users.username, users.email, posts.id, posts.user_id, posts.post, posts.time from posts, followers, users  where posts.id >"+since_id +" AND posts.user_id = users.id AND " +
                    "followers.follower=" + userID +" AND posts.user_id=followers.user_id AND posts.time<followers.unfollow_time ORDER BY posts.time DESC LIMIT " + count;
        System.out.println(query);
        List<Post> subscribedPosts = jdbcTemplate.query(query, postRowMapper);
        return subscribedPosts;
    }

    public Post getPost(String postID) {
        PostRowMapper postRowMapper = new PostRowMapper();
        try{
            Post post = (Post) jdbcTemplate.queryForObject("select * from posts INNER JOIN users on posts.user_id=users.id where posts.id=" + postID, postRowMapper);
            return post;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }
    public User getUser() {
        UserRowMapper userRowMapper = new UserRowMapper();
        try{
            User user = (User) jdbcTemplate.queryForObject("select * from users where id=" + String.valueOf(userID.get()), userRowMapper);
            return user;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public boolean addFollowing(String followee_id, String follower_id) {
        try{
            jdbcTemplate.update("INSERT INTO followers (user_id, follower) VALUES (?,?)", followee_id, follower_id);
            return true;
        }
        catch (DuplicateKeyException e){
            jdbcTemplate.update("UPDATE followers SET unfollow_time='2038-01-01 00:00:00' where user_id=? AND follower=?",followee_id ,follower_id);
            return true;
        }
    }

    public void deleteFollowing(String followee_id, String follower_id) {
            jdbcTemplate.update("UPDATE followers SET unfollow_time = NOW() where user_id=? AND follower=?",followee_id ,follower_id);
    }
}

class FollowRowMapper implements RowMapper {
    Date today = new Date();
    Timestamp timeStamp = new java.sql.Timestamp(today.getTime());

    @Override
    public Boolean mapRow(ResultSet resultSet, int i) throws SQLException {
        return (resultSet.getTimestamp("unfollow_time").after(timeStamp));
    }
}

class UserRowMapper implements RowMapper {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("email"));
        user.setCreated_at(resultSet.getTimestamp("created_at"));
        user.setDescription(resultSet.getString("description"));
        user.setName(resultSet.getString("name"));
        return user;
    }
}

class PostRowMapper implements RowMapper {

    @Override
    public Post mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User(resultSet.getInt("user_id"), resultSet.getString("username"), resultSet.getString("email"));
        Post post = new Post(resultSet.getInt("id"), resultSet.getInt("user_id"), resultSet.getString("post"), resultSet.getTimestamp("time"), user);
        return post;
    }
}
