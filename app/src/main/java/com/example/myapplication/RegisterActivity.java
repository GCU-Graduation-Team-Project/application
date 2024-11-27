package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.RegisterPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private RegisterPageBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = RegisterPageBinding.inflate(getLayoutInflater());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        binding.registerButton.setOnClickListener(view -> {

            checkUserExists();

        });

        OnBackPressedCallback exit_callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("정보가 저장되지 않습니다.\n처음화면으로 돌아가시겠습니까?");

                    builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
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

    private void checkUserExists(){
        String id = binding.idInput.getText().toString().trim();
        String email = binding.emailInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();
        String password2 = binding.passwordInput2.getText().toString().trim();

        Log.d("PasswordCheck", "Password1: " + password + ", Password2: " + password2);

        if (id.isEmpty()) {
            binding.idInputLayout.setHelperText("사용할 수 없는 아이디 입니다");
            binding.idInputLayout.setHelperTextEnabled(true);
            return;
        }
        else{
            binding.idInputLayout.setHelperText("사용할 수 있는 아이디 입니다");
            binding.idInputLayout.setHelperTextEnabled(true);
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInputLayout.setHelperText("사용할 수 없는 이메일 입니다");
            binding.emailInputLayout.setHelperTextEnabled(true);
            return;
        }
        else{
            binding.emailInputLayout.setHelperText("사용할 수 있는 이메일 입니다");
            binding.emailInputLayout.setHelperTextEnabled(true);
        }

        if (password.isEmpty()) {
            binding.passwordInputLayout2.setHelperText("비밀번호를 입력해 주세요");
            binding.passwordInputLayout2.setHelperTextEnabled(true);
            return;
        }

        if (!password.equals(password2)) {
            binding.passwordInputLayout2.setHelperText("비밀번호가 일치하지 않습니다");
            binding.passwordInputLayout2.setHelperTextEnabled(true);
            return;
        }
        else{
            binding.passwordInputLayout2.setHelperText("비밀번호가 일치합니다");
            binding.passwordInputLayout2.setHelperTextEnabled(true);
        }

        registerUser();

    }

    private void registerUser(){
        Intent intent = new Intent(this, RegisterSuccess.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

}
