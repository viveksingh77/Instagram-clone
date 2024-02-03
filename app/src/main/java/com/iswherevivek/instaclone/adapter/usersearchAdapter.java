package com.iswherevivek.instaclone.adapter;

import android.app.Notification;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.iswherevivek.instaclone.R;
import com.iswherevivek.instaclone.databinding.SearchusersampleBinding;
import com.iswherevivek.instaclone.modals.Follow;
import com.iswherevivek.instaclone.modals.User;
import com.iswherevivek.instaclone.modals.notificationModal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class usersearchAdapter extends RecyclerView.Adapter<usersearchAdapter.ViewHolder> {
    Context context;

    public usersearchAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    ArrayList<User> users = new ArrayList<>();


    @NonNull
    @Override
    public usersearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.searchusersample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull usersearchAdapter.ViewHolder holder, int position) {
        //by this we take id of particular user
        User user = users.get(position);
        Picasso.get()
                .load(user.getProfile_pic())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.userPic);
        holder.binding.UserNameS.setText(user.getName());
        //creating node of follower on clicked user
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(user.getUserID())
                .child("followers")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            holder.binding.followS.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
                            holder.binding.followS.setText("Following");
                            holder.binding.followS.setTextColor(context.getResources().getColor(R.color.white));
                            holder.binding.followS.setEnabled(false);
                        }
                        else {
                            holder.binding.followS.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //when click then our id and time get stored in db
                                    Follow follow = new Follow();
                                    follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                                    follow.setFollowedAt(new Date().getTime());
                                    FirebaseDatabase.getInstance()
                                            .getReference()
                                            .child("Users")
                                            //getting that id where we clicked position is defined above in getPosition
                                            .child(user.getUserID())
                                            .child("followers")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(follow)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    users.clear();
                                                    FirebaseDatabase.getInstance()
                                                            .getReference()
                                                            .child("Users")
                                                            .child(user.getUserID())
                                                            .child("follower_count")
                                                            .setValue(user.getFollower_count() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.binding.followS.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
                                                                    holder.binding.followS.setText("Following");
                                                                    holder.binding.followS.setTextColor(context.getResources().getColor(R.color.white));
                                                                    holder.binding.followS.setEnabled(false);
                                                                    Toast.makeText(context, "You followed " + user.getName(), Toast.LENGTH_SHORT).show();
                                                                    //sending notification
                                                                    notificationModal notification = new notificationModal();
                                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notification.setNotificationAt(new Date().getTime());
                                                                    notification.setType("follow");
                                                                    FirebaseDatabase.getInstance().getReference().child("notification")
                                                                            .child(user.getUserID())// it is the id of particular user
                                                                            .push()
                                                                            .setValue(notification);
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


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SearchusersampleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SearchusersampleBinding.bind(itemView);
        }
    }
}
