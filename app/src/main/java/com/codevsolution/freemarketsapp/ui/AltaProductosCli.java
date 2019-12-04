package com.codevsolution.freemarketsapp.ui;

import android.os.Bundle;

import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.localization.MapZona;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class AltaProductosCli extends AltaProductosFirebase
        implements Interactor.ConstantesPry, ContratoPry.Tablas {

    public AltaProductosCli(FragmentBase parent) {

        this.parent = parent;
    }

    @Override
    protected FragmentBase getParent() {
        return parent;
    }

    @Override
    protected boolean getDatos() {
        return iniciado;
    }

    @Override
    protected void cargarDatos() {

        ((FragmentCRUDProducto) parent).setOnSetDatosCliListener(new FragmentCRUDProducto.OnSetDatosCli() {
            @Override
            public void onSetDatos(Bundle bundle) {

                if (nn(bundle)) {

                    prodCrud = (ModeloSQL) bundle.getSerializable(CRUD);
                    if (prodCrud != null) {
                        prodProv = convertirProdCrud(prodCrud);
                        if (prodProv != null) {
                            prodProv.setTipo(PRODUCTOCLI);
                            esDetalle = true;
                            iniciado = true;
                            System.out.println("iniciadoCli = " + iniciado);

                        }
                    }

                }
                setInicio();
                selector();
            }
        });
    }

    @Override
    protected void setEventsMapNuevo(MapZona mapaZona) {

        mapaZona.setOnReadyMap(new MapZona.OnReadyMap() {
            @Override
            public void onMapClickListener(ArrayList<Marker> listaMarkers) {

                parent.setActivoFrameAnimationCuerpo(true);
                parent.setScrollingDetalleEnable(true);
            }

            @Override
            public void onMapLongClickListener(ArrayList<Marker> listaMarkers) {

                parent.setActivoFrameAnimationCuerpo(false);
                parent.setScrollingDetalleEnable(false);

            }

            @Override
            public void onMyLocationClickListener(long latUserMap, long lonUserMap, ArrayList<String> paisUser) {

                latUser = latUserMap;
                lonUser = lonUserMap;
            }

            @Override
            public void onMarkerDragEnd(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd, ArrayList<String> paisUser) {

                //DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                for (ModeloSQL zona : listaZonasDel.getLista()) {
                    //db.child(LUGARES).child(tipo).child(zona.getString(ZONA_NOMBRE)).child(id).removeValue();
                    firebaseUtil.removeValue(new String[]{LUGARES, tipo, zona.getString(ZONA_NOMBRE)}, id, null);
                    firebaseUtil.removeValue(new String[]{id, ZONAS}, zona.getString(ZONA_NOMBRE), null);
                }

                for (ModeloSQL zona : listaZonasAdd.getLista()) {
                    //db.child(LUGARES).child(tipo).child(zona.getString(ZONA_NOMBRE)).child(id).setValue(true);
                    firebaseUtil.setValue(new String[]{LUGARES, tipo, zona.getString(ZONA_NOMBRE)}, id, TimeDateUtil.ahora(), null);
                    firebaseUtil.setValue(new String[]{id, ZONAS}, zona.getString(ZONA_NOMBRE), TimeDateUtil.ahora(), null);
                }

                parent.setActivoFrameAnimationCuerpo(true);
                parent.setScrollingDetalleEnable(true);
            }

            @Override
            public void onMarkerDragStart() {

                parent.setActivoFrameAnimationCuerpo(false);
                parent.setScrollingDetalleEnable(false);

            }

        });
        mapaZona.setOnMarcadorEventListener(new MapZona.OnMarcadorEvent() {
            @Override
            public void onCreateMarcador(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd) {

                actualizarZonas(listaZonasDel, listaZonasAdd);

            }

            @Override
            public void onDeleteMarcador(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd) {
                actualizarZonas(listaZonasDel, listaZonasAdd);
            }

            @Override
            public void onUpdateMarcador(ListaModeloSQL listaZonasDel, ListaModeloSQL listaZonasAdd) {
                actualizarZonas(listaZonasDel, listaZonasAdd);
            }
        });
    }

    @Override
    protected String setTipo() {
        return PRODUCTOCLI;
    }

    @Override
    protected String setTipoSorteo() {
        return SORTEOCLI;
    }

}
