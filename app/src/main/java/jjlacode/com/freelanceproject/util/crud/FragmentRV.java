package jjlacode.com.freelanceproject.util.crud;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import jjlacode.com.freelanceproject.sqlite.ConsultaBD;

public abstract class FragmentRV extends FragmentBaseCRUD {

    protected ConsultaBD consulta = new ConsultaBD();
    protected ArrayList<Modelo> lista;
    protected RecyclerView rv;

    @Override
    public void onResume() {
        super.onResume();

        if (tablaCab != null) {

            lista = consulta.queryListDetalle(campos, id, tablaCab);
        } else {

            lista = consulta.queryList(campos);

        }

        setDatos();

        setAcciones();

    }

    @Override
    protected void enviarBundle() {

        setCampoID();
        if(modelo!=null) {
            id = modelo.getString(campoID);
        }
        super.enviarBundle();
        setEnviarBundle();
    }


    protected abstract void setEnviarBundle();

    protected abstract void setCampoID();
}
