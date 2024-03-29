package com.codevsolution.base.notifications.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.codevsolution.base.R;
import com.codevsolution.base.notifications.PushNotificationsActivity;
import com.codevsolution.base.notifications.PushNotificationsFragment;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class IFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = IFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "FCM Token: " + s);

        sendTokenToServer(s);
    }

    private void sendTokenToServer(String fcmToken) {
        // Acciones para enviar token a tu app server
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "¡Mensaje recibido!");
        displayNotification(remoteMessage.getNotification(), remoteMessage.getData());
        sendNewPromoBroadcast(remoteMessage);
    }

    private void sendNewPromoBroadcast(RemoteMessage remoteMessage) {
        Intent intent = new Intent(PushNotificationsFragment.ACTION_NOTIFY_NEW_PROMO);
        intent.putExtra("title", remoteMessage.getNotification().getTitle());
        intent.putExtra("description", remoteMessage.getNotification().getBody());
        intent.putExtra("expiry_date", remoteMessage.getData().get("expiry_date"));
        intent.putExtra("discount", remoteMessage.getData().get("discount"));
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(intent);
    }

    private void displayNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Intent intent = new Intent(this, PushNotificationsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_action_info)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

}
