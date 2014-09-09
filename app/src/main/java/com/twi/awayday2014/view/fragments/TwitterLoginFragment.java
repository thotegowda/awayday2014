package com.twi.awayday2014.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twi.awayday2014.R;
import com.twi.awayday2014.utils.Fonts;

public class TwitterLoginFragment extends SlidingFragment {
    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_twitter_login, container, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        TextView infoText = (TextView) mRootLayout.findViewById(R.id.infoText);
        infoText.setTypeface(Fonts.openSansLight(getActivity()));

        TextView loginButtonText = (TextView) mRootLayout.findViewById(R.id.loginButtonText);
        loginButtonText.setTypeface(Fonts.openSansRegular(getActivity()));
        return mRootLayout;
    }

}
