package com.bluedot.bluedot_vale;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
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
    }

    public int siguiente(android.view.View V) {
        Intent intent = new Intent(this, AgregarActividad2.class);

        EditText titulo = findViewById(R.id.editText);
        Log.d("asd", "titulo: " + titulo.getText().toString());
        if(titulo.getText().toString().isEmpty()){
            Toast.makeText(this, "Introducir titulo.", Toast.LENGTH_SHORT).show();
            return 1;
        }
        else{
            intent.putExtra("titulo", titulo.getText().toString());
        }

        EditText descripcion = findViewById(R.id.editText5);
        if(descripcion.getText().toString().isEmpty()){
            Toast.makeText(this, "Introducir descripción.", Toast.LENGTH_SHORT).show();
            return 1;
        }
        else{
            intent.putExtra("descripcion", descripcion.getText().toString());
        }

        if(img_bytes == null){
            Toast.makeText(this, "Introducir imagen.", Toast.LENGTH_SHORT).show();
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
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                        img_bytes = baos.toByteArray();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }

    }
}