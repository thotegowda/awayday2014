package com.twi.awayday2014.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.parse.ParsePush;
import com.twi.awayday2014.R;

import org.json.JSONException;
import org.json.JSONObject;


public class PushActivity extends Activity {

    private TextView notificationText;
    private CheckBox displayNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_push);

        displayNotification = (CheckBox) findViewById(R.id.display_notification);

        notificationText = (TextView) findViewById(R.id.notification_text);
        findViewById(R.id.push_it).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushIt(displayNotification.isChecked());
                notificationText.setText("");
            }
        });
    }

    private void pushIt(boolean notificationEnabled) {
        try {
            String message = notificationText.getText().toString().trim();
            if (message.length() > 1) {
                ParsePush push = new ParsePush();
                push.setChannel("AwayDay");
                if (notificationEnabled) {
                    push.setMessage(message);
                } else {
                    JSONObject data = new JSONObject();
                    data.put("action", "com.twi.awayday2014.UPDATE_STATUS");
                    data.put("name", "Thote");
                    data.put("message", message);
                    push.setData(data);
                }
                push.sendInBackground();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        };


    }
}
