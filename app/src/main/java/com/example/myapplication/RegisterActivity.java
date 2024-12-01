package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.myapplication.databinding.RegisterPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private RegisterPageBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = RegisterPageBinding.inflate(getLayoutInflater());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

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
        String name = binding.nameInput.getText().toString().trim();
        String email = binding.emailInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();
        String password2 = binding.passwordInput2.getText().toString().trim();

        Log.d("PasswordCheck", "Password1: " + password + ", Password2: " + password2);

        if (name.isEmpty()) {
            binding.nameInputLayout.setHelperText("사용할 수 없는 이름 입니다");
            binding.nameInputLayout.setHelperTextEnabled(true);
            return;
        }
        else{
            binding.nameInputLayout.setHelperText("사용할 수 있는 이름 입니다");
            binding.nameInputLayout.setHelperTextEnabled(true);
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

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            db =  FirebaseFirestore.getInstance();
                            String userId = user.getUid();

                            UserAccount userAccount = new UserAccount();
                            userAccount.setName(name);
                            userAccount.setId(userId);

                            db.collection("Users").document(userId)
                                    .set(userAccount)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           Toast.makeText(RegisterActivity.this, "회원가입 및 데이터 저장 성공!", Toast.LENGTH_SHORT).show();

                                           Intent intent = new Intent(RegisterActivity.this, RegisterSuccess.class);
                                           startActivity(intent);
                                           overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

                                       }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "데이터 저장 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
