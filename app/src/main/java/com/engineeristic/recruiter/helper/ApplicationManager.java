package com.engineeristic.recruiter.helper;

import android.content.Context;

/**
 * Created by d on 4/24/2017.
 */

public class ApplicationManager {


    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    private static Context mContext;
}
