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
import com.iswherevivek.instaclone.adapter.usernotifyAdapter;
import com.iswherevivek.instaclone.databinding.FragmentSubnotificationBinding;
import com.iswherevivek.instaclone.modals.notificationModal;

import java.util.ArrayList;

public class subnotification extends Fragment {
    FragmentSubnotificationBinding binding;
    ArrayList<notificationModal> notificationArray = new ArrayList<>();
    usernotifyAdapter usernotifyAdapter;
    FirebaseDatabase database ;

    public subnotification() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentSubnotificationBinding.inflate(inflater,container,false);
        database = FirebaseDatabase.getInstance();
        database.getReference().child("notification")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot snap : snapshot.getChildren()) {
                            notificationModal notification =snap.getValue(notificationModal.class);
                            notification.setNotificationId(snap.getKey());
                            notificationArray.add(notification);
                        }
                        usernotifyAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        usernotifyAdapter= new usernotifyAdapter(getContext(),notificationArray);
        binding.recyclerview.setAdapter(usernotifyAdapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }
}