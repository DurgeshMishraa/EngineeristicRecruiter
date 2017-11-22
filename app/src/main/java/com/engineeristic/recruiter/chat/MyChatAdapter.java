package com.engineeristic.recruiter.chat;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.engineeristic.recruiter.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 
 * Custom adapter that takes a list of ChatMessages and fills a chat_row_layout.xml view with the
 *   ChatMessage's information.
 */
public class MyChatAdapter extends BaseAdapter {

	private final Context context;
	private LayoutInflater inflater;
	private List<MYChatListBO> values;
	private Typeface font; 
	private MyChatProfileBO myChatProfileBO;
	private Set<String> onlineNow = new HashSet<String>();
	private DisplayImageOptions options;
	private com.nostra13.universalimageloader.core.ImageLoader loader;
	public MyChatAdapter(Context context, List<MYChatListBO> values,MyChatProfileBO myChatProfBO) {
		
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.values=values;
		this.myChatProfileBO=myChatProfBO;
		font = Typeface.createFromAsset(context.getAssets(),"DroidSans.ttf");
		loaderImage();
	}
	private void loaderImage()
	{
		try {

			options = new DisplayImageOptions.Builder()
					.displayer(new RoundedBitmapDisplayer(90))
					.showImageOnLoading(R.drawable.img)
					.showImageForEmptyUri(R.drawable.img)
					.showImageOnFail(R.drawable.img).cacheInMemory(true)
					.cacheOnDisc(true)
					.build();

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					context).defaultDisplayImageOptions(options).build();

			loader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
			loader.init(config);

			//loader.displayImage("http://3149a1ab829b87c18891-803fe0e13f8213fba3d205acc54f0e7c.r32.cf1.rackcdn.com/media/userpics/2015/09/29/2015-09-29-15-34-34-143530.jpg",imageView_recchatid,options);

		} catch (Exception e) {
			Log.e("ChatActivity ", "loaderImage() "+e.getMessage());
		}
	}
	class ViewHolder {
		TextView textView_nameRec;
		TextView textView_compRec;
		TextView textView_comdesignation;
		ImageView imageView_recchatid;
		ImageView iv_chatonlinestatus;
		TextView textView_chatCount;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.my_chat_list, parent, false);
			holder.imageView_recchatid =(ImageView) convertView.findViewById(R.id.imageView_recchatid);
			holder.iv_chatonlinestatus =(ImageView) convertView.findViewById(R.id.iv_chatonlinestatus);
			holder.textView_nameRec = (TextView) convertView.findViewById(R.id.textView_nameRec);
			holder.textView_compRec = (TextView) convertView.findViewById(R.id.textView_compRec);
			holder.textView_comdesignation = (TextView) convertView.findViewById(R.id.textView_comdesignation);
			holder.textView_chatCount=(TextView) convertView.findViewById(R.id.textView_chatCount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();			
		}
		MYChatListBO myChatListBO =values.get(position);

		holder.textView_nameRec.setText(myChatListBO.getName());
		holder.textView_nameRec.setTypeface(font);
		holder.textView_compRec.setText(myChatListBO.getCurrent_organization());
		holder.textView_compRec.setTypeface(font);
		holder.textView_comdesignation.setText(myChatListBO.getCurrent_designation());
		holder.textView_comdesignation.setTypeface(font);

		//ChatManager.getInstance().presenceSubscribe(myChatListBO.getChannelname());
		loader.displayImage(myChatListBO.getSeekerimage_url(), holder.imageView_recchatid,options);
//		ImageLoader imageLoader = RecruiterApplication.getApplication().getImageLoader();
//		holder.imageView_recchatid.setImageUrl(myChatListBO.getSeekerimage_url(), imageLoader);

		holder.iv_chatonlinestatus.setVisibility(View.GONE);
		if(onlineNow!=null && onlineNow.size()>0 && onlineNow.contains(myChatListBO.getReciver()))
		{
			holder.iv_chatonlinestatus.setVisibility(View.VISIBLE);
			holder.iv_chatonlinestatus.setImageResource(R.drawable.green_circle);
		}		

		/*if(userPresence())
		{
			holder.imageView_onlinegreen.setVisibility(View.VISIBLE);
		}*/
		holder.textView_chatCount.setVisibility(View.GONE);
		if(ChatManager.getInstance().chatCount !=null)
		{
			if(ChatManager.getInstance().chatCount .get(myChatListBO.getChannelname())!=null &&
					ChatManager.getInstance().usernameForcount.equalsIgnoreCase(myChatListBO.getChannelname()))
			{
				holder.textView_chatCount.setVisibility(View.VISIBLE);
				//int count=ChatManager.getInstance().chatCount .get(myChatListBO.getName())+1;
				//ChatManager.getInstance().chatCount .put(myChatListBO.getName(), count);
				int count=ChatManager.getInstance().chatCount.get(myChatListBO.getChannelname());
				if(count<100)
				{
					holder.textView_chatCount.setText(count+"");
				}
				else
				{
					holder.textView_chatCount.setText("99+");
				}
			}
			else if(ChatManager.getInstance().chatCount .get(myChatListBO.getChannelname())==null)
			{				
				ChatManager.getInstance().chatCount.put(myChatListBO.getChannelname(),0);
				ChatPreferences.getChatSharedInstance().setChatPrefernceChatCount(ChatConstants.CHAT_COUNT, ChatManager.getInstance().chatCount);
			}
			else 
			{
				if(ChatManager.getInstance().chatCount .get(myChatListBO.getChannelname())!=0)
				{
					holder.textView_chatCount.setVisibility(View.VISIBLE);
					int count=ChatManager.getInstance().chatCount.get(myChatListBO.getChannelname());
					if(count<100)
					{
						holder.textView_chatCount.setText(count+"");
					}
					else
					{
						holder.textView_chatCount.setText("99+");
					}
				}
				else
				{
					holder.textView_chatCount.setText(ChatManager.getInstance().chatCount .get(myChatListBO.getChannelname())+"");
				}
			}
		}
		else
		{			
			
			ChatManager.getInstance().chatCount.put(myChatListBO.getChannelname(),0);
			ChatPreferences.getChatSharedInstance().setChatPrefernceChatCount(ChatConstants.CHAT_COUNT, ChatManager.getInstance().chatCount);
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return this.values.size();
	}
	public boolean userPresence(){
		boolean isOnline = false;
		JSONObject json =ChatManager.getInstance().getJsonPresence();
		try {
			int occ = json.getInt("occupancy");

			String user = json.getString("uuid");
			String action = json.getString("action");

			isOnline= action.equals("join") || action.equals("state-change");
			if (!isOnline && myChatProfileBO.getUsrr().contains(user))
				isOnline=false;
			else if (isOnline && !myChatProfileBO.getUsrr().contains(user))
				isOnline=true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return isOnline;
	}

	public void setOnlineNow(Set<String> onlineNow,String user){

		//boolean isOnline = action.equals("join") || action.equals("state-change");
		if (onlineNow.contains(user))
			onlineNow.remove(user);
		else if (!onlineNow.contains(user))
			this.onlineNow.add(user);

		this.onlineNow = onlineNow;
		notifyDataSetChanged();
	}


	public void setChatcount()
	{
		/*if(values!=null)
		{
			for (int i = 0; i < values.size(); i++)
			{
				MYChatListBO myChatListBO=values.get(i);
				if(userName.equalsIgnoreCase(myChatListBO.getName()))
				{

				}

			}
		}*/		
		try {			
			notifyDataSetChanged();
		} catch (Exception e) {
			// TODO: handle exception
		}

		
		
		
	
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return values.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}	

}
