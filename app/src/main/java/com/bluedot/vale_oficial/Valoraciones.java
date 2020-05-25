package com.bluedot.vale_oficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Valoraciones extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "Valoración";
    private String uid;
    private TextView titulo;
    private String eltitulo;
    private String uidact;

    private ImageView imgmuybienim;
    private ImageView imgbienim;
    private ImageView mal;
    private ImageView muymal;
    private Button sinvaloracion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoraciones);
        uid = null;
        eltitulo = null;
        uidact = null;

        uid = FirebaseAuth.getInstance().getUid();

        Intent intent = getIntent();
        eltitulo = intent.getStringExtra("n_act");
        uidact = intent.getStringExtra("uid_act");

        titulo = findViewById(R.id.nactividadvalorar);
        titulo.setText(eltitulo);

        imgmuybienim = findViewById(R.id.imgmuybien);
        imgmuybienim.setOnClickListener(this);

        imgbienim = findViewById(R.id.imgbien);
        imgbienim.setOnClickListener(this);

        mal = findViewById(R.id.imgmal);
        mal.setOnClickListener(this);

        muymal = findViewById(R.id.imgmuymal);
        muymal.setOnClickListener(this);

        sinvaloracion = findViewById(R.id.novalorar);
        sinvaloracion.setOnClickListener(this);
    }

    public void volver(){
        finish();
    }

    public void valorar(String valoracion){
        Map<String, Object> val = new HashMap<>();

        val.put("Valoración", valoracion);

        FirebaseFirestore.getInstance().collection("actividades_terminadas").document(uidact).collection("valoraciones").document(uid)
                .set(val)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(Valoraciones.this, "Valoración añadida.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Valoraciones.this, Principal.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnatras:
                volver();
                break;

            case R.id.imgmuybien:
                valorar("Muy bien");
                break;

            case R.id.imgbien:
                valorar("Bien");
                break;

            case R.id.imgmal:
                valorar("Mal");
                break;

            case R.id.imgmuymal:
                valorar("Muy mal");
                break;

            case R.id.novalorar:
                valorar("No valora");
                break;
        }
    }
}
