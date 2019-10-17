package com.codevsolution.base.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import static com.codevsolution.base.javautil.JavaUtil.Constantes.CAMPO_SECUENCIA;
import static com.codevsolution.base.sqlite.ConsultaBD.queryList;
import static com.codevsolution.base.sqlite.ConsultaBD.queryListCampo;
import static com.codevsolution.base.sqlite.ConsultaBD.queryListDetalle;

public class ListaModeloSQL extends ArrayList<ArrayList<ModeloSQL>> implements Serializable {


    public ListaModeloSQL(int initialCapacity) {
        super(initialCapacity);
    }

    public ListaModeloSQL(@NonNull Collection<? extends ArrayList<ModeloSQL>> c) {
        super(c);
    }

    public ListaModeloSQL() {

        super();
        add(new ArrayList<ModeloSQL>());
    }

    public ListaModeloSQL(ArrayList<ModeloSQL> lista) {

        super();

        add(lista);
    }

    public ListaModeloSQL(ListaModeloSQL listas) {

        super();
        addAll(listas);

    }

    public ListaModeloSQL(String[] campos) {

        super();

        add(queryList(campos));
    }

    public ListaModeloSQL(String[] campos, String id, String tablaCab, String seleccion, String orden) {

        super();

        add(queryListDetalle(campos, id, tablaCab, seleccion, orden));
    }

    public ListaModeloSQL(String[] campos, String seleccion, String orden) {

        super();

        add(queryList(campos, seleccion, orden));
    }

    public ListaModeloSQL(String[] campos, String campo, String valor, String orden) {

        super();
        add(queryListCampo(campos, campo, valor, orden));

    }

    public ListaModeloSQL(String[] campos, String campo, String valor, String valor2, int flag, String orden) {

        super();
        add(queryList(campos, campo, valor, valor2, flag, orden));
    }

    public ArrayList<ModeloSQL> getLista() {

        if (size() > 0) {

            return get(0);
        }
        return null;
    }

    public ArrayList<ModeloSQL> getLista(int indice) {

        if (size() > indice) {

            return get(indice);
        }

        return null;
    }

    public ArrayList<ArrayList<ModeloSQL>> getListas() {
        return this;
    }

    public void addLista(ArrayList<ModeloSQL> lista) {

        add(lista);
    }

    public void addLista(int indice, ArrayList<ModeloSQL> lista) {

        add(indice, lista);

    }

    public void addLista(int indice, ListaModeloSQL listaModeloSQL) {

        add(indice, listaModeloSQL.getLista());

    }

    public void addLista(int indiceOrigen, int indiceDestino, ListaModeloSQL listaModeloSQL) {

        add(indiceDestino, listaModeloSQL.getLista(indiceOrigen));

    }

    public void removeLista() {

        remove(0);
    }

    public void removeLista(int indice) {

        remove(indice);

    }

    public void removeLista(ArrayList<ModeloSQL> lista) {

        remove(lista);

    }

    public void removeLista(ListaModeloSQL listaModeloSQL) {

        remove(listaModeloSQL.getLista());

    }

    public void removeLista(int indiceOrigen, ListaModeloSQL listaModeloSQL) {

        remove(listaModeloSQL.getLista(indiceOrigen));

    }

    public void addModelo(ModeloSQL modeloSQL) {

        get(0).add(modeloSQL);
        modeloSQL.setPosicionLista(get(0).indexOf(modeloSQL));
        modeloSQL.setIndiceLista(0);
        modeloSQL.setEnLista(true);
    }

    public void addModelo(int indice, ModeloSQL modeloSQL) {

        get(indice).add(modeloSQL);
        modeloSQL.setPosicionLista(get(indice).indexOf(modeloSQL));
        modeloSQL.setIndiceLista(indice);
        modeloSQL.setEnLista(true);

    }

    public void removeModelo(int posicionModelo) {

        ModeloSQL modeloSQL = get(0).get(posicionModelo);
        modeloSQL.setEnLista(false);
        modeloSQL.setIndiceLista(0);
        modeloSQL.setPosicionLista(0);
        get(0).remove(posicionModelo);
    }

    public void removeModelo(int indice, int posicionModelo) {

        ModeloSQL modeloSQL = get(indice).get(posicionModelo);
        modeloSQL.setEnLista(false);
        modeloSQL.setIndiceLista(0);
        modeloSQL.setPosicionLista(0);
        get(indice).remove(posicionModelo);
    }

    public void removeModelo(ModeloSQL modeloSQL) {

        modeloSQL.setEnLista(false);
        modeloSQL.setIndiceLista(0);
        modeloSQL.setPosicionLista(0);
        get(0).remove(modeloSQL);
    }

    public void removeModelo(int indice, ModeloSQL modeloSQL) {

        modeloSQL.setEnLista(false);
        modeloSQL.setIndiceLista(0);
        modeloSQL.setPosicionLista(0);
        get(indice).remove(modeloSQL);
    }

    public void set(int posicion, ModeloSQL modeloSQL) {

        modeloSQL.setPosicionLista(posicion);
        get(0).set(posicion, modeloSQL);
    }

    public void set(int indice, int posicion, ModeloSQL modeloSQL) {

        modeloSQL.setPosicionLista(posicion);
        get(indice).set(posicion, modeloSQL);
    }

    public String tablaModelo(int posicion) {

        return get(0).get(posicion).getNombreTabla();
    }

    public String tablaModelo(int indice, int posicion) {

        return get(indice).get(posicion).getNombreTabla();
    }

    public ModeloSQL getItem(int posicion) {

        return get(0).get(posicion);
    }

    public ModeloSQL getItem(int indice, int posicion) {

        return get(indice).get(posicion);
    }


    public boolean esDetalle(int posicion) {

        ModeloSQL modeloSQL = get(0).get(posicion);
        String[] campos = modeloSQL.getCampos();
        for (int i = 0; i < modeloSQL.getNumcampos(); i++) {
            if (campos[i].equals(CAMPO_SECUENCIA)) {
                return true;
            }
        }
        return false;
    }

    public boolean esDetalle(int indice, int posicion) {

        ModeloSQL modeloSQL = get(indice).get(posicion);
        String[] campos = modeloSQL.getCampos();
        for (int i = 0; i < modeloSQL.getNumcampos(); i++) {
            if (campos[i].equals(CAMPO_SECUENCIA)) {
                return true;
            }
        }
        return false;
    }

    public String[] getCampos(int posicion) {

        return get(0).get(posicion).getCampos();
    }

    public String[] getCampos(int indice, int posicion) {

        return get(indice).get(posicion).getCampos();
    }

    public String getCampo(int posicion, String campo) {

        return get(0).get(posicion).getCampos(campo);
    }

    public String getCampo(int indice, int posicion, String campo) {

        return get(indice).get(posicion).getCampos(campo);
    }

    public void clearLista() {

        get(0).clear();
    }

    public void clearLista(int indice) {

        get(indice).clear();
    }

    public void addAllLista(ArrayList<ModeloSQL> listaClon) {

        get(0).addAll(listaClon);
    }

    public void addAllLista(int indice, ArrayList<ModeloSQL> listaClon) {

        get(indice).addAll(listaClon);
    }

    public void addAllLista(ListaModeloSQL listaModeloSQLClon) {

        if (sizeLista() > 0) {
            get(0).addAll(listaModeloSQLClon.getLista());
        }
    }

    public void addAllLista(int indiceDestino, ListaModeloSQL listaModeloSQLClon) {

        if (size() > indiceDestino) {

            get(indiceDestino).addAll(listaModeloSQLClon.getLista());
        }
    }

    public void addAllLista(int indiceOrigen, int indiceDestino, ListaModeloSQL listaModeloSQLClon) {

        if (size() > indiceDestino) {

            get(indiceDestino).addAll(listaModeloSQLClon.getLista(indiceOrigen));
        }
    }

    public int sizeLista() {

        if (this != null && size() > 0 && get(0) != null) {
            return get(0).size();
        }
        return 0;
    }

    public int sizeLista(int indice) {

        if (size() > indice) {

            if (get(indice) != null) {
                return get(indice).size();
            }
        }

        return 0;
    }

    public void clearAddAllLista(ArrayList<ModeloSQL> listaClon) {

        if (size() > 0) {

            get(0).clear();
            get(0).addAll(listaClon);
        }
    }

    public void clearAddAllLista(ListaModeloSQL listaClon) {

        get(0).clear();
        get(0).addAll(listaClon.getLista());
    }

    public void clearAddAll(ListaModeloSQL listaModeloSQLClon) {

        clear();
        addAll(listaModeloSQLClon.getListas());
    }

    public void setLista(ArrayList<ModeloSQL> lista) {
        set(0, lista);
    }

    public void setLista(ListaModeloSQL lista) {
        set(0, lista.getLista());
    }

    public void setLista(String[] campos) {
        set(0, queryList(campos));
    }

    public void setLista(String[] campos, String seleccion, String orden) {
        set(0, queryList(campos, seleccion, orden));
    }

    public void setLista(String[] campos, String id, String tablaCab, String seleccion, String orden) {
        set(0, queryListDetalle(campos, id, tablaCab, seleccion, orden));
    }

    public void setLista(int indice, ArrayList<ModeloSQL> lista) {
        set(indice, lista);
    }

    public void setLista(int indice, ListaModeloSQL lista) {
        set(indice, lista.getLista());
    }

    public void setLista(int indice, String[] campos) {
        set(indice, queryList(campos));
    }

    public void setLista(int indice, String[] campos, String seleccion, String orden) {
        set(indice, queryList(campos, seleccion, orden));
    }

    public void setLista(int indice, String[] campos, String id, String tablaCab, String seleccion, String orden) {
        set(indice, queryListDetalle(campos, id, tablaCab, seleccion, orden));
    }

    public boolean chech() {
        if (this != null) {
            return size() > 0;
        }
        return false;
    }

    public boolean chechLista() {
        if (get(0) != null) {
            return get(0).size() > 0;
        }
        return false;
    }

    public boolean chechLista(int indice) {
        if (get(indice) != null) {
            return get(indice).size() > 0;
        }
        return false;
    }

}
