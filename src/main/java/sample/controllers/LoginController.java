package sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sample.controllers.api.RestAuthLayer;
import sample.service.ApiExceptionResolver;
import sample.service.AuthKeyStore;

import javax.servlet.http.HttpServletRequest;
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
public class LoginController extends ApiExceptionResolver{
    AuthKeyStore authKeyStore;
    RestAuthLayer authLayer;


    @Autowired
    public LoginController(AuthKeyStore authKeyStore, RestAuthLayer authLayer) {
        this.authKeyStore = authKeyStore;
        this.authLayer = authLayer;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> login(@RequestParam("email") String userIdentifier,
                              @RequestParam("password") String password,
                              HttpSession session) {

        Hashtable hs;
        String userID;

        if (userIdentifier.contains("@"))
            hs = authKeyStore.authUserByEmail(userIdentifier, password);
        else
            hs = authKeyStore.authUserByUsername(userIdentifier, password);

        if (hs.get("status") == "failed") {
            hs = new Hashtable<String, String>();
            hs.put("status", "failed");
            return hs;
        }
        userID = (String) hs.get("userID");
        session.setAttribute("email", userIdentifier);
        session.setAttribute("userID", userID);

        hs.put("status", "success");
        return hs;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public Hashtable<String, String> logout(HttpSession session, @RequestParam String userID, HttpServletRequest request) {
        authLayer.isAuthorised(userID, request);
        authKeyStore.invalidateAuthKey(userID);
        System.out.println(userID);
        Hashtable<String, String> hs = new Hashtable<String, String>();
        hs.put("status", "success");
        session.invalidate();
        return hs;
    }
}
