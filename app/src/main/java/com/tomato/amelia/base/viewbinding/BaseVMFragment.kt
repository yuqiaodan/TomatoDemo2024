package com.tomato.amelia.base.viewbinding

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

/**
 * author: created by yuqiaodan on 2023/1/9 10:15
 * description:使用ViewModel的Fragment
 */
abstract class BaseVMFragment<VB : ViewBinding, VM : ViewModel> : BaseFragment<VB>() {

    lateinit var viewModel: VM
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(getViewModel())
        super.onViewCreated(view, savedInstanceState)
        initDataObserver()
    }

    abstract fun getViewModel(): Class<VM>

    open fun initDataObserver() {

    }


}