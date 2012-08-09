package sample.model;


import java.sql.Timestamp;

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
    public Timestamp getCreated_at() {
        return created_at;
    }

    public String getDescription() {
        return description;
    }

}
