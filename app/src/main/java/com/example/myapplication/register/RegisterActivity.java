package com.example.myapplication.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityResgisterPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private ActivityResgisterPageBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RegisterUserService registrationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // 액티비티 화면 실행
        super.onCreate(savedInstanceState);
        binding = ActivityResgisterPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        registrationService = new RegisterUserService(mAuth, db);

        binding.registerButton.setOnClickListener(v -> {
            String name = binding.nameInput.getText().toString().trim();
            String email = binding.emailInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();
            String passwordConfirm = binding.passwordInput2.getText().toString().trim();

            registrationService.registerUser(name, email, password, passwordConfirm, new RegisterUserService.RegistrationCallback() {
                @Override
                public void onSuccess() {
                    // 회원가입 성공 시 다음 액티비티로 전환
                    Intent intent = new Intent(RegisterActivity.this, RegisterSuccessActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                }

                @Override
                public void onValidationError(String errorMessage) {
                    // 검증 또는 중복 체크 실패 시 에러 메시지 출력
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String errorMessage) {
                    // 기타 실패 시 에러 메시지 출력
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
