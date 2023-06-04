package com.taewon.mygallag.sprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Sprite {
    //protected 접근 제한자를 사용하여 상속받은 클래스에서도 접근 가능하도록 선언
    protected float x, y;
    protected int width, height;
    protected float dx, dy;     //dx는 수평 dy는 수직 방향으로의 이동 속도를 나타내는 값
    private Bitmap bitmap;  //스프라이트의 비트맵 이미지를 저장하는 변수
    protected int id;   //스프라이트의 고유 ID를 저장하는 변수
    private RectF rect; //스프라이트의 경계 사각형을 나타내는 객체

    //이 생성자를 통해 스프라이트 객체가 생성 주어진 리소스 ID로부터 비트맵 이미지를 디코딩, 위치와 크기 정보를 설정.
    public Sprite(Context context, int resourceId, float x, float y){
        this.id = resourceId;
        this.x = x; this.y = y;
        bitmap = BitmapFactory.decodeResource(context.getResources(),resourceId);//context의 리소스에서 resourceId에 해당하는 이미지를 디코딩하여 bitmap 변수에 저장
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        rect = new RectF(); //sprite의 충돌 판정 영역(사각형)
    }

    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public void draw(Canvas canvas, Paint paint) {canvas.drawBitmap(bitmap, x,y,paint);}

    public void move(){
        x = x+dx; y = y+dy; // x와 y의 값을 dx, dy값 만큼 증가시킴 따라서 이동과 방향의 거리는 dx와 dy에 의해 결정
        rect.left=x; rect.right = x+width;      // 왼쪽 사각형은 x로 , 오른쪽 사각형은 x + width로 설정하면서 왼쪽과 오른쪽 X좌표 업데이트. (너비)
        rect.top = y; rect.bottom = y + height; // top은 y로 botton은 y+height로 설정해주면서 상단과 하단의 Y좌표 업데이트. (높이)
    }

    public float getX() {return x;}
    public float getY() {return y;}
    public float getDx() {return dx;}
    public float getDy() {return dy;}

    public void setDx(float dx) {this.dx = dx;}
    public void setDy(float dy) {this.dy = dy;}
    public RectF getRect() {return rect;}


    public boolean checkCollision(Sprite other){
        return RectF.intersects(this.getRect(), other.getRect());   //현재 스프라이트와 다른  스프라이트의 rect가 교차하는지 확인하고 그 결과를 반환
    }

    public void handleCollision(Sprite other){} //스프라이트 간의 충돌처리

    public Bitmap getBitmap() {
        return bitmap;
    } // 현재 비트맵 이미지 반환 bitmap의 변수 반환

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }   //스프라이트의 비트맵 이미지 설정. 주어진 bitmap의 값을 this.bitmap에 할당
}
