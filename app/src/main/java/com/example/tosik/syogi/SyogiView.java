package com.example.tosik.syogi;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;



public class SyogiView extends View {

    private Board mBoard ;
    private Activity mActivity;
    private Game_mode mode;
    private Game game;
    private  Cell[][] cells;

    private float mas;//ひとまず分の大きさメモ
    private String prev;//触った駒の記憶
    private int prev_x,prev_y;
    private int turn =1; //現在の手番　1:先手 2:後手
    private int[] ou_pos = new int [2];
    private int[] gyoku_pos = new int[2];
    private int[] check_pos = new int[2];//王手している駒の座標
    private int[] through_enemy[] = new int[8][3];
    private int  Twice_count =0;



    private String enemy_name[][] = {
            {"Fu","TO"},
            {"KYO","N_KYO"},
            {"KEI","N_KEI"},
            {"GIN","N_GIN"},
            {"Quiin","SISI"}
    } ;

    private int[] direction = new int[2];//飛車角王手時の座標


    public SyogiView(Context context,Activity activity){
        super(context);
        setFocusable(true);
        mActivity = activity;
        Intent intent = mActivity.getIntent();
        mode = (Game_mode)intent.getSerializableExtra("MODE");
        game = (Game)this.getContext();
        game.setGamemode(mode);
        //盤上の駒生成
        mBoard = new Board(activity);
        cells = mBoard.getCells();
       if(mode.getChess_mode(1)==1) mBoard.setChess_pies(1);
       if(mode.getChess_mode(2)==1)mBoard.setChess_pies(2);
        mBoard.setHandi(1,mode.getHandi(1));
        mBoard.setHandi(2,mode.getHandi(2));
        if(mode.getBmotion_mode(1)==1) mBoard.setBmotion_pies(1);
        if(mode.getBmotion_mode(2)==1)mBoard.setBmotion_pies(2);
        ou_pos[0] = 1;
        ou_pos[1] = 4;
        gyoku_pos[0] = 9;
        gyoku_pos[1] = 4;
        check_pos[0] = 0;
        check_pos[1] = 0;
    }

    //OnCreat
    @Override
    protected void onDraw(Canvas canvas) {
        mas = getWidth()/9;
    super.onDraw(canvas);

        mBoard.setSize(getWidth(), getHeight());
        Paint paint = new Paint();
        paint.setTextSize(mas);
        canvas.save();
        float text_Width = paint.measureText(mode.getMode_name());
        canvas.rotate(180f,getWidth()/2, mas*2);
        canvas.drawText(mode.getMode_name(), getWidth()/2-text_Width/2, mas*5/2, paint);
        canvas.restore();
        canvas.drawText(mode.getMode_name(), getWidth()/2-text_Width/2, mas*15, paint);
        if(mode.getSimple_mode()==3)game.Chaos_sub();

        canvas.translate(0, mas*3);
        drawBoard(canvas);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    //指した時の動作
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();


        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int r = (int)((y-mas*3) / mBoard.getCellHeidht());
                int c = (int)(x / mBoard.getCellWidth());
                Cell[][] cells = mBoard.getCells();
                if(0<=r && r<=10 &&0<=c && c<=8) {
                    Cell cell = cells[r][c];
                    if (r < Board.ROWS+2 && c < Board.COLS) {
                        if (cell.getHint() == 2) {
                            //動かす(ヒントをタッチ)
                            PutEnemy(prev, turn, r, c, prev_x,prev_y);
                            if(mode.getTwice_mode(turn)==1){//もし２手差し将棋なら
                                switch(Twice_count){
                                    case 2:
                                        changeTurn(turn);
                                        Twice_count = 0;
                                        break;

                                    default:

                                }
                            }else {
                                changeTurn(turn);
                            }

                        } else if (turn == cell.getEnemy() || cell.getCapture_switch()==1) {//駒の動き表示
                            Log.d("sisicheck","駒："+cell.getName());
                            Cell.E_STATUS Estatus = cell.getStatus();

                            if(mode.getBmotion_mode(turn)==1){//もし安南将棋なら後ろの駒の名前をセット
                                if(turn==1 && r+1<=9 && !(cells[r+1][c].name().equals("None"))){//先手で後ろに駒があれば
                                    Estatus = cells[r+1][c].getStatus();
                                }else if(turn==2 && 1<=r-1 && !(cells[r-1][c].name().equals("None"))){//後手で後ろに駒があれば
                                    Estatus = cells[r-1][c].getStatus();
                                }
                            }

                            if(mode.getTwice_mode(turn)==1 && Twice_count==1){//もし２手差し将棋でカウントが1ならば
                                if(cell.name().equals("SISI")){//もし獅子なら
                                    Log.d("sisicheck","獅子");
                                    for(int i=r-1;i<=r+1;i++){
                                        for(int j = c-1;j<=c+1;j++){
                                            if(i==r && j == c){//獅子の位置
                                                continue;
                                            }
                                            if(0<i && i<10 && 0<=j && j<=8 && cells[i][j].getEnemy() != turn){
                                                //いけたら王様の動きでヒントチェック
                                                Log.d("sisicheck","座標"+(9-j)+","+i);
                                                cells[i][j].setHint(1);
                                                if(turn==1) mBoard.getHinto(i, j, Cell.E_STATUS.GYOKU, turn);
                                                if(turn==2) mBoard.getHinto(i, j, Cell.E_STATUS.OU, turn);
                                            }
                                        }
                                    }
                                }else {
                                    mBoard.getHinto2(r, c, Estatus, turn);
                                }
                            }else if(cell.name().equals("SISI")){//もし獅子なら
                                Log.d("sisicheck","獅子");
                                for(int i=r-1;i<=r+1;i++){
                                    for(int j = c-1;j<=c+1;j++){
                                        if(i==r && j == c){//獅子の位置
                                            continue;
                                        }
                                        if(0<i && i<10 && 0<=j && j<=8 && cells[i][j].getEnemy() != turn){
                                            //いけたら王様の動きでヒントチェック
                                            Log.d("sisicheck","座標"+(9-j)+","+i);
                                            cells[i][j].setHint(1);
                                            if(turn==1) mBoard.getHinto(i, j, Cell.E_STATUS.GYOKU, turn);
                                            if(turn==2) mBoard.getHinto(i, j, Cell.E_STATUS.OU, turn);
                                        }
                                    }
                                }
                            }else { //普通の場合
                                //もし安南将棋なら１つ後ろに駒があるか→あったらその動きを送る
                                //駒の動き表示
                                mBoard.getHinto(r, c, Estatus, turn);
                            }
                            prev = cells[r][c].name();
                            prev_x = r;
                            prev_y = c;
                        }

                    }
                }
                invalidate();            //画面を再描画 もう一度盤面生成読み込み
                break;
            default:
                return true;

        }
        return true;
    }


    //将棋盤生成
    private void drawBoard(Canvas canvas){
        int bw = mBoard.getWidth();//将棋盤の幅
        int bh = mBoard.getHeight();//将棋盤の高さ
        float cw = mBoard.getCellWidth();//１マスの幅
        float ch = mBoard.getCellHeidht();//１マスの高さ

        if (mBoard.getWidth() <=0 ) return;

        Paint paint = new Paint();					//本当はここでnewするのはパフォーマンス上良くない。後で直そう。
        //盤面セット
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.free_grain_sub);
        Rect rect1 = new Rect(0, (int)ch,bw,(int)bh+(int)cw);
        Rect rect2 = new Rect(0, (int)ch,bw,(int)bh+(int)cw);
        canvas.drawBitmap(bmp, rect1, rect2, paint);

        //駒台セット
        paint.setColor(Color.rgb(251, 171, 83));
        canvas.drawRect(cw*2, bw+cw, bw, bh+cw*2, paint);
        paint.setColor(Color.rgb(251, 171, 83));
        canvas.drawRect(0, 0, bw-cw*2, cw, paint);

        paint.setColor(Color.rgb(40, 40, 40));		//罫線の色
        paint.setStrokeWidth(2);
        //縦線
        for (int i = 0; i < Board.COLS; i++) {
            canvas.drawLine(cw * (i+1), cw, cw * (i+1), bh+cw, paint);
        }
        //横線
        for (int i = 0; i < Board.ROWS; i++) {
            canvas.drawLine(0, ch * (i+1), bw, ch * (i+1), paint);
        }

        Cell[][] cells = mBoard.getCells();
        for (int i = 0; i < Board.ROWS+2; i++) {
            for (int j = 0; j < Board.COLS; j++) {
                Cell cell =cells[i][j];
                Cell.E_STATUS st = cell.getStatus();

                //駒のセット
                if (i!= 0 && i != 10 && st != Cell.E_STATUS.None ){
                    //駒表示
                    if(cell.getEnemy()==2) {
                        canvas.save();
                        canvas.rotate(180f, cell.getCx(), cell.getCy());
                        paint.setTextSize(cw/2);
                        canvas.drawText(cell.getName(), cell.getCx() - cw/4, cell.getCy() + cw/4, paint);
                        canvas.restore();
                    }else{
                        paint.setTextSize(cw/2);
                        canvas.drawText(cell.getName(), cell.getCx()-cw/4, cell.getCy()+cw/5, paint);
                    }
                }
                //持ち駒セット
                switch(i){
                    case 0: //後手持ち駒
                        if(j==8){
                            canvas.save();
                            canvas.rotate(180f, cell.getCx(), cell.getCy());
                            paint.setTextSize(cw*2/3);
                            canvas.drawText("持ち駒", cell.getCx() - cw/2, cell.getCy() + cw/4, paint);
                            canvas.restore();
                        }
                        if( cells[0][j].getCapture_switch() == 1){
                            canvas.save();
                            canvas.rotate(180f, cell.getCx(), cell.getCy());
                            paint.setTextSize(cw/2);
                            canvas.drawText(cell.getName(), cell.getCx() - cw/4, cell.getCy() + cw/4, paint);
                            paint.setTextSize(cw/5);
                            canvas.drawText(String.valueOf(cell.getCapture_count()), cell.getCx()+cell.getWidth()/3 , cell.getCy()+cell.getHeight()/3 , paint);
                            canvas.restore();
                        }
                        break;
                    case 10://先手持ち駒
                        if(j==0){
                            paint.setTextSize(cw*2/3);
                            canvas.drawText("持ち駒", cell.getCx() - cw/2, cell.getCy() + cw/4, paint);
                        }
                        if( cells[10][j].getCapture_switch() == 1){
                            paint.setTextSize(cw/2);
                            canvas.drawText(cell.getName(), cell.getCx() - cw/4, cell.getCy() + cw/4, paint);
                            paint.setTextSize(cw/5);
                            canvas.drawText(String.valueOf(cell.getCapture_count()), cell.getCx()+cell.getWidth()/3 , cell.getCy()+cell.getHeight()/3 , paint);
                        }
                        break;
                }

                if(cell.getHint()==1){
                    //ヒント表示
                    paint.setColor(Color.argb(200,255,255,0));
                    canvas.drawCircle(cell.getCx(), cell.getCy(), (float) (cw * 0.46), paint);
                    cell.setHint(2);
                    paint.setColor(Color.BLACK);
                }else if(cell.getHint()==2){
                    cell.setHint(0);
                }
            }
        }

    }

    //手番チェンジ
    public void changeTurn(int Turn){
        if (Turn == 1) {
            turn = 2;
        }else{
            turn = 1;
        }
    }

    //駒打ち
    public void PutEnemy(String Enemy,int Turn,int y,int x,int y_prev,int x_prev){
        Cell cell = cells[y][x];
        Cell cell_prev = cells[y_prev][x_prev];
        Log.d("打つ","駒:"+Enemy+"縦"+y+"横："+x);
        cell.setHint(0);
        switch(Turn){
            case 1://先手番
                if(cell.getEnemy() == 2) {
                    switch (cell.getStatus()) {//駒を取る
                        case TO:
                        case Fu:
                            cells[10][8].setCapture_switch(1);
                            cells[10][8].addCapture_count(1);
                            cells[10][8].setStatus("Fu");
                            break;
                        case KYO:
                        case N_KYO:
                            cells[10][7].setCapture_switch(1);
                            cells[10][7].addCapture_count(1);
                            cells[10][7].setStatus("KYO");
                            break;
                        case KEI:
                        case N_KEI:
                            cells[10][6].setCapture_switch(1);
                            cells[10][6].addCapture_count(1);
                            cells[10][6].setStatus("KEI");
                            break;
                        case GIN:
                        case N_GIN:
                            cells[10][5].setCapture_switch(1);
                            cells[10][5].addCapture_count(1);
                            cells[10][5].setStatus("GIN");
                            break;
                        case KIN:
                            cells[10][4].setCapture_switch(1);
                            cells[10][4].addCapture_count(1);
                            break;
                        case KAKU:
                        case UMA:
                            cells[10][3].setCapture_switch(1);
                            cells[10][3].addCapture_count(1);
                            cells[10][3].setStatus("KAKU");
                            break;
                        case HISYA:
                        case RYU:
                            cells[10][2].setCapture_switch(1);
                            cells[10][2].addCapture_count(1);
                            cells[10][2].setStatus("HISYA");
                            break;
                        case SISI:
                        case Quiin:
                            cells[10][3].setCapture_switch(1);
                            cells[10][3].addCapture_count(1);
                            cells[10][3].setStatus("Quiin");
                            break;
                        case Knight:
                            cells[10][6].setCapture_switch(1);
                            cells[10][6].addCapture_count(1);
                            cells[10][6].setStatus("Knight");
                            break;
                        case OU:
                            game.win(1);
                            return;
                    }
                    if(mode.getTwice_mode(Turn) == 1)Twice_count=2;
                    if(mode.getRestrict(1)!=0 && mode.getRestrict(1)<getPiecesCount(1)){//持ち駒制限将棋ルール
                        game.win(2);
                        return;
                    }
                }
                if(y_prev == 10){//持ち駒使用
                    cell.setStatus(Enemy);
                    cell.setEnemy(turn);
                    cell_prev.reduCapture_count(1);
                    if(cell_prev.getCapture_count() == 0) {
                        cell_prev.setCapture_switch(0);
                    }
                    getCheckHinto(Turn);
                }else if(y != 10 &&(y<=3 ||y_prev<=3)){//if 成
                    Log.d("成り動作","y:"+y+"x:"+x+":駒；"+cell_prev.getStatus());
                    switch(cell_prev.getStatus()){
                        case Fu://歩
                            if(1 < y){
                                getDialog(0,y,x,Turn);
                            }else{
                                cell.setStatus("TO");
                                cell.setEnemy(turn);
                                cell_prev.setStatus("None");
                                cell_prev.setEnemy(0);
                                getCheckHinto(Turn);
                            }
                            break;
                        case KYO://香
                            if(1 < y){
                                getDialog(1,y,x,Turn);
                            }else{
                                cell.setStatus("N_KYO");
                                cell.setEnemy(turn);
                                cell_prev.setStatus("None");
                                cell_prev.setEnemy(0);
                                getCheckHinto(Turn);
                            }
                            break;
                        case KEI://桂
                            if(2 < y){
                                getDialog(2,y,x,Turn);
                            }else{
                                cell.setStatus("N_KEI");
                                cell.setEnemy(turn);
                                cell_prev.setStatus("None");
                                cell_prev.setEnemy(0);
                                getCheckHinto(Turn);
                            }
                            break;
                        case GIN://銀
                            getDialog(3,y,x,Turn);
                            break;
                        case HISYA://飛車
                            cell.setStatus("RYU");
                            cell.setEnemy(turn);
                            cell_prev.setStatus("None");
                            cell_prev.setEnemy(0);
                            getCheckHinto(Turn);
                            break;
                        case KAKU://角
                            cell.setStatus("UMA");
                            cell.setEnemy(turn);
                            cell_prev.setStatus("None");
                            cell_prev.setEnemy(0);
                            getCheckHinto(Turn);
                            break;
                        case Quiin:
                            getDialog(4,y,x,Turn);
                            break;
                        default:
                            cell.setStatus(Enemy);
                            cell.setEnemy(turn);
                            cell_prev.setStatus("None");
                            cell_prev.setEnemy(0);
                            getCheckHinto(Turn);
                            break;
                    }
                }else if(y != 10){//成れない
                    if(Enemy.equals("GYOKU")){
                        gyoku_pos[0] = y;
                        gyoku_pos[1] = x;
                    }
                    cell.setStatus(Enemy);
                    cell.setEnemy(turn);
                    cell_prev.setStatus("None");
                    cell_prev.setEnemy(0);
                    getCheckHinto(Turn);
                }
                if(mode.getTwice_mode(Turn)==1 && Twice_count!=2)Twice_count+=1;
                break;
            case 2://後手番

                if(cell.getEnemy() == 1) {//駒を取る
                    switch (cell.getStatus()) {
                        case TO:
                        case Fu:
                            cells[0][0].setCapture_switch(1);
                            cells[0][0].addCapture_count(1);
                            cells[0][0].setStatus("Fu");
                            break;
                        case KYO:
                        case N_KYO:
                            cells[0][1].setCapture_switch(1);
                            cells[0][1].addCapture_count(1);
                            cells[0][1].setStatus("KYO");
                            break;
                        case KEI:
                        case N_KEI:
                            cells[0][2].setCapture_switch(1);
                            cells[0][2].addCapture_count(1);
                            cells[0][2].setStatus("KEI");
                            break;
                        case GIN:
                        case N_GIN:
                            cells[0][3].setCapture_switch(1);
                            cells[0][3].addCapture_count(1);
                            cells[0][3].setStatus("GIN");
                            break;
                        case KIN:
                            cells[0][4].setCapture_switch(1);
                            cells[0][4].addCapture_count(1);
                            break;
                        case KAKU:
                        case UMA:
                            cells[0][5].setCapture_switch(1);
                            cells[0][5].addCapture_count(1);
                            cells[0][5].setStatus("KAKU");
                            break;
                        case HISYA:
                        case RYU:
                            cells[0][6].setCapture_switch(1);
                            cells[0][6].addCapture_count(1);
                            cells[0][6].setStatus("HISYA");
                            break;
                        case SISI:
                        case Quiin:
                            cells[0][5].setCapture_switch(1);
                            cells[0][5].addCapture_count(1);
                            cells[0][5].setStatus("Quiin");
                            break;
                        case Knight:
                            cells[0][2].setCapture_switch(1);
                            cells[0][2].addCapture_count(1);
                            cells[0][2].setStatus("Knight");
                            break;
                        case GYOKU:
                            game.win(2);
                            return;
                    }
                    if(mode.getTwice_mode(Turn) == 1)Twice_count=2;
                    if(mode.getRestrict(2)!=0 && mode.getRestrict(2)<getPiecesCount(2)){//持ち駒制限将棋ルール
                        game.win(1);
                        return;
                    }
                }
                if(y_prev == 0){//持ち駒使用
                    cell.setStatus(Enemy);
                    cell_prev.reduCapture_count(1);
                    cell.setEnemy(turn);
                    if(cell_prev.getCapture_count() == 0) {
                        cell_prev.setCapture_switch(0);
                    }
                    getCheckHinto(Turn);
                }else if(7 <= y || 7 <= y_prev){//成
                    switch(cell_prev.getStatus()){
                        case Fu:
                            if(y < 9){
                                getDialog(0,y,x,Turn);
                            }else{
                                cell.setStatus("TO");
                                cell.setEnemy(turn);
                                cell_prev.setStatus("None");
                                cell_prev.setEnemy(0);
                                getCheckHinto(Turn);
                            }
                            break;
                        case KYO:
                            if(y < 9){
                                getDialog(0,y,x,Turn);
                            }else{
                                cell.setStatus("N_KYO");
                                cell.setEnemy(turn);
                                cell_prev.setStatus("None");
                                cell_prev.setEnemy(0);
                                getCheckHinto(Turn);
                            }
                            break;
                        case KEI:
                            if(y < 8){
                                getDialog(2,y,x,Turn);
                            }else{
                                cell.setStatus("N_KEI");
                                cell.setEnemy(turn);
                                cell_prev.setStatus("None");
                                cell_prev.setEnemy(0);
                                getCheckHinto(Turn);
                            }
                            break;
                        case GIN:
                            getDialog(3,y,x,Turn);
                            break;
                        case KAKU:
                            cell.setStatus("UMA");
                            cell.setEnemy(turn);
                            cell_prev.setStatus("None");
                            cell_prev.setEnemy(0);
                            getCheckHinto(Turn);
                            break;
                        case HISYA:
                            cell.setStatus("RYU");
                            cell.setEnemy(turn);
                            cell_prev.setStatus("None");
                            cell_prev.setEnemy(0);
                            getCheckHinto(Turn);
                            break;
                        case Quiin:
                        getDialog(4,y,x,Turn);
                        break;
                        default:
                            cell.setStatus(Enemy);
                            cell.setEnemy(turn);
                            cell_prev.setStatus("None");
                            cell_prev.setEnemy(0);
                            getCheckHinto(Turn);
                            break;
                    }
                }else{//成れない
                    if(Enemy.equals("OU")){
                        ou_pos[0] = y;
                        ou_pos[1] = x;
                    }
                    cell.setStatus(Enemy);
                    cell.setEnemy(turn);
                    cell_prev.setStatus("None");
                    cell_prev.setEnemy(0);
                    getCheckHinto(Turn);
                }
                if(mode.getTwice_mode(Turn)==1 && Twice_count!=2)Twice_count+=1;
                break;
        }
        cell.setEnemy(Turn);
        if(!(y_prev ==10 || y_prev ==0)) {
            cell_prev.setStatus("None");
            cell_prev.setEnemy(0);
        }

    }

    //成るか質問
    public void getDialog(final int i,final int y,final int x,final int Turn){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity).setCancelable(false);
        alertBuilder.setMessage("成りますか？");
        alertBuilder.setPositiveButton("いいえ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //値を送って終了
                cells[y][x].setStatus(enemy_name[i][0]);
                getCheckHinto(Turn);
                invalidate();
            }
        });
        alertBuilder.setNegativeButton("はい", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //値を送って終了
                cells[y][x].setStatus(enemy_name[i][1]);
                getCheckHinto(Turn);
                invalidate();
            }
        });
        alertBuilder.create().show();
    }

    //王手判定
    public void getCheckHinto(int Turn){
        Log.d("王手チェック","王手チェック"+turn+Turn);
        if((Turn ==1 && o_enemy_search(ou_pos[0], ou_pos[1], 2, 0, 11)==1)){//turn(2　後手)
            Log.d("王手チェック","先手");
            if(mode.getCheck_mode(1)==1){
                game.win(1);
            }else {
                getCheckDialog(check_pos[0], check_pos[1], ou_pos[0], ou_pos[1]);
            }
        }
        if((Turn ==2 && o_enemy_search(gyoku_pos[0], gyoku_pos[1],1, 0, 11)==1)){
            Log.d("王手チェック","後手");
            if(mode.getCheck_mode(2)==1){
                game.win(2);
            }else {
                getCheckDialog(check_pos[0], check_pos[1], gyoku_pos[0], gyoku_pos[1]);
            }
        }
    }

    //詰み判定
    public void getCheckDialog(final int y_check,final int x_check,final int y,final int x){

        Log.d("getCheckDialog","ターン"+turn);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity).setCancelable(false);
        alertBuilder.setMessage("王手！！");
        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int Turn = turn==1 ? 2:1;
                int count = 0;//0ならば詰み
                direction[0] = 0;//y座標
                direction[1] = 0;//x座標

                //動かしちゃいけない駒チェック
                Log.d("getCheckHinto","貫通チェック");
                big_enemy_Search(y,x,Turn);

                Log.d("getCheckHint","合駒チェック");
                //飛車、角、香車なら間駒できるか
                int ch = 0;
                switch(cells[y_check][x_check].getStatus()) {
                    case HISYA:
                    case RYU:
                    case KAKU:
                    case UMA:
                        if(direction[0]!=0 && direction[1]!=0) {
                            int y_count = y + direction[0];
                            int x_count = x + direction[1];
                            int y_dire = direction[0];
                            int x_dire = direction[1];
                            while (y_count != y_check && x_count != x_check) {
                                ch += o_enemy_search(y_count, x_count,Turn, 11, 0);
                                y_count += y_dire;
                                x_count += x_dire;
                            }
                        }
                    break;
                }
                if (ch == 0) {
                    Log.d("getCheckHint", "間駒できる駒がありません");
                } else {
                    Log.d("getCheckHint", "間駒出来ます");
                    count+=1;
                }


                //逃げれるか
                Log.d("escape_Hint","逃げ場はあるか"+(9-x)+","+y+cells[y][x].getName());
                int check2 =0;
                for(int i=y-1;i<=y+1;i++){
                    for(int j = x-1;j<=x+1;j++){
                        if(i==y && j == x){//玉の位置
                            continue;
                        }
                        if(0<i && i<10 && 0<=j && j<=8 && cells[i][j].getEnemy() != turn){//いけたらそこで王手にならないか判断
                            check2+=1;
                            Log.d("enemy_Hint",(9-j)+","+i+"Turn:"+turn);
                            check2 -= o_enemy_search(i,j,turn,y,x);
                        }
                    }
                }
                Log.d("eccape_Hint","逃げれる場所の数:"+check2);
                count += check2==0? 0:1;


                //駒を取れるか
                Log.d("getCheckDialog","王様以外で駒を取れるか");
                if(enemy_search(y_check,x_check,cells,Turn,0,0)==1){
                    //bigの中からenemy検索して残っていれば
                    Log.d("getCheckDialog","駒を取れます");
                    count+=1;
                }else{
                    Log.d("getCheckDialog","駒を取れません");
                }

                //最終判断
                if(count==0){
                    game.win(Turn);
                }
            }
        });
        alertBuilder.create().show();

    }


    //王様が逃げた先に駒が利いてるか判断
    /*飛車→角→桂の順で判断
    動いた先に何か駒が聞いていれば動かせない
    * 駒がいたらreutrn 1*/
    public int o_enemy_search(int y,int x,int Turn,int y_prev,int x_prev){
        //Turn 渡した駒のターン 1:自分　2:相手
        Integer search_switch[] = new Integer[10];
        Arrays.fill(search_switch, 0);
        for(int count = 1;count < 8;count++) {
            //上
            if (0 < y-count) {
                if (search_switch[0] == 0) {
                    if (cells[y - count][x].getEnemy() == Turn &&  !(y-count==y_prev && x==x_prev)) {//もし前に玉がいた位置ならスルー
                        search_switch[0] = 1;
                    } else {
                        if (count == 1) {//１マス目
                            if(Turn ==1 && cells[y-count][x].getEnemy()==2 && Arrays.asList(Cell.E_STATUS.Up).contains(cells[y-count][x].getStatus()) && !(cells[y-count][x].name().equals("GYOKU"))) {
                                if((y_prev==11 || (y_prev==y-count && x_prev==x))&& cells[y-count][x].name().equals("OU")){
                                    search_switch[0] = 1;
                                }else {
                                    Log.d("enemy_search", (9 - x) + "," + (y-count) + cells[y-count][x].getName());
                                    search_switch[0] = 2;
                                    if(x==11) {
                                        check_pos[0] = y - count;
                                        check_pos[1] = x;
                                    }
                                }

                            }else if(Turn==2 && cells[y-count][x].getEnemy()==1 && Arrays.asList(Cell.E_STATUS.Back).contains(cells[y-count][x].getStatus()) && !(cells[y-count][x].name().equals("OU"))){
                                if((y_prev==11 || (y_prev==y-count && x_prev==x)) && cells[y-count][x].name().equals("GYOKU")){
                                    search_switch[0] = 1;
                                }else {
                                    Log.d("enemy_search", (9 - x) + "," + (y-count) + cells[y-count][x].getName());
                                    search_switch[0] = 2;
                                   if(x==11) {
                                       check_pos[0] = y - count;
                                       check_pos[1] = x;
                                   }
                                }

                            }
                        } else {
                            switch (cells[y - count][x].getStatus()) {//２マス目以降
                                case None:
                                    break;
                                case Quiin:
                                case HISYA:
                                case RYU:
                                    if(Turn!=cells[y - count][x].getEnemy()) {
                                        Log.d("enemy_search", (9 - x) + "," + (y - count));
                                        direction[0] = -1;
                                        direction[1] = 0;
                                        search_switch[0] = 2;
                                       if(x==11) {
                                           check_pos[0] = y - count;
                                           check_pos[1] = x;
                                       }
                                        break;
                                    }
                                default:
                                    if(Turn ==1 && cells[y - count][x].getStatus()==Cell.E_STATUS.KYO && cells[y - count][x].getEnemy() ==2){
                                        search_switch[0] = 2;
                                        if(x==11) {
                                            check_pos[0] = y - count;
                                            check_pos[1] = x;
                                        }
                                    }else {
                                        search_switch[0] = 1;
                                    }
                                    break;
                            }
                        }
                    }
                }
                //右上
                if (x + count <= 8 && search_switch[1] == 0) {
                    if (cells[y - count][x + count].getEnemy() == Turn &&!(y-count==y_prev && x+count==x_prev)) {
                        search_switch[1] = 1;
                    } else {
                        if (count == 1) {
                            if(Turn ==1 && cells[y-count][x+count].getEnemy()==2 && Arrays.asList(Cell.E_STATUS.D_Up).contains(cells[y-count][x+count].getStatus()) && !(cells[y-count][x+count].name().equals("GYOKU"))) {
                                if((y_prev==11 || (y_prev==y-count && x_prev==x+count))&&cells[y-count][x+count].name().equals("OU")){
                                    search_switch[1] = 1;
                                }else {
                                    Log.d("enemy_search", (9 - (x + count)) + "," + (y-count) + cells[y-count][x + count].getName());
                                    search_switch[1] = 2;
                                    if(x==11) {
                                        check_pos[0] = y - count;
                                        check_pos[1] = x + count;
                                    }
                                }

                            }else if(Turn ==2 && cells[y-count][x+count].getEnemy()==1 && Arrays.asList(Cell.E_STATUS.D_Back).contains(cells[y-count][x+count].getStatus()) && !(cells[y-count][x+count].name().equals("OU"))){
                                if((y_prev==11 || (y_prev==y-count && x_prev==x+count))&&cells[y-count][x+count].name().equals("GYOKU")){
                                    search_switch[1] = 1;
                                }else {
                                    Log.d("enemy_search", (9 - (x + count)) + "," + (y-count) + cells[y-count][x + count].getName());
                                    search_switch[1] = 2;
                                    if(x==11) {
                                        check_pos[0] = y - count;
                                        check_pos[1] = x + count;
                                    }
                                }

                            }
                        } else {
                            switch (cells[y - count][x + count].getStatus()) {//２マス目以降
                                case Quiin:
                                case KAKU:
                                case UMA:
                                    Log.d("enemy_search", (9 - (x + count)) + "," + (y - count));
                                    direction[0] = -1;
                                    direction[1] = 1;
                                    search_switch[1] = 2;
                                    if(x==11) {
                                        check_pos[0] = y - count;
                                        check_pos[1] = x + count;
                                    }
                                    break;
                                case None:
                                    break;
                                default:
                                    search_switch[1] = 1;
                                    break;
                            }
                        }
                    }
                }

                //左上
                if (0 <= x - count && search_switch[2] == 0) {
                    if (cells[y - count][x - count].getEnemy() == Turn && !(y-count==y_prev && x-count==x_prev)) {
                        search_switch[2] = 1;
                    } else {
                        if (count == 1) {
                            if(Turn ==1 && cells[y-count][x-count].getEnemy()==2 && Arrays.asList(Cell.E_STATUS.D_Up).contains(cells[y-count][x-count].getStatus()) && !(cells[y-count][x-count].name().equals("GYOKU"))) {
                                if((y_prev==11 || (y_prev==y-count && x_prev==x-count))&&cells[y-count][x-count].name().equals("OU")){
                                    search_switch[2] = 1;
                                }else {
                                    Log.d("enemy_search", (9 - (x - count)) + "," + (y-count) + cells[y-count][x - count].getName());
                                    search_switch[2] = 2;
                                    if(x==11) {
                                        check_pos[0] = y - count;
                                        check_pos[1] = x - count;
                                    }
                                }

                            }else if(Turn ==2 && cells[y-count][x-count].getEnemy()==1 && Arrays.asList(Cell.E_STATUS.D_Back).contains(cells[y-count][x-count].getStatus()) && !(cells[y-count][x-count].name().equals("OU"))){
                                if((y_prev==11 || (y_prev==y-count && x_prev==x-count))&&cells[y-count][x-count].name().equals("GYOKU")){
                                    search_switch[2] = 1;
                                }else {
                                    Log.d("enemy_search", (9 - (x - count)) + "," + (y-count) + cells[y-count][x - count].getName());
                                    search_switch[2] = 2;
                                    if(x==11) {
                                        check_pos[0] = y - count;
                                        check_pos[1] = x - count;
                                    }
                                }

                            }
                        } else {
                            switch (cells[y - count][x - count].getStatus()) {//２マス目以降
                                case Quiin:
                                case KAKU:
                                case UMA:
                                    Log.d("enemy_search", (9 - (x - count)) + "," + (y - count));
                                    direction[0] = -1;
                                    direction[1] = -1;
                                    search_switch[2] = 2;
                                    if(x==11) {
                                        check_pos[0] = y - count;
                                        check_pos[1] = x - count;
                                    }
                                    break;
                                case None:
                                    break;
                                default:
                                    search_switch[2] = 1;
                                    break;
                            }
                        }
                    }
                }
            }

            //下
            if (y + count < 10) {
                if (search_switch[3] == 0) {
                    if (cells[y + count][x].getEnemy() == Turn  && !(y+count==y_prev && x==x_prev)) {
                        search_switch[3] = 1;
                    } else {
                        if (count == 1) {//１マス目
                            if(Turn ==1 && Arrays.asList(Cell.E_STATUS.Back).contains(cells[y+count][x].getStatus()) && !(cells[y+count][x].name().equals("OU"))) {
                                if((y_prev==11 || (y_prev==y+count && x_prev==x))&&cells[y+count][x].name().equals("GYOKU")){
                                    search_switch[3] = 1;
                                }else {
                                    Log.d("enemy_search", (9 - x) + "," + (y+count) + cells[y+count][x].getName());
                                    search_switch[3] = 2;
                                    if(x==11) {
                                        check_pos[0] = y + count;
                                        check_pos[1] = x;
                                    }
                                }

                            }else if(Turn ==2 && Arrays.asList(Cell.E_STATUS.Up).contains(cells[y+count][x].getStatus()) && !(cells[y+count][x].name().equals("GYOKU"))){
                                if((y_prev==11 || (y_prev==y+count && x_prev==x)) &&cells[y+count][x].name().equals("OU")){
                                    search_switch[3] = 1;
                                }else {
                                    Log.d("enemy_search", (9 - x)+ "," + (y+count) + cells[y+count][x].getName());
                                    search_switch[3] = 2;
                                    if(x==11) {
                                        check_pos[0] = y + count;
                                        check_pos[1] = x;
                                    }
                                }

                            }
                        } else {
                            switch (cells[y + count][x].getStatus()) {//２マス目以降
                                case Quiin:
                                case HISYA:
                                case RYU:
                                    Log.d("enemy_search", (9 - x) + "," + (y + count));
                                    direction[0] = 1;
                                    direction[1] = 0;
                                    search_switch[3] = 2;
                                    if(x==11) {
                                        check_pos[0] = y + count;
                                        check_pos[1] = x;
                                    }
                                    break;
                                case None:
                                    break;
                                default:
                                    if(Turn ==2 && cells[y + count][x].getStatus()==Cell.E_STATUS.KYO && cells[y + count][x].getEnemy() ==1){
                                        Log.d("enemy_search", (9 - x) + "," + (y + count));
                                        search_switch[3] = 2;
                                        if(x==11) {
                                            check_pos[0] = y + count;
                                            check_pos[1] = x;
                                        }
                                    }else {
                                        search_switch[3] = 1;
                                    }
                                    break;
                            }
                        }
                    }
                }
                //右下
                if (x + count <= 8 && search_switch[4] == 0) {
                    if (cells[y + count][x + count].getEnemy() == Turn &&  !(y+count==y_prev && x+count==x_prev)) {
                        search_switch[4] = 1;
                    } else {
                        if (count == 1) {
                            if(Turn ==1 && Arrays.asList(Cell.E_STATUS.D_Back).contains(cells[y+count][x+count].getStatus()) && !(cells[y+count][x+count].name().equals("GYOKU"))) {
                                if((y_prev==11 || (y_prev==y+count && x_prev==x+count))&&cells[y+count][x+count].name().equals("OU")){
                                    search_switch[4] = 1;
                                }else {
                                    Log.d("enemy_search", (9 - (x + count)) + "," + (y + count) + cells[y + count][x + count].getName());
                                    search_switch[4] = 2;
                                    if(x==11) {
                                        check_pos[0] = y + count;
                                        check_pos[1] = x + count;
                                    }
                                }

                            }else if(Turn ==2 && Arrays.asList(Cell.E_STATUS.D_Up).contains(cells[y+count][x+count].getStatus()) && !(cells[y+count][x+count].name().equals("OU"))){
                                if((y_prev==11 || (y_prev==y+count && x_prev==x+count))&&cells[y+count][x+count].name().equals("GYOKU")){
                                    search_switch[4] = 1;
                                }else {
                                    Log.d("enemy_search", (9 - (x + count)) + "," + (y + count) + cells[y + count][x + count].getName());
                                    search_switch[4] = 2;
                                    if(x==11) {
                                        check_pos[0] = y + count;
                                        check_pos[1] = x + count;
                                    }
                                }

                                }
                        } else {
                            switch (cells[y + count][x + count].getStatus()) {//２マス目以降
                                case Quiin:
                                case KAKU:
                                case UMA:
                                    Log.d("enemy_search", (9 - (x + count)) + "," + (y + count));
                                    direction[0] = 1;
                                    direction[1] = 1;
                                    search_switch[4] = 2;
                                    if(x==11) {
                                        check_pos[0] = y + count;
                                        check_pos[1] = x + count;
                                    }
                                    break;
                                case None:
                                    break;
                                default:
                                    search_switch[4] = 1;
                                    break;
                            }
                        }
                    }
                }

                //左下
                if (0 <= x - count && search_switch[5] == 0) {
                    if (cells[y + count][x - count].getEnemy() == Turn &&  !(y+count==y_prev && x-count==x_prev)) {
                        search_switch[5] = 1;
                    } else {
                        if (count == 1) {
                            if(Turn ==1 && Arrays.asList(Cell.E_STATUS.D_Back).contains(cells[y+count][x-count].getStatus()) && !(cells[y+count][x-count].name().equals("GYOKU"))) {
                                if((y_prev==11 || (y_prev==y+count && x_prev==x-count))&&cells[y+count][x-count].name().equals("OU")){
                                    search_switch[5] = 1;
                                }else {
                                    Log.d("enemy_search", (9 - (x - count)) + "," + (y + count) + cells[y + count][x - count].getName());
                                    search_switch[5] = 2;
                                    if(x==11) {
                                        check_pos[0] = y + count;
                                        check_pos[1] = x - count;
                                    }
                                }

                            }else if(Turn ==2 && Arrays.asList(Cell.E_STATUS.D_Up).contains(cells[y+count][x-count].getStatus()) && !(cells[y+count][x-count].name().equals("OU"))){
                                if((y_prev==11 || (y_prev==y+count && x_prev==x-count))&&cells[y+count][x-count].name().equals("GYOKU")){
                                    search_switch[5] = 1;
                                }else {
                                    Log.d("enemy_search", (9 - (x - count)) + "," + (y + count) + cells[y + count][x - count].getName());
                                    search_switch[5] = 2;
                                    if(x==11) {
                                        check_pos[0] = y + count;
                                        check_pos[1] = x - count;
                                    }
                                }

                            }
                        } else {
                            switch (cells[y + count][x - count].getStatus()) {//２マス目以降
                                case Quiin:
                                case KAKU:
                                case UMA:
                                    Log.d("enemy_search", (9 - (x - count)) + "," + (y + count));
                                    direction[0] = 1;
                                    direction[1] = -1;
                                    search_switch[5] = 2;
                                    if(x==11) {
                                        check_pos[0] = y + count;
                                        check_pos[1] = x - count;
                                    }
                                    break;
                                case None:
                                    break;
                                default:
                                    search_switch[5] = 1;
                                    break;
                            }
                        }
                    }
                }
            }
            //右
            if (x + count <= 8 && search_switch[6]==0) {
                if (cells[y][x + count].getEnemy() == Turn &&  !(y==y_prev && x+count==x_prev)) {
                    search_switch[6] = 1;
                } else {
                    if (count == 1) {//１マス目
                            if(Turn==1 && cells[y][x+count].getEnemy()==2 && Arrays.asList(Cell.E_STATUS.Left_right).contains(cells[y][x+count].getStatus()) && !(cells[y][x + count].name().equals("GYOKU"))){
                                if((y_prev==11 || (y_prev==y&& x_prev==x+count))&&cells[y][x+count].name().equals("OU")){
                                    search_switch[6] = 1;
                                }else {
                                    Log.d("enemy_search", (9 - (x + count)) + "," + y + cells[y][x + count].getName());
                                    Log.d("ターン","玉"+Turn+"当たりごま"+cells[y][x+count].getEnemy());
                                    search_switch[6] = 2;
                                    if(x==11) {
                                        check_pos[0] = y;
                                        check_pos[1] = x + count;
                                    }
                                }
                            }else if(Turn==2 && cells[y][x+count].getEnemy()==1&& Arrays.asList(Cell.E_STATUS.Left_right).contains(cells[y][x+count].getStatus()) && !(cells[y][x + count].name().equals("OU"))){
                                if((y_prev==11 || (y_prev==y&& x_prev==x+count))&&cells[y][x+count].name().equals("GYOKU")){
                                    search_switch[6] = 1;
                                }else {
                                    Log.d("enemy_search", (9 - (x + count)) + "," + y + cells[y][x + count].getName());
                                    search_switch[6] = 2;
                                    if(x==11) {
                                        check_pos[0] = y;
                                        check_pos[1] = x + count;
                                    }
                                }
                            }

                    }else {
                        switch (cells[y][x + count].getStatus()) {//２マス目以降
                            case Quiin:
                            case HISYA:
                            case RYU:
                                Log.d("enemy_search", (9 - (x + count)) + "," + y);
                                direction[0] = 0;
                                direction[1] = 1;
                                search_switch[6] = 2;
                                if(x==11) {
                                    check_pos[0] = y;
                                    check_pos[1] = x + count;
                                }
                                break;
                            case None:
                                break;
                            default:
                                search_switch[6] = 1;
                                break;
                        }
                    }
                }
            }
            //左
            if (0 <= x - count && search_switch[7]==0) {
                if (cells[y][x - count].getEnemy() == Turn &&  !(y==y_prev && x-count==x_prev)) {
                    search_switch[7] = 1;
                } else {
                    if (count == 1) {//１マス目
                        if(Turn ==1 && cells[y][x-count].getEnemy()==2 && Arrays.asList(Cell.E_STATUS.Left_right).contains(cells[y][x-count].getStatus()) && !(cells[y][x-count].name().equals("GYOKU"))){//左との比較
                            if((y_prev==11 || (y_prev==y && x_prev==x-count))&&cells[y][x-count].name().equals("OU")){
                                search_switch[7] = 1;
                            }else {
                                Log.d("enemy_search", (9 - (x - count)) + "," + y + cells[y][x - count].getName());
                                search_switch[7] = 2;
                                if(x==11) {
                                    check_pos[0] = y;
                                    check_pos[1] = x - count;
                                }
                            }

                        }else if(Turn ==2 && cells[y][x-count].getEnemy()==1 && Arrays.asList(Cell.E_STATUS.Left_right).contains(cells[y][x-count].getStatus()) && !(cells[y][x-count].name().equals("OU"))){
                            if((y_prev==11 || (y_prev==y && x_prev==x-count)) && cells[y][x-count].name().equals("GYOKU")){
                                search_switch[7] = 1;
                            }else {
                                Log.d("enemy_search", (9 - (x - count)) + "," + y + cells[y][x - count].getName());
                                search_switch[7] = 2;
                                if(x==11) {
                                    check_pos[0] = y;
                                    check_pos[1] = x - count;
                                }
                            }

                        }
                    }else{
                        switch (cells[y][x - count].getStatus()) {//２マス目以降
                            case None:
                                break;
                            case Quiin:
                            case HISYA:
                            case RYU:
                                if(Turn!=cells[y][x-count].getEnemy()) {
                                    Log.d("enemy_search", (9 - (x-count)) + "," + y);
                                    direction[0] = 0;
                                    direction[1] = -1;
                                    search_switch[7] = 2;
                                    if(x==11) {
                                        check_pos[0] = y;
                                        check_pos[1] = x - count;
                                    }
                                    break;
                                }
                            default:
                                search_switch[7] = 1;
                                break;
                        }
                    }
                }
            }

            //桂
            if(Turn ==1) {
                if (count == 2 && 0 < y - count) {
                    if (0 < x && cells[y - 2][x - 1].getStatus() == Cell.E_STATUS.KEI) {
                        Log.d("enemy_search", (9 - (x - 1)) + "," +(y-2));
                        search_switch[8] = 2;
                        if(x==11) {
                            check_pos[0] = y - 2;
                            check_pos[1] = x - 1;
                        }
                    }
                    if (x < 8 && cells[y - 2][x + 1].getStatus() == Cell.E_STATUS.KEI) {
                        Log.d("enemy_search", (9 - (x + 1)) + "," +(y-2));
                        search_switch[8] = 2;
                        if(x==11) {
                            check_pos[0] = y - 2;
                            check_pos[1] = x + 1;
                        }
                    }
                }
            }else{
                if (count == 2 && y + count<10) {
                    if (0 < x && cells[y + 2][x - 1].getStatus() == Cell.E_STATUS.KEI) {
                        Log.d("enemy_search", (9 - (x - 1)) + "," +(y+2));
                        search_switch[8] = 2;
                        if(x==11) {
                            check_pos[0] = y + 2;
                            check_pos[1] = x - 1;
                        }
                    }
                    if (x < 8 && cells[y + 2][x + 1].getStatus() == Cell.E_STATUS.KEI) {
                        Log.d("enemy_search", (9 - (x + 1)) + "," +(y+2));
                        search_switch[8] = 2;
                        if(x==11) {
                            check_pos[0] = y + 2;
                            check_pos[1] = x + 1;
                        }
                    }
                }
            }
        }
        if(mode.getChess_mode(turn)==1){//クイーン将棋の場合
            for(int i= y-2;i<=y+2;i++){
                for(int j=x-2;j<=x+2;j++){
                    if(1<=i && i<9 && 0<=j && j<=8 && cells[i][j].getStatus()==Cell.E_STATUS.SISI){
                        search_switch[8]=2;
                        i+=9;
                        j+=9;
                    }
                }
            }

        }
        return  Arrays.asList(search_switch).contains(2) ? 1:0;

    }



    //逃げた先に駒が利いてるか判断
    /*王手している駒を取れるか*/
    public int enemy_search(int y,int x,Cell[][] cells,int Turn,int y_prev,int x_prev){
        //Turn 渡した駒のターン 1:自分　2:相手
        Integer search_switch[] = new Integer[9];
        Arrays.fill(search_switch, 0);
        for(int count = 1;count < 8;count++) {
            //上方向
            if (0 < y-count) {
                if (search_switch[0] == 0) {//上
                    if (cells[y - count][x].getEnemy() == Turn &&  !(y-count==y_prev && x==x_prev)) {
                        search_switch[0] = 1;
                    } else {
                        if (count == 1) {//１マス目
                            if(Turn ==1 && Arrays.asList(Cell.E_STATUS.Up).contains(cells[y-count][x].getStatus()) && !(cells[y-count][x].name().equals("GYOKU"))){
                                if(cells[y-count][x].name().equals("OU")){
                                    search_switch[0] = 1;
                                }else if(through_search(y-count,x,0)==1 && y_prev==0){//動かしたとき貫通王手にならないか
                                    Log.d("enemy_search", (9 - x) + "," + (y-count)+cells[y-count][x].getName());
                                    search_switch[0] = 2;
                                }else {
                                    search_switch[0] = 1;
                                }
                            }else if(Turn ==2 && Arrays.asList(Cell.E_STATUS.Up).contains(cells[y-count][x].getStatus()) && !(cells[y-count][x].name().equals("OU"))){
                                if(cells[y-count][x].name().equals("GYOKU")){
                                    search_switch[0] = 1;
                                }else if(through_search(y-count,x,0)==1 && y_prev==0){
                                    Log.d("enemy_search", (9 - x) + "," + (y-count)+cells[y-count][x].getName());
                                    search_switch[0] = 2;
                                }else {
                                    search_switch[0] = 1;
                                }
                            }
                        } else {
                            switch (cells[y - count][x].getStatus()) {//２マス目以降
                                case None:
                                    break;
                                case HISYA:
                                case RYU:
                                    Log.d("enemy_search", (9 - x) + "," + (y - count));
                                    search_switch[0] = 2;
                                    break;
                                default:
                                    if(Turn ==1 && cells[y - count][x].getStatus()==Cell.E_STATUS.KYO && cells[y - count][x].getEnemy() ==2){
                                        search_switch[0] = 2;
                                    }else {
                                        search_switch[0] = 1;
                                    }
                                    break;
                            }
                        }
                    }
                }
                //右上
                if (x + count <= 8 && search_switch[1] == 0) {
                    if (cells[y - count][x + count].getEnemy() == Turn &&!(y-count==y_prev && x+count==x_prev)) {
                        search_switch[1] = 1;
                    } else {
                        if (count == 1) {
                            if(Turn ==1) {
                                switch (cells[y - count][x + count].getStatus()) {
                                    case OU:
                                        if(!(enemy_search(y,x,cells,2,y-count,x+count) == 0)){
                                            break;
                                        }
                                    case KIN:
                                    case GIN:
                                    case N_GIN:
                                    case N_KEI:
                                    case N_KYO:
                                    case RYU:
                                    case UMA:
                                    case KAKU:
                                    case TO:
                                        if(through_search(y-count,x+count,1)==1 && y_prev==0){
                                            Log.d("enemy_search", (9 - (x+count)) + "," + (y-count)+cells[y-count][x+count].getName());
                                            search_switch[1] = 2;
                                        }else {
                                            search_switch[1] = 1;
                                        }
                                        break;
                                }
                            }else{
                                switch (cells[y - count][x + count].getStatus()) {
                                    case GYOKU:
                                        if(!(enemy_search(y,x,cells,1,y+count,x+count) == 0)){
                                            break;
                                        }
                                    case GIN:
                                    case RYU:
                                    case UMA:
                                    case KAKU:
                                    case TO:
                                        if(through_search(y-count,x+count,1)==1 && y_prev==0){
                                            Log.d("enemy_search", (9 - (x+count)) + "," + (y-count)+cells[y-count][x+count].getName());
                                            search_switch[1] = 2;
                                        }else {
                                            search_switch[1] = 1;
                                        }
                                        break;
                                }
                            }
                        } else {
                            switch (cells[y - count][x + count].getStatus()) {//２マス目以降
                                case KAKU:
                                case UMA:
                                    Log.d("enemy_search", (9 - (x + count)) + "," + (y - count));
                                    search_switch[1] = 2;
                                    break;
                                case None:
                                    break;
                                default:
                                    search_switch[1] = 1;
                                    break;
                            }
                        }
                    }
                }

                //左上
                if (0 <= x - count && search_switch[2] == 0) {
                    if (cells[y - count][x - count].getEnemy() == Turn && !(y-count==y_prev && x-count==x_prev)) {
                        search_switch[2] = 1;
                    } else {
                        if (count == 1) {
                            if(Turn==1) {
                                switch (cells[y - count][x - count].getStatus()) {
                                    case OU:
                                        if(!(enemy_search(y,x,cells,2,y-count,x-count) == 0)){
                                            break;
                                        }
                                    case KIN:
                                    case GIN:
                                    case N_GIN:
                                    case N_KEI:
                                    case N_KYO:
                                    case RYU:
                                    case UMA:
                                    case KAKU:
                                    case TO:
                                        if(through_search(y-count,x-count,2)==1 && y_prev==0){
                                            Log.d("enemy_search", (9 - (x-count)) + "," + (y-count)+cells[y-count][x-count].getName());
                                            search_switch[2] = 2;
                                        }else {
                                            search_switch[2] = 1;
                                        }
                                        break;
                                }
                            }else{
                                switch (cells[y - count][x - count].getStatus()) {
                                    case GYOKU:
                                        if(!(enemy_search(y,x,cells,1,y-count,x-count) == 0)){
                                            break;
                                        }
                                    case GIN:
                                    case RYU:
                                    case UMA:
                                    case KAKU:
                                    case TO:
                                        if(through_search(y-count,x-count,2)==1 && y_prev==0){
                                            Log.d("enemy_search", (9 - (x-count)) + "," + (y-count)+cells[y-count][x-count].getName());
                                            search_switch[2] = 2;
                                        }else {
                                            search_switch[2] = 1;
                                        }
                                        break;
                                }
                            }

                        } else {
                            switch (cells[y - count][x - count].getStatus()) {//２マス目以降
                                case KAKU:
                                case UMA:
                                    Log.d("enemy_search", (9 - (x - count)) + "," + (y - count));
                                    search_switch[2] = 2;
                                    break;
                                case None:
                                    break;
                                default:
                                    search_switch[2] = 1;
                                    break;
                            }
                        }
                    }
                }
            }

            //下
            if (y + count < 10) {
                if (search_switch[3] == 0) {
                    if (cells[y + count][x].getEnemy() == Turn  && !(y+count==y_prev && x==x_prev)) {
                        search_switch[3] = 1;
                    } else {
                        if (count == 1) {//１マス目
                            if(Turn ==1) {
                                switch (cells[y + count][x].getStatus()) {
                                    case OU:
                                        if(!(enemy_search(y,x,cells,2,y+count,x) == 0)){
                                            break;
                                        }
                                    case KIN:
                                    case N_GIN:
                                    case N_KEI:
                                    case N_KYO:
                                    case HISYA:
                                    case RYU:
                                    case UMA:
                                    case TO:
                                        if(through_search(y+count,x,3)==1 && y_prev==0){
                                            Log.d("enemy_search", (9 - x) + "," + (y + count)+cells[y+count][x].getName());
                                            search_switch[3] = 2;
                                        }else {
                                            search_switch[3] = 1;
                                        }
                                        break;
                                }
                            }else{
                                switch (cells[y + count][x].getStatus()) {
                                    case GYOKU:
                                        if(!(enemy_search(y,x,cells,1,y+count,x) == 0)){
                                            break;
                                        }
                                    case KIN:
                                    case GIN:
                                    case N_GIN:
                                    case N_KEI:
                                    case N_KYO:
                                    case HISYA:
                                    case RYU:
                                    case UMA:
                                    case TO:
                                    case Fu:
                                        if(through_search(y+count,x,3)==1 && y_prev==0){
                                            Log.d("enemy_search", (9 - x) + "," + (y + count)+cells[y+count][x].getName());
                                            search_switch[3] = 2;
                                        }else {
                                            search_switch[3] = 1;
                                        }
                                        break;
                                }
                            }
                        } else {
                            switch (cells[y + count][x].getStatus()) {//２マス目以降
                                case HISYA:
                                case RYU:
                                    Log.d("enemy_search", (9 - x) + "," + (y + count));
                                    search_switch[3] = 2;
                                    break;
                                case None:
                                    break;
                                default:
                                    if(Turn ==2 && cells[y + count][x].getStatus()==Cell.E_STATUS.KYO && cells[y + count][x].getEnemy() ==1){
                                        Log.d("enemy_search", (9 - x) + "," + (y + count));
                                        search_switch[3] = 2;
                                    }else {
                                        search_switch[3] = 1;
                                    }
                                    break;
                            }
                        }
                    }
                }
                //右下
                if (x + count <= 8 && search_switch[4] == 0) {
                    if (cells[y + count][x + count].getEnemy() == Turn &&  !(y+count==y_prev && x+count==x_prev)) {
                        search_switch[4] = 1;
                    } else {
                        if (count == 1) {
                            if(Turn ==1) {
                                switch (cells[y + count][x + count].getStatus()) {
                                    case OU:
                                        if(!(enemy_search(y,x,cells,2,y+count,x+count) == 0)){
                                            break;
                                        }
                                    case GIN:
                                    case RYU:
                                    case UMA:
                                    case KAKU:
                                    case TO:
                                        if(through_search(y+count,x+count,4)==1 && y_prev==0){
                                            Log.d("enemy_search", (9 - (x+count)) + "," + (y + count)+cells[y+count][x+-count].getName());
                                            search_switch[4] = 2;
                                        }else {
                                            search_switch[4] = 1;
                                        }
                                        break;
                                }
                            }else{
                                switch (cells[y + count][x + count].getStatus()) {
                                    case GYOKU:
                                        if(!(enemy_search(y,x,cells,1,y+count,x+count) == 0)){
                                            break;
                                        }
                                    case KIN:
                                    case GIN:
                                    case N_GIN:
                                    case N_KEI:
                                    case N_KYO:
                                    case RYU:
                                    case UMA:
                                    case KAKU:
                                    case TO:
                                        if(through_search(y+count,x+count,4)==1 && y_prev==0){
                                            Log.d("enemy_search", (9 - (x+count)) + "," + (y + count));
                                            search_switch[4] = 2;
                                        }else {
                                            search_switch[4] = 1;
                                        }
                                        break;
                                }
                            }
                        } else {
                            switch (cells[y + count][x + count].getStatus()) {//２マス目以降
                                case KAKU:
                                case UMA:
                                    Log.d("enemy_search", (9 - (x + count)) + "," + (y + count));
                                    search_switch[4] = 2;
                                    break;
                                case None:
                                    break;
                                default:
                                    search_switch[4] = 1;
                                    break;
                            }
                        }
                    }
                }

                //左下
                if (0 <= x - count && search_switch[5] == 0) {
                    if (cells[y + count][x - count].getEnemy() == Turn &&  !(y+count==y_prev && x-count==x_prev)) {
                        search_switch[5] = 1;
                    } else {
                        if (count == 1) {
                            if(Turn ==1) {
                                switch (cells[y + count][x - count].getStatus()) {
                                    case OU:
                                        if(!(enemy_search(y,x,cells,2,y+count,x-count) == 0)){
                                          break;
                                        }
                                    case GIN:
                                    case RYU:
                                    case UMA:
                                    case KAKU:
                                    case TO:
                                        if(through_search(y+count,x-count,5)==1 && y_prev==0){
                                            Log.d("enemy_search", (9 - (x-count)) + "," + (y + count));
                                            search_switch[5] = 2;
                                        } else {
                                            search_switch[5] = 1;
                                        }
                                        break;
                                }
                            }else{
                                switch (cells[y + count][x - count].getStatus()) {
                                    case GYOKU:
                                        if(!(enemy_search(y,x,cells,1,y+count,x-count) == 0)){
                                            break;
                                        }
                                    case KIN:
                                    case GIN:
                                    case N_GIN:
                                    case N_KEI:
                                    case N_KYO:
                                    case RYU:
                                    case UMA:
                                    case KAKU:
                                    case TO:
                                        if(through_search(y+count,x-count,5)==1 && y_prev==0){
                                            Log.d("enemy_search","王手にならないので取れます");
                                            Log.d("enemy_search", (9 - (x-count)) + "," + (y + count));
                                            search_switch[5] = 2;
                                        }else {
                                            search_switch[5] = 1;
                                        }
                                        break;
                                }
                            }
                        } else {
                            switch (cells[y + count][x - count].getStatus()) {//２マス目以降
                                case KAKU:
                                case UMA:
                                    Log.d("enemy_search", (9 - (x - count)) + "," + (y + count));
                                    search_switch[5] = 2;
                                    break;
                                case None:
                                    break;
                                default:
                                    search_switch[5] = 1;
                                    break;
                            }
                        }
                    }
                }
            }
            //右
            if (x + count <= 8 && search_switch[6]==0) {
                if (cells[y][x + count].getEnemy() == Turn &&  !(y==y_prev && x+count==x_prev)) {
                    search_switch[6] = 1;
                } else {
                    if (count == 1) {//１マス目
                            if(Arrays.asList(Cell.E_STATUS.Left_right).contains(cells[y][x+count].getStatus()) &&!( cells[y][x+count].name().equals("OU")) && !(cells[y][x+count].name().equals("GYOKU"))){//右 との比較
                                if(through_search(y,x+count,6)==1 && y_prev==0){
                                    Log.d("enemy_search", (9 - (x+count)) + "," + y+cells[y][x + count].getName());
                                    search_switch[6] = 2;
                                }else {
                                    search_switch[6] = 1;
                                }
                            }else{
                                if(Turn==1&& cells[y][x + count].name().equals("OU") && enemy_search(y,x,cells,2,y,x+count) == 0){
                                    Log.d("enemy_search", (9 - (x + count)) + "," + y+cells[y][x + count].getName());
                                    search_switch[6] = 2;
                                }else if(Turn==2& cells[y][x + count].name().equals("GYOKU") && enemy_search(y,x,cells,1,y,x+count) == 0){
                                    Log.d("enemy_search", (9 - (x + count)) + "," + y+cells[y][x + count].getName());
                                    search_switch[6] = 2;
                                }
                            }
                    }else {
                        switch (cells[y][x + count].getStatus()) {//２マス目以降
                            case HISYA:
                            case RYU:
                                Log.d("enemy_search", (9 - (x + count)) + "," + y);
                                search_switch[6] = 2;
                                break;
                            case None:
                                break;
                            default:
                                search_switch[6] = 1;
                                break;
                        }
                    }
                }
            }
            //左
            if (0 <= x - count && search_switch[7]==0) {
                if (cells[y][x - count].getEnemy() == Turn &&  !(y==y_prev && x-count==x_prev)) {
                    search_switch[7] = 1;
                } else {
                    if (count == 1) {//１マス目
                        if(Arrays.asList(Cell.E_STATUS.Left_right).contains(cells[y][x-count].getStatus()) &&!( cells[y][x-count].name().equals("OU")) && !(cells[y][x-count].name().equals("GYOKU"))){//左との比較
                            if(through_search(y,x-count,7)==1 && y_prev==0){
                                Log.d("enemy_search", (9 - (x-count)) + "," + y+cells[y][x - count].getName());
                                search_switch[7] = 2;
                            }else {
                                search_switch[7] = 1;
                            }
                        }else{
                            if(Turn==1&& cells[y][x - count].name().equals("OU") && enemy_search(y,x,cells,2,y,x-count) == 0){
                                Log.d("enemy_search", (9 - (x - count)) + "," + y+cells[y][x - count].getName());
                                search_switch[7] = 2;
                            }else if(Turn==2& cells[y][x - count].name().equals("GYOKU") && enemy_search(y,x,cells,1,y,x-count) == 0){
                                Log.d("enemy_search", (9 - (x - count)) + "," + y+cells[y][x - count].getName());
                                search_switch[7] = 2;
                            }
                        }
                    }else{
                        switch (cells[y][x - count].getStatus()) {//２マス目以降
                            case HISYA:
                            case RYU:
                                Log.d("enemy_search", (9 - (x - count)) + "," + y);
                                search_switch[7] = 2;
                                break;
                            case None:
                                break;
                            default:
                                search_switch[7] = 1;
                                break;
                        }
                    }
                }
            }

            //桂
            if(Turn ==1) {
                if (count == 2 && 0 < y - count) {
                    if (0 < x && cells[y - 2][x - 1].getStatus() == Cell.E_STATUS.KEI) {
                        Log.d("enemy_search", (9 - (x - 1)) + "," +(y-2)+cells[y-2][x-1].getName());
                        search_switch[8] = 2;
                    }
                    if (x < 8 && cells[y - 2][x + 1].getStatus() == Cell.E_STATUS.KEI) {
                        Log.d("enemy_search", (9 - (x + 1)) + "," +(y-2)+cells[y-2][x+1].getName());
                        search_switch[8] = 2;
                    }
                }
            }else{
                if (count == 2 && y + count<10) {
                    if (0 < x && cells[y + 2][x - 1].getStatus() == Cell.E_STATUS.KEI) {
                        Log.d("enemy_search", (9 - (x - 1)) + "," +(y+2)+cells[y+2][x-1].getName());
                        search_switch[8] = 2;
                    }
                    if (x < 8 && cells[y + 2][x + 1].getStatus() == Cell.E_STATUS.KEI) {
                        Log.d("enemy_search", (9 - (x + 1)) + "," +(y+2)+cells[+2][x+1].getName());
                        search_switch[8] = 2;
                    }
                }
            }
        }

        return  Arrays.asList(search_switch).contains(2) ? 1:0;
    }

/*y,x王様の座標
 */
    public void big_enemy_Search(int y,int x,int Turn){
        Log.d("big_enemy_search","間接チェック：王様座標"+(9 - (x )) + "," + y);
        Integer[] search_switch =new Integer[8];
        Arrays.fill(search_switch, 0);
        for(int i=0;i<8;i++){
            for(int j=0;j<3;j++){
                through_enemy[i][j] = 0;
            }
        }
        for(int count = 1;count <=9;count++) {

            //上
            if (0 < y - count) {//上に壁がないか
                if (search_switch[0] != 2 && search_switch[0] != 3) {//上
                    if (cells[y - count][x].getEnemy() == Turn && search_switch[0] == 0) {//相手の駒があったら終了
                        search_switch[0] = 2;
                        Log.d("big_enemy_search", (9 - (x)) + "," + (y - count) + cells[y - count][x].getName());
                        Log.d("big_enemy_search", "なし 1");
                    } else if (cells[y - count][x].getEnemy() == turn) {//自分の駒があったらチェック
                        if (search_switch[0] == 1) {//２回目は終了
                            search_switch[0] = 2;
                            through_enemy[0][0] = 0;
                            through_enemy[0][1] = 0;
                            through_enemy[0][2] = 0;
                            Log.d("big_enemy_search", "なし 2");
                        } else {
                            //１回目はセット
                            search_switch[0] = 1;
                            through_enemy[0][0] = y - count;
                            through_enemy[0][1] = x;
                            through_enemy[0][2] = 3;
                        }
                    } else if (search_switch[0] == 1) {//セット状態で
                        if (cells[y - count][x].getEnemy() == turn) {//自分の駒なら終了
                            search_switch[0] = 2;
                            through_enemy[0][0] = 0;
                            through_enemy[0][1] = 0;
                            through_enemy[0][2] = 0;
                            Log.d("big_enemy_search", "なし 3");
                        } else if (cells[y - count][x].name().equals("HISYA") || cells[y - count][x].name().equals("RYU")) {//相手の駒で飛車or龍なら
                            //セットおけない
                            Log.d("big_enemy_search", (9 - through_enemy[0][1]) + "," + through_enemy[0][0] + cells[through_enemy[0][0]][through_enemy[0][1]].getName() + "は動かせません");
                            search_switch[0] = 3;
                        }
                    }
                }
            }else if(search_switch[0] == 1){
                through_enemy[0][0] = 0;
                through_enemy[0][1] = 0;
                through_enemy[0][2] = 0;
                search_switch[0] = 2;
            }


                    //左上
                    if (0 < y - count && 0 <= x - count) {//左に壁がないか
                        if (search_switch[1] != 2 && search_switch[1] != 3) {//左
                            if (cells[y - count][x - count].getEnemy() == Turn && search_switch[1] == 0) {//相手の駒があったら終了
                                search_switch[1] = 2;
                                Log.d("big_enemy_search", (9 - (x - count)) + "," + (y - count) + cells[y - count][x - count].getName());
                                Log.d("big_enemy_search", "なし 1");
                            } else if (cells[y - count][x - count].getEnemy() == turn) {//自分の駒があったらチェック
                                if (search_switch[1] == 1) {//２回目は終了
                                    search_switch[1] = 2;
                                    through_enemy[1][0] = 0;
                                    through_enemy[1][1] = 0;
                                    through_enemy[1][2] = 0;
                                    Log.d("big_enemy_search", "なし 2");
                                } else {
                                    //１回目はセット
                                    search_switch[1] = 1;
                                    through_enemy[1][0] = y - count;
                                    through_enemy[1][1] = x - count;
                                    through_enemy[1][2] = 5;
                                }
                            } else if (search_switch[1] == 1) {//セット状態で
                                if (cells[y - count][x - count].getEnemy() == turn) {//自分の駒なら終了
                                    search_switch[1] = 2;
                                    through_enemy[1][0] = 0;
                                    through_enemy[1][1] = 0;
                                    through_enemy[1][2] = 0;
                                    Log.d("big_enemy_search", "なし 3");
                                } else if (cells[y - count][x - count].name().equals("HISYA") || cells[y - count][x - count].name().equals("RYU")) {//相手の駒で飛車or龍なら
                                    //セットおけない
                                    Log.d("big_enemy_search", (9 - through_enemy[1][1]) + "," +  through_enemy[1][0] + cells[ through_enemy[1][0]][through_enemy[1][1]].getName() + "は動かせません");
                                    search_switch[1] = 3;
                                }
                            }
                        }
                    }else if(search_switch[1] == 1){
                        through_enemy[1][0] = 0;
                        through_enemy[1][1] = 0;
                        through_enemy[1][2] = 0;
                        search_switch[1] = 2;
                    }

                    //右上
                    if (0 < y - count && x + count < 9) {//右に壁がないか
                        if (search_switch[2] != 2 && search_switch[2] != 3) {//右上
                            if (cells[y - count][x + count].getEnemy() == Turn && search_switch[2] == 0) {//相手の駒があったら終了
                                search_switch[2] = 2;
                                Log.d("big_enemy_search", (9 - (x + count)) + "," + (y - count) + cells[y - count][x + count].getName());
                                Log.d("big_enemy_search", "なし 1");
                            } else if (cells[y - count][x + count].getEnemy() == turn) {//自分の駒があったらチェック
                                if (search_switch[2] == 1) {//２回目は終了
                                    search_switch[2] = 2;
                                    through_enemy[2][0] = 0;
                                    through_enemy[2][1] = 0;
                                    through_enemy[2][2] = 0;
                                    Log.d("big_enemy_search", "なし 2");
                                } else {
                                    //１回目はセット
                                    search_switch[2] = 1;
                                    through_enemy[2][0] = y - count;
                                    through_enemy[2][1] = x + count;
                                    through_enemy[2][2] = 4;
                                    Log.d("big_enemy_search", (9 - through_enemy[2][1]) + "," +  through_enemy[2][0] + cells[ through_enemy[2][0]][through_enemy[2][1]].getName()+" dir:"+through_enemy[2][2] );
                                }
                            } else if (search_switch[2] == 1) {//セット状態で
                                if (cells[y - count][x + count].getEnemy() == turn) {//自分の駒なら終了
                                    search_switch[2] = 2;
                                    through_enemy[2][0] = 0;
                                    through_enemy[2][1] = 0;
                                    through_enemy[2][2] = 0;
                                    Log.d("big_enemy_search", "なし 3");
                                } else if (cells[y - count][x + count].name().equals("HISYA") || cells[y - count][x + count].name().equals("RYU")) {//相手の駒で飛車or龍なら
                                    //セットおけない
                                    Log.d("big_enemy_search", (9 - through_enemy[2][1]) + "," +  through_enemy[2][0] + cells[ through_enemy[2][0]][through_enemy[2][1]].getName() + "は動かせません");
                                    search_switch[2] = 3;
                                }
                            }
                        }
                    }else if(search_switch[2] == 1){
                        through_enemy[2][0] = 0;
                        through_enemy[2][1] = 0;
                        through_enemy[2][2] = 0;
                        search_switch[2] = 2;
                    }

            //左
            if (0 <= x - count) {//左に壁がないか
                if (search_switch[6] != 2 && search_switch[6] != 3) {//左
                    if (cells[y][x - count].getEnemy() == Turn && search_switch[6] == 0) {//相手の駒があったら終了
                        search_switch[6] = 2;
                        Log.d("big_enemy_search", (9 - (x - count)) + "," + (y) + cells[y][x - count].getName());
                        Log.d("big_enemy_search", "なし 1");
                    } else if (cells[y][x - count].getEnemy() == turn) {//自分の駒があったらチェック
                        if (search_switch[6] == 1) {//２回目は終了
                            search_switch[6] = 2;
                            through_enemy[6][0] = 0;
                            through_enemy[6][1] = 0;
                            through_enemy[6][2] = 0;
                            Log.d("big_enemy_search", "なし 2");
                        } else {
                            //１回目はセット
                            search_switch[6] = 1;
                            through_enemy[6][0] = y;
                            through_enemy[6][1] = x - count;
                            through_enemy[6][2] = 7;
                        }
                    } else if (search_switch[6] == 1) {//セット状態で
                        if (cells[y][x - count].getEnemy() == turn) {//自分の駒なら終了
                            search_switch[6] = 2;
                            through_enemy[6][0] = 0;
                            through_enemy[6][1] = 0;
                            through_enemy[6][2] = 0;
                            Log.d("big_enemy_search", "なし 3");
                        } else if (cells[y][x - count].name().equals("HISYA") || cells[y][x - count].name().equals("RYU")) {//相手の駒で飛車or龍なら
                            //セットおけない
                            Log.d("big_enemy_search", (9 - through_enemy[6][1]) + "," +  through_enemy[6][0] + cells[ through_enemy[6][0]][through_enemy[6][1]].getName() + "は動かせません");
                            search_switch[6] = 3;
                        }
                    }
                }
            }else if(search_switch[6] == 1){
            through_enemy[6][0] = 0;
            through_enemy[6][1] = 0;
            through_enemy[6][2] = 0;
            search_switch[6] = 2;
        }

            //右
            if (x + count < 9) {//下に壁がないか
                if (search_switch[7] != 2 && search_switch[7] != 3) {//下
                    if (cells[y][x + count].getEnemy() == Turn && search_switch[7] == 0) {//相手の駒があったら終了
                        search_switch[7] = 2;
                        Log.d("big_enemy_search", (9 - (x + count)) + "," + (y) + cells[y][x + count].getName());
                        Log.d("big_enemy_search", "なし 1");
                    } else if (cells[y][x + count].getEnemy() == turn) {//自分の駒があったらチェック
                        if (search_switch[7] == 1) {//２回目は終了
                            search_switch[7] = 2;
                            through_enemy[7][0] = 0;
                            through_enemy[7][1] = 0;
                            through_enemy[7][2] = 0;
                            Log.d("big_enemy_search", "なし 2");
                        } else {
                            //１回目はセット
                            search_switch[7] = 1;
                            through_enemy[7][0] = y;
                            through_enemy[7][1] = x + count;
                            through_enemy[7][2] = 6;
                        }
                    } else if (search_switch[7] == 1) {//セット状態で
                        if (cells[y][x + count].getEnemy() == turn) {//自分の駒なら終了
                            search_switch[7] = 2;
                            through_enemy[7][0] = 0;
                            through_enemy[7][1] = 0;
                            through_enemy[7][2] = 0;
                            Log.d("big_enemy_search", "なし 3");
                        } else if (cells[y][x + count].name().equals("HISYA") || cells[y][x + count].name().equals("RYU")) {//相手の駒で飛車or龍なら
                            //セットおけない
                            Log.d("big_enemy_search", (9 - through_enemy[7][1]) + "," +  through_enemy[7][0] + cells[ through_enemy[7][0]][through_enemy[7][1]].getName() + "は動かせません");
                            search_switch[7] = 3;
                        }
                    }
                }
            }else if(search_switch[7] == 1){
                through_enemy[7][0] = 0;
                through_enemy[7][1] = 0;
                through_enemy[7][2] = 0;
                search_switch[7] = 2;
            }

            //下
            if (y + count < 10) {//下に壁がないか
                if (search_switch[3] != 2 && search_switch[3] != 3) {//下
                    if (cells[y + count][x].getEnemy() == Turn && search_switch[3] == 0) {//相手の駒があったら終了
                        search_switch[3] = 2;
                        Log.d("big_enemy_search", (9 - (x)) + "," + (y + count) + cells[y + count][x].getName());
                        Log.d("big_enemy_search", "なし 1");
                    } else if (cells[y + count][x].getEnemy() == turn) {//自分の駒があったらチェック
                        if (search_switch[3] == 1) {//２回目は終了
                            search_switch[3] = 2;
                            through_enemy[3][0] = 0;
                            through_enemy[3][1] = 0;
                            through_enemy[3][2] = 0;
                            Log.d("big_enemy_search", "なし 2");
                        } else {
                            //１回目はセット
                            search_switch[3] = 1;
                            through_enemy[3][0] = y + count;
                            through_enemy[3][1] = x;
                            through_enemy[3][2] = 0;
                        }
                    } else if (search_switch[3] == 1) {//セット状態で
                        if (cells[y + count][x].getEnemy() == turn) {//自分の駒なら終了
                            search_switch[3] = 2;
                            through_enemy[3][0] = 0;
                            through_enemy[3][1] = 0;
                            through_enemy[3][2] = 0;
                            Log.d("big_enemy_search", "なし 3");
                        } else if (cells[y + count][x].name().equals("HISYA") || cells[y + count][x].name().equals("RYU")) {//相手の駒で飛車or龍なら
                            //セットおけない
                            Log.d("big_enemy_search", (9 - through_enemy[3][1]) + "," + through_enemy[3][0] + cells[through_enemy[3][0]][through_enemy[3][1]].getName() + "は動かせません");
                            search_switch[3] = 3;

                        }
                    }
                }
            }else if(search_switch[3] == 1){
                    through_enemy[3][0] = 0;
                    through_enemy[3][1] = 0;
                    through_enemy[3][2] = 0;
                    search_switch[3] = 2;
            }

                //左下
                if (y + count < 10 && 0 <= x - count) {//左に壁がないか
                    if (search_switch[4] != 2 && search_switch[4] != 3) {//左
                        if (cells[y + count][x - count].getEnemy() == Turn && search_switch[4] == 0) {//相手の駒があったら終了
                            search_switch[4] = 2;
                            Log.d("big_enemy_search", (9 - (x - count)) + "," + (y + count) + cells[y + count][x - count].getName());
                            Log.d("big_enemy_search", "なし 1");
                        } else if (cells[y + count][x - count].getEnemy() == turn) {//自分の駒があったらチェック
                            if (search_switch[1] == 1) {//２回目は終了
                                search_switch[4] = 2;
                                through_enemy[4][0] = 0;
                                through_enemy[4][1] = 0;
                                through_enemy[4][2] = 0;
                                Log.d("big_enemy_search", "なし 2");
                            } else {
                                //１回目はセット
                                search_switch[4] = 1;
                                through_enemy[4][0] = y + count;
                                through_enemy[4][1] = x - count;
                                through_enemy[4][2] = 2;
                            }
                        } else if (search_switch[4] == 1) {//セット状態で
                            if (cells[y + count][x - count].getEnemy() == turn) {//自分の駒なら終了
                                search_switch[4] = 2;
                                through_enemy[4][0] = 0;
                                through_enemy[4][1] = 0;
                                through_enemy[4][2] = 0;
                                Log.d("big_enemy_search", "なし 3");
                            } else if (cells[y + count][x - count].name().equals("HISYA") || cells[y + count][x - count].name().equals("RYU")) {//相手の駒で飛車or龍なら
                                //セットおけない
                                Log.d("big_enemy_search", (9 - through_enemy[4][1]) + "," +  through_enemy[4][0] + cells[ through_enemy[4][0]][through_enemy[4][1]].getName() + "は動かせません");
                                search_switch[4] = 3;
                            }
                        }
                    }
                }else if(search_switch[4] == 1){
                    through_enemy[4][0] = 0;
                    through_enemy[4][1] = 0;
                    through_enemy[4][2] = 0;
                    search_switch[4] =2;
                }

                //右下
                if (y + count < 10 && x + count < 9) {//右に壁がないか
                    if (search_switch[5] != 2 && search_switch[5] != 3) {//右下
                        if (cells[y + count][x + count].getEnemy() == Turn && search_switch[5] == 0) {//相手の駒があったら終了
                            search_switch[5] = 2;
                            Log.d("big_enemy_search", (9 - (x + count)) + "," + (y + count) + cells[y + count][x + count].getName());
                            Log.d("big_enemy_search", "なし 1");
                        } else if (cells[y + count][x + count].getEnemy() == turn) {//自分の駒があったらチェック
                            if (search_switch[5] == 1) {//２回目は終了
                                search_switch[5] = 2;
                                through_enemy[5][0] = 0;
                                through_enemy[5][1] = 0;
                                through_enemy[5][2] = 0;
                                Log.d("big_enemy_search", "なし 2");
                            } else {
                                //１回目はセット
                                search_switch[5] = 1;
                                through_enemy[5][0] = y + count;
                                through_enemy[5][1] = x + count;
                                through_enemy[5][2] = 1;
                            }
                        } else if (search_switch[5] == 1) {//セット状態で
                            if (cells[y + count][x + count].getEnemy() == turn) {//自分の駒なら終了
                                search_switch[5] = 2;
                                through_enemy[5][0] = 0;
                                through_enemy[5][1] = 0;
                                through_enemy[5][2] = 0;
                                Log.d("big_enemy_search", "なし 3");
                            } else if (cells[y + count][x + count].name().equals("HISYA") || cells[y + count][x + count].name().equals("RYU")) {//相手の駒で飛車or龍なら
                                //セットおけない
                                Log.d("big_enemy_search", (9 - through_enemy[5][1]) + "," +  through_enemy[5][0] + cells[ through_enemy[5][0]][through_enemy[5][1]].getName() + "は動かせません");
                                search_switch[5] = 3;
                            }
                        }
                    }
                }else if(search_switch[5] == 1){
                    through_enemy[5][0] = 0;
                    through_enemy[5][1] = 0;
                    through_enemy[5][2] = 0;
                    search_switch[5] = 2;
                }

            }

    }

//貫通比較メソッド
    /*
    y:縦　x:横　i:方向()
    もし取ることができれば1を返し
    出来なければ0を返す
     */
    public int through_search(int y,int x,int dir){
        for(int i =0;i<8;i++){
            if(through_enemy[i][0]==y && through_enemy[i][1]==x && through_enemy[i][2]!=dir){
                Log.d("through_search","駒を取れないので返します");
                return 0;
            }
        }
        return 1;
    }

    public int getPiecesCount(int Turn) {
        int count = 0;
        if (Turn == 1) {
            for (int i = 2; i <= 8; i++) {
                count += cells[10][i].getCapture_count();
            }
            return count;
        }else{
            for (int i = 0; i <= 6; i++) {
                count += cells[0][i].getCapture_count();
            }
            return count;
        }
    }


}
