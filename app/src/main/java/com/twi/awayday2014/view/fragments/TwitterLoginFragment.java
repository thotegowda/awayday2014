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

import com.twi.awayday2014.R;
import com.twi.awayday2014.animations.SmoothInterpolator;

public class TwitterLoginFragment extends Fragment {
    private static final long OPEN_ANIMATION_DURATION = 600;
    private static final long CLOSE_ANIMATION_DURATION = 200;

    private View mRootLayout;
    private ObjectAnimator mSlidingPaneOpenAnimator;
    private ObjectAnimator mSlidingPaneCloseAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootLayout = inflater.inflate(R.layout.fragment_twitter_login, container, false);
        initAnimators();
        mRootLayout.setVisibility(View.GONE);
        return mRootLayout;
    }

    public void slideIn(){
        mRootLayout.setVisibility(View.VISIBLE);
        mSlidingPaneCloseAnimator.cancel();
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
        mSlidingPaneCloseAnimator.setFloatValues(0, mRootLayout.getHeight());
        mRootLayout.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mSlidingPaneCloseAnimator.start();
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

}
