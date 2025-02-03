package com.example.myapplication.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final FirebaseFirestore db;

    public UserRepository(FirebaseFirestore db) {
        this.db = db;
    }

    /**
     * 이메일 중복 체크
     * @param email 체크할 이메일
     * @return Task<Boolean> : true면 이미 등록된 이메일, false면 등록되지 않음
     */


    public Task<Boolean> isEmailRegistered(String email) {
        return db.collection("users")  // 컬렉션 이름은 실제 사용하는 이름으로 수정하세요.
                .whereEqualTo("email", email)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return !task.getResult().isEmpty();
                    } else {
                        throw task.getException();
                    }
                });
    }

    /**
     * 사용자 데이터 저장
     * @param userId 생성된 사용자 ID
     * @param name 사용자 이름
     * @param email 사용자 이메일
     * @return Task<Void>
     */

    public Task<Void> saveUser(String userId, String name, String email) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", userId);
        userData.put("name", name);
        userData.put("email", email);
        return db.collection("Users")  // 컬렉션 이름은 실제 사용하는 이름으로 수정하세요.
                .document(userId)
                .set(userData);
    }


}
