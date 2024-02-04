package com.tomato.amelia.base.viewbinding

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

/**
 * author: created by yuqiaodan on 2023/1/9 10:12
 * description:使用ViewModel的Activity
 */
abstract class BaseVMActivity<VB : ViewBinding, VM : ViewModel> : BaseActivity<VB>() {

    lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(getViewModel())
        super.onCreate(savedInstanceState)
        initDataObserver()
    }

    abstract fun getViewModel(): Class<VM>


    open fun initDataObserver() {

    }
}