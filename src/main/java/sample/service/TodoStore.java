package sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;

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

    public List<Map<String,Object>> returnSelect() {
        List<Map<String,Object>> ar = myjdbc.queryForList("SELECT id, user_id, description FROM todos");
        if (ar==null)
            ar = new ArrayList<Map<String, Object>>();
        return ar;
    }

}