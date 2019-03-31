package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import jjlacode.com.freelanceproject.sqlite.Contract;
import jjlacode.com.freelanceproject.R;

public class FragmentUDPreferencias extends Fragment {

    /*
    EditText nombreCliente,direccionCliente,telefonoCliente,emailCliente,contactoCliente;
    TextView tipoCliente;
    */
    //TODO campos preferencias
    private Button btnsave;

    public FragmentUDPreferencias() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ud_preferencias, container, false);

        btnsave = vista.findViewById(R.id.preferencias_ud_btn_save);

        //TODO asignaciones de campos preferencias con share

        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                updatePreferencias();

            }
        });

        return vista;
    }

    public void updatePreferencias() {

        ContentValues valores = new ContentValues();
        /*
        valores.put(Contract.ColumnasCliente.NOMBRE,nombreCliente.getText().toString());
        //TODO insertar valores al ContentValues para upddate
        */




    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}