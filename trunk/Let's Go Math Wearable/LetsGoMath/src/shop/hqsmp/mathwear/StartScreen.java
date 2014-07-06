package shop.hqsmp.mathwear;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class StartScreen extends Activity implements OnClickListener {

//	private TextView title, hint;
	private Button btnOk
//	, btnExit
	;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Init();		
	}

	private void Init()
	{
		setContentView(R.layout.startscreen);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		System.tfCartoonShout = Typeface.createFromAsset(getAssets(),
				"fonts/BD_Cartoon_Shout.ttf");
		
//		title = (TextView)findViewById(R.id.title);
//		title.setTypeface(System.tfCartoonShout);
//		hint = (TextView)findViewById(R.id.hint);
//		hint.setTypeface(System.tfCartoonShout);
		
		btnOk = (Button) findViewById(R.id.btnStart);
//		btnOk.setTypeface(System.tfCartoonShout);
		btnOk.setOnClickListener(this);
//		btnExit = (Button) findViewById(R.id.btnQuit);
//		btnExit.setTypeface(System.tfCartoonShout);
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
		if(arg0.getId()==R.id.btnStart)
		{
//			Intent intent = new Intent(this, MainActivity.class);
//			startActivity(intent);
			startActivity(new Intent(getApplicationContext(), ScreenSlideActivity.class));
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
}
