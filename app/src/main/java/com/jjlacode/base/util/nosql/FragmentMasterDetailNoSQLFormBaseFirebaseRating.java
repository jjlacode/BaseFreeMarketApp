package com.jjlacode.base.util.nosql;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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

public abstract class FragmentMasterDetailNoSQLFormBaseFirebaseRating extends FragmentMasterDetailNoSQLFormBaseFirebase {

    private Object objeto;
    private String stemp = "";
    private ImageButton votar;
    private ImageButton verVoto;
    private EditMaterial voto;
    private EditMaterial comentario;
    private LinearLayout lyvoto;
    private RatingBar ratingBar;
    private RatingBar ratingBarUser;
    private float rating, nVotos, st1, st2, st3, st4, st5;
    private TextView tvst1, tvst2, tvst3, tvst4, tvst5, totVotos;
    private RecyclerView rvcoment;
    private RelativeLayout rlcoment;
    private ImageButton verComents;
    private LinearLayout lystars;
    private ArrayList<Rating> listaVotos;
    private float votoUser;
    private String tipoRating;
    private String idRating;
    private String keyVoto;
    private ArrayList<Rating> listaComents;
    private TextView ultimoVoto;


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

    }

    protected void selector() {

        maestroDetalle();

        if (esDetalle) {
            setDatos();
            idRating = firebaseFormBase.getIdchatBase();
            tipoRating = setTipo();
            recuperarVotos(ratingBar, tipoRating, idRating);
            recuperarComentarios(tipoRating, idRating);
            recuperarVotoUsuario(ratingBarUser, contexto, tipoRating, idRating);
            gone(lyvoto);
        }

        if (subTitulo == null) {
            activityBase.toolbar.setSubtitle(Interactor.setNamefdef());
        }

        activityBase.fab.hide();
        acciones();

    }

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
                String nombre = firebaseFormBase.getNombreBase();
                enviarVoto(contexto, keyVoto, nombre, tipoRating, idRating, votoUser, comentario.getTexto());
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

    public void enviarVoto(Context contexto, String key, String nombre, String tipo, String id, float valor, String comentario) {

        String idUser = CRUDutil.getSharePreference(contexto, PREFERENCIAS, USERID, NULL);
        Rating rat = new Rating(valor, tipo, id, idUser, nombre, comentario, TimeDateUtil.ahora());
        if (key == null) {
            FirebaseDatabase.getInstance().getReference().child("rating").push().setValue(rat, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    recuperarVotos(ratingBar, tipoRating, idRating);
                }
            });
        } else {
            FirebaseDatabase.getInstance().getReference().child("rating").child(key).setValue(rat, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    recuperarVotos(ratingBar, tipoRating, idRating);
                }
            });

        }

    }

    protected void recuperarVotos(final RatingBar ratingBar, final String tipo, final String id) {

        ratingBar.setRating(0.0f);
        Drawable progressDrawable = ratingBar.getProgressDrawable();
        if (progressDrawable != null) {
            DrawableCompat.setTint(progressDrawable,
                    contexto.getResources().getColor(R.color.color_star_defecto));
        }

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("rating");
        Query query = db.orderByChild("idRating").equalTo(id);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listaVotos = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {

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

                    if (rat.getTipo().equals(tipo)) {

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
                    }
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

        query.addValueEventListener(valueEventListener);

    }

    public void recuperarVotoUsuario(final RatingBar ratingBarUser, final Context contexto, final String tipo, final String id) {

        final String idUser = CRUDutil.getSharePreference(contexto, PREFERENCIAS, USERID, NULL);
        System.out.println("idUser = " + idUser);
        votoUser = 0.0f;
        comentario.setText("");
        keyVoto = null;
        ratingBarUser.setRating(votoUser);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("rating");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    Rating rat = child.getValue(Rating.class);
                    if (rat.getIdUser().equals(idUser) && rat.getTipo().equals(tipo) &&
                            rat.getIdRating().equals(id)) {
                        votoUser += rat.getValor();
                        comentario.setText(rat.getComentario());
                        keyVoto = child.getKey();
                        break;
                    }

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

    protected void recuperarComentarios(final String tipo, final String id) {


        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("rating");
        Query query = db.orderByChild("idRating").equalTo(id);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listaComents = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    if (child.getValue(Rating.class).getTipo().equals(tipo)) {

                        listaComents.add(child.getValue(Rating.class));

                    }

                }

                RVAdapter adapter = new RVAdapter(new ViewHolderRVComents(view), listaComents, R.layout.item_coments);
                rvcoment.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        query.addValueEventListener(valueEventListener);

    }


    protected void onClickRV(View v) {

        setOnClickRV(lista.get(rv.getChildAdapterPosition(v)));
        posicion = rv.getChildAdapterPosition(v);
    }

    public class ViewHolderRVComents extends BaseViewHolder implements TipoViewHolder {

        TextView comentario;
        TextView nombreComent;
        TextView fechaComent;
        RatingBar ratingBarComent;

        public ViewHolderRVComents(View itemView) {
            super(itemView);

            comentario = itemView.findViewById(R.id.tvcoments);
            nombreComent = itemView.findViewById(R.id.tvnombrecoments);
            fechaComent = itemView.findViewById(R.id.tvfechacoments);
            ratingBarComent = itemView.findViewById(R.id.ratingBarcoment);
        }

        @Override
        public void bind(ArrayList<?> lista, int position) {
            super.bind(lista, position);

            Rating rating = (Rating) lista.get(position);

            comentario.setText(rating.getComentario());
            nombreComent.setText(rating.getNombreUser());
            fechaComent.setText(TimeDateUtil.getDateString(rating.getFecha()));
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
}
