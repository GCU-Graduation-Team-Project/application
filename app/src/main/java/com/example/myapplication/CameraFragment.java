package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.CameraFragmentBinding;
import com.example.myapplication.databinding.QuestionBoxBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CameraFragment extends Fragment {
    private CameraFragmentBinding binding;
    private QuestionBoxAdapter QuestionBoxAdapter;
    private List<UserAccount> userAccounts = new ArrayList<>();
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CameraFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.reportView.setLayoutManager(new LinearLayoutManager(requireContext()));
        QuestionBoxAdapter = new QuestionBoxAdapter(new ArrayList<>());
        binding.reportView.setAdapter(QuestionBoxAdapter);


        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .whereEqualTo("id", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserAccount userAccount = document.toObject(UserAccount.class);
                            if (userAccount != null) {
                                userAccounts.add(userAccount);
                            }
                        }
                        Log.d("FirestoreDebug", "Fetched data size: " + userAccounts.size());
                        sharedViewModel.setUserAccounts(userAccounts);
                    } else {
                        Log.e("FirestoreError", "Error fetching data", task.getException());
                    }
                });


        sharedViewModel.getUserAccounts().observe(getViewLifecycleOwner(), userAccounts -> {
            if (userAccounts != null) {
                QuestionBoxAdapter.updateUserAccounts(userAccounts);
                Log.d("LiveDataUpdate", "Data size in LiveData: " + userAccounts.size());
            } else {
                Log.d("LiveDataUpdate", "No data available");
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
