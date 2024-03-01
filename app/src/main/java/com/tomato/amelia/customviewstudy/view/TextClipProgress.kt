package com.tomato.amelia.customviewstudy.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.os.Build
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
    var text: String = "Hello World!"

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

    val clipOutRectF = RectF()
    val clipOutPath = Path()

    private fun initPaint() {
        textDefaultPaint = Paint()
        textDefaultPaint.color = mTextColorDefault
        textDefaultPaint.textSize = mTextSize
        textDefaultPaint.style = Paint.Style.FILL

        textProgressPaint = Paint()
        textProgressPaint.color = mTextColorProgress
        textProgressPaint.textSize = mTextSize
        textDefaultPaint.style = Paint.Style.FILL


    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /**
         * 思路1：先绘制进度条轨道底色及文字，然后剪切canvas为需要的长度 并绘制进度条本体及文字
         *
         * 思路2：先绘制进度条本体和文字，然后把画布需要漏出来的部分剪切掉，并在剩余的画布上绘制进度条轨道底色及文字
         *
         * 实现起来差不多  唯一区别就是剪切画布采用的方法
         * 思路1是按部就班直接先绘制轨道再绘制进度条
         *  canvas.clipPath(clipOutPath)
         *
         * 思路2是先绘制进度条，再剪切掉画布漏出进度条颜色
         *  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         *             canvas.clipOutPath(clipOutPath)
         *         } else {
         *             canvas.clipPath(clipOutPath, Region.Op.DIFFERENCE)
         *         }
         *
         * 采用思路1实现
         * */





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
        val textHeight = fontMetrics.descent - fontMetrics.ascent
        val textOffset = textHeight / 2 - fontMetrics.descent
        canvas.translate(measuredWidth / 2f, measuredHeight / 2f)

        /**
         * 在 canvas.drawText("123", x, y, paint); 方法中，x 和 y 的坐标指的不是文字的左上角，而是基线 (baseline) 的起始位置
         * */

        canvas.drawText(text, -textWidth / 2, textOffset, textProgressPaint)
        canvas.restore()

        /***
         * 剪切画布然后绘制进度条path 覆盖层
         * */


        canvas.save()
        clipOutRectF.left = 0f
        clipOutRectF.top = 0f
        clipOutRectF.bottom = measuredHeight.toFloat()
        clipOutRectF.right = (mProgress / mMaxProgress.toFloat()) * measuredWidth.toFloat()

        val r = measuredHeight/2f
        clipOutPath.addRoundRect(clipOutRectF,  floatArrayOf(0f, 0f, r, r, r, r, 0f, 0f), Path.Direction.CW)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutPath(clipOutPath)
        } else {
            canvas.clipPath(clipOutPath, Region.Op.DIFFERENCE)
        }

        //绘制进度条轨道颜色
        canvas.drawColor(mProgressPathColor)
        //移到居中位置方便绘制文字 默认居中
        canvas.translate(measuredWidth / 2f, measuredHeight / 2f)
        canvas.drawText(text, -textWidth / 2, textOffset, textDefaultPaint)
        canvas.restore()


    }


    private fun dip2px(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density + 0.5f
    }
}