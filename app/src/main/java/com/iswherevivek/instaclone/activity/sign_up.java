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
import com.google.firebase.database.FirebaseDatabase;
import com.iswherevivek.instaclone.databinding.ActivitySignUpBinding;
import com.iswherevivek.instaclone.modals.User;

public class sign_up extends AppCompatActivity {
    ActivitySignUpBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //create user
        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.signUpName.getText().toString();
                String email = binding.signUpEmail.getText().toString();
                String password = binding.signUpPassword.getText().toString();
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                User user = new User(name, email, password);
                                String uid = task.getResult().getUser().getUid();
                                database.getReference()
                                        .child("Users")
                                        .child(uid)
                                        .setValue(user);
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(sign_up.this, MainActivity.class));
                                } else {
                                    Toast.makeText(sign_up.this, "Sorry we Found some issues", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        binding.signUpLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(sign_up.this, sign_in.class));
            }
        });
    }
}