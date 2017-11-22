package com.engineeristic.recruiter.myapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.chat.ChatActivity;
import com.engineeristic.recruiter.chat.ChatConstants;
import com.engineeristic.recruiter.util.AccountUtils;
import com.engineeristic.recruiter.util.ConstantFontelloID;
import com.engineeristic.recruiter.util.Utility;

/**
 * Created by h1 on 12-Jul-17.
 */

public class ContactInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTxtChat, mTxtEmail, mTxtCall,
            mTxtEmailId, mTxtContactNumber;
    private Typeface fontello;
    private String mStrJobId, mStrEmail,
            mStrContactNumber, mStrJobTitle;
    private String strJobId, strUserId, strOrigin;
    private RelativeLayout rlParent, rl_send_chat_msg, rl_email, rl_phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.contact_information);
        RecruiterApplication application = (RecruiterApplication) getApplication();
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        Bundle bundle = getIntent().getExtras();
        mStrJobId = bundle.getString("USER_ID");
        mStrEmail = bundle.getString("EMAIL");
        mStrContactNumber = bundle.getString("CONTACT_NUMBER");
        mStrJobTitle  = bundle.getString("JOB_TITLE");
        strOrigin =  bundle.getString("ORIGIN_CONTACT_INFORMATION");
        strJobId =  bundle.getString("JOB_ID");
        strUserId =  bundle.getString("USER_ID");
        AccountUtils.trackEvents("ContactInformationActivity", "rtViewContactInformation",
                "Origin= "+strOrigin+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_contact_information);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation((float)10.0);
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.title_text_clr));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.toolbar_backicon_clr), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("Contact Information");
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rlParent = (RelativeLayout)findViewById(R.id.rl_parent_contactinformation);

        rl_send_chat_msg = (RelativeLayout)findViewById(R.id.rl_send_chat_msg);
        rl_email = (RelativeLayout)findViewById(R.id.rl_email);
        rl_phone = (RelativeLayout)findViewById(R.id.rl_phone);

        rl_send_chat_msg.setOnClickListener(this);
        rl_email.setOnClickListener(this);
        rl_phone.setOnClickListener(this);

        mTxtChat = (TextView)findViewById(R.id.txt_chaticon_contactinfo);
        mTxtEmail = (TextView)findViewById(R.id.txt_emailicon_contactinfo);
        mTxtCall = (TextView)findViewById(R.id.txt_phoneicon_contactinfo);
        mTxtEmailId = (TextView)findViewById(R.id.txt_email_value_contactinfo);
        mTxtContactNumber = (TextView)findViewById(R.id.txt_phone_value_contactinfo);
        if(mStrEmail!= null)
        {
            mTxtEmailId.setText(mStrEmail);
        }
        if(mStrContactNumber!= null)
        {
            mTxtContactNumber.setText(mStrContactNumber);
        }
        fontello = Typeface.createFromAsset(getApplicationContext().getAssets(),"fontello.ttf");
        mTxtChat.setTypeface(fontello);
        mTxtEmail.setTypeface(fontello);
        mTxtCall.setTypeface(fontello);
        mTxtChat.setText(ConstantFontelloID.icon_speech_bubble);
        mTxtEmail.setText(ConstantFontelloID.icon_email_outline);
        mTxtCall.setText(ConstantFontelloID.icon_call);
        mTxtChat.setOnClickListener(this);
        mTxtEmail.setOnClickListener(this);
        mTxtCall.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int Id = v.getId();
        switch (Id) {
            case R.id.rl_send_chat_msg:
                if(mStrJobId != null){
                    AccountUtils.trackEvents("ContactInformationActivity", "rtStartChat",
                            "Origin= "+"ContactInfo"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                    Intent myIntent = new Intent(ContactInformationActivity.this, ChatActivity.class);
                    myIntent.putExtra(ChatConstants.JOBID_FOR_CHAT, mStrJobId);
                    startActivity(myIntent);
                }
                break;
            case R.id.rl_email:
                if (Utility.isNetworkAvailableRelative(this, rlParent)){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:" + mStrEmail + "?subject=" + mStrJobTitle);
                intent.setData(data);
                startActivity(intent);
                }
                break;
            case R.id.rl_phone:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                try{
                    callIntent.setData(Uri.parse("tel:" + mStrContactNumber));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                startActivity(callIntent);
                break;

            case R.id.txt_chaticon_contactinfo:
                if(mStrJobId != null){
                    AccountUtils.trackEvents("ContactInformationActivity", "rtStartChat",
                            "Origin= "+"ContactInfo"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                    Intent myIntent = new Intent(ContactInformationActivity.this, ChatActivity.class);
                    myIntent.putExtra(ChatConstants.JOBID_FOR_CHAT, mStrJobId);
                    startActivity(myIntent);
                }
                break;
            case R.id.txt_emailicon_contactinfo:
                if (Utility.isNetworkAvailableRelative(this, rlParent)){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:" + mStrEmail + "?subject=" + mStrJobTitle);
                    intent.setData(data);
                    startActivity(intent);
                }
                break;
            case R.id.txt_phoneicon_contactinfo:
                Intent callIntents = new Intent(Intent.ACTION_CALL);
                try{
                    callIntents.setData(Uri.parse("tel:" + mStrContactNumber));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                startActivity(callIntents);
                break;


            default:
                break;
        }
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
        //return super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
