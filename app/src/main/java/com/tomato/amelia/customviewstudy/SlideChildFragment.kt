package com.tomato.amelia.customviewstudy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tomato.amelia.base.viewbinding.BaseFragment
import com.tomato.amelia.databinding.FragmentSlideChildBinding

/**
 * author: created by tomato on 2024/3/1 14:36
 * description:
 */
class SlideChildFragment : BaseFragment<FragmentSlideChildBinding>() {

    var mData:String="none"
    companion object {

        fun createFragment(data: String): SlideChildFragment {
            val fragment = SlideChildFragment()
            val bundle = Bundle()
            bundle.putString("position",data)
            fragment.arguments = bundle
            return fragment

        }
    }

    override fun bindView(inflater: LayoutInflater, container: ViewGroup?): FragmentSlideChildBinding {
        return FragmentSlideChildBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding.tvCenter.text = mData
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mData =  arguments?.getString("position")?:"none"
        Log.d("SlideTag", "SlideChildFragment onCreateView: ${mData}")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SlideTag", "SlideChildFragment onViewCreated: ${mData}")
    }

    override fun onStart() {
        super.onStart()
        Log.d("SlideTag", "SlideChildFragment onStart: ${mData}")

    }
    override fun onResume() {
        super.onResume()
        Log.d("SlideTag", "SlideChildFragment onResume: ${mData}")
    }

    override fun onStop() {
        super.onStop()
        Log.d("SlideTag", "SlideChildFragment onStop: ${mData}")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SlideTag", "SlideChildFragment onDestroy: ${mData}")
    }

}