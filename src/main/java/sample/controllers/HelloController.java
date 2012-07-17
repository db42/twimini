package sample.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Hashtable;

@Controller
public class HelloController {

    @RequestMapping("/hello")
    ModelAndView helloSpringGet() {
        return new ModelAndView("hello") {{
            addObject("msg", "Hello Spring Get!!");
        }};
    }

    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    ModelAndView helloSpringPost(@RequestParam final String msg) {
        return new ModelAndView("hello") {{
            addObject("msg", msg);
        }};
    }

    @RequestMapping("/hello.json")
    @ResponseBody
    Hashtable<String, String> jsonHello() {
        return new Hashtable<String, String>() {{
            put("msg", "Hello JSON!!!");
        }};
    }


}
