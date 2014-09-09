package com.twi.awayday2014.view.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.twi.awayday2014.animations.SmoothInterpolator;

public abstract class SlidingFragment extends Fragment{
    public static final long OPEN_ANIMATION_DURATION = 600;
    public static final long CLOSE_ANIMATION_DURATION = 400;

    protected View mRootLayout;
    protected ObjectAnimator mSlidingPaneOpenAnimator;
    protected ObjectAnimator mSlidingPaneCloseAnimator;
    private int rootLayoutOriginalHeight;
    private boolean isOpen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootLayout = getLayout(inflater, container);
        initAnimators();
        mRootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                rootLayoutOriginalHeight = mRootLayout.getHeight();
                mRootLayout.setVisibility(View.GONE);
            }
        });
        return mRootLayout;
    }

    protected abstract View getLayout(LayoutInflater inflater, ViewGroup container);

    public void slideIn(){
        mRootLayout.setVisibility(View.VISIBLE);
        mSlidingPaneCloseAnimator.cancel();
        isOpen = true;
        mRootLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mSlidingPaneOpenAnimator.setFloatValues(mRootLayout.getHeight(), 0);
                mRootLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                mSlidingPaneOpenAnimator.start();

                mRootLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
    }

    public void slideOut(){
        mSlidingPaneOpenAnimator.cancel();
        isOpen = false;
        mSlidingPaneCloseAnimator.setFloatValues(0, mRootLayout.getHeight());
        mRootLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mSlidingPaneCloseAnimator.start();
    }

    public int getRootLayoutOriginalHeight() {
        return rootLayoutOriginalHeight;
    }

    private void initAnimators() {
        mSlidingPaneOpenAnimator = ObjectAnimator.ofFloat(mRootLayout, "translationY", 0);
        mSlidingPaneOpenAnimator.setInterpolator(new SmoothInterpolator());
        mSlidingPaneOpenAnimator.setDuration(OPEN_ANIMATION_DURATION);
        mSlidingPaneOpenAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationCancel(Animator animation) {
                mRootLayout.setLayerType(View.LAYER_TYPE_NONE, null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRootLayout.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        mSlidingPaneCloseAnimator = ObjectAnimator.ofFloat(mRootLayout, "translationY", 0);
        mSlidingPaneCloseAnimator.setDuration(CLOSE_ANIMATION_DURATION);
        mSlidingPaneCloseAnimator.setInterpolator(new SmoothInterpolator());
        mSlidingPaneCloseAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                mRootLayout.setLayerType(View.LAYER_TYPE_NONE, null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRootLayout.setLayerType(View.LAYER_TYPE_NONE, null);
                mRootLayout.setVisibility(View.GONE);
            }
        });
    }

    public boolean isOpen() {
        return isOpen;
    }
}