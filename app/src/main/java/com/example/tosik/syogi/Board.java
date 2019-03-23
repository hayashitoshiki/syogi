package com.example.tosik.syogi;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;

//将棋盤の情報についてのクラス
public class Board {

    public static final int COLS = 9;
    public static final int ROWS = 9;

    private int width;//将棋盤の横幅
    private int height;//将棋盤の高さ(横幅　= 高さ)
    private int top;
    private int left;

    private Cell cells[][] = new Cell[ROWS+2][COLS];
    private int Turn =1;
    static int promot;//成るかならないか判定 0:成らない 1:成る
    static int promot_end;//判定終了 0:判定中 1:判定終了

    private Activity mActivity;




    //将棋盤の生成
    public Board(Activity activity){
        mActivity = activity;
        for (int i = 0; i < ROWS+2; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j] = new Cell();
            }
        }

        //初期の配置をセット
             for(int i =0;i<9;i++){
            //歩
            cells[3][i].setStatus("Fu");
            cells[3][i].setEnemy(2);
            cells[7][i].setStatus("Fu");
            cells[7][i].setEnemy(1);
            if(i==4){
                //王
                cells[1][i].setStatus("OU");
                cells[1][i].setEnemy(2);
                cells[9][i].setStatus("GYOKU");
                cells[9][i].setEnemy(1);
            }
            if(i==3 || i==5){
                //金
                cells[1][i].setStatus("KIN");
                cells[1][i].setEnemy(2);
                cells[9][i].setStatus("KIN");
                cells[9][i].setEnemy(1);
            }else if(i==2 || i==6){
                //銀
                cells[1][i].setStatus("GIN");
                cells[1][i].setEnemy(2);
                cells[9][i].setStatus("GIN");
                cells[9][i].setEnemy(1);
            }else if(i==1 ){
                //佳
                cells[1][i].setStatus("KEI");
                cells[1][i].setEnemy(2);
                cells[9][i].setStatus("KEI");
                cells[9][i].setEnemy(1);
                cells[8][i].setStatus("KAKU");
                cells[8][i].setEnemy(1);
                cells[2][i].setStatus("HISYA");
                cells[2][i].setEnemy(2);
            }else if(i==7){
                //佳
                cells[1][i].setStatus("KEI");
                cells[1][i].setEnemy(2);
                cells[9][i].setStatus("KEI");
                cells[9][i].setEnemy(1);
                cells[8][i].setStatus("HISYA");
                cells[8][i].setEnemy(1);
                cells[2][i].setStatus("KAKU");
                cells[2][i].setEnemy(2);
            }else if(i==0 ||i==8){
                //香
                cells[1][i].setStatus("KYO");
                cells[1][i].setEnemy(2);
                cells[9][i].setStatus("KYO");
                cells[9][i].setEnemy(1);
            }
        }
        //駒台セット
        cells[10][8].setEnemy(1);
        cells[10][7].setStatus("KYO");
        cells[10][7].setEnemy(1);
        cells[10][6].setStatus("KEI");
        cells[10][6].setEnemy(1);
        cells[10][5].setStatus("GIN");
        cells[10][5].setEnemy(1);
        cells[10][4].setStatus("KIN");
        cells[10][4].setEnemy(1);
        cells[10][3].setStatus("KAKU");
        cells[10][3].setEnemy(1);
        cells[10][2].setStatus("HISYA");
        cells[10][2].setEnemy(1);


        cells[0][0].setEnemy(2);
        cells[0][1].setStatus("KYO");
        cells[0][1].setEnemy(2);
        cells[0][2].setStatus("KEI");
        cells[0][2].setEnemy(2);
        cells[0][3].setStatus("GIN");
        cells[0][3].setEnemy(2);
        cells[0][4].setStatus("KIN");
        cells[0][4].setEnemy(2);
        cells[0][5].setStatus("KAKU");
        cells[0][5].setEnemy(2);
        cells[0][6].setStatus("HISYA");
        cells[0][6].setEnemy(2);

    }

    public void setHandi(int Turn,String Hande){
        if(Turn==1){
            switch(Hande){
                case "十枚落ち":
                    cells[9][3].setStatus("None");
                    cells[9][3].setEnemy(0);
                    cells[9][5].setStatus("None");
                    cells[9][5].setEnemy(0);
                case "八枚落ち":
                    cells[9][2].setStatus("None");
                    cells[9][2].setEnemy(0);
                    cells[9][6].setStatus("None");
                    cells[9][6].setEnemy(0);
                case "六枚落ち":
                    cells[9][1].setStatus("None");
                    cells[9][1].setEnemy(0);
                    cells[9][7].setStatus("None");
                    cells[9][7].setEnemy(0);
                case "四枚落ち":
                    cells[9][0].setStatus("None");
                    cells[9][0].setEnemy(0);
                    cells[9][8].setStatus("None");
                    cells[9][8].setEnemy(0);
                case "二枚落ち":
                case "角落ち":
                    cells[8][1].setStatus("None");
                    cells[8][1].setEnemy(0);
                    if(Hande.equals("角落ち"))break;
                case "飛車落ち":
                    cells[8][7].setStatus("None");
                    cells[8][7].setEnemy(0);
                    break;
                default:
                    break;
            }
        }else{
            switch(Hande){
                case "十枚落ち":
                    cells[1][3].setStatus("None");
                    cells[1][3].setEnemy(0);
                    cells[1][5].setStatus("None");
                    cells[1][5].setEnemy(0);
                case "八枚落ち":
                    cells[1][2].setStatus("None");
                    cells[1][2].setEnemy(0);
                    cells[1][6].setStatus("None");
                    cells[1][6].setEnemy(0);
                case "六枚落ち":
                    cells[1][1].setStatus("None");
                    cells[1][1].setEnemy(0);
                    cells[1][7].setStatus("None");
                    cells[1][7].setEnemy(0);
                case "四枚落ち":
                    cells[1][0].setStatus("None");
                    cells[1][0].setEnemy(0);
                    cells[1][8].setStatus("None");
                    cells[1][8].setEnemy(0);
                case "二枚落ち":
                case "角落ち":
                    cells[2][7].setStatus("None");
                    cells[2][7].setEnemy(0);
                    if(Hande.equals("角落ち"))break;
                case "飛車落ち":
                    cells[2][1].setStatus("None");
                    cells[2][1].setEnemy(0);
                    break;
                default:
                    break;
            }
        }
    }

    public void setChess_pies(int turn){
        switch(turn){
            case 1:
                cells[9][7].setStatus("Knight");
                cells[9][7].setEnemy(1);
                cells[9][1].setStatus("Knight");
                cells[9][1].setEnemy(1);
                cells[8][7].setStatus("Quiin");
                cells[8][7].setEnemy(1);
                cells[8][1].setStatus("Quiin");
                cells[8][1].setEnemy(1);
                break;
            case 2:
                cells[1][7].setStatus("Knight");
                cells[1][7].setEnemy(2);
                cells[1][1].setStatus("Knight");
                cells[1][1].setEnemy(2);
                cells[2][7].setStatus("Quiin");
                cells[2][7].setEnemy(2);
                cells[2][1].setStatus("Quiin");
                cells[2][1].setEnemy(2);
                break;
        }
    }

    public void setBmotion_pies(int turn){
        switch(turn){
            case 1:
                cells[6][7].setStatus("Fu");
                cells[6][7].setEnemy(1);
                cells[6][1].setStatus("Fu");
                cells[6][1].setEnemy(1);
                cells[7][7].setStatus("None");
                cells[7][7].setEnemy(0);
                cells[7][1].setStatus("None");
                cells[7][1].setEnemy(0);
                break;
            case 2:
                cells[4][7].setStatus("Fu");
                cells[4][7].setEnemy(2);
                cells[4][1].setStatus("Fu");
                cells[4][1].setEnemy(2);
                cells[3][7].setStatus("None");
                cells[3][7].setEnemy(0);
                cells[3][1].setStatus("None");
                cells[3][1].setEnemy(0);
                break;

        }
    }

    //1マスのサイズセット
    public void setSize(int width, int height){
        int sz = width < height ? width : height;

        setWidth(sz);
        setHeight(sz);
    }

    public void setWidth(int width) {
        this.width = (int)(width / Board.COLS) * Board.COLS;			//列数で割り切れない場合は余りを捨てる。

        float cellW = this.getCellWidth();

        for (int i = 0; i < ROWS+2; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j].setWidth(cellW);
                cells[i][j].setLeft((int)(j * cellW));
            }
        }
    }
    public int getWidth() {
        return width;
    }
    public void setHeight(int height) {
        this.height = (int)(height / Board.ROWS) * Board.ROWS;		//行数で割り切れない場合は余りを捨てる。

        float cellH = this.getCellHeidht();

        for (int i = 0; i < ROWS+2; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j].setHeight(cellH);
                cells[i][j].setTop((int)(i * cellH));
            }
        }
    }
    public int getHeight() {
        return height;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getTop() {
        return top;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getLeft() {
        return left;
    }

    public Cell[][] getCells(){
        return cells;
    }

    public float getCellWidth(){
        return (float)(this.width / COLS);
    }

    public float getCellHeidht(){
        return (float)(this.height / ROWS);
    }

    static void setPromot(int promot2){
        promot = promot2;
        promot_end=1;
    }


    //ヒント表示
    public void getHinto(int x, int y,Cell.E_STATUS status,int turn){
        Cell cell =cells[x][y];
        Cell cell2;
        Log.d("ヒント表示","駒:"+status+"縦"+y+"横："+x);
        switch(turn) {
            case 1://先手
                if( x == 10 &cell.getCapture_switch() == 1) {//持ち駒のおける場所
                    for (int j = 0; j <= 8; j++) {//横マス
                        int k = 0;
                        if(status == Cell.E_STATUS.Fu ){//２歩がないか判断
                            for (int i = 1; i <= 9; i++) {
                                if(cells[i][j].getStatus().equals(Cell.E_STATUS.Fu) && (cells[i][j].getEnemy() == 1) ){
                                    //あったらスイッチング
                                    k =1;
                                    break;
                                }
                            }
                        }
                        for (int i = 1; i <= 9; i++) {
                            if (cells[i][j].getStatus().equals(Cell.E_STATUS.None)) {
                                if(k==1){//歩の場合
                                    break;
                                }
                                if(!((status == Cell.E_STATUS.Fu && i==1) || (status == Cell.E_STATUS.KYO && 1 == i) || (status == Cell.E_STATUS.KEI && i < 3))) {//歩or香or佳の場合
                                    cells[i][j].setHint(1);
                                }
                            }
                        }
                    }
                }else if(1 <= x && x <= 9){
                        switch(status) {
                            case RYU:
                            case UMA:
                            case GYOKU:
                            case Fu:
                            case TO:
                            case KIN:
                            case GIN:
                            case N_GIN:
                            case N_KEI:
                            case N_KYO:
                                if(1 < x){
                                    if((cell2 = cells[x-1][y]).getEnemy()!=turn) cell2.setHint(1);//前
                                    if(status.equals(Cell.E_STATUS.Fu))break;//歩なら終了
                                    if (0 < y  && (cell2 = cells[x - 1][y - 1]).getEnemy() != turn) cell2.setHint(1);//左前
                                    if (y < 8 && (cell2 = cells[x - 1][y + 1]).getEnemy() != turn) cell2.setHint(1);//右前
                                }
                                if(status.equals(Cell.E_STATUS.GIN) || status.equals(Cell.E_STATUS.GYOKU) || status.equals(Cell.E_STATUS.RYU)|| status.equals(Cell.E_STATUS.UMA)) {
                                    if (0 < y && x < 9 && (cell2 = cells[x + 1][y - 1]).getEnemy() != turn) cell2.setHint(1);//左後
                                    if (y < 8 && x < 9 && (cell2 = cells[x + 1][y + 1]).getEnemy() != turn) cell2.setHint(1);//右後
                                    if (status.equals(Cell.E_STATUS.GIN)) break;//銀なら終了
                                }
                                if ( x < 9 && (cell2 = cells[x + 1][y]).getEnemy() != turn) cell2.setHint(1);//後
                                if (0 < y  && (cell2 = cells[x ][y - 1]).getEnemy() != turn) cell2.setHint(1);//左
                                if (y < 8 && (cell2 = cells[x ][y + 1]).getEnemy() != turn) cell2.setHint(1);//右
                                if (!(status.equals(Cell.E_STATUS.UMA) || status.equals(Cell.E_STATUS.RYU))) break;//金or玉なら終了
                            case Quiin:
                            case HISYA:
                            case KYO:
                                if(status.equals(Cell.E_STATUS.HISYA) || status.equals(Cell.E_STATUS.KYO) || status.equals(Cell.E_STATUS.RYU) || status.equals(Cell.E_STATUS.Quiin)) {
                                    int i;
                                    int j;//スイッチ
                                    if (1 < x) {//上
                                        i = x - 1;
                                        j = 0;
                                        while (1 <= i) {
                                            if ((cell2 = cells[i][y]).getEnemy() != turn) cell2.setHint(1);
                                            if (cell2.getEnemy() != turn && cell2.getEnemy() != 0) j = 1;
                                            if (cell2.getEnemy() == turn || j == 1) break;
                                            i--;
                                        }
                                    }
                                    if (status.equals(Cell.E_STATUS.KYO)) break;//香車なら終了
                                    if (x < 9) {//下
                                        i = x + 1;
                                        j = 0;
                                        while (i <= 9) {
                                            if ((cell2 = cells[i][y]).getEnemy() != turn) cell2.setHint(1);
                                            if (cell2.getEnemy() != turn && cell2.getEnemy() != 0) j = 1;
                                            if (cell2.getEnemy() == turn || j == 1) break;
                                            i++;
                                        }
                                    }
                                    if (0 < y) {//左
                                        i = y - 1;
                                        j = 0;
                                        while (0 <= i) {
                                            if ((cell2 = cells[x][i]).getEnemy() != turn) cell2.setHint(1);
                                            if (cell2.getEnemy() != turn && cell2.getEnemy() != 0) j = 1;
                                            if (cell2.getEnemy() == turn || j == 1) break;
                                            i--;
                                        }
                                    }
                                    if (y < 8) {//右
                                        i = y + 1;
                                        j = 0;
                                        while (i <= 8) {
                                            if ((cell2 = cells[x][i]).getEnemy() != turn) cell2.setHint(1);
                                            if (cell2.getEnemy() != turn && cell2.getEnemy() != 0) j = 1;
                                            if (cell2.getEnemy() == turn || j == 1) break;
                                            i++;
                                        }
                                    }
                                    if(status.equals(Cell.E_STATUS.HISYA) || status.equals(Cell.E_STATUS.RYU))break;
                                }
                            case KAKU:
                                int x2;
                                int y2;
                                int j2;//スイッチ
                                if(1 < x && 0 < y){//左上
                                    x2 = x-1;
                                    y2 = y-1;
                                    j2 = 0;
                                    while(1 <= x2 && 0 <= y2){
                                        if((cell2 = cells[x2][y2]).getEnemy()!=turn)cell2.setHint(1);
                                        if(cell2.getEnemy()!=turn&& cell2.getEnemy()!=0) j2=1;
                                        if(cell2.getEnemy() == turn ||j2 ==1)break;
                                        x2--;
                                        y2--;
                                    }
                                }
                                if(1 < x && y < 8){//右上
                                    x2 = x-1;
                                    y2 = y+1;
                                    j2 = 0;
                                    while(1 <= x2 && y2 <= 8){
                                        if((cell2 = cells[x2][y2]).getEnemy()!=turn)cell2.setHint(1);
                                        if(cell2.getEnemy()!=turn&& cell2.getEnemy()!=0) j2=1;
                                        if(cell2.getEnemy() == turn ||j2 ==1)break;
                                        x2--;
                                        y2++;
                                    }
                                }
                                if(x < 9 && 0 < y){//左下
                                    x2 = x+1;
                                    y2 = y-1;
                                    j2 = 0;
                                    while(x2<= 9 && 0 <= y2){
                                        if((cell2 = cells[x2][y2]).getEnemy()!=turn)cell2.setHint(1);
                                        if(cell2.getEnemy()!=turn&& cell2.getEnemy()!=0) j2=1;
                                        if(cell2.getEnemy() == turn ||j2 ==1)break;
                                        x2++;
                                        y2--;
                                    }
                                }
                                if(x < 9 && y < 8){//右下
                                    x2 = x+1;
                                    y2 = y+1;
                                    j2 = 0;
                                    while(x2 <= 9 && y2 <= 8){
                                        if((cell2 = cells[x2][y2]).getEnemy()!=turn)cell2.setHint(1);
                                        if(cell2.getEnemy()!=turn&& cell2.getEnemy()!=0) j2=1;
                                        if(cell2.getEnemy() == turn ||j2 ==1)break;
                                        x2++;
                                        y2++;
                                    }
                                }
                                break;
                            case Knight:
                            case KEI:
                                if(2 < x){
                                    if(0 < y && (cell2 = cells[x-2][y-1]).getEnemy()!=turn)cell2.setHint(1);
                                    if(y < 8 && (cell2 = cells[x-2][y+1]).getEnemy()!=turn)cell2.setHint(1);
                                }
                                if(status.equals(Cell.E_STATUS.KEI))break;
                                if(x < 8){
                                    if(0 < y && (cell2 = cells[x+2][y-1]).getEnemy()!=turn)cell2.setHint(1);
                                    if(y < 8 && (cell2 = cells[x+2][y+1]).getEnemy()!=turn)cell2.setHint(1);
                                }
                                if(1 < y){
                                    if(1 < x && (cell2 = cells[x-1][y-2]).getEnemy()!=turn)cell2.setHint(1);
                                    if(x < 9 && (cell2 = cells[x+1][y-2]).getEnemy()!=turn)cell2.setHint(1);
                                }
                                if(y < 7){
                                    if(1 < x && (cell2 = cells[x-1][y+2]).getEnemy()!=turn)cell2.setHint(1);
                                    if(x < 9 && (cell2 = cells[x+1][y+2]).getEnemy()!=turn)cell2.setHint(1);
                                }
                                break;
                        }
                }
                break;

            case 2://後手
                if( x == 0 && cell.getCapture_switch() == 1) {//持ち駒のおける場所
                    for (int j = 0; j <= 8; j++) {//横マス
                        int k = 0;
                        if(status == Cell.E_STATUS.Fu ){//２歩がないか判断
                            for (int i = 1; i <= 9; i++) {
                                if(cells[i][j].getStatus().equals(Cell.E_STATUS.Fu) && (cells[i][j].getEnemy() == 2) ){
                                    //あったらスイッチング
                                    k =1;
                                    break;
                                }
                            }
                        }
                        for (int i = 1; i <= 9; i++) {
                            if (cells[i][j].getStatus().equals(Cell.E_STATUS.None)) {
                                if(k==1){//歩の場合
                                    break;
                                }
                                if(!((status==Cell.E_STATUS.Fu && i == 9 )|| (status == Cell.E_STATUS.KYO && i == 9) || (status == Cell.E_STATUS.KEI && 7 < i))) {//歩or香or佳の場合
                                    cells[i][j].setHint(1);
                                }
                            }
                        }
                    }
                } else if(1 <= x && x <= 9) {
                    switch (status) {
                        case OU:
                        case GYOKU:
                        case RYU:
                        case UMA:
                        case Fu:
                        case TO:
                        case KIN:
                        case GIN:
                        case N_GIN:
                        case N_KEI:
                        case N_KYO:
                            if (x < 9) {
                                if ((cell2 = cells[x + 1][y]).getEnemy() != turn)
                                    cell2.setHint(1);//前
                                if (status.equals(Cell.E_STATUS.Fu)) break;//歩なら終了
                                if (0 < y && (cell2 = cells[x + 1][y - 1]).getEnemy() != turn)
                                    cell2.setHint(1);//左前
                                if (y < 8 && (cell2 = cells[x + 1][y + 1]).getEnemy() != turn)
                                    cell2.setHint(1);//右前

                            }
                            if (status.equals(Cell.E_STATUS.GIN) || status.equals(Cell.E_STATUS.OU) || status.equals(Cell.E_STATUS.RYU) || status.equals(Cell.E_STATUS.UMA)) {
                                if (0 < y && 1 < x && (cell2 = cells[x - 1][y - 1]).getEnemy() != turn)
                                    cell2.setHint(1);//左後
                                if (y < 8 && 1 < x && (cell2 = cells[x - 1][y + 1]).getEnemy() != turn)
                                    cell2.setHint(1);//右後
                                if (status.equals(Cell.E_STATUS.GIN)) break;//銀なら終了
                            }
                            if (1 < x &&  (cell2 = cells[x - 1][y]).getEnemy() != turn)
                                cell2.setHint(1);//後
                            if (0 < y && (cell2 = cells[x][y - 1]).getEnemy() != turn)
                                cell2.setHint(1);//左
                            if (y < 8 && (cell2 = cells[x][y + 1]).getEnemy() != turn)
                                cell2.setHint(1);//右
                            if (!(status.equals(Cell.E_STATUS.UMA) || status.equals(Cell.E_STATUS.RYU)))
                                break;//金or玉なら終了
                        case Quiin:
                        case HISYA:
                        case KYO:
                            if (status.equals(Cell.E_STATUS.HISYA) || status.equals(Cell.E_STATUS.KYO) || status.equals(Cell.E_STATUS.RYU) || status.equals(Cell.E_STATUS.Quiin)) {
                                int i;
                                int j;//スイッチ
                                if (x < 9) {//下
                                    i = x + 1;
                                    j = 0;
                                    while (i <= 8) {
                                        if ((cell2 = cells[i][y]).getEnemy() != turn) cell2.setHint(1);
                                        if (cell2.getEnemy() != turn && cell2.getEnemy() != 0) j = 1;
                                        if (cell2.getEnemy() == turn || j == 1) break;
                                        i++;
                                    }
                                }
                                if (status.equals(Cell.E_STATUS.KYO)) break;//香車なら終了
                                if (1 < x) {//上
                                    i = x - 1;
                                    j = 0;
                                    while (1 <= i) {
                                        if ((cell2 = cells[i][y]).getEnemy() != turn) cell2.setHint(1);
                                        if (cell2.getEnemy() != turn && cell2.getEnemy() != 0) j = 1;
                                        if (cell2.getEnemy() == turn || j == 1) break;
                                        i--;
                                    }
                                }
                                if (0 < y) {//左
                                    i = y - 1;
                                    j = 0;
                                    while (0 <= i) {
                                        if ((cell2 = cells[x][i]).getEnemy() != turn) cell2.setHint(1);
                                        if (cell2.getEnemy() != turn && cell2.getEnemy() != 0) j = 1;
                                        if (cell2.getEnemy() == turn || j == 1) break;
                                        i--;
                                    }
                                }
                                if (y < 8) {//右
                                    i = y + 1;
                                    j = 0;
                                    while (i <= 8) {
                                        if ((cell2 = cells[x][i]).getEnemy() != turn) cell2.setHint(1);
                                        if (cell2.getEnemy() != turn && cell2.getEnemy() != 0) j = 1;
                                        if (cell2.getEnemy() == turn || j == 1) break;
                                        i++;
                                    }
                                }
                                if (status.equals(Cell.E_STATUS.HISYA) || status.equals(Cell.E_STATUS.RYU)) break;
                            }
                        case KAKU:
                            int x2;
                            int y2;
                            int j2;//スイッチ
                            if (1 < x && 0 < y) {//左上
                                x2 = x - 1;
                                y2 = y - 1;
                                j2 = 0;
                                while (1 <= x2 && 0 <= y2) {
                                    if ((cell2 = cells[x2][y2]).getEnemy() != turn) cell2.setHint(1);
                                    if (cell2.getEnemy() != turn && cell2.getEnemy() != 0) j2 = 1;
                                    if (cell2.getEnemy() == turn || j2 == 1) break;
                                    x2--;
                                    y2--;
                                }
                            }
                            if (1 < x && y < 8) {//右上
                                x2 = x - 1;
                                y2 = y + 1;
                                j2 = 0;
                                while (1 <= x2 && y2 <= 8) {
                                    if ((cell2 = cells[x2][y2]).getEnemy() != turn) cell2.setHint(1);
                                    if (cell2.getEnemy() != turn && cell2.getEnemy() != 0) j2 = 1;
                                    if (cell2.getEnemy() == turn || j2 == 1) break;
                                    x2--;
                                    y2++;
                                }
                            }
                            if (x < 9 && 0 < y) {//左下
                                x2 = x + 1;
                                y2 = y - 1;
                                j2 = 0;
                                while (x2 <= 9 && 0 <= y2) {
                                    if ((cell2 = cells[x2][y2]).getEnemy() != turn) cell2.setHint(1);
                                    if (cell2.getEnemy() != turn && cell2.getEnemy() != 0) j2 = 1;
                                    if (cell2.getEnemy() == turn || j2 == 1) break;
                                    x2++;
                                    y2--;
                                }
                            }
                            if (x < 9 && y < 8) {//右下
                                x2 = x + 1;
                                y2 = y + 1;
                                j2 = 0;
                                while (x2 <= 9 && y2 <= 8) {
                                    if ((cell2 = cells[x2][y2]).getEnemy() != turn) cell2.setHint(1);
                                    if (cell2.getEnemy() != turn && cell2.getEnemy() != 0) j2 = 1;
                                    if (cell2.getEnemy() == turn || j2 == 1) break;
                                    x2++;
                                    y2++;
                                }
                            }
                            break;
                        case Knight:
                        case KEI:
                            if (x < 8) {
                                if (0 < y && (cell2 = cells[x + 2][y - 1]).getEnemy() != turn) cell2.setHint(1);
                                if (y < 8 && (cell2 = cells[x + 2][y + 1]).getEnemy() != turn) cell2.setHint(1);
                            }
                            if(status.equals(Cell.E_STATUS.KEI))break;
                            if(2 < x){
                                if(0 < y && (cell2 = cells[x-2][y-1]).getEnemy()!=turn)cell2.setHint(1);
                                if(y < 8 && (cell2 = cells[x-2][y+1]).getEnemy()!=turn)cell2.setHint(1);
                            }
                            if(1 < y){
                                if(1 < x && (cell2 = cells[x-1][y-2]).getEnemy()!=turn)cell2.setHint(1);
                                if(x < 9 && (cell2 = cells[x+1][y-2]).getEnemy()!=turn)cell2.setHint(1);
                            }
                            if(y < 7){
                                if(1 < x && (cell2 = cells[x-1][y+2]).getEnemy()!=turn)cell2.setHint(1);
                                if(x < 9 && (cell2 = cells[x+1][y+2]).getEnemy()!=turn)cell2.setHint(1);
                            }
                            break;
                    }
                }

        }
    }

    //ヒント表示
    public void getHinto2(int x, int y,Cell.E_STATUS status,int turn){
        Cell cell =cells[x][y];
        Cell cell2;
        Log.d("ヒント表示","駒:"+status+"縦"+y+"横："+x);
        switch(turn) {
            case 1://先手
                if( x == 10 &cell.getCapture_switch() == 1) {//持ち駒のおける場所
                    for (int j = 0; j <= 8; j++) {//横マス
                        int k = 0;
                        if(status == Cell.E_STATUS.Fu ){//２歩がないか判断
                            for (int i = 1; i <= 9; i++) {
                                if(cells[i][j].getStatus().equals(Cell.E_STATUS.Fu) && (cells[i][j].getEnemy() == 1) ){
                                    //あったらスイッチング
                                    k =1;
                                    break;
                                }
                            }
                        }
                        for (int i = 1; i <= 9; i++) {
                            if (cells[i][j].getStatus().equals(Cell.E_STATUS.None)) {
                                if(k==1){//歩の場合
                                    break;
                                }
                                if(!((status == Cell.E_STATUS.Fu && i==1) || (status == Cell.E_STATUS.KYO && 1 == i) || (status == Cell.E_STATUS.KEI && i < 3))) {//歩or香or佳の場合
                                    cells[i][j].setHint(1);
                                }
                            }
                        }
                    }
                }else if(1 <= x && x <= 9){
                    switch(status) {
                        case RYU:
                        case UMA:
                        case GYOKU:
                        case Fu:
                        case TO:
                        case KIN:
                        case GIN:
                        case N_GIN:
                        case N_KEI:
                        case N_KYO:
                            if(1 < x){
                                if((cell2 = cells[x-1][y]).getEnemy()==0) cell2.setHint(1);//前
                                if(status.equals(Cell.E_STATUS.Fu))break;//歩なら終了
                                if (0 < y  && (cell2 = cells[x - 1][y - 1]).getEnemy() ==0) cell2.setHint(1);//左前
                                if (y < 8 && (cell2 = cells[x - 1][y + 1]).getEnemy() ==0) cell2.setHint(1);//右前
                            }
                            if(status.equals(Cell.E_STATUS.GIN) || status.equals(Cell.E_STATUS.GYOKU) || status.equals(Cell.E_STATUS.RYU)|| status.equals(Cell.E_STATUS.UMA)) {
                                if (0 < y && x < 9 && (cell2 = cells[x + 1][y - 1]).getEnemy() ==0) cell2.setHint(1);//左後
                                if (y < 8 && x < 9 && (cell2 = cells[x + 1][y + 1]).getEnemy() ==0) cell2.setHint(1);//右後
                                if (status.equals(Cell.E_STATUS.GIN)) break;//銀なら終了
                            }
                            if ( x < 9 && (cell2 = cells[x + 1][y]).getEnemy() ==0) cell2.setHint(1);//後
                            if (0 < y  && (cell2 = cells[x ][y - 1]).getEnemy() ==0) cell2.setHint(1);//左
                            if (y < 8 && (cell2 = cells[x ][y + 1]).getEnemy() ==0) cell2.setHint(1);//右
                            if (!(status.equals(Cell.E_STATUS.UMA) || status.equals(Cell.E_STATUS.RYU))) break;//金or玉なら終了
                        case Quiin:
                        case HISYA:
                        case KYO:
                            if(status.equals(Cell.E_STATUS.HISYA) || status.equals(Cell.E_STATUS.KYO) || status.equals(Cell.E_STATUS.RYU) || status.equals(Cell.E_STATUS.Quiin)) {
                                int i;
                                int j;//スイッチ
                                if (1 < x) {//上
                                    i = x - 1;
                                    j = 0;
                                    while (1 <= i) {
                                        if ((cell2 = cells[i][y]).getEnemy() ==0) cell2.setHint(1);
                                        if (cell2.getEnemy() != 0) break;
                                        i--;
                                    }
                                }
                                if (status.equals(Cell.E_STATUS.KYO)) break;//香車なら終了
                                if (x < 9) {//下
                                    i = x + 1;
                                    j = 0;
                                    while (i <= 9) {
                                        if ((cell2 = cells[i][y]).getEnemy() ==0) cell2.setHint(1);
                                        if (cell2.getEnemy() != 0) break;
                                        i++;
                                    }
                                }
                                if (0 < y) {//左
                                    i = y - 1;
                                    j = 0;
                                    while (0 <= i) {
                                        if ((cell2 = cells[x][i]).getEnemy() ==0) cell2.setHint(1);
                                        if (cell2.getEnemy() != 0 ) break;
                                        i--;
                                    }
                                }
                                if (y < 8) {//右
                                    i = y + 1;
                                    j = 0;
                                    while (i <= 8) {
                                        if ((cell2 = cells[x][i]).getEnemy() ==0) cell2.setHint(1);
                                        if (cell2.getEnemy() != 0) break;
                                        i++;
                                    }
                                }
                                if(status.equals(Cell.E_STATUS.HISYA) || status.equals(Cell.E_STATUS.RYU))break;
                            }
                        case KAKU:
                            int x2;
                            int y2;
                            int j2;//スイッチ
                            if(1 < x && 0 < y){//左上
                                x2 = x-1;
                                y2 = y-1;
                                j2 = 0;
                                while(1 <= x2 && 0 <= y2){
                                    if((cell2 = cells[x2][y2]).getEnemy() ==0)cell2.setHint(1);
                                    if(cell2.getEnemy() != 0 )break;
                                    x2--;
                                    y2--;
                                }
                            }
                            if(1 < x && y < 8){//右上
                                x2 = x-1;
                                y2 = y+1;
                                j2 = 0;
                                while(1 <= x2 && y2 <= 8){
                                    if((cell2 = cells[x2][y2]).getEnemy() ==0)cell2.setHint(1);
                                    if(cell2.getEnemy() != 0)break;
                                    x2--;
                                    y2++;
                                }
                            }
                            if(x < 9 && 0 < y){//左下
                                x2 = x+1;
                                y2 = y-1;
                                j2 = 0;
                                while(x2<= 9 && 0 <= y2){
                                    if((cell2 = cells[x2][y2]).getEnemy() ==0)cell2.setHint(1);
                                    if(cell2.getEnemy() != 0)break;
                                    x2++;
                                    y2--;
                                }
                            }
                            if(x < 9 && y < 8){//右下
                                x2 = x+1;
                                y2 = y+1;
                                j2 = 0;
                                while(x2 <= 9 && y2 <= 8){
                                    if((cell2 = cells[x2][y2]).getEnemy() ==0)cell2.setHint(1);
                                    if(cell2.getEnemy() != 0)break;
                                    x2++;
                                    y2++;
                                }
                            }
                            break;
                        case Knight:
                        case KEI:
                            if(2 < x){
                                if(0 < y && (cell2 = cells[x-2][y-1]).getEnemy() ==0)cell2.setHint(1);
                                if(y < 8 && (cell2 = cells[x-2][y+1]).getEnemy() ==0)cell2.setHint(1);
                            }
                            if(status.equals(Cell.E_STATUS.KEI))break;
                            if(x < 8){
                                if(0 < y && (cell2 = cells[x+2][y-1]).getEnemy() ==0)cell2.setHint(1);
                                if(y < 8 && (cell2 = cells[x+2][y+1]).getEnemy() ==0)cell2.setHint(1);
                            }
                            if(1 < y){
                                if(1 < x && (cell2 = cells[x-1][y-2]).getEnemy() ==0)cell2.setHint(1);
                                if(x < 9 && (cell2 = cells[x+1][y-2]).getEnemy() ==0)cell2.setHint(1);
                            }
                            if(y < 7){
                                if(1 < x && (cell2 = cells[x-1][y+2]).getEnemy() ==0)cell2.setHint(1);
                                if(x < 9 && (cell2 = cells[x+1][y+2]).getEnemy() ==0)cell2.setHint(1);
                            }
                            break;
                    }
                }
                break;

            case 2://後手
                if( x == 0 && cell.getCapture_switch() == 1) {//持ち駒のおける場所
                    for (int j = 0; j <= 8; j++) {//横マス
                        int k = 0;
                        if(status == Cell.E_STATUS.Fu ){//２歩がないか判断
                            for (int i = 1; i <= 9; i++) {
                                if(cells[i][j].getStatus().equals(Cell.E_STATUS.Fu) && (cells[i][j].getEnemy() == 2) ){
                                    //あったらスイッチング
                                    k =1;
                                    break;
                                }
                            }
                        }
                        for (int i = 1; i <= 9; i++) {
                            if (cells[i][j].getStatus().equals(Cell.E_STATUS.None)) {
                                if(k==1){//歩の場合
                                    break;
                                }
                                if(!((status==Cell.E_STATUS.Fu && i == 9 )|| (status == Cell.E_STATUS.KYO && i == 9) || (status == Cell.E_STATUS.KEI && 7 < i))) {//歩or香or佳の場合
                                    cells[i][j].setHint(1);
                                }
                            }
                        }
                    }
                } else if(1 <= x && x <= 9) {
                    switch (status) {
                        case OU:
                        case GYOKU:
                        case RYU:
                        case UMA:
                        case Fu:
                        case TO:
                        case KIN:
                        case GIN:
                        case N_GIN:
                        case N_KEI:
                        case N_KYO:
                            if (x < 9) {
                                if ((cell2 = cells[x + 1][y]).getEnemy() ==0)
                                    cell2.setHint(1);//前
                                if (status.equals(Cell.E_STATUS.Fu)) break;//歩なら終了
                                if (0 < y && (cell2 = cells[x + 1][y - 1]).getEnemy() ==0)
                                    cell2.setHint(1);//左前
                                if (y < 8 && (cell2 = cells[x + 1][y + 1]).getEnemy() ==0)
                                    cell2.setHint(1);//右前

                            }
                            if (status.equals(Cell.E_STATUS.GIN) || status.equals(Cell.E_STATUS.OU) || status.equals(Cell.E_STATUS.RYU) || status.equals(Cell.E_STATUS.UMA)) {
                                if (0 < y && 1 < x && (cell2 = cells[x - 1][y - 1]).getEnemy() ==0)
                                    cell2.setHint(1);//左後
                                if (y < 8 && 1 < x && (cell2 = cells[x - 1][y + 1]).getEnemy() ==0)
                                    cell2.setHint(1);//右後
                                if (status.equals(Cell.E_STATUS.GIN)) break;//銀なら終了
                            }
                            if (1 < x &&  (cell2 = cells[x - 1][y]).getEnemy() ==0)
                                cell2.setHint(1);//後
                            if (0 < y && (cell2 = cells[x][y - 1]).getEnemy() ==0)
                                cell2.setHint(1);//左
                            if (y < 8 && (cell2 = cells[x][y + 1]).getEnemy() ==0)
                                cell2.setHint(1);//右
                            if (!(status.equals(Cell.E_STATUS.UMA) || status.equals(Cell.E_STATUS.RYU)))
                                break;//金or玉なら終了
                        case Quiin:
                        case HISYA:
                        case KYO:
                            if (status.equals(Cell.E_STATUS.HISYA) || status.equals(Cell.E_STATUS.KYO) || status.equals(Cell.E_STATUS.RYU) || status.equals(Cell.E_STATUS.Quiin)) {
                                int i;
                                int j;//スイッチ
                                if (x < 9) {//下
                                    i = x + 1;
                                    j = 0;
                                    while (i <= 8) {
                                        if ((cell2 = cells[i][y]).getEnemy() ==0) cell2.setHint(1);
                                        if (cell2.getEnemy() ==0 && cell2.getEnemy() != 0) j = 1;
                                        if (cell2.getEnemy() != 0 || j == 1) break;
                                        i++;
                                    }
                                }
                                if (status.equals(Cell.E_STATUS.KYO)) break;//香車なら終了
                                if (1 < x) {//上
                                    i = x - 1;
                                    j = 0;
                                    while (1 <= i) {
                                        if ((cell2 = cells[i][y]).getEnemy() ==0) cell2.setHint(1);
                                        if (cell2.getEnemy() ==0 && cell2.getEnemy() != 0) j = 1;
                                        if (cell2.getEnemy() != 0 || j == 1) break;
                                        i--;
                                    }
                                }
                                if (0 < y) {//左
                                    i = y - 1;
                                    j = 0;
                                    while (0 <= i) {
                                        if ((cell2 = cells[x][i]).getEnemy() ==0) cell2.setHint(1);
                                        if (cell2.getEnemy() ==0 && cell2.getEnemy() != 0) j = 1;
                                        if (cell2.getEnemy() != 0 || j == 1) break;
                                        i--;
                                    }
                                }
                                if (y < 8) {//右
                                    i = y + 1;
                                    j = 0;
                                    while (i <= 8) {
                                        if ((cell2 = cells[x][i]).getEnemy() ==0) cell2.setHint(1);
                                        if (cell2.getEnemy() ==0 && cell2.getEnemy() != 0) j = 1;
                                        if (cell2.getEnemy() != 0 || j == 1) break;
                                        i++;
                                    }
                                }
                                if (status.equals(Cell.E_STATUS.HISYA) || status.equals(Cell.E_STATUS.RYU)) break;
                            }
                        case KAKU:
                            int x2;
                            int y2;
                            int j2;//スイッチ
                            if (1 < x && 0 < y) {//左上
                                x2 = x - 1;
                                y2 = y - 1;
                                j2 = 0;
                                while (1 <= x2 && 0 <= y2) {
                                    if ((cell2 = cells[x2][y2]).getEnemy() ==0) cell2.setHint(1);
                                    if (cell2.getEnemy() ==0 && cell2.getEnemy() != 0) j2 = 1;
                                    if (cell2.getEnemy() != 0 || j2 == 1) break;
                                    x2--;
                                    y2--;
                                }
                            }
                            if (1 < x && y < 8) {//右上
                                x2 = x - 1;
                                y2 = y + 1;
                                j2 = 0;
                                while (1 <= x2 && y2 <= 8) {
                                    if ((cell2 = cells[x2][y2]).getEnemy() ==0) cell2.setHint(1);
                                    if (cell2.getEnemy() ==0 && cell2.getEnemy() != 0) j2 = 1;
                                    if (cell2.getEnemy() != 0 || j2 == 1) break;
                                    x2--;
                                    y2++;
                                }
                            }
                            if (x < 9 && 0 < y) {//左下
                                x2 = x + 1;
                                y2 = y - 1;
                                j2 = 0;
                                while (x2 <= 9 && 0 <= y2) {
                                    if ((cell2 = cells[x2][y2]).getEnemy() ==0) cell2.setHint(1);
                                    if (cell2.getEnemy() ==0 && cell2.getEnemy() != 0) j2 = 1;
                                    if (cell2.getEnemy() != 0 || j2 == 1) break;
                                    x2++;
                                    y2--;
                                }
                            }
                            if (x < 9 && y < 8) {//右下
                                x2 = x + 1;
                                y2 = y + 1;
                                j2 = 0;
                                while (x2 <= 9 && y2 <= 8) {
                                    if ((cell2 = cells[x2][y2]).getEnemy() ==0) cell2.setHint(1);
                                    if (cell2.getEnemy() ==0 && cell2.getEnemy() != 0) j2 = 1;
                                    if (cell2.getEnemy() != 0 || j2 == 1) break;
                                    x2++;
                                    y2++;
                                }
                            }
                            break;
                        case Knight:
                        case KEI:
                            if (x < 8) {
                                if (0 < y && (cell2 = cells[x + 2][y - 1]).getEnemy() ==0) cell2.setHint(1);
                                if (y < 8 && (cell2 = cells[x + 2][y + 1]).getEnemy() ==0) cell2.setHint(1);
                            }
                            if(status.equals(Cell.E_STATUS.KEI))break;
                            if(2 < x){
                                if(0 < y && (cell2 = cells[x-2][y-1]).getEnemy() ==0)cell2.setHint(1);
                                if(y < 8 && (cell2 = cells[x-2][y+1]).getEnemy() ==0)cell2.setHint(1);
                            }
                            if(1 < y){
                                if(1 < x && (cell2 = cells[x-1][y-2]).getEnemy() ==0)cell2.setHint(1);
                                if(x < 9 && (cell2 = cells[x+1][y-2]).getEnemy() ==0)cell2.setHint(1);
                            }
                            if(y < 7){
                                if(1 < x && (cell2 = cells[x-1][y+2]).getEnemy() ==0)cell2.setHint(1);
                                if(x < 9 && (cell2 = cells[x+1][y+2]).getEnemy() ==0)cell2.setHint(1);
                            }
                            break;
                    }
                }

        }
    }
}
