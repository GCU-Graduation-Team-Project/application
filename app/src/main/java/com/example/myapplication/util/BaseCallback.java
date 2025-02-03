package com.example.myapplication.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.ui.fragments.SearchFragment;



public class BaseCallback extends OnBackPressedCallback {

    private final Activity activity;

    public BaseCallback(Activity activity) {
        super(true);
        this.activity = activity;
    }


    @Override
    public void handleOnBackPressed() {
        new AlertDialog.Builder(activity)
                .setMessage("정보가 저장되지 않습니다.\n처음화면으로 돌아가시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentTransaction transaction = ((AppCompatActivity) activity).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, new SearchFragment());
                        transaction.commit();
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                .show();
    }
}
