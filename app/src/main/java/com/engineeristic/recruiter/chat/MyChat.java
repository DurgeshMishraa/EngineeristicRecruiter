package com.engineeristic.recruiter.chat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.myapp.GCMPushReceiverService;
import com.engineeristic.recruiter.myapp.RecruiterApplication;
import com.engineeristic.recruiter.pojo.UpdateAppData;
import com.engineeristic.recruiter.util.AccountUtils;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.ConstantFontelloID;
import com.engineeristic.recruiter.util.GsonContextLoader;
import com.engineeristic.recruiter.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MyChat extends Activity implements ChatApiCallback, OnItemClickListener
{
	//LinearLayout ll_back;
	//private CircularNetworkImageView imageView_recchatid;
	private TextView imageView_chatindicator;
	TextView textView_occupancy;//textView_nameRec, textView_compRec, textView_comdesignation
	private ProgressDialog mProgressDialog;
	private String jobid="";
	ListView listView_mychat;
	MyChatAdapter mychatadapter;
	private ImageLoader loader;
	ArrayList<MYChatListBO> myChatListBOs;
	MyChatProfileBO myChatProfileBO=null;
	String[] channesl=null;
	private TextView mTxtIconBack;
	private Typeface  typeFontello;
	private RelativeLayout rl_chat_conversation;
	//private boolean isOnRestart = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

			setContentView(R.layout.my_chat);
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
		}
		AccountUtils.trackerScreen("My Chat");
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_mychat);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			toolbar.setElevation((float)10.0);
		}
		ChatPreferences.getChatSharedInstance().setContextPreference(Utility.getContext());
		myChatProfileBO=new MyChatProfileBO();
		myChatListBOs=new ArrayList<MYChatListBO>();
		mychatadapter=new MyChatAdapter(MyChat.this,myChatListBOs,myChatProfileBO);
		initcomponent();
		//showPleaseWaitProgressDialog(this);
		ChatManager.getInstance().setChatApiCallback(MyChat.this);
		Bundle bnd =getIntent().getExtras();
		if(ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.MY_CHAT_PROFILE_RESPONSE)!=null
				&& !ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.MY_CHAT_PROFILE_RESPONSE).
				equalsIgnoreCase("Anonymous") && bnd!=null && bnd.getByte("CHAT_NOTIFICATION") !=1)
		{

			Thread thchat= new Thread(new Runnable() {
				public void run() {
					onPostResponse(ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.MY_CHAT_PROFILE_RESPONSE),
							ChatConstants.PRE_API_MY_CHAT);
				}
			});
			thchat.start();

		}
		else
		{
			showPleaseWaitProgressDialog(MyChat.this);
			new ChatSyncTask(jobid,MyChat.this,ChatConstants.MY_CHAT, MyChat.this).execute();
		}
		GCMPushReceiverService.eventChat=new ArrayList<String>();
	}

	private void initcomponent() {
		rl_chat_conversation = (RelativeLayout)findViewById(R.id.rl_noconversation_chat);
		mTxtIconBack = (TextView)findViewById(R.id.txtbackicon_mychat);
		typeFontello = Typeface.createFromAsset(getAssets(),"fontello.ttf");
		mTxtIconBack.setTypeface(typeFontello);
		mTxtIconBack.setText(ConstantFontelloID.icon_back_icon);
		imageView_chatindicator=(TextView) findViewById(R.id.imageView_chatindicator);
		imageView_chatindicator.setTypeface(typeFontello);
		imageView_chatindicator.setText(ConstantFontelloID.icon_notification_bell);
		textView_occupancy=(TextView) findViewById(R.id.textView_occupancy);
		listView_mychat=(ListView) findViewById(R.id.listView_mychat);
		listView_mychat.setOnItemClickListener(MyChat.this);
		mTxtIconBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.PUSH_NOTIFICATION_ON,"1");
				finish();
			}
		});
		rl_chat_conversation.setVisibility(View.GONE);
		listView_mychat.setVisibility(View.VISIBLE);

	}
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.PUSH_NOTIFICATION_ON,"1");
		finish();
	}
	public void showPleaseWaitProgressDialog(Context context)
	{
		showProgressDialog(context, R.string.dialog_please_wait);
	}

	public void showProgressDialog(Context context, int resId) 
	{
		hideProgressDialog();
		mProgressDialog = new ProgressDialog(context);//,ProgressDialog.THEME_HOLO_LIGHT
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
	protected void onRestart() {
		super.onRestart();
		//if(isOnRestart){
		ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.PUSH_NOTIFICATION_ON,"0");
		ChatManager.getInstance().setChatApiCallback(MyChat.this);
		int overAllChatCount=ChatManager.getInstance().setCompleteChatCount();
		if(overAllChatCount<100)
		{
			textView_occupancy.setText(overAllChatCount+"");
		}
		else
		{
			textView_occupancy.setText("99+");
		}

		mychatadapter.notifyDataSetChanged();
		//mychatadapter.setOnlineNow(ChatManager.getInstance().usersOnline,myChatProfileBO.getUsrr());
		//}
	}
	@Override
	protected void onResume() {
		super.onResume();		
		ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.PUSH_NOTIFICATION_ON,"0");
		if(ChatManager.getInstance().isNewSeeker)
		{
			ChatManager.getInstance().isNewSeeker=false;
			new ChatSyncTask("",MyChat.this,ChatConstants.PRE_API_MY_CHAT, MyChat.this).execute();
		}

	}
	@Override
	protected void onPause() {
		super.onPause();
		ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.PUSH_NOTIFICATION_ON,"1");
	}
	@Override
	protected void onStop() {
		super.onStop();
		//ChatManager.getInstance().setChatApiCallback(MyChat.this);
		/*if (ChatManager.getInstance().mPubNub != null)
			ChatManager.getInstance().mPubNub.unsubscribeAll();*/
	}

	@Override
	public void onPostResponse(String result, final int apiCall) 
	{
		if(result==null||result.length()==0)
		{
			AlertDialog.Builder builder1 = new AlertDialog.Builder(MyChat.this);
			builder1.setTitle("Retry");
			builder1.setMessage("Sorry for inconvenience. Unable to reach server");
			builder1.setCancelable(true);		
			builder1.setPositiveButton("Try Again",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					new ChatSyncTask(jobid,MyChat.this,apiCall, MyChat.this).execute();
					dialog.cancel();
					showPleaseWaitProgressDialog(MyChat.this);
				}
			});
			builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					hideProgressDialog();
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
			try {

				JSONObject jsonObject= new JSONObject(result);
				if(jsonObject.get("status")!=null && jsonObject.get("status").equals(200) &&
						jsonObject.get("profile")!=null && !jsonObject.get("profile").equals(null) &&
						jsonObject.get("chat")!=null && !jsonObject.get("chat").equals(null))
				{
					UpdateAppData.setChat_enable(jsonObject.getString("ChatEnable"));
					if(jsonObject.getString("ChatEnable").equals("false")){
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								hideProgressDialog();
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
										startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(Constant.UPGRADE_APP)));
									}
								});
								//isOnRestart = false;
							}
						});
					}

					else{
					myChatListBOs =new ArrayList<MYChatListBO>();
					JSONObject jsonObjectProfile=jsonObject.getJSONObject("profile");
					myChatProfileBO = GsonContextLoader.getGsonContext().fromJson(jsonObjectProfile.toString(),MyChatProfileBO.class);
					ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.CHAT_UUID, myChatProfileBO.getUsrr());
					//ChatManager.getInstance().initPubNub(MyChat.this);
					if(ChatManager.getInstance() != null
							&& ChatManager.getInstance().mPubNub != null)
					{
						ChatManager.getInstance().mPubNub.setUUID(ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.CHAT_UUID));
					}
					else
					{
						ChatManager.getInstance().initializePubNub();
						ChatManager.getInstance().mPubNub.setUUID(ChatPreferences.getChatSharedInstance()
								.getChatPreference(ChatConstants.CHAT_UUID));
					}

					JSONArray jsonObjectChatsRec=jsonObject.getJSONArray("chat");
					channesl=new String[jsonObjectChatsRec.length()] ;

					for (int i = 0; i < jsonObjectChatsRec.length(); i++) {

						MYChatListBO myChatListBO=GsonContextLoader.getGsonContext().fromJson(jsonObjectChatsRec.get(i).toString(),MYChatListBO.class);

						channesl[i]=myChatListBO.getChannelname();
						myChatListBOs.add(myChatListBO);
						if(ChatManager.getInstance().chatCount.get(myChatListBO.getChannelname())==null ||
								ChatManager.getInstance().chatCount.get(myChatListBO.getChannelname())==0)
						{
							ChatManager.getInstance().chatCount.put(myChatListBO.getChannelname(),0);
							ChatPreferences.getChatSharedInstance().setChatPrefernceChatCount(ChatConstants.CHAT_COUNT,
									ChatManager.getInstance().chatCount);
							//myChatListBOsZero.add(myChatListBO);
						}
						else
						{
							ChatManager.getInstance().chatCount.put(myChatListBO.getChannelname(),ChatManager.getInstance().chatCount.get(myChatListBO.getChannelname()));
							ChatPreferences.getChatSharedInstance().setChatPrefernceChatCount(ChatConstants.CHAT_COUNT,
									ChatManager.getInstance().chatCount);
							//myChatListBOsNonZero.add(myChatListBO);
						}

					}
					ChatManager.getInstance().channelsMyChat=channesl;
					setPrioritychatList();
				}
				}
				else
				{	
					hideProgressDialog();
					UpdateAppData.setChat_enable(jsonObject.getString("ChatEnable"));
					if(jsonObject.getString("ChatEnable").equals("false")){
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
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
										startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(Constant.UPGRADE_APP)));
									}
								});

							}
						});
					}

					else{
						//dialogHandle("Sorry no chat available.");
						listView_mychat.setVisibility(View.GONE);
						rl_chat_conversation.setVisibility(View.VISIBLE);
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	private void setPrioritychatList()
	{
		hideProgressDialog();
		MyChat.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				int overAllChatCount=ChatManager.getInstance().setCompleteChatCount();
				if(overAllChatCount<100)
				{
					textView_occupancy.setText(overAllChatCount+"");
				}
				else
				{
					textView_occupancy.setText("99+");
				}
				//textView_comdesignation.setText(myChatProfileBO.getDesignation());
				//textView_nameRec.setText(myChatProfileBO.getName());
				//textView_compRec.setText(myChatProfileBO.getOrganisation());
				ImageLoader imageLoader = RecruiterApplication.getApplication().getImageLoader();
				//imageView_recchatid.setImageUrl(myChatProfileBO.getImage(), imageLoader);
				mychatadapter=new MyChatAdapter(MyChat.this,myChatListBOs,myChatProfileBO);							
				listView_mychat.setAdapter(mychatadapter);	
				mychatadapter.notifyDataSetChanged();
			}
		});
	}
	private void dialogHandle(String message)
	{
		try {
			AlertDialog.Builder builder1 = new AlertDialog.Builder(MyChat.this);
			builder1.setTitle("MY CHAT");
			builder1.setMessage(message);
			builder1.setCancelable(true);		
			builder1.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
					finish();

				}
			});

			AlertDialog alert11 = builder1.create();
			alert11.show();
			alert11.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
		} catch (Exception e) {
			Log.e("MyChat ", "dialogHandle() Exception :"+e.getMessage());
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		if(Utility.isNetworkAvailable(Utility.getContext()))
		{
			MYChatListBO myChatListBO =myChatListBOs.get(arg2);
			String chatData=setJsonString(arg2);
			String channelName=myChatListBO.getChannelname();
			String seekerId=channelName.substring(channelName.indexOf("-j")+2,channelName.length());

			if(ChatManager.getInstance().chatCount.get(myChatListBO.getChannelname())!=null)
			{
				int remaincount=ChatManager.getInstance().setCompleteChatCount()-ChatManager.getInstance().chatCount.get(myChatListBO.getChannelname());			
				if(remaincount<100)
				{
					textView_occupancy.setText(remaincount+"");
				}
				else
				{
					textView_occupancy.setText("99+");
				}				
			}
			ChatManager.getInstance().chatCount.remove(myChatListBO.getChannelname());
			ChatPreferences.getChatSharedInstance().setChatPrefernceChatCount(ChatConstants.CHAT_COUNT, ChatManager.getInstance().chatCount);
			ChatManager.getInstance().usernameForcount="";
			//ChatBoradcastMessage.cancelNotification(9999);
			Intent myIntent = new Intent(MyChat.this,ChatActivity.class);		
			myIntent.putExtra(ChatConstants.JOBID_FOR_CHAT, seekerId);				
			myIntent.putExtra("MyChat",chatData);
			startActivity(myIntent);
		}
	}

	private String setJsonString(int position)
	{
		JSONObject jsonObject = null;
		String mychatProfile = GsonContextLoader.getGsonContext().toJson(myChatProfileBO);
		String mychatindex = GsonContextLoader.getGsonContext().toJson(myChatListBOs.get(position));
		try {
			JSONObject js1= new JSONObject(mychatProfile);
			JSONObject js2= new JSONObject(mychatindex);
			jsonObject= new JSONObject();
			jsonObject.put("profile", js1);
			jsonObject.put("chat", js2);
			jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	@Override
	public void herenNowCallback(final Set<String> onlineNow) 
	{
		/*if (ChatManager.getInstance().usersOnline.contains(myChatProfileBO.getUsrr()))
			ChatManager.getInstance().usersOnline.remove(myChatProfileBO.getUsrr());
		else if (ChatManager.getInstance().usersOnline.contains(myChatProfileBO.getUsrr()))
			ChatManager.getInstance().usersOnline.add(myChatProfileBO.getUsrr());*/

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mychatadapter.setOnlineNow(ChatManager.getInstance().usersOnline,myChatProfileBO.getUsrr());
			}
		});
	}

	@Override
	public void wherenNowCallback() {
	}

	@Override
	public void subscribeCallback(ChatMessage chatMessage) {
	  try {
	   if(ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.PUSH_NOTIFICATION_ON).equalsIgnoreCase("1") )
		{
		 return;
		}
		ChatManager.getInstance().usernameForcount=chatMessage.getChannelName();
		if(ChatManager.getInstance().chatCount!=null)
		 {
		 if(ChatManager.getInstance().chatCount.get(chatMessage.getChannelName())!=null )
		  {
		   int count=ChatManager.getInstance().chatCount .get(chatMessage.getChannelName())+1;
		   ChatManager.getInstance().chatCount .put(chatMessage.getChannelName(), count);
		   ChatPreferences.getChatSharedInstance().setChatPrefernceChatCount(ChatConstants.CHAT_COUNT,ChatManager.getInstance().chatCount);
		   }
		 else if(ChatManager.getInstance().chatCount.get(chatMessage.getChannelName())==null)
		  {
		   ChatManager.getInstance().chatCount.put(chatMessage.getChannelName(),0);
		   ChatPreferences.getChatSharedInstance().setChatPrefernceChatCount(ChatConstants.CHAT_COUNT,ChatManager.getInstance().chatCount);
		  }
		 else
		 {
		  ChatManager.getInstance().chatCount .put(chatMessage.getChannelName(),ChatManager.getInstance().chatCount .get(chatMessage.getChannelName()));
		  ChatPreferences.getChatSharedInstance().setChatPrefernceChatCount(ChatConstants.CHAT_COUNT,ChatManager.getInstance().chatCount);
		 }
		 }
		 runOnUiThread(new Runnable() {
		 @Override
		 public void run() {
		 mychatadapter.setChatcount();
		 int overAllChatCount=ChatManager.getInstance().setCompleteChatCount();
		 if(overAllChatCount<100)
		 {
		  textView_occupancy.setText(overAllChatCount+"");
		 }
		 else
		 {
		  textView_occupancy.setText("99+");
		 }
		 imageView_chatindicator.setAnimation(ChatManager.getInstance().shakeAnim(MyChat.this));
		 }
		});
		//hereNow();
		//mychatadapter.setChatcount(chatMessage.getUsername(),myChatListBOs);
		} catch (Exception e) {
			Log.e("My Chat","subscribeCallback() "+e.getMessage());
		}
	}
	@Override
	public void loadhistorymessageCallback( List<ChatMessage> chatMsgs, boolean checkHistCount) {
	}
	@Override
	public void presenceSubscribeCallback(JSONObject json) {
		try {
			/*int occ = json.getInt("occupancy");
			String user = json.getString("uuid");
			String action = json.getString("action");*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void historyCallback( List<ChatMessage> chatMsgs, boolean checkHistCount) {
	}

}
