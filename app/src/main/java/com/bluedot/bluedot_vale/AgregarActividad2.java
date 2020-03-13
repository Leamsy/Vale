package com.bluedot.bluedot_vale;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AgregarActividad2 extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_actividad2);
    }

    public void volver(android.view.View V) {
        finish();
    }

    public void openDate(android.view.View V){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        TextView textView = (TextView) findViewById(R.id.fecha);
        textView.setText(day + "/" + (month+1) + "/" + year);
    }

    public void openTime(android.view.View V){
        DialogFragment timePicker = new TimePickerFragment();
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
        /*
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://vale-web.000webhostapp.com/wp-json/vale/v1/actividad/nueva";

        JSONObject jsonBody = new JSONObject();

        try {
            TextView myTextView = findViewById(R.id.editText);
            jsonBody.put("titulo", myTextView.getText().toString());
            myTextView = findViewById(R.id.editText5);
            jsonBody.put("descripcion", myTextView.getText().toString());
            jsonBody.put("fecha_desde", "00:00:00");
            jsonBody.put("fecha_hasta", "2021-09-20 00:00:00");
            CheckBox check = findViewById(R.id.checkBox);
            jsonBody.put("actividad_especial", check.isChecked());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("asdf","mal");
                Log.i("asdf",error.getMessage());
            }}){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = Global.user + ":" + Global.pass;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        requestQueue.add(objectRequest);
        */


        finish();
    }
}
