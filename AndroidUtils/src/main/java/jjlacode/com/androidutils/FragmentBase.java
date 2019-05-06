package jjlacode.com.androidutils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public abstract class FragmentBase extends Fragment {

    protected final String TAG = getClass().getName();
    protected View view;
    protected int layout;
    protected AppCompatActivity activity;
    protected ICFragmentos icFragmentos;
    protected Bundle bundle;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        setLayout();

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(layout, container, false);

        setInicio();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.activity = (AppCompatActivity) context;
            icFragmentos = (ICFragmentos) this.activity;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        icFragmentos = null;
    }


    protected abstract void setLayout();

    protected abstract void setInicio();

    /**
     * Asigna el recurso al control tipo TextView en la vista actual
     * @param control Control TextView al que se le asigna el recurso
     * @param recurso Recurso del la vista actual que se asigna al TextView
     */
    protected void setRecurso(TextView control, int recurso){

        control = view.findViewById(recurso);
    }

    protected void setRecurso(EditText control, int recurso){

        control = view.findViewById(recurso);
    }

    protected void setRecurso(Button control, int recurso){

        control = view.findViewById(recurso);
    }

    protected void setRecurso(ImageView control, int recurso){

        control = view.findViewById(recurso);
    }

    protected void setRecurso(ImageButton control, int recurso){

        control = view.findViewById(recurso);
    }

    protected void setRecurso(AutoCompleteTextView control, int recurso){

        control = view.findViewById(recurso);
    }

    protected void setRecurso(ProgressBar control, int recurso){

        control = view.findViewById(recurso);
    }

    protected void setRecurso(CheckBox control, int recurso){

        control = view.findViewById(recurso);
    }

    protected void setRecurso(RecyclerView control, int recurso){

        control = view.findViewById(recurso);
    }

    protected void setRecurso(CardView control, int recurso){

        control = view.findViewById(recurso);
    }

    /**
     * Asigna el recurso al control tipo Spinner en la vista actual
     * @param control Control Spinner al que se le asigna el recurso
     * @param recurso Recurso del la vista actual que se asigna al TextView
     */
    protected void setRecurso(Spinner control, int recurso){

        control = view.findViewById(recurso);
    }



}
