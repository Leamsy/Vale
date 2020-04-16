package com.bluedot.bluedot_vale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class VistaPeticion extends AppCompatActivity {

    String uid;
    String uid_act;
    String titulo;
    String descripcion;
    String autor_nombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_peticion);

        Intent intent = getIntent();
        uid_act = intent.getStringExtra("uid");

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("peticiones").document(uid_act);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {

                        titulo = document.getData().get("titulo").toString();
                        descripcion = document.getData().get("descripcion").toString();
                        autor_nombre = document.getData().get("autor_nombre").toString();

                        TextView titulo_view = findViewById(R.id.titulo);
                        titulo_view.setText(titulo);
                        TextView descripcion_view = findViewById(R.id.descripcion);
                        descripcion_view.setText(descripcion);
                        TextView autor_nombre_view = findViewById(R.id.autor_nombre);
                        autor_nombre_view.setText("Autor: " + autor_nombre);

                    } else {
                    }
                } else {
                }
            }
        });








    }

    public void volver(android.view.View V){
        finish();
    }
}
