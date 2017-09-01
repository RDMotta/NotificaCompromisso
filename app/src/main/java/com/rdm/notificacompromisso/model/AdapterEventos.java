package com.rdm.notificacompromisso.model;

import android.content.Context;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rdm.notificacompromisso.R;
import com.rdm.notificacompromisso.presenter.db.CompromissosProvider;

import java.util.ArrayList;

/**
 * Created by Robson Da Motta on 28/08/2017.
 */

public class AdapterEventos extends RecyclerView.Adapter<AdapterEventos.ViewHolder> {

    public View.OnClickListener onClickListener;
    private ArrayList<Compromisso> compromissos;
    private Context mContext;
    private LayoutInflater inflater;

    public AdapterEventos(Context context, ArrayList<Compromisso> compromissoArrayList) {
        this.mContext = context;
        this.compromissos = compromissoArrayList;
    }

    public AdapterEventos(Context context) {
        this.mContext = context;
        this.compromissos = new ArrayList<Compromisso>();
    }

    public void setEventos(ArrayList<Compromisso> compromissoArrayList) {
        this.compromissos = compromissoArrayList;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.onClickListener = listener;
    }

    @Override
    public AdapterEventos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mult_evento_item, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Compromisso compromisso = compromissos.get(position);
        holder.txtHora.setText(compromisso.getHora() + ":" + compromisso.getMinuto());
        holder.txtDescricao.setText(compromisso.getTitulo());
        holder.txtDescricao.setTag(compromisso);
        holder.fabInvalidarItem.setTag(compromisso);

        if (compromisso.getCancelado() == 1) {
            holder.fabInvalidarItem.setImageResource(R.drawable.ic_bell_off);
            holder.txtHora.setPaintFlags(holder.txtHora.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtDescricao.setPaintFlags(holder.txtDescricao.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.fabInvalidarItem.setImageResource(R.drawable.ic_bell);
        }

        final ViewHolder mHolder = holder;
        if (onClickListener != null) {
            holder.txtHora.setTag(compromisso);
            holder.txtInfoHora.setTag(compromisso);
            holder.txtDescricao.setTag(compromisso);
            holder.txtHora.setOnClickListener(onClickListener);
            holder.txtInfoHora.setOnClickListener(onClickListener);
            holder.txtDescricao.setOnClickListener(onClickListener);
        }

        holder.fabInvalidarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHolder.txtHora.setPaintFlags(mHolder.txtHora.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mHolder.txtDescricao.setPaintFlags(mHolder.txtDescricao.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mHolder.fabInvalidarItem.setImageResource(R.drawable.ic_bell_off);
                Compromisso compromisso = (Compromisso) view.getTag();
                compromisso.setCancelado(1);
                CompromissosProvider compromissosProvider = new CompromissosProvider();
                compromissosProvider.atualizarCompromisso(mContext, compromisso);
            }
        });
    }

    @Override
    public int getItemCount() {
        return compromissos.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtInfoHora;
        TextView txtHora;
        TextView txtDescricao;
        FloatingActionButton fabInvalidarItem;

        public ViewHolder(View viewRoot) {
            super(viewRoot);
            txtInfoHora = (TextView) viewRoot.findViewById(R.id.txt_itemhora);
            txtHora = (TextView) viewRoot.findViewById(R.id.txt_itemDesHora);
            txtDescricao = (TextView) viewRoot.findViewById(R.id.txt_itemDesc);
            fabInvalidarItem = (FloatingActionButton) viewRoot.findViewById(R.id.fabInvalidarItem);
        }
    }
}
