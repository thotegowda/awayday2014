package com.twi.awayday2014.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.services.parse.PresenterParseDataFetcher;
import com.twi.awayday2014.utils.Blur;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.view.custom.CircularImageView;
import com.twi.awayday2014.view.fragments.SpeakersFragment;

public class SpeakerDetailsActivity extends Activity {

    private CircularImageView profileImageView;
    private ImageView backgroundImage;
    private Presenter presenter;
    private int actionbarIconAlpha;
    private Drawable appIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_speaker_details);

        String presenterId = getIntent().getStringExtra(SpeakersFragment.PRESENTER_ID);
        AwayDayApplication application = (AwayDayApplication) getApplication();
        PresenterParseDataFetcher presenterParseDataFetcher = application.getPresenterParseDataFetcher();
        presenter = presenterParseDataFetcher.getDataById(presenterId);
        if(presenter == null){
            Toast.makeText(this, "Something wrong with presenter information. We will be fixing this.", Toast.LENGTH_SHORT).show();
            finish();
        }
        setupHeader();
        setupDetails();
        setupActionbar();
    }

    private void setupActionbar() {
        appIcon = getResources().getDrawable(R.drawable.ic_launcher);
        appIcon.setAlpha(255);
        getActionBar().setIcon(appIcon);
    }

    private void setupDetails() {
        TextView descriptionText = (TextView) findViewById(R.id.descriptionText);
        descriptionText.setText(presenter.getWriteUp());

        if(presenter.getAwayDayWriteup() != null && !presenter.getAwayDayWriteup().isEmpty()){
            View divider = findViewById(R.id.divider);
            divider.setVisibility(View.VISIBLE);
            TextView awaydayText = (TextView) findViewById(R.id.awayDayText);
            awaydayText.setVisibility(View.VISIBLE);
            awaydayText.setText(presenter.getAwayDayWriteup());
        }
    }

    public void setupHeader() {
        backgroundImage = (ImageView) findViewById(R.id.backgroundImage);
        profileImageView = (CircularImageView) findViewById(R.id.profile_image);
        final TextView presenterName = (TextView) findViewById(R.id.speakerName);
        presenterName.setTypeface(Fonts.openSansRegular(this));
        presenterName.setText(presenter.getName());

        TextView blog = (TextView) findViewById(R.id.blog);
        blog.setTypeface(Fonts.openSansLightItalic(this));
        if(presenter.getLink() != null && !presenter.getLink().isEmpty()){
            blog.setVisibility(View.VISIBLE);
            blog.setText(presenter.getLink());
        }

        View speakerInfo = findViewById(R.id.speakerDetails);
        speakerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = presenter.getLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        if(presenter.getImageUrl() != null ){
            Picasso.with(this)
                    .load(presenter.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(profileImageView);

            ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.theme_color));
            Picasso.with(this)
                    .load(presenter.getImageUrl())
                    .placeholder(colorDrawable)
                    .error(colorDrawable)
                    .transform(new BlurTransformation())
                    .into(backgroundImage);
        }
    }

    public class BlurTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            Blur blur = new Blur(SpeakerDetailsActivity.this);
            Bitmap result = blur.blur(source, 14);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }

}
