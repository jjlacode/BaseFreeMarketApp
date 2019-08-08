package jjlacode.com.freelanceproject.util.nosql;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.CommonPry;
import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.model.Rating;
import jjlacode.com.freelanceproject.util.android.controls.EditMaterial;
import jjlacode.com.freelanceproject.util.crud.CRUDutil;

import static jjlacode.com.freelanceproject.CommonPry.Constantes.USERID;
import static jjlacode.com.freelanceproject.CommonPry.enviarVoto;

public abstract class FragmentMasterDetailNoSQLFirebaseRating extends FragmentMasterDetailNoSQL {

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
    private TextView tvst1, tvst2, tvst3, tvst4, tvst5;
    private RecyclerView rvcoment;
    private LinearLayout lystars;
    private ArrayList<Rating> listaVotos;
    private float votoUser;
    private String tipoRating;
    private String idRating;
    private String keyVoto;


    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        super.setOnCreateView(view, inflater, container);

        View viewRB = inflater.inflate(R.layout.layout_rating, container, false);
        if (viewRB.getParent() != null) {
            ((ViewGroup) viewRB.getParent()).removeView(viewRB); // <- fix
        }
        frdetalleExtras.addView(viewRB);

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
        rvcoment = view.findViewById(R.id.rvcomentariosstar);
        gone(lyvoto);
        ratingBar.setIsIndicator(true);

    }

    protected void selector() {

        maestroDetalle();

        if (esDetalle) {
            setDatos();
            onSetDatos();
            idRating = setIdRating();
            tipoRating = setTipoRating();
            gone(lyvoto);
            recuperarVotos(tipoRating, idRating);
        }

        if (subTitulo == null) {
            activityBase.toolbar.setSubtitle(CommonPry.setNamefdef());
        }

        acciones();

    }

    protected void onSetDatos() {

    }

    protected void acciones() {

        super.acciones();

        ratingBarUser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                if (b) {

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

                    Drawable progressDrawable = ratingBar.getProgressDrawable();

                    if (progressDrawable != null) {
                        DrawableCompat.setTint(progressDrawable,
                                contexto.getResources().getColor(color));
                    }
                    voto.setText(String.valueOf((int) (v)));
                    votoUser = v;
                }
            }
        });

        votar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                gone(lyvoto);
                enviarVoto(contexto, keyVoto, tipoRating, idRating, votoUser, comentario.getTexto());
                recuperarVotos(tipoRating, idRating);
            }
        });

        verVoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                visible(lyvoto);
                recuperarVotoUsuario(contexto, tipoRating, idRating);

            }
        });

        setAcciones();

    }

    protected void recuperarVotos(final String tipo, final String id) {

        listaVotos = new ArrayList<>();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("rating");
        Query query = db.orderByChild("idRating").equalTo(id);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    listaVotos.add(child.getValue(Rating.class));

                }

                rating = 0.0f;
                nVotos = 0.0f;

                for (Rating rat : listaVotos) {

                    if (rat.getTipo().equals(tipo)) {

                        float voto = rat.getValor();

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

                Drawable progressDrawable = ratingBar.getProgressDrawable();

                if (progressDrawable != null) {
                    DrawableCompat.setTint(progressDrawable,
                            contexto.getResources().getColor(color));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        query.addValueEventListener(valueEventListener);

    }

    protected void recuperarVotos(final RatingBar ratingBar,
                                  final Context contexto, final String tipo, final String id) {

        listaVotos = new ArrayList<>();
        ratingBar.setRating(0.0f);
        Drawable progressDrawable = ratingBar.getProgressDrawable();
        if (progressDrawable != null) {
            DrawableCompat.setTint(progressDrawable,
                    contexto.getResources().getColor(R.color.color_star_defecto));
        }

        DatabaseReference db = FirebaseDatabase.getInstance().
                getReference().child("rating");

        Query query = db.orderByChild("idRating").equalTo(id);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    listaVotos.add(child.getValue(Rating.class));

                }

                rating = 0.0f;
                nVotos = 0.0f;

                for (Rating rat : listaVotos) {

                    if (rat.getTipo().equals(tipo) && rat.getIdRating().equals(id)) {

                        float voto = rat.getValor();

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
                }


                if (nVotos > 0) {
                    rating /= nVotos;
                    ratingBar.setRating(rating);
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
                Drawable progressDrawable = ratingBar.getProgressDrawable();
                if (progressDrawable != null) {
                    DrawableCompat.setTint(progressDrawable,
                            contexto.getResources().getColor(color));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        query.addValueEventListener(valueEventListener);

    }

    public void recuperarVotoUsuario(final Context contexto, final String tipo, final String id) {

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
                    }

                }

                voto.setText(String.valueOf(votoUser));
                ratingBarUser.setRating(votoUser);
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

                Drawable progressDrawable = ratingBarUser.getProgressDrawable();

                if (progressDrawable != null) {
                    DrawableCompat.setTint(progressDrawable,
                            contexto.getResources().getColor(color));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        db.addValueEventListener(valueEventListener);

    }

    protected abstract String setIdRating();

    protected abstract String setTipoRating();

    protected void onClickRV(View v) {

        setOnClickRV(lista.get(rv.getChildAdapterPosition(v)));
        setIdRating();
        setTipoRating();
    }

}
