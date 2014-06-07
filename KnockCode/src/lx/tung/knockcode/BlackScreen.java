package lx.tung.knockcode;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BlackScreen extends Activity implements OnClickListener, OnGestureListener, OnDoubleTapListener {
	private ProgressDialog progressDialog;
	private boolean isOff = false;
	private Process proc;
	private DataOutputStream os;
	private DataInputStream is;
	private String brightnessPath = "";	
	private Boolean mFastBrightnessmethod;
	private int mOldBrightness_Sys;
	private LinearLayout wakeBtn;
	private static GestureDetector gd;
	public void onBackPressed() {
		return;
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_main);
        try {
			brightnessPath = "/sys/devices/platform/msm_fb.525825/leds/lcd-backlight/brightness";
			float curBrightnessValue=android.provider.Settings.System.getInt(
				    getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
        	mOldBrightness_Sys = (int)Math.round(curBrightnessValue);
		} catch (Exception e) {
		}
		
		try {
			isOff = true;
			setBrightness(0);
			setMinCPU();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		wakeBtn = (LinearLayout) findViewById(R.id.wakeBtn);
		gd = new GestureDetector(this);
		gd.setOnDoubleTapListener(this);
		wakeBtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				BlackScreen.gd.onTouchEvent(event);
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	    private String getBrightnessPath() throws Exception
		{
			String value = "";
			if (value.trim().length() > 0) return value;
			if (proc == null)
			{
				proc = Runtime.getRuntime().exec("su");
			    os = new DataOutputStream(proc.getOutputStream());
			    is = new DataInputStream(proc.getInputStream());
			}

			if (mFastBrightnessmethod)
				os.writeBytes("ls /sys/class/backlight/*/brightness\n"); //fast, use LS
			else
				os.writeBytes("find /sys/devices -name 'brightness' \n"); //slow, use BUSYBOX

			os.writeBytes("echo end\n");

			InputStreamReader isr = new InputStreamReader(is);
		    BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
			{ 
				if (line.trim().length() == 0 || line.trim().equals("end")) break;
				if (line.toLowerCase().contains("backlight") && (!line.toLowerCase().contains("button")))
					value = value + line + "\n";
			}
			return value;
		}
	    
	    private void setBrightness(int Value) throws Exception {
			for(String s: brightnessPath.split("\\r?\\n"))
			{
				if (s.trim().length() > 0) doCmds("echo " + Value + " > " + s.trim());
			}
	    }
	    
	    private void doCmds(String cmds) throws Exception {
			if (proc == null)
			{
				proc = Runtime.getRuntime().exec("su");
			    os = new DataOutputStream(proc.getOutputStream());
			    is = new DataInputStream(proc.getInputStream());
			}
			os.writeBytes(cmds+"\n");
		}
	    
//		@Override
//		public boolean onLongClick(View v) {
//			if(v.getId() == R.id.wakeBtn){
//				try {
//					isOff = false;
//					setBrightness(mOldBrightness_Sys);
//					finish();
//					Log.d("GOT HERE", "GOT HERE");
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			return false;
//		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			Log.d("onSingleTapConfirmed", "onSingleTapConfirmed");
			return false;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			try {
				isOff = false;
				setBrightness(mOldBrightness_Sys);
				setDefaultCPU();
				Intent i= new Intent(getApplicationContext(), KnockOnService.class);
		        getApplicationContext().startService(i);
//		        DevicePolicyManager mDPM;
//		        mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
//		        mDPM.lockNow();
				finish();
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			return false;
		}

		
		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
//			Log.d("DOUBLE TAP", "DOUBLE TAP");
//			try {
//				isOff = false;
//				setBrightness(mOldBrightness_Sys);
//				setDefaultCPU();
//				finish();
//			} catch (Exception ex) {
//				// TODO Auto-generated catch block
//				ex.printStackTrace();
//			}
			return false;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		};
		
		public void setMinCPU(){
			Frequency min_frequency = SysUtils.getMinFreq();
			String min_freq = min_frequency.getValue();
			String governor = SysUtils.getGovernor();
			String[] governors = SysUtils.getAvailableGovernors();
			SysUtils.setFrequenciesAndGovernor(min_freq, min_freq, governor, null, false,
					getApplicationContext(), R.string.ok, R.string.error);
			SysUtils.disableCores(getApplicationContext(), R.string.ok, R.string.error);
		}
		
		public void setDefaultCPU(){
			SysUtils.enableCores(getApplicationContext(), R.string.ok, R.string.error);
			String[] freq = SysUtils.getAvailableFrequencies();
			String min_freq = freq[0];
			String max_freq = freq[freq.length-2];
			String governor = SysUtils.getGovernor();
			String[] governors = SysUtils.getAvailableGovernors();
			SysUtils.setFrequenciesAndGovernor(min_freq, max_freq, governor, null, false,
					getApplicationContext(), R.string.ok, R.string.error);
		}
}
