package com.taewon.mygallag.sprites;


import android.content.Context;

import com.taewon.mygallag.SpaceInvadersView;

import java.util.Timer;
import java.util.TimerTask;

public class SpecialshotSprite extends Sprite{
    private SpaceInvadersView game; // SpaceInvadersView : 게임을 상호작용 하는 뷰 클래스

    public SpecialshotSprite(Context context, SpaceInvadersView game, int resId, float x, float y){
        super(context, resId,x,y);  // resId : 특수 발사에 대한 이미지 리소스의 식별자 , x와 y는 특수 발사의 초기 위치
        this.game=game; // 게임 멤버 변수를 주입 받아 현재 게임 인스턴스 참조
        game.getPlayer().setSpecialShooting(true);// 게임 플레이어의 특수 발사 상태를 활성화

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                autoRemove();
            }
        },5000);//5초 뒤에 사라짐
    }

    @Override
    public void move() {    //특수 공격의 위치를 업데이트 하는 메서드
        super.move();
        this.x = game.getPlayer().getX() - getWidth() + 240;//특정값 240을 X 좌표에서 뺀 값 특수 발사는 플레이어의 왼쪽에서 240만큼 떨어진 위치
        this.y = game.getPlayer().getY() - getHeight();// 플레이어의 Y 좌표에서 스프라이트의 높이를 뺀 값으로 설정이 됨.
    }

    public void autoRemove(){   //특수 공격을 제거 해주는 메서드
        game.getPlayer().setSpecialShooting(false); //특수 발사 상태 해제. 이제 특수 발사는 되지않음
        game.removeSprite(this);    //특수 발사 스프라이트를 화면에서 제거
    }
}
