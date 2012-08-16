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
        return new User(resultSet.getInt("id"),
                resultSet.getString("image_url"),
                resultSet.getString("name"),
                resultSet.getString("username"),
                resultSet.getTimestamp("created_at"),
                resultSet.getString("description"),
                resultSet.getString("num_followers"),
                resultSet.getString("num_followings"),
                resultSet.getString("num_tweets"),
                false);
    }
}
