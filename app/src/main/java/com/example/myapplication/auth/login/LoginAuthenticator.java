package com.example.myapplication.auth.login;

import android.util.Log;
import com.example.myapplication.data.SharedRepository;
import com.example.myapplication.model.User;
import com.example.myapplication.util.UserInputValidator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.myapplication.data.repository.FirestoreUserRepository;

public class LoginAuthenticator {
    private static final String TAG = "LoginAuthenticator";
    private final FirebaseAuth auth;
    private final FirestoreUserRepository firestoreUserRepository;
    // 로그인 성공 후 사용자 데이터를 저장할 멤버 변수
    private User currentUser;

    public LoginAuthenticator(FirebaseAuth auth) {
        this.auth = auth;
        this.firestoreUserRepository = new FirestoreUserRepository(auth, FirebaseFirestore.getInstance());
    }

    /**
     * 이메일과 비밀번호로 로그인한 후 Firestore에서 사용자 데이터를 가져와 내부 멤버 변수에 저장하고,
     * 결과를 Log.d로 출력합니다.
     * 비동기 작업이므로, 호출 후 currentUser의 값은 작업 완료 후에 유효합니다.
     *
     * @param email    사용자 이메일
     * @param password 사용자 비밀번호
     */

    public void loginUser(String email, String password) {
        //입력값 검증
        String validationError = UserInputValidator.validateLoginInput(email, password);
        if (validationError != null) {
            Log.d(TAG, "입력 오류: " + validationError);
            return;
        }

        //Firebase 인증
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(authTask -> {
                    if (!authTask.isSuccessful()) {
                        String errorMsg = authTask.getException() != null ? authTask.getException().getMessage() : "알 수 없는 로그인 오류";
                        Log.d(TAG, "로그인 실패: " + errorMsg);
                        return;
                    }
                    getUserDataFromFireStore();
                });
    }

    public void getUserDataFromFireStore() {
        firestoreUserRepository.getUserDataFromFireStore();
    }
}
