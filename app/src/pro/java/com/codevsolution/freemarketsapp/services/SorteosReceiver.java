package com.codevsolution.freemarketsapp.services;

import android.content.Context;
import android.content.Intent;

import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.services.ReceiverBase;
import com.codevsolution.freemarketsapp.MainActivity;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import static com.codevsolution.base.logica.InteractorBase.Constantes.SORTEO;

public class SorteosReceiver extends ReceiverBase implements JavaUtil.Constantes {

    public SorteosReceiver(Messenger messenger) {
        super(messenger);
    }

    public SorteosReceiver() {
    }

    @Override
    public void onReceiver(Context context, Intent intent) {

        int contnot = AndroidUtil.getSharePreference(context, NOTIFICACIONES, CONTNOT, 0);

        if (intent.getAction() != null && intent.getExtras() != null && intent.getAction().equals(ACCION_AVISOSORTEO)) {

            String idGanador = (String) intent.getExtras().get(GANADORSORTEO);
            String idSorteo = (String) intent.getExtras().get(SORTEO);
            String contenido = (String) intent.getExtras().get(PRODUCTO);

            Interactor.notificationSorteo(context, MainActivity.class, idGanador, idSorteo,
                    contnot, R.drawable.logo, "Sorteo finalizado",
                    contenido);

            AndroidUtil.setSharePreference(context, NOTIFICACIONES, CONTNOT, contnot + 1);

        }
    }
}
