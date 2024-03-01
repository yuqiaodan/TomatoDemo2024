package com.tomato.amelia.customviewstudy

import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tomato.amelia.base.viewbinding.BaseActivity
import com.tomato.amelia.databinding.ActivityStockBinding

class SlideDownActivity : BaseActivity<ActivityStockBinding>() {

    override fun onBindView(inflater: LayoutInflater): ActivityStockBinding {
        return ActivityStockBinding.inflate(inflater)
    }

    override fun initView() {
        val viewPager = binding.viewPager
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL); // 设置方向为垂直
        viewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("SlideTag", "onPageSelected ${position}")
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

            }
        })


        viewPager.setAdapter(object :FragmentStateAdapter(supportFragmentManager,lifecycle){
            override fun getItemCount(): Int {
               return Int.MAX_VALUE
            }

            override fun createFragment(position: Int): Fragment {
                return SlideChildFragment.createFragment(position.toString())
            }

            override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
                super.onDetachedFromRecyclerView(recyclerView)
                Log.d("SlideTag", "onDetachedFromRecyclerView")

            }
        });
    }




}