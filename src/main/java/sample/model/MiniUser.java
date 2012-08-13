package sample.model;


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
        return this.image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
