package jjlacode.com.androidutils;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public interface ICFragmentos {

    void enviarBundleAFragment(Bundle bundle, Fragment myFragment);
    void enviarBundleAActivity(Bundle bundle);
}
