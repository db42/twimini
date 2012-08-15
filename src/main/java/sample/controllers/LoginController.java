package sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sample.service.UserStore;

import javax.servlet.http.HttpSession;
import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 15/8/12
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class LoginController {
    UserStore userStore;

    @Autowired
    public LoginController(UserStore UserStore) {
        this.userStore = UserStore;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> login(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              HttpSession session) {

        Hashtable hs;
        String userID;

        if (email.contains("@"))
            hs = userStore.getUserByEmail(email, password);
        else
            hs = userStore.getUserByUsername(email, password);

        if (hs == null) {
            hs = new Hashtable<String, String>();
            hs.put("status", "failed");
            return hs;
        }
        userID = (String) hs.get("userID");

        session.setAttribute("email", email);
        session.setAttribute("userID", userID);

        hs.put("status", "success");
        System.out.print("auth"+hs.get("userID"));

        return hs;
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        userStore.invalidateAuthKey((String) session.getAttribute("userID"));
        session.invalidate();
        return "redirect:/";
    }
}
