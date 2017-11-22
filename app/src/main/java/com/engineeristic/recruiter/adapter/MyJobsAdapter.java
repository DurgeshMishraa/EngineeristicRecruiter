package com.engineeristic.recruiter.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.engineeristic.recruiter.model.JobItemsModel;
import com.engineeristic.recruiter.model.JobRefreshModel;
import com.engineeristic.recruiter.myapp.NewCandidateListActivity;
import com.engineeristic.recruiter.myapp.RecruiterApplication;
import com.engineeristic.recruiter.util.AccountUtils;
import com.engineeristic.recruiter.util.Constant;
import com.engineeristic.recruiter.util.ConstantFontelloID;
import com.engineeristic.recruiter.util.DateFormat;
import com.engineeristic.recruiter.util.MySharedPreference;
import com.engineeristic.recruiter.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyJobsAdapter extends CusFooterAdapter<JobItemsModel>{

    private List<JobItemsModel> mRowItemMyJobse;
    private int rowLayout;
    private Context mContext;

    private String published_id = "";
    public static String name;
    public static String titles;
    private ImageLoader loader;
    private int reason = 1;
    final String[] spinnerItemArray = {"Unpublish", "Refresh"};
    private Typeface typeRobotoMedium, typeRobotoRegular;
    private String TAG = "MyJobsAdapter";
    private RelativeLayout rlParent;
    private CharSequence[] items;
    private JobViewHolder itemViewHolderCard;
    private Activity mActivity;



    public MyJobsAdapter(Context paramContext, RelativeLayout rlParam, Activity paramActivity) {
        // With Header & With Footer
        super(null, false, false);
        this.mContext = paramContext;
        this.mActivity = paramActivity;
        this.rlParent = rlParam;
        typeRobotoRegular = Typeface.createFromAsset(mContext.getAssets(),"robotoregular.ttf");
        typeRobotoMedium = Typeface.createFromAsset(mContext.getAssets(),"robotomedium.ttf");

        GetPublishOption publishOption = new GetPublishOption();
        publishOption.execute(Constant.UNPUBLISH_MESSAGE);
    }
    public void initData(){
        super.loadData(mRowItemMyJobse);
    }
    public void loadData(List<JobItemsModel> data){
        this.mRowItemMyJobse = data;
        super.loadData(data);
    }
    public class JobViewHolder extends RecyclerView.ViewHolder {
        //ImageView arrowImage;
        TextView jobDescription_textView;
        TextView jobCompanyDetails_textView;
        TextView jobPublishedDate_textView;
        TextView jobPostedDate;
        TextView jobDateValue;
        RelativeLayout llJobCompanyDetails;
        LinearLayout llJobDescription1;
        TextView jobExperience;
        LinearLayout eldLTypeLayout;
        LinearLayout mainLayout, ll_main_perent, container_layout;
        TextView publishedIcon;
        TextView expImageView;
        TextView locationImageView;
        Button appButton;
        Button statusButton;
        LinearLayout ll_app_btn;//, jobprofile;
        CardView card;


        public JobViewHolder(View convertView) {
            super(convertView);
            jobDescription_textView = (TextView) convertView.findViewById(R.id.textView_jobDescription);
            jobExperience = (TextView)convertView.findViewById(R.id.tv_jobExperience);
            jobCompanyDetails_textView = (TextView) convertView.findViewById(R.id.tv_jobLocation);
            jobPublishedDate_textView  = (TextView) convertView.findViewById(R.id.tv_jobCreatedDate);
            jobPostedDate = (TextView) convertView.findViewById(R.id.posted_job_text);
            jobDateValue = (TextView)  convertView.findViewById(R.id.posted_date_text);
            llJobCompanyDetails = (RelativeLayout)convertView.findViewById(R.id.ll_jobCompanyDetails);
            llJobDescription1 = (LinearLayout)convertView.findViewById(R.id.layout_jobDescription1);
            //arrowImage = (ImageView)convertView.findViewById(R.id.arrowImage);
            eldLTypeLayout = (LinearLayout)convertView.findViewById(R.id.textView_jobCompanyDetails);
            statusButton = (Button)convertView.findViewById(R.id.status_button);
            publishedIcon = (TextView) convertView.findViewById(R.id.publish_iv);
            appButton = (Button) convertView.findViewById(R.id.app_button);
            card = (CardView) convertView.findViewById(R.id.card);
            ll_app_btn = (LinearLayout) convertView.findViewById(R.id.ll_app_button);
            mainLayout = (LinearLayout) convertView.findViewById(R.id.main_perent);
            ll_main_perent = (LinearLayout) convertView.findViewById(R.id.ll_main_perent);
            //jobprofile = (LinearLayout) convertView.findViewById(R.id.jobprofile);
            container_layout = (LinearLayout) convertView.findViewById(R.id.container_layout);
            expImageView = (TextView) convertView.findViewById(R.id.imageExperience);
            locationImageView = (TextView) convertView.findViewById(R.id.imageView_menu);
            jobDescription_textView.setTypeface(typeRobotoRegular);
            jobExperience.setTypeface(typeRobotoRegular);
            jobCompanyDetails_textView.setTypeface(typeRobotoRegular);
            appButton.setTypeface(typeRobotoRegular);
            jobPostedDate.setTypeface(typeRobotoRegular);
            jobDateValue.setTypeface(typeRobotoRegular);
            statusButton.setTypeface(typeRobotoRegular);
        }
    }
    private void showSpinnerDialog(final int mPosition){
        AlertDialog.Builder b = new AlertDialog.Builder(mActivity);
        b.setItems(spinnerItemArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch(which){
                    case 0:
                        if(mRowItemMyJobse.get(mPosition).getPublish().equals("1")) {
                            showUnpublished(mPosition);
                        }else{
                            Snackbar snackbar = Snackbar.make(rlParent, mContext.getResources().getString(R.string.not_published_msg), Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        break;
                    case 1:
                        if(mRowItemMyJobse.get(mPosition).getRefresh_status().equals("1")) {
                            showRefreshJob(mPosition);
                        }else{
                            Snackbar snackbar = Snackbar.make(rlParent, mContext.getResources().getString(R.string.not_refresh_msg), Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        break;
                }
            }
        });

        b.show();
    }
    public void showUnpublished(final int mPosition){

        if (Utility.isNetworkAvailableRelative(mContext, rlParent)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.MaterialThemeDialog);
            builder.setTitle(mContext.getResources().getString(R.string.unpublish_reasons_lbl));
           /* builder.setSingleChoiceItems(R.array.Reasons, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    reason = which + 1;
                }
            })*/
            builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    reason = which + 1;
                }
            })
                    .setCancelable(false)
                    .setNegativeButton(mContext.getResources().getString(R.string.cancle_lbl), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }
                    )
                    .setPositiveButton(mContext.getResources().getString(R.string.OK_lbl), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                published_id = mRowItemMyJobse.get(mPosition).getPublished_id();
                                loadJobUnpublish(mPosition);
                            } catch (Exception e) {

                            }
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
            alert.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);

        }
    }
    private void showRefreshJob(final int mPosition){
        if (Utility.isNetworkAvailableRelative(mContext, rlParent)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle(mContext.getResources().getString(R.string.refresh_jobs_lbl)).setMessage(mContext.getResources().getString(R.string.refresh_msg_lbl));
            builder.setCancelable(false)
                    .setNegativeButton(mContext.getResources().getString(R.string.cancle_lbl), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton(mContext.getResources().getString(R.string.OK_lbl), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                published_id = mRowItemMyJobse.get(mPosition).getPublished_id();
                                loadJobRefresh(mPosition);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
            alert.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
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
        return new JobViewHolder(inflater.inflate(R.layout.jobs_item, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        return new HeaderViewHolder(inflater.inflate(R.layout.paginated_list_footer, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
        return new FooterViewHolder(inflater.inflate(R.layout.paginated_list_footer, parent, false));
    }

    private void googleAnalytics(int position){
        String strUserId = mRowItemMyJobse.get(position).getUid();
        String strJobId = mRowItemMyJobse.get(position).getId();
        AccountUtils.trackEvents("My Jobs", "rtViewCandidateApplyList",
                "Origin= "+"JobList"+" ,UserId= "+strUserId+" ,JobId= "+strJobId, null);

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final String strStatusRejPen;
        if (holder instanceof JobViewHolder) {
            final JobViewHolder itemViewHolder = (JobViewHolder) holder;
            itemViewHolderCard = itemViewHolder;
            itemViewHolder.eldLTypeLayout.setVisibility(View.VISIBLE);
            itemViewHolder.llJobCompanyDetails.setTag(position);
            itemViewHolder.appButton.setTag(position);

            strStatusRejPen = mRowItemMyJobse.get(position).getStatustext();
            itemViewHolder.llJobCompanyDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(strStatusRejPen.equalsIgnoreCase("Rejected") || strStatusRejPen.equalsIgnoreCase("pending")){
                        Toast.makeText(mContext.getApplicationContext(), "This job does not have any candidates", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        googleAnalytics(position);
                        viewApplicationFunc(position);
                    }
                }
            });
            itemViewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(strStatusRejPen.equalsIgnoreCase("Rejected") || strStatusRejPen.equalsIgnoreCase("pending")){
                        Toast.makeText(mContext.getApplicationContext(), "This job does not have any candidates", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        googleAnalytics(position);
                        viewApplicationFunc(position);
                    }
                }
            });

            itemViewHolder.ll_main_perent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(strStatusRejPen.equalsIgnoreCase("Rejected") || strStatusRejPen.equalsIgnoreCase("pending")){
                        Toast.makeText(mContext.getApplicationContext(), "This job does not have any candidates", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        googleAnalytics(position);
                        viewApplicationFunc(position);
                    }
                }
            });

            itemViewHolder.container_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(strStatusRejPen.equalsIgnoreCase("Rejected") || strStatusRejPen.equalsIgnoreCase("pending")){
                        Toast.makeText(mContext.getApplicationContext(), "This job does not have any candidates", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        googleAnalytics(position);
                        viewApplicationFunc(position);
                    }
                }
            });
            itemViewHolder.appButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(strStatusRejPen.equalsIgnoreCase("Rejected") || strStatusRejPen.equalsIgnoreCase("pending")){
                        Toast.makeText(mContext.getApplicationContext(), "This job does not have any candidates", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        googleAnalytics(position);
                        viewApplicationFunc(position);
                    }
                }
            });

            updateRow(itemViewHolder, mRowItemMyJobse.get(position));
            itemViewHolder.expImageView.setTypeface(AccountUtils.fontelloTtfIconsFont());
            itemViewHolder.locationImageView.setTypeface(AccountUtils.fontelloTtfIconsFont());
            itemViewHolder.expImageView.setText(ConstantFontelloID.ICON_JOB_EXP);
            itemViewHolder.locationImageView.setText(ConstantFontelloID.icon_location);
            itemViewHolder.publishedIcon.setTypeface(AccountUtils.fontelloTtfIconsFont());
            itemViewHolder.publishedIcon.setText(ConstantFontelloID.ICON_JOB_MENU);
            itemViewHolder.publishedIcon.setTag(position);

            itemViewHolder.publishedIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mRowItemMyJobse.get((int)v.getTag()).getPublish().equals("1"))
                        showSpinnerDialog((int)v.getTag());
                }
            });

        }else if (holder instanceof HeaderViewHolder) {

        } else if (holder instanceof FooterViewHolder) {

        }
    }
    private void loadJobRefresh(final int mPosition){
        if (Utility.isNetworkAvailableRelative(mContext, rlParent)){
            String url = Constant.JOB_REFRESH_URL + published_id + "/" + Constant.USER_COOKIE;
            RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Gson mGson = new GsonBuilder().create();
                    JobRefreshModel iModel = mGson.fromJson(response, JobRefreshModel.class);
                    if (iModel != null) {
                        if(iModel.getStatus().equalsIgnoreCase(Constant.HTTP_OK)){
                            mRowItemMyJobse.get(mPosition).setRefresh_status("0");
                            notifyDataSetChanged();
                        }else{
                            Snackbar snackbar = Snackbar.make(rlParent, iModel.getError_msg(), Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(error != null){
                                if(error instanceof NoConnectionError
                                        || error instanceof NetworkError){
                                    Snackbar snackbar = Snackbar.make(rlParent, mContext.getString(R.string.msg_no_network), Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }else if(error instanceof TimeoutError){
                                    Snackbar snackbar = Snackbar.make(rlParent, mContext.getString(R.string.msg_connection_timout), Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }else{
                                    Snackbar snackbar = Snackbar.make(rlParent, mContext.getString(R.string.msg_server_error), Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                            }
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
                    headers.put("User-agent", System.getProperty("http.agent")+" "+mContext.getResources().getString(R.string.app_name)+" "+Utility.getAppVersionName());
                    headers.put("AUTHKEY", ""+ MySharedPreference.getKeyValue(Constant.KEY_USER_AUTH_KEY));

                    return headers;
                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(Constant.SOCKET_TIMEOUT_DURATION, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);
        }
    }


    public class GetPublishOption extends AsyncTask<String , Void ,String> {
        String server_response;
        List<String> listItems = new ArrayList<String>();
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = readStream(urlConnection.getInputStream());

                    try {
                        JSONObject jObject = new JSONObject(server_response);
                        JSONArray jArray = jObject.getJSONArray("unpublishmessages");
                        for(int i =0; i<jArray.length();i++){
                            listItems.add(jArray.getJSONObject(i).getString("name"));
                        }
                        items = listItems.toArray(new CharSequence[listItems.size()]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
       /* @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("Response", "" + server_response);

        }*/
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }


    private void loadJobUnpublish(final int mPosition){
        if (Utility.isNetworkAvailableRelative(mContext, rlParent)){
            String url = Constant.UNPUBLISHED_JOB_URL + published_id +"/"+ reason +"/" + Constant.USER_COOKIE;
            RequestQueue queue = RecruiterApplication.getApplication().getReqQueue();
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Gson mGson = new GsonBuilder().create();
                    // JobRefreshModel Use for UnPublished
                    JobRefreshModel iModel = mGson.fromJson(response, JobRefreshModel.class);
                    if (iModel != null) {
                        if(iModel.getStatus().equalsIgnoreCase(Constant.HTTP_OK)){
                            mRowItemMyJobse.get(mPosition).setPublish("0");
                            mRowItemMyJobse.get(mPosition).setStatustext("unpublished");
                            notifyDataSetChanged();
                        }else{
                            Snackbar snackbar = Snackbar.make(rlParent, iModel.getError_msg(), Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(error != null){
                                if(error instanceof NoConnectionError
                                        || error instanceof NetworkError){
                                    Snackbar snackbar = Snackbar.make(rlParent, mContext.getString(R.string.msg_no_network), Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }else if(error instanceof TimeoutError){
                                    Snackbar snackbar = Snackbar.make(rlParent, mContext.getString(R.string.msg_connection_timout), Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }else{
                                    Snackbar snackbar = Snackbar.make(rlParent, mContext.getString(R.string.msg_server_error), Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                            }
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
    private void viewApplicationFunc(int position){
        if (Utility.isNetworkAvailableRelative(mContext, rlParent)){
            String title = "";
            try{
                title = mRowItemMyJobse.get(position).getTitle();
                published_id = mRowItemMyJobse.get(position).getPublished_id();
                AccountUtils.setJobPublishId(published_id);
                MySharedPreference.clearRecruiterType();
                MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_ALL_TYPE, Integer.parseInt(mRowItemMyJobse.get(position).getTotalapplications()));
                MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_UNREAD_TYPE, Integer.parseInt(mRowItemMyJobse.get(position).getTotalnewapplication()));
                MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_MAGIC_TYPE, Integer.parseInt(mRowItemMyJobse.get(position).getTotalnewapplication()));
                MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SHORTLIST_TYPE, Integer.parseInt(mRowItemMyJobse.get(position).getTotalshorlist()));
                MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_REJECT_TYPE, Integer.parseInt(mRowItemMyJobse.get(position).getTotalreject()));
                MySharedPreference.saveRecruiterViewType(Constant.RECRUITER_SAVE_TYPE, Integer.parseInt(mRowItemMyJobse.get(position).getTotalsave()));
            }
            catch (Exception e){
            }
            MySharedPreference.clearFilterType();
            Intent ic  = new Intent(mContext,NewCandidateListActivity.class);
            ic.putExtra(Constant.KEY_JOB_ID, published_id);
            ic.putExtra(Constant.KEY_JOB_TITLE,titles=title);
            Constant.JUMP_RESUME_ACTIVITY = "MyJobsAdapter";
            try{
                ic.putExtra(Constant.KEY_ACTION_ID, mRowItemMyJobse.get(position).getPublished_id());
            }
            catch (Exception e){
                e.printStackTrace();
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options = ActivityOptionsCompat. makeSceneTransitionAnimation(mActivity, itemViewHolderCard.card, "profile");
                mActivity.startActivity(ic, options.toBundle());
            }
            else {
                mActivity.startActivity(ic);
            }
            //mActivity.startActivity(ic);
        }
    }
    private void updateRow(JobViewHolder holder, JobItemsModel rowItemMyJobs){
        try{
            //holder.jobDescription_textView.setText(rowItemMyJobs.getPublished_id()+": "+rowItemMyJobs.getTitle());
            //Log.e(TAG, "Job Title: "+rowItemMyJobs.getTitle().toString().trim());
            String strTitle = rowItemMyJobs.getTitle().toString().trim();
            String[] strJobTtl = strTitle.split(strTitle.substring(strTitle.lastIndexOf("(")));
            String[] strJobTitle = strJobTtl[0].split("\\(");
            holder.jobDescription_textView.setText(strJobTitle[0]);
            holder.jobExperience.setText(rowItemMyJobs.getMin()+" - "+rowItemMyJobs.getMax()+"  "+"years");
            holder.jobCompanyDetails_textView.setText(rowItemMyJobs.getLocation());
            holder.jobPublishedDate_textView.setText(rowItemMyJobs.getCreated_date());
            String temp[] = null;
            String strYear = "";

            if(rowItemMyJobs.getCreated().indexOf("-") != -1) {
                String[] tempYear = rowItemMyJobs.getCreated().split("-");
                strYear = tempYear[2];
                if(strYear.equalsIgnoreCase("2017")){
                    strYear="";
                }
                else{
                    strYear = strYear;
                }
            }

            if(rowItemMyJobs.getCreated_date().indexOf("/") != -1) {
                temp = rowItemMyJobs.getCreated_date().split("/");
            }
            if(temp != null && temp[0] != null && temp[1] != null) {
                holder.jobPostedDate.setText(mContext.getString(R.string.posted_by_lbl));
                holder.jobDateValue.setText(temp[0] +" " + new DateFormat().C_month(temp[1])+strYear);
            }
            holder.statusButton.setText(" "+rowItemMyJobs.getStatustext()+" ");
            switch(rowItemMyJobs.getStatustext().toLowerCase()){
                case "published":
                    holder.ll_app_btn.setVisibility(View.VISIBLE);
                    holder.publishedIcon.setVisibility(View.VISIBLE);
                    holder.statusButton.setVisibility(View.GONE);
                    break;
                case "unpublished":
                    holder.ll_app_btn.setVisibility(View.VISIBLE);
                    holder.statusButton.setVisibility(View.VISIBLE);
                    holder.publishedIcon.setVisibility(View.GONE);
                    //holder.statusButton.setBackgroundResource(R.drawable.unpublished_bt);
                    holder.statusButton.setTextColor(Color.parseColor("#BDBDBD"));
                    break;
                case "updated":
                    holder.ll_app_btn.setVisibility(View.VISIBLE);
                    holder.ll_app_btn.setVisibility(View.VISIBLE);
                    holder.statusButton.setVisibility(View.VISIBLE);
                    holder.publishedIcon.setVisibility(View.GONE);
                    //holder.statusButton.setBackgroundResource(R.drawable.updated_bt);
                    holder.statusButton.setTextColor(Color.parseColor("#FFC400"));
                    break;
                case "rejected":
                    holder.ll_app_btn.setVisibility(View.GONE);
                    holder.statusButton.setVisibility(View.VISIBLE);
                    holder.publishedIcon.setVisibility(View.GONE);
                    //holder.statusButton.setBackgroundResource(R.drawable.rejected_bt);
                    holder.statusButton.setTextColor(Color.parseColor("#E53935"));
                    break;
                case "pending":
                    holder.ll_app_btn.setVisibility(View.GONE);
                    holder.statusButton.setVisibility(View.VISIBLE);
                    holder.publishedIcon.setVisibility(View.GONE);
                    //holder.statusButton.setBackgroundResource(R.drawable.pending_bt);
                    holder.statusButton.setTextColor(Color.parseColor("#FFC400"));
                    break;
            }
            holder.appButton.setText(rowItemMyJobs.getAppliedtext() + " " + mContext.getResources().getString(R.string.application_lbl));
            if(rowItemMyJobs.getAppliedtext().equals("0")) {
                holder.llJobCompanyDetails.setEnabled(true);
                holder.llJobDescription1.setEnabled(false);
            }
            else{
                holder.llJobCompanyDetails.setEnabled(true);
                holder.llJobDescription1.setEnabled(true);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
