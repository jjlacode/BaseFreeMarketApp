package jjlacode.com.androidutils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

public abstract class FragmentBaseCRUD extends FragmentBase implements JavaUtil.Constantes {

    protected String namef;
    protected String namefsub;
    protected String tabla;
    protected String[] campos;
    protected String id;
    protected int secuencia;
    protected String tablaCab;
    protected Context contexto;
    protected Modelo modelo;


    public FragmentBaseCRUD() {
    }

    /**
     * Carga el Bundle de entrada de la transisión entre Fragments ó Activity
     */
    protected void cargarBundle(){

        bundle = getArguments();

        if (bundle != null) {
            namef = bundle.getString(NAMEF);
            namefsub = bundle.getString(NAMESUB);
            modelo = (Modelo) bundle.getSerializable(MODELO);
            secuencia = bundle.getInt(SECUENCIA);

        }
    }


    protected abstract void setTabla();

    protected abstract void setTablaCab();

    protected abstract void setContext();

    protected abstract void setCampos();

    protected abstract void setBundle();

    protected abstract void setDatos();


    protected void enviarAct(){

        enviarBundle();
        icFragmentos.enviarBundleAActivity(bundle);
    }

    protected void enviarBundle(){

        bundle = new Bundle();
        bundle.putString(NAMEF,namef);
        bundle.putSerializable(MODELO,modelo);
        bundle.putInt(SECUENCIA,secuencia);

    }

    @Override
    public void onResume() {

        cargarBundle();
        setBundle();
        enviarAct();

        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setTabla();
        setTablaCab();
        setCampos();
        setContext();
        cargarBundle();
        setBundle();
        enviarAct();

        super.onCreate(savedInstanceState);
    }

}
