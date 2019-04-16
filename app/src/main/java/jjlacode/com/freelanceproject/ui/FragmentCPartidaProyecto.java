package jjlacode.com.freelanceproject.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import jjlacode.com.androidutils.AndroidUtil;
import jjlacode.com.androidutils.FragmentC;
import jjlacode.com.androidutils.Modelo;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.utilities.CommonPry;

import static jjlacode.com.freelanceproject.utilities.CommonPry.permiso;


public class FragmentCPartidaProyecto extends FragmentC implements CommonPry.Constantes, ContratoPry.Tablas {

    private String idProyecto;
    AutoCompleteTextView nombrePartida;
    EditText descripcionPartida;
    EditText cantidadPartida;

    private TextView titulo;
    private TextView nombreProyecto;
    private Modelo proyecto;
    private Uri uri;

    private ConsultaBD consulta = new ConsultaBD();
    private ArrayList<Modelo> lista;
    private String idPartida;
    private int secuencia=0;
    private Modelo partida;
    private String idPartidabase;
    private Modelo partidabase;

    public FragmentCPartidaProyecto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c_partida_proyecto, container, false);

        imagen = view.findViewById(R.id.imgNpartida);
        nombrePartida = view.findViewById(R.id.etnomNpartida);
        descripcionPartida = view.findViewById(R.id.etdescripcionNpartida);
        cantidadPartida = view.findViewById(R.id.etcantidadNpartida);
        btnsave = view.findViewById(R.id.btnsaveNpartida);
        btnback = view.findViewById(R.id.btnvolverNpartida);
        titulo = view.findViewById(R.id.tvtitnpartida);
        nombreProyecto = view.findViewById(R.id.tvnomprynpartida);

        bundle = getArguments();

        if (bundle!=null){

            namef = bundle.getString("namef");

            if (namef.equals(PROYECTO)){

                titulo.setText(getString(R.string.partida_proyecto));
                nombreProyecto.setText(proyecto.getString(PROYECTO_NOMBRE));
                proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
                idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);

            }else if (namef.equals(PRESUPUESTO)){

                titulo.setText(getString(R.string.partida_presupuesto));
                proyecto = (Modelo) bundle.getSerializable(TABLA_PROYECTO);
                nombreProyecto.setText(proyecto.getString(PROYECTO_NOMBRE));
                idProyecto = proyecto.getString(PROYECTO_ID_PROYECTO);

            }else if (namef.equals(PARTIDABASE)){

                titulo.setText(getString(R.string.partida_base));
                nombreProyecto.setVisibility(View.GONE);
                cantidadPartida.setVisibility(View.GONE);
            }

            bundle=null;
        }



        if (permiso) {
            imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mostrarDialogoOpcionesImagen();
                }
            });
        }


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (registrar()) {

                    if (namef.equals(PARTIDABASE)){

                        bundle = new Bundle();
                        bundle.putSerializable(TABLA_PARTIDABASE,partidabase);
                        bundle.putString("namef", namef);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentUDPartidaBase());
                        bundle = null;
                    }else if (namef.equals(PRESUPUESTO) || namef.equals(PROYECTO)) {

                        new CommonPry.Calculos.Tareafechas().execute();

                        bundle = new Bundle();
                        if (proyecto != null) {
                            bundle.putSerializable(TABLA_PROYECTO, proyecto);
                        }
                        bundle.putSerializable(TABLA_PARTIDA, partida);
                        bundle.putString("namef", namef);
                        icFragmentos.enviarBundleAFragment(bundle, new FragmentUDPartidaProyecto());
                        bundle = null;
                    }

                }else{

                    Toast.makeText(getContext(), "Faltan datos obligatorios", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (namef.equals(PARTIDABASE)){

                    bundle = new Bundle();
                    bundle.putString("namef", namef);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentPartidaBase());
                    bundle = null;

                }else {
                    bundle = new Bundle();
                    bundle.putSerializable(TABLA_PROYECTO, proyecto);
                    bundle.putString("namef", namef);
                    icFragmentos.enviarBundleAFragment(bundle, new FragmentPartidasProyecto());
                    bundle = null;
                }
            }
        });

        setAdaptadorAuto(nombrePartida);

        nombrePartida.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Modelo partidaclon = (Modelo) nombrePartida.getAdapter().getItem(position);

                if (namef.equals(PARTIDABASE)){

                    mostrarDialogoClonarPartidabase(partidaclon);

                }else {

                    mostrarDialogoClonarPartida(partidaclon);

                }

            }

        });

        return view;
    }

    private void mostrarDialogoClonarPartida(final Modelo modelo) {

        final CharSequence[] opciones = {"Clonar partida","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Clonar partida")){

                    if (modelo.getNombreTabla().equals(TABLA_PARTIDA)) {

                        valores = modelo.contenido();
                        idPartida = ContratoPry.generarIdTabla(TABLA_PARTIDA);
                        valores.put(PARTIDA_ID_PROYECTO, idProyecto);
                        valores.put(PARTIDA_ID_PARTIDA, idPartida);
                        valores.put(PARTIDA_CANTIDAD, 0);
                        valores.remove(PARTIDA_SECUENCIA);

                    }else if (modelo.getNombreTabla().equals(TABLA_PARTIDABASE)){

                        valores = new ContentValues();
                        idPartida = modelo.getString(PARTIDABASE_ID_PARTIDA);
                        valores.put(PARTIDA_CANTIDAD, 0);
                        valores.put(PARTIDA_DESCRIPCION, modelo.getString(PARTIDABASE_DESCRIPCION));
                        valores.put(PARTIDA_NOMBRE, modelo.getString(PARTIDABASE_NOMBRE));
                        valores.put(PARTIDA_PRECIO, modelo.getString(PARTIDABASE_PRECIO));
                        valores.put(PARTIDA_TIEMPO, modelo.getString(PARTIDABASE_TIEMPO));
                        valores.put(PARTIDA_RUTAFOTO, modelo.getString(PARTIDABASE_RUTAFOTO));
                        valores.put(PARTIDA_ID_PROYECTO,idProyecto);
                        valores.put(PARTIDA_ID_ESTADO,proyecto.getString(PROYECTO_ID_ESTADO));
                        valores.put(PARTIDA_ID_PARTIDA,idPartida);
                    }

                    uri = consulta.insertRegistroDetalle(CAMPOS_PARTIDA,idProyecto,
                            TABLA_PROYECTO,valores);
                    System.out.println("uri = " + uri);

                    partida = consulta.queryObjectDetalle(CAMPOS_PARTIDA,uri);
                    secuencia = partida.getInt(PARTIDA_SECUENCIA);

                    nombrePartida.setText(partida.getString(PARTIDA_NOMBRE));
                    descripcionPartida.setText(partida.getString(PARTIDA_DESCRIPCION));

                    if (partida.getString(PARTIDA_RUTAFOTO)!=null){
                        imagen.setImageURI(partida.getUri(PARTIDA_RUTAFOTO));
                        path = partida.getString(PARTIDA_RUTAFOTO);
                    }

                    if (modelo.getNombreTabla().equals(TABLA_PARTIDABASE)) {

                        ArrayList<Modelo> listaclon = consulta.queryListDetalle
                                (CAMPOS_DETPARTIDABASE,modelo.getString(PARTIDABASE_ID_PARTIDA),TABLA_PARTIDABASE);

                        for (Modelo clonpart : listaclon) {

                            valores = new ContentValues();
                            valores.put(DETPARTIDA_ID_PARTIDA,idPartida);
                            valores.put(DETPARTIDA_NOMBRE,clonpart.getString(DETPARTIDABASE_NOMBRE));
                            valores.put(DETPARTIDA_DESCRIPCION,clonpart.getString(DETPARTIDABASE_DESCRIPCION));
                            valores.put(DETPARTIDA_BENEFICIO,clonpart.getString(DETPARTIDABASE_BENEFICIO));
                            valores.put(DETPARTIDA_CANTIDAD,clonpart.getString(DETPARTIDABASE_CANTIDAD));
                            valores.put(DETPARTIDA_DESCUENTOPROV,clonpart.getString(DETPARTIDABASE_DESCUENTOPROV));
                            valores.put(DETPARTIDA_PRECIO,clonpart.getString(DETPARTIDABASE_PRECIO));
                            valores.put(DETPARTIDA_ID_DETPARTIDA,clonpart.getString(DETPARTIDABASE_ID_DETPARTIDA));
                            valores.put(DETPARTIDA_REFPROV,clonpart.getString(DETPARTIDABASE_REFPROV));
                            valores.put(DETPARTIDA_TIEMPO,clonpart.getString(DETPARTIDABASE_TIEMPO));
                            valores.put(DETPARTIDA_RUTAFOTO,clonpart.getString(DETPARTIDABASE_RUTAFOTO));
                            valores.put(DETPARTIDA_TIPO,clonpart.getString(DETPARTIDABASE_TIPO));
                            valores.remove(DETPARTIDABASE_SECUENCIA);

                            consulta.insertRegistroDetalle(CAMPOS_DETPARTIDA,idPartida
                                    ,TABLA_PARTIDA,valores);
                        }

                    }else if (modelo.getNombreTabla().equals(TABLA_PARTIDA)){

                        ArrayList<Modelo> listaclon = consulta.queryListDetalle
                                (CAMPOS_DETPARTIDA,modelo.getString(PARTIDA_ID_PARTIDA),TABLA_PARTIDA);

                        System.out.println("listaclon = " + listaclon.toString());

                        for (Modelo clonpart : listaclon) {

                            valores = clonpart.contenido();//AndroidUtil.clonarSinRef(clonpart);
                            valores.put(DETPARTIDA_ID_PARTIDA,idPartida);
                            valores.remove(DETPARTIDA_SECUENCIA);

                            consulta.insertRegistroDetalle(CAMPOS_DETPARTIDA,idPartida
                                    ,TABLA_PARTIDA,valores);
                        }
                    }

                }else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void mostrarDialogoClonarPartidabase(final Modelo clon) {

        final CharSequence[] opciones = {"Clonar partida base","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (opciones[which].equals("Clonar partida base")){

                    if (clon.getNombreTabla().equals(TABLA_PARTIDA)) {

                        valores = new ContentValues();
                        idPartida = clon.getString(PARTIDA_ID_PARTIDA);
                        valores.put(PARTIDABASE_DESCRIPCION, clon.getString(PARTIDA_DESCRIPCION));
                        valores.put(PARTIDABASE_NOMBRE, clon.getString(PARTIDA_NOMBRE));
                        valores.put(PARTIDABASE_PRECIO, clon.getString(PARTIDA_PRECIO));
                        valores.put(PARTIDABASE_TIEMPO, clon.getString(PARTIDA_TIEMPO));
                        valores.put(PARTIDABASE_RUTAFOTO, clon.getString(PARTIDA_RUTAFOTO));

                    }else if (clon.getNombreTabla().equals(TABLA_PARTIDABASE)){

                        idPartida = clon.getString(PARTIDABASE_ID_PARTIDA);
                        valores = clon.contenido();
                        valores.remove(PARTIDABASE_ID_PARTIDA);
                    }

                    uri = consulta.insertRegistro(TABLA_PARTIDABASE,valores);
                    partidabase = consulta.queryObject(CAMPOS_PARTIDABASE,uri);
                    idPartidabase = partidabase.getString(PARTIDABASE_ID_PARTIDA);

                    nombrePartida.setText(partidabase.getString(PARTIDABASE_NOMBRE));
                    descripcionPartida.setText(partidabase.getString(PARTIDABASE_DESCRIPCION));
                    if (partidabase.getString(PARTIDABASE_RUTAFOTO)!=null){
                        imagen.setImageURI(partidabase.getUri(PARTIDABASE_RUTAFOTO));
                        path = partidabase.getString(PARTIDABASE_RUTAFOTO);
                    }

                    if (clon.getNombreTabla().equals(TABLA_PARTIDA)) {

                        ArrayList<Modelo> listaclon = consulta.queryListDetalle
                                (CAMPOS_DETPARTIDA,clon.getString(PARTIDA_ID_PARTIDA),TABLA_PARTIDA);

                        for (Modelo clonpart : listaclon) {

                            valores = new ContentValues();
                            valores.put(DETPARTIDABASE_ID_PARTIDA,idPartidabase);
                            valores.put(DETPARTIDABASE_NOMBRE,clonpart.getString(DETPARTIDA_NOMBRE));
                            valores.put(DETPARTIDABASE_DESCRIPCION,clonpart.getString(DETPARTIDA_DESCRIPCION));
                            valores.put(DETPARTIDABASE_BENEFICIO,clonpart.getString(DETPARTIDA_BENEFICIO));
                            valores.put(DETPARTIDABASE_CANTIDAD,clonpart.getString(DETPARTIDA_CANTIDAD));
                            valores.put(DETPARTIDABASE_DESCUENTOPROV,clonpart.getString(DETPARTIDA_DESCUENTOPROV));
                            valores.put(DETPARTIDABASE_PRECIO,clonpart.getString(DETPARTIDA_PRECIO));
                            valores.put(DETPARTIDABASE_ID_DETPARTIDA,clonpart.getString(DETPARTIDA_ID_DETPARTIDA));
                            valores.put(DETPARTIDABASE_REFPROV,clonpart.getString(DETPARTIDA_REFPROV));
                            valores.put(DETPARTIDABASE_TIEMPO,clonpart.getString(DETPARTIDA_TIEMPO));
                            valores.put(DETPARTIDABASE_RUTAFOTO,clonpart.getString(DETPARTIDA_RUTAFOTO));
                            valores.put(DETPARTIDABASE_TIPO,clonpart.getString(DETPARTIDA_TIPO));
                            valores.remove(DETPARTIDA_SECUENCIA);

                            consulta.insertRegistroDetalle(CAMPOS_DETPARTIDABASE,idPartidabase
                                    ,TABLA_PARTIDABASE,valores);
                        }

                    }else if (clon.getNombreTabla().equals(TABLA_PARTIDABASE)){

                        ArrayList<Modelo> listaclon = consulta.queryListDetalle
                                (CAMPOS_DETPARTIDABASE,clon.getString(PARTIDABASE_ID_PARTIDA),TABLA_PARTIDABASE);

                        for (Modelo clonpart : listaclon) {

                            valores = clonpart.contenido();//AndroidUtil.clonarSinRef(clonpart);
                            valores.put(DETPARTIDABASE_ID_PARTIDA,idPartidabase);
                            valores.remove(DETPARTIDABASE_SECUENCIA);

                            consulta.insertRegistroDetalle(CAMPOS_DETPARTIDABASE,idPartidabase
                                    ,TABLA_PARTIDABASE,valores);
                        }
                    }

                }else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected boolean registrar() {




                valores = new ContentValues();

                if (namef.equals(TABLA_PARTIDABASE)){

                        try {

                            consulta.putDato(valores, CAMPOS_PARTIDABASE, PARTIDABASE_NOMBRE, nombrePartida.getText().toString());
                            consulta.putDato(valores, CAMPOS_PARTIDABASE, PARTIDABASE_DESCRIPCION, descripcionPartida.getText().toString());
                            consulta.putDato(valores, CAMPOS_PARTIDABASE, PARTIDABASE_RUTAFOTO, path);

                            if (null == idPartidabase ) {
                                uri = consulta.insertRegistro(TABLA_PARTIDABASE, valores);
                                partidabase = consulta.queryObject(CAMPOS_PARTIDABASE, uri);
                                System.out.println("uri = " + uri);
                                Toast.makeText(getContext(), "Nueva partida base guardada", Toast.LENGTH_SHORT).show();
                            } else {

                                consulta.updateRegistro(TABLA_PARTIDABASE, idPartidabase, valores);
                                Toast.makeText(getContext(), "registro modificado", Toast.LENGTH_SHORT).show();
                            }

                            CommonPry.Calculos.actualizarPartidaBase(idPartidabase);
                            return true;

                        }catch (Exception e){Toast.makeText(getContext(), "Error al guardar partida", Toast.LENGTH_SHORT).show();}


                }else {

                    //try {

                        consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_NOMBRE, nombrePartida.getText().toString());
                        consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_DESCRIPCION, descripcionPartida.getText().toString());
                        consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_CANTIDAD, cantidadPartida.getText().toString());
                        if (proyecto != null) {
                            consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_ID_ESTADO, proyecto.getString(PROYECTO_ID_ESTADO));
                        } else {

                            consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_ID_ESTADO, "Estado base");

                        }
                        consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_ID_PROYECTO, idProyecto);
                        consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_RUTAFOTO, path);

                        if (null == idPartida && secuencia == 0) {
                            consulta.putDato(valores, CAMPOS_PARTIDA, PARTIDA_ID_PARTIDA, ContratoPry.generarIdTabla(TABLA_PARTIDA));
                            uri = consulta.insertRegistroDetalle(CAMPOS_PARTIDA, idProyecto, TABLA_PROYECTO, valores);
                            partida = consulta.queryObjectDetalle(CAMPOS_PARTIDA, uri);
                            idPartida = partida.getString(PARTIDA_ID_PARTIDA);
                            System.out.println("uri = " + uri);
                            Toast.makeText(getContext(), "Nueva partida guardada", Toast.LENGTH_SHORT).show();
                        } else {

                            consulta.updateRegistroDetalle(TABLA_PARTIDA, idProyecto, secuencia, valores);
                            Toast.makeText(getContext(), "registro modificado", Toast.LENGTH_SHORT).show();
                        }

                        CommonPry.Calculos.actualizarPartidaProyecto(idPartida);
                        return true;

                    //}catch (Exception e){Toast.makeText(getContext(), "Error al guardar partida", Toast.LENGTH_SHORT).show();}

                }

                return false;

    }

    private void setAdaptadorAuto(AutoCompleteTextView autoCompleteTextView) {


            lista = consulta.queryList(CAMPOS_PARTIDA);
            ArrayList<Modelo> listabase = consulta.queryList(CAMPOS_PARTIDABASE);
            lista.addAll(listabase);


            autoCompleteTextView.setAdapter(new ListaAdaptadorFiltroPartidas(getContext(),
                    R.layout.item_list_partida, lista) {
                @Override
                public void onEntrada(Modelo entrada, View view) {

                    ImageView imagen = view.findViewById(R.id.imglpartida);
                    TextView descripcion = view.findViewById(R.id.tvdescripcionpartida);
                    TextView tiempo = view.findViewById(R.id.tvtiempopartida);
                    TextView cantidad = view.findViewById(R.id.tvcantidadpartida);
                    TextView importe = view.findViewById(R.id.tvimppartida);

                    String tabla = entrada.getNombreTabla();
                    System.out.println("tabla = " + tabla);

                    if (tabla.equals(TABLA_PARTIDA)) {

                    descripcion.setText(entrada.getString(PARTIDA_DESCRIPCION));
                    tiempo.setText(entrada.getString(PARTIDA_TIEMPO));
                    cantidad.setText(entrada.getString(PARTIDA_CANTIDAD));
                    importe.setText(entrada.getString(PARTIDA_PRECIO));

                    if (entrada.getString(PARTIDA_RUTAFOTO) != null) {
                        imagen.setImageURI(entrada.getUri(PARTIDA_RUTAFOTO));
                    }
                    }else if (tabla.equals(TABLA_PARTIDABASE)){

                        descripcion.setText(entrada.getString(PARTIDABASE_DESCRIPCION));
                        tiempo.setText(entrada.getString(PARTIDABASE_TIEMPO));
                        importe.setText(entrada.getString(PARTIDABASE_PRECIO));

                        if (entrada.getString(PARTIDABASE_RUTAFOTO) != null) {
                            imagen.setImageURI(entrada.getUri(PARTIDABASE_RUTAFOTO));
                        }

                    }

                }

            });

    }

    public abstract class ListaAdaptadorFiltroPartidas extends ArrayAdapter<Modelo> {

        private ArrayList<Modelo> entradas;
        private ArrayList<Modelo> entradasfiltro;
        private int R_layout_IdView;
        private Context contexto;

        ListaAdaptadorFiltroPartidas(Context contexto, int R_layout_IdView, ArrayList<Modelo> entradas) {
            super(contexto,R_layout_IdView,entradas);
            this.contexto = contexto;
            this.entradas = entradas;
            this.entradasfiltro = new ArrayList<>(entradas);
            this.R_layout_IdView = R_layout_IdView;
        }

        @Override
        public View getView(int posicion, View view, ViewGroup pariente) {
            if (view == null) {
                LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R_layout_IdView, null);
            }
            onEntrada (entradasfiltro.get(posicion), view);
            System.out.println("entradasfiltro = " + entradasfiltro.get(posicion));
            return view;
        }

        @Override
        public int getCount() {
            return entradasfiltro.size();
        }


        @NonNull
        @Override
        public Filter getFilter() {

            Filter filter;
            filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults results = new FilterResults();
                    List<Modelo> suggestion = new ArrayList<>();
                    if (constraint != null) {

                        for (Modelo item :entradas) {

                            String tabla = item.getNombreTabla();
                            System.out.println("tabla = " + tabla);

                            if (tabla.equals(TABLA_PARTIDABASE)){

                                if(item.getCampos(PARTIDABASE_NOMBRE).toLowerCase().contains(constraint.toString().toLowerCase())){

                                    suggestion.add(item);
                                }

                            }else if (tabla.equals(TABLA_PARTIDA)){

                                if(item.getCampos(PARTIDA_NOMBRE).toLowerCase().contains(constraint.toString().toLowerCase())){

                                    suggestion.add(item);
                                }
                            }



                        }
                        // Query the autocomplete API for the entered constraint
                        // Results
                        results.values = suggestion;
                        results.count = suggestion.size();
                    }
                    return results;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    entradasfiltro.clear();

                    if (results != null && results.count > 0) {
                        for (Modelo item : (List<Modelo>) results.values) {
                            entradasfiltro.add(item);
                        }
                        notifyDataSetChanged();
                    } else if (constraint == null) {
                        // no filter, add entire original list back in
                        entradasfiltro.addAll(entradas);
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

        @Override
        public Modelo getItem(int posicion) {
            return entradasfiltro.get(posicion);
        }

        @Override
        public long getItemId(int posicion) {
            return posicion;
        }

        /** Devuelve cada una de las entradas con cada una de las vistas a la que debe de ser asociada
         * @param entrada La entrada que será la asociada a la view. La entrada es del tipo del paquete/handler
         * @param view View particular que contendrá los datos del paquete/handler
         */
        public abstract void onEntrada (Modelo entrada, View view);
    }

}
