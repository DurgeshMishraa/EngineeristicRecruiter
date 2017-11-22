package com.engineeristic.recruiter.myapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.engineeristic.recruiter.model.EditProfileModel;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.Utility;

import java.util.HashMap;
import java.util.Map;

public class FeedBackActivity extends AppCompatActivity {
    //private Spinner mSpinner;
    private ImageView postImgView;
    private String chooseOption="";
    private String mFeedbackStr=null;
    private EditText detailEditText;
    private LinearLayout spinnerLayout;
    private TextView spinnerTitle;
    private LinearLayout llParent;
    private String spinnerItemArray[] = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_feedback);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_feedback);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation((float)10.0);
        }
        setSupportActionBar(toolbar);
        Spannable text = new SpannableString("Send Feedback");
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_text_clr)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.toolbar_backicon_clr), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        llParent = (LinearLayout)findViewById(R.id.ll_parent_feedback);
        detailEditText = (EditText) findViewById(R.id.editFeedback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;

            case R.id.action_send:
                coonetToFeedback();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
    private void coonetToFeedback() {
        if (Utility.isNetworkAvailableLinear(getApplicationContext(), llParent)){
            if(detailEditText != null && detailEditText.getText().toString().length()>0) {
                hideKeyBoard();
                shareFeedBack();
            }
            else {
                Snackbar snackbar = Snackbar.make(llParent, getResources().getString(R.string.plz_enter_feedback_message), Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
    }
    private void hideKeyBoard(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    private void shareFeedBack(){
        String url =  Constant.FEEDBACK_URL +Constant.USER_COOKIE;
        Log.e("FeedbackActivity","url=="+url);
        Utility.showLoadingDialog(FeedBackActivity.this);
        RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utility.dismisLoadingDialog();
                        if(response != null) {
                            Gson mGson = new GsonBuilder().create();
                            // EditProfileModel Will Use for feedback
                            EditProfileModel iModel = mGson.fromJson(response, EditProfileModel.class);
                            if (iModel != null) {
                                detailEditText.setText("");
                                if(iModel.getStatus().equalsIgnoreCase(Constant.HTTP_OK)){
                                    Snackbar snackbar = Snackbar.make(llParent, iModel.getMessage(), Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                } else {
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
                                titleMessage = getResources().getString(R.string.network_lbl);
                                messageText = getResources().getString(R.string.msg_no_network);
                            } else if (error instanceof TimeoutError) {
                                titleMessage = getResources().getString(R.string.retry_lbl);
                                messageText = getResources().getString(R.string.msg_connection_timout);
                            } else {
                                titleMessage = getResources().getString(R.string.error_lbl);
                                messageText = getResources().getString(R.string.msg_server_error);
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(FeedBackActivity.this);
                            builder.setTitle(titleMessage).setMessage(messageText)
                                    .setCancelable(true)
                                    .setPositiveButton(getString(R.string.ok_lbl), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                            if (error instanceof TimeoutError)
                                                coonetToFeedback();
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
                params.put("title", "Recruiter Feedback");
                params.put("description", detailEditText.getText().toString().trim());

                /*if(!Constant.isLiveServer){
                    String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                            ("mba" + ":" + "iim1@2#3$").getBytes(),
                            Base64.NO_WRAP);
                    params.put("Authorization", base64EncodedCredentials);
                }*/
                /*params.put("User-agent", System.getProperty("http.agent")+" "+getApplicationContext().getResources().getString(R.string.app_name)+" "+Utility.getAppVersionName());
                params.put("AUTHKEY", ""+ MySharedPreference.getKeyValue(Constant.KEY_USER_AUTH_KEY));*/
                return params;
            }

        };
        RetryPolicy policy = new DefaultRetryPolicy(Constant.SOCKET_TIMEOUT_DURATION, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }


}
