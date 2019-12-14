package com.example.pengout.view.activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.pengout.R;
import com.example.pengout.utils.MyNotificationPublisher;
import com.example.pengout.utils.ScheduleClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventActivity extends AppCompatActivity {

    private String eventID, currentUserId;
    private final String TAG = "IN EVENT ACTIVITY";
    private Button registerButton;
    private Context mContext;

    private ScheduleClient scheduleClient;
    private DatePicker picker;

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public final static String default_notification_channel_id = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        eventID = getIntent().getExtras().get("visit_event_id").toString();
        currentUserId = getIntent().getExtras().get("current_user_id").toString();
        registerButton = findViewById(R.id.registerButton);

        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

        picker = (DatePicker) findViewById(R.id.scheduleTimePicker);

//        createNotificationChannel();

        mContext = this;

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the date from our datepicker
                int day = picker.getDayOfMonth();
                int month = picker.getMonth();
                int year = picker.getYear();
                // Create a new calendar set to the date chosen
                // we set the time to midnight (i.e. the first minute of that day)
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
                scheduleClient.setAlarmForNotification(mContext, c);
                // Notify the user what they just did
//                triggerNotification();
//                updateLabel();
//                scheduleNotification(mContext, 500, 23);
                Toast.makeText(mContext, "After Schedule Notification", Toast.LENGTH_SHORT).show();
            }
        });
        Log.d(TAG, "onCreate: ");

    }


    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
//        Date date = myCalendar.getTime();
//        btnDate.setText(sdf.format(date));
        scheduleNotification(getNotification("Notofocatioandlnfsv "), 1000);
    }

    private void scheduleNotification(Notification notification, long delay) {
//        Intent notificationIntent = new Intent(this, NotificationDetailsActivity.class);
        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Intent intent = new Intent(this, NotificationDetailsActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, delay, pendingIntent);
    }


    private Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }

    private void triggerNotification() {
        Intent intent = new Intent(this, NotificationDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "13")
                .setSmallIcon(R.drawable.github)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.github))
                .setContentTitle("Notification Title")
                .setContentText("Notification Text")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Notification Text"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setChannelId("13")
                .setAutoCancel(true);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(23, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("13", "Upcoming Event", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Event Description");
            notificationChannel.setShowBadge(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

//    public void scheduleNotification(Context context, long delay, int notificationId) {//delay is after how much time(in millis) from current time you want to schedule the notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
//                .setContentTitle(context.getString(R.string.title))
//                .setContentText(context.getString(R.string.content))
//                .setAutoCancel(true)
//                .setSmallIcon(R.drawable.ic_chat)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_chat))
//                .setStyle(new NotificationCompat.BigTextStyle().bigText("This is text"))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(true)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//
//        Intent intent = new Intent(context, EventActivity.class);
//        PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        builder.setContentIntent(activity);
//
//        Notification notification = builder.build();
//
//        Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
//        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId);
//        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        long futureInMillis = SystemClock.elapsedRealtime() + delay;
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
//    }

    @Override
    protected void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }
}
