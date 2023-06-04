package com.taewon.mygallag.sprites;


import android.content.Context;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.taewon.mygallag.MainActivity;
import com.taewon.mygallag.R;
import com.taewon.mygallag.SpaceInvadersView;
import com.taewon.mygallag.items.HealitemSprite;
import com.taewon.mygallag.items.PowerItemSprite;
import com.taewon.mygallag.items.SpeedItemSprite;

import java.util.ArrayList;

public class StarshipSprite extends Sprite{
    Context context;
    SpaceInvadersView game;//멤버 변수로 지정
    public float speed;
    private int bullets, life=3, powerLevel;
    private int specialShotCount;
    private boolean isSpecialShooting;
    private static ArrayList<Integer> bulletSprites = new ArrayList<Integer>();
    private final static float MAX_SPEED = 3.5f;
    private final static int MAX_HEART = 3;

    private RectF rectF;
    public boolean isReloading = false;
    public StarshipSprite(Context context, SpaceInvadersView game,int resId, int x, int y, float speed){
        super(context, resId,x,y);//sprite 에서 상속받을 정보(애플리케이션 컨텍스트 , 스타쉽 이미지 리소스 아이디, 스타쉽 초기 x, y 좌표)
        this.context = context;
        this.game = game;
        this.speed = speed;
        init();
    }

    public void init(){// 스프라이트 정보 초기화
        dx=dy=0;  // 스타쉽 이동방향 초기화
        bullets=30; // 총알 갯수 초기화
        life=3; // 생명 수 초기화
        specialShotCount = 3; // 스페셜 샷 발사 가능 횟수 초기화
        powerLevel=0; // 파워 레벨 초기화
        Integer [] shots = {R.drawable.shot_001,R.drawable.shot_002,R.drawable.shot_003,
                R.drawable.shot_004,R.drawable.shot_005,R.drawable.shot_006,R.drawable.shot_007};// 탄환 스프라이트 리소스
        for(int i =0; i<shots.length; i++){
            bulletSprites.add(shots[i]);//bulletSprites 의 배열안에 shots 그림을 넣음
        }
    }

    @Override
    public void move() {//움직일때 화면 밖으로 나가지 못하게 하는 역할
        if((dx<0)&&(x<120)) return;
        if((dx>0)&&(x>game.screenW - 120)) return;
        if((dy<0)&&(y<120)) return;
        if((dy>0)&&(y>game.screenH - 120)) return;
        super.move();// 위 조건들을 만족하지 않을경우 슈퍼클래스의 move 사용하여 스타쉽을 이동시킴
    }

    public int getBulletsCount() {
        return bullets;
    }// 현재 남은 총알 개수를 알기위해 존재(메인액티비티로 보내줌)

    public void moveRight(double force){ setDx((float)(1*force*speed));}// 현재 속도(speed)와 매개변수(force) 값으로 속도를 구한 뒤 해당 방향으로 이동하게함
    public void moveLeft(double force){ setDx((float)(-1*force*speed));}
    public void moveDown(double force){ setDy((float)(1*force*speed));}
    public void moveUp(double force){ setDy((float)(-1*force*speed));}

    public void resetDx(){ setDx(0);}//객체의 이동을 멈추고 초기 상태로 되돌릴때 사용됨
    public void resetDy(){ setDy(0);}

    public void plusSpeed(float speed){ this.speed += speed;}// 스타쉽 속도 증가시키는 메서드

    public void fire(){
        if(isReloading | isSpecialShooting){return;}// 재장전 중이거나 특수공격중 발사를 못하게 만듦
        MainActivity.effectSound(MainActivity.PLAYER_SHOT); // 총알 발사 사운드 재생
        ShotSprite shot = new ShotSprite(context, game, bulletSprites.get(powerLevel),// 현재 파워 레벨에 따른 이펙트로 공격
                getX()+10, getY()-30, -16   ); // getX 와 getY의 지점에서 총알 생성 dy 값이 총알의 속도와 방향을 의미함
        game.getSprites().add(shot); // 총알 스프라이트를 스프라이트 목록에 추가
        bullets--; //발사할때마다 총알 개수 감소

        MainActivity.bulletCount.setText(bullets+"/30");//총알이 감소할때마다 남은 총알개수를 다시 표시해줌
        Log.d("bullets", bullets+"/30");
        if(bullets==0){// 총알이 다 떨어지면 reloadBullets(); 실행해 재장전을 시도함
            reloadBullets();
            return;
        }
    }
    public void powerUp(){
        if(powerLevel >= bulletSprites.size()-1){   //현재 파워레벨이 총알 스프라이트 사이즈보다 1작거나 같을때
            game.setScore(game.getScore() + 1);     // 스코어 1증가시킨뒤 종료함
            MainActivity.scoreTv.setText(Integer.toString(game.getScore()));
            return;
        }
        // 그렇지 않을경우 파워레벨을 증가 시킨 뒤
        MainActivity.fireBtn.setImageResource(bulletSprites.get(powerLevel)); // 파워업한 현재 총알 이미지로 발사 버튼의 이미지도 변경함
        MainActivity.fireBtn.setBackgroundResource(R.drawable.round_button_shape);// <= 왜 있지? 원래부터 round_button_shape임
    }
    public void reloadBullets(){
        isReloading = true;//재장전 중일때 fireBtn 과 reloadBtn 비활성화시킴
        MainActivity.effectSound(MainActivity.PLAYER_RELOAD);
        MainActivity.fireBtn.setEnabled(false);
        MainActivity.reloadBtn.setEnabled(false);
        //Thread sleep 사용하지 않고 지연시키는 클래스
        new Handler().postDelayed(new Runnable(){// 일정시간 지난후에 아래 작업을 수행하게 2초 딜레이를 줌
            @Override
            public void run() {
                bullets=30;
                MainActivity.fireBtn.setEnabled(true);// 비활성화 시켜놨던 버튼들 다시 활성화 시켜줌
                MainActivity.reloadBtn.setEnabled(true);
                MainActivity.bulletCount.setText(bullets+"/30");
                MainActivity.bulletCount.invalidate();  //화면새로고침, invalidate => 호출시 변경된 ui를 적용시킴
                isReloading=false;  //다시 false로
            }
        }, 2000);
    }

    public void specialShot(){
        specialShotCount--;// 스페셜 샷 공격 가능 회수 1 감소

        //SpecialshotSprite 구현
        SpecialshotSprite shot = new SpecialshotSprite(context, game, R.drawable.laser,
                getRect().right - getRect().left, 0);

        //game -> SpaceInvadersView의 getSprites() : sprite에 shot추가하기
        game.getSprites().add(shot);
    }

    public int getSpecialShotCount() {return specialShotCount;}// 현재 스페셜 샷 갯수 반환
    public boolean isSpecialShooting() {return isSpecialShooting;}// 미사용 코드

    public void setSpecialShooting(boolean specialShooting) {//현재 스페셜 샷이 활성화 되어있는지 여부 확인
        isSpecialShooting = specialShooting;
    }
    public int getLife() {return life;}

    public void hurt(){
        life--;// 체력 1 감소
        if(life<=0){
            ((ImageView) MainActivity.lifeFrame.getChildAt(life))
                    .setImageResource(R.drawable.ic_baseline_favorite_border_24);
            //SpaceInvadersView의 endGame()에서 game종료시키기
            game.endGame();
            return;
        }
        Log.d("hurt",Integer.toString(life));   //생명확인
        ((ImageView) MainActivity.lifeFrame.getChildAt(life))
                .setImageResource(R.drawable.ic_baseline_favorite_border_24);// 체력이 남아있을 경우 이미지 변경
    }
    //getChildAt(life 는 현재 체력 1, 2 ,3) 괄호안에 있는 life 의 현재 숫자가 자식View 가 됨 => 해당 View 를 변경
    //css 에서 nth-child 와 비슷함
    public void heal(){
        Log.d("heal",Integer.toString(life));
        if(life+1>MAX_HEART){// 힐템을 먹어서 현재 체력이 최대체력 보다 높아질 경우 체력을 더 늘리는게 아닌 스코어를 1점 추가해줌
            game.setScore(game.getScore()+1);
            MainActivity.scoreTv.setText(Integer.toString(game.getScore()));
            return;
        }
        ((ImageView) MainActivity.lifeFrame.getChildAt(life))
                .setImageResource(R.drawable.ic_baseline_favorite_24);// 회복할 체력 있으면 해당 체력의 이미지뷰를 변경
        life++;
    }
    private void speedUp(){
        if(MAX_SPEED >= speed +0.2f) plusSpeed(0.2f);// 현재 증가시킬 속도가 최대속도보다 작거나 같은지 확인해서 그 값이 참일경우 속도를 증가 시켜줌
        else{
            game.setScore(game.getScore()+1);// 거짓일 경우 스코어를 1 증가시켜줌
            MainActivity.scoreTv.setText(Integer.toString((game.getScore())));
        }
    }
    //line객체가 아니라 shape객체로 업캐스팅하면 다운캐스팅해서 자식인 line도 되고 shape도 되고?
    //Sprite의 handleCollision() -> 충돌처리
    @Override
    public void handleCollision(Sprite other) {//스프라이트 간의 충돌 발생시 처리할 동작을 구현
        // 스타쉽 스프라이트와 충돌한 다른 스프라이트를 제거하고 각 충돌에 실행시켜야 하는 메서드와 효과음을 호출함
        if(other instanceof AlienSprite){
            //Alien아이템이면
            game.removeSprite(other);
            MainActivity.effectSound(MainActivity.PLAYER_HURT);
            hurt();
        }
        if(other instanceof SpeedItemSprite){
            //스피드 아이템이면
            game.removeSprite(other);
            MainActivity.effectSound(MainActivity.PLAYER_GET_ITEM);
            speedUp();
        }
        if(other instanceof AlienShotSprite){
            //총맞으면
            MainActivity.effectSound(MainActivity.PLAYER_HURT);
            game.removeSprite(other);
            hurt();
        }
        if(other instanceof PowerItemSprite){
            //파워 아이템이면
            MainActivity.effectSound(MainActivity.PLAYER_GET_ITEM);
            powerUp();
            game.removeSprite(other);
        }
        if(other instanceof HealitemSprite){
            //파워 아이템이면
            MainActivity.effectSound(MainActivity.PLAYER_GET_ITEM);
            game.removeSprite(other);
            heal();
        }
    }
    public int getPowerLevel(){ return powerLevel;}// 스타쉽 파워 레벨 반환
}