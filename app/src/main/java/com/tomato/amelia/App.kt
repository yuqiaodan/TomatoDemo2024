package com.tomato.amelia

import android.app.Application
import com.tencent.mmkv.MMKV

/**
 * author: created by yuqiaodan on 2023/11/6 11:22
 * description:
 */
class App:Application() {
    companion object {
        lateinit var instance: App
    }

    init {
        instance = this
    }
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
    }
}