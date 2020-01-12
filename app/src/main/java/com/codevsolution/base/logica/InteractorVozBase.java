package com.codevsolution.base.logica;

import android.os.Bundle;

import com.codevsolution.base.interfaces.ICVoz;


public class InteractorVozBase extends InteractorBase implements InteractorBase.Constantes {

    public Bundle processMsg(String mensaje, ICVoz icVoz) {

        return icVoz.processMsg(mensaje);
    }

}
