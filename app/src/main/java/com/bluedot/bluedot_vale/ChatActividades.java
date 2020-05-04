package com.bluedot.bluedot_vale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

public class ChatActividades extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView todosmensajes;
    private EditText mensaje;
    private ImageView enviar;
    private List<Mensaje> listmensajes;
    private ImageView btnatras;
    private String titulosalachat;
    private String nombre;
    private Button grabar;

    private MyAdapter_chat myAdapter_chat;
    String uid_act;
    String idchat;
    String iduser;

    private static String fileName = null;

    private MediaRecorder recorder = null;


    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    boolean mStartRecording = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_actividades);

        Intent intent = getIntent();
        uid_act = intent.getStringExtra("uid");
        idchat = intent.getStringExtra("salachat");
        btnatras = findViewById(R.id.atrasperfilchat);
        grabar = findViewById(R.id.record);
        grabar.setBackgroundColor(RED);

        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.aac";

        ActivityCompat.requestPermissions(this, permissions, 200);

        iduser = FirebaseAuth.getInstance().getUid();

        btnatras.setOnClickListener(this);
        grabar.setOnClickListener(this);


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


        FirebaseFirestore.getInstance().collection("chat").document(idchat).collection("mensajes").orderBy("fecha")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentChange mDocumentChange : queryDocumentSnapshots.getDocumentChanges()){
                            if (mDocumentChange.getType() == DocumentChange.Type.ADDED){
                                listmensajes.add(mDocumentChange.getDocument().toObject(Mensaje.class));
                                myAdapter_chat.notifyDataSetChanged();
                                todosmensajes.getLayoutManager().scrollToPosition(listmensajes.size()-1);
                                //todosmensajes.smoothScrollToPosition(listmensajes.size());
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
                sms.getFecha();
                sms.setMensaje(mensaje.getText().toString());
                sms.setNombre(nombre);
                sms.setTipo("texto");
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
            case R.id.record:
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 200:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {

        grabar.setBackgroundColor(GREEN);
        grabar.setTextColor(BLACK);

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            recorder.prepare();
        } catch (IOException e) {
        }

        recorder.start();
    }

    private void stopRecording() {

        Button grabar = findViewById(R.id.record);
        grabar.setBackgroundColor(RED);
        grabar.setTextColor(WHITE);

        recorder.stop();
        recorder.release();
        recorder = null;

        File file = new File(fileName);
        byte[] bytesArray = new byte[(int) file.length()];

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(bytesArray); //read file into bytes[]
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference storageRef = storage.getReference();
        final java.util.Calendar c = java.util.Calendar.getInstance();
        final StorageReference ref = storageRef.child("audios_chat/" + idchat + "/" + c.getTimeInMillis() + ".aac");
        final UploadTask uploadTask = ref.putBytes(bytesArray);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("qwa", "falla");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.child("audios_chat/" + idchat + "/" + c.getTimeInMillis() + ".aac").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Mensaje sms = new Mensaje();
                        sms.getFecha();
                        sms.setMensaje(uri.toString());
                        sms.setNombre(nombre);
                        sms.setTipo("audio");
                        FirebaseFirestore.getInstance().collection("chat").document(idchat).collection("mensajes").add(sms);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            }
        });



    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }
}
