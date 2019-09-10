package com.codevsolution.freemarketsapp.ui;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.codevsolution.base.util.chat.FragmentChatBase;
import com.codevsolution.freemarketsapp.R;
import com.codevsolution.freemarketsapp.logica.Interactor;

public class FragmentChat extends FragmentChatBase implements Interactor.ConstantesPry {

    private ImageButton btnClienteWeb;
    private ImageButton btnFreelance;
    private ImageButton btnComercial;
    private ImageButton btnEcommerce;
    private ImageButton btnLugar;
    private ImageButton btnEmpresa;
    private ImageButton btnProveedorWeb;

    @Override
    protected void setLayout() {

        super.setLayout();
        layoutCabecera = R.layout.cabecera_fragment_chat;

    }

    @Override
    protected void setBundle() {
        super.setBundle();

        if (id != null) {

            if (tipoChatOrigen.equals(CLIENTEWEB)) {
                btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            } else if (tipoChatOrigen.equals(FREELANCE)) {
                btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            } else if (tipoChatOrigen.equals(COMERCIAL)) {
                btnComercial.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            } else if (tipoChatOrigen.equals(ECOMMERCE)) {
                    btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            } else if (tipoChatOrigen.equals(LUGAR)) {
                    btnLugar.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            } else if (tipoChatOrigen.equals(EMPRESA)) {
                btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            } else if (tipoChatOrigen.equals(PROVEEDORWEB)) {
                    btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            }

        }

    }


    @Override
    protected void setDatos() {

        super.setDatos();

        if (tipoChatRetorno != null && !tipoChatRetorno.equals(NULL) && nombreChat != null) {

            actuar.setText("Chatear como : " + tipoChatRetorno);

            if (tipoChatRetorno.equals(CLIENTEWEB)) {
                btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            } else if (tipoChatRetorno.equals(FREELANCE)) {
                btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorSecondary));

            } else if (tipoChatRetorno.equals(COMERCIAL)) {
                btnComercial.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            } else if (tipoChatRetorno.equals(ECOMMERCE)) {
                btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            } else if (tipoChatRetorno.equals(LUGAR)) {
                btnLugar.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            } else if (tipoChatRetorno.equals(EMPRESA)) {
                btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            } else if (tipoChatRetorno.equals(PROVEEDORWEB)) {
                btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
            }

        } else {

            Toast.makeText(contexto, "Debe tener un nombre en el perfil de este tipo", Toast.LENGTH_SHORT).show();

        }

    }

    protected void selectorChat(final String tipoChat) {

        super.selectorChat(tipoChat);

        btnClienteWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tipoChat.equals(ORIGEN)) {
                    tipoChatOrigen = CLIENTEWEB;
                    selector();
                    btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnLugar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnComercial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                } else {
                    tipoChatRetornoOld = tipoChatRetorno;
                    tipoChatRetorno = CLIENTEWEB;
                    getNombreChat();

                }

            }
        });

        btnFreelance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tipoChat.equals(ORIGEN)) {
                    tipoChatOrigen = FREELANCE;
                    selector();
                    btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnLugar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnComercial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                } else {
                    tipoChatRetornoOld = tipoChatRetorno;
                    tipoChatRetorno = FREELANCE;
                    getNombreChat();
                }
            }
        });

        btnEcommerce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tipoChat.equals(ORIGEN)) {
                    tipoChatOrigen = ECOMMERCE;
                    selector();
                    btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    btnLugar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnComercial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                } else {
                    tipoChatRetornoOld = tipoChatRetorno;
                    tipoChatRetorno = ECOMMERCE;
                    getNombreChat();
                }
            }
        });

        btnLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean valido;
                if (tipoChat.equals(ORIGEN)) {
                    tipoChatOrigen = LUGAR;
                    selector();
                    btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnLugar.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnComercial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                } else {
                    tipoChatRetornoOld = tipoChatRetorno;
                    tipoChatRetornoOld = tipoChatRetorno;
                    tipoChatRetorno = LUGAR;
                    getNombreChat();
                }

            }
        });

        btnProveedorWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tipoChat.equals(ORIGEN)) {
                    tipoChatOrigen = PROVEEDORWEB;
                    selector();
                    btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnLugar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                    btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnComercial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tipoChatRetornoOld = tipoChatRetorno;
                    tipoChatRetorno = PROVEEDORWEB;
                    getNombreChat();
                }

            }
        });

        btnComercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tipoChat.equals(ORIGEN)) {
                    tipoChatOrigen = COMERCIAL;
                    selector();
                    btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnLugar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnComercial.setBackgroundColor(getResources().getColor(R.color.colorSecondary));

                } else {
                    tipoChatRetornoOld = tipoChatRetorno;
                    tipoChatRetorno = COMERCIAL;
                    getNombreChat();

                }

            }
        });

        btnEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tipoChat.equals(ORIGEN)) {
                    tipoChatOrigen = EMPRESA;
                    selector();
                    btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnLugar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnComercial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
                } else {
                    tipoChatRetornoOld = tipoChatRetorno;
                    tipoChatRetorno = EMPRESA;
                    getNombreChat();
                }

            }
        });


    }

    protected void cambiarTipoChatRetorno() {

        super.cambiarTipoChatRetorno();

        btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnLugar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnComercial.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        if (tipoChatRetorno.equals(CLIENTEWEB)) {
            btnClienteWeb.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        } else if (tipoChatRetorno.equals(FREELANCE)) {
            btnFreelance.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        } else if (tipoChatRetorno.equals(COMERCIAL)) {
            btnComercial.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        } else if (tipoChatRetorno.equals(ECOMMERCE)) {
            btnEcommerce.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        } else if (tipoChatRetorno.equals(LUGAR)) {
            btnLugar.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        } else if (tipoChatRetorno.equals(EMPRESA)) {
            btnEmpresa.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        } else if (tipoChatRetorno.equals(PROVEEDORWEB)) {
            btnProveedorWeb.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        }


    }


    @Override
    protected void setInicio() {

        super.setInicio();

        btnClienteWeb = view.findViewById(R.id.btn_cab_chat_clienteweb);
        btnFreelance = view.findViewById(R.id.btn_cab_chat_freelance);
        btnEcommerce = view.findViewById(R.id.btn_cab_chat_ecommerce);
        btnLugar = view.findViewById(R.id.btn_cab_chat_lugar);
        btnProveedorWeb = view.findViewById(R.id.btn_cab_chat_proveedorweb);
        btnComercial = view.findViewById(R.id.btn_cab_chat_comercial);
        btnEmpresa = view.findViewById(R.id.btn_cab_chat_empresa);

    }

}
