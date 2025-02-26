package com.example.myapplication.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.auth.register.RegisterActivity;
import com.example.myapplication.ui.activity.MainActivity;
import com.example.myapplication.util.ExitCallback;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private LoginAuthenticator authenticator;
    private GoogleLogin googleSignInHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        //다크모드 설정
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());

        //Firebase 설정
        mAuth = FirebaseAuth.getInstance();

        //LoginUserAuthenticator 인스턴스 생성
        authenticator = new LoginAuthenticator(mAuth);

        // 현재 사용자가 이미 로그인 되어 있는지 확인
        if (mAuth.getCurrentUser() != null) {
            // 자동 로그인 시 사용자 데이터 가져오기
            authenticator.getUserDataFromFireStore();
            
            // 로그인된 상태라면, 로그인 화면을 건너 뛰고 메인 액티비티로 이동
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

            // 로그인 액티비티 종료
            finish();
        }

        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailText.getText().toString().trim();
            String password = binding.passwordText.getText().toString().trim();

            authenticator.loginUser("user@example.com", password);
        });

        // register 버튼을 클릭 했을 때, 화면 전환이 이루어 지도록 listener 설정
        binding.registerText.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, RegisterActivity.class);
            startActivity(intent2);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });


        googleSignInHelper = new GoogleLogin(this, getString(R.string.default_web_client_id));
        binding.googleSignInButton.setOnClickListener(v -> googleSignInHelper.startSignIn());

        // ExitCallback 생성
        OnBackPressedCallback exitCallback = new ExitCallback(this);
        // callback 등록
        this.getOnBackPressedDispatcher().addCallback(this, exitCallback);

    }

    // Activity의 onActivityResult에서 helper의 결과 처리 메서드를 호출합니다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleSignInHelper.handleActivityResult(requestCode, resultCode, data);
    }

}
