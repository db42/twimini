package sample.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

@Configuration
public class AppConfig {
    @Bean
    public SimpleJdbcTemplate simpleJdbcTemplate() throws DataAccessException {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/twimini_db");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        SimpleJdbcTemplate db = new SimpleJdbcTemplate(dataSource);

        db.update("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                "username varchar(128) NOT NULL," +
                "email varchar(128) NOT NULL," +
                "password varchar(40) NOT NULL," +
                "name varchar(255)," +
                "description varchar(255)," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                "UNIQUE (username)," +
                "UNIQUE (email)" +
                ")");

        db.update("CREATE TABLE IF NOT EXISTS posts (" +
                "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                "user_id INTEGER, FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE RESTRICT," +
                "post varchar(140) NOT NULL," +
                "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                "rtwt_id INTEGER," +
                "UNIQUE (user_id, rtwt_id)"+
                ")");

        db.update("CREATE TABLE IF NOT EXISTS followers (" +
                "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                "user_id INTEGER, FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE RESTRICT," +
                "follower INTEGER, FOREIGN KEY (follower) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE RESTRICT," +
                "unfollow_time TIMESTAMP DEFAULT '2038-01-01 00:00:00',"+
                "UNIQUE (user_id, follower)"+
                ")");
        return db;
    }

    @Bean
    public ThreadLocal<Long> userID() {
        return new ThreadLocal<Long>();
    }
}