package com.iswherevivek.instaclone.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iswherevivek.instaclone.R;
import com.iswherevivek.instaclone.databinding.CommentuserBinding;
import com.iswherevivek.instaclone.modals.User;
import com.iswherevivek.instaclone.modals.commentModal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;

    public CommentAdapter(Context context, ArrayList<com.iswherevivek.instaclone.modals.commentModal> commentModal) {
        this.context = context;
        this.commentModal = commentModal;
    }

    ArrayList<commentModal> commentModal ;

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.commentuser,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        commentModal comment = commentModal.get(position);
//        holder.binding.usercomment.setText(comment.getCommentBody());
        String time = TimeAgo.using(comment.getCommentedAt());
        holder.binding.commentTime.setText(time);
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(comment.getCommentedBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfile_pic()).placeholder(R.drawable.avatar).into(holder.binding.userPic);
                        holder.binding.usercomment.setText(Html.fromHtml("<b>"+user.getName()+ "</b>"+ " "+comment.getCommentBody(),1));



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return commentModal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CommentuserBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CommentuserBinding.bind(itemView);
        }
    }
}
