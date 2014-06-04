package lx.tung.volcamera;

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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
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
			    mediaPlayer = new MediaPlayer();
			    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.correct);
				mediaPlayer.setVolume(0.0f, 0.0f);
				mediaPlayer.setLooping(true);
				mediaPlayer.start();
			    
			    sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
			    aListener = new SensorEventListener() {
		            int orientation=-1;

		            @Override
		            public void onSensorChanged(SensorEvent event) {
		                if (event.values[1]<6.5 && event.values[1]>-6.5) {
		                    if (orientation!=1) {
		                        VolCameraService.orientation = 1;//Landscape
		                    }
		                    orientation=1;
		                } else {
		                    if (orientation!=0) {
		                    	VolCameraService.orientation = 0;//Portrait
		                    }
		                    orientation=0;
		                }
		            }

		            @Override
		            public void onAccuracyChanged(Sensor sensor, int accuracy) {
		                // TODO Auto-generated method stub

		            }
		        };
		        sensorManager.registerListener(aListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
			}
			else {
				if(VolCameraService.orientation == 1){
					sensorManager.unregisterListener(aListener);
					aListener=null;
					sensorManager=null;
					
					WakeLock wl;
				    PowerManager pm;
				    pm = (PowerManager) getSystemService(POWER_SERVICE);
				    wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				    	    			| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");  wl.acquire();
				    wl.acquire();
				    Log.d("PowerManager", "WAKE SCREEN!");
				    wl.release();
				    if(mReceiver==null){
				    	mReceiver = new ScreenReceiver();
					}
				    if (mReceiverButton != null){
				    	unregisterReceiver(mReceiverButton);
				    	mReceiverButton = null;
				    }
				    KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
			        km.newKeyguardLock("KEYGUARD").disableKeyguard();
				    try{
				    	//unregisterReceiver(mReceiverButton);
				    	if(mediaPlayer!=null && mediaPlayer.isPlaying()){
					    	mediaPlayer.stop();
					    	mediaPlayer.release();
					    	mediaPlayer=null;
					    }
				    	
				    }catch(Exception e){
				    	
				    }
				    Handler handler = new Handler(); 
				    handler.postDelayed(new Runnable() { 
				         public void run() { 
				        	 Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							 intentCamera.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							 startActivity(intentCamera); 
				         } 
				    }, 1000);
				    Log.d("VolCameraService", "After Camera");
				}
			}
		}
		else{
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
//			KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//		    km.newKeyguardLock("KEYGUARD").reenableKeyguard();
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