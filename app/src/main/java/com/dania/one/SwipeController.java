package com.dania.one;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeController extends ItemTouchHelper.Callback
{
    private final Context mContext;
    private final SwipeControllerActions mSwipeControllerActions;

    private final Drawable mReplyIcon;
    private final Drawable mReplyIconBackground;

    private RecyclerView.ViewHolder mCurrentViewHolder;
    private View mView;

    private float mDx = 0f;
    private float mReplyButtonProgress = 0f;
    private long  mLastReplyButtonAnimationTime = 0;

    private boolean mSwipeBack = false;
    private boolean mIsVibrating = false;
    private boolean mStartTracking = false;

    private int mBackgroundColor = 0x20606060;

    public SwipeController(Context aContext, SwipeControllerActions aSwipeControllerActions)
    {
        mContext = aContext;
        mSwipeControllerActions = aSwipeControllerActions;
        mReplyIcon = ContextCompat.getDrawable(aContext, R.drawable.vector_reply);
        mReplyIconBackground = ContextCompat.getDrawable(aContext, R.drawable.o_upper);
    }

    public SwipeController(
            Context aContext,
            SwipeControllerActions aSwipeControllerActions,
            int aReplyIcon,
            int aReplyIconBackground,
            int backgroundColor)
    {
        mContext = aContext;
        mSwipeControllerActions = aSwipeControllerActions;
        mReplyIcon = ContextCompat.getDrawable(getContext(), aReplyIcon);
        mReplyIconBackground = ContextCompat.getDrawable(getContext(), aReplyIconBackground);
        mBackgroundColor = backgroundColor;
    }

    @Override
    public int getMovementFlags(
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder)
    {
        mView = viewHolder.itemView;
        ViewCompat.setElevation(mView,5);
        return ItemTouchHelper.Callback.makeMovementFlags(
                ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection){
        if (mSwipeBack){
            mSwipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(
            @NonNull Canvas c,
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            float dX, float dY, int actionState,
            boolean isCurrentlyActive)
    {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            setTouchListener(recyclerView, viewHolder);
        }
        if (mView.getTranslationX() < convertToDp(150) || dX < mDx ){
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            mDx = dX;
            mStartTracking = true;
        }
        mCurrentViewHolder = viewHolder;
        drawReplyButton(c);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSwipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if (mSwipeBack){
                    if (Math.abs(mView.getTranslationX()) >= convertToDp(50)){
                        getSwipeControllerActions().onSwipePerformed(viewHolder.getAdapterPosition());
                    }
                }
                return false;
            }
        });
    }


    private void drawReplyButton(@NonNull Canvas canvas)
    {
        if (mCurrentViewHolder == null){
            return;
        }


        float translationX = mView.getTranslationX();
        long newTime = System.currentTimeMillis();
        long dt = Math.min(17, newTime - mLastReplyButtonAnimationTime);
        mLastReplyButtonAnimationTime = newTime;
        boolean showing = false;
        if (translationX >= convertToDp(30)){
            showing = true;
        }
        if (showing){
            if (mReplyButtonProgress < 1.0f){
                mReplyButtonProgress += dt / 180.0f;
                if (mReplyButtonProgress > 1.0f){
                    mReplyButtonProgress = 1.0f;
                } else {
                    mView.invalidate();
                }
            }
        } else if (translationX <= 0.0f){
            mReplyButtonProgress = 0f;
            mStartTracking = false;
            mIsVibrating = false;
        } else {
            if (mReplyButtonProgress > 0.0f){
                mReplyButtonProgress -= dt / 180.0f;
                if (mReplyButtonProgress < 0.1f){
                    mReplyButtonProgress = 0f;
                }
            }
            mView.invalidate();
        }
        int alpha;
        float scale;
        if (showing){
            if (mReplyButtonProgress <= 0.8f){
                scale = 1.2f * (mReplyButtonProgress / 0.8f);
            } else{
                scale = 1.2f - 0.2f * ((mReplyButtonProgress - 0.8f) / 0.2f);
            }
            alpha = Math.min(255, 255 * ((int)(mReplyButtonProgress / 0.8f)));
        } else{
            scale = mReplyButtonProgress;
            alpha = Math.min(255, 255 * (int)mReplyButtonProgress);
        }
        mReplyIconBackground.setAlpha(alpha);
        mReplyIcon.setAlpha(alpha);
        if (mStartTracking){
            if (!mIsVibrating && mView.getTranslationX() >= convertToDp(100)){
                mView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            }
            mIsVibrating = true;
        }

        int x;
        float y;
        if (mView.getTranslationX() > convertToDp(130)){
            x = convertToDp(130) / 2;
        }
        else {
            x = (int) mView.getTranslationX() /2;
        }

        y = mView.getTop() + ((float) mView.getMeasuredHeight() /2);
        mReplyIconBackground.setColorFilter(mBackgroundColor, PorterDuff.Mode.MULTIPLY);

        int mReplyBackgroundOffset = 18;
        mReplyIconBackground.setBounds(new Rect(
                (int)(x - convertToDp(mReplyBackgroundOffset) * scale),
                (int)(y - convertToDp(mReplyBackgroundOffset) * scale),
                (int)(x + convertToDp(mReplyBackgroundOffset) * scale),
                (int)(y + convertToDp(mReplyBackgroundOffset) * scale)
        ));
        mReplyIconBackground.draw(canvas);

        int mReplyIconXOffset = 12;
        int mReplyIconYOffset = 11;
        mReplyIcon.setBounds(new Rect(
                (int)(x - convertToDp(mReplyIconXOffset) * scale),
                (int)(y - convertToDp(mReplyIconYOffset) * scale),
                (int)(x + convertToDp(mReplyIconXOffset) * scale),
                (int)(y + convertToDp(mReplyIconYOffset) * scale)
        ));
        mReplyIcon.draw(canvas);

        mReplyIconBackground.setAlpha(255);
        mReplyIcon.setAlpha(255);
    }

    private int convertToDp(int pixels){
        return Utils.getDP((float) pixels, getContext());
    }

    public Context getContext() {
        return mContext;
    }

    public SwipeControllerActions getSwipeControllerActions() {
        return mSwipeControllerActions;
    }

    public interface SwipeControllerActions {
        void onSwipePerformed(int position);
    }
}