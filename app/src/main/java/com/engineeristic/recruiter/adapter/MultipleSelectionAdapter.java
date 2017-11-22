package com.engineeristic.recruiter.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.model.FilterModel;

public class MultipleSelectionAdapter extends BaseAdapter implements Filterable 
{
	private Context mContext;
	private List<FilterModel> listModel = null;
	private List<FilterModel> originalData = null;
	private ItemFilter mFilter = new ItemFilter();
	public MultipleSelectionAdapter(Context context, List<FilterModel> mListModel){
		mContext = context;
		this.listModel = mListModel;
		this.originalData = mListModel;
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
			holder.checkBoxIv = (ImageView) convertView.findViewById(R.id.checkbox_iv);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}

		if(mDataModel.isSelected()){
			holder.checkBoxIv.setBackgroundResource(R.drawable.filter_checked);
		}else{
			holder.checkBoxIv.setBackgroundResource(R.drawable.filter_unchecked);
		}
		holder.titleTextView.setText(mDataModel.getTitleName());
		return convertView;
	}
	public static class ViewHolder
	{
		TextView titleTextView;
		ImageView checkBoxIv;
	}
	@Override
	public Filter getFilter() {
		return mFilter;
	}

	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			
			String filterString = constraint.toString().toLowerCase();
			FilterResults results = new FilterResults();
			final List<FilterModel> list = originalData;
			int count = list.size();
			final ArrayList<FilterModel> nlist = new ArrayList<FilterModel>(count);
			FilterModel filterableModel ;
			for (int i = 0; i < count; i++) {
				filterableModel = list.get(i);
				if (filterableModel.getTitleName().toLowerCase().contains(filterString)) {
					nlist.add(filterableModel);
				}
			}
			results.values = nlist;
			results.count = nlist.size();
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			listModel = (ArrayList<FilterModel>) results.values;
			notifyDataSetChanged();
		}

	}

}
