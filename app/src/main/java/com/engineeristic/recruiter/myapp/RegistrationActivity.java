package com.engineeristic.recruiter.myapp;

import android.app.AlertDialog;
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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.engineeristic.recruiter.model.LoginModel;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.ConstantFontelloID;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userNameField,userEmailField,userPhoneField,
            userPasswordField,userConfPasswordField,userDesignationField,userOrganizationField;
    private Spinner selectedValue;
    private String chooseOption="";
    private TextView spinnerTitle, mTextViewArrowDown;
    private String option = null;
    private LinearLayout spinnerLayout, ll_Parent;

    private Typeface fontello;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.signup_screen);
        fontello = Typeface.createFromAsset(getApplicationContext().getAssets(),"fontello.ttf");
        RecruiterApplication application = (RecruiterApplication) getApplication();

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        try{
            final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_signup);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setElevation((float)10.0);
            }
            setSupportActionBar(toolbar);
            Spannable text = new SpannableString("New Recruiter Registration");
            text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_text_clr)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            getSupportActionBar().setTitle(text);
            if (getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.toolbar_backicon_clr), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);


            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            ll_Parent = (LinearLayout)findViewById(R.id.ll_coordinator_signup);
            userNameField = (EditText) findViewById(R.id.editName);
            userEmailField = (EditText) findViewById(R.id.editMailAddress);
            userPhoneField = (EditText) findViewById(R.id.phNumber);
            userPasswordField = (EditText) findViewById(R.id.editPassword);
            userConfPasswordField = (EditText) findViewById(R.id.editConfirmPassord);
            userDesignationField = (EditText) findViewById(R.id.editDesignation);
            userOrganizationField = (EditText) findViewById(R.id.editOrganization);
            mTextViewArrowDown = (TextView) findViewById(R.id.spinner_arrow_down);
            mTextViewArrowDown.setTypeface(fontello);
            mTextViewArrowDown.setText(ConstantFontelloID.icon_arrow_down);
            selectedValue = (Spinner)findViewById(R.id.spinner);
            spinnerTitle = (TextView) findViewById(R.id.spinner_title);
            if(spinnerTitle.getText().toString().equalsIgnoreCase(getResources().getString(R.string.Select_lbl))){
                spinnerTitle.setTextColor(Color.parseColor("#9e9e9e"));
            }
            else{
                spinnerTitle.setTextColor(Color.parseColor("#212121"));
            }
            spinnerLayout = (LinearLayout) findViewById(R.id.spinnLayout);
            spinnerLayout.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                        if(selectedValue != null){
                        selectedValue.setVisibility(View.VISIBLE);
                        selectedValue.performClick();
                    }
                    return false;
                }
            });
            addItemsOnSpinner2();
            addListenerOnSpinnerItemSelection();
            Button button = (Button) findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isValide()){
                    userRegister();
                    }
                }
            });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    private void userRegister(){

        if (Utility.isNetworkAvailableLinear(this, ll_Parent)){
            Utility.showLoadingDialog(RegistrationActivity.this);
            RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.REGISTER_USER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utility.dismisLoadingDialog();
                            if(response != null) {
                                Gson mGson = new GsonBuilder().create();
                                LoginModel iModel = mGson.fromJson(response, LoginModel.class);//// Login model will user for Registration
                                if (iModel != null) {
                                    if(iModel.getStatus().equalsIgnoreCase(Constant.HTTP_OK)){
                                        Utility.showToastMessage(getApplicationContext(), iModel.getMessage(), Toast.LENGTH_SHORT);
                                        finish();
                                    } else
                                        Utility.showToastMessage(getApplicationContext(), iModel.getError_msg(), Toast.LENGTH_SHORT);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                                builder.setTitle(titleMessage).setMessage(messageText)
                                        .setCancelable(true)
                                        .setPositiveButton(getString(R.string.ok_lbl), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                if (error instanceof TimeoutError)
                                                    userRegister();
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
                    params.put("name", userNameField.getText().toString().trim());
                    params.put("email", userEmailField.getText().toString().trim());
                    params.put("phone", userPhoneField.getText().toString().trim());
                    params.put("password", userPasswordField.getText().toString().trim());
                    params.put("cpassword", userConfPasswordField.getText().toString().trim());
                    params.put("designation", userDesignationField.getText().toString().trim());
                    params.put("organization", userOrganizationField.getText().toString().trim());
                    params.put("source", option);
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
    private void addItemsOnSpinner2() {

        selectedValue = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add(getResources().getString(R.string.Select_lbl));
        list.add(getResources().getString(R.string.direct_emp_lbl));
        list.add(getResources().getString(R.string.rec_firm_lbl));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        selectedValue.setAdapter(dataAdapter);
    }
    private void addListenerOnSpinnerItemSelection() {
        selectedValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    TextView tmpView = (TextView) selectedValue.getSelectedView().findViewById(R.id.spinner_item_txt);
                    tmpView.setTextColor(Color.BLACK);
                    chooseOption =  selectedValue.getItemAtPosition(position).toString();
                    spinnerTitle.setText(chooseOption);
                    if(chooseOption.equalsIgnoreCase(getResources().getString(R.string.Select_lbl))){
                        spinnerTitle.setTextColor(Color.parseColor("#9e9e9e"));
                    }
                    else{
                        spinnerTitle.setTextColor(Color.parseColor("#212121"));
                    }
                    selectedValue.setVisibility(View.GONE);
                    if(chooseOption.contains(getResources().getString(R.string.direct_emp_lbl)))
                    {
                        option="1";
                    }
                    else
                    {
                        option="2";
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }
    private boolean isValide(){

        if(userNameField !=null & userEmailField !=null & userPhoneField !=null & userPasswordField !=null & userConfPasswordField!=null & userDesignationField !=null & userOrganizationField!=null &&  userNameField.length()>0 & userEmailField.length()>10 || userPhoneField.length()<15  & userPasswordField.length()>0 & userConfPasswordField.length()>0 & userDesignationField.length()>0 & userOrganizationField.length()>0) {
            if(userNameField.getText().toString().length() <= 0){
                Snackbar snackbar = Snackbar.make(ll_Parent, getResources().getString(R.string.enter_name_message), Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }else if(!userEmailField.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                Snackbar snackbar = Snackbar.make(ll_Parent, getResources().getString(R.string.invalid_email_message), Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }else if(userPhoneField.getText().toString().length()!=10){
                Snackbar snackbar = Snackbar.make(ll_Parent, getResources().getString(R.string.enter_valid_number_message), Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }else if(userPasswordField.getText().toString().length() < 5){
                Snackbar snackbar = Snackbar.make(ll_Parent, getResources().getString(R.string.enter_password_message), Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }else if(!userPasswordField.getText().toString().equalsIgnoreCase(userConfPasswordField.getText().toString())){
                Snackbar snackbar = Snackbar.make(ll_Parent, getResources().getString(R.string.confirm_password_message_message), Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }else if(userDesignationField.getText().toString().length() <= 0){
                Snackbar snackbar = Snackbar.make(ll_Parent, getResources().getString(R.string.enter_designation_message), Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }else if(userOrganizationField.getText().toString().length() <= 0){
                Snackbar snackbar = Snackbar.make(ll_Parent, getResources().getString(R.string.enter_organization_message), Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }else if(spinnerTitle.getText().toString().equals(getResources().getString(R.string.Select_lbl))){
                Snackbar snackbar = Snackbar.make(ll_Parent, getResources().getString(R.string.please_select_type_message), Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }
        }else{
            Snackbar snackbar = Snackbar.make(ll_Parent, getResources().getString(R.string.field_mandatory_message), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
