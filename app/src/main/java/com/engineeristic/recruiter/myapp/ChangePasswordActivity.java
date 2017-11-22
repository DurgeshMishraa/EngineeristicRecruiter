package com.engineeristic.recruiter.myapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.engineeristic.recruiter.model.ChangePasswordModel;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.Utility;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView  pBackTextView, saveProfileTextView;
    private EditText originalPassField
            , newPassField
            , verifyPassField;
    //private TextView  mTv_Change_Password, mTv_New_Password, mTv_Verify_Password;
    private Typeface typeRobotoMedium, typeRobotoRegular;
    private LinearLayout llParent;
    private Button changeCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.change_password_screen);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        typeRobotoRegular = Typeface.createFromAsset(getApplicationContext().getAssets(),"robotoregular.ttf");
        typeRobotoMedium = Typeface.createFromAsset(getApplicationContext().getAssets(),"robotomedium.ttf");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_changepassword);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation((float)10.0);
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.title_text_clr));
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.toolbar_backicon_clr), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(getApplicationContext().getResources().getString(R.string.change_password_lbl));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llParent = (LinearLayout)findViewById(R.id.ll_parent_changepass);
        /*mTv_Change_Password = (TextView)findViewById(R.id.tv_original_password);
        mTv_New_Password = (TextView)findViewById(R.id.tv_new_password);
        mTv_Verify_Password = (TextView)findViewById(R.id.tv_verify_password);
        mTv_Change_Password.setTypeface(typeRobotoRegular);
        mTv_New_Password.setTypeface(typeRobotoRegular);
        mTv_Verify_Password.setTypeface(typeRobotoRegular);*/

        originalPassField = (EditText) findViewById(R.id.edit_original_pass);
        newPassField = (EditText) findViewById(R.id.edit_new_pass);
        verifyPassField = (EditText) findViewById(R.id.edit_verify_pass);
        changeCommand = (Button) findViewById(R.id.change_action);
        originalPassField.setTypeface(typeRobotoRegular);
        newPassField.setTypeface(typeRobotoRegular);
        verifyPassField.setTypeface(typeRobotoRegular);
        changeCommand.setTypeface(typeRobotoRegular);
        changeCommand.setOnClickListener(this);
        verifyPassField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    changeCommand.performClick();
                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_action:
                hideKeyBoard();
                if(isValide()){
                connectToChangePassword();
                }
                break;
        }
    }
    private void hideKeyBoard(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
    private void connectToChangePassword(){
        if (Utility.isNetworkAvailableLinear(this, llParent)){
            String url =  Constant.CHANGE_PASSWORD_URL + Constant.USER_COOKIE;
            Utility.showLoadingDialog(ChangePasswordActivity.this);
            RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utility.dismisLoadingDialog();
                            if(response != null) {
                                Gson mGson = new GsonBuilder().create();
                                ChangePasswordModel iModel = mGson.fromJson(response, ChangePasswordModel.class);
                                if (iModel != null) {
                                    if(iModel.getStatus().equalsIgnoreCase(Constant.HTTP_OK)){
                                        Snackbar snackbar = Snackbar.make(llParent, iModel.getSuccess_msg(), Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                        finish();
                                    } else{
                                        Snackbar snackbar = Snackbar.make(llParent, iModel.getError_msg(), Snackbar.LENGTH_SHORT);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                                builder.setTitle(titleMessage).setMessage(messageText)
                                        .setCancelable(true)
                                        .setPositiveButton(getString(R.string.ok_lbl), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                                if (error instanceof TimeoutError)
                                                    connectToChangePassword();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
                            }
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("old_password", originalPassField.getText().toString().trim());
                    params.put("new_password", newPassField.getText().toString().trim());
                    params.put("cnew_password", verifyPassField.getText().toString().trim());
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
                    headers.put("AUTHKEY", ""+ MySharedPreference.getKeyValue(Constant.KEY_USER_AUTH_KEY));

                    return headers;
                }

            };
            RetryPolicy policy = new DefaultRetryPolicy(Constant.SOCKET_TIMEOUT_DURATION, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);
        }
    }
    private boolean isValide(){
        if(originalPassField.getText().toString().trim().length() <= 0 || originalPassField.getText().toString().trim().equals("")){
            Snackbar snackbar = Snackbar.make(llParent, getResources().getString(R.string.msg_original_password), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }if(originalPassField.getText().toString().trim().length() < 5){
            Snackbar snackbar = Snackbar.make(llParent, getApplicationContext().getString(R.string.msg_original_password) +" "+ getApplicationContext().getString(R.string.msg_password_char), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }else if(newPassField.getText().toString().trim().length() <= 0 || newPassField.getText().toString().trim().equals("")){
            Snackbar snackbar = Snackbar.make(llParent, getResources().getString(R.string.msg_new_password), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }if(newPassField.getText().toString().trim().length() < 5){
            Snackbar snackbar = Snackbar.make(llParent, getApplicationContext().getString(R.string.new_pass_lbl) +" "+ getApplicationContext().getString(R.string.msg_password_char), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }else if(verifyPassField.getText().toString().trim().length() <= 0 || verifyPassField.getText().toString().trim().equals("")){
            Snackbar snackbar = Snackbar.make(llParent, getResources().getString(R.string.msg_verify_password), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }if(verifyPassField.getText().toString().trim().length() < 5){
            Snackbar snackbar = Snackbar.make(llParent, getResources().getString(R.string.verify_pass_lbl), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }else if(!newPassField.getText().toString().trim().equals(verifyPassField.getText().toString().trim())){
            Snackbar snackbar = Snackbar.make(llParent, getResources().getString(R.string.msg_verify_password), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }else if(originalPassField.getText().toString().trim().equals(verifyPassField.getText().toString().trim())){
            Snackbar snackbar = Snackbar.make(llParent, getResources().getString(R.string.msg_verify_password_diff), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        return true;
    }
}
