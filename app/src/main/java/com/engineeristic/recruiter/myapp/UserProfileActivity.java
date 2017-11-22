package com.engineeristic.recruiter.myapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.URLUtil;
import android.widget.ImageView;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.adapter.CropingOptionAdapter;
import com.engineeristic.recruiter.model.ProfileModel;
import com.engineeristic.recruiter.model.UserProfileModel;
import com.engineeristic.recruiter.util.AccountUtils;
import com.engineeristic.recruiter.util.CompressPostedImage;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.ConstantFontelloID;
import com.engineeristic.recruiter.util.CropingOption;
import com.engineeristic.recruiter.util.GraphicalUtil;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.UploadImage;
import com.engineeristic.recruiter.util.UploadListener;
import com.engineeristic.recruiter.util.Utility;
import com.engineeristic.recruiter.widget.CircleImageView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener,UploadListener{

    //private TextView editProfileTextView;
    //private TextView facebookIv, twiteerIv, linkedInIv;
    private ImageView editImageIv;//instantRadioIv, dailyRadioIv
    private CircleImageView userImageView;
    //private ProgressDialog pDialog;
    private TextView userNameText, designationText
            , viewdText, lastLoginText, emailIdText, phoneNumberText
            , typeText, locationText, aboutDetailsText;
    private UserProfileModel iUserModel = null;
    private final int REQUEST_CAMERA = 0, REQUEST_GALLERY_IMAGE = 1;
    private final int REQUEST_CROP_CAMERA_IMAGE = 2;
    private final int REQUEST_CROP_GALLERY_IMAGE = 3;
    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private String currentPicturePath;
    private String filePath;
    private ImageLoader imageLoader;
    private Uri mImageCaptureUri;
    private File outPutFile = null;
    private CoordinatorLayout llParent;
    private TextView instant_text;
    private TextView txt_iv_viewed,txt_iv_lastlogin,txt_iv_email,txt_iv_phone;
    private TextView txt_iv_type, txt_iv_location,txt_iv_notification;
    private Typeface typeRobotoMedium, typeRobotoRegular, typeFontello;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.user_profile_layout);
        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        AccountUtils.trackerScreen("My Profile");
        imageLoader = RecruiterApplication.getApplication().getImageLoader();
        typeRobotoRegular = Typeface.createFromAsset(getApplicationContext().getAssets(),"robotoregular.ttf");
        typeRobotoMedium = Typeface.createFromAsset(getApplicationContext().getAssets(),"robotomedium.ttf");
        typeFontello = Typeface.createFromAsset(getApplicationContext().getAssets(),"fontello.ttf");

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_userprofile);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation((float)10.0);
        }*/
        setSupportActionBar(toolbar);
        /*toolbar.setTitleTextColor(getResources().getColor(R.color.title_text_clr));
        Spannable text = new SpannableString("Profile");
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.title_text_clr)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);*/

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.toolbar_backicon_clr), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("Profile");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transperent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.title_text_clr));
        //editProfileTextView = (TextView) findViewById(R.id.edit_profile_cmd);
        txt_iv_viewed = (TextView)findViewById(R.id.txt_iv_viewed);
        txt_iv_lastlogin = (TextView)findViewById(R.id.txt_iv_lastlogin);
        txt_iv_email = (TextView)findViewById(R.id.txt_iv_email);
        txt_iv_phone = (TextView)findViewById(R.id.txt_iv_phone);
        txt_iv_type = (TextView)findViewById(R.id.txt_iv_type);
        txt_iv_location = (TextView)findViewById(R.id.txt_iv_location);
        txt_iv_notification = (TextView)findViewById(R.id.txt_iv_notification);
        txt_iv_viewed.setTypeface(typeFontello);
        txt_iv_lastlogin.setTypeface(typeFontello);
        txt_iv_email.setTypeface(typeFontello);
        txt_iv_phone.setTypeface(typeFontello);
        txt_iv_type.setTypeface(typeFontello);
        txt_iv_location.setTypeface(typeFontello);
        txt_iv_notification.setTypeface(typeFontello);
        txt_iv_viewed.setText(ConstantFontelloID.ICON_EYE);
        txt_iv_lastlogin.setText(ConstantFontelloID.ICON_LOGIN);
        txt_iv_email.setText(ConstantFontelloID.ICON_ENVELOPE);
        txt_iv_phone.setText(ConstantFontelloID.ICON_PHONE);
        txt_iv_type.setText(ConstantFontelloID.ICON_TYPE);
        txt_iv_location.setText(ConstantFontelloID.ICON_LOCATION_PROFILE);
        txt_iv_notification.setText(ConstantFontelloID.icon_notification_bellfill);

        instant_text = (TextView)findViewById(R.id.instant_text);
        llParent = (CoordinatorLayout) findViewById(R.id.ll_parent_userprofile);
        /*facebookIv = (TextView) findViewById(R.id.facebook_iv);
        twiteerIv = (TextView) findViewById(R.id.twitter_iv);
        linkedInIv = (TextView) findViewById(R.id.linked_iv);*/
        //instantRadioIv = (ImageView) findViewById(R.id.radio_instant);
        //dailyRadioIv = (ImageView) findViewById(R.id.radio_daily);
        editImageIv = (ImageView) findViewById(R.id.edit_iv);
        userImageView = (CircleImageView) findViewById(R.id.id_profile_pic);
        userNameText = (TextView)findViewById(R.id.username_text);
        //orgText = (TextView)findViewById(R.id.employer_text);
        designationText = (TextView)findViewById(R.id.designation_text);
        viewdText = (TextView)findViewById(R.id.view_count_text);
        lastLoginText = (TextView)findViewById(R.id.last_login_text);
        emailIdText = (TextView)findViewById(R.id.email_text);
        phoneNumberText = (TextView)findViewById(R.id.pnumber_text);
        typeText = (TextView)findViewById(R.id.type_text);
        locationText = (TextView)findViewById(R.id.location_text);
        aboutDetailsText = (TextView)findViewById(R.id.about_text);
       // editProfileTextView.setOnClickListener(this);

        /*facebookIv.setTypeface(typeFontello);
        twiteerIv.setTypeface(typeFontello);
        linkedInIv.setTypeface(typeFontello);
        facebookIv.setText(ConstantFontelloID.ICON_FACEBOOK);
        twiteerIv.setText(ConstantFontelloID.ICON_TWITTER);
        linkedInIv.setText(ConstantFontelloID.ICON_LINKEDIN);
        facebookIv.setOnClickListener(this);
        twiteerIv.setOnClickListener(this);
        linkedInIv.setOnClickListener(this);*/

        editImageIv.setOnClickListener(this);
        userImageView.setOnClickListener(this);
        if(Utility.isNetworkAvailableCordinator(UserProfileActivity.this, llParent)){
            loadProfile();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_user_profile:
                if(iUserModel != null){
                    Intent intent = new Intent(this, EditProfileActivity.class);
                    intent.putExtra(Constant.KEY_PROFILE_MODEL, iUserModel);
                    startActivity(intent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.facebook_iv:
            case R.id.twitter_iv:
            case R.id.linked_iv:
                if(v.getTag() != null)
                    RotateAnimation(v, 500);
                break;*/
            case R.id.id_profile_pic:
            case R.id.edit_iv:
                selectImageOption();
                break;
            default:
                break;
        }
    }
    private void selectImageOption() {
        final CharSequence[] items = { getResources().getString(R.string.cap_photo_lbl)
                , getResources().getString(R.string.choose_gallery_lbl), getResources().getString(R.string.cancel_lbl) };

        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle(getResources().getString(R.string.add_photo_lbl));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getResources().getString(R.string.cap_photo_lbl))) {
                    captureImageFromCamera();
                } else if (items[item].equals(getResources().getString(R.string.choose_gallery_lbl))) {
                    openGalleryImage();
                } else if (items[item].equals(getResources().getString(R.string.cancel_lbl))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void RotateAnimation(final View view, long durationMillis){
        final RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // Step 2:  Set the Animation properties
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.ABSOLUTE);
        anim.setDuration(durationMillis);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                openWebUrl((String)view.getTag());
            }
        });
        view.startAnimation(anim);
    }
    private void openWebUrl(String webUrl){
        if(webUrl != null
                && !webUrl.equals("")
                && URLUtil.isValidUrl(webUrl)){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(webUrl));
            startActivity(i);
        }
    }
    private void captureImageFromCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File image = Utility.generatePicturePath();//new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
        mImageCaptureUri = Uri.fromFile(image);
        currentPicturePath = image.getAbsolutePath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void openGalleryImage(){
        final PackageManager packageManager = getApplicationContext()
                .getPackageManager();
        Intent intentBrowse = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        List<ResolveInfo> list1 = packageManager
                .queryIntentActivities(
                        intentBrowse,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (list1.size() > 0) {
            startActivityForResult(intentBrowse, REQUEST_GALLERY_IMAGE);
        }
    }
    private void CropingIMG() {

        final ArrayList<CropingOption> cropOptions = new ArrayList<CropingOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
        int size = list.size();
        if (size == 0) {
            Snackbar snackbar = Snackbar.make(llParent, "Cann't find image croping app", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        } else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 256);
            intent.putExtra("outputY", 256);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            //TODO: don't use return-data tag because it's not return large image data and crash not given any message
            //intent.putExtra("return-data", true);
            //Create output file here
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));

            if (size == 1) {
                Intent i   = new Intent(intent);
                ResolveInfo res = (ResolveInfo) list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, REQUEST_CROP_CAMERA_IMAGE);
            } else {
                for (ResolveInfo res : list) {
                    final CropingOption co = new CropingOption();

                    co.title  = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon  = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);
                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropingOptionAdapter adapter = new CropingOptionAdapter(getApplicationContext(), cropOptions);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Croping App");
                builder.setCancelable(false);
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int item ) {
                        startActivityForResult( cropOptions.get(item).appIntent, REQUEST_CROP_CAMERA_IMAGE);
                    }
                });

                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel( DialogInterface dialog ) {

                        if (mImageCaptureUri != null ) {
                            getContentResolver().delete(mImageCaptureUri, null, null );
                            mImageCaptureUri = null;
                        }
                    }
                } );

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        switch (requestCode) {

            case REQUEST_CAMERA:
                System.out.println("Camera Image URI : "+mImageCaptureUri);
                CropingIMG();

                break;
            case REQUEST_GALLERY_IMAGE: {
                if (data != null) {

                    Uri imageUri = data.getData();
                    filePath = Utility.getPath(imageUri, UserProfileActivity.this);
                    CompressPostedImage compress = new CompressPostedImage(UserProfileActivity.this);
                    filePath = compress.compressImage(imageUri.toString());
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setType("image/*");
                    intent.setData(imageUri); // Uri to the image you want to crop
                    intent.putExtra("outputX", 256);
                    intent.putExtra("outputY",256);
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("scale", true);
                    intent.putExtra("crop", "true");
                    intent.putExtra("circleCrop", new String(""));
                    intent.putExtra("return-data", true);
                    File cropImageFile = new File(filePath); // Path to save the cropped image
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropImageFile));
                    startActivityForResult(intent, REQUEST_CROP_GALLERY_IMAGE);
                }
            }
            break;
            case REQUEST_CROP_GALLERY_IMAGE: {

                if (data.getData() != null) {

                    InputStream image_stream = null;
                    try {
                        image_stream = getContentResolver().openInputStream(data.getData());
                        Bitmap bitmap= BitmapFactory.decodeStream(image_stream);

                        GraphicalUtil graphicUtil = new GraphicalUtil();
                        userImageView.setImageBitmap(graphicUtil.getCircleBitmap(bitmap, 16));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    uploadProfilePic(filePath);
                }

            }
            break;

            case REQUEST_CROP_CAMERA_IMAGE: {

                try {
                    if(outPutFile.exists()){
                        Bitmap photo = Utility.decodeFile(outPutFile);
                        userImageView.setImageBitmap(Utility.getRoundedShape(photo));
                        uploadProfilePic(outPutFile.getAbsolutePath());
                    }
                    else {
                        Snackbar snackbar = Snackbar.make(llParent, "Error while save image", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;
        }
    }
    private void uploadProfilePic(String picPath){

        if(Constant.USER_COOKIE != null){
        if (Utility.isNetworkAvailableCordinator(UserProfileActivity.this, llParent)){
            /*pDialog = new ProgressDialog(UserProfileActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();*/
            UploadImage uploadImage = new UploadImage(UserProfileActivity.this);
            uploadImage.execute(Constant.PIC_UPLOAD_URL+Constant.USER_COOKIE, picPath);
        }
       }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(Constant.isRefreshUserProfile){
            loadProfile();
        }
        if (ContextCompat.checkSelfPermission(UserProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }
    }
    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar snackbar = Snackbar.make(llParent, "Permission granted.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(llParent, "Permission denied.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                break;
            }
        }
    }
    private void loadProfile(){
        Utility.showLoadingDialog(UserProfileActivity.this);
        String url = Constant.REC_PROFILE_URL + Constant.USER_COOKIE;
        if (Utility.isNetworkAvailableCordinator(UserProfileActivity.this, llParent)){
            RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Utility.dismisLoadingDialog();
                    if (response != null) {
                        Gson mGson = new GsonBuilder().create();
                        iUserModel = mGson.fromJson(response, UserProfileModel.class);
                        if (iUserModel != null) {
                            if(iUserModel.getStatus().equalsIgnoreCase(Constant.HTTP_OK))
                                updateGUI(response, iUserModel.getProfile());
                            else{
                                Snackbar snackbar = Snackbar.make(llParent, iUserModel.getError_msg(), Snackbar.LENGTH_SHORT);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                                builder.setTitle(titleMessage).setMessage(messageText)
                                        .setCancelable(true)
                                        .setPositiveButton(getString(R.string.ok_lbl), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                                if (error instanceof TimeoutError)
                                                    loadProfile();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);

                            }
                        }
                    }
            ){
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
    private void updateGUI(String responce, ProfileModel iModel){
        userNameText.setText(iModel.getRecname());
        designationText.setText(iModel.getDesignation()+" at "+iModel.getOrganisation());
        //orgText.setText(iModel.getOrganisation());
        viewdText.setText(iModel.getViews()+" times");
        lastLoginText.setText(iModel.getLast_login());
        emailIdText.setText(iModel.getEmail());
        phoneNumberText.setText(iModel.getPhone());
        typeText.setText(iModel.getType_name());
        locationText.setText(iModel.getLocation_name());
        aboutDetailsText.setText(iModel.getAbout());

        /*String strFacebookURL = iModel.getFurl();
        String strTwitterURL = iModel.getTurl();
        String strLinkedinURL = iModel.getLurl();
        if(strFacebookURL.equals("") || strFacebookURL.equals(null) || "" == strFacebookURL || null == strFacebookURL){
            facebookIv.setVisibility(View.GONE);
        }
        else{
            facebookIv.setTag(iModel.getFurl());
        }
        if(strTwitterURL.equals("") || strTwitterURL.equals(null) || "" == strTwitterURL || null == strTwitterURL){
            twiteerIv.setVisibility(View.GONE);
        }
        else{
            twiteerIv.setTag(iModel.getTurl());
        }
        if(strLinkedinURL.equals("") || strLinkedinURL.equals(null) || "" == strLinkedinURL || null == strLinkedinURL){
            linkedInIv.setVisibility(View.GONE);
        }
        else{
            linkedInIv.setTag(iModel.getLurl());
        }*/
        if(iModel.getNotification_type().equals("1")
                && iModel.getNotification_type_text().equals("Instant")){
            instant_text.setText("Instant");
            //instantRadioIv.setBackgroundResource(R.drawable.profile_notification_sel);
            //dailyRadioIv.setBackgroundResource(R.drawable.profile_notification_unsel);
        }else{
            //instantRadioIv.setBackgroundResource(R.drawable.profile_notification_unsel);
            //dailyRadioIv.setBackgroundResource(R.drawable.profile_notification_sel);
            instant_text.setText("Daily digest mail");
        }

        userNameText.setVisibility(View.VISIBLE);
        designationText.setVisibility(View.VISIBLE);
        //orgText.setVisibility(View.VISIBLE);
        viewdText.setVisibility(View.VISIBLE);
        lastLoginText.setVisibility(View.VISIBLE);
        emailIdText.setVisibility(View.VISIBLE);
        phoneNumberText.setVisibility(View.VISIBLE);
        typeText.setVisibility(View.VISIBLE);
        locationText.setVisibility(View.VISIBLE);
        aboutDetailsText.setVisibility(View.VISIBLE);

        MySharedPreference.saveKeyValue(Constant.KEY_USER_NAME, iModel.getRecname());
        MySharedPreference.saveKeyValue(Constant.KEY_USER_EMAIL, iModel.getEmail());
        MySharedPreference.saveKeyValue(Constant.KEY_USER_IMAGE, iModel.getImage());
        userImageView.setImageUrl(iModel.getImage(), imageLoader);
    }

    public void UploadSuccess(JSONObject jsonObject){
        /*if ( pDialog != null && pDialog.isShowing()){
            pDialog.dismiss();
        }*/
        if(jsonObject != null) {
            userImageView.setImageUrl(jsonObject.optString("image"), imageLoader);
            MySharedPreference.saveKeyValue(Constant.KEY_USER_IMAGE, jsonObject.optString("image"));
            Snackbar snackbar = Snackbar.make(llParent, jsonObject.optString("success_msg"), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }
    public void UploadFails(final String errorMessage){
        /*if ( pDialog != null && pDialog.isShowing()){
            pDialog.dismiss();
        }*/
        if(errorMessage != null
                && !errorMessage.equals("")){
            String titleMessage = getString(R.string.app_name);
            if(errorMessage.equalsIgnoreCase(Constant.MSG_TIME_OUT)){
                titleMessage = getString(R.string.retry_lbl);
            }else if(errorMessage.equalsIgnoreCase(Constant.MSG_NO_NETWORK)){
                titleMessage = getString(R.string.network_lbl);
            }else if(errorMessage.equalsIgnoreCase(Constant.MSG_SERVER_ERROR)){
                titleMessage = getString(R.string.error_lbl);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(titleMessage).setMessage(errorMessage)
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.ok_lbl), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(errorMessage.equalsIgnoreCase(Constant.MSG_TIME_OUT)){
                                uploadProfilePic(currentPicturePath);
                            }
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
        }
    }
}
