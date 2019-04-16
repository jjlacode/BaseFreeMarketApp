package jjlacode.com.freelanceproject.sqlite;

import android.net.Uri;

import java.util.ArrayList;
import java.util.UUID;

import jjlacode.com.androidutils.JavaUtil;


public class ContratoPry implements JavaUtil.Constantes {

    public static final String AUTORIDAD_CONTENIDO =
            "jjlacode.com.freelanceproject";

    public static final Uri URI_BASE = Uri.parse("content://" + AUTORIDAD_CONTENIDO);

    public static final String BASE_CONTENIDOS = "freelanceproject.";

    public static final String TIPO_CONTENIDO = "vnd.android.cursor.dir/vnd."
            + BASE_CONTENIDOS;

    public static final String TIPO_CONTENIDO_ITEM = "vnd.android.cursor.item/vnd."
            + BASE_CONTENIDOS;

    public interface Tablas {

        //TABLAS-----------------------------------------------------

        String TABLA_PROYECTO = "proyecto";
        String TABLA_CLIENTE = "cliente";
        String TABLA_TIPOCLIENTE = "tipocliente";
        String TABLA_ESTADO = "estado";
        String TABLA_PARTIDA = "partida";
        String TABLA_PERFIL = "perfil";
        String TABLA_AMORTIZACION = "amortizacion";
        String TABLA_TAREA = "tarea";
        String TABLA_PRODUCTO = "producto";
        String TABLA_GASTOFIJO = "gastofijo";
        String TABLA_EVENTO = "evento";
        String TABLA_TABLAS = "tablas";
        String TABLA_DETPARTIDA = "detpartida";
        String TABLA_PARTIDABASE = "partidabase";
        String TABLA_DETPARTIDABASE = "detpartidabase";

        //COLUMNAS--------------------------------------------------------

        String PROYECTO_ID_PROYECTO = "id_proyecto";
        String PROYECTO_ID_CLIENTE = "id_proyecto_cliente";
        String PROYECTO_ID_ESTADO = "id_proyecto_estado";
        String PROYECTO_NOMBRE = "nombre_proyecto";
        String PROYECTO_DESCRIPCION = "descripcion";
        String PROYECTO_FECHAENTRADA = "fechaentrada";
        String PROYECTO_FECHAENTREGAPRESUP = "fechaentregapresup";
        String PROYECTO_FECHAENTREGAACORDADA = "fechaentregaacordada";
        String PROYECTO_FECHAENTREGACALCULADA = "fechaentregacalculada";
        String PROYECTO_FECHAFINAL = "fechafinal";
        String PROYECTO_RETRASO = "retraso_proyecto";
        String PROYECTO_IMPORTEPRESUPUESTO = "importepresupuesto";
        String PROYECTO_IMPORTEFINAL = "importefinal";
        String PROYECTO_TIEMPO = "tiempo_proyecto";
        String PROYECTO_RUTAFOTO = "rutafoto_proyecto";
        String PROYECTO_TOTCOMPLETADO = "totcomplet_proyecto";
        //REFERENCIAS
        String PROYECTO_CLIENTE_NOMBRE = "nombre_cliente";
        String PROYECTO_CLIENTE_PESOTIPOCLI = "pesotipo_cliente";
        String PROYECTO_DESCRIPCION_ESTADO = "descripcion_estado";
        String PROYECTO_TIPOESTADO = "tipo_estado";

        String CLIENTE_ID_CLIENTE = "id_cliente";
        String CLIENTE_ID_TIPOCLIENTE = "id_cliente_tipocliente";
        String CLIENTE_NOMBRE = "nombre_cliente";
        String CLIENTE_DIRECCION = "direccion_cliente";
        String CLIENTE_TELEFONO = "telefono_cliente";
        String CLIENTE_EMAIL = "email_cliente";
        String CLIENTE_CONTACTO = "contacto_cliente";
        String CLIENTE_PESOTIPOCLI = "pesotipo_cliente";
        //REFERENCIAS
        String CLIENTE_TIPOCLIENTEPESO = "peso";
        String CLIENTE_DESCRIPCIONTIPOCLI = "descripcion_tipo_cliente";

        String TIPOCLIENTE_ID_TIPOCLIENTE = "id_tipocliente";
        String TIPOCLIENTE_DESCRIPCION = "descripcion_tipo_cliente";
        String TIPOCLIENTE_PESO = "peso";

        String ESTADO_ID_ESTADO = "id_estado";
        String ESTADO_DESCRIPCION = "descripcion_estado";
        String ESTADO_TIPOESTADO = "tipo_estado";

        String PARTIDA_ID_PROYECTO = "id_proyecto_partida";
        String PARTIDA_ID_PARTIDA = "id_partida";
        String PARTIDA_SECUENCIA = "secuencia";
        String PARTIDA_ID_ESTADO = "id_estado_partida";
        String PARTIDA_DESCRIPCION = "descripcion_partida";
        String PARTIDA_NOMBRE = "nombre_partida";
        String PARTIDA_CANTIDAD = "cantidad_partida";
        String PARTIDA_TIEMPO = "tiempo_partida";
        String PARTIDA_PRECIOHORA = "preciohora_partida";
        String PARTIDA_PRECIO = "precio_partida";
        String PARTIDA_COMPLETADA = "completada_partida";
        String PARTIDA_RUTAFOTO = "rutafoto_partida";
        //REFERENCIAS
        String PARTIDA_PROYECTO_RETRASO = "retraso_proyecto";
        String PARTIDA_TIPO_ESTADO = "tipo_estado";


        String DETPARTIDA_ID_PARTIDA = "id_detpartida_partida";
        String DETPARTIDA_SECUENCIA = "secuencia";
        String DETPARTIDA_ID_DETPARTIDA = "id_detpartida";
        String DETPARTIDA_DESCRIPCION = "descripcion_detpartida";
        String DETPARTIDA_NOMBRE = "nombre_detpartida";
        String DETPARTIDA_CANTIDAD = "cantidad_detpartida";
        String DETPARTIDA_TIEMPO = "tiempo_detpartida";
        String DETPARTIDA_PRECIO = "precio_detpartida";
        String DETPARTIDA_BENEFICIO = "beneficio_detpartida";
        String DETPARTIDA_DESCUENTOPROV = "descuentoprov_detpartida";
        String DETPARTIDA_RUTAFOTO = "rutafoto_detpartida";
        String DETPARTIDA_REFPROV = "ref_prov_detpartida";
        String DETPARTIDA_TIPO = "tipo_detpartida";

        String PARTIDABASE_ID_PARTIDA = "id_partidabase";
        String PARTIDABASE_DESCRIPCION = "descripcion_partidabase";
        String PARTIDABASE_NOMBRE = "nombre_partidabase";
        String PARTIDABASE_TIEMPO = "tiempo_partidabase";
        String PARTIDABASE_PRECIO = "precio_partidabase";
        String PARTIDABASE_RUTAFOTO = "rutafoto_partidabase";


        String DETPARTIDABASE_ID_PARTIDA = "id_detpartidabase_partidabase";
        String DETPARTIDABASE_SECUENCIA = "secuencia";
        String DETPARTIDABASE_ID_DETPARTIDA = "id_detpartidabase";
        String DETPARTIDABASE_DESCRIPCION = "descripcion_detpartidabase";
        String DETPARTIDABASE_NOMBRE = "nombre_detpartidabase";
        String DETPARTIDABASE_CANTIDAD = "cantidad_detpartidabase";
        String DETPARTIDABASE_TIEMPO = "tiempo_detpartidabase";
        String DETPARTIDABASE_PRECIO = "precio_detpartidabase";
        String DETPARTIDABASE_BENEFICIO = "beneficio_detpartidabase";
        String DETPARTIDABASE_DESCUENTOPROV = "descuentoprov_detpartidabase";
        String DETPARTIDABASE_RUTAFOTO = "rutafoto_detpartidabase";
        String DETPARTIDABASE_REFPROV = "ref_prov_detpartidabase";
        String DETPARTIDABASE_TIPO = "tipo_detpartidabase";

        String TAREA_ID_TAREA = "id_tarea";
        String TAREA_DESCRIPCION = "descripcion_tarea";
        String TAREA_NOMBRE = "nombre_tarea";
        String TAREA_TIEMPO = "tiempo_tarea";
        String TAREA_RUTAFOTO = "rutafoto_tarea";

        String PRODUCTO_ID_PRODUCTO = "id_producto";
        String PRODUCTO_DESCRIPCION = "descripcion_producto";
        String PRODUCTO_NOMBRE = "nombre_producto";
        String PRODUCTO_IMPORTE = "importe_producto";
        String PRODUCTO_RUTAFOTO = "rutafoto_producto";

        String GASTOFIJO_ID_GASTOFIJO = "id_gastofijo";
        String GASTOFIJO_NOMBRE = "nombre_gastofijo";
        String GASTOFIJO_DESCRIPCION = "descripcion_gastofijo";
        String GASTOFIJO_CANTIDAD = "cantidad_gastofijo";
        String GASTOFIJO_IMPORTE = "importe_gastofijo";
        String GASTOFIJO_ANYOS = "anyos_gastofijo";
        String GASTOFIJO_MESES = "meses_gastofijo";
        String GASTOFIJO_DIAS = "dias_gastofijo";

        String PERFIL_ID_PERFIL = "id_perfil";
        String PERFIL_NOMBRE = "nombre_perfil";
        String PERFIL_DESCRIPCION = "descripcion_perfil";
        String PERFIL_VACACIONES = "vacaciones";
        String PERFIL_HORASLUNES = "hlunes";
        String PERFIL_HORASMARTES = "hmartes";
        String PERFIL_HORASMIERCOLES = "hmiercoles";
        String PERFIL_HORASJUEVES = "hjueves";
        String PERFIL_HORASVIERNES = "hviernes";
        String PERFIL_HORASSABADO = "hsabado";
        String PERFIL_HORASDOMINGO = "hdomingo";
        String PERFIL_BENEFICIO = "beneficio_perfil";
        String PERFIL_SUELDO = "sueldo";

        String AMORTIZACION_ID_AMORTIZACION = "id_amortizacion";
        String AMORTIZACION_NOMBRE = "nombre_amortizacion";
        String AMORTIZACION_DESCRIPCION = "descripcion_amortizacion";
        String AMORTIZACION_CANTIDAD = "cantidad_amortizacion";
        String AMORTIZACION_FECHACOMPRA = "fecha_compra_amortizacion";
        String AMORTIZACION_IMPORTE = "importe_amortizacion";
        String AMORTIZACION_ANYOS = "anyos_amortizacion";
        String AMORTIZACION_MESES = "meses_amortizacion";
        String AMORTIZACION_DIAS = "dias_amortizacion";
        String AMORTIZACION_RUTAFOTO = "rutafoto_amortizacion";

        String EVENTO_ID_EVENTO = "id_evento";
        String EVENTO_IDMULTI = "id_multi";
        String EVENTO_DESCRIPCION = "descripcion_evento";
        String EVENTO_FECHAINIEVENTO = "fechainievento";
        String EVENTO_FECHAFINEVENTO = "fechafinevento";
        String EVENTO_HORAINIEVENTO = "horainievento";
        String EVENTO_HORAFINEVENTO = "horafinevento";
        String EVENTO_AVISO = "aviso_evento";
        String EVENTO_TELEFONO = "telefono_evento";
        String EVENTO_LUGAR = "lugar_evento";
        String EVENTO_EMAIL = "email_evento";
        String EVENTO_PROYECTOREL = "proyectorel";
        String EVENTO_NOMPROYECTOREL = "nomproyectorel";
        String EVENTO_CLIENTEREL = "clienterel";
        String EVENTO_NOMCLIENTEREL = "nomclienterel";
        String EVENTO_TIPOEVENTO = "tipoevento";
        String EVENTO_RUTAFOTO = "rutafoto_evento";
        String EVENTO_COMPLETADA = "completada_evento";

        String TABLAS_ID_TABLA = "id_tabla";
        String TABLAS_TABLA = "tabla";
        String TABLAS_CAMPO = "campo";
        String TABLAS_PARAMETROS = "parametros";


        //REFERENCIAS----------------------------------------------------------

        String ID_PROYECTO = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                TABLA_PROYECTO, PROYECTO_ID_PROYECTO);

        String ID_CLIENTE = String.format("REFERENCES %s(%s)",
                TABLA_CLIENTE, CLIENTE_ID_CLIENTE);

        String ID_TIPOCLIENTE = String.format("REFERENCES %s(%s)",
                TABLA_TIPOCLIENTE, TIPOCLIENTE_ID_TIPOCLIENTE);

        String ID_ESTADO = String.format("REFERENCES %s(%s)",
                TABLA_ESTADO, ESTADO_ID_ESTADO);

        String ID_PRODUCTO = String.format("REFERENCES %s(%s)",
                TABLA_PRODUCTO, PRODUCTO_ID_PRODUCTO);

        String ID_TAREA = String.format("REFERENCES %s(%s)",
                TABLA_TAREA, TAREA_ID_TAREA);

        String ID_PARTIDA = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                TABLA_PARTIDA, PARTIDA_ID_PARTIDA);

        String ID_PARTIDABASE = String.format("REFERENCES %s(%s) ON DELETE CASCADE",
                TABLA_PARTIDABASE, PARTIDABASE_ID_PARTIDA);

        //CAMPOS----------------------------------------------------------------

        String[] CAMPOS_PROYECTO = {"50", TABLA_PROYECTO,
                PROYECTO_ID_PROYECTO, "TEXT NON NULL UNIQUE",STRING,
                PROYECTO_ID_CLIENTE, String.format("TEXT NON NULL %s", ID_CLIENTE),STRING,
                PROYECTO_ID_ESTADO, String.format("TEXT NON NULL %s", ID_ESTADO),STRING,
                PROYECTO_NOMBRE, "TEXT NON NULL",STRING,
                PROYECTO_DESCRIPCION, "TEXT NON NULL",STRING,
                PROYECTO_FECHAENTRADA, "INTEGER NON NULL DEFAULT 0",LONG,
                PROYECTO_FECHAENTREGAPRESUP, "INTEGER NON NULL DEFAULT 0",LONG,
                PROYECTO_FECHAENTREGAACORDADA, "INTEGER NON NULL DEFAULT 0",LONG,
                PROYECTO_FECHAENTREGACALCULADA, "INTEGER NON NULL DEFAULT 0",LONG,
                PROYECTO_FECHAFINAL, "INTEGER NON NULL DEFAULT 0",LONG,
                PROYECTO_RETRASO, "INTEGER NON NULL DEFAULT 0",LONG,
                PROYECTO_IMPORTEPRESUPUESTO, "REAL NON NULL DEFAULT 0",DOUBLE,
                PROYECTO_IMPORTEFINAL, "REAL NON NULL DEFAULT 0",DOUBLE,
                PROYECTO_TIEMPO, "REAL NON NULL DEFAULT 0",DOUBLE,
                PROYECTO_RUTAFOTO, "TEXT ",STRING,
                PROYECTO_TOTCOMPLETADO, "INTEGER NON NULL DEFAULT 0",INT,
                //Campos referencia
                PROYECTO_CLIENTE_NOMBRE, NULL,STRING,
                PROYECTO_CLIENTE_PESOTIPOCLI, NULL,INT,
                PROYECTO_DESCRIPCION_ESTADO, NULL,STRING,
                PROYECTO_TIPOESTADO, NULL,INT
        };

        String[] CAMPOS_CLIENTE = {"26", TABLA_CLIENTE,
                CLIENTE_ID_CLIENTE, "TEXT NON NULL UNIQUE",STRING,
                CLIENTE_ID_TIPOCLIENTE, String.format("TEXT NON NULL %s", ID_TIPOCLIENTE),STRING,
                CLIENTE_NOMBRE, "TEXT NON NULL",STRING,
                CLIENTE_DIRECCION, "TEXT",STRING,
                CLIENTE_TELEFONO, "TEXT",STRING,
                CLIENTE_EMAIL, "TEXT",STRING,
                CLIENTE_CONTACTO, "TEXT",STRING,
                CLIENTE_PESOTIPOCLI, "INTEGER NON NULL DEFAULT 0",INT,
                //Campos referencias
                CLIENTE_TIPOCLIENTEPESO,"",INT,
                CLIENTE_DESCRIPCIONTIPOCLI, "",STRING
        };

        String[] CAMPOS_TIPOCLIENTE = {"11", TABLA_TIPOCLIENTE,
                TIPOCLIENTE_ID_TIPOCLIENTE, "TEXT NON NULL UNIQUE",STRING,
                TIPOCLIENTE_DESCRIPCION, "TEXT NON NULL",STRING,
                TIPOCLIENTE_PESO, "INTEGER NON NULL",INT
        };

        String[] CAMPOS_ESTADO = {"11", TABLA_ESTADO,
                ESTADO_ID_ESTADO, "TEXT NON NULL UNIQUE",STRING,
                ESTADO_DESCRIPCION, "TEXT NON NULL",STRING,
                ESTADO_TIPOESTADO, "INTEGER NON NULL",INT
        };

        String[] CAMPOS_PARTIDA = {"38", TABLA_PARTIDA,
                PARTIDA_ID_PROYECTO, String.format("TEXT NON NULL %s", ID_PROYECTO),STRING,
                PARTIDA_ID_PARTIDA, "TEXT NON NULL UNIQUE",STRING,
                PARTIDA_SECUENCIA, "INTEGER NON NULL",INT,
                PARTIDA_ID_ESTADO, String.format("TEXT NON NULL %s", ID_ESTADO),STRING,
                PARTIDA_DESCRIPCION, "TEXT NON NULL",STRING,
                PARTIDA_NOMBRE, "TEXT NON NULL",STRING,
                PARTIDA_CANTIDAD, "REAL NON NULL DEFAULT 0",DOUBLE,
                PARTIDA_TIEMPO, "REAL NON NULL DEFAULT 0",DOUBLE,
                PARTIDA_PRECIOHORA, "REAL NON NULL DEFAULT 0",DOUBLE,
                PARTIDA_PRECIO, "REAL NON NULL DEFAULT 0",DOUBLE,
                PARTIDA_COMPLETADA, "INTEGER NON NULL DEFAULT 0",INT,
                PARTIDA_RUTAFOTO, "TEXT",STRING,
                //Campos referencias
                PARTIDA_PROYECTO_RETRASO, NULL,LONG,
                PARTIDA_TIPO_ESTADO, NULL,INT
        };

        String[] CAMPOS_DETPARTIDA = {"41", TABLA_DETPARTIDA,
                DETPARTIDA_ID_PARTIDA, String.format("TEXT NON NULL %s", ID_PARTIDA),STRING,
                DETPARTIDA_SECUENCIA, "INTEGER NON NULL",INT,
                DETPARTIDA_ID_DETPARTIDA,"TEXT",STRING,
                DETPARTIDA_NOMBRE,"TEXT NON NULL",STRING,
                DETPARTIDA_DESCRIPCION,"TEXT NON NULL",STRING,
                DETPARTIDA_CANTIDAD, "REAL NON NULL DEFAULT 0",DOUBLE,
                DETPARTIDA_TIEMPO, "REAL NON NULL DEFAULT 0",DOUBLE,
                DETPARTIDA_PRECIO, "REAL NON NULL DEFAULT 0",DOUBLE,
                DETPARTIDA_BENEFICIO, "REAL NON NULL DEFAULT 0",DOUBLE,
                DETPARTIDA_DESCUENTOPROV, "REAL NON NULL DEFAULT 0",DOUBLE,
                DETPARTIDA_RUTAFOTO,"TEXT",STRING,
                DETPARTIDA_REFPROV,"TEXT",STRING,
                DETPARTIDA_TIPO,"TEXT NON NULL",STRING

        };

        String[] CAMPOS_PARTIDABASE = {"20", TABLA_PARTIDABASE,
                PARTIDABASE_ID_PARTIDA, "TEXT NON NULL UNIQUE",STRING,
                PARTIDABASE_DESCRIPCION, "TEXT NON NULL",STRING,
                PARTIDABASE_NOMBRE, "TEXT NON NULL",STRING,
                PARTIDABASE_TIEMPO, "REAL NON NULL DEFAULT 0",DOUBLE,
                PARTIDABASE_PRECIO, "REAL NON NULL DEFAULT 0",DOUBLE,
                PARTIDABASE_RUTAFOTO, "TEXT",STRING
        };

        String[] CAMPOS_DETPARTIDABASE = {"41", TABLA_DETPARTIDABASE,
                DETPARTIDABASE_ID_PARTIDA, String.format("TEXT NON NULL %s", ID_PARTIDABASE),STRING,
                DETPARTIDABASE_SECUENCIA, "INTEGER NON NULL",INT,
                DETPARTIDABASE_ID_DETPARTIDA,"TEXT",STRING,
                DETPARTIDABASE_NOMBRE,"TEXT NON NULL",STRING,
                DETPARTIDABASE_DESCRIPCION,"TEXT NON NULL",STRING,
                DETPARTIDABASE_CANTIDAD, "REAL NON NULL DEFAULT 0",DOUBLE,
                DETPARTIDABASE_TIEMPO, "REAL NON NULL DEFAULT 0",DOUBLE,
                DETPARTIDABASE_PRECIO, "REAL NON NULL DEFAULT 0",DOUBLE,
                DETPARTIDABASE_BENEFICIO, "REAL NON NULL DEFAULT 0",DOUBLE,
                DETPARTIDABASE_DESCUENTOPROV, "REAL NON NULL DEFAULT 0",DOUBLE,
                DETPARTIDABASE_RUTAFOTO,"TEXT",STRING,
                DETPARTIDABASE_REFPROV,"TEXT",STRING,
                DETPARTIDABASE_TIPO,"TEXT NON NULL",STRING

        };

        String[] CAMPOS_TAREA = {"17", TABLA_TAREA,
                TAREA_ID_TAREA, "TEXT NON NULL UNIQUE",STRING,
                TAREA_DESCRIPCION, "TEXT NON NULL",STRING,
                TAREA_NOMBRE, "TEXT NON NULL",STRING,
                TAREA_RUTAFOTO, "TEXT",STRING,
                TAREA_TIEMPO, "REAL NON NULL DEFAULT 0",DOUBLE
        };


        String[] CAMPOS_PRODUCTO = {"17", TABLA_PRODUCTO,
                PRODUCTO_ID_PRODUCTO, "TEXT NON NULL UNIQUE",STRING,
                PRODUCTO_DESCRIPCION, "TEXT NON NULL",STRING,
                PRODUCTO_NOMBRE, "TEXT NON NULL",STRING,
                PRODUCTO_RUTAFOTO, "TEXT",STRING,
                PRODUCTO_IMPORTE, "REAL NON NULL",DOUBLE
        };

        String[] CAMPOS_GASTOFIJO = {"26", TABLA_GASTOFIJO,
                GASTOFIJO_ID_GASTOFIJO, "TEXT NON NULL UNIQUE",STRING,
                GASTOFIJO_NOMBRE, "TEXT NON NULL",STRING,
                GASTOFIJO_DESCRIPCION, "TEXT NON NULL",STRING,
                GASTOFIJO_CANTIDAD, "REAL NON NULL DEFAULT 0",DOUBLE,
                GASTOFIJO_IMPORTE, "REAL NON NULL DEFAULT 0",DOUBLE,
                GASTOFIJO_ANYOS, "INTEGER NON NULL DEFAULT 0",INT,
                GASTOFIJO_MESES, "INTEGER NON NULL DEFAULT 0",INT,
                GASTOFIJO_DIAS, "INTEGER NON NULL DEFAULT 0",INT
        };

        String[] CAMPOS_PERFIL = {"41", TABLA_PERFIL,
                PERFIL_ID_PERFIL, "TEXT NON NULL UNIQUE",STRING,
                PERFIL_NOMBRE, "TEXT NON NULL",STRING,
                PERFIL_DESCRIPCION, "TEXT NON NULL",STRING,
                PERFIL_VACACIONES, "INTEGER NON NULL DEFAULT 30",INT,
                PERFIL_HORASLUNES, "INTEGER NON NULL DEFAULT 8",INT,
                PERFIL_HORASMARTES, "INTEGER NON NULL DEFAULT 8",INT,
                PERFIL_HORASMIERCOLES, "INTEGER NON NULL DEFAULT 8",INT,
                PERFIL_HORASJUEVES, "INTEGER NON NULL DEFAULT 8",INT,
                PERFIL_HORASVIERNES, "INTEGER NON NULL DEFAULT 8",INT,
                PERFIL_HORASSABADO, "INTEGER NON NULL DEFAULT 0",INT,
                PERFIL_HORASDOMINGO, "INTEGER NON NULL DEFAULT 0",INT,
                PERFIL_BENEFICIO, "REAL NON NULL DEFAULT 10",DOUBLE,
                PERFIL_SUELDO, "REAL NON NULL DEFAULT 20000",DOUBLE
        };

        String[] CAMPOS_AMORTIZACION = {"32", TABLA_AMORTIZACION,
                AMORTIZACION_ID_AMORTIZACION, "TEXT NON NULL UNIQUE",STRING,
                AMORTIZACION_NOMBRE, "TEXT NON NULL",STRING,
                AMORTIZACION_DESCRIPCION, "TEXT NON NULL",STRING,
                AMORTIZACION_CANTIDAD, "REAL NON NULL",DOUBLE,
                AMORTIZACION_FECHACOMPRA, "INTEGER NON NULL DEFAULT 0",LONG,
                AMORTIZACION_IMPORTE, "REAL NON NULL DEFAULT 0",DOUBLE,
                AMORTIZACION_ANYOS, "INTEGER NON NULL DEFAULT 0",INT,
                AMORTIZACION_MESES, "INTEGER NON NULL DEFAULT 0",INT,
                AMORTIZACION_DIAS, "INTEGER NON NULL DEFAULT 0",INT,
                AMORTIZACION_RUTAFOTO, "TEXT",STRING
        };

        String[] CAMPOS_EVENTO = {"56", TABLA_EVENTO,
                EVENTO_ID_EVENTO, "TEXT NON NULL UNIQUE",STRING,
                EVENTO_IDMULTI, "TEXT NON NULL",STRING,
                EVENTO_DESCRIPCION, "TEXT NON NULL",STRING,
                EVENTO_FECHAINIEVENTO, "INTEGER NON NULL DEFAULT 0",LONG,
                EVENTO_FECHAFINEVENTO, "INTEGER NON NULL DEFAULT 0",LONG,
                EVENTO_HORAINIEVENTO, "INTEGER NON NULL DEFAULT 0",LONG,
                EVENTO_HORAFINEVENTO, "INTEGER NON NULL DEFAULT 0",LONG,
                EVENTO_AVISO, "INTEGER NON NULL DEFAULT 0",LONG,
                EVENTO_TELEFONO, "TEXT",STRING,
                EVENTO_LUGAR, "TEXT",STRING,
                EVENTO_EMAIL, "TEXT",STRING,
                EVENTO_PROYECTOREL, "TEXT",STRING,
                EVENTO_NOMPROYECTOREL, "TEXT",STRING,
                EVENTO_CLIENTEREL, "TEXT",STRING,
                EVENTO_NOMCLIENTEREL, "TEXT",STRING,
                EVENTO_TIPOEVENTO, "TEXT NON NULL",STRING,
                EVENTO_RUTAFOTO, "TEXT",STRING,
                EVENTO_COMPLETADA, "INTEGER NON NULL DEFAULT 0",INT
        };

    }

    public static final String PARAMETRO_FILTRO = "filtro";
    public static final String FILTRO_CLIENTE = "cliente";
    public static final String FILTRO_TOTAL = "total";
    public static final String FILTRO_FECHA = "fecha";
    public static final String FILTRO_ESTADO = "estado";
    public static final String FILTRO_RETRASO = "retraso";

    public static ArrayList<String[]> obtenerListaCampos(){

        ArrayList<String[]> listaCampos = new ArrayList<>();
        listaCampos.add(Tablas.CAMPOS_PROYECTO);
        listaCampos.add(Tablas.CAMPOS_CLIENTE);
        listaCampos.add(Tablas.CAMPOS_TIPOCLIENTE);
        listaCampos.add(Tablas.CAMPOS_ESTADO);
        listaCampos.add(Tablas.CAMPOS_EVENTO);
        listaCampos.add(Tablas.CAMPOS_PARTIDA);
        listaCampos.add(Tablas.CAMPOS_TAREA);
        listaCampos.add(Tablas.CAMPOS_PRODUCTO);
        listaCampos.add(Tablas.CAMPOS_GASTOFIJO);
        listaCampos.add(Tablas.CAMPOS_AMORTIZACION);
        listaCampos.add(Tablas.CAMPOS_PERFIL);
        listaCampos.add(Tablas.CAMPOS_DETPARTIDA);
        listaCampos.add(Tablas.CAMPOS_PARTIDABASE);
        listaCampos.add(Tablas.CAMPOS_DETPARTIDABASE);

        return listaCampos;
    }

    public static Uri obtenerUriContenido(String tabla){

        return URI_BASE.buildUpon().appendPath(tabla).build();
    }

    public static Uri crearUriTabla(String id, String tabla){

        Uri URI_CONTENIDO = obtenerUriContenido(tabla);

        return URI_CONTENIDO.buildUpon().appendPath(id).build();
    }

    public static Uri crearUriTablaDetalle(String id, String secuencia, String tabla) {
        // Uri de la forma 'gasto/:id#:secuencia'
        Uri URI_CONTENIDO = obtenerUriContenido(tabla);
        return URI_CONTENIDO.buildUpon()
                .appendPath(String.format("%s#%s", id, secuencia))
                .build();
    }

    public static Uri crearUriTablaDetalle(String id, int secuencia, String tabla) {
        // Uri de la forma 'gasto/:id#:secuencia'
        Uri URI_CONTENIDO = obtenerUriContenido(tabla);
        return URI_CONTENIDO.buildUpon()
                .appendPath(String.format("%s#%s", id, String.valueOf(secuencia)))
                .build();
    }

    public static Uri crearUriTablaDetalleId(String id, String tabla, String tablaCab){

        Uri URI_CONTENIDO = obtenerUriContenido(tablaCab);
        return URI_CONTENIDO.buildUpon().appendPath(id).appendPath(tabla).build();

    }

    public static String obtenerIdTablaDetalleId(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    public static String[] obtenerIdTablaDetalle(Uri uri) {
        return uri.getLastPathSegment().split("#");
    }

    public static String generarIdTabla(String tabla){
        return tabla + UUID.randomUUID().toString();
    }

    public static String obtenerIdTabla(Uri uri){
        return uri.getLastPathSegment();
    }

    public static boolean tieneFiltro(Uri uri){
        return uri != null && uri.getQueryParameter(PARAMETRO_FILTRO) != null;
    }


    public static String generarMime(String id) {
        if (id != null) {
            return TIPO_CONTENIDO + id;
        } else {
            return null;
        }
    }

    public static String generarMimeItem(String id) {
        if (id != null) {
            return TIPO_CONTENIDO_ITEM + id;
        } else {
            return null;
        }
    }

    private ContratoPry() {
    }
}
