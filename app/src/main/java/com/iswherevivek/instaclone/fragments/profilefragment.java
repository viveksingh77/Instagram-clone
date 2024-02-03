package com.iswherevivek.instaclone.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.iswherevivek.instaclone.adapter.FollowerAdapter;
import com.iswherevivek.instaclone.databinding.FragmentProfilefragmentBinding;
import com.iswherevivek.instaclone.modals.User;
import com.iswherevivek.instaclone.modals.Follow;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class profilefragment extends Fragment {
    FragmentProfilefragmentBinding binding;
    final int TAKING_PIC_CODE = 100;
    final int TAKING_PROFILE_PIC_CODE = 12;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;

    public profilefragment() {
        // Required empty public constructor
    }


    FollowerAdapter adapter;
    ArrayList<Follow> friendModals;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfilefragmentBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        //fetching the data  cover pic
        database.getReference()
                .child("Users")
                .child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            Picasso.get()
                                    .load(user.getCover_photo())
                                    .placeholder(R.drawable.avatar2)
                                    .into(binding.coverPic);
                            binding.profileUsername.setText(user.getName());
                            Picasso.get().load(user.getProfile_pic()).placeholder(R.drawable.avatar).into(binding.profilePic);
                            binding.followesP.setText(user.getFollower_count()+"");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        //for Followers list
        friendModals = new ArrayList<>();
        adapter = new FollowerAdapter(getContext(), friendModals);
        binding.profileRV.setAdapter(adapter);
        binding.profileRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        database.getReference()
                .child("Users")
                .child(auth.getUid())
                .child("followers")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap: snapshot.getChildren()) {
                          Follow follow =snap.getValue(Follow.class);
                          friendModals.add(follow);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, TAKING_PIC_CODE);
            }
        });
        binding.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, TAKING_PROFILE_PIC_CODE);
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            assert data != null;
            if (requestCode == TAKING_PIC_CODE) {
                if (data.getData() != null) {
                    binding.coverPic.setImageURI(data.getData());
                    //storing cover photo in Storage
                    final StorageReference reference = storage.getReference().child("cover_photo")
                            .child(FirebaseAuth.getInstance().getUid());
                    reference.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "cover photo saved", Toast.LENGTH_SHORT).show();
                            //when photo is saved in storage then we store in database and set it as a cover photo
                            //getdownloadurl  get the url of user
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    database.getReference()
                                            .child("Users")
                                            .child(auth.getUid())
                                            .child("cover_photo")
                                            .setValue(uri.toString());
                                }
                            });
                        }
                    });
                }
            }
            if (requestCode == TAKING_PROFILE_PIC_CODE) {
                if (data.getData() != null) {
                    // binding.profilePic.setImageURI(data.getData());
                    StorageReference reference = storage.getReference()
                            .child("profile_pic")
                            .child(auth.getUid());
                    reference.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "profile pic updated", Toast.LENGTH_SHORT).show();
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    database.getReference().child("Users")
                                            .child(auth.getUid())
                                            .child("profile_pic")
                                            .setValue(uri.toString());
                                }
                            });
                        }
                    });
                }
            }
        }
    }

}