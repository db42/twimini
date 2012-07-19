package sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import sample.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class TodoStore{

    SimpleJdbcTemplate myjdbc;

    @Autowired
    public TodoStore(SimpleJdbcTemplate db){
        myjdbc = db;
    }

    public List<Map<String,Post>> returnSelect() {
        PostRowMapper postRowMapper = new PostRowMapper();

        List<Map<String,Post>> ar= null;// = myjdbc.queryForList("SELECT id, user_id, description FROM todos", postRowMapper );
        if (ar==null)
            ar = new ArrayList<Map<String, Post>>();
        return ar;
    }

}
