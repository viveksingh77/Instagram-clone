package com.iswherevivek.instaclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iswherevivek.instaclone.R;
import com.iswherevivek.instaclone.adapter.usersearchAdapter;
import com.iswherevivek.instaclone.databinding.FragmentSearchfragmentBinding;
import com.iswherevivek.instaclone.modals.User;

import java.util.ArrayList;


public class searchfragment extends Fragment {
    FragmentSearchfragmentBinding binding;
    ArrayList<User> userArrayList;
    usersearchAdapter adapter;
    FirebaseDatabase database;
    FirebaseAuth auth;

    public searchfragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchfragmentBinding.inflate(inflater,container,false);
        userArrayList = new ArrayList<>();
        adapter = new usersearchAdapter(getContext(),userArrayList);
        binding.searchRV.setAdapter(adapter);
        binding.searchRV.setLayoutManager( new LinearLayoutManager(getContext()));
        database.getReference()
                .child("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userArrayList.clear();
                        for (DataSnapshot snapshot1: snapshot.getChildren()) {
                            User user = snapshot1.getValue(User.class);
                            user.setUserID(snapshot1.getKey());
                            if (!snapshot1.getKey().equals(FirebaseAuth.getInstance().getUid())){
                                userArrayList.add(user);
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        return binding.getRoot();
    }
}