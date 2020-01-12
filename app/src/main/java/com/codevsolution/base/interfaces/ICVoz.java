package com.codevsolution.base.interfaces;

import android.os.Bundle;

import com.codevsolution.base.models.DestinosVoz;

import java.util.ArrayList;

public interface ICVoz {

    Bundle processMsg(String mensaje);

    boolean comprobarDestino(String msg);

    boolean comprobarNuevoDestino(String msg);

    ArrayList<DestinosVoz> getListaDestinosVoz();

    ArrayList<DestinosVoz> getListaNuevosDestinosVoz();
}
