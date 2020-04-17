package com.bluedot.bluedot_vale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class Autorizacion extends AppCompatActivity {

    String uid;
    String uid_auth;
    String acti_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autorizacion);

        Intent intent = getIntent();

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        uid_auth = intent.getStringExtra("uid");

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("autorizaciones").document(uid_auth);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        acti_uid = document.getData().get("actividad").toString();


                        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(document.getData().get("socio").toString());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {

                                        TextView text = findViewById(R.id.txtactividades3);
                                        text.setText("Su socio " + document.getData().get("nombre").toString() + " quiere participar en...");

                                    } else {
                                    }
                                } else {
                                }
                            }
                        });

                        DocumentReference docRef1 = FirebaseFirestore.getInstance().collection("actividades").document(document.getData().get("actividad").toString());
                        docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {

                                        TextView titulo_view = findViewById(R.id.titulo);
                                        titulo_view.setText(document.getData().get("titulo").toString());

                                        TextView descripcion_view = findViewById(R.id.descripcion);
                                        descripcion_view.setText(document.getData().get("descripcion").toString());


                                    } else {
                                    }
                                } else {
                                }
                            }
                        });


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

    public void verActividad(android.view.View V){
        Intent intent = new Intent(this, VistaActividad.class);
        intent.putExtra("uid", acti_uid);
        startActivity(intent);

    }
}
