package shop.hqsmp.mathwear;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class HighScoreScreen extends Activity implements OnClickListener {

	private TextView score;
	private Button replay;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Init();		
	}

	private void Init()
	{
		setContentView(R.layout.highscore_screen);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		System.tfCartoonShout = Typeface.createFromAsset(getAssets(),
				"fonts/BD_Cartoon_Shout.ttf");
		
		score = (TextView)findViewById(R.id.score);
		score.setTypeface(System.tfCartoonShout);
		
		score.setText("Result: " + System.CURRENT_SCORE + "\nHighscore: " + System.HIGH_SCORE);
		
		replay = (Button) findViewById(R.id.btnReplay);
		replay.setOnClickListener(this);
		
//		hint = (TextView)findViewById(R.id.hint);
//		hint.setTypeface(System.tfCartoonShout);
		
//		btnOk.setTypeface(System.tfCartoonShout);
//		btnOk.setOnClickListener(this);
//		btnExit.setOnClickListener(this);
		
//		Intent //Music = new Intent();
//		startService(//Music);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		if(arg0.getId()==R.id.btnReplay)
		{
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			this.finish();
		}
//		else if(arg0.getId()==R.id.btnQuit)
//		{
//			finish();
//		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//Music.play(this, R.raw.soundtrack01);
	}
	@Override
	protected void onPause() {
		super.onPause();
		//Music.stop(this);
	}
	
	private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(System.FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("writeToFile", "File write failed: " + e.toString());
        } 
         
    }
 
    private String readFromFile() {
         
        String ret = "";
         
        try {
            InputStream inputStream = openFileInput(System.FILENAME);
             
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
}
