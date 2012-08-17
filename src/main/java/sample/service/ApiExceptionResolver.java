package sample.service;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;
import sample.controllers.api.exceptions.NotAuthorisedException;
import sample.controllers.api.exceptions.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 16/8/12
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */

public class ApiExceptionResolver implements HandlerExceptionResolver {

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, Exception e) {
        MappingJacksonJsonView jv = new MappingJacksonJsonView();
        Hashtable<String, String> hs = new Hashtable<String, String>();
        hs.put("status", "failed");
        if (e instanceof NotAuthorisedException)
            httpServletResponse.setStatus(401);
        else if (e instanceof ResourceNotFoundException)
            httpServletResponse.setStatus(404);
        else {
            hs.put("error-message", e.getMessage());
            httpServletResponse.setStatus(500);
        }

        return new ModelAndView(jv, hs);
    }
}
