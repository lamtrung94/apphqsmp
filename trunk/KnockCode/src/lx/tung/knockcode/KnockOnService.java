package lx.tung.knockcode;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.WindowManager;

public class KnockOnService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
		boolean screenOn = intent.getBooleanExtra("screen_state", false);
	    if (!screenOn) {
	        Log.d("SCREEN", "OFF");
	    } else {
	    	WakeLock wl;
	    	PowerManager pm;
	    	pm = (PowerManager) getSystemService(POWER_SERVICE);
	    	wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
	    			| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");  wl.acquire();
	    	wl.acquire();
	    	Intent i = new Intent(this, BlackScreen.class);
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(i);
	        wl.release();
	    	Log.d("SCREEN", "ON");
	    }
	    return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		//TODO for communication return IBinder implementation
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	    // REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC
	    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
	    filter.addAction(Intent.ACTION_SCREEN_OFF);
	    BroadcastReceiver mReceiver = new ScreenReceiver();
	    registerReceiver(mReceiver, filter);
	    Log.d("KnockOnService", "ONCREATE");
	}
} 