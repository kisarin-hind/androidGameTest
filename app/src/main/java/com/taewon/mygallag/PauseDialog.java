package com.taewon.mygallag;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

public class PauseDialog extends Dialog {
    RadioGroup bgMusicOnOff, effectSoundOnOff;

    public PauseDialog(@NonNull Context context){
        super(context);
        setContentView(R.layout.pause_dialog);
        bgMusicOnOff = findViewById(R.id.bgMusicOnOff);
        effectSoundOnOff = findViewById(R.id.effectSoundOnOff);
        init();
    }
    public void init(){
        bgMusicOnOff.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.bgMusicOn:
                    MainActivity.bgMusic.setVolume(1,1);
                    break;
                case  R.id.bgMusicOff:
                    MainActivity.bgMusic.setVolume(0,0);
                    break;
            }
        });
        effectSoundOnOff.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.effectSoundOn:
                    MainActivity.effectVolumn = 1.0f;
                    break;
                case  R.id.effectSoundOff:
                    MainActivity.effectVolumn = 0;
                    break;
            }
        });
        //dismiss = cancle보다 안전하게 화면을 종료시킴, cancle과의 차이를 이해하자
        findViewById(R.id.dialogCancelBtn).setOnClickListener(v -> dismiss());
        findViewById(R.id.dialogOkBtn).setOnClickListener(v -> dismiss());
    }
}
