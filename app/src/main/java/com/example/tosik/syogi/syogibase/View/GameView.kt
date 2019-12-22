package com.example.syogibase.View

import android.app.AlertDialog
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import com.example.syogibase.Contact.GameViewContact
import com.example.syogibase.R
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf


class GameView(private val activity:GameActivity, context: Context, width:Int, height:Int): View(context), GameViewContact.View,
    KoinComponent {

    private val presenter:GameViewContact.Presenter by inject{ parametersOf(this) }
    private lateinit var canvas:Canvas
    private val paint:Paint = Paint()

    private val bw:Float = width.toFloat()//将棋盤の幅
    private val bh:Float = width.toFloat()//将棋盤の高さ
    private val cw:Float = bw/9//１マスの幅
    private val ch:Float = bh/9//１マスの高さ

    //onCreat
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas
        presenter.drawView()
    }

    //指した時の動作
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val c = (event.x / cw).toInt()
        val r = (event.y / ch).toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN ->{}
            MotionEvent.ACTION_UP ->{
                presenter.onTouchEvent(c,r)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {}
            MotionEvent.ACTION_CANCEL -> { }
        }

        return true
    }

    //将棋盤描画
    override fun drawBoard(){
        //盤面セット
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.free_grain_sub)
        val rect1 = Rect(0, ch.toInt(), bw.toInt(), bh.toInt() + cw.toInt())
        val rect2 = Rect(0, ch.toInt(), bw.toInt(), bh.toInt() + cw.toInt())
        canvas.drawBitmap(bmp, rect1, rect2, paint)
        //駒台セット
        paint.color = Color.rgb(251, 171, 83)
        canvas.drawRect(cw * 2, bw + cw, bw, bh + cw * 2.toFloat(), paint)
        canvas.drawRect(0f, 0f, bw - cw * 2.toFloat(), cw, paint)
        //罫線セット
        paint.color = Color.rgb(40, 40, 40)
        paint.strokeWidth = 2f
        for (i in 0 until 9) canvas.drawLine(cw * (i + 1).toFloat(), cw, cw * (i + 1).toFloat(), bh + cw, paint)
        for (i in 0 until 9) canvas.drawLine(0f, ch * (i + 1), bw, ch * (i + 1), paint)
    }

    //後手の駒描画
    override fun drawWhitePiece(name:String, i:Int, j:Int){
        paint.textSize = cw/2
        canvas.save()
        canvas.rotate(180f, (cw * i) + cw /2, ch*2 + (ch * j) - cw / 2)
        canvas.drawText(name, (cw*i)+cw/5, ch*2+(ch*j)-cw/4, paint)
        canvas.restore()
    }

    //先手の駒描画
    override fun drawBlackPiece(name:String, i:Int, j:Int){
        paint.textSize = cw/2
        canvas.drawText(name, (cw*i)+cw/5, ch*2+(ch*j)-cw/4, paint)
    }
    //先手の持ち駒描画
    override fun drawHoldPirceBlack(nameJP:String,stock:Int, count:Int){
        paint.textSize = cw / 2
        canvas.drawText(nameJP, (cw*(count+2))+cw/5, ch*2+(ch*9)-cw/4, paint)
        paint.textSize = cw / 5
        canvas.drawText(stock.toString(), (cw*(count+2))+cw*3/4, ch*2+(ch*9)-cw/8, paint)
    }

    //後手の持ち駒描画
    override fun drawHoldPirceWhite(nameJP:String, stock:Int, count:Int){
        canvas.save()
        canvas.rotate(180f, cw*(7-count), ch-cw/2)
        paint.textSize = cw / 2
        canvas.drawText(nameJP, (cw*(7-count))+cw/5, ch-cw/4, paint)
        paint.textSize = cw / 5
        canvas.drawText(stock.toString(), (cw*(7-count))+cw*3/4, ch-cw/8, paint)
        canvas.restore()

    }

    //ヒント描画メソッド
    override fun drawHint(x:Int, y:Int){
        paint.color = (Color.argb(200, 255, 255, 0))
        canvas.drawCircle((cw*x + cw/2 ), (ch*(y+1) + ch/2), (bw/9 * 0.46).toFloat(), paint)
        paint.color = Color.rgb(40, 40, 40)
    }

    //成り判定ダイアログ
    override fun showDialog(){
        val alertBuilder = AlertDialog.Builder(context).setCancelable(false)
        alertBuilder.setMessage("成りますか？")
        alertBuilder.setPositiveButton("はい") { dialog, which ->
            presenter.evolutionPiece(true)
            invalidate()
        }
        alertBuilder.setNegativeButton("いいえ") { dialog, which ->
            presenter.evolutionPiece(false)
            invalidate()
        }
        alertBuilder.create().show()
    }

    //終了ダイアログ表示
    override fun gameEnd(){
        activity.gameEnd()
    }

}