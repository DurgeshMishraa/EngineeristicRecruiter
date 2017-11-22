package com.engineeristic.recruiter.myapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.adapter.NewCandidateListAdapter;
import com.engineeristic.recruiter.filter.BatchFilterFragment;
import com.engineeristic.recruiter.filter.ExperienceFilterFragment;
import com.engineeristic.recruiter.filter.GenderFilterFragment;
import com.engineeristic.recruiter.filter.MultiSelectionFilterFragment;
import com.engineeristic.recruiter.filter.NoticePeriodFilterFragment;
import com.engineeristic.recruiter.filter.SalaryFilterFragment;
import com.engineeristic.recruiter.helper.OnStartDragListener;
import com.engineeristic.recruiter.helper.SimpleItemTouchHelperCallback;
import com.engineeristic.recruiter.model.CoverLatterModel;
import com.engineeristic.recruiter.model.JobTitlePojo;
import com.engineeristic.recruiter.util.AccountUtils;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.RecyclerTapListener;
import com.engineeristic.recruiter.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewCandidateListActivity extends AppCompatActivity implements OnStartDragListener, Animation.AnimationListener,
        RecyclerTapListener, View.OnClickListener{

    private  String jobtitle = null;
    private boolean isNotificationLand = false;
    private String publishedId = "";
    private String mUrl;
    private String chooseOption="";
    private int mPageNumber = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tryLaterText;
    private ArrayList<JSONObject>  nListViewArray = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewCandidateListAdapter mCandidateAdapter;
    private CardView cardViewSel;
    private TextView spinnerTitle;
    private  String catagoryType = "11";
    private String mJobTitle;
    private ItemTouchHelper mItemTouchHelper;
    private FloatingActionButton floatingButton;
    public static boolean isAllCandidateFilter = false;
    public static boolean isSingleTap = false;
    public int currentPage = 0;
    private static int mTotalPage = 0;
    private static int mCPage = 0;
    private boolean isLoadMore = false;
    private String url;
    private Animation rowHideAnim;
    private CardView mPopupLayout;
    private Button Btn01, Btn02, Btn03, Btn04, Btn05;
    private ImageView openCloseIv;
    private Animation mPopupShow;
    private Animation mPopupHide;
    private RelativeLayout rl_coordinator;
    private static final String TAG = "NewCandidateListActivit";
    private int count = 0;
    private ImageView imageNext;
    private RelativeLayout rl_parent;
    private boolean isCount = true;
    private boolean isNotification = false;
    private int nSaved, nRejected, nShortListed, nUnread, nMagicSort;
    private String strUserId, strJobId;
    private boolean isUnread = false;
    private boolean isMagicsort = false;
    private boolean isCoachMarkOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.new_candidate_list_screen);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        AccountUtils.trackerScreen("Candidate Apply List");
        rowHideAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        isNotificationLand = getIntent().getBooleanExtra(Constant.KEY_NOTIFICATION_LANDS, false);
        mJobTitle = getIntent().getStringExtra(Constant.KEY_JOB_TITLE);
        RecruiterApplication application = (RecruiterApplication) getApplication();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_newcandidatelistactivity);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
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
                resetData();
                finish();
            }
        });
        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().getString(Constant.KEY_JOB_ID) != null && getIntent().getExtras().getString(Constant.KEY_DATA_TYPE)!=null){
                publishedId = getIntent().getExtras().getString(Constant.KEY_JOB_ID);
                GCMPushReceiverService.hashMapCandidatelist.remove(getIntent().getExtras().getString(Constant.KEY_JOB_ID));
                AccountUtils.setCategoriesCandidate(0+","+"Unread");
                isNotification = true;
            }
            else{
                AccountUtils.setCategoriesCandidate("11"+","+"Magic Sort");
                publishedId = getIntent().getStringExtra(Constant.KEY_JOB_ID);
                isNotification = false;
            }
            jobtitle = getIntent().getExtras().getString(Constant.KEY_JOB_TITLE);
            JobTitlePojo.setJobtitle(jobtitle);
            if(jobtitle != null && !jobtitle.equals("")){
                if(jobtitle.contains(":")){
                    String str[] = jobtitle.split(":");
                    Spannable text = new SpannableString(str[1]);
                    text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_text_clr)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    getSupportActionBar().setTitle(text);

                }
                else{
                    Spannable text = new SpannableString(jobtitle);
                    text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_text_clr)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    getSupportActionBar().setTitle(text);
                }
            }
        }

        nSaved = MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SAVE_TYPE);
        nMagicSort = MySharedPreference.getRecruiterViewType(Constant.RECRUITER_MAGIC_TYPE);
        nRejected = MySharedPreference.getRecruiterViewType(Constant.RECRUITER_REJECT_TYPE);
        nShortListed = MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE);
        nUnread = MySharedPreference.getRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE);

        spinnerTitle = (TextView) findViewById(R.id.title_text_newcandidate);
        rl_coordinator = (RelativeLayout) findViewById(R.id.rl_coordinator_newcandidate);
        mPopupLayout = (CardView) findViewById(R.id.popup_layout_candidate);
        Btn01 = (Button) findViewById(R.id.btn_iv01_candidate);
        Btn02 = (Button) findViewById(R.id.btn_iv02_candidate);
        Btn03 = (Button) findViewById(R.id.btn_iv03_candidate);
        Btn04 = (Button) findViewById(R.id.btn_iv04_candidate);
        Btn05 = (Button) findViewById(R.id.btn_iv05_candidate);
        Btn01.setOnClickListener(this);
        Btn02.setOnClickListener(this);
        Btn03.setOnClickListener(this);
        Btn04.setOnClickListener(this);
        Btn05.setOnClickListener(this);
        tryLaterText = (TextView) findViewById(R.id.no_candidate_text);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        openCloseIv = (ImageView) findViewById(R.id.on_off_arrow);
        mPopupShow = AnimationUtils.loadAnimation(this, R.anim.popup_show);
        mPopupShow.setAnimationListener(this);
        mPopupHide = AnimationUtils.loadAnimation(this, R.anim.popup_hide);
        mPopupHide.setAnimationListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            swipeRefreshLayout.setTransitionName("profile");
        }

        mCandidateAdapter = new NewCandidateListAdapter(NewCandidateListActivity.this, rl_coordinator,
                spinnerTitle, swipeRefreshLayout, mPopupLayout, openCloseIv);
        mCandidateAdapter.setFooterVisiblity(true);
        recyclerView.setAdapter(mCandidateAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        floatingButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllCandidateFilter = false;
                Intent intent = new Intent(NewCandidateListActivity.this, CandidateFilterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        if(Utility.isFilterSelected()){
            floatingButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.filtered));
        }else{
            floatingButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.filter));
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 && floatingButton.isShown())
                {
                    floatingButton.hide();
                }
                else{
                    floatingButton.show();
                }
                if(mPopupLayout.getVisibility() == View.VISIBLE){
                    mPopupLayout.setVisibility(View.GONE);
                }


            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState){
                if(newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    if(currentPage > 1 && isLoadMore ){
                        Log.e(TAG, "onScrollStateChanged method");
                        loadMorePage(currentPage);
                        isLoadMore = false;
                    }
                }
                if(!AccountUtils.getBooleanCoachMark()){
                    if(!isCoachMarkOnce){
                    showDetailCoachMark();
                    }
                    isCoachMarkOnce = true;
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        String strCategories = AccountUtils.getCategoriesCandidate();
        if("" == strCategories){
            spinnerTitle.setText("Magic Sort");
            catagoryType = "11";
            Btn05.setTextColor(Color.parseColor("#FFFFFF"));
            Btn05.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
            Btn05.setText(" "+"Magic Sort"+" - "+nMagicSort+" ");
        }
        else{
            //String strSplit[] = spinnerTitle.getText().toString().trim().split("-");
            //String[] strCat = strSplit[0].trim().split(",");
            String[] strCat = strCategories.split(",");
            spinnerTitle.setText(strCat[1].toString().trim());
            catagoryType = strCat[0];
            if(catagoryType.equalsIgnoreCase("0")){
                Btn01.setTextColor(Color.parseColor("#FFFFFF"));
                Btn01.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
                //Btn01.setText("  "+"Unread"+" - "+nUnread+"  ");
            }
            else if(catagoryType.equalsIgnoreCase("1")){
                Btn02.setTextColor(Color.parseColor("#FFFFFF"));
                Btn02.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
                //Btn02.setText("  "+"Shortlisted"+" - "+nShortListed+"  ");
            }
            else if(catagoryType.equalsIgnoreCase("2")){
                Btn03.setTextColor(Color.parseColor("#FFFFFF"));
                Btn03.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
                //Btn03.setText("  "+"Rejected"+" - "+nRejected+"  ");
            }
            else if(catagoryType.equalsIgnoreCase("3")){
                Btn04.setTextColor(Color.parseColor("#FFFFFF"));
                Btn04.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
                //Btn04.setText("  "+"Saved"+" - "+nSaved+"  ");
            }
            else if(catagoryType.equalsIgnoreCase("11")){
                Btn05.setTextColor(Color.parseColor("#FFFFFF"));
                Btn05.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
                //Btn05.setText("  "+"Magic Sort"+" - "+nMagicSort+"  ");
            }
            Btn01.setText(" "+"Unread"+" - "+nUnread+" ");
            Btn02.setText(" "+"Shortlisted"+" - "+nShortListed+" ");
            Btn03.setText(" "+"Rejected"+" - "+nRejected+" ");
            Btn04.setText(" "+"Saved"+" - "+nSaved+" ");
            Btn05.setText(" "+"Magic Sort"+" - "+nMagicSort+" ");
        }
        cardViewSel = (CardView) findViewById(R.id.sel_card_view);
        cardViewSel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try{
                String string = spinnerTitle.getText().toString().trim();
                String [] split = string.split("-");
                int intFinal = Integer.parseInt(split[1].trim());
                if(split[0].trim().equalsIgnoreCase("Shortlisted")){
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE, intFinal);
                }
                else if(split[0].trim().equalsIgnoreCase("Unread")){
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE, intFinal);
                }
                else if(split[0].trim().equalsIgnoreCase("Magic Sort")){
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_MAGIC_TYPE, intFinal);
                }
                else if(split[0].trim().equalsIgnoreCase("Rejected")){
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_REJECT_TYPE, intFinal);

                }
                else if(split[0].trim().equalsIgnoreCase("Saved")){
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SAVE_TYPE, intFinal);
                }
                }catch (Exception e){
                    e.printStackTrace();
                }


                nSaved = MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SAVE_TYPE);
                nMagicSort = MySharedPreference.getRecruiterViewType(Constant.RECRUITER_MAGIC_TYPE);
                nRejected = MySharedPreference.getRecruiterViewType(Constant.RECRUITER_REJECT_TYPE);
                nShortListed = MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE);
                nUnread = MySharedPreference.getRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE);

                if (mPopupLayout.getVisibility() == View.GONE) {
                    mPopupLayout.setVisibility(View.VISIBLE);
                    openCloseIv.setBackgroundResource(R.drawable.up_arrow);
                    String strSplit[] = spinnerTitle.getText().toString().trim().split("-");
                    String strValue = strSplit[0].toString().trim();
                    if(strValue.equalsIgnoreCase("Unread")){
                        Btn01.setTextColor(Color.parseColor("#FFFFFF"));
                        Btn01.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
                        isUnread = true;
                    }
                    else if(strValue.equalsIgnoreCase("Shortlisted")){
                        Btn02.setTextColor(Color.parseColor("#FFFFFF"));
                        Btn02.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
                    }
                    else if(strValue.equalsIgnoreCase("Rejected")){
                        Btn03.setTextColor(Color.parseColor("#FFFFFF"));
                        Btn03.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
                    }
                    else if(strValue.equalsIgnoreCase("Saved")){
                        Btn04.setTextColor(Color.parseColor("#FFFFFF"));
                        Btn04.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
                    }
                    else if(strValue.equalsIgnoreCase("Magic Sort")){
                        Btn05.setTextColor(Color.parseColor("#FFFFFF"));
                        Btn05.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
                        isMagicsort = true;
                    }
                    //Btn01.setText(" "+"Unread"+" - "+nUnread+" ");
                    //Btn05.setText(" "+"Magic Sort"+" - "+nMagicSort+" ");
                    Btn02.setText(" "+"Shortlisted"+" - "+nShortListed+" ");
                    Btn03.setText(" "+"Rejected"+" - "+nRejected+" ");
                    Btn04.setText(" "+"Saved"+" - "+nSaved+" ");

                    if(isUnread){
                        Btn01.setText(" "+"Unread"+" - "+nUnread+" ");
                        Btn05.setText(" "+"Magic Sort"+" - "+nUnread+" ");
                        isMagicsort = false;
                        isUnread = false;
                    }
                    else if(isMagicsort){
                        Btn01.setText(" "+"Unread"+" - "+nMagicSort+" ");
                        Btn05.setText(" "+"Magic Sort"+" - "+nMagicSort+" ");
                        isMagicsort = false;
                        isUnread = false;
                    }
                    else{
                        Btn01.setText(" "+"Unread"+" - "+nUnread+" ");
                        Btn05.setText(" "+"Magic Sort"+" - "+nMagicSort+" ");
                        isMagicsort = false;
                        isUnread = false;
                    }
                } else {
                    String strSplit[] = spinnerTitle.getText().toString().trim().split("-");
                    String strValue = strSplit[0].toString().trim();
                    mPopupLayout.setVisibility(View.GONE);
                    openCloseIv.setBackgroundResource(R.drawable.down_arrow);
                    AccountUtils.trackEvents("NewCandidateListActivity", "rtViewCandidateStatus",
                            "Origin= "+"CAL"+", Selection= "+strValue+" ,UserId= "+publishedId+" ,JobId= "+"", null);
                }
                return false;
            }
        });
        //swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mCandidateAdapter, swipeRefreshLayout);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(NewCandidateListActivity.this, this));
        //loadFirstPage();
        if(Constant.JUMP_RESUME_ACTIVITY == "profile_resume_activity"){
            loadFirstPage();
            isCount = true;
        }
        else{
            if(Constant.JUMP_RESUME_ACTIVITY == "MyJobsAdapter")
            {
                loadFirstPage();
            }
        }
        if(isNotification){
            loadFirstPage();
            isNotification = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                resetData();
                finish();
                break;
        }
        return true;
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_iv01_candidate:
                catagoryType = "0";
                spinnerTitle.setText(Btn01.getText().toString().trim());
                String colorValueBtn01 = ""+Btn01.getCurrentTextColor();
                if(colorValueBtn01.equalsIgnoreCase("-1")){
                    Btn01.setTextColor(Color.parseColor("#757575"));
                    Btn01.setBackgroundResource(R.drawable.job_bt_newcandidate);
                }
                else{
                    Btn01.setTextColor(Color.parseColor("#FFFFFF"));
                    Btn01.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
                    Btn04.setTextColor(Color.parseColor("#757575"));
                    Btn03.setTextColor(Color.parseColor("#757575"));
                    Btn02.setTextColor(Color.parseColor("#757575"));
                    Btn05.setTextColor(Color.parseColor("#757575"));
                    Btn04.setBackgroundResource(R.drawable.job_bt_newcandidate);
                    Btn03.setBackgroundResource(R.drawable.job_bt_newcandidate);
                    Btn02.setBackgroundResource(R.drawable.job_bt_newcandidate);
                    Btn05.setBackgroundResource(R.drawable.job_bt_newcandidate);
                }
                String strSplit[] = spinnerTitle.getText().toString().trim().split("-");
                AccountUtils.setCategoriesCandidate(catagoryType+","+strSplit[0].toString().trim());
                Btn01.setText(" "+"Unread"+" - "+nUnread+" ");
                if(mPopupLayout.getVisibility() == View.VISIBLE){
                    mPopupLayout.startAnimation(mPopupHide);
                }
                isCount = true;
                loadFirstPage();
                break;
            case R.id.btn_iv02_candidate:
                catagoryType = "1";
                spinnerTitle.setText(Btn02.getText().toString().trim());
                String colorValueBtn02 = ""+Btn02.getCurrentTextColor();
                if(colorValueBtn02.equalsIgnoreCase("-1")){
                    Btn02.setTextColor(Color.parseColor("#757575"));
                    Btn02.setBackgroundResource(R.drawable.job_bt_newcandidate);
                }
                else{
                    Btn02.setTextColor(Color.parseColor("#FFFFFF"));
                    Btn02.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
                    Btn04.setTextColor(Color.parseColor("#757575"));
                    Btn03.setTextColor(Color.parseColor("#757575"));
                    Btn05.setTextColor(Color.parseColor("#757575"));
                    Btn01.setTextColor(Color.parseColor("#757575"));
                    Btn04.setBackgroundResource(R.drawable.job_bt_newcandidate);
                    Btn03.setBackgroundResource(R.drawable.job_bt_newcandidate);
                    Btn05.setBackgroundResource(R.drawable.job_bt_newcandidate);
                    Btn01.setBackgroundResource(R.drawable.job_bt_newcandidate);
                }
                String strSplit1[] = spinnerTitle.getText().toString().trim().split("-");
                AccountUtils.setCategoriesCandidate(catagoryType+","+strSplit1[0].toString().trim());
                Btn02.setText(" "+"Shortlisted"+" - "+nShortListed+" ");
                if(mPopupLayout.getVisibility() == View.VISIBLE){
                    mPopupLayout.startAnimation(mPopupHide);
                }
                isCount = true;
                loadFirstPage();
                break;
            case R.id.btn_iv03_candidate:
                catagoryType = "2";
                spinnerTitle.setText(Btn03.getText().toString().trim());
                String colorValueBtn03 = ""+Btn03.getCurrentTextColor();
                if(colorValueBtn03.equalsIgnoreCase("-1")){
                    Btn03.setTextColor(Color.parseColor("#757575"));
                    Btn03.setBackgroundResource(R.drawable.job_bt_newcandidate);
                }
                else{
                    Btn03.setTextColor(Color.parseColor("#FFFFFF"));
                    Btn03.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
                    Btn04.setTextColor(Color.parseColor("#757575"));
                    Btn05.setTextColor(Color.parseColor("#757575"));
                    Btn02.setTextColor(Color.parseColor("#757575"));
                    Btn01.setTextColor(Color.parseColor("#757575"));
                    Btn04.setBackgroundResource(R.drawable.job_bt_newcandidate);
                    Btn05.setBackgroundResource(R.drawable.job_bt_newcandidate);
                    Btn02.setBackgroundResource(R.drawable.job_bt_newcandidate);
                    Btn01.setBackgroundResource(R.drawable.job_bt_newcandidate);
                }
                String strSplit2[] = spinnerTitle.getText().toString().trim().split("-");
                AccountUtils.setCategoriesCandidate(catagoryType+","+strSplit2[0].toString().trim());
                Btn03.setText(" "+"Rejected"+" - "+nRejected+" ");
                if(mPopupLayout.getVisibility() == View.VISIBLE){
                    mPopupLayout.startAnimation(mPopupHide);
                }
                isCount = true;
                loadFirstPage();
                break;
            case R.id.btn_iv04_candidate:
                catagoryType = "3";
                spinnerTitle.setText(Btn04.getText().toString().trim());
                String colorValueBtn04 = ""+Btn04.getCurrentTextColor();
                if(colorValueBtn04.equalsIgnoreCase("-1")){
                    Btn04.setTextColor(Color.parseColor("#757575"));
                    Btn04.setBackgroundResource(R.drawable.job_bt_newcandidate);
                }
                else{
                    Btn04.setTextColor(Color.parseColor("#FFFFFF"));
                    Btn04.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
                    Btn05.setTextColor(Color.parseColor("#757575"));
                    Btn03.setTextColor(Color.parseColor("#757575"));
                    Btn02.setTextColor(Color.parseColor("#757575"));
                    Btn01.setTextColor(Color.parseColor("#757575"));
                    Btn05.setBackgroundResource(R.drawable.job_bt_newcandidate);
                    Btn03.setBackgroundResource(R.drawable.job_bt_newcandidate);
                    Btn02.setBackgroundResource(R.drawable.job_bt_newcandidate);
                    Btn01.setBackgroundResource(R.drawable.job_bt_newcandidate);
                }
                String strSplit3[] = spinnerTitle.getText().toString().trim().split("-");
                AccountUtils.setCategoriesCandidate(catagoryType+","+strSplit3[0].toString().trim());
                Btn04.setText(" "+"Saved"+" - "+nSaved+" ");
                if(mPopupLayout.getVisibility() == View.VISIBLE){
                    mPopupLayout.startAnimation(mPopupHide);
                }
                isCount = true;
                loadFirstPage();
                break;
            case R.id.btn_iv05_candidate:
                catagoryType = "11";
                spinnerTitle.setText(Btn05.getText().toString().trim());
                String colorValueBtn05 = ""+Btn05.getCurrentTextColor();
                if(colorValueBtn05.equalsIgnoreCase("-1")){
                    Btn05.setTextColor(Color.parseColor("#757575"));
                    Btn05.setBackgroundResource(R.drawable.job_bt_newcandidate);
                }
                else{
                    Btn05.setTextColor(Color.parseColor("#FFFFFF"));
                    Btn05.setBackgroundResource(R.drawable.job_bt_select_newcandidate);
                    Btn04.setTextColor(Color.parseColor("#757575"));
                    Btn03.setTextColor(Color.parseColor("#757575"));
                    Btn02.setTextColor(Color.parseColor("#757575"));
                    Btn01.setTextColor(Color.parseColor("#757575"));
                    Btn04.setBackgroundResource(R.drawable.job_bt_newcandidate);
                    Btn03.setBackgroundResource(R.drawable.job_bt_newcandidate);
                    Btn02.setBackgroundResource(R.drawable.job_bt_newcandidate);
                    Btn01.setBackgroundResource(R.drawable.job_bt_newcandidate);
                }
                String strSplit4[] = spinnerTitle.getText().toString().trim().split("-");
                AccountUtils.setCategoriesCandidate(catagoryType+","+strSplit4[0].toString().trim());
                Btn05.setText(" "+"Magic Sort"+" - "+nMagicSort+" ");
                if(mPopupLayout.getVisibility() == View.VISIBLE){
                    mPopupLayout.startAnimation(mPopupHide);
                }
                isCount = true;
                loadFirstPage();
                break;
            default:
                break;
        }
    }
    private void loadData() {
        if(!mUrl.equals("")){
            if (Utility.isNetworkAvailableRelative(NewCandidateListActivity.this, rl_coordinator)) {
                swipeRefreshLayout.setRefreshing(true);
                excuteCotroller(mUrl, currentPage);
            } else{
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
    private  String getUrl(final String option, final int mPage){
        if(option != null
                && !option.equals("")
                && publishedId != null
                && !publishedId.equals("")
                && Constant.USER_COOKIE != null){
            mUrl =  Constant.JOB_APPLIED_LIST_URL + publishedId + "/"+option+"/" + mPage + "/"+Constant.USER_COOKIE;
            Log.e(TAG, "getUrl method mUrl = "+mUrl);
        }
        return mUrl;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent intent = new Intent(NewCandidateListActivity.this, JobsListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
        resetData();
        isCoachMarkOnce = false;
        finish();
    }
    private void resetData(){
        MultiSelectionFilterFragment.industryMap.clear();
        MultiSelectionFilterFragment.funcrtionalMap.clear();
        MultiSelectionFilterFragment.instituteMap.clear();
        MultiSelectionFilterFragment.locationMap.clear();
        NoticePeriodFilterFragment.clearHistory();
        ExperienceFilterFragment.clearHistory();
        SalaryFilterFragment.clearHistory();
        BatchFilterFragment.clearHistory();
        GenderFilterFragment.genderType =  null;
        MySharedPreference.clearFilterType();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(Utility.isFilterSelected()){
            floatingButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.filtered));
        }else{
            floatingButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.filter));
        }
        if(isAllCandidateFilter){
            isAllCandidateFilter = false;
            loadFirstPage();
        }
        /*if(!AccountUtils.getBooleanCoachMark()){
            showDetailCoachMark();
        }*/
        if(Constant.JUMP_RESUME_ACTIVITY == "profile_resume_activity"){
            isCount = true;
            loadFirstPage();
        }
        isCoachMarkOnce = false;
    }
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener(){
        @Override
        public void onRefresh() {
            isCount = true;
            loadFirstPage();
        }};

    public void loadMorePage(int mPageNo){
        url = getUrl(catagoryType, mPageNo);
        isLoadMore = false;
        excuteCotroller(url, mPageNo);
    }
    private void loadFirstPage(){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        isLoadMore = false;
        currentPage = 1;
        url = getUrl(catagoryType, currentPage);
        excuteCotroller(url, currentPage);
    }
    private void excuteCotroller(final String mUrl, final int mCurrentPage){
        //Log.e(TAG, "excuteCotroller class");
        RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, mUrl,
         new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {
         swipeRefreshLayout.setRefreshing(false);
         if(response != null){
         try{
           final JSONObject jsonObject = new JSONObject(response);
           Log.e(TAG,"excuteCotroller jsonObject="+jsonObject);
           if(jsonObject != null && this != null){
            String Status = jsonObject.getString("status");
             if (Status.equalsIgnoreCase("200")){
               mTotalPage = jsonObject.getInt("noofpages");
               mCPage = Integer.parseInt(jsonObject.getString("page"));
               if(mCPage == mTotalPage)
               {
                  mCandidateAdapter.setFooterVisiblity(false);
               }
               if(mCurrentPage == 1){
               if(nListViewArray != null && nListViewArray.size() > 0)
               {
                 nListViewArray.clear();
               }
               nListViewArray = updateListRecord(jsonObject);
               mCandidateAdapter.loadData(nListViewArray);
               mCandidateAdapter.notifyDataSetChanged();
               currentPage = 2;
               isLoadMore = true;
                }
               else{
                if(nListViewArray != null && nListViewArray.size() > 0){
                    nListViewArray.addAll(updateListRecord(jsonObject));
                    }
                   mCandidateAdapter.initData();

                   if(mTotalPage == 1){
                    // There is only one page.
                   }
                   else{
                   if(currentPage != mTotalPage){
                   currentPage = currentPage + 1;
                   isLoadMore = true;
                   Log.e(TAG, "executecontroller class  else");
                   loadMorePage(currentPage);
                    }else{
                       isLoadMore = false;
                       mCandidateAdapter.setFooterVisiblity(false);
                        }
                    }
                   mCandidateAdapter.notifyDataSetChanged();
                   }
                   recyclerView.setVisibility(View.VISIBLE);
                   tryLaterText.setVisibility(View.GONE);

                 if(isCount){
                     String string = spinnerTitle.getText().toString().trim();
                     String [] split = string.split("-");
                     //int size = nListViewArray.size();
                     //spinnerTitle.setText(split[0].trim()+" - "+size);
                     spinnerTitle.setText(split[0].trim()+" - "+jsonObject.getString("totalapplications"));
                     isCount = false;
                 }
                }
             else if(tryLaterText != null){
                   recyclerView.setVisibility(View.GONE);
                   tryLaterText.setVisibility(View.VISIBLE);
                   tryLaterText.setText(jsonObject.getString("error_msg"));
                   String string = spinnerTitle.getText().toString().trim();
                   String [] split = string.split("-");
                   spinnerTitle.setText(split[0].trim()+" - "+0);
                   isCount = false;
             }
           }
         }
         catch (JSONException e) {
             e.printStackTrace();
         }catch (Exception e){
             e.printStackTrace();
         }
         }
         }
         },
         new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
          swipeRefreshLayout.setRefreshing(false);
          if(mTotalPage != 0 && mCurrentPage <= mTotalPage) {
           showAlertDialog(error);
             }
           }
          }){
            @Override
            protected Map<String,String> getParams(){
                return Utility.params();
            }
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
    }
    private ArrayList<JSONObject> updateListRecord(JSONObject jsonObject){
        ArrayList<JSONObject>  mListViewArray = new ArrayList<>();
        try{
            MySharedPreference.saveKeyValue(Constant.KEY_VIEW_ID, jsonObject.optString("view_cnt"));
            int totalApplications = Integer.parseInt(jsonObject.getString("totalapplications"));
            if (totalApplications > 0){
                JSONArray profileArray;
                profileArray = jsonObject.getJSONArray("jobapplications");
                for (int i = 0; i < profileArray.length(); i++)
                {
                    JSONObject JsonDataObj = profileArray.getJSONObject(i);
                    mListViewArray.add(JsonDataObj);
                }
            }
            else{
                Snackbar snackbar = Snackbar.make(rl_coordinator, getResources().getString(R.string.data_not_message), Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return mListViewArray;
    }
    private void showAlertDialog(final VolleyError error){
        String titleMessage = "";
        String messageText = "";
        if (error instanceof NoConnectionError
                || error instanceof NetworkError) {
            titleMessage = this.getString(R.string.network_lbl);
            messageText = this.getResources().getString(R.string.msg_no_network);
        } else if (error instanceof TimeoutError) {
            titleMessage = this.getString(R.string.retry_lbl);
            messageText = this.getResources().getString(R.string.msg_connection_timout);
        } else {
            titleMessage = this.getString(R.string.error_lbl);
            messageText = this.getResources().getString(R.string.msg_server_error);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleMessage).setMessage(messageText)
                .setCancelable(true)
                .setPositiveButton(this.getString(R.string.ok_lbl), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (error instanceof TimeoutError)
                            loadData();
                    }
                });
        AlertDialog alert = builder.create();
        if (!isFinishing()){
            alert.show();
        }
        alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
    }
    private String getId(final int position){
        String viewId = null;
        try{
            viewId = nListViewArray.get(position).getJSONObject("applicationdetails").getString("id").trim();
        }
        catch (JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return viewId;
    }
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onSingleTapItem(View view, int position) {
        if(nListViewArray != null && nListViewArray.size() > 0){
            if (Utility.isNetworkAvailableRelative(this, rl_coordinator)){
                final String id = getId(position);
                try{
                    if (id != null && !id.equals("")) {
                        Intent ic = new Intent(NewCandidateListActivity.this, ProfileResumeActivity.class);
                        ic.putExtra(Constant.KEY_VIEW_ID, id);
                        ic.putExtra(Constant.KEY_POSITION, position);
                        ic.putExtra("followup",  nListViewArray.get(position).getJSONObject("applicationdetails").getString("followup").toString());
                        ic.putExtra("appstatus", nListViewArray.get(position).getJSONObject("applicationdetails").getString("app_status").toString());
                        ic.putExtra(Constant.KEY_OBJECT, nListViewArray.get(position).toString());
                        ic.putExtra(Constant.KEY_JOB_TITLE, mJobTitle);
                        Bundle b = new Bundle();
                        b.putString("educationArray", nListViewArray.get(position).optJSONArray("educationdetails").toString());
                        b.putString("professiondetails", nListViewArray.get(position).optJSONArray("professiondetails").toString());
                        ic.putExtras(b);
                        startActivity(ic);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onDoubleTapItem(final View view, final int position) {

        if(mPopupLayout.getVisibility() == View.VISIBLE){
            mPopupLayout.setVisibility(View.GONE);
            openCloseIv.setBackgroundResource(R.drawable.down_arrow);
        }
        String string = spinnerTitle.getText().toString().trim();
        String [] split = string.split("-");
        int intValue = Integer.parseInt(split[1].trim());
        int intFinal = intValue-1;
        itemSaved(position);
        nListViewArray.remove(position);
        mCandidateAdapter.notifyItemRemoved(position);
        spinnerTitle.setText(split[0].trim()+" - "+""+intFinal);

        if(split[0].trim().equalsIgnoreCase("Shortlisted")){
            MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE, intFinal);
        }
        else if(split[0].trim().equalsIgnoreCase("Unread")){
            MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE, intFinal);
        }
        else if(split[0].trim().equalsIgnoreCase("Magic Sort")){
            MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_MAGIC_TYPE, intFinal);
        }
        else if(split[0].trim().equalsIgnoreCase("Rejected")){
            MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_REJECT_TYPE, intFinal);

        }
        else if(split[0].trim().equalsIgnoreCase("Saved")){
            MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SAVE_TYPE, intFinal);
        }
    }
    private void itemSaved(int position){
        if(nListViewArray != null && nListViewArray.size() > 0) {
            try {
                Snackbar snackbar;
                String aStatus;
                String strSplit[] = spinnerTitle.getText().toString().trim().split("-");
                String str = strSplit[0].toString().trim();
                if(str.equalsIgnoreCase("Saved") || str == "Saved"){
                    aStatus = "0";
                    snackbar = Snackbar.make(rl_coordinator, nListViewArray.get(position).optJSONObject("applicationdetails").optString("name")+ " marked as unread", Snackbar.LENGTH_SHORT);
                    int nUnread =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE, nUnread);
                    try {
                        strUserId = nListViewArray.get(position).optJSONObject("applicationdetails").optString("userid").toString();
                        strJobId = nListViewArray.get(position).optJSONObject("applicationdetails").optString("jobid").toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AccountUtils.trackEvents("NewCandidateListActivity", "rtMarkasunreadCandidate",
                            "Origin= "+" CALDoubleTap"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                }
                else{
                    aStatus = "3";
                    snackbar = Snackbar.make(rl_coordinator, nListViewArray.get(position).optJSONObject("applicationdetails").optString("name")+ " has been saved", Snackbar.LENGTH_SHORT);
                    int nSaved =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SAVE_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SAVE_TYPE, nSaved);
                    try {
                        strUserId = nListViewArray.get(position).optJSONObject("applicationdetails").optString("userid").toString();
                        strJobId = nListViewArray.get(position).optJSONObject("applicationdetails").optString("jobid").toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AccountUtils.trackEvents("NewCandidateListActivity", "rtSaveCandidate",
                            "Origin= "+" CALDoubleTap"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                }
                snackbar.show();
                String followup = nListViewArray.get(position).getJSONObject("applicationdetails").getString("followup");
                changeRecruiterAction(getId(position), aStatus, followup);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void changeRecruiterAction(String temPosi, String aStatus, String followup){

        if (Utility.isNetworkAvailableRelative(this, rl_coordinator)){
            String url =  Constant.REC_ACTION_URL + temPosi + "/" + aStatus +"/" +followup+"/"+Constant.USER_COOKIE;
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
                            } else{
                                Snackbar snackbar = Snackbar.make(rl_coordinator, iModel.getError_msg(), Snackbar.LENGTH_SHORT);
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
             headers.put("User-agent", System.getProperty("http.agent")+" "+getString(R.string.app_name)+" "+Utility.getAppVersionName());
             headers.put("AUTHKEY", ""+MySharedPreference.getKeyValue(Constant.KEY_USER_AUTH_KEY));
             return headers;
             }
            };
            RetryPolicy policy = new DefaultRetryPolicy(Constant.SOCKET_TIMEOUT_DURATION, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1){
            isAllCandidateFilter = true;
            isCount = true;
        }
    }

    private void showDetailCoachMark()
    {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.jobdetail_coachmark, null);
        dialog.setContentView(layout);
        imageNext = (ImageView) dialog.findViewById(R.id.imageView_prevnext);
        rl_parent = (RelativeLayout) dialog.findViewById(R.id.rl_parent);
        imageNext.setImageResource(R.drawable.right_swipe);
        imageNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count ==1){
                    imageNext.setImageResource(R.drawable.left_swipe);
                }
                if(count ==2){
                    imageNext.setImageResource(R.drawable.double_tap);

                }
                if(count ==3){
                    AccountUtils.setBooleanCoachMark(true);
                    dialog.dismiss();
                }
            }
        });
        rl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count ==1){
                    imageNext.setImageResource(R.drawable.left_swipe);
                }
                if(count ==2){
                    imageNext.setImageResource(R.drawable.double_tap);

                }
                if(count ==3){
                    AccountUtils.setBooleanCoachMark(true);
                    dialog.dismiss();
                }
            }
        });
        dialog. show();
    }
}