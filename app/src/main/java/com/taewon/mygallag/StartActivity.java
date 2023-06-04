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
        super.onCreate(savedInstanceState); //AppCompatActivity의 onCreate() 메서드를 호출하여 액티비티의 기본 초기화를 수행.
        setContentView(R.layout.activity_start);    //activity_start 레이아웃을 사용

        mediaPlayer = MediaPlayer.create(this,R.raw.robby_bgm); //음악 재생
        mediaPlayer.setLooping(true);   //반복 재생
        mediaPlayer.start();    //음악 시작
        soundPool = new SoundPool(5, AudioManager.USE_DEFAULT_STREAM_TYPE,0);   // 효과음 재생 준비
        effectId = soundPool.load(this,R.raw.reload_sound,1);   //reload sound 라는 리소스 파일을 로드하여 효과음 ID를 가져옴
        startBtn = findViewById(R.id.startBtn);
        guideTv = findViewById(R.id.guideTv);

        for (int i=0; i<imgView.length; i++){
            imgView[i] = findViewById(img_id[i]);
            int index = i;  //클릭 이벤트가 발생 했을 때 선택한 이미지 뷰의 인덱스 번호를 저장(onClickListener 안에서 i를 사용할 수 없어서)
            imgView[i].setOnClickListener(v -> {    //각각의 뷰에 클릭 이벤트 리스너 설정
                characterId = img[index];   //이미지 리소스 할당
                startBtn.setVisibility(View.VISIBLE);   //시작 버튼 보이게 함
                startBtn.setEnabled(true);  //시작버튼을 활성화시켜 누를 수 있게 함
                startBtn.setImageResource(characterId); //버튼에 선택한 이미지 넣기
                guideTv.setVisibility(View.INVISIBLE);  //마지막 TextView 숨기기
                soundPool.play(effectId,1,1,0,0,1.0f);
                //효과음 재생 이펙트 아이디에 저장된 효과음 재생. 클릭 이벤트 발생 시 효과음 재생
            });
        }
        init();
    }

    // 시작 버튼을 숨기고 비활성화하며, 클릭 시 메인 액티비티로 전환하고 선택된 캐릭터 ID를 인텐트에 추가하여 전달
    private void init(){
        //startBtn을 보이지 않도록 설정
        //invisible은 안보이지만 자리를 지키고 있는 상태(공간 점유),GONE은 뷰의 공간을 유지하지 않고 사라짐
        findViewById(R.id.startBtn).setVisibility(View.GONE);
        findViewById(R.id.startBtn).setEnabled(false);

        findViewById(R.id.startBtn).setOnClickListener(view -> {
            //StartActivity에서 MainActivity로의 전환을 위한 인텐트 생성
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
            mediaPlayer.release();  //mediaPlayer 객체가 사용한 리소스를 해제. 배경음악 재생에 사용된 미디어 플레이어 객체를 정리하는 역할
            mediaPlayer = null; //null로 설정하여 더이상 참조 불가능 하도록 설정
        }
    }
    @Override
    public void onBackPressed() {   //뒤로가기 버튼을 누르면 추가해 놓은 ExitDialog를 띄운다
        ExitDialog exitDialog = new ExitDialog(this);

        exitDialog.show();
    }
}