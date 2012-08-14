package sample.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 14/8/12
 * Time: 11:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class PostRowMapper implements RowMapper {

    @Override
    public Post mapRow(ResultSet resultSet, int i) throws SQLException {
        Post post = new Post(resultSet.getInt("id"), resultSet.getInt("user_id"), resultSet.getString("post"), resultSet.getTimestamp("time"));
        post.setAuthor_id(resultSet.getInt("author_id"));
        post.setRtwt_id(resultSet.getInt("rtwt_id"));
        return post;
    }
}
