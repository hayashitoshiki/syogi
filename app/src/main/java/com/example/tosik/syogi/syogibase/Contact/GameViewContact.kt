package com.example.syogibase.Contact

interface GameViewContact {

    interface View  {
        //将棋盤描画
        fun drawBoard()
        //先手の駒描画
        fun drawBlackPiece(name:String, i:Int, j:Int)
        //後手の駒描画
        fun drawWhitePiece(name:String, i:Int, j:Int)
        //ヒント描画
        fun drawHint(i:Int,j:Int)
        //成るか判断するダイアログ生成
        fun showDialog()
        //先手の持ち駒セット
        fun drawHoldPirceBlack(nameJP:String, stock:Int, count:Int)
        //後手の持ち駒セット
        fun drawHoldPirceWhite(nameJP:String, stock:Int, count:Int)
        //詰んだことをActivityに知らせる
        fun gameEnd()
    }

    interface Presenter{
        //タッチイベントロジック
        fun onTouchEvent(x:Int,y:Int)
        //描画ロジック
        fun drawView()
        //成り
        fun evolutionPiece(bool:Boolean)
    }
}