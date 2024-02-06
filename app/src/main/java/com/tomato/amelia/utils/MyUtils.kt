package com.tomato.amelia.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Vibrator
import android.view.View
import android.widget.Toast
import com.tomato.amelia.App

/**
 * author: created by tomato on 2024/2/4 18:02
 * description:
 */
object MyUtils {

    //dip转px
    fun dip2px(dp: Float): Float {
        val context = App.instance.applicationContext
        return dp * context.resources.displayMetrics.density + 0.5f
    }

    //px转dip
    fun px2dip(px: Float): Float {
        val context = App.instance.applicationContext
        return px / context.resources.displayMetrics.density + 0.5f
    }


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

    /**
     * 根据view创建一个bitmap
     * @param view view
     * @param width 期望宽度 px
     * @param height 期望高度 px
     */
    fun createViewBitmap(view: View, width: Int, height: Int): Bitmap {
        //指定宽高和布局参数
        view.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight);
        // 创建一个对应View大小的bitmap
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


}