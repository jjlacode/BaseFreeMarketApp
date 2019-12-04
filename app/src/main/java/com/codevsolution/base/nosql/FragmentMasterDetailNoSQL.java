package com.codevsolution.base.nosql;

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

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codevsolution.base.adapter.ListaAdaptadorFiltro;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.ViewImagenLayout;
import com.codevsolution.base.animation.OneFrameLayout;
import com.codevsolution.base.media.MediaUtil;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class FragmentMasterDetailNoSQL extends FragmentNoSQL {

    //protected LinearLayout frLista;
    protected View viewRV;
    protected int layoutItemRv;
    protected int layoutItemAuto;
    protected RecyclerView rv;
    protected AutoCompleteTextView auto;
    protected ArrayList listafiltrada;
    protected ViewImagenLayout imagen;
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
        Log.d(TAG, getMetodo());

        setLayoutItem();

        viewRV = addVista(Estilos.getIdLayout(contexto, "rvlayout"), frLista);

        rv = viewRV.findViewById(R.id.rv);
        refreshLayout = viewRV.findViewById(R.id.swipeRefresh);
        auto = viewRV.findViewById(R.id.auto);
        buscar = viewRV.findViewById(R.id.imgbuscar);
        renovar = viewRV.findViewById(R.id.imgrenovar);
        inicio = viewRV.findViewById(R.id.imginicio);
        lupa = viewRV.findViewById(R.id.imgsearch);
        voz = viewRV.findViewById(R.id.imgvoz);
        frameAnimation = view.findViewById(R.id.frameanimation);
        btnback = viewBotones.findViewById(R.id.btn_back);
        btndelete = viewBotones.findViewById(R.id.btn_del);
        btnsave = viewBotones.findViewById(R.id.btn_save);

        if (!modulo) {
            gone(btndelete);
            gone(btnsave);
        }

        refreshLayout.setColorSchemeResources(
                R.color.s1,
                R.color.s2,
                R.color.s3,
                R.color.s4
        );

        if (!modulo && land) {
            frCuerpo.setOrientation(LinearLayoutCompat.HORIZONTAL);
        } else {
            frCuerpo.setOrientation(LinearLayoutCompat.VERTICAL);
        }

    }

    @Override
    protected void setLayoutExtra() {
        super.setLayoutExtra();
        Log.d(TAG, getMetodo());

        layoutPie = R.layout.btn_sdb;
    }

    protected void setLayoutItem() {
        Log.d(TAG, getMetodo());

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, getMetodo());

        selector();
    }

    protected void selector() {
        Log.d(TAG, getMetodo());

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
        Log.d(TAG, getMetodo());

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
                setOnBack();
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

    protected void setOnBack() {
        Log.d(TAG, getMetodo());

    }

    protected void guardar() {
        Log.d(TAG, getMetodo());

    }

    protected void setControls(View view) {
        Log.d(TAG, getMetodo());
    }

    protected void setAcciones() {
        Log.d(TAG, getMetodo());
    }

    protected abstract TipoViewHolder setViewHolder(View view);

    protected void setDatos() {
        Log.d(TAG, getMetodo());

    }

    protected void listaRV() {
        Log.d(TAG, getMetodo());

        if (listab == null) {
            setLista();

        } else {
            lista = listab;
        }

        setRv();

    }

    protected void setRv() {
        Log.d(TAG, getMetodo());

        int columnas = (int) (rv.getWidth() / (metrics.density * 300));
        if (columnas < 1) {
            columnas = 1;
        }
        layoutManager = new GridLayoutManager(contexto, columnas);
        setManagerRV();
        rv.setLayoutManager(layoutManager);
        adaptadorRV = new RVAdapter(setViewHolder(view), lista, layoutItemRv);
        rv.setAdapter(adaptadorRV);

        adaptadorRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickRV(v);
            }
        });

        adaptadorFiltro = setAdaptadorAuto(contexto, layoutItemAuto, lista);

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
        Log.d(TAG, getMetodo());

    }

    protected void setManagerRV() {
        Log.d(TAG, getMetodo());

    }

    protected abstract ListaAdaptadorFiltro setAdaptadorAuto
            (Context context, int layoutItem, ArrayList lista);

    protected abstract void setLista();


    protected void actualizarConsultasRV() {
        Log.d(TAG, getMetodo());

        if (listab == null) {

            setLista();
        } else {
            lista = listab;
        }

    }

    public abstract void setOnClickRV(Object object);

    protected void onClickRV(View v) {
        Log.d(TAG, getMetodo());

        setOnClickRV(lista.get(rv.getChildAdapterPosition(v)));
        posicion = rv.getChildAdapterPosition(v);
    }

    @Override
    protected void setOnLeftSwipeCuerpo() {
        super.setOnLeftSwipeCuerpo();
        Log.d(TAG, getMetodo());

        if (posicion < lista.size() - 1) {
            posicion++;
            setOnClickRV(lista.get(posicion));
            System.out.println("posicion = " + posicion);
        }
    }

    @Override
    protected void setOnRigthSwipeCuerpo() {
        super.setOnRigthSwipeCuerpo();
        Log.d(TAG, getMetodo());

        if (posicion > 0) {
            posicion--;
            setOnClickRV(lista.get(posicion));
            System.out.println("posicion = " + posicion);
        }
    }

    protected void setOnItemClickAuto() {
        Log.d(TAG, getMetodo());

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

        if (modulo || (!land && !tablet)) {

            maestroDetallePort();

        } else if (land && !tablet && !modulo) {

            maestroDetalleLand();

        } else if (!land && !modulo) {

            maestroDetalleTabletPort();

        } else if (!modulo) {

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
            activityBase.fabNuevo.setSize(FloatingActionButton.SIZE_NORMAL);
            activityBase.fabInicio.setSize(FloatingActionButton.SIZE_NORMAL);
            activityBase.fabVoz.setSize(FloatingActionButton.SIZE_NORMAL);
            activityBase.fabVoz.show();
            activityBase.fabNuevo.show();


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
        Log.d(TAG, getMetodo());
        maestroDetalleSeparados = true;
    }

    protected void setMaestroDetalleLand() {
        Log.d(TAG, getMetodo());
        maestroDetalleSeparados = false;
    }

    protected void setMaestroDetalleTabletLand() {
        Log.d(TAG, getMetodo());
        maestroDetalleSeparados = false;
    }

    protected void setMaestroDetalleTabletPort() {
        Log.d(TAG, getMetodo());
        maestroDetalleSeparados = false;
    }

}
