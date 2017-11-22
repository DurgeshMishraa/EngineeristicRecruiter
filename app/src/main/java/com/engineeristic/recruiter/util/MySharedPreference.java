package com.engineeristic.recruiter.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySharedPreference {

	private static SharedPreferences mTypePreference, mGloblePreference, mJobPreference, mFilterPreference, mRecordPreference, mRegPreference;
	private static Editor mTypeEditor, mJobEditor, mFilterEditor, mRecordEditor, mGlobleEditor, mRegEditor;
	private static MySharedPreference mSharedPreference = null;
	private static Context mContext;
	private MySharedPreference(){

	}
	public static MySharedPreference getInstance(){
		if(mSharedPreference == null){
			mSharedPreference = new MySharedPreference();
		}
		return mSharedPreference;
	}
	public void initPrefrenceManger(Context nContext){
		this.mContext = nContext;
		mTypePreference = mContext.getSharedPreferences("REC_PREFRENCE", mContext.MODE_PRIVATE);
		mGloblePreference = mContext.getSharedPreferences("GLOBLE_PREFRENCE", mContext.MODE_PRIVATE);
		mJobPreference = mContext.getSharedPreferences("JOB_PREFRENCE", mContext.MODE_PRIVATE);
		mFilterPreference = mContext.getSharedPreferences("FILTER_PREFRENCE", mContext.MODE_PRIVATE);
		mRecordPreference = mContext.getSharedPreferences("RECORD_PREFRENCE", mContext.MODE_PRIVATE);
		mRegPreference = mContext.getSharedPreferences("REGISTER_PREFRENCE", mContext.MODE_PRIVATE);

		mGlobleEditor = mGloblePreference.edit();
		mRegEditor = mRegPreference.edit();
		mTypeEditor = mTypePreference.edit();
		mJobEditor = mJobPreference.edit();
		mFilterEditor = mFilterPreference.edit();
		mRecordEditor = mRecordPreference.edit();

	}
	public static void saveKeyValue(String KEY, String viewCount){
		mGlobleEditor.putString(KEY, viewCount);
		mGlobleEditor.commit();
	}
	public static String getKeyValue(String key){
		return mGloblePreference.getString(key, null);
	}
	public static void saveRegValue(String KEY, String viewCount){
		mRegEditor.putString(KEY, viewCount);
		mRegEditor.commit();
	}
	public static String getRegValue(String key){
		return mRegPreference.getString(key, "");
	}

	public static void saveFilterType(byte type, String mValue){
		mFilterEditor.putString("FILTER_PARAMS_TYPE"+type, mValue);
		mFilterEditor.commit();
	}
	public static String getFilterType(byte type, String defaltValue){
		String temp = "";
		temp = mFilterPreference.getString("FILTER_PARAMS_TYPE"+type, defaltValue);
		return temp;
	}



	public static void saveRecruiterViewType(byte type, int mCount){
		mTypeEditor.putInt("NEW_CANDIDATE_TYPE"+type, mCount);
		mTypeEditor.commit();
	}
	public static int getRecruiterViewType(byte type){
		return mTypePreference.getInt("NEW_CANDIDATE_TYPE"+type, 0);
	}
	public static void saveRecruiterRecord(byte type, String jobId, String iResponce){
		mRecordEditor.putString(type+"_STORE"+jobId, iResponce);
		mRecordEditor.commit();
	}
	public static String getRecruiterRecord(byte type,String jobId){
		return mRecordPreference.getString(type+"_STORE"+jobId, null);
	}

	public static void saveJobRecord(byte type, String jsonResponce){
		mJobEditor.putString("JOB_TYPES" + type, jsonResponce);
		mJobEditor.commit();
	}

	public static String getJobRecord(byte type){
		return mJobPreference.getString("JOB_TYPES" + type, null);
	}

	public static void clearGlobleRecord(){
		mGlobleEditor.clear().commit();
	}

	public static void clearCandidateRecord(){
		mRecordEditor.clear().commit();
	}

	public static void clearRecruiterType(){
		mTypeEditor.clear().commit();
	}

	public static void clearFilterType(){
		mFilterEditor.clear().commit();
	}
}
