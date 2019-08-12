package com.jjlacode.freelanceproject.services;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.services.ReceiverBase;
import com.jjlacode.base.util.sqlite.ConsultaBD;
import com.jjlacode.freelanceproject.MainActivity;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.logica.Interactor;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.jjlacode.base.util.JavaUtil.Constantes.CONTNOT;
import static com.jjlacode.base.util.JavaUtil.Constantes.NOTIFICACIONES;
import static com.jjlacode.base.util.JavaUtil.Constantes.NULL;
import static com.jjlacode.base.util.JavaUtil.Constantes.PREFERENCIAS;

public class ChatReceiver extends ReceiverBase {

    @Override
    public void onReceiver(Context context, Intent intent) {

        int contnot = CRUDutil.getSharePreference(context, NOTIFICACIONES, CONTNOT, 0);

        if (intent.getAction() != null && intent.getExtras() != null && intent.getAction().equals(ACCION_AVISOMSGCHAT)) {

            Modelo detChat = (Modelo) intent.getExtras().get(CHAT);
            Modelo chat = ConsultaBD.queryObject(CAMPOS_CHAT, detChat.getString(DETCHAT_ID_CHAT));

            String contenido = chat.getString(CHAT_NOMBRE) + " : \n" + detChat.getString(DETCHAT_MENSAJE);

            if (detChat.getInt(DETCHAT_NOTIFICADO) == 0) {
                String idchat = CRUDutil.getSharePreference(context, PREFERENCIAS, IDCHATF, NULL);
                if (!idchat.equals(chat.getString(CHAT_USUARIO))) {
                    Interactor.notificationChat(context, MainActivity.class, detChat, CHAT,
                            contnot, R.drawable.alert_box_r, "Nuevo msg de chat",
                            contenido);
                    CRUDutil.setSharePreference(context, NOTIFICACIONES, CONTNOT, contnot + 1);

                } else {

                    Intent intentVerChat = new Intent(context, MainActivity.class);
                    intentVerChat.setAction(ACCION_VERCHAT);
                    intentVerChat.putExtra(EXTRA_IDCHAT, detChat.getString(DETCHAT_ID_CHAT));
                    intentVerChat.putExtra(EXTRA_SECCHAT, detChat.getInt(DETCHAT_SECUENCIA));
                    intentVerChat.putExtra(EXTRA_TIPOCHAT, chat.getString(CHAT_TIPO));
                    intentVerChat.putExtra(EXTRA_ACTUAL, CHAT);
                    intentVerChat.putExtra(EXTRA_ID, contnot);
                    intentVerChat.setFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(intentVerChat);

                }
                ContentValues valores = new ContentValues();
                ConsultaBD.putDato(valores, CAMPOS_DETCHAT, DETCHAT_NOTIFICADO, 1);
                ConsultaBD.updateRegistroDetalle(TABLA_DETCHAT, detChat.getString(DETCHAT_ID_CHAT),
                        detChat.getInt(DETCHAT_SECUENCIA), valores);
            }
        }
    }
}
