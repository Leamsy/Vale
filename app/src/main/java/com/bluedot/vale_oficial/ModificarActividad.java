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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ModificarActividad extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


    String uid;
    String uid_act;
    int dia, mes, año;
    int hora, minutos;
    private boolean cambiado;
    private boolean cambiadohora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_actividad);

        Intent intent = getIntent();
        uid_act = intent.getStringExtra("uid");
        cambiado = false;
        cambiadohora = false;

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DocumentReference docRef = FirebaseFirestore.getInstance().collection("actividades").document(uid_act);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Timestamp fecha = (Timestamp)document.getData().get("fecha");
                        Date date = new Date(fecha.getSeconds()*1000);
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat dfh = new SimpleDateFormat("HH:mm");
                        df.setTimeZone(TimeZone.getTimeZone("UTC"));
                        dfh.setTimeZone(TimeZone.getTimeZone("UTC"));
                        String formattedDate = df.format(date);
                        String formattedDateH = dfh.format(date);

                        Button fecha_b = findViewById(R.id.fecha);
                        fecha_b.setText(formattedDate);

                        Button hora = findViewById(R.id.hora);
                        hora.setText(formattedDateH);

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

        dia = day;
        mes = month;
        año = year;
        cambiado = true;
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
        cambiadohora = true;
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

        EditText p_socios = findViewById(R.id.edit_socios);
        EditText p_voluntarios = findViewById(R.id.edit_voluntarios);
        EditText precio = findViewById(R.id.precio);

        int voluntarios = Integer.parseInt(p_voluntarios.getText().toString());
        int socios = Integer.parseInt(p_socios.getText().toString());

        if(cambiadohora && cambiado) {
            String dateString = año + "-" + (mes + 1) + "-" + dia + " " + hora + ":" + minutos;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            try {
                date = df.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Timestamp t = new Timestamp(date);

            db.collection("actividades").document(uid_act).update("fecha", t);

            db.collection("actividades").document(uid_act).update("precio", precio.getText().toString());
            db.collection("actividades").document(uid_act).update("plazas_socios", socios);
            db.collection("actividades").document(uid_act).update("plazas_voluntarios", voluntarios);

            Toast.makeText(this, "Actividad modificada.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, Mis_actividades.class);
            startActivity(intent);
            finish();

        }else if(!cambiado && cambiadohora){
            Toast.makeText(ModificarActividad.this, "Establece la fecha nuevamente",
                    Toast.LENGTH_LONG).show();
        }

        else if(cambiado && !cambiadohora){
            Toast.makeText(ModificarActividad.this, "Establece la hora nuevamente",
                    Toast.LENGTH_LONG).show();
        }else{
            db.collection("actividades").document(uid_act).update("precio", precio.getText().toString());
            db.collection("actividades").document(uid_act).update("plazas_socios", socios);
            db.collection("actividades").document(uid_act).update("plazas_voluntarios", voluntarios);

            Toast.makeText(this, "Actividad modificada.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, Mis_actividades.class);
            startActivity(intent);
            finish();
        }
    }

    public void volver(android.view.View V) {
        finish();
    }
}
