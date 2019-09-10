package com.codevsolution.freemarketsapp.services;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.codevsolution.base.util.JavaUtil;
import com.codevsolution.base.util.android.AndroidUtil;
import com.codevsolution.base.util.crud.CRUDutil;
import com.codevsolution.base.util.models.Modelo;
import com.codevsolution.base.util.services.ReceiverBase;
import com.codevsolution.base.util.sqlite.ConsultaBD;
import com.codevsolution.freemarketsapp.MainActivity;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.codevsolution.base.util.logica.InteractorBase.Constantes.EXTRA_ID;

public class EventosReceiver extends ReceiverBase implements JavaUtil.Constantes {

    public EventosReceiver(Messenger messenger) {
        super(messenger);
    }

    public EventosReceiver() {
    }

    @Override
    public void onReceiver(Context context, Intent intent) {

        System.out.println("context EventosReceiver= " + context);

        System.out.println("Recibiendo "+intent.getExtras().toString());
        System.out.println("intent Accion= " + intent.getAction());

        int contnot = AndroidUtil.getSharePreference(context, NOTIFICACIONES, CONTNOT, 0);

        if (intent.getAction()!=null && intent.getExtras()!=null && intent.getAction().equals(ACCION_AVISOEVENTO)){

            Modelo evento = (Modelo) intent.getExtras().get(EVENTO);

            String contenido = evento.getString(EVENTO_DESCRIPCION);

            if (evento.getInt(EVENTO_NOTIFICADO)==0) {
                Interactor.notificationEvento(context, MainActivity.class, evento, EVENTO,
                        contnot, R.drawable.alert_box_r, "Aviso evento próximo a vencer",
                        contenido);
                ContentValues valores = new ContentValues();
                ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_NOTIFICADO, 1);
                ConsultaBD.updateRegistro(TABLA_EVENTO, evento.getString(EVENTO_ID_EVENTO), valores);
                AndroidUtil.setSharePreference(context, NOTIFICACIONES, CONTNOT, contnot + 1);
            }
        }else if (intent.getAction()!=null && intent.getExtras()!=null && intent.getAction().equals(ACCION_POSPONER)) {

            System.out.println("Accion posponer");
            String idEvento = intent.getExtras().getString(EXTRA_IDEVENTO);

            Modelo evento = CRUDutil.setModelo(CAMPOS_EVENTO, idEvento);
            ConsultaBD consulta = new ConsultaBD();
            ContentValues valores = new ContentValues();
            long minhoy = JavaUtil.sumaHoraMin(JavaUtil.hoy());
            long minAviso = JavaUtil.sumaHoraMin(evento.getLong(EVENTO_HORAINIEVENTO));
            consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_NOTIFICADO, 0);
            consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_AVISO, (minAviso - minhoy - HORASLONG) - (3 * MINUTOSLONG));
            consulta.updateRegistro(TABLA_EVENTO, idEvento, valores);
            NotificationManager notifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notifyMgr.cancel(intent.getExtras().getInt(EXTRA_ID));

        }else if(intent.getAction()!=null && intent.getExtras()!=null && intent.getAction().equals(ACCION_CANCELAR)){

            System.out.println("Accion cancelar");
            String idEvento = intent.getExtras().getString(EXTRA_IDEVENTO);

            ConsultaBD consulta = new ConsultaBD();
            ContentValues valores = new ContentValues();
            consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_COMPLETADA, 100);
            //consulta.putDato(valores, CAMPOS_EVENTO, EVENTO_NOTIFICADO, 0);
            consulta.updateRegistro(TABLA_EVENTO, idEvento, valores);
            NotificationManager notifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notifyMgr.cancel(intent.getExtras().getInt(EXTRA_ID));

        }
    }
}