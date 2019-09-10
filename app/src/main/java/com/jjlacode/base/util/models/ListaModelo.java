package com.jjlacode.base.util.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import static com.jjlacode.base.util.JavaUtil.Constantes.CAMPO_SECUENCIA;
import static com.jjlacode.base.util.sqlite.ConsultaBD.queryList;
import static com.jjlacode.base.util.sqlite.ConsultaBD.queryListCampo;
import static com.jjlacode.base.util.sqlite.ConsultaBD.queryListDetalle;

public class ListaModelo extends ArrayList<ArrayList<Modelo>> implements Serializable {


    public ListaModelo(int initialCapacity) {
        super(initialCapacity);
    }

    public ListaModelo(@NonNull Collection<? extends ArrayList<Modelo>> c) {
        super(c);
    }

    public ListaModelo() {

        super();
        add(new ArrayList<Modelo>());
    }

    public ListaModelo(ArrayList<Modelo> lista) {

        super();

        add(lista);
    }

    public ListaModelo(ListaModelo listas) {

        super();
        addAll(listas);

    }

    public ListaModelo(String[] campos) {

        super();

        add(queryList(campos));
    }

    public ListaModelo(String[] campos, String id, String tablaCab, String seleccion, String orden) {

        super();

        add(queryListDetalle(campos, id, tablaCab, seleccion, orden));
    }

    public ListaModelo(String[] campos, String seleccion, String orden) {

        super();

        add(queryList(campos, seleccion, orden));
    }

    public ListaModelo(String[] campos, String campo, String valor, String orden) {

        super();
        add(queryListCampo(campos, campo, valor, orden));

    }

    public ListaModelo(String[] campos, String campo, String valor, String valor2, int flag, String orden) {

        super();
        add(queryList(campos, campo, valor, valor2, flag, orden));
    }

    public ArrayList<Modelo> getLista() {

        if (size()>0) {

            return get(0);
        }
        return null;
    }

    public ArrayList<Modelo> getLista(int indice) {

        if (size()>indice) {

            return get(indice);
        }

        return null;
    }

    public ArrayList<ArrayList<Modelo>> getListas() {
        return this;
    }

    public void addLista(ArrayList<Modelo> lista) {

        add(lista);
    }

    public void addLista(int indice, ArrayList<Modelo> lista) {

        add(indice, lista);

    }

    public void addLista(int indice, ListaModelo listaModelo) {

        add(indice, listaModelo.getLista());

    }

    public void addLista(int indiceOrigen, int indiceDestino, ListaModelo listaModelo) {

        add(indiceDestino, listaModelo.getLista(indiceOrigen));

    }

    public void removeLista() {

        remove(0);
    }

    public void removeLista(int indice) {

        remove(indice);

    }

    public void removeLista(ArrayList<Modelo> lista) {

        remove(lista);

    }

    public void removeLista(ListaModelo listaModelo) {

        remove(listaModelo.getLista());

    }

    public void removeLista(int indiceOrigen, ListaModelo listaModelo) {

        remove(listaModelo.getLista(indiceOrigen));

    }

    public void addModelo(Modelo modelo) {

        get(0).add(modelo);
        modelo.setPosicionLista(get(0).indexOf(modelo));
        modelo.setIndiceLista(0);
        modelo.setEnLista(true);
    }

    public void addModelo(int indice, Modelo modelo) {

        get(indice).add(modelo);
        modelo.setPosicionLista(get(indice).indexOf(modelo));
        modelo.setIndiceLista(indice);
        modelo.setEnLista(true);

    }

    public void removeModelo(int posicionModelo) {

        Modelo modelo = get(0).get(posicionModelo);
        modelo.setEnLista(false);
        modelo.setIndiceLista(0);
        modelo.setPosicionLista(0);
        get(0).remove(posicionModelo);
    }

    public void removeModelo(int indice, int posicionModelo) {

        Modelo modelo = get(indice).get(posicionModelo);
        modelo.setEnLista(false);
        modelo.setIndiceLista(0);
        modelo.setPosicionLista(0);
        get(indice).remove(posicionModelo);
    }

    public void removeModelo(Modelo modelo) {

        modelo.setEnLista(false);
        modelo.setIndiceLista(0);
        modelo.setPosicionLista(0);
        get(0).remove(modelo);
    }

    public void removeModelo(int indice, Modelo modelo) {

        modelo.setEnLista(false);
        modelo.setIndiceLista(0);
        modelo.setPosicionLista(0);
        get(indice).remove(modelo);
    }

    public void set(int posicion, Modelo modelo) {

        modelo.setPosicionLista(posicion);
        get(0).set(posicion, modelo);
    }

    public void set(int indice, int posicion, Modelo modelo) {

        modelo.setPosicionLista(posicion);
        get(indice).set(posicion, modelo);
    }

    public String tablaModelo(int posicion) {

        return get(0).get(posicion).getNombreTabla();
    }

    public String tablaModelo(int indice, int posicion) {

        return get(indice).get(posicion).getNombreTabla();
    }

    public Modelo getItem(int posicion) {

        return get(0).get(posicion);
    }

    public Modelo getItem(int indice, int posicion) {

        return get(indice).get(posicion);
    }


    public boolean esDetalle(int posicion) {

        Modelo modelo = get(0).get(posicion);
        String[] campos = modelo.getCampos();
        for (int i = 0; i < modelo.getNumcampos(); i++) {
            if (campos[i].equals(CAMPO_SECUENCIA)) {
                return true;
            }
        }
        return false;
    }

    public boolean esDetalle(int indice, int posicion) {

        Modelo modelo = get(indice).get(posicion);
        String[] campos = modelo.getCampos();
        for (int i = 0; i < modelo.getNumcampos(); i++) {
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

    public void addAllLista(ArrayList<Modelo> listaClon) {

        get(0).addAll(listaClon);
    }

    public void addAllLista(int indice, ArrayList<Modelo> listaClon) {

        get(indice).addAll(listaClon);
    }

    public void addAllLista(ListaModelo listaModeloClon) {

        if (sizeLista()>0) {
            get(0).addAll(listaModeloClon.getLista());
        }
    }

    public void addAllLista(int indiceDestino, ListaModelo listaModeloClon) {

        if (size()>indiceDestino) {

            get(indiceDestino).addAll(listaModeloClon.getLista());
        }
    }

    public void addAllLista(int indiceOrigen, int indiceDestino, ListaModelo listaModeloClon) {

        if (size()>indiceDestino) {

            get(indiceDestino).addAll(listaModeloClon.getLista(indiceOrigen));
        }
    }

    public int sizeLista() {

        if (this!=null && size()>0 && get(0) != null) {
            return get(0).size();
        }
        return 0;
    }

    public int sizeLista(int indice) {

        if (size()>indice) {

            if (get(indice) != null) {
                return get(indice).size();
            }
        }

        return 0;
    }

    public void clearAddAllLista(ArrayList<Modelo> listaClon) {

        if (size()>0) {

            get(0).clear();
            get(0).addAll(listaClon);
        }
    }

    public void clearAddAllLista(ListaModelo listaClon) {

        get(0).clear();
        get(0).addAll(listaClon.getLista());
    }

    public void clearAddAll(ListaModelo listaModeloClon) {

        clear();
        addAll(listaModeloClon.getListas());
    }

    public void setLista(ArrayList<Modelo> lista) {
        set(0, lista);
    }

    public void setLista(ListaModelo lista) {
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

    public void setLista(int indice, ArrayList<Modelo> lista) {
        set(indice, lista);
    }

    public void setLista(int indice, ListaModelo lista) {
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
