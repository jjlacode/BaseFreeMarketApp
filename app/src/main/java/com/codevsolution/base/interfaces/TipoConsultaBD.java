package com.codevsolution.base.interfaces;

import android.net.Uri;

import java.util.ArrayList;

public interface TipoConsultaBD {

    Uri obtenerUriContenido(String tabla);

    String obtenerIdTabla(Uri uri, String tabla);

    Uri crearUriTabla(String id, String tabla);

    Uri crearUriTablaDetalle(String id, String secuencia, String tabla);

    Uri crearUriTablaDetalle(String id, int secuencia, String tabla);

    Uri crearUriTablaDetalleId(String id, String tabla, String tablaCab);

    String obtenerTabCab(String tabla);

    String[] obtenerCampos(String tabla);

    ArrayList<String[]> obtenerListaCampos();
}
