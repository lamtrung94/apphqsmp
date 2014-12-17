package lx.tung.mysos.activities;

import java.io.IOException;

import lx.tung.mysos.Global;
import lx.tung.mysos.R;
import lx.tung.mysos.R.id;
import lx.tung.mysos.R.layout;
import lx.tung.mysos.utilities.DatabaseHelper;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

public class Widget_Activity extends AppWidgetProvider{
	DatabaseHelper mDb;
	@Override
	   public void onUpdate(Context context, AppWidgetManager appWidgetManager,
	   int[] appWidgetIds) {
	      for(int i=0; i<appWidgetIds.length; i++){
	    	  int currentWidgetId = appWidgetIds[i];
	    	  Intent intent = new Intent(context, Home_Activity.class);
	    	  intent.putExtra("FROM_WIDGET", true);
	    	  PendingIntent pending = PendingIntent.getActivity(context, 0,
	    		      intent, 0);
	    	  RemoteViews views = new RemoteViews(context.getPackageName(),
	    			  R.layout.widgetmain);
	    	  views.setOnClickPendingIntent(R.id.sosButton, pending);
	    	  mDb = new DatabaseHelper(context);
	  		try {
	  			mDb.createDataBase();
	  			mDb.openDataBase();
	  		} catch (IOException ioe) {
	  			throw new Error("Unable to create database");
	  		}
	  		if(Global.contactList.size()==0){
	  			mDb.getSOSPhones();
	  		}
	    	views.setTextViewText(R.id.sosButton, Global.contactList.get(0).getName());
	    	appWidgetManager.updateAppWidget(currentWidgetId,views);
	      }
	   }	
}
