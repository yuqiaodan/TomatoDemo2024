package com.tomato.amelia

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * author: created by yuqiaodan on 2023/11/17 15:23
 * description:
 */
class MainViewModel :ViewModel(){


    val testLiveData = MutableLiveData<Boolean>(false)




    fun test(){

        viewModelScope.launch(Dispatchers.Main) {
            testLiveData.value=true
            delay(100)
            testLiveData.value=true
            delay(100)
            testLiveData.value=true
            delay(100)
            testLiveData.value=true
            delay(100)
            testLiveData.value=true
            delay(100)
        }
    }




}