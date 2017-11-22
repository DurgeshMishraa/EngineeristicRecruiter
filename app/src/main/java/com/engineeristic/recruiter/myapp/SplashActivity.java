package com.engineeristic.recruiter.myapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.components.AlarmReciever;
import com.engineeristic.recruiter.model.LoginModel;
import com.engineeristic.recruiter.pojo.UpdateAppData;
import com.engineeristic.recruiter.pojo.UpdateAppModel;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.Utility;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity implements LocationListener{
    private LocationManager locationManager;
    private boolean isPaused;
    private String provider;

    Handler handler = new Handler();
    private String version="";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectToApnService();
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        isPaused = false;
        updateApp();
        setContentView(R.layout.activity_splash);
        Constant.USER_COOKIE = MySharedPreference.getKeyValue(Constant.KEY_USER_COOKIE);
        setAlarm();
        initLocationService();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isPaused){

                    if (Constant.USER_COOKIE != null && !Constant.USER_COOKIE.equals(""))
                    {
                        if(null!= UpdateAppData.status && UpdateAppData.status.equals("200"))
                        {
                            if (null != UpdateAppData.app_status && UpdateAppData.app_status.equals("1")) {
                            setContentView(R.layout.updateapp);
                            TextView headText = (TextView) findViewById(R.id.updateapp_heading);
                            TextView headMsg = (TextView) findViewById(R.id.updateapp_msg);
                            Button upgradeBtn = (Button) findViewById(R.id.updateapp_btn);
                            upgradeBtn.setText(UpdateAppData.btn_text.toString().trim());
                            headText.setText(UpdateAppData.getTitle().toString().trim());
                            headMsg.setText(UpdateAppData.getMsg().toString().trim());

                            upgradeBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=" + JobsListActivity.this.getPackageName())));
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.UPGRADE_APP)));
                                }
                            });
                            }
                            else{
                                Intent i = new Intent(SplashActivity.this, JobsListActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                                finish();
                            }
                        }
                        else{
                        Intent i = new Intent(SplashActivity.this, JobsListActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        finish();
                        }
                    }
                    else{
                        if(null!= UpdateAppData.status && UpdateAppData.status.equals("200")){

                            if(null != UpdateAppData.app_status && UpdateAppData.app_status.equals("1")){
                            setContentView(R.layout.updateapp);
                            TextView headText = (TextView)findViewById(R.id.updateapp_heading);
                            TextView headMsg = (TextView)findViewById(R.id.updateapp_msg);
                            Button upgradeBtn = (Button) findViewById(R.id.updateapp_btn);
                            upgradeBtn.setText(UpdateAppData.btn_text.toString().trim());
                            headText.setText(UpdateAppData.getTitle().toString().trim());
                            headMsg.setText(UpdateAppData.getMsg().toString().trim());

                            upgradeBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=" + JobsListActivity.this.getPackageName())));
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.UPGRADE_APP)));
                                }
                            });
                            }
                            else{
                            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                            finish();
                            }
                        }
                        else{
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        finish();
                    }
                    }
                }
            }
        }, 1000);

    }

    private String getRegistrationId(Context context) {
        String registrationId = MySharedPreference.getRegValue(Constant.KEY_REG_ID);
        if (registrationId.length() == 0) {
            return "";
        }
        int registeredVersion = Integer.parseInt(MySharedPreference.getRegValue(Constant.KEY_APP_VERSION).trim());
        int currentVersion = Utility.getAppVersion();
        if (registeredVersion != currentVersion) {
            Log.d("Recruiter", "App version changed.");
            return "";
        }
        return registrationId;
    }

    private void connectToApnService(){
        if (Utility.isNetworkAvailable(getApplicationContext())){
            String url = Constant.APN_UPDATE_URL + Constant.USER_COOKIE;
            RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response != null) {
                                Gson mGson = new GsonBuilder().create();
                                LoginModel iModel = mGson.fromJson(response, LoginModel.class);
                                if (iModel != null) {
                                    if(iModel.getStatus().equalsIgnoreCase(Constant.HTTP_OK)){

                                    } else
                                        Utility.showToastMessage(getApplicationContext(), iModel.getError_msg(), Toast.LENGTH_SHORT);
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("token", getRegistrationId(SplashActivity.this));
                    params.put("uploadContacts", "");
                    String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    params.put("deviceid", deviceId);
                    return params;
                }
                @Override
                public Map getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    if(!Constant.isLiveServer){
                        String base64EncodedCredentials = "Basic " + Base64.encodeToString(("mba" + ":" + "iim1@2#3$").getBytes(),Base64.NO_WRAP);
                        headers.put("Authorization", base64EncodedCredentials);
                    }
                    headers.put("User-agent", System.getProperty("http.agent")+" "+getResources().getString(R.string.app_name)+" "+Utility.getAppVersionName());
                    return headers;
                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(Constant.SOCKET_TIMEOUT_DURATION, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;

    }

    private void setAlarm() {
        Intent updater = new Intent(getApplicationContext(), AlarmReciever.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, updater, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarms.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 1000, pi);
    }
    private void initLocationService(){
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if(provider!=null && !provider.equals("")){
            // Get the location from the given provider
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 0, 1000, this);
            if(location != null){
                onLocationChanged(location);
            }
        }
    }
    @Override
    public void onLocationChanged(Location arg0) {
        MySharedPreference.saveKeyValue(Constant.KEY_LATITUDE, ""+arg0.getLatitude());
        MySharedPreference.saveKeyValue(Constant.KEY_LONGITUDE, ""+arg0.getLongitude());
        locationManager.removeUpdates(this);
        connectToLocationService(arg0);
    }
    @Override
    public void onProviderDisabled(String arg0) {
    }

    @Override
    public void onProviderEnabled(String arg0) {
    }
    private void connectToLocationService(final Location arg0){
        if(Constant.USER_COOKIE != null && Constant.USER_COOKIE.length()>4) {
            String url = Constant.LOCATION_URL + "en_cookie-" + Constant.USER_COOKIE;
            if (Utility.isNetworkAvailable(getApplicationContext())) {
                RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response != null) {
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(final VolleyError error) {
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("latitude", ""+arg0.getLatitude());
                        params.put("longitude", ""+arg0.getLongitude());
                        return params;
                    }
                };
                RetryPolicy policy = new DefaultRetryPolicy(Constant.SOCKET_TIMEOUT_DURATION, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                queue.add(stringRequest);
            }
        }
    }

    private void updateApp(){
     String strAppVersion = Utility.getAppVersionName();
     String url = Constant.UPDATE_APP+strAppVersion.trim();
     RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
     StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
     @Override
     public void onResponse(String response) {
     Log.e("SplashActivity","response="+response);
     Gson mGson = new GsonBuilder().create();
     UpdateAppModel iModel = mGson.fromJson(response, UpdateAppModel.class);
     if (iModel != null) {
     if(iModel.getStatus().equalsIgnoreCase(Constant.HTTP_OK)){
      Log.e("SplashActivity","updateApp Success");
      UpdateAppData.setStatus(iModel.getStatus());
      UpdateAppData.setApp_status(iModel.getUpdate_app());
      UpdateAppData.setTitle(iModel.getUpdate_title());
      UpdateAppData.setMsg(iModel.getUpdate_detail());
      UpdateAppData.setChat_enable(iModel.getChatEnable());
      UpdateAppData.setBtn_text(iModel.getButton_text());
      }else{
         Log.e("SplashActivity","updateApp Unsuccess");
         UpdateAppData.setStatus(iModel.getStatus());
         }
        }
       }
      },
      new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
      Log.e("SplashActivity", "error= "+error);
       }
      }){
      @Override
      public Map getHeaders() throws AuthFailureError {
      Map<String, String> headers = new HashMap<String, String>();
      if(!Constant.isLiveServer){
        String base64EncodedCredentials = "Basic " + Base64.encodeToString(("mba" + ":" + "iim1@2#3$").getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", base64EncodedCredentials);
        }
      headers.put("User-agent", System.getProperty("http.agent")+" "+getApplicationContext().getResources().getString(R.string.app_name)+" "+Utility.getAppVersionName());
      headers.put("AUTHKEY", ""+MySharedPreference.getKeyValue(Constant.KEY_USER_AUTH_KEY));
      return headers;
       }
      };
      RetryPolicy policy = new DefaultRetryPolicy(Constant.SOCKET_TIMEOUT_DURATION, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
      stringRequest.setRetryPolicy(policy);
      queue.add(stringRequest);
    }
    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

    }
}
