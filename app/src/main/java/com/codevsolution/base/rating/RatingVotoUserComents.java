package com.codevsolution.base.rating;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.RVAdapter;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.FragmentBase;
import com.codevsolution.base.android.controls.EditMaterialLayout;
import com.codevsolution.base.android.controls.RatingBarLayout;
import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.Rating;
import com.codevsolution.base.style.Estilos;
import com.codevsolution.base.time.TimeDateUtil;

import java.util.ArrayList;

public class RatingVotoUserComents extends Fragment {

    ViewGroupLayout vistaMain;
    FragmentBase frParent;
    AppCompatActivity activityBase;
    ViewGroup viewGroup;
    Context contexto;
    EditMaterialLayout etVoto;
    EditMaterialLayout etComentario;
    ImageButton btnEnviarVoto;
    ImageButton btnVerVotos;
    ImageButton btnVerComentarios;
    RatingBarLayout ratingBarMain;
    RatingBarLayout ratingBarUser;
    RatingBarLayout ratingBarStar1;
    RatingBarLayout ratingBarStar2;
    RatingBarLayout ratingBarStar3;
    RatingBarLayout ratingBarStar4;
    RatingBarLayout ratingBarStar5;
    TextView star1;
    TextView star2;
    TextView star3;
    TextView star4;
    TextView star5;
    TextView totalVotos;
    TextView ultimoVoto;
    TextView tvComentario;
    RecyclerView rvComentarios;
    ViewGroupLayout vistaBarMain;
    ViewGroupLayout vistaBarUser;
    ViewGroupLayout vistaStars;
    ViewGroupLayout vistaStats;
    ViewGroupLayout vistaComents;
    OnEnviarVoto onEnviarVotoListener;
    OnVerVotos onVerVotosListener;
    OnVerComents onVerComentsListener;
    private ArrayList<Rating> listaComents;


    public RatingVotoUserComents(FragmentBase frParent, ViewGroup viewGroup, AppCompatActivity activityBase) {
        this.frParent = frParent;
        this.viewGroup = viewGroup;
        this.activityBase = activityBase;
        contexto = frParent.getContext();
        setInicio();
        acciones();
    }

    private void acciones() {

    }

    private void setInicio() {

        vistaMain = new ViewGroupLayout(contexto, viewGroup);
        vistaBarMain = new ViewGroupLayout(contexto, vistaMain.getViewGroup());
        ratingBarMain = new RatingBarLayout(contexto, vistaBarMain.getViewGroup(), true, false);
        ratingBarMain.setVisibilidadImageButton(true);
        ratingBarMain.setImageButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vistaBarUser.getViewGroup().getVisibility() == View.VISIBLE) {
                    vistaBarUser.getViewGroup().setVisibility(View.GONE);
                } else {
                    vistaBarUser.getViewGroup().setVisibility(View.VISIBLE);
                    if (onVerVotosListener != null) {
                        onVerVotosListener.onClickVerVotos();
                    }
                }
            }
        });

        vistaBarUser = new ViewGroupLayout(contexto, vistaMain.getViewGroup());
        ratingBarUser = new RatingBarLayout(contexto, vistaBarUser.getViewGroup(), false, false);
        ratingBarUser.setVisibilidadVoto(true);
        ratingBarUser.setVisibilidadImageButton(true);
        ratingBarUser.setRecursoImageButton(Estilos.getIdDrawable(contexto, "ic_enviar_indigo"));
        ratingBarUser.setImageButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onEnviarVotoListener != null) {
                    onEnviarVotoListener.onClickEnviar(ratingBarUser.getVotoUser(), etComentario.getTexto());
                }
            }
        });

        vistaBarUser.getViewGroup().setVisibility(View.GONE);
        etComentario = vistaBarUser.addEditMaterialLayout(Estilos.getString(contexto, "comentario"));
        vistaStars = new ViewGroupLayout(contexto, vistaMain.getViewGroup());
        vistaStars.getViewGroup().setVisibility(View.GONE);
        ratingBarStar5 = new RatingBarLayout(contexto, vistaStars.getViewGroup(), true, true);
        ratingBarStar5.setBarRating(5);
        ratingBarStar5.setProgressTintList("color_star_ok");
        ratingBarStar5.setProgressBackgoundTintList("color_star_defecto");
        ratingBarStar5.setSecondaryProgressTintList("Color_star_small_ok");
        ratingBarStar5.setTextViewBackground("Color_star_small_ok");
        ratingBarStar5.setTextViewTextColor("Color_texto");
        ratingBarStar5.setTextViewText(Estilos.getString(contexto, "clasificacion"));
        ratingBarStar5.setTextViewGravity(Gravity.CENTER);
        ratingBarStar5.setVisibilidadTexto(true);

        ratingBarStar4 = new RatingBarLayout(contexto, vistaStars.getViewGroup(), true, true);
        ratingBarStar4.setBarRating(4);
        ratingBarStar4.setProgressTintList("color_star_ok");
        ratingBarStar4.setProgressBackgoundTintList("color_star_defecto");
        ratingBarStar4.setSecondaryProgressTintList("Color_star_small_ok");
        ratingBarStar4.setTextViewBackground("Color_star_small_ok");
        ratingBarStar4.setTextViewTextColor("Color_texto");
        ratingBarStar4.setTextViewText(Estilos.getString(contexto, "clasificacion"));
        ratingBarStar4.setTextViewGravity(Gravity.CENTER);
        ratingBarStar4.setVisibilidadTexto(true);

        ratingBarStar3 = new RatingBarLayout(contexto, vistaStars.getViewGroup(), true, true);
        ratingBarStar3.setBarRating(3);
        ratingBarStar3.setProgressTintList("color_star_acept");
        ratingBarStar3.setProgressBackgoundTintList("color_star_defecto");
        ratingBarStar3.setSecondaryProgressTintList("Color_star_small_acept");
        ratingBarStar3.setTextViewBackground("Color_star_small_acept");
        ratingBarStar3.setTextViewTextColor("Color_texto");
        ratingBarStar3.setTextViewText(Estilos.getString(contexto, "clasificacion"));
        ratingBarStar3.setTextViewGravity(Gravity.CENTER);
        ratingBarStar3.setVisibilidadTexto(true);

        ratingBarStar2 = new RatingBarLayout(contexto, vistaStars.getViewGroup(), true, true);
        ratingBarStar2.setBarRating(2);
        ratingBarStar2.setProgressTintList("color_star_acept");
        ratingBarStar2.setProgressBackgoundTintList("color_star_defecto");
        ratingBarStar2.setSecondaryProgressTintList("Color_star_small_acept");
        ratingBarStar2.setTextViewBackground("Color_star_small_acept");
        ratingBarStar2.setTextViewTextColor("Color_texto");
        ratingBarStar2.setTextViewText(Estilos.getString(contexto, "clasificacion"));
        ratingBarStar2.setTextViewGravity(Gravity.CENTER);
        ratingBarStar2.setVisibilidadTexto(true);

        ratingBarStar1 = new RatingBarLayout(contexto, vistaStars.getViewGroup(), true, true);
        ratingBarStar1.setBarRating(1);
        ratingBarStar1.setProgressTintList("color_star_notok");
        ratingBarStar1.setProgressBackgoundTintList("color_star_defecto");
        ratingBarStar1.setSecondaryProgressTintList("Color_star_small_notok");
        ratingBarStar1.setTextViewBackground("Color_star_small_notok");
        ratingBarStar1.setTextViewTextColor("Color_texto");
        ratingBarStar1.setTextViewText(Estilos.getString(contexto, "clasificacion"));
        ratingBarStar1.setTextViewGravity(Gravity.CENTER);
        ratingBarStar1.setVisibilidadTexto(true);


        btnVerComentarios = vistaMain.addImageButtonSecundary(Estilos.getIdDrawable(contexto, "ic_lista_notas_indigo"));
        btnVerComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vistaComents.getViewGroup().getVisibility() == View.VISIBLE) {
                    vistaComents.getViewGroup().setVisibility(View.GONE);
                    vistaStars.getViewGroup().setVisibility(View.GONE);
                    vistaStats.getViewGroup().setVisibility(View.GONE);

                    if (onVerComentsListener != null) {
                        onVerComentsListener.onHideComents();
                    }

                } else {

                    vistaComents.getViewGroup().setVisibility(View.VISIBLE);
                    vistaStars.getViewGroup().setVisibility(View.VISIBLE);
                    vistaStats.getViewGroup().setVisibility(View.VISIBLE);

                    if (onVerComentsListener != null) {
                        onVerComentsListener.onShowComents();
                    }

                }
            }
        });

        vistaStats = new ViewGroupLayout(contexto, vistaStars.getViewGroup());
        vistaStats.getViewGroup().setVisibility(View.GONE);

        totalVotos = vistaStats.addTextView(Estilos.getString(contexto, "clasificacion"));
        ultimoVoto = vistaStats.addTextView(Estilos.getString(contexto, "clasificacion"));

        vistaComents = new ViewGroupLayout(contexto, vistaMain.getViewGroup(), new RelativeLayout(contexto));
        rvComentarios = (RecyclerView) vistaComents.addVista(new RecyclerView(contexto));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(contexto);
        RVAdapter adapter = new RVAdapter(new ViewHolderRVComents(rvComentarios), listaComents, 0);
        rvComentarios.setLayoutManager(manager);
        rvComentarios.setAdapter(adapter);
        Estilos.setLayoutParams(vistaComents.getViewGroup(), rvComentarios, ViewGroupLayout.MATCH_PARENT, ViewGroupLayout.WRAP_CONTENT);
        rvComentarios.setBackgroundColor(Estilos.colorSecondary);
        vistaComents.getViewGroup().setVisibility(View.GONE);


    }

    public ViewGroup getVievGroup() {

        return vistaMain.getViewGroup();
    }

    public void setVisibilidadRatingBarUser(boolean visible) {

        if (visible) {
            vistaBarUser.getViewGroup().setVisibility(View.VISIBLE);
        } else {
            vistaBarUser.getViewGroup().setVisibility(View.GONE);
        }
    }

    public void setVisibilidadBtnVerVotoUser(boolean visible) {

        ratingBarMain.setVisibilidadImageButton(visible);
    }
    public void setComentario(String comentario) {
        etComentario.setText(comentario);
    }

    public void setVotoUser(float votoUser) {

        ratingBarUser.setVoto(votoUser);
    }

    public void recuperarComentarios(ArrayList<Rating> listaComents) {
        this.listaComents = listaComents;
        RecyclerView.LayoutManager manager = new LinearLayoutManager(contexto);
        RVAdapter adapter = new RVAdapter(new ViewHolderRVComents(rvComentarios), listaComents, 0);
        rvComentarios.setLayoutManager(manager);
        rvComentarios.setAdapter(adapter);

    }

    public void recuperarVotos(ArrayList<Rating> listaVotos) {

        ratingBarMain.setBarRating(0.0f);

        float rating = 0.0f;
        float nVotos = 0.0f;
        int st1 = 0;
        int st2 = 0;
        int st3 = 0;
        int st4 = 0;
        int st5 = 0;
        long ultimoVotoTemp = 0;


        for (Rating rat : listaVotos) {

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

            if (rat.getFecha() > ultimoVotoTemp) {
                ultimoVotoTemp = rat.getFecha();
            }
        }

        ratingBarStar5.setTextViewText(JavaUtil.getDecimales(st5, "0"));
        ratingBarStar4.setTextViewText(JavaUtil.getDecimales(st4, "0"));
        ratingBarStar3.setTextViewText(JavaUtil.getDecimales(st3, "0"));
        ratingBarStar2.setTextViewText(JavaUtil.getDecimales(st2, "0"));
        ratingBarStar1.setTextViewText(JavaUtil.getDecimales(st1, "0"));
        totalVotos.setText("Numero de valoraciones: " + JavaUtil.getDecimales(nVotos, "0"));
        if (ultimoVotoTemp > 0) {
            ultimoVoto.setText("Ultima valoración: " + TimeDateUtil.getDateString(ultimoVotoTemp));
        } else {
            ultimoVoto.setText("Sin valoraciones todavía");
        }

        if (nVotos > 0) {
            rating /= nVotos;
            ratingBarMain.setBarRating(rating);
            btnVerComentarios.setVisibility(View.VISIBLE);

        } else {
            ratingBarMain.setBarRating(0.0f);
            btnVerComentarios.setVisibility(View.GONE);
        }

    }

    public void recuperarVotosUsuario(float votoUser, String comentario) {

        setVotoUser(votoUser);
        setComentario(comentario);
    }

    public void setOnEnviarVotoListener(OnEnviarVoto onEnviarVotoListener) {
        this.onEnviarVotoListener = onEnviarVotoListener;
    }

    public interface OnEnviarVoto {

        void onClickEnviar(float votoUser, String comentario);
    }

    public void setOnVerVotosListener(OnVerVotos onVerVotosListener) {
        this.onVerVotosListener = onVerVotosListener;
    }

    public interface OnVerVotos {

        void onClickVerVotos();
    }

    public void setOnVerComentsListener(OnVerComents onVerComentsListener) {
        this.onVerComentsListener = onVerComentsListener;
    }

    public interface OnVerComents {

        void onShowComents();

        void onHideComents();
    }

    public class ViewHolderRVComents extends BaseViewHolder implements TipoViewHolder {

        TextView comentario;
        TextView nombreComent;
        TextView fechaComent;
        TextView tipoComment;
        RatingBarLayout ratingBarComent;
        CardView card;
        RelativeLayout relativeLayout;

        public ViewHolderRVComents(View itemView) {
            super(itemView);


            relativeLayout = itemView.findViewById(Estilos.getIdResource(contexto, "ry_item_list"));

        }

        @Override
        public void bind(ArrayList<?> lista, int position) {

            Rating rating = (Rating) lista.get(position);

            ViewGroupLayout vistaCard = new ViewGroupLayout(contexto, relativeLayout, new CardView(contexto));
            card = (CardView) vistaCard.getViewGroup();
            LinearLayoutCompat mainLinear = (LinearLayoutCompat) vistaCard.addVista(new LinearLayoutCompat(contexto));
            mainLinear.setOrientation(LinearLayoutCompat.VERTICAL);
            ViewGroupLayout vistaForm = new ViewGroupLayout(contexto, mainLinear);
            ViewGroupLayout vistaNombreTipo = new ViewGroupLayout(contexto, vistaForm.getViewGroup());
            vistaNombreTipo.setOrientacion(ViewGroupLayout.ORI_LLC_HORIZONTAL);
            nombreComent = vistaNombreTipo.addTextView(rating.getNombreUser(), 1);
            tipoComment = vistaNombreTipo.addTextView(rating.getTipo(), 1);
            comentario = vistaForm.addTextView(rating.getComentario());
            fechaComent = vistaForm.addTextView(TimeDateUtil.getDateString(rating.getFecha()));
            ViewGroupLayout vistaBar = new ViewGroupLayout(contexto, mainLinear);
            vistaBar.setOrientacion(ViewGroupLayout.ORI_LLC_VERTICAL);
            ratingBarComent = new RatingBarLayout(contexto, vistaBar.getViewGroup(), true, true);
            float ratUser = rating.getValor();
            ratingBarComent.setBarRating(ratUser);

            super.bind(lista, position);

        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRVComents(view);
        }
    }
}
