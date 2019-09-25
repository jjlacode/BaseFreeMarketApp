package com.codevsolution.base.android.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.codevsolution.freemarketsapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class ViewLinearLayout extends LinearLayoutCompat {

    LinearLayoutCompat lyView;
    ArrayList<View> vistas;
    ArrayList<Button> buttons;
    ArrayList<ImageButton> imageButtons;
    ArrayList<EditMaterial> editMaterials;
    private ArrayList<Map> camposEdit;
    private Timer timer;
    private int position;
    public static final String MAPA = "mapa";
    public static final String MAIL = "mail";
    public static final String LLAMADA = "llamada";
    public static final int HORIZONTAL = LinearLayoutCompat.HORIZONTAL;
    public static final int VERTICAL = LinearLayoutCompat.VERTICAL;

    public ViewLinearLayout(Context context) {
        super(context);

        inicializar();
    }

    public ViewLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inicializar();

        setAtributos(attrs);
    }

    public ViewLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inicializar();

        setAtributos(attrs);
    }

    private void setAtributos(AttributeSet attrs) {

    }

    private void inicializar() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li =
                (LayoutInflater) getContext().getSystemService(infService);
        li.inflate(R.layout.view_linear_layout, this, true);
        lyView = findViewById(R.id.lyviewlinearlayout);
        vistas = new ArrayList<>();
        buttons = new ArrayList<>();
        imageButtons = new ArrayList<>();
        editMaterials = new ArrayList<>();
        camposEdit = new ArrayList<>();
        setOrientacion(VERTICAL);
        asignarEventos();
    }

    private void asignarEventos() {


    }

    public void setOrientacion(int orientacion) {

        if (orientacion == VERTICAL) {
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            lyView.setLayoutParams(params);
            lyView.setOrientation(VERTICAL);

        } else {

            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            lyView.setLayoutParams(params);
            lyView.setOrientation(HORIZONTAL);

        }
    }

    public ArrayList<View> getVistas() {

        return vistas;
    }

    public ArrayList<EditMaterial> getEditMaterials() {

        return editMaterials;
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

        setLayoutParams(vista);
        lyView.addView(vista);

        return vista;
    }

    public EditMaterial addEditMaterial(String hint) {

        EditMaterial editMaterial = new EditMaterial(getContext());
        editMaterial.setHint(hint);
        editMaterial.setVisibility(View.VISIBLE);
        setLayoutParams(editMaterial);
        vistas.add(editMaterial);
        editMaterials.add(editMaterial);
        lyView.addView(editMaterial);

        return editMaterial;
    }

    public EditMaterial addEditMaterial(String hint, String campoEdit, String modo, AppCompatActivity activity) {

        final EditMaterial editMaterial = new EditMaterial(getContext());
        editMaterial.setHint(hint);
        editMaterial.setVisibility(View.VISIBLE);
        setLayoutParams(editMaterial);
        if (modo != null) {
            if (modo.equals(MAPA)) {
                editMaterial.setAccionVerMapa(new EditMaterial.ClickAccion() {
                    @Override
                    public void onClickAccion(View view) {
                        editMaterial.verEnMapa();
                    }
                });
            } else if (modo.equals(MAIL)) {
                editMaterial.setAccionEnviarMail(new EditMaterial.ClickAccion() {
                    @Override
                    public void onClickAccion(View view) {
                        editMaterial.enviarEmail();
                    }
                });
            } else if (modo.equals(LLAMADA)) {
                editMaterial.setAccionLlamada(activity, new EditMaterial.ClickAccion() {
                    @Override
                    public void onClickAccion(View view) {
                        editMaterial.llamar();
                    }
                });
            }
        }
        vistas.add(editMaterial);
        editMaterials.add(editMaterial);
        Map mapaCtrl = new HashMap();
        mapaCtrl.put("materialEdit", editMaterial);
        mapaCtrl.put("campoEdit", campoEdit);
        camposEdit.add(mapaCtrl);
        lyView.addView(editMaterial);

        return editMaterial;
    }

    public Button addButtonPrimary(int recursoString) {

        Button button = new Button(getContext());
        button.setText(recursoString);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(getResources().getColor(R.color.colorPrimary));
        button.setBackground(getResources().getDrawable(R.drawable.boton_redondo_primary));
        vistas.add(button);
        buttons.add(button);
        lyView.addView(button);

        return button;
    }

    public Button addButtonPrimary(String string) {

        Button button = new Button(getContext());
        button.setText(string);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(getResources().getColor(R.color.colorPrimary));
        button.setBackground(getResources().getDrawable(R.drawable.boton_redondo_primary));
        setLayoutParams(button);
        vistas.add(button);
        buttons.add(button);
        lyView.addView(button);

        return button;
    }

    public Button addButtonSecondary(int recursoString) {

        Button button = new Button(getContext());
        button.setText(recursoString);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
        button.setBackground(getResources().getDrawable(R.drawable.boton_redondo_secondary));
        setLayoutParams(button);
        vistas.add(button);
        buttons.add(button);
        lyView.addView(button);

        return button;
    }

    public Button addButtonSecondary(String string) {

        Button button = new Button(getContext());
        button.setText(string);
        button.setPadding(5, 5, 5, 5);
        button.setTextColor(getResources().getColor(R.color.colorSecondaryDark));
        button.setBackground(getResources().getDrawable(R.drawable.boton_redondo_secondary));
        setLayoutParams(button);
        vistas.add(button);
        buttons.add(button);
        lyView.addView(button);

        return button;
    }

    public ImageButton addImageButtonPrimary(int recurso) {

        ImageButton imageButton = new ImageButton(getContext());
        imageButton.setImageResource(recurso);
        imageButton.setPadding(5, 5, 5, 5);
        imageButton.setBackground(getResources().getDrawable(R.drawable.boton_redondo_primary));
        setLayoutParams(imageButton);
        vistas.add(imageButton);
        imageButtons.add(imageButton);
        lyView.addView(imageButton);

        return imageButton;
    }

    public ImageButton addImageButtonSecundary(int recurso) {

        ImageButton imageButton = new ImageButton(getContext());
        imageButton.setImageResource(recurso);
        imageButton.setPadding(5, 5, 5, 5);
        imageButton.setBackground(getResources().getDrawable(R.drawable.boton_redondo_secondary));
        setLayoutParams(imageButton);
        vistas.add(imageButton);
        imageButtons.add(imageButton);
        lyView.addView(imageButton);

        return imageButton;
    }


    public void setWeigthButton(int position, int weigth) {

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, weigth);
        buttons.get(position).setLayoutParams(params);
    }

    public void setWeigthButton(int position) {

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, 1);
        buttons.get(position).setLayoutParams(params);
    }

    public void setWeigthImageButton(int position, int weigth) {

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, weigth);
        imageButtons.get(position).setLayoutParams(params);
    }

    public void setWeigthImageButton(int position) {

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, 1);
        imageButtons.get(position).setLayoutParams(params);
    }


    public void addVista(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, View vista, int layout) {

        vista = inflater.inflate(layout, container, false);
        if (vista.getParent() != null) {
            ((ViewGroup) vista.getParent()).removeView(vista); // <- fix
        }
        if (vista != null) {
            this.addView(vista);
        }
    }

    private void setLayoutParams(View view) {

        LinearLayoutCompat.LayoutParams params;
        if (lyView.getOrientation() == VERTICAL) {
            params = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, 1);
        } else {
            params = new LinearLayoutCompat.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT, 1);

        }
        params.setMargins(5, 5, 5, 5);
        view.setLayoutParams(params);
    }

}
