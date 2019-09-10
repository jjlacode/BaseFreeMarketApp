package com.jjlacode.base.util.chat;

import android.content.ContentValues;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjlacode.base.util.adapter.RVAdapter;
import com.jjlacode.base.util.crud.CRUDutil;
import com.jjlacode.base.util.models.Modelo;
import com.jjlacode.base.util.models.MsgChat;
import com.jjlacode.base.util.sqlite.ContratoSystem;
import com.jjlacode.base.util.time.TimeDateUtil;
import com.jjlacode.freelanceproject.R;
import com.jjlacode.freelanceproject.logica.Interactor;

import java.util.ArrayList;


public class FragmentChatProductoEnviar extends FragmentChatBase implements
        ContratoSystem.Tablas, Interactor.ConstantesPry {

    private Switch btnClienteWeb;
    private Switch btnFreelance;
    private Switch btnComercial;
    private Switch btnEcommerce;
    private Switch btnLugar;
    private Switch btnEmpresa;
    private Switch btnProveedorWeb;
    private ArrayList<String> listaUsers;
    private ArrayList<String> listaTiposUser;
    private String idchatsp;

    @Override
    protected void setLayout() {
        super.setLayout();

        layoutCabecera = R.layout.cabecera_fragment_chat_producto;

    }

    @Override
    protected void setBundle() {
        super.setBundle();

        if (nn(bundle)) {
            idchatsp = getStringBundle(IDCHATF, NULL);
        }

        comprobarListaUsers();

    }

    @Override
    protected void acciones() {
        super.acciones();

        btnClienteWeb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {

                comprobarUsersPorTipo(CLIENTEWEB, b);

            }
        });

        btnFreelance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {

                comprobarUsersPorTipo(FREELANCE, b);

            }
        });

        btnComercial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {

                comprobarUsersPorTipo(COMERCIAL, b);

            }
        });

        btnEcommerce.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {

                comprobarUsersPorTipo(ECOMMERCE, b);

            }
        });

        btnEmpresa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {

                comprobarUsersPorTipo(EMPRESA, b);

            }
        });

        btnLugar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {

                comprobarUsersPorTipo(LUGAR, b);

            }
        });

        btnProveedorWeb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {

                comprobarUsersPorTipo(PROVEEDORWEB, b);
            }
        });
    }

    @Override
    protected void setInicio() {

        super.setInicio();

        btnClienteWeb = view.findViewById(R.id.btn_cab_chat_clienteweb_producto);
        btnFreelance = view.findViewById(R.id.btn_cab_chat_freelance_producto);
        btnEcommerce = view.findViewById(R.id.btn_cab_chat_ecommerce_producto);
        btnLugar = view.findViewById(R.id.btn_cab_chat_lugar_producto);
        btnProveedorWeb = view.findViewById(R.id.btn_cab_chat_proveedorweb_producto);
        btnComercial = view.findViewById(R.id.btn_cab_chat_comercial_producto);
        btnEmpresa = view.findViewById(R.id.btn_cab_chat_empresa_producto);

        gone(btnsave);
        gone(btndelete);
        listaUsers = new ArrayList<>();
        listaTiposUser = new ArrayList<>();

    }

    protected void comprobarListaUsers() {

        comprobarUsersPorTipo(FREELANCE, btnFreelance.isChecked());
        comprobarUsersPorTipo(CLIENTEWEB, btnClienteWeb.isChecked());
        comprobarUsersPorTipo(COMERCIAL, btnComercial.isChecked());
        comprobarUsersPorTipo(ECOMMERCE, btnEcommerce.isChecked());
        comprobarUsersPorTipo(EMPRESA, btnEmpresa.isChecked());
        comprobarUsersPorTipo(LUGAR, btnLugar.isChecked());
        comprobarUsersPorTipo(PROVEEDORWEB, btnProveedorWeb.isChecked());


    }

    protected void comprobarUsersPorTipo(final String tipo, final boolean b) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Query querydb = db.child(SUSCRIPCIONES).child(idchat).child(tipo);

        querydb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    System.out.println("suscriptor " + tipo + " " + child.getKey());
                    if (b) {
                        listaUsers.add(child.getKey());
                    } else {
                        listaUsers.remove(child.getKey());
                    }
                }
                if (listaUsers.size() > 0) {
                    btnEnviar.setEnabled(true);
                } else {
                    btnEnviar.setEnabled(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (b) {
            listaTiposUser.add(tipo);
        } else {
            listaTiposUser.remove(tipo);
        }
    }

    @Override
    protected void enviarMensaje() {

        if (msgEnv.getText() != null && !msgEnv.getText().toString().equals("")) {

            valores = new ContentValues();
            valores.put(DETCHAT_MENSAJE, msgEnv.getText().toString());
            valores.put(DETCHAT_TIPO, ENVIADO);
            valores.put(DETCHAT_FECHA, TimeDateUtil.ahora());
            valores.put(DETCHAT_CREATE, TimeDateUtil.ahora());
            valores.put(DETCHAT_TIMESTAMP, TimeDateUtil.ahora());
            valores.put(DETCHAT_NOTIFICADO, 1);
            valores.put(DETCHAT_ID_CHAT, id);
            secuencia = CRUDutil.crearRegistroSec(CAMPOS_DETCHAT, id, TABLA_CHAT, valores);

            Modelo chat = CRUDutil.setModelo(campos, id);

            MsgChat msgChat = new MsgChat();
            msgChat.setMensaje(msgEnv.getText().toString());
            msgChat.setNombre(nombreChat);
            msgChat.setFecha(TimeDateUtil.ahora());
            msgChat.setIdDestino(chat.getString(CHAT_USUARIO));
            msgChat.setIdOrigen(idchatsp);
            msgChat.setTipo(tipoChatRetorno);
            msgChat.setTipoRetorno(tipoChatOrigen);

            for (String user : listaUsers) {
                FirebaseDatabase.getInstance().getReference().child(CHAT).child(user).push().setValue(msgChat);
            }
            StringBuilder msg = new StringBuilder();
            msg.append(msgEnv.getText().toString());
            msg.append("\n");
            msg.append(getString(R.string.enviado_a));
            msg.append("\n");
            for (String s : listaTiposUser) {

                msg.append(s);
                msg.append(", ");
            }

            msg.append(getString(R.string.mensajes_enviados));
            msg.append(" = ");
            msg.append(listaUsers.size());

            valores = new ContentValues();
            valores.put(DETCHAT_MENSAJE, msg.toString());
            CRUDutil.actualizarRegistro(TABLA_DETCHAT, id, secuencia, valores);
            msgEnv.setText("");
            listaMsgChat = CRUDutil.setListaModeloDetalle(CAMPOS_DETCHAT, id, TABLA_CHAT, null, DETCHAT_FECHA + " DESC");
            RVAdapter adaptadorDetChat = new RVAdapter(new ViewHolderRVMsgChat(view), listaMsgChat.getLista(), R.layout.item_list_msgchat_base);
            rvMsgChat.setAdapter(adaptadorDetChat);

        }
    }

}
