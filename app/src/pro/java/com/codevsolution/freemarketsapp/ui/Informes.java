package com.codevsolution.freemarketsapp.ui;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.codevsolution.base.adapter.BaseViewHolder;
import com.codevsolution.base.adapter.ListaAdaptadorFiltroModelo;
import com.codevsolution.base.adapter.TipoViewHolder;
import com.codevsolution.base.android.controls.EditMaterial;
import com.codevsolution.base.javautil.JavaUtil;
import com.codevsolution.base.models.ListaModelo;
import com.codevsolution.base.models.ModeloSQL;
import com.codevsolution.base.time.Day;
import com.codevsolution.base.time.ListaDays;
import com.codevsolution.base.time.TimeDateUtil;
import com.codevsolution.base.time.calendar.fragments.FragmentMes;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import java.util.ArrayList;

public class Informes extends FragmentMes implements Interactor.TiposEstados {

    private CheckBox chTodos;
    private CheckBox chClientes;
    private CheckBox chProyectos;

    @Override
    protected ListaModelo setListaDia(long fecha) {

        ListaModelo listaDia = new ListaModelo();
        ListaModelo listaClientes = new ListaModelo(CAMPOS_CLIENTE);
        listabase = new ListaModelo(CAMPOS_PROYECTO);
        for (ModeloSQL cliente : listaClientes.getLista()) {
            listabase.addModelo(cliente);
        }
        for (ModeloSQL modeloSQL : listabase.getLista()) {


            if (TimeDateUtil.getDateString(modeloSQL.getLong(CAMPO_CREATEREG)).
                    equals(TimeDateUtil.getDateString(fecha))){

                listaDia.addModelo(modeloSQL);
            }
        }
        tipoRV = LISTA;
        return listaDia;
    }

    @Override
    protected void setVerDia(long fecha, ListaModelo listaModelo) {


    }

    @Override
    protected void setLayoutItem() {

        layoutOpciones = R.layout.opciones_informes;
        layoutItem = R.layout.item_list_informes;

    }

    @Override
    protected void setCampos() {

        campos = null;
        campo = CAMPO_CREATEREG;

    }

    @Override
    protected void setTitulo() {

                titulo = R.string.informes;

    }

    @Override
    protected void setOnDayClick(Day day, int position) {

    }

    @Override
    protected void setOnDayLongClick(Day day, int position) {

    }

    @Override
    protected void setNuevo(long fecha) {


    }

    @Override
    protected void setVerLista(ListaModelo listaModelo, ListaDays listaDays) {


    }

    @Override
    protected void setOnPrevMonth() {

    }

    @Override
    protected void setOnNextMonth() {

    }

    @Override
    protected void setOnInicio() {

        setSinBusqueda(true);
        setSinLista(true);
        setSinNuevo(true);
        setSinDia(true);

        chTodos = (CheckBox) ctrl(R.id.chtodos_informes);
        chClientes = (CheckBox) ctrl(R.id.chcli_informes);
        chProyectos = (CheckBox) ctrl(R.id.chpry_informes);

        chTodos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    chClientes.setChecked(true);
                    chProyectos.setChecked(true);
                }
                setRV();
            }
        });

        chClientes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                setRV();
            }
        });

        chProyectos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                setRV();
            }
        });
    }

    @Override
    protected TipoViewHolder setViewHolder(View view) {
        return new ViewHolderRV(view);
    }

    @Override
    protected ListaAdaptadorFiltroModelo setAdaptadorAuto(Context context, int layoutItem, ArrayList<ModeloSQL> lista, String[] campos) {
        return new ListaAdaptadorFiltroModelo(context, layoutItem, lista, campos);
    }


    public class ViewHolderRV extends BaseViewHolder implements TipoViewHolder {

        ArrayList<View> vistasHolder = new ArrayList<>();
        ArrayList<EditMaterial> controlesHolder = new ArrayList<>();
        ArrayList<Integer> recursosHolder = new ArrayList<>();
        EditMaterial impTot, tiempoTot, totProsp, totCli, totCliIn;


        public ViewHolderRV(View itemView) {
            super(itemView);

            impTot = (EditMaterial) ctrl(itemView,R.id.ipmtot_informes,vistasHolder,controlesHolder,recursosHolder);
            tiempoTot = (EditMaterial) ctrl(itemView,R.id.tiempo_tot_informes,vistasHolder,controlesHolder,recursosHolder);
            totProsp = (EditMaterial) ctrl(itemView,R.id.tot_prosp_informes,vistasHolder,controlesHolder,recursosHolder);
            totCli = (EditMaterial) ctrl(itemView,R.id.tot_cli_informes,vistasHolder,controlesHolder,recursosHolder);
            totCliIn = (EditMaterial) ctrl(itemView,R.id.tot_cliin_informes,vistasHolder,controlesHolder,recursosHolder);

        }

        @Override
        public void bind(ArrayList<?> lista, int position) {
            System.out.println("lista modeloSQL = " + lista.size());


            super.bind(lista, position);
        }

        @Override
        public void bind(ModeloSQL modeloSQL) {

            System.out.println("modeloSQL = " + modeloSQL.getString(campo));

            super.bind(modeloSQL);
        }

        @Override
        public void bind(ListaModelo lista, int position) {

            double importeTotal = 0;
            double tiempoProy = 0;
            int prospectos = 0;
            int clientes = 0;
            int clientesInactivos = 0;
            boolean hayClientes = false;
            boolean hayProyectos = false;

            System.out.println("listaModelo = " + lista.getLista().size());


            for (ModeloSQL modeloSQL : lista.getLista()) {

                switch (modeloSQL.getNombreTabla()) {

                        case PROYECTO:
                            if (chProyectos.isChecked() || chTodos.isChecked()) {
                                hayProyectos = true;
                                visible(impTot);
                                visible(tiempoTot);
                                importeTotal += modeloSQL.getDouble(PROYECTO_IMPORTEFINAL);
                                tiempoProy += modeloSQL.getDouble(PROYECTO_TIEMPO);
                            }
                            break;
                        case CLIENTE:
                            if (chClientes.isChecked() || chTodos.isChecked()) {
                                hayClientes = true;
                                visible(totProsp);
                                visible(totCli);
                                visible(totCliIn);
                                prospectos++;
                                if (modeloSQL.getInt(CLIENTE_PESOTIPOCLI) > 0) {
                                    clientes++;
                                }
                                if (modeloSQL.getLong(CLIENTE_ACTIVO) > 0) {
                                    clientesInactivos++;
                                }
                            }
                    }

                    impTot.setText(JavaUtil.formatoMonedaLocal(importeTotal));
                    tiempoTot.setText(JavaUtil.getDecimales(tiempoProy));
                    totProsp.setText(String.valueOf(prospectos));
                    totCli.setText(String.valueOf(clientes));
                    totCliIn.setText(String.valueOf(clientesInactivos));
                }

            super.bind(lista,position);
        }

        @Override
        public BaseViewHolder holder(View view) {
            return new ViewHolderRV(view);
        }
    }



}
