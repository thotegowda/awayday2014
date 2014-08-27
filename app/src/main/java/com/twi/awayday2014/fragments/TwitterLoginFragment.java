package com.twi.awayday2014.fragments;

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
import com.twi.awayday2014.animation.SmoothInterpolator;

public class TwitterLoginFragment extends Fragment {
    public static final long OPEN_ANIMATION_DURATION = 600;
    public static final long CLOSE_ANIMATION_DURATION = 400;

    private View mRootLayout;
    private ObjectAnimator mSlidingPaneOpenAnimator;
    private ObjectAnimator mSlidingPaneCloseAnimator;
    private int rootLayoutHeight;
    private OnLoginClickListener listener;

    public interface OnLoginClickListener {
        public void onLoginButtonClicked();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootLayout = inflater.inflate(R.layout.fragment_twitter_login, container, false);
        initAnimators();
        mRootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                rootLayoutHeight = mRootLayout.getHeight();
                mRootLayout.setVisibility(View.GONE);
            }
        });

        mRootLayout.findViewById(R.id.twitter_login).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                listener.onLoginButtonClicked();
            }
        });
        return mRootLayout;
    }

    public void setOnLoginClickListener(OnLoginClickListener listener) {
        this.listener = listener;
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

    public int getRootLayoutHeight() {
        return rootLayoutHeight;
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

}
