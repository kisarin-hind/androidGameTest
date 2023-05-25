package com.taewon.mygallag;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    int characterId, effectId;
    ImageButton startBtn;
    TextView guideTv;
    MediaPlayer mediaPlayer;
    ImageView imgView[] = new ImageView[8];
    Integer img_id[] = {R.id.ship_001,R.id.ship_002,R.id.ship_003,R.id.ship_004,R.id.ship_005,R.id.ship_006,R.id.ship_007,R.id.ship_008};
    Integer img[] = {R.drawable.ship_0000,R.drawable.ship_0001,R.drawable.ship_0002,R.drawable.ship_0003,R.drawable.ship_0004,R.drawable.ship_0005,R.drawable.ship_0006,R.drawable.ship_0007};
    SoundPool soundPool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mediaPlayer = MediaPlayer.create(this,R.raw.robby_bgm);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        soundPool = new SoundPool(5, AudioManager.USE_DEFAULT_STREAM_TYPE,0);
        effectId = soundPool.load(this,R.raw.reload_sound,1);
        startBtn = findViewById(R.id.startBtn);
        guideTv = findViewById(R.id.guideTv);

        for (int i=0; i<imgView.length; i++){
            imgView[i] = findViewById(img_id[i]);
            int index = i;  //선택한 이미지 번호 알기(onClickListener 안에서 i를 사용할 수 없어서)
            imgView[i].setOnClickListener(v -> {
                characterId = img[index];
                startBtn.setVisibility(View.VISIBLE);
                startBtn.setEnabled(true);
                startBtn.setImageResource(characterId); //버튼에 선택한 이미지 넣기
                guideTv.setVisibility(View.INVISIBLE);  //마지막 TextView 숨기기
                soundPool.play(effectId,1,1,0,0,1.0f);
            });
        }
        init();
    }

    private void init(){
        findViewById(R.id.startBtn).setVisibility(View.GONE);
        findViewById(R.id.startBtn).setEnabled(false);

        findViewById(R.id.startBtn).setOnClickListener(view -> {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            intent.putExtra("character",characterId);
            startActivity(intent);
            finish();   //액티비티 종료
        });
    }

    //액티비티 소멸직전에 호출된다
    //mediaPlayer가 살아있으면 리소스를 소멸시킨다.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}