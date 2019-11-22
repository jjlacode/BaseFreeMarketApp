package com.codevsolution.freemarketsapp.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.AppActivity;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.FragmentCRUD;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.time.DatePickerFragment;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;
import java.util.Objects;

import static com.codevsolution.freemarketsapp.logica.Interactor.setNamefdef;

public class FragmentCRUDAmortizacion extends FragmentCRUD implements ContratoPry.Tablas {


    private EditMaterial nombre;
    private EditMaterial descripcion;
    private EditMaterial cantidad;
    private EditMaterial importe;
    private EditMaterial anios;
    private EditMaterial meses;
    private EditMaterial dias;
    private EditMaterial fecha;

    private long fechaCompra;
    private ImageView btnimgfecha;
    private ProgressBar bar;

    public FragmentCRUDAmortizacion() {
        // Required empty public constructor
    }
    @Override
    protected TipoViewHolder setViewHolder(View view){

        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos) {
        return new AdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }

    @Override
    protected void setTabla() {

        tabla = TABLA_AMORTIZACION;

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

        nombre.setText(modeloSQL.getString(AMORTIZACION_NOMBRE));
        descripcion.setText(modeloSQL.getString(AMORTIZACION_DESCRIPCION));
        cantidad.setText(modeloSQL.getString(AMORTIZACION_CANTIDAD));
        importe.setText(modeloSQL.getString(AMORTIZACION_PRECIO));
        fecha.setText(JavaUtil.getDate(modeloSQL.getLong(AMORTIZACION_FECHACOMPRA)));
        fechaCompra = modeloSQL.getLong(AMORTIZACION_FECHACOMPRA);
        anios.setText(modeloSQL.getString(AMORTIZACION_ANYOS));
        meses.setText(modeloSQL.getString(AMORTIZACION_MESES));
        dias.setText(modeloSQL.getString(AMORTIZACION_DIAS));

        long compra = modeloSQL.getLong(AMORTIZACION_FECHACOMPRA);
        long amort = (modeloSQL.getLong(AMORTIZACION_DIAS) * DIASLONG) +
                (modeloSQL.getLong(AMORTIZACION_MESES) * MESESLONG) +
                (modeloSQL.getLong(AMORTIZACION_ANYOS) * ANIOSLONG);
        long fechafin = compra + amort;
        long amortizado = JavaUtil.hoy()-compra;
        int res = (int) ((100/(double)(fechafin-compra))*amortizado);
        bar.setProgress(res);
        btndelete.setVisibility(View.VISIBLE);

    }

    @Override
    protected void setAcciones() {

        btnimgfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update();
                showDatePickerDialogComprada();
            }
        });

    }

    @Override
    protected void setTitulo() {
        tituloSingular = R.string.amortizacion;
        tituloPlural = R.string.amortizaciones;
        tituloNuevo = R.string.nueva_amortizacion;
    }

    @Override
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_crud_amortizacion;
        layoutItem = R.layout.item_list_amortizacion;

    }

    @Override
    protected void setInicio() {

        ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, frdetalle);

        imagen = vistaForm.addViewImagenLayout();

        nombre = (EditMaterial) ctrl(R.id.etnomcudamort, AMORTIZACION_NOMBRE);
        descripcion = (EditMaterial) ctrl(R.id.etdesccudamort, AMORTIZACION_DESCRIPCION);
        cantidad = (EditMaterial) ctrl(R.id.etcantcudamort, AMORTIZACION_CANTIDAD);
        importe = (EditMaterial) ctrl(R.id.etimpcudamort, AMORTIZACION_PRECIO);
        fecha = (EditMaterial) ctrl(R.id.tvfechacudamort);
        anios = (EditMaterial) ctrl(R.id.etanioscudamort, AMORTIZACION_ANYOS);
        meses = (EditMaterial) ctrl(R.id.etmesescudamort, AMORTIZACION_MESES);
        dias = (EditMaterial) ctrl(R.id.etdiascudamort, AMORTIZACION_DIAS);
        btnimgfecha = (ImageView) ctrl(R.id.btnimgfechacudamort);
        bar = (ProgressBar) ctrl(R.id.progressBarcudamort);


    }


    @Override
    protected void setNuevo() {

    }

    @Override
    protected boolean update() {

        if(super.update()) {

            Interactor.hora = Interactor.Calculos.calculoPrecioHora();

            return true;
        }
        return false;

    }

    @Override
    protected boolean delete() {

        if(super.delete()) {

            Interactor.hora = Interactor.Calculos.calculoPrecioHora();

            return true;
        }
        return false;

    }

    @Override
    protected boolean registrar() {

        if(super.registrar()) {

            Interactor.hora = Interactor.Calculos.calculoPrecioHora();

            return true;
        }
        return false;
    }

    @Override
    protected void setContenedor() {

        setDato(AMORTIZACION_NOMBRE,nombre.getText().toString());
        setDato(AMORTIZACION_DESCRIPCION,descripcion.getText().toString());
        setDato(AMORTIZACION_CANTIDAD, JavaUtil.comprobarDouble(cantidad.getText().toString()));
        setDato(AMORTIZACION_PRECIO, JavaUtil.comprobarDouble(importe.getText().toString()));
        setDato(AMORTIZACION_ANYOS, JavaUtil.comprobarInteger(anios.getText().toString()));
        setDato(AMORTIZACION_MESES, JavaUtil.comprobarInteger(meses.getText().toString()));
        setDato(AMORTIZACION_DIAS, JavaUtil.comprobarInteger(dias.getText().toString()));
        setDato(AMORTIZACION_FECHACOMPRA,fechaCompra);
        setDato(AMORTIZACION_RUTAFOTO, path);


    }

    @Override
    protected void setcambioFragment() {

        activityBase.toolbar.setSubtitle(setNamefdef());

    }

    private void showDatePickerDialogComprada() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance
                (fechaCompra > 0 ? fechaCompra : JavaUtil.hoy(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because january is zero
                        //String selectedDate = Interactor.twoDigits(day) + " / " +
                        //        Interactor.twoDigits(month+1) + " / " + year;
                        fechaCompra = JavaUtil.fechaALong(year, month, day);
                        //String selectedDate = Interactor.formatDateForUi(year,month,day);
                        String selectedDate = JavaUtil.getDate(fechaCompra);
                        fecha.setText(selectedDate);

                        update();
                        setDatos();

                    }
                });
        newFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "datePicker");
    }

    public class AdaptadorFiltroModelo extends ListaAdaptadorFiltroModelo {

        public AdaptadorFiltroModelo(Context contexto, int R_layout_IdView, ArrayList<ModeloSQL> entradas, String[] campos) {
            super(contexto, R_layout_IdView, entradas, campos);
        }

        @Override
        protected void setEntradas(int posicion, View itemView, ArrayList<ModeloSQL> entrada) {

            TextView nombre, descripcion, cantidad, importe, fecha, anios, meses, dias;
            ImageView imagen;
            CardView card;
            ProgressBar progressBar;

            nombre = itemView.findViewById(R.id.tvnomlamort);
            descripcion = itemView.findViewById(R.id.tvdesclamort);
            cantidad = itemView.findViewById(R.id.tvcantlamort);
            importe = itemView.findViewById(R.id.tvimplamort);
            fecha = itemView.findViewById(R.id.tvfechalamort);
            anios = itemView.findViewById(R.id.tvanioslamort);
            meses = itemView.findViewById(R.id.tvmeseslamort);
            dias = itemView.findViewById(R.id.tvdiaslamort);
            imagen = itemView.findViewById(R.id.imglamort);
            card = itemView.findViewById(R.id.cardlamort);
            progressBar = itemView.findViewById(R.id.progressBarlamort);

            nombre.setText(entrada.get(posicion).getString(AMORTIZACION_NOMBRE));
            descripcion.setText(entrada.get(posicion).getString(AMORTIZACION_DESCRIPCION));
            cantidad.setText(entrada.get(posicion).getString(AMORTIZACION_CANTIDAD));
            importe.setText(entrada.get(posicion).getString(AMORTIZACION_PRECIO));
            fecha.setText(JavaUtil.getDate(entrada.get(posicion).getLong(AMORTIZACION_FECHACOMPRA)));
            anios.setText(entrada.get(posicion).getString(AMORTIZACION_ANYOS));
            meses.setText(entrada.get(posicion).getString(AMORTIZACION_MESES));
            dias.setText(entrada.get(posicion).getString(AMORTIZACION_DIAS));
            if (entrada.get(posicion).getString(AMORTIZACION_RUTAFOTO)!=null){

                setImagenUriCircle(contexto,entrada.get(posicion).getString(AMORTIZACION_RUTAFOTO),imagen);
            }
            if (entrada.get(posicion).getLong(AMORTIZACION_FECHACOMPRA)>= JavaUtil.hoy()){

                card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_notok));
            }
            long compra = entrada.get(posicion).getLong(AMORTIZACION_FECHACOMPRA);
            long amort = (entrada.get(posicion).getLong(AMORTIZACION_DIAS)*DIASLONG)+
                    (entrada.get(posicion).getLong(AMORTIZACION_MESES)*MESESLONG)+
                    (entrada.get(posicion).getLong(AMORTIZACION_ANYOS)*ANIOSLONG);
            long fechafin = compra + amort;
            long amortizado = JavaUtil.hoy()-compra;
            int res = (int) ((100/(double)(fechafin-compra))*amortizado);
            progressBar.setProgress(res);
            if (res<30){
                card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_notok));
            }else if(res<60){

                card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_acept));
            }else {

                card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_ok));
            }

            super.setEntradas(posicion, view, entrada);
        }
    }

        public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

            TextView nombre, descripcion, cantidad, importe, fecha, anios, meses, dias;
        ImageView imagen;
        CardView card;
        ProgressBar progressBar;

        public ViewHolderRV(View itemView) {
            super(itemView);


            nombre = itemView.findViewById(R.id.tvnomlamort);
            descripcion = itemView.findViewById(R.id.tvdesclamort);
            cantidad = itemView.findViewById(R.id.tvcantlamort);
            importe = itemView.findViewById(R.id.tvimplamort);
            fecha = itemView.findViewById(R.id.tvfechalamort);
            anios = itemView.findViewById(R.id.tvanioslamort);
            meses = itemView.findViewById(R.id.tvmeseslamort);
            dias = itemView.findViewById(R.id.tvdiaslamort);
            imagen = itemView.findViewById(R.id.imglamort);
            card = itemView.findViewById(R.id.cardlamort);
            progressBar = itemView.findViewById(R.id.progressBarlamort);

        }

        @Override
        public void bind(ModeloSQL modeloSQL) {

            nombre.setText(modeloSQL.getString(AMORTIZACION_NOMBRE));
            descripcion.setText(modeloSQL.getString(AMORTIZACION_DESCRIPCION));
            cantidad.setText(modeloSQL.getString(AMORTIZACION_CANTIDAD));
            importe.setText(modeloSQL.getString(AMORTIZACION_PRECIO));
            fecha.setText(JavaUtil.getDate(modeloSQL.getLong(AMORTIZACION_FECHACOMPRA)));
            anios.setText(modeloSQL.getString(AMORTIZACION_ANYOS));
            meses.setText(modeloSQL.getString(AMORTIZACION_MESES));
            dias.setText(modeloSQL.getString(AMORTIZACION_DIAS));
            if (modeloSQL.getString(AMORTIZACION_RUTAFOTO) != null) {

                setImagenUriCircle(contexto, modeloSQL.getString(AMORTIZACION_RUTAFOTO), imagen);
            }
            if (modeloSQL.getLong(AMORTIZACION_FECHACOMPRA) >= JavaUtil.hoy()) {

                card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_notok));
            }
            long compra = modeloSQL.getLong(AMORTIZACION_FECHACOMPRA);
            long amort = (modeloSQL.getLong(AMORTIZACION_DIAS) * DIASLONG) +
                    (modeloSQL.getLong(AMORTIZACION_MESES) * MESESLONG) +
                    (modeloSQL.getLong(AMORTIZACION_ANYOS) * ANIOSLONG);
            long fechafin = compra + amort;
            long amortizado = JavaUtil.hoy()-compra;
            int res = (int) ((100/(double)(fechafin-compra))*amortizado);
            progressBar.setProgress(res);
            AndroidUtil.barsCard(contexto,progressBar,null, true,100,60,30,
                    res,null,null,0,0,0,
                    card,R.color.Color_card_notok,R.color.Color_card_acept,R.color.Color_card_ok);

            super.bind(modeloSQL);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

}