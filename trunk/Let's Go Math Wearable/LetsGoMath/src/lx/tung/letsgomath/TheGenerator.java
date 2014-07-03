package lx.tung.letsgomath;

import java.util.Random;

public class TheGenerator {
	public static void generateQuiz()
	{
		if (System.score < System._STEP_EASY)
		{
			System.numberList = TheGenerator.generate(4, System._DIFF_EASY);
			System.step = System._STEP_EASY;
		}else if (System.score < System._STEP_MEDIUM)
		{
			System.numberList = TheGenerator.generate(4, System._DIFF_MEDIUM);
			System.step = System._STEP_MEDIUM;
		}else if (System.score < System._STEP_HARD)
		{
			System.numberList = TheGenerator.generate(4, System._DIFF_HARD);
			System.step = System._STEP_HARD;
		}else
		{
			System.numberList = TheGenerator.generate(4, System._DIFF_GOD);
			System.step = System._STEP_GOD;
		}
	}
	
	public static TheList generate(int amount, int max)
	{
		TheList aList = new TheList();
		int[] listInt = new int[amount];
		Random rand1 = new Random();
		Random rand = new Random(rand1.nextInt());
		for (int i = 0; i < amount - 1; i++)
		{
			listInt[i] = rand.nextInt(max) * (int)Math.pow(-1, rand.nextInt());
			if (i == amount-2 && checkAllPosOrNeg(listInt))
			{
				i = 0;
			}
		}
		listInt[listInt.length - 1] = calculateTotal(listInt);
		aList.setTheResult(listInt[listInt.length - 1]);
		shuffleArray(listInt);
		aList.setTheList(listInt);
		return aList;
	}
	
	public static int calculateTotal(int[] listInt)
	{
		int total = 0;
		for (int i = 0; i < listInt.length - 1; i++)
		{
			total += listInt[i];
		}
		return total;
	}
	
	public static boolean checkAllPosOrNeg(int[]listInt)
	{
		boolean returnValue = true;
		for(int i = 0; i < listInt.length - 2; i++) // -2 vi bo theResult
		{
			if (listInt[i] * listInt[i+1] < 0)
			{
				returnValue = false;
			}
		}
		return returnValue;
	}
	
	// Implementing Fisher–Yates shuffle
	public static void shuffleArray(int[] ar)
	  {
	    Random rnd = new Random();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      int a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	  }
}
