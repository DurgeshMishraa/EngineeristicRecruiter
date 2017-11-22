package com.engineeristic.recruiter.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.engineeristic.recruiter.chat.ChatActivity;
import com.engineeristic.recruiter.chat.ChatConstants;
import com.engineeristic.recruiter.helper.ItemTouchHelperAdapter;
import com.engineeristic.recruiter.helper.ItemTouchHelperViewHolder;
import com.engineeristic.recruiter.model.CoverLatterModel;
import com.engineeristic.recruiter.model.PostCommentModel;
import com.engineeristic.recruiter.myapp.CoverLetterActivity;
import com.engineeristic.recruiter.myapp.DownloadResumeActivity;
import com.engineeristic.recruiter.myapp.NewCandidateListActivity;
import com.engineeristic.recruiter.myapp.ProfileResumeActivity;
import com.engineeristic.recruiter.myapp.RecruiterApplication;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewCandidateListAdapter extends CusFooterAdapter<JSONObject> implements ItemTouchHelperAdapter{
    private byte PROFILE_TYPE = -1;
    private String mJobTitle = null;
    private String appStatus = "";
    private String aStatus = "";
    private String followup = "";
    private String temPosi = "";
    private byte aType = -1;
    private List<JSONObject> rowItemMyProfile ;
    private Context mContext;
    private android.app.AlertDialog alertD=null;
    private int idComment;
    private EditText userInput;
    private String userResume = "";
    private com.nostra13.universalimageloader.core.ImageLoader loader;
    private DisplayImageOptions options;
    private Typeface typeRobotoMedium, typeRobotoRegular, typeFontello;
    private RelativeLayout rlParent;
    private TextView txt1, txt2, txt3, spinnerTitle;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int nSaved, nRejected, nShortListed, nUnread, nMagicSort;
    private String strUserId = "";
    private String strJobId = "";
    private CardView mPopupLayout;
    private ImageView openCloseIv;



    public NewCandidateListAdapter(Context nContext, RelativeLayout rlParam, TextView spinnerTitleParam,
                                   SwipeRefreshLayout swipeRefreshLayoutParam, CardView popupLayoutParam, ImageView openCloseIvParam) {
        // With Header & With Footer
        super(null, false, false);
        this.mContext = nContext;
        this.rlParent = rlParam;
        this.spinnerTitle = spinnerTitleParam;
        this.swipeRefreshLayout = swipeRefreshLayoutParam;
        this.mPopupLayout = popupLayoutParam;
        this.openCloseIv = openCloseIvParam;
        typeRobotoRegular = Typeface.createFromAsset(mContext.getAssets(),"robotoregular.ttf");
        typeRobotoMedium = Typeface.createFromAsset(mContext.getAssets(),"robotomedium.ttf");
        typeFontello = Typeface.createFromAsset(mContext.getAssets(),"fontello.ttf");
        loaderImage();

        nSaved = MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SAVE_TYPE);
        nMagicSort = MySharedPreference.getRecruiterViewType(Constant.RECRUITER_MAGIC_TYPE);
        nRejected = MySharedPreference.getRecruiterViewType(Constant.RECRUITER_REJECT_TYPE);
        nShortListed = MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE);
        nUnread = MySharedPreference.getRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE);


    }
    private void loaderImage(){
        try {
            options = new DisplayImageOptions.Builder()
                    .displayer(new RoundedBitmapDisplayer(90))
                    .showImageOnLoading(R.drawable.img)
                    .showImageForEmptyUri(R.drawable.img)
                    .showImageOnFail(R.drawable.img)
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    mContext).defaultDisplayImageOptions(options).build();
            loader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
            loader.init(config);
        } catch (Exception e) {
            Log.e("NewCandidateListAdapter", "loaderImage() "+e.getMessage());
        }
    }
    public void initData(){
        super.loadData(rowItemMyProfile);
    }
    public void removeItem(int mPosition){
        if(rowItemMyProfile != null && rowItemMyProfile.size() > 0 && rowItemMyProfile.get(mPosition) != null)
            rowItemMyProfile.remove(mPosition);
    }
    public void loadData(List<JSONObject> data){
        this.rowItemMyProfile = data;
        super.loadData(data);
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{
        protected LinearLayout llWorkExp;
        protected LinearLayout llEduExp;
        private View workExpSepView;
        protected TextView ivProfile;
        protected TextView headingWorkexp;
        protected TextView headingExpqualification;
        protected TextView tvProfileName;
        protected TextView tvExp;
        protected TextView tvLocation;
        protected TextView tvJobDate;
        protected TextView tvCompanyName;
        protected TextView tvDesignation;
        protected TextView tvCompExp;
        protected TextView tvQualification;
        protected TextView tvInstitute;
        protected TextView tvCoursePeriod;
        protected LinearLayout ll_profilename;
        protected ImageView ivSeerkar;
        protected TextView informationIv;
        protected Button resumeButton, coverLetterButton;
        protected LinearLayout ll_resume_newcandidate, ll_cover_newcandidate;
        protected TextView txtResumeIcon, txtCoverIcon, txt_location_icon, txt_clock_icon;
        protected RelativeLayout mParentRl;


        public CustomViewHolder(View convertView) {
            super(convertView);
            mParentRl = (RelativeLayout) convertView.findViewById(R.id.rl_jobCompanyDetails);
            headingWorkexp = (TextView) convertView.findViewById(R.id.wrkExperience);
            headingExpqualification = (TextView) convertView.findViewById(R.id.expQualification);
            ll_profilename = (LinearLayout) convertView.findViewById(R.id.ll_profilename);
            llWorkExp = (LinearLayout) convertView.findViewById(R.id.ll_work_exp);
            workExpSepView = (View) convertView.findViewById(R.id.saperator_location);
            llEduExp = (LinearLayout) convertView.findViewById(R.id.ll_edu_exp);
            ivProfile = (TextView) convertView.findViewById(R.id.iv_profilename);
            tvProfileName = (TextView) convertView.findViewById(R.id.tv_profilename);
            tvExp = (TextView) convertView.findViewById(R.id.tv_exp);
            tvLocation = (TextView) convertView.findViewById(R.id.tv_location);
            tvJobDate = (TextView) convertView.findViewById(R.id.tv_date);
            tvCompanyName = (TextView) convertView.findViewById(R.id.tv_companyname);
            tvDesignation = (TextView) convertView.findViewById(R.id.tv_designation);
            tvCompExp = (TextView) convertView.findViewById(R.id.tv_exptime);
            tvInstitute = (TextView) convertView.findViewById(R.id.tv_instituteName);
            tvQualification = (TextView) convertView.findViewById(R.id.tv_degree);
            tvCoursePeriod = (TextView) convertView.findViewById(R.id.tv_edu_period);
            ivSeerkar = (ImageView) convertView.findViewById(R.id.seekar_iv);
            informationIv = (TextView) convertView.findViewById(R.id.info_iv);
            resumeButton = (Button) convertView.findViewById(R.id.resume_bt);
            ll_resume_newcandidate = (LinearLayout)convertView.findViewById(R.id.ll_resume_newcandidate);
            coverLetterButton = (Button) convertView.findViewById(R.id.cover_letter_bt);
            ll_cover_newcandidate = (LinearLayout)convertView.findViewById(R.id.ll_cover_newcandidate);
            txtResumeIcon = (TextView) convertView.findViewById(R.id.txt_resume_icon);
            txtCoverIcon = (TextView) convertView.findViewById(R.id.txt_cover_icon);
            txt_clock_icon = (TextView) convertView.findViewById(R.id.txt_clock_icon);
            txt_location_icon = (TextView) convertView.findViewById(R.id.txt_location_icon);
            txt_location_icon.setTypeface(typeFontello);
            txt_clock_icon.setTypeface(typeFontello);
            tvProfileName.setTypeface(typeRobotoRegular);
            tvExp.setTypeface(typeRobotoRegular);
            tvLocation.setTypeface(typeRobotoRegular);
            tvJobDate.setTypeface(typeRobotoRegular);
            tvCompanyName.setTypeface(typeRobotoRegular);
            tvDesignation.setTypeface(typeRobotoRegular);
            tvCompExp.setTypeface(typeRobotoRegular);
            tvInstitute.setTypeface(typeRobotoRegular);
            tvQualification.setTypeface(typeRobotoRegular);
            tvCoursePeriod.setTypeface(typeRobotoRegular);
            resumeButton.setTypeface(typeRobotoRegular);
            coverLetterButton.setTypeface(typeRobotoRegular);
            txtResumeIcon.setTypeface(typeFontello);
            txtCoverIcon.setTypeface(typeFontello);
            txtResumeIcon.setText(ConstantFontelloID.icon_resume);
            txtCoverIcon.setText(ConstantFontelloID.icon_cover_letter);

            txt_location_icon.setText(ConstantFontelloID.icon_location);
            txt_clock_icon.setText(ConstantFontelloID.ICON_JOB_EXP);
        }
        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
    class FooterViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
    @Override
    protected RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent) {
        return new CustomViewHolder(inflater.inflate(R.layout.new_candidate_item, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        return new HeaderViewHolder(inflater.inflate(R.layout.paginated_list_footer, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
        return new FooterViewHolder(inflater.inflate(R.layout.paginated_list_footer, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CustomViewHolder) {
            final CustomViewHolder itemViewHolder = (CustomViewHolder) holder;
            final JSONObject rowItemObject = (JSONObject) getItem(position);
            try
            {
                final DateFormat convert = new DateFormat();
                final String imgUrl = rowItemObject.optJSONObject("applicationdetails").optString("ProfilePic").trim();

                if(imgUrl == null || imgUrl.equals("")){
                    String nameFirstLetter = rowItemObject.optJSONObject("applicationdetails").optString("name").toUpperCase();
                    itemViewHolder.ivProfile.setText(nameFirstLetter.charAt(0)+"");
                    itemViewHolder.ivProfile.setTextColor(Color.WHITE);
                    itemViewHolder.ivProfile.setTextSize(18.0f);
                    itemViewHolder.ivProfile.setBackgroundResource(R.drawable.textdesign);
                    GradientDrawable drawable = (GradientDrawable)  itemViewHolder.ivProfile.getBackground();
                    drawable.setColor(Color.parseColor( rowItemObject.optJSONObject("applicationdetails").optString("color")));
                    itemViewHolder.ivProfile.setBackgroundResource(R.drawable.textdesign);
                    itemViewHolder.ivProfile.setVisibility(View.VISIBLE);
                    itemViewHolder.ivSeerkar.setVisibility(View.GONE);
                }else{
                    itemViewHolder.ivProfile.setVisibility(View.GONE);
                    itemViewHolder.ivSeerkar.setVisibility(View.VISIBLE);
                    loader.displayImage(rowItemObject.optJSONObject("applicationdetails").optString("ProfilePic").trim(), itemViewHolder.ivSeerkar, options);
                }
                itemViewHolder.tvProfileName.setText(rowItemObject.optJSONObject("applicationdetails").optString("name"));
                String month = rowItemObject.optJSONObject("applicationdetails").optString("exp_month");
                itemViewHolder.tvExp.setText(rowItemObject.optJSONObject("applicationdetails").optString("exp_year")+( "y " +month+"m"));
                itemViewHolder.tvLocation.setText(rowItemObject.optJSONObject("applicationdetails").optString("current_location"));
                String strFinalDate;
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                String strDate = rowItemObject.optJSONObject("applicationdetails").optString("applydate").toString().trim();
                String[] strSplitDate = strDate.split("-");
                String strYear = strSplitDate[2];
                if(strYear.equalsIgnoreCase(""+currentYear) || strYear == ""+currentYear){
                    strFinalDate = strSplitDate[0]+"/"+strSplitDate[1];
                }
                else{
                    strFinalDate = strDate.replace("-","/");
                }
                itemViewHolder.tvJobDate.setText(strFinalDate);
                appStatus = rowItemObject.optJSONObject("applicationdetails").getString("app_status");
                JSONArray professional = rowItemObject.getJSONArray("professiondetails");
                if(professional != null && professional.length() >0 )
                {
                    itemViewHolder.llWorkExp.setVisibility(View.VISIBLE);
                    itemViewHolder.workExpSepView.setVisibility(View.VISIBLE);
                    itemViewHolder.tvCompanyName.setText(rowItemObject.getJSONArray("professiondetails").getJSONObject(0).optString("organization"));
                    itemViewHolder.tvDesignation.setText(rowItemObject.getJSONArray("professiondetails").getJSONObject(0).optString("designation"));
                    String fromExpMonth = convert.C_month(professional.getJSONObject(0).optString("from_exp_month"));
                    String fromExpYear = professional.getJSONObject(0).optString("from_exp_year");
                    String toExpMonth = convert.C_month(professional.getJSONObject(0).optString("to_exp_month"));
                    String toExpYear = professional.getJSONObject(0).optString("to_exp_year");
                    String isCurrent = professional.getJSONObject(0).optString("is_current");
                    if (isCurrent.equalsIgnoreCase("1"))
                    {
                        itemViewHolder.tvCompExp.setText(fromExpMonth + "" + fromExpYear + " to Present");
                    }
                    else if (isCurrent.equalsIgnoreCase("0"))
                    {
                        itemViewHolder.tvCompExp.setText(fromExpMonth + "" + fromExpYear + " to " + toExpMonth + toExpYear);
                    }

                }
                else
                {
                    itemViewHolder.workExpSepView.setVisibility(View.GONE);
                    itemViewHolder.llWorkExp.setVisibility(View.GONE);
                }
                JSONArray educationdetails = rowItemObject.getJSONArray("educationdetails");
                if(educationdetails!=null && educationdetails.length() >0 )
                {
                    itemViewHolder.llEduExp.setVisibility(View.VISIBLE);
                    itemViewHolder.tvInstitute.setText(rowItemObject.getJSONArray("educationdetails").getJSONObject(0).optString("institute"));
                    itemViewHolder.tvCoursePeriod.setText(rowItemObject.getJSONArray("educationdetails").getJSONObject(0).optString("batch_from")+" to "+rowItemObject.getJSONArray("educationdetails").getJSONObject(0).optString("batch_to"));
                    itemViewHolder.tvQualification.setText((rowItemObject.getJSONArray("educationdetails").getJSONObject(0).optString("degree"))+" ("+(rowItemObject.getJSONArray("educationdetails").getJSONObject(0).optString("course_type"))+")");
                }
                else
                {
                    itemViewHolder.llEduExp.setVisibility(View.GONE);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            itemViewHolder.ll_profilename.setTag(position);
            itemViewHolder.ll_profilename.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick( View v)
                {
                    if(MySharedPreference.getKeyValue(Constant.KEY_VIEW_ID) != null && !MySharedPreference.getKeyValue(Constant.KEY_VIEW_ID).equalsIgnoreCase("0"))
                    {
                        NewCandidateListActivity.isSingleTap = true;
                        viewProfileFunc((int) v.getTag());
                    }
                    else
                        {
                        Snackbar snackbar = Snackbar.make(rlParent, MySharedPreference.getKeyValue(Constant.KEY_VIEW_MESSAGE), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                     }
                }
            });
            itemViewHolder.llWorkExp.setTag(position);
            itemViewHolder.llWorkExp.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(MySharedPreference.getKeyValue(Constant.KEY_VIEW_ID) != null && !MySharedPreference.getKeyValue(Constant.KEY_VIEW_ID).equalsIgnoreCase("0"))
                    {
                        NewCandidateListActivity.isSingleTap = true;
                        viewProfileFunc((int) v.getTag());
                    }
                    else
                    {
                        Snackbar snackbar = Snackbar.make(rlParent, MySharedPreference.getKeyValue(Constant.KEY_VIEW_MESSAGE), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }
            });
            itemViewHolder.llEduExp.setTag(position);
            itemViewHolder.llEduExp.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(MySharedPreference.getKeyValue(Constant.KEY_VIEW_ID) != null && !MySharedPreference.getKeyValue(Constant.KEY_VIEW_ID).equalsIgnoreCase("0"))
                    {
                        NewCandidateListActivity.isSingleTap = true;
                        viewProfileFunc((int) v.getTag());
                    }
                    else
                    {
                        Snackbar snackbar = Snackbar.make(rlParent, MySharedPreference.getKeyValue(Constant.KEY_VIEW_MESSAGE), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }
            });

            itemViewHolder.informationIv.setTypeface(AccountUtils.fontelloTtfIconsFont());
            itemViewHolder.informationIv.setText(ConstantFontelloID.ICON_JOB_MENU);
            itemViewHolder.informationIv.setTag(position);
            itemViewHolder.informationIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewCandidateListActivity.isSingleTap = false;
                    showCustomDialog((int)v.getTag());
                    //showCustomDialog(position);
                }
            });
            itemViewHolder.resumeButton.setTag(position);
            itemViewHolder.ll_resume_newcandidate.setTag(position);

            itemViewHolder.resumeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewCandidateListActivity.isSingleTap = false;
                    //downloadResume(rowItemMyProfile.get((int)v.getTag()));
                    downloadResume((int) v.getTag());
                }
            });

            itemViewHolder.ll_resume_newcandidate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewCandidateListActivity.isSingleTap = false;
                    //downloadResume(rowItemMyProfile.get((int)v.getTag()));
                    downloadResume((int) v.getTag());
                }
            });

            try{
                if(!rowItemMyProfile.get(position).optJSONObject("applicationdetails").getString("coverid").equalsIgnoreCase("0")){
                    itemViewHolder.ll_cover_newcandidate.setVisibility(View.VISIBLE);
                }else{
                    itemViewHolder.ll_cover_newcandidate.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            itemViewHolder.coverLetterButton.setTag(position);
            itemViewHolder.ll_cover_newcandidate.setTag(position);

            itemViewHolder.coverLetterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewCandidateListActivity.isSingleTap = false;
                    try {
                        if(rowItemMyProfile.get((int)v.getTag()).optJSONObject("applicationdetails").getString("coverid").equalsIgnoreCase("0"))
                        {
                            Snackbar snackbar = Snackbar.make(rlParent, "CoverLetter not found", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        else if (Utility.isNetworkAvailableRelative(mContext, rlParent))
                        {
                            Intent ic = new Intent(mContext, CoverLetterActivity.class);
                            ic.putExtra(Constant.KEY_VIEW_ID, getId(Integer.parseInt(v.getTag()+"")));
                            mContext.startActivity(ic);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


            itemViewHolder.ll_cover_newcandidate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewCandidateListActivity.isSingleTap = false;
                    try {
                        if(rowItemMyProfile.get((int)v.getTag()).optJSONObject("applicationdetails").getString("coverid").equalsIgnoreCase("0"))
                        {
                            Snackbar snackbar = Snackbar.make(rlParent, "CoverLetter not found", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        else if (Utility.isNetworkAvailableRelative(mContext, rlParent))
                        {
                            try {
                                strUserId = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("userid").toString();
                                strJobId = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("jobid").toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            AccountUtils.trackEvents("NewCandidateListActivity", "rtViewCoverLetter",
                                    "Origin= "+"CAL"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                            Intent ic = new Intent(mContext, CoverLetterActivity.class);
                            ic.putExtra(Constant.KEY_VIEW_ID, getId(Integer.parseInt(v.getTag()+"")));
                            mContext.startActivity(ic);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }else if (holder instanceof HeaderViewHolder) {

        } else if (holder instanceof FooterViewHolder) {

        }
    }
    private void showCustomDialog(final int mPosition){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder((Activity)mContext);
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.candidate_dialog_layout, null);

        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        String strCategoryStatus = "";

        try {
            strCategoryStatus = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("app_status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LinearLayout shortlistLayout = (LinearLayout) dialogView.findViewById(R.id.ll_shortlist_newcandidate);
        LinearLayout ll_reject = (LinearLayout) dialogView.findViewById(R.id.ll_reject_newcandidate);
        LinearLayout saveLayout = (LinearLayout) dialogView.findViewById(R.id.ll_save_newcandidate);
        txt1 = (TextView)dialogView.findViewById(R.id.txt1_newcandidate);
        txt2 = (TextView)dialogView.findViewById(R.id.txt2_newcandidate);
        txt3 = (TextView)dialogView.findViewById(R.id.txt3_newcandidate);

        if(strCategoryStatus.equalsIgnoreCase("0")|| strCategoryStatus.equalsIgnoreCase("11")){
            txt1.setText("Shortlist");
            txt2.setText("Reject");
            txt3.setText("Save");
        }

        if(strCategoryStatus.equalsIgnoreCase("1")){
            txt1.setText("Mark as unread");
            txt2.setText("Reject");
            txt3.setText("Save");
        }
        if(strCategoryStatus.equalsIgnoreCase("2")){
            txt1.setText("Shortlist");
            txt2.setText("Mark as unread");
            txt3.setText("Save");
        }
        if(strCategoryStatus.equalsIgnoreCase("3")){
            txt1.setText("Shortlist");
            txt2.setText("Reject");
            txt3.setText("Mark as unread");
        }

        shortlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strAction = txt1.getText().toString().trim();
                if(strAction.equalsIgnoreCase("Shortlist")){
                    itemShortListed(mPosition);
                    alertDialog.dismiss();
                    /*rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);*/
                    int nShortlisted =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE, nShortlisted);

                    try {
                        strUserId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("userid").toString();
                        strJobId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("jobid").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);
                    AccountUtils.trackEvents("NewCandidateListActivity", "rtShortlistCandidate",
                            "Origin= "+" CALEllipses"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);

                }
                else if(strAction.equalsIgnoreCase("Reject")){
                    itemRejected(mPosition);
                    alertDialog.dismiss();
                    /*rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);*/
                    int nReject =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_REJECT_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_REJECT_TYPE, nReject);

                    try {
                        strUserId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("userid").toString();
                        strJobId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("jobid").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);
                    AccountUtils.trackEvents("NewCandidateListActivity", "rtRejectCandidate",
                            "Origin= "+" CALEllipses"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                }
                else if(strAction.equalsIgnoreCase("Save")){
                    itemSaved(mPosition);
                    alertDialog.dismiss();
                    /*rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);*/
                    int nSaved =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SAVE_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SAVE_TYPE, nSaved);


                    try {
                        strUserId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("userid").toString();
                        strJobId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("jobid").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);
                    AccountUtils.trackEvents("NewCandidateListActivity", "rtSaveCandidate",
                            "Origin= "+" CALEllipses"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                }
                else if(strAction.equalsIgnoreCase("Mark as unread")){
                    itemMarkUnread(mPosition);
                    alertDialog.dismiss();
                    /*rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);*/
                    int nUnread =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE, nUnread);

                    try {
                        strUserId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("userid").toString();
                        strJobId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("jobid").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);
                    AccountUtils.trackEvents("NewCandidateListActivity", "rtMarkasunreadCandidate",
                            "Origin= "+" CALEllipses"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                }
                notifyDataSetChanged();
                String string = spinnerTitle.getText().toString().trim();
                String [] split = string.split("-");
                int intValue = Integer.parseInt(split[1].trim());
                int intFinal = intValue-1;
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

        });
        ll_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strAction = txt2.getText().toString().trim();
                if(strAction.equalsIgnoreCase("Shortlist")){
                    itemShortListed(mPosition);
                    alertDialog.dismiss();
                    /*rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);*/
                    int nShortlisted =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE, nShortlisted);
                    try {
                        strUserId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("userid").toString();
                        strJobId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("jobid").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);
                    AccountUtils.trackEvents("NewCandidateListActivity", "rtShortlistCandidate",
                            "Origin= "+" CALEllipses"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                }
                else if(strAction.equalsIgnoreCase("Reject")){
                    itemRejected(mPosition);
                    alertDialog.dismiss();
                    /*rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);*/
                    int nReject =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_REJECT_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_REJECT_TYPE, nReject);
                    try {
                        strUserId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("userid").toString();
                        strJobId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("jobid").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);
                    AccountUtils.trackEvents("NewCandidateListActivity", "rtRejectCandidate",
                            "Origin= "+" CALEllipses"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                }
                else if(strAction.equalsIgnoreCase("Save")){
                    itemSaved(mPosition);
                    alertDialog.dismiss();
                    /*rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);*/
                    int nSaved =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SAVE_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SAVE_TYPE, nSaved);

                    try {
                        strUserId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("userid").toString();
                        strJobId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("jobid").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);
                    AccountUtils.trackEvents("NewCandidateListActivity", "rtSaveCandidate",
                            "Origin= "+" CALEllipses"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                }
                else if(strAction.equalsIgnoreCase("Mark as unread")){
                    itemMarkUnread(mPosition);
                    alertDialog.dismiss();
                    /*rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);*/
                    int nUnread =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE, nUnread);

                    try {
                        strUserId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("userid").toString();
                        strJobId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("jobid").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);
                    AccountUtils.trackEvents("NewCandidateListActivity", "rtMarkasunreadCandidate",
                            "Origin= "+" CALEllipses"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                }
                notifyDataSetChanged();
                String string = spinnerTitle.getText().toString().trim();
                String [] split = string.split("-");
                int intValue = Integer.parseInt(split[1].trim());
                int intFinal = intValue-1;
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
        });
        saveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strAction = txt3.getText().toString().trim();
                if(strAction.equalsIgnoreCase("Shortlist")){
                    itemShortListed(mPosition);
                    alertDialog.dismiss();
                    /*rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);*/
                    int nShortlisted =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE, nShortlisted);
                    try {
                        strUserId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("userid").toString();
                        strJobId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("jobid").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);
                    AccountUtils.trackEvents("NewCandidateListActivity", "rtShortlistCandidate",
                            "Origin= "+" CALEllipses"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                }
                else if(strAction.equalsIgnoreCase("Reject")){
                    itemRejected(mPosition);
                    alertDialog.dismiss();
                    /*rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);*/
                    int nReject =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_REJECT_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_REJECT_TYPE, nReject);

                    try {
                        strUserId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("userid").toString();
                        strJobId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("jobid").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);
                    AccountUtils.trackEvents("NewCandidateListActivity", "rtRejectCandidate",
                            "Origin= "+" CALEllipses"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);

                }
                else if(strAction.equalsIgnoreCase("Save")){
                    itemSaved(mPosition);
                    alertDialog.dismiss();
                    /*rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);*/
                    int nSaved =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SAVE_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SAVE_TYPE, nSaved);

                    try {
                        strUserId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("userid").toString();
                        strJobId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("jobid").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);
                    AccountUtils.trackEvents("NewCandidateListActivity", "rtSaveCandidate",
                            "Origin= "+" CALEllipses"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                }
                else if(strAction.equalsIgnoreCase("Mark as unread")){
                    itemMarkUnread(mPosition);
                    alertDialog.dismiss();
                    /*rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);*/
                    int nUnread =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE)+1;
                    MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE, nUnread);

                    try {
                        strUserId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("userid").toString();
                        strJobId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("jobid").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rowItemMyProfile.remove(mPosition);
                    notifyItemRemoved(mPosition);
                    AccountUtils.trackEvents("NewCandidateListActivity", "rtMarkasunreadCandidate",
                            "Origin= "+" CALEllipses"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                }
                notifyDataSetChanged();
                String string = spinnerTitle.getText().toString().trim();
                String [] split = string.split("-");
                int intValue = Integer.parseInt(split[1].trim());
                int intFinal = intValue-1;
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
        });

        LinearLayout ll_startchat = (LinearLayout) dialogView.findViewById(R.id.ll_startchat_newcandidate);
        ll_startchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewCandidateListActivity.isSingleTap = false;

                try {
                    strUserId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("userid").toString();
                    strJobId = rowItemMyProfile.get(mPosition).getJSONObject("applicationdetails").getString("jobid").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AccountUtils.trackEvents("NewCandidateListActivity", "rtStartChat",
                        "Origin= "+"CAL"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);

                try {
                    Intent myIntent = new Intent(mContext,ChatActivity.class);
                    myIntent.putExtra(ChatConstants.JOBID_FOR_CHAT, rowItemMyProfile.get(mPosition).optJSONObject("applicationdetails").getString("userid"));
                    mContext.startActivity(myIntent);
                    alertDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        LinearLayout addCommentLayout = (LinearLayout) dialogView.findViewById(R.id.add_comment_layout);
        addCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewCandidateListActivity.isSingleTap = false;
                alertD  = new android.app.AlertDialog.Builder(mContext).create();
                viewComment(mPosition);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        //alertDialog.getWindow().setLayout(700, 700);
    }

    private void itemSaved(int position){
        try
        {
            aStatus = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("app_status");
            followup= rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("followup");
            temPosi = getId(position);
            if(aStatus.equalsIgnoreCase("3")){
                aStatus ="0";
            }
            else{
                aStatus = "3";
            }
            changeRecruiterAction();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void itemMarkUnread(int position){
        try
        {
            aStatus = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("app_status");
            followup= rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("followup");
            temPosi = getId(position);

            if(aStatus.equalsIgnoreCase("1") || aStatus.equalsIgnoreCase("2") || aStatus.equalsIgnoreCase("3") || aStatus.equalsIgnoreCase("11")){
                aStatus ="0";
            }
            /*else{
                aStatus = "3";
            }*/
            changeRecruiterAction();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void itemShortListed(int position){
        try
        {
            aStatus = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("app_status");
            followup= rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("followup");
            temPosi = getId(position);
            if (aStatus.equals("0"))
            {
                aStatus = "1";
            }
            else if (aStatus.equals("1"))
            {
                aStatus = "0";
            }
            else if(aStatus.equals("2"))
            {
                aStatus = "1";
            }
            else if(aStatus.equals("3")||aStatus.equals("4")||aStatus.equals("5"))
            {
                aStatus = "1";
            }
            changeRecruiterAction();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void itemRejected(int position){
        temPosi = getId(position);
        try
        {
            aStatus = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("app_status");
            followup= rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("followup");
            if (aStatus.equals("0"))
            {
                aStatus = "2";
            }
            else if(aStatus.equals("1"))
            {
                aStatus = "2";
            }
            else if (aStatus.equals("2"))
            {
                aStatus = "0";
            }
            else if(aStatus.equals("3")||aStatus.equals("4")||aStatus.equals("5"))
            {
                aStatus = "2";
            }
            changeRecruiterAction();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void viewComment(int position)
    {
        if (Utility.isNetworkAvailableRelative(mContext, rlParent))
        {
            idComment = position;
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View promptView = layoutInflater.inflate(R.layout.prompt, null);
            String strComment = "";
            try {
                strComment = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("comment").toString();
                strUserId = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("userid").toString();
                strJobId = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("jobid").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(strComment.equalsIgnoreCase("")){
                alertD.setTitle("Post Comment");
            }else {
                alertD.setTitle("Update Comment");
            }
            userInput = (EditText) promptView.findViewById(R.id.userEmailId);
            userInput.setHeight(100);
            userInput.setMaxLines(5);
            userInput.setHint("Type comment");
            try {
                userInput.setText(rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("comment").toString());
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
                    NewCandidateListActivity.isSingleTap = false;
                    hideKeyBoard();
                    alertD.dismiss();
                }
            });
            submit.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    NewCandidateListActivity.isSingleTap = false;
                    hideKeyBoard();


                    AccountUtils.trackEvents("NewCandidateListActivity", "rtAddComment",
                            "Origin= "+"CAL"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);

                    if(userInput.getText().toString().length()>0)
                    {
                        try {
                            alertD.dismiss();
                            rowItemMyProfile.get(idComment).getJSONObject("applicationdetails").put("comment", userInput.getText().toString());
                            loadPostComment();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            alertD.setView(promptView);
            alertD.show();
        }
    }
    private void showAlertDialog(final VolleyError error, final byte  nType){
        String titleMessage = "";
        String messageText = "";
        if (error instanceof NoConnectionError
                || error instanceof NetworkError) {
            titleMessage = mContext.getString(R.string.network_lbl);
            messageText = mContext.getResources().getString(R.string.msg_no_network);
        } else if (error instanceof TimeoutError) {
            titleMessage = mContext.getString(R.string.retry_lbl);
            messageText = mContext.getResources().getString(R.string.msg_connection_timout);
        } else {
            titleMessage = mContext.getString(R.string.error_lbl);
            messageText = mContext.getResources().getString(R.string.msg_server_error);
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        builder.setTitle(titleMessage).setMessage(messageText)
                .setCancelable(true)
                .setPositiveButton(mContext.getString(R.string.ok_lbl), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       if (error instanceof TimeoutError && nType == Constant.POST_COMMENT_WEBSERVICE)
                            loadPostComment();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
    }
    private void hideKeyBoard()
    {
        View view = ((Activity) mContext).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void changeRecruiterAction(){
        if (Utility.isNetworkAvailableRelative(mContext,rlParent)){
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
                                // Not Required
                            } else{
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
                    headers.put("User-agent", System.getProperty("http.agent")+" "+mContext.getString(R.string.app_name)+" "+Utility.getAppVersionName());
                    headers.put("AUTHKEY", ""+MySharedPreference.getKeyValue(Constant.KEY_USER_AUTH_KEY));
                    return headers;
                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(Constant.SOCKET_TIMEOUT_DURATION, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);
        }
    }
    private String getId(final int position){
        String viewId = null;
        try{
            viewId = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("id").trim();
        }
        catch (JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return viewId;
    }
    public void viewProfileFunc(int position)
    {
        if (Utility.isNetworkAvailableRelative(mContext, rlParent))
        {
            String id = getId(position);
            try
            {
                if (id != null
                && !id.equals("")) {
                    Intent ic = new Intent(mContext, ProfileResumeActivity.class);
                    ic.putExtra(Constant.KEY_VIEW_ID, id);
                    ic.putExtra(Constant.KEY_POSITION, position);
                    ic.putExtra("followup",  rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("followup").toString());
                    ic.putExtra("appstatus", rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("app_status").toString());
                    ic.putExtra(Constant.KEY_OBJECT, rowItemMyProfile.get(position).toString());
                    ic.putExtra(Constant.KEY_SCREEN_TYPE, PROFILE_TYPE);
                    ic.putExtra(Constant.KEY_JOB_TITLE, mJobTitle);
                    Bundle b = new Bundle();
                    b.putString("educationArray", rowItemMyProfile.get(position).optJSONArray("educationdetails").toString());
                    b.putString("professiondetails", rowItemMyProfile.get(position).optJSONArray("professiondetails").toString());
                    ic.putExtras(b);

                    String url =  Constant.RESUME_VIEW_URL
                            +"/"+ rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("id").trim()
                            +"/"+followup
                            +"/"+ Constant.USER_COOKIE;

                    /*new ResumeDownloadTask(mContext, "" +rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("name")
                            .toString()).execute(url,"" +rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("name")
                            .toString()+".pdf");*/
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void downloadResume(int position){
        if (Utility.isNetworkAvailableRelative(mContext, rlParent)){
            {

                try {
                    strUserId = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("userid").toString();
                    strJobId = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("jobid").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AccountUtils.trackEvents("NewCandidateListActivity", "rtViewResume",
                        "Origin= "+"CAL"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                try
                {
                    followup = rowItemMyProfile.get(position).optJSONObject("applicationdetails").getString("followup");
                    userResume = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("name").toString();
                    String url =  Constant.RESUME_VIEW_URL
                            +"/"+ rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("id").trim()
                            +"/"+followup
                            +"/"+ Constant.USER_COOKIE;
                    Intent intentDownloadResume = new Intent(mContext, DownloadResumeActivity.class);
                    intentDownloadResume.putExtra("DOWNLOAD_URL", url);
                    intentDownloadResume.putExtra("CANDIDATE_NAME", userResume);
                    mContext.startActivity(intentDownloadResume);
                    /*new ResumeDownloadTask(mContext, "" +rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("name")
                            .toString()).execute(url,"" +rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("name")
                            .toString()+".pdf");*/

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onItemDismiss(int position, int sDirection) {
        if(mPopupLayout.getVisibility() == View.VISIBLE){
            mPopupLayout.setVisibility(View.GONE);
            openCloseIv.setBackgroundResource(R.drawable.down_arrow);
        }
        String strStatus = "";
        try {
             strStatus = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("app_status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(sDirection == 16) {
            if(strStatus.equalsIgnoreCase("2")){
                Snackbar snackbar = Snackbar.make(rlParent, rowItemMyProfile.get(position).optJSONObject("applicationdetails").optString("name") + " has been marked as unread", Snackbar.LENGTH_SHORT);
                snackbar.show();
                itemMarkUnread(position);
                int nUnread =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE)+1;
                MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE, nUnread);

                try {
                    strUserId = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("userid").toString();
                    strJobId = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("jobid").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AccountUtils.trackEvents("NewCandidateListActivity", "rtMarkasunreadCandidate",
                        "Origin= "+" CALSwipe"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);

                swipeRefreshLayout.setRefreshing(true);

            }
            else{
                Snackbar snackbar = Snackbar.make(rlParent, rowItemMyProfile.get(position).optJSONObject("applicationdetails").optString("name")+ " has been rejected", Snackbar.LENGTH_SHORT);
                snackbar.show();
                itemRejected(position);
                int nRejected =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_REJECT_TYPE)+1;
                MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_REJECT_TYPE, nRejected);

                try {
                    strUserId = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("userid").toString();
                    strJobId = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("jobid").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AccountUtils.trackEvents("NewCandidateListActivity", "rtRejectCandidate",
                        "Origin= "+" CALSwipe"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                swipeRefreshLayout.setRefreshing(true);
            }
        }else {
            if(strStatus.equalsIgnoreCase("1")){
                Snackbar snackbar = Snackbar.make(rlParent, rowItemMyProfile.get(position).optJSONObject("applicationdetails").optString("name") + " has been marked as unread", Snackbar.LENGTH_SHORT);
                snackbar.show();
                itemMarkUnread(position);
                int nUnread =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE)+1;
                MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE, nUnread);

                try {
                    strUserId = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("userid").toString();
                    strJobId = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("jobid").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AccountUtils.trackEvents("NewCandidateListActivity", "rtMarkasunreadCandidate",
                        "Origin= "+" CALSwipe"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);
                swipeRefreshLayout.setRefreshing(true);
            }
            else{
                Snackbar snackbar = Snackbar.make(rlParent, rowItemMyProfile.get(position).optJSONObject("applicationdetails").optString("name") + " has been shortlisted", Snackbar.LENGTH_SHORT);
                snackbar.show();
                itemShortListed(position);
                int nShortlisted =  MySharedPreference.getRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE)+1;
                MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE, nShortlisted);

                try {
                    strUserId = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("userid").toString();
                    strJobId = rowItemMyProfile.get(position).getJSONObject("applicationdetails").getString("jobid").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AccountUtils.trackEvents("NewCandidateListActivity", "rtShortlistCandidate",
                        "Origin= "+" CALSwipe"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);

                swipeRefreshLayout.setRefreshing(true);
            }
        }
        rowItemMyProfile.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        String string = spinnerTitle.getText().toString().trim();
        String [] split = string.split("-");
        int intValue = Integer.parseInt(split[1].trim());
        int intFinal = intValue-1;
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
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(rowItemMyProfile, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    private void loadPostComment(){
        if (Utility.isNetworkAvailableRelative(mContext, rlParent)
                && rowItemMyProfile != null){
            Utility.showLoadingDialog((Activity) mContext);
            String url = null;
            try {
                url = Constant.ADD_COMMENT_URL +rowItemMyProfile.get(idComment).getJSONObject("applicationdetails").getString("id").trim()+"/"+ Constant.USER_COOKIE;
            } catch (JSONException e1) {
                e1.printStackTrace();
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
                                        Snackbar snackbar = Snackbar.make(rlParent, mContext.getString(R.string.comment_submited_succes_message), Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                    } else{
                                        Snackbar snackbar = Snackbar.make(rlParent, iModel.getError_msg(), Snackbar.LENGTH_SHORT);
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
                    params.put("comment", userInput.getText().toString().trim());
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
                    headers.put("User-agent", System.getProperty("http.agent")+" "+mContext.getResources().getString(R.string.app_name)+" "+Utility.getAppVersionName());
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
