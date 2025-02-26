package com.example.myapplication.data.repository;

import android.util.Log;
import android.widget.Toast;
import android.app.Activity;

import com.example.myapplication.data.SharedRepository;
import com.example.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirestoreUserRepository {
    private static final String TAG = "FirestoreUserRepository";
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;
    private Activity activity;

    public FirestoreUserRepository(FirebaseAuth auth, FirebaseFirestore db, Activity activity) {
        this.auth = auth;
        this.db = db;
        this.activity = activity;
    }

    public FirestoreUserRepository(FirebaseAuth auth, FirebaseFirestore db) {
        this.auth = auth;
        this.db = db;
        this.activity = null;
    }

    /**
     * Firestore에서 사용자 데이터를 가져와 SharedRepository에 저장
     */
    public void getUserDataFromFireStore() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            Log.d(TAG, "현재 로그인된 사용자가 없습니다.");
            return;
        }

        db.collection("Users")
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
                            Log.d(TAG, "사용자 데이터 로드 성공: " + userData);
                        } else {
                            Log.d(TAG, "사용자 데이터를 파싱하는데 실패했습니다.");
                        }
                    } else {
                        Log.d(TAG, "사용자 데이터가 존재하지 않습니다.");
                    }
                });
    }

    /**
     * 사용자 데이터를 Firestore에 저장
     */
    public void setUserDataToFireStore() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Log.d(TAG, "현재 로그인된 사용자가 없습니다.");
            return;
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("email", user.getEmail());
        userData.put("name", user.getDisplayName());
        userData.put("id", user.getUid());

        db.collection("Users")
                .document(user.getUid())
                .set(userData)
                .addOnCompleteListener(firestoreTask -> {
                    if (firestoreTask.isSuccessful()) {
                        if (activity != null) {
                            Toast.makeText(activity, "FireStore 저장 성공: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        }
                        Log.d(TAG, "Firestore 저장 성공: " + user.getEmail());
                    } else {
                        if (activity != null) {
                            Toast.makeText(activity, "Firestore 저장 실패", Toast.LENGTH_SHORT).show();
                        }
                        Log.d(TAG, "Firestore 저장 실패");
                    }
                });
    }
} 