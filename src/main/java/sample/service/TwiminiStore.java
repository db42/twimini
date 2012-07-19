package sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import sample.model.Post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public TwiminiStore(SimpleJdbcTemplate jdbcTemplate,@Qualifier("userID") ThreadLocal<Long> userID){
        this.jdbcTemplate = jdbcTemplate;
        this.userID = userID;
    }

    public void addPost( String post) {
        jdbcTemplate.update("INSERT INTO posts (user_id, post) VALUES (?,?)", userID.get(), post);
    }

    public List<Post> getPosts(){
        PostRowMapper postRowMapper = new PostRowMapper();

        List<Post> posts = (List< Post>) jdbcTemplate.query("SELECT * from posts where user_id=" + userID.get(), postRowMapper);

        if (posts==null)
            posts = new ArrayList< Post>();
        return posts;

   }

    public void addFollower(int following) {
        jdbcTemplate.update("INSERT INTO followers (user_id, follower) VALUES (?,?)", following, userID.get());
    }

    public void addUser(String name, String email, String password) {
        jdbcTemplate.update("INSERT INTO users (username, email, password) VALUES (?,?,?)", name, email, password);
    }
}

class PostRowMapper implements RowMapper {

    @Override
    public Post mapRow(ResultSet resultSet, int i) throws SQLException {
        Post post = new Post(resultSet.getInt("id"), resultSet.getInt("user_id"), resultSet.getString("post"), resultSet.getTimestamp("time"));
        return post;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
