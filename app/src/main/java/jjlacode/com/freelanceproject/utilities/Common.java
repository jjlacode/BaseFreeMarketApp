package jjlacode.com.freelanceproject.utilities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.BuildConfig;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.sqlite.QueryDB;

import static jjlacode.com.androidutils.AppActivity.getAppContext;


public class Common implements JavaUtil.Constantes, Contract.Tablas {


        public static String perfila = null;//Perfil activo para calculos y preferencias
        public static int prioridad;
        public static double hora;
        public static double beneficio;
        public static boolean permiso;
        public static String namefsub;
        public static String idProyecto;
        public static String agendaTemp;


    public interface  Constantes {

        String NUEVOPRESUPUESTO = getAppContext().getString(R.string.nuevo_presupuesto);
        String NUEVOPROYECTO = getAppContext().getString(R.string.nuevo_proyecto);
        String CLIENTE = getAppContext().getString(R.string.cliente);
        String CLIENTES = getAppContext().getString(R.string.clientes);
        String PROSPECTO = "prospecto";
        String PROSPECTOS = getAppContext().getString(R.string.prospectos);
        String PRESUPUESTO = "presupuesto";
        String PRESUPUESTOS = getAppContext().getString(R.string.presupuestos);
        String PROYECTO = "proyecto";
        String PROYECTOS = getAppContext().getString(R.string.proyectos);
        String COBROS = "cobros";
        String HISTORICO = "historico";
        String AGENDA = "agenda";
        String PERFIL = "perfil";
        String PREFERENCIAS = "preferencias";
        String PARTIDA = getAppContext().getString(R.string.partida);
        String PARTIDAS = getAppContext().getString(R.string.partidas);
        String GASTO = getAppContext().getString(R.string.gasto);
        String GASTOS = getAppContext().getString(R.string.gastos);
        String HABITUAL = getAppContext().getString(R.string.habitual);
        String NUEVO = getAppContext().getString(R.string.nuevo);
        String OCASIONAL = getAppContext().getString(R.string.ocasional);
        String PRINCIPAL = getAppContext().getString(R.string.principal);
        String PRODUCTOPERSONALIZADO = getAppContext().getString(R.string.producto_personalizado);
        String TAREAPERSONALIZADA = getAppContext().getString(R.string.tarea_personalizada);
        int COD_SELECCIONA = 10;
        int COD_FOTO = 20;
        String CARPETA_PRINCIPAL = "freelanceproyect/";
        String CARPETA_IMAGEN = "imagenes";
        String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;
        String PROVIDERFILE = BuildConfig.APPLICATION_ID + ".providerFreelanceProject";

    }

    public interface TiposEstados{

        int TNUEVOPRESUP = 1;
        int TPRESUPPENDENTREGA = 2;
        int TPRESUPESPERA = 3;
        int TPRESUPACEPTADO = 4;
        int TPROYECTEJECUCION = 5;
        int TPROYECPENDENTREGA = 6;
        int TPROYECTPENDCOBRO = 7;
        int TPROYECTCOBRADO = 8;
        int TPRESUPNOACEPTADO = 0;
    }

    public interface Estados{

        String NUEVOPRESUP = getAppContext().getString(R.string.nuevo_presupuesto_est);
        String PRESUPPENDENTREGA = getAppContext().getString(R.string.presupuesto_pendiente_entrega_est);
        String PRESUPESPERA = getAppContext().getString(R.string.presupuesto_en_espera_aceptar_est);
        String PRESUPACEPTADO = getAppContext().getString(R.string.presupuesto_aceptado_pendiente_ejecucion_est);
        String PROYECTEJECUCION = getAppContext().getString(R.string.proyecto_en_ejecucion_est);
        String PROYECPENDENTREGA = getAppContext().getString(R.string.proyecto_pendiente_entrega_est);
        String PROYECTPENDCOBRO = getAppContext().getString(R.string.proyecto_entregado_pendiente_cobro_est);
        String PROYECTCOBRADO = getAppContext().getString(R.string.proyecto_cobrado_est);
        String PRESUPNOACEPTADO = getAppContext().getString(R.string.presupuesto_no_aceptado_est);
    }

    public  interface TiposEvento {

        String TAREA = "tarea";
        String CITA = "cita";
        String LLAMADA = "llamada";
        String EMAIL = "email";
        String EVENTO = "evento";
    }


    public static long fechaEntregaCalculada(double horastrabajos, double hlunes, double hmartes,
                                             double hmiercoles, double hjueves, double hviernes,
                                             double hsabado, double hdomingo){

        TimeZone timezone = TimeZone.getDefault();
        Calendar calendar = new GregorianCalendar(timezone);
        int diahoy= calendar.get(Calendar.DAY_OF_WEEK)-1;
        long totaldias=0;
        while (horastrabajos>1){

            switch (diahoy){
                case 1: horastrabajos-= hmartes;totaldias++;if (horastrabajos<1){if (hmartes<=0){horastrabajos++;}else{break;}}
                case 2: horastrabajos-= hmiercoles;totaldias++;if (horastrabajos<1){if (hmiercoles<=0){horastrabajos++;}else{break;}}
                case 3: horastrabajos-= hjueves;totaldias++;if (horastrabajos<1){if (hjueves<=0){horastrabajos++;}else{break;}}
                case 4: horastrabajos-= hviernes;totaldias++;if (horastrabajos<1){if (hviernes<=0){horastrabajos++;}else{break;}}
                case 5: horastrabajos-= hsabado;totaldias++;if (horastrabajos<1){if (hsabado<=0){horastrabajos++;}else{break;}}
                case 6: horastrabajos-= hdomingo;totaldias++;if (horastrabajos<1){if (hdomingo<=0){horastrabajos++;}else{break;}}
                case 7: horastrabajos-= hlunes;totaldias++;if (horastrabajos<1){if (hlunes<=0){horastrabajos++;}}
            }diahoy=1;
        }
        long fechahoy = calendar.getTimeInMillis();
        return fechahoy + (totaldias*24*60*60*1000);

    }

    public static String getRealPathFromURI(Uri contentUri, ContentResolver resolver) {

        Cursor cursor = null;
        try {

            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = resolver.query(contentUri, proj, null, null, null);
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            return cursor.getString(column_index);
        } finally {

            if (cursor != null) {

                cursor.close();
            }
        }
    }




    public static class Calculos implements  Constantes{

        static ContentResolver resolver = getAppContext().getContentResolver();

        public static double calculoPrecioHora() {

            ArrayList<Modelo> listaAmortizaciones = QueryDB.queryList(CAMPOS_AMORTIZACION);

            ArrayList<Modelo> listaGastosFijos = QueryDB.queryList(CAMPOS_GASTOFIJO);

            long hoy = JavaUtil.hoy();
            double precioHoraAmortizaciones = 0;

            for (Modelo amortizacion : listaAmortizaciones) {

                Long fecha = amortizacion.getLong(AMORTIZACION_FECHACOMPRA);

                long horas = (amortizacion.getInt(AMORTIZACION_ANYOS) * 365 * 24) +
                        (amortizacion.getInt(AMORTIZACION_MESES) * 30 * 24) +
                        (amortizacion.getInt(AMORTIZACION_DIAS) * 24);
                if (fecha + (horas * 60 * 60 * 1000) > hoy) {

                    precioHoraAmortizaciones += amortizacion.getDouble(AMORTIZACION_IMPORTE) / horas;

                }
            }

            double precioHoraGastosFijos = 0;

            for (Modelo gastoFijo : listaGastosFijos) {

                int horas = (gastoFijo.getInt(GASTOFIJO_ANYOS) * 365 * 24) +
                        (gastoFijo.getInt(GASTOFIJO_MESES) * 30 * 24) +
                        (gastoFijo.getInt(GASTOFIJO_DIAS) * 24);

                precioHoraGastosFijos += gastoFijo.getDouble(GASTOFIJO_IMPORTE) / horas;
            }

            double totalAmortizacionesYGastos = (precioHoraAmortizaciones + precioHoraGastosFijos) * 24 * 365;

            String seleccion = PERFIL_NOMBRE + " = '" + perfila + "'";

            ArrayList<Modelo> listaPerfiles = QueryDB.queryList(CAMPOS_PERFIL,seleccion, null);

            Modelo perfil = listaPerfiles.get(0);

            double beneficio = perfil.getDouble(PERFIL_BENEFICIO);
            double totHoras = perfil.getDouble(PERFIL_HORASLUNES) +
                    perfil.getDouble(PERFIL_HORASMARTES) +
                    perfil.getDouble(PERFIL_HORASMIERCOLES) +
                    perfil.getDouble(PERFIL_HORASJUEVES) +
                    perfil.getDouble(PERFIL_HORASVIERNES) +
                    perfil.getDouble(PERFIL_HORASSABADO) +
                    perfil.getDouble(PERFIL_HORASDOMINGO);

            int semanas = JavaUtil.semanasAnio();
            double horasTrabajadasAnyo = semanas * totHoras;
            double horasVacacionesARestar = Math.round(perfil.getDouble(PERFIL_VACACIONES) / 7) * totHoras;
            double base = totalAmortizacionesYGastos / (horasTrabajadasAnyo - horasVacacionesARestar);
            return (base + ((base / 100) * beneficio));
        }

        public static void recalcularFechas() {

            String seleccionPerfil = PERFIL_NOMBRE + " = '"+perfila+"'";

            ArrayList<Modelo> listaPerfiles = QueryDB.queryList(CAMPOS_PERFIL,seleccionPerfil, null);

            Modelo perfilActivo = listaPerfiles.get(0);

            String ordenProyectosFecha = PROYECTO_FECHAENTRADA + " ASC";

            ArrayList<Modelo> listaProyectos = QueryDB.queryList(CAMPOS_PROYECTO,null, ordenProyectosFecha);

            for (int i = 0; i < listaProyectos.size(); i++) {

                double horasTrabajoPendientes = 0;

                Modelo proyecto = listaProyectos.get(i);

                if (proyecto.getInt(PROYECTO_TIPOESTADO) != 0) {

                    if (proyecto.getInt(PROYECTO_TIPOESTADO) > 6) {

                        if (proyecto.getLong(PROYECTO_FECHAFINAL) == 0) {

                            ContentValues valores = new ContentValues();
                            valores.put(PROYECTO_FECHAFINAL, JavaUtil.hoy());

                            resolver.update(Contract.crearUriTabla(proyecto.getString(PROYECTO_ID_PROYECTO), TABLA_PROYECTO),
                                    valores, null, null);

                        }

                    } else {

                        for (int x = i; x < listaProyectos.size(); x++) {

                            Modelo proyecto2 = listaProyectos.get(x);

                            if ((proyecto2.getInt(PROYECTO_CLIENTE_PESOTIPOCLI) >=
                                    proyecto.getInt(PROYECTO_CLIENTE_PESOTIPOCLI))|| (prioridad == 0)) {

                                if (proyecto.getInt(PROYECTO_TIPOESTADO) < 4) {


                                    ArrayList<Modelo> listaPartidas = QueryDB.queryListDetalle
                                            (CAMPOS_PARTIDA,proyecto2.getString(PARTIDA_ID_PROYECTO), TABLA_PROYECTO);

                                    for (int y = 0; y < listaPartidas.size(); y++) {

                                        Modelo partida = listaPartidas.get(y);


                                        double horasrealizadas =
                                                ((partida.getDouble(PARTIDA_TIEMPO)
                                                        * partida.getDouble(PARTIDA_CANTIDAD)) / 100)
                                                        * partida.getInt(PARTIDA_COMPLETADA);

                                        horasTrabajoPendientes += (partida.getDouble(PARTIDA_TIEMPO)
                                                * partida.getDouble(PARTIDA_CANTIDAD)) - horasrealizadas;


                                    }
                                } else if (proyecto.getInt(PROYECTO_TIPOESTADO) > 3) {

                                    if (proyecto2.getInt(PROYECTO_TIPOESTADO) > 3) {

                                        ArrayList<Modelo> listaPartidas = QueryDB.queryListDetalle
                                                (CAMPOS_PARTIDA,proyecto2.getString(PROYECTO_ID_PROYECTO),TABLA_PROYECTO);

                                        for (int y = 0; y < listaPartidas.size(); y++) {

                                            Modelo partida = listaPartidas.get(y);

                                            double horasrealizadas = ((partida.getDouble(PARTIDA_TIEMPO) *
                                                    partida.getDouble(PARTIDA_CANTIDAD)) / 100)
                                                    * partida.getInt(PARTIDA_COMPLETADA);

                                            horasTrabajoPendientes += (partida.getDouble(PARTIDA_TIEMPO)
                                                    * partida.getDouble(PARTIDA_CANTIDAD)) - horasrealizadas;


                                        }
                                    }
                                }

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
                        valores.put(PROYECTO_FECHAENTREGACALCULADA, fecha);

                        resolver.update(Contract.crearUriTabla(proyecto.getString(PROYECTO_ID_PROYECTO), TABLA_PROYECTO),
                                valores, null, null);

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

            ArrayList<Modelo> listaTipoCliente = QueryDB.queryList
                    (CAMPOS_TIPOCLIENTE,null, null);

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

            ArrayList<Modelo> listaProyectos = QueryDB.queryList
                    (CAMPOS_PROYECTO,null, null);

            ArrayList<Modelo> lista = QueryDB.queryList
                    (CAMPOS_CLIENTE,null,null);

            Modelo cliente= null;
            for (Modelo item : lista) {

                if (item.getString(CLIENTE_ID_CLIENTE).equals(idCliente)){
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

                cliente.setCampos(CLIENTE_ID_TIPOCLIENTE,idPrincipal);
                cliente.setCampos(CLIENTE_PESOTIPOCLI,(String.valueOf(pesoPrincipal)));
                modificado = true;

            } else if (ratioCliente >= 10 && !cliente.getString(CLIENTE_ID_TIPOCLIENTE).equals(idHabitual) &&
                    listaProyectos.size() > 10) {

                cliente.setCampos(CLIENTE_ID_TIPOCLIENTE,idHabitual);
                cliente.setCampos(CLIENTE_PESOTIPOCLI,(String.valueOf(pesoHabitual)));
                modificado = true;

            } else if (ratioCliente >= 3 && totcli > 1 && listaProyectos.size() > 10
                    && !cliente.getString(CLIENTE_ID_TIPOCLIENTE).equals(idOcasional)) {

                cliente.setCampos(CLIENTE_ID_TIPOCLIENTE,idOcasional);
                cliente.setCampos(CLIENTE_PESOTIPOCLI,(String.valueOf(pesoOcasional)));
                modificado = true;

            } else if (totcli == 1 && !cliente.getString(CLIENTE_ID_TIPOCLIENTE).equals(idNuevo) &&
                    estadoUltimoProyecto >= TiposEstados.TPRESUPACEPTADO) {

                cliente.setCampos(CLIENTE_ID_TIPOCLIENTE,idNuevo);
                cliente.setCampos(CLIENTE_PESOTIPOCLI,(String.valueOf(pesoNuevo)));
                modificado = true;

            }

            if (modificado && cliente.getString(CLIENTE_ID_TIPOCLIENTE) != null) {
                ContentValues valores = new ContentValues();
                valores.put(CLIENTE_ID_TIPOCLIENTE, cliente.getString(CLIENTE_ID_TIPOCLIENTE));
                valores.put(CLIENTE_PESOTIPOCLI, cliente.getInt(CLIENTE_PESOTIPOCLI));

                resolver.update(Contract.crearUriTabla
                        (cliente.getString(CLIENTE_ID_CLIENTE), TABLA_CLIENTE),
                        valores, null, null);
            }

        }

        public static class Tareafechas extends AsyncTask<Void, Float, Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {

                recalcularFechas();
                return null;
            }

            protected void onPreExecute() {
            }

            protected void onProgressUpdate(Float... valores) {
            }

            protected void onPostExecute(Integer bytes) {
            }
        }

        public static class TareaTipoCliente extends AsyncTask<String, Float, Integer> {

            @Override
            protected Integer doInBackground(String... strings) {

                calcularTipoCliente(strings[0]);
                return null;
            }

            protected void onPreExecute() {
            }

            protected void onProgressUpdate(Float... valores) {
            }

            protected void onPostExecute(Integer bytes) {
            }
        }

    }


}
