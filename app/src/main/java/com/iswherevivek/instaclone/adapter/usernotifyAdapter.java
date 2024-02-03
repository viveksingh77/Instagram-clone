package com.iswherevivek.instaclone.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iswherevivek.instaclone.R;
import com.iswherevivek.instaclone.databinding.NotificationsampleBinding;
import com.iswherevivek.instaclone.modals.User;
import com.iswherevivek.instaclone.modals.notificationModal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class usernotifyAdapter extends RecyclerView.Adapter<usernotifyAdapter.ViewHolder> {
    Context context;
    ArrayList<notificationModal> notificationList ;

    public usernotifyAdapter(Context context, ArrayList<notificationModal> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public usernotifyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notificationsample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull usernotifyAdapter.ViewHolder holder, int position) {
        notificationModal modal = notificationList.get(position);
        String type = modal.getType();
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(modal.getNotificationBy())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfile_pic())
                                .placeholder(R.drawable.avatar)
                                .into(holder.binding.postPic);
                        if (type.equals("like")){
                            holder.binding.notify.setText(Html.fromHtml("<b>" + user.getName() + "</b>"+ "Liked your post",0));
                        }else if(type.equals("comment")) {
                            holder.binding.notify.setText(Html.fromHtml("<b>" + user.getName() + "</b>"+ "commented on your post",0));
                        }else {
                            holder.binding.notify.setText(Html.fromHtml("<b>" + user.getName() + "</b>"+ "stated following you",0));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
      NotificationsampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           binding = NotificationsampleBinding.bind(itemView);
        }
    }
}
