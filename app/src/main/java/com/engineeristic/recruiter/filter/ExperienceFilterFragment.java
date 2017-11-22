package com.engineeristic.recruiter.filter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ExperienceFilterFragment extends Fragment implements OnClickListener{

	private RelativeLayout minLayout, maxLayout;
	private TextView minTextView, maxTextView;
	public static String  temp01 = "", temp02 = "";
	private int preSelectionIndex01 = 0, preSelectionIndex02 = 0;
	private String[] expListArray = null;
	public static  HashMap<String, String> expHashMap = new HashMap<String, String>();
	private String minId = "", maxId = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		minId = MySharedPreference.getFilterType(Constant.FILTER_MIN_EXPERIENCE_PARAMS, "-1");
		maxId = MySharedPreference.getFilterType(Constant.FILTER_MAX_EXPERIENCE_PARAMS, "-1");

		try{
			JSONArray jsonArray = new JSONObject(
					Utility.ReadFromfile("filteExperience.txt", getActivity())).getJSONArray("expyears");
			expListArray = new String[jsonArray.length()];
			for(short s = 0; s < jsonArray.length(); s++){		
				expListArray[s] = jsonArray.getJSONObject(s).getString("name");
				expHashMap.put(jsonArray.getJSONObject(s).getString("name"), jsonArray.getJSONObject(s).getString("id"));

				if(!minId.equals("-1") && jsonArray.getJSONObject(s).getString("id").equals(minId)){
					temp01 = jsonArray.getJSONObject(s).getString("name");
				}
				if(!maxId.equals("-1") && jsonArray.getJSONObject(s).getString("id").equals(maxId)){
					temp02 = jsonArray.getJSONObject(s).getString("name");
				}
			}
			if(!temp01.equals(getActivity().getResources().getString(R.string.any_lbl)) && expListArray != null){
				preSelectionIndex01 = Utility.getIndexInArray(temp01, expListArray);
			}
			if(!temp02.equals(getActivity().getResources().getString(R.string.any_lbl)) && expListArray != null){
				preSelectionIndex02 = Utility.getIndexInArray(temp02, expListArray);
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View convertView =  inflater.inflate(R.layout.filter_experience_layout, container, false);

		minLayout = (RelativeLayout) convertView.findViewById(R.id.min_expe_layout);
		maxLayout = (RelativeLayout) convertView.findViewById(R.id.max_expe_layout);

		minTextView = (TextView) convertView.findViewById(R.id.min_value_text);
		maxTextView = (TextView) convertView.findViewById(R.id.max_value_text);

		if(!temp01.equals("") && !temp01.equals(getActivity().getResources().getString(R.string.any_lbl))){
			minTextView.setText(temp01+ " "+ getActivity().getString(R.string.yrs_lbl));
		}else{
			minTextView.setText(getString(R.string.any_lbl));

		}
		if(!temp02.equals("") && !temp02.equals(getActivity().getResources().getString(R.string.any_lbl))){
			maxTextView.setText(temp02+ " "+ getActivity().getString(R.string.yrs_lbl));
		}else{
			maxTextView.setText(getString(R.string.any_lbl));
		}

		minLayout.setOnClickListener(this);
		maxLayout.setOnClickListener(this);

		return convertView;
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
	private  void minExpChoice(final int preSel, String header, final CharSequence[] array) {
		//Initialize the Alert Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),  R.style.MaterialThemeDialog);
		builder.setTitle(""+header)
		.setSingleChoiceItems(array, preSel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				temp01 = (String) array[which];
			}
		})
		.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				if(!temp01.equals(getActivity().getResources().getString(R.string.any_lbl))){
					minTextView.setText(temp01+ " "+ getActivity().getString(R.string.yrs_lbl));
					preSelectionIndex01 = Utility.getIndexInArray(temp01, expListArray);
				}else{
					minTextView.setText(temp01);
				}
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		android.app.AlertDialog alert = builder.create();
		alert.show();
		alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
		alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);


	}
	private  void maxExpChoice(final int preSel, String header, final CharSequence[] array) {
		//Initialize the Alert Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),  R.style.MaterialThemeDialog);
		builder.setTitle(""+header).setSingleChoiceItems(array, preSel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				temp02 = (String) array[which];
			}
		})
		.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				if(!temp02.equals(getActivity().getResources().getString(R.string.any_lbl))){
					maxTextView.setText(temp02+ " "+ getActivity().getString(R.string.yrs_lbl));
					preSelectionIndex02 = Utility.getIndexInArray(temp02, expListArray);
				}else{
					maxTextView.setText(temp02);
				}
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		android.app.AlertDialog alert = builder.create();
		alert.show();
		alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
		alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);

	}
	public static String getMinExp(){
		if(temp01.equals("Any")){
			return "-1";
		}else{
			return (String)expHashMap.get(temp01);
		}
	}
	public static String getMaxExp(){
		if(temp02.equals("Any")){
			return "-1";
		}else{
			return (String)expHashMap.get(temp02);
		}
	}
	public static void clearHistory(){
		expHashMap.clear();
		temp01 = "";
		temp02 = "";
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch(v.getId()){
		case R.id.min_expe_layout:
			if(expListArray != null)
				minExpChoice(preSelectionIndex01, "Select "+getString(R.string.min_experience_lbl), expListArray);
			break;
		case R.id.max_expe_layout:
			if(expListArray != null)
				maxExpChoice(preSelectionIndex02, "Select "+getString(R.string.min_experience_lbl), expListArray);
			break;
		}
	}
}
