package com.codevsolution.freemarketsapp.ui;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.codevsolution.base.models.Productos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.Timer;
import java.util.TimerTask;

public abstract class AltaProductosSorteos extends FragmentMasterDetailNoSQLFormProductosFirebaseRatingWeb
        implements Interactor.ConstantesPry {

    private String ganador;
    protected boolean sorteo;
    protected EditMaterial maxParticipantes;
    protected Button volverSortear;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        View viewSOR = inflater.inflate(R.layout.layout_sorteo, container, false);
        if (viewSOR.getParent() != null) {
            ((ViewGroup) viewSOR.getParent()).removeView(viewSOR); // <- fix
        }
        frdetalleExtraspost.addView(viewSOR);
    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        if (nn(bundle)) {
            sorteo = getBooleanBundle(SORTEO, false);
        }

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

        maxParticipantes = (EditMaterial) ctrl(R.id.etmaxpartformprodprov);
        volverSortear = (Button) ctrl(R.id.btn_volver_sorteo_prod);
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
        gone(maxParticipantes);

        if (id != null) {
            comprobarGanador();
        }

        visible(maxParticipantes);
        maxParticipantes.setHint(getString(R.string.max_participantes_sorteo) + " " +
                Math.round(JavaUtil.comprobarDouble(precio.getTexto()) * 100));

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query querySorteo = db.child(SORTEO).child(id);
        querySorteo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshotSorteo) {

                maxParticipantes.setText(String.valueOf(dataSnapshotSorteo.getValue(Long.class)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onContarSuscritos(int suscriptores) {
        super.onContarSuscritos(suscriptores);

        System.out.println("suscritos " + suscriptores);

        if (suscriptores > 0) {
            chActivo.setEnabled(false);
        }
    }

    private void comprobarGanador() {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query querydb = db.child(GANADORSORTEO).child(id);
        querydb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    ganador = dataSnapshot.getValue(String.class);
                    if (ganador != null && !ganador.equals(NULL)) {
                        volverSortear.setText(getString(R.string.sorteo_finalizado));
                        visible(volverSortear);
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

        volverSortear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.child(GANADORSORTEO).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        gone(volverSortear);
                    }
                });

            }
        });
    }
}
