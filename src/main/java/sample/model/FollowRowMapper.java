package sample.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 14/8/12
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class FollowRowMapper implements org.springframework.jdbc.core.RowMapper {
    Date today = new Date();

    @Override
    public Boolean mapRow(ResultSet resultSet, int i) throws SQLException {
        Timestamp timeStamp = new Timestamp(today.getTime());
        return (resultSet.getTimestamp("unfollow_time").after(timeStamp));
    }
}
