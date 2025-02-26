package com.example.myapplication.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.ui.fragments.CameraFragment.CameraFragment;
import com.example.myapplication.ui.fragments.HomeFragment.HomeFragment;
import com.example.myapplication.ui.fragments.SearchFragment.SearchFragment;
import com.example.myapplication.ui.fragments.SettingFragment.SettingFragment;
import com.example.myapplication.util.ExitCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener);
        binding.bottomNavigation.setVisibility(android.view.View.GONE);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                // 로딩 뷰 숨기기 및 프래그먼트 컨테이너 보이기
                binding.loadingView.setVisibility(android.view.View.GONE);
                binding.fragmentContainer.setVisibility(android.view.View.VISIBLE);
                binding.bottomNavigation.setVisibility(android.view.View.VISIBLE);

                if (!getSupportFragmentManager().isStateSaved()) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(binding.fragmentContainer.getId(), new HomeFragment())
                            .commit();
                }
            }

        }, 2000);

        // 액티비티 컨텍스트를 사용하여 ExitCallback 생성
        OnBackPressedCallback exitCallback = new ExitCallback(this);
        // Fragment의 라이프사이클에 맞게 콜백 등록

        this.getOnBackPressedDispatcher().addCallback(this, exitCallback);

    }


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


}