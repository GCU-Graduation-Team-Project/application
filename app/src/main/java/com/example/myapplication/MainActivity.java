package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    if (item.getItemId() == R.id.navigation_home) {
                        selectedFragment = new HomeFragment();
                    } else if (item.getItemId() == R.id.navigation_search) {
                        selectedFragment = new SearchFragment();
                    } else if (item.getItemId() == R.id.navigation_camera) {
                        selectedFragment = new CameraFragment();
                    } else if (item.getItemId() == R.id.navigation_settings) {
                        selectedFragment = new SettingFragment();
                    }

                    transaction.setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.fade_out
                    );

                    if (selectedFragment != null) {
                        transaction.replace(binding.fragmentContainer.getId(), selectedFragment);
                        transaction.commit();
                    }
                    return true;
                }
            };
    private OnBackPressedCallback exit_callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.fragmentContainer.getId(), new HomeFragment())
                    .commit();
        }

        exit_callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("앱을 종료하시겠습니까?");

                    builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                            System.exit(0);
                        }
                    });

                    builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }

            }
        };

        getOnBackPressedDispatcher().addCallback(exit_callback);


    }

}