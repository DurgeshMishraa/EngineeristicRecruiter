package com.engineeristic.recruiter.myapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.engineeristic.recruiter.chat.ChatActivity;
import com.engineeristic.recruiter.chat.ChatConstants;
import com.engineeristic.recruiter.model.CoverLatterModel;
import com.engineeristic.recruiter.model.PostCommentModel;
import com.engineeristic.recruiter.util.AccountUtils;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.ConstantFontelloID;
import com.engineeristic.recruiter.util.DateFormat;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileResumeActivity extends AppCompatActivity implements View.OnClickListener {

    //public static boolean isActionChanged = false;
    public static boolean isPostCommented = false;
    private Activity temp = this;
    private String cookie = "";
    static String viewid = "";
    private String appStatus = "";
    private String followup = "";
    private String aStatus = "";
    private TextView heading_Work;
    private TextView heading_Education;
    private TextView heading_Moreinfo;
    private ProgressBar mFooterProgress;
    public static JSONObject jOProfileInfo;
    private String published_id = "";
    TextView expImageView, locationImageView, txtViewLocation;
    //tvGenderAge
    private TextView   profileName, exp, appliedDate,currentLocation,
            preferredLocation, currentSalary, expectedSalary , eMail , contactNumber,noticePeriod,
            tvCompanyName,tvCompanyDegi,tvCompanyExp;
    private TextView firstNameImage;
    private LinearLayout llWorkExp , llWorkExptext;
    private LinearLayout llEduQualification , llEduQualificationtext;
    private View saperatorView ,saperatorViewEdu , saperatorProfileAge;
    private  LayoutInflater mInflater = null;
    private JSONArray educationArray, professionDetails;
    public ProgressDialog dialog = null;
    private ImageView  ivSeerkar;//iv_comment
    private ProgressDialog pDialog;
    private String eFieldStr = null;
    private String jobtitle = null;
    //private Tracker mTracker;
    private com.nostra13.universalimageloader.core.ImageLoader loader;
    private DisplayImageOptions options;
    // Durgesh
    private LinearLayout ll_shortlist,ll_reject, ll_button_holder;
    private RelativeLayout rl_resume, rl_cover_letter, rl_contact_information;
    private String mStrEmail, mStrContactNumber, strCurrentLocation;
    private TextView  mContactInformationIcon,mResumeIcon, mCoverLetterIcon,
            mTxtShortlistIcon, mTxtRejectIcon;//mSaveMoreIcon
    private Typeface fontello;
    private Menu menu;
    private TextView txt_shortlist, txt_Reject;
    private RelativeLayout llParent;
    private ScrollView sv_candidatesdetails;
    private String strUserId ;
    private String strJobId ;
    private float mTouchPosition;
    private float mReleasePosition;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.candidate_details_activity);
        fontello = Typeface.createFromAsset(getApplicationContext().getAssets(),"fontello.ttf");
        RecruiterApplication application = (RecruiterApplication) getApplication();
        //mTracker = application.getDefaultTracker();
        AccountUtils.trackerScreen("Candidate Profile");
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_candidatedetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation((float)10.0);
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.toolbar_backicon_clr), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        loaderImage();
        jobtitle = getIntent().getExtras().getString(Constant.KEY_JOB_TITLE);
        if(jobtitle != null && !jobtitle.equals(""))
            getSupportActionBar().setTitle("");
        //isActionChanged = false;
        isPostCommented = false;
        Constant.PROFILE_RESUME_TYPE = getIntent().getByteExtra(Constant.KEY_SCREEN_TYPE, (byte)-1);
        DateFormat cs = new DateFormat();
        mInflater = (LayoutInflater)getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        try
        {
            jOProfileInfo = new JSONObject(getIntent().getStringExtra(Constant.KEY_OBJECT));
            Constant.candidatePos = getIntent().getIntExtra(Constant.KEY_POSITION,0);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        try {
            strUserId = jOProfileInfo.optJSONObject("applicationdetails").getString("userid");
            strJobId = jOProfileInfo.optJSONObject("applicationdetails").getString("jobid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        viewid = getIntent().getStringExtra(Constant.KEY_VIEW_ID);
        sv_candidatesdetails = (ScrollView) findViewById(R.id.sv_candidatesdetails);
        llParent = (RelativeLayout)findViewById(R.id.ll_parent_profileresume);
        ivSeerkar = (ImageView) findViewById(R.id.seekar_iv);
        firstNameImage = (TextView)findViewById(R.id.iv_profilename);
        profileName = (TextView)findViewById(R.id.tv_profilename);
        exp = (TextView)findViewById(R.id.tv_exp);
        noticePeriod = (TextView)findViewById(R.id.tv_notice);
        appliedDate = (TextView)findViewById(R.id.tv_date);
        heading_Work = (TextView) findViewById(R.id.wrkExperience);
        heading_Education = (TextView) findViewById(R.id.eduQualification);
        heading_Moreinfo = (TextView) findViewById(R.id.tv_more_info);
        currentLocation = (TextView)findViewById(R.id.current_location);
        preferredLocation = (TextView)findViewById(R.id.preferred_location);
        currentSalary = (TextView)findViewById(R.id.current_salary);
        expectedSalary = (TextView)findViewById(R.id.expected_salary);
        eMail = (TextView)findViewById(R.id.email_id);
        contactNumber = (TextView)findViewById(R.id.contact_num);
        llWorkExptext = (LinearLayout)findViewById(R.id.ll_work_exp_text);
        llEduQualificationtext  = (LinearLayout)findViewById(R.id.ll_edu_exp_text);
        llWorkExp = (LinearLayout)findViewById(R.id.ll_work_exp);
        llEduQualification = (LinearLayout)findViewById(R.id.ll_edu_exp);
        txt_shortlist = (TextView)findViewById(R.id.txt_shortlist);
        txt_Reject = (TextView)findViewById(R.id.txt_Reject);
        //iv_comment = (ImageView)findViewById(R.id.iv_comment);
        ll_button_holder = (LinearLayout)findViewById(R.id.ll_button_holder);
        ll_shortlist = (LinearLayout)findViewById(R.id.ll_shortlist);
        ll_reject = (LinearLayout)findViewById(R.id.ll_reject);
        View viewSeprator= (View)findViewById(R.id.seprtr_resume);
        rl_cover_letter = (RelativeLayout)findViewById(R.id.rl_cover_letter);
        try {
            if(jOProfileInfo.optJSONObject("applicationdetails").getString("coverid").equalsIgnoreCase("0")){
                rl_cover_letter.setVisibility(View.GONE);
                viewSeprator.setVisibility(View.GONE);
            }
            else{
                rl_cover_letter.setVisibility(View.VISIBLE);
                viewSeprator.setVisibility(View.VISIBLE);
            }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        txtViewLocation = (TextView) findViewById(R.id.tv_jobLocation_candidate_details);
        try {
            strCurrentLocation = jOProfileInfo.optJSONObject("applicationdetails").getString("current_location");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        txtViewLocation.setText(strCurrentLocation);
        expImageView = (TextView) findViewById(R.id.imageExperience_candidate_detail);
        locationImageView = (TextView) findViewById(R.id.txtlocationicon_candidate_details);
        expImageView.setTypeface(AccountUtils.fontelloTtfIconsFont());
        locationImageView.setTypeface(AccountUtils.fontelloTtfIconsFont());
        expImageView.setText(ConstantFontelloID.ICON_JOB_EXP);
        locationImageView.setText(ConstantFontelloID.ICON_JOB_LOCATION);
        rl_resume = (RelativeLayout)findViewById(R.id.rl_resume);
        rl_contact_information = (RelativeLayout)findViewById(R.id.rl_contact_information);
        rl_cover_letter.setOnClickListener(this);
        rl_resume.setOnClickListener(this);
        ll_shortlist.setOnClickListener(this);
        ll_reject.setOnClickListener(this);
        rl_contact_information.setOnClickListener(this);

        mTxtShortlistIcon = (TextView)findViewById(R.id.txt_shortlist_icon);
        mTxtRejectIcon = (TextView)findViewById(R.id.txt_reject_icon);
        mCoverLetterIcon = (TextView)findViewById(R.id.cover_letter_icon);
        //mSaveMoreIcon = (TextView)findViewById(R.id.see_more_icon);
        mContactInformationIcon = (TextView)findViewById(R.id.contact_information_icon);
        mResumeIcon = (TextView)findViewById(R.id.resume_icon);
        mCoverLetterIcon.setTypeface(fontello);
        //mSaveMoreIcon.setTypeface(fontello);
        mContactInformationIcon.setTypeface(fontello);
        mResumeIcon.setTypeface(fontello);
        mCoverLetterIcon.setText(ConstantFontelloID.icon_expand_right);
        //mSaveMoreIcon.setText(ConstantFontelloID.icon_expand_right);
        mContactInformationIcon.setText(ConstantFontelloID.icon_expand_right);
        mResumeIcon.setText(ConstantFontelloID.icon_expand_right);
        mTxtShortlistIcon.setTypeface(fontello);
        mTxtRejectIcon.setTypeface(fontello);
        mTxtShortlistIcon.setText(ConstantFontelloID.icon_shortlist);
        mTxtRejectIcon.setText(ConstantFontelloID.icon_reject);

        try
        {
            final String imgUrl = jOProfileInfo.optJSONObject("applicationdetails").optString("ProfilePic").trim();
            if(imgUrl == null || imgUrl.equals("")) {
                String nameFirstLetter = jOProfileInfo.optJSONObject("applicationdetails").optString("name").toUpperCase();

                firstNameImage.setText(nameFirstLetter.charAt(0) + "");
                firstNameImage.setTextColor(Color.WHITE);
                firstNameImage.setTextSize(18.0f);
                firstNameImage.setBackgroundResource(R.drawable.textdesign);

                GradientDrawable drawable = (GradientDrawable) firstNameImage.getBackground();
                drawable.setColor(Color.parseColor(jOProfileInfo.optJSONObject("applicationdetails").optString("color")));
                firstNameImage.setVisibility(View.VISIBLE);
                ivSeerkar.setVisibility(View.GONE);
            }else{
                firstNameImage.setVisibility(View.GONE);
                ivSeerkar.setVisibility(View.VISIBLE);
                loader.displayImage(jOProfileInfo.optJSONObject("applicationdetails").optString("ProfilePic").trim(), ivSeerkar, options);
            }

            String expYear  =  jOProfileInfo.getJSONObject("applicationdetails").getString("exp_year");
            String expMonth =  jOProfileInfo.getJSONObject("applicationdetails").getString("exp_month");
            int notice = Integer.parseInt(jOProfileInfo.getJSONObject("applicationdetails").optString("notice_period"));
            String ctc =  (jOProfileInfo.getJSONObject("applicationdetails").optString("current_ctc"));
            String expSalary =  (jOProfileInfo.getJSONObject("applicationdetails").optString("expected_ctc"));
            String gender =  (jOProfileInfo.getJSONObject("applicationdetails").optString("gender"));
            profileName.setText(jOProfileInfo.getJSONObject("applicationdetails").getString("name"));
            exp.setText(expYear + "yr " + expMonth + "m");
            saperatorProfileAge = (View)findViewById(R.id.saperator_profile_age);
            if (notice != 7)
            {
                noticePeriod.setText(notice + " Month");
            }
            else
            {
                noticePeriod.setText("Immediately ");
            }
            String strFinalDate;
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            String strDate = jOProfileInfo.getJSONObject("applicationdetails").optString("applydate");
            String[] strSplitDate = strDate.split("-");
            String strYear = strSplitDate[2];
            if(strYear.equalsIgnoreCase(""+currentYear) || strYear == ""+currentYear){
                strFinalDate = strSplitDate[0]+"/"+strSplitDate[1];
            }
            else{
                strFinalDate = strDate.replace("-","/");
            }
            appliedDate.setText(strFinalDate);
            currentLocation.setText(jOProfileInfo.getJSONObject("applicationdetails").optString("current_location"));
            preferredLocation.setMovementMethod(LinkMovementMethod.getInstance());
            preferredLocation.setText(jOProfileInfo.getJSONObject("applicationdetails").optString("preferred_location"), TextView.BufferType.SPANNABLE);
            eMail.setText(jOProfileInfo.getJSONObject("applicationdetails").optString("email") + " ");
            contactNumber.setText(jOProfileInfo.getJSONObject("applicationdetails").getString("phone") + " ");

            if (!ctc.equalsIgnoreCase("Confidential"))
            {
                currentSalary.setText(ctc + " Lacs");
            }
            else
            {
                currentSalary.setText(ctc);
            }
            if (!expSalary.equalsIgnoreCase("Confidential"))
            {
                expectedSalary.setText(expSalary + " Lacs");
            }
            else
            {
                expectedSalary.setText(expSalary);
            }

            educationArray    = jOProfileInfo.getJSONArray("educationdetails");
            professionDetails = jOProfileInfo.getJSONArray("professiondetails");

            if(educationArray !=null && educationArray.length()>0 )
            {
                llEduQualificationtext.setVisibility(View.VISIBLE);
                for (int i = 0; i < educationArray.length(); i++)
                {
                    View eduView = mInflater.inflate(R.layout.details_edu_layout, null);
                    //eduImage = (ImageView) eduView.findViewById(R.id.iv_eduImage);
                    tvCompanyName = (TextView)  eduView.findViewById(R.id.tv_institute);
                    tvCompanyDegi = (TextView)  eduView.findViewById(R.id.tv_qualifi);
                    tvCompanyExp = (TextView)  eduView.findViewById(R.id.tv_duration);
                    saperatorViewEdu = (View)eduView.findViewById(R.id.saperator_view_edu);
                    if(i==educationArray.length()-1)
                    {
                        saperatorViewEdu.setVisibility(View.GONE);
                    }
                    else
                    {
                        saperatorViewEdu.setVisibility(View.VISIBLE);
                    }

                    String instiName = educationArray.getJSONObject(i).getString("institute");
                    String qualification = educationArray.getJSONObject(i).getString("degree")+" ("+educationArray.getJSONObject(i).getString("course_type")+")";
                    String yearEdu = educationArray.getJSONObject(i).getString("batch_from") + " to " + educationArray.getJSONObject(i).getString("batch_to");
                    tvCompanyName.setText(instiName);
                    tvCompanyDegi.setText(qualification);
                    tvCompanyExp.setText(yearEdu);
                    llEduQualification.addView(eduView);
                }
            }
            else
            {
                llEduQualificationtext.setVisibility(View.GONE);

            }

            if(professionDetails !=null && professionDetails.length()>0 )
            {
                saperatorProfileAge.setVisibility(View.VISIBLE);
                llWorkExptext.setVisibility(View.VISIBLE);
                for (int i = 0; i < professionDetails.length(); i++)
                {
                    View view = mInflater.inflate(R.layout.details_work_layout, null);
                    tvCompanyName = (TextView) view.findViewById(R.id.tv_company);
                    tvCompanyDegi = (TextView) view.findViewById(R.id.tv_design);
                    tvCompanyExp = (TextView) view.findViewById(R.id.tv_exptime);
                    saperatorView = (View)view.findViewById(R.id.saperator_view);
                    if(i==professionDetails.length()-1)
                    {
                        saperatorView.setVisibility(View.GONE);
                    }
                    else
                    {
                        saperatorView.setVisibility(View.VISIBLE);
                    }

                    String companyName = professionDetails.getJSONObject(i).getString("organization");
                    String companyDesignation = professionDetails.getJSONObject(i).getString("designation");
                    String mothFrom  = professionDetails.getJSONObject(i).getString("from_exp_month");
                    String monthTo = professionDetails.getJSONObject(i).getString("to_exp_month");
                    String yearFrom = professionDetails.getJSONObject(i).getString("from_exp_year");
                    String yearTo = professionDetails.getJSONObject(i).getString("to_exp_year");
                    mothFrom = cs.C_month(mothFrom);
                    monthTo = cs.C_month(monthTo);
                    String details = null;
                    String isCurrent = professionDetails.getJSONObject(i).getString("is_current");

                    if (isCurrent.equalsIgnoreCase("1"))
                    {
                        details = mothFrom + "" + yearFrom + " to " + " Present";
                    }
                    else if (isCurrent.equalsIgnoreCase("0"))
                    {
                        details = mothFrom + "" + yearFrom + " to " + monthTo + "" + yearTo;
                    }

                    tvCompanyName.setText(companyName);
                    tvCompanyDegi.setText(companyDesignation);
                    tvCompanyExp.setText(details);
                    llWorkExp.addView(view);
                }
            }
            else
            {
                llWorkExptext.setVisibility(View.GONE);
                saperatorProfileAge.setVisibility(View.GONE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            appStatus = jOProfileInfo.optJSONObject("applicationdetails").getString("app_status");
            //rejected
            if(appStatus.equalsIgnoreCase("2")){
                ll_reject.setBackgroundColor(Color.parseColor("#E53935"));
                mTxtRejectIcon.setTextColor(Color.parseColor("#ffffff"));
                txt_Reject.setTextColor(Color.parseColor("#ffffff"));
                txt_Reject.setText("Rejected");

                ll_shortlist.setBackgroundColor(Color.parseColor("#f8f8f8"));
                mTxtShortlistIcon.setTextColor(Color.parseColor("#757575"));
                txt_shortlist.setTextColor(Color.parseColor("#757575"));
                txt_shortlist.setText("Shortlist");
            }
            // shortlisted
            if(appStatus.equalsIgnoreCase("1")){
                ll_reject.setBackgroundColor(Color.parseColor("#f8f8f8"));
                mTxtRejectIcon.setTextColor(Color.parseColor("#757575"));
                txt_Reject.setTextColor(Color.parseColor("#757575"));
                txt_Reject.setText("Reject");

                ll_shortlist.setBackgroundColor(Color.parseColor("#16a085"));
                mTxtShortlistIcon.setTextColor(Color.parseColor("#ffffff"));
                txt_shortlist.setTextColor(Color.parseColor("#ffffff"));
                txt_shortlist.setText("Shortlisted");
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        loadResumeDetails();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        String strComment = "";
        getMenuInflater().inflate(R.menu.menu_profile_resume, menu);
        try {
            strComment = jOProfileInfo.optJSONObject("applicationdetails").getString("comment").toString().trim();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(strComment.equalsIgnoreCase("") || strComment.equalsIgnoreCase(null)){
            menu.getItem(2).setTitle("Add Comment");
        }
        else{
            menu.getItem(2).setTitle("Edit Comment");
        }

        try
        {
            String strAppStatus = jOProfileInfo.optJSONObject("applicationdetails").getString("app_status");
            if(strAppStatus.equalsIgnoreCase("3")){
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.saved));
            }
            else{
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.save));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;

            case R.id.action_profile_resume_chat:
                AccountUtils.trackEvents("ProfileResumeActivity", "rtStartChat",
                        "Origin= "+"CandidateProfile"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                try {
                    if(jOProfileInfo != null){
                        Intent myIntent = new Intent(ProfileResumeActivity.this, ChatActivity.class);
                        myIntent.putExtra(ChatConstants.JOBID_FOR_CHAT, jOProfileInfo.optJSONObject("applicationdetails").getString("userid"));
                        startActivity(myIntent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.action_profile_resume_save:
                try{
                    aStatus = jOProfileInfo.optJSONObject("applicationdetails").getString("app_status");
                    followup= jOProfileInfo.optJSONObject("applicationdetails").getString("followup");
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(aStatus.equalsIgnoreCase("3")){
                    AccountUtils.trackEvents("ProfileResumeActivity", "rtMarkasunreadCandidate",
                            "Origin= "+"CandidateProfile"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                    aStatus ="0";
                    menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.save));
                    int nUnread =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE, nUnread);
                }
                else{


                    AccountUtils.trackEvents("ProfileResumeActivity", "rtSaveCandidate",
                            "Origin= "+"CandidateProfile"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);

                    aStatus = "3";
                    menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.saved));

                    ll_shortlist.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    mTxtShortlistIcon.setTextColor(Color.parseColor("#757575"));
                    txt_shortlist.setTextColor(Color.parseColor("#757575"));
                    txt_shortlist.setText("Shortlist");
                    ll_reject.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    mTxtRejectIcon.setTextColor(Color.parseColor("#757575"));
                    txt_Reject.setTextColor(Color.parseColor("#757575"));
                    txt_Reject.setText("Reject");

                    int nSaved =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SAVE_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SAVE_TYPE, nSaved);
                }
                recruiterAction();
                Constant.JUMP_RESUME_ACTIVITY = "profile_resume_activity";
                break;

            case R.id.action_profile_resume_comment:
                viewComment();
                break;
            case R.id.action_profile_resume_contactinfo:
                try {
                    mStrEmail = eMail.getText().toString().trim();
                    mStrContactNumber = contactNumber.getText().toString().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.setClass(this, ContactInformationActivity.class);
                intent.putExtra("EMAIL", mStrEmail);
                intent.putExtra("CONTACT_NUMBER", mStrContactNumber);
                intent.putExtra("JOB_TITLE", jobtitle);
                intent.putExtra("ORIGIN_CONTACT_INFORMATION", "CandidateProfileEllipses");
                try {
                    intent.putExtra("USER_ID", jOProfileInfo.optJSONObject("applicationdetails").getString("userid"));
                    intent.putExtra("JOB_ID", jOProfileInfo.optJSONObject("applicationdetails").getString("jobid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
                break;
            default:
                break;
        }
        //return super.onOptionsItemSelected(item);
        return true;
    }
    private void loaderImage(){
        try {

            options = new DisplayImageOptions.Builder()
                    .displayer(new RoundedBitmapDisplayer(90))
                    .showImageOnLoading(R.drawable.img)
                    .showImageForEmptyUri(R.drawable.img)
                    .showImageOnFail(R.drawable.img).cacheInMemory(true)
                    .cacheOnDisc(true)
                    .build();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    this).defaultDisplayImageOptions(options).build();

            loader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
            loader.init(config);
        } catch (Exception e) {
            Log.e("ChatActivity ", "loaderImage() "+e.getMessage());
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void recruiterAction(){
        if (Utility.isNetworkAvailableRelative(ProfileResumeActivity.this, llParent)){
        try {
            jOProfileInfo.optJSONObject("applicationdetails").put("app_status", aStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        changeRecruiterAction();
        }
    }
    private void changeRecruiterAction(){
        if (Utility.isNetworkAvailableRelative(ProfileResumeActivity.this, llParent)){
            String url =  Constant.REC_ACTION_URL + getIntent().getExtras().getString(Constant.KEY_VIEW_ID)+"/" + aStatus +"/" +followup+"/"+Constant.USER_COOKIE;
            RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response != null) {
                        Gson mGson = new GsonBuilder().create();
                        // CoverLatter Model is using for recruiter Action changed
                        CoverLatterModel iModel = mGson.fromJson(response, CoverLatterModel.class);
                        if (iModel != null) {
                            if(iModel.getStatus().equalsIgnoreCase(Constant.HTTP_OK)){
                                // Not Required
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
                        }
                    }){
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

    public void viewComment()
    {
        if (Utility.isNetworkAvailableRelative(ProfileResumeActivity.this, llParent))
        {
            String strComment = "";
            try {
                strComment = jOProfileInfo.optJSONObject("applicationdetails").getString("comment").toString().trim();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final AlertDialog alertD  = new AlertDialog.Builder(ProfileResumeActivity.this).create();
            LayoutInflater layoutInflater = LayoutInflater.from(ProfileResumeActivity.this);
            View promptView = layoutInflater.inflate(R.layout.prompt, null);
            if(strComment.equalsIgnoreCase("")){
                alertD.setTitle("Post Comment");
            }else {
                alertD.setTitle("Update Comment");
            }
            final EditText userInput = (EditText) promptView.findViewById(R.id.userEmailId);
            userInput.setHeight(100);
            userInput.setMaxLines(5);
            userInput.setHint("Type comment");
            userInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    eFieldStr = arg0.toString();
                }
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {

                }
                @Override
                public void afterTextChanged(Editable arg0) {
                }
            });
            try {
                userInput.setText(jOProfileInfo.optJSONObject("applicationdetails").getString("comment").toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Button cancel = (Button) promptView.findViewById(R.id.cancel);
            Button submit = (Button) promptView.findViewById(R.id.submit);
            cancel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    hideKeyBoard();
                    alertD.dismiss();
                }
            });
            submit.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    AccountUtils.trackEvents("ProfileResumeActivity", "rtAddComment",
                            "Origin= "+"CandidateProfile"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                    hideKeyBoard();
                    if(eFieldStr.trim().length()>0)
                    {
                        loadComments(eFieldStr.trim());
                        alertD.dismiss();
                    }
                }
            });
            alertD.setView(promptView);
            alertD.show();
        }
    }

    private void loadComments(final String mComments){
        if (Utility.isNetworkAvailableRelative(getApplicationContext(), llParent)
                && jOProfileInfo != null){
            Utility.showLoadingDialog(ProfileResumeActivity.this);
            String url = null;
            try{
                url = Constant.ADD_COMMENT_URL +jOProfileInfo.getJSONObject("applicationdetails").getString("id").trim()+"/"+ Constant.USER_COOKIE;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Utility.dismisLoadingDialog();
                            if(response != null){
                                Gson mGson = new GsonBuilder().create();
                                PostCommentModel iModel = mGson.fromJson(response, PostCommentModel.class);
                                if (iModel != null) {
                                    if(iModel.getStatus().equalsIgnoreCase(Constant.HTTP_OK)) {
                                        isPostCommented = true;
                                        try{
                                            jOProfileInfo.optJSONObject("applicationdetails").put("comment", eFieldStr);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        //iv_comment.setImageResource(R.drawable.comment_hover);

                                        Snackbar snackbar = Snackbar.make(llParent, getString(R.string.comment_submited_succes_message), Snackbar.LENGTH_SHORT);
                                        snackbar.show();
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
                        public void onErrorResponse(VolleyError error) {
                            Utility.dismisLoadingDialog();
                            showAlertDialog(error, Constant.POST_COMMENT_WEBSERVICE);
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("comment", mComments);
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
    private void showAlertDialog(final VolleyError error, final byte  nType){
        String titleMessage = "";
        String messageText = "";
        if (error instanceof NoConnectionError
                || error instanceof NetworkError) {
            titleMessage = getString(R.string.network_lbl);
            messageText = this.getResources().getString(R.string.msg_no_network);
        } else if (error instanceof TimeoutError) {
            titleMessage = getString(R.string.retry_lbl);
            messageText = this.getResources().getString(R.string.msg_connection_timout);
        } else {
            titleMessage = getString(R.string.error_lbl);
            messageText = this.getResources().getString(R.string.msg_server_error);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleMessage).setMessage(messageText)
                .setCancelable(true)
                .setPositiveButton(this.getString(R.string.ok_lbl), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        if (error instanceof TimeoutError && nType == Constant.POST_COMMENT_WEBSERVICE)
                            loadComments(eFieldStr);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);

    }
    private void hideKeyBoard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch(viewId){
            case R.id.ll_shortlist:
                try
                {
                    aStatus = jOProfileInfo.optJSONObject("applicationdetails").getString("app_status");
                    followup= jOProfileInfo.optJSONObject("applicationdetails").getString("followup");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                if(aStatus.equalsIgnoreCase("1")){
                    AccountUtils.trackEvents("ProfileResumeActivity", "rtMarkasunreadCandidate",
                            "Origin= "+"CandidateProfile"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                    aStatus = "0";
                    ll_shortlist.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    mTxtShortlistIcon.setTextColor(Color.parseColor("#757575"));
                    txt_shortlist.setTextColor(Color.parseColor("#757575"));
                    txt_shortlist.setText("Shortlist");
                    int nUnread =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE, nUnread);
                }
                else{

                    AccountUtils.trackEvents("ProfileResumeActivity", "rtShortlistCandidate",
                            "Origin= "+"CandidateProfile"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);

                    aStatus = "1";
                    ll_shortlist.setBackgroundColor(Color.parseColor("#16a085"));
                    mTxtShortlistIcon.setTextColor(Color.parseColor("#ffffff"));
                    txt_shortlist.setTextColor(Color.parseColor("#ffffff"));
                    txt_shortlist.setText("Shortlisted");

                    ll_reject.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    mTxtRejectIcon.setTextColor(Color.parseColor("#757575"));
                    txt_Reject.setTextColor(Color.parseColor("#757575"));
                    txt_Reject.setText("Reject");
                    menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.save));

                    int nShortlisted =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE, nShortlisted);
                }
                recruiterAction();
                Constant.JUMP_RESUME_ACTIVITY = "profile_resume_activity";
                break;
            case R.id.ll_reject:
                try
                {
                    aStatus = jOProfileInfo.optJSONObject("applicationdetails").getString("app_status");
                    followup= jOProfileInfo.optJSONObject("applicationdetails").getString("followup");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                if(aStatus.equalsIgnoreCase("2")){
                    AccountUtils.trackEvents("ProfileResumeActivity", "rtMarkasunreadCandidate",
                            "Origin= "+"CandidateProfile"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                    aStatus = "0";
                    ll_reject.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    mTxtRejectIcon.setTextColor(Color.parseColor("#757575"));
                    txt_Reject.setTextColor(Color.parseColor("#757575"));
                    txt_Reject.setText("Reject");
                    int nUnread =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE, nUnread);
                }
                else{

                    AccountUtils.trackEvents("ProfileResumeActivity", "rtRejectCandidate",
                            "Origin= "+"CandidateProfile"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                    aStatus = "2";
                    ll_reject.setBackgroundColor(Color.parseColor("#E53935"));
                    mTxtRejectIcon.setTextColor(Color.parseColor("#ffffff"));
                    txt_Reject.setTextColor(Color.parseColor("#ffffff"));
                    txt_Reject.setText("Rejected");

                    ll_shortlist.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    mTxtShortlistIcon.setTextColor(Color.parseColor("#757575"));
                    txt_shortlist.setTextColor(Color.parseColor("#757575"));
                    txt_shortlist.setText("Shortlist");
                    menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.save));
                    int nRejected =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_REJECT_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_REJECT_TYPE, nRejected);
                }
                recruiterAction();
                Constant.JUMP_RESUME_ACTIVITY = "profile_resume_activity";
                break;

            case R.id.rl_resume:
                if( jOProfileInfo != null){
                if (Utility.isNetworkAvailableRelative(ProfileResumeActivity.this, llParent))
                {
                    AccountUtils.trackEvents("ProfileResumeActivity", "rtViewResume",
                            "Origin= "+"CandidateProfile"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                    try
                    {
                        followup= jOProfileInfo.optJSONObject("applicationdetails").getString("followup");
                        String userResume = jOProfileInfo.optJSONObject("applicationdetails").getString("name").toString();
                        String url =  Constant.RESUME_VIEW_URL
                                +"/"+jOProfileInfo.getJSONObject("applicationdetails").getString("id").trim()
                                +"/"+followup
                                +"/"+ Constant.USER_COOKIE;
                        Intent intentDownloadResume = new Intent(this, DownloadResumeActivity.class);
                        intentDownloadResume.putExtra("DOWNLOAD_URL", url);
                        intentDownloadResume.putExtra("CANDIDATE_NAME", userResume);
                        startActivity(intentDownloadResume);
                        //new ResumeDownloadTask(ProfileResumeActivity.this, jOProfileInfo.getJSONObject("applicationdetails").getString("name")).execute(url,jOProfileInfo.getJSONObject("applicationdetails").getString("name")+".pdf");
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                }
                break;
            case R.id.rl_cover_letter:
                try {
                    if(jOProfileInfo.optJSONObject("applicationdetails").getString("coverid").equalsIgnoreCase("0"))
                    {
                        Snackbar snackbar = Snackbar.make(llParent, "CoverLetter not found", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                    else
                    {
                        AccountUtils.trackEvents("ProfileResumeActivity", "rtViewCoverLetter",
                                "Origin= "+"CandidateProfile"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                        try {
                            Intent ic = new Intent(ProfileResumeActivity.this, CoverLetterActivity.class);
                            ic.putExtra(Constant.KEY_VIEW_ID, jOProfileInfo.getJSONObject("applicationdetails").getString("id").trim());
                            startActivity(ic);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                break;

            case R.id.rl_contact_information:
                try {
                    mStrEmail = eMail.getText().toString().trim();
                    mStrContactNumber = contactNumber.getText().toString().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.setClass(this, ContactInformationActivity.class);
                intent.putExtra("EMAIL", mStrEmail);
                intent.putExtra("CONTACT_NUMBER", mStrContactNumber);
                intent.putExtra("JOB_TITLE", jobtitle);
                intent.putExtra("ORIGIN_CONTACT_INFORMATION", "CandidateProfile");
                try {
                    intent.putExtra("USER_ID", jOProfileInfo.optJSONObject("applicationdetails").getString("userid"));
                    intent.putExtra("JOB_ID", jOProfileInfo.optJSONObject("applicationdetails").getString("jobid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private void loadResumeDetails(){
        if (Utility.isNetworkAvailableRelative(ProfileResumeActivity.this, llParent)){
            String url = "";
            try{
                url =  Constant.VIEW_CLICK_COUNT_URL + jOProfileInfo.getJSONObject("applicationdetails").getString("id").trim()+"/"+jOProfileInfo.getJSONObject("applicationdetails").getString("followup")+"/"+Constant.USER_COOKIE;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("status")==200)
                            {
                                if(jsonObject.optString("view_cnt").equals("0")){
                                    MySharedPreference.saveKeyValue(Constant.KEY_VIEW_MESSAGE, jsonObject.optString("view_cnt_msg"));
                                    Utility.showToastMessage(getApplicationContext(), jsonObject.optString("view_cnt_msg"), Toast.LENGTH_SHORT);
                                }
                                MySharedPreference.saveKeyValue(Constant.KEY_VIEW_ID, jsonObject.optString("view_cnt"));
                            }
                            else
                            {
                                Utility.showToastMessage(getApplicationContext(), jsonObject.optString("error_msg"), Toast.LENGTH_SHORT);
                            }
                        }catch(JSONException ex){
                            ex.printStackTrace();
                        }catch(Exception ex){
                            ex.printStackTrace();
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
