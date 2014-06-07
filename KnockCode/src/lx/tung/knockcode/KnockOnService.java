package lx.tung.knockcode;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class KnockOnService extends Service {
	BroadcastReceiver mReceiver;
	
	static SensorManager sensorManager = null;
	static SensorEventListener aListener = null;
	static int orientation = -1;
	static WakeLock wl;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
		Log.d("KnockOnService", "onStartCommand() start;");
		boolean screenOff = intent.getBooleanExtra("screen_state", false);
		boolean knockOn = intent.getBooleanExtra("knockOn", false);
		
		Log.d("KnockOnService", "screenOff: " + screenOff);
	    if (!screenOff) {
	        if(sensorManager != null && aListener != null){
	        	Log.d("KnockOnService", "sensorManager.unregister;");
	        	sensorManager.unregisterListener(aListener);
	        	sensorManager = null;
	        	aListener = null;
	        	Log.d("KnockOnService", "sensorManager == null;");
	        }
	        
	        if(knockOn == true){
	        	Log.d("KnockOnService", "wake screen release");
	        	Log.d("KnockOnService","mHandler.removeCallbacks(mRunnable);");
	        	wl.release();
	        	wl = null;
	        	if(BlackScreen.mHandler!=null && BlackScreen.mRunnable != null){
	        		BlackScreen.mHandler.removeCallbacksAndMessages(null);
	        	}
	        }
	    } else {
	    	Log.d("KnockOnService", "sensorManager.register;");
	    	sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
		    aListener = new SensorEventListener() {
	            int orientation=-1;

	            @Override
	            public void onSensorChanged(SensorEvent event) {
	                if (event.values[1]<6.5 && event.values[1]>-6.5) {
	                    if (orientation!=1) {
	                        KnockOnService.orientation = 1;//Landscape
	                    }
	                    orientation=1;
	                } else {
	                    if (orientation!=0) {
	                    	KnockOnService.orientation = 0;//Portrait
	                    	Log.d("KnockOnService", "wake screen");
	                    	Intent i = new Intent(KnockOnService.this, BlackScreen.class);
	            	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            	        startActivity(i);
	            	    	PowerManager pm;
	            	    	pm = (PowerManager) getSystemService(POWER_SERVICE);
	            	    	wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
	            	    			| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
	            	    	wl.acquire();
	            	    	Log.d("KnockOnService", "wake screen acquire");
	                    	
	                    }
	                    orientation=0;
	                }
	            }

	            @Override
	            public void onAccuracyChanged(Sensor sensor, int accuracy) {
	                // TODO Auto-generated method stub

	            }
	        };
	        sensorManager.registerListener(aListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    	
//	        unregisterReceiver(mReceiver);
	    	Log.d("SCREEN", "OFF");
//	    	this.stopSelf();
	    }
	    Log.d("KnockOnService", "onStartCommand() end;");
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
	    
	    Notification n  = new NotificationCompat.Builder(this)
        .setContentTitle("Knock On")
        .setContentText("Knock On activated")
        .setSmallIcon(R.drawable.ic_launcher).build();
	    
	    startForeground (1, n);
	    
	    Log.d("KnockOnService", "ONCREATE");
	}
} 