package lx.tung.mysos.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Utilities {
	public static boolean enableNetwork(Context mContext){
		if(!isOnline(mContext)){
			setMobileDataEnabled(mContext, true);
			return false;
		}
		return true;
	}
	
	public static void setMobileDataEnabled(Context context, boolean enabled) {
	    final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    Class conmanClass;
		try {
			conmanClass = Class.forName(conman.getClass().getName());
			final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
		    iConnectivityManagerField.setAccessible(true);
		    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
		    final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
		    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		    setMobileDataEnabledMethod.setAccessible(true);

		    setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isOnline(Context mContext) {
	    ConnectivityManager cm =
	        (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}
	
	public static boolean checkLocationService(Context mContext){
		LocationManager lm = null;
		boolean gps_enabled = false, network_enabled = false;
		if(lm == null){
			lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		}
		try{
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		}catch(Exception ex){
			return false;
		}
		try{
			network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		}catch(Exception ex){
			return false;
		}
		if(!gps_enabled && !network_enabled){
			Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			mContext.startActivity(i);
		}
		
		return true;
	}
	
	public static String createMessage(String aLat, String aLong){
		String message = "SOS!!! http://mapsto.me/?lat=" + aLat +"&long=" + aLong;
		return message;
	}
	
	public static void sendSms(Context mContext, String aLat, String aLong, String phoneNum){
		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNum, null, createMessage(aLat, aLong), null, null);
			Toast.makeText(mContext, "SMS Sent!", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(mContext, "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}		
	}
}
