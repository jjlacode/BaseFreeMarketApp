package jjlacode.com.freelanceproject.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.sqlite.ConsultaBD;

import static jjlacode.com.freelanceproject.util.CommonPry.Constantes.PERSISTENCIA;

public abstract class FragmentBaseCRUD extends FragmentBase implements JavaUtil.Constantes {


    protected int tituloPlural;
    protected int tituloSingular;
    protected String tabla;
    protected String[] campos;
    protected String id;
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
    protected String subTitulo;
    protected boolean nuevo;


    public FragmentBaseCRUD() {
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences persistencia=getActivity().getSharedPreferences(PERSISTENCIA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=persistencia.edit();
        editor.putString(ORIGEN, origen);
        editor.putString(ACTUAL, actual);
        editor.putString(SUBTITULO, subTitulo);
        editor.putString(ID,id);
        editor.putInt(SECUENCIA,secuencia);
        editor.apply();
        System.out.println("Guardado onPause");
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
            nuevo = bundle.getBoolean(NUEVOREGISTRO);
            if (subTitulo==null){
                subTitulo = CommonPry.setNamefdef();
            }
            listab = (ListaModelo) bundle.getSerializable(LISTA);
            modelo = (Modelo) bundle.getSerializable(MODELO);

        }
        if (tablaCab!=null && modelo!=null){
                getID();
                getSecuencia();
        }else if (modelo!=null){
                getID();
        }

        setBundle();
        enviarAct();
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
        System.out.println("bundle enviado a activityBase desde "+ tituloPlural);
    }

    protected void enviarBundle(){

        bundle = new Bundle();
        bundle.putString(ORIGEN, origen);
        bundle.putString(ACTUAL,actual);
        bundle.putSerializable(MODELO,modelo);

    }

    @Override
    public void onResume() {

        if (tituloPlural>0) {
            activityBase.toolbar.setTitle(tituloPlural);
        }
        if (subTitulo!=null) {
            activityBase.toolbar.setSubtitle(subTitulo);
        }

        cargarBundle();

        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setTabla();
        setTablaCab();
        setCampos();
        setCampoID();
        setContext();
        setTitulo();

        super.onCreate(savedInstanceState);
    }

    protected void setListaModelo(){
        lista = new ListaModelo(campos);
    }

    protected void setListaModelo(ListaModelo list){
        lista = new ListaModelo(campos);
        lista.clearAddAll(list.getLista());
    }

    protected void setListaModelo(ArrayList<Modelo> list){
        lista = new ListaModelo(campos);
        lista.clearAddAll(list);
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

    protected Modelo setModelo(String id){
        return consulta.queryObject(campos,id);
    }

    protected Modelo setModelo(String id, int secuencia){

        return consulta.queryObjectDetalle(campos,id,secuencia);
    }

    protected Modelo setModelo(String id, String secuencia){
        return consulta.queryObjectDetalle(campos,id,secuencia);
    }

    protected Modelo setModelo(String[] campos, String id){
        return consulta.queryObject(campos,id);
    }

    protected Modelo setModelo(String[] campos, String id, String secuencia){
        return consulta.queryObjectDetalle(campos,id,secuencia);
    }

    protected int actualizarRegistro(){

        if (tablaCab==null) {
            return consulta.updateRegistro(tabla, id, valores);
        }else{
            return consulta.updateRegistroDetalle(tabla, id, secuencia,valores);
        }
    }

    protected int actualizarRegistro(String tabla, String id, int secuencia){

        if (tablaCab==null) {
            return consulta.updateRegistro(tabla, id, valores);
        }else{
            return consulta.updateRegistroDetalle(tabla, id, secuencia,valores);
        }
    }

    protected Uri crearRegistroUri(){

        if (tablaCab==null){
            return consulta.insertRegistro(tabla,valores);
        }else{
            return consulta.insertRegistroDetalle(campos,id,tablaCab,valores);
        }
    }

    protected Uri crearRegistroUri(String tabla, String[] campos, String tablacab, String id){

        if (tablaCab==null){
            return consulta.insertRegistro(tabla,valores);
        }else{
            return consulta.insertRegistroDetalle(campos,id,tablaCab,valores);
        }
    }

    protected String crearRegistroId(){

            return consulta.idInsertRegistro(tabla,valores);
    }

    protected String crearRegistroId(String tabla){

        return consulta.idInsertRegistro(tabla,valores);
    }

    protected int crearRegistroDetalleSec(){

            return consulta.secInsertRegistroDetalle(campos,id,tablaCab,valores);
    }

    protected int crearRegistroDetalleSec(String[] campos, String tablacab, String id){

        return consulta.secInsertRegistroDetalle(campos,id,tablaCab,valores);
    }

    protected int borrarRegistro(){
        if (tablaCab==null) {
            return consulta.deleteRegistro(tabla, id);
        }else{
            return consulta.deleteRegistroDetalle(tabla,id,secuencia);
        }
    }

    protected int borrarRegistro(String tabla, String id, int secuencia){
        if (tablaCab==null) {
            return consulta.deleteRegistro(tabla, id);
        }else{
            return consulta.deleteRegistroDetalle(tabla,id,secuencia);
        }
    }

    protected String getString(String campo){
        return modelo.getString(campo);
    }

    protected int getInt(String campo){
        return modelo.getInt(campo);
    }

    protected long getLong(String campo){
        return modelo.getLong(campo);
    }

    protected double getDouble(String campo){
        return modelo.getDouble(campo);
    }

    protected float getFloat(String campo){
        return modelo.getFloat(campo);
    }

    protected short getShort(String campo){
        return modelo.getShort(campo);
    }

    protected String getID(){
        return modelo.getString(campoID);
    }

    protected int getSecuencia(){
        return modelo.getInt("secuencia");
    }


}
