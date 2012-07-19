package sample.model;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 19/7/12
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class User {
    int id;
    String username;
    String email;
    String password;

    public User(int id, String username, String email, String password){
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
