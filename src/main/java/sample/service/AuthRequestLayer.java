package sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.exceptions.ApiExceptionResolver;
import sample.exceptions.NotAuthorisedException;
import sample.service.db.AuthStore;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 7/8/12
 * Time: 6:57 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class AuthRequestLayer extends ApiExceptionResolver {
    AuthStore authStore;

    @Autowired
    public AuthRequestLayer(AuthStore authStore) {
        this.authStore = authStore;
    }

    public boolean isAuthorised(String userID, HttpServletRequest request){
        String auth_header = request.getHeader("Authorization");
        if (auth_header==null)
            throw new NotAuthorisedException();

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] decodedBytes = new byte[0];
        try {
            decodedBytes = decoder.decodeBuffer(auth_header);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String auth_key = new String(decodedBytes);

        if (authStore.getUserByAuthKey(userID, auth_key) == null) {
            throw new NotAuthorisedException();
        }

        return true;
    }
}
