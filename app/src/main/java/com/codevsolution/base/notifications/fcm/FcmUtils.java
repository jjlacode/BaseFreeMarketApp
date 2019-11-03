package com.codevsolution.base.notifications.fcm;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class FcmUtils {

    public static boolean success;

    public static boolean suscribirTopic(String topic) {

        success = false;
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!task.isSuccessful()) {
                            success = false;
                        }

                        success = true;
                    }
                });
        return success;
    }

    public static boolean anularSuscripcionTopic(String topic) {

        success = false;
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!task.isSuccessful()) {
                            success = false;
                        }

                        success = true;
                    }
                });
        return success;
    }

    public static void sendNotificationToUser(String user, String message) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("notificationRequests");

        Map notification = new HashMap<>();
        notification.put("username", user);
        notification.put("message", message);

        db.push().setValue(notification);
    }
}
