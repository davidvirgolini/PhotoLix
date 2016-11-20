package Entities;

import java.util.ArrayList;

/**
 * Created by Luigi on 19-Nov-16.
 */

public class Photo {
    private String id;
    private String secret;
    private String server;
    private int farm;
    private String url;
    private String date;
    private String albumTitle;
    private ArrayList<Comment> comments;

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(){
        url = "https://farm" + farm  +
                ".staticflickr.com/" + server +
                "/" + id +
                "_" + secret + ".jpg";

    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
