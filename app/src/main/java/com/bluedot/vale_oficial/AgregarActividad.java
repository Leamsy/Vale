package com.bluedot.vale_oficial;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import static android.view.View.INVISIBLE;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AgregarActividad extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 100;

    Uri img_uri;
    byte[] img_bytes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_actividad);

        findViewById(R.id.prueba).setVisibility(View.GONE);
    }

    public int siguiente(android.view.View V) {
        Intent intent = new Intent(this, AgregarActividad2.class);

        EditText titulo = findViewById(R.id.editText);
        Log.d("asd", "titulo: " + titulo.getText().toString());
        if(titulo.getText().toString().isEmpty()){
            Toast.makeText(this, "Introduce el título de la actividad", Toast.LENGTH_SHORT).show();
            return 1;
        }
        else{
            intent.putExtra("titulo", titulo.getText().toString());
        }

        EditText descripcion = findViewById(R.id.editText5);
        if(descripcion.getText().toString().isEmpty()){
            Toast.makeText(this, "Introducir la descripción de la actividad", Toast.LENGTH_SHORT).show();
            return 1;
        }
        else{
            intent.putExtra("descripcion", descripcion.getText().toString());
        }

        if(img_bytes == null){
            Toast.makeText(this, "Introduce una imagen descriptiva de la actividad", Toast.LENGTH_SHORT).show();
            return 1;
        }
        else{
            intent.putExtra("imagen", img_bytes);
        }

        startActivity(intent);
        finish();
        return 0;
    }

    public void volver(android.view.View V) {
        finish();
    }

    public void irAcasa(android.view.View V){
        Intent intent = new Intent(AgregarActividad.this, Principal.class);
        startActivity(intent);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    ImageView prueba = findViewById(R.id.prueba);
                    Button button = findViewById(R.id.imagen);
                    button.setVisibility(INVISIBLE);
                    img_uri = data.getData();
                    prueba.setImageURI(img_uri);


                    try {
                        ContentResolver cr = getBaseContext().getContentResolver();
                        InputStream inputStream = cr.openInputStream(img_uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                        img_bytes = baos.toByteArray();
                        findViewById(R.id.imagen).setVisibility(View.GONE);
                        findViewById(R.id.prueba).setVisibility(View.VISIBLE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }

    }
}