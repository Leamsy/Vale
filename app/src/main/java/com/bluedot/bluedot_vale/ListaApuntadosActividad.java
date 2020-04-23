package com.bluedot.bluedot_vale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static android.view.View.INVISIBLE;

public class ListaApuntadosActividad extends AppCompatActivity implements View.OnClickListener{

    private List<ItemAdapter> data = new ArrayList<>();
    private ArrayAdapter<TextView> arrayAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    private String actividad;
    private String TAG = "Lista de apuntados";
    Context context = this;

    String idvistausuario;
    String imagen_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_apuntados_actividad);

        //Recuperar el id de la actividad
        Intent intent = getIntent();
        actividad = intent.getStringExtra("uid");

        recyclerView = (RecyclerView) findViewById(R.id.listaapuntados);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ORIENTATION_PORTRAIT);
        recyclerView.addItemDecoration(dividerItemDecoration);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Obtener todos los documentos de la colección apuntados



        CollectionReference colRef = FirebaseFirestore.getInstance().collection("actividades").document(actividad).collection("apuntados");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //Toast.makeText(ListaApuntadosActividad.this, document.getId(),
                                  //      Toast.LENGTH_SHORT).show();
                                DocumentReference docRef_user = FirebaseFirestore.getInstance().collection("users").document(document.getId());
                                /////Obtener datos del documento/////
                                docRef_user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document2 = task.getResult();
                                            if (document2.exists()) {
                                                Log.d(TAG, "DocumentSnapshot data: " + document2.getData());
                                                idvistausuario = document2.getId();

                                                final ItemAdapter itemAdapter = new ItemAdapter();
                                                if(document2.getData().get("nombre") != null){
                                                    itemAdapter.setText(document2.getData().get("nombre").toString());
                                                }

                                                if(document2.getData().get("foto_perfil") != null){
                                                    imagen_url = document2.getData().get("foto_perfil").toString();
                                                    itemAdapter.setImage(imagen_url);
                                                }

                                                itemAdapter.setUidvisitante(idvistausuario);
                                                data.add(itemAdapter);
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                        mAdapter = new MyAdapterApuntados(data, context);
                                        recyclerView.setAdapter(mAdapter);
                                    }
                                });
                                /////////////////////////////////////
                                }
                                } else {
                                 Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                                }
                                });
    }//OnCreate

    public void volver(){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnatrasapuntados:
                volver();
                break;
        }
    }
}
