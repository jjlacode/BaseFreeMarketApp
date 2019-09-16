package com.codevsolution.base.nosql;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.codevsolution.base.chat.FragmentChatBase;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltro;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.Marcador;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.models.Productos;
import com.codevsolution.base.sqlite.ContratoSystem;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;
import java.util.List;

public abstract class FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb
        extends FragmentMasterDetailNoSQLFirebaseRatingWebMapSus implements ContratoSystem.Tablas, InteractorBase.Constantes {

    protected EditMaterial nombre;
    protected EditMaterial descripcion;
    protected EditMaterial referencia;
    protected EditMaterial proveedor;
    protected EditMaterial precio;
    protected EditMaterial descuento;
    protected EditMaterial claves;
    protected EditMaterial etWeb;
    protected RadioGroup radioGroupProd;
    protected RadioButton radioButtonProd1;
    protected RadioButton radioButtonProd2;

    protected int contadorProdActivo;
    protected int contadorProdTotal;
    protected int limiteProdActivos = 100;
    protected int limiteProdTotal = 150;
    protected Productos prodProv;
    protected Button btnSortear;
    protected Button btnClonar;
    protected Switch sincronizaClon;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        View viewFB = inflater.inflate(R.layout.fragment_formulario_prodprov, container, false);
        if (viewFB.getParent() != null) {
            ((ViewGroup) viewFB.getParent()).removeView(viewFB); // <- fix
        }
        frdetalleExtrasante.addView(viewFB);


        nombre = (EditMaterial) ctrl(R.id.etnombreformprodprov);
        descripcion = (EditMaterial) ctrl(R.id.etdescformprodprov);
        referencia = (EditMaterial) ctrl(R.id.etrefformprodprov);
        proveedor = (EditMaterial) ctrl(R.id.etproveedorformprodprov);
        precio = (EditMaterial) ctrl(R.id.etprecioformprodprov);
        descuento = (EditMaterial) ctrl(R.id.etdescuentoformprodprov);
        claves = (EditMaterial) ctrl(R.id.etclavesformprodprov);
        etWeb = (EditMaterial) ctrl(R.id.etwebformprodprov);
        imagen = (ImagenLayout) ctrl(R.id.imgformprodprov);
        imagen.setIcfragmentos(icFragmentos);
        radioGroupProd = (RadioGroup) ctrl(R.id.radio_group_prod);
        radioButtonProd1 = (RadioButton) ctrl(R.id.radioButtonprod);
        radioButtonProd2 = (RadioButton) ctrl(R.id.radioButtonserv);
        btnSortear = (Button) ctrl(R.id.btn_sorteo_prod);
        btnClonar = (Button) ctrl(R.id.btn_clonar_prod);
        sincronizaClon = (Switch) ctrl(R.id.sw_sincroniza_clon);

        if (tipoForm.equals(NUEVO)) {
            gone(frCabecera);
            nombre.setActivo(true);
            descripcion.setActivo(true);
            referencia.setActivo(true);
            proveedor.setActivo(true);
            precio.setActivo(true);
            etWeb.setActivo(true);
            zona.setActivo(true);

        } else {
            gone(btnEnviarNoticias);
            gone(chActivo);
            btnClonar.setEnabled(false);

        }

    }

    @Override
    protected void setLayoutItem() {

        layoutItem = R.layout.item_list_firebase_formproducto_rating_web;

    }

    @Override
    protected void setLayout() {

    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRVFormProdProvRatingWeb(view);
    }

    @Override
    protected ListaAdaptadorFiltro setAdaptadorAuto(Context context, int layoutItem, ArrayList lista) {
        return new AdapterFiltroFormProdProvRatingWeb(context, layoutItem, lista);
    }

    @Override
    protected void setLista() {

        gone(lyChat);

        if (tipoForm.equals(LISTA) && paisUser != null && paisUser.size() > 0) {

            lista = new ArrayList<Productos>();

            String lugar = "";

            for (int i = 5; i >= getAlcance(); i--) {

                switch (i) {

                    case 5:
                        lugar = (String) paisUser.get(4);
                        break;
                    case 4:
                        lugar = (String) paisUser.get(3);
                        break;
                    case 3:
                        lugar = (String) paisUser.get(2);
                        break;
                    case 2:
                        lugar = (String) paisUser.get(1);
                        break;
                    case 1:
                        lugar = (String) paisUser.get(0);
                        break;
                    case 0:
                        lugar = MUNDIAL;
                        break;

                }

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                Query querydb = db.child(LUGARES).child(tipo).child(lugar.toLowerCase());

                querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot prod : dataSnapshot.getChildren()) {

                            if (prod.getValue(Boolean.class)) {

                                DatabaseReference dbproductosprov = FirebaseDatabase.getInstance().getReference().
                                        child(tipo).child(prod.getKey());

                                dbproductosprov.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {


                                        Productos prodProv = dataSnapshot2.getValue(Productos.class);
                                        lista.add(prodProv);

                                        System.out.println("lista.size() = " + lista.size());
                                        setRv();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        } else if (tipoForm.equals(NUEVO)) {

            gone(btnEnviarNoticias);
            gone(chActivo);
            gone(verVoto);
            gone(frCabecera);

            lista = new ArrayList<Productos>();

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            Query querydb = db.child(INDICE + tipo).child(idUser);

            querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot prod : dataSnapshot.getChildren()) {

                        contadorProdTotal++;

                        if ((boolean) prod.getValue()) {
                            contadorProdActivo++;
                        }

                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        Query querydb = db.child(tipo).child(prod.getKey());

                        querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                                if (nn(dataSnapshot2.getValue())) {
                                    lista.add(dataSnapshot2.getValue(Productos.class));
                                }

                                setRv();

                                if (contadorProdTotal < limiteProdTotal) {
                                    activityBase.fabNuevo.show();
                                } else {
                                    activityBase.fabNuevo.hide();
                                }

                                if (contadorProdActivo < limiteProdActivos) {
                                    chActivo.setEnabled(true);
                                } else {
                                    chActivo.setEnabled(false);
                                    chActivo.setChecked(false);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

            if (lista.size() == 0) {
                activityBase.fabNuevo.show();
                activityBase.fabInicio.hide();
            }

        }

    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        if (tipoForm.equals(NUEVO)) {
            getFirebaseFormBase();
            activityBase.fabInicio.hide();
            activityBase.fabNuevo.show();

        } else {
            activityBase.fabNuevo.hide();
            activityBase.fabInicio.show();
        }
        if (nn(bundle) && bundle.getBoolean(AVISO)) {

            if (nn(id) && prodProv == null) {

                DatabaseReference dbproductosprov = FirebaseDatabase.getInstance().getReference().
                        child(tipo).child(id);

                dbproductosprov.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        prodProv = dataSnapshot.getValue(Productos.class);
                        selector();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }


    }

    protected void setDatos() {


        if (tipoForm.equals(NUEVO)) {

            contarSuscritos();
            visible(btnEnviarNoticias);
            visible(btndelete);
            visible(btnsave);
            visible(chActivo);
            visible(claves);
            visible(etWeb);
            gone(proveedor);
            gone(lystars);
            descuento.setActivo(true);
            visible(radioGroupProd);
            gone(verVoto);
            zona.setActivo(false);
            visible(opcionesZona);
            gone(lyMap);
            gone(radioGroupMap);
            gone(suscripcion);
            visible(suscritos);
            gone(lyChat);
            visible(btnSortear);


        } else {
            gone(opcionesZona);
            visible(proveedor);
            comprobarSuscripcion();
            visible(suscripcion);
            gone(suscritos);
            gone(btndelete);
            gone(btnsave);
            visible(lyChat);

            if (idChat == null) {
                ListaModelo listaChats = CRUDutil.setListaModelo(CAMPOS_CHAT);
                for (Modelo chat : listaChats.getLista()) {
                    if (chat.getString(CHAT_USUARIO).equals(id)) {
                        idChat = chat.getString(CHAT_ID_CHAT);
                    }
                }
            }

            listaMsgChat = CRUDutil.setListaModeloDetalle(CAMPOS_DETCHAT, idChat, TABLA_CHAT, null, DETCHAT_FECHA + " DESC");

            RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat_base);
            rvMsgChat.setAdapter(adaptadorDetChat);
            visible(rvMsgChat);
            noticias.setChecked(true);
        }

        if (prodProv != null && !nuevo) {

            if (prodProv.isSincronizado()) {
                sincronizaClon.setChecked(true);
            } else {
                sincronizaClon.setChecked(false);
            }

            if (prodProv.getTipo().equals(PRODUCTOS)) {
                radioButtonProd1.setChecked(true);
                radioButtonProd2.setChecked(false);
            } else if (prodProv.getTipo().equals(SERVICIOS)) {
                radioButtonProd1.setChecked(false);
                radioButtonProd2.setChecked(true);
            }

            nombre.setText(prodProv.getNombre());
            referencia.setText(prodProv.getRefprov());
            proveedor.setText(prodProv.getProveedor());
            precio.setText(JavaUtil.formatoMonedaLocal(prodProv.getPrecio()));
            descuento.setText(prodProv.getDescProv() + " %");
            descripcion.setText(prodProv.getDescripcion());
            claves.setText(prodProv.getAlcance());
            web = prodProv.getWeb();
            etWeb.setText(prodProv.getWeb());

            id = prodProv.getId();

            if (tipoForm.equals(NUEVO) && !nuevo) {
                chActivo.setChecked(prodProv.isActivo());
            }

            activityBase.toolbar.setSubtitle(prodProv.getNombre());
            accionesImagen();

        }

    }


    @Override
    protected void acciones() {
        super.acciones();

        if (tipoForm.equals(NUEVO)) {

            btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (id != null) {

                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        db.child(tipo).child(id).removeValue();
                        db.child(INDICE + tipo).child(idUser).child(id).removeValue();
                        db.child(RATING).child(tipo).child(id).removeValue();
                        ImagenUtil.deleteImagefirestore(id);

                        Query querydb = db.child(MARC).child(id);
                        querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot marcador : dataSnapshot.getChildren()) {
                                    deleteMarcador(marcador.getValue(Marcador.class), mapa);

                                }
                                esDetalle = false;
                                nuevo = false;
                                selector();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

            });

            btnSortear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    crearSorteo();

                }
            });

            sincronizaClon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (b && nn(prodProv)) {
                        sincronizarClon(prodProv);
                    }
                }
            });

            radioGroupProd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    if (radioButtonProd1.isChecked()) {
                        nombre.setHint(getString(R.string.producto));
                    } else if (radioButtonProd2.isChecked()) {
                        nombre.setHint(getString(R.string.servicio));
                    }
                }
            });

        } else {

            btnClonar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clonarProd();

                }
            });
        }

    }

    protected String setTipoSorteo() {
        return null;
    }

    protected void crearSorteo() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String sorteo = setTipoSorteo();

        prodProv = new Productos();
        if (radioButtonProd1.isChecked()) {
            prodProv.setTipo(PRODUCTOS);
        } else if (radioButtonProd2.isChecked()) {
            prodProv.setTipo(SERVICIOS);
        }

        prodProv.setCategoria(sorteo);
        prodProv.setNombre(nombre.getTexto());
        prodProv.setIdprov(idUser);
        prodProv.setActivo(false);
        prodProv.setDescripcion(descripcion.getTexto());
        prodProv.setPrecio(JavaUtil.comprobarDouble(precio.getTexto()));
        prodProv.setRefprov(referencia.getTexto());
        prodProv.setProveedor(firebaseFormBase.getNombreBase());
        prodProv.setAlcance(claves.getTexto());
        prodProv.setWeb(etWeb.getTexto());

        if (sincronizaClon.isChecked()) {
            prodProv.setSincronizado(true);
        } else {
            prodProv.setSincronizado(false);
        }

        final String idSorteo = db.child(sorteo).push().getKey();
        prodProv.setId(idSorteo);

        if (idSorteo != null) {

            db.child(SORTEO).child(idSorteo).setValue(JavaUtil.comprobarDouble(precio.getTexto()) * 100);
            final String finalSorteo = sorteo;
            db.child(sorteo).child(idSorteo).setValue(prodProv).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    DatabaseReference dbSorteo = FirebaseDatabase.getInstance().getReference();
                    dbSorteo.child(INDICE + finalSorteo).child(idUser).child(idSorteo).
                            setValue(false).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                        Query querydb = db.child(MARC).child(idUser);
                                        querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                    Marcador marcador = child.getValue(Marcador.class);
                                                    DatabaseReference dbmarc = FirebaseDatabase.getInstance().getReference();
                                                    String idMarc = dbmarc.child(MARC).child(idSorteo).push().getKey();
                                                    marcador.setKey(idMarc);
                                                    marcador.setId(idSorteo);
                                                    marcador.setTipo(finalSorteo);

                                                    dbmarc.child(MARC).child(idSorteo).setValue(setLugarZonaMarc(marcador))
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    ImagenUtil.copyImageFirestore(id, idSorteo);
                                                                    Toast.makeText(contexto, getString(R.string.sorteo_creado), Toast.LENGTH_SHORT).show();

                                                                }
                                                            });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    } else {
                                        Toast.makeText(contexto, getString(R.string.fallo_creando_sorteo), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            });

        } else {
            Toast.makeText(contexto, getString(R.string.fallo_creando_sorteo), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void guardar() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        prodProv = new Productos();
        if (radioButtonProd1.isChecked()) {
            prodProv.setTipo(PRODUCTOS);
        } else if (radioButtonProd2.isChecked()) {
            prodProv.setTipo(SERVICIOS);
        }

        prodProv.setCategoria(tipo);
        prodProv.setNombre(nombre.getTexto());
        prodProv.setIdprov(idUser);
        prodProv.setActivo(chActivo.isChecked());
        prodProv.setDescripcion(descripcion.getTexto());
        prodProv.setPrecio(JavaUtil.comprobarDouble(precio.getTexto()));
        prodProv.setDescProv(JavaUtil.comprobarDouble(descuento.getTexto()));
        prodProv.setRefprov(referencia.getTexto());
        if (tipo.equals(SORTEOCLI) || tipo.equals(SORTEOPRO)) {
            prodProv.setProveedor(proveedor.getTexto());
        } else {
            prodProv.setProveedor(firebaseFormBase.getNombreBase());
        }
        prodProv.setAlcance(claves.getTexto());
        prodProv.setWeb(etWeb.getTexto());

        if (nuevo) {
            id = db.child(tipo).push().getKey();
        }
        prodProv.setId(id);

        if (id != null) {

            db.child(tipo).child(id).setValue(prodProv).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (nuevo) {

                        DatabaseReference dbindmarc = FirebaseDatabase.getInstance().getReference();
                        Query queryddindmarc = dbindmarc.child(MARC).child(idUser);
                        queryddindmarc.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot marcadorUser : dataSnapshot.getChildren()) {

                                    Marcador marcador = marcadorUser.getValue(Marcador.class);
                                    DatabaseReference dbmarc = FirebaseDatabase.getInstance().getReference();
                                    String idMarc = dbmarc.child(MARC).child(id).push().getKey();
                                    marcador.setKey(idMarc);
                                    marcador.setId(id);
                                    marcador.setTipo(tipo);
                                    dbmarc.child(MARC).child(id).child(idMarc).setValue(setLugarZonaMarc(marcador));
                                    Marker mark = mapa.crearMarcadorMap((double) ((marcador.getLatitud()) / 1000),
                                            (double) ((marcador.getLongitud()) / 1000), 5, "", "", true);
                                    marcador.setIdMark(mark.getId());
                                    listaMarcadores.add(marcador);

                                    nuevo = false;
                                    selector();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        setMarcadores();

                        Toast.makeText(contexto, "Registro creado con exito", Toast.LENGTH_SHORT).show();
                    }
                }

            });

            if (!nuevo) {
                Toast.makeText(contexto, "Registro actualizado con exito", Toast.LENGTH_SHORT).show();
            }

            if (chActivo.isChecked()) {
                db.child(INDICE + tipo).child(idUser).child(id).setValue(true);
            } else {
                db.child(INDICE + tipo).child(idUser).child(id).setValue(false);
            }

        } else {
            Toast.makeText(contexto, getString(R.string.fallo_subiendo_registro), Toast.LENGTH_SHORT).show();
        }
    }

    protected String setTipoProdClon() {

        return null;
    }

    protected void clonarProd() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        prodProv = new Productos();

        if (radioButtonProd1.isChecked()) {
            prodProv.setTipo(PRODUCTOS);
        } else if (radioButtonProd2.isChecked()) {
            prodProv.setTipo(SERVICIOS);
        }

        final String tipoClon = setTipoProdClon();
        prodProv.setCategoria(tipoClon);
        prodProv.setNombre(nombre.getTexto());
        prodProv.setIdprov(idUser);
        prodProv.setIdClon(id);
        prodProv.setActivo(chActivo.isChecked());
        prodProv.setDescripcion(descripcion.getTexto());
        prodProv.setPrecio(JavaUtil.comprobarDouble(precio.getTexto()));
        prodProv.setRefprov(referencia.getTexto());
        prodProv.setProveedor(firebaseFormBase.getNombreBase());
        prodProv.setAlcance(claves.getTexto());
        prodProv.setWeb(etWeb.getTexto());

        final String idnew = db.child(tipoClon).push().getKey();
        prodProv.setId(idnew);

        if (nn(id) && nn(idnew)) {

            db.child(INDICE + tipoClon).child(idUser).child(idnew).setValue(false);

            db.child(tipoClon).child(idnew).setValue(prodProv).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    Query querydb = db.child(MARC).child(idUser);
                    querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot marcadorUser : dataSnapshot.getChildren()) {

                                Marcador marcador = marcadorUser.getValue(Marcador.class);
                                DatabaseReference dbmarc = FirebaseDatabase.getInstance().getReference();
                                String idMarc = dbmarc.child(MARC).child(idnew).push().getKey();
                                marcador.setKey(idMarc);
                                marcador.setId(idnew);
                                marcador.setTipo(tipoClon);
                                dbmarc.child(MARC).child(idnew).child(idMarc).setValue(setLugarZonaMarc(marcador))
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    ImagenUtil.copyImageFirestore(id, idnew);
                                                    Toast.makeText(contexto, "Se ha clonado el producto", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(contexto, "Ha ocurrido un error al clonar producto", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });

        } else {
            Toast.makeText(contexto, getString(R.string.fallo_subiendo_registro), Toast.LENGTH_SHORT).show();
        }
    }

    protected void sincronizarClon(final Productos prodProv) {


    }

    @Override
    protected String setTipoRating() {
        if (nn(prodProv)) {
            return prodProv.getCategoria();
        }
        return null;
    }

    @Override
    protected String setIdRating() {
        if (nn(prodProv)) {
            return prodProv.getId();
        }
        return null;
    }

    @Override
    public void setOnClickRV(Object object) {

        prodProv = (Productos) object;
        id = prodProv.getId();
        esDetalle = true;

        selector();

    }

    protected void setMaestroDetalleTabletPort() {
        maestroDetalleSeparados = true;
    }


    private class AdapterFiltroFormProdProvRatingWeb extends ListaAdaptadorFiltro {


        public AdapterFiltroFormProdProvRatingWeb(Context contexto, int R_layout_IdView, ArrayList entradas) {
            super(contexto, R_layout_IdView, entradas);

        }

        @Override
        public void onEntrada(Object entrada, View view) {

            ImageView imagen = view.findViewById(R.id.imglformprodprovratingweb);
            TextView nombre = view.findViewById(R.id.tvnomlformprodprovratingweb);
            TextView descripcion = view.findViewById(R.id.tvdesclformprodprovratingweb);
            TextView referencia = view.findViewById(R.id.tvreflformprodprovratingweb);
            TextView proveedor = view.findViewById(R.id.tvprovlformprodprovratingweb);
            TextView precio = view.findViewById(R.id.tvpreciolformprodprovratingweb);

            nombre.setText(((Productos) entrada).getNombre());
            descripcion.setText(((Productos) entrada).getDescripcion());
            referencia.setText(((Productos) entrada).getRefprov());
            proveedor.setText(((Productos) entrada).getProveedor());
            precio.setText(JavaUtil.formatoMonedaLocal(((Productos) entrada).getPrecio()));

            if (nn(((Productos) entrada).getId()) && !((Productos) entrada).getId().equals("")) {
                ImagenUtil.setImageFireStoreCircle(((Productos) entrada).getId() +
                        ((Productos) entrada).getCategoria(), imagen);
            }

        }

        @Override
        public List onFilter(ArrayList entradas, CharSequence constraint) {

            List suggestion = new ArrayList();


            for (Object entrada : entradas) {

                if (((Productos) entrada).getNombre() != null && ((Productos) entrada).getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((Productos) entrada).getRefprov() != null && ((Productos) entrada).getRefprov().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((Productos) entrada).getDescripcion() != null && ((Productos) entrada).getDescripcion().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                } else if (((Productos) entrada).getAlcance() != null && ((Productos) entrada).getAlcance().toLowerCase().contains(constraint.toString().toLowerCase())) {

                    suggestion.add(entrada);
                }
            }
            return suggestion;
        }
    }

    private class ViewHolderRVFormProdProvRatingWeb extends BaseViewHolder implements TipoViewHolder {

        ImageView imagen;
        TextView nombre;
        TextView descripcion;
        TextView proveedor;
        TextView referencia;
        TextView precio;
        TextView tipo;
        ImageButton btnchat;
        WebView webView;
        ProgressBar progressBarWebCard;
        RatingBar ratingBarCard;
        RatingBar ratingBarUserCard;
        NestedScrollView lylweb;
        String webprod;

        public ViewHolderRVFormProdProvRatingWeb(View view) {
            super(view);

            imagen = view.findViewById(R.id.imglformprodprovratingweb);
            nombre = view.findViewById(R.id.tvnomlformprodprovratingweb);
            tipo = view.findViewById(R.id.tvtipolformprodprovratingweb);
            descripcion = view.findViewById(R.id.tvdesclformprodprovratingweb);
            referencia = view.findViewById(R.id.tvreflformprodprovratingweb);
            proveedor = view.findViewById(R.id.tvprovlformprodprovratingweb);
            precio = view.findViewById(R.id.tvpreciolformprodprovratingweb);
            webView = view.findViewById(R.id.browserweblformprodprovratingweb);
            progressBarWebCard = view.findViewById(R.id.progressBarWebCard);
            btnchat = view.findViewById(R.id.btnchatlformprodprovratingweb);
            ratingBarCard = view.findViewById(R.id.ratingBarCardformprodprovratingweb);
            ratingBarUserCard = view.findViewById(R.id.ratingBarUserCardformprodprovratingweb);

            lylweb = view.findViewById(R.id.lylwebformprodprovratingweb);

        }

        @Override
        public void bind(ArrayList<?> lista, int position) {

            final Productos prodProv = (Productos) lista.get(position);

            if (prodProv.getTipo().equals(SERVICIOS)) {
                tipo.setText(R.string.servicio_may);
            } else if (prodProv.getTipo().equals(PRODUCTOS)) {
                tipo.setText(R.string.producto_may);
            }

            nombre.setText(prodProv.getNombre());
            descripcion.setText(prodProv.getDescripcion());
            proveedor.setText(prodProv.getProveedor());
            referencia.setText(prodProv.getRefprov());
            precio.setText(JavaUtil.formatoMonedaLocal(prodProv.getPrecio()));
            webprod = prodProv.getWeb();

            if (nn(prodProv.getId()) && !prodProv.getId().equals("")) {
                ImagenUtil.setImageFireStoreCircle(prodProv.getId(), imagen);
            }

            if (webprod != null && JavaUtil.isValidURL(webprod)) {

                visible(lylweb);

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setDisplayZoomControls(false);

                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                // Cargamos la web


                webView.loadUrl(webprod);
                webView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int progress) {
                        progressBarWebCard.setProgress(0);
                        progressBarWebCard.setVisibility(View.VISIBLE);
                        progressBarWebCard.setProgress(progress * 1000);

                        progressBarWebCard.incrementProgressBy(progress);

                        if (progress == 100) {
                            progressBarWebCard.setVisibility(View.GONE);
                        }
                    }
                });

            } else {
                gone(lylweb);
            }

            if (!tipoForm.equals(NUEVO)) {
                btnchat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        perfilUser = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
                        ListaModelo listaChats = new ListaModelo(CAMPOS_CHAT);
                        String idChat = null;
                        for (Modelo chat : listaChats.getLista()) {
                            if (chat.getString(CHAT_USUARIO).equals(prodProv.getIdprov())) {
                                idChat = chat.getString(CHAT_ID_CHAT);
                            }
                        }
                        if (idChat == null) {
                            if (nn(firebaseFormBase)) {
                                ContentValues values = new ContentValues();
                                values.put(CHAT_NOMBRE, firebaseFormBase.getNombreBase());
                                values.put(CHAT_USUARIO, firebaseFormBase.getIdchatBase());
                                values.put(CHAT_CREATE, TimeDateUtil.ahora());
                                values.put(CHAT_TIMESTAMP, TimeDateUtil.ahora());

                                idChat = CRUDutil.crearRegistroId(TABLA_CHAT, values);
                            } else {
                                Toast.makeText(contexto, "Debe tener un perfil de " +
                                        perfilUser + " con el nombre o seudonimo como minimo para utilizar el chat", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (idChat != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString(CAMPO_ID, idChat);
                            icFragmentos.enviarBundleAFragment(bundle, new FragmentChatBase());
                        }
                    }
                });
            } else {
                gone(btnchat);
                gone(ratingBarUserCard);
                gone(proveedor);
            }

            ratingBarCard.setRating(0);
            recuperarVotos(ratingBarCard, prodProv.getCategoria(), prodProv.getId());
            ratingBarCard.setIsIndicator(true);

            ratingBarUserCard.setRating(0);
            recuperarVotoUsuario(ratingBarUserCard, contexto, prodProv.getCategoria(), prodProv.getId());
            ratingBarUserCard.setIsIndicator(true);


            super.bind(lista, position);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVFormProdProvRatingWeb(view);
        }
    }

}
