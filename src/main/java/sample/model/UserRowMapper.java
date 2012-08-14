package sample.model;

import org.springframework.jdbc.core.RowMapper;
import sample.utilities.MD5Encoder;

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
    MD5Encoder md5Encoder;

    public UserRowMapper(MD5Encoder md5Encoder){
        this.md5Encoder = md5Encoder;
    }

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        String email = resultSet.getString("email");
        String image_url = resultSet.getString("image_url");
        user.setCreated_at(resultSet.getTimestamp("created_at"));
        user.setDescription(resultSet.getString("description"));
        user.setName(resultSet.getString("name"));

        if (image_url == null){
            String baseUrl = "http://www.gravatar.com/avatar/";
            image_url = baseUrl.concat(md5Encoder.encodeString(email));
        }
        user.setImage_url(image_url);

        user.setNum_tweets(resultSet.getString("num_tweets"));
        user.setNum_followers(resultSet.getString("num_followers"));
        user.setNum_followings(resultSet.getString("num_followings"));
        user.setFollowed(false);

        return user;
    }
}
