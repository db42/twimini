package sample.model;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: dushyant
 * Date: 19/7/12
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */


public class User extends MiniUser{
    String description;
    Timestamp created_at;
    boolean followed;
    String num_tweets;
    String num_followers;
    String num_followings;

    public User(int id, String image_url, String name, String username,
                Timestamp created_at,
                String description,
                String num_followers,
                String num_followings,
                String num_tweets,
                boolean followed) {
        super(id, image_url, name, username);
        this.created_at = created_at;
        this.description = description;
        this.followed = followed;
        this.num_followers = num_followers;
        this.num_followings = num_followings;
        this.num_tweets = num_tweets;
    }

    public String getNum_tweets() {
        return num_tweets;
    }

    public String getNum_followers() {
        return num_followers;
    }

    public String getNum_followings() {
        return num_followings;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public String getCreated_at() {
        return new SimpleDateFormat("MMM ''yy").format(created_at);
    }

    public String getDescription() {
        return description;
    }
}
