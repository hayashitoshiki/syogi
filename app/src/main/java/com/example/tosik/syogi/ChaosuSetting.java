package com.example.tosik.syogi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class ChaosuSetting extends AppCompatActivity {
    private Game_mode mode;
    // 選択肢
    private String spinnerItems[] = {"なし", "１", "２", "３","４","５"};
    private String Hande[] = {"なし","角落ち","飛車落ち","二枚落ち","四枚落ち","六枚落ち","八枚落ち","十枚落ち"};
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chaosu_setting);

        mode = new Game_mode();
        mode.set_Mode(3);

        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);
        Spinner Handespinner1 = findViewById(R.id.Handespinner1);
        Spinner Handespinner2 = findViewById(R.id.Handespinner2);

        // ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Hande);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // spinner に adapter をセット
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        Handespinner1.setAdapter(adapter2);
        Handespinner2.setAdapter(adapter2);

        // 制限将棋の制限枚数セット
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                String item = (String)spinner.getSelectedItem();
                if(item.equals("なし")){
                    i=0;
                }else{
                    i =Integer.parseInt(item);
                }
                mode.setRestrict(i,1);
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                String item = (String)spinner.getSelectedItem();
                if(item.equals("なし")){
                    i=0;
                }else{
                    i =Integer.parseInt(item);
                }
                mode.setRestrict(i,2);
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //駒落ちのセット
        Handespinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                String item = (String)spinner.getSelectedItem();
                mode.setHande(item,1);
                Log.d("セット","item"+item);
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        Handespinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
                Spinner spinner = (Spinner)parent;
                String item = (String)spinner.getSelectedItem();
                mode.setHande(item,2);
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //王手将棋のセット
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.Check_Group1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    RadioButton radioButton = (RadioButton) findViewById(checkedId);
                    String text = radioButton.getText().toString();
                    if(text.equals("ON")){
                        Log.d("checkd,","mo-do "+1);
                        mode.setCheck_mode(1,1);
                    }else{
                        mode.setCheck_mode(1,0);
                    }
                }
            }
        });
        RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.Check_Group2);
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    RadioButton radioButton = (RadioButton) findViewById(checkedId);
                    String text = radioButton.getText().toString();
                    if(text.equals("ON")){
                        Log.d("checkd,","mo-do "+1);
                        mode.setCheck_mode(2,1);
                    }else{
                        mode.setCheck_mode(2,0);
                    }
                }
            }
        });


        //２手差し将棋のセット
        RadioGroup TwiceGroup = (RadioGroup) findViewById(R.id.Twice_Group1);
       TwiceGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    RadioButton radioButton = (RadioButton) findViewById(checkedId);
                    String text = radioButton.getText().toString();
                    if(text.equals("ON")){
                        Log.d("checkd,","mo-do "+1);
                        mode.setTwice_mode(1,1);
                    }else{
                        mode.setTwice_mode(1,0);
                    }
                }
            }
        });
        RadioGroup TwiceGroup2 = (RadioGroup) findViewById(R.id.Twice_Group2);
        TwiceGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    RadioButton radioButton = (RadioButton) findViewById(checkedId);
                    String text = radioButton.getText().toString();
                    if(text.equals("ON")){
                        Log.d("checkd,","mo-do "+1);
                        mode.setTwice_mode(2,1);
                    }else{
                        mode.setTwice_mode(2,0);
                    }
                }
            }
        });

        //クイーン将棋のセット
        RadioGroup ChessGroup1 = (RadioGroup) findViewById(R.id.Chess_Group1);
        ChessGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    RadioButton radioButton = (RadioButton) findViewById(checkedId);
                    String text = radioButton.getText().toString();
                    if(text.equals("ON")){
                        mode.setChess_mode(1,1);
                    }else{
                        mode.setChess_mode(1,0);
                    }
                }
            }
        });
        RadioGroup ChessGroup2 = (RadioGroup) findViewById(R.id.Chess_Group2);
        ChessGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    RadioButton radioButton = (RadioButton) findViewById(checkedId);
                    String text = radioButton.getText().toString();
                    if(text.equals("ON")){
                        mode.setChess_mode(2,1);
                    }else{
                        mode.setChess_mode(2,0);
                    }
                }
            }
        });

        //安南将棋のセット
        RadioGroup BmotionGroup1 = (RadioGroup) findViewById(R.id.Bmotion_Group1);
        BmotionGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    RadioButton radioButton = (RadioButton) findViewById(checkedId);
                    String text = radioButton.getText().toString();
                    if(text.equals("ON")){
                        mode.setBmotion_mode(1,1);
                    }else{
                        mode.setBmotion_mode(1,0);
                    }
                }
            }
        });
        RadioGroup BmotionGroup2 = (RadioGroup) findViewById(R.id.Bmotion_Group2);
        BmotionGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    RadioButton radioButton = (RadioButton) findViewById(checkedId);
                    String text = radioButton.getText().toString();
                    if(text.equals("ON")){
                        mode.setBmotion_mode(2,1);
                    }else{
                        mode.setBmotion_mode(2,0);
                    }
                }
            }
        });
    }


    public void GameStart(View v){
        Intent intent = new Intent(getApplication(),Game.class);
        intent.putExtra("MODE", mode);
        startActivity(intent);
    }
}
