package com.codevsolution.freemarketsapp.logica;

import androidx.fragment.app.Fragment;
import com.codevsolution.freemarketsapp.ui.ListadoProductos;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.models.DestinosVoz;
import com.codevsolution.freemarketsapp.R;
import java.util.ArrayList;

import static com.codevsolution.base.android.AppActivity.getAppContext;


public class Interactor extends InteractorBase implements JavaUtil.Constantes,
        InteractorBase.Constantes {


        public static String perfila = null;//Perfil setActivo para calculos y preferencias
        public static boolean prioridad;
        public static int diaspasados;
        public static int diasfuturos;
        public static double hora;

    public static Fragment fragmentMenuInicio = new ListadoProductos();




    //private static ConsultaBD consulta = new ConsultaBD();

    public interface ConstantesPry {


        String HTTPAYUDA = "http://frelanceproject.jjlacode.ml/";
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
        String PRODLUGAR = "prodlugar";
        String LUGAR = getAppContext().getString(R.string.lugar);
        String EMPRESA = getAppContext().getString(R.string.empresa);
        String PARTIDABASE = "partidabase";
        String CLIENTE = "cliente";
        String TIPOCLIENTE = "tipo_cliente";
        String ESTADO = "estado";
        String PRODUCTO = "producto";
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
        String PROYECTO = "proyecto";
        String DIARIO = "Notas";
        String EVENTO = "evento";
        String NOTA = "nota";
        String TRABAJO = "trabajo";
        String AMORTIZACION = "amortizacion";
        String PROVEEDOR = "proveedor";
        String PARTIDA = "partida";
        String HABITUAL = getAppContext().getString(R.string.habitual);
        String OCASIONAL = getAppContext().getString(R.string.ocasional);
        String PRINCIPAL = getAppContext().getString(R.string.principal);
        String GANADORSORTEO = "ganador_sorteo";
        int COD_SELECCIONA = 10;
        int COD_FOTO = 20;
        String CARPETA_PRINCIPAL = "freelanceproyect/";
        String CARPETA_IMAGEN = "imagenes";
        String ACCION_AVISOEVENTO = "com.jjlacode.freelanceproject.action.AVISOEVENTO";
        String ACCION_AVISOSORTEO = "com.jjlacode.freelanceproject.action.AVISOSORTEO";
        String ACCION_POSPONER = "com.jjlacode.freelanceproject.action.POSPONER";
        String ACCION_CANCELAR = "com.jjlacode.freelanceproject.action.CANCELAR";
        String ACCION_VER = "com.jjlacode.freelanceproject.action.VER";
        String ACCION_VERLUGAR = "com.jjlacode.freelanceproject.action.VERLUGAR";
        String STARTSERVER ="Servicio iniciado";
        String STOPSERVER = "Servicio detenido";
        String EXTRA_IDEVENTO = "com.jjlacode.freelanceproject.EXTRA_IDEVENTO";
        String EXTRA_BUNDLE = "com.jjlacode.freelanceproject.EXTRA_BUNDLE";
        String EXTRA_ACCION = "com.jjlacode.freelanceproject.EXTRA_ACCION";
        String EXTRA_SORTEO = "com.jjlacode.freelanceproject.EXTRA_SORTEO";
        String EXTRA_PRODUCTO = "com.jjlacode.freelanceproject.EXTRA_PRODUCTO";
        String EXTRA_GANADOR = "com.jjlacode.freelanceproject.EXTRA_GANADOR";


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
        int TPROYECTHISTORICO = 9;
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
        String PROYECTHISTORICO = getAppContext().getString(R.string.proyecto_historico_est);
        String PRESUPNOACEPTADO = getAppContext().getString(R.string.presupuesto_no_aceptado_est);
    }


    public static ArrayList<DestinosVoz> getListaDestinosVoz() {

        ArrayList<DestinosVoz> listaDestinos = new ArrayList<>();
        //listaDestinos.add(new DestinosVoz(INICIO.toLowerCase(), new MenuInicio()));

        return listaDestinos;
    }

    public static ArrayList<DestinosVoz> getListaNuevosDestinosVoz() {

        ArrayList<DestinosVoz> listaDestinos = new ArrayList<>();

        return listaDestinos;
    }




}
