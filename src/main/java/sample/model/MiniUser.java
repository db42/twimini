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

    public MiniUser(int id, String image_url, String name, String username) {
        this.id = id;
        this.image_url = image_url;
        this.name = name;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return this.image_url;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
