package com.taewon.mygallag.sprites;


import android.content.Context;
import android.os.Looper;

import com.taewon.mygallag.MainActivity;
import com.taewon.mygallag.SpaceInvadersView;
import com.taewon.mygallag.items.HealitemSprite;
import com.taewon.mygallag.items.PowerItemSprite;
import com.taewon.mygallag.items.SpeedItemSprite;

import java.util.ArrayList;
import java.util.Random;
import android.os.Handler;
import android.util.Log;

public class AlienSprite extends Sprite {
    private Context context;
    private SpaceInvadersView game;
    ArrayList<AlienShotSprite> alienShotSprites;    //alienShotSprites를 배열에 넣는다
    Handler fireHandler = null; //적 공격을 위한 핸들러
    boolean isDestroyed = false;
    public AlienSprite(Context context, SpaceInvadersView game, int resId, int x, int y){
        super(context,resId,x,y);
        this.context = context;
        this.game = game;
        alienShotSprites = new ArrayList<>();
        Random r = new Random();
        int randomDx = r.nextInt(5);
        int randomDy = r.nextInt(5);
        if(randomDy<=0) dy=1;   //수직 이동 거리가 0이하이면 1로 잡아줌
        dx = randomDx; dy = randomDy;   //랜덤 값을 줘 일정 범위 내(0~4)에서 적이 랜덤하게 이동
        fireHandler = new Handler(Looper.getMainLooper()) {
        };
        //Handler = 특정 메세지를 Looper 의 MessageQueue 에 넣거나, Looper 가 MessageQueue 에서 특정 메세지를 꺼내어 전달하면 이를 처리하는 기능을 수행하는 중간 다리 역할을 수행한다.
        //Message = 하나의 작은 작업 단위
        //MessageQueue = Message를 하나씩 적재해 Looper가 차례대로 처리할 수 있게 해줌
        fireHandler.postDelayed(new Runnable() {    //1초 후에 실행, 핸들러(postDelayed)를 이용한 지연 실행
            @Override
            public void run() {
                Log.d("run", "동작");
                Random r = new Random();
                //isFire = 1~100의 숫자를 생성해 그 숫자가 30보다 작거나 같으면 참, 크면 거짓으로 간주하는 변수 = 30% 확율
                boolean isFire = r.nextInt(100)+1 <= 30;
                if(isFire && !isDestroyed){     //isFire가 참이고 isDestroyed가 거짓이면 fire()실행
                    fire();
                    fireHandler.postDelayed(this,1000);
                }
            }
        },1000);
    }

    @Override
    public void move() {
        super.move();
        //dx가 0보다 작고 x도 10보다 작은 상황 or dx가 0보다 크고 x도 800보다 많은 상황
        if(((dx<0)&&(x<10)||(dx>0)&&(x>800))){
            dx = -dx;   //가로 이동 방향 변경
            if(y>game.screenH){game.removeSprite(this);}    //화면을 벗어나면 SpaceInvadersView의 removeSprite실행
        }
    }
    private void fire(){
        AlienShotSprite alienShotSprite = new AlienShotSprite(context, game, getX(), getY()+30, 16);
        alienShotSprites.add(alienShotSprite);//alienShotSprite를 위한 Arraylist 추가
        game.getSprites().add(alienShotSprite); //spaceInvadersView의 Arraylist에 추가
    }

    @Override
    public void handleCollision(Sprite other) {
        if(other instanceof ShotSprite){//적이 총알 맞으면
            game.removeSprite(other);   //부딛힌 총알 제거
            game.removeSprite(this);    //적 제거
            destroyAlien();
            return;
        }
        if(other instanceof SpecialshotSprite){ //적이 번개 맞으면
            game.removeSprite(this);    //적 제거
            destroyAlien();
            return;
        }
    }
    private void destroyAlien(){    //적이 사라질 때
        isDestroyed = true;     //거짓에서 참으로 변경 = 적이 파괴됨
        game.setCurrEnemyCount(game.getCurrEnemyCount()-1); //getCurrEnemyCount 1 감소
        for(int i=0; i<alienShotSprites.size(); i++)
            game.removeSprite(alienShotSprites.get(i));
        //각각의 아이템 드롭 함수로 넘어가짐
        spawnHealItem();
        spawnPowerItem();
        spawnSpeedItem();
        //점수 상승
        game.setScore(game.getScore()+1);
        MainActivity.scoreTv.setText(Integer.toString(game.getScore()));
    }

    private void spawnSpeedItem(){
        Random r = new Random();
        int speedItemDrop = r.nextInt(100)+1;
        //1~100 <= 5 속도 상승 아이템 드랍률5%
        if(speedItemDrop <=5){
            int dx = r.nextInt(10)+1;
            int dy = r.nextInt(10)+5;
            game.getSprites().add(new SpeedItemSprite(context,game,
                    (int)this.getX(),(int) this.getY(),dx,dy));
        }
    }
    private void spawnPowerItem(){
        Random r = new Random();
        int powerItemDrop = r.nextInt(100)+1;
        //1~100 <= 3 파워 상승 아이템 드랍률 3%
        if(powerItemDrop <=3){
            int dx = r.nextInt(10)+1;
            int dy = r.nextInt(10)+10;
            game.getSprites().add(new PowerItemSprite(context,game,
                    (int)this.getX(),(int) this.getY(),dx,dy));
        }
    }
    private void spawnHealItem(){
        Random r = new Random();
        int healItemDrop = r.nextInt(100)+1;
        //1~100 <= 3 회복 아이템 드랍률 1%
        if(healItemDrop <=1){
            int dx = r.nextInt(10)+1;
            int dy = r.nextInt(10)+10;
            game.getSprites().add(new HealitemSprite(context,game,
                    (int)this.getX(),(int) this.getY(),dx,dy));
        }
    }
}
