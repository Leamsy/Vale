package com.bluedot.vale_oficial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private String TAG = "Lista de autorizaciones";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_autorizaciones);

        recyclerView = (RecyclerView) findViewById(R.id.listaautorizaciones);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ORIENTATION_PORTRAIT);
        recyclerView.addItemDecoration(dividerItemDecoration);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("actividades")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            findViewById(R.id.gif).setVisibility(INVISIBLE);
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //Ahora de cada documentos miro la subcolección de pendientes
                                FirebaseFirestore.getInstance().collection("actividades").document(document.getId()).collection("pendientes")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot documentpendientes : task.getResult()) {
                                                        Log.d(TAG, documentpendientes.getId() + " => " + documentpendientes.getData());
                                                        // Si el id del documento coincide con uno de mis tutorados, que muestre el elemento
                                                        final String idpendiente = documentpendientes.getId();
                                                        FirebaseFirestore.getInstance().collection("users").document(uid).collection("tutelados")
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot documenttutelados : task.getResult()) {
                                                                                Log.d(TAG, documenttutelados.getId() + " => " + documenttutelados.getData());
                                                                                if(idpendiente.equals(documenttutelados.getId())){
                                                                                    final ItemAdapter itemAdapter = new ItemAdapter();
                                                                                    document.getData().get("titulo").toString();
                                                                                    //Meter título de la actividad
                                                                                    itemAdapter.setText(document.getData().get("titulo").toString());
                                                                                    //Meter id de la persona a la que queremos autorizar
                                                                                    itemAdapter.setUid(documenttutelados.getId());
                                                                                    //Id de la actividad
                                                                                    itemAdapter.setIdActividad(document.getId());
                                                                                    data.add(itemAdapter);
                                                                                }
                                                                            }
                                                                            mAdapter = new MyAdapter_Autorizaciones(data, context);
                                                                            recyclerView.setAdapter(mAdapter);

                                                                        } else {
                                                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        /*
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
        */
    }

    public void volver(android.view.View V){
        finish();
    }

    public void irAcasa(android.view.View V){
        Intent intent = new Intent(ListaAutorizaciones.this, Menu_tutor.class);
        startActivity(intent);
        finish();
    }
}
