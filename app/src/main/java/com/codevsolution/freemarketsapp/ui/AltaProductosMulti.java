package com.codevsolution.freemarketsapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.codevsolution.base.util.android.AndroidUtil;
import com.codevsolution.base.util.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

public class AltaProductosMulti extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb
        implements Interactor.ConstantesPry {

    private LinearLayout lyMulti;
    private Switch multiFreelance;
    private Switch multiProveedorWeb;
    private Switch multiEcommerce;
    private Switch multiComercial;
    private Switch multiLugar;
    private ToggleButton verMulti;

    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

    }

    @Override
    protected String setTipoForm() {
        return NUEVO;
    }

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        View viewML = inflater.inflate(R.layout.fragment_multiproducto, container, false);
        if (viewML.getParent() != null) {
            ((ViewGroup) viewML.getParent()).removeView(viewML); // <- fix
        }
        frdetalleExtrasante.addView(viewML);

        lyMulti = (LinearLayout) ctrl(R.id.lymulti);
        multiFreelance = (Switch) ctrl(R.id.swmultifreelance);
        multiProveedorWeb = (Switch) ctrl(R.id.swmultiproveedor);
        multiComercial = (Switch) ctrl(R.id.swmulticomercial);
        multiEcommerce = (Switch) ctrl(R.id.swmultiecommerce);
        multiLugar = (Switch) ctrl(R.id.swmultiLugar);
        verMulti = (ToggleButton) ctrl(R.id.btn_ver_multi);

    }

    @Override
    protected void acciones() {
        super.acciones();

        comprobarMultis();

        verMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verMulti.isChecked()) {
                    visible(lyMulti);
                } else {
                    gone(lyMulti);
                }
            }
        });

        multiFreelance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                if (b) {
                    db.child(INDICE + PRODFREELANCE).child(idUser).child(id).setValue(true);
                    db.child(INDICE + MULTI).child(idUser).child(id).child(PRODFREELANCE).setValue(true);
                } else {
                    db.child(INDICE + PRODFREELANCE).child(idUser).child(id).removeValue();
                    db.child(INDICE + MULTI).child(idUser).child(id).child(PRODFREELANCE).setValue(false);

                }
            }
        });

        multiProveedorWeb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                if (b) {
                    db.child(INDICE + PRODPROVCAT).child(idUser).child(id).setValue(true);
                    db.child(INDICE + MULTI).child(idUser).child(id).child(PRODPROVCAT).setValue(true);
                } else {
                    db.child(INDICE + PRODPROVCAT).child(idUser).child(id).removeValue();
                    db.child(INDICE + MULTI).child(idUser).child(id).child(PRODPROVCAT).setValue(false);
                }
            }
        });

        multiComercial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                if (b) {
                    db.child(INDICE + PRODCOMERCIAL).child(idUser).child(id).setValue(true);
                    db.child(INDICE + MULTI).child(idUser).child(id).child(PRODCOMERCIAL).setValue(true);
                } else {
                    db.child(INDICE + PRODCOMERCIAL).child(idUser).child(id).removeValue();
                    db.child(INDICE + MULTI).child(idUser).child(id).child(PRODCOMERCIAL).setValue(false);
                }
            }
        });

        multiEcommerce.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                if (b) {
                    db.child(INDICE + PRODECOMMERCE).child(idUser).child(id).setValue(true);
                    db.child(INDICE + MULTI).child(idUser).child(id).child(PRODECOMMERCE).setValue(true);
                } else {
                    db.child(INDICE + PRODECOMMERCE).child(idUser).child(id).removeValue();
                    db.child(INDICE + MULTI).child(idUser).child(id).child(PRODECOMMERCE).setValue(false);
                }
            }
        });

        multiLugar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                if (b) {
                    db.child(INDICE + PRODLUGAR).child(idUser).child(id).setValue(true);
                    db.child(INDICE + MULTI).child(idUser).child(id).child(PRODLUGAR).setValue(true);
                } else {
                    db.child(INDICE + PRODLUGAR).child(idUser).child(id).removeValue();
                    db.child(INDICE + MULTI).child(idUser).child(id).child(PRODLUGAR).setValue(false);
                }
            }
        });
    }

    private void comprobarMultis() {

        idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query querydb = db.child(PERFILES).child(idUser);

        querydb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot perfil : dataSnapshot.getChildren()) {

                    if (perfil.getKey().equals(FREELANCE)) {
                        multiFreelance.setEnabled(perfil.getValue(Boolean.class));
                    } else if (perfil.getKey().equals(PROVEEDORWEB)) {
                        multiProveedorWeb.setEnabled(perfil.getValue(Boolean.class));
                    } else if (perfil.getKey().equals(COMERCIAL)) {
                        multiComercial.setEnabled(perfil.getValue(Boolean.class));
                    } else if (perfil.getKey().equals(ECOMMERCE)) {
                        multiEcommerce.setEnabled(perfil.getValue(Boolean.class));
                    } else if (perfil.getKey().equals(LUGAR)) {
                        multiLugar.setEnabled(perfil.getValue(Boolean.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (id != null) {
            DatabaseReference db2 = FirebaseDatabase.getInstance().getReference();
            Query querydb2 = db2.child(INDICE + MULTI).child(idUser).child(id);

            querydb2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot perfil : dataSnapshot.getChildren()) {

                        if (perfil.getKey().equals(PRODFREELANCE)) {
                            multiFreelance.setChecked(perfil.getValue(Boolean.class));
                        } else if (perfil.getKey().equals(PRODPROVCAT)) {
                            multiProveedorWeb.setChecked(perfil.getValue(Boolean.class));
                        } else if (perfil.getKey().equals(PRODCOMERCIAL)) {
                            multiComercial.setChecked(perfil.getValue(Boolean.class));
                        } else if (perfil.getKey().equals(PRODECOMMERCE)) {
                            multiEcommerce.setChecked(perfil.getValue(Boolean.class));
                        } else if (perfil.getKey().equals(PRODLUGAR)) {
                            multiLugar.setChecked(perfil.getValue(Boolean.class));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
}
