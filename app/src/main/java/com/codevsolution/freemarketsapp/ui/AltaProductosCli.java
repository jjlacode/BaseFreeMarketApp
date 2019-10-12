package com.codevsolution.freemarketsapp.ui;

import android.content.ContentValues;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.media.ImagenUtil;
import com.codevsolution.base.models.Modelo;
import com.codevsolution.base.models.Productos;
import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.logica.Interactor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.codevsolution.base.logica.InteractorBase.Constantes.SORTEOCLI;

public class AltaProductosCli extends AltaProductosFirebase
        implements Interactor.ConstantesPry, ContratoPry.Tablas {

    @Override
    protected String setTipo() {
        return PRODUCTOCLI;
    }

    @Override
    protected String setTipoSorteo() {
        return SORTEOCLI;
    }



}
