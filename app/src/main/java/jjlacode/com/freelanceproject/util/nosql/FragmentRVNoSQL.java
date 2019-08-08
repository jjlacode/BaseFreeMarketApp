package jjlacode.com.freelanceproject.util.nosql;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltro;
import jjlacode.com.freelanceproject.util.adapter.RVAdapter;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.android.FragmentBase;
import jjlacode.com.freelanceproject.util.animation.OneFrameLayout;
import jjlacode.com.freelanceproject.util.media.MediaUtil;

public abstract class FragmentRVNoSQL extends FragmentBase {

    protected LinearLayout frLista;
    protected View viewRV;
    protected int layoutItem;
    protected RecyclerView rv;
    protected AutoCompleteTextView auto;
    protected ArrayList listafiltrada;
    protected ImageView buscar;
    protected ImageView renovar;
    protected ImageView inicio;
    protected ImageView lupa;
    protected ImageView voz;
    protected ListaAdaptadorFiltro adaptadorFiltro;
    protected RVAdapter adaptadorRV;
    protected SwipeRefreshLayout refreshLayout;
    protected String subTitulo;
    protected ArrayList listab;
    protected ArrayList lista;
    protected Context contexto;
    protected int tituloPlural;
    protected ImageButton btnBack;
    protected ImageButton btnSave;
    protected ImageButton btnDelete;
    protected OneFrameLayout fragment_container;
    protected RecyclerView.LayoutManager layoutManager;
    private Object objeto;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        rv = view.findViewById(R.id.rv);
        refreshLayout = view.findViewById(R.id.swipeRefresh);
        auto = view.findViewById(R.id.auto);
        buscar = view.findViewById(R.id.imgbuscar);
        renovar = view.findViewById(R.id.imgrenovar);
        inicio = view.findViewById(R.id.imginicio);
        lupa = view.findViewById(R.id.imgsearch);
        voz = view.findViewById(R.id.imgvoz);
        btnBack = view.findViewById(R.id.btn_back);
        btnSave = view.findViewById(R.id.btn_save);
        btnDelete = view.findViewById(R.id.btn_del);
        gone(btnSave);
        gone(btnDelete);
        gone(btnBack);
        gone(frameAnimationCuerpo);

        //btnsave.setVisibility(View.GONE);
        //btndelete.setTextColor(getResources().getColor(colorAccent));

        refreshLayout.setColorSchemeResources(
                R.color.s1,
                R.color.s2,
                R.color.s3,
                R.color.s4
        );

        Chronometer timer = (Chronometer) view.findViewById(R.id.chronocrud);
        setTimerEdit(timer);

        setControls(view);

    }

    @Override
    public void onResume() {
        super.onResume();

        selector();
    }

    protected void selector() {

        visible(inicio);
        listaRV();
        if (subTitulo == null) {
            activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());
        }
        acciones();
    }

    protected void acciones() {


        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        if (listab == null) {

                            setLista();

                        }
                        rv.setVisibility(View.VISIBLE);
                        setRv();
                        refreshLayout.setRefreshing(false);
                    }
                }
        );


        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (adaptadorFiltro.getLista() != null) {
                    lista.clear();
                    lista.addAll(adaptadorFiltro.getLista());
                }

                setRv();
            }
        });

        lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        renovar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actualizarConsultasRV();
                setRv();
                auto.setText("");
            }
        });

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setRv();
                if (subTitulo == null) {
                    activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());
                }
            }
        });

        auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                auto.setDropDownWidth(0);
                if (adaptadorFiltro.getLista() != null) {
                    lista.clear();
                    lista.addAll(adaptadorFiltro.getLista());
                }

                setRv();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setAcciones();
    }

    protected void setControls(View view) {
    }

    protected void setAcciones() {
    }

    protected String[] setCamposFiltro() {
        return null;
    }

    protected abstract TipoViewHolder setViewHolder(View view);

    protected void listaRV() {

        if (listab == null) {
            setLista();
            System.out.println("lista.size() = " + lista.size());

        } else {
            lista = listab;
        }

        setRv();

    }

    protected void setRv() {

        int columnas = (int) (rv.getWidth() / (metrics.density * 300));
        if (columnas < 1) {
            columnas = 1;
        }
        layoutManager = new GridLayoutManager(contexto, columnas);
        setManagerRV();
        rv.setLayoutManager(layoutManager);
        adaptadorRV = new RVAdapter(setViewHolder(view), lista, layoutItem);
        rv.setAdapter(adaptadorRV);

        adaptadorRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickRV(v);
            }
        });

        adaptadorFiltro = setAdaptadorAuto(contexto, layoutItem, lista);

        auto.setAdapter(adaptadorFiltro);

        auto.setThreshold(3);

        setOnItemClickAuto();

        //if (lista.chechLista()) {
        rv.setVisibility(View.VISIBLE);
        auto.setVisibility(View.VISIBLE);
        buscar.setVisibility(View.VISIBLE);
        renovar.setVisibility(View.VISIBLE);
        inicio.setVisibility(View.VISIBLE);
        lupa.setVisibility(View.VISIBLE);

        buscar.setVisibility(View.GONE);
        if (listab == null) {
            inicio.setVisibility(View.GONE);
        }

        onSetRV();

    }

    protected void onSetRV() {

    }

    protected void setManagerRV() {

    }

    protected abstract ListaAdaptadorFiltro setAdaptadorAuto
            (Context context, int layoutItem, ArrayList lista);

    protected abstract void setLista();

    protected void actualizarConsultasRV() {

        if (listab == null) {

            setLista();
        } else {
            lista = listab;
        }

    }

    protected void onClickRV(View v) {

        setOnClickRV(lista.get(rv.getChildAdapterPosition(v)));
    }

    public abstract void setOnClickRV(Object object);


    protected void setOnItemClickAuto() {

        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {

                auto.setText("");
                objeto = adaptadorFiltro.getItem(position);

                selector();

            }
        });

    }

    protected void setImagenFireStore(Context contexto, String rutaFoto, ImageView imagen, int drawable) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStore(rutaFoto, imagen, drawable);

    }

    protected void setImagenFireStore(MediaUtil imagenUtil, String rutaFoto, ImageView imagen, int drawable) {

        imagenUtil.setImageFireStore(rutaFoto, imagen, drawable);

    }

    protected void setImagenFireStore(Context contexto, String rutaFoto, ImageView imagen) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStore(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenFireStore(MediaUtil imagenUtil, String rutaFoto, ImageView imagen) {

        imagenUtil.setImageFireStore(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenFireStoreCircle(Context contexto, String rutaFoto, ImageView imagen, int drawable) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStoreCircle(rutaFoto, imagen, drawable);

    }

    protected void setImagenFireStoreCircle(MediaUtil imagenUtil, String rutaFoto, ImageView imagen, int drawable) {

        imagenUtil.setImageFireStoreCircle(rutaFoto, imagen, drawable);

    }

    protected void setImagenFireStoreCircle(Context contexto, String rutaFoto, ImageView imagen) {

        MediaUtil imagenUtil = new MediaUtil(contexto);
        imagenUtil.setImageFireStoreCircle(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }

    protected void setImagenFireStoreCircle(MediaUtil imagenUtil, String rutaFoto, ImageView imagen) {

        imagenUtil.setImageFireStoreCircle(rutaFoto, imagen, R.drawable.ic_add_a_photo_black_24dp);

    }


}
