package sample.model;

import java.sql.Timestamp;

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
    String name;
    String email;
    String description;
    Timestamp created_at;

    public User(int id, String username, String email){
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
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
    public Timestamp getCreated_at() {
        return created_at;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }


}
