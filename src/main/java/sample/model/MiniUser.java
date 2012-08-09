package sample.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    private static String convertToURL(String email) {
        byte[] bytesOfMessage = new byte[0];
        bytesOfMessage = email.getBytes();

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] md5_url = md.digest(bytesOfMessage);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < md5_url.length; ++i) {
            sb.append(Integer.toHexString((md5_url[i] & 0xFF) | 0x100).substring(1, 3));
        }
        String baseUrl = "http://www.gravatar.com/avatar/";
        return baseUrl.concat(sb.toString());
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
