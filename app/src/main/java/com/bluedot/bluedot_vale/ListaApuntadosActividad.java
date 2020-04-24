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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private List<String> listaapuntados = new ArrayList<>();
    Context context = this;
    private boolean apuntado = false;
    private Button btnvereliminados;
    private String esautor = "false";

    String idvistausuario;
    String imagen_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_apuntados_actividad);

        //Recuperar el id de la actividad
        Intent intent = getIntent();
        actividad = intent.getStringExtra("uid");
        esautor = intent.getStringExtra("esautor");

        if(!esautor.equals("true"))
            findViewById(R.id.btnrechazados).setVisibility(View.GONE);

        btnvereliminados = findViewById(R.id.btnrechazados);

        btnvereliminados.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.listaapuntados);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ORIENTATION_PORTRAIT);
        recyclerView.addItemDecoration(dividerItemDecoration);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Obtener todos los documentos de la colección apuntados
        CollectionReference colRef = FirebaseFirestore.getInstance().collection("users");

        FirebaseFirestore.getInstance().collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot documentusers : task.getResult()) {
                                FirebaseFirestore.getInstance().collection("actividades").document(actividad).collection("apuntados").get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                apuntado = false;
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot documentapuntados : task.getResult()) {
                                                        if(documentapuntados.getId().equals(documentusers.getId())){
                                                            apuntado = true;
                                                        }
                                                    }
                                                }
                                                Log.d("aa", "b");
                                                if(apuntado){
                                                    final ItemAdapter itemAdapter = new ItemAdapter();
                                                    itemAdapter.setText(documentusers.getData().get("nombre").toString());

                                                    imagen_url = documentusers.getData().get("foto_perfil").toString();
                                                    itemAdapter.setImage(imagen_url);

                                                    itemAdapter.setUidvisitante(documentusers.getId());

                                                    data.add(itemAdapter);
                                                }
                                                Log.d("aa", data.toString());
                                                mAdapter = new MyAdapterApuntados(data, context);
                                                recyclerView.setAdapter(mAdapter);
                                            }
                                        });
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

    public void verRechazados(){
        Intent intent = new Intent(ListaApuntadosActividad.this, ListaRechazadosActividad.class);
        intent.putExtra("uid", actividad);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnatrasapuntados:
                volver();
                break;

            case R.id.btnrechazados:
                verRechazados();
                break;
        }
    }
}
