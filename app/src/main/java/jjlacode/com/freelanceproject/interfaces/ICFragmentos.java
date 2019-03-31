package jjlacode.com.freelanceproject.interfaces;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public interface ICFragmentos {

    void enviarBundleAFragment(Bundle bundle, Fragment myFragment);
    void enviarBundleAActivity(Bundle bundle);
}
