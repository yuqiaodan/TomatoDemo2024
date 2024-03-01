package com.tomato.amelia.customviewstudy.viewcomposite

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private var mAdapter: BannerAdapter? = null

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
                Log.d("PagerBanner", "onPageSelected:$position ")
                val itemPosition = position % (mAdapter?.getItemCount() ?: 1)
                binding.tvTitle.text = mAdapter?.getTitle(itemPosition) ?: ""
                //切换指示器
                updateIndicator(itemPosition)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    fun setAdapter(adapter: BannerAdapter) {

        mAdapter = adapter
        //提前加载左右2个
        binding.viewPager.offscreenPageLimit=5

        binding.viewPager.adapter = mAdapter
        mAdapter?.notifyDataSetChanged()
        val startIndex = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % adapter.getItemCount())
        //默认从 Int.MAX_VALUE 中间开始 这样左右可以做到伪无限 但要减去余数的偏移量 以从0开始
        binding.viewPager.setCurrentItem(startIndex, false)
        binding.tvTitle.text = adapter.getTitle(0)
        initIndicator(0)
    }

    private fun initIndicator(startPosition: Int) {
        mAdapter?.let { adapter ->
            val count = adapter.getItemCount()
            binding.llIndicator.removeAllViews()
            for (i in 0 until count) {
                val ponit = View(context)
                ponit.tag = i
                if (startPosition == i) {
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

    private fun updateIndicator(position: Int) {
        val indicatorCount = binding.llIndicator.childCount
        if (indicatorCount > 0) {
            for (i in 0 until binding.llIndicator.childCount) {
                val point = binding.llIndicator[i]
                if (position == point.tag) {
                    point.setBackgroundColor(Color.parseColor("#FF0000"))
                } else {
                    point.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
            }
        }
    }

    /**
     * 实现无限轮播思路：
     * 思路1：设置 getCount()为 Int.MAX_VALUE  从中间开始 左右都为10亿 每秒滑动1次 也需要11574天(31.7年)才能滑动完 可以理解为无限了（伪无限）
     * 思路2：设置数据 [1，2，3]为 [3(补)，1，2，3，1(补)] 在切换位补位的1和3时 使用viewPager.setCurrentItem(0, false)切换到实际的位置（真无限）
     * 推荐思路1 方便实现 思路2虽然是真无限，但逻辑相对比较复杂，且可以看到重复加载了两个布局3(补)和1(补),运行效率较低
     * */
    abstract class BannerAdapter : PagerAdapter() {

        final override fun getCount(): Int {
            return Int.MAX_VALUE
        }

        abstract fun getItemCount(): Int

        abstract fun getItemView(container: ViewGroup, position: Int): Any

        abstract fun getTitle(position: Int): String

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        final override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val itemPosition = position % getItemCount()
            return getItemView(container, itemPosition)
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(obj as View)
        }

    }


}