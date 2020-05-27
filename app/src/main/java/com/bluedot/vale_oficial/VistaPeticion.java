package com.bluedot.vale_oficial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class VistaPeticion extends AppCompatActivity implements View.OnClickListener {

    String uid;
    String uid_act;
    String titulo;
    String descripcion;
    String autor_nombre;
    private ImageView volver;
    private ImageView casica;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_peticion);

        volver = findViewById(R.id.btnatrasvp);
        volver.setOnClickListener(this);

        casica = findViewById(R.id.btncasita);
        casica.setOnClickListener(this);

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
                        autor_nombre_view.setText(autor_nombre);

                    } else {
                    }
                } else {
                }
            }
        });

    }

    public void volver(){
        finish();
    }

    public void irAcasa(){
        Intent intent = new Intent(VistaPeticion.this, Principal.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnatrasvp:
                volver();
                break;
            case R.id.btncasita:
                irAcasa();
                break;
        }
    }
}
