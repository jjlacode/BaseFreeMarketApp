package com.codevsolution.freemarketsapp.ui;

import android.text.Editable;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.codevsolution.base.util.JavaUtil;
import com.codevsolution.base.util.android.controls.EditMaterial;
import com.codevsolution.base.util.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.Timer;
import java.util.TimerTask;

public class AltaProductosSorteos extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb
        implements Interactor.ConstantesPry {

    private String ganador;

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        ganador = getStringBundle(GANADORSORTEO, NULL);

    }

    protected void getFirebaseFormBase() {

        nombreChat = SORTEO;
        setRv();
    }

    @Override
    protected void setLayout() {

    }

    @Override
    protected void setInicio() {

        referencia.setHint(getString(R.string.referencia_sorteo));
        nombre.setHint(getString(R.string.producto_sorteado));
        precio.setHint(getString(R.string.valor_producto));
        chActivo.setTextOn(getString(R.string.en_sorteo));
        chActivo.setTextOff(getString(R.string.empezar_sorteo));
        suscritos.setHint(getString(R.string.participantes));

    }

    @Override
    protected String setTipoForm() {
        return NUEVO;
    }

    @Override
    protected void setDatos() {
        super.setDatos();

        gone(btnSortear);

        if (JavaUtil.comprobarLong(suscritos.getTexto()) > 0) {
            chActivo.setEnabled(false);
        }
        visible(maxParticipantes);
        maxParticipantes.setHint(getString(R.string.max_participantes_sorteo) + " " +
                Math.round(JavaUtil.comprobarDouble(precio.getTexto()) * 100));

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child(SORTEO).child(id);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxParticipantes.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void acciones() {
        super.acciones();

        maxParticipantes.setAlCambiarListener(new EditMaterial.AlCambiarListener() {
            @Override
            public void antesCambio(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void cambiando(CharSequence s, int start, int before, int count) {

                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void despuesCambio(Editable s) {

                if (JavaUtil.comprobarLong(s.toString()) >
                        Math.round(JavaUtil.comprobarDouble(precio.getTexto()) * 100)) {
                    maxParticipantes.setText(String.valueOf(Math.round(JavaUtil.comprobarDouble(precio.getTexto()) * 100)));
                }

                final Editable temp = s;
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!temp.toString().equals("")) {

                            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                            db.child(SORTEO).child(id).setValue(JavaUtil.comprobarLong(maxParticipantes.getTexto()));

                        }
                    }
                }, 2000);
            }
        });
    }
}
