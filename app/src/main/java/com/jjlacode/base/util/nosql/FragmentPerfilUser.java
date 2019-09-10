package com.jjlacode.base.util.nosql;

import android.view.View;
import android.widget.ImageButton;
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
import com.jjlacode.base.util.android.AndroidUtil;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.base.util.android.controls.ImagenLayout;
import com.jjlacode.base.util.models.FirebaseFormBase;
import com.jjlacode.freelanceproject.R;


public class FragmentPerfilUser extends FragmentNoSQL {

    private EditMaterial nombre;
    private EditMaterial dirección;
    private EditMaterial telefono;
    private EditMaterial descripcion;
    private EditMaterial email;
    private EditMaterial web;
    private EditMaterial claves;
    private boolean pagado;
    private DatabaseReference dbFirebase;
    private String idUser;
    private FirebaseFormBase firebaseFormBase;
    private String tipo;

    @Override
    protected void setLayout() {
        layout = R.layout.fragment_perfil_user;
    }

    @Override
    protected void setLayoutExtra() {
        super.setLayoutExtra();

        layoutPie = R.layout.btn_sdb;
    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        tipo = bundle.getString(TIPO);
        activityBase.toolbar.setTitle("Perfil " + tipo);


    }

    @Override
    protected void setInicio() {

        nombre = (EditMaterial) ctrl(R.id.etnombrepubli);
        descripcion = (EditMaterial) ctrl(R.id.etdescripcionpubli);
        dirección = (EditMaterial) ctrl(R.id.etdireccionpubli);
        telefono = (EditMaterial) ctrl(R.id.ettelefonopubli);
        email = (EditMaterial) ctrl(R.id.etemailpubli);
        web = (EditMaterial) ctrl(R.id.etwebpubli);
        claves = (EditMaterial) ctrl(R.id.etclavespubli);
        imagen = (ImagenLayout) ctrl(R.id.imgpubli);
        btnback = (ImageButton) ctrl(R.id.btn_back);
        btndelete = (ImageButton) ctrl(R.id.btn_del);
        btnsave = (ImageButton) ctrl(R.id.btn_save);
        pagado = true;


    }

    protected void acciones() {

        super.acciones();


        idUser = AndroidUtil.getSharePreference(contexto, USERID, USERID, NULL);

        System.out.println("idUser = " + idUser);

        if (pagado) {
            web.setActivo(true);
        } else {
            web.setActivo(false);
            web.setText("");
        }

        activityBase.fabInicio.show();

        nombre.setMaxLeng(30);
        dirección.setMaxLeng(100);
        descripcion.setMaxLeng(200);
        telefono.setMaxLeng(15);
        email.setMaxLeng(30);
        web.setMaxLeng(50);
        claves.setMaxLeng(200);

        if (!idUser.equals(NULL)) {

            dbFirebase = FirebaseDatabase.getInstance().getReference().child(tipo).child(idUser);

            ValueEventListener eventListenerProd = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    firebaseFormBase = dataSnapshot.getValue(FirebaseFormBase.class);

                    if (firebaseFormBase != null) {
                        nombre.setText(firebaseFormBase.getNombreBase());
                        descripcion.setText(firebaseFormBase.getDescripcionBase());
                        dirección.setText(firebaseFormBase.getDireccionBase());
                        email.setText(firebaseFormBase.getEmailBase());
                        telefono.setText(firebaseFormBase.getTelefonoBase());
                        web.setText(firebaseFormBase.getWebBase());
                        claves.setText(firebaseFormBase.getClavesBase());
                        if (!idUser.equals("")) {
                            //setImagenFireStore(contexto, idUser + setTipo(), imagen);
                            imagen.setImageFirestore(idUser + tipo);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            dbFirebase.addValueEventListener(eventListenerProd);

        }

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                firebaseFormBase = new FirebaseFormBase();
                firebaseFormBase.setNombreBase(nombre.getText().toString());
                firebaseFormBase.setDescripcionBase(descripcion.getText().toString());
                firebaseFormBase.setDireccionBase(dirección.getText().toString());
                firebaseFormBase.setEmailBase(email.getText().toString());
                firebaseFormBase.setTelefonoBase(telefono.getText().toString());
                firebaseFormBase.setWebBase(web.getText().toString());
                firebaseFormBase.setClavesBase(claves.getText().toString());
                firebaseFormBase.setTipoBase(tipo);
                firebaseFormBase.setIdchatBase(idUser);

                dbFirebase = FirebaseDatabase.getInstance().getReference();
                dbFirebase.child(tipo).child(idUser).setValue(firebaseFormBase, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                    }
                });
                dbFirebase.child(PERFILES).child(idUser).child(tipo).setValue(true);

            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbFirebase = FirebaseDatabase.getInstance().getReference();
                dbFirebase.child(PERFILES).child(idUser).child(tipo).setValue(false);
                dbFirebase.child(tipo).child(idUser).removeValue();
                vaciarControles();

            }
        });
    }


    @Override
    protected void guardarImagen() {
        super.guardarImagen();

        if (nn(path)) {


            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child(idUser + tipo);
            UploadTask uploadTask = storageRef.putStream(imagen.getInputStreamAuto(path));

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(contexto, "Imagen subida OK", Toast.LENGTH_SHORT).show();
                    if (idUser != null && !idUser.equals("") && !idUser.equals(NULL)) {

                        //setImagenFireStore(contexto, idUser, imagen);
                        imagen.setImageFirestore(idUser + tipo);
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
