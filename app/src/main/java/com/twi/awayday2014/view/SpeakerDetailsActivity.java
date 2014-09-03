package com.twi.awayday2014.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presenter;

public class SpeakerDetailsActivity extends Activity {

    private ImageView profileImageView;
    private TextView name;
    private TextView headline;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_speaker_details);

        String presenterId = getIntent().getStringExtra("presenter_id");
        Presenter presenter = Presenter.findById(Presenter.class, Long.valueOf(presenterId));
        setupViews();
        bind(presenter);
    }

    public void setupViews() {
        profileImageView = ((ImageView) findViewById(R.id.profile_image));
        name = (TextView)findViewById(R.id.speaker_name);
        headline = (TextView) findViewById(R.id.speaker_headline);
        description = (TextView) findViewById(R.id.speaker_description);
    }

    public void bind(Presenter presenter) {
        profileImageView.setImageResource(presenter.profileResource());
        name.setText(presenter.getName());
        headline.setText(presenter.getTitle());
        description.setText(presenter.getDescription());

    }
}
