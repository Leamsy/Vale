package com.bluedot.vale_oficial;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Principal extends AppCompatActivity  {
    private String TAG = "Principal";
    private String uid;
    private String nActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        uid = FirebaseAuth.getInstance().getUid();
        nActividad = null;

        FirebaseFirestore.getInstance().collection("actividades_terminadas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                nActividad = document.getData().get("titulo").toString();
                                /////////Buscar dentro de las actividades en apuntados///////////
                                FirebaseFirestore.getInstance().collection("actividades_terminadas").document(document.getId()).collection("apuntados")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document2 : task.getResult()) {
                                                        Log.d(TAG, document2.getId() + " => " + document2.getData());

                                                        /////////Obtenemos el ID de los apuntados////////////////
                                                        /////////Si estamos ahí, miramos que no estemos tambien en la lista de valorados////////////
                                                        ////////Si no estamos, sacamos la pregunta////////////////////
                                                        if(document2.getId().equals(uid)){
                                                            DocumentReference docRef = FirebaseFirestore.getInstance().collection("actividades_terminadas").document(document.getId()).collection("valoraciones").document(uid);
                                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        final DocumentSnapshot documentvaloracion = task.getResult();
                                                                        if (documentvaloracion.exists()) {
                                                                            Log.d(TAG, "DocumentSnapshot data: " + documentvaloracion.getData());
                                                                        } else {
                                                                            //Si no existe que saque el comentario de valorar

                                                                            AlertDialog.Builder dialog=new AlertDialog.Builder(Principal.this);
                                                                            dialog.setMessage("Se ha realizado la actividad: " + nActividad + ", donde estabas apuntado/a, pulsa sobre VALORAR para valorar a la persona o personas con las que has realizado la actividad");
                                                                            dialog.setTitle("Valorar: " + nActividad);
                                                                            dialog.setPositiveButton("VALORAR",
                                                                                    new DialogInterface.OnClickListener() {
                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                            Intent intent = new Intent(Principal.this, Valoraciones.class);
                                                                                            intent.putExtra("uid_act", document.getId());
                                                                                            intent.putExtra("n_act", nActividad);
                                                                                            startActivity(intent);
                                                                                            //finish();
                                                                                        }
                                                                                    });
                                                                            dialog.setNegativeButton("MÁS TARDE",
                                                                                    new DialogInterface.OnClickListener() {
                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                        }
                                                                                    });
                                                                            AlertDialog alertDialog=dialog.create();
                                                                            alertDialog.show();

                                                                            /////////////////////////////////////////////////
                                                                        }
                                                                    } else {
                                                                        Log.d(TAG, "get failed with ", task.getException());
                                                                    }
                                                                }
                                                            });

                                                        }
                                                        ////////////////////////////////////////////////////////
                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                                //////////////////////////////////////////////////////////////
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void irSubmenu(android.view.View V){
        Intent intent = new Intent(this, Submenu_Actividades.class);
        startActivity(intent);
    }

    public void cambiarPerfil(android.view.View V){
        Intent intent = new Intent(this, PerfilUsuario.class);
        startActivity(intent);
    }

    public void cerrarApp(android.view.View V){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
