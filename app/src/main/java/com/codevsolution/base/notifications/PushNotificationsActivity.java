package com.codevsolution.base.notifications;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.codevsolution.base.R;
import com.codevsolution.base.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class PushNotificationsActivity extends AppCompatActivity {

    private static final String TAG = PushNotificationsActivity.class.getSimpleName();

    private PushNotificationsFragment mNotificationsFragment;
    private PushNotificationsPresenter mNotificationsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.title_activity_notifications));

        // ¿Existe un usuario logueado?
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        mNotificationsFragment =
                (PushNotificationsFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.notifications_container);
        if (mNotificationsFragment == null) {
            mNotificationsFragment = PushNotificationsFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.notifications_container, mNotificationsFragment)
                    .commit();
        }

        mNotificationsPresenter = new PushNotificationsPresenter(
                mNotificationsFragment, FirebaseMessaging.getInstance());
    }
}
