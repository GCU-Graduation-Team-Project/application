package com.example.myapplication.ui.fragments.CameraFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentCamera2Binding;

public class CameraFragment2 extends Fragment {
    private FragmentCamera2Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCamera2Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonNext.setOnClickListener(v -> {
            Fragment nextFragment = new CameraFragment3();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

            transaction.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.fade_out
            );

            transaction.replace(R.id.fragment_container, nextFragment);
            transaction.commit();
        });

    }
}
