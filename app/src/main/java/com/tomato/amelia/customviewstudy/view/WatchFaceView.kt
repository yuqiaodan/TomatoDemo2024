package com.tomato.amelia.customviewstudy.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import com.tomato.amelia.R
import com.tomato.amelia.utils.MyUtils
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


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
    private var mBackgroundBitmap: Bitmap? = null

    //背景bitmap 矩形范围 指定bitmap大小
    //bitmap原矩形大小
    private var mBackgroundRectSrc: Rect? = null

    //bitmap目标矩形大小
    private var mBackgroundRectDst: Rect? = null

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


        //读取背景Bitmap
        if (mFaceBgId != -1) {
            mBackgroundBitmap = BitmapFactory.decodeResource(resources, mFaceBgId)
        }


        Log.d(
            "WatchFaceView",
            "mSecondColor:$mSecondColor\n" +
                    "mMinuteColor:$mMinuteColor\n" +
                    "mHourColor:$mHourColor\n" +
                    "mScaleColor:$mScaleColor\n" +
                    "mFaceBgId:$mFaceBgId\n" +
                    "mScaleShow:$mScaleShow\n" +
                    "mBackgroundBitmap W H -> ${mBackgroundBitmap?.width} ${mBackgroundBitmap?.height}"

        )


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
        initBackgroundRect()
    }

    //秒针画笔
    lateinit var mSecondPaint: Paint

    //分针画笔
    lateinit var mMinutePaint: Paint

    //时针画笔
    lateinit var mHourPaint: Paint

    //刻度画笔
    lateinit var mScalePaint: Paint

    //背景画笔
    lateinit var mBgPaint: Paint


    /**
     * 第三步 初始化画笔
     * */
    private fun initPaint() {
        //秒针画笔
        mSecondPaint = Paint()
        //开启抗锯齿
        mSecondPaint.isAntiAlias = true
        //画笔颜色
        mSecondPaint.color = mSecondColor
        //画笔模式:描边模式  可选：描边/填充/填充且描边
        mSecondPaint.style = Paint.Style.STROKE
        //描边宽度
        mSecondPaint.strokeWidth = 2f
        //画笔Cap：半圆（圆角）结束  可选：按绘制路径（圆角）/半圆/直角
        mSecondPaint.strokeCap = Paint.Cap.ROUND

        //分针画笔
        mMinutePaint = Paint()
        mMinutePaint.isAntiAlias = true
        mMinutePaint.color = mMinuteColor
        mMinutePaint.style = Paint.Style.STROKE
        mMinutePaint.strokeWidth = 4f
        mMinutePaint.strokeCap = Paint.Cap.ROUND


        //时针画笔
        mHourPaint = Paint()
        mHourPaint.isAntiAlias = true
        mHourPaint.color = mHourColor
        mHourPaint.style = Paint.Style.STROKE
        mHourPaint.strokeWidth = 6f
        mHourPaint.strokeCap = Paint.Cap.ROUND


        //刻度画笔
        mScalePaint = Paint()
        mScalePaint.isAntiAlias = true
        mScalePaint.color = mScaleColor
        mScalePaint.style = Paint.Style.STROKE
        mScalePaint.strokeWidth = 4f
        mScalePaint.strokeCap = Paint.Cap.ROUND

        //背景画笔
        mBgPaint = Paint()
        mBgPaint.isAntiAlias = true
        mBgPaint.color = context.getColor(R.color.black)
        mBgPaint.style = Paint.Style.FILL

    }


    /**
     *
     * 创建绘制bitmap的 源矩形 和 目标矩形 对象
     *
     * 绘制背景是Bitmap 目前为固定高度 但View的宽高却是由外部决定的
     * 所以想要在view内部绘制bitmap作为背景 且适配View的宽高
     * 则需要给drawBitmap一个变形映射规则
     *
     * drawBitmap重载方法之一：
     * Canvas.drawBitmap(@NonNull Bitmap bitmap, @Nullable Rect src, @NonNull Rect dst,@Nullable Paint paint)
     * 需要传入两个Rect  第一个为原bitmap大小rect 第二个为目标大小rect
     * 目标大小rect:可以实现定位以及bitmap缩放
     * 这样可以将固定尺寸的bitmap做一个变形映射 适配view尺寸
     * 比如源bitmap尺寸为100*100 目标矩形尺寸为30*30 则可以绘制出30*30的Bitmap
     *
     * 知识点1 Rect 和 RectF区别 ：
     * Rect 和 RectF 都是用来表示矩形的类
     * Rect: Rect 类的精度是整数，适用于以像素为单位的计算或绘制，常用于处理屏幕坐标和像素处理。
     * RectF: RectF 类的精度是浮点数，适用于需要更高精度的计算或绘制，常用于对图形进行更复杂的变换，或者需要精确测量的绘制操作。
     *
     * */
    private fun initBackgroundRect() {
        mBackgroundBitmap?.let { bitmap ->
            val rect1 = Rect()
            rect1.left = 0
            rect1.top = 0
            rect1.right = bitmap.width
            rect1.bottom = bitmap.height
            mBackgroundRectSrc = rect1

            //需要一个居中的 宽高为总宽高3/4的bitmap作为背景
            //所以 rect2 的left top确定右上角位置， right bottom确定宽高
            val rect2 = Rect()
            rect2.left = (measuredWidth * 0.125).toInt()
            rect2.top = (measuredHeight * 0.125).toInt()
            rect2.right = rect2.left + (measuredWidth * 0.75).toInt()
            rect2.bottom = rect2.top + (measuredHeight * 0.75).toInt()

            mBackgroundRectDst = rect2
        }

    }


    /**
     * onDraw调用很频繁 每绘制1帧都会调用
     * 所以尽量不要在此处进行初始化方法 或者创建新对象
     * */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = measuredWidth.toFloat()
        val height = measuredHeight.toFloat()

        //绘制背景颜色（黑色表盘）
        canvas.drawCircle(width / 2, height / 2, width / 2, mBgPaint)

        //绘制背景图片 居中 宽高为3/4的图片
        val bgBitmap = mBackgroundBitmap
        val bgRectSrc = mBackgroundRectSrc
        val bgRectDst = mBackgroundRectDst
        if (bgBitmap != null && bgRectSrc != null && bgRectDst != null) {
            //drawBitmap有一个重载方法 需要传入两个Rect  第一个为原大小 第二个为目标大小 这样可以将固定尺寸的bitmap做一个变形映射 适配view尺寸
            canvas.drawBitmap(bgBitmap, bgRectSrc, bgRectDst, mBgPaint)
        }

        //保存画布当前状态
        canvas.save()
        //将画布(0,0)坐标平移到居中位置 方便画圆
        canvas.translate(width / 2, height / 2)
        val r1 = width / 2 * 0.95f
        val r2 = width / 2 * 0.8f

        /**
         *  为什么Math.sin(90.0)计算结果是0.89399666 而不是1
         *  Math.sin()函数通常接受的参数是弧度（radians）而不是度（degrees）
         *  因此，当你调用Math.sin(90.0)时，你实际上是在计算sin(90 rad)而不是sin(90°)
         *  可以通过Math.toRadians将角度转为期望的弧度传入Math.sin进行计算
         *  double radians = Math.toRadians(degrees);
         * */
        //角度30度画一个刻度 总共画12个
        val th = 360 / 12
        //绘制刻度
        for (i in 0..12) {
            //角度转为弧度
            val radian = Math.toRadians(th * i.toDouble())
            val x1 = r1 * cos(radian).toFloat()
            val y1 = r1 * sin(radian).toFloat()

            val x2 = r2 * cos(radian).toFloat()
            val y2 = r2 * sin(radian).toFloat()

            Log.d("WatchFaceView", "onDraw: ${th * i}  ${x1} ${y1} ${x2} ${y2}")
            canvas.drawLine(x1, y1, x2, y2, mScalePaint)
        }
        //恢复到画布之前保存的状态 坐标（0，0）恢复到左上角
        canvas.restore()


    }

}