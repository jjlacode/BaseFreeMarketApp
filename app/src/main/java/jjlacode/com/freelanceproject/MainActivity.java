package jjlacode.com.freelanceproject;

import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;

import jjlacode.com.freelanceproject.settings.SettingsActivity;
import jjlacode.com.freelanceproject.ui.FragmentAgenda;
import jjlacode.com.freelanceproject.ui.FragmentCRUDEvento;
import jjlacode.com.freelanceproject.ui.FragmentCRUDPartidaProyecto;
import jjlacode.com.freelanceproject.ui.FragmentCRUDPartidaBase;
import jjlacode.com.freelanceproject.util.CommonPry;
import jjlacode.com.freelanceproject.util.MainActivityBase;
import jjlacode.com.freelanceproject.ui.FragmentCRUDAmortizacion;
import jjlacode.com.freelanceproject.ui.FragmentCRUDCliente;
import jjlacode.com.freelanceproject.ui.FragmentCRUDGastoFijo;
import jjlacode.com.freelanceproject.ui.FragmentCRUDPerfil;
import jjlacode.com.freelanceproject.ui.FragmentCRUDProyecto;
import jjlacode.com.freelanceproject.util.VisorPDF;
import jjlacode.com.freelanceproject.util.VisorPDFEmail;


public class MainActivity extends MainActivityBase {



    @Override
    protected void acciones() {
        super.acciones();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activityBase in AndroidManifest.xml.
        int id = item.getItemId();
        bundle = new Bundle();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }if (id == R.id.action_inicio) {
            bundle.putString(ACTUAL, INICIO);
            recargarFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setOnNavigation(MenuItem item) {
        // Handle navigation view item clicks here.
        int itemId = item.getItemId();


        if (itemId == R.id.nav_clientes) {

            bundle.putString(ACTUAL, CLIENTE);
            recargarFragment();

        }
        else if (itemId == R.id.nav_proyectos) {

            bundle.putString(ACTUAL, PROYECTO);
            recargarFragment();

        } else if (itemId == R.id.nav_agenda) {

            bundle.putString(ACTUAL, INICIO);
            recargarFragment();

        }  else if (itemId == R.id.nav_perfiles) {

            bundle.putString(ACTUAL, PERFIL);
            recargarFragment();

        }else if (itemId == R.id.nav_eventos) {

            bundle.putString(ACTUAL, EVENTO);
            recargarFragment();

        }else if (itemId == R.id.nav_amortizacion) {

            bundle.putString(ACTUAL, AMORTIZACION);
            recargarFragment();

        }else if (itemId == R.id.nav_gastosfijos) {

            bundle.putString(ACTUAL, GASTOSFIJOS);
            recargarFragment();

        }else if (itemId == R.id.nav_partidabase) {

            bundle.putString(ACTUAL, PARTIDABASE);
            recargarFragment();

        }

    }

    protected void recargarFragment(){

        super.recargarFragment();

        String actual = bundle.getString(ACTUAL, INICIO);

        switch (actual){

            case PROYECTO:

            case COBROS:

            case HISTORICO:

            case PRESUPUESTO:

                enviarBundleAFragment(bundle, new FragmentCRUDProyecto());
                break;

            case PARTIDA:

                enviarBundleAFragment(bundle, new FragmentCRUDPartidaProyecto());
                break;

            case PROSPECTO:

            case CLIENTE:

                enviarBundleAFragment(bundle, new FragmentCRUDCliente());
                break;

            case INICIO:

                fab2.hide();
                fab.hide();
                toolbar.setSubtitle(CommonPry.setNamefdef());
                enviarBundleAFragment(bundle, new FragmentAgenda());
                break;
            case AMORTIZACION:

                enviarBundleAFragment(bundle, new FragmentCRUDAmortizacion());
                break;

            case PERFIL:

                enviarBundleAFragment(bundle, new FragmentCRUDPerfil());
                break;

            case GASTOSFIJOS:

                enviarBundleAFragment(bundle, new FragmentCRUDGastoFijo());
                break;

            case EVENTO:
                enviarBundleAFragment(bundle, new FragmentCRUDEvento());
                break;

            case PARTIDABASE:
                enviarBundleAFragment(bundle, new FragmentCRUDPartidaBase());
                break;

            case VISORPDFMAIL:
                enviarBundleAFragment(bundle, new VisorPDFEmail());
                break;

            case VISORPDF:
                enviarBundleAFragment(bundle, new VisorPDF());
                break;


        }
        bundle = null;
    }



}
