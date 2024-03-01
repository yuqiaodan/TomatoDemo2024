package com.tomato.amelia.customviewstudy.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * author: created by tomato on 2024/3/1 16:27
 * description:
 */
class TextClipProgress @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    //文字内容
    var text: String = "进度条测试123123"

    //文字大小
    var mTextSize = dip2px(context, 30f)

    //默认文字颜色
    val mTextColorDefault = Color.parseColor("#000000")

    //进度条中的文字颜色
    val mTextColorProgress = Color.parseColor("#FFFFFF")

    //最大进度
    var mMaxProgress: Int = 100

    //当前进度
    var mProgress: Int = 50

    //进度条颜色
    var mProgressColor = Color.parseColor("#88CCEE")

    //进度条轨道颜色
    var mProgressPathColor = Color.parseColor("#F9F8EB")

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        initPaint()
    }

    lateinit var textDefaultPaint: Paint

    lateinit var textProgressPaint: Paint

    val clipRectF = RectF()

    private fun initPaint() {
        textDefaultPaint = Paint()
        textDefaultPaint.color = mTextColorDefault

        textDefaultPaint.textSize = mTextSize

        textProgressPaint = Paint()
        textProgressPaint.color = mTextColorProgress

        textProgressPaint.textSize = mTextSize

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /***
         * 绘制进度条底色 和文字
         * */
        //绘制进度条颜色
        canvas.drawColor(mProgressColor)
        //移到居中位置方便绘制文字 默认居中
        canvas.save()
        // 获取文字的宽度
        val textWidth = textDefaultPaint.measureText(text)
        // 获取文字的高度
        val fontMetrics = textDefaultPaint.fontMetrics
        val textHeight = fontMetrics.bottom - fontMetrics.top
        canvas.translate(measuredHeight / 2f, measuredWidth / 2f)
        canvas.drawText(text, -textWidth / 2, -textHeight / 2, textProgressPaint)
        canvas.restore()

        /***
         * 剪切画布然后绘制进度条path 覆盖层
         * */
        canvas.save()
        clipRectF.left = (mProgress / mMaxProgress.toFloat()) * measuredWidth.toFloat()
        clipRectF.top = 0f
        clipRectF.bottom = measuredHeight.toFloat()
        clipRectF.right = measuredWidth.toFloat()
        canvas.clipRect(clipRectF)
        //绘制进度条轨道颜色
        canvas.drawColor(mProgressPathColor)
        //移到居中位置方便绘制文字 默认居中
        canvas.translate(measuredHeight / 2f, measuredWidth / 2f)
        canvas.drawText(text, -textWidth / 2, -textHeight / 2, textDefaultPaint)
        canvas.restore()

    }


    private fun dip2px(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density + 0.5f
    }
}