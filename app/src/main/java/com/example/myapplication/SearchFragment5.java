package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.SearchFragment5Binding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SearchFragment5 extends Fragment {
    private SearchFragment5Binding binding;
    private OnBackPressedCallback main_callback;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ActivityResultLauncher<Intent> filePickerLauncher;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchFragment5Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.progressBar.setProgress(100);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        db =  FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();


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
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
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
