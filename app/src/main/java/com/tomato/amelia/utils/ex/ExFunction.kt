package com.tomato.amelia.utils.ex

import android.content.Context
import androidx.core.text.HtmlCompat
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.Random

/**
 * author: created by yuqiaodan on 2023/1/17 15:06
 * description: 拓展函数
 */


/**
 * ----------------全局通用拓展方法----------------
 * */

/**
 * 获取随机数
 * 闭区间 [min,max]
 * */
fun getRandom(min: Int, max: Int): Int {
    return Random().nextInt(max - min + 1) + min
}


/**
 * ----------------Context拓展方法----------------
 * */

//dip转px
fun Context.dip2px(dp: Float): Float {
    return dp * resources.displayMetrics.density + 0.5f
}

fun Context.px2dip(px: Float): Float {
    return px / resources.displayMetrics.density + 0.5f
}


/**
 * ----------------String拓展方法----------------
 * */
//String转为Html样式
fun String.toHtmlText(): CharSequence {
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)
}


/**
 * ----------------其他拓展方法----------------
 * */


/**
 * double保留两位小数
 * toFtd
 * To Format Two Decimals
 * 格式化为两位小数
 * */
fun Double.toFtd(): String {
    return DecimalFormat("0.00").format(this)
}


fun Float.toFtd(): String {
    return DecimalFormat("0.00").format(this)
}

/**
 * 现金值展示,解决小数位数多了后使用科学计数法展示的问题
 */
fun Double.cashValue(): String {
    val df = DecimalFormat("#.########################################")
    return df.format(this)
}

fun Float.cashValue(): String {
    return BigDecimal(this.toString()).toString()
}

