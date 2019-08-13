package com.jjlacode.um.base.ui;

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
import com.jjlacode.base.util.Models.FirebaseFormBase;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.nosql.FragmentNoSQL;
import com.jjlacode.freelanceproject.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class FragmentPublicidad extends FragmentNoSQL {

    private EditMaterial nombre;
    private EditMaterial dirección;
    private EditMaterial telefono;
    private EditMaterial descripcion;
    private EditMaterial email;
    private EditMaterial web;
    private EditMaterial claves;
    private ImageButton guardar;
    private boolean pagado;
    private DatabaseReference dbFirebase;
    private String idUser;
    private FirebaseFormBase firebaseFormBase;

    @Override
    protected void setLayout() {
        layout = R.layout.fragment_publicidad;
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
        guardar = (ImageButton) ctrl(R.id.btn_save_publicidad);
        imagen = (ImageView) ctrl(R.id.imgpubli);
        pagado = setPagado();

        idUser = CRUDutil.getSharePreference(contexto, PREFERENCIAS, setTipoIdUser(), NULL);

        System.out.println("idUser = " + idUser);

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

        if (!idUser.equals(NULL)) {

            dbFirebase = FirebaseDatabase.getInstance().getReference().child(setTipo()).child(idUser);

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
                            setImagenFireStore(contexto, idUser + setTipo(), imagen);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            dbFirebase.addValueEventListener(eventListenerProd);

        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!idUser.equals(NULL)) {


                    dbFirebase = FirebaseDatabase.getInstance().getReference().child(setTipo()).child(idUser);

                    Map<String, Object> actualizacion = new HashMap<>();
                    actualizacion.put("/nombreBase", nombre.getTexto());
                    actualizacion.put("/descripcionBase", descripcion.getTexto());
                    actualizacion.put("/direccionBase", dirección.getTexto());
                    actualizacion.put("/telefonoBase", telefono.getTexto());
                    actualizacion.put("/emailBase", email.getTexto());
                    actualizacion.put("/webBase", web.getTexto());
                    actualizacion.put("/clavesBase", claves.getTexto());
                    actualizacion.put("/idchatBase", idUser);
                    actualizacion.put("/tipoBase", setTipo());

                    dbFirebase.updateChildren(actualizacion, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            if (databaseError == null)
                                Log.d(TAG, "Operación update idfreelance OK");
                            else
                                Log.e(TAG, "Error: " + databaseError.getMessage());
                        }
                    });


                } else {

                    firebaseFormBase = new FirebaseFormBase();
                    firebaseFormBase.setNombreBase(nombre.getText().toString());
                    firebaseFormBase.setDescripcionBase(descripcion.getText().toString());
                    firebaseFormBase.setDireccionBase(dirección.getText().toString());
                    firebaseFormBase.setEmailBase(email.getText().toString());
                    firebaseFormBase.setTelefonoBase(telefono.getText().toString());
                    firebaseFormBase.setWebBase(web.getText().toString());
                    firebaseFormBase.setClavesBase(claves.getText().toString());
                    firebaseFormBase.setTipoBase(setTipo());

                    dbFirebase = FirebaseDatabase.getInstance().getReference().child(setTipo());
                    idUser = dbFirebase.push().getKey();
                    dbFirebase.child(idUser).setValue(firebaseFormBase, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            dbFirebase.child(idUser).child(IDCHATF).setValue(idUser);
                            CRUDutil.setSharePreference(contexto, PREFERENCIAS, setTipo(), idUser);
                        }
                    });
                }

            }
        });
    }

    protected abstract boolean setPagado();

    protected abstract String setTipoIdUser();

    protected abstract String setTipo();

    @Override
    protected void guardarImagen() {
        super.guardarImagen();

        if (nn(path)) {

            if (idUser == NULL || idUser.equals("")) {
                dbFirebase = FirebaseDatabase.getInstance().getReference().child(setTipo());
                idUser = dbFirebase.push().getKey();
                dbFirebase.child(idUser).setValue(firebaseFormBase, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                        dbFirebase.child(idUser).child(IDCHATF).setValue(idUser);
                        CRUDutil.setSharePreference(contexto, PREFERENCIAS, setTipoIdUser(), idUser);
                    }
                });
            }
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child(idUser + setTipo());
            UploadTask uploadTask = storageRef.putFile(Uri.fromFile(new File(path)));

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(contexto, "Imagen subida OK", Toast.LENGTH_SHORT).show();
                    if (idUser != NULL && !idUser.equals("")) {

                        setImagenFireStore(contexto, idUser, imagen);
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
