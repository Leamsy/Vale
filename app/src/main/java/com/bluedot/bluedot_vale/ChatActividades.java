package com.bluedot.bluedot_vale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChatActividades extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView todosmensajes;
    private EditText mensaje;
    private ImageView enviar;
    private List<Mensaje> listmensajes;
    private ImageView btnatras;
    private String titulosalachat;
    private String nombre;

    private MyAdapter_chat myAdapter_chat;
    String uid_act;
    String idchat;
    String iduser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_actividades);

        Intent intent = getIntent();
        uid_act = intent.getStringExtra("uid");
        idchat = intent.getStringExtra("salachat");
        btnatras = findViewById(R.id.atrasperfilchat);

        iduser = FirebaseAuth.getInstance().getUid();

        btnatras.setOnClickListener(this);

        todosmensajes = findViewById(R.id.reciclerchat);
        mensaje = findViewById(R.id.campomensajes);
        enviar = findViewById(R.id.enviarchat);

        listmensajes = new ArrayList<>();
        myAdapter_chat = new MyAdapter_chat(listmensajes);
        todosmensajes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        todosmensajes.setAdapter(myAdapter_chat);
        todosmensajes.setHasFixedSize(true);


        //Obtener titulo de la actividad
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("actividades").document(uid_act);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String TAG = "Sala chat";
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        titulosalachat = document.getData().get("titulo").toString();
                        TextView titulochat = findViewById(R.id.titulochat);
                        titulochat.setText(titulosalachat);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //Obtener nombre del usuario
        DocumentReference docRef2 = FirebaseFirestore.getInstance().collection("users").document(iduser);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String TAG = "Sala chat";
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        nombre = document.getData().get("nombre").toString();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        FirebaseFirestore.getInstance().collection("chat").document(idchat).collection("mensajes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentChange mDocumentChange : queryDocumentSnapshots.getDocumentChanges()){
                            if (mDocumentChange.getType() == DocumentChange.Type.ADDED){
                                listmensajes.add(mDocumentChange.getDocument().toObject(Mensaje.class));
                                myAdapter_chat.notifyDataSetChanged();
                                todosmensajes.smoothScrollToPosition(listmensajes.size());
                            }
                        }
                    }
                });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mensaje.length() == 0)
                    return;
                Mensaje sms = new Mensaje();
                sms.setMensaje(mensaje.getText().toString());
                sms.setNombre(nombre);
                FirebaseFirestore.getInstance().collection("chat").document(idchat).collection("mensajes").add(sms);
                mensaje.setText("");
            }
        });

    }

    public void volver(){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.atrasperfilchat:
                volver();
                break;
        }
    }
}
