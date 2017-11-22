package com.engineeristic.recruiter.chat;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.model.StartChatBO;
import com.engineeristic.recruiter.myapp.RecruiterApplication;
import com.engineeristic.recruiter.pojo.UpdateAppData;
import com.engineeristic.recruiter.util.AccountUtils;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.ConstantFontelloID;
import com.engineeristic.recruiter.util.GsonContextLoader;
import com.engineeristic.recruiter.util.Utility;
import com.engineeristic.recruiter.widget.CircleImageView;
import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChatActivity extends ListActivity implements ChatApiCallback,IChatDateHandler{

	private EditText mMessageET;
	private MenuItem mHereNow;
	private ListView mListView;
	private ChatAdapter mChatAdapter;
	private String gcmRegId;
	private List<ChatMessage> chatMsgsList = null;
	TextView textViewLdhistory,textView_chatHeader;
	private ProgressDialog mProgressDialog;
	String dateHistory="";
	String mjobID="";
	String mjobTitle="";
	String mjobPosted="";
	LinearLayout msg_box;
	private RelativeLayout rl_notification_chatactivity;
	StartChatBO chatResponse;
	private CircleImageView imageView_recchatid;
	private ImageView iv_chatonlinestatus;
	private TextView textView_nameRec,textView_linkRec,textView_occupancy;
	private Typeface font;
	private TextView imageView_chatindicator;
	StringBuffer stringbuffer= new StringBuffer();
	private String Tag="ChatActivity";
	private boolean refreshValidator=false;
	String mychat=null;
	private int overAllChatCount=0;
	private TextView mTxtIconBack;
	private Typeface  typeFontello;
	//private boolean isOnRestart = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(UpdateAppData.chat_enable.equals("false")){
				setContentView(R.layout.updateapp);
				TextView headText = (TextView)findViewById(R.id.updateapp_heading);
				TextView headMsg = (TextView)findViewById(R.id.updateapp_msg);
				Button upgradeBtn = (Button) findViewById(R.id.updateapp_btn);
				upgradeBtn.setText("UPGRADE YOUR APP");
				headText.setText("Hi! Thank you for being an avid user of engineeristic.com");
				headMsg.setText(Constant.CHAT_UPGRADE_MSG);
				upgradeBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.UPGRADE_APP)));
					}
				});
			//isOnRestart = false;
		}

		else{
			//isOnRestart = true;
		try {
			setContentView(R.layout.chat_layout);
			if (android.os.Build.VERSION.SDK_INT >= 21) {
				Window window = getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
			}
			AccountUtils.trackerScreen("Chat Message Detail");
			final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chatactivity);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				toolbar.setElevation((float) 10.0);
			}
			ChatPreferences.getChatSharedInstance().setContextPreference(Utility.getContext());
			chatMsgsList = new ArrayList<ChatMessage>();
			stringbuffer = new StringBuffer();
			mjobID = getIntent().getStringExtra(ChatConstants.JOBID_FOR_CHAT);
			font = Typeface.createFromAsset(getAssets(), "DroidSans.ttf");
			mjobTitle = getIntent().getStringExtra(ChatConstants.JOBTITLE_FOR_CHAT);
			mychat = getIntent().getStringExtra("MyChat");
			this.mListView = getListView();
			this.mChatAdapter = new ChatAdapter(this, new ArrayList<ChatMessage>());

			mTxtIconBack = (TextView) findViewById(R.id.txtbackicon_chatactivity);
			typeFontello = Typeface.createFromAsset(getAssets(), "fontello.ttf");
			mTxtIconBack.setTypeface(typeFontello);
			mTxtIconBack.setText(ConstantFontelloID.icon_back_icon);
			this.mMessageET = (EditText) findViewById(R.id.message_et);
			this.msg_box = (LinearLayout) findViewById(R.id.msg_box);
			textView_nameRec = (TextView) findViewById(R.id.textView_nameRec);
			textView_linkRec = (TextView) findViewById(R.id.textView_linkRec);
			textView_occupancy = (TextView) findViewById(R.id.textView_occupancy);
			imageView_recchatid = (CircleImageView) findViewById(R.id.imageView_recchatid);
			iv_chatonlinestatus = (ImageView) findViewById(R.id.iv_chatonlinestatus);
			iv_chatonlinestatus.setVisibility(View.GONE);
			imageView_chatindicator = (TextView) findViewById(R.id.imageView_chatindicator);
			imageView_chatindicator.setTypeface(typeFontello);
			imageView_chatindicator.setText(ConstantFontelloID.icon_notification_bell);
			rl_notification_chatactivity = (RelativeLayout) findViewById(R.id.rl_notification_chatactivity);

			View header = getLayoutInflater().inflate(R.layout.listheaderview, null);
			textViewLdhistory = (TextView) header.findViewById(R.id.textView_loadhistory);
			textView_chatHeader = (TextView) header.findViewById(R.id.textView_chatHeader);

			this.mListView.addHeaderView(header);
			this.mListView.setAdapter(mChatAdapter);
			mChatAdapter.setdateHandler(ChatActivity.this);
			setupListView();

			textViewLdhistory.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if ("Load History".equalsIgnoreCase(textViewLdhistory.getText().toString())) {
						if (Utility.isNetworkAvailable(Utility.getContext())) {
							ChatManager.getInstance().loadHistory(chatResponse.getProfile().getName());
							//loadHistory();
						} else {
							Utility.showToastMessage(getApplicationContext(), "Sorry, you're not connected to the Internet. "
									+ "Please check your connection settings.", Toast.LENGTH_SHORT);
						}
					}
					return false;
				}
			});

			ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.PUSH_NOTIFICATION_ON, "0");
			mTxtIconBack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.PUSH_NOTIFICATION_ON, "1");
					finish();
				}
			});
			textViewLdhistory.setVisibility(View.GONE);
			showPleaseWaitProgressDialog(this);
			ChatManager.getInstance().setChatApiCallback(ChatActivity.this);
			setCompleteChatCount();
			if (mychat == null) {
				new ChatSyncTask(mjobID, ChatActivity.this, ChatConstants.START_CHAT, ChatActivity.this).execute();
			} else {
				if (mychat != null) {
					onPostResponse(mychat, 100);
				}
			}
		} catch (Exception e) {
			Log.e("ChatActivity ", "onCreate() " + e.getMessage());
		}
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				Log.i("Chat Activity ", scrollState + "");
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				textView_chatHeader.setText(dateHistory);
			}
		});
	  }
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.PUSH_NOTIFICATION_ON,"1");
		finish();
	}
	@Override
	protected void onResume() {
		super.onResume();
		Log.i("chat activity", "onResume");
		ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.PUSH_NOTIFICATION_ON,"0");
	}
	@Override
	protected void onPause() {
		super.onPause();
		Log.i("chat activity", "onPause()");
		ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.PUSH_NOTIFICATION_ON,"1");
	}
	private void setfontstyles()
	{
		textView_nameRec.setTypeface(font);
		//textView_compRec.setTypeface(font);
		textView_linkRec.setTypeface(font);
		textView_occupancy.setTypeface(font);
		textViewLdhistory.setTypeface(font);
		mMessageET.setTypeface(font);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		super.onStop();
		/*if (ChatManager.getInstance().mPubNub != null)
			ChatManager.getInstance().mPubNub.unsubscribeAll();*/
	}

	/**
	 * Instantiate PubNub object if it is null. Subscribe to channel and pull old messages via
	 *   history.
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		/*if (ChatManager.getInstance().mPubNub==null){
			initPubNub();
		} else {
			subscribeWithPresence();
			history();
		}*/
	}

	/**
	 * I remove the PubNub object in onDestroy since turning the screen off triggers onStop and
	 *   I wanted PubNub to receive messages while the screen is off.
	 *
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Instantiate PubNub object with username as UUID
	 *   Then subscribe to the current channel with presence.
	 *   Finally, populate the listview with past messages from history
	 */
	public void initPubNub(){

		try {

			if(mychat==null)
			{
				ChatManager.getInstance().initializePubNub();
				String[] channel=new String[1];
				boolean checkChannelExist=false;
				channel[0]=ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.CHAT_ROOM);
				if(ChatManager.getInstance().channelsMyChat!=null && ChatManager.getInstance().channelsMyChat.length>0)
				{

					for (int i = 0; i < ChatManager.getInstance().channelsMyChat.length; i++)
					{
						if(ChatManager.getInstance().channelsMyChat[i].equalsIgnoreCase(channel[0]))
						{
							checkChannelExist=true;
						}
					}

					if(!checkChannelExist)
					{
						ChatManager.getInstance().groupSubscribe("engineeristic--"+ChatPreferences.getChatSharedInstance()
								.getChatPreference(ChatConstants.CHAT_UUID), channel);
					}
				}
				else
				{
					ChatManager.getInstance().groupSubscribe("engineeristic--"+ChatPreferences.getChatSharedInstance()
							.getChatPreference(ChatConstants.CHAT_UUID), channel);
				}
				//subscribeWithPresence();
			}
			mProgressDialog.dismiss();
			ChatManager.getInstance().history(chatResponse.getProfile().getName());
			ChatManager.getInstance().hereNow(ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.CHAT_ROOM));

		} catch (Exception e) {
			Log.e("ChatActivity ", "initPubNub() "+e.getMessage());
		}
	}
	public static ArrayList<ChatMessage>  lasthistorymessage(String channel,final ArrayList<ChatMessage> arrayListLastMsg)
	{
		ChatManager.getInstance().mPubNub.history(channel,1,false,new Callback() {
			@Override
			public void successCallback(String channel, final Object message) {
				try {
					JSONArray json = (JSONArray) message;
					//Log.d("History", json.toString());
					final JSONArray messages = json.getJSONArray(0);
					for (int i = 0; i < messages.length(); i++) {
						JSONObject jsonMsg = messages.getJSONObject(i).getJSONObject("data");
						String name = jsonMsg.getString(ChatConstants.JSON_USER);
						String msg  = jsonMsg.getString(ChatConstants.JSON_MSG);
						long time   = jsonMsg.getLong(ChatConstants.JSON_TIME);
						ChatMessage chatMessage =new ChatMessage(name, msg, time);
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

	/**
	 * Setup the listview to scroll to bottom anytime it receives a message.
	 */
	private void setupAutoScroll(){
		this.mChatAdapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
				mListView.setSelection(mChatAdapter.getCount() - 1);
				// mListView.smoothScrollToPosition(mChatAdapter.getCount()-1);
			}
		});
	}
	/**
	 * On message click, display the last time the user logged in.
	 */
	private void setupListView(){
		this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//ChatMessage chatMsg = mChatAdapter.getItem(position);
				//sendNotification(chatMsg.getUsername());			

			}
		});
	}
	/**
	 * Publish message to current channel.
	 * @param view The 'SEND' Button which is clicked to trigger a sendMessage call.
	 */
	public void sendMessage(View view){


		if(Utility.isNetworkAvailable(Utility.getContext()))
		{
			String message = mMessageET.getText().toString();
			if (message.equals("")) return;
			mMessageET.setText("");
			ChatMessage chatMsg = new ChatMessage(chatResponse.getProfile().getName(),message, new Date().getTime());
			//chatMsg.setUsr(chatResponse.getProfile().getUsr());

			String usr =ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.CHAT_UUID);
			chatMsg.setUsr(usr);
			try {
				JSONObject json = new JSONObject();

				json.put("UUID", ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.CHAT_UUID));
				json.put("time",new Date().getTime());
				json.put("usr",chatResponse.getProfile().getUsr());
				json.put("name",chatResponse.getProfile().getName());
				json.put("tt",chatResponse.getProfile().getTt());
				json.put("msg",message);
				json.put("img",chatResponse.getProfile().getImage());
				//json.put("type",chatResponse.getProfile().getTt());
				json.put("type","1");
				chatMsgsList.add(chatMsg);
				mChatAdapter.addMessage(chatMsg,mListView);
				//validateSendMessage=true;
				ChatManager.getInstance().mPubNub.publish(ChatPreferences.getChatSharedInstance().
						getChatPreference(ChatConstants.CHAT_ROOM), json, new BasicCallback());
				JSONObject jsonNotify = new JSONObject();
				jsonNotify.put("type","1");
				jsonNotify.put("data1",ChatPreferences.getChatSharedInstance().
						getChatPreference(ChatConstants.CHAT_ROOM));
				jsonNotify.put("data2","");
				jsonNotify.put("data", chatResponse.getProfile().getName() +" sent you a message - "+message);
				ChatManager.getInstance().mPubNub.publish(chatResponse.getChat().getNotify(),jsonNotify, new BasicCallback());
				sendMessageByMail(message);
			} catch (JSONException e){
				e.printStackTrace();
			}
		}
	}

	private void sendMessageByMail(final String message){
		if (Utility.isNetworkAvailable(getApplicationContext())){

			String channelName = chatResponse.getChat().getChannelname();
			String seekerId = channelName.substring(channelName.indexOf("-j")+2,channelName.length());
			String url =  Constant.SEND_CHAT_MAIL+seekerId+"/"+Constant.USER_COOKIE;
			RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
			StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							if(response != null) {
								// Not Required any action
							}
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(final VolleyError error) {
						}
					}){
				@Override
				protected Map<String,String> getParams(){
					Map<String,String> params = new HashMap<String, String>();
					params.put("msg", message);
					params.put("channel", ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.CHAT_ROOM));
					return params;
				}

			};
			RetryPolicy policy = new DefaultRetryPolicy(Constant.SOCKET_TIMEOUT_DURATION, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			stringRequest.setRetryPolicy(policy);
			queue.add(stringRequest);
		}
	}

	@Override
	public void onPostResponse(String result,final int apiCall)
	{
		if((result==null||result.length()==0) && apiCall!=ChatConstants.PRE_API_MY_CHAT)
		{
			AlertDialog.Builder builder1 = new AlertDialog.Builder(ChatActivity.this);
			builder1.setTitle("Retry");
			builder1.setMessage("Sorry for inconvenience. Unable to reach server");
			builder1.setCancelable(true);
			builder1.setPositiveButton("Try Again",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							new ChatSyncTask(mjobID,ChatActivity.this,apiCall, ChatActivity.this).execute();
							dialog.cancel();
							showPleaseWaitProgressDialog(ChatActivity.this);
						}
					});
			builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					mProgressDialog.dismiss();
					dialog.cancel();
					finish();
				}
			});
			try {
				AlertDialog alert11 = builder1.create();
				alert11.show();
				alert11.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
				alert11.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			//mSwipeRefreshLayout.setRefreshing(false);
			if(apiCall!=ChatConstants.PRE_API_MY_CHAT)
			{
				chatResponse = GsonContextLoader.getGsonContext().fromJson(result,StartChatBO.class);
				//ChatManager.getInstance().usersOnline=new HashSet<String>();
				if (null != result)
				{
					if((chatResponse.getStatus()!=null && chatResponse.getStatus().equalsIgnoreCase("200"))||mychat!=null)
					{
						setEssentialChatData(chatResponse);
						textViewLdhistory.setText("Load History");
						msg_box.setVisibility(View.VISIBLE);
						//textViewLdhistory.setVisibility(View.VISIBLE);
						//rl_channelbar.setVisibility(View.VISIBLE);
					}
					else
					{
						mProgressDialog.dismiss();
						textViewLdhistory.setText("chat not avilable for this job post");
						textViewLdhistory.setVisibility(View.GONE);
					}
				}
			}
		}
	}
	private void dialogHandle()
	{
		try {
			AlertDialog.Builder builder1 = new AlertDialog.Builder(ChatActivity.this);
			builder1.setMessage("The open house is over. Thank you for participating. You may view the transcript.");
			builder1.setCancelable(true);
			builder1.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			AlertDialog alert11 = builder1.create();
			alert11.show();
			alert11.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setEssentialChatData(StartChatBO chatResponse)
	{
		ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.CHAT_ROOM, chatResponse.getChat().getChannelname());
		ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.CHAT_UUID, chatResponse.getProfile().getUsrr());
		ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.CHAT_USERNAME, chatResponse.getChat().getReciver());
		initPubNub();
		if(ChatManager.getInstance().chatCount!=null)
		{
			ChatManager.getInstance().chatCount.remove(chatResponse.getChat().getChannelname());
			ChatPreferences.getChatSharedInstance().setChatPrefernceChatCount(ChatConstants.CHAT_COUNT, ChatManager.getInstance().chatCount);
		}
		setCompleteChatCount();
		//this.mChatAdapter.userPresence(ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.CHAT_USERNAME), "join"); // Set user to online. Status changes handled in presence
		textView_nameRec.setText(chatResponse.getChat().getName());
		//textView_compRec.setText(chatResponse.getChat().getCurrent_organization());
		textView_linkRec.setText(chatResponse.getChat().getCurrent_designation()+ " at " + chatResponse.getChat().getCurrent_organization());
		//this.mChannelView.setText(ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.CHAT_ROOM));
		ImageLoader imageLoader = RecruiterApplication.getApplication().getImageLoader();
		imageView_recchatid.setImageUrl(chatResponse.getChat().getSeekerimage_url(), imageLoader);
	}
	public void showPleaseWaitProgressDialog(Context context)
	{
		showProgressDialog(context, R.string.dialog_please_wait);
	}

	public void showProgressDialog(Context context, int resId)
	{
		hideProgressDialog();
		mProgressDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);
		mProgressDialog.setMessage(mProgressDialog.getContext().getString(resId));
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();
	}

	public void hideProgressDialog() {

		try {
			if (null != mProgressDialog) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void herenNowCallback(final Set<String> onlineNow )
	{
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				String chatUser=chatResponse.getChat().getReciver();
				if (onlineNow.contains(chatUser))
				{
					iv_chatonlinestatus.setVisibility(View.VISIBLE);
				}
				else
				{
					iv_chatonlinestatus.setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	public void wherenNowCallback() {

	}

	@Override
	public void subscribeCallback(final ChatMessage chatMessage) {
		try {
			if(ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.CHAT_ROOM).equalsIgnoreCase(chatMessage.getChannelName())
					&& chatMessage.getUsr().equalsIgnoreCase(chatResponse.getChat().getReciver()))
			{
				/*String usr =chatResponse.getProfile().getName();
				chatMessage.setUsr(usr);*/
				chatMsgsList.add(chatMessage);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						mChatAdapter.addMessage(chatMessage,mListView);
					}
				});
			}
			else
			{
				if(!ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.CHAT_ROOM).equalsIgnoreCase(chatMessage.getChannelName())&&
						!chatMessage.getUsr().equalsIgnoreCase(chatResponse.getProfile().getUsrr()))
				{
					imageView_chatindicator.setAnimation(ChatManager.getInstance().shakeAnim(ChatActivity.this));
					if(ChatManager.getInstance().chatCount.get(chatMessage.getChannelName())!=null
							&& !ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.CHAT_ROOM).equalsIgnoreCase(chatMessage.getChannelName()))

					{
						int count=ChatManager.getInstance().chatCount.get(chatMessage.getChannelName())+1;
						ChatManager.getInstance().chatCount.put(chatMessage.getChannelName(), count);
						ChatPreferences.getChatSharedInstance().setChatPrefernceChatCount(ChatConstants.CHAT_COUNT, ChatManager.getInstance().chatCount);
					}
					else if(ChatManager.getInstance().chatCount.get(chatMessage.getChannelName())==null)
					{
						ChatManager.getInstance().chatCount.put(chatMessage.getChannelName(),0);
						ChatPreferences.getChatSharedInstance().setChatPrefernceChatCount(ChatConstants.CHAT_COUNT, ChatManager.getInstance().chatCount);
					}
					ChatManager.getInstance().isNewSeeker=true;
				}
			}
			setCompleteChatCount();
			//ChatManager.getInstance().hereNow(ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.CHAT_ROOM));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setCompleteChatCount()
	{
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				overAllChatCount=ChatManager.getInstance().setCompleteChatCount();
				if(overAllChatCount<100)
				{
					textView_occupancy.setText(overAllChatCount+"");
				}
				else
				{
					textView_occupancy.setText("99+");
				}
				rl_notification_chatactivity.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if(overAllChatCount>0 && mychat==null)
						{
							Intent myIntent = new Intent(ChatActivity.this,MyChat.class);
							Bundle bundle = new Bundle();
							bundle.putByte("CHAT_NOTIFICATION",(byte) 0);
							myIntent.putExtras(bundle);
							startActivity(myIntent);
							finish();
						}
						else if(overAllChatCount>0)
						{
							finish();
						}
					}
				});
			}
		});
	}
	@Override
	public void loadhistorymessageCallback(final List<ChatMessage> chatMsgs,final boolean checkHistCount) {

		if(chatMsgs!=null && chatMsgs.size()>0)
		{
			for(int i = 0; i < chatMsgsList.size(); i++)
			{
				chatMsgs.add(chatMsgsList.get(i));
			}
			ChatActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {

					if(checkHistCount)
					{
						textViewLdhistory.setVisibility(View.GONE);
						textView_chatHeader.setVisibility(View.GONE);
					}
					chatMsgsList =new ArrayList<ChatMessage>();
					chatMsgsList=chatMsgs;
					mChatAdapter.setMessagesList(chatMsgs,mListView);
				}
			});
		}
		else
		{
			textViewLdhistory.setVisibility(View.GONE);
		}
	}
	@Override
	public void presenceSubscribeCallback(JSONObject json) {
		try {
			int occ = json.getInt("occupancy");
			String user = json.getString("uuid");
			String action = json.getString("action");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void historyCallback(final List<ChatMessage> chatMsgs,final boolean checkHistCount) {

		ChatActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(checkHistCount)
				{
					textViewLdhistory.setVisibility(View.GONE);
					textView_chatHeader.setVisibility(View.GONE);
				}
				else
				{
					textViewLdhistory.setVisibility(View.VISIBLE);
				}
				chatMsgsList=chatMsgs;
				mChatAdapter.setMessages(chatMsgsList);
			}
		});
	}
	@Override
	public void dateHandler(String date)
	{
		dateHistory=date;
	}
}
