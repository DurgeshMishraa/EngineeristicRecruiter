package com.engineeristic.recruiter.myapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.engineeristic.recruiter.widget.CircularNetworkImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.adapter.MyJobsAdapter;
import com.engineeristic.recruiter.chat.ChatConstants;
import com.engineeristic.recruiter.chat.ChatManager;
import com.engineeristic.recruiter.chat.ChatPreferences;
import com.engineeristic.recruiter.chat.ChatSyncTask;
import com.engineeristic.recruiter.chat.MyChat;
import com.engineeristic.recruiter.model.JobItemsModel;
import com.engineeristic.recruiter.model.JobModel;
import com.engineeristic.recruiter.model.LoginModel;
import com.engineeristic.recruiter.pojo.UpdateAppData;
import com.engineeristic.recruiter.util.AccountUtils;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.ConstantFontelloID;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.Utility;
import com.engineeristic.recruiter.widget.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobsListActivity extends AppCompatActivity implements
        Animation.AnimationListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
   // private Tracker mTracker;
    private int RES_ID_01 = 1;
    private Animation mPopupShow;
    private Animation mPopupHide;
    //private LinearLayout mPopupLayout;
    private CardView mPopupLayout;
    private ImageView openCloseIv;
   // private Button applyButton;
    private List<String> jobSelList = new ArrayList<String>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView dataNotFoundText;
    private RecyclerView recyclerView;
    private MyJobsAdapter mJobsAdapter;
    public int currentPage = 0;
    private static int mTotalPage = 0;
    private static int mCPage = 0;
    private String url;
    private boolean isLoadMore = false;
    private ArrayList<JobItemsModel> mJobList = new ArrayList<JobItemsModel>();
    private TextView myJobIcon, myProfileIcon, myChatIcon, rateAppIcon,
            shareAppIcon, feedbackIcon, changePassIcon, logoutIcon;
    private TextView selOptiopnText, selOptiopnText02, title_textnumber;
    private Button Btn01, Btn02, Btn03, Btn04, Btn05;
    private Typeface typeRobotoMedium, typeRobotoRegular;
    private int nJobOptionSelection;
    //private LinearLayout ll_seltab;
    private CardView mCardViewView;
    private RelativeLayout rlParent;
    private final String TAG = "JobsListActivity";
    private String strSelectetCategories;

    String strLocalPending = "";
    String strLocalRejected = "";
    String strLocalUpdated = "";
    String strLocalPublished = "";
    String strLocalUnpublished = "";
    String strCategoriesCallValue;
    private Bundle bnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);
        bnd = getIntent().getExtras();

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        AccountUtils.trackerScreen("My Jobs");
        typeRobotoRegular = Typeface.createFromAsset(getAssets(),"robotoregular.ttf");
        typeRobotoMedium = Typeface.createFromAsset(getAssets(),"robotomedium.ttf");
        RecruiterApplication application = (RecruiterApplication) getApplication();
        Constant.USER_COOKIE = MySharedPreference.getKeyValue(Constant.KEY_USER_COOKIE);
        //mTracker = application.getDefaultTracker();
        toolbar = (Toolbar) findViewById(R.id.toolbar_joblist);
        //toolbar.setTitle("" + getResources().getString(R.string.my_jobs_lbl));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           toolbar.setElevation((float)10.0);
            //toolbar.setElevation((float)0.0);
        }
        setSupportActionBar(toolbar);
        Spannable text = new SpannableString(getResources().getString(R.string.my_jobs_lbl));
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_text_clr)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
        initDrawer();
        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // checkChatCount();
            }
        };
        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        Utility.setContext(this);
        ChatPreferences.getChatSharedInstance().setContextPreference(Utility.getContext());
        checkChatCount();
        rlParent = (RelativeLayout)findViewById(R.id.rl_parent_joblistactivity);
        mPopupShow = AnimationUtils.loadAnimation(this, R.anim.popup_show);
        mPopupShow.setAnimationListener(this);
        mPopupHide = AnimationUtils.loadAnimation(this, R.anim.popup_hide);
        mPopupHide.setAnimationListener(this);
        //mPopupLayout = (LinearLayout) findViewById(R.id.popup_layout);
        mPopupLayout = (CardView) findViewById(R.id.popup_layout);
        //ll_seltab = (LinearLayout) findViewById(R.id.sel_tab_layout);
        //ll_seltab.setOnClickListener(this);
        mCardViewView = (CardView) findViewById(R.id.card_views_joblist);
        mCardViewView.setOnClickListener(this);
        openCloseIv = (ImageView) findViewById(R.id.on_off_arrow);
        selOptiopnText = (TextView) findViewById(R.id.title_text);
        selOptiopnText02 = (TextView) findViewById(R.id.title_text2);
        title_textnumber = (TextView) findViewById(R.id.title_textnumber);
        //applyButton = (Button) findViewById(R.id.apply_button);
        myJobIcon = (TextView) findViewById(R.id.textview_jobs_icon);
        myProfileIcon = (TextView) findViewById(R.id.textview_profile_icon);
        myChatIcon = (TextView) findViewById(R.id.textview_mychat_icon);
        rateAppIcon = (TextView) findViewById(R.id.textview_rate_icon);
        shareAppIcon = (TextView) findViewById(R.id.textview_share_icon);
        feedbackIcon = (TextView) findViewById(R.id.textview_feedback_icon);
        changePassIcon = (TextView) findViewById(R.id.textview_password_icon);
        logoutIcon = (TextView) findViewById(R.id.textview_logout_icon);

        TextView txtMyJobs = (TextView) findViewById(R.id.textview_jobs_title);
        TextView txtMyProfile = (TextView) findViewById(R.id.textview_profile_title);
        TextView txtMyChat = (TextView) findViewById(R.id.textview_mychat_title);
        TextView txtRateApp = (TextView) findViewById(R.id.textview_rate_title);
        TextView txtShareApp = (TextView) findViewById(R.id.textview_share_title);
        TextView txtFeedback = (TextView) findViewById(R.id.textview_feedback_title);
        TextView txtChangePassword = (TextView) findViewById(R.id.textview_password_title);
        TextView txtLogout = (TextView) findViewById(R.id.textview_logout_title);
        txtMyJobs.setTypeface(typeRobotoMedium);
        txtMyProfile.setTypeface(typeRobotoMedium);
        txtMyChat.setTypeface(typeRobotoMedium);
        txtRateApp.setTypeface(typeRobotoMedium);
        txtShareApp.setTypeface(typeRobotoMedium);
        txtFeedback.setTypeface(typeRobotoMedium);
        txtChangePassword.setTypeface(typeRobotoMedium);
        txtLogout.setTypeface(typeRobotoMedium);

        myJobIcon.setTypeface(AccountUtils.fontelloTtfIconsFont());
        myProfileIcon.setTypeface(AccountUtils.fontelloTtfIconsFont());
        myChatIcon.setTypeface(AccountUtils.fontelloTtfIconsFont());
        rateAppIcon.setTypeface(AccountUtils.fontelloTtfIconsFont());
        shareAppIcon.setTypeface(AccountUtils.fontelloTtfIconsFont());
        feedbackIcon.setTypeface(AccountUtils.fontelloTtfIconsFont());
        changePassIcon.setTypeface(AccountUtils.fontelloTtfIconsFont());
        logoutIcon.setTypeface(AccountUtils.fontelloTtfIconsFont());

        myJobIcon.setText(ConstantFontelloID.ICON_NAV_JOBS);
        myProfileIcon.setText(ConstantFontelloID.ICON_NAV_PROFILE);
        myChatIcon.setText(ConstantFontelloID.ICON_NAV_CHAT);
        rateAppIcon.setText(ConstantFontelloID.ICON_NAV_RATE);
        shareAppIcon.setText(ConstantFontelloID.ICON_NAV_SHARE);
        feedbackIcon.setText(ConstantFontelloID.ICON_NAV_FEEDBACK);
        changePassIcon.setText(ConstantFontelloID.ICON_NAV_PASSWORD);
        logoutIcon.setText(ConstantFontelloID.ICON_NAV_LOGOUT);

        Btn01 = (Button) findViewById(R.id.btn_iv01);
        Btn02 = (Button) findViewById(R.id.btn_iv02);
        Btn03 = (Button) findViewById(R.id.btn_iv03);
        Btn04 = (Button) findViewById(R.id.btn_iv04);
        Btn05 = (Button) findViewById(R.id.btn_iv05);
        Btn01.setOnClickListener(this);
        Btn02.setOnClickListener(this);
        Btn03.setOnClickListener(this);
        Btn04.setOnClickListener(this);
        Btn05.setOnClickListener(this);


        String strCategories = AccountUtils.getCategories();
        if("" == strCategories){
            selOptiopnText.setText(getResources().getString(R.string.published_jobs));
            selOptiopnText02.setText(getResources().getString(R.string.pending_jobs));
            jobSelList.add(getResources().getString(R.string.published_jobs));
            jobSelList.add(getResources().getString(R.string.pending_jobs));
            Btn01.setText("   "+getResources().getString(R.string.published_jobs)+"   ");
            Btn05.setText("   "+getResources().getString(R.string.pending_jobs)+"   ");
            Btn01.setBackgroundResource(R.drawable.job_bt_select);
            Btn05.setBackgroundResource(R.drawable.job_bt_select);
            Btn01.setTextColor(Color.parseColor("#FFFFFF"));
            Btn05.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else{
            String[] strCat = strCategories.split(",");
            int length = strCat.length;
            for(int i = 0; i < length; i++){
                jobSelList.add(strCat[i]);
                if(strCat[i].equalsIgnoreCase(getResources().getString(R.string.published_jobs))){
                    Btn01.setText("   "+getResources().getString(R.string.published_jobs)+"   ");
                    Btn01.setBackgroundResource(R.drawable.job_bt_select);
                    Btn01.setTextColor(Color.parseColor("#FFFFFF"));
                }
                if(strCat[i].equalsIgnoreCase(getResources().getString(R.string.unpublished_jobs))){
                    Btn02.setText("   "+getResources().getString(R.string.unpublished_jobs)+"   ");
                    Btn02.setBackgroundResource(R.drawable.job_bt_select);
                    Btn02.setTextColor(Color.parseColor("#FFFFFF"));
                }
                if(strCat[i].equalsIgnoreCase(getResources().getString(R.string.updated_jobs))){
                    Btn03.setText("   "+getResources().getString(R.string.updated_jobs)+"   ");
                    Btn03.setBackgroundResource(R.drawable.job_bt_select);
                    Btn03.setTextColor(Color.parseColor("#FFFFFF"));
                }
                if(strCat[i].equalsIgnoreCase(getResources().getString(R.string.rejected_jobs))){
                    Btn04.setText("   "+getResources().getString(R.string.rejected_jobs)+"   ");
                    Btn04.setBackgroundResource(R.drawable.job_bt_select);
                    Btn04.setTextColor(Color.parseColor("#FFFFFF"));
                }
                if(strCat[i].equalsIgnoreCase(getResources().getString(R.string.pending_jobs))){
                    Btn05.setText("   "+getResources().getString(R.string.pending_jobs)+"   ");
                    Btn05.setBackgroundResource(R.drawable.job_bt_select);
                    Btn05.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
            savedCategory();
        }
        //applyButton.setOnClickListener(this);
        //selOptiopnText.setText(Utility.getItemSelection(jobSelList).trim());
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        dataNotFoundText = (TextView) findViewById(R.id.no_job_text);
        mJobsAdapter = new MyJobsAdapter(getApplicationContext(), rlParent, this);
        mJobsAdapter.setFooterVisiblity(true);
        recyclerView = (RecyclerView) findViewById(R.id.job_recycler_view);
        recyclerView.setAdapter(mJobsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        setScrolling();
        if(Utility.isNetworkAvailableRelative(this,rlParent)){
        loadFirstPage();
        }
        if (bnd != null && bnd.get("title") != null && bnd.get("title").toString().equalsIgnoreCase("chat")) {
            //if (AccountUtils.isLoggedIn()) {
                //GCMIntentService.eventChat = new ArrayList<String>();
                //AccountUtils.trackerScreen("Chat Screen");
            if (Utility.isNetworkAvailableRelative(getApplicationContext(), rlParent)) {
                Intent myIntent = new Intent(JobsListActivity.this, MyChat.class);
                startActivity(myIntent);
            }
            }

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_iv01:
                String colorValueBtn01 = ""+Btn01.getCurrentTextColor();
                if(colorValueBtn01.equalsIgnoreCase("-1")){
                    Btn01.setTextColor(Color.parseColor("#757575"));
                    Btn01.setBackgroundResource(R.drawable.job_bt);
                    jobSelList.remove(getResources().getString(R.string.published_jobs));
                    //strLocalPublished = "0";
                    categoryShuffle();
                }
                else{
                    Btn01.setTextColor(Color.parseColor("#FFFFFF"));
                    Btn01.setText("   " + getResources().getString(R.string.published_jobs) + "   ");
                    //Btn01.setText(" " + getResources().getString(R.string.published_jobs) + " ");
                    Btn01.setBackgroundResource(R.drawable.job_bt_select);
                    selOptiopnText.setVisibility(View.VISIBLE);
                    jobSelList.add(getResources().getString(R.string.published_jobs));
                    //strLocalPublished = "1";
                    categoryShuffle();
                }
                loadFirstPage();
                break;
            case R.id.btn_iv02:
                String colorValueBtn02 = ""+Btn02.getCurrentTextColor();
                if(colorValueBtn02.equalsIgnoreCase("-1")){
                    Btn02.setTextColor(Color.parseColor("#757575"));
                    Btn02.setBackgroundResource(R.drawable.job_bt);
                    jobSelList.remove(getResources().getString(R.string.unpublished_jobs));
                    //strLocalUnpublished = "0";
                    categoryShuffle();
                }
                else{
                    Btn02.setTextColor(Color.parseColor("#FFFFFF"));
                    Btn02.setText("   "+getResources().getString(R.string.unpublished_jobs)+"   ");
                    //Btn02.setText(" "+getResources().getString(R.string.unpublished_jobs)+" ");
                    Btn02.setBackgroundResource(R.drawable.job_bt_select);
                    jobSelList.add(getResources().getString(R.string.unpublished_jobs));
                    //strLocalUnpublished = "1";
                    categoryShuffle();
                }
                loadFirstPage();
                break;
            case R.id.btn_iv03:
                String colorValueBtn03 = ""+Btn03.getCurrentTextColor();
                if(colorValueBtn03.equalsIgnoreCase("-1")){
                    Btn03.setTextColor(Color.parseColor("#757575"));
                    Btn03.setBackgroundResource(R.drawable.job_bt);
                    jobSelList.remove(getResources().getString(R.string.updated_jobs));
                    //strLocalUpdated = "0";
                    categoryShuffle();
                }
                else{
                    Btn03.setTextColor(Color.parseColor("#FFFFFF"));
                    Btn03.setText("   "+getResources().getString(R.string.updated_jobs)+"   ");
                    //Btn03.setText(" "+getResources().getString(R.string.updated_jobs)+" ");
                    Btn03.setBackgroundResource(R.drawable.job_bt_select);
                    jobSelList.add(getResources().getString(R.string.updated_jobs));
                    //strLocalUpdated = "1";
                    categoryShuffle();
                }
                loadFirstPage();
                break;
            case R.id.btn_iv04:
                String colorValueBtn04 = ""+Btn04.getCurrentTextColor();
                if(colorValueBtn04.equalsIgnoreCase("-1")){
                    Btn04.setTextColor(Color.parseColor("#757575"));
                    Btn04.setBackgroundResource(R.drawable.job_bt);
                    jobSelList.remove(getResources().getString(R.string.rejected_jobs));
                    //strLocalRejected = "0";
                    categoryShuffle();
                }
                else {
                    Btn04.setTextColor(Color.parseColor("#FFFFFF"));
                    Btn04.setText("   " + getResources().getString(R.string.rejected_jobs) + "   ");
                    //Btn04.setText(" " + getResources().getString(R.string.rejected_jobs) + " ");
                    Btn04.setBackgroundResource(R.drawable.job_bt_select);
                    jobSelList.add(getResources().getString(R.string.rejected_jobs));
                    //strLocalRejected = "1";
                    categoryShuffle();
                }
                loadFirstPage();
                break;
            case R.id.btn_iv05:
                String colorValueBtn05 = ""+Btn05.getCurrentTextColor();
                if(colorValueBtn05.equalsIgnoreCase("-1")){
                    Btn05.setTextColor(Color.parseColor("#757575"));
                    Btn05.setBackgroundResource(R.drawable.job_bt);
                    jobSelList.remove(getResources().getString(R.string.pending_jobs));
                    //strLocalPending = "0";
                    categoryShuffle();
                }
                else {
                    Btn05.setTextColor(Color.parseColor("#FFFFFF"));
                    Btn05.setText("   " + getResources().getString(R.string.pending_jobs) + "   ");
                    //Btn05.setText(" " + getResources().getString(R.string.pending_jobs) + " ");
                    Btn05.setBackgroundResource(R.drawable.job_bt_select);
                    jobSelList.add(getResources().getString(R.string.pending_jobs));
                    //strLocalPending = "1";
                    categoryShuffle();
                }
                loadFirstPage();
                break;

            case R.id.card_views_joblist:
                if (mPopupLayout.getVisibility() == View.GONE) {
                    mPopupLayout.setVisibility(View.VISIBLE);
                    openCloseIv.setBackgroundResource(R.drawable.up_arrow);
                } else {
                    mPopupLayout.setVisibility(View.GONE);
                    openCloseIv.setBackgroundResource(R.drawable.down_arrow);
                    String strSelection= "";
                    if(jobSelList.size() ==1){
                        strSelection = jobSelList.get(0);
                    }
                    else if(jobSelList.size() ==2){
                        strSelection = jobSelList.get(0)+","+jobSelList.get(1);
                    }
                    else if(jobSelList.size() ==3){
                        strSelection = jobSelList.get(0)+","+jobSelList.get(1)+","+jobSelList.get(2);
                    }
                    else if(jobSelList.size() ==4){
                        strSelection = jobSelList.get(0)+","+jobSelList.get(1)+","+jobSelList.get(2)+","+jobSelList.get(3);
                    }
                    else if(jobSelList.size() ==5){
                        strSelection = jobSelList.get(0)+","+jobSelList.get(1)+","+jobSelList.get(2)+","+jobSelList.get(3)+","+jobSelList.get(4);
                    }
                    AccountUtils.trackEvents("My Jobs", "rtViewJobStatus",
                            "Origin= "+"JobList"+", Selection= "+strSelection+" ,UserId= "+""+" ,JobId= "+"", null);

                }
                break;
            default:
                break;
        }
    }
    private void categoryShuffle(){
        if(jobSelList.size() == 0){
            selOptiopnText.setVisibility(View.GONE);
            selOptiopnText02.setVisibility(View.GONE);
            title_textnumber.setText("+"+0);
            title_textnumber.setVisibility(View.GONE);
            AccountUtils.setCategories("");
        }
        else if(jobSelList.size() == 1){
            selOptiopnText.setVisibility(View.VISIBLE);
            selOptiopnText02.setVisibility(View.GONE);
            selOptiopnText.setText(jobSelList.get(0));
            title_textnumber.setText("+"+0);
            title_textnumber.setVisibility(View.GONE);
            AccountUtils.setCategories(jobSelList.get(0));

        }
        else if(jobSelList.size() == 2){
            selOptiopnText.setVisibility(View.VISIBLE);
            selOptiopnText02.setVisibility(View.VISIBLE);
            selOptiopnText.setText(jobSelList.get(0));
            selOptiopnText02.setText(jobSelList.get(1));
            title_textnumber.setText("+"+0);
            title_textnumber.setVisibility(View.GONE);
            AccountUtils.setCategories(jobSelList.get(0)+","+jobSelList.get(1));
        }
        else if(jobSelList.size() == 3){
            selOptiopnText.setVisibility(View.VISIBLE);
            selOptiopnText02.setVisibility(View.VISIBLE);
            selOptiopnText.setText(jobSelList.get(0));
            selOptiopnText02.setText(jobSelList.get(1));
            title_textnumber.setVisibility(View.VISIBLE);
            title_textnumber.setText("+"+1);
            AccountUtils.setCategories(jobSelList.get(0)+","+jobSelList.get(1)+","+
                    jobSelList.get(2));
        }
        else if(jobSelList.size() == 4){
            selOptiopnText.setVisibility(View.VISIBLE);
            selOptiopnText02.setVisibility(View.VISIBLE);
            selOptiopnText.setText(jobSelList.get(0));
            selOptiopnText02.setText(jobSelList.get(1));
            title_textnumber.setVisibility(View.VISIBLE);
            title_textnumber.setText("+"+2);
            AccountUtils.setCategories(jobSelList.get(0)+","+jobSelList.get(1)+","+
                    jobSelList.get(2)+","+ jobSelList.get(3));
        }
        else if(jobSelList.size() == 5){
            selOptiopnText.setVisibility(View.VISIBLE);
            selOptiopnText02.setVisibility(View.VISIBLE);
            selOptiopnText.setText(jobSelList.get(0));
            selOptiopnText02.setText(jobSelList.get(1));
            title_textnumber.setVisibility(View.VISIBLE);
            title_textnumber.setText("+"+3);
            AccountUtils.setCategories(jobSelList.get(0)+","+jobSelList.get(1)+","+
                    jobSelList.get(2)+","+ jobSelList.get(3)+","+ jobSelList.get(4));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mTracker.setScreenName("JobList Activity");
        //mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    private void checkChatCount() {
        try {
            ChatManager.getInstance().initializePubNub();
            ChatPreferences.getChatSharedInstance().setChatPrefernce(ChatConstants.CHAT_UUID, ChatPreferences.getChatSharedInstance().getChatPreference(ChatConstants.LOGIN_UUID));
            new ChatSyncTask(ChatConstants.PRE_API_MY_CHAT).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initDrawer(){
        CircleImageView imgProfile = (CircleImageView) findViewById(R.id.profile_image);
        TextView txtName = (TextView) findViewById(R.id.username);
        TextView txtEmail = (TextView) findViewById(R.id.email);
        txtName.setTypeface(typeRobotoMedium);
        txtEmail.setTypeface(typeRobotoRegular);
        String userName = MySharedPreference.getKeyValue(Constant.KEY_USER_NAME);
        String userEmail = MySharedPreference.getKeyValue(Constant.KEY_USER_EMAIL);
        String imgUrl = MySharedPreference.getKeyValue(Constant.KEY_USER_IMAGE);
        if (userName != null && userEmail != null && imgUrl != null && !imgUrl.equals("")) {
            txtName.setText(userName);
            txtEmail.setText(userEmail);
            ImageLoader imageLoader = RecruiterApplication.getApplication().getImageLoader();
            imgProfile.setImageUrl(imgUrl, imageLoader);
        }
    }
    public void selectDrawerItem(View view) {
        switch (view.getId()) {
            case R.id.first_block_layout:
                startActivity(new Intent(JobsListActivity.this, UserProfileActivity.class));
                break;
            case R.id.my_profile:
                startActivity(new Intent(JobsListActivity.this, UserProfileActivity.class));
                break;
            case R.id.my_jobs:
                drawerLayout.closeDrawers();
                break;
            case R.id.my_Chat:
                if (Utility.isNetworkAvailableRelative(getApplicationContext(), rlParent)) {
                Intent myIntent = new Intent(JobsListActivity.this, MyChat.class);
                Bundle bundle = new Bundle();
                bundle.putByte("CHAT_NOTIFICATION", (byte) 0);
                myIntent.putExtras(bundle);
                startActivity(myIntent);
                }
                break;
            case R.id.change_password:
                startActivity(new Intent(JobsListActivity.this, ChangePasswordActivity.class));
                break;
            case R.id.feedback:
                startActivity(new Intent(JobsListActivity.this, FeedBackActivity.class));
                break;
            case R.id.rate_app:
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + JobsListActivity.this.getPackageName())));
                break;
            case R.id.share_app:
                shareApp();
                break;
            case R.id.logout:
                logout();
                break;
        }
    }
    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, Constant.SHARE_APP_TEXT);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    private void savedCategory(){
        if(jobSelList.size() == 1){
            selOptiopnText.setVisibility(View.VISIBLE);
            selOptiopnText02.setVisibility(View.GONE);
            selOptiopnText.setText(jobSelList.get(0));
            title_textnumber.setText("+"+0);
            title_textnumber.setVisibility(View.GONE);
        }
        else if(jobSelList.size() == 2){
            selOptiopnText.setVisibility(View.VISIBLE);
            selOptiopnText02.setVisibility(View.VISIBLE);
            selOptiopnText.setText(jobSelList.get(0));
            selOptiopnText02.setText(jobSelList.get(1));
            title_textnumber.setText("+"+0);
            title_textnumber.setVisibility(View.GONE);
        }
        else if(jobSelList.size() == 3){
            selOptiopnText.setVisibility(View.VISIBLE);
            selOptiopnText02.setVisibility(View.VISIBLE);
            selOptiopnText.setText(jobSelList.get(0));
            selOptiopnText02.setText(jobSelList.get(1));
            title_textnumber.setVisibility(View.VISIBLE);
            title_textnumber.setText("+"+1);
        }
        else if(jobSelList.size() == 4){
            selOptiopnText.setVisibility(View.VISIBLE);
            selOptiopnText02.setVisibility(View.VISIBLE);
            selOptiopnText.setText(jobSelList.get(0));
            selOptiopnText02.setText(jobSelList.get(1));
            title_textnumber.setVisibility(View.VISIBLE);
            title_textnumber.setText("+"+2);
        }
        else if(jobSelList.size() == 5){
            selOptiopnText.setVisibility(View.VISIBLE);
            selOptiopnText02.setVisibility(View.VISIBLE);
            selOptiopnText.setText(jobSelList.get(0));
            selOptiopnText02.setText(jobSelList.get(1));
            title_textnumber.setVisibility(View.VISIBLE);
            title_textnumber.setText("+"+3);
        }
    }
    private void logout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(JobsListActivity.this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.are_you_sure_message));
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.logout_que_message))
                //.setCancelable(false)
                .setNegativeButton(getResources().getString(R.string.cancel_lbl), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setPositiveButton(getResources().getString(R.string.OK_lbl), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        connectToLogout();
                        try {
                            MySharedPreference.clearGlobleRecord();
                            MySharedPreference.clearCandidateRecord();
                            MySharedPreference.clearFilterType();
                            MySharedPreference.clearRecruiterType();
                            ChatPreferences.getChatSharedInstance().removeChatPreferenceValue(ChatConstants.MY_CHAT_PROFILE_RESPONSE);
                            ChatPreferences.getChatSharedInstance().removeChatPreferenceValue(ChatConstants.LOGIN_MYNOTIFICATION);
                            ChatPreferences.getChatSharedInstance().removeChatPreferenceValue(ChatConstants.LOGIN_UUID);
                            ChatPreferences.getChatSharedInstance().removeChatPreferenceValue(ChatConstants.CHAT_COUNT);
                            ChatManager.getInstance().chatCount = new HashMap<String, Integer>();
                            ChatManager.getInstance().mPubNub = null;
                            Utility.cancelNotification();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        Intent ic = new Intent(JobsListActivity.this, LoginActivity.class);
                        ic.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ic.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ic);
                        finish();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
    }

    private void connectToLogout() {
        if (Utility.isNetworkAvailableRelative(getApplicationContext(), rlParent)) {
            Utility.showLoadingDialog(JobsListActivity.this);
            String url = Constant.LOGOUT_URL + Constant.USER_COOKIE;
            RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                Utility.dismisLoadingDialog();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            if (response != null) {
                                Gson mGson = new GsonBuilder().create();
                                LoginModel iModel = mGson.fromJson(response, LoginModel.class);
                                if (iModel != null) {
                                    if (iModel.getStatus().equalsIgnoreCase(Constant.HTTP_OK)) {

                                    } else {
                                        Snackbar snackbar = Snackbar.make(rlParent, iModel.getError_msg(), Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                    }
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            try{
                            Utility.dismisLoadingDialog();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
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
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(JobsListActivity.this);
                                builder.setTitle(titleMessage).setMessage(messageText)
                                        .setCancelable(true)
                                        .setPositiveButton(getString(R.string.ok_lbl), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                if (error instanceof TimeoutError)
                                                    connectToLogout();
                                            }
                                        });
                                android.app.AlertDialog alert = builder.create();
                                alert.show();
                                alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    params.put("deviceid", deviceId);
                    return params;
                }

                @Override
                public Map getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();

                    if (!Constant.isLiveServer) {
                        String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                                ("mba" + ":" + "iim1@2#3$").getBytes(),
                                Base64.NO_WRAP);
                        headers.put("Authorization", base64EncodedCredentials);
                    }
                    headers.put("AUTHKEY", ""+ MySharedPreference.getKeyValue(Constant.KEY_USER_AUTH_KEY));
                    //  headers.put("User-agent", System.getProperty("http.agent")+" "+getResources().getString(R.string.app_name)+" "+Utility.getAppVersionName());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (animation.equals(mPopupShow)) {
            mPopupLayout.setVisibility(View.VISIBLE);
        } else if (animation.equals(mPopupHide)) {
            mPopupLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation.equals(mPopupShow)) {
            openCloseIv.setBackgroundResource(R.drawable.up_arrow);
        } else if (animation.equals(mPopupHide)) {
            openCloseIv.setBackgroundResource(R.drawable.down_arrow);
        }
    }
    @Override
    public void onAnimationRepeat(Animation animation) {
    }
    public String getUrl(int mPageNo){
        String tempUrl = null;
        switch(jobSelList.size()){
            case 0:
                tempUrl = "";
                break;
            case 1:
                tempUrl = Utility.getItemSelection(jobSelList).toLowerCase().replace(" jobs", "").trim();
                break;
            default:
                tempUrl = Utility.getItemSelection(jobSelList).toLowerCase().replace(" jobs, ", ",").replace(" jobs", "").trim();
                break;
        }
        //String url = Constant.MY_JOB_URL + mPageNo +"/0/" + Constant.USER_COOKIE+"?filterv2="+tempUrl;
        try{
            strCategoriesCallValue = AccountUtils.getJobListCallValue();
        }catch (Exception e){
            e.printStackTrace();
        }
        if("" == strCategoriesCallValue){
            strCategoriesCallValue = "10010";
        }else {
            String colorValueBtn01 = ""+Btn01.getCurrentTextColor();
            String colorValueBtn02 = ""+Btn02.getCurrentTextColor();
            String colorValueBtn03 = ""+Btn03.getCurrentTextColor();
            String colorValueBtn04 = ""+Btn04.getCurrentTextColor();
            String colorValueBtn05 = ""+Btn05.getCurrentTextColor();
            if(colorValueBtn01.equalsIgnoreCase("-1")){
                strLocalPublished = "1";
            }
            else{
                strLocalPublished = "0";
            }

            if(colorValueBtn02.equalsIgnoreCase("-1")){
                strLocalUnpublished = "1";
            }
            else{
                strLocalUnpublished = "0";
            }
            if(colorValueBtn03.equalsIgnoreCase("-1")){
                strLocalUpdated = "1";
            }
            else{
                strLocalUpdated = "0";
            }
            if(colorValueBtn04.equalsIgnoreCase("-1")){
                strLocalRejected = "1";
            }
            else{
                strLocalRejected = "0";
            }
            if(colorValueBtn05.equalsIgnoreCase("-1")){
                strLocalPending = "1";
            }
            else{
                strLocalPending = "0";
            }

        strCategoriesCallValue = strLocalPending + strLocalRejected + strLocalUpdated + strLocalPublished + strLocalUnpublished;
        }
        AccountUtils.setJobListCallValue(strCategoriesCallValue);
        String url = Constant.MY_JOB_URL + mPageNo +"/"+strCategoriesCallValue+"/" + Constant.USER_COOKIE;
        strSelectetCategories = tempUrl;
        return url;
    }
    private void setScrolling(){
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if(mPopupLayout.getVisibility() == View.VISIBLE){
                    mPopupLayout.startAnimation(mPopupHide);
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if(currentPage > 1 && isLoadMore ){
                        loadMorePage(currentPage);
                        isLoadMore = false;
                    }
                }
            }
        });
    }
    public void loadMorePage(int mPageNo){
        isLoadMore = false;
        url = getUrl(mPageNo);
        requestToAllJob(url, mPageNo);

    }
    private void loadFirstPage(){
        isLoadMore = false;
        currentPage = 1;
        url = getUrl(currentPage);
        Log.e(TAG, "at loadFirstPage url= "+url);
        if("" == strSelectetCategories){
            if (mJobList != null) {
                if (mJobList.size() > 0)
                    mJobList.clear();
            }
            recyclerView.setVisibility(View.GONE);
            dataNotFoundText.setVisibility(View.VISIBLE);
            dataNotFoundText.setText("You don't have any jobs for this filter. Please select a different filter.");
        }
        else{
            swipeRefreshLayout.post(new Runnable() {
             @Override
             public void run() {
              swipeRefreshLayout.setRefreshing(true);
                }
              }
            );

            Log.e(TAG, "at loadFirstPage method url= "+url);
            requestToAllJob(url, currentPage);
        }
    }
    private void requestToAllJob(String mUrl, final int mCurrentPage) {
        if(Utility.isNetworkAvailableRelative(this,rlParent)){
            RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
            StringRequest stringRequest = new StringRequest(mUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (response != null) {
                        final Gson mGson = new GsonBuilder().create();
                        final JobModel iModel = mGson.fromJson(response, JobModel.class);
                        if (iModel != null && iModel.getStatus().equalsIgnoreCase(Constant.HTTP_OK)) {
                            mTotalPage = iModel.getnoofpages();
                            mCPage = iModel.getPage();
                            UpdateAppData.setChat_enable(iModel.getChatEnable());
                            ArrayList<JobItemsModel> mList = iModel.getJobsarray();
                            if(mCPage == mTotalPage){
                                mJobsAdapter.setFooterVisiblity(false);
                            }
                            recyclerView.setVisibility(View.VISIBLE);
                            dataNotFoundText.setVisibility(View.GONE);
                            if(mCurrentPage == 1) {
                                if (mJobList != null) {
                                    if (mJobList.size() > 0)
                                        mJobList.clear();
                                }
                                mJobList = iModel.getJobsarray();
                                mJobsAdapter.loadData(mJobList);
                                mJobsAdapter.notifyDataSetChanged();
                                if(mCPage != mTotalPage) {
                                    currentPage = 2;
                                    isLoadMore = true;
                                }
                            }else{
                                if(mJobList != null && mJobList.size() > 0){
                                    mJobList.addAll(iModel.getJobsarray());
                                }
                                mJobsAdapter.initData();
                                if(currentPage != mTotalPage){
                                    currentPage = currentPage + 1;
                                    isLoadMore = true;
                                    loadMorePage(currentPage);
                                }else{
                                    isLoadMore = false;
                                    mJobsAdapter.setFooterVisiblity(false);
                                }
                                mJobsAdapter.notifyDataSetChanged();
                            }

                        } else if (dataNotFoundText != null) {
                            recyclerView.setVisibility(View.GONE);
                            dataNotFoundText.setVisibility(View.VISIBLE);
                            //dataNotFoundText.setText(iModel.getError_msg());
                            dataNotFoundText.setText("You don't have any jobs for this filter. Please select a different filter.");
                        }

                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            swipeRefreshLayout.setRefreshing(false);
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
                                if(mTotalPage != 0 && mCurrentPage <= mTotalPage) {
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(JobsListActivity.this);
                                    builder.setTitle(titleMessage).setMessage(messageText)
                                            .setCancelable(true)
                                            .setPositiveButton(getString(R.string.ok_lbl), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    if (error instanceof TimeoutError)
                                                        requestToAllJob(url, currentPage);
                                                }
                                            });
                                    android.app.AlertDialog alert = builder.create();
                                    if (!isFinishing()) {
                                        alert.show();
                                        alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
                                    }
                                }
                            }
                        }
                    }){
                @Override
                public Map getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    if(!Constant.isLiveServer){
                        String base64EncodedCredentials = "Basic " + Base64.encodeToString(("mba" + ":" + "iim1@2#3$").getBytes(), Base64.NO_WRAP);
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
        }else{
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        dataNotFoundText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
       //swipeRefreshLayout.setRefreshing(true);
        isLoadMore = false;
        currentPage = 1;
        url = getUrl(currentPage);
        if("" == strSelectetCategories){
            if (mJobList != null) {
                if (mJobList.size() > 0)
                    mJobList.clear();
            }
            recyclerView.setVisibility(View.GONE);
            dataNotFoundText.setVisibility(View.VISIBLE);
            dataNotFoundText.setText("You don't have any jobs for this filter. Please select a different filter.");
        }
        else{
            requestToAllJob(url, currentPage);
        }
    }
}
