package jjlacode.com.freelanceproject.util.android;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.sqlite.ContratoPry;
import jjlacode.com.freelanceproject.util.adapter.BaseViewHolder;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltroRV;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import static jjlacode.com.freelanceproject.util.JavaUtil.Constantes.ORIGEN;


public class FragmentGrid extends FragmentRV implements CommonPry.TiposEvento,
        ContratoPry.Tablas, CommonPry.Constantes {

    protected int columnas = 3;
    protected int filas = 4;
    protected int altoimg = 100;
    protected int anchoimg = 100;
    protected int padtxt = 20;
    protected int padalto = 10;
    protected int padancho = 10;
    private float sizeT;
    private float relScreen;

    public FragmentGrid() {
        // Required empty public constructor
    }

    @Override
    protected void setLayout() {

        layoutItem = R.layout.item_grid;
    }

    @Override
    protected void setInicio() {


        bundle = getArguments();
        if (bundle!=null){

            String origen = bundle.getString(ORIGEN);
            bundle=null;
        }

        //frCuerpo.setOrientation(LinearLayout.VERTICAL);
        gone(frameAnimationCuerpo);
        gone(frPie);
        frCuerpo.setPadding(0,0,0,0);

        relScreen = alto/ancho;

        setLista();

        if (land){

            for (int i=lista.size() ;i>0; i--) {
                filas = i;
                columnas = (int)((double)lista.size()/i)+(lista.size()%i);
                if (filas >= columnas-1){continue;}
                break;
            }

        }else{
            for (int i=1 ;i<lista.size(); i++) {
                columnas = i;
                filas = (int)((double)lista.size()/i);
                if(lista.size()%i>0){filas++;}
                if (filas > columnas+1){continue;}
                break;
            }
        }

        anchoimg = ancho/ columnas;
        altoimg = alto/ filas;
        padancho = (int) ((double)(anchoimg)/(4));
        padalto = (int) ((double)(altoimg)/(4));
        padtxt = (int) ((double)padancho/2);

        sizeT = (sizeText*4)/5;
        contexto = getContext();

    }

    @Override
    protected void onSetRV() {
        super.onSetRV();
        gone(lupa);
        gone(auto);
        gone(renovar);
        gone(voz);
        gone(activityBase.fab);
        visible(activityBase.fab2);

    }

    @Override
    protected void setManagerRV() {
        super.setManagerRV();

        layoutManager = new GridLayoutManager(contexto, columnas);

    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroRV setAdaptadorAuto(Context context, int layoutItem, ArrayList lista, String[] campos) {
        return null;
    }

    @Override
    public void setOnClickRV(Object object) {

    }

    private class ViewHolderRV extends BaseViewHolder implements TipoViewHolder{

        LinearLayoutCompat main;
        TextView nombre;
        ImageView imagen;

        public ViewHolderRV(View view) {
            super(view);

            main = view.findViewById(R.id.main_item_grid);
            nombre = view.findViewById(R.id.tv_item_grid);
            imagen = view.findViewById(R.id.img_item_grid);
        }

        @Override
        public void bind(ArrayList<?> lista, int position) {
            super.bind(lista, position);

            GridModel gridModel = (GridModel) lista.get(position);
            LinearLayoutCompat.LayoutParams param = new LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT, (int) (((rv.getHeight())-(padalto*densidad))/ filas));
            main.setLayoutParams(param);

            imagen.setImageResource(gridModel.getDrawable());
            imagen.setPadding(padancho,padalto,padancho,0);

            if (gridModel.getNombre()!=null) {
                nombre.setText(gridModel.getNombre());
                nombre.setTextSize(sizeT);
                nombre.setPadding(padtxt, 0, padtxt, 0);
            }else{
                gone(nombre);
                imagen.setPadding(padancho,padalto,padancho,padalto);
            }

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }

    public class GridModel {

        private int drawable;
        private String nombre;

        public GridModel(int drawable, String nombre) {
            this.drawable = drawable;
            this.nombre = nombre;
        }

        public int getDrawable() {
            return drawable;
        }

        public void setDrawable(int drawable) {
            this.drawable = drawable;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

    }

}
