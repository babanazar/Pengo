package com.example.pengout.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;
import com.example.pengout.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.HashMap;

public class EventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private String eventID, currentUserId, eventName, eventPlace, eventTime, eventDate;
    private final String TAG = "IN EVENT ACTIVITY";
    private Button registerButton;
    private Context mContext;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public final static String default_notification_channel_id = "default";

    private Calendar now = Calendar.getInstance();
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;

    private final Calendar timeOfEvent = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        eventID = getIntent().getExtras().get("visit_event_id").toString();
        eventName = getIntent().getExtras().get("visit_event_name").toString();
        eventPlace = getIntent().getExtras().get("visit_event_place").toString();
        eventTime = getIntent().getExtras().get("visit_event_time").toString();
        eventDate = getIntent().getExtras().get("visit_event_date").toString();

        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        currentUserId = getIntent().getExtras().get("current_user_id").toString();


        rootRef.child("clicked").child(currentUserId).child(eventID).child("timestamp").setValue(System.currentTimeMillis())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: clicked info is saved");
                        } else {
                            Log.d(TAG, "onComplete: clicked info save fail");
                        }
                    }
                });


        // set date and time here
        int yearNumber = Integer.parseInt(eventDate.substring(11));
        timeOfEvent.set(Calendar.YEAR, yearNumber);


        int dayOfMonth = Integer.parseInt(eventDate.substring(8, 10)) - 1;
        timeOfEvent.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        int monthNumber = getMonthNumber(eventDate);
        timeOfEvent.set(Calendar.MONTH, monthNumber);

        timeOfEvent.set(Calendar.HOUR_OF_DAY, 9);
        timeOfEvent.set(Calendar.MINUTE, 30);


        registerButton = findViewById(R.id.registerButton);


        /*
        canceling notification
        NotifyMe.cancel(getApplicationContext(), "test");
         */

        mContext = this;

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setNotification(timeOfEvent);


                rootRef.child("registered").child(currentUserId).child(eventID).child("timestamp").setValue(System.currentTimeMillis())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "onComplete: registered save success");
                                    Toast.makeText(mContext, "Save to table is successful", Toast.LENGTH_SHORT).show();
                                }else{
                                    Log.d(TAG, "onComplete: registered save fail");
                                }
                            }
                        });
            }
        });

    }

    private int getMonthNumber(String eventDate) {
        String month = eventDate.substring(4, 7);
        switch (month) {
            case "Jan":
                return 0;

            case "Feb":
                return 1;

            case "Mar":
                return 2;

            case "Apr":
                return 3;

            case "May":
                return 4;

            case "Jun":
                return 5;

            case "Jul":
                return 6;

            case "Aug":
                return 7;

            case "Sep":
                return 8;

            case "Oct":
                return 9;

            case "Nov":
                return 10;

            case "Dec":
                return 11;

            default:
                return -1;
        }
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

    public void setNotification(Calendar date) {

        NotifyMe notifyMe = new NotifyMe.Builder(this)
                .title(eventName)
                .content(eventName + " is approaching! It's on " + eventTime + " tomorrow.")
                .color(0, 255, 255, 255)
                .led_color(255, 255, 255, 255)
                .time(date)
//                .addAction(new Intent(EventActivity.this, NotificationDetailsActivity.class), "Snooze", false)
                .key("text")
                .addAction(new Intent(EventActivity.this, NotificationDetailsActivity.class), "Details", true, false)
//                .addAction(new Intent(EventActivity.this, NotificationDetailsActivity.class), "Done")
                .large_icon(R.mipmap.ic_launcher_round)
                .build();
    }
}
