package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import jjlacode.com.androidutils.FragmentC;
import jjlacode.com.androidutils.FragmentCUD;
import jjlacode.com.androidutils.FragmentUD;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.utilities.CommonPry;

public class FragmentUDPreferencias extends FragmentCUD implements CommonPry.Constantes {

    private CheckBox prioridad;
    private EditText diaspasados;
    private EditText diasfuturos;

    public FragmentUDPreferencias() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ud_preferencias, container, false);

        btnsave = view.findViewById(R.id.btnsaveudpref);
        prioridad = view.findViewById(R.id.chprioridadudpref);
        diasfuturos = view.findViewById(R.id.etdiasdespagendaudpref);
        diaspasados = view.findViewById(R.id.etdiaspasadosagendaudpref);

        SharedPreferences preferences=getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);

        if (preferences.getBoolean(PRIORIDAD,true)){

            prioridad.setChecked(true);


        }

        diasfuturos.setText(String.valueOf(preferences.getInt(DIASFUTUROS,90)));
        diaspasados.setText(String.valueOf(preferences.getInt(DIASPASADOS,20)));

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        return view;
    }

    @Override
    protected void update() {

        SharedPreferences preferences=getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean(PRIORIDAD,prioridad.isChecked());
        CommonPry.prioridad = prioridad.isChecked();
        editor.putInt(DIASPASADOS,Integer.parseInt(diaspasados.getText().toString()));
        CommonPry.diaspasados = Integer.parseInt(diaspasados.getText().toString());
        editor.putInt(DIASFUTUROS,Integer.parseInt(diasfuturos.getText().toString()));
        CommonPry.diasfuturos = Integer.parseInt(diasfuturos.getText().toString());
        editor.apply();

        icFragmentos.ejecutarEnActivity();

        Toast.makeText(getActivity(), "Preferencias guardadas", Toast.LENGTH_LONG).show();


    }

    @Override
    protected void delete(){


    }

    @Override
    protected boolean registrar() {
        return false;
    }

    @Override
    protected boolean contenedor() {
        return false;
    }

    @Override
    protected void cambiarFragment() {

    }


}