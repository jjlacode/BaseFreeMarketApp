package com.codevsolution.freemarketsapp.ui;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.codevsolution.base.chat.FragmentChatBase;
import com.codevsolution.base.models.Productos;
import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public abstract class ListadoProductosSorteos extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb
        implements Interactor.ConstantesPry {

    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

    }

    @Override
    protected String setTipoForm() {
        return LISTA;
    }

    @Override
    protected String setIdRating() {
        if (nn(prodProv)) {
            return prodProv.getIdprov();
        }
        return id;
    }

    @Override
    protected void setDatos() {

        suscripcion.setText(getString(R.string.participar));
        visible(chatProv);

        super.setDatos();

    }

    @Override
    protected void setLista() {

        gone(lyChat);

        lista = new ArrayList<Productos>();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query querydb = db.child(idUser).child(SUSCRIPCIONESPRO);

        querydb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot user : dataSnapshot.getChildren()) {

                    if (user.getValue(Boolean.class)) {

                        DatabaseReference dbuser = FirebaseDatabase.getInstance().getReference().
                                child(INDICE + tipo).child(user.getKey());
                        dbuser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot prod : dataSnapshot.getChildren()) {

                                    DatabaseReference dbproductosprov = FirebaseDatabase.getInstance().getReference().
                                            child(tipo).child(prod.getKey());

                                    dbproductosprov.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                                            System.out.println("dataSnapshot2 = " + dataSnapshot2.getValue());
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

    @Override
    protected void acciones() {
        super.acciones();

        chatProv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle = new Bundle();
                putBundle(IDCHATF, prodProv.getIdprov());
                putBundle(TIPO, CHAT);
                putBundle(NOMBRECHAT, prodProv.getProveedor());
                icFragmentos.enviarBundleAFragment(bundle, new FragmentChatBase());
            }
        });
    }
}
