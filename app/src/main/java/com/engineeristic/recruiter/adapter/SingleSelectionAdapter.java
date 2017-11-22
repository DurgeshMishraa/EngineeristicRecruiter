package com.engineeristic.recruiter.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.model.FilterModel;

public class SingleSelectionAdapter extends BaseAdapter  
{
	private Context mContext;
	private List<FilterModel> listModel = null;
	public SingleSelectionAdapter(Context context, List<FilterModel> mListModel){
		mContext = context;
		this.listModel = mListModel;
	}
	@Override
	public int getCount() {
		if(null != listModel) {
			return listModel.size();
		}
		return 0;
	}
	@Override
	public Object getItem(int position) {
		return listModel.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final ViewHolder holder;
		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		final FilterModel mDataModel = (FilterModel) getItem(position);
		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.multi_selection_list_item, null);
			holder = new ViewHolder();
			holder.titleTextView = (TextView) convertView.findViewById(R.id.filter_title_text);
			holder.radioIv = (ImageView) convertView.findViewById(R.id.checkbox_iv);
			holder.radioIv.setBackgroundResource(R.drawable.profile_notification_unsel);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		if(mDataModel.isSelected()){
			holder.radioIv.setBackgroundResource(R.drawable.profile_notification_sel);
		}else{
			holder.radioIv.setBackgroundResource(R.drawable.profile_notification_unsel);
		}
		holder.titleTextView.setText(mDataModel.getTitleName());
		return convertView;
	}
	public static class ViewHolder
	{
		TextView titleTextView;
		ImageView radioIv;
	}

}
