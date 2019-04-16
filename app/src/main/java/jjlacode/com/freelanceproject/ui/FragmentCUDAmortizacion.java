package jjlacode.com.freelanceproject.ui;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import jjlacode.com.androidutils.DatePickerFragment;
import jjlacode.com.androidutils.FragmentCUD;
import jjlacode.com.androidutils.JavaUtil;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.utilities.CommonPry;

import static jjlacode.com.freelanceproject.utilities.CommonPry.permiso;

public class FragmentCUDAmortizacion extends FragmentCUD implements ContratoPry.Tablas {

    private String idAmortizacion;

    private Modelo amortizacion;
    private EditText nombre;
    private EditText descripcion;
    private EditText cantidad;
    private EditText importe;
    private EditText anios;
    private EditText meses;
    private EditText dias;
    private TextView fecha;

    private static ConsultaBD consulta = new ConsultaBD();
    private long fechaCompra;

    public FragmentCUDAmortizacion() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cud_amortizacion, container, false);

        btnsave = view.findViewById(R.id.amortizacion_ud_btn_save);
        btndelete = view.findViewById(R.id.amortizacion_ud_btn_del);
        btnback = view.findViewById(R.id.amortizacion_ud_btn_back);
        nombre = view.findViewById(R.id.etnomcudamort);
        descripcion = view.findViewById(R.id.etdesccudamort);
        cantidad = view.findViewById(R.id.etcantcudamort);
        importe = view.findViewById(R.id.etimpcudamort);
        fecha = view.findViewById(R.id.tvfechacudamort);
        anios = view.findViewById(R.id.etanioscudamort);
        meses = view.findViewById(R.id.etmesescudamort);
        dias = view.findViewById(R.id.etdiascudamort);
        imagen = view.findViewById(R.id.imgcudamort);
        ImageButton btnimgfecha = view.findViewById(R.id.btnimgfechacudamort);
        ProgressBar bar = view.findViewById(R.id.progressBarcudamort);

        bundle = getArguments();

        if (bundle != null) {

            amortizacion = (Modelo) bundle.getSerializable(TABLA_AMORTIZACION);
            idAmortizacion = amortizacion != null ? amortizacion.getString(AMORTIZACION_ID_AMORTIZACION) : null;
            namef = bundle.getString("namef");
            bundle = null;
        }

        if (idAmortizacion!=null){

            nombre.setText(amortizacion.getString(AMORTIZACION_NOMBRE));
            descripcion.setText(amortizacion.getString(AMORTIZACION_DESCRIPCION));
            cantidad.setText(amortizacion.getString(AMORTIZACION_CANTIDAD));
            importe.setText(amortizacion.getString(AMORTIZACION_IMPORTE));
            fecha.setText(JavaUtil.getDate(amortizacion.getLong(AMORTIZACION_FECHACOMPRA)));
            fechaCompra = amortizacion.getLong(AMORTIZACION_FECHACOMPRA);
            anios.setText(amortizacion.getString(AMORTIZACION_ANYOS));
            meses.setText(amortizacion.getString(AMORTIZACION_MESES));
            dias.setText(amortizacion.getString(AMORTIZACION_DIAS));
            if (amortizacion.getString(AMORTIZACION_RUTAFOTO)!=null) {
                imagen.setImageURI(amortizacion.getUri(AMORTIZACION_RUTAFOTO));
            }
            long compra = amortizacion.getLong(AMORTIZACION_FECHACOMPRA);
            long amort = (amortizacion.getLong(AMORTIZACION_DIAS)*DIASLONG)+
                    (amortizacion.getLong(AMORTIZACION_MESES)*MESESLONG)+
                    (amortizacion.getLong(AMORTIZACION_ANYOS)*ANIOSLONG);
            long fechafin = compra + amort;
            long amortizado = JavaUtil.hoy()-compra;
            int res = (int) ((100/(double)(fechafin-compra))*amortizado);
            bar.setProgress(res);

        }

        btnimgfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialogComprada();
            }
        });

        if (permiso) {
            imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mostrarDialogoOpcionesImagen();
                }
            });
        }

        btndelete.setVisibility(View.GONE);

        if (idAmortizacion != null) {


            btndelete.setVisibility(View.VISIBLE);
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

        if (idAmortizacion != null) {
            consulta.updateRegistro(TABLA_AMORTIZACION, idAmortizacion, valores);
            CommonPry.Calculos.calculoPrecioHora();
            icFragmentos.ejecutarEnActivity();
        } else {

            registrar();
        }

    }

    @Override
    protected void delete() {

        consulta.deleteRegistro(TABLA_AMORTIZACION, idAmortizacion);

    }

    @Override
    protected boolean registrar() {

        try {
            consulta.insertRegistro(TABLA_AMORTIZACION, valores);
            CommonPry.Calculos.calculoPrecioHora();
            icFragmentos.ejecutarEnActivity();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected boolean contenedor() {

        valores = new ContentValues();

        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_NOMBRE,nombre.getText().toString());
        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_DESCRIPCION,descripcion.getText().toString());
        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_CANTIDAD,JavaUtil.comprobarDouble(cantidad.getText().toString()));
        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_IMPORTE,JavaUtil.comprobarDouble(importe.getText().toString()));
        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_ANYOS,JavaUtil.comprobarInteger(anios.getText().toString()));
        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_MESES,JavaUtil.comprobarInteger(meses.getText().toString()));
        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_DIAS,JavaUtil.comprobarInteger(dias.getText().toString()));
        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_FECHACOMPRA,fechaCompra);
        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_RUTAFOTO,path);

        return true;

    }

    protected void cambiarFragment() {

        bundle = new Bundle();
        bundle.putString("namef", namef);
        icFragmentos.enviarBundleAFragment(bundle, new FragmentAmortizacion());

    }

    private void showDatePickerDialogComprada() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance
                (JavaUtil.hoy(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because january is zero
                        //String selectedDate = CommonPry.twoDigits(day) + " / " +
                        //        CommonPry.twoDigits(month+1) + " / " + year;
                        fechaCompra = JavaUtil.fechaALong(year, month, day);
                        //String selectedDate = CommonPry.formatDateForUi(year,month,day);
                        String selectedDate = JavaUtil.getDate(fechaCompra);
                        fecha.setText(selectedDate);

                        new CommonPry.Calculos.Tareafechas().execute();

                    }
                });
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

}