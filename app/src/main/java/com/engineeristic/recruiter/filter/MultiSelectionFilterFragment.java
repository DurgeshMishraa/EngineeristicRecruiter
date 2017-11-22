package com.engineeristic.recruiter.filter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.adapter.MultipleSelectionAdapter;
import com.engineeristic.recruiter.model.FilterModel;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultiSelectionFilterFragment extends Fragment{

	public static String KEY = "filter_type";
	public static final byte INDUSTRY_FILTER = 0;
	public static final byte FUNCTIONAL_AREA_FILTER = 1;
	public static final byte INSTITUTE_FILTER = 2;
	public static final byte LOCATION_FILTER = 3;
	private static byte FILTER_TYPE = 0;

	private List<FilterModel> itemsList = new ArrayList<FilterModel>();

	public static  HashMap<String, FilterModel> industryMap = new HashMap<String, FilterModel>();
	public static  HashMap<String, FilterModel> funcrtionalMap = new HashMap<String, FilterModel>();
	public static  HashMap<String, FilterModel> instituteMap = new HashMap<String, FilterModel>();
	public static  HashMap<String, FilterModel> locationMap = new HashMap<String, FilterModel>();


	private ListView listView;
	private EditText searchListField = null;

	private String industryIds =  "";
	private String functionalIds = "";
	private String instituteIds = "";
	private String locationIds = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			getIDs();
			FILTER_TYPE  = getArguments().getByte(KEY);
			switch(FILTER_TYPE){
			case INDUSTRY_FILTER:
				storedCollections(new JSONObject(
						Utility.ReadFromfile("filterIndustry.txt", getActivity())).getJSONArray("industries"));
				break;
			case FUNCTIONAL_AREA_FILTER:
				storedCollections(new JSONObject(
						Utility.ReadFromfile("filterFunctional.txt", getActivity())).getJSONArray("functionalareas"));
				break;
			case INSTITUTE_FILTER:
				storedCollections(new JSONObject(
						Utility.ReadFromfile("filterInstitute.txt", getActivity())).getJSONArray("institutes"));
				break;
			case LOCATION_FILTER:
				storedCollections(new JSONObject(
						Utility.ReadFromfile("preferredlocation.txt", getActivity())).getJSONArray("locations"));
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View convertView =  inflater.inflate(R.layout.filter_list_layout, container, false);
		searchListField = (EditText) convertView.findViewById(R.id.edit_field);
		//InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
		//imm.hideSoftInputFromWindow(searchListField.getWindowToken(), 0);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		switch(FILTER_TYPE){
		case INDUSTRY_FILTER:
			searchListField.setHint(getActivity().getString(R.string.search_by_industry_lbl));
			break;
		case FUNCTIONAL_AREA_FILTER:
			searchListField.setHint(getActivity().getString(R.string.search_by_farea_lbl));
			break;
		case INSTITUTE_FILTER:
			searchListField.setHint(getActivity().getString(R.string.search_by_institute_lbl));
			break;
		case LOCATION_FILTER:
			searchListField.setHint(getActivity().getString(R.string.search_by_location_lbl));
			break;
		}
		listView = (ListView) convertView.findViewById(R.id.filter_list);
		if(itemsList != null && itemsList.size() > 0){
			//			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
			//					android.R.layout.simple_list_item_1, android.R.id.text1, itemsArray);

			final MultipleSelectionAdapter adapter = new MultipleSelectionAdapter(getActivity(), itemsList);
			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					final FilterModel mDataModel = (FilterModel) adapter.getItem(arg2);

					ImageView checkBoxIv = (ImageView) arg1.findViewById(R.id.checkbox_iv);
					if(!mDataModel.isSelected()){
						checkBoxIv.setBackgroundResource(R.drawable.filter_checked);
						mDataModel.setSelected(true);
						switch(FILTER_TYPE){
						case INDUSTRY_FILTER:
							industryMap.put(mDataModel.getTitleName(), mDataModel);
							break;
						case FUNCTIONAL_AREA_FILTER:
							funcrtionalMap.put(mDataModel.getTitleName(), mDataModel);
							break;
						case INSTITUTE_FILTER:
							instituteMap.put(mDataModel.getTitleName(), mDataModel);
							break;
						case LOCATION_FILTER:
							locationMap.put(mDataModel.getTitleName(), mDataModel);
							break;
						}
					}else{
						checkBoxIv.setBackgroundResource(R.drawable.filter_unchecked);
						mDataModel.setSelected(false);
						switch(FILTER_TYPE){
						case INDUSTRY_FILTER:
							industryMap.remove(mDataModel.getTitleName());
							break;
						case FUNCTIONAL_AREA_FILTER:
							funcrtionalMap.remove(mDataModel.getTitleName());
							break;
						case INSTITUTE_FILTER:
							instituteMap.remove(mDataModel.getTitleName());
							break;
						case LOCATION_FILTER:
							locationMap.remove(mDataModel.getTitleName());
							break;
						}
					}
				}
			});

			searchListField.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
					// When user changed the Text
					adapter.getFilter().filter(cs);   
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub                          
				}
			});
		}
		return convertView;

	}
	private void getIDs(){
		industryIds = MySharedPreference.getFilterType(Constant.FILTER_INDUSTRY_PARAMS, "0");
		functionalIds = MySharedPreference.getFilterType(Constant.FILTER_FUNCTIONAL_PARAMS, "0");
		instituteIds = MySharedPreference.getFilterType(Constant.FILTER_INSTITUTE_PARAMS, "0");
		locationIds = MySharedPreference.getFilterType(Constant.FILTER_LOCATION_PARAMS, "0");
	}
	private FilterModel getModelFromDB(String title, String id){
		FilterModel iModelObj = null;
		switch(FILTER_TYPE){
		case INDUSTRY_FILTER:
			if(!industryIds.equals("0") && isAvailableId(id, industryIds.split(","))){
				iModelObj= new FilterModel();
				iModelObj.setTitleName(title);
				iModelObj.setId(id);
				iModelObj.setSelected(true);
			}
			break;
		case FUNCTIONAL_AREA_FILTER:
			if(!functionalIds.equals("0") && isAvailableId(id, functionalIds.split(","))){
				iModelObj= new FilterModel();
				iModelObj.setTitleName(title);
				iModelObj.setId(id);
				iModelObj.setSelected(true);
			}
			break;
		case INSTITUTE_FILTER:
			if(!instituteIds.equals("0") && isAvailableId(id, instituteIds.split(","))){
				iModelObj= new FilterModel();
				iModelObj.setTitleName(title);
				iModelObj.setId(id);
				iModelObj.setSelected(true);
			}
			break;
		case LOCATION_FILTER:
			if(!locationIds.equals("0") && isAvailableId(id, locationIds.split(","))){
				iModelObj= new FilterModel();
				iModelObj.setTitleName(title);
				iModelObj.setId(id);
				iModelObj.setSelected(true);
			}
			break;
		}

		return iModelObj;
	}

	private boolean isAvailableId(String id, String[] array){
		for(int i = 0; i < array.length; i++){
			if(array[i].equals(id)){
				return true;
			}
		}

		return false;
	}

	private void storedCollections(JSONArray jsonArray){
		try {
			for(int s = 0; s < jsonArray.length(); s++){	
				final FilterModel obj = new FilterModel();

				switch(FILTER_TYPE){
				case INDUSTRY_FILTER:
					final FilterModel iObj = getItemModel(jsonArray.getJSONObject(s).getString("name"), industryMap);
					final FilterModel iDBobj = getModelFromDB(jsonArray.getJSONObject(s).getString("name"), jsonArray.getJSONObject(s).getString("id"));
					if(iDBobj != null){
						industryMap.put(iDBobj.getTitleName(), iDBobj);
						itemsList.add(iDBobj);
					}else if(iObj != null){
						iObj.setSelected(true);
						itemsList.add(iObj);
					}else{
						obj.setTitleName(jsonArray.getJSONObject(s).getString("name"));
						obj.setId(jsonArray.getJSONObject(s).getString("id"));
						obj.setSelected(false);
						itemsList.add(obj);
					}
					break;
				case FUNCTIONAL_AREA_FILTER:
					final FilterModel fObj = getItemModel(jsonArray.getJSONObject(s).getString("name"), funcrtionalMap);
					final FilterModel fDBobj = getModelFromDB(jsonArray.getJSONObject(s).getString("name"), jsonArray.getJSONObject(s).getString("id"));
					if(fDBobj != null){
						funcrtionalMap.put(fDBobj.getTitleName(), fDBobj);
						itemsList.add(fDBobj);
					}else if(fObj != null){
						fObj.setSelected(true);
						itemsList.add(fObj);
					}else{
						obj.setTitleName(jsonArray.getJSONObject(s).getString("name"));
						obj.setId(jsonArray.getJSONObject(s).getString("id"));
						obj.setSelected(false);
						itemsList.add(obj);
					}
					break;
				case INSTITUTE_FILTER:
					final FilterModel inObj = getItemModel(jsonArray.getJSONObject(s).getString("name"), instituteMap);
					final FilterModel lnDBobj = getModelFromDB(jsonArray.getJSONObject(s).getString("name"), jsonArray.getJSONObject(s).getString("id"));
					if(lnDBobj != null){
						instituteMap.put(lnDBobj.getTitleName(), lnDBobj);
						itemsList.add(lnDBobj);
					}else if(inObj != null){
						inObj.setSelected(true);
						itemsList.add(inObj);
					}else{
						obj.setTitleName(jsonArray.getJSONObject(s).getString("name"));
						obj.setId(jsonArray.getJSONObject(s).getString("id"));
						obj.setSelected(false);
						itemsList.add(obj);
					}
					break;
				case LOCATION_FILTER:
					if(s != 4){ // For Delhi/NCR tag ignored
						final FilterModel lObj = getItemModel(jsonArray.getJSONObject(s).getString("name"), locationMap);
						final FilterModel lsDBobj = getModelFromDB(jsonArray.getJSONObject(s).getString("name"), jsonArray.getJSONObject(s).getString("id"));
						if(lsDBobj != null){
							locationMap.put(lsDBobj.getTitleName(), lsDBobj);
							itemsList.add(lsDBobj);
						}else if(lObj != null){
							lObj.setSelected(true);
							itemsList.add(lObj);
						}else{
							obj.setTitleName(jsonArray.getJSONObject(s).getString("name"));
							obj.setId(jsonArray.getJSONObject(s).getString("id"));
							obj.setSelected(false);
							itemsList.add(obj);
						}
					}
					break;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	private FilterModel getItemModel(String title, HashMap<String, FilterModel> hashMap){
		return (FilterModel)hashMap.get(title);
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
