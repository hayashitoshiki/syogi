package com.example.syogibase.View

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.example.syogibase.R

//勝った時の勝敗表示View
class WinLoseModal(context: Context, private val turn: Int) : View(context) {
    private var mas: Float = 0.toFloat()//ひとまず分の大きさメモ

    init {
        isFocusable = true
    }

    override fun onDraw(canvas: Canvas) {
        mas = (width / 9).toFloat()
        super.onDraw(canvas)
        val paint = Paint()
        paint.textSize = mas * 3


        if (turn == 2) {
            canvas.save()

            var text_Width = paint.measureText("Win")
            paint.textSkewX = -0.25f
            paint.color = Color.rgb(255, 0, 0)
            canvas.rotate(180f, (width / 2).toFloat(), mas * 2)
            canvas.drawText("Win", width / 2 - text_Width / 2, mas * 2, paint)
            canvas.restore()

            text_Width = paint.measureText("Lose")
            paint.textSkewX = -0.25f
            paint.color = Color.rgb(0, 0, 255)
            canvas.drawText("Lose", width / 2 - text_Width / 2, mas * 15, paint)
        } else {
            canvas.save()

            var text_Width = paint.measureText("Lose")
            paint.textSkewX = -0.25f
            paint.color = Color.rgb(0, 0, 255)
            canvas.rotate(180f, (width / 2).toFloat(), mas * 2)
            canvas.drawText("Lose", width / 2 - text_Width / 2, mas * 2, paint)
            canvas.restore()

            text_Width = paint.measureText("Win")
            paint.textSkewX = -0.25f
            paint.color = Color.rgb(255, 0, 0)
            canvas.drawText("Win", width / 2 - text_Width / 2, mas * 15, paint)
        }
    }

    //指した時の動作
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return true
    }
}
