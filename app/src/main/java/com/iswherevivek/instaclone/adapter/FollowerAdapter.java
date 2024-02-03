package com.iswherevivek.instaclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iswherevivek.instaclone.R;
import com.iswherevivek.instaclone.databinding.ProfilefrindsBinding;
import com.iswherevivek.instaclone.modals.Follow;
import com.iswherevivek.instaclone.modals.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {
    Context context;

    public FollowerAdapter(Context context, ArrayList<Follow> friendModalArrayList) {
        this.context = context;
        this.friendModalArrayList = friendModalArrayList;
    }

    ArrayList<Follow> friendModalArrayList = new ArrayList<>();

    @NonNull
    @Override
    public FollowerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profilefrinds,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerAdapter.ViewHolder holder, int position) {
        Follow friendModal = friendModalArrayList.get(position);
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(friendModal.getFollowedBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfile_pic())
                                .placeholder(R.drawable.avatar)
                                .into(holder.binding.profileFriend);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return friendModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ProfilefrindsBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
          binding = ProfilefrindsBinding.bind(itemView);
        }
    }
}
