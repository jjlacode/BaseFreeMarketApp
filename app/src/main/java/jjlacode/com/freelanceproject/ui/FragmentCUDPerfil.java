package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import jjlacode.com.androidutils.FragmentCUD;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.utilities.CommonPry;

public class FragmentCUDPerfil extends FragmentCUD implements ContratoPry.Tablas {

    private String idPerfil;

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


    private static ConsultaBD consulta = new ConsultaBD();

    public FragmentCUDPerfil() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cud_perfil, container, false);

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

        bundle = getArguments();

        if (bundle!=null) {

            perfil = (Modelo) bundle.getSerializable(TABLA_PERFIL);
            idPerfil = perfil != null ? perfil.getString(PERFIL_ID_PERFIL) : null;
            System.out.println("idPerfil = " + idPerfil);
            namef = bundle.getString("namef");
            bundle = null;
            bundle = new Bundle();
            bundle.putString("namef",namef);
            icFragmentos.enviarBundleAActivity(bundle);
            bundle = null;
        }

        btnperfilact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonPry.perfila = nombre.getText().toString();

                SharedPreferences preferences=getContext().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("perfil_activo", CommonPry.perfila);
                editor.apply();

                icFragmentos.ejecutarEnActivity();
            }
        });

        btndelete.setVisibility(View.GONE);
        btnperfilact.setVisibility(View.GONE);

        if (idPerfil!=null){

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
            sueldo.setText(perfil.getString(PERFIL_SUELDO));
            btndelete.setVisibility(View.VISIBLE);
            btnperfilact.setVisibility(View.VISIBLE);
        }

        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                update();

                cambiarFragment();

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                delete();

               cambiarFragment();

            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cambiarFragment();

            }
        });

        return view;
    }


    @Override
    protected void update() {

        contenedor();

        if (idPerfil!=null) {
            consulta.updateRegistro(TABLA_PERFIL, idPerfil, valores);
            icFragmentos.ejecutarEnActivity();
        }else{

                registrar();
            }

    }

    @Override
    protected void delete() {

        consulta.deleteRegistro(TABLA_PERFIL,idPerfil);

    }

    @Override
    protected boolean registrar() {

        consulta.insertRegistro(TABLA_PERFIL,valores);
        return true;
    }

    @Override
    protected boolean contenedor(){

        valores = new ContentValues();

        consulta.putDato(valores,CAMPOS_PERFIL,PERFIL_NOMBRE,nombre.getText().toString());
        consulta.putDato(valores,CAMPOS_PERFIL,PERFIL_DESCRIPCION,descripcion.getText().toString());
        consulta.putDato(valores,CAMPOS_PERFIL,PERFIL_HORASLUNES,lunes.getText().toString());
        consulta.putDato(valores,CAMPOS_PERFIL,PERFIL_HORASMARTES,martes.getText().toString());
        consulta.putDato(valores,CAMPOS_PERFIL,PERFIL_HORASMIERCOLES,miercoles.getText().toString());
        consulta.putDato(valores,CAMPOS_PERFIL,PERFIL_HORASJUEVES,jueves.getText().toString());
        consulta.putDato(valores,CAMPOS_PERFIL,PERFIL_HORASVIERNES,viernes.getText().toString());
        consulta.putDato(valores,CAMPOS_PERFIL,PERFIL_HORASSABADO,sabado.getText().toString());
        consulta.putDato(valores,CAMPOS_PERFIL,PERFIL_HORASDOMINGO,domingo.getText().toString());
        consulta.putDato(valores,CAMPOS_PERFIL,PERFIL_VACACIONES,vacaciones.getText().toString());
        consulta.putDato(valores,CAMPOS_PERFIL,PERFIL_SUELDO,sueldo.getText().toString());


        return true;
    }

    protected void cambiarFragment(){

        bundle = new Bundle();
        bundle.putString("namef", namef);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentPerfil());

    }

}