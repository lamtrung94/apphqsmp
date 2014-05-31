package lx.tung.volcamera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {
	 
    private boolean screenOff;
    
    @Override
    public void onReceive(Context context, Intent intent) {
    	Intent i = new Intent(context, VolCameraService.class);
    	if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOff = true;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOff = false;
        }
        i.putExtra("screen_state", screenOff);
        context.startService(i);
    }
 
}