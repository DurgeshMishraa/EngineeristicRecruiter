package com.engineeristic.recruiter.myapp;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.model.JobTitlePojo;
import com.engineeristic.recruiter.util.ConstantFontelloID;
import com.engineeristic.recruiter.util.Utility;

/**
 * Created by h1 on 24-Aug-17.
 */

public class DownloadResumeActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private WebView webView;
    private String url, mStrCandidateName, mStrJobTitle;
    private TextView titleText, pBackTextView;
    private Typeface typeRobotoMedium, typeRobotoRegular, typeFontello;
    private RelativeLayout mParent_Rl;
    private TextView  mTxtIcon_Refresh;//, ttl_text_sharedownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_download_resume);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        typeFontello = Typeface.createFromAsset(getAssets(), "fontello.ttf");
        typeRobotoRegular = Typeface.createFromAsset(getAssets(),"robotoregular.ttf");
        typeRobotoMedium = Typeface.createFromAsset(getAssets(),"robotomedium.ttf");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_downloadresume);
        //toolbar.setTitleTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation((float)10.0);
        }
        mTxtIcon_Refresh = (TextView) findViewById(R.id.ttl_text_refreshdownload);
        //ttl_text_sharedownload = (TextView) findViewById(R.id.ttl_text_sharedownload);
        mTxtIcon_Refresh.setTypeface(typeFontello);
        //ttl_text_sharedownload.setTypeface(typeFontello);
        mTxtIcon_Refresh.setText(ConstantFontelloID.ICON_REFRESH);
        //ttl_text_sharedownload.setText(ConstantFontelloID.ICON_NAV_SHARE);
        //ttl_text_sharedownload.setOnClickListener(this);
        pBackTextView = (TextView) findViewById(R.id.back_cmd_downloadresume);
        titleText = (TextView)findViewById(R.id.ttl_text_downloadresume);
        mParent_Rl = (RelativeLayout)findViewById(R.id.rl_parent_downloadresume);
        titleText.setTypeface(typeRobotoMedium);
        pBackTextView.setTypeface(typeFontello);
        pBackTextView.setText(ConstantFontelloID.icon_back_icon);
        pBackTextView.setOnClickListener(this);
        mStrCandidateName = getIntent().getExtras().getString("CANDIDATE_NAME");
        titleText.setText(mStrCandidateName+"'s Resume");
        webView =(WebView)findViewById(R.id.webView);
        progressBar =(ProgressBar)findViewById(R.id.firstBar);
        url = getIntent().getExtras().getString("DOWNLOAD_URL");
        webView.setScrollContainer(false);
        webView.setWebViewClient(new MyWebViewClient());
        webView .getSettings().setJavaScriptEnabled(true);
        webView .getSettings().setDomStorageEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        //webView.setScrollbarFadingEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        if (Utility.isNetworkAvailableRelative(this, mParent_Rl)){
            webView.loadUrl("http://docs.google.com/gview?embedded=true&url="+url);
        }
        mTxtIcon_Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isNetworkAvailableRelative(DownloadResumeActivity.this, mParent_Rl)){
                    webView.loadUrl("http://docs.google.com/gview?embedded=true&url="+url);
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else{
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });


        mStrJobTitle = JobTitlePojo.getJobtitle();
    }

    public class MyWebViewClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView .getSettings().setJavaScriptEnabled(true);
            webView .getSettings().setDomStorageEnabled(true);
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_cmd_downloadresume:
                finish();
                break;

            /*case R.id.ttl_text_sharedownload:
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                String myFilePath = Environment.getExternalStorageDirectory() + "/Download/"+this.getResources().getString(R.string.app_name)+"/" + mStrCandidateName + ".pdf";

                File fileWithinMyDir = new File(myFilePath);
                if(fileWithinMyDir.exists()) {
                    intentShareFile.setType("application/pdf");
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+myFilePath));
                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "engineeristic.com | "+mStrCandidateName+"'s Resume");
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Hi\n"+"Please find attached "+mStrCandidateName+"'s Resume who has applied for the position - "+mStrJobTitle+".");
                    startActivity(Intent.createChooser(intentShareFile, "Share File"));
                }
                break;*/
            default:
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
