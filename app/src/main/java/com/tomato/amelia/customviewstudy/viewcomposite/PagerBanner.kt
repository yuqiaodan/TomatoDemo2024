package com.tomato.amelia.customviewstudy.viewcomposite

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.get
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tomato.amelia.databinding.LayoutViewpagerBannerBinding
import com.tomato.amelia.utils.MyUtils

/**
 * author: created by tomato on 2024/2/22 19:59
 * description:
 */
class PagerBanner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var mAdapter: PagerAdapter? = null

    private var mTitleBinder: BindTitleListener? = null

    init {
        initView()
        initEvent()
    }

    lateinit var binding: LayoutViewpagerBannerBinding

    private fun initView() {
        binding = LayoutViewpagerBannerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun initEvent() {

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                binding.tvTitle.text = mTitleBinder?.getTitle(position) ?: ""
                //切换指示器
                updateIndicator()
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    fun setData(adapter: PagerAdapter, listener: BindTitleListener) {
        mAdapter = adapter
        mTitleBinder = listener
        binding.viewPager.adapter = mAdapter
        binding.viewPager.setCurrentItem(0, false)
        binding.tvTitle.text = listener.getTitle(0)
        mAdapter?.notifyDataSetChanged()
        initIndicator()
    }

    private fun initIndicator() {
        mAdapter?.let { adapter ->
            val count = adapter.count
            binding.llIndicator.removeAllViews()
            for (i in 0 until count) {
                val ponit = View(context)
                ponit.tag = i
                if (binding.viewPager.currentItem == i) {
                    ponit.setBackgroundColor(Color.parseColor("#FF0000"))
                } else {
                    ponit.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                val layoutParams = LinearLayout.LayoutParams(MyUtils.dip2px(10f).toInt(), MyUtils.dip2px(10f).toInt())
                layoutParams.setMargins(MyUtils.dip2px(5f).toInt(), 0, MyUtils.dip2px(10f).toInt(), 0)
                ponit.layoutParams = layoutParams
                binding.llIndicator.addView(ponit)
            }
        }
    }

    private fun updateIndicator() {
        val indicatorCount = binding.llIndicator.childCount
        if (indicatorCount > 0) {
            for (i in 0 until binding.llIndicator.childCount) {
                val point = binding.llIndicator[i]
                if (binding.viewPager.currentItem == point.tag) {
                    point.setBackgroundColor(Color.parseColor("#FF0000"))
                } else {
                    point.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
            }
        }
    }


    interface BindTitleListener {

        fun getTitle(position: Int): String

    }

    /**
     * 实现无限轮播思路：
     * 思路1：设置 getCount()为 Int.MAX_VALUE  从中间开始 左右都为10亿 每秒滑动1次 也需要11574天才能滑动完 可以理解为无限了（伪无限）
     * 思路2：设置数据 [1，2，3]为 [3(补)，1，2，3，1(补)] 在切换位补位的1和3时 使用viewPager.setCurrentItem(0, false)切换到实际的位置（真无限）
     * 推荐思路1 方便实现
     * 思路2虽然是真无限，但逻辑相对比较复杂，且可以看到多加载了两个布局3(补)和1(补),运行效率较低
     * */

}