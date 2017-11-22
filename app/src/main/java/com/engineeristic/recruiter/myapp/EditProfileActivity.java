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
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.engineeristic.recruiter.model.EditProfileModel;
import com.engineeristic.recruiter.model.LocationsModel;
import com.engineeristic.recruiter.model.PrefLocationModel;
import com.engineeristic.recruiter.model.ProfileModel;
import com.engineeristic.recruiter.model.UserProfileModel;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout instantRadioLayout, dailyRadioLayout;
    private ImageView instantRadioIv, dailyRadioIv;
    private LinearLayout typeSpinnerLayout, citySpinnerLayout;
    private TextView typeSpinnerTitle, citySpinnerTitle, userEmailText;
    private Spinner sSpinner;//, cSpinner;
    private String typeOption = null, applyEmailStatus = null, locationId = "", temp = "";
    private int preSelectionIndex = 0;
    private EditText nameEditText, phoneEditText, organizationEditText, designationEditText, companyEditText, aboutEditText;
    private HashMap<String, String> cityHashMap = new HashMap<String, String>();
    private String[] cityListArray = null;
    private ProfileModel iProfileModel = null;
    private PrefLocationModel iPrefLocModel = null;
    private LocationsModel[] iLocModel = null;
    private LinearLayout llParent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.edit_profile_screen);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation((float)10.0);
        }
        setSupportActionBar(toolbar);
        Spannable text = new SpannableString("Edit Profile");
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_text_clr)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.toolbar_backicon_clr), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        llParent = (LinearLayout)findViewById(R.id.ll_parent_editprofile);
        nameEditText = (EditText) findViewById(R.id.edit_name);
        phoneEditText = (EditText) findViewById(R.id.edit_phone);
        userEmailText = (TextView) findViewById(R.id.edit_email);
        organizationEditText = (EditText) findViewById(R.id.edit_org);
        designationEditText = (EditText) findViewById(R.id.edit_designation);
        companyEditText = (EditText) findViewById(R.id.edit_compny_url);
        aboutEditText = (EditText) findViewById(R.id.about_details_text);
        instantRadioLayout = (LinearLayout) findViewById(R.id.instant_radio_layout);
        dailyRadioLayout = (LinearLayout) findViewById(R.id.daily_radio_layout);
        instantRadioIv = (ImageView) findViewById(R.id.radio_instant);
        dailyRadioIv = (ImageView) findViewById(R.id.radio_daily);
        instantRadioLayout.setOnClickListener(this);
        dailyRadioLayout.setOnClickListener(this);
        //editProfileTextView.setOnClickListener(this);
        try {
            addItemsOnTypeSpinner();
            loadCityFromAssets();
            addListenerOnSpinnerItemSelection();
            Bundle iBundle = getIntent().getExtras();
            if(iBundle != null){
                iProfileModel = ((UserProfileModel) iBundle.getSerializable(Constant.KEY_PROFILE_MODEL)).getProfile();
                if (iProfileModel != null) {
                    setEditValue(nameEditText, iProfileModel.getRecname());
                    setEditValue(phoneEditText, iProfileModel.getPhone());
                    setEditValue(organizationEditText, iProfileModel.getOrganisation());
                    setEditValue(designationEditText, iProfileModel.getDesignation());
                    setEditValue(companyEditText, iProfileModel.getCompany_url());
                    setEditValue(aboutEditText, iProfileModel.getAbout().trim());
                    userEmailText.setText(iProfileModel.getEmail());
                    userEmailText.setVisibility(View.VISIBLE);
                    citySpinnerTitle.setText(iProfileModel.getLocation_name());
                    typeSpinnerTitle.setText(iProfileModel.getType_name());
                    if (iProfileModel.getType_id().equals("2"))
                        sSpinner.setSelection(0);
                    else if (iProfileModel.getType_id().equals("1"))
                        sSpinner.setSelection(1);
                    // Show preSelection city in dialog
                    locationId = getLocationId(iProfileModel.getLocation_name());
                    preSelectionIndex = Utility.getIndexInArray(iProfileModel.getLocation_name(), cityListArray);
                    applyEmailStatus = iProfileModel.getNotification_type();
                    if (iProfileModel.getNotification_type().equals("1")
                            && iProfileModel.getNotification_type_text().equals("Instant")) {
                        instantRadioIv.setBackgroundResource(R.drawable.profile_notification_sel);
                        dailyRadioIv.setBackgroundResource(R.drawable.profile_notification_unsel);
                    } else {
                        instantRadioIv.setBackgroundResource(R.drawable.profile_notification_unsel);
                        dailyRadioIv.setBackgroundResource(R.drawable.profile_notification_sel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_edit_profile:
                if(isValide()){
                connectToEditProfile();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }


    private String getLocationId(String city){
        return (String)cityHashMap.get(city);
    }


    private void setEditValue(EditText editText, String value){
        if(value != null && !value.equals("")){
            String strValue = value.trim();
            editText.setText(strValue);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.save_profile_cmd:
                //startActivity(new Intent(this, ChangePasswordActivity.class));
                connectToEditProfile();
                break;*/
            case R.id.instant_radio_layout:
                instantRadioIv.setBackgroundResource(R.drawable.profile_notification_sel);
                dailyRadioIv.setBackgroundResource(R.drawable.profile_notification_unsel);
                applyEmailStatus = "1";
                break;
            case R.id.daily_radio_layout:
                dailyRadioIv.setBackgroundResource(R.drawable.profile_notification_sel);
                instantRadioIv.setBackgroundResource(R.drawable.profile_notification_unsel);
                applyEmailStatus = "0";
                break;
            default:
                break;
        }
    }
    private void loadCityFromAssets(){
        citySpinnerTitle = (TextView) findViewById(R.id.edit_location_text);
        citySpinnerLayout = (LinearLayout) findViewById(R.id.city_spiner_layout);
        citySpinnerLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(cityListArray != null)
                    onCreateDialogSingleChoice(preSelectionIndex, getString(R.string.save_lbl) + " "+getString(R.string.Location_lbl), cityListArray);
                return false;
            }
        });

        String cJsonString = Utility.ReadFromfile("preferredlocation.txt", EditProfileActivity.this);
        try {

            if(cJsonString != null){
                Gson mGson = new GsonBuilder().create();
                iPrefLocModel = mGson.fromJson(cJsonString, PrefLocationModel.class);
                if(iPrefLocModel != null){
                    iLocModel = iPrefLocModel.getLocations();
                    if(iLocModel != null){
                        cityListArray = new String[iLocModel.length];
                        for(short s = 0; s < iLocModel.length; s++){
                            cityListArray[s] = iLocModel[s].getName();
                            cityHashMap.put(iLocModel[s].getName(), iLocModel[s].getId());
                        }
                    }
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private  void onCreateDialogSingleChoice(final int preSel, String header, final CharSequence[] array) {
        //Initialize the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialThemeDialog);
        builder.setTitle(""+header)
                .setSingleChoiceItems(array, preSel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        temp = (String) array[which];
                    }
                })
                .setPositiveButton(EditProfileActivity.this.getResources().getString(R.string.ok_lbl), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        citySpinnerTitle.setText(temp);
                        locationId = getLocationId(temp);
                        preSelectionIndex = Utility.getIndexInArray(temp, cityListArray);
                    }
                })
                .setNegativeButton(EditProfileActivity.this.getResources().getString(R.string.cancel_lbl), new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
                alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);

    }
    public void addListenerOnSpinnerItemSelection() {

        sSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tmpView = (TextView) sSpinner.getSelectedView().findViewById(R.id.spinner_item_txt);
                tmpView.setTextColor(Color.BLACK);
                String chooseOption =  sSpinner.getItemAtPosition(position).toString();
                typeSpinnerTitle.setText(chooseOption);
                sSpinner.setVisibility(View.GONE);
                if(chooseOption.contains(getResources().getString(R.string.direct_emp_lbl)))
                {
                    typeOption ="1";
                }
                else
                {
                    typeOption ="2";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private void addItemsOnTypeSpinner() {
        typeSpinnerTitle = (TextView) findViewById(R.id.edit_type_text);
        typeSpinnerLayout = (LinearLayout) findViewById(R.id.type_spiner_layout);
        typeSpinnerLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sSpinner.setVisibility(View.VISIBLE);
                sSpinner.performClick();
                return false;
            }
        });

        sSpinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add(getResources().getString(R.string.rec_firm_lbl));
        list.add(getResources().getString(R.string.direct_emp_lbl));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        sSpinner.setAdapter(dataAdapter);
    }
    private boolean isValide(){
        if(nameEditText.getText().toString().length() <= 0 || nameEditText.getText().toString().trim().equals("")){
            Snackbar snackbar = Snackbar.make(llParent, getApplicationContext().getString(R.string.msg_enter_name), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }else if(phoneEditText.getText().toString().length() != 10 || phoneEditText.getText().toString().trim().equals("")){
            Snackbar snackbar = Snackbar.make(llParent, getApplicationContext().getString(R.string.msg_enter_valid_number), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }else if(organizationEditText.getText().toString().length() <= 0 || organizationEditText.getText().toString().trim().equals("")){
            Snackbar snackbar = Snackbar.make(llParent, getApplicationContext().getString(R.string.msg_enter_organization), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }else if(designationEditText.getText().toString().length() <= 0 || designationEditText.getText().toString().trim().equals("")){
            Snackbar snackbar = Snackbar.make(llParent, getApplicationContext().getString(R.string.msg_enter_designation), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        else if(citySpinnerTitle.getText().toString().length() <= 0 || citySpinnerTitle.getText().toString().trim().equals("")){
            Snackbar snackbar = Snackbar.make(llParent, getApplicationContext().getString(R.string.msg_enter_location), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        return true;
    }
    private void connectToEditProfile(){

        if (Utility.isNetworkAvailableLinear(getApplicationContext(), llParent)){
            String url =  Constant.EDIT_PROFILE_URL +Constant.USER_COOKIE;
            Utility.showLoadingDialog(EditProfileActivity.this);
            RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utility.dismisLoadingDialog();
                            if(response != null) {
                                Gson mGson = new GsonBuilder().create();
                                EditProfileModel iModel = mGson.fromJson(response, EditProfileModel.class);
                                if (iModel != null) {
                                    if(iModel.getStatus().equalsIgnoreCase(Constant.HTTP_OK)){
                                        Constant.isRefreshUserProfile = true;
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
                                if (error instanceof NoConnectionError || error instanceof NetworkError) {
                                    titleMessage = getString(R.string.network_lbl);
                                    messageText = getResources().getString(R.string.msg_no_network);
                                } else if (error instanceof TimeoutError) {
                                    titleMessage = getString(R.string.retry_lbl);
                                    messageText = getResources().getString(R.string.msg_connection_timout);
                                } else {
                                    titleMessage = getString(R.string.error_lbl);
                                    messageText = getResources().getString(R.string.msg_server_error);
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                                builder.setTitle(titleMessage).setMessage(messageText)
                                        .setCancelable(true)
                                        .setPositiveButton(getString(R.string.ok_lbl), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                                if (error instanceof TimeoutError)
                                                    connectToEditProfile();
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
                    params.put("recname", nameEditText.getText().toString().trim());
                    params.put("phone", phoneEditText.getText().toString().trim());
                    params.put("organisation", organizationEditText.getText().toString().trim());
                    params.put("wurl", companyEditText.getText().toString().trim());
                    params.put("designation", designationEditText.getText().toString().trim());
                    params.put("company_status", typeOption);
                    params.put("about", aboutEditText.getText().toString().trim());
                    params.put("apply_email_status", applyEmailStatus);
                    params.put("location", locationId);
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
                    headers.put("AUTHKEY", ""+MySharedPreference.getKeyValue(Constant.KEY_USER_AUTH_KEY));
                    return headers;
                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(Constant.SOCKET_TIMEOUT_DURATION, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);
        }
    }
}
