package com.iswherevivek.instaclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.iswherevivek.instaclone.R;
import com.iswherevivek.instaclone.databinding.ActivityMainBinding;
import com.iswherevivek.instaclone.fragments.addpost;
import com.iswherevivek.instaclone.fragments.homefragment;
import com.iswherevivek.instaclone.fragments.notificationfragment;
import com.iswherevivek.instaclone.fragments.profilefragment;
import com.iswherevivek.instaclone.fragments.searchfragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    MenuItem lastSelectedItem;
    int item = R.id.bottom_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolBar);
        MainActivity.this.setTitle("Your profile");
        binding.toolBar.setVisibility(View.GONE);
        LoadFragment(new homefragment());
        binding.navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int selected = item.getItemId();
               if (selected==R.id.bottom_home){
                   LoadFragment(new homefragment());
                   onOptionsItemSelected(item);
                   binding.toolBar.setVisibility(View.GONE);
               } else if (selected==R.id.bottom_notification) {
                  LoadFragment(new notificationfragment());
                   onOptionsItemSelected(item);
                   binding.toolBar.setVisibility(View.GONE);

               } else if (selected==R.id.bottom_search) {
                  LoadFragment(new searchfragment());
                   onOptionsItemSelected(item);
                   binding.toolBar.setVisibility(View.GONE);

               } else {
                   LoadFragment(new profilefragment());
                   onOptionsItemSelected(item);
                   binding.toolBar.setVisibility(View.VISIBLE);

               }

               return true;
            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              LoadFragment(new addpost());
                binding.toolBar.setVisibility(View.GONE);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (lastSelectedItem!=null){
            resetIcon(lastSelectedItem);
        }
        int selected = item.getItemId();
        if (selected==R.id.bottom_home){
            handleMenuItemClicked(item,R.drawable.homed);

        } else if (selected==R.id.bottom_notification) {
            handleMenuItemClicked(item,R.drawable.notificationd);

        } else if (selected==R.id.bottom_search) {
            handleMenuItemClicked(item,R.drawable.people);
        } else {
            handleMenuItemClicked(item,R.drawable.userd);
        }
        lastSelectedItem=item;
        return true;
    }

    private void handleMenuItemClicked(MenuItem item , int newIconResource){
        //setting the original icon
        item.setIcon(newIconResource);
    }
    private void resetIcon(MenuItem item){
        int selected = item.getItemId();
        if (selected==R.id.bottom_home){
            item.setIcon(R.drawable.home);

        } else if (selected==R.id.bottom_notification) {
            item.setIcon(R.drawable.notification);

        } else if (selected==R.id.bottom_search) {
            item.setIcon(R.drawable.searchh);
        } else {
            item.setIcon(R.drawable.user);
        }
    }

    private  void showBottomDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetfb);
        ImageView arrow = dialog.findViewById(R.id.arrowup);
        ImageView cancel = dialog.findViewById(R.id.cancelButton);
        LinearLayout video = dialog.findViewById(R.id.layoutVideo);
        LinearLayout shorts = dialog.findViewById(R.id.layoutShorts);
        LinearLayout live = dialog.findViewById(R.id.layoutLive);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "upload video", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "now live", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        shorts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "upload shorts", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    private void LoadFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}