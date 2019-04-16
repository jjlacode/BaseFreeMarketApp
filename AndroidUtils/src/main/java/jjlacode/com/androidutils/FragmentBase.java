package jjlacode.com.androidutils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public abstract class FragmentBase extends Fragment implements JavaUtil.Constantes {

    protected String namef;
    protected String namefsub;
    protected AppCompatActivity activity;
    protected ICFragmentos icFragmentos;
    protected Bundle bundle;

    public FragmentBase() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
