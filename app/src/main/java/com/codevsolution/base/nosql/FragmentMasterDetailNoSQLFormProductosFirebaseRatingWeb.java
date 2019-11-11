package com.codevsolution.base.nosql;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.NestedScrollView;

import com.chargebee.models.Subscription;
import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltro;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.AndroidUtil;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.ImagenLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.chat.FragmentChatBase;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.firebase.ContratoFirebase;
import com.codevsolution.base.firebase.FirebaseUtil;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.models.ListaModeloSQL;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.models.Productos;
import com.codevsolution.base.models.Rating;
import com.codevsolution.base.sqlite.ContratoSystem;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.InteractorSuscriptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public abstract class FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb
        extends FragmentMasterDetailNoSQLFirebaseRatingWebMapSus implements ContratoSystem.Tablas, InteractorBase.Constantes {

    protected EditMaterialLayout nombre;
    protected EditMaterialLayout descripcion;
    protected EditMaterialLayout referencia;
    protected EditMaterialLayout proveedor;
    protected EditMaterialLayout precio;
    protected EditMaterialLayout descuento;
    protected EditMaterialLayout claves;
    protected EditMaterialLayout etWeb;
    protected EditMaterial limitProdAct;
    protected EditMaterial prodActUsados;
    protected EditMaterial limitProd;
    protected EditMaterial prodUsados;
    protected RadioGroup radioGroupProd;
    protected RadioButton radioButtonProd1;
    protected RadioButton radioButtonProd2;

    protected long contadorProdActivo;
    protected long contadorProdTotal;
    protected long limiteProdActivos;
    protected long limiteProdTotal;
    protected Productos prodProv;
    protected Button btnSortear;
    protected Button btnClonar;
    protected String idProv;
    private ArrayList<Subscription> listaSus;
    protected ModeloSQL prodCrud;


    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, frdetalleExtrasante);

        imagen = (ImagenLayout) vistaForm.addVista(new ImagenLayout(contexto));
        imagen.setFocusable(false);
        nombre = vistaForm.addEditMaterialLayout(getString(R.string.nombre));
        descripcion = vistaForm.addEditMaterialLayout(getString(R.string.descripcion));
        referencia = vistaForm.addEditMaterialLayout(getString(R.string.descripcion));
        proveedor = vistaForm.addEditMaterialLayout(getString(R.string.proveedor));
        precio = vistaForm.addEditMaterialLayout(getString(R.string.importe));
        descuento = vistaForm.addEditMaterialLayout(getString(R.string.descuento_proveedor));
        claves = vistaForm.addEditMaterialLayout(getString(R.string.palabras_clave));
        etWeb = vistaForm.addEditMaterialLayout(getString(R.string.web));
        radioGroupProd = (RadioGroup) vistaForm.addVista(new RadioGroup(contexto));
        radioButtonProd1 = (RadioButton) vistaForm.addVista(new RadioButton(contexto));
        radioButtonProd1.setText(R.string.producto);
        radioButtonProd2 = (RadioButton) vistaForm.addVista(new RadioButton(contexto));
        radioButtonProd2.setText(R.string.servicios);
        btnSortear = vistaForm.addButtonSecondary(R.string.sortear);
        btnClonar = vistaForm.addButtonSecondary(R.string.clonar_prod_a_cliente);
        gone(btnClonar);


        if (tipoForm.equals(NUEVO)) {
            visible(frCabecera);
            nombre.setActivo(false);
            descripcion.setActivo(false);
            referencia.setActivo(false);
            proveedor.setActivo(false);
            precio.setActivo(false);
            etWeb.setActivo(false);
            InteractorSuscriptions interactorSuscriptions = new InteractorSuscriptions(contexto);
            interactorSuscriptions.comprobarSuscripciones(new InteractorSuscriptions.CheckSubscriptions() {
                @Override
                public void onNotSubscriptions() {

                    System.out.println("No hay suscripciones activas");
                }

                @Override
                public void onProductLimit() {

                    System.out.println("se ha alcanzado el limite de productos publicados");
                }

                @Override
                public void onError(String msgError) {

                    System.out.println(msgError);
                }

                @Override
                public void onCheckSuscriptionsOk(ArrayList<Subscription> listaSuscripciones) {

                    System.out.println("---------------------------------Check suscripción OK");
                    System.out.println("listaSuscripciones.size() = " + listaSuscripciones.size());
                    for (Subscription subscription : listaSuscripciones) {
                        limiteProdActivos += subscription.planQuantity();
                        limiteProdTotal = Math.round(limiteProdActivos * 1.5);
                    }
                    alComprobarSuscripciones();
                }
            });

        } else {
            gone(frCabecera);
            gone(btnEnviarNoticias);
            gone(chActivo);
            btnClonar.setEnabled(false);

        }

        activityBase.fabNuevo.hide();
        activityBase.fabInicio.show();

    }

    @Override
    protected void setLayoutItem() {

        layoutItemRv = R.layout.item_list_firebase_formproducto_rating_web;

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

        System.out.println("paisUser = " + paisUser);
        if (tipoForm.equals(LISTA) && paisUser != null && paisUser.size() > 0) {

            lista = new ArrayList<Productos>();

            String lugar = "";

            for (int i = 5; i >= mapaZona.getAlcance(); i--) {

                switch (i) {

                    case 5:
                        lugar = paisUser.get(4);
                        break;
                    case 4:
                        lugar = paisUser.get(3);
                        break;
                    case 3:
                        lugar = paisUser.get(2);
                        break;
                    case 2:
                        lugar = paisUser.get(1);
                        break;
                    case 1:
                        lugar = paisUser.get(0);
                        break;
                    case 0:
                        lugar = MUNDIAL;
                        break;

                }

                System.out.println("lugar = " + lugar);

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                Query querydb = db.child(LUGARES).child(tipo).child(lugar.toLowerCase());

                querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot prod : dataSnapshot.getChildren()) {

                            if (prod.getValue(Boolean.class)) {

                                DatabaseReference dbproductosprov = FirebaseDatabase.getInstance().getReference().
                                        child(PRODUCTOS).child(prod.getKey());

                                dbproductosprov.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {


                                        final Productos prodProv = dataSnapshot2.getValue(Productos.class);
                                        DatabaseReference dbproduser = FirebaseDatabase.getInstance().getReference();
                                        dbproduser.child(prodProv.getIdprov()).child(SUSESTADO).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                String estado = dataSnapshot.getValue(String.class);
                                                System.out.println("estado = " + estado);
                                                System.out.println("estado active = "+Subscription.Status.ACTIVE);
                                                if (estado.equals("ACTIVE")) {

                                                    lista.add(prodProv);

                                                    System.out.println("lista.size() = " + lista.size());
                                                    setRv();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


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
            gone(frCabecera);

            lista = new ArrayList<Productos>();

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            Query querydb = db.child(INDICE + tipo).child(idUser);

            querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot prod : dataSnapshot.getChildren()) {

                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        Query querydb = db.child(PRODUCTOS).child(prod.getKey());

                        querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                                if (nn(dataSnapshot2.getValue())) {
                                    lista.add(dataSnapshot2.getValue(Productos.class));
                                }

                                setRv();


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

        }
    }

    protected void alComprobarSuscripciones() {

    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();


            activityBase.fabNuevo.hide();
            activityBase.fabInicio.show();

        if (nn(bundle) && bundle.getBoolean(AVISO)) {

            if (nn(id) && prodProv == null) {

                DatabaseReference dbproductosprov = FirebaseDatabase.getInstance().getReference().
                        child(PRODUCTOS).child(id);

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

        imagen.setTextTitulo(tipo.toUpperCase());

        if (tipoForm.equals(NUEVO)) {

            contarSuscritos();
            visible(btnEnviarNoticias);
            visible(btndelete);
            visible(btnsave);
            visible(chActivo);
            descuento.setActivo(false);
            visible(radioGroupProd);
            gone(suscripcion);
            visible(suscritos);
            gone(lyChat);
            visible(btnSortear);
            if (contadorProdActivo < limiteProdActivos) {
                chActivo.setEnabled(true);
            } else {
                chActivo.setEnabled(false);
            }
            gone(nombre.getLinearLayout());
            gone(descripcion.getLinearLayout());
            gone(precio.getLinearLayout());
            gone(proveedor.getLinearLayout());
            gone(referencia.getLinearLayout());
            gone(descuento.getLinearLayout());
            gone(claves.getLinearLayout());
            gone(etWeb.getLinearLayout());

        } else {

            visible(proveedor.getLinearLayout());
            comprobarSuscripcion();
            visible(suscripcion);
            gone(suscritos);
            gone(btndelete);
            gone(btnsave);
            visible(lyChat);

            if (idChat == null) {
                ListaModeloSQL listaChats = CRUDutil.setListaModelo(CAMPOS_CHAT);
                for (ModeloSQL chat : listaChats.getLista()) {
                    if (chat.getString(CHAT_USUARIO).equals(id) && chat.getString(CHAT_TIPO).equals(tipo)) {
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
            idProv = prodProv.getIdprov();

            id = prodProv.getId();

            if (tipoForm.equals(NUEVO) && !nuevo) {
                chActivo.setChecked(prodProv.isActivo());
            }

            activityBase.toolbar.setSubtitle(prodProv.getNombre());
            accionesImagen();
            if (nn(prodCrud)) {
                guardar();
            }

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
                        db.child(PRODUCTOS).child(id).removeValue();
                        db.child(INDICE + tipo).child(idUser).child(id).removeValue();
                        db.child(RATING).child(tipo).child(id).removeValue();
                        ImagenUtil.deleteImagefirestore(id);

                        esDetalle = false;
                        nuevo = false;
                        selector();

                    }
                }

            });

            btnSortear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    crearSorteo();

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
            chActivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        visible(btnPublicar);
                    } else {
                        gone(btnPublicar);
                    }
                }
            });


        } else {


        }

    }

    protected String setTipoSorteo() {
        return null;
    }

    protected void crearSorteo() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String sorteo = setTipoSorteo();

        final String idSorteo = db.child(sorteo).push().getKey();
        prodProv.setActivo(false);
        prodProv.setId(idSorteo);

        if (idSorteo != null) {

            db.child(SORTEO).child(idSorteo).setValue(0L);
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

                                        ImagenUtil.copyImageFirestore(id, idSorteo);
                                        Toast.makeText(contexto, getString(R.string.sorteo_creado), Toast.LENGTH_SHORT).show();

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

        System.out.println("Guardando prodProv");
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        prodProv.setActivo(chActivo.isChecked());

        id = prodProv.getId();
        if (tipo == null) {
            setTipo();
        }
        if (prodProv.getIdprov() == null) {
            prodProv.setIdprov(idUser);
        }
        if (id == null) {
            nuevo = true;
        }

        mapaZona.setId(id);
        mapaZona.setTipo(tipo);


            firebaseUtil.setValue(ContratoFirebase.getRutaProductos(), id, prodProv, new FirebaseUtil.OnSetValue() {
                @Override
                public void onSetValueOk(String key) {

                    if (nuevo) {

                        ListaModeloSQL listaMarcUser = new ListaModeloSQL(CAMPOS_MARCADOR, MARCADOR_ID_REL, idUser, null);

                        for (ModeloSQL marcUser : listaMarcUser.getLista()) {

                            mapaZona.crearMarcador(tipo, id, marcUser.getLong(MARCADOR_LATITUD), marcUser.getLong(MARCADOR_LONGITUD));

                        }

                        nuevo = false;
                        selector();
                        prodProv.setId(key);

                        Toast.makeText(contexto, "Registro creado con exito", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(contexto, "Registro actualizado con exito", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSetValueFail(String key) {

                    Toast.makeText(contexto, getString(R.string.fallo_subiendo_registro), Toast.LENGTH_SHORT).show();
                }
            });

        if (chActivo.isChecked() && id != null) {
                firebaseUtil.setValue(ContratoFirebase.getRutaIndiceProducto(tipo), id, true, null);
                //db.child(INDICE + tipo).child(idUser).child(id).setValue(true);
            } else {
                firebaseUtil.setValue(ContratoFirebase.getRutaIndiceProducto(tipo), id, false, null);
                //db.child(INDICE + tipo).child(idUser).child(id).setValue(false);
            }

        if (nn(prodCrud) && nn(prodProv)) {
            alGuardar(prodProv, prodCrud);
        }

    }

    protected void actualizarProdCrud(Productos prodProv, ModeloSQL prodCrud) {

    }


    protected void alGuardar(Productos prodProv, ModeloSQL prodCrud) {

    }


    protected void crearProdCrud(Productos prodProv) {


    }

    protected void sincronizarClon(final Productos prodProv) {


    }

    @Override
    protected String setIdRating() {
        if (nn(prodProv)) {
            return prodProv.getId();
        }
        return null;
    }

    @Override
    protected void enviarVoto(Context contexto, final String id, float valor, String comentario) {

        perfilUser = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
        idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);

        if (nombreVoto != null) {

            Rating rat = new Rating(valor, perfilUser, id, idUser, nombreVoto, comentario, TimeDateUtil.ahora());
            FirebaseDatabase.getInstance().getReference().child(RATING).child(id).child(idUser + perfilUser).setValue(rat, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    recuperarVotos(id);
                }
            });
        } else {
            Toast.makeText(contexto, "Debe tener un perfil de " + perfilUser + " para votar", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void setOnClickRV(Object object) {

        prodProv = (Productos) object;
        id = prodProv.getId();
        esDetalle = true;
        if (nn(id)) {

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
                        ListaModeloSQL listaChats = new ListaModeloSQL(CAMPOS_CHAT);
                        String idChat = null;
                        for (ModeloSQL chat : listaChats.getLista()) {
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
                                values.put(CHAT_TIPO, CHAT);

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
            recuperarVotos(prodProv.getId());
            ratingBarCard.setIsIndicator(true);

            ratingBarUserCard.setRating(0);
            recuperarVotoUsuario(contexto, prodProv.getId());
            ratingBarUserCard.setIsIndicator(true);


            super.bind(lista, position);
        }

        protected void recuperarVotos(final String id) {

            DatabaseReference db;

            if (id != null) {
                db = FirebaseDatabase.getInstance().getReference().child(RATING).child(id);


                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        ArrayList<Rating> listaVotos = new ArrayList<>();

                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            System.out.println("child.getValue() = " + child.getValue());
                            listaVotos.add(child.getValue(Rating.class));

                        }

                        ratingBarCard.setRating(0.0f);
                        Drawable progressDrawable = ratingBarCard.getProgressDrawable();
                        if (progressDrawable != null) {
                            DrawableCompat.setTint(progressDrawable, Estilos.getIdDrawable(contexto, "color_star_defecto"));
                        }

                        float rating = 0.0f;
                        float nVotos = 0.0f;
                        long ultimoVotoTemp = 0;


                        for (Rating rat : listaVotos) {

                            float voto = rat.getValor();

                            System.out.println("voto = " + voto);

                            if (voto == 1) {
                                nVotos++;
                            } else if (voto == 2) {
                                nVotos++;
                            } else if (voto == 3) {
                                nVotos++;
                            } else if (voto == 4) {
                                nVotos++;
                            } else if (voto == 5) {
                                nVotos++;
                            }

                            rating += voto;

                            if (rat.getFecha() > ultimoVotoTemp) {
                                ultimoVotoTemp = rat.getFecha();
                            }
                        }

                        if (nVotos > 0) {
                            rating /= nVotos;
                            ratingBarCard.setRating(rating);
                        } else {
                            ratingBarCard.setRating(0.0f);
                        }

                        String color = "";
                        if (rating > 3) {
                            color = "Color_star_ok";
                        } else if (rating > 1.5) {
                            color = "Color_star_acept";
                        } else if (rating > 0) {
                            color = "Color_star_notok";
                        } else {
                            color = "color_star_defecto";
                        }

                        ratingBarCard.setProgressTintList(ColorStateList.valueOf(Estilos.getIdColor(contexto, color)));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                db.addValueEventListener(valueEventListener);
            }

        }

        public void recuperarVotoUsuario(final Context contexto, final String id) {

            idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);
            perfilUser = AndroidUtil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
            DatabaseReference db;

            if (id != null && tipo != null) {
                db = FirebaseDatabase.getInstance().getReference().child(RATING).child(id).child(idUser + perfilUser);


                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        Rating rat = dataSnapshot.getValue(Rating.class);

                        if (rat != null) {
                            float rating = rat.getValor();
                            ratingBarUserCard.setRating(rating);

                            String color = "";
                            if (rating > 3) {
                                color = "Color_star_ok";
                            } else if (rating > 1.5) {
                                color = "Color_star_acept";
                            } else if (rating > 0) {
                                color = "Color_star_notok";
                            } else {
                                color = "color_star_defecto";
                            }

                            ratingBarUserCard.setProgressTintList(ColorStateList.valueOf(Estilos.getIdColor(contexto, color)));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };

                db.addValueEventListener(valueEventListener);

            }
        }
        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVFormProdProvRatingWeb(view);
        }
    }

    public static class ViewHolderRVMsgChat extends BaseViewHolder implements TipoViewHolder {

        TextView mensaje, fecha;
        CardView card;
        WebView webView;
        ProgressBar progressBarWebCard;
        NestedScrollView lylweb;

        public ViewHolderRVMsgChat(View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.tvmsgchat_base);
            fecha = itemView.findViewById(R.id.tvmsgchatfecha_base);
            card = itemView.findViewById(R.id.cardmsgchat_base);
            webView = itemView.findViewById(R.id.browserwebl_chat_base);
            progressBarWebCard = itemView.findViewById(R.id.progressBarWebCardchat);
            lylweb = itemView.findViewById(R.id.lylweb_chat_base);

        }

        @Override
        public void bind(ModeloSQL modeloSQL) {

            int tipo = modeloSQL.getInt(DETCHAT_TIPO);

            mensaje.setText(modeloSQL.getString(DETCHAT_MENSAJE));
            fecha.setText(TimeDateUtil.getDateTimeString(modeloSQL.getLong(DETCHAT_FECHA)));

            if (tipo == RECIBIDO) {

                card.setCardBackgroundColor(getContext().getResources().getColor(R.color.Color_msg_recibido));
                card.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
                params.setMargins((int) (10 * densidad), (int) (10 * densidad), (int) (10 * densidad), 0);

                card.setLayoutParams(params);

                card.setVisibility(View.VISIBLE);

            } else {
                card.setVisibility(View.GONE);

            }

            String webprod = modeloSQL.getString(DETCHAT_URL);


            if (webprod != null && JavaUtil.isValidURL(webprod)) {

                lylweb.setVisibility(View.VISIBLE);

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
                lylweb.setVisibility(View.GONE);
            }

            super.bind(modeloSQL);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVMsgChat(view);
        }
    }
}
