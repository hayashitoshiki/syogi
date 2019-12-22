package com.example.syogibase.Model.Data

//将棋の駒格納クラス


enum class Piece(val nameJP:String) {
    None(""),
    FU("歩"),
    TO("と"),
    KYO("香"),
    N_KYO("杏"),
    KEI("桂"),
    N_KEI("圭"),
    GIN("銀"),
    N_GIN("全"),
    KIN("金"),
    HISYA("飛"),
    RYU("龍"),
    KAKU("角"),
    UMA("馬"),
    OU("王"),
    GYOKU("玉");

    //駒の動き方
    fun getMove():Array<Array<PieceMove>>{
        when(this){
            FU  -> return arrayOf(arrayOf(PieceMove(0,-1)))
            KEI -> return arrayOf(arrayOf(PieceMove(1,-2)), arrayOf(PieceMove(-1,-2)))
            KYO -> return arrayOf(arrayOf(PieceMove(0,-1), PieceMove(0,-2),PieceMove(0,-3), PieceMove(0,-4),PieceMove(0,-5), PieceMove(0,-6),PieceMove(0,-7), PieceMove(0,-8)))
            GIN  ->return arrayOf(arrayOf(PieceMove(-1,-1)), arrayOf(PieceMove(0,-1)),arrayOf( PieceMove(1,-1)),
                arrayOf(PieceMove(-1,1)),  arrayOf(PieceMove(1,1)))
            KIN,TO,N_KYO,N_KEI,N_GIN  ->return arrayOf(arrayOf(PieceMove(-1,-1)), arrayOf(PieceMove(0,-1)), arrayOf(PieceMove(1,-1)),
                arrayOf(PieceMove(-1,0)),  arrayOf(PieceMove(1,0)),
                arrayOf(PieceMove(0,1) ))
            OU,GYOKU  -> return arrayOf(arrayOf(PieceMove(-1,-1)), arrayOf(PieceMove(-1,0)), arrayOf(PieceMove(-1,1)),
                arrayOf(PieceMove(0,-1)),  arrayOf(PieceMove(0,1)),
                arrayOf(PieceMove(1,-1)),  arrayOf(PieceMove(1,0)),  arrayOf(PieceMove(1,1)))
            HISYA  ->return arrayOf(arrayOf(PieceMove(0,1), PieceMove(0,2),PieceMove(0,3), PieceMove(0,4),PieceMove(0,5), PieceMove(0,6),PieceMove(0,7), PieceMove(0,8)),
                arrayOf(PieceMove( 1,0), PieceMove( 2,0),PieceMove( 3,0), PieceMove( 4,0),PieceMove( 5,0), PieceMove( 6,0),PieceMove( 7,0), PieceMove( 8,0)),
                arrayOf(PieceMove(-1,0), PieceMove(-2,0),PieceMove(-3,0), PieceMove(-4,0),PieceMove(-5,0), PieceMove(-6,0),PieceMove(-7,0), PieceMove(-8,0)),
                arrayOf(PieceMove(0,-1), PieceMove(0,-2),PieceMove(0,-3), PieceMove(0,-4),PieceMove(0,-5), PieceMove(0,-6),PieceMove(0,-7), PieceMove(0,-8)))
            RYU ->return arrayOf(arrayOf(PieceMove(0,1), PieceMove(0,2),PieceMove(0,3), PieceMove(0,4),PieceMove(0,5), PieceMove(0,6),PieceMove(0,7), PieceMove(0,8)),
                arrayOf(PieceMove( 1,0), PieceMove( 2,0),PieceMove( 3,0), PieceMove( 4,0),PieceMove( 5,0), PieceMove( 6,0),PieceMove( 7,0), PieceMove( 8,0)),
                arrayOf(PieceMove(-1,0), PieceMove(-2,0),PieceMove(-3,0), PieceMove(-4,0),PieceMove(-5,0), PieceMove(-6,0),PieceMove(-7,0), PieceMove(-8,0)),
                arrayOf(PieceMove(0,-1), PieceMove(0,-2),PieceMove(0,-3), PieceMove(0,-4),PieceMove(0,-5), PieceMove(0,-6),PieceMove(0,-7), PieceMove(0,-8)),
                arrayOf(PieceMove(-1,-1)), arrayOf(PieceMove(-1, 1)),arrayOf( PieceMove(1,-1)), arrayOf(PieceMove(1,1)))
            KAKU  ->return arrayOf(arrayOf(PieceMove(1,1), PieceMove(2,2),PieceMove(3,3), PieceMove(4,4),PieceMove(5,5), PieceMove(6,6),PieceMove(7,7), PieceMove(8,8)),
                arrayOf(PieceMove( 1,-1), PieceMove( 2,-2),PieceMove( 3,-3), PieceMove( 4,-4),PieceMove( 5,-5), PieceMove( 6,-6),PieceMove( 7,-7), PieceMove( 8,-8)),
                arrayOf(PieceMove(-1,-1), PieceMove(-2,-2),PieceMove(-3,-3), PieceMove(-4,-4),PieceMove(-5,-5), PieceMove(-6,-6),PieceMove(-7,-7), PieceMove(-8,-8)),
                arrayOf(PieceMove(-1,1), PieceMove( -2, 2),PieceMove(-3, 3), PieceMove(-4, 4),PieceMove(-5, 5), PieceMove(-6, 6),PieceMove(-7, 7), PieceMove(-8, 8)))
            UMA  ->return arrayOf(arrayOf(PieceMove(1,1), PieceMove(2,2),PieceMove(3,3), PieceMove(4,4),PieceMove(5,5), PieceMove(6,6),PieceMove(7,7), PieceMove(8,8)),
                arrayOf(PieceMove( 1,-1), PieceMove( 2,-2),PieceMove( 3,-3), PieceMove( 4,-4),PieceMove( 5,-5), PieceMove( 6,-6),PieceMove( 7,-7), PieceMove( 8,-8)),
                arrayOf(PieceMove(-1,-1), PieceMove(-2,-2),PieceMove(-3,-3), PieceMove(-4,-4),PieceMove(-5,-5), PieceMove(-6,-6),PieceMove(-7,-7), PieceMove(-8,-8)),
                arrayOf(PieceMove(-1,1), PieceMove( -2, 2),PieceMove(-3, 3), PieceMove(-4, 4),PieceMove(-5, 5), PieceMove(-6, 6),PieceMove(-7, 7), PieceMove(-8, 8)),
                arrayOf(PieceMove(-1,0)), arrayOf(PieceMove(1, 0)),arrayOf( PieceMove(0,-1)), arrayOf(PieceMove(0,1)))
            else ->return arrayOf(arrayOf(PieceMove(0,0)))
        }
    }

    //進化取得
    fun evolution():Piece{
        when(this){
            FU    ->return TO
            KEI   ->return N_KYO
            KYO   ->return N_KYO
            GIN   ->return N_GIN
            HISYA ->return RYU
            KAKU  ->return UMA
            else  ->return None
        }
    }

    //成れる駒の分類
    fun findEvolution():Boolean{
        when(this){
            FU,KEI,KYO,GIN,HISYA,KAKU  ->return true
            else -> return false
        }
    }



}