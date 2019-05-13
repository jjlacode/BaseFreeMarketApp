package jjlacode.com.freelanceproject.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import jjlacode.com.freelanceproject.BuildConfig;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.model.ProdProv;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;

import static jjlacode.com.freelanceproject.util.AppActivity.getAppContext;


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
        public static String namesubdef;


    private static ConsultaBD consulta = new ConsultaBD();


    public interface  Constantes {

        String PERSISTENCIA = "persistencia";
        String PRIORIDAD = "prioridad";
        String PREFERENCIAS = "preferencias";
        String DIASPASADOS = "diaspasados";
        String DIASFUTUROS = "diasfuturos";
        String BASEDATOS = "freelanceproject.db";
        String PERFILACTIVO = "perfil activo";
        String VISORPDF = "visor pdf";
        String VISORPDFMAIL = "visor pdf - email";
        String TODAS = "Todas";
        String TODOS = "Todos";
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
        String TCLIENTE = getAppContext().getString(R.string.t_cliente);
        String CLIENTES = getAppContext().getString(R.string.clientes);
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
        String AGENDA = "agenda";
        String EVENTO = "evento";
        String PERFIL = "perfil";
        String AMORTIZACION = "amortizacion";
        String GASTOSFIJOS = "gastos fijos";
        String PARTIDA = "partida";
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

    public interface TiposNota{

        String NOTATEXTO = "Nota de Texto";
        String NOTAAUDIO = "Nota de Audio";
        String NOTAVIDEO = "Nota de Video";
        String NOTAIMAGEN = "Nota de Imagen";
    }


    public static String setNamefdef(){

        hora = Calculos.calculoPrecioHora();
        int longitud = perfila.length();
        String perfil = null;
        if (longitud>10) {
            perfil = perfila.substring(0, 10);
        }else{
            perfil = perfila;
        }

        if (prioridad){

            namesubdef = "Perfil: " + perfil + " hora: " +  JavaUtil.formatoMonedaLocal(hora) + " - P";


        }else{

            namesubdef = "Perfil: " + perfil + " hora: " + JavaUtil.formatoMonedaLocal(hora);

        }

        return namesubdef;
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




    public static class Calculos implements  Constantes, TiposDetPartida{


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
            double hora = (base + ((base / 100) * beneficio));

            return hora;
        }

        public static double calculoCosteHora(){

            double horapvp = calculoPrecioHora();
            Modelo perfil = consulta.queryObject(CAMPOS_PERFIL,PERFIL_NOMBRE,perfila,null,IGUAL,null);
            double beneficio = perfil.getDouble(PERFIL_BENEFICIO);

            return horapvp/(1+(beneficio/100));

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

        public static boolean sincronizarPartidaBase(String idPartidabase){

            ArrayList<Modelo> listaDetPartida;
            ContentValues valores = null;
            listaDetPartida = consulta.queryListDetalle(CAMPOS_DETPARTIDABASE,idPartidabase,TABLA_PARTIDABASE);

            for (Modelo modelo : listaDetPartida) {
                if (modelo.getString(DETPARTIDABASE_ID_DETPARTIDABASE)==null){
                    System.out.println("Id nulo");
                    return false;
                }
            }

            for (final Modelo detPartida : listaDetPartida) {

                String tipo = detPartida.getString(DETPARTIDABASE_TIPO);
                final String id = detPartida.getString(DETPARTIDABASE_ID_DETPARTIDABASE);
                System.out.println("iddep = " + id);

                if (id != null) {

                    switch (tipo) {

                        case TIPOTAREA:

                            Modelo tarea = consulta.queryObject(CAMPOS_TAREA, id);
                            if (tarea != null) {
                                valores = new ContentValues();
                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_DESCRIPCION, tarea.getString(TAREA_DESCRIPCION));
                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_NOMBRE, tarea.getString(TAREA_NOMBRE));
                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_TIEMPO, tarea.getString(TAREA_TIEMPO));
                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_RUTAFOTO, tarea.getString(TAREA_RUTAFOTO));

                                consulta.updateRegistroDetalle(TABLA_DETPARTIDABASE, detPartida.getString(DETPARTIDABASE_ID_PARTIDABASE), detPartida.getInt(DETPARTIDA_SECUENCIA), valores);
                            }
                            break;

                        case TIPOPRODUCTO:

                            Modelo producto = consulta.queryObject(CAMPOS_PRODUCTO, id);
                            if (producto != null) {
                                valores = new ContentValues();
                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_DESCRIPCION, producto.getString(PRODUCTO_DESCRIPCION));
                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_NOMBRE, producto.getString(PRODUCTO_NOMBRE));
                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_PRECIO, producto.getString(PRODUCTO_IMPORTE));
                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_RUTAFOTO, producto.getString(PRODUCTO_RUTAFOTO));

                                consulta.updateRegistroDetalle(TABLA_DETPARTIDABASE, detPartida.getString(DETPARTIDABASE_ID_PARTIDABASE), detPartida.getInt(DETPARTIDA_SECUENCIA), valores);
                            }

                            break;

                        case TIPOPARTIDA:

                            Modelo partida = consulta.queryObject(CAMPOS_PARTIDABASE, id);
                            if (partida != null) {
                                valores = new ContentValues();
                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_DESCRIPCION, partida.getString(PARTIDABASE_DESCRIPCION));
                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_NOMBRE, partida.getString(PARTIDABASE_NOMBRE));
                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_TIEMPO, partida.getString(PARTIDABASE_TIEMPO));
                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_PRECIO, partida.getString(PARTIDABASE_PRECIO));
                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_RUTAFOTO, partida.getString(PARTIDABASE_RUTAFOTO));

                                consulta.updateRegistroDetalle(TABLA_DETPARTIDABASE, detPartida.getString(DETPARTIDABASE_ID_PARTIDABASE), detPartida.getInt(DETPARTIDA_SECUENCIA), valores);
                            }

                            break;


                        case TIPOPRODUCTOPROV:


                            DatabaseReference dbProveedor =
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("productos");

                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if (ds.getRef().getKey().equals(id)) {
                                            ProdProv prodProv = ds.getValue(ProdProv.class);
                                            if (prodProv != null) {
                                                System.out.println("prodProv = " + prodProv.getNombre());
                                                ContentValues valores = new ContentValues();
                                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_DESCRIPCION, prodProv.getDescripcion());
                                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_NOMBRE, prodProv.getNombre());
                                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_REFPROV, prodProv.getRefprov());
                                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_PRECIO, prodProv.getPrecio());
                                                consulta.putDato(valores, CAMPOS_DETPARTIDABASE, DETPARTIDABASE_RUTAFOTO, prodProv.getRutafoto());

                                                consulta.updateRegistroDetalle(TABLA_DETPARTIDABASE, detPartida.getString(DETPARTIDABASE_ID_PARTIDABASE), detPartida.getInt(DETPARTIDA_SECUENCIA), valores);
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
            }
            if (actualizarPartidaBase(idPartidabase)) {
                System.out.println("Partida base actualizada " + idPartidabase);
                return true;
            }
            return false;
        }

        public static boolean actualizarPartidaProyecto(String idPartida){

            double tiempoPartida= 0;
            double importeProductosPartida= 0;
            double coste = 0;
            double importeTiempoPartida= 0;
            double totalPartida= 0;
            ArrayList<Modelo> listaDetPartida;
            listaDetPartida = consulta.queryListDetalle(CAMPOS_DETPARTIDA,idPartida,TABLA_PARTIDA);
            for (Modelo detPartida : listaDetPartida) {

                if (detPartida.getDouble(DETPARTIDA_TIEMPO)>0) {
                    tiempoPartida += detPartida.getDouble(DETPARTIDA_TIEMPO)*detPartida.getDouble(DETPARTIDA_CANTIDAD);
                }else {
                    double importedet = detPartida.getDouble(DETPARTIDA_PRECIO)*detPartida.getDouble(DETPARTIDA_CANTIDAD);
                    if (detPartida.getString(DETPARTIDA_TIPO).equals(TIPOPRODUCTO)) {
                        importeProductosPartida += importedet + ((importedet / 100) * detPartida.getDouble(DETPARTIDA_BENEFICIO));
                    }else{
                        importeProductosPartida += importedet;
                    }
                    if (detPartida.getString(DETPARTIDA_TIPO).equals(TIPOPRODUCTOPROV)) {
                        coste += importedet - ((importedet / 100) * detPartida.getDouble(DETPARTIDA_DESCUENTOPROV));
                    }else{
                        coste += importedet;
                    }
                }
            }
            coste += (tiempoPartida * calculoCosteHora());
            importeTiempoPartida = tiempoPartida * CommonPry.hora;
            totalPartida = importeProductosPartida +importeTiempoPartida;

            ContentValues valores = new ContentValues();
            consulta.putDato(valores,CAMPOS_PARTIDA,PARTIDA_TIEMPO,tiempoPartida);
            consulta.putDato(valores,CAMPOS_PARTIDA,PARTIDA_PRECIO,totalPartida);
            consulta.putDato(valores,CAMPOS_PARTIDA,PARTIDA_COSTE,coste);
            consulta.putDato(valores,CAMPOS_PARTIDA,PARTIDA_PRECIOHORA,CommonPry.hora);

            Modelo partida = consulta.queryObject(CAMPOS_PARTIDA,PARTIDA_ID_PARTIDA,idPartida,null,
                    JavaUtil.Constantes.IGUAL,null);
            String idProyecto_Partida = partida.getString(PARTIDA_ID_PROYECTO);
            int secuenciaPartida = partida.getInt(PARTIDA_SECUENCIA);
            int i =consulta.updateRegistroDetalle(TABLA_PARTIDA,idProyecto_Partida,secuenciaPartida,valores);
            if (i > 0) {
                System.out.println("Partidas actualizadas = " + i);
                return true;
            }

            return false;
        }

        public static boolean actualizarPartidaBase(String idPartidabase){

            double tiempoPartida= 0;
            double importeProductosPartida= 0;
            double coste = 0;
            double importeTiempoPartida= 0;
            double totalPartida= 0;
            ArrayList<Modelo> listaDetPartida;
            listaDetPartida = consulta.queryListDetalle(CAMPOS_DETPARTIDABASE,idPartidabase,TABLA_PARTIDABASE);
            for (Modelo detPartida : listaDetPartida) {

                if (detPartida.getDouble(DETPARTIDABASE_TIEMPO)>0) {
                    tiempoPartida += detPartida.getDouble(DETPARTIDABASE_TIEMPO)*detPartida.getDouble(DETPARTIDABASE_CANTIDAD);
                }else {
                    double importedet = detPartida.getDouble(DETPARTIDABASE_PRECIO)*detPartida.getDouble(DETPARTIDABASE_CANTIDAD);
                    if (detPartida.getString(DETPARTIDABASE_TIPO).equals(TIPOPRODUCTO)) {
                        importeProductosPartida += importedet + ((importedet / 100) * detPartida.getDouble(DETPARTIDABASE_BENEFICIO));
                    }else{
                        importeProductosPartida += importedet;
                    }
                    if (detPartida.getString(DETPARTIDABASE_TIPO).equals(TIPOPRODUCTOPROV)) {
                        coste += importedet - ((importedet / 100) * detPartida.getDouble(DETPARTIDABASE_DESCUENTOPROV));
                    }else{
                        coste += importedet;
                    }
                }
            }
            coste += (tiempoPartida * calculoCosteHora());
            importeTiempoPartida = tiempoPartida * CommonPry.hora;
            totalPartida = importeProductosPartida +importeTiempoPartida;

            ContentValues valores = new ContentValues();
            consulta.putDato(valores,CAMPOS_PARTIDABASE,PARTIDABASE_TIEMPO,tiempoPartida);
            consulta.putDato(valores,CAMPOS_PARTIDABASE,PARTIDABASE_PRECIO,totalPartida);
            consulta.putDato(valores,CAMPOS_PARTIDABASE,PARTIDABASE_COSTE,coste);

            int i = consulta.updateRegistro(TABLA_PARTIDABASE,idPartidabase,valores);

            if (i > 0){return true;}
            return false;

        }

        public static void actualizarPresupuesto(Modelo partida){

            ArrayList<Modelo> listaPartidas = consulta.queryListDetalle(CAMPOS_PARTIDA,partida.getString(PARTIDA_ID_PROYECTO),TABLA_PROYECTO);

            double totalTiempo= 0;
            double totalPrecio= 0;
            double totalcoste = 0;
            int totcompletada= 0;

            for (Modelo itemPartida : listaPartidas) {
                actualizarPartidaProyecto(itemPartida.getString(PARTIDA_ID_PARTIDA));
                double cantidad = itemPartida.getDouble(PARTIDA_CANTIDAD);
                totalTiempo+=itemPartida.getDouble(PARTIDA_TIEMPO)*cantidad;
                totalPrecio+=itemPartida.getDouble(PARTIDA_PRECIO)*cantidad;
                totcompletada+= itemPartida.getInt(PARTIDA_COMPLETADA);
                totalcoste+=itemPartida.getDouble(PARTIDA_COSTE)*cantidad;
            }

            totcompletada = (int) (Math.round(((double) totcompletada) / listaPartidas.size()));
            ContentValues valores = new ContentValues();
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_TIEMPO,totalTiempo);
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_IMPORTEPRESUPUESTO,totalPrecio);
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_COSTE,totalcoste);
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_TOTCOMPLETADO,totcompletada);

            consulta.updateRegistro(TABLA_PROYECTO,partida.getString(PARTIDA_ID_PROYECTO),valores);

        }

        public static void actualizarPresupuesto(String idProyecto){

            ArrayList<Modelo> listaPartidas = consulta.queryListDetalle(CAMPOS_PARTIDA,idProyecto,TABLA_PROYECTO);

            double totalTiempo= 0;
            double totalPrecio= 0;
            double totalcoste = 0;
            int totcompletada= 0;

            for (Modelo itemPartida : listaPartidas) {
                actualizarPartidaProyecto(itemPartida.getString(PARTIDA_ID_PARTIDA));
                double cantidad = itemPartida.getDouble(PARTIDA_CANTIDAD);
                totalTiempo+=itemPartida.getDouble(PARTIDA_TIEMPO)*cantidad;
                totalPrecio+=itemPartida.getDouble(PARTIDA_PRECIO)*cantidad;
                totcompletada+= itemPartida.getInt(PARTIDA_COMPLETADA);
                totalcoste+=itemPartida.getDouble(PARTIDA_COSTE)*cantidad;
            }

            totcompletada = (int) (Math.round(((double) totcompletada) / listaPartidas.size()));
            ContentValues valores = new ContentValues();
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_TIEMPO,totalTiempo);
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_IMPORTEPRESUPUESTO,totalPrecio);
            consulta.putDato(valores,CAMPOS_PROYECTO,PROYECTO_COSTE,totalcoste);
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
                double totalcoste = 0;

                for (Modelo itemPartida : listaPartidas) {
                    actualizarPartidaProyecto(itemPartida.getString(PARTIDA_ID_PARTIDA));
                    double cantidad = itemPartida.getDouble(PARTIDA_CANTIDAD);
                    totalTiempo += itemPartida.getDouble(PARTIDA_TIEMPO) * cantidad;
                    totalPrecio += itemPartida.getDouble(PARTIDA_PRECIO) * cantidad;
                    totcompletada += itemPartida.getInt(PARTIDA_COMPLETADA);
                    totalcoste+=itemPartida.getDouble(PARTIDA_COSTE)*cantidad;
                }

                totcompletada = (int) (Math.round(((double) totcompletada) / listaPartidas.size()));
                ContentValues valores = new ContentValues();
                consulta.putDato(valores, CAMPOS_PROYECTO, PROYECTO_TIEMPO, totalTiempo);
                consulta.putDato(valores, CAMPOS_PROYECTO, PROYECTO_IMPORTEPRESUPUESTO, totalPrecio);
                consulta.putDato(valores, CAMPOS_PROYECTO,PROYECTO_COSTE,totalcoste);
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

    }


}
