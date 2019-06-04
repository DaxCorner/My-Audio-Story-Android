package com.doozycod.childrenaudiobook.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.doozycod.childrenaudiobook.Activities.LibraryActivity;
import com.doozycod.childrenaudiobook.Activities.SignUpActivity;
import com.doozycod.childrenaudiobook.Activities.SplashActivity;
import com.doozycod.childrenaudiobook.R;
import com.doozycod.childrenaudiobook.Utils.SharedPreferenceMethod;
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

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.constraint.Constraints.TAG;
import static com.doozycod.childrenaudiobook.Service.Config.NOTIFICATION_ID;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private NotificationChannel mChannel;
    private NotificationManager notifManager;
    Bundle bundle = new Bundle();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data " + remoteMessage.getData().get("audio_story"));
            Log.e(TAG, "Message data " + remoteMessage.getData().get("audio_message"));
            Log.e(TAG, "Message Notification " + remoteMessage.getData().get("click_action"));
            sendNotification(remoteMessage);
//            scheduleJob(remoteMessage.getData());
        }

    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent;

        intent = new Intent(this, LibraryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

//        bundle.putString("push", "yes");

        //Add Any key-value to pass extras to intent

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        ;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

}
