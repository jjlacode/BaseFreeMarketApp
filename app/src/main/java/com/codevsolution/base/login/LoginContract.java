package com.codevsolution.base.login;


import com.codevsolution.base.logica.BasePresenter;
import com.codevsolution.base.logica.BaseView;

/**
 * Interacción MVP en Login
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void showProgress(boolean show);

        void setEmailError(String error);

        void setPasswordError(String error);

        void showLoginError(String msg);

        void showRegError(String msg);

        void accessApp() throws ClassNotFoundException;

        void showBienvenida() throws ClassNotFoundException;

        void showGooglePlayServicesDialog(int errorCode);

        void showGooglePlayServicesError();

        void showNetworkError();
    }

    interface Presenter extends BasePresenter {
        void attemptLogin(String email, String password);

        void registro(String email, String password);
    }
}
