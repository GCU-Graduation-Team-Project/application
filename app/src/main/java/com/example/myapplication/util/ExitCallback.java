package com.example.myapplication.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.activity.OnBackPressedCallback;

public class ExitCallback extends OnBackPressedCallback {
    private final Activity activity;

    public ExitCallback(Activity activity) {
        super(true);
        this.activity = activity;
    }

    @Override
    public void handleOnBackPressed() {
        new AlertDialog.Builder(activity)
                .setMessage("앱을 종료하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finishAffinity();
                        System.exit(0);
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
