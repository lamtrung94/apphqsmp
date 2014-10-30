package lx.tung;

import lx.tung.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetActivity extends AppWidgetProvider{
	@Override
	   public void onUpdate(Context context, AppWidgetManager appWidgetManager,
	   int[] appWidgetIds) {
	      for(int i=0; i<appWidgetIds.length; i++){
	    	  int currentWidgetId = appWidgetIds[i];
	    	  Intent intent = new Intent(context, MainActivity.class);
	    	  intent.putExtra("FROM_WIDGET", true);
	    	  PendingIntent pending = PendingIntent.getActivity(context, 0,
	    		      intent, 0);
	    	  RemoteViews views = new RemoteViews(context.getPackageName(),
	    			  R.layout.widgetmain);
	    	  views.setOnClickPendingIntent(R.id.sosButton, pending);
	    	  appWidgetManager.updateAppWidget(currentWidgetId,views);
	    	  Toast.makeText(context, "widget added", Toast.LENGTH_SHORT).show();	
	      }
	   }	
}
