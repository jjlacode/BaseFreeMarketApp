package com.jjlacode.freelanceproject.ui;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.model.Freelance;
import com.jjlacode.freelanceproject.util.android.controls.EditMaterial;
import com.jjlacode.freelanceproject.util.crud.CRUDutil;
import com.jjlacode.freelanceproject.util.nosql.FragmentNoSQL;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.jjlacode.freelanceproject.CommonPry.Constantes.FREELANCE;
import static com.jjlacode.freelanceproject.CommonPry.Constantes.IDCHATF;
import static com.jjlacode.freelanceproject.CommonPry.Constantes.IDFREELANCE;
import static com.jjlacode.freelanceproject.CommonPry.Constantes.NOMBRECHAT;

public class FragmentPublicidad extends FragmentNoSQL {

    private EditMaterial nombre;
    private EditMaterial dirección;
    private EditMaterial telefono;
    private EditMaterial descripcion;
    private EditMaterial email;
    private EditMaterial web;
    private EditMaterial claves;
    private ImageButton guardar;
    private boolean pagado;
    private DatabaseReference dbFreelance;
    private String idFreelance;
    private Freelance freelance;

    @Override
    protected void setLayout() {
        layout = R.layout.fragment_publicidad;
    }

    @Override
    protected void setInicio() {

        nombre = (EditMaterial) ctrl(R.id.etnombrefreelance);
        descripcion = (EditMaterial) ctrl(R.id.etdescripcionfreelance);
        dirección = (EditMaterial) ctrl(R.id.etdireccionfreelance);
        telefono = (EditMaterial) ctrl(R.id.ettelefonofreelance);
        email = (EditMaterial) ctrl(R.id.etemailfreelance);
        web = (EditMaterial) ctrl(R.id.etwebfreelance);
        claves = (EditMaterial) ctrl(R.id.etclavesfreelance);
        guardar = (ImageButton) ctrl(R.id.btn_save_publicidad);
        imagen = (ImageView) ctrl(R.id.imgfreelance);
        pagado = true;

        idFreelance = CRUDutil.getSharePreference(contexto, PREFERENCIAS, IDFREELANCE, NULL);

        System.out.println("idFreelance = " + idFreelance);

        if (pagado) {
            web.setActivo(true);
        } else {
            web.setActivo(false);
            web.setText("");
        }

        nombre.setMaxLeng(30);
        dirección.setMaxLeng(100);
        descripcion.setMaxLeng(200);
        telefono.setMaxLeng(15);
        email.setMaxLeng(30);
        web.setMaxLeng(50);
        claves.setMaxLeng(200);

        if (!idFreelance.equals(NULL)) {

            dbFreelance = FirebaseDatabase.getInstance().getReference().child(FREELANCE).child(idFreelance);

            ValueEventListener eventListenerProd = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    freelance = dataSnapshot.getValue(Freelance.class);

                    if (freelance != null) {
                        nombre.setText(freelance.getNombref());
                        descripcion.setText(freelance.getDescripcionf());
                        dirección.setText(freelance.getDireccionf());
                        email.setText(freelance.getEmailf());
                        telefono.setText(freelance.getTelefonof());
                        web.setText(freelance.getWebf());
                        claves.setText(freelance.getClavesf());
                        if (!idFreelance.equals("")) {
                            setImagenFireStore(contexto, idFreelance, imagen);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            dbFreelance.addValueEventListener(eventListenerProd);

        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CRUDutil.setSharePreference(contexto, PREFERENCIAS, NOMBRECHAT, nombre.getTexto());

                if (!idFreelance.equals(NULL)) {


                    dbFreelance = FirebaseDatabase.getInstance().getReference().child(FREELANCE).child(idFreelance);

                    Map<String, Object> actualizacion = new HashMap<>();
                    actualizacion.put("/nombref", nombre.getTexto());
                    actualizacion.put("/descripcionf", descripcion.getTexto());
                    actualizacion.put("/direccionf", dirección.getTexto());
                    actualizacion.put("/telefonof", telefono.getTexto());
                    actualizacion.put("/emailf", email.getTexto());
                    actualizacion.put("/webf", web.getTexto());
                    actualizacion.put("/clavesf", claves.getTexto());
                    actualizacion.put("/idchatf", idFreelance);

                    dbFreelance.updateChildren(actualizacion, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            if (databaseError == null)
                                Log.d(TAG, "Operación update idfreelance OK");
                            else
                                Log.e(TAG, "Error: " + databaseError.getMessage());
                        }
                    });


                } else {

                    freelance = new Freelance();
                    freelance.setNombref(nombre.getText().toString());
                    freelance.setDescripcionf(descripcion.getText().toString());
                    freelance.setDireccionf(dirección.getText().toString());
                    freelance.setEmailf(email.getText().toString());
                    freelance.setTelefonof(telefono.getText().toString());
                    freelance.setWebf(web.getText().toString());
                    freelance.setClavesf(claves.getText().toString());

                    dbFreelance = FirebaseDatabase.getInstance().getReference().child(FREELANCE);
                    idFreelance = dbFreelance.push().getKey();
                    dbFreelance.child(idFreelance).setValue(freelance, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            dbFreelance.child(idFreelance).child(IDCHATF).setValue(idFreelance);
                            CRUDutil.setSharePreference(contexto, PREFERENCIAS, IDFREELANCE, idFreelance);
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void guardarImagen() {
        super.guardarImagen();

        if (nn(path)) {

            if (idFreelance == NULL || idFreelance.equals("")) {
                dbFreelance = FirebaseDatabase.getInstance().getReference().child(FREELANCE);
                idFreelance = dbFreelance.push().getKey();
                dbFreelance.child(idFreelance).setValue(freelance, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                        dbFreelance.child(idFreelance).child(IDCHATF).setValue(idFreelance);
                        CRUDutil.setSharePreference(contexto, PREFERENCIAS, IDFREELANCE, idFreelance);
                    }
                });
            }
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child(idFreelance);
            UploadTask uploadTask = storageRef.putFile(Uri.fromFile(new File(path)));

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(contexto, "Imagen subida OK", Toast.LENGTH_SHORT).show();
                    if (idFreelance != NULL && !idFreelance.equals("")) {

                        setImagenFireStore(contexto, idFreelance, imagen);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(contexto, "Fallo al subir imagen", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
