package com.bluedot.vale_oficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Autorizacion extends AppCompatActivity implements View.OnClickListener{

    private String uid;
    private String uid_auth;
    private String acti_uid;
    private String uid_act;
    private String titulo_act;
    private String TAG="Autorización";
    private Button masinfo;
    private Button apuntarse;
    private boolean hayplazas = false;
    private Button rechazarlo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autorizacion);

        apuntarse = findViewById(R.id.btnapuntarse);
        apuntarse.setOnClickListener(this);

        rechazarlo = findViewById(R.id.btndenegar);
        rechazarlo.setOnClickListener(this);



        Intent intent = getIntent();

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        uid_auth = intent.getStringExtra("uid");
        uid_act = intent.getStringExtra("uid_act");
        titulo_act = intent.getStringExtra("act_nombre");

        TextView eltitulodelact = findViewById(R.id.tituloaut);
        eltitulodelact.setText(titulo_act);

        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(uid_auth);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String nombresocio = document.getData().get("nombre").toString();;
                        TextView elnombre = findViewById(R.id.nombreUser);
                        //Toast.makeText(Autorizacion.this, document.getData().get("nombre").toString(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(Autorizacion.this, nombresocio, Toast.LENGTH_SHORT).show();
                        elnombre.setText(nombresocio);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("actividades").document(uid_act);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String desc = document.getData().get("descripcion").toString();
                        TextView campodes = findViewById(R.id.descripcionaut);
                        campodes.setText(desc);

                        Timestamp fecha = (Timestamp) document.getData().get("fecha");

                        Date date = new Date(fecha.getSeconds()*1000);

                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat dfh = new SimpleDateFormat("HH:mm");

                        df.setTimeZone(TimeZone.getTimeZone("UTC"));
                        dfh.setTimeZone(TimeZone.getTimeZone("UTC"));

                        String formattedDate = df.format(date);
                        String formattedDateH = dfh.format(date);

                        TextView fecha_view = findViewById(R.id.fechatutor);
                        fecha_view.setText(formattedDate);
                        TextView hora_view = findViewById(R.id.horatutor);
                        hora_view.setText(formattedDateH);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    public void volver(){
        finish();
    }

    public void verActividad(){
        Intent intent = new Intent(Autorizacion.this, VistaActividad.class);
        intent.putExtra("uid", uid_act);
        intent.putExtra("uid_user", uid_auth);
        startActivity(intent);
    }

    public void permitir(){
        hayplazas = false;
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("actividades").document(uid_act);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Integer plazas = 0;
                        String plzstring = document.getData().get("plazas_socios").toString();
                        plazas = Integer.parseInt(plzstring);
                        if(plazas > 0) {
                            hayplazas = true;
                            findViewById(R.id.btnapuntarse).setVisibility(View.GONE);
                            findViewById(R.id.btndenegar).setVisibility(View.GONE);
                            siapuntar();
                        }else{
                            Toast.makeText(Autorizacion.this, "Lo sentimos, ya no quedan plazas para esta actividad, inténtelo más tarde por si quedara alguna libre", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void rechazar(){
        findViewById(R.id.btnapuntarse).setVisibility(View.GONE);
        findViewById(R.id.btndenegar).setVisibility(View.GONE);
        Map<String, Object> mapa = new HashMap<>();
        FirebaseFirestore.getInstance().collection("actividades").document(uid_act).collection("rechazados").document(uid_auth).set(mapa);
        FirebaseFirestore.getInstance().collection("actividades").document(uid_act).collection("pendientes").document(uid_auth)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        Toast.makeText(Autorizacion.this, "No ha autorizado al usuario a la actividad, no podrá volver a apuntarse a esta actividad", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Autorizacion.this, ListaAutorizaciones.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void siapuntar(){
        FirebaseFirestore.getInstance().collection("actividades").document(uid_act).collection("pendientes").document(uid_auth)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        Map<String, Object> mapa = new HashMap<>();
                        FirebaseFirestore.getInstance().collection("actividades").document(uid_act).collection("apuntados").document(uid_auth).set(mapa);
                        DocumentReference actualizarRef = FirebaseFirestore.getInstance().collection("actividades").document(uid_act);
                        actualizarRef.update("plazas_socios", FieldValue.increment(-1));
                        Toast.makeText(Autorizacion.this, "Se ha autorizado a la actividad correctamente", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Autorizacion.this, ListaAutorizaciones.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        Toast.makeText(Autorizacion.this, "La autorización ha fallado, vuelva a intentarlo", Toast.LENGTH_LONG).show();
                    }
                });

    }


@Override
public void onClick(View v) {
        switch (v.getId()) {

        case R.id.btnatrasaut:
            volver();
        break;

            case R.id.btnapuntarse:
                permitir();
                break;

            case R.id.btndenegar:
                rechazar();
                break;
        }
    }
}
