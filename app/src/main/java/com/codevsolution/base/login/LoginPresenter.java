package com.codevsolution.base.login;


import androidx.annotation.NonNull;

/**
 * Presentador del login
 */
public class LoginPresenter implements LoginContract.Presenter, LoginInteractor.Callback {

    private final LoginContract.View mLoginView;
    private LoginInteractor mLoginInteractor;

    public LoginPresenter(@NonNull LoginContract.View loginView,
                          @NonNull LoginInteractor loginInteractor) {
        mLoginView = loginView;
        loginView.setPresenter(this);
        mLoginInteractor = loginInteractor;
    }

    @Override
    public void start() {
        // Comprobar si el usuario está logueado
    }

    @Override
    public void attemptLogin(String email, String password) {
        mLoginView.showProgress(true);
        mLoginInteractor.login(email, password, this);
    }

    @Override
    public void registro(String email, String password) {
        mLoginView.showProgress(true);
        mLoginInteractor.registro(email, password, this);

    }

    @Override
    public void onEmailError(String msg) {
        mLoginView.showProgress(false);
        mLoginView.setEmailError(msg);
    }

    @Override
    public void onPasswordError(String msg) {
        mLoginView.showProgress(false);
        mLoginView.setPasswordError(msg);
    }

    @Override
    public void onAuthSuccess() {
        try {
            mLoginView.accessApp();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAuthFailed(String msg) {
        mLoginView.showProgress(false);
        mLoginView.showLoginError(msg);
    }

    @Override
    public void onRegSuccess() {
        try {
            mLoginView.showBienvenida();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRegFailed(String msg) {
        mLoginView.showProgress(false);
        mLoginView.showRegError(msg);
    }

    @Override
    public void onBeUserResolvableError(int errorCode) {
        mLoginView.showProgress(false);
        mLoginView.showGooglePlayServicesDialog(errorCode);
    }

    @Override
    public void onGooglePlayServicesFailed() {
        mLoginView.showGooglePlayServicesError();
    }

    @Override
    public void onNetworkConnectFailed() {
        mLoginView.showProgress(false);
        mLoginView.showNetworkError();
    }


}
