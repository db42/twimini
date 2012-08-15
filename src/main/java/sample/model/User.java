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


public class User  extends MiniUser{
    String description;
    Timestamp created_at;
    boolean followed;
    String num_tweets;
    String num_followers;
    String num_followings;

    public void setNum_followers(String num_followers) {
        this.num_followers = num_followers;
    }

    public void setNum_followings(String num_followings) {
        this.num_followings = num_followings;
    }

    public void setNum_tweets(String num_tweets) {
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
    public String getCreated_at() {
        return new SimpleDateFormat("MMM ''yy").format(created_at);
    }

    public String getDescription() {
        return description;
    }
}
