package com.engineeristic.recruiter.util;

import android.view.View;

/**
 * Created by d on 5/3/2017.
 */

public interface RecyclerTapListener {

    public void onSingleTapItem(View view, int position);
    public void onDoubleTapItem(View view, int position);
}
