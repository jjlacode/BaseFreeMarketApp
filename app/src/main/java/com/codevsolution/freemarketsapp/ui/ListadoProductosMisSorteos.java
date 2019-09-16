package com.codevsolution.freemarketsapp.ui;


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

public abstract class ListadoProductosMisSorteos extends ListadoProductosSorteos
        implements Interactor.ConstantesPry {

    @Override
    protected void setLista() {

        gone(lyChat);

        lista = new ArrayList<Productos>();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query querydb = db.child(idUser).child(SUSCRIPCIONES + tipo);

        querydb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot sorteo : dataSnapshot.getChildren()) {


                    DatabaseReference dbproductosprov = FirebaseDatabase.getInstance().getReference().
                            child(tipo).child(sorteo.getKey());

                    dbproductosprov.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {


                            Productos prodProv = dataSnapshot2.getValue(Productos.class);
                            lista.add(prodProv);

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
