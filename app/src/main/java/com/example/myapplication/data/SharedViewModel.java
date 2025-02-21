package com.example.myapplication.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.UserAccount;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {

    //사용자가 사용하는 데이터 : 질문 리스트, 이력서, 동영상 등을 관리하는데 사용됨.

    private final MutableLiveData<List<UserAccount>> userAccounts = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<UserAccount>> getUserAccounts() {
        return userAccounts;
    }

    public void setUserAccounts(List<UserAccount> accounts) {
        userAccounts.setValue(accounts);
    }
}
