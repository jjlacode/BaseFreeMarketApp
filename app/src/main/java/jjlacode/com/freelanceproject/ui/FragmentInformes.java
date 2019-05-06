package jjlacode.com.freelanceproject.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import jjlacode.com.freelanceproject.util.FragmentBase;
import jjlacode.com.freelanceproject.util.Modelo;
import jjlacode.com.freelanceproject.R;


public class FragmentInformes extends FragmentBase {

    RecyclerView rv;

    public FragmentInformes() {
        // Required empty public constructor
    }


    @Override
    protected void setLayout() {

        layout = R.layout.fragment_informes;
    }

    @Override
    protected void setInicio() {


    }

    public class AdaptadorRV extends RecyclerView.Adapter<AdaptadorRV.ViewHolder>
            implements View.OnClickListener {

        protected ArrayList<Modelo> list;
        private View.OnClickListener listener;
        private String namef;
        private int layout;


        public AdaptadorRV(ArrayList<Modelo> list, int layout, String namef) {

            this.list = list;
            this.namef = namef;
            this.layout = layout;
        }

        @NonNull
        @Override
        public AdaptadorRV.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

            final View view = LayoutInflater.from(parent.getContext()).inflate(layout, null, false);

            view.setOnClickListener(this);


            return new ViewHolder(view);

        }

        public void setOnClickListener(View.OnClickListener listener) {

            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorRV.ViewHolder viewHolder, int position) {

        }

        @Override
        public int getItemCount() {

            return list.size();
        }

        @Override
        public void onClick(View v) {

            if (listener != null) {

                listener.onClick(v);

            }

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            protected ViewHolder(@NonNull View itemView) {
                super(itemView);


            }

        }

    }

}
