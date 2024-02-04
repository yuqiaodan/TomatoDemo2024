package com.tomato.amelia.customviewstudy

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.tomato.amelia.databinding.LayoutViewInputNumberBinding

/**
 * author: created by tomato on 2024/1/17 11:22
 * description:
 * 自定义view demo1 自定义组合view 数字输入button
 */
class InputNumberView : RelativeLayout {


    private lateinit var contentBinding: LayoutViewInputNumberBinding

    private var mCurrentNumber = 0

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
    //constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun initView(context: Context?) {
        /**
         * attachToParent 表示是否添加到父布局
         * attachToParent = true 等价于  attachToParent = false时 手动addView
         * */
        contentBinding = LayoutViewInputNumberBinding.inflate(LayoutInflater.from(context), this, true)

        contentBinding.btnAdd.setOnClickListener {
            mCurrentNumber++
            updateText()

        }
        contentBinding.btnSub.setOnClickListener {
            mCurrentNumber--
            updateText()
        }


    }


    fun setNumber(value: Int) {
        mCurrentNumber = value
        updateText()

    }

    private fun updateText() {
        contentBinding.tvNumber.text = mCurrentNumber.toString()
    }
}