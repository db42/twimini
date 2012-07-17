package sample.controllers;

import org.codehaus.jackson.map.deser.StdDeserializer;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@Controller
public class TodoController {

    ArrayList tasks = new ArrayList();
    @RequestMapping("/todo") ModelAndView todoGet(HttpSession session){

        ModelAndView mv = new ModelAndView("todo");
        if (session.getAttribute("tasks") != null)
            mv.addObject("tasks", session.getAttribute("tasks"));
        return mv;

    }

    @RequestMapping(value = "/todo/new", method = RequestMethod.POST)
    ModelAndView todoPost(@RequestParam String task, HttpSession session){

      ModelAndView mv = new ModelAndView("todo");

      ArrayList<String> tasks = (ArrayList)session.getAttribute("tasks");
      if (tasks == null)
          tasks =  new ArrayList<String>();


      tasks.add(task);
      session.setAttribute("tasks", tasks);

      return new ModelAndView("redirect:/todo");
    }

    @RequestMapping(value = "/todo/delete", method = RequestMethod.GET)
    ModelAndView todeleteGet(@RequestParam String id, HttpSession session){

      ArrayList<String> tasks = (ArrayList)session.getAttribute("tasks");
      tasks.remove(Integer.parseInt(id));

      session.setAttribute("tasks", tasks);

      return new ModelAndView("redirect:/todo");

    }

    @RequestMapping(value = "/todo/modify", method = RequestMethod.POST)
    ModelAndView tomodifyPost(@RequestParam String id, @RequestParam String task_new, HttpSession session){

        ArrayList<String> tasks = (ArrayList)session.getAttribute("tasks");
        tasks.set(Integer.parseInt(id), task_new);

        session.setAttribute("tasks", tasks);
        return new ModelAndView("redirect:/todo");

    }

    @RequestMapping(value = "/todo/new.json", method = RequestMethod.POST)
    @ResponseBody Hashtable<String, String> jsonNew(@RequestParam String task, HttpSession session) {

        ArrayList<String> tasks = (ArrayList)session.getAttribute("tasks");
        if (tasks == null)
            tasks =  new ArrayList<String>();

        tasks.add(task);
        session.setAttribute("tasks", tasks);

        Hashtable<String, String> hs =  new Hashtable<String, String>();
        hs.put("task", task);
        hs.put( "index", Integer.toString(tasks.size()-1));
        return hs;
    }
    @RequestMapping(value = "/todo/delete.json", method = RequestMethod.GET)
    @ResponseBody Hashtable<String, String> deletejson(@RequestParam String id, HttpSession session){

        ArrayList<String> tasks = (ArrayList)session.getAttribute("tasks");
        tasks.remove(Integer.parseInt(id));

        session.setAttribute("tasks", tasks);

        Hashtable<String, String> hs =  new Hashtable<String, String>();
        hs.put("index", id);

        return hs;

    }

    @RequestMapping(value = "/todo/modify.json", method = RequestMethod.POST)
    @ResponseBody Hashtable<String, String> modifyjson(@RequestParam String id, @RequestParam String task_new, HttpSession session){
        ArrayList<String> tasks = (ArrayList)session.getAttribute("tasks");
        tasks.set(Integer.parseInt(id), task_new);

        session.setAttribute("tasks", tasks);
        Hashtable<String, String> hs =  new Hashtable<String, String>();
        hs.put("index", id);
        hs.put("task", task_new);

        return hs;
    }
}