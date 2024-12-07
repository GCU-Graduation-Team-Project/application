package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.databinding.CameraFragmentBinding;
import com.example.myapplication.databinding.SearchFragment6Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment6 extends Fragment {
    private SearchFragment6Binding binding;
    private QuestionBoxAdapter adapter;
    private List<UserAccount> dataList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchFragment6Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.progressBar.setProgress(100);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);
        String name = sharedPreferences.getString("name", null);
        String email = sharedPreferences.getString("email", null);
        String question1 = sharedPreferences.getString("question1", null);
        String question2 = sharedPreferences.getString("question2", null);
        String question3 = sharedPreferences.getString("question3", null);
        String question4 = sharedPreferences.getString("question4", null);
        String pdf_url = null;

        UserAccount userAccount = new UserAccount(userId, name, email, question1, question2, question3, question4, pdf_url);

        db.collection("Users").document(userId)
                .set(userAccount)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Fragment nextFragment = new SearchFragment7();
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                            transaction.setCustomAnimations(
                                    R.anim.fade_in,
                                    R.anim.fade_out,
                                    R.anim.fade_in,
                                    R.anim.fade_out
                            );

                            transaction.replace(R.id.fragment_container, nextFragment);
                            transaction.commit();

                        } else {
                            Log.e("Firestore", "Error adding document", task.getException());
                        }
                    }
                });


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("정보가 저장되지 않습니다.\n처음화면으로 돌아가시겠습니까?");

                Fragment firstFragment = new SearchFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        transaction.replace(R.id.fragment_container, firstFragment);
                        transaction.commit();
                    }
                });

                builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setCancelable(false);
                builder.show();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
