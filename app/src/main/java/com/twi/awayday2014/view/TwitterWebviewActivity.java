package com.twi.awayday2014.view;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;

public class TwitterWebviewActivity extends Activity {
    private Intent mIntent;
    private ProgressDialog progress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_webview);
        mIntent = getIntent();
        String url = (String) mIntent.getExtras().get("URL");
        Log.e("DEBUG", url);
        WebView webView = (WebView) findViewById(R.id.webview);
        progress = ProgressDialog.show(this, "", "Loading...");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String callbackUrl = ((AwayDayApplication) getApplication()).getTwitterService().getPreference().getCallbackUrl();
                if (url.contains(callbackUrl)) {
                    Uri uri = Uri.parse(url);
                    String oauthVerifier = uri.getQueryParameter("oauth_verifier");
                    mIntent.putExtra("oauth_verifier", oauthVerifier);
                    setResult(RESULT_OK, mIntent);
                    finish();
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progress.dismiss();
                super.onPageFinished(view, url);
            }
        });
        webView.loadUrl(url);
    }
}
