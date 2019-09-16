package com.codevsolution.base.time;

import java.util.Timer;
import java.util.TimerTask;

public class Contador {

    private Timer timer = new Timer();
    private int ticks = 0;
    Listener listener;
    private boolean pausado;

    //Clase interna que funciona como contador
    class Cuenta extends TimerTask {

        public void run() {
            ticks++;
            listener.onTick(ticks);
        }
    }

    public void start(int periodo) {
        if (!pausado) {
            this.ticks = 0;
        }
        timer = new Timer();
        timer.schedule(new Cuenta(), 0, periodo);
    }

    public void start() {
        if (!pausado) {
            this.ticks = 0;
        }
        timer = new Timer();
        timer.schedule(new Cuenta(), 0, 1000);
    }

    public void stop() {
        timer.cancel();
        pausado = false;
    }

    public void pause() {
        timer.cancel();
        pausado = true;
    }

    //Metodo que retorna los ticks transcurridos
    public int getTicks() {
        return this.ticks;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {

        void onTick(int ticks);
    }

}
