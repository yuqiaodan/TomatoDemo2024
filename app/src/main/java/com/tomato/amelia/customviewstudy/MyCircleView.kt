package com.tomato.amelia.customviewstudy

import android.content.Context
import android.util.AttributeSet
import android.view.View


/**
 * author: created by yuqiaodan on 2023/11/17 10:40
 * description:
 * 自定义View步骤：
 * 1.判断
 */
class MyCircleView:View{

    constructor(context: Context?) : super(context) {
        initView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }
    //只需要前三个构造函数就可以了
    //constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr,defStyleRes)

    private fun initView(context: Context?) {


    }


}

