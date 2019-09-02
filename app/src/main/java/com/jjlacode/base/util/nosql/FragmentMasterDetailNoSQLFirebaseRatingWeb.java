package com.jjlacode.base.util.nosql;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

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
import com.jjlacode.base.util.JavaUtil;
import com.jjlacode.base.util.Models.Rating;
import com.jjlacode.base.util.adapter.BaseViewHolder;
import com.jjlacode.base.util.adapter.RVAdapter;
import com.jjlacode.base.util.adapter.TipoViewHolder;
import com.jjlacode.base.util.android.controls.EditMaterial;
import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.time.TimeDateUtil;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.logica.Interactor;

import java.util.ArrayList;

public abstract class FragmentMasterDetailNoSQLFirebaseRatingWeb extends FragmentMasterDetailNoSQL {

    private View viewWeb;
    protected WebView browser;
    protected ProgressBar progressBarWeb;
    protected NestedScrollView lyweb;
    protected String web;
    protected String stemp = "";
    protected ImageButton votar;
    protected ImageButton verVoto;
    private EditMaterial voto;
    private EditMaterial comentario;
    protected LinearLayout lyvoto;
    protected RatingBar ratingBar;
    protected RatingBar ratingBarUser;
    private float rating, nVotos, st1, st2, st3, st4, st5;
    private TextView tvst1, tvst2, tvst3, tvst4, tvst5, totVotos;
    private RecyclerView rvcoment;
    private RelativeLayout rlcoment;
    private ImageButton verComents;
    private LinearLayout lystars;
    private ArrayList<Rating> listaVotos;
    private float votoUser;
    protected String tipoRating;
    protected String idRating;
    private ArrayList<Rating> listaComents;
    private TextView ultimoVoto;
    private int posicion;
    protected String nombreVoto;
    protected String tipo;
    protected String tipoForm;
    private DatabaseReference db;


    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);


        View viewRB = inflater.inflate(R.layout.layout_rating, container, false);
        if (viewRB.getParent() != null) {
            ((ViewGroup) viewRB.getParent()).removeView(viewRB); // <- fix
        }
        frdetalleExtraspost.addView(viewRB);

        ratingBar = view.findViewById(R.id.ratingBarFreelance);
        ratingBarUser = view.findViewById(R.id.ratingBarUserFreelance);
        votar = (ImageButton) ctrl(R.id.btn_votarfreelance);
        verVoto = (ImageButton) ctrl(R.id.btn_vervotofreelance);
        voto = (EditMaterial) ctrl(R.id.etvotodetfreelance);
        comentario = (EditMaterial) ctrl(R.id.etcomentdetfreelance);
        lyvoto = view.findViewById(R.id.lyvoto);
        lystars = view.findViewById(R.id.lystars);
        tvst1 = view.findViewById(R.id.tvstar1);
        tvst2 = view.findViewById(R.id.tvstar2);
        tvst3 = view.findViewById(R.id.tvstar3);
        tvst4 = view.findViewById(R.id.tvstar4);
        tvst5 = view.findViewById(R.id.tvstar5);
        totVotos = view.findViewById(R.id.totalvotos);
        ultimoVoto = view.findViewById(R.id.ultimovoto);
        rvcoment = view.findViewById(R.id.rvcomentariosstar);
        rlcoment = view.findViewById(R.id.rlcomentariosstar);
        verComents = view.findViewById(R.id.btn_comentstar);

        gone(lyvoto);
        ratingBar.setIsIndicator(true);

        viewWeb = inflater.inflate(R.layout.layout_webview, container, false);
        if (viewWeb.getParent() != null) {
            ((ViewGroup) viewWeb.getParent()).removeView(viewWeb); // <- fix
        }
        frdetalleExtraspost.addView(viewWeb);

        browser = (WebView) view.findViewById(R.id.webBrowser);
        progressBarWeb = view.findViewById(R.id.progressBarWeb);
        lyweb = view.findViewById(R.id.lywebBrowser);
        tipoForm = setTipoForm();

    }

    @Override
    protected void cargarBundle() {
        super.cargarBundle();

        if (bundle != null) {
            tipo = bundle.getString(TIPO);
        }
    }

    protected void selector() {


        setNombreVoto();
        System.out.println("nombreVoto = " + nombreVoto);

        maestroDetalle();

        if (esDetalle) {
            setDatos();
            idRating = setIdRating();
            tipoRating = setTipoRating();
            recuperarVotos(ratingBar, tipoRating, idRating);
            recuperarComentarios(tipoRating, idRating);
            recuperarVotoUsuario(ratingBarUser, contexto, tipoRating, idRating);
            gone(lyvoto);
            onSetDatos();
        }

        if (subTitulo == null) {
            activityBase.toolbar.setSubtitle(Interactor.setNamefdef());
        }

        if (tipoForm.equals(NUEVO)) {
            activityBase.fabInicio.hide();
            activityBase.fabNuevo.show();

        } else {
            activityBase.fabNuevo.hide();
            activityBase.fabInicio.show();
        }
        acciones();

    }

    protected abstract String setTipoRating();

    protected abstract String setIdRating();

    protected abstract String setTipoForm();

    protected void acciones() {

        super.acciones();

        ratingBarUser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                if (b) {

                    int color = 0;
                    if (v > 3) {
                        color = R.color.Color_star_ok;
                    } else if (v > 1.5) {
                        color = R.color.Color_star_acept;
                    } else if (v > 0) {
                        color = R.color.Color_star_notok;
                    } else {
                        color = R.color.color_star_defecto;
                    }

                    ratingBarUser.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(contexto, color)));

                    voto.setText(String.valueOf((int) (v)));
                    votoUser = v;
                }
            }
        });

        votar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gone(lyvoto);
                enviarVoto(contexto, tipoRating, idRating, votoUser, comentario.getTexto());
            }
        });

        verVoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lyvoto.getVisibility() == View.VISIBLE) {
                    gone(lyvoto);
                } else {
                    visible(lyvoto);
                    recuperarVotoUsuario(ratingBarUser, contexto, tipoRating, idRating);
                }

            }
        });

        verComents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rlcoment.getVisibility() == View.VISIBLE) {
                    gone(rlcoment);
                } else {

                    recuperarComentarios(tipoRating, idRating);
                    visible(rlcoment);

                }
            }
        });

        setAcciones();

    }

    protected void setNombreVoto() {

        perfilUser = CRUDutil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
        idUser = CRUDutil.getSharePreference(contexto, USERID, USERID, NULL);

        if (!idUser.equals(NULL)) {

            DatabaseReference dbFirebase = FirebaseDatabase.getInstance().getReference()
                    .child(perfilUser).child(idUser).child("nombreBase");

            ValueEventListener eventListenerProd = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    nombreVoto = dataSnapshot.getValue(String.class);
                    System.out.println("nombreVoto = " + nombreVoto);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            dbFirebase.addValueEventListener(eventListenerProd);

        }

    }

    public void enviarVoto(Context contexto, final String tipo, final String id, float valor, String comentario) {

        perfilUser = CRUDutil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);
        idUser = CRUDutil.getSharePreference(contexto, USERID, USERID, NULL);

        if (nombreVoto != null) {

            Rating rat = new Rating(valor, perfilUser, id, idUser, nombreVoto, comentario, TimeDateUtil.ahora());
            FirebaseDatabase.getInstance().getReference().child("rating").child(tipo).child(id).child(idUser + perfilUser).setValue(rat, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    recuperarVotos(ratingBar, tipo, id);
                }
            });
        } else {
            Toast.makeText(contexto, "Debe tener un perfil de " + perfilUser + " para votar", Toast.LENGTH_SHORT).show();
        }

    }

    protected void recuperarVotos(final RatingBar ratingBar, final String tipo, final String id) {

        ratingBar.setRating(0.0f);
        Drawable progressDrawable = ratingBar.getProgressDrawable();
        if (progressDrawable != null) {
            DrawableCompat.setTint(progressDrawable,
                    contexto.getResources().getColor(R.color.color_star_defecto));
        }

        if (id != null && tipo != null) {
            db = FirebaseDatabase.getInstance().getReference().child("rating").child(tipo).child(id);


            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listaVotos = new ArrayList<>();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        System.out.println("child.getValue() = " + child.getValue());
                        listaVotos.add(child.getValue(Rating.class));

                    }

                    rating = 0.0f;
                    nVotos = 0.0f;
                    st1 = 0;
                    st2 = 0;
                    st3 = 0;
                    st4 = 0;
                    st5 = 0;
                    long ultimoVotoTemp = 0;


                    for (Rating rat : listaVotos) {

                        //System.out.println("rat tipo= " + rat.getTipo());
                        //System.out.println("tipo = " + tipo);
                        //if (rat.getTipo().equals(tipo)) {

                        float voto = rat.getValor();

                        System.out.println("voto = " + voto);

                        if (voto == 1) {
                            st1++;
                            nVotos++;
                        } else if (voto == 2) {
                            st2++;
                            nVotos++;
                        } else if (voto == 3) {
                            st3++;
                            nVotos++;
                        } else if (voto == 4) {
                            st4++;
                            nVotos++;
                        } else if (voto == 5) {
                            st5++;
                            nVotos++;
                        }

                        rating += voto;
                        //}
                        if (rat.getFecha() > ultimoVotoTemp) {
                            ultimoVotoTemp = rat.getFecha();
                        }
                    }

                    tvst5.setText(JavaUtil.getDecimales(st5, "0"));
                    tvst4.setText(JavaUtil.getDecimales(st4, "0"));
                    tvst3.setText(JavaUtil.getDecimales(st3, "0"));
                    tvst2.setText(JavaUtil.getDecimales(st2, "0"));
                    tvst1.setText(JavaUtil.getDecimales(st1, "0"));
                    totVotos.setText("Numero de valoraciones: " + JavaUtil.getDecimales(nVotos, "0"));
                    if (ultimoVotoTemp > 0) {
                        ultimoVoto.setText("Ultima valoración: " + TimeDateUtil.getDateString(ultimoVotoTemp));
                    } else {
                        ultimoVoto.setText("Sin valoraciones todavía");
                    }

                    if (nVotos > 0) {
                        rating /= nVotos;
                        ratingBar.setRating(rating);
                    } else {
                        ratingBar.setRating(0.0f);
                    }

                    int color = 0;
                    if (rating > 3) {
                        color = R.color.Color_star_ok;
                    } else if (rating > 1.5) {
                        color = R.color.Color_star_acept;
                    } else if (rating > 0) {
                        color = R.color.Color_star_notok;
                    } else {
                        color = R.color.color_star_defecto;
                    }

                    ratingBar.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(contexto, color)));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            db.addValueEventListener(valueEventListener);
        }

    }

    public void recuperarVotoUsuario(final RatingBar ratingBarUser, final Context contexto, final String tipo, final String id) {

        idUser = CRUDutil.getSharePreference(contexto, USERID, USERID, NULL);
        perfilUser = CRUDutil.getSharePreference(contexto, PREFERENCIAS, PERFILUSER, NULL);

        votoUser = 0.0f;
        comentario.setText("");
        ratingBarUser.setRating(votoUser);

        if (id != null && tipo != null) {
            db = FirebaseDatabase.getInstance().getReference().child("rating").child(tipo).child(id).child(idUser + perfilUser);


            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    Rating rat = dataSnapshot.getValue(Rating.class);

                    if (rat != null) {
                        votoUser = rat.getValor();
                        comentario.setText(rat.getComentario());
                    }

                    voto.setText(String.valueOf(votoUser));
                    ratingBarUser.setRating(votoUser);
                    int color = 0;
                    if (votoUser > 3) {
                        color = R.color.Color_star_ok;
                    } else if (votoUser > 1.5) {
                        color = R.color.Color_star_acept;
                    } else if (votoUser > 0) {
                        color = R.color.Color_star_notok;
                    } else {
                        color = R.color.color_star_defecto;
                    }

                    ratingBarUser.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(contexto, color)));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            db.addValueEventListener(valueEventListener);

        }
    }

    protected void recuperarComentarios(final String tipo, final String id) {


        if (id != null && tipo != null) {
            db = FirebaseDatabase.getInstance().getReference().child("rating").child(tipo).child(id);


            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listaComents = new ArrayList<>();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        listaComents.add(child.getValue(Rating.class));

                    }

                    RVAdapter adapter = new RVAdapter(new ViewHolderRVComents(view), listaComents, R.layout.item_coments);
                    rvcoment.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            db.addValueEventListener(valueEventListener);
        }

    }

    @Override
    protected void setOnLeftSwipeCuerpo() {
        super.setOnLeftSwipeCuerpo();

        if (posicion < lista.size() - 1) {
            posicion++;
            setOnClickRV(lista.get(posicion));
            System.out.println("posicion = " + posicion);
        }
    }

    @Override
    protected void setOnRigthSwipeCuerpo() {
        super.setOnRigthSwipeCuerpo();
        if (posicion > 0) {
            posicion--;
            setOnClickRV(lista.get(posicion));
            System.out.println("posicion = " + posicion);
        }
    }

    protected void onClickRV(View v) {

        setOnClickRV(lista.get(rv.getChildAdapterPosition(v)));
        posicion = rv.getChildAdapterPosition(v);
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

    public class ViewHolderRVComents extends BaseViewHolder implements TipoViewHolder {

        TextView comentario;
        TextView nombreComent;
        TextView fechaComent;
        TextView tipoComment;
        RatingBar ratingBarComent;

        public ViewHolderRVComents(View itemView) {
            super(itemView);

            comentario = itemView.findViewById(R.id.tvcoments);
            nombreComent = itemView.findViewById(R.id.tvnombrecoments);
            fechaComent = itemView.findViewById(R.id.tvfechacoments);
            ratingBarComent = itemView.findViewById(R.id.ratingBarcoment);
            tipoComment = itemView.findViewById(R.id.tvtipocoments);
        }

        @Override
        public void bind(ArrayList<?> lista, int position) {
            super.bind(lista, position);

            Rating rating = (Rating) lista.get(position);

            comentario.setText(rating.getComentario());
            nombreComent.setText(rating.getNombreUser());
            fechaComent.setText(TimeDateUtil.getDateString(rating.getFecha()));
            tipoComment.setText(rating.getTipo());
            float ratUser = rating.getValor();
            ratingBarComent.setRating(ratUser);

            int color = 0;
            if (ratUser > 3) {
                color = R.color.Color_star_ok;
            } else if (ratUser > 1.5) {
                color = R.color.Color_star_acept;
            } else if (ratUser > 0) {
                color = R.color.Color_star_notok;
            } else {
                color = R.color.color_star_defecto;
            }

            ratingBarComent.setProgressTintList(ColorStateList.valueOf(ContextCompat.getColor(contexto, color)));


        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVComents(view);
        }
    }

    protected void onSetDatos() {

        web = setWeb();

        if (web != null && JavaUtil.isValidURL(web)) {

            visible(lyweb);

            browser.getSettings().setJavaScriptEnabled(true);
            browser.getSettings().setBuiltInZoomControls(true);
            browser.getSettings().setDisplayZoomControls(false);

            browser.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            // Cargamos la web
            browser.loadUrl(web);
        } else {
            gone(lyweb);
        }
    }

    protected String setWeb() {
        return null;
    }
}
