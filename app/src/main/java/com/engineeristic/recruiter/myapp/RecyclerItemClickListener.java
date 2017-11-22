package com.engineeristic.recruiter.myapp;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.engineeristic.recruiter.util.RecyclerTapListener;

/**
 * Created by d on 4/18/2017.
 */

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private RecyclerTapListener mListener;
    private RecyclerView mRecycleView;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(final Context context, RecyclerTapListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                final View childView = mRecycleView.findChildViewUnder(e.getX(), e.getY());

                if (childView != null
                        && mListener != null
                        && NewCandidateListActivity.isSingleTap) {
                    mListener.onSingleTapItem(childView, mRecycleView.getChildAdapterPosition(childView));
                }
                return true;
            }
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // User tapped the screen twice.
                final View childView = mRecycleView.findChildViewUnder(e.getX(), e.getY());

                if (childView != null && mListener != null) {
                    mListener.onDoubleTapItem(childView, mRecycleView.getChildAdapterPosition(childView));
                }
                //return false;
                return true;
            }
        });
    }
    private static String getTouchType(MotionEvent e){

        String touchTypeDescription = " ";
        int touchType = e.getToolType(0);

        switch (touchType) {
            case MotionEvent.TOOL_TYPE_FINGER:
                touchTypeDescription += "(finger)";
                break;
            case MotionEvent.TOOL_TYPE_STYLUS:
                touchTypeDescription += "(stylus, ";
                //Get some additional information about the stylus touch
                float stylusPressure = e.getPressure();
                touchTypeDescription += "pressure: " + stylusPressure;

                if(Build.VERSION.SDK_INT >= 21) {
                    //touchTypeDescription += ", buttons pressed: " + getButtonsPressed(e);
                }
                touchTypeDescription += ")";
                break;
            case MotionEvent.TOOL_TYPE_ERASER:
                touchTypeDescription += "(eraser)";
                break;
            case MotionEvent.TOOL_TYPE_MOUSE:
                touchTypeDescription += "(mouse)";
                break;
            default:
                touchTypeDescription += "(unknown tool)";
                break;
        }

        return touchTypeDescription;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        this.mRecycleView = view;
        mGestureDetector.onTouchEvent(e);
        return false;
    }
    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}