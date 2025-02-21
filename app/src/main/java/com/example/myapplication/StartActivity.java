package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.myapplication.databinding.ActivityStartPageBinding;
import com.example.myapplication.login.LoginActivity;
import com.google.firebase.FirebaseApp;

public class StartActivity extends AppCompatActivity {
    private ActivityStartPageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // binding 설정
        binding = ActivityStartPageBinding.inflate(getLayoutInflater());

        // 다크모드 비활성화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // 액티비티 화면 띄우기
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        // Firebase 실행
        FirebaseApp.initializeApp(this);

        // start 버튼을 눌렀을 때, 로그인 화면 으로 전환
        binding.buttonStart.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

    }

}
