package com.iswherevivek.instaclone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iswherevivek.instaclone.R;
import com.iswherevivek.instaclone.activity.comment;
import com.iswherevivek.instaclone.databinding.PostrvBinding;
import com.iswherevivek.instaclone.modals.PostModal;
import com.iswherevivek.instaclone.modals.User;
import com.iswherevivek.instaclone.modals.notificationModal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class postAdapter extends RecyclerView.Adapter<postAdapter.ViewHolder> {
    Context context;
    ArrayList<PostModal> postModalArrayList;

    public postAdapter(Context context, ArrayList<PostModal> postModalArrayList) {
        this.context = context;
        this.postModalArrayList = postModalArrayList;
    }


    @NonNull
    @Override
    public postAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.postrv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull postAdapter.ViewHolder holder, int position) {
        PostModal modal = postModalArrayList.get(position);
        Picasso.get().load(modal.getPostImage())
                .placeholder(R.drawable.avatar2).into(holder.binding.postPost);
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(modal.getPostedBy()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfile_pic())
                                .placeholder(R.drawable.avatar)
                                .into(holder.binding.postPic);
                        holder.binding.postName.setText(user.getName());

                        if (!modal.getPostDescription().isEmpty()) {
                            holder.binding.postDescription.setText(modal.getPostDescription());
                            holder.binding.postDescription.setVisibility(View.VISIBLE);

                        } else {
                            holder.binding.postDescription.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        FirebaseDatabase.getInstance().getReference()
                .child("post")
                .child(modal.getPostID())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            holder.binding.likepost.setImageResource(R.drawable.love);
                        }
                        else {
                            holder.binding.likepost.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FirebaseDatabase.getInstance().getReference().child("post")
                                            .child(modal.getPostID())
                                            .child("likes")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("post")
                                                            .child(modal.getPostID())
                                                            .child("like_count")
                                                            .setValue(modal.getLike_count() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.binding.likepost.setImageResource(R.drawable.love);

                                                                    //sending notification
                                                                    notificationModal notificationLike = new notificationModal();
                                                                    notificationLike.setType("like");
                                                                    notificationLike.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notificationLike.setNotificationAt(new Date().getTime());
                                                                    notificationLike.setPostId(modal.getPostID());
                                                                    notificationLike.setPostedBy(modal.getPostedBy());
                                                                    FirebaseDatabase.getInstance().getReference().child("notification")
                                                                            .child(modal.getPostedBy())
                                                                            .push().setValue(notificationLike);

                                                                }
                                                            });
                                                }
                                            });
                                }
                            });

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.binding.likepostnum.setText(modal.getLike_count()+"");
        holder.binding.commentpostnum.setText(modal.getComment_count()+"");
        holder.binding.commentpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, comment.class);
                intent.putExtra("postId",modal.getPostID());
                intent.putExtra("postBy",modal.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return postModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PostrvBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = PostrvBinding.bind(itemView);
        }
    }
}
