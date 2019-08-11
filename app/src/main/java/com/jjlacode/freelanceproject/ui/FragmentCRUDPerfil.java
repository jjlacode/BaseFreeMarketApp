package com.jjlacode.freelanceproject.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.adapter.BaseViewHolder;
import com.jjlacode.base.util.adapter.ListaAdaptadorFiltroModelo;
import com.jjlacode.base.util.adapter.TipoViewHolder;
import com.jjlacode.base.util.android.AppActivity;
import com.jjlacode.base.util.crud.FragmentCRUD;
import com.jjlacode.base.util.crud.Modelo;
import com.jjlacode.base.util.sqlite.ConsultaBD;
import com.jjlacode.freelanceproject.CommonPry;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.sqlite.ContratoPry;

import java.util.ArrayList;

public class FragmentCRUDPerfil extends FragmentCRUD implements ContratoPry.Tablas {


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

    public FragmentCRUDPerfil() {
        // Required empty public constructor
    }

    @Override
    protected TipoViewHolder setViewHolder(View view){

        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<Modelo> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    @Override
    protected void setLista() {

    }

    @Override
    protected void setImagen() {
    }

    @Override
    protected void setTabla() {

        tabla = TABLA_PERFIL;

    }

    @Override
    protected void setTablaCab() {

        tablaCab = ContratoPry.getTabCab(tabla);
    }

    @Override
    protected void setCampos() {

        campos = ContratoPry.obtenerCampos(tabla);

    }

    @Override
    protected void setDatos() {

        nombre.setText(modelo.getString(PERFIL_NOMBRE));
        descripcion.setText(modelo.getString(PERFIL_DESCRIPCION));
        lunes.setText(modelo.getString(PERFIL_HORASLUNES));
        martes.setText(modelo.getString(PERFIL_HORASMARTES));
        miercoles.setText(modelo.getString(PERFIL_HORASMIERCOLES));
        jueves.setText(modelo.getString(PERFIL_HORASJUEVES));
        viernes.setText(modelo.getString(PERFIL_HORASVIERNES));
        sabado.setText(modelo.getString(PERFIL_HORASSABADO));
        domingo.setText(modelo.getString(PERFIL_HORASDOMINGO));
        vacaciones.setText(modelo.getString(PERFIL_VACACIONES));
        sueldo.setText(JavaUtil.formatoMonedaLocal(modelo.getDouble(PERFIL_SUELDO)));
        btndelete.setVisibility(View.VISIBLE);
        btnperfilact.setVisibility(View.VISIBLE);

        if (modelo.getString(PERFIL_NOMBRE)!=null && modelo.getString(PERFIL_NOMBRE).equals(CommonPry.perfila)){

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

                subTitulo = CommonPry.setNamefdef();
                enviarAct();
            }
        });


    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.perfil;
        tituloPlural = R.string.perfiles;
        tituloNuevo = R.string.nuevo_perfil;
    }


    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_perfil;
        layoutItem = R.layout.item_list_perfil;

    }

    @Override
    protected void setInicio() {

        nombre = (EditText) ctrl(R.id.etnomudperfil, PERFIL_NOMBRE);
        descripcion = (EditText) ctrl(R.id.etdescudperfil, PERFIL_DESCRIPCION);
        lunes = (EditText) ctrl(R.id.ethlunesudperfil, PERFIL_HORASLUNES);
        martes = (EditText) ctrl(R.id.ethmartesudperfil, PERFIL_HORASMARTES);
        miercoles = (EditText) ctrl(R.id.ethmiercolesudperfil, PERFIL_HORASMIERCOLES);
        jueves = (EditText) ctrl(R.id.ethjuevesudperfil, PERFIL_HORASJUEVES);
        viernes = (EditText) ctrl(R.id.ethviernesudperfil, PERFIL_HORASVIERNES);
        sabado = (EditText) ctrl(R.id.ethsabadoudperfil, PERFIL_HORASSABADO);
        domingo = (EditText) ctrl(R.id.ethdomingoudperfil, PERFIL_HORASDOMINGO);
        vacaciones = (EditText) ctrl(R.id.etvacaudperfil, PERFIL_VACACIONES);
        sueldo = (EditText) ctrl(R.id.etsueldocudperfil, PERFIL_SUELDO);
        btnperfilact = (Button) ctrl(R.id.btnperfilactudperfil);
        activo = (TextView) ctrl(R.id.tvpactivocudperfil);

    }


    @Override
    protected void setNuevo() {

        activo.setVisibility(View.GONE);
        btnperfilact.setVisibility(View.GONE);

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



        if (!modelo.getString(PERFIL_NOMBRE).equals(CommonPry.perfila)) {
            super.delete();
            consulta.deleteRegistro(tabla, id);
        }else{
            Toast.makeText(getContext(),"No se puede borrar el modelo setActivo", Toast.LENGTH_SHORT).show();
        }

        return true;

    }


    @Override
    protected void setContenedor() {

        consulta.putDato(valores,campos,PERFIL_NOMBRE,nombre.getText().toString());
        consulta.putDato(valores,campos,PERFIL_DESCRIPCION,descripcion.getText().toString());
        consulta.putDato(valores, campos, PERFIL_HORASLUNES, JavaUtil.comprobarInteger(lunes.getText().toString()));
        consulta.putDato(valores, campos, PERFIL_HORASMARTES, JavaUtil.comprobarInteger(martes.getText().toString()));
        consulta.putDato(valores, campos, PERFIL_HORASMIERCOLES, JavaUtil.comprobarInteger(miercoles.getText().toString()));
        consulta.putDato(valores, campos, PERFIL_HORASJUEVES, JavaUtil.comprobarInteger(jueves.getText().toString()));
        consulta.putDato(valores, campos, PERFIL_HORASVIERNES, JavaUtil.comprobarInteger(viernes.getText().toString()));
        consulta.putDato(valores, campos, PERFIL_HORASSABADO, JavaUtil.comprobarInteger(sabado.getText().toString()));
        consulta.putDato(valores, campos, PERFIL_HORASDOMINGO, JavaUtil.comprobarInteger(domingo.getText().toString()));
        consulta.putDato(valores, campos, PERFIL_VACACIONES, JavaUtil.comprobarInteger(vacaciones.getText().toString()));
        consulta.putDato(valores, campos, PERFIL_SUELDO, JavaUtil.comprobarDouble(sueldo.getText().toString()));


    }

    protected void cambiarFragment(){

        super.cambiarFragment();

    }

    @Override
    protected void setcambioFragment() {

    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {


        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<Modelo> entrada) {

            TextView nombre,descripcion;
            CardView card;

            nombre = itemView.findViewById(R.id.tvnomlperfil);
            descripcion = itemView.findViewById(R.id.tvdesclperfil);
            card = itemView.findViewById(R.id.cardlperfil);

            nombre.setText(entrada.get(posicion).getString(PERFIL_NOMBRE));
            descripcion.setText(entrada.get(posicion).getString(PERFIL_DESCRIPCION));

            if (entrada.get(posicion).getString(PERFIL_NOMBRE).equals(CommonPry.perfila)){

                card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_ok));
            }

            super.setEntradas(posicion, view, entrada);
        }
    }

    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        TextView nombre,descripcion;
        CardView card;

        public ViewHolderRV(View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.tvnomlperfil);
            descripcion = itemView.findViewById(R.id.tvdesclperfil);
            card = itemView.findViewById(R.id.cardlperfil);
        }

        @Override
        public void bind(Modelo modelo) {

            nombre.setText(modelo.getString(PERFIL_NOMBRE));
            descripcion.setText(modelo.getString(PERFIL_DESCRIPCION));

            if (modelo.getString(PERFIL_NOMBRE).equals(CommonPry.perfila)){

                card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_ok));
            }
            super.bind(modelo);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }


    }