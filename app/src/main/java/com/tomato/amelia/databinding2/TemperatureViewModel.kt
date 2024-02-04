package com.tomato.amelia.databinding2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * author: created by yuqiaodan on 2023/11/17 16:22
 * description:
 */
class TemperatureViewModel : ViewModel() {

    //支持什么数据
    val supportTest= MutableLiveData<TemSensorType>()
    //具体数值
    //体温
    val bodyTempValue = MutableLiveData<Float>()

    //环境温度
    val envTempValue = MutableLiveData<Float>()


    fun measureTheTemperature(){
        supportTest.value=TemSensorType.BODY_ENV_TEMP
        bodyTempValue.value=36.8f
        envTempValue.value=11.8f
    }

}