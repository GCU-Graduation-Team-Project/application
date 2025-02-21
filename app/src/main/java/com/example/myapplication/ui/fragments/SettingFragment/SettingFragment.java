package com.example.myapplication.ui.fragments.SettingFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.myapplication.data.SharedRepository;
import com.example.myapplication.databinding.FragmentSettingBinding;
import com.example.myapplication.model.User;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // LiveData를 관찰하여 사용자 정보 변경 시 UI 업데이트
        SharedRepository.getInstance().getUserLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // 사용자 정보가 변경될 때마다 TextView 업데이트
                String name = user.getName();
                String email = user.getEmail();
                Log.d("LiveData", name);
                binding.nameView.setText(name);
                binding.emailView.setText(email);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
