package com.taewon.mygallag.sprites;

import android.content.Context;

import com.taewon.mygallag.R;
import com.taewon.mygallag.SpaceInvadersView;

public class AlienShotSprite extends Sprite{    //Sprite를 상속받음
    private Context context;    //context = 어플리케이션 환경에 대한 글로벌 정보를 갖는 인터페이스
    private SpaceInvadersView game; //SpaceInvadersView을 받음
    public AlienShotSprite(Context context, SpaceInvadersView game, float x, float y, int dy){
        super(context, R.drawable.shot_001,x,y);    //적 공격 이미지
        this.game = game;
        this.context = context;
        setDy(dy);
    }
}
