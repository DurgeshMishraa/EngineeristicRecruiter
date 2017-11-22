package com.engineeristic.recruiter.chat;
import java.io.IOException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class GCM_RegUnReg {

	
	/**
	 * GCM Functionality.
	 * In order to use GCM Push notifications you need an API key and a Sender ID.
	 * Get your key and ID at - https://developers.google.com/cloud-messaging/
	 */
	private GoogleCloudMessaging gcm;
	private String gcmRegId;
	private Activity activityGCM;
	private static GCM_RegUnReg gcm_RegUnReg =new GCM_RegUnReg();
	
	private GCM_RegUnReg()
	{		
	}
	
	public static GCM_RegUnReg getGCM_RegUnRegInstance()
	{
		if(gcm_RegUnReg==null)
		{
			gcm_RegUnReg=new GCM_RegUnReg();
			return gcm_RegUnReg;
		}
		return gcm_RegUnReg;
				
	}
	
	public void setActivityContext(Activity activity)
	{
		activityGCM= activity;
	}

	public void gcmRegister() {
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(activityGCM.getApplicationContext());
			try {
				gcmRegId = getRegistrationId();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (gcmRegId.isEmpty()) {
				registerInBackground();
			} else {
				Toast.makeText(activityGCM,"Registration ID already exists: " + gcmRegId,Toast.LENGTH_SHORT).show();
			}
		} else {
			Log.e("GCM-register", "No valid Google Play Services APK found.");
		}
	}
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activityGCM.getApplicationContext());
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode,activityGCM, ChatConstants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.e("GCM-check", "This device is not supported.");
				activityGCM.finish();
			}
			return false;
		}
		return true;
	}
	private String getRegistrationId() {

		ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.GCM_REG_ID, "");
		return ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.GCM_REG_ID);
	}
	
	private void registerInBackground() {
		new RegisterTask().execute();
	}
	private class RegisterTask extends AsyncTask<Void, Void, String>{
		@Override
		protected String doInBackground(Void... params) {
			String msg="";
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(activityGCM.getApplicationContext());
				}
				gcmRegId = gcm.register(ChatConstants.GCM_SENDER_ID);
				msg = "Device registered, registration ID: " + gcmRegId;

				sendRegistrationId(gcmRegId);

				storeRegistrationId(gcmRegId);
				Log.i("GCM-register", msg);
			} catch (IOException e){
				e.printStackTrace();
			}
			return msg;
		}
	}

	private void sendRegistrationId(String regId) {
		ChatManager.getInstance().mPubNub.enablePushNotificationsOnChannel(ChatPreferences.getChatSharedInstance().
				getChatPreference(ChatConstants.CHAT_USERNAME), regId, new BasicCallback());
	}
	private void storeRegistrationId(String regId) {

		ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.GCM_REG_ID,regId);
	}

	public void gcmUnregister() {
		new UnregisterTask().execute();
	}
	private class UnregisterTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(activityGCM);
				}

				// Unregister from GCM
				gcm.unregister();

				// Remove Registration ID from memory
				removeRegistrationId();

				// Disable Push Notification
				ChatManager.getInstance().mPubNub.disablePushNotificationsOnChannel(ChatPreferences.getChatSharedInstance().
						getChatPreference(ChatConstants.CHAT_USERNAME), gcmRegId);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private void removeRegistrationId() {

		ChatPreferences.getChatSharedInstance().removeChatPreferenceValue(ChatConstants.GCM_REG_ID);
	}

}
