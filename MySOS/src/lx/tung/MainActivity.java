package lx.tung;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.mysos.R;

public class MainActivity extends Activity implements OnClickListener{
	Button mButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mysoslayout);
		mButton = (Button) findViewById(R.id.btnSOS);
		mButton.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnSOS){
//			turnGPSOn();
			Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+841667795223")); 
            startActivity(callIntent);
		}
	}
	
	public void turnGPSOn()
	{
	     Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
	     intent.putExtra("enabled", true);
	     this.getApplicationContext().sendBroadcast(intent);

	    String provider = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	    if(!provider.contains("gps")){ //if gps is disabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        this.getApplicationContext().sendBroadcast(poke);


	    }
	}
	// automatic turn off the gps
	public void turnGPSOff()
	{
	    String provider = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	    if(provider.contains("gps")){ //if gps is enabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        this.getApplicationContext().sendBroadcast(poke);
	    }
	}
}
