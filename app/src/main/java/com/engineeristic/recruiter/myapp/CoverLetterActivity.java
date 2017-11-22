package com.engineeristic.recruiter.myapp;

import android.app.AlertDialog;
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
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.model.CoverLatterModel;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.Utility;

import java.util.HashMap;
import java.util.Map;

public class CoverLetterActivity extends AppCompatActivity {
    private TextView txtCover;
    private LinearLayout llParent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.coverletter_layout);
        RecruiterApplication application = (RecruiterApplication) getApplication();
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_coverletter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation((float)10.0);
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.title_text_clr));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.toolbar_backicon_clr), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        llParent = (LinearLayout)findViewById(R.id.ll_parent_coverletter);
        txtCover = (TextView)findViewById(R.id.coverText);
        loadCoverLetter();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCoverLetter(){
        if (Utility.isNetworkAvailableLinear(CoverLetterActivity.this, llParent)){
            String url =  Constant.COVER_LATTER_URL + getIntent().getExtras().getString(Constant.KEY_VIEW_ID)+"/" + Constant.USER_COOKIE;
            Utility.showLoadingDialog(CoverLetterActivity.this);
            RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Utility.dismisLoadingDialog();
                    if(response != null) {
                        Gson mGson = new GsonBuilder().create();
                        CoverLatterModel iModel = mGson.fromJson(response, CoverLatterModel.class);
                        if (iModel != null) {
                            if(iModel.getStatus().equalsIgnoreCase(Constant.HTTP_OK)){
                                //txtName.setText(iModel.getName());
                                getSupportActionBar().setTitle(iModel.getName()+"'s"+" Cover Letter");
                                txtCover.setText(iModel.getCovertext());
                            } else{
                                Snackbar snackbar = Snackbar.make(llParent, iModel.getError_msg(), Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        }
                        else{
                            //getSupportActionBar().setTitle("Cover Letter");
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
                                    messageText = CoverLetterActivity.this.getResources().getString(R.string.msg_no_network);
                                } else if (error instanceof TimeoutError) {
                                    titleMessage = getString(R.string.retry_lbl);
                                    messageText = CoverLetterActivity.this.getResources().getString(R.string.msg_connection_timout);
                                } else {
                                    titleMessage = getString(R.string.error_lbl);
                                    messageText = CoverLetterActivity.this.getResources().getString(R.string.msg_server_error);
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(CoverLetterActivity.this);
                                builder.setTitle(titleMessage).setMessage(messageText)
                                        .setCancelable(true)
                                        .setPositiveButton(CoverLetterActivity.this.getString(R.string.ok_lbl), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                                if (error instanceof TimeoutError)
                                                    loadCoverLetter();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                                alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
                            }
                        }
                    }){
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

}
