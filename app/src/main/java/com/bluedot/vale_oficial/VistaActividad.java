package com.bluedot.vale_oficial;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class VistaActividad extends AppCompatActivity  implements View.OnClickListener{

    String uid;
    String uid_act;
    String titulo;
    String descripcion;
    String imagen;
    Timestamp fecha;
    String precio;
    String plazas_socios;
    String plazas_voluntarios;
    String requiere_autorizacion;
    String sala_chat;

    private ImageView atras;
    private Button reservar;
    private Button chatear;
    Context context = this;
    private String rol_usuario;
    private Boolean es_autor;
    private String idautor;
    private boolean rechazado;
    private String TAG = "Vista Actividad";
    private String necesita_aut;
    private String elauth;
    private boolean pend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_actividad);

        findViewById(R.id.txtinfo).setVisibility(GONE);

        loading();

        elauth = null;

        atras = findViewById(R.id.btnatras);

        atras.setOnClickListener(this);

        Intent intent = getIntent();
        uid_act = intent.getStringExtra("uid");

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        idautor = uid;
        rechazado = false;
        pend = false;
        elauth = intent.getStringExtra("uid_auth");

        FirebaseFirestore.getInstance().collection("actividades").document(uid_act).collection("rechazados")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals(uid))
                                    rechazado = true;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("actividades").document(uid_act).collection("pendientes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals(uid))
                                    pend = true;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        final DocumentReference docRef = FirebaseFirestore.getInstance().collection("actividades").document(uid_act);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        if(document.getData().get("autor").toString().equals(uid)){
                            es_autor = true;
                        }
                        else{
                            es_autor = false;
                        }
                        final DocumentReference docRef_user = FirebaseFirestore.getInstance().collection("actividades").document(uid_act).collection("apuntados").document(uid);
                        docRef_user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document2 = task.getResult();
                                    if (document2.exists()) {
                                        findViewById(R.id.cvplazas).setVisibility(GONE);
                                        findViewById(R.id.txtinfo).setVisibility(VISIBLE);
                                        TextView txt = findViewById(R.id.txtinfo);
                                        txt.setText("Estas apuntado a esta actividad");

                                        Button boton = findViewById(R.id.boton);
                                        boton.setText("CHAT");
                                        boton.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                chatear();
                                            }
                                        });
                                        ready();
                                    } else {
                                        Button boton = findViewById(R.id.boton);
                                        findViewById(R.id.txtinfo).setVisibility(VISIBLE);
                                        TextView txt = findViewById(R.id.txtinfo);
                                        txt.setText("No estás apuntado a esta actividad aún");
                                        boton.setText("APUNTARSE");
                                        boton.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                apuntarse();
                                            }
                                        });


                                        final DocumentReference docRef_usu = FirebaseFirestore.getInstance().collection("users").document(uid);
                                        docRef_usu.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document3 = task.getResult();
                                                    if (document3.exists()) {

                                                        Button button = findViewById(R.id.boton);
                                                        TextView plazas = findViewById(R.id.plazas);
                                                        Log.d("aa", document3.getData().get("rol").toString());
                                                        rol_usuario = document3.getData().get("rol").toString();
                                                        necesita_aut = document3.getData().get("siempre_autorizacion").toString();
                                                        if(document3.getData().get("rol").toString().equals("socio")){

                                                            plazas.setText("Plazas disponibles para socios: " + document.getData().get("plazas_socios").toString());
                                                            if(document.getData().get("plazas_socios").toString().equals("0")){
                                                                button.setText("No quedan plazas para esta actividad");
                                                                //button.setVisibility(GONE);
                                                                button.setAlpha((float)0.4);
                                                                button.setOnClickListener(new View.OnClickListener() {
                                                                    public void onClick(View v) {
                                                                    }
                                                                });
                                                            }
                                                        }
                                                        else if(document3.getData().get("rol").toString().equals("voluntario")){

                                                            plazas.setText("Plazas disponibles para voluntarios: " + document.getData().get("plazas_voluntarios").toString());
                                                            if(document.getData().get("plazas_voluntarios").toString().equals("0")){
                                                                button.setText("No quedan plazas para esta actividad");
                                                                //button.setVisibility(GONE);
                                                                button.setAlpha((float)0.4);
                                                                button.setOnClickListener(new View.OnClickListener() {
                                                                    public void onClick(View v) {
                                                                    }
                                                                });
                                                            }
                                                        }
                                                        ready();
                                                    }
                                                }
                                            }
                                        });

                                    }


                                    titulo = document.getData().get("titulo").toString();
                                    descripcion = document.getData().get("descripcion").toString();
                                    imagen = document.getData().get("imagen").toString();
                                    fecha = (Timestamp)document.getData().get("fecha");
                                    precio = document.getData().get("precio").toString();
                                    plazas_socios = document.getData().get("plazas_socios").toString();
                                    plazas_voluntarios = document.getData().get("plazas_voluntarios").toString();
                                    requiere_autorizacion = document.getData().get("requiere_autorizacion").toString();
                                    sala_chat = document.getData().get("salachat").toString();

                                    ImageView imagen_view = findViewById(R.id.imagen);
                                    Picasso.get().load(imagen).into(imagen_view);
                                    TextView titulo_view = findViewById(R.id.titulo);
                                    titulo_view.setText(titulo);
                                    TextView descripcion_view = findViewById(R.id.descripcion);
                                    descripcion_view.setText(descripcion);
                                    TextView fecha_view = findViewById(R.id.fecha);

                                    Date date = new Date(fecha.getSeconds()*1000);
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    SimpleDateFormat dfh = new SimpleDateFormat("HH:mm");
                                    df.setTimeZone(TimeZone.getTimeZone("UTC"));
                                    dfh.setTimeZone(TimeZone.getTimeZone("UTC"));
                                    String formattedDate = df.format(date);
                                    String formattedDateH = dfh.format(date);

                                    fecha_view.setText(formattedDate);
                                    TextView hora_view = findViewById(R.id.hora);
                                    hora_view.setText(formattedDateH);
                                    TextView precio_view = findViewById(R.id.precio);
                                    precio_view.setText(precio + "€");
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

    public void apuntarse(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        findViewById(R.id.boton).setVisibility(GONE);

        Map<String, Object> map1= new HashMap<>();
        //Si el socio siempre requiere de autorizacion
        if(rol_usuario.equals("socio") && necesita_aut.equals("true")){
            db.collection("actividades").document(uid_act).collection("pendientes").document(uid).set(map1);
        }
        //Si la actividad requiere de autorizacion para los socios
        else if(rol_usuario.equals("socio") && requiere_autorizacion.equals("true")){
            db.collection("actividades").document(uid_act).collection("pendientes").document(uid).set(map1);
        }
        //en otro caso los apunta a la actividad directamente
        else {
            db.collection("users").document(uid).collection("mis_actividades").document(uid_act).set(map1);
            db.collection("actividades").document(uid_act).collection("apuntados").document(uid).set(map1);
        }

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("actividades").document(uid_act);

        if(rol_usuario.equals("socio") && necesita_aut.equals("false") && requiere_autorizacion.equals("false")){
            db.collection("actividades").document(uid_act).update("plazas_socios", (Integer.parseInt(plazas_socios) - 1));
        }
        else if(rol_usuario.equals("voluntario")){
            db.collection("actividades").document(uid_act).update("plazas_voluntarios", (Integer.parseInt(plazas_voluntarios) - 1));
        }
        finish();
    }

    public void chatear(){
        Intent intent = new Intent(VistaActividad.this, ChatActividades.class);
        intent.putExtra("uid", uid_act);
        intent.putExtra("salachat", sala_chat);
        startActivity(intent);
    }

    public void volver(){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnatras:
                volver();
                break;
            case R.id.boton:
                apuntarse();
                break;
            case R.id.modificar:
                modificar();
                break;
            case R.id.verapuntados:
                verapuntados();
                break;
            case R.id.eliminar:
                eliminarActividad();
                break;
        }
    }

    private void loading(){
        findViewById(R.id.scrollmiactividad).setVisibility(INVISIBLE);
        findViewById(R.id.plazas).setVisibility(GONE);
        findViewById(R.id.boton).setVisibility(GONE);
        findViewById(R.id.eliminar).setVisibility(GONE);
        findViewById(R.id.modificar).setVisibility(GONE);
        findViewById(R.id.verapuntados).setVisibility(GONE);
        findViewById(R.id.espaciova2).setVisibility(GONE);
        findViewById(R.id.espaciova3).setVisibility(GONE);
    }

    private void ready(){
        findViewById(R.id.scrollmiactividad).setVisibility(VISIBLE);
        findViewById(R.id.plazas).setVisibility(VISIBLE);
        if(!rechazado)
            findViewById(R.id.boton).setVisibility(VISIBLE);

        if(rechazado)
            findViewById(R.id.txtinfo).setVisibility(GONE);

        if(pend)
            findViewById(R.id.boton).setVisibility(GONE);

        findViewById(R.id.gif).setVisibility(INVISIBLE);
        findViewById(R.id.verapuntados).setVisibility(VISIBLE);

        if(es_autor){
            findViewById(R.id.modificar).setVisibility(VISIBLE);
            findViewById(R.id.verapuntados).setVisibility(VISIBLE);
            findViewById(R.id.eliminar).setVisibility(VISIBLE);
            findViewById(R.id.espaciova2).setVisibility(VISIBLE);
            findViewById(R.id.espaciova3).setVisibility(VISIBLE);
            findViewById(R.id.cvplazas).setVisibility(GONE);
        }
    }

    private void modificar(){
        Intent intent = new Intent(this, ModificarActividad.class);
        intent.putExtra("uid", uid_act);
        startActivity(intent);
    }

    private void verapuntados(){
        Intent intent = new Intent(this, ListaApuntadosActividad.class);
        intent.putExtra("uid", uid_act);
        intent.putExtra("esautor", es_autor.toString());
        intent.putExtra("idautor", idautor);
        startActivity(intent);
    }

    private void eliminarActividad(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("¿Estás seguro de que deseas eliminar la actividad?");
        dialog.setTitle("ELIMINAR ACTIVIDAD");
        dialog.setPositiveButton("SÍ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        moveFirestoreDocument();

                        Toast.makeText(context, "Actividad eliminada.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(VistaActividad.this, Mis_actividades.class);
                        startActivity(intent);
                        finish();
                    }
                });
        dialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    public void moveFirestoreDocument() {

        //Borrar subcoleccion de apuntados
        FirebaseFirestore.getInstance().collection("actividades").document(uid_act).collection("apuntados")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //////////////////////////////////////////////////////////////////
                                FirebaseFirestore.getInstance().collection("actividades").document(uid_act).collection("apuntados").document(document.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                                //////////////////////////////////////////////////////////////////
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        //Borrar subcoleccion de rechazados
        FirebaseFirestore.getInstance().collection("actividades").document(uid_act).collection("rechazados")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //////////////////////////////////////////////////////////////////
                                FirebaseFirestore.getInstance().collection("actividades").document(uid_act).collection("rechazados").document(document.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                                //////////////////////////////////////////////////////////////////
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        //Borrar subcoleccion de pendientes
        FirebaseFirestore.getInstance().collection("actividades").document(uid_act).collection("pendientes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //////////////////////////////////////////////////////////////////
                                FirebaseFirestore.getInstance().collection("actividades").document(uid_act).collection("pendientes").document(document.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                                //////////////////////////////////////////////////////////////////
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        //Borrar la actividad
        FirebaseFirestore.getInstance().collection("actividades").document(uid_act)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }//Fin funcion borrar
}
