package sample.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 14/8/12
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserRowMapper implements RowMapper {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setImage_url( resultSet.getString("image_url"));
        user.setCreated_at(resultSet.getTimestamp("created_at"));
        user.setDescription(resultSet.getString("description"));
        user.setName(resultSet.getString("name"));
        user.setNum_tweets(resultSet.getString("num_tweets"));
        user.setNum_followers(resultSet.getString("num_followers"));
        user.setNum_followings(resultSet.getString("num_followings"));
        user.setFollowed(false);

        return user;
    }
}
