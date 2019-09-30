package com.codevsolution.base.json;

import android.util.JsonReader;

import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.Modelo;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public ListaModelo leerFlujoJson(InputStream in) throws IOException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
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
            Modelo modelo = new Modelo(modeloJson.getEstructura(), modeloJson.getValores());
            listaModelos.addModelo(modelo);
        }
        return listaModelos;
    }
}
