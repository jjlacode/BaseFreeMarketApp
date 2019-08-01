package jjlacode.com.freelanceproject.util.nosql;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.adapter.ListaAdaptadorFiltro;
import jjlacode.com.freelanceproject.util.adapter.RVAdapter;
import jjlacode.com.freelanceproject.util.adapter.TipoViewHolder;
import jjlacode.com.freelanceproject.util.android.FragmentBase;
import jjlacode.com.freelanceproject.util.animation.OneFrameLayout;
import jjlacode.com.freelanceproject.util.media.MediaUtil;

public abstract class FragmentMasterDetailNoSQL extends FragmentBase {

    protected LinearLayout frLista;
    protected View viewRV;
    protected int layoutItem;
    protected RecyclerView rv;
    protected AutoCompleteTextView auto;
    protected ArrayList listafiltrada;
    protected ImageView imagen;
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
    protected int tituloPlural;
    protected RecyclerView.LayoutManager layoutManager;
    private Object objeto;
    protected boolean maestroDetalleSeparados;
    protected boolean esDetalle;
    protected String path;
    protected MediaUtil mediaUtil = new MediaUtil(contexto);
    final protected int COD_FOTO = 10;
    final protected int COD_SELECCIONA = 20;
    private OneFrameLayout frameAnimation;
    private String stemp = "";


    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);
        frLista = view.findViewById(R.id.layout_rv);

        viewRV = inflater.inflate(R.layout.rvlayout, container, false);
        if (viewRV.getParent() != null) {
            ((ViewGroup) viewRV.getParent()).removeView(viewRV); // <- fix
        }
        frLista.addView(viewRV);

        rv = view.findViewById(R.id.rv);
        refreshLayout = view.findViewById(R.id.swipeRefresh);
        auto = view.findViewById(R.id.auto);
        buscar = view.findViewById(R.id.imgbuscar);
        renovar = view.findViewById(R.id.imgrenovar);
        inicio = view.findViewById(R.id.imginicio);
        lupa = view.findViewById(R.id.imgsearch);
        voz = view.findViewById(R.id.imgvoz);
        frameAnimation = view.findViewById(R.id.frameanimation);
        btnback = view.findViewById(R.id.btn_back);
        btndelete = view.findViewById(R.id.btn_del);
        btnsave = view.findViewById(R.id.btn_save);

        gone(btndelete);
        gone(btnsave);

        refreshLayout.setColorSchemeResources(
                R.color.s1,
                R.color.s2,
                R.color.s3,
                R.color.s4
        );

        if (land) {
            frCuerpo.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            frCuerpo.setOrientation(LinearLayout.VERTICAL);
        }
    }

    @Override
    protected void setLayoutExtra() {
        super.setLayoutExtra();
        layoutPie = R.layout.btn_sdb;
    }

    @Override
    public void onResume() {
        super.onResume();

        selector();
    }

    protected void selector() {

        maestroDetalle();
        if (esDetalle) {
            setDatos();
        }
        if (subTitulo == null) {
            activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());
        }
        acciones();
    }

    protected void acciones() {

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                esDetalle = false;
                selector();
            }
        });


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
                stemp = "";
                auto.setHint(stemp);
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


                if (!s.toString().contains(" ")) {
                    auto.setDropDownWidth(0);
                    if (adaptadorFiltro.getLista() != null) {
                        lista.clear();
                        lista.addAll(adaptadorFiltro.getLista());
                    }

                    setRv();
                } else if (!auto.getText().toString().equals("")) {
                    stemp += "+" + s;
                    auto.setHint(stemp);
                    auto.setText("");
                }
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

    protected abstract TipoViewHolder setViewHolder(View view);

    protected abstract void setDatos();

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


    protected void maestroDetalle() {

        Log.d(TAG, getMetodo());

        if (!land && !tablet) {

            maestroDetallePort();

        } else if (land && !tablet) {

            maestroDetalleLand();

        } else if (!land) {

            maestroDetalleTabletPort();

        } else {

            maestroDetalleTabletLand();

        }

    }

    protected void maestroDetallePort() {

        Log.d(TAG, getMetodo());

        setMaestroDetallePort();
        if (maestroDetalleSeparados) {
            defectoMaestroDetalleSeparados();
        } else {
            defectoMaestroDetalleJuntos();
        }

    }

    protected void maestroDetalleLand() {

        Log.d(TAG, getMetodo());

        setMaestroDetalleLand();
        if (maestroDetalleSeparados) {
            defectoMaestroDetalleSeparados();
        } else {
            defectoMaestroDetalleJuntos();
        }

    }

    protected void maestroDetalleTabletPort() {

        Log.d(TAG, getMetodo());

        setMaestroDetalleTabletPort();
        if (maestroDetalleSeparados) {
            defectoMaestroDetalleSeparados();
        } else {
            defectoMaestroDetalleJuntos();

        }

    }

    protected void maestroDetalleTabletLand() {

        Log.d(TAG, getMetodo());

        setMaestroDetalleTabletLand();
        if (maestroDetalleSeparados) {
            defectoMaestroDetalleSeparados();
        } else {
            defectoMaestroDetalleJuntos();
        }

    }

    protected void defectoMaestroDetalleSeparados() {

        Log.d(TAG, getMetodo());

        if (layoutCabecera > 0) {
            visible(frCabecera);
        }
        visible(frLista);
        visible(rv);
        visible(refreshLayout);
        gone(frameAnimationCuerpo);
        gone(frPie);

        if (esDetalle) {
            if (layoutCabecera > 0) {
                gone(frCabecera);
            }
            gone(frLista);
            gone(rv);
            gone(refreshLayout);
            visible(frameAnimationCuerpo);
            visible(frPie);
            activityBase.fab.setSize(FloatingActionButton.SIZE_MINI);
            activityBase.fab2.setSize(FloatingActionButton.SIZE_MINI);

        } else {
            activityBase.fab2.show();
            activityBase.fab.show();
            activityBase.fab.setSize(FloatingActionButton.SIZE_NORMAL);
            activityBase.fab2.setSize(FloatingActionButton.SIZE_NORMAL);

            if (grabarVoz == null) {
                listaRV();
                grabarVoz = null;
            }
        }

        setDefectoMaestroDetalleSeparados();

    }

    protected void setDefectoMaestroDetalleSeparados() {

        Log.d(TAG, getMetodo());

    }

    protected void setDefectoMaestroDetalleJuntos() {

        Log.d(TAG, getMetodo());

    }

    protected void defectoMaestroDetalleJuntos() {

        Log.d(TAG, getMetodo());

        visible(frLista);
        visible(rv);
        visible(refreshLayout);
        visible(frameAnimationCuerpo);
        visible(frPie);
        if (layoutCabecera > 0) {
            visible(frCabecera);
        } else {
            gone(frCabecera);
        }
        activityBase.fab2.show();
        activityBase.fab.show();
        visible(btndelete);
        listaRV();

        setDefectoMaestroDetalleJuntos();
    }

    protected void setMaestroDetallePort() {
        maestroDetalleSeparados = true;
    }

    protected void setMaestroDetalleLand() {
        maestroDetalleSeparados = false;
    }

    protected void setMaestroDetalleTabletLand() {
        maestroDetalleSeparados = false;
    }

    protected void setMaestroDetalleTabletPort() {
        maestroDetalleSeparados = false;
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
