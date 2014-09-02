package com.twi.awayday2014.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.AwayDayNotification;
import com.twi.awayday2014.models.NotificationType;
import com.wrapp.floatlabelededittext.FloatLabeledEditText;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import static com.twi.awayday2014.models.NotificationType.fromDisplayText;
import static com.twi.awayday2014.utils.Constants.Notifications.MESSAGE_MAX_LENGTH;
import static com.twi.awayday2014.utils.Constants.Notifications.PREVIEW_MODE;
import static com.twi.awayday2014.utils.Constants.Notifications.PREVIEW_NOTIFICATION;
import static com.twi.awayday2014.utils.Constants.Notifications.TITLE_MAX_LENGTH;


public class PushActivity extends Activity {
    private FloatLabeledEditText notificationsTitle;
    private FloatLabeledEditText notificationsText;
    private NotificationType notificationType;
    private Spinner notificationTypeSpinner;
    private boolean pushVerified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_push);

        setupEditTexts();
        setupButtons();
        notificationTypeSpinner = (Spinner) findViewById(R.id.notification_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.notification_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notificationTypeSpinner.setAdapter(adapter);
        notificationTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                notificationType = fromDisplayText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getActionBar().setHomeButtonEnabled(true);
        pushVerified = false;
    }

    private void setupButtons() {
        Button previewButton = (Button) findViewById(R.id.preview_button);
        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PushActivity.this, NotificationsActivity.class);
                intent.putExtra(PREVIEW_MODE, true);
                intent.putExtra(PREVIEW_NOTIFICATION, new AwayDayNotification(
                        notificationsTitle.getText().toString(),
                        DateTime.now(),
                        notificationsText.getText().toString(),
                        notificationType
                ));
                PushActivity.this.startActivity(intent);
            }
        });
        final Button pushButton = (Button) findViewById(R.id.push_button);
        final TextView warningMsg = (TextView) findViewById(R.id.warningMsg);
        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pushVerified) {
                    pushButton.setBackgroundColor(Color.parseColor("#f3383e"));
                    warningMsg.setVisibility(View.VISIBLE);
                    pushVerified = true;
                } else {
                    if(pushNotification()){
                        finish();
                    }else{
                        Toast.makeText(PushActivity.this, "Some error occured while sending push notification", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setupEditTexts() {
        InputFilter[] filter = new InputFilter[1];
        filter[0] = new InputFilter.LengthFilter(TITLE_MAX_LENGTH);
        notificationsTitle = (FloatLabeledEditText) findViewById(R.id.notification_title);
        EditText notificationsTitleEditText = notificationsTitle.getEditText();
        notificationsTitleEditText.setFilters(filter);
        notificationsTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int remainingChar = TITLE_MAX_LENGTH - s.length();
                if (s.length() > 0) {
                    notificationsTitle.setHint("Title " + remainingChar);
                } else {
                    notificationsTitle.setHint("Title");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        filter = new InputFilter[1];
        filter[0] = new InputFilter.LengthFilter(MESSAGE_MAX_LENGTH);
        notificationsText = (FloatLabeledEditText) findViewById(R.id.notification_text);
        EditText notificationsTextEditText = notificationsText.getEditText();
        notificationsTextEditText.setFilters(filter);
        notificationsTextEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int remainingChar = MESSAGE_MAX_LENGTH - s.length();
                if (s.length() > 0) {
                    notificationsText.setHint("Message " + remainingChar);
                } else {
                    notificationsText.setHint("Message");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean pushNotification() {
        try {
            String title = notificationsTitle.getText().toString().trim();
            String msg = notificationsText.getText().toString().trim();
            String type = notificationType.getDisplayText();
            if (title.length() > 1 && msg.length() > 1 && type.length() > 1) {
                ParseQuery androidDeviceMatchQuery = ParseInstallation.getQuery();
                androidDeviceMatchQuery.whereEqualTo("deviceType", "android");
                ParsePush push = new ParsePush();
                push.setQuery(androidDeviceMatchQuery);

                JSONObject data = new JSONObject();
                data.put("action", "com.twi.awayday2014.UPDATE_STATUS");
                data.put("type", type);
                data.put("heading", title);
                data.put("message", msg);
                push.setData(data);
                push.sendInBackground();
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        };
        return false;
    }
}
