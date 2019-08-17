package com.jjlacode.base.util.login;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.sqlite.ConsultaBD;
import com.jjlacode.freelanceproject.logica.Interactor;

import static com.jjlacode.base.util.JavaUtil.Constantes.PERFILUSER;
import static com.jjlacode.base.util.JavaUtil.Constantes.PERSISTENCIA;
import static com.jjlacode.base.util.JavaUtil.Constantes.PREFERENCIAS;
import static com.jjlacode.freelanceproject.logica.Interactor.Constantes.DIASFUTUROS;
import static com.jjlacode.freelanceproject.logica.Interactor.Constantes.DIASPASADOS;
import static com.jjlacode.freelanceproject.logica.Interactor.Constantes.PERFILACTIVO;
import static com.jjlacode.freelanceproject.logica.Interactor.Constantes.PRIORIDAD;
import static com.jjlacode.freelanceproject.logica.Interactor.Constantes.USERID;
import static com.jjlacode.freelanceproject.sqlite.ContratoPry.Tablas.CAMPOS_PERFIL;
import static com.jjlacode.freelanceproject.sqlite.ContratoPry.Tablas.PERFIL_DESCRIPCION;
import static com.jjlacode.freelanceproject.sqlite.ContratoPry.Tablas.PERFIL_NOMBRE;
import static com.jjlacode.freelanceproject.sqlite.ContratoPry.Tablas.TABLA_PERFIL;

/**
 * Interactor del login
 */
public class LoginInteractor {

    private final Context mContext;
    private FirebaseAuth mFirebaseAuth;
    private String perfil;

    public LoginInteractor(Context context, FirebaseAuth firebaseAuth) {
        mContext = context;
        if (firebaseAuth != null) {
            mFirebaseAuth = firebaseAuth;
        } else {
            throw new RuntimeException("La instancia de FirebaseAuth no puede ser null");
        }
    }

    public void login(String email, String password, String perfil, final Callback callback) {
        // Check lógica
        this.perfil = perfil;
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
        signInUser(email, password, perfil, callback);

    }

    public void registro(String email, String password, String perfil, final Callback callback) {

        this.perfil = perfil;
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

        registrar(email, password, perfil, callback);

    }

    private boolean isValidEmail(String email, Callback callback) {
        boolean isValid = true;
        if (TextUtils.isEmpty(email)) {
            callback.onEmailError("Escribe tu correo");
            isValid = false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callback.onEmailError("Correo no válido");
            isValid = false;
        }
        // Más reglas de negocio...
        return isValid;
    }

    private boolean isValidPassword(String password, Callback callback) {
        boolean isValid = true;
        if (TextUtils.isEmpty(password)) {
            callback.onPasswordError("Escribe tu contraseña");
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

    private void signInUser(String email, String password, final String perfil, final Callback callback) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            callback.onAuthFailed(task.getException().getMessage());
                        } else {
                            if (comprobarInicio(task.getResult().getUser().getUid(), callback)) {
                                CRUDutil.setSharePreference(mContext, USERID, USERID, task.getResult().getUser().getUid());
                                CRUDutil.setSharePreference(mContext, PREFERENCIAS, PERFILUSER, perfil);

                                callback.onAuthSuccess();
                            }
                        }
                    }
                });
    }

    private void registrar(String email, String password, final String perfil, final Callback callback) {

        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    iniciarDB(task.getResult().getUser().getUid(), perfil, callback);
                    callback.onRegSuccess();

                } else {
                    callback.onRegFailed(task.getException().getMessage());
                }
            }
        });
    }

    private Boolean comprobarInicio(String userId, Callback callback) {

        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        String BASEDATOS = "unionmarket.db";

        if (mContext.getDatabasePath(BASEDATOS) != null && preferences.contains(PERFILACTIVO)) {

            Interactor.perfila = preferences.getString(PERFILACTIVO, "Defecto");
            Interactor.prioridad = preferences.getBoolean(PRIORIDAD, true);
            Interactor.diaspasados = preferences.getInt(DIASPASADOS, 20);
            Interactor.diasfuturos = preferences.getInt(DIASFUTUROS, 90);
            Interactor.hora = Interactor.Calculos.calculoPrecioHora();
            Interactor.setNamefdef();

            Log.d("inicio", "Inicio correcto");

            SharedPreferences persistencia = mContext.getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = persistencia.edit();
            editor.clear();
            editor.apply();

            return true;

        } else {


            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            Log.d("inicio", "borrada preferencias");

            if (mContext.getDatabasePath(BASEDATOS) != null) {


                mContext.deleteDatabase(BASEDATOS);

                Log.d("inicio", "borrada : " + BASEDATOS);
            }
            iniciarDB(userId, perfil, callback);
        }

        return false;
    }

    private void iniciarDB(String userID, String perfil, Callback callback) {

        System.out.println("userID iniciardb= " + userID);
        SharedPreferences preferences = mContext.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(USERID, userID).apply();

        ContentValues valoresPer = new ContentValues();

        ConsultaBD.putDato(valoresPer, CAMPOS_PERFIL, PERFIL_NOMBRE, "Defecto");
        ConsultaBD.putDato(valoresPer, CAMPOS_PERFIL, PERFIL_DESCRIPCION,
                "Perfil por defecto, jornada normal de 8 horas diarias" +
                        " de lunes a viernes y 30 dias de vacaciones al año, " +
                        " y un sueldo anual de " + JavaUtil.formatoMonedaLocal(20000));
        Uri reg = ConsultaBD.insertRegistro(TABLA_PERFIL, valoresPer);
        System.out.println(reg);

        preferences = mContext.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);

        try {

            editor = preferences.edit();
            editor.putString(PERFILUSER, perfil);
            editor.putString(PERFILACTIVO, "Defecto");
            editor.putBoolean(PRIORIDAD, true);
            editor.putInt(DIASPASADOS, 20);
            editor.putInt(DIASFUTUROS, 90);
            editor.apply();

        } catch (Exception e) {


            editor = preferences.edit();

            editor.clear().apply();

            mContext.deleteDatabase(userID);
            System.out.println("error al crear base");

        }
        Interactor.prioridad = true;
        Interactor.perfila = "Defecto";
        Interactor.diasfuturos = 90;
        Interactor.diaspasados = 20;
        Interactor.hora = Interactor.Calculos.calculoPrecioHora();
        Interactor.setNamefdef();
        CRUDutil.setSharePreference(mContext, USERID, USERID, userID);
        callback.onAuthSuccess();


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
