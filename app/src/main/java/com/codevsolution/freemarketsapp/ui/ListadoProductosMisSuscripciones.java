package com.codevsolution.freemarketsapp.ui;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.codevsolution.base.models.Productos;
import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public abstract class ListadoProductosMisSuscripciones extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb
        implements Interactor.ConstantesPry {

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        location = false;
        super.setOnCreateView(view, inflater, container);
    }

    @Override
    protected void setLista() {

        gone(lyChat);
        gone(frCabecera);

        lista = new ArrayList<Productos>();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query querydb = db.child(idUser).child(SUSCRIPCIONES + tipo);

        querydb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (final DataSnapshot suscripcion : dataSnapshot.getChildren()) {


                    final DatabaseReference dbproductosprov = FirebaseDatabase.getInstance().getReference();

                    final Query querydb2 = dbproductosprov.child(tipo).child(suscripcion.getKey());
                    querydb2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                            Productos prodProv = dataSnapshot2.getValue(Productos.class);
                            lista.add(prodProv);

                            setRv();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            dbproductosprov.child(idUser).child(SUSCRIPCIONES + tipo).child(suscripcion.getKey()).removeValue();
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

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
    protected void setDatos() {
        super.setDatos();
    }
}
