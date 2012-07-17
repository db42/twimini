package sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sample.service.TodoStore;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: abhijeet.pa
 * Date: 29/06/12
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class TodoController {

    SimpleJdbcTemplate myjdbc;

    @Autowired
    public void TodoController(SimpleJdbcTemplate db){
        myjdbc = db;
    }

    @RequestMapping("/todo")
    ModelAndView todoSpringGet(HttpSession session) {

        ModelAndView mv = new ModelAndView("todo");

        TodoStore tds = new TodoStore(myjdbc);
        List<Map<String,Object>> ar = tds.returnSelect();

        mv.addObject("todo_new", ar);
        return mv;
    }

    @RequestMapping(value = "/todo/new", method = RequestMethod.POST)
    ModelAndView todoSpringPost(@RequestParam String todo_new, HttpSession session) {

        ModelAndView mv = new ModelAndView("redirect:/todo");
        if (!todo_new.equals("")) {
            ArrayList<String> ar = (ArrayList) session.getAttribute("data");
            if (ar == null)
                ar = new ArrayList<String>();

            ar.add(todo_new);
            session.setAttribute("data", ar);
        }
        return mv;
    }

    @RequestMapping(value = "/todo/delete", method = RequestMethod.POST)
    ModelAndView todoDeleteSpringPost(@RequestParam int index, HttpSession session) {

        ArrayList<String> ar = (ArrayList) session.getAttribute("data");
        ModelAndView mv = new ModelAndView("redirect:/todo");
        ar.remove(index);
        session.setAttribute("data", ar);
        return mv;

    }

    @RequestMapping(value = "/todo/edit", method = RequestMethod.POST)
    ModelAndView todoEditSpringPost(@RequestParam int index_new, @RequestParam String todo_old_change, HttpSession session) {

        ArrayList<String> ar = (ArrayList) session.getAttribute("data");
        ModelAndView mv = new ModelAndView("redirect:/todo");
        ar.remove(index_new);
        ar.add(index_new, todo_old_change);
        session.setAttribute("data", ar);
        return mv;

    }

    @RequestMapping("/todo/new.json")
    @ResponseBody
    Hashtable<String, String> todoSpringPostJson(@RequestParam String todo_new,
                                                 HttpSession session) {
        Hashtable hs = new Hashtable<String, String>();
        int index=0;
        if (!todo_new.equals("")) {

            myjdbc.update("INSERT INTO todos (user_id, description) VALUES (?,?)", "1", todo_new);

            index = myjdbc.queryForInt("SELECT id from todos WHERE user_id=? AND description=?", "1", todo_new);

            hs.put("todo_old_change",todo_new);
            hs.put("index_new",index);
            hs.put("index",index);
        }
        return hs;
    }

    @RequestMapping("/todo/delete.json")
    @ResponseBody
    Hashtable<String, String> todoDeleteSpringPostJson(@RequestParam int index,
                                                       HttpSession session) {
        Hashtable hs = new Hashtable<String, String>();

        myjdbc.update("DELETE from todos WHERE id=? AND user_id=?", index, "1");

        hs.put("index",index);
        return hs;
    }

    @RequestMapping("/todo/edit.json")
    @ResponseBody
    Hashtable<String, String> todoEditSpringPostJson(@RequestParam int index_new,
                                                     @RequestParam String todo_old_change,
                                                     HttpSession session) {
        Hashtable hs = new Hashtable<String, String>();

        myjdbc.update("UPDATE todos SET description=? WHERE id=?", todo_old_change, index_new);

        hs.put("todo_old_change", todo_old_change);
        hs.put("index_new", index_new);
        hs.put("index", index_new);
        return hs;
    }


}
