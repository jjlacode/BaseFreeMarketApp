package com.codevsolution.base.interfaces;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.codevsolution.base.android.FragmentBase;

public interface ICFragmentos {

    void enviarBundleAFragment(Bundle bundle, Fragment myFragment);

    void enviarBundleAActivity(Bundle bundle);

    void enviarBundleFragmentFragment(Bundle bundle);

    void addFragment(Fragment myFragment, int layout);

    void addFragment(Bundle bundle, Fragment myFragment, int layout);

    void reemplazaFragment(Bundle bundle, Fragment myFragment, int layout);

    void eliminarFragment(Fragment myFragment);

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

    void abrirAyudaWeb();

    TextToSpeech playTTs(String texto);

    TextToSpeech playTTs(String texto, String utteranceId);

    TextToSpeech getTTs();

    void onVoz(Bundle bundle);

    void setFragment(FragmentBase fragment);

    void pressBack(Bundle bundle);

    TipoConsultaBD setConsultaBd();

    ICVoz setICVoz();
}
