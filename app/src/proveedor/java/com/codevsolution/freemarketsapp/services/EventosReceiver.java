package com.codevsolution.freemarketsapp.services;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.services.ReceiverBase;
import com.codevsolution.base.sqlite.ConsultaBDBase;
import com.codevsolution.freemarketsapp.sqlite.ConsultaBD;
import com.codevsolution.freemarketsapp.MainActivity;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.codevsolution.base.logica.InteractorBase.Constantes.EXTRA_ID;

public class EventosReceiver extends ReceiverBase implements JavaUtil.Constantes {

    @Override
    public void onReceiver(Context context, Intent intent) {

        int contnot = AndroidUtil.getSharePreference(context, NOTIFICACIONES, CONTNOT, 0);
        ConsultaBDBase consultaBD = new ConsultaBDBase(new ConsultaBD());
        CRUDutil crudUtil = new CRUDutil();

        if (intent.getAction() != null && intent.getExtras() != null && intent.getAction().equals(ACCION_AVISOEVENTO)) {

            ModeloSQL evento = (ModeloSQL) intent.getExtras().get(EVENTO);

            String contenido = evento.getString(EVENTO_DESCRIPCION);

            if (evento.getInt(EVENTO_NOTIFICADO) == 0) {
                Interactor.notificationEvento(context, MainActivity.class, evento, EVENTO,
                        contnot, R.drawable.alert_box_r, "Aviso evento próximo a vencer",
                        contenido);
                ContentValues valores = new ContentValues();
                if (evento.getLong(EVENTO_AVISO) < MINUTOSLONG * 2) {
                    consultaBD.putDato(valores, EVENTO_AVISO, 0);
                    consultaBD.putDato(valores, EVENTO_NOTIFICADO, 1);

                } else {
                    consultaBD.putDato(valores, EVENTO_AVISO, Math.round((double) evento.getLong(EVENTO_AVISO) / 2));

                }
                consultaBD.updateRegistro(TABLA_EVENTO, evento.getString(EVENTO_ID_EVENTO), valores);
                AndroidUtil.setSharePreference(context, NOTIFICACIONES, CONTNOT, contnot + 1);
            }
        } else if (intent.getAction() != null && intent.getExtras() != null && intent.getAction().equals(ACCION_POSPONER)) {

            System.out.println("Accion posponer");
            String idEvento = intent.getExtras().getString(EXTRA_IDEVENTO);

            ModeloSQL evento = crudUtil.updateModelo(CAMPOS_EVENTO, idEvento);
            ContentValues valores = new ContentValues();
            long minhoy = JavaUtil.sumaHoraMin(JavaUtil.hoy());
            long minAviso = JavaUtil.sumaHoraMin(evento.getLong(EVENTO_HORAINIEVENTO));
            consultaBD.putDato(valores, EVENTO_NOTIFICADO, 0);
            consultaBD.putDato(valores, EVENTO_AVISO, (minAviso - minhoy - HORASLONG) - (3 * MINUTOSLONG));
            consultaBD.updateRegistro(TABLA_EVENTO, idEvento, valores);
            NotificationManager notifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notifyMgr.cancel(intent.getExtras().getInt(EXTRA_ID));

        } else if (intent.getAction() != null && intent.getExtras() != null && intent.getAction().equals(ACCION_CANCELAR)) {

            System.out.println("Accion cancelar");
            String idEvento = intent.getExtras().getString(EXTRA_IDEVENTO);

            ContentValues valores = new ContentValues();
            consultaBD.putDato(valores, EVENTO_COMPLETADA, 100);
            //consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_NOTIFICADO, 0);
            consultaBD.updateRegistro(TABLA_EVENTO, idEvento, valores);
            NotificationManager notifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notifyMgr.cancel(intent.getExtras().getInt(EXTRA_ID));

        }
    }
}
