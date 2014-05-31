package lx.tung.volcamera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class RemoteControlReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.d("RemoteControlReceiver", "onReceive");
    	
    	if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {    		
            int newVolume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);
            int oldVolume = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", 0);
            if (newVolume >= oldVolume) {
            	Intent i = new Intent(context, VolCameraService.class);
                i.putExtra("screen_state", true);
                i.putExtra("volume_clicked", true);
                context.startService(i);
            }
        }
    	
    }
}