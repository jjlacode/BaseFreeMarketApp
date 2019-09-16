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

public class MisSorteosPro extends ListadoProductosMisSorteos {

    @Override
    protected String setTipo() {
        return SORTEOPRO;
    }

}
