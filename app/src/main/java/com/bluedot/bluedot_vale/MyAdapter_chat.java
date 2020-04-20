package com.bluedot.bluedot_vale;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter_chat extends RecyclerView.Adapter<MyAdapter_chat.MensajeHolder>{

    private List<Mensaje> listmensajes;

    public MyAdapter_chat(List<Mensaje> listmensajes) {
        this.listmensajes = listmensajes;
    }

    @NonNull
    @Override
    public MensajeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensaje, parent, false);
        return new MensajeHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MensajeHolder holder, int position) {
        holder.tvmensaje.setText(listmensajes.get(position).getMensaje());
        holder.tvname.setText(listmensajes.get(position).getNombre());

    }

    @Override
    public int getItemCount() {
        return listmensajes.size();
    }

    class MensajeHolder extends RecyclerView.ViewHolder{
        private TextView tvname;
        private TextView tvmensaje;

        public MensajeHolder(@NonNull View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.nombreusuariochat);
            tvmensaje = itemView.findViewById(R.id.mensajedeluserchat);
        }
    }
}