package com.codevsolution.freemarketsapp.sqlite;

import android.net.Uri;

import com.codevsolution.base.interfaces.TipoConsultaBD;
import com.codevsolution.base.javautil.JavaUtil;

public class ConsultaBD implements JavaUtil.Constantes, TipoConsultaBD {


    public ConsultaBD(){}

    @Override
    public  Uri obtenerUriContenido(String tabla) {

        if (ContratoPry.obtenerCampos(tabla) != null) {

            return ContratoPry.obtenerUriContenido(tabla);

        }

        return null;
    }

    @Override
    public  String obtenerIdTabla(Uri uri, String tabla) {

        if (ContratoPry.obtenerCampos(tabla) != null) {


            return ContratoPry.obtenerIdTabla(uri);

        }

        return null;
    }


    @Override
    public  Uri crearUriTabla(String id, String tabla) {

        if (ContratoPry.obtenerCampos(tabla) != null) {


            return ContratoPry.crearUriTabla(id,tabla);

        }


        return null;
    }

    @Override
    public  Uri crearUriTablaDetalle(String id, String secuencia, String tabla) {

        if (ContratoPry.obtenerCampos(tabla) != null) {


            return ContratoPry.crearUriTablaDetalle(id, secuencia,
                    tabla);

        }

        return null;
    }

    @Override
    public  Uri crearUriTablaDetalle(String id, int secuencia, String tabla) {

        if (ContratoPry.obtenerCampos(tabla) != null) {

            System.out.println("uri = " + ContratoPry.crearUriTablaDetalle(id, secuencia,
                    tabla));

            return ContratoPry.crearUriTablaDetalle(id, secuencia,
                    tabla);

        }

        return null;
    }

    @Override
    public  Uri crearUriTablaDetalleId(String id, String tabla, String tablaCab) {

        if (ContratoPry.obtenerCampos(tabla) != null) {


            return ContratoPry.crearUriTablaDetalleId(id, tabla,
                    tablaCab);

        }

        return null;
    }

    @Override
    public  String obtenerTabCab(String tabla) {

        if (ContratoPry.obtenerCampos(tabla) != null) {


            return ContratoPry.getTabCab(tabla);

        }

        return null;
    }

    @Override
    public  String[] obtenerCampos(String tabla) {

        if (ContratoPry.obtenerCampos(tabla) != null) {


            return ContratoPry.obtenerCampos(tabla);

        }

        return null;
    }

}