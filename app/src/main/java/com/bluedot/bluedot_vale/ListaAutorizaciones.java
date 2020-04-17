package com.bluedot.bluedot_vale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static android.view.View.INVISIBLE;

public class ListaAutorizaciones extends AppCompatActivity {

    private List<ItemAdapter> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    String uid;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_autorizaciones);

        recyclerView = (RecyclerView) findViewById(R.id.listaSuge);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ORIENTATION_PORTRAIT);
        recyclerView.addItemDecoration(dividerItemDecoration);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference colRef = FirebaseFirestore.getInstance().collection("autorizaciones");

        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    findViewById(R.id.gif).setVisibility(INVISIBLE);

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        final ItemAdapter itemAdapter = new ItemAdapter();
                        if(document.getData().get("titulo") != null){
                            itemAdapter.setText(document.getData().get("titulo").toString());
                        }

                        itemAdapter.setUid(document.getId());
                        data.add(itemAdapter);
                    }
                    mAdapter = new MyAdapter_Autorizaciones(data, context);
                    recyclerView.setAdapter(mAdapter);

                } else {
                    Log.d("aa", "No existe el usuario");
                }
            }
        });
    }

    public void volver(android.view.View V){
        finish();
    }
}
