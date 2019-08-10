package jjlacode.com.freelanceproject.util.Models;

public class Contactos {

    private String tipo;
    private String datos;
    private String numero;

    public Contactos(String tipo, String datos, String numero) {
        this.tipo = tipo;
        this.datos = datos;
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
