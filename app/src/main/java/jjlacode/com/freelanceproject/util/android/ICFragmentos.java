package jjlacode.com.freelanceproject.util.android;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
}
