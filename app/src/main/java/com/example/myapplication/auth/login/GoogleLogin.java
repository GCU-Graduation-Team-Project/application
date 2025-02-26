package com.example.myapplication.auth.login;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.myapplication.data.repository.FirestoreUserRepository;

/**
 *
 * FirebaseGoogleSignInHelper 클래스는 구글 로그인과 Firebase 인증, 그리고
 * Firestore에 사용자(이메일, 이름, uid) 정보를 저장하는 기능을 제공합니다.
 * 콜백 인터페이스 없이 내부에서 Toast 메시지 등으로 결과를 처리합니다.
 *
 */

public class GoogleLogin {

    public static final int RC_SIGN_IN = 9001;

    private Activity mActivity;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseFirestore mFirestore;
    private FirestoreUserRepository firestoreUserRepository;

    /**
     * 생성자
     *
     * @param activity           로그인 진행에 사용할 Activity
     * @param defaultWebClientId Firebase 콘솔에서 발급받은 OAuth 2.0 클라이언트 ID
     *
     */


    public GoogleLogin(Activity activity, String defaultWebClientId) {
        this.mActivity = activity;
        this.mAuth = FirebaseAuth.getInstance();
        this.mFirestore = FirebaseFirestore.getInstance();
        this.firestoreUserRepository = new FirestoreUserRepository(mAuth, mFirestore, activity);

        // Google Sign-In 옵션 설정
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(defaultWebClientId)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    /**
     * 구글 로그인 화면을 호출.
     */

    public void startSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Activity의 onActivityResult()에서 호출하여 로그인 결과를 처리합니다.
     *
     * @param requestCode 요청 코드
     * @param resultCode  결과 코드
     * @param data        Intent 데이터
     */

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(mActivity, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * 구글 ID 토큰으로 Firebase 인증을 진행하고,
     * 인증 성공 시 Firestore에 사용자 정보를 저장합니다.
     *
     * @param idToken 구글 ID 토큰
     */

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firestoreUserRepository.setUserDataToFireStore();
                        } else {
                            Toast.makeText(mActivity, "Firebase 인증 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
