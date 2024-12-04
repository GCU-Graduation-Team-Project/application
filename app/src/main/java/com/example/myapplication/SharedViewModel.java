package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<UserAccount> userAccount = new MutableLiveData<>();

    public LiveData<UserAccount> getUserAccount() {
        return userAccount;
    }

    public void loadUserAccount(String userId) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference docRef = database.collection("Users").document(userId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserAccount account = document.toObject(UserAccount.class);
                        userAccount.setValue(account);
                    }
                    else{
                        userAccount.setValue(null);
                    }
                }
                else{
                    userAccount.setValue(null);
                }
            }
        });
    }
}
