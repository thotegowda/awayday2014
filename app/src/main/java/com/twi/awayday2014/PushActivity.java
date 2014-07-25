package com.twi.awayday2014;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.parse.ParsePush;


public class PushActivity extends Activity {

    private TextView notificationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_push);

        notificationText = (TextView) findViewById(R.id.notification_text);
        findViewById(R.id.push_it).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushIt();
                notificationText.setText("");
            }
        });
    }

    private void pushIt() {
        String message = notificationText.getText().toString().trim();
        if (message.length() > 5) {
            ParsePush push = new ParsePush();
            push.setChannel("AwayDay");
            push.setMessage(message);
            push.sendInBackground();
        }
    }
}
