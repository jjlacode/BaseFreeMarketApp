package com.codevsolution.base.android.controls;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import static com.codevsolution.base.logica.InteractorBase.Constantes.BTNPRIMARY;
import static com.codevsolution.base.logica.InteractorBase.Constantes.BTNSECONDARY;
import static com.codevsolution.base.logica.InteractorBase.Constantes.BTNTRANSPARENTE;
import static com.codevsolution.base.logica.InteractorBase.Constantes.COLOR;
import static com.codevsolution.base.logica.InteractorBase.Constantes.COLORPRIMARY;
import static com.codevsolution.base.logica.InteractorBase.Constantes.COLORSECONDARYDARK;
import static com.codevsolution.base.logica.InteractorBase.Constantes.DRAWABLE;

public class ViewGroupLayout {

    protected ViewGroup viewGroup;
    protected ViewGroup viewGroupParent;
    protected ArrayList<View> vistas;
    protected ArrayList<Button> buttons;
    protected ArrayList<ImageButton> imageButtons;
    protected ArrayList<EditMaterial> editMaterials;
    protected ArrayList<EditMaterialLayout> editMaterialLayouts;
    protected ArrayList<Map> camposEdit;
    protected Timer timer;
    protected int position;
    protected int orientacion;
    protected Context context;
    public static final String MAPA = "mapa";
    public static final String MAIL = "mail";
    public static final String LLAMADA = "llamada";
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    public static final int ORI_LLC_VERTICAL = LinearLayoutCompat.VERTICAL;
    public static final int ORI_LLC_HORIZONTAL = LinearLayoutCompat.HORIZONTAL;
    public static final int ORI_LL_VERTICAL = LinearLayout.VERTICAL;
    public static final int ORI_LL_HORIZONTAL = LinearLayout.HORIZONTAL;

    public ViewGroupLayout(Context context, ViewGroup viewGroupParent) {

        this.context = context;
        this.viewGroupParent = viewGroupParent;
        viewGroup = new LinearLayoutCompat(context);
        ((LinearLayoutCompat)viewGroup).setOrientation(ORI_LLC_VERTICAL);
        this.viewGroupParent.addView(this.viewGroup);
        inicio();
        inicializar();
        asignarEventos();
    }

    public ViewGroupLayout(Context context, ViewGroup viewGroupParent, ViewGroup viewGroup) {

        this.context = context;
        this.viewGroupParent = viewGroupParent;
        this.viewGroup = viewGroup;
        viewGroupParent.addView(this.viewGroup);
        setLayoutParams(viewGroupParent,viewGroup);
        inicio();
        inicializar();
        asignarEventos();
    }

    private void inicio() {
        vistas = new ArrayList<>();
        buttons = new ArrayList<>();
        imageButtons = new ArrayList<>();
        editMaterials = new ArrayList<>();
        editMaterialLayouts = new ArrayList<>();
        camposEdit = new ArrayList<>();
    }

    protected void inicializar(){

    };

    protected void asignarEventos(){

    };

    public ArrayList<View> getVistas() {

        return vistas;
    }

    public ArrayList<EditMaterial> getEditMaterials() {

        return editMaterials;
    }

    public ArrayList<EditMaterialLayout> getEditMaterialLayouts() {

        return editMaterialLayouts;
    }

    public ArrayList<Map> getCamposEdit() {

        return camposEdit;
    }

    public View addVista(View vista) {

        vistas.add(vista);

        if (vista instanceof EditMaterial) {
            vista.setFocusable(true);
            editMaterials.add((EditMaterial) vista);
        } else if (vista instanceof Button) {
            buttons.add((Button) vista);
        } else if (vista instanceof ImageButton) {
            imageButtons.add((ImageButton) vista);
        }

        viewGroup.addView(vista);
        setLayoutParams(viewGroup, vista);

        return vista;
    }

    public ViewGroup getViewGroup() {
        return viewGroup;
    }

    public ViewGroup getViewGroupParent() {
        return viewGroupParent;
    }

    public ViewImagenLayout addImagenLayout(){

        ViewImagenLayout imagenLayout = new ViewImagenLayout(viewGroup,context);
        setLayoutParams(viewGroup,imagenLayout.getLinearLayoutCompat());

        return imagenLayout;
    }

    public TextView addTextView(String text) {

        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setVisibility(View.VISIBLE);
        vistas.add(textView);
        viewGroup.addView(textView);
        setLayoutParams(viewGroup, textView);

        return textView;
    }


    public EditMaterial addEditMaterial(String hint) {

        EditMaterial editMaterial = new EditMaterial(context);
        editMaterial.setHint(hint);
        editMaterial.setVisibility(View.VISIBLE);
        vistas.add(editMaterial);
        editMaterials.add(editMaterial);
        viewGroup.addView(editMaterial);
        setLayoutParams(viewGroup, editMaterial);

        return editMaterial;
    }

    public EditMaterialLayout addEditMaterialLayout(String hint) {

        EditMaterialLayout editMaterial = new EditMaterialLayout(viewGroup, context);
        editMaterial.setHint(hint);
        editMaterial.getLinearLayout().setVisibility(View.VISIBLE);
        setLayoutParams(viewGroup, editMaterial.getLinearLayout());
        editMaterialLayouts.add(editMaterial);

        return editMaterial;
    }

    public EditMaterialLayout addEditMaterialLayout(int hint) {

        EditMaterialLayout editMaterial = new EditMaterialLayout(viewGroup, context);
        editMaterial.setHint(context.getString(hint));
        editMaterial.getLinearLayout().setVisibility(View.VISIBLE);
        setLayoutParams(viewGroup, editMaterial.getLinearLayout());
        editMaterialLayouts.add(editMaterial);

        return editMaterial;
    }

    public EditMaterialLayout addEditMaterialLayout(String hint, String campoEdit) {

        final EditMaterialLayout editMaterial = new EditMaterialLayout(viewGroup, context);
        editMaterial.setHint(hint);
        setLayoutParams(viewGroup, editMaterial.getLinearLayout());

        editMaterialLayouts.add(editMaterial);
        Map mapaCtrl = new HashMap();
        mapaCtrl.put("materialEdit", editMaterial);
        mapaCtrl.put("campoEdit", campoEdit);
        camposEdit.add(mapaCtrl);

        return editMaterial;
    }



    public EditMaterialLayout addEditMaterialLayout(String hint, String campoEdit, String modo, AppCompatActivity activity) {

        final EditMaterialLayout editMaterial = new EditMaterialLayout(viewGroup, context);
        editMaterial.setHint(hint);

        if (modo != null) {
            if (modo.equals(MAPA)) {
                editMaterial.setAccionVerMapa(new EditMaterialLayout.ClickAccion() {
                    @Override
                    public void onClickAccion(View view) {
                        editMaterial.verEnMapa();
                    }
                });
            } else if (modo.equals(MAIL)) {
                editMaterial.setAccionEnviarMail(new EditMaterialLayout.ClickAccion() {
                    @Override
                    public void onClickAccion(View view) {
                        editMaterial.enviarEmail();
                    }
                });
            } else if (modo.equals(LLAMADA)) {
                editMaterial.setAccionLlamada(activity, new EditMaterialLayout.ClickAccion() {
                    @Override
                    public void onClickAccion(View view) {
                        editMaterial.llamar();
                    }
                });
            }
        }
        setLayoutParams(viewGroup, editMaterial.getLinearLayout());
        editMaterialLayouts.add(editMaterial);
        Map mapaCtrl = new HashMap();
        mapaCtrl.put("materialEdit", editMaterial);
        mapaCtrl.put("campoEdit", campoEdit);
        camposEdit.add(mapaCtrl);

        return editMaterial;
    }

    public Button addButtonPrimary(int recursoString) {

        Button button = new Button(context);
        button.setText(recursoString);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(context.getResources().getColor(context.getResources().
                getIdentifier(COLORPRIMARY, COLOR,
                        context.getPackageName())));
        button.setBackground(context.getResources().getDrawable(context.getResources().
                getIdentifier(BTNPRIMARY, DRAWABLE,
                        context.getPackageName())));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonPrimary(String string) {

        Button button = new Button(context);
        button.setText(string);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(context.getResources().getColor(context.getResources().
                getIdentifier(COLORPRIMARY, COLOR,
                        context.getPackageName())));
        button.setBackground(context.getResources().getDrawable(context.getResources().
                getIdentifier(BTNPRIMARY, DRAWABLE,
                        context.getPackageName())));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonSecondary(int recursoString) {

        Button button = new Button(context);
        button.setText(recursoString);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(context.getResources().getColor(context.getResources().
                getIdentifier(COLORSECONDARYDARK, COLOR,
                        context.getPackageName())));
        button.setBackground(context.getResources().getDrawable(context.getResources().
                getIdentifier(BTNSECONDARY, DRAWABLE,
                        context.getPackageName())));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonSecondary(String string) {

        Button button = new Button(context);
        button.setText(string);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(context.getResources().getColor(context.getResources().
                getIdentifier(COLORSECONDARYDARK, COLOR,
                        context.getPackageName())));
        button.setBackground(context.getResources().getDrawable(context.getResources().
                getIdentifier(BTNSECONDARY, DRAWABLE,
                        context.getPackageName())));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonTrans(int recursoString) {

        Button button = new Button(context);
        button.setText(recursoString);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(context.getResources().getColor(context.getResources().
                getIdentifier(COLORSECONDARYDARK, COLOR,
                        context.getPackageName())));
        button.setBackground(context.getResources().getDrawable(context.getResources().
                getIdentifier(BTNTRANSPARENTE, DRAWABLE,
                        context.getPackageName())));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonTrans(String string) {

        Button button = new Button(context);
        button.setText(string);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(context.getResources().getColor(context.getResources().
                getIdentifier(COLORSECONDARYDARK, COLOR,
                        context.getPackageName())));
        button.setBackground(context.getResources().getDrawable(context.getResources().
                getIdentifier(BTNTRANSPARENTE, DRAWABLE,
                        context.getPackageName())));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        setLayoutParams(viewGroup, button);

        return button;
    }

    public ImageButton addImageButtonPrimary(int recurso) {

        ImageButton imageButton = new ImageButton(context);
        imageButton.setImageResource(recurso);
        imageButton.setPadding(5, 5, 5, 5);
        imageButton.setBackground(context.getResources().getDrawable(context.getResources().
                getIdentifier(BTNPRIMARY, DRAWABLE,
                        context.getPackageName())));
        vistas.add(imageButton);
        imageButtons.add(imageButton);
        viewGroup.addView(imageButton);
        setLayoutParams(viewGroup, imageButton);

        return imageButton;
    }

    public ImageButton addImageButtonSecundary(int recurso) {

        ImageButton imageButton = new ImageButton(context);
        imageButton.setImageResource(recurso);
        imageButton.setPadding(5, 5, 5, 5);
        imageButton.setBackground(context.getResources().getDrawable(context.getResources().
                getIdentifier(BTNSECONDARY, DRAWABLE,
                        context.getPackageName())));
        vistas.add(imageButton);
        imageButtons.add(imageButton);
        viewGroup.addView(imageButton);
        setLayoutParams(viewGroup, imageButton);

        return imageButton;
    }

    protected void setLayoutParams(ViewGroup viewGroup, View view) {

        if (viewGroup instanceof LinearLayoutCompat) {
            LinearLayoutCompat.LayoutParams params;
            if (((LinearLayoutCompat) viewGroup).getOrientation() == ORI_LLC_VERTICAL) {
                params = new LinearLayoutCompat.LayoutParams(MATCH_PARENT, MATCH_PARENT, 1);
            } else {
                params = new LinearLayoutCompat.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1);
            }
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof LinearLayout) {
            LinearLayout.LayoutParams params;
            if (((LinearLayout) viewGroup).getOrientation() == ORI_LL_VERTICAL) {
                params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, 1);
            } else {
                params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1);
            }
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params;
                params = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof CardView) {
            CardView.LayoutParams params;
            params = new CardView.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof FrameLayout) {
            FrameLayout.LayoutParams params;
            params = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            params.setMargins(5, 5, 5, 5);
            view.setLayoutParams(params);
        }

    }

    protected void setLayoutParams(ViewGroup viewGroup, View view, int ancho, int alto, float peso, int pad) {

        if (viewGroup instanceof LinearLayoutCompat) {
            LinearLayoutCompat.LayoutParams params;
                params = new LinearLayoutCompat.LayoutParams(ancho,alto,peso);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof LinearLayout) {
            LinearLayout.LayoutParams params;
                params = new LinearLayout.LayoutParams(ancho,alto,peso);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(ancho,alto);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof CardView) {
            CardView.LayoutParams params;
            params = new CardView.LayoutParams(ancho,alto);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        } else if (viewGroup instanceof FrameLayout) {
            FrameLayout.LayoutParams params;
            params = new FrameLayout.LayoutParams(ancho,alto);
            params.setMargins(pad, pad, pad, pad);
            view.setLayoutParams(params);
        }


    }

    public void setOrientacion(int orientacion) {

        if (viewGroupParent instanceof LinearLayoutCompat) {
            if (orientacion == ORI_LLC_VERTICAL) {
                LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                viewGroup.setLayoutParams(params);
                this.orientacion = ORI_LLC_VERTICAL;
                ((LinearLayoutCompat) viewGroup).setOrientation(ORI_LLC_VERTICAL);

            } else {

                LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT);
                viewGroup.setLayoutParams(params);
                this.orientacion = ORI_LLC_HORIZONTAL;
                ((LinearLayoutCompat) viewGroup).setOrientation(ORI_LLC_HORIZONTAL);

            }
        } else if (viewGroupParent instanceof LinearLayout) {
            if (orientacion == ORI_LL_VERTICAL) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                viewGroup.setLayoutParams(params);
                this.orientacion = ORI_LL_VERTICAL;
                ((LinearLayoutCompat) viewGroup).setOrientation(ORI_LLC_VERTICAL);

            } else {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                viewGroup.setLayoutParams(params);
                this.orientacion = ORI_LL_HORIZONTAL;
                ((LinearLayoutCompat) viewGroup).setOrientation(ORI_LLC_HORIZONTAL);

            }
        }



    }

    public void setOrientacion(int orientacion, float weigth) {

        if (viewGroupParent instanceof LinearLayoutCompat) {
            if (orientacion == ORI_LLC_VERTICAL) {
                LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,weigth);
                viewGroup.setLayoutParams(params);
                this.orientacion = ORI_LLC_VERTICAL;
                ((LinearLayoutCompat) viewGroup).setOrientation(ORI_LLC_VERTICAL);

            } else {

                LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,weigth);
                viewGroup.setLayoutParams(params);
                this.orientacion = ORI_LLC_HORIZONTAL;
                ((LinearLayoutCompat) viewGroup).setOrientation(ORI_LLC_HORIZONTAL);

            }
        } else if (viewGroupParent instanceof LinearLayout) {
            if (orientacion == ORI_LL_VERTICAL) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                viewGroup.setLayoutParams(params);
                this.orientacion = ORI_LL_VERTICAL;
                ((LinearLayoutCompat) viewGroup).setOrientation(ORI_LLC_VERTICAL);

            } else {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                viewGroup.setLayoutParams(params);
                this.orientacion = ORI_LL_HORIZONTAL;
                ((LinearLayoutCompat) viewGroup).setOrientation(ORI_LLC_HORIZONTAL);

            }
        }



    }

}
