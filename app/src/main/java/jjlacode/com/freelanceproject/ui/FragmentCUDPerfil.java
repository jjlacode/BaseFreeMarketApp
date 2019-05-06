package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import jjlacode.com.freelanceproject.util.FragmentCUD;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.util.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.CommonPry;

public class FragmentCUDPerfil extends FragmentCUD implements ContratoPry.Tablas {


    private Modelo perfil;
    private EditText nombre;
    private EditText descripcion;
    private EditText lunes;
    private EditText martes;
    private EditText miercoles;
    private EditText jueves;
    private EditText viernes;
    private EditText sabado;
    private EditText domingo;
    private EditText vacaciones;
    private EditText sueldo;
    private Button btnperfilact;
    private TextView activo;


    private static ConsultaBD consulta = new ConsultaBD();

    public FragmentCUDPerfil() {
        // Required empty public constructor
    }


    @Override
    protected void setTabla() {

    }

    @Override
    protected void setTablaCab() {

    }

    @Override
    protected void setContext() {

    }

    @Override
    protected void setCampos() {

    }

    @Override
    protected void setCampoID() {

    }

    @Override
    protected void setBundle() {

        if (bundle!=null) {

            perfil = (Modelo) bundle.getSerializable(TABLA_PERFIL);
            if (perfil!=null) {
                id = perfil.getString(PERFIL_ID_PERFIL);
                System.out.println("id = " + id);
            }
            enviarAct();
        }

    }

    @Override
    protected void setDatos() {

        nombre.setText(perfil.getString(PERFIL_NOMBRE));
        descripcion.setText(perfil.getString(PERFIL_DESCRIPCION));
        lunes.setText(perfil.getString(PERFIL_HORASLUNES));
        martes.setText(perfil.getString(PERFIL_HORASMARTES));
        miercoles.setText(perfil.getString(PERFIL_HORASMIERCOLES));
        jueves.setText(perfil.getString(PERFIL_HORASJUEVES));
        viernes.setText(perfil.getString(PERFIL_HORASVIERNES));
        sabado.setText(perfil.getString(PERFIL_HORASSABADO));
        domingo.setText(perfil.getString(PERFIL_HORASDOMINGO));
        vacaciones.setText(perfil.getString(PERFIL_VACACIONES));
        sueldo.setText(JavaUtil.formatoMonedaLocal(perfil.getDouble(PERFIL_SUELDO)));
        btndelete.setVisibility(View.VISIBLE);
        btnperfilact.setVisibility(View.VISIBLE);

        if (perfil.getString(PERFIL_NOMBRE).equals(CommonPry.perfila)){

            activo.setVisibility(View.VISIBLE);
            btnperfilact.setVisibility(View.GONE);
        }else{

            activo.setVisibility(View.GONE);
            btnperfilact.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void setAcciones() {

        btnperfilact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonPry.perfila = nombre.getText().toString();
                activo.setVisibility(View.VISIBLE);
                btnperfilact.setVisibility(View.GONE);

                SharedPreferences preferences=getContext().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("perfil_activo", CommonPry.perfila);
                editor.apply();

                new CommonPry.Calculos.Tareafechas().execute();

                CommonPry.setNamefdef();
            }
        });


    }


    @Override
    protected void setLayout() {

        layout = R.layout.fragment_cud_perfil;

    }

    @Override
    protected void setInicio() {

        btnsave = view.findViewById(R.id.perfil_ud_btn_save);
        btndelete = view.findViewById(R.id.perfil_ud_btn_del);
        btnback = view.findViewById(R.id.perfil_ud_btn_back);
        nombre = view.findViewById(R.id.etnomudperfil);
        descripcion = view.findViewById(R.id.etdescudperfil);
        lunes = view.findViewById(R.id.ethlunesudperfil);
        martes = view.findViewById(R.id.ethmartesudperfil);
        miercoles = view.findViewById(R.id.ethmiercolesudperfil);
        jueves = view.findViewById(R.id.ethjuevesudperfil);
        viernes = view.findViewById(R.id.ethviernesudperfil);
        sabado = view.findViewById(R.id.ethsabadoudperfil);
        domingo = view.findViewById(R.id.ethdomingoudperfil);
        vacaciones = view.findViewById(R.id.etvacaudperfil);
        sueldo = view.findViewById(R.id.etsueldocudperfil);
        btnperfilact = view.findViewById(R.id.btnperfilactudperfil);
        activo = view.findViewById(R.id.tvpactivocudperfil);

    }


    @Override
    protected void setNuevo() {

        lunes.setText("");
        martes.setText("");
        miercoles.setText("");
        jueves.setText("");
        viernes.setText("");
        sabado.setText("");
        domingo.setText("");
        vacaciones.setText("");
        sueldo.setText(JavaUtil.formatoMonedaLocal(0));
        activo.setVisibility(View.GONE);
        btnperfilact.setVisibility(View.GONE);
        btndelete.setVisibility(View.GONE);

    }

    @Override
    protected boolean update() {

        super.update();

        new CommonPry.Calculos.Tareafechas().execute();

        CommonPry.setNamefdef();

        return true;

    }

    @Override
    protected boolean delete() {



        if (!perfil.getString(PERFIL_NOMBRE).equals(CommonPry.perfila)) {
            super.delete();
            consulta.deleteRegistro(tabla, id);
        }else{
            Toast.makeText(getContext(),"No se puede borrar el perfil activo", Toast.LENGTH_SHORT).show();
        }

        return true;

    }

    @Override
    protected boolean registrar() {

        super.registrar();

        consulta.insertRegistro(tabla,valores);

        return true;

    }

    @Override
    protected void setContenedor() {

        consulta.putDato(valores,campos,PERFIL_NOMBRE,nombre.getText().toString());
        consulta.putDato(valores,campos,PERFIL_DESCRIPCION,descripcion.getText().toString());
        consulta.putDato(valores,campos,PERFIL_HORASLUNES,JavaUtil.comprobarInteger(lunes.getText().toString()));
        consulta.putDato(valores,campos,PERFIL_HORASMARTES,JavaUtil.comprobarInteger(martes.getText().toString()));
        consulta.putDato(valores,campos,PERFIL_HORASMIERCOLES,JavaUtil.comprobarInteger(miercoles.getText().toString()));
        consulta.putDato(valores,campos,PERFIL_HORASJUEVES,JavaUtil.comprobarInteger(jueves.getText().toString()));
        consulta.putDato(valores,campos,PERFIL_HORASVIERNES,JavaUtil.comprobarInteger(viernes.getText().toString()));
        consulta.putDato(valores,campos,PERFIL_HORASSABADO,JavaUtil.comprobarInteger(sabado.getText().toString()));
        consulta.putDato(valores,campos,PERFIL_HORASDOMINGO,JavaUtil.comprobarInteger(domingo.getText().toString()));
        consulta.putDato(valores,campos,PERFIL_VACACIONES,JavaUtil.comprobarInteger(vacaciones.getText().toString()));
        consulta.putDato(valores,campos,PERFIL_SUELDO,JavaUtil.comprobarDouble(sueldo.getText().toString()));


    }


    protected void cambiarFragment(){

        super.cambiarFragment();

        icFragmentos.enviarBundleAFragment(bundle, new FragmentPerfil());

    }

    @Override
    protected void setcambioFragment() {

    }

}