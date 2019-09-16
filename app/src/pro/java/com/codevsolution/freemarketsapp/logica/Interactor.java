package com.codevsolution.freemarketsapp.logica;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.models.DestinosVoz;
import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.sqlite.ConsultaBD;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.BuildConfig;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.model.ProdProv;
import com.codevsolution.freemarketsapp.services.EventosReceiver;
import com.codevsolution.freemarketsapp.ui.CalendarioEventos;
import com.codevsolution.freemarketsapp.ui.FragmentNuevoEvento;
import com.codevsolution.freemarketsapp.ui.MenuInicio;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Intent.EXTRA_EMAIL;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.codevsolution.base.android.AppActivity.getAppContext;
import static com.codevsolution.base.sqlite.ConsultaBD.queryList;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.ACCION_CANCELAR;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.ACCION_POSPONER;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.ACCION_VER;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.EXTRA_GANADOR;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.EXTRA_IDEVENTO;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.EXTRA_SORTEO;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PARTIDA;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODPROVCAT;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PRODUCTO;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.TRABAJO;


public class Interactor extends InteractorBase implements JavaUtil.Constantes,
        InteractorBase.Constantes, ContratoPry.Tablas {


    public static String perfila = null;//Perfil setActivo para calculos y preferencias
    public static boolean prioridad;
    public static int diaspasados;
    public static int diasfuturos;
    public static double hora;
    public static double beneficio;
    public static boolean permiso;
    public static String namefsub;
    public static String idProyecto;
    public static String agendaTemp;
    public static String namesubdef;
    public static boolean grabar;

    public static Fragment fragmentMenuInicio = new MenuInicio();


    //private static ConsultaBD consulta = new ConsultaBD();

    public interface ConstantesPry {


        String HTTPAYUDA = "https://codevsolution.com/freemarketsapp/";
        String PRIORIDAD = "prioridad";
        String DIASPASADOS = "diaspasados";
        String DIASFUTUROS = "diasfuturos";
        String PERFILACTIVO = "perfil setActivo";
        String SORTEOS = getAppContext().getString(R.string.sorteos);
        String PRODSORTEOS = "prodsorteos";
        String FREELANCE = getAppContext().getString(R.string.freelance);
        String CLIENTEWEB = getAppContext().getString(R.string.clienteweb);
        String COMERCIAL = getAppContext().getString(R.string.comercial);
        String PRODCOMERCIAL = "prodcomer";
        String PRODFREELANCE = "prodfreelance";
        String PROVEEDORWEB = getAppContext().getString(R.string.proveedorweb);
        String ECOMMERCE = getAppContext().getString(R.string.ecommerce);
        String PRODECOMMERCE = "prodecom";
        String PRODEMPRESA = "prodempresa";
        String PRODLUGAR = "prodlugar";
        String LUGAR = getAppContext().getString(R.string.lugar);
        String EMPRESA = getAppContext().getString(R.string.empresa);
        String PARTIDABASE = "partidabase";
        String NUEVOPRESUPUESTO = getAppContext().getString(R.string.nuevo_presupuesto);
        String NUEVOPROYECTO = getAppContext().getString(R.string.nuevo_proyecto);
        String NUEVOCLIENTE = getAppContext().getString(R.string.nuevo_cliente);
        String NUEVOPROSPECTO = getAppContext().getString(R.string.nuevo_prospecto);
        String NUEVOEVENTO = getAppContext().getString(R.string.nuevo_evento);
        String NUEVANOTA = getAppContext().getString(R.string.nueva_nota);
        String NUEVOGASTOFIJO = getAppContext().getString(R.string.nuevo_gasto);
        String NUEVAPARTIDA = getAppContext().getString(R.string.nueva_partida_proy);
        String NUEVOPERFIL = getAppContext().getString(R.string.nuevo_perfil);
        String NUEVAAMORTIZACION = getAppContext().getString(R.string.nueva_amortizacion);
        String NUEVAPARTIDABASE = getAppContext().getString(R.string.nueva_partidabase);
        String CLIENTE = "cliente";
        String MIPERFIL = "mi_perfil";
        String SIGUIENDO = "siguiendo";
        String SIGUIENDOPRO = "siguiendo_pro";
        String SIGUIENDOPRODCLI = "siguiendo_prodcli";
        String SIGUIENDOPRODPRO = "siguiendo_prodpro";
        String MISSORTEOS = "mis_sorteos";
        String MISSORTEOSCLI = "mis_sorteoscli";
        String MISSORTEOSPRO = "mis_sorteospro";
        String TCLIENTE = getAppContext().getString(R.string.t_cliente);
        String CLIENTES = getAppContext().getString(R.string.clientes);
        String TTIPOCLIENTE = getAppContext().getString(R.string.tipo_cliente);
        String TIPOCLIENTE = "tipo_cliente";
        String ESTADO = "estado";
        String PRODUCTO = "producto";
        String PRODUCTOPRO = "producto_pro";
        String PRODUCTOCLI = "producto_cli";
        String TAREA = "tarea";
        String GASTOFIJO = "gastofijo";
        String DETPARTIDABASE = "detpartidabase";
        String DETPARTIDA = "detpartida";
        String PROVCAT = "provcat";
        String USADO = "usado";
        String PRODPROVCAT = "prodprovcat";
        String PEDIDOPROVCAT = "pedido_prov_cat";
        String DETPEDIDOPROVCAT = "detalle_pedido_prov_cat";
        String PEDIDOPROVEEDOR = "pedido_proveedor";
        String DETPEDIDOPROVEEDOR = "detalle_pedido_proveedor";
        String PEDIDOCLIENTE = "pedido_cliente";
        String PROSPECTO = "prospecto";
        String TPROSPECTO = getAppContext().getString(R.string.t_prospecto);
        String PROSPECTOS = getAppContext().getString(R.string.prospectos);
        String PRESUPUESTO = "presupuesto";
        String TPRESUPUESTO = getAppContext().getString(R.string.t_presupuesto);
        String PRESUPUESTOS = getAppContext().getString(R.string.presupuestos);
        String PROYECTO = "proyecto";
        String TPROYECTO = getAppContext().getString(R.string.t_proyecto);
        String PROYECTOS = getAppContext().getString(R.string.proyectos);
        String COBROS = "Proyectos cobros";
        String PROYCOBROS = getAppContext().getString(R.string.proyectos_cobros);
        String HISTORICO = "Proyectos historico";
        String PROYHISTORICO = getAppContext().getString(R.string.proyectos_historico);
        String GARANTIA = "Proyectos garantia";
        String PROYGARANTIA = getAppContext().getString(R.string.proyectos_garantia);
        String AGENDA = "Agenda";
        String DIARIO = "Notas";
        String TRABAJOS = "Trabajos";
        String EVENTO = "evento";
        String PERFILTR = "perfiltr";
        String NOTA = "nota";
        String NOTAS = "notas";
        String TRABAJO = "trabajo";
        String AMORTIZACION = "amortizacion";
        String GASTOSFIJOS = "gastos fijos";
        String PROVEEDOR = "proveedor";
        String PARTIDA = "partida";
        String PARTIDAS = getAppContext().getString(R.string.partidas);
        String GASTO = getAppContext().getString(R.string.gasto);
        String GASTOS = getAppContext().getString(R.string.gastos);
        String HABITUAL = getAppContext().getString(R.string.habitual);
        String OCASIONAL = getAppContext().getString(R.string.ocasional);
        String PRINCIPAL = getAppContext().getString(R.string.principal);
        String GANADORSORTEO = "ganador_sorteo";
        int COD_SELECCIONA = 10;
        int COD_FOTO = 20;
        String CARPETA_PRINCIPAL = "freelanceproyect/";
        String CARPETA_IMAGEN = "imagenes";
        String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;
        String PROVIDERFILE = BuildConfig.APPLICATION_ID + ".providerFreelanceProject";
        String ACCION_AVISOEVENTO = "com.jjlacode.freelanceproject.action.AVISOEVENTO";
        String ACCION_AVISOSORTEO = "com.jjlacode.freelanceproject.action.AVISOSORTEO";
        String ACCION_POSPONER = "com.jjlacode.freelanceproject.action.POSPONER";
        String ACCION_CANCELAR = "com.jjlacode.freelanceproject.action.CANCELAR";
        String ACCION_VER = "com.jjlacode.freelanceproject.action.VER";
        String ACCION_VERLUGAR = "com.jjlacode.freelanceproject.action.VERLUGAR";
        String STARTSERVER = "Servicio iniciado";
        String STOPSERVER = "Servicio detenido";
        String EXTRA_IDEVENTO = "com.jjlacode.freelanceproject.EXTRA_IDEVENTO";
        String EXTRA_BUNDLE = "com.jjlacode.freelanceproject.EXTRA_BUNDLE";
        String EXTRA_ACCION = "com.jjlacode.freelanceproject.EXTRA_ACCION";
        String EXTRA_SORTEO = "com.jjlacode.freelanceproject.EXTRA_SORTEO";
        String EXTRA_PRODUCTO = "com.jjlacode.freelanceproject.EXTRA_PRODUCTO";
        String EXTRA_GANADOR = "com.jjlacode.freelanceproject.EXTRA_GANADOR";


    }

    public interface TiposEstados {

        int TNUEVOPRESUP = 1;
        int TPRESUPPENDENTREGA = 2;
        int TPRESUPESPERA = 3;
        int TPRESUPACEPTADO = 4;
        int TPROYECTEJECUCION = 5;
        int TPROYECPENDENTREGA = 6;
        int TPROYECTPENDCOBRO = 7;
        int TPROYECTCOBRADO = 8;
        int TPROYECTHISTORICO = 9;
        int TPRESUPNOACEPTADO = 0;
    }

    public interface Estados {

        String NUEVOPRESUP = getAppContext().getString(R.string.nuevo_presupuesto_est);
        String PRESUPPENDENTREGA = getAppContext().getString(R.string.presupuesto_pendiente_entrega_est);
        String PRESUPESPERA = getAppContext().getString(R.string.presupuesto_en_espera_aceptar_est);
        String PRESUPACEPTADO = getAppContext().getString(R.string.presupuesto_aceptado_pendiente_ejecucion_est);
        String PROYECTEJECUCION = getAppContext().getString(R.string.proyecto_en_ejecucion_est);
        String PROYECPENDENTREGA = getAppContext().getString(R.string.proyecto_pendiente_entrega_est);
        String PROYECTPENDCOBRO = getAppContext().getString(R.string.proyecto_entregado_pendiente_cobro_est);
        String PROYECTCOBRADO = getAppContext().getString(R.string.proyecto_cobrado_est);
        String PROYECTHISTORICO = getAppContext().getString(R.string.proyecto_historico_est);
        String PRESUPNOACEPTADO = getAppContext().getString(R.string.presupuesto_no_aceptado_est);
    }

    public interface TiposEvento {

        String TIPOEVENTOTAREA = "Tarea";
        String TIPOEVENTOCITA = "Cita";
        String TIPOEVENTOLLAMADA = "Llamada";
        String TIPOEVENTOEMAIL = "Email";
        String TIPOEVENTOEVENTO = "Evento";
    }

    public interface TiposDetPartida {

        String TIPOTRABAJO = TRABAJO;
        String TIPOPRODUCTO = PRODUCTO;
        String TIPOPRODUCTOPROV = PRODPROVCAT;
        String TIPOPARTIDA = PARTIDA;
    }

    public interface TiposNota {

        String NOTATEXTO = "Nota de Texto";
        String NOTAAUDIO = "Nota de Audio";
        String NOTAVIDEO = "Nota de Video";
        String NOTAIMAGEN = "Nota de Imagen";
    }

    public static ArrayList<DestinosVoz> getListaDestinosVoz() {

        ArrayList<DestinosVoz> listaDestinos = new ArrayList<>();
        listaDestinos.add(new DestinosVoz(INICIO.toLowerCase(), new MenuInicio()));
        listaDestinos.add(new DestinosVoz(CALENDARIO.toLowerCase(), new CalendarioEventos()));

        return listaDestinos;
    }

    public static ArrayList<DestinosVoz> getListaNuevosDestinosVoz() {

        ArrayList<DestinosVoz> listaDestinos = new ArrayList<>();
        //listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.evento).toLowerCase(),
        //        new FragmentNuevoEvento()));

        return listaDestinos;
    }

    public static String getNombreUser() {

        return null;
    }


    public static String setNamefdef() {

        hora = Calculos.calculoPrecioHora();
        int longitud = perfila.length();
        String perfil = null;
        if (longitud > 10) {
            perfil = perfila.substring(0, 10);
        } else {
            perfil = perfila;
        }

        if (prioridad) {

            //namesubdef = "Perfil: " + perfil + " hora: " +  JavaUtil.formatoMonedaLocal(hora) + " - P";
            namesubdef = AndroidUtil.getSharePreference(getAppContext(), PREFERENCIAS, PERFILUSER, NULL);


        } else {

            //namesubdef = "Perfil: " + perfil + " hora: " + JavaUtil.formatoMonedaLocal(hora);
            namesubdef = AndroidUtil.getSharePreference(getAppContext(), PREFERENCIAS, PERFILUSER, NULL);

        }

        return namesubdef;
    }

    public static void notificationSorteo(Context contexto, Class<?> clase, String idGanador,
                                          String idSorteo, int id, int iconId, String titulo, String contenido) {

        RemoteViews remoteView = new RemoteViews(contexto.getPackageName(), R.layout.notificacion_chat);
        remoteView.setTextViewText(R.id.tvdescnotchat, contenido);

        Intent intentVerChat = new Intent(contexto, clase);
        intentVerChat.setAction(ACCION_VERSORTEO);
        intentVerChat.putExtra(EXTRA_ACTUAL, SORTEO);
        intentVerChat.putExtra(EXTRA_SORTEO, idSorteo);
        intentVerChat.putExtra(EXTRA_GANADOR, idGanador);
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
                        .setWhen(TimeDateUtil.ahora())
                        .setColor(contexto.getResources().getColor(R.color.colorPrimary))
                        .setTicker(titulo)
                        .setVibrate(new long[]{100, 250, 100, 500})
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setSound(Uri.parse("android.resource://" + contexto.getPackageName() + "/" + R.raw.popcorn))
                        .setContent(remoteView)
                        .setAutoCancel(true);

        Notification notification = builder.build();
        NotificationManager notifyMgrSorteo = (NotificationManager) contexto.getSystemService(NOTIFICATION_SERVICE);

        // Construir la notificación y emitirla
        notifyMgrSorteo.notify(id, notification);


    }

    public static void notificationEvento(Context contexto, Class<?> clase, Modelo evento, String actual,
                                          int id, int iconId, String titulo, String contenido) {

        RemoteViews remoteView = new RemoteViews(contexto.getPackageName(), R.layout.notificacion_evento_evento);
        remoteView.setTextViewText(R.id.tvdescnot, evento.getString(EVENTO_DESCRIPCION));

        String tipo = evento.getString(EVENTO_TIPO);
        String idEvento = null;
        if (tipo.equals(TiposEvento.TIPOEVENTOCITA)) {
            remoteView = new RemoteViews(contexto.getPackageName(), R.layout.notificacion_evento_cita);

            idEvento = evento.getString(EVENTO_ID_EVENTO);
            String direccion = evento.getString(EVENTO_DIRECCION);
            String dir = direccion.substring(0, 35);
            remoteView.setTextViewText(R.id.tvdescnot, evento.getString(EVENTO_DESCRIPCION));
            remoteView.setTextViewText(R.id.tvlugarnot, dir);

            String address = evento.getString(EVENTO_DIRECCION);
            String urlMap = null;

            if (address != null) {
                address = address.replace(" ", "+");
                String address2 = address.replace(",", "%2C");
                String baseUrl = "https://www.google.com/maps/search/?api=1&query=";
                urlMap = String.format("%s%s", baseUrl, address2);

                System.out.println("address = " + address);

            }

            Intent intentMapa = null;
            try {

                intentMapa = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(String.format("geo:0,0?q=%s",
                                URLEncoder.encode(address, "UTF-8"))));

                PackageManager packageManager = contexto.getPackageManager();
                List activities = packageManager.queryIntentActivities(intentMapa,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    PendingIntent lugar = PendingIntent.getActivity(
                            contexto,
                            2,
                            intentMapa,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteView.setOnClickPendingIntent(R.id.imgmapanot, lugar);
                } else {
                    System.out.println("urlMap = " + urlMap);
                    intentMapa = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s",
                            URLEncoder.encode(urlMap, "UTF-8"))));
                    contexto.startActivity(intentMapa);
                    PendingIntent lugar = PendingIntent.getActivity(
                            contexto,
                            3,
                            intentMapa,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteView.setOnClickPendingIntent(R.id.imgmapanot, lugar);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intentCancelarCita = new Intent(contexto, EventosReceiver.class);
            intentCancelarCita.setAction(ACCION_CANCELAR);
            intentCancelarCita.putExtra(EXTRA_IDEVENTO, idEvento);
            intentCancelarCita.putExtra(EXTRA_ACTUAL, actual);
            intentCancelarCita.putExtra(EXTRA_ID, id);
            PendingIntent cancelarCita = PendingIntent.getBroadcast(
                    contexto,
                    id,
                    intentCancelarCita,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.btndescartarrnotcita, cancelarCita);

            Intent intentVerCita = new Intent(contexto, clase);
            intentVerCita.setAction(ACCION_VER);
            intentVerCita.putExtra(EXTRA_IDEVENTO, idEvento);
            intentVerCita.putExtra(EXTRA_ACTUAL, actual);
            intentVerCita.putExtra(EXTRA_ID, id);

            intentVerCita.setFlags(FLAG_ACTIVITY_NEW_TASK);
            PendingIntent verCita = PendingIntent.getActivity(
                    contexto,
                    id,
                    intentVerCita,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.btnvernotcita, verCita);

            Intent intentPosponerCita = new Intent(contexto, EventosReceiver.class);

            intentPosponerCita.setAction(ACCION_POSPONER);
            intentPosponerCita.putExtra(EXTRA_IDEVENTO, idEvento);
            intentPosponerCita.putExtra(EXTRA_ACTUAL, actual);
            intentPosponerCita.putExtra(EXTRA_ID, id);

            PendingIntent posponerCita = PendingIntent.getBroadcast(
                    contexto,
                    id,
                    intentPosponerCita,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.btnposponernotcita, posponerCita);

            System.out.println("id = " + id);

            // Estructurar la notificación
            Notification.Builder builder =
                    new Notification.Builder(contexto)
                            .setDefaults(Notification.DEFAULT_LIGHTS)
                            .setSmallIcon(iconId)
                            .setContentTitle(titulo)
                            .setContentText(contenido)
                            .setWhen(evento.getLong(EVENTO_HORAINIEVENTO))
                            .setColor(contexto.getResources().getColor(R.color.colorPrimary))
                            .setTicker(titulo)
                            .setVibrate(new long[]{100, 250, 100, 500})
                            .setVisibility(Notification.VISIBILITY_PUBLIC)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setSound(Uri.parse("android.resource://" + contexto.getPackageName() + "/" + R.raw.popcorn))
                            .setContent(remoteView)
                            .setAutoCancel(true);

            Notification notification = builder.build();
            NotificationManager notifyMgrCita = (NotificationManager) contexto.getSystemService(NOTIFICATION_SERVICE);

            // Construir la notificación y emitirla
            notifyMgrCita.notify(id, notification);

        } else if (tipo.equals(TiposEvento.TIPOEVENTOLLAMADA)) {

            idEvento = evento.getString(EVENTO_ID_EVENTO);
            remoteView = new RemoteViews(contexto.getPackageName(), R.layout.notificacion_evento_llamada);

            remoteView.setTextViewText(R.id.tvdescnot, evento.getString(EVENTO_DESCRIPCION));
            remoteView.setTextViewText(R.id.tvtelefononot, evento.getString(EVENTO_TELEFONO));
            Uri uri = Uri.parse("tel:" + evento.getString(EVENTO_TELEFONO));
            Intent intentllamada = new Intent(Intent.ACTION_DIAL, uri);
            intentllamada.addFlags(FLAG_ACTIVITY_NEW_TASK);

            PendingIntent llamada = PendingIntent.getActivity(
                    contexto,
                    4,
                    intentllamada,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            remoteView.setOnClickPendingIntent(R.id.imgllamadanot, llamada);

            Intent intentCancelarLlamada = new Intent(contexto, EventosReceiver.class);
            intentCancelarLlamada.setAction(ACCION_CANCELAR);
            intentCancelarLlamada.putExtra(EXTRA_IDEVENTO, idEvento);
            intentCancelarLlamada.putExtra(EXTRA_ACTUAL, actual);
            intentCancelarLlamada.putExtra(EXTRA_ID, id);
            PendingIntent cancelarLlamada = PendingIntent.getBroadcast(
                    contexto,
                    id,
                    intentCancelarLlamada,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.btndescartarrnotllamada, cancelarLlamada);

            Intent intentVerLlamada = new Intent(contexto, clase);
            intentVerLlamada.setAction(ACCION_VER);
            intentVerLlamada.putExtra(EXTRA_IDEVENTO, idEvento);
            intentVerLlamada.putExtra(EXTRA_ACTUAL, actual);
            intentVerLlamada.putExtra(EXTRA_ID, id);

            intentVerLlamada.setFlags(FLAG_ACTIVITY_NEW_TASK);
            PendingIntent verLlamada = PendingIntent.getActivity(
                    contexto,
                    id,
                    intentVerLlamada,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.btnvernotllamada, verLlamada);

            Intent intentPosponerLlamada = new Intent(contexto, EventosReceiver.class);

            intentPosponerLlamada.setAction(ACCION_POSPONER);
            intentPosponerLlamada.putExtra(EXTRA_IDEVENTO, idEvento);
            intentPosponerLlamada.putExtra(EXTRA_ACTUAL, actual);
            intentPosponerLlamada.putExtra(EXTRA_ID, id);

            PendingIntent posponerLlamada = PendingIntent.getBroadcast(
                    contexto,
                    id,
                    intentPosponerLlamada,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.btnposponernotllamada, posponerLlamada);

            System.out.println("id = " + id);

            // Estructurar la notificación
            Notification.Builder builder =
                    new Notification.Builder(contexto)
                            .setDefaults(Notification.DEFAULT_LIGHTS)
                            .setSmallIcon(iconId)
                            .setContentTitle(titulo)
                            .setContentText(contenido)
                            .setWhen(evento.getLong(EVENTO_HORAINIEVENTO))
                            .setColor(contexto.getResources().getColor(R.color.colorPrimary))
                            .setTicker(titulo)
                            .setVibrate(new long[]{100, 250, 100, 500})
                            .setVisibility(Notification.VISIBILITY_PUBLIC)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setSound(Uri.parse("android.resource://" + contexto.getPackageName() + "/" + R.raw.popcorn))
                            .setContent(remoteView)
                            .setAutoCancel(true);

            Notification notification = builder.build();
            NotificationManager notifyMgrLlamada = (NotificationManager) contexto.getSystemService(NOTIFICATION_SERVICE);

            // Construir la notificación y emitirla
            notifyMgrLlamada.notify(id, notification);

        } else if (tipo.equals(TiposEvento.TIPOEVENTOEMAIL)) {

            idEvento = evento.getString(EVENTO_ID_EVENTO);
            remoteView = new RemoteViews(contexto.getPackageName(), R.layout.notificacion_evento_email);

            remoteView.setTextViewText(R.id.tvdescnot, evento.getString(EVENTO_DESCRIPCION));
            remoteView.setTextViewText(R.id.tvemailnot, evento.getString(EVENTO_EMAIL));
            String[] dir = {evento.getString(EVENTO_EMAIL)};
            Intent intentmail = new Intent(Intent.ACTION_SEND);
            intentmail.setData(Uri.parse("mailto:"));
            if (!TextUtils.isEmpty(evento.getString(EVENTO_EMAIL))) {
                intentmail.putExtra(EXTRA_EMAIL, dir);
                intentmail.putExtra(EXTRA_SUBJECT, evento.getString(EVENTO_ASUNTO));
                intentmail.putExtra(EXTRA_TEXT, evento.getString(EVENTO_MENSAJE));
                if (evento.getString(EVENTO_RUTAADJUNTO) != null) {
                    intentmail.setType("application/pdf");
                    intentmail.putExtra(Intent.EXTRA_STREAM, Uri.parse(evento.getString(EVENTO_RUTAADJUNTO)));
                }
                if (intentmail.resolveActivity(contexto.getPackageManager()) == null) {
                    Toast.makeText(contexto, "No hay disponible ninguna app de email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(contexto, "La dirección de email no es valida", Toast.LENGTH_SHORT).show();
            }
            intentmail.addFlags(FLAG_ACTIVITY_NEW_TASK);

            PendingIntent email = PendingIntent.getActivity(
                    contexto,
                    id,
                    intentmail,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            remoteView.setOnClickPendingIntent(R.id.imgemailnot, email);

            Intent intentCancelarEmail = new Intent(contexto, EventosReceiver.class);
            intentCancelarEmail.setAction(ACCION_CANCELAR);
            intentCancelarEmail.putExtra(EXTRA_IDEVENTO, idEvento);
            intentCancelarEmail.putExtra(EXTRA_ACTUAL, actual);
            intentCancelarEmail.putExtra(EXTRA_ID, id);
            PendingIntent cancelarEmail = PendingIntent.getBroadcast(
                    contexto,
                    id,
                    intentCancelarEmail,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.btndescartarrnotemail, cancelarEmail);

            Intent intentVerEmail = new Intent(contexto, clase);
            intentVerEmail.setAction(ACCION_VER);
            intentVerEmail.putExtra(EXTRA_IDEVENTO, idEvento);
            intentVerEmail.putExtra(EXTRA_ACTUAL, actual);
            intentVerEmail.putExtra(EXTRA_ID, id);

            intentVerEmail.setFlags(FLAG_ACTIVITY_NEW_TASK);
            PendingIntent verEmail = PendingIntent.getActivity(
                    contexto,
                    id,
                    intentVerEmail,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.btnvernotemail, verEmail);

            Intent intentPosponerEmail = new Intent(contexto, EventosReceiver.class);

            intentPosponerEmail.setAction(ACCION_POSPONER);
            intentPosponerEmail.putExtra(EXTRA_IDEVENTO, idEvento);
            intentPosponerEmail.putExtra(EXTRA_ACTUAL, actual);
            intentPosponerEmail.putExtra(EXTRA_ID, id);

            PendingIntent posponerEmail = PendingIntent.getBroadcast(
                    contexto,
                    id,
                    intentPosponerEmail,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.btnposponernotemail, posponerEmail);

            System.out.println("id = " + id);

            // Estructurar la notificación
            Notification.Builder builder =
                    new Notification.Builder(contexto)
                            .setDefaults(Notification.DEFAULT_LIGHTS)
                            .setSmallIcon(iconId)
                            .setContentTitle(titulo)
                            .setContentText(contenido)
                            .setWhen(evento.getLong(EVENTO_HORAINIEVENTO))
                            .setColor(contexto.getResources().getColor(R.color.colorPrimary))
                            .setTicker(titulo)
                            .setVibrate(new long[]{100, 250, 100, 500})
                            .setVisibility(Notification.VISIBILITY_PUBLIC)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setSound(Uri.parse("android.resource://" + contexto.getPackageName() + "/" + R.raw.popcorn))
                            .setContent(remoteView)
                            .setAutoCancel(true);

            Notification notification = builder.build();
            NotificationManager notifyMgrEmail = (NotificationManager) contexto.getSystemService(NOTIFICATION_SERVICE);

            // Construir la notificación y emitirla
            notifyMgrEmail.notify(id, notification);

        } else if (tipo.equals(TiposEvento.TIPOEVENTOEVENTO)) {

            idEvento = evento.getString(EVENTO_ID_EVENTO);

            Intent intentCancelarEvento = new Intent(contexto, EventosReceiver.class);
            intentCancelarEvento.setAction(ACCION_CANCELAR);
            intentCancelarEvento.putExtra(EXTRA_IDEVENTO, idEvento);
            intentCancelarEvento.putExtra(EXTRA_ACTUAL, actual);
            intentCancelarEvento.putExtra(EXTRA_ID, id);
            PendingIntent cancelarEvento = PendingIntent.getBroadcast(
                    contexto,
                    id,
                    intentCancelarEvento,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.btndescartarrnotevento, cancelarEvento);

            Intent intentVerEvento = new Intent(contexto, clase);
            intentVerEvento.setAction(ACCION_VER);
            intentVerEvento.putExtra(EXTRA_IDEVENTO, idEvento);
            intentVerEvento.putExtra(EXTRA_ACTUAL, actual);
            intentVerEvento.putExtra(EXTRA_ID, id);

            intentVerEvento.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
            PendingIntent verEvento = PendingIntent.getActivity(
                    contexto,
                    id,
                    intentVerEvento,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.btnvernotevento, verEvento);

            Intent intentPosponerEvento = new Intent(contexto, EventosReceiver.class);

            intentPosponerEvento.setAction(ACCION_POSPONER);
            intentPosponerEvento.putExtra(EXTRA_IDEVENTO, idEvento);
            intentPosponerEvento.putExtra(EXTRA_ACTUAL, actual);
            intentPosponerEvento.putExtra(EXTRA_ID, id);

            PendingIntent posponerEvento = PendingIntent.getBroadcast(
                    contexto,
                    id,
                    intentPosponerEvento,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteView.setOnClickPendingIntent(R.id.btnposponernotevento, posponerEvento);

            System.out.println("id = " + id);

            // Estructurar la notificación
            Notification.Builder builder =
                    new Notification.Builder(contexto)
                            .setDefaults(Notification.DEFAULT_LIGHTS)
                            .setSmallIcon(iconId)
                            .setContentTitle(titulo)
                            .setContentText(contenido)
                            .setWhen(evento.getLong(EVENTO_HORAINIEVENTO))
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

    }


    public static long fechaEntregaCalculada(double horastrabajos, double hlunes, double hmartes,
                                             double hmiercoles, double hjueves, double hviernes,
                                             double hsabado, double hdomingo) {

        TimeZone timezone = TimeZone.getDefault();
        Calendar calendar = new GregorianCalendar(timezone);
        int diahoy = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        long totaldias = 0;
        while (horastrabajos > 1) {

            switch (diahoy) {
                case 1:
                    horastrabajos -= hmartes;
                    totaldias++;
                    if (horastrabajos < 1) {
                        if (hmartes <= 0) {
                            horastrabajos++;
                        } else {
                            break;
                        }
                    }
                case 2:
                    horastrabajos -= hmiercoles;
                    totaldias++;
                    if (horastrabajos < 1) {
                        if (hmiercoles <= 0) {
                            horastrabajos++;
                        } else {
                            break;
                        }
                    }
                case 3:
                    horastrabajos -= hjueves;
                    totaldias++;
                    if (horastrabajos < 1) {
                        if (hjueves <= 0) {
                            horastrabajos++;
                        } else {
                            break;
                        }
                    }
                case 4:
                    horastrabajos -= hviernes;
                    totaldias++;
                    if (horastrabajos < 1) {
                        if (hviernes <= 0) {
                            horastrabajos++;
                        } else {
                            break;
                        }
                    }
                case 5:
                    horastrabajos -= hsabado;
                    totaldias++;
                    if (horastrabajos < 1) {
                        if (hsabado <= 0) {
                            horastrabajos++;
                        } else {
                            break;
                        }
                    }
                case 6:
                    horastrabajos -= hdomingo;
                    totaldias++;
                    if (horastrabajos < 1) {
                        if (hdomingo <= 0) {
                            horastrabajos++;
                        } else {
                            break;
                        }
                    }
                case 7:
                    horastrabajos -= hlunes;
                    totaldias++;
                    if (horastrabajos < 1) {
                        if (hlunes <= 0) {
                            horastrabajos++;
                        }
                    }
            }
            diahoy = 1;
        }
        long fechahoy = calendar.getTimeInMillis();
        return fechahoy + (totaldias * 24 * 60 * 60 * 1000);

    }

    public static int getTipoEstado(String idEstado) {

        ArrayList<Modelo> listaEstados = queryList(CAMPOS_ESTADO, null, null);

        for (Modelo estado : listaEstados) {

            if (estado.getString(ESTADO_ID_ESTADO).equals(idEstado)) {

                return estado.getInt(ESTADO_TIPOESTADO);
            }

        }
        return -1;
    }

    public static class Calculos implements ConstantesPry, TiposDetPartida {


        public static ArrayList<Modelo> comprobarEventos() {

            long hoy = JavaUtil.hoy();
            ArrayList<Modelo> lista = new ArrayList<>();

            ListaModelo listaEventos = new ListaModelo(CAMPOS_EVENTO);

            for (Modelo evento : listaEventos.getLista()) {

                long aviso = evento.getLong(EVENTO_AVISO);

                if (aviso > 0) {

                    if (hoy >= evento.getLong(EVENTO_FECHAINIEVENTO) +
                            evento.getLong(EVENTO_HORAINIEVENTO) -
                            evento.getLong(EVENTO_AVISO)) {

                        lista.add(evento);

                    }

                    System.out.println(JavaUtil.getDateTime(evento.getLong(EVENTO_FECHAINIEVENTO) +
                            evento.getLong(EVENTO_HORAINIEVENTO) -
                            evento.getLong(EVENTO_AVISO)));
                    System.out.println("hoy = " + hoy);
                }
            }

            return lista;
        }

        public static double calculoPrecioHora() {

            ArrayList<Modelo> listaAmortizaciones = ConsultaBD.queryList(CAMPOS_AMORTIZACION);

            ArrayList<Modelo> listaGastosFijos = ConsultaBD.queryList(CAMPOS_GASTOFIJO);

            long hoy = JavaUtil.hoy();
            double precioHoraAmortizaciones = 0;

            for (Modelo amortizacion : listaAmortizaciones) {

                long fecha = amortizacion.getLong(AMORTIZACION_FECHACOMPRA);

                long horas = (amortizacion.getInt(AMORTIZACION_ANYOS) * 365 * 24) +
                        (amortizacion.getInt(AMORTIZACION_MESES) * 30 * 24) +
                        (amortizacion.getInt(AMORTIZACION_DIAS) * 24);
                if (fecha + (horas * 60 * 60 * 1000) > hoy) {

                    precioHoraAmortizaciones += amortizacion.getDouble(AMORTIZACION_PRECIO) / (double) horas;

                }
            }

            double precioHoraGastosFijos = 0;

            for (Modelo gastoFijo : listaGastosFijos) {

                long horas = (gastoFijo.getInt(GASTOFIJO_ANYOS) * 365 * 24) +
                        (gastoFijo.getInt(GASTOFIJO_MESES) * 30 * 24) +
                        (gastoFijo.getInt(GASTOFIJO_DIAS) * 24);

                precioHoraGastosFijos += gastoFijo.getDouble(GASTOFIJO_PRECIO) / (double) horas;
            }

            double totalAmortizacionesYGastos = (precioHoraAmortizaciones + precioHoraGastosFijos) * 24 * 365;

            Modelo perfil = ConsultaBD.queryObject(CAMPOS_PERFIL, PERFIL_NOMBRE, perfila, null, IGUAL, null);


            double beneficio = perfil.getDouble(PERFIL_BENEFICIO);
            double totHoras = perfil.getDouble(PERFIL_HORASLUNES) +
                    perfil.getDouble(PERFIL_HORASMARTES) +
                    perfil.getDouble(PERFIL_HORASMIERCOLES) +
                    perfil.getDouble(PERFIL_HORASJUEVES) +
                    perfil.getDouble(PERFIL_HORASVIERNES) +
                    perfil.getDouble(PERFIL_HORASSABADO) +
                    perfil.getDouble(PERFIL_HORASDOMINGO);

            totalAmortizacionesYGastos += perfil.getDouble(PERFIL_SUELDO);

            int semanas = JavaUtil.semanasAnio();
            double horasTrabajadasAnyo = semanas * totHoras;
            double horasVacacionesARestar = Math.round(perfil.getDouble(PERFIL_VACACIONES) / 7) * totHoras;
            double base = totalAmortizacionesYGastos / (horasTrabajadasAnyo - horasVacacionesARestar);
            double hora = (base + ((base / 100) * beneficio));

            return hora;
        }

        public static double calculoCosteHora() {

            double horapvp = calculoPrecioHora();
            Modelo perfil = ConsultaBD.queryObject(CAMPOS_PERFIL, PERFIL_NOMBRE, perfila, null, IGUAL, null);
            double beneficio = perfil.getDouble(PERFIL_BENEFICIO);

            return horapvp / (1 + (beneficio / 100));

        }

        public static void recalcularFechas() {

            Modelo perfilActivo = ConsultaBD.queryObject(CAMPOS_PERFIL, PERFIL_NOMBRE, perfila, null, IGUAL, null);

            String ordenProyectosFecha = PROYECTO_FECHAENTRADA + " ASC";

            ArrayList<Modelo> listaProyectos = ConsultaBD.queryList(CAMPOS_PROYECTO, null, ordenProyectosFecha);

            for (int i = 0; i < listaProyectos.size(); i++) {

                double horasTrabajoPendientes = 0;

                Modelo proyecto = listaProyectos.get(i);

                if (proyecto.getInt(PROYECTO_TIPOESTADO) != 0) {

                    if (proyecto.getInt(PROYECTO_TIPOESTADO) > 6) {

                        if (proyecto.getLong(PROYECTO_FECHAFINAL) == 0) {

                            ContentValues valores = new ContentValues();
                            ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_FECHAFINAL, JavaUtil.hoy());
                            ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_FECHAFINALF, JavaUtil.getDate(JavaUtil.hoy()));
                            ConsultaBD.updateRegistro(TABLA_PROYECTO, proyecto.getString(PROYECTO_ID_PROYECTO), valores);

                        }

                    } else {

                        for (int x = i; x < listaProyectos.size(); x++) {

                            Modelo proyecto2 = listaProyectos.get(x);

                            if ((proyecto2.getInt(PROYECTO_CLIENTE_PESOTIPOCLI) >=
                                    proyecto.getInt(PROYECTO_CLIENTE_PESOTIPOCLI)) || (!prioridad)) {


                                double horasrealizadas =
                                        (proyecto2.getDouble(PROYECTO_TIEMPO) / 100)
                                                * proyecto2.getInt(PROYECTO_TOTCOMPLETADO);

                                horasTrabajoPendientes += proyecto2.getDouble(PROYECTO_TIEMPO)
                                        - horasrealizadas;


                            }
                        }
                        System.out.println("horasTrabajoPendientes = " + horasTrabajoPendientes);
                        long fecha = fechaEntregaCalculada(horasTrabajoPendientes,
                                perfilActivo.getDouble(PERFIL_HORASLUNES),
                                perfilActivo.getDouble(PERFIL_HORASMARTES),
                                perfilActivo.getDouble(PERFIL_HORASMIERCOLES),
                                perfilActivo.getDouble(PERFIL_HORASJUEVES),
                                perfilActivo.getDouble(PERFIL_HORASVIERNES),
                                perfilActivo.getDouble(PERFIL_HORASSABADO),
                                perfilActivo.getDouble(PERFIL_HORASDOMINGO));

                        ContentValues valores = new ContentValues();
                        ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_FECHAENTREGACALCULADA, fecha);
                        ConsultaBD.updateRegistro(TABLA_PROYECTO, proyecto.getString(PROYECTO_ID_PROYECTO), valores);
                    }

                }
            }
        }


        public static void calcularTipoCliente(String idCliente) {

            String idPrincipal = null;
            String idHabitual = null;
            String idOcasional = null;
            String idNuevo = null;
            String idProspecto = null;
            int pesoPrincipal = 0;
            int pesoHabitual = 0;
            int pesoOcasional = 0;
            int pesoNuevo = 0;
            int pesoProspecto = 0;

            ArrayList<Modelo> listaTipoCliente = ConsultaBD.queryList
                    (CAMPOS_TIPOCLIENTE, null, null);

            for (Modelo tipoCliente : listaTipoCliente) {

                if (tipoCliente.getString(TIPOCLIENTE_DESCRIPCION).equals(PRINCIPAL)) {

                    idPrincipal = tipoCliente.getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                    pesoPrincipal = tipoCliente.getInt(TIPOCLIENTE_PESO);

                } else if (tipoCliente.getString(TIPOCLIENTE_DESCRIPCION).equals(HABITUAL)) {

                    idHabitual = tipoCliente.getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                    pesoHabitual = tipoCliente.getInt(TIPOCLIENTE_PESO);

                } else if (tipoCliente.getString(TIPOCLIENTE_DESCRIPCION).equals(NUEVO)) {

                    idNuevo = tipoCliente.getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                    pesoNuevo = tipoCliente.getInt(TIPOCLIENTE_PESO);

                } else if (tipoCliente.getString(TIPOCLIENTE_DESCRIPCION).equals(OCASIONAL)) {

                    idOcasional = tipoCliente.getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                    pesoOcasional = tipoCliente.getInt(TIPOCLIENTE_PESO);

                } else if (tipoCliente.getString(TIPOCLIENTE_DESCRIPCION).equals(PROSPECTO)) {

                    idProspecto = tipoCliente.getString(TIPOCLIENTE_ID_TIPOCLIENTE);
                    pesoProspecto = tipoCliente.getInt(TIPOCLIENTE_PESO);

                }
            }

            ArrayList<Modelo> listaProyectos = ConsultaBD.queryList
                    (CAMPOS_PROYECTO, null, null);

            ArrayList<Modelo> lista = ConsultaBD.queryList
                    (CAMPOS_CLIENTE, null, null);

            Modelo cliente = null;
            for (Modelo item : lista) {

                if (item.getString(CLIENTE_ID_CLIENTE).equals(idCliente)) {
                    cliente = item;
                }
            }

            int totcli = 0;
            double ratioCliente = 0;
            boolean modificado = false;
            int estadoUltimoProyecto = 0;


            for (Modelo proyecto : listaProyectos) {

                if (!proyecto.getString(PROYECTO_DESCRIPCION_ESTADO).equals(Estados.PRESUPNOACEPTADO) &&
                        proyecto.getString(PROYECTO_ID_CLIENTE).equals(idCliente)) {

                    totcli++;
                    estadoUltimoProyecto = proyecto.getInt(PROYECTO_TIPOESTADO);

                }
            }

            ratioCliente = (double) (totcli * 100) / listaProyectos.size();

            if (ratioCliente >= 15 && !cliente.getString(CLIENTE_ID_TIPOCLIENTE).equals(idPrincipal) &&
                    listaProyectos.size() > 15) {

                cliente.setCampos(CLIENTE_ID_TIPOCLIENTE, idPrincipal);
                cliente.setCampos(CLIENTE_PESOTIPOCLI, (String.valueOf(pesoPrincipal)));
                modificado = true;

            } else if (ratioCliente >= 10 && !cliente.getString(CLIENTE_ID_TIPOCLIENTE).equals(idHabitual) &&
                    listaProyectos.size() > 10) {

                cliente.setCampos(CLIENTE_ID_TIPOCLIENTE, idHabitual);
                cliente.setCampos(CLIENTE_PESOTIPOCLI, (String.valueOf(pesoHabitual)));
                modificado = true;

            } else if (ratioCliente >= 3 && totcli > 1 && listaProyectos.size() > 10
                    && !cliente.getString(CLIENTE_ID_TIPOCLIENTE).equals(idOcasional)) {

                cliente.setCampos(CLIENTE_ID_TIPOCLIENTE, idOcasional);
                cliente.setCampos(CLIENTE_PESOTIPOCLI, (String.valueOf(pesoOcasional)));
                modificado = true;

            } else if (totcli == 1 && !cliente.getString(CLIENTE_ID_TIPOCLIENTE).equals(idNuevo) &&
                    estadoUltimoProyecto >= TiposEstados.TPRESUPACEPTADO) {

                cliente.setCampos(CLIENTE_ID_TIPOCLIENTE, idNuevo);
                cliente.setCampos(CLIENTE_PESOTIPOCLI, (String.valueOf(pesoNuevo)));
                modificado = true;

            }

            if (modificado && cliente.getString(CLIENTE_ID_TIPOCLIENTE) != null) {
                ContentValues valores = new ContentValues();
                ConsultaBD.putDato(valores, CAMPOS_CLIENTE, CLIENTE_ID_TIPOCLIENTE, cliente.getString(CLIENTE_ID_TIPOCLIENTE));
                ConsultaBD.putDato(valores, CAMPOS_CLIENTE, CLIENTE_PESOTIPOCLI, cliente.getInt(CLIENTE_PESOTIPOCLI));
                ConsultaBD.updateRegistro(TABLA_CLIENTE, cliente.getString(CLIENTE_ID_CLIENTE), valores);
            }

        }

        public static boolean sincronizarPartidaBase(String idPartidabase) {

            ArrayList<Modelo> listaDetPartida;
            ContentValues valores = null;
            listaDetPartida = ConsultaBD.queryListDetalle(CAMPOS_DETPARTIDABASE, idPartidabase, TABLA_PARTIDABASE);

            for (final Modelo detPartida : listaDetPartida) {

                final String id = detPartida.getString(DETPARTIDABASE_ID_DETPARTIDABASE);

                if (id == null) {
                    System.out.println("Id nulo");
                    return false;
                }

                String tipo = detPartida.getString(DETPARTIDABASE_TIPO);

                System.out.println("iddepbase = " + id);


                switch (tipo) {

                    case TIPOTRABAJO:

                        Modelo tarea = ConsultaBD.queryObject(CAMPOS_TRABAJO, id);

                        if (tarea != null && tarea.getLong(TRABAJO_TIMESTAMP) > detPartida.getLong(DETPARTIDABASE_TIMESTAMP)) {

                            valores = new ContentValues();
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_DESCRIPCION, tarea.getString(TRABAJO_DESCRIPCION));
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_NOMBRE, tarea.getString(TRABAJO_NOMBRE));
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_TIEMPO, tarea.getString(TRABAJO_TIEMPO));
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_RUTAFOTO, tarea.getString(TRABAJO_RUTAFOTO));

                            ConsultaBD.updateRegistroDetalle(TABLA_DETPARTIDABASE, detPartida.getString(DETPARTIDABASE_ID_PARTIDABASE), detPartida.getInt(DETPARTIDA_SECUENCIA), valores);
                        }
                        break;

                    case TIPOPRODUCTO:

                        Modelo producto = ConsultaBD.queryObject(CAMPOS_PRODUCTO, id);
                        if (producto != null && producto.getLong(PRODUCTO_TIMESTAMP) > detPartida.getLong(DETPARTIDABASE_TIMESTAMP)) {
                            valores = new ContentValues();
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_DESCRIPCION, producto.getString(PRODUCTO_DESCRIPCION));
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_NOMBRE, producto.getString(PRODUCTO_NOMBRE));
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_PRECIO, producto.getString(PRODUCTO_PRECIO));
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_RUTAFOTO, producto.getString(PRODUCTO_RUTAFOTO));

                            ConsultaBD.updateRegistroDetalle(TABLA_DETPARTIDABASE, detPartida.getString(DETPARTIDABASE_ID_PARTIDABASE), detPartida.getInt(DETPARTIDA_SECUENCIA), valores);
                        }

                        break;

                    case TIPOPARTIDA:

                        Modelo partida = ConsultaBD.queryObject(CAMPOS_PARTIDABASE, id);
                        if (partida != null && partida.getLong(PARTIDABASE_TIMESTAMP) > detPartida.getLong(DETPARTIDABASE_TIMESTAMP)) {
                            valores = new ContentValues();
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_DESCRIPCION, partida.getString(PARTIDABASE_DESCRIPCION));
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_NOMBRE, partida.getString(PARTIDABASE_NOMBRE));
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_TIEMPO, partida.getString(PARTIDABASE_TIEMPO));
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_PRECIO, partida.getString(PARTIDABASE_PRECIO));
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_RUTAFOTO, partida.getString(PARTIDABASE_RUTAFOTO));

                            ConsultaBD.updateRegistroDetalle(TABLA_DETPARTIDABASE, detPartida.getString(DETPARTIDABASE_ID_PARTIDABASE), detPartida.getInt(DETPARTIDA_SECUENCIA), valores);
                        }

                        break;


                    case TIPOPRODUCTOPROV:


                        DatabaseReference dbProveedor =
                                FirebaseDatabase.getInstance().getReference()
                                        .child("productos");

                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if (ds.getRef().getKey().equals(id)) {
                                        ProdProv prodProv = ds.getValue(ProdProv.class);
                                        if (prodProv != null) {
                                            System.out.println("prodProv = " + prodProv.getNombre());
                                            ContentValues valores = new ContentValues();
                                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_DESCRIPCION, prodProv.getDescripcion());
                                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_NOMBRE, prodProv.getNombre());
                                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_REFPROVCAT, prodProv.getRefprov());
                                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_PRECIO, prodProv.getPrecio());
                                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_RUTAFOTO, prodProv.getRutafoto());

                                            ConsultaBD.updateRegistroDetalle(TABLA_DETPARTIDABASE, detPartida.getString(DETPARTIDABASE_ID_PARTIDABASE), detPartida.getInt(DETPARTIDA_SECUENCIA), valores);
                                        }
                                        break;
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };

                        dbProveedor.addValueEventListener(eventListener);
                }
            }
            if (actualizarPartidaBase(idPartidabase)) {
                System.out.println("Partida base actualizada " + idPartidabase);
                return true;
            }
            return false;
        }

        public static boolean actualizarPartidaProyecto(String idPartida) {

            double tiempoPartida = 0;
            double importeProductosPartida = 0;
            double coste = 0;
            double importeTiempoPartida = 0;
            double totalPartida = 0;
            ArrayList<Modelo> listaDetPartida;
            listaDetPartida = ConsultaBD.queryListDetalle(CAMPOS_DETPARTIDA, idPartida, TABLA_PARTIDA);
            for (Modelo detPartida : listaDetPartida) {

                if (detPartida.getDouble(DETPARTIDA_TIEMPO) > 0) {
                    tiempoPartida += detPartida.getDouble(DETPARTIDA_TIEMPO) * detPartida.getDouble(DETPARTIDA_CANTIDAD);
                } else {
                    double importedet = detPartida.getDouble(DETPARTIDA_PRECIO) * detPartida.getDouble(DETPARTIDA_CANTIDAD);
                    if (detPartida.getString(DETPARTIDA_TIPO).equals(TIPOPRODUCTO)) {
                        importeProductosPartida += importedet + ((importedet / 100) * detPartida.getDouble(DETPARTIDA_BENEFICIO));
                    } else {
                        importeProductosPartida += importedet;
                    }
                    if (detPartida.getString(DETPARTIDA_TIPO).equals(TIPOPRODUCTOPROV)) {
                        coste += importedet - ((importedet / 100) * detPartida.getDouble(DETPARTIDA_DESCUENTOPROVCAT));
                    } else {
                        coste += importedet;
                    }
                }
            }
            coste += (tiempoPartida * calculoCosteHora());
            importeTiempoPartida = tiempoPartida * Interactor.hora;
            totalPartida = importeProductosPartida + importeTiempoPartida;

            Modelo partida = ConsultaBD.queryObject(CAMPOS_PARTIDA, PARTIDA_ID_PARTIDA, idPartida, null,
                    JavaUtil.Constantes.IGUAL, null);

            double cantidadPartida = partida.getDouble(PARTIDA_CANTIDAD);

            ContentValues valores = new ContentValues();
            ConsultaBD.putDato(valores, CAMPOS_PARTIDA, PARTIDA_TIEMPO, tiempoPartida * cantidadPartida);
            ConsultaBD.putDato(valores, CAMPOS_PARTIDA, PARTIDA_PRECIO, totalPartida * cantidadPartida);
            ConsultaBD.putDato(valores, CAMPOS_PARTIDA, PARTIDA_COSTE, coste * cantidadPartida);
            ConsultaBD.putDato(valores, CAMPOS_PARTIDA, PARTIDA_PRECIOHORA, Interactor.hora);

            String idProyecto_Partida = partida.getString(PARTIDA_ID_PROYECTO);
            int secuenciaPartida = partida.getInt(PARTIDA_SECUENCIA);
            int i = ConsultaBD.updateRegistroDetalle(TABLA_PARTIDA, idProyecto_Partida, secuenciaPartida, valores);
            if (i > 0) {
                System.out.println("Partidas actualizadas = " + i);
                return true;
            }

            return false;
        }

        public static void sincronizarPartidasBase() {

            ListaModelo listaPartidasBase = new ListaModelo(CAMPOS_PARTIDABASE);
            if (listaPartidasBase.chechLista()) {
                for (Modelo partidasBase : listaPartidasBase.getLista()) {

                    String id = partidasBase.getString(PARTIDABASE_ID_PARTIDABASE);
                    sincronizarPartidaBase(id);

                }

            }
        }

        public static boolean actualizarPartidaBase(String idPartidabase) {

            double tiempoPartida = 0;
            double importeProductosPartida = 0;
            double coste = 0;
            double importeTiempoPartida = 0;
            double totalPartida = 0;
            ArrayList<Modelo> listaDetPartida;
            listaDetPartida = ConsultaBD.queryListDetalle(CAMPOS_DETPARTIDABASE, idPartidabase, TABLA_PARTIDABASE);
            for (Modelo detPartida : listaDetPartida) {

                if (detPartida.getDouble(DETPARTIDABASE_TIEMPO) > 0) {
                    tiempoPartida += detPartida.getDouble(DETPARTIDABASE_TIEMPO);
                } else {
                    double importedet = detPartida.getDouble(DETPARTIDABASE_PRECIO) * detPartida.getDouble(DETPARTIDABASE_CANTIDAD);
                    if (detPartida.getString(DETPARTIDABASE_TIPO).equals(TIPOPRODUCTO)) {
                        importeProductosPartida += importedet + ((importedet / 100) * detPartida.getDouble(DETPARTIDABASE_BENEFICIO));
                    } else {
                        importeProductosPartida += importedet;
                    }
                    if (detPartida.getString(DETPARTIDABASE_TIPO).equals(TIPOPRODUCTOPROV)) {
                        coste += importedet - ((importedet / 100) * detPartida.getDouble(DETPARTIDABASE_DESCUENTOPROVCAT));
                    } else {
                        coste += importedet;
                    }
                }
            }


            coste += (tiempoPartida * calculoCosteHora());
            importeTiempoPartida = tiempoPartida * Interactor.hora;
            System.out.println("hora = " + Interactor.hora);
            System.out.println("tiempoPartida = " + tiempoPartida);
            totalPartida = importeProductosPartida + importeTiempoPartida;

            ContentValues valores = new ContentValues();
            ConsultaBD.putDato(valores, CAMPOS_PARTIDABASE, PARTIDABASE_TIEMPO, tiempoPartida);
            ConsultaBD.putDato(valores, CAMPOS_PARTIDABASE, PARTIDABASE_PRECIO, totalPartida);
            ConsultaBD.putDato(valores, CAMPOS_PARTIDABASE, PARTIDABASE_COSTE, coste);

            int i = ConsultaBD.updateRegistro(TABLA_PARTIDABASE, idPartidabase, valores);

            return i > 0;

        }

        public static void actualizarPresupuesto(Modelo partida) {

            ArrayList<Modelo> listaPartidas = ConsultaBD.queryListDetalle(CAMPOS_PARTIDA, partida.getString(PARTIDA_ID_PROYECTO), TABLA_PROYECTO);

            double totalTiempo = 0;
            double totalPrecio = 0;
            double totalcoste = 0;
            int totcompletada = 0;

            for (Modelo itemPartida : listaPartidas) {
                actualizarPartidaProyecto(itemPartida.getString(PARTIDA_ID_PARTIDA));
                double cantidad = itemPartida.getDouble(PARTIDA_CANTIDAD);
                totalTiempo += itemPartida.getDouble(PARTIDA_TIEMPO) * cantidad;
                totalPrecio += itemPartida.getDouble(PARTIDA_PRECIO) * cantidad;
                totcompletada += itemPartida.getInt(PARTIDA_COMPLETADA);
                totalcoste += itemPartida.getDouble(PARTIDA_COSTE) * cantidad;
            }

            totcompletada = (int) (Math.round(((double) totcompletada) / listaPartidas.size()));
            ContentValues valores = new ContentValues();
            ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_TIEMPO, totalTiempo);
            ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_IMPORTEPRESUPUESTO, totalPrecio);
            ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_COSTE, totalcoste);
            ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_TOTCOMPLETADO, totcompletada);

            ConsultaBD.updateRegistro(TABLA_PROYECTO, partida.getString(PARTIDA_ID_PROYECTO), valores);

        }

        public static void actualizarPresupuesto(String idProyecto) {

            ArrayList<Modelo> listaPartidas = ConsultaBD.queryListDetalle(CAMPOS_PARTIDA, idProyecto, TABLA_PROYECTO);

            double totalTiempo = 0;
            double totalPrecio = 0;
            double totalcoste = 0;
            int totcompletada = 0;

            for (Modelo itemPartida : listaPartidas) {
                actualizarPartidaProyecto(itemPartida.getString(PARTIDA_ID_PARTIDA));
                double cantidad = itemPartida.getDouble(PARTIDA_CANTIDAD);
                totalTiempo += itemPartida.getDouble(PARTIDA_TIEMPO) * cantidad;
                totalPrecio += itemPartida.getDouble(PARTIDA_PRECIO) * cantidad;
                totcompletada += itemPartida.getInt(PARTIDA_COMPLETADA);
                totalcoste += itemPartida.getDouble(PARTIDA_COSTE) * cantidad;
            }

            totcompletada = (int) (Math.round(((double) totcompletada) / listaPartidas.size()));
            ContentValues valores = new ContentValues();
            ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_TIEMPO, totalTiempo);
            ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_IMPORTEPRESUPUESTO, totalPrecio);
            ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_COSTE, totalcoste);
            ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_TOTCOMPLETADO, totcompletada);

            ConsultaBD.updateRegistro(TABLA_PROYECTO, idProyecto, valores);

        }

        public static void actualizarPresupuesto() {

            ArrayList<Modelo> listaProy = queryList(CAMPOS_PROYECTO);

            for (Modelo proy : listaProy) {
                String idProyecto = proy.getString(PROYECTO_ID_PROYECTO);

                ArrayList<Modelo> listaPartidasact = ConsultaBD.queryListDetalle(CAMPOS_PARTIDA, idProyecto, TABLA_PROYECTO);

                for (Modelo itemPartida : listaPartidasact) {
                    actualizarPartidaProyecto(itemPartida.getString(PARTIDA_ID_PARTIDA));
                }

                ArrayList<Modelo> listaPartidas = ConsultaBD.queryListDetalle(CAMPOS_PARTIDA, idProyecto, TABLA_PROYECTO);

                double totalTiempo = 0;
                double totalPrecio = 0;
                int totcompletada = 0;
                double totalcoste = 0;

                for (Modelo itemPartida : listaPartidas) {
                    actualizarPartidaProyecto(itemPartida.getString(PARTIDA_ID_PARTIDA));
                    double cantidad = itemPartida.getDouble(PARTIDA_CANTIDAD);
                    totalTiempo += itemPartida.getDouble(PARTIDA_TIEMPO) * cantidad;
                    totalPrecio += itemPartida.getDouble(PARTIDA_PRECIO) * cantidad;
                    totcompletada += itemPartida.getInt(PARTIDA_COMPLETADA);
                    totalcoste += itemPartida.getDouble(PARTIDA_COSTE) * cantidad;
                }

                totcompletada = (int) (Math.round(((double) totcompletada) / listaPartidas.size()));
                ContentValues valores = new ContentValues();
                ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_TIEMPO, totalTiempo);
                ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_IMPORTEPRESUPUESTO, totalPrecio);
                ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_COSTE, totalcoste);
                ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_TOTCOMPLETADO, totcompletada);

                int i = ConsultaBD.updateRegistro(TABLA_PROYECTO, idProyecto, valores);
                System.out.println("Proys actualizados = " + i);
            }

        }

        public static double actualizarTarea(String idtarea, boolean automatico) {

            ListaModelo listaDetPartidas = new ListaModelo(CAMPOS_DETPARTIDA, DETPARTIDA_ID_DETPARTIDA, idtarea, null);

            double tiempo = 0;
            double tiemporeal = 0;
            double mediaTiempo = 0;
            int cont = 0;
            Modelo tarea = ConsultaBD.queryObject(CAMPOS_TRABAJO, idtarea);

            for (Modelo detPartida : listaDetPartidas.getLista()) {

                tiemporeal = detPartida.getDouble(DETPARTIDA_TIEMPOREAL);

                if (tiemporeal > 0 && detPartida.getInt(DETPARTIDA_COMPLETA) == 1) {
                    tiempo += tiemporeal;
                    cont++;
                }
            }
            if (cont > 0 && automatico) {

                ContentValues valores = new ContentValues();
                mediaTiempo = tiempo / cont;
                ConsultaBD.putDato(valores, CAMPOS_TRABAJO, TRABAJO_TIEMPO, mediaTiempo);
                ConsultaBD.updateRegistro(TABLA_TRABAJO, idtarea, valores);
                return mediaTiempo;
            } else if (cont > 0) {
                mediaTiempo = tiempo / cont;
                return mediaTiempo;
            }
            if (tarea != null) {
                return tarea.getDouble(TRABAJO_TIEMPO);
            }
            return 0;
        }

        public static class Tareafechas extends AsyncTask<Void, Float, Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {

                recalcularFechas();
                return null;
            }

        }

        public static class TareaActualizarProys extends AsyncTask<Void, Float, Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {

                actualizarPresupuesto();
                return null;
            }

        }

        public static class TareaTipoCliente extends AsyncTask<String, Float, Integer> {

            @Override
            protected Integer doInBackground(String... strings) {

                calcularTipoCliente(strings[0]);
                return null;
            }

        }

        public static class TareaActualizaProy extends AsyncTask<String, Float, Integer> {

            @Override
            protected Integer doInBackground(String... strings) {

                actualizarPresupuesto(strings[0]);
                return null;
            }

        }

        public static class TareaSincronizarPartidasBase extends AsyncTask<Void, Float, Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {

                sincronizarPartidasBase();
                return null;
            }

        }

        public static class TareaActualizarTareaAuto extends AsyncTask<String, Float, Integer> {

            @Override
            protected Integer doInBackground(String... strings) {

                actualizarTarea(strings[0], true);
                return null;
            }

        }

        public static class TareaActualizarTarea extends AsyncTask<String, Float, Integer> {

            @Override
            protected Integer doInBackground(String... strings) {

                actualizarTarea(strings[0], false);
                return null;
            }

        }

    }

}
