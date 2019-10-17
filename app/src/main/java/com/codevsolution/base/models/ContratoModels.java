package com.codevsolution.base.models;

import com.codevsolution.base.javautil.JavaUtil;

import java.util.ArrayList;
import java.util.UUID;

public class ContratoModels implements JavaUtil.Constantes {

    public interface Modelos {

        String MODELO_PERSONA = "Persona";

        String PERSONA_ID = MOD_ID + MODELO_PERSONA;
        String PERSONA_NOMBRE = MOD_NOMBRE + MODELO_PERSONA;
        String PERSONA_APELLIDOS = MOD_APELLIDOS + MODELO_PERSONA;
        String PERSONA_DIRECCION = MOD_DIRECCION + MODELO_PERSONA;
        String PERSONA_TELEFONO = MOD_TELEFONO + MODELO_PERSONA;
        String PERSONA_EMAIL = MOD_EMAIL + MODELO_PERSONA;


        String[] CAMPOS_PERSONA = {

                MODELO_PERSONA,
                PERSONA_ID, STRING,
                PERSONA_APELLIDOS, STRING,
                PERSONA_DIRECCION, STRING,
                PERSONA_TELEFONO, STRING,
                PERSONA_EMAIL, STRING
        };
    }

    public static ArrayList<String[]> obtenerListaCampos() {

        ArrayList<String[]> listaCampos = new ArrayList<>();
        listaCampos.add(Modelos.CAMPOS_PERSONA);


        return listaCampos;
    }

    public static String[] obtenerCampos(String modelo) {

        ArrayList<String[]> listaCampos = obtenerListaCampos();

        for (String[] campo : listaCampos) {
            if (campo[1].equals(modelo)) {
                return campo;
            }
        }

        return null;
    }

    public static String generarIdModelo(String modelo) {
        return modelo + UUID.randomUUID().toString();
    }
}
