package lx.tung.mysos.activities;

import java.io.IOException;

import lx.tung.mysos.Global;
import lx.tung.mysos.R;
import lx.tung.mysos.R.drawable;
import lx.tung.mysos.R.id;
import lx.tung.mysos.R.layout;
import lx.tung.mysos.R.string;
import lx.tung.mysos.utilities.DatabaseHelper;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;

public class Settings_Activity extends Activity implements OnClickListener{

	Button btnOk;
	TextView tv1, tv2, tv3, mlblTitle;
	EditText ep1, ep2, ep3, en1, en2, en3;
	DatabaseHelper mDb;
	
	public void onBackPressed() {
		Intent i = new Intent();
		i.setClass(Settings_Activity.this, Home_Activity.class);
		startActivity(i);
		this.finish();
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.mysossettings);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title_with_nobutton);
		mDb = new DatabaseHelper(getApplicationContext());
		try {
			mDb.createDataBase();
			mDb.openDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		if(Global.contactList.size()==0){
			mDb.getSOSPhones();
		}
		btnOk = (Button) findViewById(R.id.acceptBtn);
		btnOk.setOnClickListener(this);
		tv1 = (TextView) findViewById(R.id.tvphonenum1);
		tv1.setText(getResources().getString(R.string.phonenum1));
		tv2 = (TextView) findViewById(R.id.tvphonenum2);
		tv2.setText(getResources().getString(R.string.phonenum2));
		tv3 = (TextView) findViewById(R.id.tvphonenum3);
		tv3.setText(getResources().getString(R.string.phonenum3));
		
		ep1 = (EditText) findViewById(R.id.etnum1phone);
		ep1.setHint(Global.contactList.get(0).getNum());
		ep2 = (EditText) findViewById(R.id.etnum2phone);
		ep2.setHint(Global.contactList.get(1).getNum());
		ep3 = (EditText) findViewById(R.id.etnum3phone);
		ep3.setHint(Global.contactList.get(2).getNum());
		
		en1 = (EditText) findViewById(R.id.etnum1name);
		en1.setHint(Global.contactList.get(0).getName());
		en2 = (EditText) findViewById(R.id.etnum2name);
		en2.setHint(Global.contactList.get(1).getName());
		en3 = (EditText) findViewById(R.id.etnum3name);
		en3.setHint(Global.contactList.get(2).getName());
		
		mlblTitle = (TextView) findViewById(R.id.lblTitle);
		mlblTitle.setText("");
		mlblTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.settingsicon), null, null, null);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.acceptBtn){
			mDb.updateContact(en1.getText().toString(), ep1.getText().toString(), 1);
			mDb.updateContact(en2.getText().toString(), ep2.getText().toString(), 2);
			mDb.updateContact(en3.getText().toString(), ep3.getText().toString(), 3);
			Intent i = new Intent();
			i.setClass(Settings_Activity.this, Home_Activity.class);
			startActivity(i);
			this.finish();
		}
	}

}
