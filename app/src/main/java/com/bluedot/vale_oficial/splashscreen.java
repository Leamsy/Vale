package com.bluedot.vale_oficial;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class splashscreen extends AppCompatActivity {

    private final int DURACION_SPLASH = 1000;
    private FirebaseAuth mAuth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        uid = null;
        mAuth = null;

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Toast.makeText(this, currentUser.toString() , Toast.LENGTH_LONG).show();
            updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if(isNetworkAvailable()){
            if (user != null) {
                uid = mAuth.getUid();

                DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(uid);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                if(document.getData().get("rol").toString().equals("tutor")){
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(splashscreen.this, Menu_tutor.class);
                                            startActivity(intent);
                                            finish();
                                        };
                                    }, DURACION_SPLASH);
                                }
                                else if (document.getData().get("activo").toString().equals("false")){
                                    mAuth.signOut();
                                    Toast.makeText(splashscreen.this, "No eres un usuario autorizado",
                                            Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(splashscreen.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(splashscreen.this, Principal.class);
                                            startActivity(intent);
                                            finish();
                                        };
                                    }, DURACION_SPLASH);
                                }

                            } else {
                                mAuth.signOut();
                                Intent intent = new Intent(splashscreen.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            mAuth.signOut();
                            Intent intent = new Intent(splashscreen.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
            else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(splashscreen.this, Login.class);
                        startActivity(intent);
                        finish();
                    };
                }, DURACION_SPLASH);
            }
        }
        else{
            Toast.makeText(this, "Revise su conexión a Internet", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
