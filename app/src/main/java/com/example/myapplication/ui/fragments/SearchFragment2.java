package com.example.myapplication.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSearch2Binding;
import com.example.myapplication.util.BaseCallback;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SearchFragment2 extends Fragment {

    private FragmentSearch2Binding binding;
    private OnBackPressedCallback main_callback;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearch2Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // BaseCallback 생성
        BaseCallback baseCallback = new BaseCallback(requireActivity());
        // callback 등록
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), baseCallback);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.progressBar.setProgress(25);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        db =  FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(uid, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userId = sharedPreferences.getString("userId", null);

        final String[] question2Value = {""};
        binding.ChipGroupId.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener(){

            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                if (!checkedIds.isEmpty()) {
                    int selectedId = checkedIds.get(0);
                    Chip selectedChip = group.findViewById(selectedId);
                    question2Value[0] = selectedChip.getText().toString();
                }
            }
        });

        binding.buttonNext.setOnClickListener(v -> {
            editor.putString("question2" , question2Value[0]);
            editor.apply();

            Fragment nextFragment = new SearchFragment3();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
