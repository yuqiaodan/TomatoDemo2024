package com.tomato.amelia.utils

import android.content.Context
import android.os.Vibrator
import android.widget.Toast
import com.tomato.amelia.App

/**
 * author: created by tomato on 2024/2/4 18:02
 * description:
 */
object MyAppUtils {

    fun showToast(context: Context, msg:String){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
    }


    /**
     * 震动
     * */
    fun vibrate(milliseconds: Long) {
        val vibrator: Vibrator =  App.instance.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(milliseconds)
    }

}