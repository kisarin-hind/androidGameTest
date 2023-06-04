package com.taewon.mygallag;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.taewon.mygallag.sprites.AlienSprite;
import com.taewon.mygallag.sprites.Sprite;
import com.taewon.mygallag.sprites.StarshipSprite;

import java.util.ArrayList;
import java.util.Random;

public class SpaceInvadersView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
    //SurfaceView 는 스레드를 이용해 강제로 화면에 그려주므로 View보다 빠르다. 애니메이션, 영상 처리에 사용한다.
    //SurfaceHolder.callback = Surface의 변화감지를 위해 필요하다. 지금처럼 SurfaceView와 거의 같이 사용한다.
    public static int MAX_ENEMY_COUNT = 10; //최대 적 수
    private Context context;
    private int characterId;    //StartActivity에서 받은 비행정 이미지
    private SurfaceHolder ourHolder;    //캔버스 락&언락을 제어해 View보다 빠르게 화면에 그려줌. 프레임 단위로 변동하는 게임에 좋음.
    private Paint paint;
    public int screenW, screenH;
    private Rect src, dst;
    private ArrayList sprites = new ArrayList();    //한 프레임에서 화면에 나오는 객체들을 저장
    private Sprite starship;
    private int score, currEnemyCount;  //점수(격추한 적 수), 적수
    private Thread gameThread = null;
    private volatile boolean running;   //휘발성 bollean 함수
    private Canvas canvas;  //화면을 그리는 용도
    int mapBitmapY = 0; //배경 맵을 스크롤하는데 사용하는 변수
                                            //StartActivity에서 받아온 비행기 이미지
    public SpaceInvadersView(Context context, int characterId, int x, int y){
        super(context);
        this.context = context;
        this.characterId = characterId;
        ourHolder = getHolder();    //현재 SurfaceView를 리턴 받는다.
        paint = new Paint();
        screenW = x;
        screenH = y;
        src = new Rect();   //원본 사각형
        dst = new Rect();   //사본 사각형
        dst.set(0,0,screenW,screenH);   //시작 좌표와 끝 좌표
        startGame();
    }

    private void startGame(){
        sprites.clear();    //ArrayList 지우기(초기화)
        initSprites();  //ArrayList에 추가
        score = 0;  //게임 시작시 score는 0
    }
    public void endGame(){
        Log.e("GameOver", "GameOver");
        Intent intent = new Intent(context,ResultActivity.class);   //점수를 반환
        intent.putExtra("score",score);
        context.startActivity(intent);
        //gameThread.stop();
        ((Activity) context).finish();
    }
    public void removeSprite(Sprite sprite){ sprites.remove(sprite); }

    private void initSprites(){ //sprite초기화
        starship = new StarshipSprite(context,this,characterId,screenW/2,screenH-400,1.5f); //StarshipSprite생성 아이템 생성
        sprites.add(starship);  //starship(플레이어)을 ArrayList에 추가
        spawnEnemy();
        spawnEnemy();
    }
    public void spawnEnemy(){
        Random r = new Random();
        int x = r.nextInt(300)+100; //100~399
        int y = r.nextInt(300)+100; //100~399
        //랜덤으로 정해진 범위 내에서 AlienSprite 실행 해 적 생성
        Sprite alien = new AlienSprite(context,this,R.drawable.ship_0002, 100+x, 100+y);
        sprites.add(alien); //alien(적)을 Arraylist에 추가
        currEnemyCount++;
    }

    public ArrayList getSprites(){return sprites;}

    public void resume(){   //사용자가 만드는 resume함수
        running = true;     //public void run을 실행
        gameThread = new Thread(this);
        gameThread.start();
    }
    //Sprite를 StarshipSprite로 형변환하여 리턴
    public StarshipSprite getPlayer() {return (StarshipSprite) starship;}
    //점수 반환
    public int getScore() {return score;}

    public void setScore(int score){ this.score = score;}

    public void setCurrEnemyCount(int currEnemyCount){ this.currEnemyCount = currEnemyCount;}

    public int getCurrEnemyCount(){return currEnemyCount;}

    public void pause(){
        running = false;
        try{
            gameThread.join();//스레드 종료대기
        }catch (InterruptedException e){

        }
    }
    //클래스의 인스턴스가 Thread 객체로 전달될 때 실행

    public void run(){
        while(running){
            Random r = new Random();
            //1~100이 스피드+파워/2보다 작을 때 참 == 스피드,파워 아이템을 먹을수록 적 출현 확률 증가
            boolean isEnemySpawn = r.nextInt(100) + 1 < (getPlayer().speed +
                    (int) (getPlayer().getPowerLevel() / 2));

            //적 출현 확율(isEnemySpawn)을 만족하며 currEnemyCount(현재 적 개체수)< MAX_ENEMY_COUNT(최대 적 개채수)면 적 객체 추가
            if(isEnemySpawn && currEnemyCount < MAX_ENEMY_COUNT) spawnEnemy();

            for(int i = 0; i < sprites.size(); i++){
                Sprite sprite = (Sprite) sprites.get(i);    //ArrayList에서 하나씩 가져와서 움직임
                sprite.move();  //가져온 개체 move 메소드 실행
            }
            for(int p = 0; p < sprites.size(); p++){    //List의 요소들을 한 번씩 실행
                for(int s = p+1; s < sprites.size(); s++){//위의 요소들의 다음 인텍스부터 한 번씩 반복실행
                    try {
                        Sprite me = (Sprite) sprites.get(p);    //바깥 for의 인덱스 개체
                        Sprite other = (Sprite) sprites.get(s); //안쪽 for의 인덱스 개체
                        //충돌체크(checkCollision은 충돌 여부를 판단하는 로직을 포함한다)
                        if(me.checkCollision(other)){
                            me.handleCollision(other);
                            other.handleCollision(me);
                        }
                    }
                    catch (Exception e){    //예외가 발생해도 코드를 중단시키지 않게 하기위함
                        e.printStackTrace();
                    }
                }
            }
            draw();
            //게임 프레임 간격을 조절하기 위해 사용
            try{
                Thread.sleep(10);   //0.01초의 딜레이를 줘 프로그램 실행 속도를 제어함
            }catch (Exception e){

            }
        }
    }

    public void draw(){
        if(ourHolder.getSurface().isValid()){   //그래픽을 그리기 위한 Surface가 유효한지 확인
            canvas = ourHolder.lockCanvas();    //Canvas 객체 가져오기
            canvas.drawColor(Color.BLACK);      //배경색 검정
            mapBitmapY++;
            if(mapBitmapY<0) mapBitmapY = 0;
            paint.setColor(Color.BLUE);         //그리기에 사용되는 색은 파랑색
            for(int i = 0; i < sprites.size(); i++){    //sprites객체 수만큼 반복해서 그려줌
                Sprite sprite = (Sprite) sprites.get(i);
                sprite.draw(canvas,paint);
            }
            ourHolder.unlockCanvasAndPost(canvas);  //Canvas를 잠금 해제하고, 변경된 그래픽을 랜더링해줌
        }
    }
    //뷰가 생성될 때 호출됨.
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        startGame();
    }
    //뷰가 변경될 때 호출됨.
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }
    //뷰가 종료될 때 호출됨.
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

}
