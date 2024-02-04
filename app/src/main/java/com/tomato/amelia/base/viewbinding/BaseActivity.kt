package com.tomato.amelia.base.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * author: created by yuqiaodan on 2023/1/9 09:46
 * description:Activity基类 使用ViewBinding
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = onBindView(layoutInflater)
        setContentView(binding.root)
        initView()
    }


    /**
     * eg:
     * return ActivityMainBinding.inflate(inflater)
     * */
    abstract fun onBindView(inflater: LayoutInflater): VB


    /**
     * 初始化View 设置监听 填充初始数据等
     * */
    abstract fun initView()


}