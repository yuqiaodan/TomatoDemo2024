package com.tomato.amelia.base.databinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * author: created by yuqiaodan on 2023/11/17 15:09
 * description:
 */
abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var binding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**方式一:直接创建binding 需要setContentView**/
        /*val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)*/
        /**方式二:通过DataBindingUtil创建binding 不需要setContentView DataBindingUtil可以通过Id来创建binding*/
        binding = DataBindingUtil.setContentView<T>(this, getLayoutId())
        initView()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()


}