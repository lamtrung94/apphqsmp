package lx.tung.knockcode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private EditText knockCodePass;
	private Button okBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingslayout);
		knockCodePass = (EditText) findViewById(R.id.inputKnockPass);
		okBtn = (Button) findViewById(R.id.btnOk);
		okBtn.setOnClickListener(this);
		// use this to start and trigger a service
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

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnOk){
			String knockCode = knockCodePass.getText().toString();
			if(knockCode.length()!=4){
				return;
			}else{
				for(int i = 0; i < knockCode.length(); i++){
					if(Integer.valueOf(knockCode.substring(i, i+1)) > 4){
						return;
					}
				}
				// Save knock code
				writeToFile(knockCode);
				Intent i= new Intent(getApplicationContext(), KnockOnService.class);
		        // potentially add data to the intent
		        getApplicationContext().startService(i);
		        Toast.makeText(getApplicationContext(), "Knock On", Toast.LENGTH_LONG).show();
		        finish();
			}
		}
		
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
