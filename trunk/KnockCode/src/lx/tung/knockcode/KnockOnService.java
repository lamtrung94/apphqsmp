package lx.tung.knockcode;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class KnockOnService extends Service {
	BroadcastReceiver mReceiver;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
		boolean screenOn = intent.getBooleanExtra("screen_state", false);
	    if (!screenOn) {
	        
	    } else {
	    	WakeLock wl;
	    	PowerManager pm;
	    	Intent i = new Intent(this, BlackScreen.class);
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(i);
	    	pm = (PowerManager) getSystemService(POWER_SERVICE);
	    	wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
	    			| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");  wl.acquire();
	    	wl.acquire();
	        wl.release();
	        unregisterReceiver(mReceiver);
	    	Log.d("SCREEN", "ON");
	    	this.stopSelf();
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
	    mReceiver = new ScreenReceiver();
	    registerReceiver(mReceiver, filter);
	    Log.d("KnockOnService", "ONCREATE");
	}
} 