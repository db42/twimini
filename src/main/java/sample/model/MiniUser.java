package sample.model;

import org.springframework.beans.factory.annotation.Autowired;
import sample.utilities.MD5Encoder;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 9/8/12
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class MiniUser{
    int id;
    String username;
    String name;
    String email;
    String image_url;

    @Autowired
    static MD5Encoder md5Encoder;

    private static String convertToURL(String email) {
        String baseUrl = "http://www.gravatar.com/avatar/";
        return baseUrl.concat(md5Encoder.encodeString(email));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return convertToURL(this.email);
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
