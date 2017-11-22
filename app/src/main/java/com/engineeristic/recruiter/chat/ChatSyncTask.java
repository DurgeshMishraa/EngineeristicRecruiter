package com.engineeristic.recruiter.chat;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.engineeristic.recruiter.myapp.RecruiterApplication;

import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.GsonContextLoader;
import com.engineeristic.recruiter.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class ChatSyncTask extends AsyncTask<String,Void,String> {

	Activity chatHandleActivity;
	String jobId="";
	ChatApiCallback chatApiCallback;
	private InputStream is = null;
	private JSONObject jObj = null;
	private String json = "";
	private int apiCall;
	private Context mContext;

	public ChatSyncTask(String jobid,ChatApiCallback chatApiCb,int apiCall, Context nContext) {
		jobId=jobid;
		chatApiCallback=chatApiCb;
		this.apiCall=apiCall;
		this.mContext = nContext;
		//Log.e("Chat Sync","555");

	}
	public ChatSyncTask(int apiCall)
	{
		//Log.e("Chat Sync","666");
		this.apiCall=apiCall;
	}
	@Override
	protected void onPreExecute() {
		//Log.e("Chat Sync","777");
		super.onPreExecute();
	}
	@Override
	protected String doInBackground(String... params) {
		//Log.e("Chat Sync","888");
		return "";
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		chatApicall(apiCall);
	}

	private String chatApicall(int apiCall)
	{
		String result = "";
		JSONObject  json=null;
		try
		{
			switch (apiCall) {
				case ChatConstants.START_CHAT:
					json = getJosnData(Constant.URL_STARTCHAT+jobId+"/"+Constant.USER_COOKIE);
					break;
				case ChatConstants.MY_CHAT:
					json = getJosnData(Constant.URL_MYCHAT+Constant.USER_COOKIE);
					break;
				case ChatConstants.PRE_API_MY_CHAT:
					json = getJosnData(Constant.URL_MYCHAT+ Constant.USER_COOKIE);
					break;


				default:
					break;
			}


			if(json!=null)
			{
				result = json.toString();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	private void precallList(String result)
	{
		if(result==null||result.length()==0)
		{
			new ChatSyncTask(apiCall).execute();
			//Log.e("Chat Sync","3333");
		}
		else
		{
			try {

				JSONObject jsonObject= new JSONObject(result);
				if(jsonObject.get("status")!=null && jsonObject.get("status").equals(200) &&
						jsonObject.get("profile")!=null && !jsonObject.get("profile").equals(null) &&
						jsonObject.get("chat")!=null && !jsonObject.get("chat").equals(null))
				{
					//Log.e("Chat Sync","444");
					if(ChatPreferences.getChatSharedInstance()!=null && ChatManager.getInstance().mPubNub!=null)
					{
					ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.MY_CHAT_PROFILE_RESPONSE,result.toString());

					JSONObject jsonObjectProfile=jsonObject.getJSONObject("profile");
					MyChatProfileBO myChatProfileBO = GsonContextLoader.getGsonContext().fromJson(jsonObjectProfile.toString(),MyChatProfileBO.class);
					ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.CHAT_UUID, myChatProfileBO.getUsrr());
					//ChatManager.getInstance().initPubNub(MyChat.this);
					ChatManager.getInstance().mPubNub.setUUID(ChatPreferences.getChatSharedInstance()
							.getChatPreference(ChatConstants.CHAT_UUID));


					JSONArray jsonObjectChatsRec=jsonObject.getJSONArray("chat");
					String[] channesl=new String[jsonObjectChatsRec.length()] ;

					for (int i = 0; i < jsonObjectChatsRec.length(); i++) {

						MYChatListBO myChatListBO=GsonContextLoader.getGsonContext().fromJson(jsonObjectChatsRec.get(i).toString(),MYChatListBO.class);

						channesl[i]=myChatListBO.getChannelname();
						//ChatManager.getInstance().hereNow(myChatListBO.getChannelname());
					}
					ChatManager.getInstance().channelsMyChat=channesl;
					//ChatManager.getInstance().subcribeChannels(channesl);
					ChatManager.getInstance().groupSubscribe("engineeristic--"+ChatPreferences.getChatSharedInstance()
							.getChatPreference(ChatConstants.CHAT_UUID), channesl);
					if(chatApiCallback!=null)
					{
						chatApiCallback.onPostResponse(result,apiCall);
					}
				}
				}
				else
				{
					Log.i("Chat Sync","Pre MyChat Sorry no chat available.");

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	private JSONObject getJosnData(String url){
		RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
		StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				if(response != null) {
					if(apiCall==ChatConstants.MY_CHAT || apiCall==ChatConstants.START_CHAT)
					{
						chatApiCallback.onPostResponse(response,apiCall);
						//Log.e("Chat Sync","111");
					}
					else
					{
						precallList(response);
						//Log.e("Chat Sync","2222");
					}
				}
			}
		},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(final VolleyError error) {
						if(apiCall==ChatConstants.MY_CHAT || apiCall==ChatConstants.START_CHAT)
						{
							chatApiCallback.onPostResponse("",apiCall);
						}
						else
						{
							precallList("");
						}
					}
				}){
			@Override
			public Map getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();

				if(!Constant.isLiveServer){
					String base64EncodedCredentials = "Basic " + Base64.encodeToString(
							("mba" + ":" + "iim1@2#3$").getBytes(),
							Base64.NO_WRAP);
					headers.put("Authorization", base64EncodedCredentials);
				}
				headers.put("User-agent", System.getProperty("http.agent")+" engineeristic Recruiter "+ Utility.getAppVersionName());
				return headers;
			}

		};

		RetryPolicy policy = new DefaultRetryPolicy(Constant.SOCKET_TIMEOUT_DURATION, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		stringRequest.setRetryPolicy(policy);
		queue.add(stringRequest);
		return jObj;
	}
}
