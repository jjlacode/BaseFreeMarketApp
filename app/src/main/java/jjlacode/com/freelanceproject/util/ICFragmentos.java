package jjlacode.com.freelanceproject.util;

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
}
