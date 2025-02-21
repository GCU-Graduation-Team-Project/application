package com.example.myapplication.data;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.model.User;


public class SharedRepository {

    private static final SharedRepository INSTANCE = new SharedRepository();
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    private SharedRepository() {
        // 초기 사용자 정보 (필요에 따라 값 설정)
        userLiveData.setValue(new User("", "", ""));
    }

    public static SharedRepository getInstance() {
        return INSTANCE;
    }

    // LiveData로 사용자 정보 반환
    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    // 사용자 정보 갱신
    public void updateUser(User user) {
        userLiveData.postValue(user);
    }

}
