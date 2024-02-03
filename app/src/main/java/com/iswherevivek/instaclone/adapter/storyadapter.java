package com.iswherevivek.instaclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iswherevivek.instaclone.R;
import com.iswherevivek.instaclone.databinding.StoryDesignBinding;
import com.iswherevivek.instaclone.modals.Storymodal;
import com.iswherevivek.instaclone.modals.User;
import com.iswherevivek.instaclone.modals.userStories;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class storyadapter extends RecyclerView.Adapter<storyadapter.ViewHolder> {
    ArrayList<Storymodal> storymodal = new ArrayList<>();
    Context context;

    public storyadapter(ArrayList<Storymodal> storymodal, Context context) {
        this.storymodal = storymodal;
        this.context = context;
    }

    @NonNull
    @Override
    public storyadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.story_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull storyadapter.ViewHolder holder, int position) {
        Storymodal story=storymodal.get(position);
        if (story.getStories().size()>0) {
            //if want to show story on storyView instead of userpic on story view
//        userStories lastStory = story.getStories().get(story.getStories().size()-1);
//        Picasso.get().load(lastStory.getImage()).into(holder.binding.story);
            holder.binding.view01.setPortionsCount(story.getStories().size());
            FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(story.getStoryBy()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            holder.binding.userName.setText(user.getName());
                            Picasso.get().load(user.getProfile_pic()).placeholder(R.drawable.avatar).into(holder.binding.story);
                            holder.binding.story.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ArrayList<MyStory> myStories = new ArrayList<>();

                                    for (userStories story : story.getStories()) {
                                        myStories.add(new MyStory(
                                                story.getImage()
                                        ));
                                    }
                                    new StoryView.Builder(((AppCompatActivity) context).getSupportFragmentManager())
                                            .setStoriesList(myStories) // Required
                                            .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                            .setTitleText(user.getName()) // Default is Hidden
                                            .setSubtitleText("") // Default is Hidden
                                            .setTitleLogoUrl(user.getProfile_pic()) // Default is Hidden
                                            .setStoryClickListeners(new StoryClickListeners() {
                                                @Override
                                                public void onDescriptionClickListener(int position) {
                                                    //your action
                                                }

                                                @Override
                                                public void onTitleIconClickListener(int position) {
                                                    //your action
                                                }
                                            }) // Optional Listeners
                                            .build() // Must be called before calling show method
                                            .show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }


    }

    @Override
    public int getItemCount() {
        return storymodal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        StoryDesignBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = StoryDesignBinding.bind(itemView);
        }
    }
}
