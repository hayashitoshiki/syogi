package com.example.tosik.syogi;


//駒の情報についてのクラス
/*＜１マスの情報＞
    ・駒の有無
    ・先手駒or後手駒
    ・ヒントの表示の有無
*/
public class Cell {

    //盤上ステータス
    public enum E_STATUS{
        None("なし"),
        Fu("歩"),
        HISYA("飛"),
        RYU("龍"),
        KAKU("角"),
        UMA("馬"),
        OU("王"),
        GYOKU("玉"),
        KIN("金"),
        GIN("銀"),
        N_GIN("全"),
        KEI("桂"),
        N_KEI("圭"),
        KYO("香"),
        N_KYO("杏"),
        TO("と"),
        Quiin("姫"),
        Knight("騎"),
        SISI("獅"),
        HINTO(" "),
        HINTO_prev(" ");
        private String name;
        private E_STATUS ( String name) {
            this.name = name;
        }
        private String getName(){
            return name;
        }
        public static final E_STATUS[] Left_right={
                HISYA,
                RYU,
                UMA,
                KIN,
                N_GIN,
                N_KEI,
                N_KYO,
                TO,
                Quiin,
                SISI
        };
        public static final E_STATUS[] Up ={
                OU,
                GYOKU,
                Fu,
                TO,
                KYO,
                N_KYO,
                N_KEI,
                GIN,
                N_GIN,
                KIN,
                KAKU,
                UMA,
                HISYA,
                RYU,
                Quiin,
                SISI
        };
        public static final E_STATUS[] D_Up ={
                OU,
                GYOKU,
                TO,
                N_KYO,
                N_KEI,
                GIN,
                N_GIN,
                KIN,
                KAKU,
                UMA,
                RYU,
                Quiin,
                SISI
        };
        public static final E_STATUS[] Back ={
                OU,
                GYOKU,
                TO,
                N_KYO,
                N_KEI,
                N_GIN,
                KIN,
                UMA,
                HISYA,
                RYU,
                Quiin,
                SISI
        };
        public static final E_STATUS[] D_Back ={
                OU,
                GYOKU,
                GIN,
                KAKU,
                UMA,
                RYU,
                Quiin,
                SISI
        };

    }

    //駒台
    private int capture_switch;//0:なし 1:あり
    private int capture_count;//その駒の持駒の数


    private float width;
    private float height;
    private int top;
    private int left;
    private E_STATUS status = E_STATUS.None;
    private int turn;//0:駒なし 1:先手 2:後手
    private int hint; //ヒント表示0:表示なし 1:ヒント表示


    public void setWidth(float width) {
        this.width = width;
    }
    public float getWidth() {
        return width;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    public float getHeight() {
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
    public int getEnemy(){return turn;}
    public void setEnemy(int Turn){this.turn = Turn;};
    public int getHint(){return hint;}
    public void setHint(int hint){this.hint = hint;}
    public void setCapture_switch(int capture_switch){this.capture_switch = capture_switch;}
    public int getCapture_switch(){return this.capture_switch;}
    public void setCapture_count(int capture_count){this.capture_count = capture_count;}
    public void addCapture_count(int capture_count){this.capture_count += capture_count;}
    public void reduCapture_count(int capture_count){this.capture_count -= capture_count;}
    public int getCapture_count(){return this.capture_count;}

    //横
    public float getCx(){
        return (float) ((float)this.left + (float)this.width/2.0);
    }

    //縦
    public float getCy(){
        return (float) ((float)this.top + (float)this.height/2.0);
    }

    public void setStatus(String status) {
        this.status = E_STATUS.valueOf(status);
    }

    public E_STATUS getStatus() {//switch文の場合
        return status;
    }
    public String getName() {//if文の場合
        return status.getName();
    }
    public String name(){
        return status.name();
    }

}
