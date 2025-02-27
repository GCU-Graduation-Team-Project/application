package com.example.myapplication.ui.fragments.SearchFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSearch3Binding;
import com.example.myapplication.util.BaseCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SearchFragment3 extends Fragment {
    private FragmentSearch3Binding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearch3Binding.inflate(inflater, container, false);
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

        binding.progressBar.setProgress(50);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        db =  FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(uid, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userId = sharedPreferences.getString("userId", null);


        binding.buttonNext.setOnClickListener(v -> {

            String question3 = binding.question3.getText().toString().trim();
            editor.putString("question3" , question3);
            editor.apply();

            Fragment nextFragment = new SearchFragment4();
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
