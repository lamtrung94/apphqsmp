package lx.tung.mysos.activities;

import java.io.IOException;
import java.text.DecimalFormat;

import lx.tung.mysos.Global;
import lx.tung.mysos.R;
import lx.tung.mysos.utilities.DatabaseHelper;
import lx.tung.mysos.utilities.Utilities;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class Home_Activity extends Activity implements OnClickListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	DatabaseHelper mDb;
	Button mButton1, mButton2, mButton3, mBtnSettings, mBtnBooklets;
	TextView mlblTitle;

	LocationClient mLocationClient;
	LocationRequest lr = LocationRequest.create();
	String lastLast, lastLong;
	boolean fromWidget = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mysoslayout);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.main_title_with_nobutton);
		mLocationClient = new LocationClient(this, this, this);
		fromWidget = getIntent().getBooleanExtra("FROM_WIDGET", false);
		if (fromWidget) {
			triggerSOS();
		}

		mButton1 = (Button) findViewById(R.id.btnSOS1);
		mButton2 = (Button) findViewById(R.id.btnSOS2);
		mButton3 = (Button) findViewById(R.id.btnSOS3);
		mBtnSettings = (Button) findViewById(R.id.btnSettings);
		mBtnBooklets = (Button) findViewById(R.id.btnBooklets);
		mlblTitle = (TextView) findViewById(R.id.lblTitle);

		Initialize();
	}

	private void Initialize() {
		mDb = new DatabaseHelper(getApplicationContext());
		try {
			mDb.createDataBase();
			mDb.openDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		if (Global.contactList.size() == 0) {
			mDb.getSOSPhones();
		}
		mButton1.setOnClickListener(this);
		mButton1.setText(Global.contactList.get(0).getName() + " ("
				+ Global.contactList.get(0).getNum() + ")");
		mButton2.setOnClickListener(this);
		mButton2.setText(Global.contactList.get(1).getName() + " ("
				+ Global.contactList.get(1).getNum() + ")");
		mButton3.setOnClickListener(this);
		mButton3.setText(Global.contactList.get(2).getName() + " ("
				+ Global.contactList.get(2).getNum() + ")");
		mBtnSettings.setOnClickListener(this);
		mBtnSettings.setText(getResources().getString(R.string.settings));
		mBtnBooklets.setOnClickListener(this);
		mBtnBooklets.setText(getResources().getString(R.string.booklets));
		
		mlblTitle.setCompoundDrawablesWithIntrinsicBounds(getResources()
				.getDrawable(R.drawable.alarmicon), null, null, null);
		mDb.close();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnSOS1) {
			triggerSOS(0);
		} else if (v.getId() == R.id.btnSOS2) {
			triggerSOS(1);
		} else if (v.getId() == R.id.btnSOS3) {
			triggerSOS(2);
		}

		else if (v.getId() == R.id.btnSettings) {
			Intent i = new Intent();
			i.setClass(getApplicationContext(), Settings_Activity.class);
			startActivity(i);
		} else if (v.getId() == R.id.btnBooklets) {
			Intent i = new Intent();
			i.setClass(getApplicationContext(), Booklets_List_Activity.class);
			startActivity(i);
		}
	}

	private void triggerSOS() {
		Utilities.enableNetwork(Home_Activity.this);
		Global.phoneToSMS = Global.contactList.get(0).getNum();
		mLocationClient.connect();
		Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ Global.contactList.get(0).getNum()));
		startActivity(callIntent);
	}

	private void triggerSOS(int index) {
		Utilities.enableNetwork(Home_Activity.this);
		Global.phoneToSMS = Global.contactList.get(index).getNum();
		mLocationClient.connect();
		Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ Global.contactList.get(index).getNum()));
		startActivity(callIntent);
	}

	private LocationListener mLocationListener = new LocationListener() {
		private long mLastEventTime = 0;

		@Override
		public void onLocationChanged(Location location) {
			double delayBtnEvents = (System.nanoTime() - mLastEventTime) / (100000000.0);
			mLastEventTime = System.nanoTime();
			String samplingRate = (new DecimalFormat("0.0000")
					.format(1 / delayBtnEvents));
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
		lr.setInterval(10000).setPriority(
				LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationClient.requestLocationUpdates(lr, mLocationListener);
		mLocationClient.requestLocationUpdates(lr, mLocationListener);
		Location aLocation = mLocationClient.getLastLocation();
		lastLast = Double.toString(aLocation.getLatitude());
		lastLong = Double.toString(aLocation.getLongitude());

		Utilities.sendSms(Home_Activity.this, lastLast, lastLong,
				Global.phoneToSMS);

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		super.onResume();
		Initialize();
	}
}
