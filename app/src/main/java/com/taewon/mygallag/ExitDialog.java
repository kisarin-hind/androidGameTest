package com.taewon.mygallag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExitDialog extends Dialog {
    Button btnExitConfirm,btnExitCancel;

    public ExitDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.activity_exit_dialog);
        btnExitConfirm = findViewById(R.id.btnExitConfirm);
        btnExitCancel = findViewById(R.id.btnExitCancel);

        findViewById(R.id.btnExitConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.finishAffinity((Activity) getContext());
                System.exit(0); //확인 버튼을 누르면 시스템 종료
            }
        });
        findViewById(R.id.btnExitCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }   //취소버튼을 누르면 다이얼로그 종료
        });
    }





}