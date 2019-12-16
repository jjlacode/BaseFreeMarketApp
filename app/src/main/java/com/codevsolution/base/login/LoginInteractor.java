package com.codevsolution.base.login;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.encrypt.EncryptUtil;
import com.codevsolution.freemarketsapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.codevsolution.base.logica.InteractorBase.Constantes.USERID;

/**
 * Interactor del login
 */
public class LoginInteractor {

    private final Context mContext;
    private FirebaseAuth mFirebaseAuth;

    public LoginInteractor(Context context, FirebaseAuth firebaseAuth) {
        mContext = context;
        if (firebaseAuth != null) {
            mFirebaseAuth = firebaseAuth;
        } else {
            throw new RuntimeException("La instancia de FirebaseAuth no puede ser null");
        }
    }

    public void login(String email, String password, final Callback callback) {
        // Check lógica
        boolean c1 = isValidEmail(email, callback);
        boolean c2 = isValidPassword(password, callback);
        if (!(c1 && c2)) {
            return;
        }

        // Check red
        if (!isNetworkAvailable()) {
            callback.onNetworkConnectFailed();
            return;
        }

        // Check Google Play Service
        if (!isGooglePlayServicesAvailable(callback)) {
            return;
        }

        // Consultar Firebase Authentication
        signInUser(email, password, callback);

    }

    public void registro(String email, String password, final Callback callback) {

        boolean c1 = isValidEmail(email, callback);
        boolean c2 = isValidPassword(password, callback);
        if (!(c1 && c2)) {
            return;
        }

        // Check red
        if (!isNetworkAvailable()) {
            callback.onNetworkConnectFailed();
            return;
        }

        // Check Google Play Service
        if (!isGooglePlayServicesAvailable(callback)) {
            return;
        }

        registrar(email, password, callback);

    }

    private boolean isValidEmail(String email, Callback callback) {
        boolean isValid = true;
        if (TextUtils.isEmpty(email)) {
            callback.onEmailError(mContext.getString(R.string.correo_vacio));
            isValid = false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callback.onEmailError(mContext.getString(R.string.correo_no_valido));
            isValid = false;
        }
        // Más reglas de negocio...
        return isValid;
    }

    private boolean isValidPassword(String password, Callback callback) {
        boolean isValid = true;
        if (TextUtils.isEmpty(password)) {
            callback.onPasswordError(mContext.getString(R.string.pass_vacia));
            isValid = false;
        }

        // Más reglas de negocio...
        return isValid;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connMgr =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable(Callback callback) {
        int statusCode = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(mContext);

        if (GoogleApiAvailability.getInstance().isUserResolvableError(statusCode)) {
            callback.onBeUserResolvableError(statusCode);
            return false;
        } else if (statusCode != ConnectionResult.SUCCESS) {
            callback.onGooglePlayServicesFailed();
            return false;
        }

        return true;
    }

    private void signInUser(String email, String password, final Callback callback) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        String uid = task.getResult().getUser().getUid();
                        if (!task.isSuccessful()) {
                            callback.onAuthFailed(task.getException().getMessage());
                        } else {
                            AndroidUtil.setSharePreference(mContext, USERID, USERID, uid);
                            callback.onAuthSuccess();

                        }
                    }
                });
    }

    private void registrar(String email, String password, final Callback callback) {

        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                String uid = task.getResult().getUser().getUid();
                if (task.isSuccessful()) {

                    AndroidUtil.setSharePreference(mContext, USERID, USERID, uid);
                    callback.onRegSuccess();
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child(task.getResult().getUser().getUid()).child("pass").setValue(EncryptUtil.generaPass());

                } else {

                    callback.onRegFailed(task.getException().getMessage());
                }
            }
        });
    }


    interface Callback {

        void onEmailError(String msg);

        void onPasswordError(String msg);

        void onNetworkConnectFailed();

        void onBeUserResolvableError(int errorCode);

        void onGooglePlayServicesFailed();

        void onAuthFailed(String msg);

        void onRegSuccess();

        void onRegFailed(String msg);

        void onAuthSuccess();
    }

}
