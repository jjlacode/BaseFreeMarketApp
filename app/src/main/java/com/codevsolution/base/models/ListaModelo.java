package com.codevsolution.base.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

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

    public ArrayList<Modelo> getLista() {

        if (size() > 0) {

            return get(0);
        }
        return null;
    }

    public ArrayList<Modelo> getLista(int indice) {

        if (size() > indice) {

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

    public String nombreModelo(int posicion) {

        return get(0).get(posicion).getNombreModelo();
    }

    public String nombreModelo(int indice, int posicion) {

        return get(indice).get(posicion).getNombreModelo();
    }

    public Modelo getItem(int posicion) {

        return get(0).get(posicion);
    }

    public Modelo getItem(int indice, int posicion) {

        return get(indice).get(posicion);
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

        if (sizeLista() > 0) {
            get(0).addAll(listaModeloClon.getLista());
        }
    }

    public void addAllLista(int indiceDestino, ListaModelo listaModeloClon) {

        if (size() > indiceDestino) {

            get(indiceDestino).addAll(listaModeloClon.getLista());
        }
    }

    public void addAllLista(int indiceOrigen, int indiceDestino, ListaModelo listaModeloClon) {

        if (size() > indiceDestino) {

            get(indiceDestino).addAll(listaModeloClon.getLista(indiceOrigen));
        }
    }

    public int sizeLista() {

        if (size() > 0 && get(0) != null) {
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

    public void clearAddAllLista(ArrayList<Modelo> listaClon) {

        if (size() > 0) {

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

    public void setLista(int indice, ArrayList<Modelo> lista) {
        set(indice, lista);
    }

    public void setLista(int indice, ListaModelo lista) {
        set(indice, lista.getLista());
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
