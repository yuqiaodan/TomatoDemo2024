package com.tomato.amelia.base.databinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * author: created by yuqiaodan on 2023/11/6 14:29
 * description: Fragment基类 无ViewModel
 */
abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    protected lateinit var binding: T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        /**通过DataBindingUtil可以通过布局Id创建databinding
         * 暂未发现通过id创建viewbinding方法 要么反射 要么交由子类创建
         *  **/

        /**方式一:通过DataBindingUtil.bind直接根据view创建binding  但bind返回结果可为空  对于已创建view的 例如adapter popup可考虑使用*/
        /*val view = inflater.inflate(getSubLayoutId(), container, false)
        binding = DataBindingUtil.bind<T>(view)*/

        /**方式二:通过DataBindingUtil.inflate直接根据布局Id创建binding  返回结果不可为空 推荐使用*/
        binding = DataBindingUtil.inflate<T>(inflater, getLayoutId(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //填充初始数据  设置监听等
        initView()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()


}