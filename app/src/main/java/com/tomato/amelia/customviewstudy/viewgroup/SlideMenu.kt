package com.tomato.amelia.customviewstudy.viewgroup

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import com.tomato.amelia.R
import com.tomato.amelia.databinding.ItemSlideActionBinding
import kotlin.math.absoluteValue

/**
 * author: created by tomato on 2024/2/18 14:43
 * description:
 * 自定义ViewGroup 例子3 仿QQ聊天item侧滑菜单
 *
 * 自定义ViewGroup步骤：
 * 1.获取相关属性，定义相关属性 init 中实现
 * 2.添加子view，根据属性修改子view样式 (可以根据数据内部创建、通过适配器、布局包裹)
 * 3.先测量孩子的宽高，再根据孩子的数据测量自己 onMeasure中实现
 * 4.摆放child 布局 onLayout中实现
 * 5.定义功能接口（需要返回的数据和动作）
 * 6.处理事件动作和数据
 *
 */
class SlideMenu @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    //功能
    private val mFunction: Int

    //监听
    private var mActionListener: OnActionClickListener? = null

    //是否正在跟随手势横向滑动
    private var isHorSlide = false

    //本次开始移动的x位置 用于计算本次移动距离
    private var mStartMoveX = 0f

    //本次点击动作的x，y位置（onTouchEvent中赋值） 用于判断本次是向左还是向右移动 以及解决和父布局中的竖向滑动冲突
    private var mDownX = 0f
    private var mDownY = 0f


    //本次点击动作的x,y位置（onInterceptTouchEvent中赋值） 用于判断本次点击是否拦截
    private var mInterceptDownX = 0f
    private var mInterceptDownY = 0f

    /**
     * Android中的Scroller是一个专门用于处理滚动效果的工具类，它可以帮助创建平滑的滚动动画。Scroller自身不会直接使视图滚动，而是提供了计算滚动位置的功能。
     * */
    private var mScroller: Scroller

    init {
        /***第一步 获取相关属性**/
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.SlideMenu)
        mFunction = typeArray.getInt(R.styleable.SlideMenu_slideFunction, 1)
        Log.d("SlideMenu", "mFunction:${mFunction}")
        typeArray.recycle()

        mScroller = Scroller(context)

    }


    /**
     * onFinishInflate 加载结束
     * 可以在此回调中获取到通过Layout包裹的子view数量
     * */
    override fun onFinishInflate() {
        super.onFinishInflate()
        setUpChildren()
    }


    /***第二步 添加子view
     * 添加子view方式
     * 1.内部创建
     * 2.包裹layout内容（布局中添加） SlideMenu采用此方式
     * 3.通过adapter创建
     * **/
    private fun setUpChildren() {
        //此时只有1个子view
        Log.d("SlideMenu", "childCount:${childCount}")
        //给在layout中的内容添加侧滑菜单 我们只希望有一个整体的子view
        if (childCount > 1) {
            throw IllegalArgumentException("只能包含一个子View！")
        }
        getChildAt(0).tag = 1
        /***
         * 添加菜单子view
         *
         * 如果不使用viewBinding 而是使用布局id创建的话有以下小坑：
         * LayoutInflater.from(context).inflate(R.layout.item_slide_action, this, true)
         * attachToRoot = true时 自动添加到父布局 返回的view时parent
         * attachToRoot = false时 不添加到父布局 返回的view是其本身
         */
        val menuLayoutBinding = ItemSlideActionBinding.inflate(LayoutInflater.from(context), this, false)
        //添加菜单后 有2个子View
        addView(menuLayoutBinding.root)

        /***第六步 处理事件**/
        menuLayoutBinding.tvDelete.setOnClickListener {
            mActionListener?.onDelete()
            scrollToSmooth(0, 0, 200)
        }
        menuLayoutBinding.tvRead.setOnClickListener {
            mActionListener?.onRead()
            scrollToSmooth(0, 0, 200)
        }
        menuLayoutBinding.tvTop.setOnClickListener {
            mActionListener?.onTop()
            scrollToSmooth(0, 0, 200)
        }
        Log.d("SlideMenu", "childCount:${childCount} ${menuLayoutBinding.root.layoutParams?.height}")
    }

    /***第三步 测量**/
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        Log.d(
            "SlideMenu",
            "widthSize:${widthSize} widthMode:${widthMode} UNSPECIFIED:${MeasureSpec.UNSPECIFIED} AT_MOST:${MeasureSpec.AT_MOST} EXACTLY:${MeasureSpec.EXACTLY} "
        )
        //测量content内容部分
        val contentView = getChildAt(0)
        val contentHeight = contentView.layoutParams.height
        //根据content部分的高度方式来测量高度
        val contentHeightSpec = when (contentHeight) {
            LayoutParams.MATCH_PARENT -> {
                MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY)
            }

            LayoutParams.WRAP_CONTENT -> {
                MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST)
            }

            else -> {
                MeasureSpec.makeMeasureSpec(contentHeight, MeasureSpec.EXACTLY)
            }
        }
        contentView.measure(widthMeasureSpec, contentHeightSpec)

        val contentViewMeasureHeight = contentView.measuredHeight

        //测量菜单部分
        val menuView = getChildAt(1)

        //菜单宽度占总宽度4/3 高度就以内容高度为准
        menuView.measure(
            MeasureSpec.makeMeasureSpec((widthSize * 3 / 4).toInt(), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(contentViewMeasureHeight, MeasureSpec.EXACTLY)
        )

        //测量自己
        setMeasuredDimension(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(contentViewMeasureHeight, MeasureSpec.EXACTLY)
        )
    }


    //定位： 内容View左边位置的值 通过此值计算出所有子view位置
    private var mContentLeft = 0

    /***第四步 摆放**/
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d("SlideMenu", "onLayout  l:${l} t:${t} r:${r} b:${b}")
        //内容部分
        val contentView = getChildAt(0)
        val contentLeft = mContentLeft
        val contentTop = 0
        val contentRight = contentLeft + contentView.measuredWidth
        val contentBottom = contentTop + contentView.measuredHeight
        contentView.layout(contentLeft, contentTop, contentRight, contentBottom)
        //菜单部分
        val menuView = getChildAt(1)
        val menuLeft = contentRight
        val menuTop = contentTop
        val menuRight = menuLeft + menuView.measuredWidth
        val menuBottom = contentBottom
        menuView.layout(menuLeft, menuTop, menuRight, menuBottom)
    }


    /***第五步 定义功能接口**/
    fun setActionListener(listener: OnActionClickListener) {
        this.mActionListener = listener
    }

    interface OnActionClickListener {
        fun onDelete()
        fun onTop()
        fun onRead()
    }


    /**
     * ViewGroup 的 onInterceptTouchEvent 方法用于拦截触摸事件，可以在 ViewGroup 中拦截子视图的触摸事件
     * 例如子View设置了点击监听 则自动消费掉触摸事件 无法回调到onTouchEvent来 所以在这里决定要不要拦截
     * @return true 表示拦截该事件 停止事件分发机制继续分发 不让子view处理
     *         false 表示不拦截该事件 由事件分发机制继续分发
     * */
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val action = ev?.action
        val x = ev?.x
        val y = ev?.y
        Log.d("SlideMenu", "onInterceptTouchEvent (${x},${y})")
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mInterceptDownX = ev.x
                mInterceptDownY = ev.y

                /**
                 * 注意 只有 MotionEvent.ACTION_MOVE会回调
                 * MotionEvent.ACTION_DOWN中没有处理拦截逻辑 不会回调到onTouchEvent
                 * 所以需要在MotionEvent.ACTION_DOWN中也给mDownX,mDownY,mStartMoveX赋值 记录移动起点位置
                 * **/
                mDownX = ev.x
                mDownY = ev.y
                mStartMoveX = ev.x
            }

            MotionEvent.ACTION_MOVE -> {
                //判断本次事件意图是否是为了横向滑动 ：x轴距离大于y轴距离且多10px才视为需要横向滑动
                if ((ev.x - mInterceptDownX).absoluteValue > ((ev.y - mInterceptDownY).absoluteValue + 10)) {
                    /***
                     * 本该分发到子view的事件
                     * 判断到为横向平移了 所以进行拦截
                     * 拦截后 该事件会回调到onTouchEvent
                     *
                     * 注意 只有 MotionEvent.ACTION_MOVE会回调
                     * MotionEvent.ACTION_DOWN中没有处理拦截逻辑 正常来讲不会回调到onTouchEvent
                     * 所以需要在MotionEvent.ACTION_DOWN中也给mDownX,mDownY,mStartMoveX赋值
                     * **/
                    return true
                }
            }

            MotionEvent.ACTION_UP -> {

            }

            else -> {

            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    /***第六步 处理事件**/
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action
        val x = event?.x
        val y = event?.y
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                //记录本次移动的起点
                mDownX = event.x
                mDownY = event.y
                mStartMoveX = event.x
                Log.d("SlideMenu", "ACTION_DOWN (${x},${y})")
            }

            MotionEvent.ACTION_MOVE -> {
                //计算本次移动的差值 实际距离
                val moveValueX = event.x - mStartMoveX

                //简单处理一下滑动冲突：首次移动x轴距离大于y轴距离10px 才视为需要处理横向滑动 否则将手势交由父布局处理
                if ((event.x - mDownX).absoluteValue > ((event.y - mDownY).absoluteValue + 10)) {
                    isHorSlide = true
                    //正在处理手势 横向滑动 请求父布局不要拦截触摸事件
                    parent.requestDisallowInterceptTouchEvent(true)
                }

                if (isHorSlide) {
                    /***
                     * 修改位置有两种方式
                     * 1.修改知道的定位参数 然后requestLayout()来重新布局
                     * 2.使用scrollBy来修改View相对当前绘制位置的偏移量并重新绘制绘
                     * 注意：此方法会修改View的实际位置，但不会修改View的布局矩形宽高及大小
                     *
                     * scrollBy方法用于相对于当前位置按照指定的偏移量进行滚动。
                     * scrollTo方法用于将View滚动到指定的坐标位置
                     */

                    //方式1：修改布局定位参数 申请重新布局
                    /* mContentLeft += moveValue.toInt()
                     // 申请重新布局
                     requestLayout()*/
                    //处理滑动边界问题 计算移动后的x值 若小于0则不能继续移动了
                    val targetX = -moveValueX + scrollX
                    Log.d("SlideMenu", "ACTION_MOVE (${x},${y})  moveValue:$moveValueX scrollX:${scrollX} targetX:$targetX")
                    if (targetX <= 0) {
                        //向左最小平移距离为0
                        scrollTo(0, 0)
                    } else if (targetX >= getMenuViewMeasuredWidth()) {
                        //向右最大平移距离为菜单内容测量长度
                        scrollTo(getMenuViewMeasuredWidth(), 0)
                    } else {
                        //方式2：
                        scrollBy(-moveValueX.toInt(), 0)
                    }
                    //记录本次移动后的起点 为下次移动做准备
                    mStartMoveX = event.x
                }
            }

            MotionEvent.ACTION_UP -> {
                Log.d("SlideMenu", "ACTION_UP (${x},${y})")
                if (isHorSlide) {
                    isHorSlide = false
                    //处理手势完毕 允许父布局拦截触摸事件
                    parent.requestDisallowInterceptTouchEvent(false)
                    val mUpX = event.x
                    if (mUpX > mDownX) {
                        //向右移动
                        if (scrollX < getMenuViewMeasuredWidth() - 200) {
                            //scrollTo(0, 0)
                            scrollToSmooth(0, 0, 200)
                        } else {
                            //scrollTo(getMenuViewMeasuredWidth(), 0)
                            scrollToSmooth(getMenuViewMeasuredWidth(), 0, 100)
                        }
                    }

                    if (mUpX < mDownX) {
                        //向左移动
                        if (scrollX > 200) {
                            //scrollTo(getMenuViewMeasuredWidth(), 0)
                            scrollToSmooth(getMenuViewMeasuredWidth(), 0, 200)
                        } else {
                            //scrollTo(0, 0)
                            scrollToSmooth(0, 0, 100)
                        }
                    }

                }
            }
        }
        return true
    }


    /**
     * 平滑移动
     * @param x 目标x
     * @param y 目标y
     * @param during 持续时间
     * */
    private fun scrollToSmooth(x: Int, y: Int, during: Long) {
        val xValue = scrollX - x
        val yValue = scrollY - y
        val anim = ValueAnimator.ofFloat(1f, 0f)
        anim.duration = during
        anim.interpolator = LinearInterpolator()
        anim.addUpdateListener {
            val value = it.animatedValue as Float
            val currX = x + xValue * value
            val currY = y + yValue * value
            scrollTo(currX.toInt(), currY.toInt())
        }
        anim.start()
    }


    private fun getMenuViewMeasuredWidth(): Int {
        return getChildAt(1).measuredWidth
    }


}