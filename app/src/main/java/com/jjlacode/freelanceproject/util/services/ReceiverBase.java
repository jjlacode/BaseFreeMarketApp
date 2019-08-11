package com.jjlacode.freelanceproject.util.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jjlacode.freelanceproject.CommonPry;
import com.jjlacode.freelanceproject.sqlite.ContratoPry;

public abstract class ReceiverBase extends BroadcastReceiver implements CommonPry.Constantes, ContratoPry.Tablas {

    // Sin instancias

    public ReceiverBase(Messenger messenger) {
        this.messenger = messenger;
    }

    public ReceiverBase() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        /*
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

        }else if(intent.getAction()!=null && intent.getExtras()!=null && intent.getAction().equals(ACCION_AVISOMSGCHAT)){

            Modelo detChat = (Modelo) intent.getExtras().get(CHAT);
            Modelo chat = ConsultaBD.queryObject(CAMPOS_CHAT,detChat.getString(DETCHAT_ID_CHAT));

            String contenido = chat.getString(CHAT_NOMBRE)+" : \n"+detChat.getString(DETCHAT_MENSAJE);

            if (detChat.getInt(DETCHAT_NOTIFICADO)==0) {
                String idchat = CRUDutil.getSharePreference(context,PREFERENCIAS,IDCHATF,NULL);
                if (!idchat.equals(chat.getString(CHAT_USUARIO))) {
                    CommonPry.notificationChat(context, MainActivity.class, detChat, CHAT,
                            contnot, R.drawable.alert_box_r, "Nuevo msg de chat",
                            contenido);
                    CRUDutil.setSharePreference(context, NOTIFICACIONES, CONTNOT, contnot + 1);

                }else{

                    Intent intentVerChat = new Intent(context, MainActivity.class);
                    intentVerChat.setAction(ACCION_VERCHAT);
                    intentVerChat.putExtra(EXTRA_IDCHAT,detChat.getString(DETCHAT_ID_CHAT));
                    intentVerChat.putExtra(EXTRA_SECCHAT,detChat.getInt(DETCHAT_SECUENCIA));
                    intentVerChat.putExtra(EXTRA_TIPOCHAT,chat.getString(CHAT_TIPO));
                    intentVerChat.putExtra(EXTRA_ACTUAL,CHAT);
                    intentVerChat.putExtra(EXTRA_ID,contnot);
                    intentVerChat.setFlags(FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(intentVerChat);

                }
                ContentValues valores = new ContentValues();
                ConsultaBD.putDato(valores, CAMPOS_DETCHAT, DETCHAT_NOTIFICADO, 1);
                ConsultaBD.updateRegistroDetalle(TABLA_DETCHAT, detChat.getString(DETCHAT_ID_CHAT),
                        detChat.getInt(DETCHAT_SECUENCIA), valores);
            }
        }
        */

        onReceiver(context, intent);
    }

    public abstract void onReceiver(Context context, Intent intent);

    public interface Messenger {
        void alRecibirMsg();
    }

    protected Messenger messenger;

    public void setMessenger(Messenger messenger) {
        this.messenger = messenger;
    }


}