package com.iswherevivek.instaclone.modals;

import java.util.ArrayList;

public class Storymodal {
    private String storyBy;
    private long storyAt;
    ArrayList<userStories> stories ;

    public Storymodal() {
    }

    public String getStoryBy() {
        return storyBy;
    }

    public void setStoryBy(String storyBy) {
        this.storyBy = storyBy;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }

    public ArrayList<userStories> getStories() {
        return stories;
    }

    public void setStories(ArrayList<userStories> stories) {
        this.stories = stories;
    }

    public Storymodal(String storyBy, long storyAt, ArrayList<userStories> stories) {
        this.storyBy = storyBy;
        this.storyAt = storyAt;
        this.stories = stories;
    }
}
