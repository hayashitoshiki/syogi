package com.example.syogibase.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.syogibase.R


class GameActivity : AppCompatActivity() {

    var frame:FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val inflater = getLayoutInflater()

        //画面作成

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        frame = this.findViewById(R.id.frame) as FrameLayout
        val view: View = GameView(this,this, frame!!.width,frame!!.height)
        frame!!.addView(view, 0)
    }

    fun gameEnd(){
        val inflater = getLayoutInflater()
        val viewGroup = this.findViewById(R.id.constraint_layout) as ConstraintLayout
        val view3: View
        view3 = WinLoseModal(this, 1)

        frame!!.addView(view3, 1)
        val view2 = inflater.inflate(R.layout.modal_game_end, viewGroup)
        val animation = AnimationUtils.loadAnimation(this, R.anim.fadein)
        view2.startAnimation(animation)
        view2.setVisibility(View.VISIBLE)
    }

    //決着後の選択ボダン
    fun end(v: View) {
        val intent = Intent(application, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    //もう一度
    fun restart(v: View) {
        val intent = Intent()
        intent.setClass(this, this.javaClass)
        this.startActivity(intent)
        this.finish()
    }
}
