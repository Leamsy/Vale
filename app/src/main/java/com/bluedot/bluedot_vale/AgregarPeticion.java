package com.bluedot.bluedot_vale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class AgregarPeticion extends AppCompatActivity {

    Map<String, Object> map= new HashMap<>();
    Context context = this;
    String uid;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_peticion);
    }

    public void volver(android.view.View V){
        finish();
    }

    public void enviar(android.view.View V){
        Timestamp timestamp = null;
        timestamp = timestamp.now();

        map.put("autor", FirebaseAuth.getInstance().getCurrentUser().getUid());

        map.put("fecha", timestamp);

        TextView titulo = findViewById(R.id.titulo);
        map.put("titulo", titulo.getText().toString());

        TextView descripcion = findViewById(R.id.descripcion);
        map.put("descripcion", descripcion.getText().toString());

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(!titulo.getText().toString().equals("") && !descripcion.getText().toString().equals("")) {
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(uid);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            map.put("autor_nombre", document.getData().get("nombre").toString());

                            final FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("peticiones").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Toast.makeText(context, "La petición ha sido enviada.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Error.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });

                        } else {
                        }
                    } else {
                    }
                }
            });
        }else{
            Toast.makeText(context, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
        }
    }
}
