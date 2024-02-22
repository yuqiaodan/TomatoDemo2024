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
import java.util.Calendar
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
 * 5.添加功能接口
 * 6.处理事件
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


    val mCalendar = Calendar.getInstance()

    init {
        /**
         * 第一步：获取相关属性，定义相关属性 init 中实现
         * */
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.WatchFaceView)
        mSecondColor = typeArray.getColor(R.styleable.WatchFaceView_secondColor, context.getColor(R.color.watch_red))
        mMinuteColor = typeArray.getColor(R.styleable.WatchFaceView_minuteColor, context.getColor(R.color.watch_yellow))
        mHourColor = typeArray.getColor(R.styleable.WatchFaceView_hourColor, context.getColor(R.color.watch_green))
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

        //autoRunClock()
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
        mSecondPaint.strokeWidth = 6f
        //画笔Cap：半圆（圆角）结束  可选：按绘制路径（圆角）/半圆/直角
        mSecondPaint.strokeCap = Paint.Cap.ROUND

        //分针画笔
        mMinutePaint = Paint()
        mMinutePaint.isAntiAlias = true
        mMinutePaint.color = mMinuteColor
        mMinutePaint.style = Paint.Style.STROKE
        mMinutePaint.strokeWidth = 8f
        mMinutePaint.strokeCap = Paint.Cap.ROUND


        //时针画笔
        mHourPaint = Paint()
        mHourPaint.isAntiAlias = true
        mHourPaint.color = mHourColor
        mHourPaint.style = Paint.Style.STROKE
        mHourPaint.strokeWidth = 12f
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

        //绘制刻度
        for (i in 0..12) {
            //角度转为弧度  角度每30度画一个刻度 总共画12个 需要将角度转为弧度
            val radian = Math.toRadians(360f * (i / 12f).toDouble())
            val x1 = r1 * cos(radian).toFloat()
            val y1 = r1 * sin(radian).toFloat()

            val x2 = r2 * cos(radian).toFloat()
            val y2 = r2 * sin(radian).toFloat()

            Log.d("WatchFaceView", "onDraw:${x1} ${y1} ${x2} ${y2}")
            canvas.drawLine(x1, y1, x2, y2, mScalePaint)
        }
        canvas.drawCircle(0f, 0f, 10f, mScalePaint)

        //设置当前时间
        //mCalendar.time = Date(System.currentTimeMillis())
        mCalendar.timeInMillis = System.currentTimeMillis()

        //绘制指针（分针、时针、秒针）
        //hour [0 , 11]
        val hour = mCalendar.get(Calendar.HOUR_OF_DAY)
        //hour [0 , 59]
        val minute = mCalendar.get(Calendar.MINUTE)
        //hour [0 , 59]
        val second = mCalendar.get(Calendar.SECOND)

        val hourF = hour + minute / 60f + second / 3600f
        val minuteF = minute + second / 60f

        val adPm = mCalendar.get(Calendar.AM_PM)
        if (adPm == Calendar.AM) {
            //上午
        } else {
            //下午
        }

        //绘制秒针 先保存当前画布状态
        canvas.save()

        //计算秒针旋转角度 一圈总共360度  秒针角度= 360 * (second / 60f)
        val secondDegrees = 360 * (second / 60f)
        //根据角度 旋转画布坐标系 准备在x轴上画出秒针 由于X轴默认 水平从左到右 但秒为0时 秒针应该为竖直向上 偏移角度为90度 所以减去
        canvas.rotate(secondDegrees - 90, 0f, 0f)
        //在x轴上画出秒针
        canvas.drawLine(0f, 0f, width / 2 * 0.9f, 0f, mSecondPaint)
        //恢复绘制秒针之前的状态
        canvas.restore()

        canvas.save()
        //计算分针旋转角度 一圈总共360度  分针角度= 360 * (minuteF / 60f)
        val minuteDegrees = 360 * (minuteF / 60f)
        canvas.rotate(minuteDegrees - 90, 0f, 0f)
        canvas.drawLine(0f, 0f, width / 2 * 0.6f, 0f, mMinutePaint)
        canvas.restore()

        canvas.save()
        //计算时针旋转角度 一圈总共360度  时针角度= 360 * (hourF / 12f)
        val hourDegrees = 360 * (hourF / 12f)
        canvas.rotate(hourDegrees - 90, 0f, 0f)
        canvas.drawLine(0f, 0f, width / 2 * 0.3f, 0f, mHourPaint)
        canvas.restore()

        //恢复到画布之前保存的状态 坐标（0，0）恢复到左上角
        canvas.restore()

    }


    /**
     * 以子线程的方式刷新UI 让时钟动起来（不太推荐这种方式）
     * 注意:需要处理view不可见的情况 也需要处理view从window中移除的情况 及时恢复和停止线程
     * */
    fun autoRunClockOnThread() {
        val thread = Thread(object : Runnable {
            override fun run() {
                while (true) {
                    Thread.sleep(1000L)
                    postInvalidate()
                }

            }
        })
        thread.start()
    }

    //时钟是否正在更新
    private var isUpdating = false

    /**
     * 当你的 View 被添加到窗口中时，系统会调用这个方法。
     * 在此处以handler的方式让时钟动起来 很方便的处理view被移除和添加后 自动暂停动画等操作
     * view自带了handler的所有方法 post postDelay 直接可以调用 效果和使用handler是一样的
     * */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        /***
         * post和postDelayed也是基于handle实现的
         * invalidate()   触发重绘 只能在主线程调用
         * postInvalidate()   触发重绘 可以在子线程调用
         * ***/
        isUpdating = true
        post(object : Runnable {
            override fun run() {
                if (isUpdating) {
                    invalidate()
                    postDelayed(this, 1000L)
                } else {
                    //移除掉延迟执行的任务
                    removeCallbacks(this)
                }
            }
        })
    }

    /**
     *  当你的 View 从窗口分离(例如被移出视图树)时，系统会调用这个方法。这通常发生在 View 被移除或 Activity 被销毁的情况下。
     * */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isUpdating = false
    }


    /**
     * 当包含你自定义 View 的 Window 可见性发生改变时，比如由于包含该 View 的 Activity 的生命周期导致其被隐藏或显示，系统会调用这个方法
     * */
    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
    }

    /**
     * 这个回调会传递给你一个改变后的可见性值，该值可以是 View.VISIBLE、View.INVISIBLE 或 View.GONE。
     * 你可以执行需要的操作，例如启动或暂停动画，资源加载或释放等，以适应 View 的可见性状态变化
     * 注意，在初始化 View 或因为其他原因（如父容器的可见性变化）导致 View 可见性变化时，也会调用 onVisibilityChanged，并不仅限于自定义 View 本身的 setVisibility 方法调用
     * 确保你的自定义 View 在消失时可以正确释放资源，以及在再次出现时可以正确恢复状态，这有助于防止潜在的内存泄漏或不必要的资源消耗
     * */
    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) {
            // View 变为可见
        } else if (visibility == View.INVISIBLE || visibility == View.GONE) {
            // View 变为不可见或完全不显示
        }
    }

}