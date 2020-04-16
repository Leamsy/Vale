package com.bluedot.bluedot_vale;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter_Peticiones extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ItemAdapter> mList;
    private Context mContext;


    public MyAdapter_Peticiones(List<ItemAdapter> list, Context context){
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
                Intent intent = new Intent(((ViewHolder) viewHolder).context, VistaPeticion.class);
                intent.putExtra("uid", itemAdapter.getUid());
                ((ViewHolder) viewHolder).context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTv_name;
        private final Context context;
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mTv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

}