package com.tomato.amelia.customviewstudy.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import com.tomato.amelia.R
import com.tomato.amelia.databinding.ItemSlideActionBinding

/**
 * author: created by tomato on 2024/2/18 14:43
 * description:
 */
class SlideMenu @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    //功能
    private val mFunction: Int

    //监听
    private var mActionListener: OnActionClickListener? = null


    init {
        /***第一步 获取相关属性**/
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.SlideMenu)
        mFunction = typeArray.getInt(R.styleable.SlideMenu_slideFunction, 1)
        Log.d("SlideMenu", "mFunction:${mFunction}")
        typeArray.recycle()
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
        }
        menuLayoutBinding.tvRead.setOnClickListener {
            mActionListener?.onRead()
        }
        menuLayoutBinding.tvTop.setOnClickListener {
            mActionListener?.onTop()
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

    /***第四步 摆放**/
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d("SlideMenu", "onLayout  l:${l} t:${t} r:${r} b:${b}")
        //内容部分
        val contentView = getChildAt(0)
        val contentLeft = 0
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


    /***第六步 处理事件**/
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action
        val x = event?.x
        val y = event?.y
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("SlideMenu", "ACTION_DOWN (${x},${y})")

            }

            MotionEvent.ACTION_UP -> {
                Log.d("SlideMenu", "ACTION_UP (${x},${y})")
            }


            MotionEvent.ACTION_MOVE -> {
                Log.d("SlideMenu", "ACTION_MOVE (${x},${y})")
            }
        }

        return true
    }


}