package com.example.myapplication.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.register.RegisterActivity;
import com.example.myapplication.ui.MainActivity;
import com.example.myapplication.util.ExitCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());


        mAuth = FirebaseAuth.getInstance();

        // 현재 사용자가 이미 로그인 되어 있는지 확인
        if (mAuth.getCurrentUser() != null) {

            // 로그인된 상태 라면, 로그인 화면을 건너 뛰고 메인 액티비티로 이동
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

            // 로그인 액티비티 종료
            finish();
        }



        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailText.getText().toString().trim();
            String password = binding.passwordText.getText().toString().trim();

            if (TextUtils.isEmpty(email) ) {
                binding.emailLayout.setError("이메일을 입력해주세요");
                return;
            }else{
                binding.emailLayout.setError(null);
            }

            if (TextUtils.isEmpty(password)) {
                binding.passwordLayout.setError("비밀번호를 입력해주세요");
                return;
            }else{
                binding.passwordLayout.setError(null);
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {

                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = currentUser.getUid();

                            SharedPreferences sharedPreferences = getSharedPreferences(userId, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email);
                            editor.putString("userId", userId);
                            editor.apply();

                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            binding.passwordLayout.setError("로그인 실패");
                        }
                    });
        });

        // register 버튼을 클릭 했을 때, 화면 전환이 이루어 지도록 listener 설정
        binding.registerText.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, RegisterActivity.class);
            startActivity(intent2);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        // ExitCallback 생성
        OnBackPressedCallback exitCallback = new ExitCallback(this);
        // callback 등록
        this.getOnBackPressedDispatcher().addCallback(this, exitCallback);

    }


}
