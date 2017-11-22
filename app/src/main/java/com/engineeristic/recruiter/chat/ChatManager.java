package com.engineeristic.recruiter.chat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.engineeristic.recruiter.R;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class ChatManager {


	public Pubnub mPubNub;
	//	public Pubnub mPubNubChatService;	
	private long loadhistorytime;
	private JSONObject jsonfinal=new JSONObject() ;
	private static ChatManager chatManager =new ChatManager();
	private String lasthistorymessage="";
	private long exportchattime;
	private JSONObject jsonPresence=new JSONObject();
	ChatApiCallback chatApiCallback;
	public Set<String> usersOnline = new HashSet<String>();
	public Map<String, Integer> chatCount = new HashMap<String,Integer>();
	public String usernameForcount=""; 
	public static String dateMatch="";
	public static String dateMatchToday="";
	public static Set<String> dateMatchHistory = new HashSet<String>();
	protected static final String tag = "ChatManager";
	public String REQUEST_STRING="NotifyChannel";
	public String RESPONSE_STRING = "myResponse";
	public String RESPONSE_MESSAGE = "myResponseMessage";
	public String[] channelsMyChat=null; 
	public long timeMatch;
	public boolean isNewSeeker=false;

	private ChatManager(){

	}

	public void setChatApiCallback(ChatApiCallback chatApiCb)
	{
		chatApiCallback=chatApiCb;
		usersOnline = new HashSet<String>();
		dateMatchHistory = new HashSet<String>();
	}

	public static ChatManager getInstance()
	{
		if(chatManager==null)
		{
			chatManager=new ChatManager();
			return chatManager;
		}	
		return chatManager;
	}


	public JSONObject getJsonPresence() {
		return jsonPresence;
	}

	public void setJsonPresence(JSONObject jsonPresence) {
		this.jsonPresence = jsonPresence;
	}

	public long getExportchattime() 
	{
		return exportchattime;
	}

	public void setExportchattime(long exportchattime) {
		this.exportchattime = exportchattime;
	}

	public String getLasthistorymessage() {
		return lasthistorymessage;
	}

	public void setLasthistorymessage(String lasthistorymessage) {
		this.lasthistorymessage = lasthistorymessage;
	}

	public JSONObject getJsonfinal() {
		return jsonfinal;
	}
	public void setJsonfinal(JSONObject jsonfinal) {
		this.jsonfinal = jsonfinal;
	}
	public long getLoadhistorytime() {
		return loadhistorytime;
	}
	public void setLoadhistorytime(long loadhistorytime) {
		this.loadhistorytime = loadhistorytime;
	}
	public  void initializePubNub(){
		if(mPubNub==null)
		{
			mPubNub = new Pubnub(ChatConstants.PUBLISH_KEY, ChatConstants.SUBSCRIBE_KEY);
		}

		//subscribeWithPresence();
		//history();
		//gcmRegister();
	}

	public ArrayList<ChatMessage> lasthistorymessage(String[] contactType)
	{		
		final ArrayList<ChatMessage> arrayListLastMsg=new ArrayList<ChatMessage>();
		for (int j=0;j<contactType.length;j++)
		{
			mPubNub.history(contactType[j],1,false,new Callback() {
				@Override
				public void successCallback(String channel, final Object message) {
					try {
						JSONArray json = (JSONArray) message;
						Log.d("History", json.toString());	

						final JSONArray messages = json.getJSONArray(0);	
						if(messages.length()>0)
						{
							for (int i = 0; i < messages.length(); i++) {
								JSONObject jsonMsg = messages.getJSONObject(i).getJSONObject("data");
								String name = jsonMsg.getString(ChatConstants.JSON_USER);
								String msg  = jsonMsg.getString(ChatConstants.JSON_MSG);
								long time   = jsonMsg.getLong(ChatConstants.JSON_TIME);					
								ChatMessage chatMessage =new ChatMessage(name, msg, time);
								arrayListLastMsg.add(chatMessage);
							}
						}
						else
						{
							ChatMessage chatMessage=null;
							arrayListLastMsg.add(chatMessage);
						}


					} catch (JSONException e){ e.printStackTrace(); }
				}

				@Override
				public void errorCallback(String channel, PubnubError error) {
					Log.d("History", error.toString());
				}
			});
		}

		return arrayListLastMsg;

	}

	public String lasthistorymessage(String contactType)
	{		

		//final ArrayList<ChatMessage> arrayListLastMsg=new ArrayList<ChatMessage>();
		/*for (int j=0;j<contactType.length;j++)
		{*/
		mPubNub.history(contactType,1,false,new Callback() {
			@Override
			public void successCallback(String channel, final Object message) {
				try {
					JSONArray json = (JSONArray) message;
					Log.d("History", json.toString());	

					final JSONArray messages = json.getJSONArray(0);	
					if(messages.length()>0)
					{
						for (int i = 0; i < messages.length(); i++) {
							JSONObject jsonMsg = messages.getJSONObject(i).getJSONObject("data");
							String name = jsonMsg.getString(ChatConstants.JSON_USER);
							String msg =jsonMsg.getString(ChatConstants.JSON_MSG);
							long time   = jsonMsg.getLong(ChatConstants.JSON_TIME);					
							//ChatMessage chatMessage =new ChatMessage(name, msg, time);
							//arrayListLastMsg.add(chatMessage);
							setLasthistorymessage(msg);
						}
					}
					else
					{
						//ChatMessage chatMessage=null;
						//arrayListLastMsg.add(chatMessage);
						setLasthistorymessage("");
					}


				} catch (JSONException e){ e.printStackTrace(); }
			}

			@Override
			public void errorCallback(String channel, PubnubError error) {
				Log.d("History", error.toString());
			}
		});
		//}

		return getLasthistorymessage();

	}

	public void presenceSubscribe(String channel)  {
		Callback callback = new Callback() {
			@Override
			public void successCallback(String channel, Object response) {
				Log.i("PN-pres","Pres: " + response.toString() + " class: " + response.getClass().toString());
				if (response instanceof JSONObject){
					JSONObject json = (JSONObject) response;
					Log.d("PN-main","Presence: " + json.toString());
					try {
						final int occ = json.getInt("occupancy");
						final String user = json.getString("uuid");
						final String action = json.getString("action");
						//chatApiCallback.presenceSubscribeCallback(json);
					} catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
			}

			@Override
			public void errorCallback(String channel, PubnubError error) {
				Log.d("Presence", "Error: " + error.toString());
			}
		};
		try {
			ChatManager.getInstance().mPubNub.presence(channel, callback);
		} catch (PubnubException e) { e.printStackTrace(); }
	}

	public void initPubNub(Activity activity){

		try {
			//ChatManager.getInstance().initializePubNub();
			/*ChatManager.getInstance().mPubNub.setUUID(ChatPreferences.getChatSharedInstance()
					.getChatPreference(ChatConstants.CHAT_UUID));*/

			GCM_RegUnReg.getGCM_RegUnRegInstance().setActivityContext(activity);
			GCM_RegUnReg.getGCM_RegUnRegInstance().gcmRegister();


		} catch (Exception e) {
			Log.e("ChatActivity ", "initPubNub() "+e.getMessage());
		}
	}

	public void subcribeChannels(String[] channels)
	{
		Callback subscribeCallback = new Callback() {
			@Override
			public void connectCallback(String channel, Object message) {
				System.out.println("SUBSCRIBE : CONNECT on channel:" + channel
						+ " : " + message.getClass() + " : "
						+ message.toString());
				//hereNow(channel);
				//presenceSubscribe(channel);
			}

			@Override
			public void disconnectCallback(String channel, Object message) {
				System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
						+ " : " + message.getClass() + " : "
						+ message.toString());

			}


			public void reconnectCallback(String channel, Object message) {
				System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
						+ " : " + message.getClass() + " : "
						+ message.toString());
			}

			@Override
			public void successCallback(String channel, Object message) {
				System.out.println("SUBSCRIBE : " + channel + " : "
						+ message.getClass() + " : " + message.toString());

				try {
					if(chatApiCallback!=null)
					{
						JSONObject jsonObj = (JSONObject) message;
						String name;
						name = jsonObj.getString("name");
						String msg =jsonObj.getString("msg");
						long time =jsonObj.getLong("time");					
						timeMatch=time;

						ChatMessage chatMsg = new ChatMessage(name, msg, time);
						validateDateHistory(chatMsg,time);					
						chatApiCallback.subscribeCallback(chatMsg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}




				/*if (uuid.equals(ChatManager.getInstance().mPubNub.getUUID()))
				{							
					return; // Ignore own messages
				}*/




			}

			@Override
			public void errorCallback(String channel, PubnubError error) {
				System.out.println("SUBSCRIBE : ERROR on channel " + channel
						+ " : " + error.toString());
			}};
			try {
				ChatManager.getInstance().mPubNub.subscribe(channels, subscribeCallback);

			} catch (PubnubException e){ e.printStackTrace();}
	}

	public void groupSubscribe(String groupName,String[] channels)
	{
		channelGroupAddChannels(groupName,channels);
		channelGroupSubscribe(groupName);
	}
	private void channelGroupAddChannels(String groupName,String[] channels)
	{
		Callback subscribeCallback = new Callback() {
			@Override
			public void connectCallback(String channel, Object message) {
				System.out.println("SUBSCRIBE : CONNECT on channel:" + channel
						+ " : " + message.getClass() + " : "
						+ message.toString());
				//hereNow(channel);
				//presenceSubscribe(channel);
			}

			@Override
			public void disconnectCallback(String channel, Object message) {
				System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
						+ " : " + message.getClass() + " : "
						+ message.toString());

			}


			public void reconnectCallback(String channel, Object message) {
				System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
						+ " : " + message.getClass() + " : "
						+ message.toString());
			}



			@Override
			public void successCallback(String channel, Object message) {
				System.out.println("SUBSCRIBE : " + channel + " : "
						+ message.getClass() + " : " + message.toString());

				try {
					//JSONObject jsonObj = (JSONObject) message;
					/*if(chatApiCallback!=null)
					{
						JSONObject jsonObj = (JSONObject) message;
						String name;
						name = jsonObj.getString("name");
						String msg =jsonObj.getString("msg");
						long time =jsonObj.getLong("time");					
						timeMatch=time;

						ChatMessage chatMsg = new ChatMessage(name, msg, time);
						validateDateHistory(chatMsg,time);					
						chatApiCallback.subscribeCallback(chatMsg);
					}*/
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}



			}

			@Override
			public void errorCallback(String channel, PubnubError error) {
				System.out.println("SUBSCRIBE : ERROR on channel " + channel
						+ " : " + error.toString());
			}};
			try {
				ChatManager.getInstance().mPubNub.channelGroupAddChannel(groupName, channels, subscribeCallback);

			} catch (Exception e)
			{
				e.printStackTrace();
			}
	}
	private void channelGroupSubscribe(String groupName)
	{
		Callback subscribeCallback = new Callback() {
			@Override
			public void connectCallback(String channel, Object message) {
				System.out.println("SUBSCRIBE : CONNECT on channel:" + channel
						+ " : " + message.getClass() + " : "
						+ message.toString());
				//hereNow(channel);
				//presenceSubscribe(channel);
			}

			@Override
			public void disconnectCallback(String channel, Object message) {
				System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
						+ " : " + message.getClass() + " : "
						+ message.toString());

			}


			public void reconnectCallback(String channel, Object message) {
				System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
						+ " : " + message.getClass() + " : "
						+ message.toString());
			}



			@Override
			public void successCallback(String channel, Object message) {
				System.out.println("SUBSCRIBE : " + channel + " : "
						+ message.getClass() + " : " + message.toString());

				try {
					if(chatApiCallback!=null)
					{
						JSONObject jsonObj = (JSONObject) message;
						String name;
						name = jsonObj.getString("name");
						String msg =jsonObj.getString("msg");
						long time =jsonObj.getLong("time");	
						String uuid =jsonObj.getString("UUID");
						timeMatch=time;

						ChatMessage chatMsg = new ChatMessage(name, msg, time);
						chatMsg.setChannelName(channel);
						chatMsg.setUsr(uuid);
						validateDateHistory(chatMsg,time);					
						chatApiCallback.subscribeCallback(chatMsg);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}




				/*if (uuid.equals(ChatManager.getInstance().mPubNub.getUUID()))
				{							
					return; // Ignore own messages
				}*/




			}

			@Override
			public void errorCallback(String channel, PubnubError error) {
				System.out.println("SUBSCRIBE : ERROR on channel " + channel
						+ " : " + error.toString());
			}};
			try {
				ChatManager.getInstance().mPubNub.channelGroupSubscribe(groupName,subscribeCallback);

			} catch (PubnubException e)
			{
				e.printStackTrace();
			}
	}


	public void whereNow()
	{
		try {

			ChatManager.getInstance().mPubNub.whereNow(ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.CHAT_UUID),new Callback() 
			{
				@Override
				public void successCallback(String channel, Object response) {
					// TODO Auto-generated method stub
					super.successCallback(channel, response);

					JSONObject json = (JSONObject) response;
				}


			});

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void hereNow(String channel) {
		ChatManager.getInstance().mPubNub.hereNow(channel,new Callback(){
			@Override
			public void successCallback(String channel, Object response) {
				try {
					JSONObject json = (JSONObject) response;
					final int occ = json.getInt("occupancy");
					final JSONArray hereNowJSON = json.getJSONArray("uuids");
					Log.d("JSON_RESP", "Here Now: " + json.toString());

					//usersOnline.add(username);
					for (int i = 0; i < hereNowJSON.length(); i++) {
						usersOnline.add(hereNowJSON.getString(i));
					}				

					if(chatApiCallback!=null)
					{
						chatApiCallback.herenNowCallback(usersOnline);
					}
					/*ChatActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							//mHereNow.setTitle(String.valueOf(occ));
							textView_occupancy.setText(occ+"");
							mChatAdapter.setOnlineNow(usersOnline);
							if (displayUsers)
								alertHereNow(usersOnline);
						}
					});*/
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setStateLogin(){
		Callback callback = new Callback() {
			@Override
			public void successCallback(String channel, Object response) {
				Log.d("PUBNUB", "State: " + response.toString());
			}
		};
		try {
			JSONObject state = new JSONObject();
			state.put(ChatConstants.STATE_LOGIN, System.currentTimeMillis());
			ChatManager.getInstance().mPubNub.setState(ChatPreferences.getChatSharedInstance().
					getChatPreference(ChatConstants.CHAT_ROOM), ChatManager.getInstance().mPubNub.getUUID(), state, callback);
		}
		catch (Exception e) {
			Log.e("ChatActivity ", "setStateLogin() "+e.getMessage());
		}
	}

	public void getStateLogin(final String user){
		Callback callback = new Callback() {
			@Override
			public void successCallback(String channel, Object response) {
				if (!(response instanceof JSONObject)) return; // Ignore if not JSON
				try {
					JSONObject state = (JSONObject) response;
					final boolean online = state.has(ChatConstants.STATE_LOGIN);
					final long loginTime = online ? state.getLong(ChatConstants.STATE_LOGIN) : 0;

					Log.d("PUBNUB", "State: " + response.toString());
				} catch (JSONException e){ e.printStackTrace(); }
			}
		};
		ChatManager.getInstance().mPubNub.getState(ChatPreferences.getChatSharedInstance().
				getChatPreference(ChatConstants.CHAT_ROOM), user, callback);
	}

	public void history(final String userName)
	{

		ChatManager.getInstance().mPubNub.history(ChatPreferences.getChatSharedInstance().
				getChatPreference(ChatConstants.CHAT_ROOM),50,false,new Callback() {
			boolean checkHistCount=false;
			List<ChatMessage> chatMsgs=new ArrayList<ChatMessage>();
			@Override
			public void successCallback(String channel, final Object message) {
				try {
					JSONArray json = (JSONArray) message;

					Log.i(tag,"History()"+ json.toString());

					ChatManager.getInstance().setLoadhistorytime(json.getLong(1));
					ChatManager.getInstance().setExportchattime(json.getLong(1));
					final JSONArray messages = json.getJSONArray(0);	
					JSONObject jsonMsg=new JSONObject();
					if(messages.length()<50)
					{
						checkHistCount=true;
					}
					for (int i = 0; i < messages.length(); i++) {
						jsonMsg = messages.getJSONObject(i);
						String msg  = jsonMsg.getString("msg");							
						String name=jsonMsg.getString("name");	
						long time  = jsonMsg.getLong("time");
						String uuid =jsonMsg.getString("UUID");

						ChatMessage chatMsg = new ChatMessage(name, msg, time);
						chatMsg.setUsr(uuid);
						validateDateHistory(chatMsg,time);
						chatMsgs.add(chatMsg);
					}

					chatApiCallback.historyCallback(chatMsgs, checkHistCount);

				} catch (JSONException e){
					e.printStackTrace(); 
				}
			}

			@Override
			public void errorCallback(String channel, PubnubError error) {
				Log.i(tag,"History()"+ error.toString());
			}
		});
	}


	public void loadHistory(final String userName)
	{
		ChatManager.getInstance().mPubNub.history(ChatPreferences.getChatSharedInstance().
				getChatPreference(ChatConstants.CHAT_ROOM),ChatManager.getInstance().getLoadhistorytime(),50, false, new Callback() {
			boolean checkHistCount=false;
			List<ChatMessage> chatMsgs = new ArrayList<ChatMessage>();
			@Override
			public void successCallback(String channel, final Object message) {
				try {
					JSONArray json = (JSONArray) message;
					Log.d("History", json.toString());
					final JSONArray messages = json.getJSONArray(0);
					//loadhistorytime = json.getLong(1);
					ChatManager.getInstance().setLoadhistorytime(json.getLong(1));
					if(messages.length()<50)
					{
						checkHistCount=true;
					}
					if(messages.length()>0)
					{

						for (int i = 0; i < messages.length(); i++) 
						{
							JSONObject jsonMsg = messages.getJSONObject(i);
							String msg  = jsonMsg.getString("msg");							
							String name=jsonMsg.getString("name");	
							long time  = jsonMsg.getLong("time");
							String uuid =jsonMsg.getString("UUID");

							ChatMessage chatMsg = new ChatMessage(name, msg, time);
							chatMsg.setUsr(uuid);
							validateDateHistory(chatMsg,time);

							chatMsgs.add(chatMsg);					

						}

						chatApiCallback.loadhistorymessageCallback(chatMsgs, checkHistCount);

					}
					else
					{
						chatApiCallback.loadhistorymessageCallback(chatMsgs, checkHistCount);
					}
				} catch (JSONException e){ e.printStackTrace(); }
			}

			@Override
			public void errorCallback(String channel, PubnubError error) {
				Log.d("loadHistory", error.toString());
			}
		});


	}

	public static ArrayList<ChatMessage>  lasthistorymessage(String channel,final ArrayList<ChatMessage> arrayListLastMsg)
	{		
		ChatManager.getInstance().mPubNub.history(channel,1,false,new Callback() {
			@Override
			public void successCallback(String channel, final Object message) {
				try {
					JSONArray json = (JSONArray) message;
					Log.d("History", json.toString());					

					final JSONArray messages = json.getJSONArray(0);					
					for (int i = 0; i < messages.length(); i++) {
						JSONObject jsonMsg = messages.getJSONObject(i).getJSONObject("data");
						String name = jsonMsg.getString(ChatConstants.JSON_USER);
						String msg  = jsonMsg.getString(ChatConstants.JSON_MSG);
						long time   = jsonMsg.getLong(ChatConstants.JSON_TIME);					
						ChatMessage chatMessage =new ChatMessage(name, msg, time);
						//validateDateHistory(chatMessage,time);
						arrayListLastMsg.add(chatMessage);
					}

				} catch (JSONException e){ e.printStackTrace(); }
			}

			@Override
			public void errorCallback(String channel, PubnubError error) {
				Log.d("History", error.toString());
			}
		});

		return arrayListLastMsg;

	}

	private static void validateDateHistory(ChatMessage chatMessage,long time)
	{
		if(todayDateformat().equalsIgnoreCase(historyDateformat(time))
				&& !dateMatchHistory.contains(historyDateformat(time)))
		{
			//dateMatchToday=historyDateformat(time);	
			dateMatchHistory.add(historyDateformat(time));
			chatMessage.setDate("Today");
		}
		else if(!dateMatchHistory.contains(historyDateformat(time)))
		{
			//dateMatch=historyDateformat(time);	
			dateMatchHistory.add(historyDateformat(time));
			chatMessage.setDate(historyDateformat(time));

		}
		else
		{
			chatMessage.setDate("");
		}
	}

	private static String historyDateformat(long timeStamp)
	{		 
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");

		// Create a calendar object that will convert the date and time value in milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeStamp);		
		String timeDate=formatter.format(calendar.getTime());
		return timeDate;
	}
	private static String todayDateformat()
	{		 
		long timeStamp=new Date().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");

		// Create a calendar object that will convert the date and time value in milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeStamp);		
		String timeDate=formatter.format(calendar.getTime());

		return timeDate;
	}
	public int setCompleteChatCount()	
	{
		int overAllChatCount=0;
		ChatManager.getInstance().chatCount=ChatPreferences.getChatSharedInstance().
				getChatPreferenceChatCount(ChatConstants.CHAT_COUNT);
		if(ChatManager.getInstance().chatCount!=null)
		{			
			Set<String> keys=ChatManager.getInstance().chatCount.keySet();
			for (String string : keys) {
				overAllChatCount=overAllChatCount+ChatManager.getInstance().chatCount.get(string);
			}
		}

		return overAllChatCount;
	}

	public Animation shakeAnim(Context context)
	{
		Animation shake;
		shake = AnimationUtils.loadAnimation(context, R.anim.shake);

		return shake;
	}

	public void setOnlineStatus(String receiver,String channelRoom) 
	{		
		try {

			/*final Set<String> checkOnline=new HashSet<String>();
			ChatManager.getInstance().mPubNub.hereNow(channelRoom,new Callback() {
				@Override
				public void successCallback(String channel, Object response) {
					try {
						JSONObject json = (JSONObject) response;
						final int occ = json.getInt("occupancy");
						final JSONArray hereNowJSON = json.getJSONArray("uuids");
						Log.d("JSON_RESP", "Here Now: " + json.toString());

						//usersOnline.add(username);
						for (int i = 0; i < hereNowJSON.length(); i++) {
							checkOnline.add(hereNowJSON.getString(i));
						}				
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});*/

			ChatManager.getInstance().mPubNub.whereNow(receiver,new Callback() 
			{
				@Override
				public void successCallback(String channel, Object response) {
					// TODO Auto-generated method stub
					super.successCallback(channel, response);

					JSONObject json = (JSONObject) response;
					json.toString();
				}
				@Override
				public void errorCallback(String arg0, Object arg1) {
					// TODO Auto-generated method stub
					super.errorCallback(arg0, arg1);
					JSONObject json = (JSONObject) arg1;
					json.toString();
				}


			});

		} catch (Exception e) {
			// TODO: handle exception
		}


	}

}
