package com.example.pengout.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;
import com.example.pengout.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class EventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private String eventID, currentUserId;
    private final String TAG = "IN EVENT ACTIVITY";
    private Button registerButton;
    private Context mContext;


    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public final static String default_notification_channel_id = "default";

    Calendar now = Calendar.getInstance();
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        eventID = getIntent().getExtras().get("visit_event_id").toString();
        currentUserId = getIntent().getExtras().get("current_user_id").toString();
        registerButton = findViewById(R.id.registerButton);


        datePickerDialog =  DatePickerDialog.newInstance(
                EventActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );


        timePickerDialog =  TimePickerDialog.newInstance(
                EventActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                false
        );


        /*
        canceling notification
        NotifyMe.cancel(getApplicationContext(), "test");
         */

        mContext = this;

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getFragmentManager(), "Datetimepickerdialog");

                Toast.makeText(mContext, "After Schedule Notification", Toast.LENGTH_SHORT).show();
            }
        });
        Log.d(TAG, "onCreate: ");

    }










    @Override
    protected void onStop() {
        super.onStop();
    }



    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
        now.set(Calendar.MINUTE, minute);

        NotifyMe notifyMe = new NotifyMe.Builder(this)
                .title("Title of Notification")
                .content("Content if it")
                .color(0, 255, 255, 255)
                .led_color(255, 255, 255, 255)
                .time(now)
//                .addAction(new Intent(EventActivity.this, NotificationDetailsActivity.class), "Snooze", false)
                .key("text")
                .addAction(new Intent(EventActivity.this, NotificationDetailsActivity.class), "Details", true, false)
//                .addAction(new Intent(EventActivity.this, NotificationDetailsActivity.class), "Done")
                .large_icon(R.mipmap.ic_launcher_round)
                .build();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, monthOfYear);
        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        timePickerDialog.show(getFragmentManager(), "Time piker daylog");
    }


}
