package com.tomato.amelia.base.databinding

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * author: created by yuqiaodan on 2023/11/6 14:48
 * description:Fragment基类 有ViewModel
 */
abstract class BaseVMFragment<T : ViewDataBinding, VM : ViewModel> : BaseFragment<T>() {

    protected lateinit var viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //创建ViewModel
        initViewModel()
        super.onViewCreated(view, savedInstanceState)
        //观察数据变化、更新UI
        observerData()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(getViewModelClass())
    }

    abstract fun getViewModelClass(): Class<VM>

    open fun observerData() {}


}