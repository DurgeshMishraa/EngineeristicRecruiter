package com.engineeristic.recruiter.filter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.adapter.SingleSelectionAdapter;
import com.engineeristic.recruiter.model.FilterModel;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.Utility;

public class NoticePeriodFilterFragment extends Fragment{

	private ListView listView;
	private List<FilterModel> itemsList = new ArrayList<FilterModel>();
	public static FilterModel modelObj = null;

	private String savedId = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			savedId = MySharedPreference.getFilterType(Constant.FILTER_NOTICE_PERIOD_PARAMS, "0");

			final JSONArray jsonArray = new JSONObject(Utility.ReadFromfile("filterNoticePeriod.txt", getActivity())).getJSONArray("noticeperiods");
			for(int s = 0; s < jsonArray.length(); s++){	
				final FilterModel obj = new FilterModel();
				obj.setTitleName(jsonArray.getJSONObject(s).getString("name"));
				obj.setId(jsonArray.getJSONObject(s).getString("id"));


				if(!savedId.equals("0") && jsonArray.getJSONObject(s).getString("id").equals(savedId)){
					obj.setSelected(true);
					itemsList.add(obj);
					modelObj = obj;
				}else if(modelObj == null){
					obj.setSelected(false);
					itemsList.add(obj);
				}else{

					if(modelObj.getTitleName().equals(jsonArray.getJSONObject(s).getString("name"))){
						modelObj.setSelected(true);
						itemsList.add(modelObj);
					}else{
						obj.setSelected(false);
						itemsList.add(obj);
					}

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View convertView =  inflater.inflate(R.layout.filter_notice_period_layout, container, false);
		listView = (ListView) convertView.findViewById(R.id.filter_list);
		if(itemsList != null && itemsList.size() > 0){

			final SingleSelectionAdapter adapter = new SingleSelectionAdapter(getActivity(), itemsList);
			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					changeSelection(arg2, itemsList, (FilterModel) adapter.getItem(arg2));
				}
			});
		}

		return convertView;
	}
	private void changeSelection(final int position, final List<FilterModel> mItemsList, final FilterModel mObj){
		for(byte b = 0; b < mItemsList.size(); b++){
			final View view = listView.getChildAt(b);
			final ImageView radioIv = (ImageView) view.findViewById(R.id.checkbox_iv);
			if(position == b){
				this.modelObj = mObj;
				radioIv.setBackgroundResource(R.drawable.profile_notification_sel);
			}else{
				radioIv.setBackgroundResource(R.drawable.profile_notification_unsel);
			}
		}




	}
	public static String getSelectedId(){
		String temp = null;
		if(modelObj != null){
			temp = modelObj.getId();
		}
		return temp;
	}
	public static String getSelectedTitle(){
		String temp = null;
		if(modelObj != null){
			temp = modelObj.getTitleName();
		}
		return temp;
	}
	public static void clearHistory(){
		modelObj = null;

	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
