package lx.tung.letsgomath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	/** The view to show the ad. */
//	private AdView adView;

	/* Your ad unit id. Replace with your actual ad unit id. */
	private static final String AD_UNIT_ID = "a15340b36633078";
		
//	private ImageView timerLine, clock;
	private Button btnNumber01, btnNumber02, btnNumber03, btnNumber04
//	,
//		btnFacebook, btnTwitter
	;
	private TextView txtScore
//	, txtResult
	;
	private LinearLayout game;
	
	@Override
	public void onBackPressed() {
		if(aCountDownTimer!=null)
		{
			aCountDownTimer.cancel();
		}
		super.onBackPressed();
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Init();
//		AddAd();
		System.status = System._STATUS_INIT;
		FillData();
		
	}

	private void Init()
	{
//		//Music.playeffect(getApplicationContext(), R.raw.start);
//		System.CURRENT_SCORE = 0;
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		System.tfCartoonShout = Typeface.createFromAsset(getAssets(),
				"fonts/BD_Cartoon_Shout.ttf");
		System.tfComicSerifPro = Typeface.createFromAsset(getAssets(),
				"fonts/HVD_Comic_Serif_Pro.otf");
		System.score = 0;
		
		game = (LinearLayout)findViewById(R.id.game);
		
		txtScore = (TextView) findViewById(R.id.score);
		txtScore.setTypeface(System.tfComicSerifPro);
		
		btnNumber01 = (Button) findViewById(R.id.number01);
		btnNumber01.setTypeface(System.tfComicSerifPro);
		btnNumber01.setOnClickListener(this);
		
		btnNumber02 = (Button) findViewById(R.id.number02);
		btnNumber02.setTypeface(System.tfComicSerifPro);
		btnNumber02.setOnClickListener(this);
		
		btnNumber03 = (Button) findViewById(R.id.number03);
		btnNumber03.setTypeface(System.tfComicSerifPro);
		btnNumber03.setOnClickListener(this);
		
		btnNumber04 = (Button) findViewById(R.id.number04);
		btnNumber04.setTypeface(System.tfComicSerifPro);
		btnNumber04.setOnClickListener(this);
		
//		txtResult = (TextView) findViewById(R.id.result);
//		txtResult.setTypeface(System.tfComicSerifPro);
//		
//		timerLine = (ImageView) findViewById(R.id.timer);
//		clock = (ImageView) findViewById(R.id.clock);
		
//		btnFacebook = (Button) findViewById(R.id.facebook);
//		btnFacebook.setOnClickListener(this);
//		btnTwitter = (Button) findViewById(R.id.twitter);
//		btnTwitter.setOnClickListener(this);
		
	}
	
//	private void AddAd()
//	{
//		// Create an ad.
//	    adView = new AdView(this);
//	    adView.setAdSize(AdSize.BANNER);
//	    adView.setAdUnitId(AD_UNIT_ID);
//	    
//	    // Add the AdView to the view hierarchy. The view will have no size
//	    // until the ad is loaded.
//	    LinearLayout layout = (LinearLayout) findViewById(R.id.adlinearlayout);
//	    layout.addView(adView);
//	    
//	 // Create an ad request. Check logcat output for the hashed device ID to
//	    // get test ads on a physical device.
//	    Bundle bundle = new Bundle();
//	    bundle.putString("color_bg", "019D49");
//	    AdMobExtras extras = new AdMobExtras(bundle);
//	    
//	    
//	    AdRequest adRequest = new AdRequest.Builder()
//	    	.addNetworkExtras(extras)
//	        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//	        .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
//	        .build();
//
//	    // Start loading the ad in the background.
//	    adView.loadAd(adRequest);
//	}
	
	private void FillData()
	{
		System.HIGH_SCORE = Integer.valueOf(readFromFile());
		txtScore.setText(Integer.toString(System.score));
		
		TheGenerator.generateQuiz();
		
		btnNumber01.setText(Integer.toString(System.numberList.getTheList()[0]));
		btnNumber02.setText(Integer.toString(System.numberList.getTheList()[1]));
		btnNumber03.setText(Integer.toString(System.numberList.getTheList()[2]));
		btnNumber04.setText(Integer.toString(System.numberList.getTheList()[3]));
		
		if(System.status == System._STATUS_INIT)
		{
//			txtResult.setText(System.getCommentInit());
		}else if(System.status == System._STATUS_SUCCESS)
		{
			//Music.playeffect(getApplicationContext(), R.raw.correct);
//			txtResult.setText(System.getCommentSuccess());
//			txtResult.setTextColor(getResources().getColor(R.color.black));
		}else if(System.status == System._STATUS_FAIL)
		{
			System.CURRENT_SCORE = System.score;
			Intent intent = new Intent(this, HighScoreScreen.class);
			startActivity(intent);
			this.finish();
			game.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.shake));
			//Music.playeffect(getApplicationContext(), R.raw.wrong);
			if(System.score > System.HIGH_SCORE)
			{
				writeToFile(Integer.toString(System.score));
				System.HIGH_SCORE = System.score;
			}
//			txtResult.setText(System.getCommentFail());
//			txtResult.setTextColor(getResources().getColor(R.color.Red1));
			if(aCountDownTimer != null)
			{
				aCountDownTimer.cancel();
			}
			System.resetScore();
			System.status = System._STATUS_INIT;
		}else if(System.status == System._STATUS_TIME_OUT)
		{
			System.CURRENT_SCORE = System.score;
			Intent intent = new Intent(this, HighScoreScreen.class);
			startActivity(intent);
			this.finish();
			game.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.shake));
			//Music.playeffect(getApplicationContext(), R.raw.wrong);
			if(System.score > System.HIGH_SCORE)
			{
				writeToFile(Integer.toString(System.score));
				System.HIGH_SCORE = System.score;
			}
//			txtResult.setText(System.getCommentTimeOut());
//			txtResult.setTextColor(getResources().getColor(R.color.Red1));
			System.resetScore();
			System.status = System._STATUS_INIT;
		}	
		
	}
	
	private void checkResult(int value)
	{
		if (value == System.numberList.getTheResult())
		{
			System.increaseScore();
			System.status = System._STATUS_SUCCESS;
			FillData();
			resetTimer(System.step, 
//					timerLine, txtResult, 
					0, false);
		}else if (value != System.numberList.getTheResult())
		{
			//System.resetScore();
			System.status = System._STATUS_FAIL;
			FillData();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		if(arg0.getId()==R.id.number01)
		{
			checkResult(System.numberList.getTheList()[0]);
		}else if(arg0.getId()==R.id.number02)
		{
			checkResult(System.numberList.getTheList()[1]);
		}else if(arg0.getId()==R.id.number03)
		{
			checkResult(System.numberList.getTheList()[2]);
		}else if(arg0.getId()==R.id.number04)
		{
			checkResult(System.numberList.getTheList()[3]);
		}
//			else if(arg0.getId()==R.id.facebook)
//		{
//			shareScreen();
//		}
	}
	
	public CountDownTimer aCountDownTimer;
	
	public void resetTimer(final int STEP, 
//			final ImageView timerLine, final TextView txtResult, 
			long timeleft, boolean isResumed)
	{
		int timer = 0;
		if(STEP == System._STEP_EASY)
		{
			timer = System._TIMER_EASY;
		}else if(STEP == System._STEP_MEDIUM)
		{
			timer = System._TIMER_MEDIUM;
		}else if(STEP == System._STEP_HARD)
		{
			timer = System._TIMER_HARD;
		}else if(STEP == System._STEP_GOD)
		{
			timer = System._TIMER_GOD;
		}
		if (!isResumed || System.timeleft < 1)
		{
			timeleft = timer;
		}
			
//		int width = timerLine.getWidth();
		final double finalTimer = timer;
//		final double initWidth = width-clock.getWidth();
//		final double initFinishPos = timerLine.getX();
//		final double initStartPost = initFinishPos + initWidth * (timeleft/(finalTimer*1000));
//		clock.setX((float) initStartPost);
		
		if(aCountDownTimer!=null)
		{
			aCountDownTimer.cancel();
		}
		aCountDownTimer = new CountDownTimer(timeleft*1000, 1) {
		     public void onTick(long millisUntilFinished) {
//		    	 clock.setX((int) Math.floor(initFinishPos + initWidth * (millisUntilFinished/(finalTimer*1000))));
		    	 System.timeleft = (long) Math.floor(millisUntilFinished);
		    	 double portionTime = System.timeleft/(finalTimer*1000);
		    	 Log.d("portionTime", Double.toString(portionTime));
		    	 if (portionTime > 3/4){
		    		 btnNumber01.setBackground(getResources().getDrawable(R.drawable.number01));
		    		 btnNumber02.setBackground(getResources().getDrawable(R.drawable.number02));
		    		 btnNumber03.setBackground(getResources().getDrawable(R.drawable.number03));
		    		 btnNumber04.setBackground(getResources().getDrawable(R.drawable.number04));
		    		 btnNumber01.setTextColor(getResources().getColor(R.color.orangeyellow));
		    		 btnNumber02.setTextColor(getResources().getColor(R.color.orangeyellow));
		    		 btnNumber03.setTextColor(getResources().getColor(R.color.orangeyellow));
		    		 btnNumber04.setTextColor(getResources().getColor(R.color.orangeyellow));
		    	 }else if (portionTime > 1/2){
		    		 btnNumber01.setBackground(getResources().getDrawable(R.drawable.number01_timeout));
		    		 btnNumber02.setBackground(getResources().getDrawable(R.drawable.number02));
		    		 btnNumber03.setBackground(getResources().getDrawable(R.drawable.number03));
		    		 btnNumber04.setBackground(getResources().getDrawable(R.drawable.number04));
		    		 btnNumber01.setTextColor(getResources().getColor(R.color.white1));
		    		 btnNumber02.setTextColor(getResources().getColor(R.color.orangeyellow));
		    		 btnNumber03.setTextColor(getResources().getColor(R.color.orangeyellow));
		    		 btnNumber04.setTextColor(getResources().getColor(R.color.orangeyellow));
		    	 }else if (portionTime > 1/4){
		    		 btnNumber01.setBackground(getResources().getDrawable(R.drawable.number01_timeout));
		    		 btnNumber02.setBackground(getResources().getDrawable(R.drawable.number02_timeout));
		    		 btnNumber03.setBackground(getResources().getDrawable(R.drawable.number03));
		    		 btnNumber04.setBackground(getResources().getDrawable(R.drawable.number04));
		    		 btnNumber01.setTextColor(getResources().getColor(R.color.white1));
		    		 btnNumber02.setTextColor(getResources().getColor(R.color.white1));
		    		 btnNumber03.setTextColor(getResources().getColor(R.color.orangeyellow));
		    		 btnNumber04.setTextColor(getResources().getColor(R.color.orangeyellow));
		    	 }else if (portionTime > 1/8){
		    		 btnNumber01.setBackground(getResources().getDrawable(R.drawable.number01_timeout));
		    		 btnNumber02.setBackground(getResources().getDrawable(R.drawable.number02_timeout));
		    		 btnNumber03.setBackground(getResources().getDrawable(R.drawable.number03_timeout));
		    		 btnNumber04.setBackground(getResources().getDrawable(R.drawable.number04));
		    		 btnNumber01.setTextColor(getResources().getColor(R.color.white1));
		    		 btnNumber02.setTextColor(getResources().getColor(R.color.white1));
		    		 btnNumber03.setTextColor(getResources().getColor(R.color.white1));
		    		 btnNumber04.setTextColor(getResources().getColor(R.color.orangeyellow));
		    	 }else {
		    		 btnNumber01.setBackground(getResources().getDrawable(R.drawable.number01_timeout));
		    		 btnNumber02.setBackground(getResources().getDrawable(R.drawable.number02_timeout));
		    		 btnNumber03.setBackground(getResources().getDrawable(R.drawable.number03_timeout));
		    		 btnNumber04.setBackground(getResources().getDrawable(R.drawable.number04_timeout));
		    		 btnNumber01.setTextColor(getResources().getColor(R.color.white1));
		    		 btnNumber02.setTextColor(getResources().getColor(R.color.white1));
		    		 btnNumber03.setTextColor(getResources().getColor(R.color.white1));
		    		 btnNumber04.setTextColor(getResources().getColor(R.color.white1));
		    	 } 
		    	 //timerLine.setLayoutParams(new LayoutParams((int) Math.floor(initWidth * (millisUntilFinished/(finalTimer*1000))), timerLine.getLayoutParams().height));
//		    	 timerLine.getLayoutParams().width = (int) Math.floor(initWidth * (millisUntilFinished/(finalTimer*1000)));
		     }

		     public void onFinish() {
//		    	 clock.setX((int)initFinishPos);
		    	 System.status = System._STATUS_TIME_OUT;
		    	 FillData();
		     }
		  }.start();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(aCountDownTimer!=null && System.timeleft > 0)
		{
			resetTimer(System.step, 
//					timerLine, txtResult, 
					System.timeleft/1000, true);
		}
		////Music.play(this, R.raw.soundtrack01);
		/*if (adView != null) {
		      adView.resume();
		}*/
	}
	@Override
	protected void onPause()
	{
		if(aCountDownTimer!=null)
		{
			aCountDownTimer.cancel();
		}
		/*if (adView != null) {
		      adView.pause();
		}*/
		super.onPause();
		////Music.stop(this);
	}
	
	 /** Called before the activity is destroyed. */
	  @Override
	  public void onDestroy() {
	    // Destroy the AdView.
//	    if (adView != null) {
//	      adView.destroy();
//	    }
	    super.onDestroy();
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
    
    private void shareScreen()
    {
    	View view =  findViewById(R.id.mainlayout);//your layout id
        view.getRootView();
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) 
        {
            File picDir  = new File(Environment.getExternalStorageDirectory()+ "/Pictures/Screenshots");
            if (!picDir.exists())
            {
                picDir.mkdir();
            }
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache(true);
            Bitmap bitmap = view.getDrawingCache();
//          Date date = new Date();
            String fileName = "letsgomath.jpg";
            File picFile = new File(picDir + "/" + fileName);
            try 
            {
                picFile.createNewFile();
                FileOutputStream picOut = new FileOutputStream(picFile);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), (int)(bitmap.getHeight()/1.2));
                boolean saved = bitmap.compress(CompressFormat.JPEG, 100, picOut);
                if (saved) 
                {
                    Toast.makeText(getApplicationContext(), "Screenshot will be saved to " + picFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    sharingIntent.setType("image/jpeg");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + picFile.getAbsolutePath()));
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "Check out Let's Go Math at https://play.google.com/store/apps/details?id=lx.tung.letsgomath , one of the most difficult but yet fun Math game! My highscore is " + System.HIGH_SCORE + "! What about yours?");
                    
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                } else 
                {
                    //Error
                }
                picOut.close();
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
            view.destroyDrawingCache();
        } else {
        	Toast.makeText(getApplicationContext(), "Storage not available!", Toast.LENGTH_SHORT).show();

        }
    }
}
