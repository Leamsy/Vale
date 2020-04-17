package com.bluedot.bluedot_vale;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu_tutor extends AppCompatActivity implements View.OnClickListener{

    public Button mBtnLogout;
    private FirebaseAuth mAuth;
    public ImageView listado;
    public ImageView salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tutor);

        mAuth = FirebaseAuth.getInstance();

        mBtnLogout = findViewById(R.id.cerrar_sesion2);
        listado = findViewById(R.id.imlista);
        salir = findViewById(R.id.salir);


        mBtnLogout.setOnClickListener(this);
        listado.setOnClickListener(this);
        salir.setOnClickListener(this);
    }

    public void cambiarAutorizaciones(){
        Intent intent = new Intent(this, ListaAutorizaciones.class);
        startActivity(intent);
    }

    public void cerrarApp(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            Intent intent = new Intent(Menu_tutor.this, Login.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cerrar_sesion2:
                signOut();
                break;
            case R.id.imlista:
                cambiarAutorizaciones();
                break;
            case R.id.salir:
                cerrarApp();
                break;
        }
    }


}
