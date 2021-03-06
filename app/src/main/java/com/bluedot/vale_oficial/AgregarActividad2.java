package com.bluedot.vale_oficial;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AgregarActividad2 extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    String titulo;
    String descripcion;
    byte[] imagen;
    Context context = this;
    Map<String, Object> map= new HashMap<>();
    int dia, mes, año, hora, minutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_actividad2);

        Intent intent = getIntent();
        titulo = intent.getStringExtra("titulo");
        descripcion = intent.getStringExtra("descripcion");

        imagen = intent.getByteArrayExtra("imagen");
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

        dia = day;
        mes = month;
        año = year;
    }

    public void openTime(android.view.View V){
        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = (TextView) findViewById(R.id.hora);
        textView.setText(String.format("%02d",hourOfDay) + " : " + String.format("%02d",minute));

        hora = hourOfDay;
        minutos = minute;
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

        if(agregarCampos() == 0){
            findViewById(R.id.button3).setVisibility(View.GONE);
            FirebaseStorage storage = FirebaseStorage.getInstance();

            final StorageReference storageRef = storage.getReference();
            final StorageReference ref = storageRef.child("fotos_actividades/" + titulo + ".jpg");
            final UploadTask uploadTask = ref.putBytes(imagen);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("qwa", "falla");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.child("fotos_actividades/" + titulo + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            map.put("imagen", uri.toString());
                            subirActividad();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }
            });
        }
    }

    private int agregarCampos(){
        map.put("autor", FirebaseAuth.getInstance().getCurrentUser().getUid());
        map.put("titulo", titulo);
        map.put("descripcion", descripcion);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> map1= new HashMap<>();
        db.collection("chat").add(map1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                map.put("salachat", documentReference.getId());
            }
        });

        TextView fecha = findViewById(R.id.fecha);
        if(fecha.getText().toString().equals("Elegir fecha")){
            Toast.makeText(context, "Introducir fecha.", Toast.LENGTH_SHORT).show();
            return 1;
        }
        else{

            String dateString = año+"-"+(mes+1)+"-"+dia+" "+hora+":"+minutos;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            try {
                date = df.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Timestamp t = new Timestamp(date);
            map.put("fecha", t);
        }

        TextView hora = findViewById(R.id.hora);
        if(hora.getText().toString().equals("Hora")){
            Toast.makeText(context, "Introducir hora.", Toast.LENGTH_SHORT).show();
            return 1;
        }
        else{
        }

        TextView precio = findViewById(R.id.precio);
        if(precio.getText().toString().isEmpty()){
            Toast.makeText(context, "Introducir precio.", Toast.LENGTH_SHORT).show();
            return 1;
        }
        else{
            map.put("precio", precio.getText().toString());
        }

        TextView plazas_socios = findViewById(R.id.edit_socios);
        if(plazas_socios.getText().toString().isEmpty()){
            Toast.makeText(context, "Introducir plazas de socios.", Toast.LENGTH_SHORT).show();
            return 1;
        }
        else{
            map.put("plazas_socios", plazas_socios.getText().toString());
        }

        TextView plazas_voluntarios = findViewById(R.id.edit_voluntarios);
        if(plazas_voluntarios.getText().toString().isEmpty()){
            Toast.makeText(context, "Introducir plazas de voluntarios.", Toast.LENGTH_SHORT).show();
            return 1;
        }
        else{
            int n = Integer.parseInt(plazas_voluntarios.getText().toString());
            if(n>=1){
                n--;
            }

            map.put("plazas_voluntarios", n);
        }

        CheckBox requiere_autorizacion = findViewById(R.id.requiere_autorizacion);
        map.put("requiere_autorizacion", requiere_autorizacion.isChecked());

        return 0;
    }

    private void subirActividad(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("actividades").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                Map<String, Object> map1= new HashMap<>();

                db.collection("actividades").document(documentReference.getId()).collection("apuntados").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(map1);
                db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("mis_actividades").document(documentReference.getId()).set(map1);

                Toast.makeText(context, "La actividad ha sido enviada.", Toast.LENGTH_SHORT).show();
                //finish();
                Intent intent = new Intent(AgregarActividad2.this, Submenu_Actividades.class);
                startActivity(intent);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    public void irAcasa(android.view.View V){
        Intent intent = new Intent(AgregarActividad2.this, Principal.class);
        startActivity(intent);
        finish();
    }


    public void mensaje(android.view.View V){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Si la actividad conlleva un riesgo, por mínimo que sea, para el usuario. Si tiene dudas de si la actividad debe requerir autorización, por favor, pongase en contacto con la asociación Vale");
        dialog.setTitle("Se requerirá autorización cuando...");
        dialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }
}
