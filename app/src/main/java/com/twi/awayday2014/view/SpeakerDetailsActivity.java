package com.twi.awayday2014.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.utils.Blur;
import com.twi.awayday2014.view.custom.CircularImageView;

public class SpeakerDetailsActivity extends Activity {

    private CircularImageView profileImageView;
    private TextView name;
    private TextView headline;
    private TextView description;
    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_speaker_details);

        String presenterId = getIntent().getStringExtra("presenter_id");
        Presenter presenter = Presenter.findById(Presenter.class, Long.valueOf(presenterId));
        setupHeader();
    }

    public void setupHeader() {
        backgroundImage = (ImageView) findViewById(R.id.backgroundImage);
        profileImageView = (CircularImageView) findViewById(R.id.profile_image);
        name = (TextView)findViewById(R.id.speakerName);
        Bitmap placeHolderImage = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder);
//        new BlurBackgroundTask(placeHolderImage).execute();
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

}
