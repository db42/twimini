package sample.service.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import sample.model.User;
import sample.model.UserRowMapper;
import sample.utilities.MD5Encoder;

import java.util.Date;
import java.util.Hashtable;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 15/8/12
 * Time: 4:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class AuthStore {
    SimpleJdbcTemplate jdbcTemplate;
    MD5Encoder md5Encoder;

    @Autowired
    public AuthStore(SimpleJdbcTemplate jdbcTemplate, MD5Encoder md5Encoder){
        this.jdbcTemplate = jdbcTemplate;
        this.md5Encoder = md5Encoder;
    }


    private Hashtable<String, String> authUser(String field_name, String field_value, String password) {
        UserRowMapper userRowMapper = new UserRowMapper();
        Hashtable<String, String> hs = new Hashtable<String, String>();
        try{
            User user = (User) jdbcTemplate.queryForObject("select * from users where "+ field_name + "=\"" + field_value + "\" and password=SHA1(\""+ password + "\")", userRowMapper);

            String auth_key = db_gen_auth_key(Integer.toString(user.getId()));
            hs.put("status","success");
            hs.put("userID", Integer.toString(user.getId()));
            hs.put("auth_key", auth_key);
            return hs;
        }
        catch (EmptyResultDataAccessException e){
            hs.put("status","failed");
            return hs;
        }
    }

    public Hashtable<String, String> authUserByEmail(String email, String password) {
        return authUser("email", email, password);
    }

    public Hashtable<String, String> authUserByUsername(String username, String password) {
        return authUser("username", username, password);
    }

    public boolean authUserByUserID(String userID, String password) {
        UserRowMapper userRowMapper = new UserRowMapper();
        Hashtable<String, String> hs = new Hashtable<String, String>();
        try{
            User user = (User) jdbcTemplate.queryForObject("select * from users where id =\"" + userID + "\" and password=SHA1(\""+ password + "\")", userRowMapper);
            return true;
        }
        catch (EmptyResultDataAccessException e){
            return false;
        }
    }

    public User getUserByAuthKey(String userID, String auth_key) {
        UserRowMapper userRowMapper = new UserRowMapper();
        try{
            return (User) jdbcTemplate.queryForObject("select * from users where id=" + userID + " and auth_key=\""+ auth_key + "\"", userRowMapper);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public String db_gen_auth_key(String userID) {
        long current_time_value = new Date().getTime();
        Random random_number = new Random();
        String auth_key = md5Encoder.encodeString(Long.toString(current_time_value).concat(random_number.toString()));
        jdbcTemplate.update("UPDATE users SET auth_key=? where id=?",auth_key, userID);
        return auth_key;
    }

    public void invalidateAuthKey(String userID) {
        jdbcTemplate.update("UPDATE users SET auth_key=? where id=?","", userID);
    }
}
