package lx.tung.volcamera;

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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class VolCameraService extends Service{
	static BroadcastReceiver mReceiver;
	static BroadcastReceiver mReceiverButton = null;
	static SensorManager sensorManager = null;
	static SensorEventListener aListener = null;
	static int orientation = -1;
	static boolean screenOff;
	static boolean volumeDown;
	static MediaPlayer mediaPlayer;
	static WakeLock wl;
	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {		
		boolean isVolClicked = intent.getBooleanExtra("fromVolClicked", false);
		boolean isScreenChanged = intent.getBooleanExtra("fromScreenState", false);
		if(isVolClicked && !isScreenChanged){
			//screenOff = intent.getBooleanExtra("screen_state", false);
			volumeDown = intent.getBooleanExtra("volume_clicked", false);
		}else if (isScreenChanged && !isVolClicked){
			screenOff = intent.getBooleanExtra("screen_state", false);
			volumeDown = false;
		}
		
		if(screenOff){
			if(!volumeDown){
				if(mReceiverButton==null){
					mReceiverButton = new RemoteControlReceiver();
				}
				IntentFilter filer2 = new IntentFilter();
				filer2.addAction("android.media.VOLUME_CHANGED_ACTION");
			    mReceiverButton = new RemoteControlReceiver();
			    registerReceiver(mReceiverButton, filer2);
			    
			    sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
			    aListener = new SensorEventListener() {
		            int orientation=-1;

		            @Override
		            public void onSensorChanged(SensorEvent event) {
		                if (event.values[1]<6.5 && event.values[1]>-6.5) {
		                    if (orientation!=1) {
		                        VolCameraService.orientation = 1;//Landscape
		                        if(VolCameraService.mediaPlayer!=null)
		                        	VolCameraService.mediaPlayer.reset();
		                        VolCameraService.mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.silent);
		                        VolCameraService.mediaPlayer.setVolume(0.0f, 0.0f);
		                        VolCameraService.mediaPlayer.setLooping(false);
		                        VolCameraService.mediaPlayer.start();
		                    }
		                    orientation=1;
		                } else {
		                    if (orientation!=0) {
		                    	VolCameraService.orientation = 0;//Portrait
		                    	if(VolCameraService.mediaPlayer!=null && VolCameraService.mediaPlayer.isPlaying()){
		                    		VolCameraService.mediaPlayer.stop();
		                    		VolCameraService.mediaPlayer.release();
		                    		VolCameraService.mediaPlayer=null;
							    }
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
			}
			else {
				if(VolCameraService.orientation == 1){
					sensorManager.unregisterListener(aListener);
					aListener=null;
					sensorManager=null;
					
					//WakeLock wl;
				    PowerManager pm;
				    pm = (PowerManager) getSystemService(POWER_SERVICE);
				    wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				    	    			| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
				    wl.acquire();
				    wl.release();
				    if(mReceiver==null){
				    	mReceiver = new ScreenReceiver();
					}
				    if (mReceiverButton != null){
				    	unregisterReceiver(mReceiverButton);
				    	mReceiverButton = null;
				    }
				    try{
				    	//unregisterReceiver(mReceiverButton);
				    	if(mediaPlayer!=null && mediaPlayer.isPlaying()){
					    	mediaPlayer.stop();
					    	mediaPlayer.release();
					    	mediaPlayer=null;
					    }
				    	
				    }catch(Exception e){
				    	
				    }
				    
				    Intent intentIntemediateActivity = new Intent(getApplicationContext(), IntemediateActivty.class);
				    intentIntemediateActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				    startActivity(intentIntemediateActivity);
				}
			}
		}
		else{
			if (wl!=null && wl.isHeld()){
				wl.release();
				wl = null;
			}
			if (mReceiverButton != null){
		    	unregisterReceiver(mReceiverButton);
		    	mReceiverButton = null;
		    }
			if (aListener!=null && sensorManager!=null){
				sensorManager.unregisterListener(aListener);
				aListener=null;
				sensorManager=null;
			}
			if(mediaPlayer!=null && mediaPlayer.isPlaying()){
		    	mediaPlayer.stop();
		    	mediaPlayer.release();
		    	mediaPlayer=null;
		    }
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
        Log.d("VolCameraService.this.orientation:",Integer.toString(VolCameraService.this.orientation));
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
	    filter.addAction(Intent.ACTION_SCREEN_OFF);
	    mReceiver = new ScreenReceiver();
	    registerReceiver(mReceiver, filter);
	    //mReceiverButton = new RemoteControlReceiver();
	    
	    Notification n  = new NotificationCompat.Builder(this)
        .setContentTitle("VolCamera")
        .setContentText("VolCamera activated")
        .setSmallIcon(R.drawable.ic_launcher)
        //.setContentIntent(pIntent)
        //.setAutoCancel(true)
        //.addAction(R.drawable.icon, "Call", pIntent)
        //.addAction(R.drawable.icon, "More", pIntent)
        //.addAction(R.drawable.icon, "And more", pIntent)
        .build()
        ;
	    
	    startForeground (1, n);
	    
	}
} 