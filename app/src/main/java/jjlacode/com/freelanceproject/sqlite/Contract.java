package jjlacode.com.freelanceproject.sqlite;

import android.net.Uri;

import java.util.ArrayList;
import java.util.UUID;

import jjlacode.com.androidutils.JavaUtil;


public class Contract implements JavaUtil.Constantes {

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
        String TABLA_GASTO = "gasto";
        String TABLA_PERFIL = "perfil";
        String TABLA_AMORTIZACION = "amortizacion";
        String TABLA_TAREA = "tarea";
        String TABLA_PRODUCTO = "producto";
        String TABLA_GASTOFIJO = "gastofijo";
        String TABLA_EVENTO = "evento";
        String TABLA_TABLAS = "tablas";

        //COLUMNAS--------------------------------------------------------

        String PROYECTO_ID_PROYECTO = "id_proyecto";
        String PROYECTO_ID_CLIENTE = "id_cliente";
        String PROYECTO_ID_ESTADO = "id_estado";
        String PROYECTO_NOMBRE = "nombre_proyecto";
        String PROYECTO_DESCRIPCION = "descripcion_proyecto";
        String PROYECTO_FECHAENTRADA = "fechaentrada";
        String PROYECTO_FECHAENTREGAPRESUP = "fechaentregapresup";
        String PROYECTO_FECHAENTREGAACORDADA = "fechaentregaacordada";
        String PROYECTO_FECHAENTREGACALCULADA = "fechaentregacalculada";
        String PROYECTO_FECHAFINAL = "fechafinal";
        String PROYECTO_RETRASO = "retraso";
        String PROYECTO_IMPORTEPRESUPUESTO = "importepresupuesto";
        String PROYECTO_IMPORTEFINAL = "importefinal";
        String PROYECTO_RUTAFOTO = "rutafoto";
        String PROYECTO_TOTCOMPLETADO = "totcomplet";
        String PROYECTO_CLIENTE_NOMBRE = "nombre_cliente";
        String PROYECTO_CLIENTE_PESOTIPOCLI = "pesotipocli";
        String PROYECTO_DESCRIPCION_ESTADO = "descripcion_estado";
        String PROYECTO_TIPOESTADO = "tipo_estado";

        String CLIENTE_ID_CLIENTE = "id_cliente";
        String CLIENTE_ID_TIPOCLIENTE = "id_tipocliente";
        String CLIENTE_NOMBRE = "nombre_cliente";
        String CLIENTE_DIRECCION = "direccion";
        String CLIENTE_TELEFONO = "telefono";
        String CLIENTE_EMAIL = "email";
        String CLIENTE_CONTACTO = "contacto";
        String CLIENTE_PESOTIPOCLI = "pesotipocli";
        String CLIENTE_TIPOCLIENTEPESO = "peso";
        String CLIENTE_DESCRIPCIONTIPOCLI = "descripcion_tipo_cliente";

        String TIPOCLIENTE_ID_TIPOCLIENTE = "id_tipocliente";
        String TIPOCLIENTE_DESCRIPCION = "descripcion_tipo_cliente";
        String TIPOCLIENTE_PESO = "peso";

        String ESTADO_ID_ESTADO = "id_estado";
        String ESTADO_DESCRIPCION = "descripcion_estado";
        String ESTADO_TIPOESTADO = "tipo_estado";

        String PARTIDA_ID_PROYECTO = "id_proyecto";
        String PARTIDA_SECUENCIA = "secuencia";
        String PARTIDA_ID_ESTADO = "id_estado";
        String PARTIDA_ID_TAREA = "id_tarea";
        String PARTIDA_DESCRIPCION = "descripcion_partida";
        String PARTIDA_CANTIDAD = "cantidad";
        String PARTIDA_TIEMPO = "tiempo";
        String PARTIDA_COMPLETADA = "completada";
        String PARTIDA_PROYECTO_RETRASO = "retraso";
        String PARTIDA_TIPO_ESTADO = "tipo_estado";

        String TAREA_ID_TAREA = "id_tarea";
        String TAREA_DESCRIPCION = "descripcion_tarea";
        String TAREA_TIEMPO = "tiempo";

        String GASTO_ID_PROYECTO = "id_proyecto";
        String GASTO_SECUENCIA = "secuencia";
        String GASTO_ID_PRODUCTO = "id_producto";
        String GASTO_DESCRIPCION = "descripcion_gasto";
        String GASTO_CANTIDAD = "cantidad";
        String GASTO_IMPORTE = "importe";
        String GASTO_BENEFICIO = "beneficio";

        String PRODUCTO_ID_PRODUCTO = "id_producto";
        String PRODUCTO_DESCRIPCION = "descripcion_producto";
        String PRODUCTO_IMPORTE = "importe";

        String GASTOFIJO_ID_GASTOFIJO = "id_gastofijo";
        String GASTOFIJO_DESCRIPCION = "descripcion";
        String GASTOFIJO_CANTIDAD = "cantidad";
        String GASTOFIJO_IMPORTE = "importe";
        String GASTOFIJO_ANYOS = "anyos";
        String GASTOFIJO_MESES = "meses";
        String GASTOFIJO_DIAS = "dias";

        String PERFIL_ID_PERFIL = "id_perfil";
        String PERFIL_NOMBRE = "nombre_perfil";
        String PERFIL_VACACIONES = "vacaciones";
        String PERFIL_HORASLUNES = "hlunes";
        String PERFIL_HORASMARTES = "hmartes";
        String PERFIL_HORASMIERCOLES = "hmiercoles";
        String PERFIL_HORASJUEVES = "hjueves";
        String PERFIL_HORASVIERNES = "hviernes";
        String PERFIL_HORASSABADO = "hsabado";
        String PERFIL_HORASDOMINGO = "hdomingo";
        String PERFIL_BENEFICIO = "beneficio";

        String AMORTIZACION_ID_AMORTIZACION = "id_amortizacion";
        String AMORTIZACION_DESCRIPCION = "descripcion";
        String AMORTIZACION_CANTIDAD = "cantidad";
        String AMORTIZACION_FECHACOMPRA = "fecha_compra";
        String AMORTIZACION_IMPORTE = "importe";
        String AMORTIZACION_ANYOS = "anyos";
        String AMORTIZACION_MESES = "meses";
        String AMORTIZACION_DIAS = "dias";

        String EVENTO_ID_EVENTO = "id_evento";
        String EVENTO_IDMULTI = "id_multi";
        String EVENTO_DESCRIPCION = "descripcion";
        String EVENTO_FECHAINIEVENTO = "fechainievento";
        String EVENTO_FECHAFINEVENTO = "fechafinevento";
        String EVENTO_HORAINIEVENTO = "horainievento";
        String EVENTO_HORAFINEVENTO = "horafinevento";
        String EVENTO_AVISO = "aviso";
        String EVENTO_TELEFONO = "telefono";
        String EVENTO_LUGAR = "lugar";
        String EVENTO_EMAIL = "email";
        String EVENTO_PROYECTOREL = "proyectorel";
        String EVENTO_NOMPROYECTOREL = "nomproyectorel";
        String EVENTO_CLIENTEREL = "clienterel";
        String EVENTO_NOMCLIENTEREL = "nomclienterel";
        String EVENTO_TIPOEVENTO = "tipoevento";
        String EVENTO_RUTAFOTO = "rutafoto";
        String EVENTO_COMPLETADA = "completada";

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

        //CAMPOS----------------------------------------------------------------

        String[] CAMPOS_PROYECTO = {"47", TABLA_PROYECTO,
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

        String[] CAMPOS_PARTIDA = {"26", TABLA_PARTIDA,
                PARTIDA_ID_PROYECTO, String.format("TEXT NON NULL %s", ID_PROYECTO),STRING,
                PARTIDA_SECUENCIA, "INTEGER NON NULL",INT,
                PARTIDA_ID_ESTADO, String.format("TEXT NON NULL %s", ID_ESTADO),STRING,
                PARTIDA_ID_TAREA, "TEXT NON NULL",STRING,
                PARTIDA_DESCRIPCION, "TEXT NON NULL",STRING,
                PARTIDA_CANTIDAD, "REAL NON NULL DEFAULT 0",DOUBLE,
                PARTIDA_TIEMPO, "REAL NON NULL DEFAULT 0",DOUBLE,
                PARTIDA_COMPLETADA, "INTEGER NON NULL DEFAULT 0",INT,
                //Campos referencias
                PARTIDA_PROYECTO_RETRASO, NULL,LONG,
                PARTIDA_TIPO_ESTADO, NULL,INT
        };

        String[] CAMPOS_TAREA = {"11", TABLA_TAREA,
                TAREA_ID_TAREA, "TEXT NON NULL UNIQUE",STRING,
                TAREA_DESCRIPCION, "TEXT NON NULL",STRING,
                TAREA_TIEMPO, "REAL NON NULL DEFAULT 0",DOUBLE
        };

        String[] CAMPOS_GASTO = {"23", TABLA_GASTO,
                GASTO_ID_PROYECTO, "TEXT NON NULL ",STRING,
                GASTO_SECUENCIA, "INTEGER NON NULL",INT,
                GASTO_ID_PRODUCTO, "TEXT NON NULL",STRING,
                GASTO_DESCRIPCION, "TEXT NON NULL",STRING,
                GASTO_CANTIDAD, "REAL NON NULL DEFAULT 0",DOUBLE,
                GASTO_IMPORTE, "REAL NON NULL DEFAULT 0",DOUBLE,
                GASTO_BENEFICIO, "NON NULL DEFAULT 0",DOUBLE
        };

        String[] CAMPOS_PRODUCTO = {"11", TABLA_PRODUCTO,
                PRODUCTO_ID_PRODUCTO, "TEXT NON NULL UNIQUE",STRING,
                PRODUCTO_DESCRIPCION, "TEXT NON NULL",STRING,
                PRODUCTO_IMPORTE, "REAL NON NULL",DOUBLE
        };

        String[] CAMPOS_GASTOFIJO = {"23", TABLA_GASTOFIJO,
                GASTOFIJO_ID_GASTOFIJO, "TEXT NON NULL UNIQUE",STRING,
                GASTOFIJO_DESCRIPCION, "TEXT NON NULL",STRING,
                GASTOFIJO_CANTIDAD, "REAL NON NULL DEFAULT 0",DOUBLE,
                GASTOFIJO_IMPORTE, "REAL NON NULL DEFAULT 0",DOUBLE,
                GASTOFIJO_ANYOS, "INTEGER NON NULL DEFAULT 0",INT,
                GASTOFIJO_MESES, "INTEGER NON NULL DEFAULT 0",INT,
                GASTOFIJO_DIAS, "INTEGER NON NULL DEFAULT 0",INT
        };

        String[] CAMPOS_PERFIL = {"35", TABLA_PERFIL,
                PERFIL_ID_PERFIL, "TEXT NON NULL UNIQUE",STRING,
                PERFIL_NOMBRE, "TEXT NON NULL",STRING,
                PERFIL_VACACIONES, "INTEGER NON NULL DEFAULT 30",INT,
                PERFIL_HORASLUNES, "INTEGER NON NULL DEFAULT 8",INT,
                PERFIL_HORASMARTES, "INTEGER NON NULL DEFAULT 8",INT,
                PERFIL_HORASMIERCOLES, "INTEGER NON NULL DEFAULT 8",INT,
                PERFIL_HORASJUEVES, "INTEGER NON NULL DEFAULT 8",INT,
                PERFIL_HORASVIERNES, "INTEGER NON NULL DEFAULT 8",INT,
                PERFIL_HORASSABADO, "INTEGER NON NULL DEFAULT 0",INT,
                PERFIL_HORASDOMINGO, "INTEGER NON NULL DEFAULT 0",INT,
                PERFIL_BENEFICIO, "REAL NON NULL DEFAULT 10",DOUBLE
        };

        String[] CAMPOS_AMORTIZACION = {"26", TABLA_AMORTIZACION,
                AMORTIZACION_ID_AMORTIZACION, "TEXT NON NULL UNIQUE",STRING,
                AMORTIZACION_DESCRIPCION, "TEXT NON NULL",STRING,
                AMORTIZACION_CANTIDAD, "REAL NON NULL",DOUBLE,
                AMORTIZACION_FECHACOMPRA, "INTEGER NON NULL DEFAULT 0",LONG,
                AMORTIZACION_IMPORTE, "REAL NON NULL DEFAULT 0",DOUBLE,
                AMORTIZACION_ANYOS, "INTEGER NON NULL DEFAULT 0",INT,
                AMORTIZACION_MESES, "INTEGER NON NULL DEFAULT 0",INT,
                AMORTIZACION_DIAS, "INTEGER NON NULL DEFAULT 0",INT
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
            listaCampos.add(Tablas.CAMPOS_GASTO);
            listaCampos.add(Tablas.CAMPOS_PRODUCTO);
            listaCampos.add(Tablas.CAMPOS_GASTOFIJO);
            listaCampos.add(Tablas.CAMPOS_AMORTIZACION);
            listaCampos.add(Tablas.CAMPOS_PERFIL);

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

    private Contract() {
    }
}
