package com.jjlacode.freelanceproject.services;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.jjlacode.freelanceproject.CommonPry;
import com.jjlacode.freelanceproject.MainActivity;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.util.JavaUtil;
import com.jjlacode.freelanceproject.util.crud.CRUDutil;
import com.jjlacode.freelanceproject.util.crud.Modelo;
import com.jjlacode.freelanceproject.util.services.ReceiverBase;
import com.jjlacode.freelanceproject.util.sqlite.ConsultaBD;

import static android.content.Context.NOTIFICATION_SERVICE;

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

        int contnot = CRUDutil.getSharePreference(context,NOTIFICACIONES,CONTNOT,0);

        if (intent.getAction()!=null && intent.getExtras()!=null && intent.getAction().equals(ACCION_AVISOEVENTO)){

            Modelo evento = (Modelo) intent.getExtras().get(EVENTO);

            String contenido = evento.getString(EVENTO_DESCRIPCION);

            if (evento.getInt(EVENTO_NOTIFICADO)==0) {
                CommonPry.notificationEvento(context, MainActivity.class, evento,EVENTO,
                        contnot, R.drawable.alert_box_r, "Aviso evento próximo a vencer",
                        contenido);
                ContentValues valores = new ContentValues();
                ConsultaBD.putDato(valores, CAMPOS_EVENTO, EVENTO_NOTIFICADO, 1);
                ConsultaBD.updateRegistro(TABLA_EVENTO, evento.getString(EVENTO_ID_EVENTO), valores);
                CRUDutil.setSharePreference(context, NOTIFICACIONES, CONTNOT, contnot + 1);
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
