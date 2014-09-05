package com.twi.awayday2014.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.services.parse.PresenterParseDataFetcher;
import com.twi.awayday2014.utils.Blur;
import com.twi.awayday2014.utils.Fonts;
import com.twi.awayday2014.view.custom.CircularImageView;

public class SpeakerDetailsActivity extends Activity {

    private CircularImageView profileImageView;
    private ImageView backgroundImage;
    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_speaker_details);

        String presenterId = getIntent().getStringExtra("presenter_id");
        AwayDayApplication application = (AwayDayApplication) getApplication();
        PresenterParseDataFetcher presenterParseDataFetcher = application.getPresenterParseDataFetcher();
        presenter = presenterParseDataFetcher.getDataById(presenterId);
        setupHeader();
    }

    public void setupHeader() {
        backgroundImage = (ImageView) findViewById(R.id.backgroundImage);
        profileImageView = (CircularImageView) findViewById(R.id.profile_image);
        TextView presenterName = (TextView) findViewById(R.id.speakerName);
        presenterName.setTypeface(Fonts.openSansRegular(this));
        presenterName.setText(presenter.getName());

        if(presenter.getImageUrl() != null){
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

    public class BlurBackgroundTask extends AsyncTask<Void, Void, Bitmap> {

        private Bitmap src;

        public BlurBackgroundTask(Bitmap src){

            this.src = src;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Blur blur = new Blur(SpeakerDetailsActivity.this);
            Bitmap result = blur.blur(src, 14);
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Bitmap placeHolderImage = BitmapFactory.decodeResource(getResources(), R.drawable.awayday_2014_background_blur);
            BitmapDrawable[] bitmapDrawables = new BitmapDrawable[2];
            bitmapDrawables[0] = new BitmapDrawable(getResources(), placeHolderImage);
            bitmapDrawables[1] = new BitmapDrawable(getResources(), bitmap);
            TransitionDrawable transitionDrawable = new TransitionDrawable(bitmapDrawables);
            backgroundImage.setImageDrawable(transitionDrawable);
            transitionDrawable.startTransition(500);
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
