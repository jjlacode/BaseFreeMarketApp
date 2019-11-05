package com.codevsolution.base.logica;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.SplashActivity;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ModeloFire;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoSystem;
import com.codevsolution.freemarketsapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.codevsolution.base.android.AppActivity.getAppContext;
import static com.codevsolution.base.logica.InteractorBase.Constantes.ACCION_VERCHAT;
import static com.codevsolution.base.logica.InteractorBase.Constantes.EXTRA_ACTUAL;
import static com.codevsolution.base.logica.InteractorBase.Constantes.EXTRA_ID;
import static com.codevsolution.base.logica.InteractorBase.Constantes.EXTRA_IDCHAT;
import static com.codevsolution.base.logica.InteractorBase.Constantes.EXTRA_IDSPCHAT;
import static com.codevsolution.base.logica.InteractorBase.Constantes.EXTRA_SECCHAT;

public class InteractorBase implements ContratoSystem.Tablas, JavaUtil.Constantes {

    public static String colorTmp = null;

    public interface Constantes {

        String USERID = "userid";
        String PLANMENSUAL = "plan_mensual";
        String PLANTRIMESTRAL = "plan_trimestral";
        String PLANSEMESTRAL = "plan_semestral";
        String PLANANUAL = "plan_anual";
        String SUSESTADO = "estado_suscripcion";
        String LOCALIZACION = "localizacion";
        String ORDENASCENDENTE = " ASC";
        String ORDENDESCENDENTE = " DESC";
        String USERS = "users";
        String ANON = "anonimo";
        String PERFILES = "perfiles";
        String SERVICIOS = "servicios";
        String PRODUCTOS = "productos";
        String NUMPRODSUS = "num_productos_suscritos";
        String INDICE = "indice";
        String LUGARES = "lugares";
        String MIUBICACION = "miUbicacion";
        String MUNDIAL = "Mundial";
        String INDICEMARC = "indicemarc";
        String MARC = "marc";
        String RATING = "rating";
        String MULTI = "multi";
        String SUSCRIPCIONES = "suscripciones";
        String SUSCRIPCIONESPRO = "suscripcionespro";
        String SUSCRIPCIONESSORTEOSCLI = "suscripciones_sorteoscli";
        String SUSCRIPCIONESSORTEOSPRO = "suscripciones_sorteospro";
        String PARTICIPANTES = "participantes";
        String CHAT = "chat";
        String DETCHAT = "detchat";
        String MARCADOR = "marcador";
        String ZONA = "zona";
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
        String CRUD = "crud";
        String MODULO = "modulo";
        String SORTEO = "sorteo";
        String SORTEOPRO = "sorteo_pro";
        String SORTEOCLI = "sorteo_cli";
        int RECIBIDO = 1;
        int ENVIADO = 2;
        String EXTRA_IDCHAT = "jjlacode.com.base.util.EXTRA_IDCHAT";
        String EXTRA_IDSPCHAT = "jjlacode.com..base.utilEXTRA_IDSPCHAT";
        String EXTRA_SECCHAT = "jjlacode.com.base.util.EXTRA_SECCHAT";
        String EXTRA_ACTUAL = "jjlacode.com.base.util.EXTRA_ACTUAL";
        String ACCION_VERCHAT = "jjlacode.com.base.util.action.VERCHAT";
        String ACCION_VERSORTEO = "jjlacode.com.base.util.action.VERSORTEO";
        String EXTRA_ID = "jjlacode.com.base.util.EXTRA_ID";
        String ACCION_AVISOMSGCHAT = "jjlacode.com.base.util.action.AVISOMSGCHAT";


    }

    public static String setNamefdef() {

        return AndroidUtil.getSharePreference(getAppContext(), PREFERENCIAS, PERFILUSER, NULL);
    }

    public static void notificationChat(Context contexto, Class<?> clase, ModeloSQL detchat, String actual,
                                        int id, int iconId, String titulo, String contenido) {

        RemoteViews remoteView = new RemoteViews(contexto.getPackageName(), R.layout.notificacion_chat);
        remoteView.setTextViewText(R.id.tvdescnotchat, contenido);

        ModeloSQL chat = CRUDutil.updateModelo(CAMPOS_CHAT, detchat.getString(DETCHAT_ID_CHAT));
        Intent intentVerChat = new Intent(contexto, clase);
        intentVerChat.setAction(ACCION_VERCHAT);
        intentVerChat.putExtra(EXTRA_IDSPCHAT, chat.getString(CHAT_USUARIO));
        intentVerChat.putExtra(EXTRA_IDCHAT, detchat.getString(DETCHAT_ID_CHAT));
        intentVerChat.putExtra(EXTRA_SECCHAT, detchat.getInt(DETCHAT_SECUENCIA));
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

    public static ModeloFire convertirModelo(ModeloSQL modeloSQL) {

        ModeloFire fire = new ModeloFire();

        ArrayList<String> valores = new ArrayList<>(Arrays.asList(modeloSQL.getValores()));
        fire.setValores(valores);

        return fire;
    }

    public static void logOut() {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getAppContext(), SplashActivity.class);
        getAppContext().startActivity(intent);
    }
}
