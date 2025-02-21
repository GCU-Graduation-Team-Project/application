package com.example.myapplication.register;

import com.example.myapplication.util.UserInputValidator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterUserService {
    private final FirebaseAuth auth;
    private final RegisterUserRepository resgisterUserData;



    // 기본 생성자 설정
    public RegisterUserService(FirebaseAuth auth, FirebaseFirestore db) {
        this.auth = auth;
        this.resgisterUserData = new RegisterUserRepository(db);
    }


    // 회원가입 결과를 콜백으로 전달하기 위한 인터페이스
    public interface RegistrationCallback {
        void onSuccess();                          // 회원가입 성공
        void onValidationError(String errorMessage);  // 입력 검증 또는 중복 체크 실패
        void onFailure(String errorMessage);          // 기타 실패 (예: FirebaseAuth, Firestore 오류)
    }




    public void registerUser(String name, String email, String password, String confirmPassword, RegistrationCallback callback) {


        /**
         * 회원가입 처리 메서드
         *
         * @param name           사용자 이름
         * @param email          사용자 이메일
         * @param password       비밀번호
         * @param confirmPassword 비밀번호 확인
         * @param callback       결과 콜백
         */


        // 1. 입력 검증
        String validationError = UserInputValidator.validateRegistrationInput(name, email, password, confirmPassword);
        if (validationError != null) {
            callback.onValidationError(validationError);
            return;
        }

        resgisterUserData.isEmailRegistered(email)
                .addOnSuccessListener(isRegistered -> {
                    if (isRegistered) {
                        callback.onValidationError("중복된 이메일이 존재합니다");
                    } else {
                        // FirebaseAuth를 통한 계정 생성
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(authTask -> {
                                    if (authTask.isSuccessful()) {

                                        // 4. Firestore에 사용자 데이터 저장

                                        FirebaseUser user = auth.getCurrentUser();
                                        String userId = user.getUid();

                                        resgisterUserData.saveUser(userId, name, email)
                                                .addOnCompleteListener(saveTask -> {
                                                    if (saveTask.isSuccessful()) {
                                                        callback.onSuccess();
                                                    } else {
                                                        String errorMsg = saveTask.getException() != null ? saveTask.getException().getMessage() : "알 수 없는 오류";
                                                        callback.onFailure("Firestore 저장 실패: " + errorMsg);
                                                    }
                                                });

                                    } else {
                                        String errorMsg = authTask.getException() != null ? authTask.getException().getMessage() : "알 수 없는 오류";
                                        callback.onFailure("회원가입 실패: " + errorMsg);
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Firestore 쿼리 실패 시 처리
                    callback.onFailure("이메일 중복 확인 중 오류 발생: " + e.getMessage());
                });
    }

}
