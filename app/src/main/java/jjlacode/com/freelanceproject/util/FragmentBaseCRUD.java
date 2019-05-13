package jjlacode.com.freelanceproject.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.sqlite.ConsultaBD;

import static jjlacode.com.freelanceproject.util.CommonPry.Constantes.PERSISTENCIA;
import static jjlacode.com.freelanceproject.util.CommonPry.namesubdef;
import static jjlacode.com.freelanceproject.util.CommonPry.setNamefdef;

public abstract class FragmentBaseCRUD extends FragmentBase implements JavaUtil.Constantes {

    protected String namef;
    protected String namesub;
    protected String nameftemp;
    protected String namesubtemp;
    protected String namesubclass;
    protected String tabla;
    protected String[] campos;
    protected String id;
    protected int secuencia;
    protected String tablaCab;
    protected Context contexto;
    protected Modelo modelo;
    protected String campoID;
    protected boolean nuevo;
    protected ConsultaBD consulta = new ConsultaBD();
    protected ContentValues valores;
    protected ListaModelo lista;


    public FragmentBaseCRUD() {
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences persistencia=getActivity().getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=persistencia.edit();
        editor.putString(NAMEF,namef);
        editor.putString(ID,id);
        editor.putInt(SECUENCIA,secuencia);
        editor.putString(NAMESUB,namesubclass);
        editor.putString(NAMEFTEMP,nameftemp);
        editor.putString(NAMESUBTEMP,namesubtemp);
        editor.apply();
        System.out.println("Guardado onPause");
    }

    /**
     * Carga el Bundle de entrada de la transisión entre Fragments ó Activity
     */
    protected void cargarBundle(){

        super.cargarBundle();

        if (bundle != null) {
            namef = bundle.getString(NAMEF);
            namesub = bundle.getString(NAMESUB);
            nameftemp = bundle.getString(NAMEFTEMP);
            namesubtemp = bundle.getString(NAMESUBTEMP);
            lista = (ListaModelo) bundle.getSerializable(LISTA);

            if (tablaCab==null) {
                modelo = (Modelo) bundle.getSerializable(MODELO);
            }
            secuencia = bundle.getInt(SECUENCIA);
            id = bundle.getString(ID);
            nuevo = bundle.getBoolean(NUEVOREGISTRO);
            if (modelo==null && id!=null){
                if(secuencia>0){
                    modelo = consulta.queryObjectDetalle(campos,id,secuencia);
                }else if(tablaCab==null){
                    System.out.println("id recibido de bundle= " + id);
                    System.out.println("tabla = " + tabla);
                    modelo = consulta.queryObject(campos,id);
                }
            }
            if (nuevo){
                if (tablaCab==null){
                    id=null;
                    modelo = null;
                }else{
                    secuencia=0;
                    modelo = null;
                }
            }
            if (lista==null){

                if (tablaCab==null){
                    setListaModelo();
                }else if (id!=null){
                    setListaModeloDetalle();
                }
            }

        }
        setBundle();
    }



    protected abstract void setTabla();

    protected abstract void setTablaCab();

    protected abstract void setContext();

    protected abstract void setCampos();

    protected abstract void setCampoID();

    protected abstract void setBundle();

    protected abstract void setDatos();

    protected abstract void setAcciones();


    protected void acciones(){


    }


    protected void enviarAct(){

        enviarBundle();
        icFragmentos.enviarBundleAActivity(bundle);
        System.out.println("bundle enviado a activity "+namef);
    }

    protected void enviarBundle(){

        bundle = new Bundle();
        bundle.putString(NAMEF,namef);
        bundle.putString(NAMESUB,namesubclass);
        bundle.putSerializable(MODELO,modelo);
        bundle.putInt(SECUENCIA,secuencia);
        bundle.putString(ID,id);

    }

    @Override
    public void onResume() {

        cargarBundle();
        /*
        if (!nuevo){

            namesubclass = namesubtemp;

        }else{

            namesubclass = namesub;
        }
        */
        if (namesub!=null) {
            namesubclass = namesub;
        }else{
            namesubclass = namesubdef = setNamefdef();
        }
        if (bundle!=null) {
            enviarAct();
        }

        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setTabla();
        setTablaCab();
        setCampos();
        setCampoID();
        setContext();

        super.onCreate(savedInstanceState);
    }

    protected void setListaModelo(){
        lista = new ListaModelo(campos);
    }

    protected void setListaModeloDetalle(){
        lista = new ListaModelo(campos,id,tablaCab,null,null);
    }

    protected void setListaModelo(String seleccion){
        lista = new ListaModelo(campos,seleccion,null);
    }

    protected void setListaModeloDetalle(String seleccion){
        lista = new ListaModelo(campos,id,tablaCab,seleccion,null);
    }

    protected void setListaModelo(String seleccion, String orden){
        lista = new ListaModelo(campos,seleccion,orden);
    }

    protected void setListaModeloDetalle(String seleccion, String orden){
        lista = new ListaModelo(campos,id,tablaCab,seleccion,orden);
    }

    protected void setListaModelo(String campo, String valor, int flag){
        lista = new ListaModelo(campos,campo,valor,null,flag,null);
    }


}
