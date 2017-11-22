package com.engineeristic.recruiter.filter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.MySharedPreference;

public class GenderFilterFragment extends Fragment implements OnClickListener{


	private LinearLayout maleLayout, femaleLayout, thirdLayout, fourthLayout;
	private ImageView maleRadio, femaleRadio, thirdRadio, anyRadio;
	private TextView maleTextView, femaleTextView, thirdTextView, fourthTextView;
	public static String genderType = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView =  inflater.inflate(R.layout.filter_gender_layout, container, false);
		maleLayout = (LinearLayout)convertView.findViewById(R.id.male_radio_layout);
		femaleLayout = (LinearLayout) convertView.findViewById(R.id.female_radio_layout);
		thirdLayout = (LinearLayout) convertView.findViewById(R.id.third_radio_layout);
		fourthLayout = (LinearLayout) convertView.findViewById(R.id.four_radio_layout);

		maleTextView = (TextView) convertView.findViewById(R.id.view_count_text01);
		femaleTextView = (TextView) convertView.findViewById(R.id.view_count_text02);
		thirdTextView = (TextView) convertView.findViewById(R.id.view_count_text03);
		fourthTextView = (TextView) convertView.findViewById(R.id.view_count_text04);

		maleRadio = (ImageView) convertView.findViewById(R.id.radio_male);
		femaleRadio = (ImageView) convertView.findViewById(R.id.radio_female);
		thirdRadio = (ImageView) convertView.findViewById(R.id.radio_third);
		anyRadio = (ImageView) convertView.findViewById(R.id.radio_four);

		maleLayout.setOnClickListener(this);
		femaleLayout.setOnClickListener(this);
		thirdLayout.setOnClickListener(this);
		fourthLayout.setOnClickListener(this);
		femaleTextView.setOnClickListener(this);
		maleTextView.setOnClickListener(this);
		thirdTextView.setOnClickListener(this);
		fourthTextView.setOnClickListener(this);

		if(genderType == null)
			genderType = MySharedPreference.getFilterType(Constant.FILTER_GENDER_PARAMS, "-1");

		if(genderType != null){
			switch(genderType){
			case "1":
				maleLayout.performClick();
				break;
			case "2":
				femaleLayout.performClick();
				break;
			case "3":
				thirdLayout.performClick();
				break;
			}
		}
		return convertView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.male_radio_layout:
		case R.id.view_count_text01:
			maleRadio.setBackgroundResource(R.drawable.profile_notification_sel);
			femaleRadio.setBackgroundResource(R.drawable.profile_notification_unsel);
			thirdRadio.setBackgroundResource(R.drawable.profile_notification_unsel);
			anyRadio.setBackgroundResource(R.drawable.profile_notification_unsel);
			genderType = "1";
			break;
		case R.id.female_radio_layout:
		case R.id.view_count_text02:
			femaleRadio.setBackgroundResource(R.drawable.profile_notification_sel);	
			maleRadio.setBackgroundResource(R.drawable.profile_notification_unsel);	
			thirdRadio.setBackgroundResource(R.drawable.profile_notification_unsel);	
			anyRadio.setBackgroundResource(R.drawable.profile_notification_unsel);
			genderType = "2";
			break;
		case R.id.third_radio_layout:
		case R.id.view_count_text03:
			femaleRadio.setBackgroundResource(R.drawable.profile_notification_unsel);	
			maleRadio.setBackgroundResource(R.drawable.profile_notification_unsel);	
			thirdRadio.setBackgroundResource(R.drawable.profile_notification_sel);
			anyRadio.setBackgroundResource(R.drawable.profile_notification_unsel);
			genderType = "3";
			break;
		case R.id.four_radio_layout:
		case R.id.view_count_text04:
			femaleRadio.setBackgroundResource(R.drawable.profile_notification_unsel);	
			maleRadio.setBackgroundResource(R.drawable.profile_notification_unsel);	
			thirdRadio.setBackgroundResource(R.drawable.profile_notification_unsel);
			anyRadio.setBackgroundResource(R.drawable.profile_notification_sel);
			genderType = "-1";
			break;

		default:
			break;
		}
	}
}
