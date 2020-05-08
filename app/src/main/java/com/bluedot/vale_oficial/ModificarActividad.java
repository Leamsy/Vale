package com.bluedot.vale_oficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ModificarActividad extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


    String uid;
    String uid_act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_actividad);

        Intent intent = getIntent();
        uid_act = intent.getStringExtra("uid");

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DocumentReference docRef = FirebaseFirestore.getInstance().collection("actividades").document(uid_act);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Button fecha = findViewById(R.id.fecha);
                        fecha.setText(document.getData().get("fecha").toString());

                        Button hora = findViewById(R.id.hora);
                        hora.setText(document.getData().get("hora").toString());

                        EditText p_socios = findViewById(R.id.edit_socios);
                        p_socios.setText(document.getData().get("plazas_socios").toString());

                        EditText p_voluntarios = findViewById(R.id.edit_voluntarios);
                        p_voluntarios.setText(document.getData().get("plazas_voluntarios").toString());

                        EditText pre = findViewById(R.id.precio);
                        pre.setText(document.getData().get("precio").toString());
                    }
                }
            }
        });

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
        textView.setText(String.format("%02d",hourOfDay) + " : " + String.format("%02d",minute));
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

    public void modificar(android.view.View V){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        Button fecha = findViewById(R.id.fecha);
        Button hora = findViewById(R.id.hora);
        EditText p_socios = findViewById(R.id.edit_socios);
        EditText p_voluntarios = findViewById(R.id.edit_voluntarios);
        EditText precio = findViewById(R.id.precio);

        db.collection("actividades").document(uid_act).update("fecha", fecha.getText().toString());
        db.collection("actividades").document(uid_act).update("hora", hora.getText().toString());
        db.collection("actividades").document(uid_act).update("precio", precio.getText().toString());
        db.collection("actividades").document(uid_act).update("plazas_socios", p_socios.getText().toString());
        db.collection("actividades").document(uid_act).update("plazas_voluntarios", p_voluntarios.getText().toString());

        Toast.makeText(this, "Actividad modificada.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, Mis_actividades.class);
        startActivity(intent);
        finish();
    }

    public void volver(android.view.View V) {
        finish();
    }
}
