package jjlacode.com.freelanceproject.model;

import java.io.Serializable;

public class Rating implements Serializable {

    private float valor;
    private String tipo;
    private String idRating;
    private String idUser;
    private String comentario;

    public Rating() {
    }

    public Rating(float valor, String tipo, String idRating, String idUser, String comentario) {
        this.valor = valor;
        this.tipo = tipo;
        this.idRating = idRating;
        this.idUser = idUser;
        this.comentario = comentario;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getIdRating() {
        return idRating;
    }

    public void setIdRating(String idRating) {
        this.idRating = idRating;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
