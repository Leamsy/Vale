package com.bluedot.bluedot_vale;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MyAdapter_chat extends RecyclerView.Adapter<MyAdapter_chat.MensajeHolder>{

    private List<Mensaje> listmensajes;
    boolean mStartPlaying = true;
    private MediaPlayer player = null;

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
    public void onBindViewHolder(@NonNull MensajeHolder holder, final int position) {
        if(listmensajes.get(position).getTipo().equals("texto")){
            holder.tvmensaje.setText(listmensajes.get(position).getMensaje());
            holder.tvname.setText(listmensajes.get(position).getNombre());
            holder.tvaudio.setVisibility(INVISIBLE);
        }
        else if(listmensajes.get(position).getTipo().equals("audio")){
            holder.tvmensaje.setText("AUDIO");
            holder.tvname.setText(listmensajes.get(position).getNombre());
            holder.tvaudio.setVisibility(VISIBLE);
            holder.tvaudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPlay(mStartPlaying, listmensajes.get(position).getMensaje());
                    mStartPlaying = !mStartPlaying;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listmensajes.size();
    }

    class MensajeHolder extends RecyclerView.ViewHolder{
        private TextView tvname;
        private TextView tvmensaje;
        private Button tvaudio;

        public MensajeHolder(@NonNull View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.nombreusuariochat);
            tvmensaje = itemView.findViewById(R.id.mensajedeluserchat);
            tvaudio = itemView.findViewById(R.id.play);
        }
    }

    private void onPlay(boolean start, String url) {
        if (start) {
            startPlaying(url);
        } else {
            stopPlaying();
        }
    }

    private void startPlaying(String url) {
        player = new MediaPlayer();
        try {
            player.setDataSource(url);
            player.prepare();
            player.start();
        } catch (IOException e) {
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }
}