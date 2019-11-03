package com.codevsolution.base.android.controls;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.codevsolution.base.android.MainActivityBase;
import com.codevsolution.base.logica.InteractorBase;
import com.codevsolution.base.style.Estilos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;


public class ViewGroupLayout implements InteractorBase.Constantes, Estilos.Constantes {

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
    protected MainActivityBase activityBase;
    public static final String MAPA = "mapa";
    public static final String MAIL = "mail";
    public static final String LLAMADA = "llamada";
    public static final String WEB = "web";

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

    public ViewGroupLayout(MainActivityBase activityBase, Context context, ViewGroup viewGroupParent) {

        this.activityBase = activityBase;
        this.context = context;
        this.viewGroupParent = viewGroupParent;
        viewGroup = new LinearLayoutCompat(context);
        ((LinearLayoutCompat) viewGroup).setOrientation(ORI_LLC_VERTICAL);
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
        Estilos.setLayoutParams(viewGroupParent, viewGroup);
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

    }

    protected void asignarEventos(){

    }

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
        Estilos.setLayoutParams(viewGroup, vista);

        return vista;
    }

    public View addVista(View vista, int peso) {

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
        Estilos.setLayoutParams(viewGroup, vista, peso);

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
        Estilos.setLayoutParams(viewGroup, imagenLayout.getLinearLayoutCompat());

        return imagenLayout;
    }

    public ViewImagenLayout addImagenLayout(int peso) {

        ViewImagenLayout imagenLayout = new ViewImagenLayout(viewGroup, context);
        Estilos.setLayoutParams(viewGroup, imagenLayout.getLinearLayoutCompat(), peso);

        return imagenLayout;
    }

    public TextView addTextView(String text) {

        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setVisibility(View.VISIBLE);
        vistas.add(textView);
        viewGroup.addView(textView);
        Estilos.setLayoutParams(viewGroup, textView);

        return textView;
    }

    public TextView addTextView(String text, int peso) {

        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setVisibility(View.VISIBLE);
        vistas.add(textView);
        viewGroup.addView(textView);
        Estilos.setLayoutParams(viewGroup, textView, peso);

        return textView;
    }


    public EditMaterial addEditMaterial(String hint) {

        EditMaterial editMaterial = new EditMaterial(context);
        editMaterial.setHint(hint);
        editMaterial.setVisibility(View.VISIBLE);
        vistas.add(editMaterial);
        editMaterials.add(editMaterial);
        viewGroup.addView(editMaterial);
        Estilos.setLayoutParams(viewGroup, editMaterial);

        return editMaterial;
    }

    public EditMaterialLayout addEditMaterialLayout(String hint) {

        EditMaterialLayout editMaterial = new EditMaterialLayout(viewGroup, context);
        editMaterial.setHint(hint);
        editMaterial.getLinearLayout().setVisibility(View.VISIBLE);
        Estilos.setLayoutParams(viewGroup, editMaterial.getLinearLayout());
        editMaterialLayouts.add(editMaterial);

        return editMaterial;
    }

    public EditMaterialLayout addEditMaterialLayout(String hint, int peso) {

        EditMaterialLayout editMaterial = new EditMaterialLayout(viewGroup, context);
        editMaterial.setHint(hint);
        editMaterial.getLinearLayout().setVisibility(View.VISIBLE);
        Estilos.setLayoutParams(viewGroup, editMaterial.getLinearLayout(), peso);
        editMaterialLayouts.add(editMaterial);

        return editMaterial;
    }

    public EditMaterialLayout addEditMaterialLayout(int hint) {

        EditMaterialLayout editMaterial = new EditMaterialLayout(viewGroup, context);
        editMaterial.setHint(context.getString(hint));
        editMaterial.getLinearLayout().setVisibility(View.VISIBLE);
        Estilos.setLayoutParams(viewGroup, editMaterial.getLinearLayout());
        editMaterialLayouts.add(editMaterial);

        return editMaterial;
    }

    public EditMaterialLayout addEditMaterialLayout(int hint, int peso) {

        EditMaterialLayout editMaterial = new EditMaterialLayout(viewGroup, context);
        editMaterial.setHint(context.getString(hint));
        editMaterial.getLinearLayout().setVisibility(View.VISIBLE);
        Estilos.setLayoutParams(viewGroup, editMaterial.getLinearLayout(), peso);
        editMaterialLayouts.add(editMaterial);

        return editMaterial;
    }

    public EditMaterialLayout addEditMaterialLayout(String hint, String campoEdit) {

        final EditMaterialLayout editMaterial = new EditMaterialLayout(viewGroup, context);
        editMaterial.setHint(hint);
        Estilos.setLayoutParams(viewGroup, editMaterial.getLinearLayout());

        editMaterialLayouts.add(editMaterial);
        Map mapaCtrl = new HashMap();
        mapaCtrl.put("materialEdit", editMaterial);
        mapaCtrl.put("campoEdit", campoEdit);
        camposEdit.add(mapaCtrl);

        return editMaterial;
    }

    public EditMaterialLayout addEditMaterialLayout(String hint, String campoEdit, int peso) {

        final EditMaterialLayout editMaterial = new EditMaterialLayout(viewGroup, context);
        editMaterial.setHint(hint);
        Estilos.setLayoutParams(viewGroup, editMaterial.getLinearLayout(), peso);

        editMaterialLayouts.add(editMaterial);
        Map mapaCtrl = new HashMap();
        mapaCtrl.put("materialEdit", editMaterial);
        mapaCtrl.put("campoEdit", campoEdit);
        camposEdit.add(mapaCtrl);

        return editMaterial;
    }

    public EditMaterialLayout addEditMaterialLayout(int hint, String campoEdit) {

        final EditMaterialLayout editMaterial = new EditMaterialLayout(viewGroup, context);
        editMaterial.setHint(context.getString(hint));
        Estilos.setLayoutParams(viewGroup, editMaterial.getLinearLayout());

        editMaterialLayouts.add(editMaterial);
        Map mapaCtrl = new HashMap();
        mapaCtrl.put("materialEdit", editMaterial);
        mapaCtrl.put("campoEdit", campoEdit);
        camposEdit.add(mapaCtrl);

        return editMaterial;
    }

    public EditMaterialLayout addEditMaterialLayout(int hint, String campoEdit, int peso) {

        final EditMaterialLayout editMaterial = new EditMaterialLayout(viewGroup, context);
        editMaterial.setHint(context.getString(hint));
        Estilos.setLayoutParams(viewGroup, editMaterial.getLinearLayout(), peso);

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
                        editMaterial.setTipo(EditMaterialLayout.DIRECCION);
                        editMaterial.verEnMapa();
                    }
                });
            } else if (modo.equals(MAIL)) {
                editMaterial.setAccionEnviarMail(new EditMaterialLayout.ClickAccion() {
                    @Override
                    public void onClickAccion(View view) {
                        editMaterial.setTipo(EditMaterialLayout.EMAIL);
                        editMaterial.enviarEmail();
                    }
                });
            } else if (modo.equals(LLAMADA)) {
                editMaterial.setAccionLlamada(activity, new EditMaterialLayout.ClickAccion() {
                    @Override
                    public void onClickAccion(View view) {
                        editMaterial.setTipo(EditMaterialLayout.TELEFONO);
                        editMaterial.llamar();
                    }
                });
            } else if (modo.equals(WEB)) {
                editMaterial.setAccionVerWeb(new EditMaterialLayout.ClickAccion() {
                    @Override
                    public void onClickAccion(View view) {
                        editMaterial.setTipo(EditMaterialLayout.URI);
                        editMaterial.verWeb();
                    }
                });
            }
        }
        Estilos.setLayoutParams(viewGroup, editMaterial.getLinearLayout());
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
        button.setTextColor(Estilos.colorPrymary);
        button.setBackground(Estilos.getBotonSecondaryDark());
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonPrimary(String string) {

        Button button = new Button(context);
        button.setText(string);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorPrymary);
        button.setBackground(Estilos.getBotonSecondaryDark());
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonPrimary() {

        Button button = new Button(context);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorPrymary);
        button.setBackground(Estilos.getBotonSecondaryDark());
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonPrimary(MainActivityBase activityBase, int recursoString) {

        Button button = new Button(context);
        button.setText(recursoString);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorPrymary);
        button.setBackground(Estilos.getBotonSecondaryDark(activityBase));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonPrimary(MainActivityBase activityBase, String string) {

        Button button = new Button(context);
        button.setText(string);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorPrymary);
        button.setBackground(Estilos.getBotonSecondaryDark(activityBase));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonSecondary(int recursoString) {

        Button button = new Button(context);
        button.setText(recursoString);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorSecondaryDark);
        button.setBackground(Estilos.getBotonSecondary());
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonSecondary(String string) {

        Button button = new Button(context);
        button.setText(string);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorSecondaryDark);
        button.setBackground(Estilos.getBotonSecondary());
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonSecondary() {

        Button button = new Button(context);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorSecondaryDark);
        button.setBackground(Estilos.getBotonSecondary());
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonTrans(int recursoString) {

        Button button = new Button(context);
        button.setText(recursoString);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorSecondaryDark);
        button.setBackground(Estilos.getBotonPrimary());
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonTrans() {

        Button button = new Button(context);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorSecondaryDark);
        button.setBackground(Estilos.getBotonPrimary());
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button);

        return button;
    }


    public Button addButtonTrans(String string) {

        Button button = new Button(context);
        button.setText(string);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorSecondaryDark);
        button.setBackground(Estilos.getBotonPrimary());
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonSecondary(MainActivityBase activityBase, int recursoString) {

        Button button = new Button(context);
        button.setText(recursoString);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorSecondaryDark);
        button.setBackground(Estilos.getBotonSecondary(activityBase));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button);

        return button;
    }

    public Button addButtonSecondary(MainActivityBase activityBase, String string) {

        Button button = new Button(context);
        button.setText(string);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorSecondaryDark);
        button.setBackground(Estilos.getBotonSecondary(activityBase));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button);

        return button;
    }

/*
    public Button addButtonTrans(MainActivityBase activityBase, int recursoString) {

        Button button = new Button(context);
        button.setText(recursoString);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorSecondaryDark(context));
        button.setBackground(Estilos.btnTrans(context));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button);

        return button;
    }

 */

    public Button addButtonTrans(MainActivityBase activityBase, String string) {

        Button button = new Button(context);
        button.setText(string);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorSecondaryDark);
        button.setBackground(Estilos.getBotonPrimary(activityBase));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button);

        return button;
    }

    public ImageButton addImageButtonPrimary(int recurso) {

        ImageButton imageButton = new ImageButton(context);
        imageButton.setImageResource(recurso);
        imageButton.setPadding(5, 5, 5, 5);
        imageButton.setBackground(Estilos.btnPrimary(context));
        vistas.add(imageButton);
        imageButtons.add(imageButton);
        viewGroup.addView(imageButton);
        Estilos.setLayoutParams(viewGroup, imageButton);

        return imageButton;
    }

    public ImageButton addImageButtonSecundary(int recurso) {

        ImageButton imageButton = new ImageButton(context);
        imageButton.setImageResource(recurso);
        imageButton.setPadding(5, 5, 5, 5);
        imageButton.setBackground(Estilos.getBotonSecondary());
        vistas.add(imageButton);
        imageButtons.add(imageButton);
        viewGroup.addView(imageButton);
        Estilos.setLayoutParams(viewGroup, imageButton);

        return imageButton;
    }

    public Button addButtonPrimary(int recursoString, int peso) {

        Button button = new Button(context);
        button.setText(recursoString);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorPrimary(context));
        button.setBackground(Estilos.btnPrimary(context));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button, peso);

        return button;
    }

    public Button addButtonPrimary(String string, int peso) {

        Button button = new Button(context);
        button.setText(string);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorPrimary(context));
        button.setBackground(Estilos.btnPrimary(context));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button, peso);

        return button;
    }

    public Button addButtonSecondary(int recursoString, int peso) {

        Button button = new Button(context);
        button.setText(recursoString);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorSecondaryDark(context));
        button.setBackground(Estilos.getBotonSecondary());
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button, peso);

        return button;
    }

    public Button addButtonSecondary(MainActivityBase activityBase, int recursoString, int peso) {

        Button button = new Button(context);
        button.setText(recursoString);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorSecondaryDark);
        button.setBackground(Estilos.getBotonSecondary(activityBase));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button, peso);

        return button;
    }

    public Button addButtonSecondary(String string, int peso) {

        Button button = new Button(context);
        button.setText(string);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorSecondaryDark(context));
        button.setBackground(Estilos.btnSecondary(context));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button, peso);

        return button;
    }

    public Button addButtonTrans(int recursoString, int peso) {

        Button button = new Button(context);
        button.setText(recursoString);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorSecondaryDark(context));
        button.setBackground(Estilos.btnTrans(context));
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button, peso);

        return button;
    }

    public Button addButtonTrans(String string, int peso) {

        Button button = new Button(context);
        button.setText(string);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(Estilos.colorSecondaryDark(context));
        button.setBackground(Estilos.getBotonPrimary());
        vistas.add(button);
        buttons.add(button);
        viewGroup.addView(button);
        Estilos.setLayoutParams(viewGroup, button, peso);

        return button;
    }

    public ImageButton addImageButtonPrimary(int recurso, int peso) {

        ImageButton imageButton = new ImageButton(context);
        imageButton.setImageResource(recurso);
        imageButton.setPadding(5, 5, 5, 5);
        imageButton.setBackground(Estilos.btnPrimary(context));
        vistas.add(imageButton);
        imageButtons.add(imageButton);
        viewGroup.addView(imageButton);
        Estilos.setLayoutParams(viewGroup, imageButton, peso);

        return imageButton;
    }

    public ImageButton addImageButtonSecundary(int recurso, int peso) {

        ImageButton imageButton = new ImageButton(context);
        imageButton.setImageResource(recurso);
        imageButton.setPadding(5, 5, 5, 5);
        vistas.add(imageButton);
        imageButtons.add(imageButton);
        viewGroup.addView(imageButton);
        Estilos.setLayoutParams(viewGroup, imageButton, peso);
        imageButton.setBackground(Estilos.getBotonSecondary());


        return imageButton;
    }

    public ImageButton addImageButtonSecundary(MainActivityBase activityBase, int recurso, int peso) {

        ImageButton imageButton = new ImageButton(context);
        imageButton.setImageResource(recurso);
        imageButton.setPadding(5, 5, 5, 5);
        imageButton.setBackground(Estilos.getBotonSecondary(activityBase));
        vistas.add(imageButton);
        imageButtons.add(imageButton);
        viewGroup.addView(imageButton);
        Estilos.setLayoutParams(viewGroup, imageButton, peso);

        return imageButton;
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
