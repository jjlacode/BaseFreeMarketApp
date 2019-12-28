package com.codevsolution.base.login;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.encrypt.EncryptUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.SQLiteUtil;
import com.codevsolution.freemarketsapp.MainActivity;
import com.codevsolution.freemarketsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.NULL;
import static com.codevsolution.base.logica.InteractorBase.Constantes.INICIO;
import static com.codevsolution.base.logica.InteractorBase.Constantes.SYSTEM;
import static com.codevsolution.base.logica.InteractorBase.Constantes.USERID;
import static com.codevsolution.base.logica.InteractorBase.Constantes.USERIDCODE;
import static com.codevsolution.base.sqlite.ContratoSystem.Tablas.CAMPOS_USERS;
import static com.codevsolution.base.sqlite.ContratoSystem.Tablas.TABLA_USERS;
import static com.codevsolution.base.sqlite.ContratoSystem.Tablas.USERS_USERID;
import static com.codevsolution.base.sqlite.ContratoSystem.Tablas.USERS_USERIDCODE;

/**
 * Muestra el formulario de login
 */
public class LoginFragment extends Fragment implements LoginContract.View {

    private LoginContract.Presenter mPresenter;
    private ImagenLayout imagen;
    private EditMaterial mEmail;
    private EditMaterial mPassword;
    private Button mSignInButton;
    private Button registrar;
    private View mLoginForm;
    private View mLoginProgress;
    private Callback mCallback;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;
    private DisplayMetrics metrics;
    private boolean land;
    private boolean tablet;
    private float densidad;
    private int anchoReal;
    private int altoReal;


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        // Setup de argumentos en caso de que los haya
        return fragment;
    }

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Extracción de argumentos en caso de que los haya
        }

        // Obtener instancia FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setIdUserCode(user.getUid());
                    accessApp();

                }
            }
        };
    }

    protected String setIdUserCode(String userID) {

        String id = null;
        String pathDb = Environment.getDataDirectory().getPath() + "/data/"
                + AppActivity.getPackage(getContext()) + "/databases/";

        String BASEDATOS = SYSTEM + userID + ".db";

        if (!SQLiteUtil.checkDataBase(pathDb + BASEDATOS)) {

            id = UUID.randomUUID().toString();
            AndroidUtil.setSharePreference(getContext(), USERID, USERIDCODE, id);
            AndroidUtil.setSharePreference(getContext(), USERID, USERID, id);


            ContentValues values = new ContentValues();
            ConsultaBD.putDato(values, USERS_USERID, userID);
            ConsultaBD.putDato(values, USERS_USERIDCODE, id);
            ConsultaBD.insertRegistro(TABLA_USERS, values);

        } else {

            ModeloSQL user = ConsultaBD.queryObject(CAMPOS_USERS, USERS_USERID, userID);
            id = EncryptUtil.decodificaStr(user.getString(USERS_USERIDCODE));
            AndroidUtil.setSharePreference(getContext(), USERID, USERIDCODE, id);
        }
        return id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginForm = root.findViewById(R.id.login_form);
        mLoginProgress = root.findViewById(R.id.login_progress);

        mEmail = root.findViewById(R.id.etcorreologin);
        mPassword = root.findViewById(R.id.etpasslogin);
        registrar = root.findViewById(R.id.btnRegistrar);
        mSignInButton = root.findViewById(R.id.btnacceder);
        imagen = root.findViewById(R.id.imglogin);

        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        land = getResources().getBoolean(R.bool.esLand);
        tablet = getResources().getBoolean(R.bool.esTablet);
        densidad = metrics.density;
        anchoReal = metrics.widthPixels;
        altoReal = metrics.heightPixels;

        imagen.setImageResource(R.drawable.logo, anchoReal / 2, altoReal / 3);
        imagen.setVisibleTitulo(true);
        imagen.setVisiblePie(true);
        imagen.setTextPie(R.string.auth);
        imagen.setColorTextoTitulo(getResources().getColor(R.color.colorPrimary));
        imagen.setColorTextoPie(getResources().getColor(R.color.colorPrimary));
        imagen.setTextTitulo(R.string.app_name);
        imagen.setTextAutoSizeTitulo(getActivity(), 3f);
        imagen.setTextAutoSizePie(getActivity(), 1.5f);

        mLoginForm.setVisibility(View.VISIBLE);

        userID = AndroidUtil.getSharePreference(getContext(), USERID, USERID, NULL);

        if (userID == NULL) {
            registrar.setVisibility(View.VISIBLE);
        }

        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        registrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                registro();
            }
        });


        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mCallback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " debe implementar CallbackDatos");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LoginActivity.REQUEST_GOOGLE_PLAY_SERVICES:
                attemptLogin();
                break;
        }
    }

    private void attemptLogin() {

        mPresenter.attemptLogin(
                mEmail.getText().toString(),
                mPassword.getText().toString());

    }

    private void registro() {

        mPresenter.registro(mEmail.getText().toString(),
                mPassword.getText().toString());

    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = presenter;
        } else {
            throw new RuntimeException("El presenter no puede ser nulo");
        }
    }

    @Override
    public void showProgress(boolean show) {
        mLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setEmailError(String error) {
        Toast.makeText(getContext(), "Correo no valido", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPasswordError(String error) {
        Toast.makeText(getContext(), "Contraseña no valida", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRegError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void accessApp() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(INICIO, 1);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showBienvenida() {

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(INICIO, 2);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showGooglePlayServicesDialog(int codeError) {
        mCallback.onInvokeGooglePlayServices(codeError);
    }

    @Override
    public void showGooglePlayServicesError() {
        Toast.makeText(getActivity(),
                "Se requiere Google Play Services para usar la app", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(getActivity(),
                "La red no está disponible. Conéctese y vuelva a intentarlo", Toast.LENGTH_LONG)
                .show();
    }

    interface Callback {
        void onInvokeGooglePlayServices(int codeError);
    }

}
