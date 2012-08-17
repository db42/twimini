package sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import sample.model.FollowRowMapper;
import sample.model.User;
import sample.model.UserRowMapper;

import java.util.Hashtable;
import java.util.List;

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
    AuthKeyStore authKeyStore;
    String defaultUserCount = "20";


    @Autowired
    public UserStore(SimpleJdbcTemplate jdbcTemplate, AuthKeyStore authKeyStore){
        this.jdbcTemplate = jdbcTemplate;
        this.authKeyStore = authKeyStore;
    }

    private boolean doesFollow(int user_id, String follower){
        try{
            FollowRowMapper followRowMapper = new FollowRowMapper();
            return (Boolean) jdbcTemplate.queryForObject("select * from followers where user_id="+user_id+" AND follower="+follower +" AND unfollow_time > NOW()", followRowMapper);
        } catch (EmptyResultDataAccessException e){
            return false;
        }
    }

    public boolean addFollowing(String followee_id, String follower_id) {
        try{
            jdbcTemplate.update("INSERT INTO followers (user_id, follower) VALUES (?,?)", followee_id, follower_id);
        }
        catch (DuplicateKeyException e){
            jdbcTemplate.update("UPDATE followers SET unfollow_time='2038-01-01 00:00:00' where user_id=? AND follower=?",followee_id ,follower_id);
        }
        jdbcTemplate.update("UPDATE users SET num_followings=num_followings+1 where id=?", follower_id);
        jdbcTemplate.update("UPDATE users SET num_followers=num_followers+1 where id=?", followee_id);
        return true;
    }

    public void deleteFollowing(String followee_id, String follower_id) {
        jdbcTemplate.update("UPDATE followers SET unfollow_time = NOW() where user_id=? AND follower=?",followee_id ,follower_id);
        jdbcTemplate.update("UPDATE users SET num_followings=num_followings-1 where id=?", follower_id);
        jdbcTemplate.update("UPDATE users SET num_followers=num_followers-1 where id=?", followee_id);
    }

    public Hashtable<String, String> addUser(String username, String email, String password, String image_url) {
        Hashtable<String, String> hs = new Hashtable<String, String>();
        try {
            jdbcTemplate.update("INSERT INTO users (username, email, password, image_url) VALUES (?,?,SHA1(?),?)",username, email, password, image_url);
            hs.put("status", "success");
            return hs;
        }
        catch (DuplicateKeyException e){
            hs.put("status", "failed");
            hs.put("message", "User already exists with same username or email");
            return hs;
        }
    }

    public Hashtable<String, String> updateUserPassword(String userID, String old_password, String new_password) {
        Hashtable<String, String> hs = new Hashtable<String, String>();
        jdbcTemplate.update("UPDATE users SET password=SHA1(?) where id=?",new_password, userID);
        hs.put("status", "success");
        return hs;
    }

    public Hashtable<String, String> updateUserAccount(String userID, String username, String email) {
        Hashtable<String, String> hs = new Hashtable();
        hs.put("status", "failed");

        if (validateUserById(userID))
            try{
                jdbcTemplate.update("UPDATE users SET username=?, email=? where id=?",username, email, userID);
                hs.put("status", "success");
            }
            catch (DuplicateKeyException e){
                hs.put("message", "User already exists with same username or email");
                return hs;
            }

        return hs;
    }

    public Hashtable<String, String> updateUserProfile(String userID, String name, String description) {
        Hashtable<String, String> hs = new Hashtable();
        jdbcTemplate.update("UPDATE users SET name=?, description=? where id=?", name, description, userID);
        hs.put("status","success");
        return hs;
    }

    public boolean validateUserById(String userID){
        UserRowMapper userRowMapper = new UserRowMapper();
        try{
            User user = (User) jdbcTemplate.queryForObject("select * from users where id=" + userID, userRowMapper);
            return true;
        }
        catch (EmptyResultDataAccessException e){
            return false;
        }
    }

    public User getUser(String userID, String callerUserID){
        UserRowMapper userRowMapper = new UserRowMapper();
        try{
            User user = (User) jdbcTemplate.queryForObject("select * from users where id=" + userID, userRowMapper);

            if (callerUserID != null) {
                user.setFollowed(doesFollow(user.getId(), callerUserID));
            }
            return user;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public boolean authUserByUserID(String userID, String password) {
        UserRowMapper userRowMapper = new UserRowMapper();
        try{
            User user = (User) jdbcTemplate.queryForObject("select * from users where id=" + userID + " and password=SHA1(\""+ password + "\")", userRowMapper);
            return true;
        }
        catch (EmptyResultDataAccessException e){
            return false;
        }
    }

    public List<User> getFollowers(String userID, String count, String max_id, String callerUserID ) {
        UserRowMapper userRowMapper = new UserRowMapper();
        count = (count == null) ? defaultUserCount : count;
        String query;

        if (max_id == null)
            query = "select * from users where id in (select follower from followers where user_id="+ userID +" AND unfollow_time > NOW()) ORDER BY id DESC LIMIT "+count;
        else
            query = "select * from users where id in (select follower from followers where user_id="+ userID +" AND unfollow_time > NOW() AND follower<" + max_id+" ) ORDER BY id DESC LIMIT "+count;

        List<User> followers = jdbcTemplate.query(query, userRowMapper);
        if (callerUserID != null) {
            for(User u:followers){
                u.setFollowed(doesFollow(u.getId(), callerUserID));
            }
        }
        return followers;
    }

    public List<User> getFollowings(String userID, String count, String max_id, String callerUserID) {
        UserRowMapper userRowMapper = new UserRowMapper();
        count = (count == null) ? defaultUserCount : count;
        String query;

        if (max_id == null)
            query = "select * from users where id in (select user_id from followers where follower="+ userID +" AND unfollow_time > NOW()) ORDER BY id DESC LIMIT "+count;
        else
            query = "select * from users where id in (select user_id from followers where follower="+ userID +" AND unfollow_time > NOW() AND user_id<" + max_id+" ) ORDER BY id DESC LIMIT "+count;

        List<User> followings = jdbcTemplate.query(query, userRowMapper);
        if (callerUserID!=null) {
            if (userID == callerUserID)
                for(User u:followings){
                    u.setFollowed(true);
                }
            else {
                for(User u:followings){
                    u.setFollowed(doesFollow(u.getId(), callerUserID));
                }
            }
        }
        return followings;
    }

    public List<User> searchForUsers(String query, String callerUserID) {
        UserRowMapper userRowMapper = new UserRowMapper();
        query = "\"%"+query+"%\"";
        query = "select * from users where name LIKE "+query+" OR username LIKE "+query;

        List<User> followers = jdbcTemplate.query(query, userRowMapper);
        if (callerUserID != null) {
            for(User u:followers){
                u.setFollowed(doesFollow(u.getId(), callerUserID));
            }
        }
        return followers;
    }
}

