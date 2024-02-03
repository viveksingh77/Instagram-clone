package com.iswherevivek.instaclone.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iswherevivek.instaclone.R;
import com.iswherevivek.instaclone.databinding.FragmentAddpostBinding;
import com.iswherevivek.instaclone.modals.PostModal;
import com.iswherevivek.instaclone.modals.User;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class addpost extends Fragment {
    FragmentAddpostBinding binding;
    Uri uri;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;


    public addpost() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddpostBinding.inflate(inflater, container, false);
        binding.writebtn.requestFocus();
        //for enable disable post button
        binding.writebtn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String description = binding.writebtn.getText().toString();
                if (!description.isEmpty()){
                    binding.postbtn.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.buttonbackground));
                    binding.postbtn.setEnabled(true);
                }else {
                    binding.postbtn.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.signup_bg_shape));
                    binding.postbtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,90);
            }
        });
        //for posting the img



        binding.postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.progressview.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.whiteshade));
                //path
                StorageReference reference = storage.getReference()
                        .child("posts")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(new Date().getTime()+"");
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                PostModal post = new PostModal();
                                post.setPostImage(uri.toString());
                                post.setPostAt(new Date().getTime());
                                post.setPostedBy(auth.getUid());
                                post.setPostDescription(binding.writebtn.getText().toString());

                                database.getReference().child("post")
                                        .push()
                                        .setValue(post)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                binding.progressBar.setVisibility(View.INVISIBLE);
                                                binding.progressview.setVisibility(View.GONE);
                                                Toast.makeText(getContext(), "post Uploaded", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                });
            }
        });



        //load the photo and name of user
        database.getReference()
                .child("Users")
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                      if (snapshot.exists()){
                          User user = snapshot.getValue(User.class);
                          Picasso.get().load(user.getProfile_pic())
                                  .placeholder(R.drawable.avatar)
                                  .into(binding.userPic);
                          binding.UserNameS.setText(user.getName());
                      }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData()!=null){
            uri = data.getData();
            binding.addpost.setVisibility(View.VISIBLE);
            binding.postbtn.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.buttonbackground));
            binding.postbtn.setEnabled(true);
            binding.addpost.setImageURI(uri);
        }
    }
}