package com.tomato.amelia.customviewstudy.viewcomposite

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.tomato.amelia.R
import com.tomato.amelia.databinding.LayoutViewInputNumberBinding

/**
 * author: created by tomato on 2024/1/17 11:22
 * description:
 * 自定义组合view 例子1 数字输入button
 *
 * 自定义组合view步骤：
 * 1.获取相关属性，定义相关属性
 * 2.加载组合view
 * 3.定义功能接口
 * 4.处理事件
 *
 */
class InputNumberView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private lateinit var contentBinding: LayoutViewInputNumberBinding

    private var mCurrentNumber = 0

    private var numberChangeListener : NumberChangeListener?=null

    var mDefaultValue = 0
    var mMax = 0
    var mMin = 0
    var mStep = 1
    var mDisable = false
    var mBtnBackground = -1

    init {
        //第一步 获取属性
        initAttrs(context,attrs)
        //第二步 初始化view 将属性值填充到view
        initView(context)
        //第三步 添加事件
        initEvent()
    }

    private fun initAttrs(context: Context,attrs: AttributeSet? = null){
        //获取相关attr属性
        val type = context.obtainStyledAttributes(attrs, R.styleable.InputNumberView)
        mDefaultValue = type.getInt(R.styleable.InputNumberView_defaultValue, 0)
        mMax = type.getInt(R.styleable.InputNumberView_max, 0)
        mMin = type.getInt(R.styleable.InputNumberView_min, 0)
        mStep = type.getInt(R.styleable.InputNumberView_step, 1)
        mDisable = type.getBoolean(R.styleable.InputNumberView_disable, false)
        mBtnBackground = type.getResourceId(R.styleable.InputNumberView_btnBackground, -1)
        Log.d("InputNumberViewTag", "InputNumberView mDefaultValue:$mDefaultValue mMax:$mMax mMin:$mMin mStep:$mStep mDisable:$mDisable mBtnBackground:$mBtnBackground")
        //TypedArray一定要回收
        type.recycle()
    }

    private fun initView(context: Context?) {
        /**
         * attachToParent 表示是否添加到父布局
         * attachToParent = true 等价于  attachToParent = false时 手动addView
         * */
        contentBinding = LayoutViewInputNumberBinding.inflate(LayoutInflater.from(context), this, true)
        //每个属性值都用起来
        mCurrentNumber = mDefaultValue

        if(mBtnBackground!=-1){
            contentBinding.btnAdd.setBackgroundResource(mBtnBackground)
            contentBinding.btnSub.setBackgroundResource(mBtnBackground)
        }
        updateText()

    }

    private fun initEvent(){
        contentBinding.btnAdd.setOnClickListener {
            mCurrentNumber+=mStep
            if(mCurrentNumber>=mMax){
                mCurrentNumber= mMax
            }
            updateText()

        }
        contentBinding.btnSub.setOnClickListener {
            mCurrentNumber-=mStep
            if(mCurrentNumber<mMin){
                mCurrentNumber= mMin
            }
            updateText()
        }
    }


    private fun updateText() {
        contentBinding.btnAdd.isEnabled = (mCurrentNumber!=mMax)
        contentBinding.btnSub.isEnabled = (mCurrentNumber!=mMin)
        contentBinding.tvNumber.text = mCurrentNumber.toString()
        numberChangeListener?.onNumberChanged(mCurrentNumber)
    }

    interface NumberChangeListener{
        fun onNumberChanged(number:Int)
    }

    /**
     * 业务层调用 设置当前值
     * */
    fun setNumber(value: Int) {
        mCurrentNumber = value
        updateText()
    }

    /**
     * 添加监听
     * */
    fun setNumberListener(listener: NumberChangeListener){
        this.numberChangeListener=listener
    }

    /**
     * 业务层调用 设置步长
     * */
    fun setStep(step:Int){
        this.mStep=step
    }

    /**
     * 业务层调用 设置按钮背景
     * */
    fun setBtnBackground(res:Int){
        this.mBtnBackground = res
        if(mBtnBackground!=-1){
            contentBinding.btnAdd.setBackgroundResource(mBtnBackground)
            contentBinding.btnSub.setBackgroundResource(mBtnBackground)
        }
    }
}