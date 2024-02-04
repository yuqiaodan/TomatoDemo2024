package com.tomato.amelia.databinding2

import com.tomato.amelia.R
import com.tomato.amelia.base.databinding.BaseVMActivity
import com.tomato.amelia.databinding.ActivityTemperatureBinding

class TemperatureActivity : BaseVMActivity<ActivityTemperatureBinding, TemperatureViewModel>() {

    override fun getViewModelClass() = TemperatureViewModel::class.java

    override fun getLayoutId() = R.layout.activity_temperature

    override fun initView() {
        viewModel.measureTheTemperature()
    }

    override fun observerData() {
        super.observerData()
        viewModel.supportTest.observe(this) {
            //支持类型
            when (it) {
                TemSensorType.BODY_TEMP -> {
                    //支持体温
                    /***
                     * 使用ViewStub比include节省资源
                     * ViewStub没有调用inflate()之前 布局资源不会被加载
                     * 而include则默认任何时候都加载布局资源
                     * */
                    if (!binding.itemBodyTemp.isInflated) {
                        binding.itemBodyTemp.viewStub?.inflate()
                    }
                }
                TemSensorType.BODY_ENV_TEMP -> {
                    //支持体温和环境温度
                    if (!binding.itemBodyAndEnvTemp.isInflated) {
                        binding.itemBodyAndEnvTemp.viewStub?.inflate()
                    }
                }
                TemSensorType.NONE -> {
                    //不支持
                    if (!binding.itemNotSupport.isInflated) {
                        binding.itemNotSupport.viewStub?.inflate()
                    }
                }
            }
        }


        viewModel.bodyTempValue.observe(this) {
            binding.floatBodyTemp = it
        }

        viewModel.envTempValue.observe(this) {
            binding.floatEnvTemp = it
        }
    }
}