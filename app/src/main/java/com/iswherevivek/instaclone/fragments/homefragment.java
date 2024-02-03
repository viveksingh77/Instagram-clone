package com.iswherevivek.instaclone.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iswherevivek.instaclone.adapter.postAdapter;
import com.iswherevivek.instaclone.adapter.storyadapter;
import com.iswherevivek.instaclone.databinding.FragmentHomefragmentBinding;
import com.iswherevivek.instaclone.modals.PostModal;
import com.iswherevivek.instaclone.modals.Storymodal;
import com.iswherevivek.instaclone.modals.userStories;
import java.util.ArrayList;
import java.util.Date;

public class homefragment extends Fragment {
    ArrayList<Storymodal> storyList;
    ArrayList<PostModal> postList;

    storyadapter storyadapter ;
    postAdapter postAdapter;
    FirebaseDatabase database ;
    FirebaseAuth auth;
    ActivityResultLauncher<String> galleryLauncher;
    FirebaseStorage storage;

    public homefragment() {
    // Required empty public constructor
}
FragmentHomefragmentBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        database=FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage =FirebaseStorage.getInstance();
       // binding.scrollPostLayout.showShimmerAdapter();

        // Inflate the layout for this fragment
       binding = FragmentHomefragmentBinding.inflate(inflater, container, false);
       //story
       storyList = new ArrayList<>();
       storyadapter = new storyadapter(storyList,getContext());
       binding.storySection.setAdapter(storyadapter);
       binding.storySection.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

       database.getReference().child("story").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   storyList.clear();
                   for (DataSnapshot storysnap: snapshot.getChildren()) {
                       Storymodal story = new Storymodal();
                       story.setStoryBy(storysnap.getKey());
                       story.setStoryAt(storysnap.child("lastUpdated").getValue(Long.class));
                       ArrayList<userStories> stories = new ArrayList<>();
                       for (DataSnapshot snap1: storysnap.child("userStories").getChildren()) {
                           userStories userStories = snap1.getValue(userStories.class);
                           stories.add(userStories);
                       }
                       story.setStories(stories);
                       storyList.add(story);
                   }
                   storyadapter.notifyDataSetChanged();
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
       binding.yourStory.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               galleryLauncher.launch("image/*");

           }
       });
       //taking image from gallery
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent()
                , new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
//                        binding.yourStory.setImageURI(result);
                        //path
                        final StorageReference reference = storage.getReference().child("stories")
                                .child(auth.getUid())
                                .child(new Date().getTime()+"");
                        reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Storymodal story = new Storymodal();
                                        story.setStoryAt(new Date().getTime());

                                   database.getReference().child("story")
                                           .child(auth.getUid())
                                           .child("lastUpdated")
                                           .setValue(story.getStoryAt())
                                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void unused) {
                                                   userStories Stories = new userStories(uri.toString(),story.getStoryAt());
                                                   database.getReference().child("story")
                                                           .child(auth.getUid())
                                                           .child("userStories")
                                                           .push()
                                                           .setValue(Stories);
                                               }
                                           });
                                    }
                                });
                            }
                        });
                    }
                });













       //post
       postList = new ArrayList<>();
        database.getReference().child("post")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        postList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()){
                            PostModal post = snap.getValue(PostModal.class);
                            post.setPostID(snap.getKey());
                            postList.add(post);
                        }
                       // binding.scrollPostLayout.hideShimmerAdapter();
                        postAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        postAdapter= new postAdapter(getContext(),postList);
        binding.scrollPostLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.scrollPostLayout.setAdapter(postAdapter);



       return binding.getRoot();
    }
}