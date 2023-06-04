package com.taewon.mygallag.items;


import android.content.Context;

import com.taewon.mygallag.R;
import com.taewon.mygallag.SpaceInvadersView;
import com.taewon.mygallag.sprites.Sprite;

import java.util.Timer;
import java.util.TimerTask;

public class SpeedItemSprite extends Sprite {
    SpaceInvadersView game;
    public SpeedItemSprite(Context context, SpaceInvadersView game, int x, int y, int dx, int dy){
        super(context, R.drawable.power_item,x,y);//x,y좌표에 아이템 생성
        this.game = game;
        this.dx = dx;
        this.dy = dy;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                autoRemove();
            }
        },15000);//15초가 지나면 새로운 타이머 생성
    }
    private void autoRemove(){game.removeSprite(this);}

    @Override
    public void move() {//아이템 이동방향 조정
        if((dx<0)&&(x<120)){    //dx가 음수(왼쪽으로 이동중)이고 x좌표가 120(item길이)이하(화면 왼쪽 끝에 닿으면)일때 수평 이동 방향 전환하고 반환
            dx *= -1;
            return;
        }
        if((dx<0)&&(x>game.screenW - 120)){//dx가 양수(오른쪽으로 이동중)이고 x좌표가 화면 가로길이-120이상(화면 오른쪽 끝에 닿으면)일때 수평 이동 방향 전환하고 반환
            dx *= -1;
            return;
        }
        if((dy<0)&&(y<120)){//dy가 음수(아래쪽으로 이동중)이고 y좌표가 120이하(바닥에 닿으면)면 수직 이동 방향 전환하고 반환
            dy *= -1;
            return;
        }
        if((dy>0)&&(y>game.screenH-120)){//dy가 양수(위쪽으로 이동중)이고 y좌표가 화면 세로길이-120이상(천장에 닿으면)일때 수직 이동 방향 전환하고 반환
            dy *= -1;
            return;
        }
        super.move();
    }
}
