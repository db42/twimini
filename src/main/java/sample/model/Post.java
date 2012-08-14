package sample.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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
    int rtwt_id;
    int author_id;
    String post;
    Timestamp timestamp;
    boolean reposted;

    public boolean isReposted() {
        return reposted;
    }

    public void setReposted(boolean reposted) {
        this.reposted = reposted;
    }


    public void Post(){
        reposted = false;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public int getRtwt_id() {
        return rtwt_id;
    }

    public void setRtwt_id(int rtwt_id) {
        this.rtwt_id = rtwt_id;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getPost() {
        return post;
    }

    public String getTimestamp() {
        String dateString = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss").format(timestamp);

        return dateString.substring(0,10)+'T'+dateString.substring(10)+'Z';
    }



    public Post(int id, int user_id, String post, Timestamp time){
        this.id = id;
        this.user_id = user_id;
        this.post = post;
        this.timestamp = time;
    }

}
