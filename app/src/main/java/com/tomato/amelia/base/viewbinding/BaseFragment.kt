package com.tomato.amelia.base.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * author: created by yuqiaodan on 2023/1/9 10:03
 * description:Fragment基类 使用ViewBinding
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    lateinit var binding: VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = bindView(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    /**
     * eg:
     * return ResultProfileBinding.inflate(inflater, container, false)
     * */
    abstract fun bindView(inflater: LayoutInflater, container: ViewGroup?): VB

    /**
     * 初始化View 设置监听 填充初始数据等
     * */
    abstract fun initView()
}