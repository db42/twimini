package sample.model;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 19/7/12
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class Post {
    int id;
    int user_id;
    String post;
    Timestamp timestamp;
    User user;

    public int getId() {
        return id;
    }

//    public int getUser_id() {
//        return user_id;
//    }

    public String getPost() {
        return post;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public User getUser() {
        return user;
    }

    public Post(int id, int user_id, String post, Timestamp time, User user){
        this.id = id;
        this.user_id = user_id;
        this.post = post;
        this.timestamp = time;
        this.user = user;
    }

}
