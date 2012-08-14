package sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import sample.model.*;
import sample.utilities.MD5Encoder;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

/**
 * Created on IntelliJ IDEA.
 * User: dushyant
 * Date: 18/7/12
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class UserStore {
    SimpleJdbcTemplate jdbcTemplate;
    MD5Encoder md5Encoder;

    @Autowired
    public UserStore(SimpleJdbcTemplate jdbcTemplate, MD5Encoder md5Encoder){
        this.jdbcTemplate = jdbcTemplate;
        this.md5Encoder = md5Encoder;
    }

    //TODO: return status
    public void addFollower(int following, String userID) {
        jdbcTemplate.update("INSERT INTO followers (user_id, follower) VALUES (?,?)", following, userID);
        jdbcTemplate.update("UPDATE users SET num_followings=num_followings+1 where id=?", userID);
        jdbcTemplate.update("UPDATE users SET num_followers=num_followers+1 where id=?", following);
    }

    public void removeFollower(int following, String userID) {
        jdbcTemplate.update("UPDATE followers SET unfollow_time = NOW() where user_id=? AND follower=?", following, userID);
        jdbcTemplate.update("UPDATE users SET num_followers=num_followers-1 where id=?", following);
        jdbcTemplate.update("UPDATE users SET num_followings=num_followings-1 where id=?", userID);
    }


    public User addUser(String username, String email, String password) {
        //todo : make sure username and password are unique
        try {
            String baseUrl = "http://www.gravatar.com/avatar/";
            String image_url = baseUrl.concat(md5Encoder.encodeString(email));
            jdbcTemplate.update("INSERT INTO users (username, email, password, image_url) VALUES (?,?,?,?)",username, email, password, image_url);
            UserRowMapper userRowMapper = new UserRowMapper(md5Encoder);
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

    public User updateUserPassword(String userID, String old_password, String new_password) {
        User user = this.getUserByUserID(userID, old_password);
        if (user!=null)
            jdbcTemplate.update("UPDATE users SET password=? where id=?",new_password, userID);
        return user;
    }

    public User updateUserAccount(String userID, String username, String email) {
        User user = this.getUser(userID);
        try{
            jdbcTemplate.update("UPDATE users SET username=?, email=? where id=?",username, email, userID);
            return user;
        }
        catch (DuplicateKeyException e){
            return null;
        }
    }

    public boolean updateUserProfile(String userID, String name, String description) {
        jdbcTemplate.update("UPDATE users SET name=?, description=? where id=?", name, description, userID);
        return true;
    }

    public User getUser(String userID){
        UserRowMapper userRowMapper = new UserRowMapper(md5Encoder);
        try{
            User user = (User) jdbcTemplate.queryForObject("select * from users where id=" + userID, userRowMapper);
            return user;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public User getUser(String userID, String callerUserID){
        UserRowMapper userRowMapper = new UserRowMapper(md5Encoder);
        FollowRowMapper followRowMapper = new FollowRowMapper();
        try{
//            User user = (User) jdbcTemplate.queryForObject("select * from users INNER JOIN followers on users.id=followers.user_id where users.id=" + userID + " AND followers.follower="+callerUserID, userRowMapper);
            User user = (User) jdbcTemplate.queryForObject("select * from users where id=" + userID, userRowMapper);
            user.setFollowed(false);

            if (callerUserID != null) {
                try{
                    Boolean follow = (Boolean) jdbcTemplate.queryForObject("select * from followers where user_id="+userID +" AND follower="+callerUserID, followRowMapper);
                    user.setFollowed(follow);
                }
                catch (EmptyResultDataAccessException e){
                }
            }
            return user;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public User getUserByUserID(String userID, String password) {
        UserRowMapper userRowMapper = new UserRowMapper(md5Encoder);
        try{
            User user = (User) jdbcTemplate.queryForObject("select * from users where id=" + userID + " and password=\""+ password + "\"", userRowMapper);
            return user;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }
    public Hashtable<String, String> getUserByEmail(String email, String password) {
        UserRowMapper userRowMapper = new UserRowMapper(md5Encoder);
        try{
            Hashtable<String, String> hs = new Hashtable<String, String>();
            System.out.println("select * from users where email=\"" + email + "\" and password=\""+ password + "\"");
            User user = (User) jdbcTemplate.queryForObject("select * from users where email=\"" + email + "\" and password=\""+ password + "\"", userRowMapper);

            String auth_key = db_gen_auth_key(Integer.toString(user.getId()));
            hs.put("userID", Integer.toString(user.getId()));
            hs.put("auth_key", auth_key);
            return hs;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public Hashtable<String, String> getUserByUsername(String username, String password) {
        UserRowMapper userRowMapper = new UserRowMapper(md5Encoder);
        try{
            Hashtable<String, String> hs = new Hashtable<String, String>();
            System.out.println("select * from users where username=\"" + username + "\" and password=\""+ password + "\"");
            User user = (User) jdbcTemplate.queryForObject("select * from users where username=\"" + username + "\" and password=\""+ password + "\"", userRowMapper);

            String auth_key = db_gen_auth_key(Integer.toString(user.getId()));
            hs.put("userID", Integer.toString(user.getId()));
            hs.put("auth_key", auth_key);
            return hs;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public User getUserByAuthKey(String userID, String auth_key) {
        UserRowMapper userRowMapper = new UserRowMapper(md5Encoder);
        try{
            System.out.println("authorisation key from user: " + auth_key);
            User user = (User) jdbcTemplate.queryForObject("select * from users where id=" + userID + " and auth_key=\""+ auth_key + "\"", userRowMapper);
            return user;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    private String db_gen_auth_key(String userID) {
        long current_time_value = new Date().getTime();
        Random random_number = new Random();
        String auth_key = md5Encoder.encodeString(Long.toString(current_time_value).concat(random_number.toString()));
        jdbcTemplate.update("UPDATE users SET auth_key=? where id=?",auth_key, userID);
        System.out.println("authorisation key generated by system: " + auth_key);
        return auth_key;
    }

    public void invalidateAuthKey(String userID) {
        jdbcTemplate.update("UPDATE users SET auth_key=? where id=?","", userID);
        System.out.println("authorisation key destroyed");
    }

    public List<User> getFollowers(String userID, String count, String max_id) {
        UserRowMapper userRowMapper = new UserRowMapper(md5Encoder);
        count = (count == null) ? "20" : count;
        String query;

        if (max_id == null)
            query = "select * from users where id in (select follower from followers where user_id="+ userID +" AND unfollow_time > NOW()) ORDER BY id DESC LIMIT "+count;
        else
            query = "select * from users where id in (select follower from followers where user_id="+ userID +" AND unfollow_time > NOW() AND follower<" + max_id+" ) ORDER BY id DESC LIMIT "+count;

        List<User> followers = jdbcTemplate.query(query, userRowMapper);
        for(User u:followers){
            try{
                FollowRowMapper followRowMapper = new FollowRowMapper();
                Boolean follow = (Boolean) jdbcTemplate.queryForObject("select * from followers where user_id="+u.getId()+" AND follower="+userID, followRowMapper);
                u.setFollowed(follow);
            }
            catch (EmptyResultDataAccessException e){
                u.setFollowed(false);
            }
        }
        return followers;
    }

    public List<User> getFollowings(String userID, String count, String max_id) {
        UserRowMapper userRowMapper = new UserRowMapper(md5Encoder);
        count = (count == null) ? "20" : count;
        String query;

        if (max_id == null)
            query = "select * from users where id in (select user_id from followers where follower="+ userID +" AND unfollow_time > NOW()) ORDER BY id DESC LIMIT "+count;
        else
            query = "select * from users where id in (select user_id from followers where follower="+ userID +" AND unfollow_time > NOW() AND user_id<" + max_id+" ) ORDER BY id DESC LIMIT "+count;

        List<User> followings = jdbcTemplate.query(query, userRowMapper);
        for(User u:followings){
            u.setFollowed(true);
        }
        return followings;
    }



    public boolean addFollowing(String followee_id, String follower_id) {
        try{
            jdbcTemplate.update("INSERT INTO followers (user_id, follower) VALUES (?,?)", followee_id, follower_id);
            jdbcTemplate.update("UPDATE users SET num_followings=num_followings+1 where id=?", follower_id);
            jdbcTemplate.update("UPDATE users SET num_followers=num_followers+1 where id=?", followee_id);
            return true;
        }
        catch (DuplicateKeyException e){
            jdbcTemplate.update("UPDATE followers SET unfollow_time='2038-01-01 00:00:00' where user_id=? AND follower=?",followee_id ,follower_id);
            jdbcTemplate.update("UPDATE users SET num_followings=num_followings+1 where id=?", follower_id);
            jdbcTemplate.update("UPDATE users SET num_followers=num_followers+1 where id=?", followee_id);
            return true;
        }
    }

    public void deleteFollowing(String followee_id, String follower_id) {
        jdbcTemplate.update("UPDATE followers SET unfollow_time = NOW() where user_id=? AND follower=?",followee_id ,follower_id);
        jdbcTemplate.update("UPDATE users SET num_followings=num_followings-1 where id=?", follower_id);
        jdbcTemplate.update("UPDATE users SET num_followers=num_followers-1 where id=?", followee_id);

    }

}

