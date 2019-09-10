package com.codevsolution.base.util.nosql;

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
import com.codevsolution.base.util.adapter.ListaAdaptadorFiltro;
import com.codevsolution.base.util.adapter.RVAdapter;
import com.codevsolution.base.util.adapter.TipoViewHolder;
import com.codevsolution.base.util.android.AndroidUtil;
import com.codevsolution.base.util.android.controls.EditMaterial;
import com.codevsolution.base.util.android.controls.ImagenLayout;
import com.codevsolution.base.util.animation.OneFrameLayout;
import com.codevsolution.base.util.media.MediaUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class FragmentMasterDetailNoSQL extends FragmentNoSQL {

    protected LinearLayout frLista;
    protected View viewRV;
    protected int layoutItem;
    protected RecyclerView rv;
    protected AutoCompleteTextView auto;
    protected ArrayList listafiltrada;
    protected ImagenLayout imagen;
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
    protected String tituloPlural;
    protected String titulo;
    protected RecyclerView.LayoutManager layoutManager;
    private Object objeto;
    protected boolean maestroDetalleSeparados;
    protected boolean esDetalle;
    protected String path;
    protected MediaUtil mediaUtil = new MediaUtil(contexto);
    protected OneFrameLayout frameAnimation;
    protected String stemp = "";
    protected int posicion;
    protected boolean autoGuardado;


    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        setLayoutItem();
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

    protected void setLayoutItem() {

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
            activityBase.toolbar.setSubtitle(Interactor.setNamefdef());
        }
        acciones();
    }

    protected void acciones() {

        super.acciones();

        if (autoGuardado) {
            for (final EditMaterial editMaterial : materialEdits) {
                editMaterial.setCambioFocoListener(new EditMaterial.CambioFocoEdit() {
                    @Override
                    public void alPerderFoco(View view) {
                        alCambiarCampos(editMaterial);
                        if (editMaterial.getNextFocusDownId() == 0) {
                            AndroidUtil.ocultarTeclado(contexto, view);
                        }
                    }

                    @Override
                    public void alRecibirFoco(View view) {
                        editMaterial.finalTexto();
                    }
                });

                editMaterial.setAlCambiarListener(new EditMaterial.AlCambiarListener() {
                    @Override
                    public void antesCambio(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void cambiando(CharSequence s, int start, int before, int count) {

                        if (timer != null) {
                            timer.cancel();
                        }
                    }

                    @Override
                    public void despuesCambio(Editable s) {
                        final Editable temp = s;
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (!temp.toString().equals("")) {
                                    guardar();
                                }
                            }
                        }, 1000);
                    }
                });
            }
        }

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                esDetalle = false;
                System.out.println("esDetalle = " + esDetalle);
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
                    activityBase.toolbar.setSubtitle(Interactor.setNamefdef());
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

    protected void guardar() {

    }

    protected void setControls(View view) {
    }

    protected void setAcciones() {
    }

    protected abstract TipoViewHolder setViewHolder(View view);

    protected void setDatos() {

    }

    protected void listaRV() {

        if (listab == null) {
            setLista();

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

    public abstract void setOnClickRV(Object object);

    protected void onClickRV(View v) {

        setOnClickRV(lista.get(rv.getChildAdapterPosition(v)));
        posicion = rv.getChildAdapterPosition(v);
    }

    @Override
    protected void setOnLeftSwipeCuerpo() {
        super.setOnLeftSwipeCuerpo();

        if (posicion < lista.size() - 1) {
            posicion++;
            setOnClickRV(lista.get(posicion));
            System.out.println("posicion = " + posicion);
        }
    }

    @Override
    protected void setOnRigthSwipeCuerpo() {
        super.setOnRigthSwipeCuerpo();
        if (posicion > 0) {
            posicion--;
            setOnClickRV(lista.get(posicion));
            System.out.println("posicion = " + posicion);
        }
    }

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
            activityBase.fabNuevo.setSize(FloatingActionButton.SIZE_MINI);
            activityBase.fabInicio.setSize(FloatingActionButton.SIZE_MINI);
            activityBase.fabVoz.setSize(FloatingActionButton.SIZE_MINI);

        } else {
            activityBase.fabVoz.show();
            activityBase.fabNuevo.show();
            activityBase.fabNuevo.setSize(FloatingActionButton.SIZE_NORMAL);
            activityBase.fabInicio.setSize(FloatingActionButton.SIZE_NORMAL);
            activityBase.fabVoz.setSize(FloatingActionButton.SIZE_NORMAL);

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
        activityBase.fabVoz.show();
        activityBase.fabNuevo.show();
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

}
