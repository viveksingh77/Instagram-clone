package com.iswherevivek.instaclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iswherevivek.instaclone.R;
import com.iswherevivek.instaclone.adapter.CommentAdapter;
import com.iswherevivek.instaclone.databinding.CommentAcitivityBinding;
import com.iswherevivek.instaclone.modals.PostModal;
import com.iswherevivek.instaclone.modals.User;
import com.iswherevivek.instaclone.modals.commentModal;
import com.iswherevivek.instaclone.modals.notificationModal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class comment extends AppCompatActivity {
    CommentAcitivityBinding binding;
    Intent intent;
    String postId,postBy;
    FirebaseAuth auth ;
    FirebaseDatabase database;
    ArrayList<commentModal> commentModals = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CommentAcitivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        intent = getIntent();
        postId = intent.getStringExtra("postId");
        postBy = intent.getStringExtra("postBy");
        database.getReference().child("post")
                .child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        PostModal postModal = snapshot.getValue(PostModal.class);
                        Picasso.get().load(postModal.getPostImage()).placeholder(R.drawable.avatar2).into(binding.post);
                        binding.descriptionC.setText(postModal.getPostDescription());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        database.getReference().child("Users")
                .child(postBy)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfile_pic()).placeholder(R.drawable.avatar).into(binding.postPic);
                        binding.username.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentModal comment = new commentModal();
                comment.setCommentBody(binding.writeComment.getText().toString());
                comment.setCommentedAt(new Date().getTime());
                comment.setCommentedBy(auth.getUid());
                database.getReference().child("post")
                        .child(postId)
                        .child("comments")
                        .push()
                        .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference()
                                        .child("post")
                                        .child(postId)
                                        .child("comment_count")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int comment_count =0;
                                                if (snapshot.exists()){
                                                    comment_count= snapshot.getValue(Integer.class);
                                                }
                                                database.getReference().child("post")
                                                        .child(postId)
                                                        .child("comment_count")
                                                        .setValue(comment_count+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                binding.writeComment.setText("");
                                                                Toast.makeText(comment.this, "commented", Toast.LENGTH_SHORT).show();

                                                                //sending notification
                                                                notificationModal notificationComment = new notificationModal();
                                                                notificationComment.setPostedBy(postBy);
                                                                notificationComment.setPostId(postId);
                                                                notificationComment.setType("comment");
                                                                notificationComment.setNotificationAt(new Date().getTime());
                                                                notificationComment.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                FirebaseDatabase.getInstance().getReference().child("notification")
                                                                        .child(postBy)
                                                                        .push()
                                                                        .setValue(notificationComment);
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        });
            }
        });
        //setting the Adapter
        CommentAdapter commentAdapter = new CommentAdapter(comment.this,commentModals);
        binding.commentSectionC.setLayoutManager(new LinearLayoutManager(this));
        binding.commentSectionC.setAdapter(commentAdapter);

        //getting the data from database
        database.getReference().child("post")
                .child(postId)
                .child("comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        commentModals.clear();
                        for (DataSnapshot snap: snapshot.getChildren()) {
                            commentModal commentlist = snap.getValue(commentModal.class);
                            commentModals.add(commentlist);
                        }
                        commentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}