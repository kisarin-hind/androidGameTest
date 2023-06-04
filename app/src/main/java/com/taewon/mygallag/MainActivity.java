package com.taewon.mygallag;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {
    private Intent userIntent;
    ArrayList<Integer> bgMusicList;
    public static SoundPool effectSound;    ////static을 적으면 객체를 안만들어도 쓸 수 있다.
    public static float effectVolumn;
    ImageButton specialShotBtn;
    public static ImageButton fireBtn, reloadBtn;
    JoystickView joyStick;
    public static TextView scoreTv;
    LinearLayout gameFrame;
    ImageView pauseBtn;
    public static  LinearLayout lifeFrame;
    SpaceInvadersView spaceInvadersView;
    public static MediaPlayer bgMusic;
    int bgMusicIndex;
    public static TextView bulletCount;
    private static ArrayList<Integer> effectSoundList;
    public static final int PLAYER_SHOT =0;
    public static final int PLAYER_HURT =1;
    public static final int PLAYER_RELOAD =2;
    public static final int PLAYER_GET_ITEM =3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userIntent = getIntent();
        bgMusicIndex = 0;
        bgMusicList = new ArrayList<Integer>();
        bgMusicList.add(R.raw.main_game_bgm1);
        bgMusicList.add(R.raw.main_game_bgm2);
        bgMusicList.add(R.raw.main_game_bgm3);

        effectSound = new SoundPool(5, AudioManager.USE_DEFAULT_STREAM_TYPE,0);
        effectVolumn = 1;

        specialShotBtn = findViewById(R.id.specialShotBtn);
        joyStick = findViewById(R.id.joyStick);
        scoreTv = findViewById(R.id.score);
        fireBtn = findViewById(R.id.fireBtn);
        reloadBtn = findViewById(R.id.reloadBtn);
        gameFrame = findViewById(R.id.gameFrame);
        pauseBtn = findViewById(R.id.pauseBtn);
        lifeFrame = findViewById(R.id.lifeFrame);

        init();
        setBtnBehavior();//조이스틱 작동함수 실행
    }

    @Override
    protected void onResume() {
        super.onResume();
        bgMusic.start();
        spaceInvadersView.resume();
    }
    private void init(){    // 기본적인 노래설정 정보 초기화 등등..
        Display display = getWindowManager().getDefaultDisplay();   //현재 view의 display를 얻어온다.
        Point size = new Point();
        display.getSize(size);  // 현재 화면 크기 정보를 Point 객체로 가져옴

        //이전 액티비티에서 전달받은 캐릭터 정보를 받아와서 spaceInvadersView 를 생성
        spaceInvadersView = new SpaceInvadersView(this,userIntent.getIntExtra(("character"),R.drawable.ship_0000),size.x,size.y);
        gameFrame.addView(spaceInvadersView);   //프레임에 만든 아이템 넣기

        //음악 바꾸기
        changeBgMusic();
        //음악 끝나면 음악 변경
        bgMusic.setOnCompletionListener(mediaPlayer -> changeBgMusic());

        bulletCount = findViewById(R.id.bulletCount);

        bulletCount.setText(spaceInvadersView.getPlayer().getBulletsCount()+"/30");//StarShipSprite 에서 받아온 정보를 토대로 현재 총알 개수와 점수를 설정해서 각 View 에 보여줌
        scoreTv.setText(Integer.toString(spaceInvadersView.getScore()));

        effectSoundList = new ArrayList<>();// 각각의 효과음 로드
        effectSoundList.add(PLAYER_SHOT, effectSound.load(MainActivity.this, R.raw.player_shot_sound,1));
        effectSoundList.add(PLAYER_HURT, effectSound.load(MainActivity.this, R.raw.player_hurt_sound,1));
        effectSoundList.add(PLAYER_RELOAD, effectSound.load(MainActivity.this, R.raw.reload_sound,1));
        effectSoundList.add(PLAYER_GET_ITEM, effectSound.load(MainActivity.this, R.raw.player_get_item_sound,1));
        bgMusic.start();    //배경음악 재생
    }
    private void changeBgMusic(){
        bgMusic = MediaPlayer.create(this,bgMusicList.get(bgMusicIndex));//bgMusicIndex 에 해당하는 음악으로 새로운 배경음악 생성
        bgMusic.start();// 생성된 배경음악 재생
        bgMusic.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                changeBgMusic();
            }
        });
        bgMusicIndex++;
        //인덱스 반복(음악 갯수만큼 바뀜)
        bgMusicIndex = bgMusicIndex % bgMusicList.size();// bgMusicIndex 값 설정 => bgMusicList 의 사이즈를 bgMusicIndex 의 값으로 나눈 나머지
    }

    @Override
    protected void onPause() {// 액티비티 일시 정지
        super.onPause();
        bgMusic.pause(); // 배경음악 일시 정지
        spaceInvadersView.pause(); // 게임 일시 정지
    }

    public static void effectSound(int flag){
        // 괄호안 순서대로 ( 재생할 효과음 리소스 ID, 효과음 볼륨 , 효과음 볼륨, 재생 우선순위(기본값) , 반복재생 여부 (반복하지 않음) , 재생속도(기본값))
        effectSound.play(effectSoundList.get(flag), effectVolumn, effectVolumn,
                0,0,1.0f);//soundpool 실행
    }

    private void setBtnBehavior(){
        joyStick.setAutoReCenterButton(true);// 조이스틱 자동 중앙 재설정 기능
        joyStick.setOnKeyListener(new View.OnKeyListener() {//조이스틱의 키 이벤트를 받아와서 로그에 키 코드를 출력함
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("KeyCode",Integer.toString(i));
                return false;
            }
        });
        joyStick.setOnMoveListener(new JoystickView.OnMoveListener() {// 이동 각도와 세기에 따라 플레이어 이동시킴
            @Override
            public void onMove(int angle, int strength) {
                if(angle >67.5 && angle <112.5){    //위
                    spaceInvadersView.getPlayer().moveUp(strength/10);
                    spaceInvadersView.getPlayer().resetDx();
                } else if (angle >247.5 && angle <292.5) {  //아래
                    spaceInvadersView.getPlayer().moveDown(strength/10);
                    spaceInvadersView.getPlayer().resetDx();
                } else if (angle >112.5 && angle <157.5) {//왼쪽 위
                    spaceInvadersView.getPlayer().moveUp(strength/10 * 0.5);
                    spaceInvadersView.getPlayer().moveLeft(strength/10 * 0.5);
                } else if (angle >157.5 && angle <202.5) {//왼쪽
                    spaceInvadersView.getPlayer().moveLeft(strength/10);
                    spaceInvadersView.getPlayer().resetDy();
                } else if (angle >202.5 && angle <247.5) {//왼쪽 아래
                    spaceInvadersView.getPlayer().moveLeft(strength/10 * 0.5);
                    spaceInvadersView.getPlayer().moveDown(strength/10 * 0.5);
                } else if (angle >22.5 && angle <67.5) {//오른쪽 위
                    spaceInvadersView.getPlayer().moveUp(strength/10 * 0.5);
                    spaceInvadersView.getPlayer().moveRight(strength/10 * 0.5);
                } else if (angle >337.5 && angle <22.5) {//오른쪽
                    spaceInvadersView.getPlayer().moveRight(strength/10);
                    spaceInvadersView.getPlayer().resetDy();
                } else if (angle >292.5 && angle <337.5) {//오른쪽 아래
                    spaceInvadersView.getPlayer().moveRight(strength/10 * 0.5);
                    spaceInvadersView.getPlayer().moveDown(strength/10 * 0.5);
                }
            }
        });
        // 버튼이 눌렸을때 spaceInvadersView.getPlayer() 의 fire() 메서드를 호출시킴
        fireBtn.setOnClickListener(view -> spaceInvadersView.getPlayer().fire());
        // 버튼이 눌렸을때 spaceInvadersView.getPlayer() 의 reloadBullets() 메서드를 호출시킴
        reloadBtn.setOnClickListener(view -> spaceInvadersView.getPlayer().reloadBullets());

        pauseBtn.setOnClickListener(view -> {
            spaceInvadersView.pause();  //spaceInvadersView 일시정지
            PauseDialog pauseDialog = new PauseDialog(MainActivity.this);
            pauseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    spaceInvadersView.resume(); //spaceInvadersView 종료
                }
            });
            pauseDialog.show();
        });
        specialShotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spaceInvadersView.getPlayer().getSpecialShotCount() >= 0)// 스페셜 샷 카운트가 0보다 크거나 같을경우
                    spaceInvadersView.getPlayer().specialShot(); // spaceInvadersView.getPlayer() 에서 specialShot() 메서드를 호출해옴
            }
        });
    }

    // 뒤로가기 버튼 누르면 PauseDialog 를 띄움
    @Override
    public void onBackPressed() {
        spaceInvadersView.pause();
        PauseDialog pauseDialog = new PauseDialog(MainActivity.this);
        pauseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                spaceInvadersView.resume();
            }
        });
        pauseDialog.show();
    }

}