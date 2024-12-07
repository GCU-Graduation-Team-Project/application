package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.LoginPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginActivity extends AppCompatActivity {
    private LoginPageBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = LoginPageBinding.inflate(getLayoutInflater());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());


        mAuth = FirebaseAuth.getInstance();

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

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userID", userId);
                            editor.apply();

                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            binding.passwordLayout.setError("로그인 실패");
                        }
                    });
        });

        binding.registerText.setOnClickListener(v -> {
            Intent intent2 = new Intent(this, RegisterActivity.class);
            startActivity(intent2);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        OnBackPressedCallback exit_callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
