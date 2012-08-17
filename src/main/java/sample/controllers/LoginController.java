package sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sample.service.AuthLayer;
import sample.exceptions.ApiExceptionResolver;
import sample.service.db.AuthKeyStore;

import javax.servlet.http.HttpServletRequest;
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
    AuthLayer authLayer;

    @Autowired
    public LoginController(AuthKeyStore authKeyStore, AuthLayer authLayer) {
        this.authKeyStore = authKeyStore;
        this.authLayer = authLayer;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    Hashtable<String, String> login(@RequestParam("email") String userIdentifier,
                              @RequestParam("password") String password){

        Hashtable hs;
        if (userIdentifier.contains("@"))
            hs = authKeyStore.authUserByEmail(userIdentifier, password);
        else
            hs = authKeyStore.authUserByUsername(userIdentifier, password);

        return hs;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public Hashtable<String, String> logout(@RequestParam String userID, HttpServletRequest request) {
        authLayer.isAuthorised(userID, request);
        authKeyStore.invalidateAuthKey(userID);
        Hashtable<String, String> hs = new Hashtable<String, String>();
        hs.put("status", "success");
        return hs;
    }
}
