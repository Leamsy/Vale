package com.bluedot.vale_oficial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
import static android.view.View.VISIBLE;

public class Peticiones extends AppCompatActivity {

    private List<ItemAdapter> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    String uid;
    Context context = this;
    private SearchView searcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peticiones);

        searcher = findViewById(R.id.searchpeticiones);

        loading();

        recyclerView = (RecyclerView) findViewById(R.id.listaSuge);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ORIENTATION_PORTRAIT);
        recyclerView.addItemDecoration(dividerItemDecoration);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final CollectionReference colRef = FirebaseFirestore.getInstance().collection("peticiones");

        colRef.orderBy("fecha", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    ready();
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        final ItemAdapter itemAdapter = new ItemAdapter();
                        if(document.getData().get("titulo") != null){
                            itemAdapter.setText(document.getData().get("titulo").toString());
                        }

                        itemAdapter.setUid(document.getId());
                        data.add(itemAdapter);
                    }
                    mAdapter = new MyAdapter_Peticiones(data, context);
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

    public void buscar(String nt){
        ArrayList<ItemAdapter> miLista = new ArrayList<>();
        for (ItemAdapter obj: data){
            if(obj.getText().toLowerCase().contains(nt.toLowerCase()))
                miLista.add(obj);
        }

        mAdapter = new MyAdapter_Peticiones(miLista, context);
        recyclerView.setAdapter(mAdapter);
    }

    public void volver(android.view.View V){
        Intent intent = new Intent(this, Submenu_Actividades.class);
        startActivity(intent);
        finish();
    }

    public void aniadirPeticion(android.view.View V){
        Intent intent = new Intent(this, AgregarPeticion.class);
        startActivity(intent);
    }

    private void loading(){
        findViewById(R.id.linear).setVisibility(INVISIBLE);
        findViewById(R.id.gif).setVisibility(VISIBLE);
    }

    private void ready(){
        findViewById(R.id.linear).setVisibility(VISIBLE);
        findViewById(R.id.gif).setVisibility(INVISIBLE);
    }

}
