package com.codevsolution.base.util.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.codevsolution.base.util.sqlite.ContratoPry;
import com.codevsolution.freemarketsapp.logica.Interactor;

public abstract class ReceiverBase extends BroadcastReceiver implements Interactor.ConstantesPry, ContratoPry.Tablas {

    // Sin instancias

    public ReceiverBase(Messenger messenger) {
        this.messenger = messenger;
    }

    public ReceiverBase() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {



        onReceiver(context, intent);
    }

    public abstract void onReceiver(Context context, Intent intent);

    public interface Messenger {
        void alRecibirMsg();
    }

    protected Messenger messenger;

    public void setMessenger(Messenger messenger) {
        this.messenger = messenger;
    }


}