package com.jjlacode.base.util.logica;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.google.firebase.auth.FirebaseAuth;
import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.android.AndroidUtil;
import com.jjlacode.base.util.android.SplashActivity;
import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.models.Modelo;
import com.jjlacode.base.util.models.ModeloFire;
import com.jjlacode.base.util.sqlite.ContratoSystem;
import com.jjlacode.freelanceproject.R;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.jjlacode.base.util.android.AppActivity.getAppContext;
import static com.jjlacode.base.util.logica.InteractorBase.Constantes.ACCION_VERCHAT;
import static com.jjlacode.base.util.logica.InteractorBase.Constantes.EXTRA_ACTUAL;
import static com.jjlacode.base.util.logica.InteractorBase.Constantes.EXTRA_ID;
import static com.jjlacode.base.util.logica.InteractorBase.Constantes.EXTRA_IDCHAT;
import static com.jjlacode.base.util.logica.InteractorBase.Constantes.EXTRA_IDSPCHAT;
import static com.jjlacode.base.util.logica.InteractorBase.Constantes.EXTRA_SECCHAT;
import static com.jjlacode.base.util.logica.InteractorBase.Constantes.EXTRA_TIPOCHAT;
import static com.jjlacode.base.util.logica.InteractorBase.Constantes.EXTRA_TIPOCHATRETORNO;

public class InteractorBase implements ContratoSystem.Tablas, JavaUtil.Constantes {

    public interface Constantes {

        String USERID = "userid";
        String LOCALIZACION = "localizacion";
        String USERS = "users";
        String ANON = "anonimo";
        String PERFILES = "perfiles";
        String PRODUCTOS = "productos";
        String SERVICIOS = "servicios";
        String PRODMULTI = "prodmulti";
        String SERVMULTI = "servmulti";
        String PERFILMULTI = "perfilmulti";
        String INDICE = "indice";
        String LUGARES = "lugares";
        String MIUBICACION = "miUbicacion";
        String MUNDIAL = "Mundial";
        String INDICEMARC = "indicemarc";
        String MARC = "marc";
        String RATING = "rating";
        String MULTI = "multi";
        String SUSCRIPCIONES = "suscripciones";
        String CHAT = "chat";
        String DETCHAT = "detchat";
        String IDCHATF = "idchatBase";
        String NOMBRECHAT = "nombre_chat";
        String LOG = "log";
        String VISORPDFMAIL = "visor pdf - email";
        String TODAS = "Todas";
        String TODOS = "Todos";
        String INICIO = "inicio";
        String SALIR = "salir";
        String TABLAS = "tablas";
        String NUEVO = getAppContext().getString(R.string.nuevo);
        String SORTEO = "sorteo";
        int RECIBIDO = 1;
        int ENVIADO = 2;
        String EXTRA_IDCHAT = "jjlacode.com.base.util.EXTRA_IDCHAT";
        String EXTRA_IDSPCHAT = "jjlacode.com..base.utilEXTRA_IDSPCHAT";
        String EXTRA_SECCHAT = "jjlacode.com.base.util.EXTRA_SECCHAT";
        String EXTRA_TIPOCHAT = "jjlacode.com.base.util.EXTRA_TIPOCHAT";
        String EXTRA_TIPOCHATRETORNO = "jjlacode.com.base.util.EXTRA_TIPOCHATRETORNO";
        String EXTRA_ACTUAL = "jjlacode.com.base.util.EXTRA_ACTUAL";
        String ACCION_VERCHAT = "jjlacode.com.base.util.action.VERCHAT";
        String ACCION_VERSORTEO = "jjlacode.com.base.util.action.VERSORTEO";
        String EXTRA_ID = "jjlacode.com.base.util.EXTRA_ID";
        String ACCION_AVISOMSGCHAT = "jjlacode.com.base.util.action.AVISOMSGCHAT";


    }

    public static String setNamefdef() {

        return AndroidUtil.getSharePreference(getAppContext(), PREFERENCIAS, PERFILUSER, NULL);
    }

    public static void notificationChat(Context contexto, Class<?> clase, Modelo detchat, String actual,
                                        int id, int iconId, String titulo, String contenido) {

        RemoteViews remoteView = new RemoteViews(contexto.getPackageName(), R.layout.notificacion_chat);
        remoteView.setTextViewText(R.id.tvdescnotchat, contenido);

        Modelo chat = CRUDutil.setModelo(CAMPOS_CHAT, detchat.getString(DETCHAT_ID_CHAT));
        Intent intentVerChat = new Intent(contexto, clase);
        intentVerChat.setAction(ACCION_VERCHAT);
        intentVerChat.putExtra(EXTRA_IDSPCHAT, chat.getString(CHAT_USUARIO));
        intentVerChat.putExtra(EXTRA_IDCHAT, detchat.getString(DETCHAT_ID_CHAT));
        intentVerChat.putExtra(EXTRA_SECCHAT, detchat.getInt(DETCHAT_SECUENCIA));
        intentVerChat.putExtra(EXTRA_TIPOCHAT, chat.getString(CHAT_TIPO));
        intentVerChat.putExtra(EXTRA_TIPOCHATRETORNO, chat.getString(CHAT_TIPORETORNO));
        intentVerChat.putExtra(EXTRA_ACTUAL, actual);
        intentVerChat.putExtra(EXTRA_ID, id);

        intentVerChat.setFlags(FLAG_ACTIVITY_NEW_TASK);
        PendingIntent verChat = PendingIntent.getActivity(
                contexto,
                id,
                intentVerChat,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.btnvernotchat, verChat);

        // Estructurar la notificación
        Notification.Builder builder =
                new Notification.Builder(contexto)
                        .setDefaults(Notification.DEFAULT_LIGHTS)
                        .setSmallIcon(iconId)
                        .setContentTitle(titulo)
                        .setContentText(contenido)
                        .setWhen(detchat.getLong(DETCHAT_FECHA))
                        .setColor(contexto.getResources().getColor(R.color.colorPrimary))
                        .setTicker(titulo)
                        .setVibrate(new long[]{100, 250, 100, 500})
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setSound(Uri.parse("android.resource://" + contexto.getPackageName() + "/" + R.raw.popcorn))
                        .setContent(remoteView)
                        .setAutoCancel(true);

        Notification notification = builder.build();
        NotificationManager notifyMgrEvento = (NotificationManager) contexto.getSystemService(NOTIFICATION_SERVICE);

        // Construir la notificación y emitirla
        notifyMgrEvento.notify(id, notification);


    }

    public static ModeloFire convertirModelo(Modelo modelo) {

        ModeloFire fire = new ModeloFire();

        ArrayList<String> valores = new ArrayList<>(Arrays.asList(modelo.getValores()));
        fire.setValores(valores);

        return fire;
    }

    public static void logOut() {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getAppContext(), SplashActivity.class);
        getAppContext().startActivity(intent);
    }
}
