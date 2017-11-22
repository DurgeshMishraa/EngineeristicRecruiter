package com.engineeristic.recruiter.filter;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class BatchFilterFragment extends Fragment implements OnClickListener{

	private RelativeLayout minLayout, maxLayout;
	private TextView minTextView, maxTextView;
	public static String  temp01 = "", temp02 = "";
	private int preSelectionIndex01 = 0, preSelectionIndex02 = 0;
	private String[] minBatchArray = null, maxBatchArray;
	public static  HashMap<String, String> minBatchHashMap = new HashMap<String, String>();
	public static  HashMap<String, String> maxBatchHashMap = new HashMap<String, String>();
	private String minId = "", maxId = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		minId = MySharedPreference.getFilterType(Constant.FILTER_MIN_BATCH_PARAMS, "-1");
		maxId = MySharedPreference.getFilterType(Constant.FILTER_MAX_BATCH_PARAMS, "-1");
		try{
			JSONArray minArray = new JSONObject(
					Utility.ReadFromfile("filteMinBatch.txt", getActivity())).getJSONArray("batchfromyears");
			minBatchArray = new String[minArray.length()];
			for(short s = 0; s < minArray.length(); s++){		
				minBatchArray[s] = minArray.getJSONObject(s).getString("name");
				minBatchHashMap.put(minArray.getJSONObject(s).getString("name"), minArray.getJSONObject(s).getString("id"));
				if(!minId.equals("-1") && minArray.getJSONObject(s).getString("id").equals(minId)){
					temp01 = minArray.getJSONObject(s).getString("name");
				}
			}
			JSONArray maxArray = new JSONObject(
					Utility.ReadFromfile("filteMaxBatch.txt", getActivity())).getJSONArray("batchtoyears");
			maxBatchArray = new String[maxArray.length()];
			for(short s = 0; s < maxArray.length(); s++){		
				maxBatchArray[s] = maxArray.getJSONObject(s).getString("name");
				maxBatchHashMap.put(maxArray.getJSONObject(s).getString("name"), maxArray.getJSONObject(s).getString("id"));
				if(!maxId.equals("-1") && maxArray.getJSONObject(s).getString("id").equals(maxId)){
					temp02 = maxArray.getJSONObject(s).getString("name");
				}
			}
			if(!temp01.equals("Any") && minBatchArray != null){
				preSelectionIndex01 = Utility.getIndexInArray(temp01, minBatchArray);
			}
			if(!temp02.equals("Any") && maxBatchArray != null){
				preSelectionIndex02 = Utility.getIndexInArray(temp02, maxBatchArray);
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
		View convertView =  inflater.inflate(R.layout.filter_batch_layout, container, false);
		minLayout = (RelativeLayout) convertView.findViewById(R.id.min_expe_layout);
		maxLayout = (RelativeLayout) convertView.findViewById(R.id.max_expe_layout);

		minTextView = (TextView) convertView.findViewById(R.id.min_value_text);
		maxTextView = (TextView) convertView.findViewById(R.id.max_value_text);
		if(!temp01.equals("") && !temp01.equals("Any")){
			minTextView.setText(temp01);
		}else{
			minTextView.setText(getString(R.string.any_lbl));
		}

		if(!temp02.equals("") && !temp02.equals("Any")){
			maxTextView.setText(temp02);
		}else{
			maxTextView.setText(getString(R.string.any_lbl));
		}
		minLayout.setOnClickListener(this);
		maxLayout.setOnClickListener(this);
		return convertView;
	}
	public static String getMinBatch(){
		if(temp01.equals("Any")){
			return "-1";
		}else{
			return (String)minBatchHashMap.get(temp01);
		}
	}
	public static String getMaxBatch(){
		if(temp02.equals("Any")){
			return "-1";
		}else{
			return (String)maxBatchHashMap.get(temp02);
		}
	}
	public static void clearHistory(){
		minBatchHashMap.clear();
		maxBatchHashMap.clear();
		temp01 = "";
		temp02 = "";
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.min_expe_layout:
			if(minBatchArray != null)
				minExpChoice(preSelectionIndex01, "Select "+getString(R.string.min_batch_lbl), minBatchArray);
			break;
		case R.id.max_expe_layout:
			if(maxBatchArray != null)
				maxExpChoice(preSelectionIndex02, "Select "+getString(R.string.min_batch_lbl), maxBatchArray);
			break;
		}
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
				if(!temp01.equals("Any")){
					minTextView.setText(temp01);
					preSelectionIndex01 = Utility.getIndexInArray(temp01, minBatchArray);
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
		builder.setTitle(""+header)
		.setSingleChoiceItems(array, preSel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				temp02 = (String) array[which];
			}
		})
		.setPositiveButton("Set", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				if(!temp02.equals("Any")){
					maxTextView.setText(temp02);
					preSelectionIndex02 = Utility.getIndexInArray(temp02, maxBatchArray);
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
}
