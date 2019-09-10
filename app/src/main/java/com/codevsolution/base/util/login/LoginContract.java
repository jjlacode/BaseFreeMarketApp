package com.codevsolution.base.util.login;


import com.codevsolution.base.util.BasePresenter;
import com.codevsolution.base.util.BaseView;

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

        void accessApp();

        void showBienvenida();

        void showGooglePlayServicesDialog(int errorCode);

        void showGooglePlayServicesError();

        void showNetworkError();
    }

    interface Presenter extends BasePresenter {
        void attemptLogin(String email, String password, String perfil);

        void registro(String email, String password, String perfil);
    }
}
