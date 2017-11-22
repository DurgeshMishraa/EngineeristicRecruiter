package com.engineeristic.recruiter.chat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ChatPreferences {

	private static SharedPreferences mSharedPrefs;
	private static ChatPreferences chatPreferences= new ChatPreferences();
	
	private ChatPreferences()
	{			
	}
	public static ChatPreferences getChatSharedInstance()
	{
		if(chatPreferences==null)
		{
			return new ChatPreferences();
		}
		return chatPreferences;
	}
	
	public void setContextPreference(Context context)
	{
		mSharedPrefs = context.getSharedPreferences(ChatConstants.CHAT_PREFS,context.MODE_PRIVATE);
	}
	
	
	public void setChatPrefernce(String key,String Value)
	{
		SharedPreferences.Editor edit = mSharedPrefs.edit();
		edit.putString(key, Value);
		edit.apply();
	}
	public String getChatPreference(String key)
	{
		return mSharedPrefs.getString(key,"Anonymous");
	}
	
	public void setChatPrefernceChatCount(String key, Map<String, Integer> Value)
	{
		try {
			SharedPreferences.Editor edit = mSharedPrefs.edit();
			JSONObject jsonObject = new JSONObject(Value);
			String jsonString = jsonObject.toString();        
			edit.remove(key).commit();
			edit.putString(key, jsonString);
			edit.commit();
		}
		catch (Exception e) 
		{
			Log.e("Chat Preference :","setChatPrefernceChatCount() Exception "+ e.getMessage());
		}
	}
	public  Map<String, Integer> getChatPreferenceChatCount(String key)
	{
		Map<String, Integer> outputMap=new HashMap<String, Integer>();
		try {

			String jsonString = mSharedPrefs.getString(key,(new JSONObject()).toString());
			JSONObject jsonObject = new JSONObject(jsonString);
			Iterator<String> keysItr = jsonObject.keys();
			while(keysItr.hasNext()) {
				String keys = keysItr.next();
				Integer value = (Integer) jsonObject.get(keys);
				outputMap.put(keys, value);
			}

		}
		catch (Exception e) 
		{
			Log.e("Chat Preference :","getChatPreferenceChatCount() Exception "+ e.getMessage());
		}

		return outputMap;
	}
	public void removeChatPreferenceValue(String key)
	{
		SharedPreferences.Editor edit = mSharedPrefs.edit();
		edit.remove(key);
		edit.apply();
	}
}
