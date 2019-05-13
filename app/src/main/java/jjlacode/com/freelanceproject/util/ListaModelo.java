package jjlacode.com.freelanceproject.util;

import java.io.Serializable;
import java.util.ArrayList;

import jjlacode.com.freelanceproject.sqlite.ConsultaBD;

import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.SECUENCIA;

public class ListaModelo implements Serializable {

    private ArrayList<Modelo> lista;

    public ListaModelo(ArrayList<Modelo> lista) {
        this.lista = new ArrayList<Modelo>(lista);
    }

    public ListaModelo(String[] campos){
        ConsultaBD consulta = new ConsultaBD();
        lista = consulta.queryList(campos);
    }

    public ListaModelo(String[] campos, String id, String tablaCab, String seleccion, String orden){
        ConsultaBD consulta = new ConsultaBD();
        lista = consulta.queryListDetalle(campos,id,tablaCab,seleccion,orden);
    }

    public ListaModelo(String[] campos, String seleccion, String orden ){
        ConsultaBD consulta = new ConsultaBD();
        lista = consulta.queryList(campos,seleccion,orden);
    }

    public ListaModelo (String[] campos, String campo, String valor, String orden){
        ConsultaBD consulta = new ConsultaBD();
        lista = consulta.queryListCampo(campos,campo,valor, orden);

    }

    public ListaModelo (String[] campos, String campo, String valor, String valor2, int flag, String orden){

        ConsultaBD consulta = new ConsultaBD();
        lista = consulta.queryList(campos,campo,valor,valor2,flag,orden);
    }

    public ArrayList<Modelo> getLista() {
        return lista;
    }

    public void add(Modelo modelo){

        lista.add(modelo);
    }

    public String tabla(int posicion){

        return lista.get(posicion).getNombreTabla();
    }

    public Modelo getItem(int posicion){

        return lista.get(posicion);
    }

    public boolean esDetalle(int posicion){

        Modelo modelo = lista.get(posicion);
        String[] campos = modelo.getCampos();
        for (int i = 0; i<modelo.getNumcampos();i++){
            if (campos[i].equals(SECUENCIA)){
                return true;
            }
        }
        return false;
    }

    public String[] getCampos(int posicion){

        return lista.get(posicion).getCampos();
    }

    public String getCampo(int posicion, String campo){

        return lista.get(posicion).getCampos(campo);
    }

    public void clear(){

        lista.clear();
    }

    public void addAll(ArrayList<Modelo> listaClon){

        lista.addAll(listaClon);
    }

    public int size(){

        if (lista!=null) {
            return lista.size();
        }
        return 0;
    }

    public void clearAddAll(ArrayList<Modelo> listaClon){

        lista.clear();
        lista.addAll(listaClon);
    }

    public void setLista(ArrayList<Modelo> lista) {
        this.lista = lista;
    }

    public void setLista(String[] campos){
        ConsultaBD consulta = new ConsultaBD();
        lista = consulta.queryList(campos);
    }

    public void setLista(String[] campos, String seleccion, String orden ){
        ConsultaBD consulta = new ConsultaBD();
        lista = consulta.queryList(campos,seleccion,orden);
    }

    public void setLista(String[] campos, String id, String tablaCab, String seleccion, String orden){
        ConsultaBD consulta = new ConsultaBD();
        lista = consulta.queryListDetalle(campos,id,tablaCab,seleccion,orden);
    }

    public boolean chech(){
        if(lista!=null) {
            return lista.size() > 0;
        }
        return false;
    }
}
