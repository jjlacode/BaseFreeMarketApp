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

public class AltaProductosPro extends AltaProductosFirebase
        implements Interactor.ConstantesPry {

    @Override
    protected String setTipo() {
        return PRODUCTOPRO;
    }

    @Override
    protected String setTipoSorteo() {
        return SORTEOPRO;
    }

}
