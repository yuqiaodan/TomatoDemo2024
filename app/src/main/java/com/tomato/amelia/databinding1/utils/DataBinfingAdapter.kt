package com.tomato.amelia.databinding1.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.tomato.amelia.R

/**
 * author: created by yuqiaodan on 2023/11/16 17:22
 * description:
 */

/**
 * BindingAdapter必须是静态方法或者全局方法
 * 静态方法：java需要写为static方法
 * kotlin需要写在伴生对象中
 * 全局方法:写在class外
 * BindingAdapter 可以传递多个参数 任意类型
 * 一个方法可以被多个同场景的view通用
 * */
@BindingAdapter("finalPrice", "reducePrice")
fun setSellPrice(tv: TextView, finalPrice: String, reducePrice: Float) {
    tv.text = tv.context.getString(R.string.sell_price, "${finalPrice.toFloat() - reducePrice}")

}