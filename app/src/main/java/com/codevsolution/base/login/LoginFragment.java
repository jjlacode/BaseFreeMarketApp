package com.codevsolution.base.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ViewImagenLayout;
import com.codevsolution.base.style.Estilos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.NULL;
import static com.codevsolution.base.logica.InteractorBase.Constantes.EMAILUSER;
import static com.codevsolution.base.logica.InteractorBase.Constantes.INICIO;
import static com.codevsolution.base.logica.InteractorBase.Constantes.SYSTEM;
import static com.codevsolution.base.logica.InteractorBase.Constantes.USERID;
import static com.codevsolution.base.logica.InteractorBase.Constantes.USERIDCODE;

/**
 * Muestra el formulario de login
 */
public class LoginFragment extends Fragment implements LoginContract.View {

    private LoginContract.Presenter mPresenter;
    private ViewImagenLayout imagen;
    private EditMaterialLayout mEmail;
    private EditMaterialLayout mPassword;
    private Button mSignInButton;
    private Button registrar;
    private ImageView btnAyuda;
    private LinearLayoutCompat main;
    private LinearLayoutCompat mLoginImg;
    private LinearLayoutCompat mLoginForm;
    private LinearLayoutCompat mLoginEdit;
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
    private Context contexto;


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
                    try {
                        accessApp();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
    }

    private void setIdUserCode(String userID) {

        String id = AndroidUtil.getSharePreferenceMaster(contexto, SYSTEM, userID, NULL);

        if (id != null && !id.equals(NULL)) {

            AndroidUtil.setSharePreference(contexto, USERID, USERIDCODE, id);
            AndroidUtil.setSharePreference(contexto, USERID, USERID, id);

        } else {

            id = UUID.randomUUID().toString();
            AndroidUtil.setSharePreferenceMaster(contexto, SYSTEM, userID, id);
            AndroidUtil.setSharePreference(contexto, USERID, USERIDCODE, id);
            AndroidUtil.setSharePreference(contexto, USERID, USERID, id);

        }
        System.out.println("id loginFragment= " + id);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(Estilos.getIdLayout(contexto, "fragment_login"), container, false);

        main = root.findViewById(Estilos.getIdResource(contexto, "login_main"));
        mLoginImg = root.findViewById(Estilos.getIdResource(contexto, "login_img"));
        mLoginForm = root.findViewById(Estilos.getIdResource(contexto, "login_form"));
        mLoginEdit = root.findViewById(Estilos.getIdResource(contexto, "login_edit"));
        mEmail = new EditMaterialLayout(mLoginEdit, contexto);
        mEmail.setHint("Email");
        mEmail.setTipo(EditMaterialLayout.TEXTO | EditMaterialLayout.EMAIL);
        mEmail.btnInicioVisible(false);
        mEmail.setAlCambiarListener(new EditMaterialLayout.AlCambiarListener() {
            @Override
            public void antesCambio(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void cambiando(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void despuesCambio(Editable s) {

                AndroidUtil.setSharePreferenceMaster(contexto, USERID, EMAILUSER, s.toString());
            }
        });
        mPassword = new EditMaterialLayout(mLoginEdit, contexto);
        mPassword.setHint("Password");
        mPassword.setTipo(EditMaterialLayout.TEXTO | EditMaterialLayout.PASS);
        mPassword.btnInicioVisible(false);
        mLoginProgress = root.findViewById(Estilos.getIdResource(contexto, "login_progress"));

        registrar = root.findViewById(Estilos.getIdResource(contexto, "btnRegistrar"));
        mSignInButton = root.findViewById(Estilos.getIdResource(contexto, "btnacceder"));
        btnAyuda = root.findViewById(Estilos.getIdResource(contexto, "btnAyudaLogin"));
        btnAyuda.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppActivity.verWeb(contexto, "https://" + Estilos.getString(contexto, "app_name_min") + ".codevsolution.com/login", getActivity());
            }
        });
        imagen = new ViewImagenLayout(mLoginImg, contexto);

        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        land = getResources().getBoolean(Estilos.getIdBool(contexto, "esLand"));
        tablet = getResources().getBoolean(Estilos.getIdBool(contexto, "esTablet"));
        densidad = metrics.density;
        anchoReal = metrics.widthPixels;
        altoReal = metrics.heightPixels;

        imagen.setImageResource(Estilos.getIdDrawable(contexto, "logo"), anchoReal / 2, altoReal / 3);
        imagen.setVisibleTitulo(true);
        imagen.setVisiblePie(true);
        imagen.setTextTitulo(Estilos.getString(contexto, "app_name"));
        imagen.setTextPie(Estilos.getString(contexto, "auth"));
        imagen.setColorTextoTitulo(Estilos.colorSecondaryDark);
        imagen.setColorTextoPie(Estilos.colorSecondaryDark);
        imagen.setTextAutoSizeTitulo(getActivity(), 3f);
        imagen.setTextAutoSizePie(getActivity(), 1.5f);
        imagen.setGravedad(Gravity.CENTER);
        imagen.setGravedadTitulo(Gravity.CENTER);
        imagen.setGravedadPie(Gravity.CENTER);
        imagen.setGoneBtn();

        mLoginForm.setVisibility(View.VISIBLE);

        userID = AndroidUtil.getSharePreference(getContext(), USERID, USERID, NULL);

        if (userID != null && userID.equals(NULL)) {
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
        contexto = context;
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
    public void accessApp() throws ClassNotFoundException {
        String clase = "com.codevsolution." + Estilos.getString(contexto, "app_name_min") + ".MainActivity";
        Intent intent = new Intent(getActivity(), Class.forName(clase));
        intent.putExtra(INICIO, 1);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showBienvenida() throws ClassNotFoundException {
        String clase = "com.codevsolution." + Estilos.getString(contexto, "app_name_min") + ".MainActivity";
        Intent intent = new Intent(getActivity(), Class.forName(clase));
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
