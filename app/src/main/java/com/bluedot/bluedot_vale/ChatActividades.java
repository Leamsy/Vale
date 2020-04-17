package com.bluedot.bluedot_vale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChatActividades extends AppCompatActivity {

    private RecyclerView todosmensajes;
    private EditText mensaje;
    private ImageView enviar;
    private List<Mensaje> listmensajes;

    private MyAdapter_chat myAdapter_chat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_actividades);

        todosmensajes = findViewById(R.id.reciclerchat);
        mensaje = findViewById(R.id.campomensajes);
        enviar = findViewById(R.id.enviarchat);

        listmensajes = new ArrayList<>();
        myAdapter_chat = new MyAdapter_chat(listmensajes);
        todosmensajes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        todosmensajes.setAdapter(myAdapter_chat);
        todosmensajes.setHasFixedSize(true);

        //FirebaseFirestore.getInstance().collection("chat").
    }


}
