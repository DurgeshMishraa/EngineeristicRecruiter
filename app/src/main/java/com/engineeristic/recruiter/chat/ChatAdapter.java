package com.engineeristic.recruiter.chat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.engineeristic.recruiter.R;


/**
 * Created by GleasonK on 7/11/15.
 *
 * Custom adapter that takes a list of ChatMessages and fills a chat_row_layout.xml view with the
 *   ChatMessage's information.
 */
public class ChatAdapter extends ArrayAdapter<ChatMessage> {
	private final Context context;
	private LayoutInflater inflater;
	private List<ChatMessage> values;	
	private Set<String> onlineNow = new HashSet<String>();
	private Typeface font; 
	private ListView listView;
	IChatDateHandler iChatDateHandler;
	private String dateMatch="";
	public ChatAdapter(Context context, List<ChatMessage> values) {
		super(context, R.layout.chat_row_layout, android.R.id.text1, values);
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.values=values;
		font = Typeface.createFromAsset(context.getAssets(),"DroidSans.ttf");
	}

	class ViewHolder {
		TextView user;
		TextView message;
		TextView timeStamp;
		TextView textView_chatOnDate;
		View userPresence;
		LinearLayout ll_chatrow,ll_parent;
		
		
		//ChatMessage chatMsg;
	}
	public void setdateHandler(IChatDateHandler iChDtHandler)
	{
		this.iChatDateHandler=iChDtHandler;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ChatMessage chatMsg = this.values.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.chat_row_layout, parent, false);
			holder.ll_chatrow=(LinearLayout) convertView.findViewById(R.id.ll_chatrow);			
			holder.ll_parent=(LinearLayout) convertView.findViewById(R.id.ll_parent);
			holder.user = (TextView) convertView.findViewById(R.id.chat_user);
			holder.message = (TextView) convertView.findViewById(R.id.chat_message);
			holder.timeStamp = (TextView) convertView.findViewById(R.id.chat_time);
			holder.textView_chatOnDate=(TextView) convertView.findViewById(R.id.textView_chatOnDate);
			holder.userPresence = convertView.findViewById(R.id.user_presence);		
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();			
		}
		
		holder.message.setText(chatMsg.getMessage().toString().trim());
		holder.message.setTypeface(font);
		holder.timeStamp.setText(formatTimeStamp(chatMsg.getTimeStamp()));
		holder.timeStamp.setTypeface(font);

		
		holder.textView_chatOnDate.setVisibility(View.GONE);
		
		if(chatMsg.getDate()!=null && chatMsg.getDate().length()>0)
		{
			holder.textView_chatOnDate.setVisibility(View.VISIBLE);
			holder.textView_chatOnDate.setText(chatMsg.getDate());
			
		}
		iChatDateHandler.dateHandler(historyDateformat(chatMsg.getTimeStamp()));
		//String userJoin=context.getSharedPreferences(ChatConstants.CHAT_PREFS,context.MODE_PRIVATE).getString(ChatConstants.CHAT_USERNAME,"Anonymous");
		if(chatMsg.getUsr().equalsIgnoreCase(ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.CHAT_UUID)))
		{	
			
			holder.ll_chatrow.setBackgroundResource(0);
			holder.message.setTextColor(Color.BLACK);
			holder.timeStamp.setTextColor(Color.GRAY);
			holder.ll_parent.setGravity(Gravity.RIGHT);
			holder.ll_parent.setPadding(60,0,15,0);
			holder.ll_chatrow.setPadding(15,5,25,10);
			//holder.ll_chatrow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.green_bubble));
			
			holder.ll_chatrow.setBackgroundResource(R.drawable.green_bubble);

		}
		else
		{	
			
			holder.ll_chatrow.setBackgroundResource(0);
			holder.message.setTextColor(Color.BLACK);
			holder.timeStamp.setTextColor(Color.GRAY);			

			holder.ll_parent.setGravity(Gravity.LEFT);
			holder.ll_parent.setPadding(15,0,60, 0);
			holder.ll_chatrow.setPadding(25,5,20,5);
			//holder.ll_chatrow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.white_bubble));
			holder.ll_chatrow.setBackgroundResource(R.drawable.white_bubble);

		}
		return convertView;
	}

	@Override
	public int getCount() {
		return this.values.size();
	}

	/**
	 * Method to add a single message and update the listview.
	 * @param chatMsg Message to be added
	 */
	public void addMessage(ChatMessage chatMsg,ListView listview){

		this.listView=listview;
		this.values.add(chatMsg);
		listView.setTranscriptMode(listview.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		//listView.setSelection(getCount());		
		notifyDataSetChanged();
	}

	/**
	 * Method to add a list of messages and update the listview.
	 * @param chatMsgs Messages to be added
	 */
	public void setMessages(List<ChatMessage> chatMsgs){
		this.values.clear();
		this.values.addAll(chatMsgs);			
		notifyDataSetChanged();
	}

	public void setMessagesList(List<ChatMessage> chatMsgs,ListView listview){
		this.listView=listview;
		this.values.clear();
		this.values.addAll(chatMsgs);
		listView.setTranscriptMode(NO_SELECTION);
		listView.setSelection(0);
		//listView.setscr
		notifyDataSetChanged();


	}

	/**
	 * Handle users. Fill the onlineNow set with current users. Data is used to display a green dot
	 *   next to users who are currently online.
	 * @param user UUID of the user online.
	 * @param action The presence action
	 */
	public void userPresence(String user, String action){
		boolean isOnline = action.equals("join") || action.equals("state-change");
		if (!isOnline && this.onlineNow.contains(user))
			this.onlineNow.remove(user);
		else if (isOnline && !this.onlineNow.contains(user))
			this.onlineNow.add(user);

		notifyDataSetChanged();
	}

	/**
	 * Overwrite the onlineNow array with all the values attained from a call to hereNow().
	 * @param onlineNow
	 */
	public void setOnlineNow(Set<String> onlineNow){
		this.onlineNow = onlineNow;
		notifyDataSetChanged();
	}

	/**
	 * Format the long System.currentTimeMillis() to a better looking timestamp. Uses a calendar
	 *   object to format with the user's current time zone.
	 * @param timeStamp
	 * @return
	 */
	public static String formatTimeStamp(long timeStamp){
		// Create a DateFormatter object for displaying date in specified format.
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");

		// Create a calendar object that will convert the date and time value in milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeStamp);
		return formatter.format(calendar.getTime());
	}

	private String historyDateformat(long timeStamp)
	{		 
			SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");

			// Create a calendar object that will convert the date and time value in milliseconds to date.
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(timeStamp);		
			String timeDate=formatter.format(calendar.getTime());
			return timeDate;
	}
	private String todayDateformat()
	{		 
			long timeStamp=new Date().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");

			// Create a calendar object that will convert the date and time value in milliseconds to date.
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(timeStamp);		
			String timeDate=formatter.format(calendar.getTime());
			
			return timeDate;
	}
	/**
	 * Clear all values from the values array and update the listview. Used when changing rooms.
	 */
		
	public void clearMessages(){
		this.values.clear();
		notifyDataSetChanged();
	}
	
	

}
