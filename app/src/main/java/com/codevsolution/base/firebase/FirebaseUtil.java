package com.codevsolution.base.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseUtil {

    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    public FirebaseUtil() {
    }

    public DatabaseReference getDatabase() {

        return db;
    }

    public void getValue(String[] ruta, String id, final String clase, final OnGetValue onGetValueListener) {

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
                        try {
                            onGetValueListener.onGetValue(dataSnapshot.getValue((Class.forName(clase)).getClass()));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

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

            final ArrayList<Class<?>> listaValores = new ArrayList<>();

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        try {
                            listaValores.add(child.getValue((Class.forName(clase)).getClass()));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
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

    public void setValue(String[] ruta, String id, boolean valor, final OnSetValue onSetValueListener) {

        Task<Void> query = null;

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

        final String finalId = id;
        query.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (onSetValueListener != null) {
                    if (task.isSuccessful()) {
                        onSetValueListener.onSetValueOk(finalId);
                    } else {
                        onSetValueListener.onSetValueFail(finalId);
                    }
                }
            }
        });
    }

    public void setValue(String[] ruta, String id, Object valor, final OnSetValue onSetValueListener) {

        Task<Void> query = null;

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

        final String finalId = id;
        query.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (onSetValueListener != null) {
                    if (task.isSuccessful()) {
                        onSetValueListener.onSetValueOk(finalId);
                    } else {
                        onSetValueListener.onSetValueFail(finalId);
                    }
                }
            }
        });
    }

    public void setValue(String[] ruta, String id, String valor, final OnSetValue onSetValueListener) {

        Task<Void> query = null;

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

        final String finalId = id;
        query.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (onSetValueListener != null) {
                    if (task.isSuccessful()) {
                        onSetValueListener.onSetValueOk(finalId);
                    } else {
                        onSetValueListener.onSetValueFail(finalId);
                    }
                }
            }
        });
    }

    public void setValue(String[] ruta, String id, double valor, final OnSetValue onSetValueListener) {

        Task<Void> query = null;

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

        final String finalId = id;
        query.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (onSetValueListener != null) {
                    if (task.isSuccessful()) {
                        onSetValueListener.onSetValueOk(finalId);
                    } else {
                        onSetValueListener.onSetValueFail(finalId);
                    }
                }
            }
        });
    }

    public void setValue(String[] ruta, String id, int valor, final OnSetValue onSetValueListener) {

        Task<Void> query = null;

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

        final String finalId = id;
        query.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (onSetValueListener != null) {
                    if (task.isSuccessful()) {
                        onSetValueListener.onSetValueOk(finalId);
                    } else {
                        onSetValueListener.onSetValueFail(finalId);
                    }
                }
            }
        });
    }

    public void setValue(String[] ruta, String id, long valor, final OnSetValue onSetValueListener) {

        Task<Void> query = null;

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

        final String finalId = id;
        query.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (onSetValueListener != null) {
                    if (task.isSuccessful()) {
                        onSetValueListener.onSetValueOk(finalId);
                    } else {
                        onSetValueListener.onSetValueFail(finalId);
                    }
                }
            }
        });
    }

    public interface OnGetValue {

        void onGetValue(Class<?> objeto);

        void onGetValue(ArrayList<Class<?>> listaObjetos);
    }

    public interface OnSetValue {

        void onSetValueOk(String key);

        void onSetValueFail(String key);
    }

}
