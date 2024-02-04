package com.tomato.amelia.base.databinding

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * author: created by yuqiaodan on 2023/11/17 15:14
 * description:
 */
abstract class BaseVMActivity<T : ViewDataBinding, VM : ViewModel> : BaseActivity<T>() {

    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[getViewModelClass()]
        super.onCreate(savedInstanceState)
        observerData()
    }

    abstract fun getViewModelClass(): Class<VM>

    //观察
    open fun observerData() {}

}