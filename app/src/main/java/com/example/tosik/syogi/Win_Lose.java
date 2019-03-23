package com.example.tosik.syogi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

//勝った時の勝敗表示View
public class Win_Lose extends View {
    private float mas;//ひとまず分の大きさメモ
    private int turn;

    public Win_Lose(Context context,int turn){
        super(context);
        setFocusable(true);
        this.turn = turn;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mas = getWidth()/9;
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextSize(mas*3);


        if(turn ==2){
            canvas.save();

            float text_Width = paint.measureText("Win");
            paint.setTextSkewX(-0.25f);
            paint.setColor(Color.rgb(255, 0, 0));
            canvas.rotate(180f,getWidth()/2, mas*2);
            canvas.drawText("Win", getWidth()/2-text_Width/2, mas*2, paint);
            canvas.restore();

            text_Width = paint.measureText("Lose");
            paint.setTextSkewX(-0.25f);
            paint.setColor(Color.rgb(0, 0, 255));
            canvas.drawText("Lose", getWidth()/2-text_Width/2, mas*15, paint);
        }else{
            canvas.save();

            float text_Width = paint.measureText("Lose");
            paint.setTextSkewX(-0.25f);
            paint.setColor(Color.rgb(0, 0, 255));
            canvas.rotate(180f,getWidth()/2, mas*2);
            canvas.drawText("Lose", getWidth()/2-text_Width/2, mas*2, paint);
            canvas.restore();

            text_Width = paint.measureText("Win");
            paint.setTextSkewX(-0.25f);
            paint.setColor(Color.rgb(255, 0, 0));
            canvas.drawText("Win", getWidth()/2-text_Width/2, mas*15, paint);
        }
    }

    //指した時の動作
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
