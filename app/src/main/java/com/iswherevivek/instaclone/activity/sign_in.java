package com.iswherevivek.instaclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.iswherevivek.instaclone.databinding.ActivitySignInBinding;

public class sign_in extends AppCompatActivity {
    ActivitySignInBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser currentuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentuser = auth.getCurrentUser();
        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.signInEmail.getText().toString();
                String password = binding.signInPassword.getText().toString();
                auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    startActivity(new Intent(sign_in.this,MainActivity.class));
                                }
                                else {
                                    Toast.makeText(sign_in.this, "No user Found ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        binding.signInSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(sign_in.this, sign_up.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentuser!=null){
            startActivity(new Intent(sign_in.this,MainActivity.class));
        }
    }
}