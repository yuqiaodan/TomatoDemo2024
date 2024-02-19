package com.tomato.amelia.customviewstudy.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import com.tomato.amelia.R
import com.tomato.amelia.utils.MyUtils
import kotlin.math.min

/**
 * author: created by tomato on 2024/2/19 19:52
 * description:
 * 自定义View 例子1 自定义View绘制表盘内容
 * 自定义View步骤：
 * 1.获取相关属性，定义相关属性 init 中实现
 * 2.测量自己的宽高
 * 3.创建画笔 根据具体绘制view的内容 按需要创建几支画笔 （也可以用一只画笔 每次绘制不同内容前修改其属性 例如颜色等）
 * 想象为准备n支不同颜色的笔 绘制不同部分。或者准备一支笔，绘制不同部分前确定其颜色。
 * 画笔有很多属性除了颜色外，还有抗锯齿等等设置，笔尖宽度等内容
 * 4.绘制View内容
 *
 */
class WatchFaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    @ColorInt
    //秒针颜色
    private val mSecondColor: Int

    @ColorInt
    //分针颜色
    private val mMinuteColor: Int

    @ColorInt
    //时针颜色
    private val mHourColor: Int

    @ColorInt
    //刻度颜色
    private val mScaleColor: Int

    //背景
    private val mFaceBgId: Int

    //是否显示刻度
    private val mScaleShow: Boolean

    //背景bitmap
    private  var mBackgroundBitmap: Bitmap?=null

    init {
        /**
         * 第一步：获取相关属性，定义相关属性 init 中实现
         * */
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.WatchFaceView)
        mSecondColor = typeArray.getColor(R.styleable.WatchFaceView_secondColor, context.getColor(R.color.watch_red))
        mMinuteColor = typeArray.getColor(R.styleable.WatchFaceView_minuteColor, context.getColor(R.color.watch_yellow))
        mHourColor = typeArray.getColor(R.styleable.WatchFaceView_hourColor, context.getColor(R.color.watch_blue))
        mScaleColor = typeArray.getColor(R.styleable.WatchFaceView_scaleColor, context.getColor(R.color.white))
        mFaceBgId = typeArray.getResourceId(R.styleable.WatchFaceView_faceBg, -1)
        mScaleShow = typeArray.getBoolean(R.styleable.WatchFaceView_scaleShow, true)

        Log.d(
            "WatchFaceView",
            "mSecondColor:$mSecondColor\n" +
                    "mMinuteColor:$mMinuteColor\n" +
                    "mHourColor:$mHourColor\n" +
                    "mScaleColor:$mScaleColor\n" +
                    "mFaceBgId:$mFaceBgId\n" +
                    "mScaleShow:$mScaleShow\n"
        )


        //读取背景Bitmap
        if (mFaceBgId != -1) {
            mBackgroundBitmap = BitmapFactory.decodeResource(resources, mFaceBgId)
        }

        typeArray.recycle()

        initPaint()
    }

    /**
     * 第二步：测量自己的宽高
     * */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        /**
         * 获取期望宽高和模式 只是期望 不一定遵守
         * 实际根据业务来决定最终宽高
         * **/
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        Log.d(
            "WatchFaceView",
            "widthSize:$widthSize\n" +
                    "widthMode:$widthMode\n" +
                    "heightSize:$heightSize\n" +
                    "heightMode:$heightMode\n"
        )
        //计算扣除padding值
        //正常来讲 padding应该在绘制的时候计算 如果此处测量宽高减去了padding则 padding会变成margin的效果
        //val widthTargetSize = widthSize - paddingStart - paddingEnd
        //val heightTargetSize = heightSize - paddingTop - paddingBottom
        //判断大小 固定为正方形 宽高值取小的值 但不为0
        var targetSize = if (widthSize == 0) {
            heightSize
        } else if (heightSize == 0) {
            widthSize
        } else {
            min(widthSize, heightSize)
        }

        //如果期望宽高都为0 则最小设置为80dp
        if (targetSize == 0) {
            targetSize = MyUtils.dip2px(80f).toInt()
        }

        setMeasuredDimension(targetSize, targetSize)
    }

    //秒针画笔
    val mSecondPaint = Paint()

    //分针画笔
    val mMinutePaint = Paint()

    //时针画笔
    val mHourPaint = Paint()

    //刻度画笔
    val mScalePaint = Paint()

    /**
     * 第三步 初始化画笔
     * */
    private fun initPaint() {

        //秒针画笔
        //开启抗锯齿
        mSecondPaint.isAntiAlias = true
        //画笔颜色
        mSecondPaint.color = mSecondColor
        //画笔模式:描边模式  可选：描边/填充/填充且描边
        mSecondPaint.style = Paint.Style.STROKE
        //描边宽度
        mSecondPaint.strokeWidth = 2f
        //画笔Cap：半圆（圆角）结束  可选：按绘制路径/圆角/直角
        mSecondPaint.strokeCap = Paint.Cap.ROUND

        //分针画笔
        mMinutePaint.isAntiAlias = true
        mMinutePaint.color = mMinuteColor
        mMinutePaint.style = Paint.Style.STROKE
        mMinutePaint.strokeWidth = 4f
        mMinutePaint.strokeCap = Paint.Cap.ROUND


        //时针画笔

        mHourPaint.isAntiAlias = true
        mHourPaint.color = mHourColor
        mHourPaint.style = Paint.Style.STROKE
        mHourPaint.strokeWidth = 6f
        mHourPaint.strokeCap = Paint.Cap.ROUND


        //刻度画笔

        mScalePaint.isAntiAlias = true
        mScalePaint.color = mScaleColor
        mScalePaint.style = Paint.Style.STROKE
        mScalePaint.strokeWidth = 4f
        mScalePaint.strokeCap = Paint.Cap.ROUND

    }


    /**
     * onDraw调用很频繁 每绘制1帧都会调用
     * 所以尽量不要在此处进行初始化方法 或者新建对象
     * 知识点1 Rect 和 RectF区别 ：
     * Rect 和 RectF 都是用来表示矩形的类
     * Rect: Rect 类的精度是整数，适用于以像素为单位的计算或绘制，常用于处理屏幕坐标和像素处理。
     * RectF: RectF 类的精度是浮点数，适用于需要更高精度的计算或绘制，常用于对图形进行更复杂的变换，或者需要精确测量的绘制操作。
     *
     * */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = measuredWidth
        val height = measuredHeight

        mBackgroundBitmap?.let {

            canvas.drawBitmap(it,0f,0f,mScalePaint)
        }


        //canvas.drawCircle(width / 2f, height / 2f, 50f, mPaint)
    }

}