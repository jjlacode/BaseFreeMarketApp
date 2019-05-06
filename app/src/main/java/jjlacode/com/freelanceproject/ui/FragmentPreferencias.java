package jjlacode.com.freelanceproject.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import jjlacode.com.freelanceproject.util.FragmentBase;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.CommonPry;

public class FragmentPreferencias extends FragmentBase implements CommonPry.Constantes {

    private CheckBox prioridad;
    private EditText diaspasados;
    private EditText diasfuturos;
    private Button btnsave;

    public FragmentPreferencias() {
        // Required empty public constructor
    }

    @Override
    protected void setLayout() {

        layout = R.layout.fragment_preferencias;

    }

    @Override
    protected void setInicio() {

        btnsave = view.findViewById(R.id.btnsaveudpref);
        prioridad = view.findViewById(R.id.chprioridadudpref);
        diasfuturos = view.findViewById(R.id.etdiasdespagendaudpref);
        diaspasados = view.findViewById(R.id.etdiaspasadosagendaudpref);


        prioridad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                new CommonPry.Calculos.Tareafechas().execute();

            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        SharedPreferences preferences=getActivity().getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);

        if (preferences.getBoolean(PRIORIDAD,true)){

            prioridad.setChecked(true);


        }

        diasfuturos.setText(String.valueOf(preferences.getInt(DIASFUTUROS,90)));
        diaspasados.setText(String.valueOf(preferences.getInt(DIASPASADOS,20)));

    }

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

        CommonPry.setNamefdef();

        Toast.makeText(getActivity(), "Preferencias guardadas", Toast.LENGTH_LONG).show();


    }



}