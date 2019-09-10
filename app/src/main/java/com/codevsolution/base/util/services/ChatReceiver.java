package com.codevsolution.base.util.services;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.codevsolution.base.util.JavaUtil;
import com.codevsolution.base.util.android.AndroidUtil;
import com.codevsolution.base.util.logica.InteractorBase;
import com.codevsolution.base.util.models.Modelo;
import com.codevsolution.base.util.sqlite.ConsultaBD;
import com.codevsolution.base.util.sqlite.ContratoSystem;
import com.codevsolution.freemarketsapp.MainActivity;
import com.codevsolution.freemarketsapp.R;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ChatReceiver extends ReceiverBase implements InteractorBase.Constantes,
        JavaUtil.Constantes, ContratoSystem.Tablas {

    @Override
    public void onReceiver(Context context, Intent intent) {

        int contnot = AndroidUtil.getSharePreference(context, NOTIFICACIONES, CONTNOT, 0);

        if (intent.getAction() != null && intent.getExtras() != null && intent.getAction().equals(ACCION_AVISOMSGCHAT)) {

            Modelo detChat = (Modelo) intent.getExtras().get(CHAT);
            Modelo chat = ConsultaBD.queryObject(CAMPOS_CHAT, detChat.getString(DETCHAT_ID_CHAT));

            String contenido = chat.getString(CHAT_NOMBRE) + " : \n" + detChat.getString(DETCHAT_MENSAJE);

            if (detChat.getInt(DETCHAT_NOTIFICADO) == 0) {
                String idchat = AndroidUtil.getSharePreference(context, PREFERENCIAS, IDCHATF, NULL);
                String actual = CHAT;
                if (chat.getString(CHAT_TIPORETORNO).equals(PRODFREELANCE)) {
                    actual = PRODFREELANCE;
                } else if (chat.getString(CHAT_TIPORETORNO).equals(PRODCOMERCIAL)) {
                    actual = PRODCOMERCIAL;
                } else if (chat.getString(CHAT_TIPORETORNO).equals(PRODECOMMERCE)) {
                    actual = PRODECOMMERCE;
                } else if (chat.getString(CHAT_TIPORETORNO).equals(PRODPROVCAT)) {
                    actual = PRODPROVCAT;
                } else if (chat.getString(CHAT_TIPORETORNO).equals(PRODLUGAR)) {
                    actual = PRODLUGAR;
                } else if (chat.getString(CHAT_TIPORETORNO).equals(PRODSORTEOS)) {
                    actual = PRODSORTEOS;
                } else if (chat.getString(CHAT_TIPORETORNO).equals(USADO)) {
                    actual = USADO;
                }
                if (!idchat.equals(chat.getString(CHAT_USUARIO))) {

                    InteractorBase.notificationChat(context, MainActivity.class, detChat, actual,
                            contnot, R.drawable.logo, "Nuevo msg de chat",
                            contenido);
                    AndroidUtil.setSharePreference(context, NOTIFICACIONES, CONTNOT, contnot + 1);

                } else {

                    Intent intentVerChat = new Intent(context, MainActivity.class);
                    intentVerChat.setAction(ACCION_VERCHAT);
                    intentVerChat.putExtra(EXTRA_IDSPCHAT, chat.getString(CHAT_USUARIO));
                    intentVerChat.putExtra(EXTRA_IDCHAT, detChat.getString(DETCHAT_ID_CHAT));
                    intentVerChat.putExtra(EXTRA_SECCHAT, detChat.getInt(DETCHAT_SECUENCIA));
                    intentVerChat.putExtra(EXTRA_TIPOCHAT, chat.getString(CHAT_TIPO));
                    intentVerChat.putExtra(EXTRA_TIPOCHATRETORNO, chat.getString(CHAT_TIPORETORNO));
                    intentVerChat.putExtra(EXTRA_ACTUAL, actual);
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
