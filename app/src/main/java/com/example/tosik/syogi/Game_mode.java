package com.example.tosik.syogi;

import java.io.Serializable;

public class Game_mode implements Serializable {
    private int Simple_Mode;//0;通常将棋　1:王手将棋 2:持ち駒制限将棋 3:カオスモード
    //カオスモード時 ON/OFF
    private int[] Check_mode = new int[2];//王手将棋
    private int[] Restrict = new int[2]; //持ち駒制限 0:なし 1～:制約数
    private int[] Chess_mode = new int[2];
    private int[] Twice_mode = new int[2];//２手差し
    private int[] Bmotion_mode = new int[2];//安南将棋
    private String[] Handi = new String[2];
    /* 駒落ち
    0:なし 11:角落ち 12:飛車落ち 2:二枚落ち 4:四枚落ち 6:六枚落ち 8:八枚落ち 10:十枚落ち
     */


    public Game_mode(){
        Handi[0] = "なし";
        Handi[1] = "なし";
    }

    public void set_Mode(int i) {
                Simple_Mode = i;
    }
    public int getSimple_mode(){
        return Simple_Mode;
    }


    public void setCheck_mode(){
        Simple_Mode = 1;
        Check_mode[0] =1;
        Check_mode[1] = 1;
    }
    public void setCheck_mode(int Turn,int i){
        Check_mode[Turn-1]=i;
    }

    public int getCheck_mode(int Turn){
        return Check_mode[Turn-1];
    }

    public void setRestrict(int Restrict){
        this.Restrict[0] = Restrict;
        this.Restrict[1] = Restrict;
        Simple_Mode =2;
    }
    public void setRestrict(int Restrict ,int turn){
        this.Restrict[turn-1]= Restrict;
    }
    public int getRestrict(int turn){return Restrict[turn-1];}
    public String getRestrictText(int turn){
        return "持ち駒制限("+Restrict[turn-1]+")";
    }

    public void setHande(String Handi,int Turn){
        this.Handi[Turn-1] = Handi;
    }
    public String getHandi(int Turn){
        return this.Handi[Turn-1];
    }

    public void setChess_mode(){
        Simple_Mode = 4;
        Chess_mode[0] = 1;
        Chess_mode[1] = 1;
    }
    public void setChess_mode(int turn,int Chess_mode){this.Chess_mode[turn-1] = Chess_mode;}
    public int getChess_mode(int turn){ return Chess_mode[turn-1]; }

    public void setTwice_mode(){
        Simple_Mode = 5;
        Twice_mode[0]= 1;
        Twice_mode[1] = 1;
    }
    public void setTwice_mode(int turn,int Twice_mode){this.Twice_mode[turn-1] = Twice_mode;}
    public int getTwice_mode(int turn){return Twice_mode[turn-1];}

    public void setBmotion_mode(){
        Simple_Mode = 6;
        Bmotion_mode[0]= 1;
        Bmotion_mode[1] = 1;
    }
    public void setBmotion_mode(int turn,int Bmotion_mode){this.Bmotion_mode[turn-1] = Bmotion_mode;}
    public int getBmotion_mode(int turn){return Bmotion_mode[turn-1];}

    public String getMode_name(){
        switch(Simple_Mode){

            case 1:
                return "王手将棋";
            case 2:
                return "持ち駒制限("+Restrict[0]+")";
            case 3:
                return "カオス将棋";
            case 4:
                return "クイーン将棋";
            case 5:
                return "２手差し将棋";
            case 6:
                return "安南将棋";
            default:
                return "通常将棋";
        }

    }


}
