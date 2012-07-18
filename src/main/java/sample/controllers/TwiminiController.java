package sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sample.service.TwiminiStore;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 18/7/12
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class TwiminiController {
    TwiminiStore tStore;

    @Autowired
    public TwiminiController(TwiminiStore tStore){
        this.tStore = tStore;
    }
}
