package com.iswherevivek.instaclone.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.iswherevivek.instaclone.fragments.subRequestN;
import com.iswherevivek.instaclone.fragments.subnotification;

public class notificationAdapter extends FragmentStateAdapter {

    public notificationAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new subnotification();
            case 1:
                return new subRequestN();
            default:
                throw new IllegalArgumentException("argumentException"+position);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
