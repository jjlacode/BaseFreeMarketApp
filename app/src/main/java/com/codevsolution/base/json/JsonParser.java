package com.codevsolution.base.json;

import android.util.JsonReader;

import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.ModeloSQL;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class JsonParser {

    public ListaModelo leerFlujoJsonSQL(InputStream in) throws IOException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        ArrayList<ModeloJson> modelosJson = new ArrayList();

        reader.beginArray();

        while (reader.hasNext()) {

            ModeloJson modeloJson = gson.fromJson(String.valueOf(reader), ModeloJson.class);
            modelosJson.add(modeloJson);
        }


        reader.endArray();
        reader.close();
        ListaModelo listaModelos = new ListaModelo();
        for (ModeloJson modeloJson : modelosJson) {
            ModeloSQL modeloSQL = new ModeloSQL(modeloJson.getEstructura(), modeloJson.getValores());
            listaModelos.addModelo(modeloSQL);
        }
        return listaModelos;
    }

    public ListaModelo leerFlujoJson(InputStream in) throws IOException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        ArrayList<ModeloJson> modelosJson = new ArrayList();

        reader.beginArray();

        while (reader.hasNext()) {

            ModeloJson modeloJson = gson.fromJson(String.valueOf(reader), ModeloJson.class);
            modelosJson.add(modeloJson);
        }


        reader.endArray();
        reader.close();
        ListaModelo listaModelos = new ListaModelo();
        for (ModeloJson modeloJson : modelosJson) {
            ModeloSQL modeloSQL = new ModeloSQL(modeloJson.getEstructura(), modeloJson.getValores());
            listaModelos.addModelo(modeloSQL);
        }
        return listaModelos;
    }
}
