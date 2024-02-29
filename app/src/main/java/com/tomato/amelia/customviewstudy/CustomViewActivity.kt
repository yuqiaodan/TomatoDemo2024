package com.tomato.amelia.customviewstudy

import android.animation.ValueAnimator
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.tomato.amelia.R
import com.tomato.amelia.base.databinding.BaseActivity
import com.tomato.amelia.customviewstudy.view.PopCircleProgress
import com.tomato.amelia.customviewstudy.viewcomposite.InputNumberView
import com.tomato.amelia.customviewstudy.viewcomposite.PagerBanner
import com.tomato.amelia.customviewstudy.viewgroup.FlowLayout
import com.tomato.amelia.customviewstudy.viewgroup.KeypadView
import com.tomato.amelia.customviewstudy.viewgroup.SlideMenu
import com.tomato.amelia.databinding.ActivityCustomViewBinding
import com.tomato.amelia.databinding.ItemPagerBinding
import com.tomato.amelia.utils.MyUtils

/**
 * 自定义viwe步骤：
 * 1.确定使用自定义组合view还是自定义view
 * 2.1 如果使用自定义view
 *      a.创建画笔 根据属性 绘制UI
 *      b.处理用户交互事件 点击/滑动/拖动
 * 2.2 如果使用自定义组合view
 *      a.获取相关属性 测量自身 以及child大小
 *      b.布局 摆放child
 *      c.处理用户交互事件 点击/滑动/拖动
 * */
class CustomViewActivity : BaseActivity<ActivityCustomViewBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_custom_view
    }

    override fun initView() {


        initBanner()

        playProgressAnim(binding.circleProgress)

        binding.slideView.setActionListener(object : SlideMenu.OnActionClickListener {
            override fun onDelete() {
                MyUtils.showToast(this@CustomViewActivity, "onDelete")
            }

            override fun onTop() {
                MyUtils.showToast(this@CustomViewActivity, "onTop")
            }

            override fun onRead() {
                MyUtils.showToast(this@CustomViewActivity, "onRead")
            }
        })


        binding.inputView.setNumberListener(object : InputNumberView.NumberChangeListener {
            override fun onNumberChanged(number: Int) {
                MyUtils.showToast(this@CustomViewActivity, number.toString())
            }
        })

        val flowDataList = listOf(
            "键盘",
            "显示器",
            "鼠标",
            "春夏秋春夏秋冬男装春夏秋冬男装春夏秋冬男装春夏秋冬男装春夏秋冬男装春夏秋冬男装春夏秋冬男装冬男装",
            "iPad",
            "Air Pod",
            "Macbook Pro",
            "耳机",
            "男鞋",
            "女装",
            "忽如一夜春风来",
            "李白",
            "千树万树梨花开",
            "登高杜甫",
            "上月下月中月",
            "寂寂无名",
            "僧推月下门",
            "无语柳树",
            "切记切记冷冷清清"
        )
        binding.textFlowLayout.setTextList(flowDataList, object : FlowLayout.ItemClickListener {
            override fun onItemClick(text: String) {
                MyUtils.showToast(this@CustomViewActivity, text)
            }
        })

        binding.keyPad.setKeypadListener(object : KeypadView.KeypadListener {
            override fun onClickNumber(num: Int) {
                MyUtils.showToast(this@CustomViewActivity, num.toString())
            }

            override fun onDelete() {
                MyUtils.showToast(this@CustomViewActivity, "Delete")
            }
        })

        testViewWidth()

    }


    /**
     * view.measuredWidth
     * 是布局测量阶段计算的宽度，用作后续布局决策的依据，但最终的宽度可能会在布局阶段调整。
     *
     * view.layoutParams.width 是 View  （在View添加到父布局之前 此值为null）
     * 在布局前由外部指定的期望宽度，它告诉布局管理器如何处理这个 View 的宽度。布局的实际结果需要结合 view.width 和 view.measuredWidth 来最终确定。
     *
     * view.width
     * 是最终在屏幕上显示的实际宽度，包括 View 的内容加上填充。
     * */
    fun testViewWidth() {

        val textView = TextView(this)
        textView.text = "测试"
        textView.setBackgroundResource(R.color.black)
        textView.setTextColor(Color.parseColor("#FFFFFF"))
        textView.gravity = Gravity.CENTER
        log(textView)
        textView.measure(MeasureSpec.makeMeasureSpec(100, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(100, MeasureSpec.AT_MOST))
        log(textView)
        textView.layout(0, 0, textView.measuredWidth, textView.measuredHeight)
        log(textView)
        binding.llContent.addView(textView)
        log(textView)

    }

    fun log(view: View) {
        Log.d(
            "TextParams", "\nwidth:${view.width} height:${view.height}" +
                    "\nmeasuredWidth:${view.measuredWidth} measuredHeight:${view.measuredHeight}" +
                    "\nlayoutParams width:${view.layoutParams?.width} layoutParams height:${view.layoutParams?.height}"
        )

    }

    fun playProgressAnim(progressBar: PopCircleProgress) {
        val anim = ValueAnimator.ofInt(0, progressBar.getMaxProgress())
        anim.interpolator = LinearInterpolator()
        anim.addUpdateListener {
            val value = it.animatedValue as Int
            progressBar.setProgress(value)
        }
        anim.duration = 5 * 1000L
        anim.repeatCount = ValueAnimator.INFINITE
        anim.start()
    }


    data class PagerItem(val title: String, val pic: Int)

    fun initBanner() {
        val picList: ArrayList<PagerItem> = arrayListOf()
        picList.add(PagerItem("第1张图片", R.mipmap.pager_pic_1))
        picList.add(PagerItem("第2张图片", R.mipmap.pager_pic_2))
        picList.add(PagerItem("第3张图片", R.mipmap.pager_pic_3))
        picList.add(PagerItem("第4张图片", R.mipmap.pager_pic_4))
        picList.add(PagerItem("第5张图片", R.mipmap.pager_pic_5))
        picList.add(PagerItem("第6张图片", R.mipmap.pager_pic_6))
        picList.add(PagerItem("第7张图片", R.mipmap.pager_pic_7))

        val adapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return picList.size
            }

            override fun isViewFromObject(view: View, obj: Any): Boolean {
                return view == obj
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val itemBinding = ItemPagerBinding.inflate(LayoutInflater.from(this@CustomViewActivity), container, false)
                val mPosition = position % picList.size
                itemBinding.ivCover.setImageResource(picList[mPosition].pic)
                container.addView(itemBinding.root)
                return itemBinding.root
            }

            override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
                container.removeView(obj as View)
            }
        }

        binding.viewBanner.setData(adapter, object : PagerBanner.BindTitleListener {
            override fun getTitle(position: Int): String {
                return picList[position].title
            }
        })


    }
}