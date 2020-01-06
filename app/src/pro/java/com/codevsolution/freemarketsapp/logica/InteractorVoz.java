package com.codevsolution.freemarketsapp.logica;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.models.DestinosVoz;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.ui.CalendarioEventos;
import com.codevsolution.freemarketsapp.ui.FragmentCRUDCliente;
import com.codevsolution.freemarketsapp.ui.FragmentCRUDEvento;
import com.codevsolution.freemarketsapp.ui.FragmentCRUDProducto;
import com.codevsolution.freemarketsapp.ui.FragmentCRUDProyecto;
import com.codevsolution.freemarketsapp.ui.FragmentNuevaNota;
import com.codevsolution.freemarketsapp.ui.FragmentNuevoEvento;
import com.codevsolution.freemarketsapp.ui.MenuInicio;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.codevsolution.base.android.AppActivity.getAppContext;
import static com.codevsolution.base.settings.PreferenciasBase.CLAVEVOZ;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposEvento.TIPOEVENTOCITA;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposEvento.TIPOEVENTOEMAIL;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposEvento.TIPOEVENTOEVENTO;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposEvento.TIPOEVENTOLLAMADA;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposNota.NOTAAUDIO;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposNota.NOTAIMAGEN;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposNota.NOTATEXTO;
import static com.codevsolution.freemarketsapp.logica.Interactor.TiposNota.NOTAVIDEO;

public class InteractorVoz extends InteractorBase implements InteractorBase.Constantes {

    static Context context = AppActivity.getAppContext();
    static Bundle bundle;
    private static String orden;
    private static boolean nota;
    private static boolean evento;
    private static boolean buscaTmp;

    public static Bundle processMsg(String mensaje) {

        if (bundle == null) {
            bundle = new Bundle();
        }

        StringBuilder res = new StringBuilder();
        String[] results = mensaje.split(Pattern.quote(" "));
        orden = "";

        boolean valido = false;
        ArrayList<String> resTmp = new ArrayList<>();
        String clave = AndroidUtil.getSharePreference(context, PREFERENCIAS, CLAVEVOZ, NULL);
        for (String result : results) {
            if (clave != null && !result.toLowerCase().contains(clave.toLowerCase())) {
                resTmp.add(result);
            } else if (clave != null && result.toLowerCase().contains(clave.toLowerCase())) {
                valido = true;
                System.out.println("Clave correcta");
            }
        }

        if (valido) {

            for (String msg : resTmp) {

                System.out.println("msg = " + msg);
                if (!msg.isEmpty()) {

                    if (msg.contains(context.getString(R.string.abrir)) ||
                            msg.contains(context.getString(R.string.abre)) ||
                            msg.contains(context.getString(R.string.ir_a))) {

                        orden = DESTINO;

                    } else if (buscaTmp) {

                        comprobarDestino(msg);
                        buscaTmp = false;

                    } else if (msg.contains(context.getString(R.string.busca))) {

                        orden = BUSCA;
                        buscaTmp = true;

                    } else if (msg.contains(context.getString(R.string.ayuda).toLowerCase())) {

                        orden = AYUDA;

                    } else if (orden.equals(NUEVO) && msg.contains(context.getString(R.string.audio).toLowerCase()) ||
                            msg.contains(context.getString(R.string.nuevo_audio).toLowerCase())) {

                        nota = true;
                        bundle.putString(TIPO, NOTAAUDIO);

                    } else if (msg.contains(context.getString(R.string.texto).toLowerCase())) {

                        nota = true;
                        bundle.putString(TIPO, NOTATEXTO);

                    } else if (msg.contains(context.getString(R.string.video).toLowerCase())) {
                        nota = true;
                        bundle.putString(TIPO, NOTAVIDEO);

                    } else if (msg.contains(context.getString(R.string.imagen).toLowerCase())) {
                        nota = true;
                        bundle.putString(TIPO, NOTAIMAGEN);

                    } else if (msg.contains(context.getString(R.string.cita).toLowerCase())) {

                        evento = true;
                        bundle.putString(TIPO, TIPOEVENTOCITA);

                    } else if (msg.contains(context.getString(R.string.llamada).toLowerCase())) {
                        evento = true;
                        bundle.putString(TIPO, TIPOEVENTOLLAMADA);

                    } else if (msg.contains(context.getString(R.string.email).toLowerCase())) {
                        evento = true;
                        bundle.putString(TIPO, TIPOEVENTOEMAIL);

                    } else if (msg.contains(context.getString(R.string.evento).toLowerCase())) {
                        evento = true;
                        bundle.putString(TIPO, TIPOEVENTOEVENTO);

                    } else if (msg.contains(context.getString(R.string.nuevo).toLowerCase()) ||
                            msg.contains(context.getString(R.string.nueva).toLowerCase()) ||
                            msg.contains(context.getString(R.string.crear).toLowerCase())) {

                        orden = NUEVO;

                    } else {
                        System.out.println("Añadiendo " + msg + " a res");
                        res.append(msg);
                        res.append(" ");
                        if (resTmp.indexOf(msg) == resTmp.size() - 1 && res.length() > 0 && res.charAt(res.length() - 1) == ' ') {
                            res.delete(res.length() - 1, res.length() - 1);
                        }
                        System.out.println("res.toString() = " + res.toString());
                    }

                }
            }
            if (orden.equals(NUEVO)) {
                if (evento) {
                    comprobarNuevoDestino(context.getString(R.string.evento));
                    evento = false;
                } else if (nota) {
                    comprobarNuevoDestino(context.getString(R.string.nota));
                    nota = false;
                } else {
                    comprobarNuevoDestino(res.toString());
                }
            } else if (orden.equals(BUSCA) && !res.toString().isEmpty()) {
                bundle.putString(BUSCA, res.toString());
            } else if (orden.equals(DESTINO)) {
                comprobarDestino(res.toString());
            } else if (orden.equals(AYUDA) && !res.toString().isEmpty()) {
                bundle.putString(WEB, res.toString());
            }
            return bundle;
        }

        return null;
    }

    private static boolean comprobarDestino(String msg) {
        ArrayList<DestinosVoz> destinos = getListaDestinosVoz();
        for (DestinosVoz destino : destinos) {
            if (msg.trim().toLowerCase().contains(destino.getDestino().trim().toLowerCase()) ||
                    destino.getDestino().trim().toLowerCase().contains(msg.trim().toLowerCase()) ||
                    msg.toLowerCase().contains(destino.getDestino().toLowerCase()) ||
                    destino.getDestino().toLowerCase().contains(msg.toLowerCase())) {
                bundle.putString(TAGPERS, ((Fragment) destino.getFragment()).getClass().getName());
                return true;
            }
        }
        return false;
    }

    private static boolean comprobarNuevoDestino(String msg) {
        ArrayList<DestinosVoz> destinos = getListaNuevosDestinosVoz();
        for (DestinosVoz destino : destinos) {
            if (msg.trim().toLowerCase().contains(destino.getDestino().trim().toLowerCase()) ||
                    destino.getDestino().trim().toLowerCase().contains(msg.trim().toLowerCase()) ||
                    msg.toLowerCase().contains(destino.getDestino().toLowerCase()) ||
                    destino.getDestino().toLowerCase().contains(msg.toLowerCase())) {
                bundle.putString(TAGPERS, ((Fragment) destino.getFragment()).getClass().getName());
                bundle.putBoolean(NUEVOREGISTRO, true);
                return true;
            }
        }
        return false;
    }

    public static ArrayList<DestinosVoz> getListaDestinosVoz() {

        ArrayList<DestinosVoz> listaDestinos = new ArrayList<>();
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.inicio).toLowerCase(), new MenuInicio()));
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.calendario).toLowerCase(), new CalendarioEventos()));
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.cliente).toLowerCase(), new FragmentCRUDCliente()));
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.prospecto).toLowerCase(), new FragmentCRUDCliente()));
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.clientes).toLowerCase(), new FragmentCRUDCliente()));
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.prospectos).toLowerCase(), new FragmentCRUDCliente()));
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.evento).toLowerCase(), new FragmentCRUDEvento()));
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.eventos).toLowerCase(), new FragmentCRUDEvento()));
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.producto).toLowerCase(), new FragmentCRUDProducto()));
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.productos).toLowerCase(), new FragmentCRUDProducto()));
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.proyecto).toLowerCase(), new FragmentCRUDProyecto()));
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.proyectos).toLowerCase(), new FragmentCRUDProyecto()));
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.presupuesto).toLowerCase(), new FragmentCRUDProyecto()));
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.presupuestos).toLowerCase(), new FragmentCRUDProyecto()));

        return listaDestinos;
    }

    public static ArrayList<DestinosVoz> getListaNuevosDestinosVoz() {

        ArrayList<DestinosVoz> listaDestinos = new ArrayList<>();
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.evento).toLowerCase(),
                new FragmentNuevoEvento(), true));
        listaDestinos.add(new DestinosVoz(getAppContext().getString(R.string.nota).toLowerCase(),
                new FragmentNuevaNota(), true));

        return listaDestinos;
    }

}
