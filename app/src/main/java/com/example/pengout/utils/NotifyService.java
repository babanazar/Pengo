package com.example.pengout.utils;


import android.R;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.pengout.view.activity.NotificationDetailsActivity;

import static com.example.pengout.view.activity.EventActivity.NOTIFICATION_CHANNEL_ID;
import static com.example.pengout.view.activity.EventActivity.default_notification_channel_id;


/**
 * This service is started when an Alarm has been raised
 * <p>
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 *
 * @author paul.blundell
 */
public class NotifyService extends Service {

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    // Unique id to identify the notification.
    private static final int NOTIFICATION = 123;
    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.blundell.tut.service.INTENT_NOTIFY";
    // The system notification manager
    private NotificationManager mNM;

    private Context mContext;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mContext = getApplicationContext();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalYESSService", "Received start id " + startId + ": " + intent);

        // If this service was started by out AlarmTask intent then we want to show our notification
        if (intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification();

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    private void showNotification() {
        Log.v("SHOW NOTIFICATION", "*/-/-*/-*/-*/-*/-*/-*/-*/trying to notify-*/-*/*-*-*/54");
        // This is the 'title' of the notification
        CharSequence title = "Alarm!!";
        // This is the icon to use on the notification
        int icon = R.drawable.ic_dialog_alert;
        // This is the scrolling text of the notification
        CharSequence text = "Your notification time is upon us.";
        // What time to show on the notification
        long time = System.currentTimeMillis();


        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, NotificationDetailsActivity.class), 0);

        //        Notification notification = new Notification(icon, text, time);
        Notification notification = getNotification("content of notify dir", contentIntent, mNM);


        // Set the info for the views that show in the notification panel.
//        notification.setLatestEventInfo(this, title, text, contentIntent);
//        notification.contentIntent = contentIntent;

        // Clear the notification when it is pressed
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Send the notification to the system.
        mNM.notify(NOTIFICATION, notification);

        // Stop the service when we are finished
        stopSelf();
    }

    private Notification getNotification(String content, PendingIntent contentIntent, NotificationManager notificationManager) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, default_notification_channel_id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id = NOTIFICATION + "";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);

            if (mChannel == null) {
                mChannel = new NotificationChannel(id, "Title", importance);
                mChannel.enableVibration(true);
                notificationManager.createNotificationChannel(mChannel);
            }

            builder.setContentTitle("Scheduled Notification");
            builder.setContentText(content);
            builder.setContentIntent(contentIntent);
            builder.setSmallIcon(com.example.pengout.R.drawable.ic_launcher_foreground);
            builder.setAutoCancel(true);
            builder.setTicker("aMessage");
            return builder.build();
        } else {
            builder.setContentTitle("Scheduled Notification");
            builder.setContentText(content);
            builder.setContentIntent(contentIntent);
            builder.setSmallIcon(com.example.pengout.R.drawable.ic_launcher_foreground);
            builder.setAutoCancel(true);
            builder.setTicker("aMessage");
            return builder.build();
        }
    }
}
