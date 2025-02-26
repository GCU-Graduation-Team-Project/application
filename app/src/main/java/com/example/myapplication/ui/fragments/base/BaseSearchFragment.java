package com.example.myapplication.ui.fragments.base;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.myapplication.R;
import com.example.myapplication.util.BaseCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class BaseSearchFragment extends Fragment {
    protected FirebaseAuth mAuth;
    protected FirebaseFirestore db;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        
        // BaseCallback 등록
        BaseCallback baseCallback = new BaseCallback(requireActivity());
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), baseCallback);
    }

    protected void navigateToNext(Fragment nextFragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
        );
        transaction.replace(R.id.fragment_container, nextFragment);
        transaction.commit();
    }
} 