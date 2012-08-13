package sample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthInterceptor extends HandlerInterceptorAdapter {
    private final ThreadLocal<String> userID;

    @Autowired
    public AuthInterceptor(@Qualifier("userID") ThreadLocal<String> userID) {
        this.userID = userID;
    }

    @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession(false);
        if (session != null) {
            String email = (String) session.getAttribute("email");
            if (email != null) {
                userID.set((String) session.getAttribute("userID"));
                return true;
            }
        }
        response.sendRedirect("/");
        return false;
    }
}
