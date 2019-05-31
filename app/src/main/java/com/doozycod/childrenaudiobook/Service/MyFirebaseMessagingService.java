package com.doozycod.childrenaudiobook.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.doozycod.childrenaudiobook.Activities.LibraryActivity;
import com.doozycod.childrenaudiobook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.doozycod.childrenaudiobook.Service.Config.NOTIFICATION_ID;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("Token=>>>", s);


        storeRegIdInPref(s);

        sendRegistrationToServer(s);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", s);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        Map<String, String> data = remoteMessage.getData();
        ShowNotification(notification, data);
    }

    private void ShowNotification(RemoteMessage.Notification notification, Map<String, String> data) {

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

//        Intent intent = new Intent(this, LibraryActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Intent notifyIntent = new Intent(this, LibraryActivity.class);
// Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/raw/notification");


//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
//        builder.setContentIntent(notifyPendingIntent);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(data.get("title"))
                .setContentText(data.get("text"))
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(notifyPendingIntent)
                .setContentInfo("ANY")
                .setLargeIcon(icon)
                .setColor(Color.RED)
                .setSmallIcon(R.mipmap.ic_launcher);


        notificationBuilder.setLights(Color.YELLOW, 1000, 300);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);

        //add code here
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }
}
