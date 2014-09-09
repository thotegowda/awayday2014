package com.twi.awayday2014.view.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twi.awayday2014.R;

public class TwitterLoginFragment extends SlidingFragment {
    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_twitter_login, container, false);
    }
}
