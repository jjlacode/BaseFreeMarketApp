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

import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.models.Productos;
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
import com.codevsolution.freemarketsapp.services.EventosReceiver;
import com.codevsolution.freemarketsapp.ui.CalendarioEventos;
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
import static com.codevsolution.base.sqlite.ConsultaBD.putDato;
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
    public static long offsetInicioTrabajos = HORASLONG;


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
        String PRODUCTOLOCAL = "producto_local";
        String TAREA = "tarea";
        String GASTOFIJO = "gastofijo";
        String DETPARTIDABASE = "detpartidabase";
        String DETPARTIDA = "detpartida";
        String PROVCAT = "proveedor_web";
        String USADO = "usado";
        String PRODPROVCAT = "producto_web";
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
                                          String idSorteo, String actual, int id, int iconId, String titulo, String contenido) {

        RemoteViews remoteView = new RemoteViews(contexto.getPackageName(), R.layout.notificacion_chat);
        remoteView.setTextViewText(R.id.tvdescnotchat, contenido);

        Intent intentVerChat = new Intent(contexto, clase);
        intentVerChat.setAction(ACCION_VERSORTEO);
        intentVerChat.putExtra(EXTRA_ACTUAL, actual);
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


    public static long fechaEntregaCalculada(double horastrabajos, String idProyecto) {

        Modelo perfilActivo = ConsultaBD.queryObject(CAMPOS_PERFIL, PERFIL_NOMBRE, perfila, null, IGUAL, null);
        Modelo proyecto = CRUDutil.updateModelo(CAMPOS_PROYECTO,idProyecto);
        long horaHoy = 0;
        long fechahoy = 0;
        TimeZone timezone = TimeZone.getDefault();
        Calendar calendar = new GregorianCalendar(timezone);
        if (idProyecto!=null && proyecto.getLong(PROYECTO_FECHAINICIOACORDADA)>0){
            calendar.setTimeInMillis(proyecto.getLong(PROYECTO_FECHAINICIOACORDADA));
            fechahoy = TimeDateUtil.soloFecha(proyecto.getLong(PROYECTO_FECHAINICIOACORDADA));
            System.out.println("CALCULO FECHA FINAL");
        }else if(idProyecto!=null && proyecto.getLong(PROYECTO_FECHAINICIOCALCULADA)> 0){
            calendar.setTimeInMillis(proyecto.getLong(PROYECTO_FECHAINICIOCALCULADA));
            fechahoy = TimeDateUtil.soloFecha(proyecto.getLong(PROYECTO_FECHAINICIOCALCULADA));
            System.out.println("CALCULO FECHA FINAL");
        }else{
            fechahoy = TimeDateUtil.soloFecha(TimeDateUtil.ahora());
            System.out.println("CALCULO FECHA INICIO");
        }
        if (proyecto!=null) {
            System.out.println("fecha inicio acordada " + TimeDateUtil.getDateString(proyecto.getLong(PROYECTO_FECHAINICIOACORDADA)));
            System.out.println("hora inicio acordada " + TimeDateUtil.getTimeString(proyecto.getLong(PROYECTO_HORAINICIOACORDADA)));
        }
        if (idProyecto!=null && proyecto.getLong(PROYECTO_HORAINICIOACORDADA)>0){
            horaHoy = proyecto.getLong(PROYECTO_HORAINICIOACORDADA);
        }else if(idProyecto!=null && proyecto.getLong(PROYECTO_FECHAINICIOCALCULADA)> 0){
            horaHoy = TimeDateUtil.soloHora(proyecto.getLong(PROYECTO_FECHAINICIOCALCULADA));
        }else{
            horaHoy = JavaUtil.sumaHoraMin(TimeDateUtil.ahora())  + offsetInicioTrabajos;
        }
        int diahoy = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        long totaldias = 0;
        boolean primero =false;
        System.out.println("horas trabajos al entrar = " + horastrabajos);
        if (horastrabajos==0){
            primero = true;

        }
        String IM = null;
        String FM = null;
        String IT = null;
        String FT = null;

        while (horastrabajos > 0 || primero) {

            switch (diahoy) {

                case 0:

                    IM = PERFIL_HORAIMDOMINGO;
                    FM = PERFIL_HORAFMDOMINGO;
                    IT = PERFIL_HORAITDOMINGO;
                    FT = PERFIL_HORAFTDOMINGO;

                    System.out.println("perfilActivo.getLong(IM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IM)));
                    System.out.println("perfilActivo.getLong(FM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FM)));
                    System.out.println("perfilActivo.getLong(IT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IT)));
                    System.out.println("perfilActivo.getLong(FT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FT)));
                    System.out.println("horaHoy antes= " + TimeDateUtil.getTimeString(horaHoy));
                    System.out.println("horastrabajos antes= " + horastrabajos);


                    while (((perfilActivo.getLong(FT) > 0 &&
                            horaHoy < perfilActivo.getLong(FT)) ||
                            (perfilActivo.getLong(FM) > 0 &&
                                    horaHoy < perfilActivo.getLong(FM))) && (horastrabajos > 0 || primero)){

                        if ((horaHoy >= perfilActivo.getLong(IT) &&
                                perfilActivo.getLong(FT) > 0 &&
                                horaHoy < perfilActivo.getLong(FT)) ||
                                (horaHoy >= perfilActivo.getLong(IM) &&
                                        perfilActivo.getLong(FM) > 0 &&
                                        horaHoy < perfilActivo.getLong(FM))){
                            System.out.println("horaHoy%HORASLONG = " + horaHoy%HORASLONG);
                            System.out.println("minutos "+((double)(horaHoy%HORASLONG)/MINUTOSLONG));
                            System.out.println("Fraccion "+(1/(60/((double)(horaHoy%HORASLONG)/MINUTOSLONG))));
                            primero = false;
                            if (horaHoy%HORASLONG>0){
                                horastrabajos-=(1/(60/((double)(HORASLONG-(horaHoy%HORASLONG))/MINUTOSLONG)));
                                horaHoy+=(HORASLONG-(horaHoy%HORASLONG));
                            }else {
                                horastrabajos--;
                                horaHoy += HORASLONG;
                            }

                        } else {
                            horaHoy += HORASLONG;
                        }

                        System.out.println("horaHoy = " + TimeDateUtil.getTimeString(horaHoy));
                        System.out.println("horastrabajos = " + horastrabajos);

                    }

                    if (horastrabajos <= 0) {
                        break;
                    }else{
                        horaHoy = perfilActivo.getLong(PERFIL_HORAIMLUNES);
                        totaldias++;

                    }

                case 1:

                    IM = PERFIL_HORAIMLUNES;
                    FM = PERFIL_HORAFMLUNES;
                    IT = PERFIL_HORAITLUNES;
                    FT = PERFIL_HORAFTLUNES;

                    System.out.println("perfilActivo.getLong(IM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IM)));
                    System.out.println("perfilActivo.getLong(FM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FM)));
                    System.out.println("perfilActivo.getLong(IT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IT)));
                    System.out.println("perfilActivo.getLong(FT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FT)));
                    System.out.println("horaHoy antes= " + TimeDateUtil.getTimeString(horaHoy));
                    System.out.println("horastrabajos antes= " + horastrabajos);


                    while (((perfilActivo.getLong(FT) > 0 &&
                            horaHoy < perfilActivo.getLong(FT)) ||
                            (perfilActivo.getLong(FM) > 0 &&
                                    horaHoy < perfilActivo.getLong(FM))) && (horastrabajos > 0 || primero)){

                        if ((horaHoy >= perfilActivo.getLong(IT) &&
                                perfilActivo.getLong(FT) > 0 &&
                                horaHoy < perfilActivo.getLong(FT)) ||
                                (horaHoy >= perfilActivo.getLong(IM) &&
                                        perfilActivo.getLong(FM) > 0 &&
                                        horaHoy < perfilActivo.getLong(FM))){
                            System.out.println("horaHoy%HORASLONG = " + horaHoy%HORASLONG);
                            System.out.println("minutos "+((double)(horaHoy%HORASLONG)/MINUTOSLONG));
                            System.out.println("Fraccion "+(1/(60/((double)(horaHoy%HORASLONG)/MINUTOSLONG))));
                            primero = false;
                            if (horaHoy%HORASLONG>0){
                                horastrabajos-=(1/(60/((double)(HORASLONG-(horaHoy%HORASLONG))/MINUTOSLONG)));
                                horaHoy+=(HORASLONG-(horaHoy%HORASLONG));
                            }else {
                                horastrabajos--;
                                horaHoy += HORASLONG;
                            }

                        } else {
                            horaHoy += HORASLONG;
                        }

                        System.out.println("horaHoy = " + TimeDateUtil.getTimeString(horaHoy));
                        System.out.println("horastrabajos = " + horastrabajos);

                    }


                    if (horastrabajos <= 0) {
                        break;
                    }else{
                        horaHoy = perfilActivo.getLong(PERFIL_HORAIMMARTES);
                        totaldias++;

                    }


                case 2:


                    IM = PERFIL_HORAIMMARTES;
                    FM = PERFIL_HORAFMMARTES;
                    IT = PERFIL_HORAITMARTES;
                    FT = PERFIL_HORAFTMARTES;

                    System.out.println("perfilActivo.getLong(IM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IM)));
                    System.out.println("perfilActivo.getLong(FM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FM)));
                    System.out.println("perfilActivo.getLong(IT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IT)));
                    System.out.println("perfilActivo.getLong(FT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FT)));
                    System.out.println("horaHoy antes= " + TimeDateUtil.getTimeString(horaHoy));
                    System.out.println("horastrabajos antes= " + horastrabajos);


                    while (((perfilActivo.getLong(FT) > 0 &&
                            horaHoy < perfilActivo.getLong(FT)) ||
                            (perfilActivo.getLong(FM) > 0 &&
                                    horaHoy < perfilActivo.getLong(FM))) && (horastrabajos > 0 || primero)){

                        if ((horaHoy >= perfilActivo.getLong(IT) &&
                                perfilActivo.getLong(FT) > 0 &&
                                horaHoy < perfilActivo.getLong(FT)) ||
                                (horaHoy >= perfilActivo.getLong(IM) &&
                                        perfilActivo.getLong(FM) > 0 &&
                                        horaHoy < perfilActivo.getLong(FM))){
                            System.out.println("horaHoy%HORASLONG = " + horaHoy%HORASLONG);
                            System.out.println("minutos "+((double)(horaHoy%HORASLONG)/MINUTOSLONG));
                            System.out.println("Fraccion "+(1/(60/((double)(horaHoy%HORASLONG)/MINUTOSLONG))));
                            primero = false;
                            if (horaHoy%HORASLONG>0){
                                horastrabajos-=(1/(60/((double)(HORASLONG-(horaHoy%HORASLONG))/MINUTOSLONG)));
                                horaHoy+=(HORASLONG-(horaHoy%HORASLONG));
                            }else {
                                horastrabajos--;
                                horaHoy += HORASLONG;
                            }

                        } else {
                            horaHoy += HORASLONG;
                        }

                        System.out.println("horaHoy = " + TimeDateUtil.getTimeString(horaHoy));
                        System.out.println("horastrabajos = " + horastrabajos);

                    }

                    if (horastrabajos <= 0) {
                        break;
                    }else{
                        horaHoy = perfilActivo.getLong(PERFIL_HORAIMMIERCOLES);
                        totaldias++;

                    }


                case 3:

                    IM = PERFIL_HORAIMMIERCOLES;
                    FM = PERFIL_HORAFMMIERCOLES;
                    IT = PERFIL_HORAITMIERCOLES;
                    FT = PERFIL_HORAFTMIERCOLES;

                    System.out.println("perfilActivo.getLong(IM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IM)));
                    System.out.println("perfilActivo.getLong(FM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FM)));
                    System.out.println("perfilActivo.getLong(IT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IT)));
                    System.out.println("perfilActivo.getLong(FT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FT)));
                    System.out.println("horaHoy antes= " + TimeDateUtil.getTimeString(horaHoy));
                    System.out.println("horastrabajos antes= " + horastrabajos);


                    while (((perfilActivo.getLong(FT) > 0 &&
                            horaHoy < perfilActivo.getLong(FT)) ||
                            (perfilActivo.getLong(FM) > 0 &&
                                    horaHoy < perfilActivo.getLong(FM))) && (horastrabajos > 0 || primero)){

                        if ((horaHoy >= perfilActivo.getLong(IT) &&
                                perfilActivo.getLong(FT) > 0 &&
                                horaHoy < perfilActivo.getLong(FT)) ||
                                (horaHoy >= perfilActivo.getLong(IM) &&
                                        perfilActivo.getLong(FM) > 0 &&
                                        horaHoy < perfilActivo.getLong(FM))){
                            System.out.println("horaHoy%HORASLONG = " + horaHoy%HORASLONG);
                            System.out.println("minutos "+((double)(horaHoy%HORASLONG)/MINUTOSLONG));
                            System.out.println("Fraccion "+(1/(60/((double)(horaHoy%HORASLONG)/MINUTOSLONG))));
                            primero = false;
                            if (horaHoy%HORASLONG>0){
                                horastrabajos-=(1/(60/((double)(HORASLONG-(horaHoy%HORASLONG))/MINUTOSLONG)));
                                horaHoy+=(HORASLONG-(horaHoy%HORASLONG));
                            }else {
                                horastrabajos--;
                                horaHoy += HORASLONG;
                            }

                        } else {
                            horaHoy += HORASLONG;
                        }

                        System.out.println("horaHoy = " + TimeDateUtil.getTimeString(horaHoy));
                        System.out.println("horastrabajos = " + horastrabajos);

                    }


                    if (horastrabajos <= 0) {
                        break;
                    }else{
                        horaHoy = perfilActivo.getLong(PERFIL_HORAIMJUEVES);
                        totaldias++;

                    }

                case 4:

                    IM = PERFIL_HORAIMJUEVES;
                    FM = PERFIL_HORAFMJUEVES;
                    IT = PERFIL_HORAITJUEVES;
                    FT = PERFIL_HORAFTJUEVES;

                    System.out.println("perfilActivo.getLong(IM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IM)));
                    System.out.println("perfilActivo.getLong(FM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FM)));
                    System.out.println("perfilActivo.getLong(IT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IT)));
                    System.out.println("perfilActivo.getLong(FT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FT)));
                    System.out.println("horaHoy antes= " + TimeDateUtil.getTimeString(horaHoy));
                    System.out.println("horastrabajos antes= " + horastrabajos);


                    while (((perfilActivo.getLong(FT) > 0 &&
                            horaHoy < perfilActivo.getLong(FT)) ||
                            (perfilActivo.getLong(FM) > 0 &&
                                    horaHoy < perfilActivo.getLong(FM))) && (horastrabajos > 0 || primero)){

                        if ((horaHoy >= perfilActivo.getLong(IT) &&
                                perfilActivo.getLong(FT) > 0 &&
                                horaHoy < perfilActivo.getLong(FT)) ||
                                (horaHoy >= perfilActivo.getLong(IM) &&
                                        perfilActivo.getLong(FM) > 0 &&
                                        horaHoy < perfilActivo.getLong(FM))){
                            System.out.println("horaHoy%HORASLONG = " + horaHoy%HORASLONG);
                            System.out.println("minutos "+((double)(horaHoy%HORASLONG)/MINUTOSLONG));
                            System.out.println("Fraccion "+(1/(60/((double)(horaHoy%HORASLONG)/MINUTOSLONG))));
                            primero = false;
                            if (horaHoy%HORASLONG>0){
                                horastrabajos-=(1/(60/((double)(HORASLONG-(horaHoy%HORASLONG))/MINUTOSLONG)));
                                horaHoy+=(HORASLONG-(horaHoy%HORASLONG));
                            }else {
                                horastrabajos--;
                                horaHoy += HORASLONG;
                            }

                        } else {
                            horaHoy += HORASLONG;
                        }

                        System.out.println("horaHoy = " + TimeDateUtil.getTimeString(horaHoy));
                        System.out.println("horastrabajos = " + horastrabajos);

                    }


                    if (horastrabajos <= 0) {
                        break;
                    }else{
                        horaHoy = perfilActivo.getLong(PERFIL_HORAIMVIERNES);
                        totaldias++;

                    }

                case 5:

                    IM = PERFIL_HORAIMVIERNES;
                    FM = PERFIL_HORAFMVIERNES;
                    IT = PERFIL_HORAITVIERNES;
                    FT = PERFIL_HORAFTVIERNES;

                    System.out.println("perfilActivo.getLong(IM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IM)));
                    System.out.println("perfilActivo.getLong(FM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FM)));
                    System.out.println("perfilActivo.getLong(IT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IT)));
                    System.out.println("perfilActivo.getLong(FT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FT)));
                    System.out.println("horaHoy antes= " + TimeDateUtil.getTimeString(horaHoy));
                    System.out.println("horastrabajos antes= " + horastrabajos);


                    while (((perfilActivo.getLong(FT) > 0 &&
                            horaHoy < perfilActivo.getLong(FT)) ||
                            (perfilActivo.getLong(FM) > 0 &&
                                    horaHoy < perfilActivo.getLong(FM))) && (horastrabajos > 0 || primero)){

                        if ((horaHoy >= perfilActivo.getLong(IT) &&
                                perfilActivo.getLong(FT) > 0 &&
                                horaHoy < perfilActivo.getLong(FT)) ||
                                (horaHoy >= perfilActivo.getLong(IM) &&
                                        perfilActivo.getLong(FM) > 0 &&
                                        horaHoy < perfilActivo.getLong(FM))){
                            System.out.println("horaHoy%HORASLONG = " + horaHoy%HORASLONG);
                            System.out.println("minutos "+((double)(horaHoy%HORASLONG)/MINUTOSLONG));
                            System.out.println("Fraccion "+(1/(60/((double)(horaHoy%HORASLONG)/MINUTOSLONG))));
                            primero = false;
                            if (horaHoy%HORASLONG>0){
                                horastrabajos-=(1/(60/((double)(HORASLONG-(horaHoy%HORASLONG))/MINUTOSLONG)));
                                horaHoy+=(HORASLONG-(horaHoy%HORASLONG));
                            }else {
                                horastrabajos--;
                                horaHoy += HORASLONG;
                            }

                        } else {
                            horaHoy += HORASLONG;
                        }

                        System.out.println("horaHoy = " + TimeDateUtil.getTimeString(horaHoy));
                        System.out.println("horastrabajos = " + horastrabajos);

                    }

                    if (horastrabajos <= 0) {
                        break;
                    }else{
                        horaHoy = perfilActivo.getLong(PERFIL_HORAIMSABADO);
                        totaldias++;

                    }


                case 6:

                    IM = PERFIL_HORAIMSABADO;
                    FM = PERFIL_HORAFMSABADO;
                    IT = PERFIL_HORAITSABADO;
                    FT = PERFIL_HORAFTSABADO;

                    System.out.println("perfilActivo.getLong(IM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IM)));
                    System.out.println("perfilActivo.getLong(FM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FM)));
                    System.out.println("perfilActivo.getLong(IT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IT)));
                    System.out.println("perfilActivo.getLong(FT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FT)));
                    System.out.println("horaHoy antes= " + TimeDateUtil.getTimeString(horaHoy));
                    System.out.println("horastrabajos antes= " + horastrabajos);


                    while (((perfilActivo.getLong(FT) > 0 &&
                            horaHoy < perfilActivo.getLong(FT)) ||
                            (perfilActivo.getLong(FM) > 0 &&
                                    horaHoy < perfilActivo.getLong(FM))) && (horastrabajos > 0 || primero)){

                        if ((horaHoy >= perfilActivo.getLong(IT) &&
                                perfilActivo.getLong(FT) > 0 &&
                                horaHoy < perfilActivo.getLong(FT)) ||
                                (horaHoy >= perfilActivo.getLong(IM) &&
                                        perfilActivo.getLong(FM) > 0 &&
                                        horaHoy < perfilActivo.getLong(FM))){
                            System.out.println("horaHoy%HORASLONG = " + horaHoy%HORASLONG);
                            System.out.println("minutos "+((double)(horaHoy%HORASLONG)/MINUTOSLONG));
                            System.out.println("Fraccion "+(1/(60/((double)(horaHoy%HORASLONG)/MINUTOSLONG))));
                            primero = false;
                            if (horaHoy%HORASLONG>0){
                                horastrabajos-=(1/(60/((double)(HORASLONG-(horaHoy%HORASLONG))/MINUTOSLONG)));
                                horaHoy+=(HORASLONG-(horaHoy%HORASLONG));
                            }else {
                                horastrabajos--;
                                horaHoy += HORASLONG;
                            }

                        } else {
                            horaHoy += HORASLONG;
                        }

                        System.out.println("horaHoy = " + TimeDateUtil.getTimeString(horaHoy));
                        System.out.println("horastrabajos = " + horastrabajos);

                    }
                    if (horastrabajos <= 0) {
                        break;
                    }else{
                        horaHoy = perfilActivo.getLong(PERFIL_HORAIMDOMINGO);
                        totaldias++;

                    }

            }
            diahoy = 0;
            primero = false;
        }
        fechahoy +=horaHoy;
        System.out.println("fechahoy = " + fechahoy);
        System.out.println("totaldias = " + totaldias);
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

        public static void recalcularAgenda(long inicio, long fin, int anios){

            String ordenAgenda = AGENDA_VALORENTRADA + Constantes.ORDENASCENDENTE;

            if (anios == 0){anios=1;}
            if (inicio==0){
                inicio = (TimeDateUtil.ahora()-(ANIOSLONG * anios));
            }
            if (fin==0){

                fin = (TimeDateUtil.ahora()+(ANIOSLONG * anios));
            }
            ListaModelo listaMinutos = CRUDutil.setListaModelo(CAMPOS_AGENDA, AGENDA_VALORENTRADA,
                    String.valueOf(inicio),String.valueOf(fin),ENTRE,ordenAgenda);

            for (int i = 0; i < listaMinutos.getLista().size(); i++) {

                ContentValues values = new ContentValues();
                Modelo segmento = listaMinutos.getItem(i);
                Modelo segmentoAnt = CRUDutil.updateModelo(CAMPOS_AGENDA, segmento.getString(AGENDA_ID_MINANT));
                if (i > 0 && segmentoAnt == null) {
                    segmentoAnt = listaMinutos.getItem(i - 1);
                }
                Modelo partida = CRUDutil.updateModelo(CAMPOS_PARTIDA, segmento.getString(AGENDA_ID_PARTIDA)
                        , segmento.getString(AGENDA_SECUENCIA_PARTIDA));
                Modelo detPartida = CRUDutil.updateModelo(CAMPOS_DETPARTIDA, segmento.getString(AGENDA_ID_DETPARTIDA)
                        , segmento.getString(AGENDA_SECUENCIA_DETPARTIDA));
                boolean activo = false;
                if (detPartida != null && partida != null && partida.getInt(PARTIDA_TIPO_ESTADO) > TiposEstados.TPRESUPESPERA &&
                        partida.getInt(PARTIDA_TIPO_ESTADO) < TiposEstados.TPROYECPENDENTREGA) {
                    activo = true;
                }

                long valorEntrada = segmento.getLong(AGENDA_VALORENTRADA);
                long valorSalida = segmento.getLong(AGENDA_VALORSALIDA);


                if (segmentoAnt != null) {
                    long valorAnterior = segmentoAnt.getLong(AGENDA_VALORSALIDA);

                    values.put(AGENDA_VALORANT, valorAnterior);
                    values.put(AGENDA_ID_MINANT, segmentoAnt.getString(AGENDA_ID_AGENDA));
                    values.put(AGENDA_ESPACIOANT, calculoEspacioEntreMinutos(valorAnterior, valorEntrada));

                }
                if (i<listaMinutos.getLista().size()-1){
                    Modelo segmentoSig = listaMinutos.getItem(i + 1);
                    long valorSiguiente = segmentoSig.getLong(AGENDA_VALORENTRADA);

                    values.put(AGENDA_VALORSIG,valorSiguiente);
                    values.put(AGENDA_ID_MINSIG, segmentoSig.getString(AGENDA_ID_AGENDA));
                    values.put(AGENDA_ESPACIOSIG,calculoEspacioEntreMinutos
                            (valorSalida, valorSiguiente));
                }

                if (activo) {
                    values.put(AGENDA_ACTIVO, 1);
                }else{
                    values.put(AGENDA_ACTIVO, 0);
                }
                CRUDutil.actualizarRegistro(segmento, values);

            }
        }

        public static void recalcularFechas(boolean guardar) {

            long inicio = 0;
            long fin = 0;
            int anios = 0;
            recalcularAgenda(inicio,fin,anios);
            long fechaFin = 0;
            String ordenProyectosNuevos = PROYECTO_FECHAENTRADA + Constantes.ORDENASCENDENTE;

            ListaModelo listaProyectosnuevos = CRUDutil.setListaModelo(CAMPOS_PROYECTO, PROYECTO_FECHAINICIOACORDADA, "0", IGUAL,ordenProyectosNuevos);


            for (int i = 0; i < listaProyectosnuevos.getLista().size(); i++) {

                Modelo proyectoNuevo = listaProyectosnuevos.getLista().get(i);
                String ordenPartidas = PARTIDA_ORDEN + Constantes.ORDENASCENDENTE;
                ListaModelo listaPartidas = CRUDutil.setListaModelo(CAMPOS_PARTIDA,PARTIDA_ID_PROYECTO,proyectoNuevo.getString(PROYECTO_ID_PROYECTO),IGUAL,ordenPartidas);

                int ultimaPartida = 0;
                for (Modelo partida : listaPartidas.getLista()) {
                    if (partida.getInt(PARTIDA_ORDEN)>ultimaPartida){
                        ultimaPartida = partida.getInt(PARTIDA_ORDEN);
                    }
                }
                for (int x = 0; x < listaPartidas.sizeLista(); x++) {
                    Modelo partida = listaPartidas.getLista().get(x);
                    String ordenDetPartidas = DETPARTIDA_ORDEN + Constantes.ORDENASCENDENTE;
                    ListaModelo listaDetPartidas = CRUDutil.setListaModelo(CAMPOS_DETPARTIDA, DETPARTIDA_ID_PARTIDA, partida.getString(PARTIDA_ID_PARTIDA), IGUAL, ordenDetPartidas);
                    int ultimaDetPartida = 0;
                    for (Modelo detPartida : listaDetPartidas.getLista()) {
                        if (detPartida.getInt(DETPARTIDA_ORDEN) > ultimaDetPartida) {
                            ultimaDetPartida = detPartida.getInt(DETPARTIDA_ORDEN);
                        }
                    }
                    for (int y = 0; y < listaDetPartidas.sizeLista(); y++) {

                        Modelo detPartida = listaDetPartidas.getLista().get(y);

                        if (detPartida.getInt(DETPARTIDA_ORDEN)>0){

                            if (partida.getInt(PARTIDA_ORDEN) == 1) {
                                if (detPartida.getInt(DETPARTIDA_ORDEN) == 1) {
                                    inicio = TimeDateUtil.soloFecha(detPartida.getLong(DETPARTIDA_FECHAINICIOCALCULADA)) +
                                            TimeDateUtil.soloHora(detPartida.getLong(DETPARTIDA_HORAINICIOCALCULADA));
                                }

                            } else if (partida.getInt(PARTIDA_ORDEN) > 1) {

                                if (detPartida.getInt(DETPARTIDA_ORDEN) == 1) {
                                    Modelo partidaAnt = null;
                                    for (Modelo partidatemp : listaPartidas.getLista()) {
                                        if (partidatemp.getInt(PARTIDA_ORDEN) == partida.getInt(PARTIDA_ORDEN) - 1) {
                                            partidaAnt = partidatemp.clonar(false);
                                            break;
                                        }
                                    }
                                    if (partidaAnt != null) {
                                        inicio = partidaAnt.getLong(PARTIDA_FECHAENTREGACALCULADA);
                                    }
                                }
                            }

                            if (inicio == 0 && detPartida.getInt(DETPARTIDA_ORDEN) > 1) {

                                Modelo detPartidaAnt = null;
                                for (Modelo detPartidatemp : listaDetPartidas.getLista()) {
                                    if (detPartidatemp.getInt(DETPARTIDA_ORDEN) == detPartida.getInt(DETPARTIDA_ORDEN) - 1) {
                                        detPartidaAnt = detPartidatemp.clonar(false);
                                    }
                                }
                                if (detPartidaAnt != null) {
                                    inicio = detPartidaAnt.getLong(DETPARTIDA_FECHAENTREGACALCULADA);
                                }
                            }

                            fechaFin = calculoMinutoFinDetPartida(inicio, detPartida, guardar);

                            ContentValues valores = new ContentValues();
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDA, DETPARTIDA_FECHAINICIOCALCULADA, inicio);
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDA, DETPARTIDA_FECHAINICIOCALCULADAF, TimeDateUtil.getDateString(inicio));
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDA, DETPARTIDA_HORAINICIOCALCULADA, inicio);
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDA, DETPARTIDA_HORAINICIOCALCULADAF, TimeDateUtil.getTimeString(inicio));
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDA, DETPARTIDA_FECHAENTREGACALCULADA, fechaFin);
                            ConsultaBD.putDato(valores, CAMPOS_DETPARTIDA, DETPARTIDA_FECHAENTREGACALCULADAF, TimeDateUtil.getDateTimeString(fechaFin));
                            ConsultaBD.updateRegistroDetalle(TABLA_DETPARTIDA, detPartida.getString(DETPARTIDA_ID_PARTIDA), detPartida.getInt(DETPARTIDA_SECUENCIA), valores);
                            System.out.println("valores detpartida= " + valores);
                            if (partida.getInt(PARTIDA_ORDEN) == 1) {
                                valores = new ContentValues();
                                ConsultaBD.putDato(valores, CAMPOS_PARTIDA, PARTIDA_FECHAINICIOCALCULADA, inicio);
                                ConsultaBD.putDato(valores, CAMPOS_PARTIDA, PARTIDA_FECHAINICIOCALCULADAF, TimeDateUtil.getDateString(inicio));
                                ConsultaBD.putDato(valores, CAMPOS_PARTIDA, PARTIDA_HORAINICIOCALCULADA, inicio);
                                ConsultaBD.putDato(valores, CAMPOS_PARTIDA, PARTIDA_HORAINICIOCALCULADAF, TimeDateUtil.getTimeString(inicio));
                                ConsultaBD.updateRegistroDetalle(TABLA_PARTIDA, partida.getString(PARTIDA_ID_PROYECTO), partida.getInt(PARTIDA_SECUENCIA), valores);
                                System.out.println("valores inicio partida= " + valores);

                                if (detPartida.getInt(DETPARTIDA_ORDEN) == 1) {
                                    valores = new ContentValues();
                                    ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_FECHAINICIOCALCULADA, inicio);
                                    ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_FECHAINICIOCALCULADAF, TimeDateUtil.getDateString(inicio));
                                    ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_HORAINICIOCALCULADA, inicio);
                                    ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_HORAINICIOCALCULADAF, TimeDateUtil.getTimeString(inicio));
                                    ConsultaBD.updateRegistro(TABLA_PROYECTO, proyectoNuevo.getString(PROYECTO_ID_PROYECTO), valores);
                                    System.out.println("valores inicio proyecto= " + valores);

                                }
                            }
                            if (partida.getInt(PARTIDA_ORDEN) == ultimaPartida) {
                                valores = new ContentValues();
                                ConsultaBD.putDato(valores, CAMPOS_PARTIDA, PARTIDA_FECHAENTREGACALCULADA, fechaFin);
                                ConsultaBD.putDato(valores, CAMPOS_PARTIDA, PARTIDA_FECHAENTREGACALCULADAF, TimeDateUtil.getDateTimeString(fechaFin));
                                ConsultaBD.updateRegistroDetalle(TABLA_PARTIDA, partida.getString(PARTIDA_ID_PROYECTO), partida.getInt(PARTIDA_SECUENCIA), valores);
                                System.out.println("valores fin partida= " + valores);
                                if (detPartida.getInt(DETPARTIDA_ORDEN) == ultimaDetPartida) {
                                    valores = new ContentValues();
                                    ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_FECHAENTREGACALCULADA, fechaFin);
                                    ConsultaBD.putDato(valores, CAMPOS_PROYECTO, PROYECTO_FECHAENTREGACALCULADAF, TimeDateUtil.getDateTimeString(fechaFin));
                                    ConsultaBD.updateRegistro(TABLA_PROYECTO, proyectoNuevo.getString(PROYECTO_ID_PROYECTO), valores);
                                    System.out.println("valores fin proyecto= " + valores);

                                }
                            }

                        }
                    }
                }
            }

        }


        private static long calculoMinutoFinDetPartida(long inicio, Modelo detPartida, boolean guardar) {

            Modelo perfilActivo = ConsultaBD.queryObject(CAMPOS_PERFIL, PERFIL_NOMBRE, perfila, null, IGUAL, null);
            long horaHoy = 0;
            long fechahoy = 0;
            long fechaIni = 0;
            long fin = TimeDateUtil.ahora() + ANIOSLONG;
            Modelo segmento = null;
            Modelo segmentoAnt = null;
            Modelo segmentoSig = null;

            if (inicio == 0) {
                inicio = TimeDateUtil.ahora();
            }

            String ordenAgenda = AGENDA_VALORENTRADA + Constantes.ORDENASCENDENTE;
            ListaModelo listaMinutos = CRUDutil.setListaModelo(CAMPOS_AGENDA, AGENDA_VALORENTRADA,
                    String.valueOf(inicio), String.valueOf(fin), ENTRE, ordenAgenda);

            System.out.println("listaMinutos = " + listaMinutos.sizeLista());

            boolean split = false;
            if (detPartida.getInt(DETPARTIDA_SPLIT) == 1) {
                split = true;
            }

            if (listaMinutos.sizeLista() > 0) {
                for (int m = 0; m < listaMinutos.getLista().size(); m++) {

                    segmento = listaMinutos.getLista().get(m);
                    segmentoAnt = CRUDutil.updateModelo(CAMPOS_AGENDA, segmento.getString(AGENDA_ID_MINANT));
                    segmentoSig = CRUDutil.updateModelo(CAMPOS_AGENDA, segmento.getString(AGENDA_ID_MINSIG));

                    double espacioTotal = 0;
                    for (Modelo segmentoTmp : listaMinutos.getLista()) {
                        if (espacioTotal > 0 || segmentoTmp.getInt(AGENDA_ACTIVO) == 0) {
                            if (segmentoTmp.getInt(AGENDA_ACTIVO) == 0) {
                                espacioTotal += segmentoTmp.getDouble(AGENDA_ESPACIO);
                            } else {
                                break;
                            }
                        }
                    }

                    if (segmento.getInt(AGENDA_ACTIVO) == 1 && segmentoAnt == null &&
                            (split || calculoEspacioEntreMinutos(segmento.getLong(AGENDA_VALORENTRADA), inicio) + espacioTotal
                                    > detPartida.getDouble(DETPARTIDA_TIEMPO))) {

                        fechaIni = inicio;

                        break;

                    } else if (segmento.getInt(AGENDA_ACTIVO) == 1 && segmentoAnt != null && segmentoAnt.getInt(AGENDA_ACTIVO) == 0 &&
                            (split || calculoEspacioEntreMinutos(segmento.getLong(AGENDA_VALORENTRADA), inicio) + espacioTotal
                                    > detPartida.getDouble(DETPARTIDA_TIEMPO))) {

                        fechaIni = inicio;
                        break;

                    } else if (segmento.getInt(AGENDA_ACTIVO) == 0) {


                        if (segmentoAnt != null && segmentoAnt.getInt(AGENDA_ACTIVO) == 1 &&
                                (split || espacioTotal > detPartida.getDouble(DETPARTIDA_TIEMPO))) {

                            fechaIni = segmento.getLong(AGENDA_VALORENTRADA);
                            break;

                        } else if (segmentoAnt != null && segmentoAnt.getInt(AGENDA_ACTIVO) == 0 &&
                                (split || calculoEspacioEntreMinutos(segmentoAnt.getLong(AGENDA_VALORSALIDA), inicio) +
                                        espacioTotal > detPartida.getDouble(DETPARTIDA_TIEMPO))) {

                            fechaIni = inicio;
                            break;

                        }
                    }
                }

            } else {

                fechaIni = inicio;

            }
            System.out.println("fechaIni = " + TimeDateUtil.getDateTimeString(fechaIni));
            ContentValues values = new ContentValues();
            Modelo partida = CRUDutil.updateModelo(CAMPOS_PARTIDA, PARTIDA_ID_PARTIDA,
                    detPartida.getString(DETPARTIDA_ID_PARTIDA),null,IGUAL,null);
            int secuenciaPartida = partida.getInt(PARTIDA_SECUENCIA);
            String idPartida = partida.getString(PARTIDA_ID_PROYECTO);
            String idDetPartida = detPartida.getString(DETPARTIDA_ID_PARTIDA);
            int secuenciaDetPartida = detPartida.getInt(DETPARTIDA_SECUENCIA);
            if (guardar) {

                values.put(AGENDA_VALORENTRADA, inicio);
                values.put(AGENDA_INICIO, 1);
                    values.put(AGENDA_ID_PARTIDA, idPartida);
                    values.put(AGENDA_ID_DETPARTIDA, idDetPartida);
                    values.put(AGENDA_SECUENCIA_PARTIDA, secuenciaPartida);
                    values.put(AGENDA_SECUENCIA_DETPARTIDA, secuenciaDetPartida);

            }
            double cantPartida = partida.getDouble(PARTIDA_CANTIDAD);
            double minutosTrabajos = detPartida.getDouble(DETPARTIDA_TIEMPO) *
                    detPartida.getDouble(DETPARTIDA_CANTIDAD) * 60 * cantPartida;
            System.out.println("minutosTrabajos entrada= " + minutosTrabajos);
            TimeZone timezone = TimeZone.getDefault();
            Calendar calendar = new GregorianCalendar(timezone);
            if (fechaIni>0){
                calendar.setTimeInMillis(fechaIni);
                fechahoy = TimeDateUtil.soloFecha(fechaIni);
                horaHoy = TimeDateUtil.soloHora(fechaIni);
            }else{
                fechahoy = TimeDateUtil.soloFecha(TimeDateUtil.ahora()) +
                        ((int) (detPartida.getLong(DETPARTIDA_OFFSET) / DIASLONG)) * DIASLONG;
                horaHoy = TimeDateUtil.soloHora(TimeDateUtil.ahora()) + (detPartida.getLong(DETPARTIDA_OFFSET)
                        - (((int)(detPartida.getLong(DETPARTIDA_OFFSET)/DIASLONG))*DIASLONG));
            }

            long fechaTemp = fechaIni;
            int diahoy = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            long totaldias = 0;

            long IM = 0;
            long FM = 0;
            long IT = 0;
            long FT = 0;
            long IMP = 0;

            while (minutosTrabajos > 0 ) {

                for (int dia = diahoy;dia<=6;dia++){

                    switch (dia) {


                        case 0:

                            IM = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAIMDOMINGO));
                            FM = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAFMDOMINGO));
                            IT = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAITDOMINGO));
                            FT = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAFTDOMINGO));
                            IMP = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAIMLUNES));
                            break;

                        case 1:

                            IM = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAIMLUNES));
                            FM = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAFMLUNES));
                            IT = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAITLUNES));
                            FT = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAFTLUNES));
                            IMP = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAIMMARTES));

                            break;

                        case 2:

                            IM = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAIMMARTES));
                            FM = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAFMMARTES));
                            IT = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAITMARTES));
                            FT = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAFTMARTES));
                            IMP = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAIMMIERCOLES));

                            break;

                        case 3:

                            IM = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAIMMIERCOLES));
                            FM = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAFMMIERCOLES));
                            IT = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAITMIERCOLES));
                            FT = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAFTMIERCOLES));
                            IMP = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAIMJUEVES));

                            break;

                        case 4:

                            IM = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAIMJUEVES));
                            FM = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAFMJUEVES));
                            IT = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAITJUEVES));
                            FT = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAFTJUEVES));
                            IMP = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAIMVIERNES));

                            break;

                        case 5:

                            IM = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAIMVIERNES));
                            FM = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAFMVIERNES));
                            IT = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAITVIERNES));
                            FT = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAFTVIERNES));
                            IMP = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAIMSABADO));

                            break;

                        case 6:

                            IM = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAIMSABADO));
                            FM = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAFMSABADO));
                            IT = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAITSABADO));
                            FT = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAFTSABADO));
                            IMP = TimeDateUtil.soloHora(perfilActivo.getLong(PERFIL_HORAIMDOMINGO));

                    }

                    if (IM >= 0 && horaHoy < IM) {
                        horaHoy = IM;
                    }
                    if (IT >= 0 && horaHoy > FM && horaHoy < IT) {
                        horaHoy = IT;
                    }

                    //Si la fecha está entre el inicio del horario y el final y quedan minutos de trabajo por restar
                    while (((IT >= 0 && horaHoy >= FM && horaHoy < IT) || (IT >= 0 && horaHoy >= IT && FT >= 0 && horaHoy < FT) ||
                            (IM >= 0 && horaHoy >= IM && FM >= 0 && horaHoy < FM)) && (minutosTrabajos > 0)){

                        //Si la fecha está en horario de tarde
                        if (horaHoy >= IT && FT >= 0 && horaHoy < FT && (segmento == null || (fechaTemp >= segmento.getLong(AGENDA_VALORENTRADA) &&
                                (segmento.getInt(AGENDA_ACTIVO) == 0 && fechaTemp < segmento.getLong(AGENDA_VALORSALIDA))))) {

                            if (minutosTrabajos >= (double) (FT-horaHoy)/MINUTOSLONG) {
                                minutosTrabajos -= (double) (FT - horaHoy) / MINUTOSLONG;
                                    horaHoy = FT;
                            }else{
                                horaHoy += minutosTrabajos* MINUTOSLONG;
                                minutosTrabajos = 0;

                            }

                            //Si la fecha está en horario de mañana
                        } else if (((horaHoy >= IM && FM >= 0 && horaHoy < FM)) && (segmento == null || (fechaTemp >= segmento.getLong(AGENDA_VALORENTRADA) &&
                                (segmento.getInt(AGENDA_ACTIVO) == 0 && fechaTemp < segmento.getLong(AGENDA_VALORSALIDA))))) {

                            if (minutosTrabajos >= (double) (FM-horaHoy)/MINUTOSLONG) {
                                minutosTrabajos -= (double) (FM - horaHoy) / MINUTOSLONG;
                                if (IT>=0) {
                                    horaHoy = IT;
                                }else{
                                        horaHoy = FM;
                                }
                            }else{
                                horaHoy += minutosTrabajos* MINUTOSLONG;
                                minutosTrabajos = 0;
                            }


                        } else {

                            if (IT>=0 && minutosTrabajos>0) {

                                horaHoy = IT;
                            }

                        }

                        if (split && segmento != null && segmento.getLong(AGENDA_VALORSALIDA) > 0 && fechaTemp >= segmento.getLong(AGENDA_VALORSALIDA)) {

                            if (guardar) {

                                putDato(values, CAMPOS_AGENDA, AGENDA_VALORSALIDA, fechaTemp);
                                putDato(values, CAMPOS_AGENDA, AGENDA_FIN, 0);

                                if (segmentoAnt != null) {

                                    putDato(values, CAMPOS_AGENDA, AGENDA_VALORANT, segmentoAnt.getLong(AGENDA_VALORENTRADA));
                                    putDato(values, CAMPOS_AGENDA, AGENDA_ID_MINANT, segmentoAnt.getString(AGENDA_ID_AGENDA));

                                }

                                CRUDutil.crearRegistro(TABLA_AGENDA, values);

                                Uri uri = CRUDutil.crearRegistro(TABLA_AGENDA, values);
                                segmentoAnt = ConsultaBD.queryObject(CAMPOS_AGENDA, uri);
                            }

                            segmento = CRUDutil.updateModelo(CAMPOS_AGENDA, segmento.getString(AGENDA_ID_MINSIG));
                            while (segmento != null && segmento.getInt(AGENDA_ACTIVO) == 1 && segmento.getString(AGENDA_ID_MINSIG) != null) {
                                segmento = CRUDutil.updateModelo(CAMPOS_AGENDA, segmento.getString(AGENDA_ID_MINSIG));
                            }

                            if (guardar) {
                                values = new ContentValues();
                                putDato(values, CAMPOS_AGENDA, AGENDA_VALORENTRADA, segmentoAnt.getLong(AGENDA_VALORSALIDA));
                                putDato(values, CAMPOS_AGENDA, AGENDA_INICIO, 0);
                                values.put(AGENDA_ID_PARTIDA, idPartida);
                                values.put(AGENDA_ID_DETPARTIDA, idDetPartida);
                                values.put(AGENDA_SECUENCIA_PARTIDA, secuenciaPartida);
                                values.put(AGENDA_SECUENCIA_DETPARTIDA, secuenciaDetPartida);
                                putDato(values, CAMPOS_AGENDA, AGENDA_ID_MINANT, segmentoAnt.getString(AGENDA_ID_AGENDA));


                            }

                        }

                        fechaTemp = TimeDateUtil.soloFecha(fechaTemp) + horaHoy;

                    }

                    if (minutosTrabajos <= 0) {

                        if (guardar){

                            putDato(values, CAMPOS_AGENDA, AGENDA_VALORSALIDA, fechaTemp);
                            putDato(values,CAMPOS_AGENDA,AGENDA_FIN,1);

                            if (segmentoAnt != null) {

                                putDato(values, CAMPOS_AGENDA, AGENDA_VALORANT, segmentoAnt.getLong(AGENDA_VALORENTRADA));
                                putDato(values, CAMPOS_AGENDA, AGENDA_ID_MINANT, segmentoAnt.getString(AGENDA_ID_AGENDA));

                            }

                            CRUDutil.crearRegistro(TABLA_AGENDA, values);
                        }
                        break;
                    }else{

                        totaldias++;
                        fechaTemp += DIASLONG;
                        if (split && fechahoy+(totaldias * DIASLONG )+((horaHoy-TimeDateUtil.
                                soloHora(TimeDateUtil.ahora())) / MINUTOSLONG) > segmento.getLong(AGENDA_VALORSIG)) {

                        }
                        if (IMP>=0) {
                            horaHoy = IMP;
                        }else {
                            horaHoy = 0;
                        }

                    }

                }
                diahoy = 0;
            }

            return fechahoy + (totaldias * DIASLONG) + TimeDateUtil.soloHora(horaHoy);//((horaHoy-TimeDateUtil.soloHora(TimeDateUtil.ahora()))/MINUTOSLONG);

        }

        private static long calculoFinDetPartida(Modelo minutoini, Modelo detPartida, boolean guardar) {

            double horastrabajos = detPartida.getDouble(DETPARTIDA_TIEMPO);
            long min = 0;
            double horasAnt = 0;
            Modelo partida = null;
            long bloqueMin = 1;

            System.out.println("minutoini = " + minutoini);
            if (minutoini == null) {
                guardar = false;
                ContentValues values = new ContentValues();
                values.put(AGENDA_VALORENTRADA, TimeDateUtil.ahora());
                values.put(AGENDA_ESPACIOSIG, horastrabajos);
                Uri uri = CRUDutil.crearRegistro(TABLA_AGENDA, values);
                System.out.println("Uri 1er registro tabla AGENDA = " + uri);
                minutoini = ConsultaBD.queryObject(CAMPOS_AGENDA, uri);
            }

            if (guardar) {
                partida = CRUDutil.updateModelo(CAMPOS_PARTIDA, PARTIDA_ID_PARTIDA,
                        detPartida.getString(DETPARTIDA_ID_PARTIDA), null, IGUAL, null);
                if (minutoini.getLong(AGENDA_VALORSIG) > minutoini.getLong(AGENDA_VALORENTRADA) + bloqueMin) {
                    ContentValues values = new ContentValues();
                    values.put(AGENDA_VALORENTRADA, minutoini.getLong(AGENDA_VALORENTRADA) + bloqueMin);
                    values.put(AGENDA_ID_PARTIDA, partida.getString(PARTIDA_ID_PROYECTO));
                    values.put(AGENDA_ID_DETPARTIDA, partida.getString(PARTIDA_ID_PARTIDA));
                    values.put(AGENDA_SECUENCIA_PARTIDA, partida.getInt(PARTIDA_SECUENCIA));
                    values.put(AGENDA_SECUENCIA_DETPARTIDA, detPartida.getInt(DETPARTIDA_SECUENCIA));

                    CRUDutil.crearRegistro(TABLA_AGENDA, values);
                }
            }

            Modelo minutoSig = minutoini.clonar(false);

            while (horastrabajos > 0) {

                horasAnt = horastrabajos;
                horastrabajos -= minutoSig.getDouble(AGENDA_ESPACIOSIG);
                System.out.println("horastrabajos = " + horastrabajos);

                if (horastrabajos > 0) {
                    min += Math.round((minutoSig.getDouble(AGENDA_ESPACIOSIG) * 1000) * ((double) (HORASLONG) / 1000));
                    while (minutoSig.getInt(AGENDA_FIN) == 0 && minutoSig.getString(AGENDA_ID_MINSIG) != null &&
                            minutoSig.getLong(AGENDA_VALORSIG) <= minutoSig.getLong(AGENDA_VALORENTRADA) + bloqueMin) {
                        minutoSig = CRUDutil.updateModelo(CAMPOS_AGENDA, minutoSig.getString(AGENDA_ID_MINSIG));
                        if (guardar && minutoSig.getInt(AGENDA_FIN) == 1 &&
                                minutoSig.getLong(AGENDA_VALORANT) < minutoSig.getLong(AGENDA_VALORENTRADA) - bloqueMin) {
                            partida = CRUDutil.updateModelo(CAMPOS_PARTIDA, PARTIDA_ID_PARTIDA,
                                    detPartida.getString(DETPARTIDA_ID_PARTIDA), null, IGUAL, null);
                            if (minutoSig.getLong(AGENDA_VALORSIG) > minutoSig.getLong(AGENDA_VALORENTRADA) + bloqueMin) {
                                ContentValues values = new ContentValues();
                                values.put(AGENDA_VALORENTRADA, minutoSig.getLong(AGENDA_VALORENTRADA) + bloqueMin);
                                values.put(AGENDA_ID_PARTIDA, partida.getString(PARTIDA_ID_PROYECTO));
                                values.put(AGENDA_ID_DETPARTIDA, partida.getString(PARTIDA_ID_PARTIDA));
                                values.put(AGENDA_SECUENCIA_PARTIDA, partida.getInt(PARTIDA_SECUENCIA));
                                values.put(AGENDA_SECUENCIA_DETPARTIDA, detPartida.getInt(DETPARTIDA_SECUENCIA));
                                CRUDutil.crearRegistro(TABLA_AGENDA, values);
                            }
                        }
                    }
                } else {
                    min += Math.round((horasAnt * 1000) * ((double) (HORASLONG) / 1000)) + Math.round((horastrabajos * 1000) * ((double) (HORASLONG) / 1000));
                }

            }

            System.out.println("min = " + min);
            System.out.println("return fechafin " + TimeDateUtil.getDateTimeString(minutoini.getLong(AGENDA_VALORENTRADA) + min));
            return minutoini.getLong(AGENDA_VALORENTRADA) + min;

        }


        private static double calculoEspacioEntreMinutos(long fechaIni, long fechaFin) {

            Modelo perfilActivo = ConsultaBD.queryObject(CAMPOS_PERFIL, PERFIL_NOMBRE, perfila, null, IGUAL, null);
            long horaHoy = 0;
            long fechahoy = 0;
            double horastrabajos = 0;
            TimeZone timezone = TimeZone.getDefault();
            Calendar calendar = new GregorianCalendar(timezone);
            if (fechaIni>0){
                calendar.setTimeInMillis(fechaIni);
                fechahoy = TimeDateUtil.soloFecha(fechaIni);
                horaHoy = TimeDateUtil.soloHora(fechaIni);
            }else{
                fechahoy = TimeDateUtil.soloFecha(TimeDateUtil.ahora());
                horaHoy = TimeDateUtil.soloHora(TimeDateUtil.ahora());
            }

            int diahoy = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            String IM = null;
            String FM = null;
            String IT = null;
            String FT = null;

            while (fechahoy < fechaFin) {

                switch (diahoy) {

                    case 0:

                        IM = PERFIL_HORAIMDOMINGO;
                        FM = PERFIL_HORAFMDOMINGO;
                        IT = PERFIL_HORAITDOMINGO;
                        FT = PERFIL_HORAFTDOMINGO;

                        System.out.println("perfilActivo.getLong(IM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IM)));
                        System.out.println("perfilActivo.getLong(FM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FM)));
                        System.out.println("perfilActivo.getLong(IT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IT)));
                        System.out.println("perfilActivo.getLong(FT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FT)));
                        System.out.println("horaHoy antes= " + TimeDateUtil.getTimeString(horaHoy));
                        System.out.println("horastrabajos antes= " + horastrabajos);


                        while (((perfilActivo.getLong(FT) >= 0 &&
                                horaHoy < perfilActivo.getLong(FT)) ||
                                (perfilActivo.getLong(FM) >= 0 &&
                                        horaHoy < perfilActivo.getLong(FM))) && fechahoy<fechaFin){

                            if ((horaHoy >= perfilActivo.getLong(IT) &&
                                    perfilActivo.getLong(FT) >= 0 &&
                                    horaHoy < perfilActivo.getLong(FT)) ||
                                    (horaHoy >= perfilActivo.getLong(IM) &&
                                            perfilActivo.getLong(FM) >= 0 &&
                                            horaHoy < perfilActivo.getLong(FM))){
                                System.out.println("horaHoy%HORASLONG = " + horaHoy%HORASLONG);
                                System.out.println("minutos "+((double)(horaHoy%HORASLONG)/MINUTOSLONG));
                                System.out.println("Fraccion "+(1/(60/((double)(horaHoy%HORASLONG)/MINUTOSLONG))));
                                if (horaHoy%HORASLONG>0){
                                    horastrabajos+=(1/(60/((double)(HORASLONG-(horaHoy%HORASLONG))/MINUTOSLONG)));
                                    horaHoy+=(HORASLONG-(horaHoy%HORASLONG));
                                }else {
                                    horastrabajos++;
                                    horaHoy += HORASLONG;
                                }

                            } else {
                                horaHoy += HORASLONG;
                            }

                            System.out.println("horastrabajos = " + horastrabajos);
                            fechahoy += horaHoy;
                            System.out.println("fechahoy = " + fechahoy);
                            System.out.println("fechaFin = " + fechaFin);
                        }

                        if (fechahoy >= fechaFin) {
                            break;
                        }else{
                            horaHoy = perfilActivo.getLong(PERFIL_HORAIMLUNES);

                        }

                    case 1:

                        IM = PERFIL_HORAIMLUNES;
                        FM = PERFIL_HORAFMLUNES;
                        IT = PERFIL_HORAITLUNES;
                        FT = PERFIL_HORAFTLUNES;

                        System.out.println("perfilActivo.getLong(IM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IM)));
                        System.out.println("perfilActivo.getLong(FM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FM)));
                        System.out.println("perfilActivo.getLong(IT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IT)));
                        System.out.println("perfilActivo.getLong(FT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FT)));
                        System.out.println("horaHoy antes= " + TimeDateUtil.getTimeString(horaHoy));
                        System.out.println("horastrabajos antes= " + horastrabajos);


                        while (((perfilActivo.getLong(FT) >= 0 &&
                                horaHoy < perfilActivo.getLong(FT)) ||
                                (perfilActivo.getLong(FM) >= 0 &&
                                        horaHoy < perfilActivo.getLong(FM))) && fechahoy<fechaFin){

                            if ((horaHoy >= perfilActivo.getLong(IT) &&
                                    perfilActivo.getLong(FT) >= 0 &&
                                    horaHoy < perfilActivo.getLong(FT)) ||
                                    (horaHoy >= perfilActivo.getLong(IM) &&
                                            perfilActivo.getLong(FM) >= 0 &&
                                            horaHoy < perfilActivo.getLong(FM))){
                                System.out.println("horaHoy%HORASLONG = " + horaHoy%HORASLONG);
                                System.out.println("minutos "+((double)(horaHoy%HORASLONG)/MINUTOSLONG));
                                System.out.println("Fraccion "+(1/(60/((double)(horaHoy%HORASLONG)/MINUTOSLONG))));
                                if (horaHoy%HORASLONG>0){
                                    horastrabajos+=(1/(60/((double)(HORASLONG-(horaHoy%HORASLONG))/MINUTOSLONG)));
                                    horaHoy+=(HORASLONG-(horaHoy%HORASLONG));
                                }else {
                                    horastrabajos++;
                                    horaHoy += HORASLONG;
                                }

                            } else {
                                horaHoy += HORASLONG;
                            }

                            System.out.println("horastrabajos = " + horastrabajos);
                            fechahoy += horaHoy;
                            System.out.println("fechahoy = " + fechahoy);
                            System.out.println("fechaFin = " + fechaFin);

                        }

                        if (fechahoy >= fechaFin) {
                            break;
                        }else{
                            horaHoy = perfilActivo.getLong(PERFIL_HORAIMMARTES);

                        }


                    case 2:


                        IM = PERFIL_HORAIMMARTES;
                        FM = PERFIL_HORAFMMARTES;
                        IT = PERFIL_HORAITMARTES;
                        FT = PERFIL_HORAFTMARTES;

                        System.out.println("perfilActivo.getLong(IM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IM)));
                        System.out.println("perfilActivo.getLong(FM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FM)));
                        System.out.println("perfilActivo.getLong(IT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IT)));
                        System.out.println("perfilActivo.getLong(FT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FT)));
                        System.out.println("horaHoy antes= " + TimeDateUtil.getTimeString(horaHoy));
                        System.out.println("horastrabajos antes= " + horastrabajos);


                        while (((perfilActivo.getLong(FT) >= 0 &&
                                horaHoy < perfilActivo.getLong(FT)) ||
                                (perfilActivo.getLong(FM) >= 0 &&
                                        horaHoy < perfilActivo.getLong(FM))) && fechahoy<fechaFin){

                            if ((horaHoy >= perfilActivo.getLong(IT) &&
                                    perfilActivo.getLong(FT) >= 0 &&
                                    horaHoy < perfilActivo.getLong(FT)) ||
                                    (horaHoy >= perfilActivo.getLong(IM) &&
                                            perfilActivo.getLong(FM) >= 0 &&
                                            horaHoy < perfilActivo.getLong(FM))){
                                System.out.println("horaHoy%HORASLONG = " + horaHoy%HORASLONG);
                                System.out.println("minutos "+((double)(horaHoy%HORASLONG)/MINUTOSLONG));
                                System.out.println("Fraccion "+(1/(60/((double)(horaHoy%HORASLONG)/MINUTOSLONG))));
                                if (horaHoy%HORASLONG>0){
                                    horastrabajos+=(1/(60/((double)(HORASLONG-(horaHoy%HORASLONG))/MINUTOSLONG)));
                                    horaHoy+=(HORASLONG-(horaHoy%HORASLONG));
                                }else {
                                    horastrabajos++;
                                    horaHoy += HORASLONG;
                                }

                            } else {
                                horaHoy += HORASLONG;
                            }

                            System.out.println("horastrabajos = " + horastrabajos);
                            fechahoy += horaHoy;
                            System.out.println("fechahoy = " + fechahoy);
                            System.out.println("fechaFin = " + fechaFin);

                        }

                        if (fechahoy >= fechaFin) {
                            break;
                        }else{
                            horaHoy = perfilActivo.getLong(PERFIL_HORAIMMIERCOLES);

                        }


                    case 3:

                        IM = PERFIL_HORAIMMIERCOLES;
                        FM = PERFIL_HORAFMMIERCOLES;
                        IT = PERFIL_HORAITMIERCOLES;
                        FT = PERFIL_HORAFTMIERCOLES;

                        System.out.println("perfilActivo.getLong(IM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IM)));
                        System.out.println("perfilActivo.getLong(FM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FM)));
                        System.out.println("perfilActivo.getLong(IT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IT)));
                        System.out.println("perfilActivo.getLong(FT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FT)));
                        System.out.println("horaHoy antes= " + TimeDateUtil.getTimeString(horaHoy));
                        System.out.println("horastrabajos antes= " + horastrabajos);


                        while (((perfilActivo.getLong(FT) >= 0 &&
                                horaHoy < perfilActivo.getLong(FT)) ||
                                (perfilActivo.getLong(FM) >= 0 &&
                                        horaHoy < perfilActivo.getLong(FM))) && fechahoy<fechaFin){

                            if ((horaHoy >= perfilActivo.getLong(IT) &&
                                    perfilActivo.getLong(FT) >= 0 &&
                                    horaHoy < perfilActivo.getLong(FT)) ||
                                    (horaHoy >= perfilActivo.getLong(IM) &&
                                            perfilActivo.getLong(FM) >= 0 &&
                                            horaHoy < perfilActivo.getLong(FM))){
                                System.out.println("horaHoy%HORASLONG = " + horaHoy%HORASLONG);
                                System.out.println("minutos "+((double)(horaHoy%HORASLONG)/MINUTOSLONG));
                                System.out.println("Fraccion "+(1/(60/((double)(horaHoy%HORASLONG)/MINUTOSLONG))));
                                if (horaHoy%HORASLONG>0){
                                    horastrabajos+=(1/(60/((double)(HORASLONG-(horaHoy%HORASLONG))/MINUTOSLONG)));
                                    horaHoy+=(HORASLONG-(horaHoy%HORASLONG));
                                }else {
                                    horastrabajos++;
                                    horaHoy += HORASLONG;
                                }

                            } else {
                                horaHoy += HORASLONG;
                            }

                            System.out.println("horastrabajos = " + horastrabajos);
                            fechahoy += horaHoy;
                            System.out.println("fechahoy = " + fechahoy);
                            System.out.println("fechaFin = " + fechaFin);

                        }

                        if (fechahoy >= fechaFin) {
                            break;
                        }else{
                            horaHoy = perfilActivo.getLong(PERFIL_HORAIMJUEVES);

                        }

                    case 4:

                        IM = PERFIL_HORAIMJUEVES;
                        FM = PERFIL_HORAFMJUEVES;
                        IT = PERFIL_HORAITJUEVES;
                        FT = PERFIL_HORAFTJUEVES;

                        System.out.println("perfilActivo.getLong(IM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IM)));
                        System.out.println("perfilActivo.getLong(FM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FM)));
                        System.out.println("perfilActivo.getLong(IT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IT)));
                        System.out.println("perfilActivo.getLong(FT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FT)));
                        System.out.println("horaHoy antes= " + TimeDateUtil.getTimeString(horaHoy));
                        System.out.println("horastrabajos antes= " + horastrabajos);


                        while (((perfilActivo.getLong(FT) >= 0 &&
                                horaHoy < perfilActivo.getLong(FT)) ||
                                (perfilActivo.getLong(FM) >= 0 &&
                                        horaHoy < perfilActivo.getLong(FM))) && fechahoy<fechaFin){

                            if ((horaHoy >= perfilActivo.getLong(IT) &&
                                    perfilActivo.getLong(FT) >= 0 &&
                                    horaHoy < perfilActivo.getLong(FT)) ||
                                    (horaHoy >= perfilActivo.getLong(IM) &&
                                            perfilActivo.getLong(FM) >= 0 &&
                                            horaHoy < perfilActivo.getLong(FM))){
                                System.out.println("horaHoy%HORASLONG = " + horaHoy%HORASLONG);
                                System.out.println("minutos "+((double)(horaHoy%HORASLONG)/MINUTOSLONG));
                                System.out.println("Fraccion "+(1/(60/((double)(horaHoy%HORASLONG)/MINUTOSLONG))));
                                if (horaHoy%HORASLONG>0){
                                    horastrabajos+=(1/(60/((double)(HORASLONG-(horaHoy%HORASLONG))/MINUTOSLONG)));
                                    horaHoy+=(HORASLONG-(horaHoy%HORASLONG));
                                }else {
                                    horastrabajos++;
                                    horaHoy += HORASLONG;
                                }

                            } else {
                                horaHoy += HORASLONG;
                            }

                            System.out.println("horastrabajos = " + horastrabajos);
                            fechahoy += horaHoy;
                            System.out.println("fechahoy = " + fechahoy);
                            System.out.println("fechaFin = " + fechaFin);

                        }

                        if (fechahoy >= fechaFin) {
                            break;
                        }else{
                            horaHoy = perfilActivo.getLong(PERFIL_HORAIMVIERNES);

                        }

                    case 5:

                        IM = PERFIL_HORAIMVIERNES;
                        FM = PERFIL_HORAFMVIERNES;
                        IT = PERFIL_HORAITVIERNES;
                        FT = PERFIL_HORAFTVIERNES;

                        System.out.println("perfilActivo.getLong(IM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IM)));
                        System.out.println("perfilActivo.getLong(FM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FM)));
                        System.out.println("perfilActivo.getLong(IT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IT)));
                        System.out.println("perfilActivo.getLong(FT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FT)));
                        System.out.println("horaHoy antes= " + TimeDateUtil.getTimeString(horaHoy));
                        System.out.println("horastrabajos antes= " + horastrabajos);


                        while (((perfilActivo.getLong(FT) >= 0 &&
                                horaHoy < perfilActivo.getLong(FT)) ||
                                (perfilActivo.getLong(FM) >= 0 &&
                                        horaHoy < perfilActivo.getLong(FM))) && fechahoy<fechaFin){

                            if ((horaHoy >= perfilActivo.getLong(IT) &&
                                    perfilActivo.getLong(FT) >= 0 &&
                                    horaHoy < perfilActivo.getLong(FT)) ||
                                    (horaHoy >= perfilActivo.getLong(IM) &&
                                            perfilActivo.getLong(FM) >= 0 &&
                                            horaHoy < perfilActivo.getLong(FM))){
                                System.out.println("horaHoy%HORASLONG = " + horaHoy%HORASLONG);
                                System.out.println("minutos "+((double)(horaHoy%HORASLONG)/MINUTOSLONG));
                                System.out.println("Fraccion "+(1/(60/((double)(horaHoy%HORASLONG)/MINUTOSLONG))));
                                if (horaHoy%HORASLONG>0){
                                    horastrabajos+=(1/(60/((double)(HORASLONG-(horaHoy%HORASLONG))/MINUTOSLONG)));
                                    horaHoy+=(HORASLONG-(horaHoy%HORASLONG));
                                }else {
                                    horastrabajos++;
                                    horaHoy += HORASLONG;
                                }

                            } else {
                                horaHoy += HORASLONG;
                            }

                            System.out.println("horastrabajos = " + horastrabajos);
                            fechahoy += horaHoy;
                            System.out.println("fechahoy = " + fechahoy);
                            System.out.println("fechaFin = " + fechaFin);

                        }

                        if (fechahoy >= fechaFin) {
                            break;
                        }else{
                            horaHoy = perfilActivo.getLong(PERFIL_HORAIMSABADO);

                        }


                    case 6:

                        IM = PERFIL_HORAIMSABADO;
                        FM = PERFIL_HORAFMSABADO;
                        IT = PERFIL_HORAITSABADO;
                        FT = PERFIL_HORAFTSABADO;

                        System.out.println("perfilActivo.getLong(IM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IM)));
                        System.out.println("perfilActivo.getLong(FM) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FM)));
                        System.out.println("perfilActivo.getLong(IT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(IT)));
                        System.out.println("perfilActivo.getLong(FT) = " + TimeDateUtil.getTimeString(perfilActivo.getLong(FT)));
                        System.out.println("horaHoy antes= " + TimeDateUtil.getTimeString(horaHoy));
                        System.out.println("horastrabajos antes= " + horastrabajos);


                        while (((perfilActivo.getLong(FT) >= 0 &&
                                horaHoy < perfilActivo.getLong(FT)) ||
                                (perfilActivo.getLong(FM) >= 0 &&
                                        horaHoy < perfilActivo.getLong(FM))) && fechahoy<fechaFin){

                            if ((horaHoy >= perfilActivo.getLong(IT) &&
                                    perfilActivo.getLong(FT) >= 0 &&
                                    horaHoy < perfilActivo.getLong(FT)) ||
                                    (horaHoy >= perfilActivo.getLong(IM) &&
                                            perfilActivo.getLong(FM) >= 0 &&
                                            horaHoy < perfilActivo.getLong(FM))){
                                System.out.println("horaHoy%HORASLONG = " + horaHoy%HORASLONG);
                                System.out.println("minutos "+((double)(horaHoy%HORASLONG)/MINUTOSLONG));
                                System.out.println("Fraccion "+(1/(60/((double)(horaHoy%HORASLONG)/MINUTOSLONG))));
                                if (horaHoy%HORASLONG>0){
                                    horastrabajos+=(1/(60/((double)(HORASLONG-(horaHoy%HORASLONG))/MINUTOSLONG)));
                                    horaHoy+=(HORASLONG-(horaHoy%HORASLONG));
                                }else {
                                    horastrabajos++;
                                    horaHoy += HORASLONG;
                                }

                            } else {
                                horaHoy += HORASLONG;
                            }

                            System.out.println("horastrabajos = " + horastrabajos);
                            fechahoy += horaHoy;
                            System.out.println("fechahoy = " + fechahoy);
                            System.out.println("fechaFin = " + fechaFin);

                        }

                        if (fechahoy >= fechaFin) {
                            break;
                        }else{
                            horaHoy = perfilActivo.getLong(PERFIL_HORAIMDOMINGO);

                        }

                }
                diahoy = 0;
            }

            return horastrabajos;

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

        public static void actualizarProdProvPartidaProy(String idPartida) {

            ArrayList<Modelo> listaDetPartida;
            listaDetPartida = ConsultaBD.queryListDetalle(CAMPOS_DETPARTIDA, idPartida, TABLA_PARTIDA);
            final Modelo partida = ConsultaBD.queryObject(CAMPOS_PARTIDA, PARTIDA_ID_PARTIDA, idPartida, null,
                    JavaUtil.Constantes.IGUAL, null);
            for (final Modelo detPartida : listaDetPartida) {

                if (detPartida.getString(DETPARTIDA_TIPO).equals(TIPOPRODUCTOPROV)) {

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child(PRODUCTOPRO).child(detPartida.getString(DETPARTIDA_ID_DETPARTIDA)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Productos prodProv = dataSnapshot.getValue(Productos.class);

                            ContentValues valores = new ContentValues();

                            valores.put(DETPARTIDA_NOMBRE, prodProv.getNombre());
                            valores.put(DETPARTIDA_DESCRIPCION, prodProv.getDescripcion());
                            valores.put(DETPARTIDA_REFPROVCAT, prodProv.getRefprov());
                            valores.put(DETPARTIDA_RUTAFOTO, prodProv.getRutafoto());

                            if (partida.getInt(PARTIDA_TIPO_ESTADO) == 1) {
                                valores.put(DETPARTIDA_PRECIO, prodProv.getPrecio());
                                valores.put(DETPARTIDA_DESCUENTOPROVCAT, prodProv.getDescProv());
                            }

                            int res = CRUDutil.actualizarRegistro(detPartida, valores);
                            System.out.println("detpartidas actualizadas = " + res);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }

        }

        public static void actualizarPartidaProyecto(String idPartida) {

            double tiempoPartida = 0;
            double importeProductosPartida = 0;
            double coste = 0;
            double importeTiempoPartida = 0;
            double totalPartida = 0;
            ArrayList<Modelo> listaDetPartida;
            listaDetPartida = ConsultaBD.queryListDetalle(CAMPOS_DETPARTIDA, idPartida, TABLA_PARTIDA);
            Modelo partida = ConsultaBD.queryObject(CAMPOS_PARTIDA, PARTIDA_ID_PARTIDA, idPartida, null,
                    JavaUtil.Constantes.IGUAL, null);

            double precioHora;
            if (partida.getInt(PARTIDA_TIPO_ESTADO)==TiposEstados.TNUEVOPRESUP){
                precioHora = hora;
            }else{
                Modelo proyecto = CRUDutil.updateModelo(CAMPOS_PROYECTO,partida.getString(PARTIDA_ID_PROYECTO));
                precioHora = proyecto.getDouble(PROYECTO_PRECIOHORA);
            }

            for (Modelo detPartida : listaDetPartida) {

                ContentValues valores = new ContentValues();

                if (detPartida.getString(DETPARTIDA_TIPO).equals(TIPOTRABAJO)) {

                    Modelo trabajo = CRUDutil.updateModelo(CAMPOS_TRABAJO, detPartida.getString(DETPARTIDA_ID_DETPARTIDA));
                    valores.put(DETPARTIDA_NOMBRE, trabajo.getString(TRABAJO_NOMBRE));
                    valores.put(DETPARTIDA_DESCRIPCION, trabajo.getString(TRABAJO_DESCRIPCION));
                    valores.put(DETPARTIDA_RUTAFOTO, trabajo.getString(TRABAJO_RUTAFOTO));

                    if (partida.getInt(PARTIDA_TIPO_ESTADO) == 1) {
                        valores.put(DETPARTIDA_TIEMPO, trabajo.getDouble(TRABAJO_TIEMPO));
                        valores.put(DETPARTIDA_PRECIO, trabajo.getDouble(TRABAJO_TIEMPO) * precioHora);
                    }

                } else if (detPartida.getString(DETPARTIDA_TIPO).equals(TIPOPRODUCTO)) {

                    Modelo producto = CRUDutil.updateModelo(CAMPOS_PRODUCTO, detPartida.getString(DETPARTIDA_ID_DETPARTIDA));
                    valores.put(DETPARTIDA_NOMBRE, producto.getString(PRODUCTO_NOMBRE));
                    valores.put(DETPARTIDA_DESCRIPCION, producto.getString(PRODUCTO_DESCRIPCION));
                    valores.put(DETPARTIDA_REFPROVEEDOR, producto.getString(PRODUCTO_REFERENCIA));
                    valores.put(DETPARTIDA_RUTAFOTO, producto.getString(PRODUCTO_RUTAFOTO));

                    if (partida.getInt(PARTIDA_TIPO_ESTADO) == 1) {
                        valores.put(DETPARTIDA_PRECIO, producto.getDouble(PRODUCTO_PRECIO));
                        valores.put(DETPARTIDA_DESCUENTOPROVEEDOR, producto.getDouble(PRODUCTO_DESCPROV));
                    }
                }

                System.out.println("valores = " + valores);
                int res = CRUDutil.actualizarRegistro(detPartida, valores);
                System.out.println("detpartidas actualizadas = " + res);

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
            importeTiempoPartida = tiempoPartida * precioHora;
            totalPartida = importeProductosPartida + importeTiempoPartida;


            double cantidadPartida = partida.getDouble(PARTIDA_CANTIDAD);

            ContentValues valores = new ContentValues();
            ConsultaBD.putDato(valores, CAMPOS_PARTIDA, PARTIDA_TIEMPO, tiempoPartida * cantidadPartida);
            ConsultaBD.putDato(valores, CAMPOS_PARTIDA, PARTIDA_PRECIO, totalPartida * cantidadPartida);
            ConsultaBD.putDato(valores, CAMPOS_PARTIDA, PARTIDA_COSTE, coste * cantidadPartida);

            String idProyecto_Partida = partida.getString(PARTIDA_ID_PROYECTO);
            int secuenciaPartida = partida.getInt(PARTIDA_SECUENCIA);
            int i = ConsultaBD.updateRegistroDetalle(TABLA_PARTIDA, idProyecto_Partida, secuenciaPartida, valores);
            if (i > 0) {
                System.out.println("Partidas actualizadas = " + i);
            }

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
                totalTiempo += itemPartida.getDouble(PARTIDA_TIEMPO) ;
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
                totalTiempo += itemPartida.getDouble(PARTIDA_TIEMPO) ;
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
                    totalTiempo += itemPartida.getDouble(PARTIDA_TIEMPO) ;
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

        public static void sincroPartidaBaseToPartidaProy(final String id, final String secuencia) {

            try {

                Modelo modelo = CRUDutil.updateModelo(CAMPOS_PARTIDA, id, secuencia);
                Modelo partidaBase = CRUDutil.updateModelo(CAMPOS_PARTIDABASE, modelo.getString(PARTIDA_ID_PARTIDABASE));
                ContentValues valores = new ContentValues();

                putDato(valores, CAMPOS_PARTIDA, PARTIDA_NOMBRE, partidaBase.getString(PARTIDABASE_NOMBRE));
                putDato(valores, CAMPOS_PARTIDA, PARTIDA_DESCRIPCION, partidaBase.getString(PARTIDABASE_DESCRIPCION));
                putDato(valores, CAMPOS_PARTIDA, PARTIDA_RUTAFOTO, partidaBase.getString(PARTIDABASE_RUTAFOTO));

                CRUDutil.actualizarRegistro(TABLA_PARTIDA, id, secuencia, valores);

                String iddetpartida = modelo.getString(PARTIDA_ID_PARTIDA);
                String iddetpartidabase = partidaBase.getString(PARTIDABASE_ID_PARTIDABASE);
                ListaModelo listaDetPartidabase = CRUDutil.setListaModeloDetalle(CAMPOS_DETPARTIDABASE, iddetpartidabase, TABLA_PARTIDABASE);
                for (Modelo detPartidaBase : listaDetPartidabase.getLista()) {

                    valores = new ContentValues();

                    putDato(valores, CAMPOS_DETPARTIDA, DETPARTIDA_ID_PARTIDA, iddetpartida);
                    putDato(valores, CAMPOS_DETPARTIDA, DETPARTIDA_ID_DETPARTIDA, detPartidaBase.getString(DETPARTIDABASE_ID_DETPARTIDABASE));
                    putDato(valores, CAMPOS_DETPARTIDA, DETPARTIDA_TIPO, detPartidaBase.getString(DETPARTIDABASE_TIPO));
                    putDato(valores, CAMPOS_DETPARTIDA, DETPARTIDA_CANTIDAD, detPartidaBase.getString(DETPARTIDABASE_CANTIDAD));
                    boolean detnuevo = true;
                    ListaModelo listaDetPartida = CRUDutil.setListaModeloDetalle(CAMPOS_DETPARTIDA, iddetpartida, TABLA_PARTIDA);
                    for (Modelo detPartida : listaDetPartida.getLista()) {

                        if (detPartida.getString(DETPARTIDA_ID_DETPARTIDA).equals(detPartidaBase.getString(DETPARTIDABASE_ID_DETPARTIDABASE))) {
                            CRUDutil.actualizarRegistro(TABLA_DETPARTIDA, iddetpartida, detPartida.getInt(DETPARTIDA_SECUENCIA), valores);
                            detnuevo = false;
                        }
                    }
                    if (detnuevo) {
                        CRUDutil.crearRegistroSec(CAMPOS_DETPARTIDA, iddetpartida, TABLA_PARTIDA, valores);

                    }
                }

                ListaModelo listaDetPartida = CRUDutil.setListaModeloDetalle(CAMPOS_DETPARTIDA, iddetpartida, TABLA_PARTIDA);
                for (Modelo detPartida : listaDetPartida.getLista()) {
                    boolean cambio = true;
                    for (Modelo detPartidaBase : listaDetPartidabase.getLista()) {

                        if (detPartida.getString(DETPARTIDA_ID_DETPARTIDA).equals(detPartidaBase.getString(DETPARTIDABASE_ID_DETPARTIDABASE))) {
                            cambio = false;
                        }
                    }
                    if (cambio) {
                        CRUDutil.borrarRegistro(TABLA_DETPARTIDA, detPartida.getString(DETPARTIDA_ID_PARTIDA), detPartida.getInt(DETPARTIDA_SECUENCIA));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static class Tareafechas extends AsyncTask<Boolean, Float, Integer> {

            @Override
            protected Integer doInBackground(Boolean... booleans) {

                recalcularFechas(booleans[0]);
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

        public static class TareaSincroPartidaProy extends AsyncTask<String, Float, Modelo> {

            @Override
            protected Modelo doInBackground(String... strings) {

                sincroPartidaBaseToPartidaProy(strings[0], strings[1]);
                return CRUDutil.updateModelo(CAMPOS_PARTIDA, strings[0], strings[1]);
            }

            @Override
            protected void onPostExecute(Modelo modelo) {
                super.onPostExecute(modelo);
                new Interactor.Calculos.TareaActualizaPartidaProy().execute(modelo.getString(PARTIDA_ID_PARTIDA));

            }

        }


        public static class TareaActualizaPartidaProy extends AsyncTask<String, Float, Integer> {

            @Override
            protected Integer doInBackground(String... strings) {

                actualizarPartidaProyecto(strings[0]);
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
