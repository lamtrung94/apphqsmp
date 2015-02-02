package tung.lx.uetlinker.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Vector;

import tung.lx.uetlinker.Constants;
import tung.lx.uetlinker.Models.LinkObject;

/**
 * Created by Tung on 1/26/2015.
 */
public class LinkGetter {
    public Vector<LinkObject> getLink(String URL) throws IOException {
        Vector<LinkObject> lsLinkObject = new Vector<LinkObject>();
        Document aDoc =
                Jsoup.connect(URL).get();
        Elements elements = aDoc.getElementsByClass("views-field-title");
        System.out.println(elements.size());
        for (int i = 0; i < elements.size(); i++){
            if (elements.get(i).child(0) != null){
                if (elements.get(i).child(0).child(0) != null){
                    String title = elements.get(i).child(0).child(0).attr("title");
                    String link = Constants.PREFIX_UET_LINK
                            + elements.get(i).child(0).child(0).attr("href");
                    lsLinkObject.add(new LinkObject(title, link));
                }
            }
        }
        return lsLinkObject;
    }
}
