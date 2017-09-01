package com.rdm.notificacompromisso.model;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Robson Da Motta on 26/07/2017.
 */

public class Compromisso implements Parcelable {

    public static final String COMPROMISSO_TAG = "compromisso";
    private long identificador;
    private String titulo;
    private String descricao;
    private String autor;
    private int hora;
    private int minuto;
    private int dia;
    private int mes;
    private int ano;
    private int cancelado;

    public long getIdentificador() {
        return identificador;
    }

    public void setIdentificador(long identificador) {
        this.identificador = identificador;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getCancelado() {
        return cancelado;
    }

    public void setCancelado(int cancelado) {
        this.cancelado = cancelado;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.identificador);
        dest.writeString(this.titulo);
        dest.writeString(this.descricao);
        dest.writeString(this.autor);
        dest.writeInt(this.hora);
        dest.writeInt(this.minuto);
        dest.writeInt(this.dia);
        dest.writeInt(this.mes);
        dest.writeInt(this.ano);
        dest.writeInt(this.cancelado);
    }

    public Compromisso() {
    }

    protected Compromisso(Parcel in) {
        this.identificador = in.readLong();
        this.titulo = in.readString();
        this.descricao = in.readString();
        this.autor = in.readString();
        this.hora = in.readInt();
        this.minuto = in.readInt();
        this.dia = in.readInt();
        this.mes = in.readInt();
        this.ano = in.readInt();
        this.cancelado = in.readInt();
    }

    public static final Creator<Compromisso> CREATOR = new Creator<Compromisso>() {
        @Override
        public Compromisso createFromParcel(Parcel source) {
            return new Compromisso(source);
        }

        @Override
        public Compromisso[] newArray(int size) {
            return new Compromisso[size];
        }
    };
}
