package com.example.syogibase.DI

import android.app.Application
import com.example.syogibase.Contact.GameViewContact
import com.example.syogibase.Model.BoardRepository
import com.example.syogibase.Presenter.GameLogicPresenter
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(applicationContext)
            modules(module)
        }
    }

    // Koinモジュール
    private val module: Module = module  {
        factory <GameViewContact.Presenter>{ (v: GameViewContact.View) -> GameLogicPresenter(v,get()) }
        factory { BoardRepository() }
    }


}