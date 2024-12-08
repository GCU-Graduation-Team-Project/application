package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.UserAccount;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<UserAccount>> userAccounts = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<UserAccount>> getUserAccounts() {
        return userAccounts;
    }

    public void setUserAccounts(List<UserAccount> accounts) {
        userAccounts.setValue(accounts);
    }
}
