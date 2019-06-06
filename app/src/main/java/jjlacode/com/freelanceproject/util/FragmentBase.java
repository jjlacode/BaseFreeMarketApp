package jjlacode.com.freelanceproject.util;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import jjlacode.com.freelanceproject.R;

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
    protected float sizef;
    protected int altoimg;
    protected int padimg;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        setLayout();
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        land = getResources().getBoolean(R.bool.esLand);
        tablet = getResources().getBoolean(R.bool.esTablet);
        int ancho = metrics.widthPixels;
        if (!land){
            altoimg = (int) ((double)ancho/3);
            padimg = (int) ((double)ancho/10);
            sizef = (float) ((double)ancho/100);
        }else {
            altoimg = (int) ((double)ancho/6);
            padimg = (int) ((double)ancho/20);
            sizef = (float) ((double)ancho/100);
        }

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(layout, container, false);

        System.out.println("land = " + land);
        System.out.println("tablet = " + tablet);
        System.out.println("sizef = " + sizef);
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
     * Asigna el recurso al control tipo TextView en la vista actual
     * @param recurso Recurso del la vista actual que se asigna al TextView
     */
    protected TextView setTextView(int recurso){

        return view.findViewById(recurso);
    }

    protected EditText setEditText(int recurso){

        return view.findViewById(recurso);
    }

    protected Button setButton(int recurso){

        return view.findViewById(recurso);
    }

    protected ImageView setImageView(int recurso){

        return view.findViewById(recurso);
    }

    protected ImageButton setImageButton(int recurso){

        return view.findViewById(recurso);
    }

    protected AutoCompleteTextView setAutoCompleteTextView(int recurso){

        return view.findViewById(recurso);
    }

    protected ProgressBar setProgressBar(int recurso){

        return view.findViewById(recurso);
    }

    protected CheckBox setCheckBox(int recurso){

        return view.findViewById(recurso);
    }

    protected RecyclerView setRecyclerView(int recurso){

        return view.findViewById(recurso);
    }

    protected CardView setCardView(int recurso){

        return view.findViewById(recurso);
    }


    /**
     * Asigna el recurso al control tipo Spinner en la vista actual
     * @param recurso Recurso del la vista actual que se asigna al TextView
     */
    protected Spinner setSpinner(int recurso){

        return view.findViewById(recurso);
    }



}
