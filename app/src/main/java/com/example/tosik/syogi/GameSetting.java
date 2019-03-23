package com.example.tosik.syogi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class GameSetting extends Activity {
    private Game_mode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setting);
        mode = new Game_mode();
    }

    @Override
    public void onResume(){
        super.onResume();
        mode = new Game_mode();
    }

    public void normal(View v){
        Intent intent = new Intent(getApplication(),Game.class);
        intent.putExtra("MODE", mode);
        startActivity(intent);
    }

    public void Check_mode(View v){
        mode.setCheck_mode();
        Intent intent = new Intent(getApplication(),Game.class);
        intent.putExtra("MODE", mode);
        startActivity(intent);
    }

    public void Restrict_mode(View v){
        mode.setRestrict(2);
        Intent intent = new Intent(getApplication(),Game.class);
        intent.putExtra("MODE", mode);
        startActivity(intent);
    }

    public void Chaos_mode(View v){
        Intent intent = new Intent(getApplication(),ChaosuSetting.class);
        startActivity(intent);
    }

    public void Chess_mode(View v){
        mode.setChess_mode();
        Intent intent = new Intent(getApplication(),Game.class);
        intent.putExtra("MODE", mode);
        startActivity(intent);
    }

    public void Twice_mode(View V){
        mode.setTwice_mode();
        Intent intent = new Intent(getApplication(),Game.class);
        intent.putExtra("MODE", mode);
        startActivity(intent);
    }

    public void Bmotion_mode(View v){
        mode.setBmotion_mode();
        Intent intent = new Intent(getApplication(),Game.class);
        intent.putExtra("MODE", mode);
        startActivity(intent);
    }


}
