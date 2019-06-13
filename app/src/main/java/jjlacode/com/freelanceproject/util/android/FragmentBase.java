package jjlacode.com.freelanceproject.util.android;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.R;
import jjlacode.com.freelanceproject.util.android.controls.EditMaterial;

public abstract class FragmentBase extends Fragment {

    protected final String TAG = getClass().getName();
    protected View view;
    protected int layout;
    protected MainActivityBase activityBase;
    protected ICFragmentos icFragmentos;
    protected Bundle bundle;
    protected boolean land;
    protected boolean tablet;
    protected DisplayMetrics metrics;

    protected int ancho;
    protected int alto;
    protected boolean multiPanel;
    protected ArrayList<View> vistas;
    protected ArrayList<EditMaterial> materialEdits;
    protected ArrayList<Integer> recursos;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setLayout();
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        multiPanel = esMultiPanel(metrics);

        land = getResources().getBoolean(R.bool.esLand) && multiPanel;
        tablet = getResources().getBoolean(R.bool.esTablet);
        ancho = metrics.widthPixels;
        alto = metrics.heightPixels;

        materialEdits = new ArrayList<>();
        vistas = new ArrayList<>();
        recursos = new ArrayList<>();

        super.onCreate(savedInstanceState);
    }

    public boolean esMultiPanel(DisplayMetrics metrics) {
        // Determinar que siempre sera multipanel
        return ((float)metrics.densityDpi / (float)metrics.widthPixels) < 0.30;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(layout, container, false);

        System.out.println("land = " + land);
        System.out.println("tablet = " + tablet);
        setInicio();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivityBase) {
            this.activityBase = (MainActivityBase) context;
            icFragmentos = this.activityBase;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        icFragmentos = null;
    }

    protected void cargarBundle(){

        bundle = getArguments();
        System.out.println("bundle getArguments = " + bundle);
    }


    protected abstract void setLayout();

    protected abstract void setInicio();

    /**
     * Asigna el recurso al control en la vista actual
     * @param recurso Recurso del la vista actual que se asigna al Control
     */
    protected View ctrl(int recurso){

        View vista = view.findViewById(recurso);
        vistas.add(vista);
        if (vista instanceof EditMaterial){
            materialEdits.add((EditMaterial) vista);
        }
        recursos.add(recurso);
        return vista;

    }

    protected EditMaterial setEditMaterial(int recurso){

        EditMaterial vista = view.findViewById(recurso);
        materialEdits.add(vista);
        return vista;

    }

    protected void vaciarControles(){

        for (View vista : vistas) {
            if (vista instanceof EditText){
                ((EditText) vista).setText("");
            }else if (vista instanceof EditMaterial){
                ((EditMaterial) vista).setText("");
            }else if (vista instanceof CheckBox){
                ((CheckBox) vista).setChecked(false);
            }else if (vista instanceof ProgressBar){
                ((ProgressBar) vista).setProgress(0);
            }
        }

    }

    protected void vaciarEditMaterial(){

        for (EditMaterial vista : materialEdits) {
            vista.setText("");
        }
    }

    protected void setControl(TextView textView, int recurso){
        textView = view.findViewById(recurso);
    }

    protected void setControl(EditText editText, int recurso){
        editText = view.findViewById(recurso);
    }

    protected void setControl(EditMaterial editMaterial, int recurso){
        editMaterial = view.findViewById(recurso);
    }

    protected void setControl(Spinner spinner, int recurso){
        spinner = view.findViewById(recurso);
    }

    protected boolean setControl(Button button, int recurso){
        return button == view.findViewById(recurso);
    }

    protected void setControl(ImageButton imageButton, int recurso){
        imageButton = view.findViewById(recurso);
    }

    protected void setControl(ImageView imageView, int recurso){
        imageView = view.findViewById(recurso);
    }

    protected void setControl(CardView cardView, int recurso){
        cardView = view.findViewById(recurso);
    }

    protected void setControl(RecyclerView recyclerView, int recurso){
        recyclerView = view.findViewById(recurso);
    }

    protected void setControl(AutoCompleteTextView autoCompleteTextView, int recurso){
        autoCompleteTextView = view.findViewById(recurso);
    }

    protected void setControl(LinearLayout linearLayout, int recurso){
        linearLayout = view.findViewById(recurso);
    }

    protected void setControl(FrameLayout frameLayout, int recurso){
        frameLayout = view.findViewById(recurso);
    }

    protected void setControl(CoordinatorLayout coordinatorLayout, int recurso){
        coordinatorLayout = view.findViewById(recurso);
    }

    protected void setControl(TabLayout tabLayout, int recurso){
        tabLayout = view.findViewById(recurso);
    }

    protected void setControl(GridLayout gridLayout, int recurso){
        gridLayout = view.findViewById(recurso);
    }

    protected void setControl(GridView gridView, int recurso){
        gridView = view.findViewById(recurso);
    }

    protected void setControl(ProgressBar progressBar, int recurso){
        progressBar = view.findViewById(recurso);
    }

    protected void setControl(CheckBox checkBox, int recurso){
        checkBox = view.findViewById(recurso);
    }

    protected void setControl(RadioButton radioButton, int recurso){
        radioButton = view.findViewById(recurso);
    }

    protected void setControl(RadioGroup radioGroup, int recurso){
        radioGroup = view.findViewById(recurso);
    }

    protected void gone(View view){

        view.setVisibility(View.GONE);

    }

    protected void visible(View view){

        view.setVisibility(View.VISIBLE);

    }

    protected void allGone(){

        for (View vista : vistas) {

            vista.setVisibility(View.GONE);
        }
    }

    protected void allVisible(){

        for (View vista : vistas) {

            vista.setVisibility(View.VISIBLE);
        }
    }



}
