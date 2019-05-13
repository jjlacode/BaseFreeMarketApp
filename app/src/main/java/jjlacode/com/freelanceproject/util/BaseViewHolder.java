package jjlacode.com.freelanceproject.util;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


public class BaseViewHolder extends RecyclerView.ViewHolder {



        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(Modelo modelo){

        }

        public Context getContext(){
            return itemView.getContext();

        }
}
