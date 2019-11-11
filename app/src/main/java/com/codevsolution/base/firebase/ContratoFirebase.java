package com.codevsolution.base.firebase;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.logica.InteractorBase;

public class ContratoFirebase implements InteractorBase.Constantes, JavaUtil.Constantes {

    static String idUser = AndroidUtil.getSharePreference(AppActivity.getAppContext(), USERID, USERID, NULL);

    public static String[] getRutaProductos() {
        return new String[]{PRODUCTOS};
    }

    public static String[] getRutaRaiz() {
        return new String[]{};
    }

    public static String[] getRutaConfig() {
        return new String[]{CONFIG};
    }

    public static String[] getRutaPro() {
        return new String[]{PRO};
    }

    public static String[] getRutaCli() {
        return new String[]{CLI};
    }

    public static String[] getRutaIndiceProducto(String tipo) {
        return new String[]{INDICE + tipo, idUser};
    }

    public static String[] getRutaPerfilTipo(String tipo) {
        return new String[]{PERFIL, tipo};
    }

    public static String[] getRutaSuscripciones(String idTipo) {
        return new String[]{SUSCRIPCIONES, idTipo};
    }

    public static String[] getRutaUser() {
        return new String[]{idUser};
    }

    public static String[] getRutaSuscripcionesProUser() {
        return new String[]{idUser, SUSCRIPCIONESPRO};
    }

    public static String[] getRutaSuscripcionesSorteosCliUser() {
        return new String[]{idUser, SUSCRIPCIONESSORTEOSCLI};
    }

    public static String[] getRutaSuscripcionesSorteosProUser() {
        return new String[]{idUser, SUSCRIPCIONESSORTEOSPRO};
    }


}
