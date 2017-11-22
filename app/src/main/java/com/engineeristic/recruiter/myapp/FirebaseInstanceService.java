package com.engineeristic.recruiter.myapp;

import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.Utility;

import org.json.JSONObject;

import java.util.ArrayList;

public class FirebaseInstanceService extends FirebaseInstanceIdService {
    //private static final String TAG = "MyFirebaseIIDService";
    private ArrayList<JSONObject> arrContacts;
    @Override
    public void onTokenRefresh() {
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        storeRegistrationId(getApplicationContext(), refreshedToken);
    }
    private void storeRegistrationId(Context context, String regId) {
        int appVersion = Utility.getAppVersion();
        MySharedPreference.saveRegValue(Constant.KEY_REG_ID, regId);
        MySharedPreference.saveRegValue(Constant.KEY_APP_VERSION, ""+appVersion);
    }
}