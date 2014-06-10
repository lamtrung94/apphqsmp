package lx.tung.knockcode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.KeyguardManager;
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
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class KnockOnService extends Service {
	static BroadcastReceiver mReceiver;
	
	static SensorManager sensorManager = null;
	static SensorEventListener aListener = null;
	static int orientation = -1;
	static WakeLock wl;
	static KeyguardManager km;
	static String knockCode;
	static boolean restartScreenOn = false;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
		Log.d("KnockOnService", "onStartCommand() start;");
		boolean screenOff = intent.getBooleanExtra("screen_state", false);
		boolean knockOn = intent.getBooleanExtra("knockOn", false);
		knockCode = readFromFile();
		Log.d("KNOCK CODE:", knockCode);
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
//		    	if(km == null){
//		    		km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//		    	}
//		        km.newKeyguardLock("KEYGUARD").reenableKeyguard();
//		        km = null;
	        	Log.d("KnockOnService", "wake screen release");
	        	Log.d("KnockOnService","mHandler.removeCallbacks(mRunnable);");
	        	wl.release();
	        	wl = null;
	        	if(KnockCodeBlackScreen.mHandler!=null && KnockCodeBlackScreen.mRunnable != null){
	        		KnockCodeBlackScreen.mHandler.removeCallbacksAndMessages(null);
	        	}
	        	if(KnockOnBlackScreen.mHandler!=null && KnockOnBlackScreen.mRunnable != null){
	        		KnockOnBlackScreen.mHandler.removeCallbacksAndMessages(null);
	        	}
	        }else{
	        	if(KnockCodeBlackScreen.running == false && KnockOnBlackScreen.running == false){
	        		PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		        	manager.goToSleep(SystemClock.uptimeMillis());
	        	}
//	        	PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//	        	manager.goToSleep(SystemClock.uptimeMillis());
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
	                        restartScreenOn = true;
	                    }
	                    orientation=1;
	                } else {
	                    if (orientation!=0 && restartScreenOn == true) {
	                    	KnockOnService.orientation = 0;//Portrait
	                    	Log.d("KnockOnService", "wake screen");
	                    	Intent i;
	                    	if(!knockCode.trim().equals("0000")){
	                    		KnockCodeBlackScreen.running = true;
	                    		i = new Intent(KnockOnService.this, KnockCodeBlackScreen.class);
	                    		Log.d("KnockOnService", "KnockCodeBlackScreenIntent");
	                    	}else{
	                    		KnockOnBlackScreen.running = true;
	                    		i = new Intent(KnockOnService.this, KnockOnBlackScreen.class);
	                    		Log.d("KnockOnService", "KnockOnBlackScreenIntent");
	                    	}
	                    	
	            	        
	                    	PowerManager pm;
	            	    	pm = (PowerManager) getSystemService(POWER_SERVICE);
	            	    	wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
	            	    			| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
	            	    	wl.acquire();
	            	    	km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
	            	    	km.newKeyguardLock("KEYGUARD").disableKeyguard();
	            	    	Log.d("KnockOnService", "wake screen acquire");
	            	    	restartScreenOn = false;
	            	    	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            	        startActivity(i);
	            	    	
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
	    filter.setPriority(10000);
	    mReceiver = new ScreenReceiver();
	    registerReceiver(mReceiver, filter);
	    
	    Notification n  = new NotificationCompat.Builder(this)
        .setContentTitle("Knock On")
        .setContentText("Knock On activated")
        .setSmallIcon(R.drawable.ic_launcher).build();
	    
	    startForeground (1, n);
	    restartScreenOn = true;
	    Log.d("KnockOnService", "ONCREATE");
	}
	
	private String readFromFile() {
        
        String ret = "";
         
        try {
            InputStream inputStream = openFileInput("aTxt");
             
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                 
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                 
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("readFromFile", "File not found: " + e.toString());
            ret = "0";
            writeToFile("0");
        } catch (IOException e) {
            Log.e("readFromFile", "Can not read file: " + e.toString());
        }
 
        return ret;
    }
	
	private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("aTxt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("writeToFile", "File write failed: " + e.toString());
        } 
         
    }
} 