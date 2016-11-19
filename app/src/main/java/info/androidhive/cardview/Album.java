package info.androidhive.cardview;

/**
 * Created by Lincoln on 18/05/16.
 */
public class Album {
    private String title;
    private String url;
    private String id;
    private String primary;

    public Album() {
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String name) {
        this.url = name;
    }
}
