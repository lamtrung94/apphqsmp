package tung.lx.uetlinker.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import tung.lx.uetlinker.Constants;

/**
 * Created by Tung on 1/27/2015.
 */
public class Utils {
    public static List<String> getKeywordList(String keywordString){
        List<String>keywordList = new ArrayList<String>();
        if(keywordString != null && !"".equals(keywordString.trim())){
            while(keywordString.indexOf(',') != -1){
                keywordList.add(keywordString.substring(0, keywordString.indexOf(',')));
                keywordString = keywordString.substring(keywordString.indexOf(',')+1);
            }
            if (keywordString != null && !"".equals(keywordString.trim())){
                keywordList.add(keywordString);
            }
            return keywordList;
        }
        return new ArrayList<String>();
    }

    public static void saveKeywordsPreferences(Context context, String keywords){
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("keywords", keywords);
        editor.commit();
    }

    public static void refreshData(Context context){
        LinkGetter aLinkGetter = new LinkGetter();
        try{
            Global.lsLinkObjectAnnouncement = aLinkGetter.getLink(Constants.ANNOUNCEMENT_LINK);
            Global.lsLinkObjectExam = aLinkGetter.getLink(Constants.EXAM_LINK);
            Global.lsLinkObjectSchedule = aLinkGetter.getLink(Constants.SCHEDULE_LINK);
            Toast.makeText(context, "Get data successfully!", Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Toast.makeText(context, "Get data failed!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
