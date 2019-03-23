package com.example.tosik.syogi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class Game extends Activity {

    private Game_mode mode;
    private View view;
    private View view2;

    ArrayList<View> arr = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade());


        setContentView(R.layout.game);
        view = new SyogiView(this,this);
        arr.add(view);

        FrameLayout frame;
        frame = (FrameLayout)this.findViewById(R.id.frame);
        frame.addView(view, 0);			//一番奥にReversiViewを追加。

    }

    public void setGamemode(Game_mode mode){
        this.mode = mode;
    }

    //カオスモード時のルール表示
    public void Chaos_sub(){
        //持ち駒制限の表示
        if(mode.getRestrict(1)!=0) {
            TextView textview_rec1 = findViewById(R.id.Rectrict_text1);
            textview_rec1.setText(mode.getRestrictText(1));
            textview_rec1.setVisibility(View.VISIBLE);
        }
        if(mode.getRestrict(2)!=0) {
            TextView textview_rec2 = findViewById(R.id.Rectrict_text2);
            textview_rec2.setText(mode.getRestrictText(2));
            textview_rec2.setVisibility(View.VISIBLE);
        }

        //駒落ちの表示
        if(!(mode.getHandi(1).equals("なし"))){
            TextView textview_rec1 = findViewById(R.id.Handi_text1);
            textview_rec1.setText(mode.getHandi(1));
            textview_rec1.setVisibility(View.VISIBLE);
        }
        if(!(mode.getHandi(2).equals("なし"))){
            TextView textview_rec1 = findViewById(R.id.Handi_text2);
            textview_rec1.setText(mode.getHandi(2));
            textview_rec1.setVisibility(View.VISIBLE);
        }

        //クイーン将棋の表示
        if(mode.getChess_mode(1)!=0) {
            TextView textview_Check1 = findViewById(R.id.Check_text1);
            textview_Check1.setText("王手将棋");
            textview_Check1.setVisibility(View.VISIBLE);
        }
        if(mode.getChess_mode(2)!=0) {
            TextView textview_Check2 = findViewById(R.id.Check_text2);
            textview_Check2.setText("王手将棋");
            textview_Check2.setVisibility(View.VISIBLE);
        }

        //王手将棋の表示
        if(mode.getCheck_mode(1)!=0) {
            TextView textview_Chess1 = findViewById(R.id.Chess_text1);
            textview_Chess1.setVisibility(View.VISIBLE);
        }
        if(mode.getCheck_mode(2)!=0) {
            TextView textview_Chess2 = findViewById(R.id.Chess_text2);
            textview_Chess2.setVisibility(View.VISIBLE);
        }

        //２手差し将棋の表示
        if(mode.getTwice_mode(1)!=0) {
            TextView textview_Twice1 = findViewById(R.id.Twice_text);
            textview_Twice1.setVisibility(View.VISIBLE);
        }
        if(mode.getTwice_mode(2)!=0) {
            TextView textview_Twice2 = findViewById(R.id.Twice_text2);
            textview_Twice2.setVisibility(View.VISIBLE);
        }

        //安南将棋の表示
        if(mode.getTwice_mode(1)!=0) {
            TextView textview_Bmotion1 = findViewById(R.id.Bmotion_text);
            textview_Bmotion1.setVisibility(View.VISIBLE);
        }
        if(mode.getTwice_mode(2)!=0) {
            TextView textview_Bmotion2 = findViewById(R.id.Bmotion_text2);
            textview_Bmotion2.setVisibility(View.VISIBLE);
        }
    }

    //勝った時のテキスト表示
    public void win(int turn) {
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup viewGroup = findViewById(R.id.relative_layout);
        View view3;
        view3 = new Win_Lose(this,turn);
        FrameLayout frame;
        frame = (FrameLayout)this.findViewById(R.id.frame);
        frame.addView(view3, 1);
        view2 = inflater.inflate( R.layout.game_end, viewGroup);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        arr.add(view2);
        view2.startAnimation(animation);
        view2.setVisibility(View.VISIBLE);
    }

    //決着後の選択ボダン
    public void end(View v){
        Intent intent = new Intent(getApplication(),MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void restart(View v){
        Intent intent = new Intent();
        intent.setClass(this, this.getClass());
        intent.putExtra("MODE", mode);
        this.startActivity(intent);
        this.finish();
    }


}
