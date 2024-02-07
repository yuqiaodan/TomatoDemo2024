package com.tomato.amelia.customviewstudy.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import com.tomato.amelia.R
import com.tomato.amelia.databinding.ItemFlowLayoutBinding
import com.tomato.amelia.utils.MyUtils

/**
 * author: created by tomato on 2024/2/6 14:06
 * description:
 * 自定义ViewGroup 例子1 搜索推荐流式布局
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
class FlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    //点击监听
    private var mItemClickListener: ItemClickListener? = null

    //总view集合 一个list表示一行的view
    private val mLinesView = arrayListOf<List<View>>()

    //text数据源
    private val mData = arrayListOf<String>()

    //最大行数
    private val mMaxLine: Int

    //横向间隔
    private val mItemHorizontalMargin: Float

    //竖向间隔
    private val mItemVerticalMargin: Float

    //文字最大长度
    private val mTextMaxLength: Int

    //文字颜色
    @ColorInt
    private val mTextColor: Int

    //描边颜色
    @ColorInt
    private val mBorderColor: Int

    //描边圆角
    private val mBorderRadius: Float

    init {
        /**第一步 获取相关属性，定义相关属性**/
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout)
        mMaxLine = typeArray.getInt(R.styleable.FlowLayout_maxLine, 3)
        mItemHorizontalMargin = typeArray.getDimension(R.styleable.FlowLayout_itemHorizontalMargin, MyUtils.dip2px(5f))
        mItemVerticalMargin = typeArray.getDimension(R.styleable.FlowLayout_itemVerticalMargin, MyUtils.dip2px(5f))
        mTextMaxLength = typeArray.getInt(R.styleable.FlowLayout_textMaxLength, 10)
        mTextColor = typeArray.getColor(R.styleable.FlowLayout_textColor, resources.getColor(R.color.text_gray))
        mBorderColor = typeArray.getColor(R.styleable.FlowLayout_borderColor, resources.getColor(R.color.text_gray))
        mBorderRadius = typeArray.getDimension(R.styleable.FlowLayout_borderRadius, MyUtils.dip2px(5f))
        Log.d(
            "FlowLayoutTag", "FlowLayout\n" +
                    "maxLine:$mMaxLine\n" +
                    "itemHorizontalMargin:$mItemHorizontalMargin\n" +
                    "itemVerticalMargin:$mItemVerticalMargin\n" +
                    "textMaxLength:$mTextMaxLength\n" +
                    "textColor:$mTextColor\n" +
                    "borderColor:$mBorderColor\n" +
                    "borderRadius:$mBorderRadius\n"
        )
        typeArray.recycle()
    }

    /**第二步 添加子view**/
    private fun setUpChildren() {
        //清空所有原view
        removeAllViews()
        //添加子view
        mData.forEach { text ->
            //在这里可以通过动态创建drawable来实现mTextColor、mBorderColor、mBorderRadius等属性
            val itemBinding = ItemFlowLayoutBinding.inflate(LayoutInflater.from(context), this, true)
            itemBinding.tvText.setOnClickListener {
                /**第六步 处理事件**/
                mItemClickListener?.onItemClick(text)
            }
            itemBinding.tvText.text = text
        }
    }


    /**
     * 第三步 测量
     * 覆写onMeasure进行测量步骤
     * 这两个参数来自于父布局 起包含了值和模式(测量规范模式)
     * @param widthMeasureSpec 期望宽度 包含期望宽度size(px)和测量标准模式mode
     * @param heightMeasureSpec 期望高度 包含期望高度度size(px)和测量标准模式mode
     * widthMeasureSpec 值为int类型 大小为4字节 一个字节8bit 总共32bit 前2bit为Mode 后32bit位值
     *
     * 可以通过MeasureSpec.getMode MeasureSpec.getSize获取Mode和size
     * mode有三种枚举值:
     * MeasureSpec.UNSPECIFIED 不限制此view宽高 自由设置
     * MeasureSpec.EXACTLY 此view的宽高有一个明确的值 值为size
     * MeasureSpec.AT_MOST 此view的宽高可以根据自身变化 但最大为size
     *
     * 可以通过MeasureSpec.makeMeasureSpec构建期望宽高
     * */

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d("FlowLayoutTag", "onMeasure")

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        Log.d(
            "FlowLayoutTag", "widthMode->${widthMode} widthSize->$widthSpecSize heightMode->${heightMode} heightSize->${heightSpecSize}\n " +
                    "UNSPECIFIED->${MeasureSpec.UNSPECIFIED}\n" +
                    "AT_MOST->${MeasureSpec.AT_MOST}\n" +
                    "EXACTLY->${MeasureSpec.EXACTLY}"
        )
        /***
         * 测量子view
         * 子view数量 若为0则没有必要进行测量**/
        if (childCount == 0) {
            return
        }
        //清空所有view
        mLinesView.clear()
        //添加默认行
        var singleLine = arrayListOf<View>()
        mLinesView.add(singleLine)
        //子view全部设置为MeasureSpec.AT_MOST包裹模式
        val childWidthSpec = MeasureSpec.makeMeasureSpec(widthSpecSize, MeasureSpec.AT_MOST)
        /**
         * 子view高度设置为不限制UNSPECIFIED
         * 由子view自行根据内容决定高度(可以在子view中进一步限制高度 比如这里是textview 则可以限制maxline=1 或则其他条件)
         * 这样可以实现：FlowLayout "layout_height="wrap_content" heightSpecSize=0 时 也可以实现高度根据内容变化
         * */
        val childHeightSpec = MeasureSpec.makeMeasureSpec(heightSpecSize, MeasureSpec.UNSPECIFIED)
        //按位置依次获取子view
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView.visibility != View.VISIBLE) {
                continue
            }
            //调用measureChild测量孩子 实际也是调用子view的onMeasure方法
            measureChild(childView, childWidthSpec, childHeightSpec)
            //测量完毕后 获取孩子的尺寸 判断是否可以添加到当前行
            if (singleLine.size == 0) {
                singleLine.add(childView)
            } else {
                if (checkChildCanBeAddToLine(singleLine, childView, widthSpecSize)) {
                    singleLine.add(childView)
                } else {
                    singleLine = arrayListOf<View>()
                    singleLine.add(childView)
                    mLinesView.add(singleLine)
                }
            }
        }

        /**
         * 测量自己
         * 宽度直接占满父布局 不关心Mode
         * 高度根据子view的行数和单个子view的高度动态计算 加上竖向间隔
         * */
        val lines = mLinesView.size
        val height = getChildAt(0).measuredHeight * lines + (lines - 1) * mItemVerticalMargin + paddingTop + paddingBottom
        Log.d("FlowLayoutTag", "onMeasure widthSize = $widthSpecSize , height = $height  ,${lines} ${getChildAt(0).measuredHeight}")
        setMeasuredDimension(widthSpecSize, height.toInt())
    }

    /**
     * 第四步摆放
     * ViewGroup强制实现
     * 摆放子view
     * */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d("FlowLayoutTag", "onLayout")
        var currentLeft = paddingLeft
        var currentTop = paddingTop
        mLinesView.forEach { singleLine ->
            var lineHeight = 0
            singleLine.forEach { child ->
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight
                lineHeight = childHeight
                //调用child.layout设置位置 分别设置l 、 r 、 t 、  b
                child.layout(currentLeft, currentTop, currentLeft + childWidth, currentTop + childHeight)
                currentLeft = child.right + mItemHorizontalMargin.toInt()
            }
            currentLeft = paddingLeft
            currentTop += lineHeight + mItemVerticalMargin.toInt()
        }

    }


    /**
     * 设置数据 根据数据创建子view并且添加到ViewCroup
     * */
    fun setTextList(list: List<String>, itemClickListener: ItemClickListener? = null) {
        mItemClickListener = itemClickListener
        mData.clear()
        mData.addAll(list)
        setUpChildren()
    }

    /**
     * 判断view是否可以添加到当前行
     * @param line 当前行已添加的view
     * @param child 即将添加的view
     * @param parentWidthSize 父宽度 宽度限制
     * */
    private fun checkChildCanBeAddToLine(line: List<View>, child: View, parentWidthSize: Int): Boolean {
        var totalWidth = 0
        line.forEach {
            totalWidth += it.measuredWidth
        }
        /***
         * 如何行长度加上即将添加进来的view长度 超出限制宽度则不可以添加 否则可以添加
         * 注意计算上横向间隔宽度
         * **/
        return totalWidth + child.measuredWidth + line.size * mItemHorizontalMargin + paddingStart + paddingEnd <= parentWidthSize
    }


    /**第五步 定义功能接口**/
    interface ItemClickListener {
        fun onItemClick(text: String)
    }

}