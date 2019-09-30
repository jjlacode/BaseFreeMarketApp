package com.codevsolution.base.android.controls;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import static com.codevsolution.base.logica.InteractorBase.Constantes.BTNPRIMARY;
import static com.codevsolution.base.logica.InteractorBase.Constantes.BTNSECONDARY;
import static com.codevsolution.base.logica.InteractorBase.Constantes.COLOR;
import static com.codevsolution.base.logica.InteractorBase.Constantes.COLORPRIMARY;
import static com.codevsolution.base.logica.InteractorBase.Constantes.COLORSECONDARYDARK;
import static com.codevsolution.base.logica.InteractorBase.Constantes.DRAWABLE;

public abstract class ViewLayout {

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
    protected Context context;
    public static final String MAPA = "mapa";
    public static final String MAIL = "mail";
    public static final String LLAMADA = "llamada";

    public ViewLayout(Context context, ViewGroup viewGroup) {

        this.context = context;
        viewGroupParent = viewGroup;
        setViewGroup();
        viewGroupParent.addView(this.viewGroup);
        inicio();
        inicializar();
        asignarEventos();
    }

    protected abstract void setViewGroup();

    private void inicio() {
        vistas = new ArrayList<>();
        buttons = new ArrayList<>();
        imageButtons = new ArrayList<>();
        editMaterials = new ArrayList<>();
        editMaterialLayouts = new ArrayList<>();
        camposEdit = new ArrayList<>();
    }

    protected abstract void inicializar();

    protected abstract void asignarEventos();

    protected abstract void setLayoutParams(ViewGroup viewGroup, View view);

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
        editMaterial.getLinearLayout().setVisibility(LinearLayoutCompat.VISIBLE);
        setLayoutParams(viewGroup, editMaterial.getLinearLayout());
        editMaterialLayouts.add(editMaterial);

        return editMaterial;
    }

    public EditMaterialLayout addEditMaterialLayout(int hint) {

        EditMaterialLayout editMaterial = new EditMaterialLayout(viewGroup, context);
        editMaterial.setHint(context.getString(hint));
        editMaterial.getLinearLayout().setVisibility(LinearLayoutCompat.VISIBLE);
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

}
