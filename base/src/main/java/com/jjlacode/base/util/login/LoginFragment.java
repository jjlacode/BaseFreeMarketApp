package com.jjlacode.base.util.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jjlacode.freelanceproject.MainActivity;
import com.jjlacode.freelanceproject.R;

import static com.jjlacode.base.util.JavaUtil.Constantes.NULL;
import static com.jjlacode.base.util.JavaUtil.Constantes.PREFERENCIAS;
import static com.jjlacode.freelanceproject.CommonPry.Constantes.INICIO;
import static com.jjlacode.freelanceproject.CommonPry.Constantes.USERID;

/**
 * Muestra el formulario de login
 */
public class LoginFragment extends Fragment implements LoginContract.View {

    private LoginContract.Presenter mPresenter;
    private EditText mEmail;
    private EditText mPassword;
    private Button mSignInButton;
    private View mLoginForm;
    private View mLoginProgress;
    private Callback mCallback;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

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
                SharedPreferences preferences = getContext().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
                String userID = preferences.getString(USERID, NULL);
                if (user != null && user.getUid().equals(userID)) {
                    System.out.println("user: " + user.getUid());
                    showPushNotifications();
                } else {
                    // El usuario no está logueado
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginForm = root.findViewById(R.id.login_form);
        mLoginProgress = root.findViewById(R.id.login_progress);

        mEmail = (EditText) root.findViewById(R.id.etcorreologin);
        mPassword = (EditText) root.findViewById(R.id.etpasslogin);

        mSignInButton = (Button) root.findViewById(R.id.btnacceder);

        mLoginForm.setVisibility(View.VISIBLE);

        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
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
                    + " debe implementar Callback");
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
    public void showPushNotifications() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(INICIO, 1);
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
