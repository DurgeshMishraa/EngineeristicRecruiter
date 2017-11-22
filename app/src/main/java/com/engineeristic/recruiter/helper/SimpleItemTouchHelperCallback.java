package com.engineeristic.recruiter.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.View;

import com.engineeristic.recruiter.R;
import com.engineeristic.recruiter.myapp.RecruiterApplication;
import com.engineeristic.recruiter.util.AccountUtils;
import com.engineeristic.recruiter.util.ConstantFontelloID;


public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    public static final float ALPHA_FULL = 1.0f;
    private final ItemTouchHelperAdapter mAdapter;
    private String TAG = "SimpleItemTouchHelper";
    private String strCat;
    private Typeface typeFontello;
    private SwipeRefreshLayout swipeRefreshLayout;



    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter, SwipeRefreshLayout swipeRefreshLayoutParam) {
        mAdapter = adapter;
        swipeRefreshLayout = swipeRefreshLayoutParam;



    }
    @Override
    public boolean isLongPressDragEnabled() {
        return false; // Stop long press & position swipe
    }
    @Override
    public boolean isItemViewSwipeEnabled() {
        //swipeRefreshLayout.setRefreshing(false);
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Set movement flags based on the layout manager
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        //swipeRefreshLayout.setRefreshing(false);
        // Notify the adapter of the move
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        // Notify the adapter of the dismissal
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition(), i);
    }
    int rectOffset = 1;
    int iconOffset = 1;
    @Override
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        swipeRefreshLayout.setRefreshing(false);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Get RecyclerView item from the ViewHolder
            String strCategories = AccountUtils.getCategoriesCandidate();
            if(""== strCategories){
                strCat = "Magic Sort";
            }
            else{
                String[] strCatValue = strCategories.split(",");
                strCat = strCatValue[1].toString().trim();
            }
            View itemView = viewHolder.itemView;
            Bitmap iconLeft, iconRight;
            Paint paint = new Paint();
            Paint paintTtf = new Paint();
            typeFontello = Typeface.createFromAsset(RecruiterApplication.getApplication().getApplicationContext().getAssets(),"fontello.ttf");
            if(strCat.equalsIgnoreCase("Magic Sort") || strCat.equalsIgnoreCase("Unread")){
                iconLeft = BitmapFactory.decodeResource(RecruiterApplication.getApplication().getApplicationContext().getResources(), R.drawable.shortlist);
                iconRight = BitmapFactory.decodeResource(RecruiterApplication.getApplication().getApplicationContext().getResources(), R.drawable.reject);
                if (dX > rectOffset) {
                paint.setColor(Color.parseColor("#16a085"));
                canvas.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom(), paint);
                if (dX > iconOffset) {
                    paintTtf.setColor(Color.WHITE);
                    paintTtf.setTextSize(72);
                    paintTtf.setTypeface(typeFontello);
                    canvas.drawText(ConstantFontelloID.icon_shortlist, (float) itemView.getLeft() + 150,(float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconLeft.getHeight()) / 2  ,paintTtf);

                    paint.setColor(Color.WHITE);
                    paint.setTextSize(50);
                    canvas.drawText("Shortlist", (float) itemView.getLeft() + 90,(float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconLeft.getHeight()) / 2 + 60,paint);
                }
            } else if (dX < -rectOffset) {
                paint.setColor(Color.parseColor("#E53935"));
                canvas.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom(), paint);
                if (dX < -iconOffset) {
                    /*canvas.drawBitmap(iconRight,(float) itemView.getRight() - 50 - iconRight.getWidth(),
                            (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconRight.getHeight()) / 2,
                            paint);*/
                    paintTtf.setColor(Color.WHITE);
                    //paintTtf.setStyle(Paint.Style.FILL);
                    paintTtf.setTextSize(72);
                    paintTtf.setTypeface(typeFontello);
                    canvas.drawText(ConstantFontelloID.icon_reject, (float) itemView.getRight() +70 - iconRight.getWidth()-200, (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconRight.getHeight()) / 2 , paintTtf);

                    paint.setColor(Color.WHITE);
                    //paint.setStyle(Paint.Style.FILL);
                    paint.setTextSize(50);
                    canvas.drawText("Reject", (float) itemView.getRight() +40 - iconRight.getWidth()-200,(float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconRight.getHeight()) / 2 + 50, paint);
                }
            }
            }

            else if(strCat.equalsIgnoreCase("Shortlisted")){
                iconLeft = BitmapFactory.decodeResource(RecruiterApplication.getApplication().getApplicationContext().getResources(), R.drawable.unread);
                iconRight = BitmapFactory.decodeResource(RecruiterApplication.getApplication().getApplicationContext().getResources(), R.drawable.reject);
                if (dX > rectOffset) {
                    paint.setColor(Color.parseColor("#6d6d6d"));
                    canvas.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom(), paint);
                    if (dX > iconOffset) {
                        /*canvas.drawBitmap(iconLeft,(float) itemView.getLeft() + 50,
                                (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconLeft.getHeight()) / 2,
                                paint);*/
                        paintTtf.setColor(Color.WHITE);
                        paintTtf.setStyle(Paint.Style.FILL);
                        paintTtf.setTextSize(72);
                        paintTtf.setTypeface(typeFontello);
                        canvas.drawText(ConstantFontelloID.icon_email_outline, (float) itemView.getLeft() + 150,(float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconLeft.getHeight()) / 2,paintTtf);

                        paint.setColor(Color.WHITE);
                        paint.setStyle(Paint.Style.FILL);
                        paint.setTextSize(50);
                        canvas.drawText("Mark as unread", (float) itemView.getLeft() + 20,(float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconLeft.getHeight()) / 2 + 60,paint);

                    }
                } else if (dX < -rectOffset) {
                    paint.setColor(Color.parseColor("#E53935"));
                    canvas.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom(), paint);
                    if (dX < -iconOffset) {
                        /*canvas.drawBitmap(iconRight,(float) itemView.getRight() - 50 - iconRight.getWidth(),
                                (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconRight.getHeight()) / 2,
                                paint);*/
                        paintTtf.setColor(Color.WHITE);
                        paintTtf.setStyle(Paint.Style.FILL);
                        paintTtf.setTextSize(72);
                        paintTtf.setTypeface(typeFontello);
                        canvas.drawText(ConstantFontelloID.icon_reject, (float) itemView.getRight() +30 - iconRight.getWidth()-200, (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconRight.getHeight()) / 2 , paintTtf);

                        paint.setColor(Color.WHITE);
                        paint.setStyle(Paint.Style.FILL);
                        paint.setTextSize(50);
                        canvas.drawText("Reject", (float) itemView.getRight() + 0 - iconRight.getWidth()-200,(float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconRight.getHeight()) / 2 + 50, paint);

                    }
                }
            }
            else if(strCat.equalsIgnoreCase("Rejected")){
                iconLeft = BitmapFactory.decodeResource(RecruiterApplication.getApplication().getApplicationContext().getResources(), R.drawable.shortlist);
                iconRight = BitmapFactory.decodeResource(RecruiterApplication.getApplication().getApplicationContext().getResources(), R.drawable.unread);
                if (dX > rectOffset) {
                    paint.setColor(Color.parseColor("#16a085"));
                    canvas.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom(), paint);
                    if (dX > iconOffset) {
                        /*canvas.drawBitmap(iconLeft,(float) itemView.getLeft() + 50,
                                (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconLeft.getHeight()) / 2,
                                paint);*/
                        paintTtf.setColor(Color.WHITE);
                        //paintTtf.setStyle(Paint.Style.FILL);
                        paintTtf.setTextSize(72);
                        paintTtf.setTypeface(typeFontello);
                        canvas.drawText(ConstantFontelloID.icon_shortlist, (float) itemView.getLeft() + 150,(float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconLeft.getHeight()) / 2,paintTtf);

                        paint.setColor(Color.WHITE);
                        //paint.setStyle(Paint.Style.FILL);
                        paint.setTextSize(50);
                        canvas.drawText("Shortlist", (float) itemView.getLeft() + 90,(float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconLeft.getHeight()) / 2 + 60,paint);
                    }
                } else if (dX < -rectOffset) {
                    paint.setColor(Color.parseColor("#6d6d6d"));
                    canvas.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom(), paint);
                    if (dX < -iconOffset) {
                        /*canvas.drawBitmap(iconRight,(float) itemView.getRight() - 50 - iconRight.getWidth(),
                                (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconRight.getHeight()) / 2,
                                paint);*/
                        paintTtf.setColor(Color.WHITE);
                        //paintTtf.setStyle(Paint.Style.FILL);
                        paintTtf.setTextSize(72);
                        paintTtf.setTypeface(typeFontello);
                        canvas.drawText(ConstantFontelloID.icon_email_outline, (float) itemView.getRight() +30 - iconRight.getWidth()-200, (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconRight.getHeight()) / 2 , paintTtf);

                        paint.setColor(Color.WHITE);
                        //paint.setStyle(Paint.Style.FILL);
                        paint.setTextSize(50);
                        canvas.drawText("Mark as unread", (float) itemView.getRight() - 100 - iconRight.getWidth()-200,(float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconRight.getHeight()) / 2 + 50, paint);
                    }
                }
            }

            else if(strCat.equalsIgnoreCase("Saved")){
                iconLeft = BitmapFactory.decodeResource(RecruiterApplication.getApplication().getApplicationContext().getResources(), R.drawable.shortlist);
                iconRight = BitmapFactory.decodeResource(RecruiterApplication.getApplication().getApplicationContext().getResources(), R.drawable.reject);

                if (dX > rectOffset) {
                    paint.setColor(Color.parseColor("#16a085"));
                    canvas.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom(), paint);
                    if (dX > iconOffset) {
                        /*canvas.drawBitmap(iconLeft,(float) itemView.getLeft() + 50,(float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconLeft.getHeight()) / 2,
                                paint);*/
                        paintTtf.setColor(Color.WHITE);
                        paintTtf.setStyle(Paint.Style.FILL);
                        paintTtf.setTextSize(72);
                        paintTtf.setTypeface(typeFontello);
                        canvas.drawText(ConstantFontelloID.icon_shortlist, (float) itemView.getLeft() + 150,(float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconLeft.getHeight()) / 2,paintTtf);

                        paint.setColor(Color.WHITE);
                        paint.setStyle(Paint.Style.FILL);
                        paint.setTextSize(50);
                        canvas.drawText("Shortlist", (float) itemView.getLeft() + 90,(float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconLeft.getHeight()) / 2 + 60,paint);
                    }
                } else if (dX < -rectOffset) {
                    paint.setColor(Color.parseColor("#E53935"));
                    canvas.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom(), paint);
                    if (dX < -iconOffset) {
                        /*canvas.drawBitmap(iconRight,(float) itemView.getRight() - 50 - iconRight.getWidth(),
                                (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconRight.getHeight()) / 2,
                                paint);*/
                        paintTtf.setColor(Color.WHITE);
                        paintTtf.setStyle(Paint.Style.FILL);
                        paintTtf.setTextSize(72);
                        paintTtf.setTypeface(typeFontello);
                        canvas.drawText(ConstantFontelloID.icon_reject, (float) itemView.getRight() +70 - iconRight.getWidth()-200, (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconRight.getHeight()) / 2, paintTtf);

                        paint.setColor(Color.WHITE);
                        paint.setStyle(Paint.Style.FILL);
                        paint.setTextSize(50);
                        canvas.drawText("Reject", (float) itemView.getRight() +40 - iconRight.getWidth()-200,(float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - iconRight.getHeight()) / 2 + 50, paint);

                    }
                }
            }
            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        /*else{
            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }*/
    }
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                // Let the view holder know that this item is being moved or dragged
                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(ALPHA_FULL);
        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            // Tell the view holder it's time to restore the idle state
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }
    private int convertDpToPx(int dp){
        return Math.round(dp * (ApplicationManager.getContext().getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
