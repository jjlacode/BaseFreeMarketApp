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
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;

import static jjlacode.com.androidutils.AppActivity.getAppContext;


public class CommonPry implements JavaUtil.Constantes, ContratoPry.Tablas {


        public static String perfila = null;//Perfil activo para calculos y preferencias
        public static boolean prioridad;
        public static int diaspasados;
        public static int diasfuturos;
        public static double hora;
        public static double beneficio;
        public static boolean permiso;
        public static String namefsub;
        public static String idProyecto;
        public static String agendaTemp;

    private static ConsultaBD consulta = new ConsultaBD();


    public interface  Constantes {

        String PRIORIDAD = "prioridad";
        String PREFERENCIAS = "preferencias";
        String DIASPASADOS = "diaspasados";
        String DIASFUTUROS = "diasfuturos";
        String BASEDATOS = "freelanceproject.db";
        String PERFILACTIVO = "perfil activo";
        String PARTIDABASE = "partidabase";
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
        String AMORTIZACION = "amortizacion";
        String GASTOSFIJOS = "gastos fijos";
        String PARTIDA = getAppContext().getString(R.string.partida);
        String PARTIDAS = getAppContext().getString(R.string.partidas);
        String GASTO = getAppContext().getString(R.string.gasto);
        String GASTOS = getAppContext().getString(R.string.gastos);
        String HABITUAL = getAppContext().getString(R.string.habitual);
        String NUEVO = getAppContext().getString(R.string.nuevo);
        String OCASIONAL = getAppContext().getString(R.string.ocasional);
        String PRINCIPAL = getAppContext().getString(R.string.principal);
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

        String TAREA = "Tarea";
        String CITA = "Cita";
        String LLAMADA = "Llamada";
        String EMAIL = "Email";
        String EVENTO = "Evento";
    }

    public interface TiposDetPartida{

        String TIPOTAREA = "tarea";
        String TIPOPRODUCTO = "producto";
        String TIPOPRODUCTOPROV = "productoprov";
        String TIPOPARTIDA = "partida";
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


        public static double calculoPrecioHora() {

            ArrayList<Modelo> listaAmortizaciones = consulta.queryList(CAMPOS_AMORTIZACION);

            ArrayList<Modelo> listaGastosFijos = consulta.queryList(CAMPOS_GASTOFIJO);

            long hoy = JavaUtil.hoy();
            double precioHoraAmortizaciones = 0;

            for (Modelo amortizacion : listaAmortizaciones) {

                long fecha = amortizacion.getLong(AMORTIZACION_FECHACOMPRA);

                long horas = (amortizacion.getInt(AMORTIZACION_ANYOS) * 365 * 24) +
                        (amortizacion.getInt(AMORTIZACION_MESES) * 30 * 24) +
                        (amortizacion.getInt(AMORTIZACION_DIAS) * 24);
                if (fecha + (horas * 60 * 60 * 1000) > hoy) {

                    precioHoraAmortizaciones += amortizacion.getDouble(AMORTIZACION_IMPORTE) / (double) horas;

                }
            }

            double precioHoraGastosFijos = 0;

            for (Modelo gastoFijo : listaGastosFijos) {

                long horas = (gastoFijo.getInt(GASTOFIJO_ANYOS) * 365 * 24) +
                        (gastoFijo.getInt(GASTOFIJO_MESES) * 30 * 24) +
                        (gastoFijo.getInt(GASTOFIJO_DIAS) * 24);

                precioHoraGastosFijos += gastoFijo.getDouble(GASTOFIJO_IMPORTE) / (double) horas;
            }

            double totalAmortizacionesYGastos = (precioHoraAmortizaciones + precioHoraGastosFijos) * 24 * 365;

            Modelo perfil = consulta.queryObject(CAMPOS_PERFIL,PERFIL_NOMBRE,perfila,null,IGUAL,null);


            double beneficio = perfil.getDouble(PERFIL_BENEFICIO);
            double totHoras = perfil.getDouble(PERFIL_HORASLUNES) +
                    perfil.getDouble(PERFIL_HORASMARTES) +
                    perfil.getDouble(PERFIL_HORASMIERCOLES) +
                    perfil.getDouble(PERFIL_HORASJUEVES) +
                    perfil.getDouble(PERFIL_HORASVIERNES) +
                    perfil.getDouble(PERFIL_HORASSABADO) +
                    perfil.getDouble(PERFIL_HORASDOMINGO);

            totalAmortizacionesYGastos+= perfil.getDouble(PERFIL_SUELDO);

            int semanas = JavaUtil.semanasAnio();
            double horasTrabajadasAnyo = semanas * totHoras;
            double horasVacacionesARestar = Math.round(perfil.getDouble(PERFIL_VACACIONES) / 7) * totHoras;
            double base = totalAmortizacionesYGastos / (horasTrabajadasAnyo - horasVacacionesARestar);
            return (base + ((base / 100) * beneficio));
        }

        public static void recalcularFechas() {

            Modelo perfilActivo = consulta.queryObject(CAMPOS_PERFIL,PERFIL_NOMBRE,perfila,null,IGUAL,null);

            String ordenProyectosFecha = PROYECTO_FECHAENTRADA + " ASC";

            ArrayList<Modelo> listaProyectos = consulta.queryList(CAMPOS_PROYECTO,null, ordenProyectosFecha);

            for (int i = 0; i < listaProyectos.size(); i++) {

                double horasTrabajoPendientes = 0;

                Modelo proyecto = listaProyectos.get(i);

                if (proyecto.getInt(PROYECTO_TIPOESTADO) != 0) {

                    if (proyecto.getInt(PROYECTO_TIPOESTADO) > 6) {

                        if (proyecto.getLong(PROYECTO_FECHAFINAL) == 0) {

                            ContentValues valores = new ContentValues();
                            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_FECHAFINAL,JavaUtil.hoy());
                            consulta.updateRegistro(TABLA_PROYECTO,proyecto.getString(PROYECTO_ID_PROYECTO),valores);

                        }

                    } else {

                        for (int x = i; x < listaProyectos.size(); x++) {

                            Modelo proyecto2 = listaProyectos.get(x);

                            if ((proyecto2.getInt(PROYECTO_CLIENTE_PESOTIPOCLI) >=
                                    proyecto.getInt(PROYECTO_CLIENTE_PESOTIPOCLI))|| (!prioridad)) {


                                    double horasrealizadas =
                                            (proyecto2.getDouble(PROYECTO_TIEMPO)/ 100)
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
                        consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_FECHAENTREGACALCULADA,fecha);
                        consulta.updateRegistro(TABLA_PROYECTO,proyecto.getString(PROYECTO_ID_PROYECTO),valores);
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

            ArrayList<Modelo> listaTipoCliente = consulta.queryList
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

            ArrayList<Modelo> listaProyectos = consulta.queryList
                    (CAMPOS_PROYECTO,null, null);

            ArrayList<Modelo> lista = consulta.queryList
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
                consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_ID_TIPOCLIENTE,cliente.getString(CLIENTE_ID_TIPOCLIENTE));
                consulta.putDato(valores,CAMPOS_CLIENTE,CLIENTE_PESOTIPOCLI,cliente.getInt(CLIENTE_PESOTIPOCLI));
                consulta.updateRegistro(TABLA_CLIENTE,cliente.getString(CLIENTE_ID_CLIENTE),valores);
            }

        }

        public static void actualizarPartidaProyecto(String idPartida){

            double tiempoPartida= 0;
            double importeProductosPartida= 0;
            double importeTiempoPartida= 0;
            double totalPartida= 0;
            ArrayList<Modelo> listaDetPartida;
            listaDetPartida = consulta.queryListDetalle(CAMPOS_DETPARTIDA,idPartida,TABLA_PARTIDA);
            for (Modelo detPartida : listaDetPartida) {

                if (detPartida.getDouble(DETPARTIDA_TIEMPO)>0) {
                    tiempoPartida += detPartida.getDouble(DETPARTIDA_TIEMPO)*detPartida.getDouble(DETPARTIDA_CANTIDAD);
                }else {
                    double importedet = detPartida.getDouble(DETPARTIDA_PRECIO)*detPartida.getDouble(DETPARTIDA_CANTIDAD);
                    importeProductosPartida += importedet + ((importedet/100)*detPartida.getDouble(DETPARTIDA_DESCUENTOPROV));
                }
            }
            importeTiempoPartida = tiempoPartida * CommonPry.hora;
            totalPartida = importeProductosPartida +importeTiempoPartida;

            ContentValues valores = new ContentValues();
            consulta.putDato(valores,CAMPOS_PARTIDA,PARTIDA_TIEMPO,tiempoPartida);
            consulta.putDato(valores,CAMPOS_PARTIDA,PARTIDA_PRECIO,totalPartida);
            consulta.putDato(valores,CAMPOS_PARTIDA,PARTIDA_PRECIOHORA,CommonPry.hora);

            Modelo partida = consulta.queryObject(CAMPOS_PARTIDA,PARTIDA_ID_PARTIDA,idPartida,null,
                    JavaUtil.Constantes.IGUAL,null);
            String idProyecto_Partida = partida.getString(PARTIDA_ID_PROYECTO);
            int secuenciaPartida = partida.getInt(PARTIDA_SECUENCIA);
            int i =consulta.updateRegistroDetalle(TABLA_PARTIDA,idProyecto_Partida,secuenciaPartida,valores);
            System.out.println("Partidas actualizadas = " + i);

        }

        public static void actualizarPartidaBase(String idPartidabase){

            double tiempoPartida= 0;
            double importeProductosPartida= 0;
            double importeTiempoPartida= 0;
            double totalPartida= 0;
            ArrayList<Modelo> listaDetPartida;
            listaDetPartida = consulta.queryListDetalle(CAMPOS_DETPARTIDABASE,idPartidabase,TABLA_PARTIDABASE);
            for (Modelo detPartida : listaDetPartida) {

                if (detPartida.getDouble(DETPARTIDABASE_TIEMPO)>0) {
                    tiempoPartida += detPartida.getDouble(DETPARTIDABASE_TIEMPO)*detPartida.getDouble(DETPARTIDABASE_CANTIDAD);
                }else {
                    double importedet = detPartida.getDouble(DETPARTIDABASE_PRECIO)*detPartida.getDouble(DETPARTIDABASE_CANTIDAD);
                    importeProductosPartida += importedet + ((importedet/100)*detPartida.getDouble(DETPARTIDABASE_DESCUENTOPROV));
                }
            }
            importeTiempoPartida = tiempoPartida * CommonPry.hora;
            totalPartida = importeProductosPartida +importeTiempoPartida;

            ContentValues valores = new ContentValues();
            consulta.putDato(valores,CAMPOS_PARTIDABASE,PARTIDABASE_TIEMPO,tiempoPartida);
            consulta.putDato(valores,CAMPOS_PARTIDABASE,PARTIDABASE_PRECIO,totalPartida);

            Modelo partida = consulta.queryObject(CAMPOS_PARTIDABASE,idPartidabase);
            consulta.updateRegistro(TABLA_PARTIDABASE,idPartidabase,valores);

        }

        public static void actualizarPresupuesto(Modelo partida){

            ArrayList<Modelo> listaPartidas = consulta.queryListDetalle(CAMPOS_PARTIDA,partida.getString(PARTIDA_ID_PROYECTO),TABLA_PROYECTO);

            double totalTiempo= 0;
            double totalPrecio= 0;
            int totcompletada= 0;

            for (Modelo itemPartida : listaPartidas) {
                actualizarPartidaProyecto(itemPartida.getString(PARTIDA_ID_PARTIDA));
                double cantidad = itemPartida.getDouble(PARTIDA_CANTIDAD);
                totalTiempo+=itemPartida.getDouble(PARTIDA_TIEMPO)*cantidad;
                totalPrecio+=itemPartida.getDouble(PARTIDA_PRECIO)*cantidad;
                totcompletada+= itemPartida.getInt(PARTIDA_COMPLETADA);
            }

            totcompletada = (int) (Math.round(((double) totcompletada) / listaPartidas.size()));
            ContentValues valores = new ContentValues();
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_TIEMPO,totalTiempo);
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_IMPORTEPRESUPUESTO,totalPrecio);
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_TOTCOMPLETADO,totcompletada);

            consulta.updateRegistro(TABLA_PROYECTO,partida.getString(PARTIDA_ID_PROYECTO),valores);

        }

        public static void actualizarPresupuesto(String idProyecto){

            ArrayList<Modelo> listaPartidas = consulta.queryListDetalle(CAMPOS_PARTIDA,idProyecto,TABLA_PROYECTO);

            double totalTiempo= 0;
            double totalPrecio= 0;
            int totcompletada= 0;

            for (Modelo itemPartida : listaPartidas) {
                actualizarPartidaProyecto(itemPartida.getString(PARTIDA_ID_PARTIDA));
                double cantidad = itemPartida.getDouble(PARTIDA_CANTIDAD);
                totalTiempo+=itemPartida.getDouble(PARTIDA_TIEMPO)*cantidad;
                totalPrecio+=itemPartida.getDouble(PARTIDA_PRECIO)*cantidad;
                totcompletada+= itemPartida.getInt(PARTIDA_COMPLETADA);
            }

            totcompletada = (int) (Math.round(((double) totcompletada) / listaPartidas.size()));
            ContentValues valores = new ContentValues();
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_TIEMPO,totalTiempo);
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_IMPORTEPRESUPUESTO,totalPrecio);
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_TOTCOMPLETADO,totcompletada);

            consulta.updateRegistro(TABLA_PROYECTO,idProyecto,valores);

        }

        public static void actualizarPresupuesto(){

            ArrayList<Modelo> listaProy = consulta.queryList(CAMPOS_PROYECTO);

            for (Modelo proy : listaProy) {
                String idProyecto = proy.getString(PROYECTO_ID_PROYECTO);

                ArrayList<Modelo> listaPartidas = consulta.queryListDetalle(CAMPOS_PARTIDA, idProyecto, TABLA_PROYECTO);

                double totalTiempo = 0;
                double totalPrecio = 0;
                int totcompletada = 0;

                for (Modelo itemPartida : listaPartidas) {
                    actualizarPartidaProyecto(itemPartida.getString(PARTIDA_ID_PARTIDA));
                    double cantidad = itemPartida.getDouble(PARTIDA_CANTIDAD);
                    totalTiempo += itemPartida.getDouble(PARTIDA_TIEMPO) * cantidad;
                    totalPrecio += itemPartida.getDouble(PARTIDA_PRECIO) * cantidad;
                    totcompletada += itemPartida.getInt(PARTIDA_COMPLETADA);
                }

                totcompletada = (int) (Math.round(((double) totcompletada) / listaPartidas.size()));
                ContentValues valores = new ContentValues();
                consulta.putDato(valores, CAMPOS_PROYECTO, PROYECTO_TIEMPO, totalTiempo);
                consulta.putDato(valores, CAMPOS_PROYECTO, PROYECTO_IMPORTEPRESUPUESTO, totalPrecio);
                consulta.putDato(valores, CAMPOS_PROYECTO, PROYECTO_TOTCOMPLETADO, totcompletada);

                int i = consulta.updateRegistro(TABLA_PROYECTO, idProyecto, valores);
                System.out.println("Proys actualizados = " + i);
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

        public static class TareaActualizarProys extends AsyncTask<Void, Float, Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {

                actualizarPresupuesto();
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

        public static class TareaActualizaProy extends AsyncTask<String, Float, Integer> {

            @Override
            protected Integer doInBackground(String... strings) {

                actualizarPresupuesto(strings[0]);
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
