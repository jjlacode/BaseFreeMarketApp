package com.jjlacode.base.util.interfaces;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

public interface ICFragmentos {

    void enviarBundleAFragment(Bundle bundle, Fragment myFragment);

    void enviarBundleAActivity(Bundle bundle);

    void fabVisible();

    void snackBarShow(View view, String mensaje);

    void setIcoFab(int recurso);

    void setIcoFab(Drawable drawable);

    void fabOculto();

    void showTitle(int title);

    void showSubTitle(int subTitle);

    void showTitle(String title);

    void showSubTitle(String subTitle);

    void enviarAyudaWeb(String ayudaWeb);
}
