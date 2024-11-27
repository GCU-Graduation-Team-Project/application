package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.SearchFragment7Binding;

public class SearchFragment7 extends Fragment {
    private SearchFragment7Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchFragment7Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.buttonNext.setOnClickListener(v -> {
            Fragment nextFragment = new SearchFragment();
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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}