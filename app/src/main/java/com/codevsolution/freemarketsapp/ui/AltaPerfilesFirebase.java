package com.codevsolution.freemarketsapp.ui;

import android.content.ContentValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.codevsolution.base.android.controls.ViewGroupLayout;
import com.codevsolution.base.crud.CRUDutil;
import com.codevsolution.base.nosql.FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb;
import com.codevsolution.base.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.CLIENTE;
import static com.codevsolution.freemarketsapp.logica.Interactor.ConstantesPry.PROSPECTO;

public abstract class AltaPerfilesFirebase extends FragmentMasterDetailNoSQLFormBaseFirebaseRatingWeb implements ContratoPry.Tablas {

    protected Button prospectos;
    protected Button clientes;
    protected Button proveedores;

    @Override
    protected void setOnCreateView(View view, LayoutInflater inflater, ViewGroup container) {
        location = true;
        super.setOnCreateView(view, inflater, container);
        frdetalleExtrasante.setOrientation(LinearLayoutCompat.VERTICAL);
        ViewGroupLayout vistaBtns = new ViewGroupLayout(contexto, frdetalleExtrasante);
        vistaBtns.setOrientacion(ViewGroupLayout.ORI_LL_HORIZONTAL);
        prospectos = vistaBtns.addButtonSecondary(R.string.importar_a_prospectos, 1);
        prospectos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                importarCliente(PROSPECTO);
            }
        });
        clientes = vistaBtns.addButtonSecondary(R.string.importar_a_clientes, 1);
        clientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                importarCliente(CLIENTE);
            }
        });
        proveedores = vistaBtns.addButtonSecondary(R.string.importar_a_clientes, 1);
        proveedores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                importarCliente(CLIENTE);
            }
        });
    }

    @Override
    protected void selector() {
        super.selector();
        activityBase.fabInicio.hide();

    }

    protected void importarCliente(String tipoCliente) {

        ContentValues valores = new ContentValues();
        valores.put(CLIENTE_ID_FIRE, id);
        valores.put(CLIENTE_NOMBRE, nombre.getTexto());
        valores.put(CLIENTE_DIRECCION, direccion.getTexto());
        valores.put(CLIENTE_EMAIL, email.getTexto());
        valores.put(CLIENTE_TELEFONO, telefono.getTexto());
        valores.put(CLIENTE_WEB, etWeb.getTexto());
        valores.put(CLIENTE_CONTACTO, nombre.getTexto());
        valores.put(CLIENTE_TIPOFIRE, tipo);
        valores.put(CLIENTE_ID_TIPOCLIENTE, Interactor.getIdTipoCliente(tipoCliente));
        valores.put(CLIENTE_PESOTIPOCLI, Interactor.getPesoTipoCliente(tipoCliente));

        if (CRUDutil.crearRegistro(TABLA_CLIENTE, valores) != null) {
            Toast.makeText(contexto, tipoCliente + " importado con exito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(contexto, "Error al importar el " + tipoCliente, Toast.LENGTH_SHORT).show();

        }
    }

    protected void importarProveedor() {

        ContentValues valores = new ContentValues();
        valores.put(PROVEEDOR_ID_FIRE, id);
        valores.put(PROVEEDOR_NOMBRE, nombre.getTexto());
        valores.put(PROVEEDOR_DIRECCION, direccion.getTexto());
        valores.put(PROVEEDOR_EMAIL, email.getTexto());
        valores.put(PROVEEDOR_TELEFONO, telefono.getTexto());
        valores.put(PROVEEDOR_WEB, etWeb.getTexto());
        valores.put(PROVEEDOR_CONTACTO, nombre.getTexto());

        if (CRUDutil.crearRegistro(TABLA_PROVEEDOR, valores) != null) {
            Toast.makeText(contexto, "Proveedor importado con exito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(contexto, "Error al importar el proveedor", Toast.LENGTH_SHORT).show();

        }
    }

}
