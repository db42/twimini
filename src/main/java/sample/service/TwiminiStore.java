package sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;

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
}
