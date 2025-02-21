package com.example.myapplication.ui.fragments.SearchFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentSearch5Binding;
import com.example.myapplication.util.BaseCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class SearchFragment5 extends Fragment {
    private FragmentSearch5Binding binding;
    private OnBackPressedCallback main_callback;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ActivityResultLauncher<Intent> filePickerLauncher;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearch5Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.progressBar.setProgress(100);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        db =  FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // BaseCallback 생성
        BaseCallback baseCallback = new BaseCallback(requireActivity());
        // callback 등록
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), baseCallback);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.uploadButton.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/pdf");
            filePickerLauncher.launch(intent);;
        });

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                Uri pdfUri = result.getData().getData();

                if (pdfUri != null) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(uid, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("pdfUri", pdfUri.toString()); // Uri를 문자열로 저장
                    editor.apply();

                    Toast.makeText(getContext(), "PDF 선택됨: " + pdfUri.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "경로를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        
        binding.buttonNext.setOnClickListener(v -> {
            Fragment nextFragment = new SearchFragment6();
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

    public String getFilePathFromUri(Context context, Uri uri) {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            if (uri.getAuthority().equals("com.android.providers.downloads.documents")) {
                String[] split = documentId.split(":");
                String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + split[1];
                return filePath;
            }
        }
        return null;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
