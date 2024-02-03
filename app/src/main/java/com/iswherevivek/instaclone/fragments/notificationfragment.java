package com.iswherevivek.instaclone.fragments;

import android.content.res.ColorStateList;
import android.os.Binder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;
import com.iswherevivek.instaclone.R;
import com.iswherevivek.instaclone.adapter.notificationAdapter;
import com.iswherevivek.instaclone.databinding.FragmentNotificationfragmentBinding;

public class notificationfragment extends Fragment {
    FragmentNotificationfragmentBinding binding;
    notificationAdapter notificationAdapter ;

    public notificationfragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentNotificationfragmentBinding.inflate(inflater,container,false);

        binding.viewPager2.setAdapter(new notificationAdapter(getActivity()));
        new TabLayoutMediator(binding.tabLayout,binding.viewPager2,(tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Notification");
                    break;
                case 1:
                    tab.setText("Requests");
                    break;
                default:
                    throw new IllegalArgumentException("Exception Occurs");
            }
        }).attach();
        return binding.getRoot();
    }
}