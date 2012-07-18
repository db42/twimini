package sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Autowired
    public TwiminiStore(SimpleJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addPost(int userID, String post) {
        jdbcTemplate.update("INSERT INTO post (user_id, post) VALUES (?,?)", userID, post);
    }

    public List<Map<String, Object>> getPosts(int userID){
        List<Map<String, Object>> posts = jdbcTemplate.queryForList("select * from posts where user_id=?",userID);
        if (posts == null)
            posts = new ArrayList<Map<String, Object>>();

        return posts;
    }

    public void addFollower(int following, int follower) {
        jdbcTemplate.update("INSERT INTO followers (user_id, follower) VALUES (?,?)", following, follower);
    }
}
