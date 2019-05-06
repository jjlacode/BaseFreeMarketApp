package jjlacode.com.freelanceproject.sqlite;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import jjlacode.com.freelanceproject.util.CommonPry;
import static jjlacode.com.freelanceproject.sqlite.ContratoPry.*;

public class ProviderFreelanceProject extends ContentProvider
        implements Tablas, CommonPry.TiposEstados {

    private DataBase bd;

    private ContentResolver resolver;

    public static final UriMatcher uriMatcher;

    // Casos
    public static final int PROYECTO = 101;
    public static final int PROYECTO_ID = 102;
    public static final int PROYECTO_ID_PARTIDA = 103;

    public static final int PARTIDA = 105;
    public static final int PARTIDA_ID = 106;
    public static final int PARTIDA_ID_DETPARTIDA = 129;

    public static final int CLIENTE = 109;
    public static final int CLIENTE_ID = 110;

    public static final int ESTADO = 111;
    public static final int ESTADO_ID = 112;

    public static final int PERFIL = 113;
    public static final int PERFIL_ID = 114;

    public static final int AMORTIZACION = 115;
    public static final int AMORTIZACION_ID = 116;

    public static final int TIPOCLIENTE = 117;
    public static final int TIPOCLIENTE_ID = 118;

    public static final int GASTOFIJO = 121;
    public static final int GASTOFIJO_ID = 122;

    public static final int PRODUCTO = 123;
    public static final int PRODUCTO_ID = 124;

    public static final int TAREA = 125;
    public static final int TAREA_ID = 126;

    public static final int EVENTO = 127;
    public static final int EVENTO_ID = 128;

    public static final int DETPARTIDA = 130;
    public static final int DETPARTIDA_ID = 131;

    public static final int PARTIDABASE = 132;
    public static final int PARTIDABASE_ID = 133;
    public static final int PARTIDABASE_ID_DETPARTIDA = 134;

    public static final int DETPARTIDABASE = 135;
    public static final int DETPARTIDABASE_ID = 136;

    public static final String AUTORIDAD = AUTORIDAD_CONTENIDO;//"jjlacode.com.freelanceproject2";

    static {

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTORIDAD, TABLA_PROYECTO, PROYECTO);
        uriMatcher.addURI(AUTORIDAD, TABLA_PROYECTO+"/*", PROYECTO_ID);
        uriMatcher.addURI(AUTORIDAD, TABLA_PROYECTO+"/*/"+TABLA_PARTIDA, PROYECTO_ID_PARTIDA);

        uriMatcher.addURI(AUTORIDAD, TABLA_PARTIDA, PARTIDA);
        uriMatcher.addURI(AUTORIDAD, TABLA_PARTIDA+"/*", PARTIDA_ID);
        uriMatcher.addURI(AUTORIDAD, TABLA_PARTIDA+"/*/"+TABLA_DETPARTIDA, PARTIDA_ID_DETPARTIDA);

        uriMatcher.addURI(AUTORIDAD, TABLA_DETPARTIDA, DETPARTIDA);
        uriMatcher.addURI(AUTORIDAD, TABLA_DETPARTIDA+"/*", DETPARTIDA_ID);

        uriMatcher.addURI(AUTORIDAD, TABLA_PARTIDABASE, PARTIDABASE);
        uriMatcher.addURI(AUTORIDAD, TABLA_PARTIDABASE+"/*", PARTIDABASE_ID);
        uriMatcher.addURI(AUTORIDAD, TABLA_PARTIDABASE+"/*/"+TABLA_DETPARTIDABASE, PARTIDABASE_ID_DETPARTIDA);

        uriMatcher.addURI(AUTORIDAD, TABLA_DETPARTIDABASE, DETPARTIDABASE);
        uriMatcher.addURI(AUTORIDAD, TABLA_DETPARTIDABASE+"/*", DETPARTIDABASE_ID);

        uriMatcher.addURI(AUTORIDAD, TABLA_CLIENTE, CLIENTE);
        uriMatcher.addURI(AUTORIDAD, TABLA_CLIENTE+"/*", CLIENTE_ID);

        uriMatcher.addURI(AUTORIDAD, TABLA_TIPOCLIENTE, TIPOCLIENTE);
        uriMatcher.addURI(AUTORIDAD, TABLA_TIPOCLIENTE+"/*", TIPOCLIENTE_ID);

        uriMatcher.addURI(AUTORIDAD, TABLA_ESTADO, ESTADO);
        uriMatcher.addURI(AUTORIDAD, TABLA_ESTADO+"/*", ESTADO_ID);

        uriMatcher.addURI(AUTORIDAD, TABLA_PERFIL, PERFIL);
        uriMatcher.addURI(AUTORIDAD, TABLA_PERFIL+"/*", PERFIL_ID);

        uriMatcher.addURI(AUTORIDAD, TABLA_AMORTIZACION, AMORTIZACION);
        uriMatcher.addURI(AUTORIDAD, TABLA_AMORTIZACION+"/*", AMORTIZACION_ID);

        uriMatcher.addURI(AUTORIDAD, TABLA_GASTOFIJO, GASTOFIJO);
        uriMatcher.addURI(AUTORIDAD, TABLA_GASTOFIJO+"/*", GASTOFIJO_ID);

        uriMatcher.addURI(AUTORIDAD, TABLA_PRODUCTO, PRODUCTO);
        uriMatcher.addURI(AUTORIDAD, TABLA_PRODUCTO+"/*", PRODUCTO_ID);

        uriMatcher.addURI(AUTORIDAD, TABLA_TAREA, TAREA);
        uriMatcher.addURI(AUTORIDAD, TABLA_TAREA+"/*", TAREA_ID);

        uriMatcher.addURI(AUTORIDAD, TABLA_EVENTO, EVENTO);
        uriMatcher.addURI(AUTORIDAD, TABLA_EVENTO+"/*", EVENTO_ID);
    }

    private static final String PROYECTO_JOIN_CLIENTE_Y_ESTADO=
            String.format("%s " +
                            "INNER JOIN %s " +
                            "ON %s.%s = %s.%s " +
                            "INNER JOIN %s " +
                            "ON %s.%s = %s.%s",TABLA_PROYECTO,
                            TABLA_CLIENTE,TABLA_PROYECTO,PROYECTO_ID_CLIENTE,
                            TABLA_CLIENTE,CLIENTE_ID_CLIENTE,
                            TABLA_ESTADO,TABLA_PROYECTO,PROYECTO_ID_ESTADO,
                            TABLA_ESTADO,ESTADO_ID_ESTADO);

    private final String proyProyecto = String.format("%s.*,%s,%s,%s,%s",

            TABLA_PROYECTO,
            CLIENTE_NOMBRE,
            CLIENTE_PESOTIPOCLI,
            ESTADO_DESCRIPCION,
            ESTADO_TIPOESTADO);

    private static final String CLIENTE_JOIN_TIPOSCLIENTE=
            String.format("%s " +
            "INNER JOIN %s " +
            "ON %s.%s = %s.%s",TABLA_CLIENTE,
                    TABLA_TIPOCLIENTE,TABLA_CLIENTE,CLIENTE_ID_TIPOCLIENTE,
                    TABLA_TIPOCLIENTE,TIPOCLIENTE_ID_TIPOCLIENTE);

    private final String proyCliente = String.format("%s.*,%s,%s",

            TABLA_CLIENTE ,
            TIPOCLIENTE_DESCRIPCION,
            TIPOCLIENTE_PESO);

    private static final String PARTIDAS_JOIN_ESTADO_JOIN_PROYECTO =
            String.format("%s " +
                    "INNER JOIN %s " +
                    "ON %s.%s = %s.%s " +
                    "INNER JOIN %s " +
                    "ON %s.%s = %s.%s",TABLA_PARTIDA,
                    TABLA_ESTADO,TABLA_PARTIDA,PARTIDA_ID_ESTADO,
                    TABLA_ESTADO,ESTADO_ID_ESTADO,
                    TABLA_PROYECTO,TABLA_PARTIDA,PARTIDA_ID_PROYECTO,
                    TABLA_PROYECTO,PROYECTO_ID_PROYECTO);

    private static final String proyPartida = String.format("%s.*,%s,%s",

            TABLA_PARTIDA,
            PROYECTO_RETRASO,
            ESTADO_TIPOESTADO);






    public ProviderFreelanceProject() {

    }

    @Override
    public boolean onCreate() {
        bd = new DataBase(getContext());


        resolver = getContext().getContentResolver();
        return true;
    }


    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case PROYECTO:
                return generarMime(TABLA_PROYECTO);
            case PROYECTO_ID:
                return generarMimeItem(TABLA_PROYECTO);
            case PARTIDA:
                return generarMime(TABLA_PARTIDA);
            case PARTIDA_ID:
                return generarMimeItem(TABLA_PARTIDA);
            case DETPARTIDA:
                return generarMime(TABLA_DETPARTIDA);
            case DETPARTIDA_ID:
                return generarMimeItem(TABLA_DETPARTIDA);
            case PARTIDABASE:
                return generarMime(TABLA_PARTIDABASE);
            case PARTIDABASE_ID:
                return generarMimeItem(TABLA_PARTIDABASE);
            case DETPARTIDABASE:
                return generarMime(TABLA_DETPARTIDABASE);
            case DETPARTIDABASE_ID:
                return generarMimeItem(TABLA_DETPARTIDABASE);
            case CLIENTE:
                return generarMime(TABLA_CLIENTE);
            case CLIENTE_ID:
                return generarMimeItem(TABLA_CLIENTE);
            case TIPOCLIENTE:
                return generarMime(TABLA_TIPOCLIENTE);
            case TIPOCLIENTE_ID:
                return generarMimeItem(TABLA_TIPOCLIENTE);
            case ESTADO:
                return generarMime(TABLA_ESTADO);
            case ESTADO_ID:
                return generarMimeItem(TABLA_ESTADO);
            case PERFIL:
                return generarMime(TABLA_PERFIL);
            case PERFIL_ID:
                return generarMimeItem(TABLA_PERFIL);
            case AMORTIZACION:
                return generarMime(TABLA_AMORTIZACION);
            case AMORTIZACION_ID:
                return generarMimeItem(TABLA_AMORTIZACION);
            case GASTOFIJO:
                return generarMime(TABLA_GASTOFIJO);
            case GASTOFIJO_ID:
                return generarMimeItem(TABLA_GASTOFIJO);
            case PRODUCTO:
                return generarMime(TABLA_PRODUCTO);
            case PRODUCTO_ID:
                return generarMimeItem(TABLA_PRODUCTO);
            case TAREA:
                return generarMime(TABLA_TAREA);
            case TAREA_ID:
                return generarMimeItem(TABLA_TAREA);
            case EVENTO:
                return generarMime(TABLA_EVENTO);
            case EVENTO_ID:
                return generarMimeItem(TABLA_EVENTO);
            default:
                throw new UnsupportedOperationException("Uri desconocida =>" + uri);
        }
    }

    private ContentValues matcherUri(Uri uri){

        String tabla = null;
        String idTabla = null;
        String setTablas = null;
        String proyeccion = null;
        boolean esDetalle= false;
        boolean esId = false;

        ContentValues values = new ContentValues();

        switch (uriMatcher.match(uri)) {

            case PROYECTO:
                // Generar Pk
                tabla = TABLA_PROYECTO;
                idTabla = PROYECTO_ID_PROYECTO;
                setTablas = PROYECTO_JOIN_CLIENTE_Y_ESTADO;
                proyeccion = proyProyecto;
                esId = false;
                esDetalle = false;
                break;
            case PROYECTO_ID:
                tabla = TABLA_PROYECTO;
                idTabla = PROYECTO_ID_PROYECTO;
                setTablas = PROYECTO_JOIN_CLIENTE_Y_ESTADO;
                proyeccion = proyProyecto;
                esId = true;
                esDetalle = false;
            break;


            case PROYECTO_ID_PARTIDA:
                tabla = TABLA_PARTIDA;
                setTablas = PARTIDAS_JOIN_ESTADO_JOIN_PROYECTO;
                proyeccion = proyPartida;
                idTabla = PARTIDA_ID_PROYECTO;
                esId = false;
                esDetalle = true;

                break;

            case PARTIDA:
                tabla = TABLA_PARTIDA;
                setTablas = PARTIDAS_JOIN_ESTADO_JOIN_PROYECTO;
                proyeccion = proyPartida;
                idTabla = PARTIDA_ID_PROYECTO;
                esDetalle = false;
                esId = false;
                break;

            case PARTIDA_ID:

                tabla = TABLA_PARTIDA;
                setTablas = PARTIDAS_JOIN_ESTADO_JOIN_PROYECTO;
                proyeccion = proyPartida;
                idTabla = PARTIDA_ID_PROYECTO;
                esDetalle = true;
                esId = true;
                break;

            case PARTIDA_ID_DETPARTIDA:

                tabla = TABLA_DETPARTIDA;
                setTablas = tabla;
                proyeccion = tabla+".*";
                idTabla = DETPARTIDA_ID_PARTIDA;
                esDetalle = true;
                esId = false;
                break;

            case DETPARTIDA:
                tabla = TABLA_DETPARTIDA;
                setTablas = tabla;
                proyeccion = tabla+".*";
                idTabla = DETPARTIDA_ID_PARTIDA;
                esId = false;
                esDetalle = false;

                break;
            case DETPARTIDA_ID:

                tabla = TABLA_DETPARTIDA;
                setTablas = tabla;
                proyeccion = tabla+".*";
                idTabla = DETPARTIDA_ID_PARTIDA;
                esDetalle = true;
                esId = true;
                break;
            case PARTIDABASE:
                tabla = TABLA_PARTIDABASE;
                setTablas = tabla;
                proyeccion = tabla+".*";
                idTabla = PARTIDABASE_ID_PARTIDABASE;
                esDetalle = false;
                esId = false;
                break;

            case PARTIDABASE_ID:

                tabla = TABLA_PARTIDABASE;
                setTablas = tabla;
                proyeccion = tabla+".*";
                idTabla = PARTIDABASE_ID_PARTIDABASE;
                esDetalle = false;
                esId = true;
                break;

            case PARTIDABASE_ID_DETPARTIDA:

                tabla = TABLA_DETPARTIDABASE;
                setTablas = tabla;
                proyeccion = tabla+".*";
                idTabla = DETPARTIDABASE_ID_PARTIDABASE;
                esDetalle = true;
                esId = false;
                break;

            case DETPARTIDABASE:
                tabla = TABLA_DETPARTIDABASE;
                setTablas = tabla;
                proyeccion = tabla+".*";
                idTabla = DETPARTIDABASE_ID_PARTIDABASE;
                esId = false;
                esDetalle = false;

                break;
            case DETPARTIDABASE_ID:

                tabla = TABLA_DETPARTIDABASE;
                setTablas = tabla;
                proyeccion = tabla+".*";
                idTabla = DETPARTIDABASE_ID_PARTIDABASE;
                esDetalle = true;
                esId = true;
                break;

            case CLIENTE:
                // Generar Pk
                tabla = TABLA_CLIENTE;
                setTablas = CLIENTE_JOIN_TIPOSCLIENTE;
                proyeccion = proyCliente;
                idTabla = CLIENTE_ID_CLIENTE;
                esId = false;
                esDetalle = false;
                break;

            case CLIENTE_ID:

                tabla = TABLA_CLIENTE;
                idTabla = CLIENTE_ID_CLIENTE;
                setTablas = CLIENTE_JOIN_TIPOSCLIENTE;
                proyeccion = proyCliente;
                esId = true;
                esDetalle = false;
                break;

            case TIPOCLIENTE:
                // Generar Pk
                tabla = TABLA_TIPOCLIENTE;
                setTablas = tabla;
                proyeccion = "*";
                idTabla = TIPOCLIENTE_ID_TIPOCLIENTE;
                esId = false;
                esDetalle = false;
                break;

            case TIPOCLIENTE_ID:

                tabla = TABLA_TIPOCLIENTE;
                idTabla = TIPOCLIENTE_ID_TIPOCLIENTE;
                setTablas = tabla;
                proyeccion = tabla+".*";
                esId = true;
                esDetalle = false;
                break;

            case ESTADO:
                // Generar Pk
                tabla = TABLA_ESTADO;
                setTablas = tabla;
                proyeccion = tabla+".*";
                idTabla = ESTADO_ID_ESTADO;
                esId = false;
                esDetalle = false;
                break;

            case ESTADO_ID:

                tabla = TABLA_ESTADO;
                idTabla = ESTADO_ID_ESTADO;
                setTablas = tabla;
                proyeccion = tabla+".*";
                esId = true;
                esDetalle = false;
                break;

            case PERFIL:
                // Generar Pk
                tabla = TABLA_PERFIL;
                setTablas = tabla;
                proyeccion = tabla+".*";
                idTabla = PERFIL_ID_PERFIL;
                esId = false;
                esDetalle = false;
                break;

            case PERFIL_ID:

                tabla = TABLA_PERFIL;
                idTabla = PERFIL_ID_PERFIL;
                setTablas = tabla;
                proyeccion = tabla+".*";
                esId = true;
                esDetalle = false;
                break;

            case AMORTIZACION:
                // Generar Pk
                tabla = TABLA_AMORTIZACION;
                setTablas = tabla;
                proyeccion = tabla+".*";
                idTabla = AMORTIZACION_ID_AMORTIZACION;
                esId = false;
                esDetalle = false;
                break;

            case AMORTIZACION_ID:

                tabla = TABLA_AMORTIZACION;
                idTabla = AMORTIZACION_ID_AMORTIZACION;
                setTablas = tabla;
                proyeccion = tabla+".*";
                esId = true;
                esDetalle = false;
                break;

            case GASTOFIJO:
                // Generar Pk
                tabla = TABLA_GASTOFIJO;
                setTablas = tabla;
                proyeccion = tabla+".*";
                idTabla = GASTOFIJO_ID_GASTOFIJO;
                esId = false;
                esDetalle = false;
                break;

            case GASTOFIJO_ID:

                tabla = TABLA_GASTOFIJO;
                idTabla = GASTOFIJO_ID_GASTOFIJO;
                setTablas = tabla;
                proyeccion = tabla+".*";
                esId = true;
                esDetalle = false;
                break;

            case PRODUCTO:
                // Generar Pk
                tabla = TABLA_PRODUCTO;
                setTablas = tabla;
                proyeccion = tabla+".*";
                idTabla = PRODUCTO_ID_PRODUCTO;
                esId = false;
                esDetalle = false;
                break;

            case PRODUCTO_ID:

                tabla = TABLA_PRODUCTO;
                idTabla = PRODUCTO_ID_PRODUCTO;
                setTablas = tabla;
                proyeccion = tabla+".*";
                esId = true;
                esDetalle = false;
                break;

            case TAREA:
                // Generar Pk
                tabla = TABLA_TAREA;
                setTablas = tabla;
                proyeccion = tabla+".*";
                idTabla = TAREA_ID_TAREA;
                esId = false;
                esDetalle = false;
                break;

            case TAREA_ID:

                tabla = TABLA_TAREA;
                idTabla = TAREA_ID_TAREA;
                setTablas = tabla;
                proyeccion = tabla+".*";
                esId = true;
                esDetalle = false;
                break;

            case EVENTO:
                // Generar Pk
                tabla = TABLA_EVENTO;
                setTablas = tabla;
                proyeccion = tabla+".*";
                idTabla = EVENTO_ID_EVENTO;
                esId = false;
                esDetalle = false;
                break;

            case EVENTO_ID:

                tabla = TABLA_EVENTO;
                idTabla = EVENTO_ID_EVENTO;
                setTablas = tabla;
                proyeccion = tabla+".*";
                esId = true;
                esDetalle = false;

        }

        values.put("tabla",tabla);
        values.put("idTabla",idTabla);
        values.put("proyeccion", proyeccion);
        values.put("setTablas", setTablas);
        values.put("esDetalle", esDetalle);
        values.put("esId", esId);

        return values;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = bd.getWritableDatabase();


        ContentValues valores = matcherUri(uri);

        String secuencia= values.getAsString("secuencia");
        String tabla = valores.getAsString("tabla");
        String idTabla = valores.getAsString("idTabla");
        String id = generarIdTabla(tabla);

        System.out.println("valores = " + valores);


                if (tabla!=null){
                        if (secuencia == null || Integer.parseInt(secuencia)==0) {
                            values.put(idTabla, id);
                        }
                    System.out.println("values = " + values);
                    db.insertOrThrow(tabla, null, values);
                    notificarCambio(uri);
                    if (secuencia != null && Integer.parseInt(secuencia)>0) {
                        id= values.getAsString(idTabla);
                        return crearUriTablaDetalle(id, secuencia, tabla);
                    }else {
                        return crearUriTabla(id, tabla);
                    }
                } else{
                    throw new UnsupportedOperationException("Uri no soportada");
                }
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Obtener base de datos
        SQLiteDatabase db = bd.getReadableDatabase();

        Cursor c;

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        ContentValues valores = matcherUri(uri);

        String proy = valores.getAsString("proyeccion");
        String[] proyeccion = new String[] {proy};//proy.split(",");
        String setTablas = valores.getAsString("setTablas");
        String idTabla = valores.getAsString("idTabla");
        String[] ids = null;
        boolean esId = valores.getAsBoolean("esId");
        boolean esDetalle = valores.getAsBoolean("esDetalle");
        String tabla = valores.getAsString("tabla");

        if (selection == null) {

            if (esDetalle && esId) {

                ids = obtenerIdTablaDetalle(uri);
                String id = ids[0];
                String secuencia = ids[1];
                selection = tabla + "." +idTabla + " = '" + id + "' AND " +
                        "secuencia = '" + secuencia + "'";
                System.out.println("secuencia = " + secuencia);
                System.out.println("id = " + id);
                System.out.println("selection = " + selection);

            } else if (esDetalle) {

                String id = obtenerIdTablaDetalleId(uri);
                selection = tabla + "." + idTabla + " = '" + id + "'";

            } else if (esId) {

                String id = obtenerIdTabla(uri);
                selection = idTabla + " = '" + id + "'";

            }
        }


        if (setTablas!=null) {
            builder.setTables(setTablas);
            c = builder.query(db, proyeccion, selection,
                    selectionArgs, null, null, sortOrder);
            c.setNotificationUri(resolver, uri);

            return c;

        }else{

            throw new UnsupportedOperationException("Uri no soportada");
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db = bd.getWritableDatabase();

        ContentValues valores = matcherUri(uri);

        String tabla = valores.getAsString("tabla");
        String idTabla = valores.getAsString("idTabla");
        boolean esId = valores.getAsBoolean("esId");
        boolean esDetalle = valores.getAsBoolean("esDetalle");
        String id = null;
        String[] ids = null;
        String secuencia = null;
        String seleccion = null;

        if (selection == null) {
            if (!esDetalle) {
                id = obtenerIdTabla(uri);
                seleccion = idTabla + " = '" + id + "'";

            } else if (esDetalle && !esId){

                id = obtenerIdTablaDetalleId(uri);
                seleccion = tabla+"."+idTabla + " = '" + id + "'";

            }else {
                ids = obtenerIdTablaDetalle(uri);
                id = ids[0];
                secuencia = ids[1];
                seleccion = idTabla + " = '" + id + "' AND " +
                        "secuencia = '" + secuencia + "'";
            }
        }else{
            seleccion = selection;
        }

        if (tabla!=null) {

            notificarCambio(uri);

            return db.update(tabla, values,
                    seleccion ,
                    selectionArgs);

        }else {

            throw new UnsupportedOperationException("Uri no soportada");

        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = bd.getWritableDatabase();

        ContentValues valores = matcherUri(uri);

        String tabla = valores.getAsString("tabla");
        String idTabla = valores.getAsString("idTabla");
        boolean esId = valores.getAsBoolean("esId");
        boolean esDetalle = valores.getAsBoolean("esDetalle");
        String id = null;
        String[] ids = null;
        String secuencia = null;
        String seleccion = null;

        if (selection == null) {

            if (!esDetalle) {
                id = obtenerIdTabla(uri);
                seleccion = idTabla + " = '" + id + "'";

            } else if (esDetalle && !esId) {

                id = obtenerIdTablaDetalleId(uri);
                seleccion = tabla + "." + idTabla + " = '" + id + "'";

            }else {
                ids = obtenerIdTablaDetalle(uri);
                id = ids[0];
                secuencia = ids[1];
                seleccion = idTabla + " = '" + id + "' AND " +
                        "secuencia = '" + secuencia + "'";
            }
        }else{
            seleccion = selection;
        }

        if (tabla!=null) {
            notificarCambio(uri);
            return db.delete(tabla,seleccion,
                    selectionArgs);
        }else{

            throw new UnsupportedOperationException("Uri no soportada");
        }
    }

    private void notificarCambio(Uri uri) {
        resolver.notifyChange(uri, null);
    }

    private String construirFiltro(String filtro) {

        String sentencia = null;

        switch (filtro) {
            case FILTRO_CLIENTE:
                sentencia = "cliente.nombres";
                break;
            case FILTRO_FECHA:
                sentencia = "proyecto.fecha";
                break;
        }

        return sentencia;
    }

}
