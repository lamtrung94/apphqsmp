package tung.lx.uetlinker.Models;

/**
 * Created by Tung on 1/26/2015.
 */
public class LinkObject {
    private String title;
    private String url;

    public LinkObject(String aTitle, String aUrl){
        title = aTitle;
        url = aUrl;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }


}

