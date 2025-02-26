package com.example.myapplication.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserAccount;
import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();
    private final MutableLiveData<List<UserAccount>> userAccounts = new MutableLiveData<>(new ArrayList<>());

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        currentUser.setValue(user);
    }

    public LiveData<List<UserAccount>> getUserAccounts() {
        return userAccounts;
    }

    public void setUserAccounts(List<UserAccount> accounts) {
        userAccounts.setValue(accounts);
    }
} 