package com.codevsolution.base.services;

import android.content.Context;
import android.content.Intent;

import com.codevsolution.base.MainActivity;
import com.codevsolution.base.android.AndroidUtil;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.CAMBIO;
import static com.codevsolution.base.javautil.JavaUtil.Constantes.PERSISTENCIA;
import static com.codevsolution.base.logica.InteractorBase.Constantes.ACCION_JEDI;
import static com.codevsolution.base.logica.InteractorBase.Constantes.EXTRA_VOZ;

public class JediReceiver extends ReceiverBase {
    @Override
    public void onReceiver(Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals(ACCION_JEDI)) {
            Intent intentVoz = new Intent(context, MainActivity.class);
            intentVoz.setAction(ACCION_JEDI);
            intentVoz.putExtra(EXTRA_VOZ, intent.getStringExtra(EXTRA_VOZ));
            intentVoz.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            AndroidUtil.setSharePreference(context, PERSISTENCIA, CAMBIO, false);
            context.startActivity(intentVoz);
        }
    }
}
