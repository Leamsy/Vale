package com.bluedot.bluedot_vale;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapterApuntados extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ItemAdapter> mList;
    private Context mContext;
    private String TAG = "Borrar usuario de apuntado";
    private String esAutor;
    private String idActividad;
    private String idPersona;

    public MyAdapterApuntados(List<ItemAdapter> list, Context context){
        super();
        mList = list;
        mContext = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apuntados, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        final ItemAdapter itemAdapter = mList.get(position);
        esAutor = itemAdapter.getAutor();
        idActividad = itemAdapter.getIdActividad();
        idPersona = itemAdapter.getUidvisitante();

        ((ViewHolder) viewHolder).mTv_name.setText(itemAdapter.getText());
        ((ViewHolder) viewHolder).mTv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(((ViewHolder) viewHolder).context, Vista_perfil_visitante.class);
                intent.putExtra("uid", itemAdapter.getUidvisitante());
                ((ViewHolder) viewHolder).context.startActivity(intent);
            }
        });

        if(esAutor.equals("false"))
        ((ViewHolder) viewHolder).btnEliminar.setVisibility(View.GONE);

        try {
            Picasso.get().load(itemAdapter.getImage()).into(((ViewHolder) viewHolder).mImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((ViewHolder) viewHolder).mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(((ViewHolder) viewHolder).context, Vista_perfil_visitante.class);
                intent.putExtra("uid", itemAdapter.getUidvisitante());
                ((ViewHolder) viewHolder).context.startActivity(intent);
            }
        });

        ((ViewHolder) viewHolder).btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarUsuarioDeApuntado();
            }
        });

    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTv_name;
        public ImageView mImg;
        public ImageView btnEliminar;

        private final Context context;
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mTv_name = (TextView) itemView.findViewById(R.id.nombreuserapuntado);
            mImg = (ImageView) itemView.findViewById(R.id.imguserapuntado);
            btnEliminar = (ImageView) itemView.findViewById(R.id.btnBorrarUserActividad);
        }
    }

    public void borrarUsuarioDeApuntado(){

        //Esto borra el usuario que le da la gana (ARREGLARLO)
        
        FirebaseFirestore.getInstance().collection("actividades").document(idActividad).collection("apuntados").document(idPersona)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error deleting document", e);
                    }
                });
    }





}
