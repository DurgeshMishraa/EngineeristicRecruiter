package com.engineeristic.recruiter.myapp;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.chat.ChatConstants;
import com.engineeristic.recruiter.chat.ChatPreferences;
import com.engineeristic.recruiter.model.LoginModel;
import com.engineeristic.recruiter.model.LoginProfileModel;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText usname;
    private EditText pass;
    private TextView forgetPassword;
    private Button signin;
    private TextView userSignup;
    private ArrayList<JSONObject> arrContacts;

    private RelativeLayout rl_coordinator;
    private String errorMsg;;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        usname = (EditText) findViewById(R.id.login_edt_id);
        pass = (EditText) findViewById(R.id.login_edt_pass);
        signin = (Button) findViewById(R.id.login_button);
        forgetPassword = (TextView)findViewById(R.id.forgotPassword);
        userSignup = (TextView)findViewById(R.id.userSignup);
        rl_coordinator = (RelativeLayout) findViewById(R.id.Login_rel_layout);

        userSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValide()){
                connectToLogin();
                hideKeyBoard();
                }
                /*else{
                    Snackbar snackbar = Snackbar.make(rl_coordinator, "Please fill valid credential", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }*/

            }
        });

        pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    signin.performClick();
                }
                return false;
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                if(usname.getText().toString() != null && usname.getText().toString().trim().length() > 0){
                    intent.putExtra(Constant.KEY_USER_EMAIL, usname.getText().toString().trim());
                }
                startActivity(intent);
            }
        });

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                readContacts();
                return null;
            }

        }.execute();
        Utility.setContext(this);
        ChatPreferences.getChatSharedInstance().setContextPreference(Utility.getContext());
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    private boolean isValide(){
        String target = usname.getText().toString();
        if (usname.getText().toString().equals("")){
            Snackbar snackbar = Snackbar.make(rl_coordinator, getResources().getString(R.string.enter_valid_emailpass_message), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }else if (pass.getText().toString().equals("")){
            Snackbar snackbar = Snackbar.make(rl_coordinator, getResources().getString(R.string.enter_valid_emailpass_message), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }else if (!Utility.isValidEmail(target)) {
            Snackbar snackbar = Snackbar.make(rl_coordinator, getResources().getString(R.string.enter_valid_emailpass_message), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        return true;
    }
    private void hideKeyBoard(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public void readContacts()
    {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String phone = null; String emailContact = null;
        if (cur.getCount() > 0)
        {
            arrContacts = new ArrayList<JSONObject>();
            while (cur.moveToNext()) {
             String id = cur.getString(cur .getColumnIndex(ContactsContract.Contacts._ID));
             String name = cur .getString(cur .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
             if (Integer .parseInt(cur.getString(cur .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
              {
               JSONObject phoneContact=new JSONObject();
               try {
                     phoneContact.put("contactname", name);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    Cursor pCur = cr.query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
                    while (pCur.moveToNext())
                    {
                        if(pCur.getCount()==1)
                        {
                            phone = pCur .getString(pCur .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            try {
                                phoneContact.put("phoneNumber1", phone);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            for(int i=0;i<pCur.getCount();i++)
                            {
                                phone = pCur .getString(pCur .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                try {
                                    phoneContact.put("phoneNumber"+(i+1), phone);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                    pCur.close();
                    Cursor emailCur = cr.query( ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[] { id }, null);
                    while (emailCur.moveToNext()) {
                        emailContact = emailCur .getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        try {
                            phoneContact.put("email", emailContact);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    emailCur.close();
                    arrContacts.add(phoneContact);
                }
            }
        }


    }
    private void connectToLogin(){
        if (Utility.isNetworkAvailableRelative(this, rl_coordinator)){
            Utility.showLoadingDialog(LoginActivity.this);
            RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utility.dismisLoadingDialog();
                            if(response != null) {
                                Gson mGson = new GsonBuilder().create();
                                LoginModel iModel = mGson.fromJson(response, LoginModel.class);
                                if (iModel != null) {
                                    if(iModel.getStatus().equalsIgnoreCase(Constant.HTTP_OK)){
                                        LoginProfileModel pModel = iModel.getProfile();
                                        String name = pModel.getName();
                                        MySharedPreference.saveKeyValue(Constant.KEY_USER_COOKIE, iModel.getEn_cookie());
                                        MySharedPreference.saveKeyValue(Constant.KEY_USER_REC_ID, iModel.getProfile().getRecruiter_id());
                                        MySharedPreference.saveKeyValue(Constant.KEY_USER_NAME, iModel.getProfile().getName());
                                        MySharedPreference.saveKeyValue(Constant.KEY_USER_EMAIL, iModel.getProfile().getEmail());
                                        MySharedPreference.saveKeyValue(Constant.KEY_USER_PHONE, iModel.getProfile().getPhone());
                                        MySharedPreference.saveKeyValue(Constant.KEY_USER_ORGANIZATION, iModel.getProfile().getOrganisation());
                                        MySharedPreference.saveKeyValue(Constant.KEY_USER_DESIGNATION, iModel.getProfile().getDesignation());
                                        MySharedPreference.saveKeyValue(Constant.KEY_USER_UUID, iModel.getProfile().getUUID());
                                        MySharedPreference.saveKeyValue(Constant.KEY_USER_IMAGE, iModel.getProfile().getImage());
                                        MySharedPreference.saveKeyValue(Constant.KEY_USER_NOTIFICATION, iModel.getProfile().getMynotification());
                                        MySharedPreference.saveKeyValue(Constant.KEY_USER_AUTH_KEY, iModel.getProfile().getAUTHKEY());
                                        Constant.USER_COOKIE = MySharedPreference.getKeyValue(Constant.KEY_USER_COOKIE);
                                        ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.LOGIN_UUID, iModel.getProfile().getUUID());
                                        ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.LOGIN_MYNOTIFICATION, iModel.getProfile().getMynotification());
                                        connectToApnService();
                                        Intent ic = new Intent(LoginActivity.this,JobsListActivity.class);
                                        startActivity(ic);
                                        finish();
                                    } else{
                                        errorMsg = iModel.getError_msg().toString().trim();
                                        Snackbar snackbar = Snackbar.make(rl_coordinator, errorMsg, Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                }

                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Utility.dismisLoadingDialog();
                            if (error != null) {
                                String titleMessage = "";
                                String messageText = "";
                                if (error instanceof NoConnectionError
                                        || error instanceof NetworkError) {
                                    titleMessage = getString(R.string.network_lbl);
                                    messageText = getResources().getString(R.string.msg_no_network);
                                } else if (error instanceof TimeoutError) {
                                    titleMessage = getString(R.string.retry_lbl);
                                    messageText = getResources().getString(R.string.msg_connection_timout);
                                } else {
                                    titleMessage = getString(R.string.error_lbl);
                                    messageText = getResources().getString(R.string.msg_server_error);
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle(titleMessage).setMessage(messageText)
                                        .setCancelable(true)
                                        .setPositiveButton(getString(R.string.ok_lbl), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                if (error instanceof TimeoutError)
                                                    connectToLogin();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                                alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
                            }
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("email", usname.getText().toString().trim());
                    params.put("password", pass.getText().toString().trim());
                    return params;
                }

                @Override
                public Map getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();

                    if(!Constant.isLiveServer){
                        String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                                ("mba" + ":" + "iim1@2#3$").getBytes(),
                                Base64.NO_WRAP);
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
                                        errorMsg = iModel.getError_msg();
                                        Snackbar snackbar = Snackbar.make(rl_coordinator, errorMsg, Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            error.getMessage();
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("token", getRegistrationId(LoginActivity.this));
                    if(arrContacts != null && arrContacts.size() > 0){
                        params.put("uploadContacts", arrContacts.toString());
                    }
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
                    headers.put("AUTHKEY", ""+MySharedPreference.getKeyValue(Constant.KEY_USER_AUTH_KEY));
                    return headers;
                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(Constant.SOCKET_TIMEOUT_DURATION, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);
        }
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
}
