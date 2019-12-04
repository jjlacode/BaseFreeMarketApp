package com.codevsolution.base.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseUtil {

    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    protected GenericTypeIndicator<ArrayList<String>> tipoArrayListsString = new GenericTypeIndicator<ArrayList<String>>() {
    };

    public FirebaseUtil() {
    }

    public DatabaseReference getDatabase() {

        return db;
    }

    public void getValue(String[] ruta, String id, final Class<?> clase, final OnGetValue onGetValueListener) {

        if (id != null) {

            DatabaseReference query = null;
            switch (ruta.length) {
                case 0:
                    query = db.child(id);
                    break;
                case 1:
                    query = db.child(ruta[0]).child(id);
                    break;
                case 2:
                    query = db.child(ruta[0]).child(ruta[1]).child(id);
                    break;
                case 3:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(id);
                    break;
                case 4:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(id);
                    break;
                case 5:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]).child(id);
                    break;

            }


            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (onGetValueListener != null) {
                        onGetValueListener.onGetValue(dataSnapshot.getValue(clase));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {

            DatabaseReference query = null;

            switch (ruta.length) {
                case 0:
                    query = db;
                    break;
                case 1:
                    query = db.child(ruta[0]);
                    break;
                case 2:
                    query = db.child(ruta[0]).child(ruta[1]);
                    break;
                case 3:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]);
                    break;
                case 4:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]);
                    break;
                case 5:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]);
                    break;

            }

            final ArrayList<Object> listaValores = new ArrayList<>();

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        listaValores.add(child.getValue(clase));
                    }
                    if (onGetValueListener != null) {
                        onGetValueListener.onGetValue(listaValores);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    public void getValue(String[] ruta, String id, final OnGetValue onGetValueListener) {

        if (id != null) {

            DatabaseReference query = null;
            switch (ruta.length) {
                case 0:
                    query = db.child(id);
                    break;
                case 1:
                    query = db.child(ruta[0]).child(id);
                    break;
                case 2:
                    query = db.child(ruta[0]).child(ruta[1]).child(id);
                    break;
                case 3:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(id);
                    break;
                case 4:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(id);
                    break;
                case 5:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]).child(id);
                    break;

            }


            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (onGetValueListener != null) {
                        onGetValueListener.onGetValue(dataSnapshot.getValue(tipoArrayListsString));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {

            DatabaseReference query = null;

            switch (ruta.length) {
                case 0:
                    query = db;
                    break;
                case 1:
                    query = db.child(ruta[0]);
                    break;
                case 2:
                    query = db.child(ruta[0]).child(ruta[1]);
                    break;
                case 3:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]);
                    break;
                case 4:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]);
                    break;
                case 5:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]);
                    break;

            }

            final ArrayList<Object> listaValores = new ArrayList<>();

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        listaValores.add(child.getValue(tipoArrayListsString));
                    }
                    if (onGetValueListener != null) {
                        onGetValueListener.onGetValue(listaValores);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    public String setValue(String[] ruta, String id, boolean valor, final OnSetValue onSetValueListener) {

        Task<Void> query = null;
        boolean nuevo = false;
        if (id == null) {
            nuevo = true;
        }

        switch (ruta.length) {
            case 0:
                if (id == null) {
                    id = db.push().getKey();
                }
                query = db.child(id).setValue(valor);
                break;
            case 1:
                if (id == null) {
                    id = db.child(ruta[0]).push().getKey();
                }
                query = db.child(ruta[0]).child(id).setValue(valor);
                break;
            case 2:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(id).setValue(valor);
                break;
            case 3:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(id).setValue(valor);
                break;
            case 4:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(id).setValue(valor);
                break;
            case 5:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]).child(id).setValue(valor);
                break;

        }

        onCompleteSetValue(query, id, nuevo, onSetValueListener);

        return id;

    }

    public String setValue(String[] ruta, String id, Object valor, final OnSetValue onSetValueListener) {

        Task<Void> query = null;
        boolean nuevo = false;
        if (id == null) {
            nuevo = true;
        }

        switch (ruta.length) {
            case 0:
                if (id == null) {
                    id = db.push().getKey();
                }
                query = db.child(id).setValue(valor);
                break;
            case 1:
                if (id == null) {
                    id = db.child(ruta[0]).push().getKey();
                }
                query = db.child(ruta[0]).child(id).setValue(valor);
                break;
            case 2:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(id).setValue(valor);
                break;
            case 3:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(id).setValue(valor);
                break;
            case 4:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(id).setValue(valor);
                break;
            case 5:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]).child(id).setValue(valor);
                break;

        }

        onCompleteSetValue(query, id, nuevo, onSetValueListener);

        return id;

    }

    public String setValue(String[] ruta, String id, String valor, final OnSetValue onSetValueListener) {

        Task<Void> query = null;
        boolean nuevo = false;
        if (id == null) {
            nuevo = true;
        }

        switch (ruta.length) {
            case 0:
                if (id == null) {
                    id = db.push().getKey();
                }
                query = db.child(id).setValue(valor);
                break;
            case 1:
                if (id == null) {
                    id = db.child(ruta[0]).push().getKey();
                }
                query = db.child(ruta[0]).child(id).setValue(valor);
                break;
            case 2:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(id).setValue(valor);
                break;
            case 3:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(id).setValue(valor);
                break;
            case 4:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(id).setValue(valor);
                break;
            case 5:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]).child(id).setValue(valor);
                break;

        }

        onCompleteSetValue(query, id, nuevo, onSetValueListener);

        return id;

    }

    public String setValue(String[] ruta, String id, double valor, final OnSetValue onSetValueListener) {

        Task<Void> query = null;
        boolean nuevo = false;
        if (id == null) {
            nuevo = true;
        }

        switch (ruta.length) {
            case 0:
                if (id == null) {
                    id = db.push().getKey();
                }
                query = db.child(id).setValue(valor);
                break;
            case 1:
                if (id == null) {
                    id = db.child(ruta[0]).push().getKey();
                }
                query = db.child(ruta[0]).child(id).setValue(valor);
                break;
            case 2:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(id).setValue(valor);
                break;
            case 3:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(id).setValue(valor);
                break;
            case 4:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(id).setValue(valor);
                break;
            case 5:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]).child(id).setValue(valor);
                break;

        }

        onCompleteSetValue(query, id, nuevo, onSetValueListener);

        return id;

    }

    public String setValue(String[] ruta, String id, int valor, final OnSetValue onSetValueListener) {

        Task<Void> query = null;
        boolean nuevo = false;
        if (id == null) {
            nuevo = true;
        }

        switch (ruta.length) {
            case 0:
                if (id == null) {
                    id = db.push().getKey();
                }
                query = db.child(id).setValue(valor);
                break;
            case 1:
                if (id == null) {
                    id = db.child(ruta[0]).push().getKey();
                }
                query = db.child(ruta[0]).child(id).setValue(valor);
                break;
            case 2:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(id).setValue(valor);
                break;
            case 3:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(id).setValue(valor);
                break;
            case 4:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(id).setValue(valor);
                break;
            case 5:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]).child(id).setValue(valor);
                break;

        }

        onCompleteSetValue(query, id, nuevo, onSetValueListener);

        return id;

    }

    public String setValue(String[] ruta, String id, long valor, final OnSetValue onSetValueListener) {

        Task<Void> query = null;
        boolean nuevo = false;
        if (id == null) {
            nuevo = true;
        }

        switch (ruta.length) {
            case 0:
                if (id == null) {
                    id = db.push().getKey();
                }
                query = db.child(id).setValue(valor);
                break;
            case 1:
                if (id == null) {
                    id = db.child(ruta[0]).push().getKey();
                }
                query = db.child(ruta[0]).child(id).setValue(valor);
                break;
            case 2:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(id).setValue(valor);
                break;
            case 3:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(id).setValue(valor);
                break;
            case 4:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(id).setValue(valor);
                break;
            case 5:
                if (id == null) {
                    id = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]).push().getKey();
                }
                query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]).child(id).setValue(valor);
                break;

        }

        onCompleteSetValue(query, id, nuevo, onSetValueListener);

        return id;

    }

    private void onCompleteSetValue(Task<Void> query, String id, boolean nuevo, final OnSetValue onSetValueListener) {

        final String finalId = id;
        boolean finalNuevo = nuevo;
        query.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (onSetValueListener != null) {
                    if (task.isSuccessful()) {
                        if (finalNuevo) {
                            onSetValueListener.onCreateOk(finalId);
                            System.out.println("Prod Fire creado con exito");
                        } else {
                            System.out.println("Prod Fire actualizado con exito");
                            onSetValueListener.onSetValueOk(finalId);
                        }
                    } else {
                        System.out.println("error creando Prod Fire");
                        onSetValueListener.onSetValueFail(finalId);
                    }
                }
            }
        });
    }

    public void removeValue(String[] ruta, String id, final OnRemoveValue onRemoveValueListener) {

        if (id != null) {

            DatabaseReference query = null;
            switch (ruta.length) {
                case 0:
                    query = db.child(id);
                    break;
                case 1:
                    query = db.child(ruta[0]).child(id);
                    break;
                case 2:
                    query = db.child(ruta[0]).child(ruta[1]).child(id);
                    break;
                case 3:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(id);
                    break;
                case 4:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(id);
                    break;
                case 5:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]).child(id);
                    break;

            }


            query.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (onRemoveValueListener != null) {
                        if (task.isSuccessful()) {
                            onRemoveValueListener.onRemoveValueOk();
                        } else {
                            onRemoveValueListener.onRemoveValueFail();
                        }
                    }
                }
            });

        } else {

            DatabaseReference query = null;

            switch (ruta.length) {
                case 0:
                    query = db;
                    break;
                case 1:
                    query = db.child(ruta[0]);
                    break;
                case 2:
                    query = db.child(ruta[0]).child(ruta[1]);
                    break;
                case 3:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]);
                    break;
                case 4:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]);
                    break;
                case 5:
                    query = db.child(ruta[0]).child(ruta[1]).child(ruta[2]).child(ruta[3]).child(ruta[4]);
                    break;

            }

        }
    }

    public interface OnRemoveValue {

        void onRemoveValueOk();

        void onRemoveValueFail();
    }

    public interface OnGetValue {

        void onGetValue(Object objeto);

        void onGetValue(ArrayList listaObjetos);
    }

    public interface OnSetValue {

        void onCreateOk(String key);

        void onSetValueOk(String key);

        void onSetValueFail(String key);
    }

}
