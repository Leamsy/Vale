package com.bluedot.bluedot_vale;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static android.view.View.INVISIBLE;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AgregarActividad extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_actividad);
    }

    public void siguiente(android.view.View V) {
        Intent intent = new Intent(this, AgregarActividad2.class);
        startActivity(intent);
        finish();
    }

    public void volver(android.view.View V) {
        finish();
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

    public void pickFromGallery(android.view.View V) {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    ImageView prueba = findViewById(R.id.prueba);
                    Button button = findViewById(R.id.imagen);
                    button.setVisibility(INVISIBLE);
                    Uri selectedImage = data.getData();
                    prueba.setImageURI(selectedImage);
                    break;
            }
        }

    }
}