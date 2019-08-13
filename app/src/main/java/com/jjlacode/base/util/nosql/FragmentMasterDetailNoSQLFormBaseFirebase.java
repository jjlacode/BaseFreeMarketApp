package com.jjlacode.base.util.nosql;

import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjlacode.base.util.Models.FirebaseFormBase;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.freelanceproject.R;

import java.util.ArrayList;

public abstract class FragmentMasterDetailNoSQLFormBaseFirebase extends FragmentMasterDetailNoSQL {

    protected EditMaterial nombreBase;
    protected EditMaterial descripcionBase;
    protected EditMaterial direccionBase;
    protected EditMaterial emailBase;
    protected EditMaterial telefonoBase;
    protected FirebaseFormBase firebaseFormBase;
    protected DatabaseReference db;
    protected Query query;
    protected ProgressDialog progressDialog;


    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        View viewFB = inflater.inflate(R.layout.fragment_formulario_basico, container, false);
        if (viewFB.getParent() != null) {
            ((ViewGroup) viewFB.getParent()).removeView(viewFB); // <- fix
        }
        frdetalleExtrasante.addView(viewFB);

        nombreBase = (EditMaterial) ctrl(R.id.etnombreformbase);
        descripcionBase = (EditMaterial) ctrl(R.id.etdescformbase);
        direccionBase = (EditMaterial) ctrl(R.id.etdireccionformbase);
        emailBase = (EditMaterial) ctrl(R.id.etemailformbase);
        telefonoBase = (EditMaterial) ctrl(R.id.ettelefonoformbase);
        imagen = (ImageView) ctrl(R.id.imgformbase);

    }

    @Override
    protected void setLista() {

        progressDialog = ProgressDialog.show(contexto, "Cargando lista de " + setTipo(), "Por favor espere...", false, false);

        lista = new ArrayList<FirebaseFormBase>();

        db = FirebaseDatabase.getInstance().getReference();

        query = db.child(setTipo());

        ValueEventListener eventListenerProd = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    FirebaseFormBase firebaseFormBase = ds.getValue(FirebaseFormBase.class);
                    lista.add(firebaseFormBase);
                }

                System.out.println("lista " + setTipo() + ": " + lista.size());
                setRv();

                progressDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };

        query.addValueEventListener(eventListenerProd);
    }

    protected void setMaestroDetalleTabletPort() {
        maestroDetalleSeparados = true;
    }


    protected void setDatos() {

        nombreBase.setText(firebaseFormBase.getNombreBase());
        direccionBase.setText(firebaseFormBase.getDireccionBase());
        emailBase.setText(firebaseFormBase.getEmailBase());
        telefonoBase.setText(firebaseFormBase.getTelefonoBase());
        descripcionBase.setText(firebaseFormBase.getDescripcionBase());

        activityBase.toolbar.setTitle(firebaseFormBase.getTipoBase());

        setImagenFireStore(contexto, firebaseFormBase.getIdchatBase() + setTipo(), imagen);

    }

    @Override
    public void setOnClickRV(Object object) {

        firebaseFormBase = (FirebaseFormBase) object;
        esDetalle = true;
        selector();

    }

    protected abstract String setTipo();
}
