package com.bluedot.bluedot_vale;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AgregarActividad2 extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    String titulo;
    String descripcion;
    Bitmap imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_actividad2);

        Intent intent = getIntent();
        titulo = intent.getStringExtra("titulo");
        descripcion = intent.getStringExtra("descripcion");

        byte[] byteArray = getIntent().getByteArrayExtra("imagen");
        imagen = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public void volver(android.view.View V) {
        finish();
    }

    public void openDate(android.view.View V){
        DatePickerFragment  datePicker = new DatePickerFragment ();
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        TextView textView = (TextView) findViewById(R.id.fecha);
        textView.setText(day + "/" + (month+1) + "/" + year);
    }

    public void openTime(android.view.View V){
        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = (TextView) findViewById(R.id.hora);
        textView.setText(hourOfDay + " : " + minute);
    }

    public void mas_s(android.view.View V){
        EditText text = findViewById(R.id.edit_socios);
        int n = Integer.parseInt(text.getText().toString());
        n++;
        text.setText(String.valueOf(n));
    }

    public void menos_s(android.view.View V){
        EditText text = findViewById(R.id.edit_socios);
        int n = Integer.parseInt(text.getText().toString());
        if(n>1){
            n--;
        }
        text.setText(String.valueOf(n));
    }

    public void mas_v(android.view.View V){
        EditText text = findViewById(R.id.edit_voluntarios);
        int n = Integer.parseInt(text.getText().toString());
        n++;
        text.setText(String.valueOf(n));
    }

    public void menos_v(android.view.View V){
        EditText text = findViewById(R.id.edit_voluntarios);
        int n = Integer.parseInt(text.getText().toString());
        if(n>1){
            n--;
        }
        text.setText(String.valueOf(n));
    }

    public void enviar(android.view.View V) {

        Toast.makeText(this, "La actividad ha sido enviada.", Toast.LENGTH_SHORT).show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> map= new HashMap<>();
        map.put("titulo", titulo);
        map.put("descripcion", descripcion);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagen.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference storageRef = storage.getReference();
        StorageReference ref = storageRef.child("images/" + titulo + ".jpg");
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("qwa", "falla");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

        TextView fecha = findViewById(R.id.fecha);
        map.put("fecha", fecha.getText().toString());
        TextView hora = findViewById(R.id.hora);
        map.put("hora", hora.getText().toString());
        TextView precio = findViewById(R.id.precio);
        map.put("precio", precio.getText().toString());
        TextView plazas_socios = findViewById(R.id.edit_socios);
        map.put("plazas_socios", plazas_socios.getText().toString());
        TextView plazas_voluntarios = findViewById(R.id.edit_voluntarios);
        map.put("plazas_voluntarios", plazas_voluntarios.getText().toString());
        TextView requiere_autorizacion = findViewById(R.id.requiere_autorizacion);
        map.put("requiere_autorizacion", requiere_autorizacion.getText().toString());

        db.collection("actividades").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference documentReference) {
            Log.d("aa", "DocumentSnapshot written with ID: " + documentReference.getId());
        }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("aa", "Error adding document", e);
            }
        });


        finish();
    }
}
