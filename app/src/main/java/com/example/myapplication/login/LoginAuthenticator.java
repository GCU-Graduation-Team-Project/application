package com.example.myapplication.login;

import android.util.Log;
import com.example.myapplication.data.SharedRepository;
import com.example.myapplication.model.User;
import com.example.myapplication.util.UserInputValidator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginAuthenticator {
    private static final String TAG = "LoginAuthenticator";
    private final FirebaseAuth auth;
    // 로그인 성공 후 사용자 데이터를 저장할 멤버 변수
    private User currentUser;

    public LoginAuthenticator(FirebaseAuth auth) {
        this.auth = auth;
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
        // 1. 입력값 검증
        String validationError = UserInputValidator.validateLoginInput(email, password);
        if (validationError != null) {
            Log.d(TAG, "입력 오류: " + validationError);
            return;
        }

        // 2. Firebase 인증
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(authTask -> {
                    if (!authTask.isSuccessful()) {
                        String errorMsg = authTask.getException() != null ? authTask.getException().getMessage() : "알 수 없는 로그인 오류";
                        Log.d(TAG, "로그인 실패: " + errorMsg);
                        return;
                    }
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    if (firebaseUser == null) {
                        Log.d(TAG, "사용자 인식에 실패했습니다.");
                        return;
                    }

                    // 3. Firestore에서 사용자 데이터 가져오기
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    firestore.collection("Users")
                            .document(firebaseUser.getUid())
                            .get()
                            .addOnCompleteListener(firestoreTask -> {
                                if (!firestoreTask.isSuccessful() || firestoreTask.getResult() == null) {
                                    Log.d(TAG, "사용자 데이터 불러오기에 실패했습니다.");
                                    return;
                                }
                                DocumentSnapshot documentSnapshot = firestoreTask.getResult();
                                if (documentSnapshot.exists()) {
                                    User userData = documentSnapshot.toObject(User.class);
                                    if (userData != null) {
                                        SharedRepository.getInstance().updateUser(userData);
                                        currentUser = userData;
                                        Log.d(TAG, "로그인 성공, 사용자 데이터: " + userData);
                                    } else {
                                        Log.d(TAG, "사용자 데이터를 파싱하는데 실패했습니다.");
                                    }
                                } else {
                                    Log.d(TAG, "사용자 데이터가 존재하지 않습니다.");
                                }
                            });
                });
    }
}
