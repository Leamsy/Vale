package com.bluedot.vale_oficial;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyAdapter_Autorizaciones extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ItemAdapter> mList;
    private Context mContext;
    private String TAG = "Adapter de autorizados";


    public MyAdapter_Autorizaciones(List<ItemAdapter> list, Context context){
        super();
        mList = list;
        mContext = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_peticiones, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {

        final ItemAdapter itemAdapter = mList.get(position);

        ((ViewHolder) viewHolder).mTv_name.setText(itemAdapter.getText());
        ((ViewHolder) viewHolder).mTv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(((ViewHolder) viewHolder).context, Autorizacion.class);
                intent.putExtra("uid", itemAdapter.getUid());
                intent.putExtra("uid_act", itemAdapter.getIdActividad());
                intent.putExtra("act_nombre", itemAdapter.getText());
                ((ViewHolder) viewHolder).context.startActivity(intent);
                ((ListaAutorizaciones)mContext).finish();
            }
        });

        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(itemAdapter.getUid());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String nombresocio = document.getData().get("nombre").toString();
                        ((ViewHolder) viewHolder).mTv_socio.setText(nombresocio);

                        ((ViewHolder) viewHolder).mTv_socio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(((ViewHolder) viewHolder).context, Autorizacion.class);
                                intent.putExtra("uid", itemAdapter.getUid());
                                intent.putExtra("uid_act", itemAdapter.getIdActividad());
                                intent.putExtra("act_nombre", itemAdapter.getText());
                                ((ViewHolder) viewHolder).context.startActivity(intent);
                                ((ListaAutorizaciones)mContext).finish();
                            }
                        });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTv_name;
        public TextView mTv_socio;
        private final Context context;
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mTv_name = (TextView) itemView.findViewById(R.id.tv_name);
            mTv_socio = (TextView) itemView.findViewById(R.id.tv_autor);
        }
    }

}