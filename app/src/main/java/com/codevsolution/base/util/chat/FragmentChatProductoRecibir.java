package com.codevsolution.base.util.chat;

public class FragmentChatProductoRecibir extends FragmentChatBase {


    @Override
    protected void setBundle() {
        super.setBundle();

        if (id != null) {


        }

    }

    @Override
    protected void setDatos() {

        super.setDatos();


    }

    @Override
    protected void setInicio() {
        super.setInicio();

        gone(actuar);
        gone(lyEnvMsg);
        gone(frCabecera);
        gone(rv);
        gonePie();
    }
}
