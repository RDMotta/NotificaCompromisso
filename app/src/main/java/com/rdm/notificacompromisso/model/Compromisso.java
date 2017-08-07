package com.rdm.notificacompromisso.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;
/**
 * Created by Robson Da Motta on 26/07/2017.
 */

public class Compromisso implements Parcelable {

    public static final String COMPROMISSO_TAG = "compromisso";
    private long identificador;
    private String descricao;
    private String autor;
    private Long hora;
    private Date data;

    public long getIdentificador() {
        return identificador;
    }

    public void setIdentificador(long identificador) {
        this.identificador = identificador;
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

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.identificador);
        dest.writeString(this.descricao);
        dest.writeString(this.autor);
        dest.writeValue(this.hora);
        dest.writeLong(this.data != null ? this.data.getTime() : -1);
    }

    public Compromisso() {
    }

    protected Compromisso(Parcel in) {
        this.identificador = in.readLong();
        this.descricao = in.readString();
        this.autor = in.readString();
        this.hora = (Long) in.readValue(Long.class.getClassLoader());
        long tmpData = in.readLong();
        this.data = tmpData == -1 ? null : new Date(tmpData);
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
