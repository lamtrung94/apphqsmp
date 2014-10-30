package lx.tung;

import java.text.DecimalFormat;

import lx.tung.R;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class MainActivity extends Activity implements OnClickListener, 
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener	{
	
	Button mButton;
	LocationClient mLocationClient;
	LocationRequest lr = LocationRequest.create();
	String lastLast, lastLong;
	boolean fromWidget = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mysoslayout);
		mLocationClient = new LocationClient(this,this,this);
		fromWidget = getIntent().getBooleanExtra("FROM_WIDGET", false);
		if(fromWidget){
			triggerSOS();
		}
		mButton = (Button) findViewById(R.id.btnSOS);
		mButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnSOS){
			triggerSOS();
		}
	}
	
	private void triggerSOS(){
		Utilities.enableNetwork(MainActivity.this);
		mLocationClient.connect();
		Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+841667795223")); 
        startActivity(callIntent);
	}
	
	private LocationListener mLocationListener = new LocationListener() {
		private long mLastEventTime = 0;
		@Override
		public void onLocationChanged(Location location) {
			double delayBtnEvents = (System.nanoTime()-mLastEventTime)/(100000000.0);
			mLastEventTime = System.nanoTime();
			String samplingRate = (new DecimalFormat("0.0000").format(1/delayBtnEvents));
			float speed = (float) (location.getSpeed() * 3.6);
		}
	};
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		lr.setFastestInterval(10000);
		lr.setInterval(10000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationClient.requestLocationUpdates(lr, mLocationListener);
		mLocationClient.requestLocationUpdates(lr, mLocationListener);
		Location aLocation = mLocationClient.getLastLocation();
		lastLast = Double.toString(aLocation.getLatitude());
		lastLong = Double.toString(aLocation.getLongitude());
		
//		Utilities.sendSms(MainActivity.this, lastLast, lastLong);
		
	}
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
}
