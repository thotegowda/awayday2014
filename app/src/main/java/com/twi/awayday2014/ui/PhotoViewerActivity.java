package com.twi.awayday2014.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.twi.awayday2014.Utils;

public class PhotoViewerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = getIntent().getStringExtra(Utils.EXTRA_URL);
        if (url == null) {
            finish();
            return;
        }

        ImageView photoView = new ImageView(this);
        photoView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        setContentView(photoView);

        Picasso.with(this).load(url).into(photoView);
    }
}
