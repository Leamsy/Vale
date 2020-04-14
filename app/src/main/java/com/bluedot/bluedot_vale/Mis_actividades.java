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

        final CollectionReference colRef = FirebaseFirestore.getInstance().collection("users").document(uid).collection("mis_actividades");

        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId());

                        ////////////////////////////////////////////////////
                        DocumentReference docRef = FirebaseFirestore.getInstance().collection("actividades").document(document.getId());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document2 = task.getResult();

                                    if (document2.exists()) {
                                        //Que cree el itemadapter
                                        Toast.makeText(Mis_actividades.this, document2.getId(),
                                                Toast.LENGTH_SHORT).show();
                                        
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                        ////////////////////////////////////////////////////
                    }
                    mAdapter = new MyAdapter(data, context);
                    recyclerView.setAdapter(mAdapter);

                } else {
                    Log.d(TAG, "No existe el usuario");
                }
            }
        });

    }
    public void volver(android.view.View V){
        finish();
    }
}
