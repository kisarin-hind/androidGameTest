package com.taewon.mygallag.sprites;


import android.content.Context;

import com.taewon.mygallag.SpaceInvadersView;

public class ShotSprite extends Sprite{
    private SpaceInvadersView game;
    public ShotSprite(Context context,SpaceInvadersView game,int resId,float x, float y, int dy){
        super(context,resId,x,y);   //Sprite 클래스의 생성자를 호출하는 부분
        this.game = game;   //SpaceInvadersView 객체를 game 멤버 변수에 할당하는 부분, ShotSprite 객체는 SpaceInvadersView 객체에 대한 참조를 유지
        setDy(dy);  //dy 값을 setDy 메서드를 통해 ShotSprite 객체의 dy 멤버 변수에 설정, setDy 메서드는 dy 값을 검증하고 할당하는 역할?
    }
}
