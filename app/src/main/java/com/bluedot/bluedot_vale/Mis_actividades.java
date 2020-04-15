package com.bluedot.bluedot_vale;

import android.content.Context;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static android.view.View.INVISIBLE;

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

    String imagen_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_actividades);

        recyclerView = (RecyclerView) findViewById(R.id.reciclermisactividades);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ORIENTATION_PORTRAIT);
        recyclerView.addItemDecoration(dividerItemDecoration);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Recoger de mis actividades
        final CollectionReference colRef = FirebaseFirestore.getInstance().collection("users").document(uid).collection("mis_actividades");

        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Mis_actividades.this, "Vamos a recoger los ID de las actividades:",
                            Toast.LENGTH_SHORT).show();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Toast.makeText(Mis_actividades.this, document.getId(),
                               Toast.LENGTH_SHORT).show();
                        misactividades.add(document.getId());

                        /*
                        DocumentReference docRef = FirebaseFirestore.getInstance().collection("actividades").document(document.getId());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                if (task2.isSuccessful()) {

                                    DocumentSnapshot document2 = task2.getResult();
                                    if (document2.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document2.getData());
                                        Toast.makeText(Mis_actividades.this, document2.getData().get("titulo").toString(),
                                                Toast.LENGTH_SHORT).show();
                                        final ItemAdapter itemAdapter = new ItemAdapter();
                                        itemAdapter.setText(document2.getData().get("titulo").toString());
                                        imagen_url = document2.getData().get("imagen").toString();
                                        itemAdapter.setImage(imagen_url);
                                        itemAdapter.setUid(document2.getId());
                                        data.add(itemAdapter);
                                        mAdapter = new MyAdapter(data, context);
                                        recyclerView.setAdapter(mAdapter);
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task2.getException());
                                }
                            }
                        });
                         */
                    }//[for]
                    Toast.makeText(Mis_actividades.this, misactividades.toString(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("aa", "No existe el usuario");
                }
            }
        });

    }//OnCreate

    public void volver(android.view.View V){
        finish();
    }
}
