package com.bluedot.vale_oficial;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;

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
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;

public class Lista_actividades extends AppCompatActivity {

    private List<ItemAdapter> data = new ArrayList<>();
    private ArrayAdapter<TextView> arrayAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    private SearchView searcher;
    Context context = this;
    String uid;

    String imagen_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_actividades);

        recyclerView = (RecyclerView) findViewById(R.id.listaSuge);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ORIENTATION_PORTRAIT);
        recyclerView.addItemDecoration(dividerItemDecoration);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        findViewById(R.id.button).setVisibility(GONE);
        findViewById(R.id.espacioac).setVisibility(GONE);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        searcher = findViewById(R.id.search);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DocumentReference docRef_user = FirebaseFirestore.getInstance().collection("users").document(uid);
        docRef_user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document3 = task.getResult();
                    if (document3.exists()) {

                        if(!document3.getData().get("rol").toString().equals("socio")){
                            findViewById(R.id.button).setVisibility(View.VISIBLE);
                            findViewById(R.id.espacioac).setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        final CollectionReference colRef = FirebaseFirestore.getInstance().collection("actividades");

        colRef.orderBy("fecha", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    findViewById(R.id.gif).setVisibility(INVISIBLE);

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Date c = new Date();
                        db.collection("update").document("update").update("a", c.getTime());

                        final ItemAdapter itemAdapter = new ItemAdapter();
                        if(document.getData().get("titulo") != null){
                            itemAdapter.setText(document.getData().get("titulo").toString());
                        }

                        if(document.getData().get("imagen") != null){
                            imagen_url = document.getData().get("imagen").toString();
                            itemAdapter.setImage(imagen_url);
                        }

                        itemAdapter.setUid(document.getId());
                        data.add(itemAdapter);
                    }
                    mAdapter = new MyAdapter(data, context);
                    recyclerView.setAdapter(mAdapter);

                } else {
                    Log.d("aa", "No existe el usuario");
                }
            }
        });

        searcher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscar(newText);
                return true;
            }
        });

    }

    public void volver(android.view.View V){
        Intent intent = new Intent(this, Submenu_Actividades.class);
        startActivity(intent);
        finish();
    }

    public void aniadir(android.view.View V){
        Intent intent = new Intent(this, AgregarActividad.class);
        startActivity(intent);
    }

    public void buscar(String nt){
        ArrayList<ItemAdapter> miLista = new ArrayList<>();
        for (ItemAdapter obj: data){
            if(obj.getText().toLowerCase().contains(nt.toLowerCase()))
                miLista.add(obj);
        }

        mAdapter = new MyAdapter(miLista, context);
        recyclerView.setAdapter(mAdapter);
    }

    public void irAcasa(android.view.View V){
        Intent intent = new Intent(Lista_actividades.this, Principal.class);
        startActivity(intent);
        finish();
    }

}
