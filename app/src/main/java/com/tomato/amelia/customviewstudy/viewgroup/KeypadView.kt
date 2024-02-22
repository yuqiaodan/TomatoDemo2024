package com.tomato.amelia.customviewstudy.viewgroup

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import com.tomato.amelia.R
import com.tomato.amelia.utils.MyUtils


/**
 * author: created by tomato on 2024/2/7 14:06
 * description:
 * 自定义ViewGroup 例子2 键盘样式布局
 *
 * 自定义ViewGroup步骤：
 * 1.获取相关属性，定义相关属性 init 中实现
 * 2.添加子view，根据属性修改子view样式 (可以根据数据内部创建、通过适配器、布局包裹)
 * 3.先测量孩子的宽高，再根据孩子的数据测量自己 onMeasure中实现
 * 4.摆放child 布局 onLayout中实现
 * 5.定义功能接口（需要返回的数据和动作）
 * 6.处理事件动作和数据
 *
 * P43
 */
class KeypadView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    companion object {
        //默认 3列 4行
        const val DEFAULT_COLUMN_COUNT = 3
        const val DEFAULT_ROW_COUNT = 4
    }

    private val mColumn = DEFAULT_COLUMN_COUNT
    private val mRow = DEFAULT_ROW_COUNT


    //item之间的距离
    private val itemMargin = MyUtils.dip2px(3f)


    //数字文案颜色
    @ColorInt
    val mNumberColor: Int

    //数字文案大小
    val mNumberSize: Int

    //按钮点击颜色
    @ColorInt
    val mItemPressColor: Int

    //按钮普通颜色
    @ColorInt
    val mItemNormalColor: Int

    init {
        /***第一步 获取相关属性**/
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.KeypadView)

        mNumberColor = typeArray.getColor(R.styleable.KeypadView_numberColor, context.getColor(R.color.keypad_white))

        //获取字体大小 采用getDimensionPixelSize方法 和getDimension相同 最终都是返回px 只是将结果四舍五入为整数了
        mNumberSize = typeArray.getDimensionPixelSize(R.styleable.KeypadView_numberSize, -1)

        mItemNormalColor = typeArray.getColor(R.styleable.KeypadView_itemNormalColor, context.getColor(R.color.keypad_gray))

        mItemPressColor = typeArray.getColor(R.styleable.KeypadView_itemPressColor, context.getColor(R.color.orange))

        Log.d(
            "KeypadView",
            "mNumberColor:$mNumberColor\n" +
                    "mNumberSize:$mNumberSize\n" +
                    "mItemPressColor:$mItemPressColor\n" +
                    "mItemNormalColor:$mItemNormalColor\n"
        )
        typeArray.recycle()

        setUpChildren()
    }

    /***第二步 添加子view**/
    private fun setUpChildren() {
        removeAllViews()
        for (i in 0..10) {
            val item = TextView(context)
            if (i == 10) {
                //标记一下 为删除按钮
                item.tag = true
                item.text = "X"
            } else {
                item.tag = i
                //内容
                item.text = i.toString()
            }

            //字体颜色
            item.setTextColor(mNumberColor)
            //居中
            item.gravity = Gravity.CENTER
            //设置文字大小
            if (mNumberSize == -1) {
                //未设置则默认14sp
                item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            } else {
                item.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNumberSize.toFloat())
            }
            /***设置背景 通过代码创建drawable的方式**/
            //有渐变用GradientDrawable 没有渐变可以选择使用ShapeDrawable
            val normalShape = GradientDrawable()
            //形状
            normalShape.shape = GradientDrawable.RECTANGLE
            //圆角
            normalShape.cornerRadius = MyUtils.dip2px(5f)
            //描边
            //gradientDrawable.setStroke(MyUtils.dip2px(1f).toInt(), Color.WHITE)
            //背景 渐变
            //gradientDrawable.colors = intArrayOf(Color.parseColor("#FFE2E2"), Color.WHITE, Color.WHITE)
            //渐变方向
            //gradientDrawable.orientation = GradientDrawable.Orientation.TOP_BOTTOM
            //背景 填充
            normalShape.setColor(mItemNormalColor)

            val pressShape = GradientDrawable()
            pressShape.shape = GradientDrawable.RECTANGLE
            pressShape.cornerRadius = MyUtils.dip2px(5f)
            pressShape.setColor(mItemPressColor)

            //添加selector 不同状态shape
            val bg = StateListDrawable()
            //先添加state_pressed状态才会生效
            bg.addState(intArrayOf(android.R.attr.state_pressed), pressShape)
            bg.addState(intArrayOf(), normalShape)

            //设置背景
            item.background = bg

            item.setOnClickListener {
                if (it.tag == true) {
                    mKeypadListener?.onDelete()
                } else {
                    mKeypadListener?.onClickNumber(i)
                }
            }
            addView(item)
            Log.d("KeypadView", "item Width:${item.layoutParams?.width} Height:${item.layoutParams?.height}")
        }

    }

    /***第三步 测量**/
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        //一行3列 所以孩子的宽度就是总宽度除以3
        val preItemWidth = widthSpecSize / mColumn
        //总共4行 所以孩子的宽度就是总高度除以4
        val preItemHeight = heightSpecSize / mRow
        Log.d("KeypadView", "widthSpecSize:${widthSpecSize} heightSpecSize:${heightSpecSize}")
        Log.d("KeypadView", "preItemWidth:${preItemWidth} preItemHeight:${preItemHeight}")
        for (i in 0 until childCount) {
            val item = getChildAt(i)
            val isDeleteBtn = (item.tag == true)
            if (isDeleteBtn) {
                /**
                 * 此处使用measureChild情况不对 计算出来TextView仍然为包裹自身
                 * ChatGpt4.0解释如下：
                 * 当您使用measureChild方法时，它内部将根据子视图的LayoutParams和传入的规格(parentWidthMeasureSpec和parentHeightMeasureSpec)计算出实际应用到子视图的规格。
                 * 如果子视图的LayoutParams是wrap_content，那么在计算 MeasureSpecs 时，尺寸的模式就被设置为MeasureSpec.AT_MOST，即使您传入的是MeasureSpec.EXACTLY
                 * 如果您希望忽略子视图的LayoutParams并直接指定尺寸，您应该继续使用item.measure(...)而不是measureChild(...)
                 * */
                item.measure(
                    MeasureSpec.makeMeasureSpec(preItemWidth * 2, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(preItemHeight, MeasureSpec.EXACTLY)
                )
                /* measureChild(
                     item,
                     MeasureSpec.makeMeasureSpec(preItemWidth * 2, MeasureSpec.EXACTLY),
                     MeasureSpec.makeMeasureSpec(preItemHeight, MeasureSpec.EXACTLY)
                 )*/
            } else {
                item.measure(
                    MeasureSpec.makeMeasureSpec(preItemWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(preItemHeight, MeasureSpec.EXACTLY)
                )
                /*measureChild(
                    item,
                    MeasureSpec.makeMeasureSpec(preItemWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(preItemHeight, MeasureSpec.EXACTLY)
                )*/
            }
        }
        //自身按父布局给的规则来
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    /***第四步 摆放
     * 摆放这一步其实比较考验数学思维
     * 最好按自己的想法和思路实现
     * 总会实现出来的 只是过程繁琐和简洁的区别
     * 实在不行用笨方法 一个个摆放都可以 Map<Int,View> 挨个寻找view 一个个对齐
     * **/
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val item = getChildAt(i)
            val mTag = item.tag
            if (mTag == true) {
                val iTop = item.measuredHeight * 3
                //左：父布局宽度 除以3
                val iLeft = measuredWidth / 3
                val iRight = iLeft + item.measuredWidth
                val iBottom = item.measuredHeight * 4
                item.layout(iLeft, iTop, iRight, iBottom)
            } else {
                when (mTag) {
                    7, 8, 9 -> {
                        val iTop = 0
                        val iLeft = (mTag.toString().toInt() - 7) * item.measuredWidth
                        val iRight = (mTag.toString().toInt() - 7 + 1) * item.measuredWidth
                        val iBottom = item.measuredHeight
                        item.layout(iLeft, iTop, iRight, iBottom)
                    }

                    4, 5, 6 -> {
                        val iTop = item.measuredHeight
                        val iLeft = (mTag.toString().toInt() - 4) * item.measuredWidth
                        val iRight = (mTag.toString().toInt() - 4 + 1) * item.measuredWidth
                        val iBottom = item.measuredHeight * 2
                        item.layout(iLeft, iTop, iRight, iBottom)

                    }

                    1, 2, 3 -> {
                        val iTop = item.measuredHeight * 2
                        val iLeft = (mTag.toString().toInt() - 1) * item.measuredWidth
                        val iRight = (mTag.toString().toInt() - 1 + 1) * item.measuredWidth
                        val iBottom = item.measuredHeight * 3
                        item.layout(iLeft, iTop, iRight, iBottom)
                    }

                    0 -> {
                        val iTop = item.measuredHeight * 3
                        val iLeft = 0
                        val iRight = item.measuredWidth
                        val iBottom = item.measuredHeight * 4
                        item.layout(iLeft, iTop, iRight, iBottom)
                    }
                }
            }

        }
    }

    private var mKeypadListener: KeypadListener? = null
    fun setKeypadListener(listener: KeypadListener) {
        this.mKeypadListener = listener
    }

    interface KeypadListener {

        fun onClickNumber(num: Int)

        fun onDelete()
    }


}