package jjlacode.com.freelanceproject.ui;

import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.AppActivity;
import jjlacode.com.freelanceproject.util.DatePickerFragment;
import jjlacode.com.freelanceproject.util.FragmentCRUD;
import jjlacode.com.freelanceproject.util.JavaUtil;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.CommonPry;
import jjlacode.com.freelanceproject.util.Modelo;

import static jjlacode.com.freelanceproject.util.CommonPry.namesubdef;
import static jjlacode.com.freelanceproject.util.CommonPry.setNamefdef;

public class FragmentCRUDAmortizacion extends FragmentCRUD implements ContratoPry.Tablas {


    private EditText nombre;
    private EditText descripcion;
    private EditText cantidad;
    private EditText importe;
    private EditText anios;
    private EditText meses;
    private EditText dias;
    private TextView fecha;

    private long fechaCompra;
    private ImageButton btnimgfecha;
    private ProgressBar bar;

    public FragmentCRUDAmortizacion() {
        // Required empty public constructor
    }

    @Override
    protected void setTabla() {

        tabla = TABLA_AMORTIZACION;

    }

    @Override
    protected void setTablaCab() {

        tablaCab = null;

    }

    @Override
    protected void setContext() {

        contexto = getContext();

    }

    @Override
    protected void setCampos() {

        campos = CAMPOS_AMORTIZACION;

    }

    @Override
    protected void setCampoID() {
        campoID = AMORTIZACION_ID_AMORTIZACION;
    }

    @Override
    protected void setBundle() {


    }

    @Override
    protected void setDatos() {

        id = modelo.getString(AMORTIZACION_ID_AMORTIZACION);
        nombre.setText(modelo.getString(AMORTIZACION_NOMBRE));
        descripcion.setText(modelo.getString(AMORTIZACION_DESCRIPCION));
        cantidad.setText(modelo.getString(AMORTIZACION_CANTIDAD));
        importe.setText(modelo.getString(AMORTIZACION_IMPORTE));
        fecha.setText(JavaUtil.getDate(modelo.getLong(AMORTIZACION_FECHACOMPRA)));
        fechaCompra = modelo.getLong(AMORTIZACION_FECHACOMPRA);
        anios.setText(modelo.getString(AMORTIZACION_ANYOS));
        meses.setText(modelo.getString(AMORTIZACION_MESES));
        dias.setText(modelo.getString(AMORTIZACION_DIAS));
        if (modelo.getString(AMORTIZACION_RUTAFOTO)!=null) {
            setImagenUriCircle(modelo.getString(AMORTIZACION_RUTAFOTO));
        }

        long compra = modelo.getLong(AMORTIZACION_FECHACOMPRA);
        long amort = (modelo.getLong(AMORTIZACION_DIAS)*DIASLONG)+
                (modelo.getLong(AMORTIZACION_MESES)*MESESLONG)+
                (modelo.getLong(AMORTIZACION_ANYOS)*ANIOSLONG);
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
    protected void setLayout() {

        layoutCuerpo = R.layout.fragment_cud_amortizacion;

    }

    @Override
    protected void setInicio() {

        nombre = view.findViewById(R.id.etnomcudamort);
        descripcion = view.findViewById(R.id.etdesccudamort);
        cantidad = view.findViewById(R.id.etcantcudamort);
        importe = view.findViewById(R.id.etimpcudamort);
        fecha = view.findViewById(R.id.tvfechacudamort);
        anios = view.findViewById(R.id.etanioscudamort);
        meses = view.findViewById(R.id.etmesescudamort);
        dias = view.findViewById(R.id.etdiascudamort);
        imagen = view.findViewById(R.id.imgcudamort);
        btnimgfecha = view.findViewById(R.id.btnimgfechacudamort);
        bar = view.findViewById(R.id.progressBarcudamort);


    }

    @Override
    protected void setLista() {

        AdaptadorRV adaptador = new AdaptadorRV(lista,R.layout.item_list_amortizacion,namef);
        rv.setAdapter(adaptador);

        onSetAdapter(lista);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickRV(v);

            }
        });



        setOnItemClickAuto();

    }

    @Override
    protected void setNuevo() {

        path = null;
        anios.setText(null);
        meses.setText(null);
        dias.setText(null);

        btndelete.setVisibility(View.GONE);

    }

    @Override
    protected void setMaestroDetallePort() {
        maestroDetalleSeparados = true;
    }

    @Override
    protected void setMaestroDetalleLand() { maestroDetalleSeparados = false; }

    @Override
    protected void setMaestroDetalleTabletLand() { maestroDetalleSeparados = false; }

    @Override
    protected void setMaestroDetalleTabletPort() { maestroDetalleSeparados = false; }

    @Override
    protected boolean update() {

        if(super.update()) {

            CommonPry.hora = CommonPry.Calculos.calculoPrecioHora();

            return true;
        }
        return false;

    }

    @Override
    protected boolean delete() {

        if(super.delete()) {

            CommonPry.hora = CommonPry.Calculos.calculoPrecioHora();

            return true;
        }
        return false;

    }

    @Override
    protected boolean registrar() {

        if(super.registrar()) {

            CommonPry.hora = CommonPry.Calculos.calculoPrecioHora();

            return true;
        }
        return false;
    }

    @Override
    protected void setContenedor() {

        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_NOMBRE,nombre.getText().toString());
        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_DESCRIPCION,descripcion.getText().toString());
        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_CANTIDAD,JavaUtil.comprobarDouble(cantidad.getText().toString()));
        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_IMPORTE,JavaUtil.comprobarDouble(importe.getText().toString()));
        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_ANYOS,JavaUtil.comprobarInteger(anios.getText().toString()));
        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_MESES,JavaUtil.comprobarInteger(meses.getText().toString()));
        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_DIAS,JavaUtil.comprobarInteger(dias.getText().toString()));
        consulta.putDato(valores,CAMPOS_AMORTIZACION,AMORTIZACION_FECHACOMPRA,fechaCompra);
        if (path!=null) {
            consulta.putDato(valores, CAMPOS_AMORTIZACION, AMORTIZACION_RUTAFOTO, path);
        }


    }

    @Override
    protected void setcambioFragment() {

        if (namesubclass.equals(NUEVAAMORTIZACION)){
            namesubclass = namesubdef = setNamefdef();
        }


    }

    private void showDatePickerDialogComprada() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance
                (fechaCompra>0?fechaCompra:JavaUtil.hoy(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because january is zero
                        //String selectedDate = CommonPry.twoDigits(day) + " / " +
                        //        CommonPry.twoDigits(month+1) + " / " + year;
                        fechaCompra = JavaUtil.fechaALong(year, month, day);
                        //String selectedDate = CommonPry.formatDateForUi(year,month,day);
                        String selectedDate = JavaUtil.getDate(fechaCompra);
                        fecha.setText(selectedDate);

                        update();
                        setDatos();

                    }
                });
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public class AdaptadorRV extends RecyclerView.Adapter<AdaptadorRV.ViewHolder>
            implements View.OnClickListener{

        protected ArrayList<Modelo> list;
        private View.OnClickListener listener;
        private String namef;
        private int layout;


        public AdaptadorRV(ArrayList<Modelo> list, int layout, String namef) {

            this.list = list;
            this.namef = namef;
            this.layout = layout;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(layout, null, false);

            view.setOnClickListener(this);


            return new ViewHolder(view);

        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

            viewHolder.nombre.setText(list.get(position).getString(AMORTIZACION_NOMBRE));
            viewHolder.descripcion.setText(list.get(position).getString(AMORTIZACION_DESCRIPCION));
            viewHolder.cantidad.setText(list.get(position).getString(AMORTIZACION_CANTIDAD));
            viewHolder.importe.setText(list.get(position).getString(AMORTIZACION_IMPORTE));
            viewHolder.fecha.setText(JavaUtil.getDate(list.get(position).getLong(AMORTIZACION_FECHACOMPRA)));
            viewHolder.anios.setText(list.get(position).getString(AMORTIZACION_ANYOS));
            viewHolder.meses.setText(list.get(position).getString(AMORTIZACION_MESES));
            viewHolder.dias.setText(list.get(position).getString(AMORTIZACION_DIAS));
            if (list.get(position).getString(AMORTIZACION_RUTAFOTO)!=null){

                setImagenUriCircle(list.get(position).getString(AMORTIZACION_RUTAFOTO),viewHolder.imagen);
            }
            if (list.get(position).getLong(AMORTIZACION_FECHACOMPRA)>= JavaUtil.hoy()){

                viewHolder.card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_notok));
            }
            long compra = list.get(position).getLong(AMORTIZACION_FECHACOMPRA);
            long amort = (list.get(position).getLong(AMORTIZACION_DIAS)*DIASLONG)+
                    (list.get(position).getLong(AMORTIZACION_MESES)*MESESLONG)+
                    (list.get(position).getLong(AMORTIZACION_ANYOS)*ANIOSLONG);
            long fechafin = compra + amort;
            long amortizado = JavaUtil.hoy()-compra;
            int res = (int) ((100/(double)(fechafin-compra))*amortizado);
            viewHolder.progressBar.setProgress(res);
            if (res<30){
                viewHolder.card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_notok));
            }else if(res>=30 && res<60){

                viewHolder.card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_acept));
            }else if(res>=60){

                viewHolder.card.setCardBackgroundColor(
                        AppActivity.getAppContext().getResources().getColor(R.color.Color_card_ok));
            }
        }

        @Override
        public int getItemCount() {

            if (list!=null) {
                return list.size();
            }
            return 0;
        }

        @Override
        public void onClick(View v) {

            if (listener != null) {

                listener.onClick(v);

            }

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView nombre,descripcion,cantidad,importe,fecha,anios,meses,dias;
            ImageView imagen;
            CardView card;
            ProgressBar progressBar;

            protected ViewHolder(@NonNull View itemView) {
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

        }

    }

}