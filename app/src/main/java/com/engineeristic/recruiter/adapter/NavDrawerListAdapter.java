package com.engineeristic.recruiter.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.engineeristic.recruiter.model.NavDrawerItem;
import com.engineeristic.recruiter.R;

import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		//super(context,navDrawerItems);
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}
	@Override
	public int getCount() {
		return navDrawerItems.size();
	}
	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//View v=super.getView(position, convertView, parent);
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater)	context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}
		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView textView_chatcount= (TextView) convertView.findViewById(R.id.textView_chatcount);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.left_drawer_textviewid);
		ImageView imageView = (ImageView)convertView.findViewById(R.id.divpix);
		//TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
		textView_chatcount.setVisibility(View.GONE);
		//if(navDrawerItems.get(position).getIcon()!=0){
		imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
		//}
		//else
		//{
		//	imgIcon.setVisibility(View.GONE);
		//}
		txtTitle.setText(navDrawerItems.get(position).getTitle());


		if(3 == position){

		}

		return convertView;
	}

}
