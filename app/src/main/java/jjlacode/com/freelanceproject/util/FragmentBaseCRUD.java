package jjlacode.com.freelanceproject.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;

import static jjlacode.com.freelanceproject.R.color.colorAccent;

public abstract class FragmentBaseCRUD extends FragmentBase implements JavaUtil.Constantes {


    protected int tituloPlural;
    protected int tituloSingular;
    protected String tabla;
    protected String[] campos;
    protected String id;
    protected String idAOrigen;
    protected int secuencia;
    protected String tablaCab;
    protected Context contexto;
    protected Modelo modelo;
    protected String campoID;

    protected ConsultaBD consulta = new ConsultaBD();
    protected ContentValues valores;
    protected ListaModelo lista;
    protected ListaModelo listab;
    protected String origen;
    protected String actual;
    protected String actualtemp;
    protected String subTitulo;
    protected boolean nuevo;

    protected RelativeLayout frPrincipal;
    protected LinearLayout frdetalle;
    protected LinearLayout frPie;
    protected LinearLayout frCabecera;
    protected View viewCabecera;
    protected View viewCuerpo;
    protected View viewBotones;
    protected int layoutCuerpo;
    protected int layoutCabecera;

    protected Button btnsave;
    protected Button btnback;
    protected Button btndelete;
    protected ImageView imagen;


    protected String path;
    protected ImagenUtil imagenUtil = new ImagenUtil(contexto);
    final protected int COD_FOTO = 10;
    final protected int COD_SELECCIONA = 20;


    public FragmentBaseCRUD() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.contenido, container, false);
        land = getResources().getBoolean(R.bool.esLand);
        tablet = getResources().getBoolean(R.bool.esTablet);
        System.out.println("land = " + land);
        System.out.println("tablet = " + tablet);

        frPrincipal = view.findViewById(R.id.contenedor);
        frdetalle = view.findViewById(R.id.layout_detalle);

        frPie = view.findViewById(R.id.layout_pie);
        viewBotones = inflater.inflate(R.layout.btn_sdb,container,false);
        viewCuerpo = inflater.inflate(layoutCuerpo,container,false);
        frCabecera = view.findViewById(R.id.layout_cabecera);
        if (layoutCabecera>0) {
            viewCabecera = inflater.inflate(layoutCabecera, container,false);
            if(viewCabecera.getParent() != null) {
                ((ViewGroup)viewCabecera.getParent()).removeView(viewCabecera); // <- fix
            }
            if (viewCabecera!=null) {
                frCabecera.addView(viewCabecera);
            }
        }
        if(viewBotones.getParent() != null) {
            ((ViewGroup)viewBotones.getParent()).removeView(viewBotones); // <- fix
        }
        if(viewCuerpo.getParent() != null) {
            ((ViewGroup)viewCuerpo.getParent()).removeView(viewCuerpo); // <- fix
        }


        frdetalle.addView(viewCuerpo);
        frPie.addView(viewBotones);

        btnback = view.findViewById(R.id.btn_back);
        btnsave = view.findViewById(R.id.btn_save);
        btndelete = view.findViewById(R.id.btn_del);

        //btnsave.setVisibility(View.GONE);
        btndelete.setTextColor(getResources().getColor(colorAccent));

        setInicio();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences persistencia=getActivity().getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=persistencia.edit();
        editor.putString(ORIGEN, origen);
        editor.putString(ACTUAL, actual);
        editor.putString(ACTUALTEMP, actualtemp);
        editor.putString(SUBTITULO, subTitulo);
        editor.putString(ID,id);
        editor.putInt(SECUENCIA,secuencia);
        editor.apply();
        System.out.println("Guardado onPause");
        System.out.println("id = " + id);
        //enviarBundle();
        //enviarAct();
    }


    protected boolean onUpdate(){

        return false;
    }

    /**
     * Carga el Bundle de entrada de la transisión entre Fragments ó Activity
     */
    protected void cargarBundle(){

        super.cargarBundle();

        listab = null;

        if (bundle != null) {
            origen = bundle.getString(ORIGEN);
            subTitulo = bundle.getString(SUBTITULO);
            actual = bundle.getString(ACTUAL);
            actualtemp = bundle.getString(ACTUALTEMP);
            nuevo = bundle.getBoolean(NUEVOREGISTRO);
            if (subTitulo==null){
                subTitulo = CommonPry.setNamefdef();
            }
            listab = (ListaModelo) bundle.getSerializable(LISTA);
            modelo = (Modelo) bundle.getSerializable(MODELO);
            if (id==null) {
                id = bundle.getString(ID);
            }
            if (secuencia==0) {
                secuencia = bundle.getInt(SECUENCIA);
            }

        }

        setBundle();
        //enviarBundle();
        //enviarAct();
    }

    protected abstract void setTabla();

    protected abstract void setTablaCab();

    protected abstract void setContext();

    protected abstract void setCampos();

    protected abstract void setCampoID();

    protected abstract void setBundle();

    protected abstract void setDatos();

    protected abstract void setAcciones();

    protected abstract void setTitulo();

    protected void acciones(){

    }

    protected void enviarAct(){

        icFragmentos.enviarBundleAActivity(bundle);
        System.out.println("bundle enviado a activityBase desde "+ getString(tituloPlural));
        System.out.println("bundle = " + bundle);
    }

    protected void enviarBundle(){

        bundle.putString(ORIGEN, origen);
        bundle.putString(ACTUAL,actual);
        bundle.putString(ACTUALTEMP, actualtemp);
        bundle.putSerializable(MODELO,modelo);
        bundle.putString(ID,id);
        bundle.putString(SUBTITULO,subTitulo);
        bundle.putInt(SECUENCIA, secuencia);

    }

    @Override
    public void onResume() {

        super.onResume();

        cargarBundle();

        if (bundle.containsKey(PERSISTENCIA) && bundle.getBoolean(PERSISTENCIA)) {

            SharedPreferences persistencia = getActivity().getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = persistencia.edit();
            if (id == null) {
                id = persistencia.getString(ID, null);
            }
            if (secuencia == 0) {
                secuencia = persistencia.getInt(SECUENCIA, 0);
            }

            editor.putString(ID, null);
            editor.putInt(SECUENCIA, 0);
            editor.apply();
        }

        System.out.println("onResume id = " + id);

        setTabla();
        setTablaCab();
        setCampos();
        setCampoID();
        setContext();
        setTitulo();

        if (tablaCab==null && id!=null){
            modelo = CRUDutil.setModelo(campos,id);
        }
        else if (id!=null && secuencia>0){
            modelo = CRUDutil.setModelo(campos,id,secuencia);
        }

        System.out.println("onResume modelo = " + modelo);

        if (tituloPlural>0) {
            activityBase.toolbar.setTitle(tituloPlural);
        }
        if (subTitulo!=null) {
            activityBase.toolbar.setSubtitle(subTitulo);
        }

        enviarBundle();
        enviarAct();

    }




}
