package com.example.myapplication.ui.fragments.SearchFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSearch5Binding;
import com.example.myapplication.model.UserAccount;
import com.example.myapplication.util.BaseCallback;
import com.example.myapplication.util.QuestionBoxAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SearchFragment6 extends Fragment {
    private FragmentSearch5Binding binding;
    private QuestionBoxAdapter adapter;
    private List<UserAccount> dataList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private UserAccount userAccount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearch5Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // BaseCallback 생성
        BaseCallback baseCallback = new BaseCallback(requireActivity());
        // callback 등록
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), baseCallback);


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.progressBar.setProgress(100);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(uid, Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);
        String name = sharedPreferences.getString("name", null);
        String email = sharedPreferences.getString("email", null);
        String question1 = sharedPreferences.getString("question1", null);
        String question2 = sharedPreferences.getString("question2", null);
        String question3 = sharedPreferences.getString("question3", null);
        String question4 = sharedPreferences.getString("question4", null);
        String pdf_uri = sharedPreferences.getString("pdfUri", null);

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yy-MM-dd");
        String formattedDate = currentDate.format(dateFormatter);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = currentTime.format(timeFormatter);


        String fileName = "uploaded_pdf_" + System.currentTimeMillis() + ".pdf";
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        StorageReference pdfRef = storageReference.child("pdfs/" + fileName);
        Uri file_path = Uri.parse(pdf_uri);

        UploadTask uploadTask = pdfRef.putFile(file_path);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            pdfRef.getDownloadUrl().addOnSuccessListener(uri -> {

                String downloadUrl = uri.toString();

                userAccount = new UserAccount(userId, name, email, question1, question2, question3, question4, downloadUrl, formattedDate, formattedTime);

                db.collection("Questions")
                        .add(userAccount) // 자동 랜덤 ID로 문서 생성
                        .addOnSuccessListener(documentReference -> {
                            Log.d("Firestore", "Document added with ID: " + documentReference.getId());


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


                        });

            });
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
