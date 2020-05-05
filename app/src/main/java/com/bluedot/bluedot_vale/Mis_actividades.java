package com.bluedot.bluedot_vale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Mis_actividades extends AppCompatActivity {
    private List<ItemAdapter> data = new ArrayList<>();
    private ArrayAdapter<TextView> arrayAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    private List<String> misactividades = new ArrayList<>();

    private static final String TAG = "Mostrar mis actividades";

    Context context = this;
    String uid;
    Boolean apuntado;
    String imagen_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_actividades);

        loading();

        recyclerView = (RecyclerView) findViewById(R.id.reciclermisactividades);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ORIENTATION_PORTRAIT);
        recyclerView.addItemDecoration(dividerItemDecoration);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference colRef = FirebaseFirestore.getInstance().collection("actividades");

        FirebaseFirestore.getInstance().collection("actividades").orderBy("fecha", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {

                                FirebaseFirestore.getInstance().collection("actividades").document(document.getId()).collection("apuntados").get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                apuntado = false;
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        if(document.getId().equals(uid)){
                                                            Log.d("aa", "a");
                                                            apuntado = true;
                                                        }
                                                    }
                                                }
                                                Log.d("aa", "b");
                                                if(apuntado){
                                                    final ItemAdapter itemAdapter = new ItemAdapter();
                                                    itemAdapter.setText(document.getData().get("titulo").toString());

                                                    imagen_url = document.getData().get("imagen").toString();
                                                    itemAdapter.setImage(imagen_url);

                                                    itemAdapter.setUid(document.getId());

                                                    data.add(itemAdapter);Log.d("aa", "c");
                                                }
                                                Log.d("aa", data.toString());
                                                mAdapter = new MyAdapter(data, context);
                                                recyclerView.setAdapter(mAdapter);
                                            }
                                        });
                                ready();
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void volver(android.view.View V){
        Intent intent = new Intent(this, Submenu_Actividades.class);
        startActivity(intent);
        finish();
    }

    private void loading(){
        findViewById(R.id.reciclermisactividades).setVisibility(INVISIBLE);
        findViewById(R.id.gif).setVisibility(VISIBLE);
    }

    private void ready(){
        findViewById(R.id.reciclermisactividades).setVisibility(VISIBLE);
        findViewById(R.id.gif).setVisibility(INVISIBLE);
    }
}
