package com.bluedot.bluedot_vale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

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

        EditText titulo = findViewById(R.id.editText);
        intent.putExtra("titulo", titulo.getText().toString());
        EditText descripcion = findViewById(R.id.editText5);
        intent.putExtra("descripcion", descripcion.getText().toString());

        ImageView imagen = findViewById(R.id.imageView22);
        imagen.setDrawingCacheEnabled(true);
        imagen.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imagen.getDrawable()).getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        intent.putExtra("imagen", byteArray);
        startActivity(intent);
        finish();
    }

    public void volver(android.view.View V) {
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
                    Uri selectedImage = data.getData();
                    prueba.setImageURI(selectedImage);
                    break;
            }
        }

    }
}