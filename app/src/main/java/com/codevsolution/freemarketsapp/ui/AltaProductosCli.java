package com.codevsolution.freemarketsapp.ui;

import androidx.annotation.NonNull;

import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.models.Productos;
import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AltaProductosCli extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb
        implements Interactor.ConstantesPry {


    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

    }

    @Override
    protected String setTipoProdClon() {
        return PRODUCTOPRO;
    }

    @Override
    protected String setTipo() {
        return PRODUCTOCLI;
    }

    @Override
    protected String setTipoForm() {
        return NUEVO;
    }

    @Override
    protected String setTipoSorteo() {
        return SORTEOCLI;
    }

    protected void sincronizarClon(final Productos prodProv) {

        final String tipo = prodProv.getCategoria();
        final String tipoClon = setTipoProdClon();

        if (nn(prodProv) && nn(prodProv.getIdClon()) && !prodProv.getIdClon().isEmpty()) {

            final String id = prodProv.getId();
            final String idClon = prodProv.getIdClon();

            if (nn(id) && nn(idClon)) {

                final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                Query querydb = db.child(tipoClon).child(idClon);
                querydb.addListenerForSingleValueEvent(new ValueEventListener() {
                    private Productos prodProvClon;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        prodProvClon = dataSnapshot.getValue(Productos.class);
                        db.child(tipo).child(id).setValue(prodProvClon);
                        ImagenUtil.copyImageFirestore(idClon, id);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }
    }

}
