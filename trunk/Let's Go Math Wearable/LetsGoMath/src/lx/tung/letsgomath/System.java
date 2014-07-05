package lx.tung.letsgomath;

import java.util.Random;

import android.graphics.Typeface;
import android.os.CountDownTimer;

public class System {
	public static int score = 0;
	public static Typeface tfComicSerifPro;
	public static Typeface tfCartoonShout;
	public static final String FILENAME = "result.txt";
	public static int CURRENT_SCORE = 0;
	public static int HIGH_SCORE = 0;
	public static long timeleft = 0;
	
	public static int indexComment = 0;

	public static int _DIFF_EASY = 5;
	public static int _DIFF_MEDIUM = 10;
	public static int _DIFF_HARD = 100;
	public static int _DIFF_GOD = 500;
	
	public static int _STEP_EASY = 10;
	public static int _STEP_MEDIUM = 20;
	public static int _STEP_HARD = 30;
	public static int _STEP_GOD = 70;
	public static int step = _STEP_EASY;
	
	public static int _TIMER_EASY = 8;
	public static int _TIMER_MEDIUM = 11;
	public static int _TIMER_HARD = 17;
	public static int _TIMER_GOD = 22;
	
	public static CountDownTimer aCountDownTimer;
	
	public static int _STATUS_TIME_OUT = -2;
	public static int _STATUS_FAIL = -1;
	public static int _STATUS_SUCCESS = 1;
	public static int _STATUS_INIT = 0;
	public static int status = _STATUS_INIT;
	
	public static String[] commentSuccess= {
		"Hoorraay!!!!!",
		"Lucky Shot...",
		"You did it!",
		"Well Done!"		
	};
	public static String[] commentFail= {
		"Oh man!",
		"Seriously? That was a piece of cake.",
		"You can do better next time :)",
		"Game over LOL"
	};
	
	public static String[] commentInit= {
		"Find the number that is sum of the other 3 numbers.",
	};
	
	public static String[] commentTimeOut= {
		"Time out!",
		"What takes you so long? :(",
		"Hurry up next time, shall we? :)",
		"Ok, too slow, let's start again :("
	};
	
	public static TheList numberList;
	
	
	public static void resetScore()
	{
		score = 0;
	}
	
	public static void increaseScore()
	{
		score++;
	}
	
	public static String getCommentSuccess()
	{
		Random rand = new Random();
		return commentSuccess[rand.nextInt(4)];
	}
	
	public static String getCommentFail()
	{
		Random rand = new Random();
		return commentFail[rand.nextInt(commentFail.length)];
	}
	
	public static String getCommentInit()
	{
		Random rand = new Random();
		return commentInit[rand.nextInt(commentInit.length)];
	}
	
	public static String getCommentTimeOut()
	{
		Random rand = new Random();
		return commentTimeOut[rand.nextInt(commentTimeOut.length)];
	}
	

}
