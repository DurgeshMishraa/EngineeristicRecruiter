package com.engineeristic.recruiter.util;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.engineeristic.recruiter.myapp.RecruiterApplication;

/**
 * Created by d on 5/10/2017.
 */

public class AccountUtils {

    //private static Context mContext;

    public static Typeface fontelloTtfIconsFont(){
        Typeface font = Typeface.createFromAsset(RecruiterApplication.getApplication().getApplicationContext().getAssets(),"fontello.ttf");
        return font;
    }
    /*AccountUtils(){
        mContext = RecruiterApplication.getApplication().getApplicationContext();
    }*/

    public static String getCategories() {
        String strCategories ="";
        if(RecruiterApplication.getApplication().getApplicationContext()!=null)        {
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(RecruiterApplication.getApplication().getApplicationContext());
            strCategories = mPrefs.getString("SELECTED_CATEGORIES",strCategories);
        }
        return strCategories;

    }

    public static void setCategories(String paramStrCategories) {
        if(RecruiterApplication.getApplication().getApplicationContext()!=null)
        {
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(RecruiterApplication.getApplication().getApplicationContext());
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.putString("SELECTED_CATEGORIES",paramStrCategories);
            prefsEditor.commit();
        }
    }

    public static String getCategoriesCandidate() {
        String strCategory ="";
        if(RecruiterApplication.getApplication().getApplicationContext()!=null)        {
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(RecruiterApplication.getApplication().getApplicationContext());
            strCategory = mPrefs.getString("SELECTED_CATEGORIES_CANDIDATE",strCategory);
        }
        return strCategory;
    }
    public static void setCategoriesCandidate(String paramStrCategory) {
        if(RecruiterApplication.getApplication().getApplicationContext()!=null)
        {
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(RecruiterApplication.getApplication().getApplicationContext());
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.putString("SELECTED_CATEGORIES_CANDIDATE",paramStrCategory);
            prefsEditor.commit();
        }
    }

    public static String getJobListCallValue() {
        String strJobListCallValue ="";
        if(RecruiterApplication.getApplication().getApplicationContext()!=null)        {
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(RecruiterApplication.getApplication().getApplicationContext());
            strJobListCallValue = mPrefs.getString("JOB_LIST_CALL_VALUE",strJobListCallValue);
        }
        return strJobListCallValue;
    }
    public static void setJobListCallValue(String paramStrJobListCallValue) {
        if(RecruiterApplication.getApplication().getApplicationContext()!=null)
        {
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(RecruiterApplication.getApplication().getApplicationContext());
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.putString("JOB_LIST_CALL_VALUE",paramStrJobListCallValue);
            prefsEditor.commit();
        }
    }

    public static boolean getBooleanCoachMark() {
        boolean value = false;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RecruiterApplication.getApplication().getApplicationContext());
        if (preferences != null) {
            value = preferences.getBoolean("COACH_MARK", false);
        }
        return value;
    }

    public static boolean setBooleanCoachMark(boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RecruiterApplication.getApplication().getApplicationContext());
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("COACH_MARK", value);
            return editor.commit();
        }
        return false;
    }


    public static String getJobPublishId() {
        String strJobPublishId ="";
        if(RecruiterApplication.getApplication().getApplicationContext()!=null)        {
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(RecruiterApplication.getApplication().getApplicationContext());
            strJobPublishId = mPrefs.getString("PUBLISHED_JOB_ID",strJobPublishId);
        }
        return strJobPublishId;

    }

    public static void setJobPublishId(String paramStrJobPublishId) {
        if(RecruiterApplication.getApplication().getApplicationContext()!=null)
        {
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(RecruiterApplication.getApplication().getApplicationContext());
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.putString("PUBLISHED_JOB_ID",paramStrJobPublishId);
            prefsEditor.commit();
        }
    }

    public static void trackerScreen(String screenName)
    {
        Tracker t = ((RecruiterApplication)RecruiterApplication.getApplication().getApplicationContext()).getTracker(RecruiterApplication.TrackerName.APP_TRACKER);
        t.setScreenName(screenName);
        t.send(new HitBuilders.AppViewBuilder().build());

    }
    public static void trackEvents(String category, String action, String label,String value)
    {
        try {
            Tracker t = ((RecruiterApplication)RecruiterApplication.getApplication().getApplicationContext()).getTracker(RecruiterApplication.TrackerName.APP_TRACKER);
            // Build and send an Event.
            t.send(new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setAction(action)
                    .setLabel(label)
                    .build());
        } catch (Exception e)
        {
        //Log.e("Base Action Bar ACtivity ","trackerScreen() Exception :"+ e.getMessage());
        }
    }
}
