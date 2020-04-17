package com.bluedot.bluedot_vale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private static final String TAG = "Guardar en firebase";
    Boolean jugador=false;
    Boolean juego=false;
    Boolean playa=false;
    Boolean flamenco=false;
    Boolean paseo=false;
    Boolean corriendo=false;
    String uid;

    private EditText useremail;
    private String pass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Instancia de Firebase
        mAuth = FirebaseAuth.getInstance();
        useremail = (EditText) findViewById(R.id.logintxt);
    }

    public void clicklogin(){

        String email = useremail.getText().toString().trim();


        if (email.isEmpty()){
            Toast.makeText(Login.this, "Campo email está vacío",
                    Toast.LENGTH_SHORT).show();
        }
        else if (pass.isEmpty() || pass.length() < 3){
            Toast.makeText(Login.this, "El campo contraseña debe tener al menos una imagen seleccionada",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(Login.this, "Bienvenido",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();

                                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(uid);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {

                                                if(document.getData().get("rol").toString().equals("tutor")){
                                                    startActivity(new Intent(Login.this, Menu_tutor.class));
                                                    finish();
                                                }
                                                else{
                                                    startActivity(new Intent(Login.this, Principal.class));
                                                    finish();
                                                }

                                            } else {
                                            }
                                        } else {
                                        }
                                    }
                                });

                            } else{
                                /*if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(RegisterActivity.this, "El usuario ya existe",
                                            Toast.LENGTH_SHORT).show();
                                } else {*/
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Login.this, "Login incorrecto o fallo de red",
                                        Toast.LENGTH_SHORT).show();
                                //}
                            }

                            // ...
                        }


                    });
        }

    }//Fin clicklogin

    public void clickjugador(){
        if(!jugador){
            jugador = true;
            findViewById(R.id.jugador).setAlpha((float)0.4);
            pass += "P1a";
        }
    }
    public void clickjuegomesa(){
        if(!juego){
            juego = true;
            findViewById(R.id.juegomesa).setAlpha((float)0.4);
            pass += "g4m";
        }
    }

    public void clickplaya(){
        if(!playa){
            playa = true;
            findViewById(R.id.playa).setAlpha((float)0.4);
            pass += "c0s";
        }
    }

    public void clickflamenco(){
        if(!flamenco){
            flamenco = true;
            findViewById(R.id.flamenco).setAlpha((float)0.4);
            pass += "f1A";
        }
    }

    public void clickpaseo(){
        if(!paseo){
            paseo = true;
            findViewById(R.id.paseo).setAlpha((float)0.4);
            pass += "W41";
        }
    }

    public void clickcorriendo(){
        if(!corriendo){
            corriendo = true;
            findViewById(R.id.corriendo).setAlpha((float)0.4);
            pass += "RuNn";
        }
    }

    public void clickgoma(){
        jugador=false;
        juego=false;
        playa=false;
        flamenco=false;
        paseo=false;
        corriendo=false;
        findViewById(R.id.jugador).setAlpha((float)1);
        findViewById(R.id.juegomesa).setAlpha((float)1);
        findViewById(R.id.playa).setAlpha((float)1);
        findViewById(R.id.flamenco).setAlpha((float)1);
        findViewById(R.id.paseo).setAlpha((float)1);
        findViewById(R.id.corriendo).setAlpha((float)1);
        pass="";
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jugador:
                clickjugador();
                break;
            case R.id.juegomesa:
                clickjuegomesa();
                break;
            case R.id.playa:
                clickplaya();
                break;
            case R.id.flamenco:
                clickflamenco();
                break;
            case R.id.paseo:
                clickpaseo();
                break;
            case R.id.corriendo:
                clickcorriendo();
                break;
            case R.id.goma:
                clickgoma();
                break;
            case R.id.loginbutton:
                clicklogin();
                break;
        }
    }
}
