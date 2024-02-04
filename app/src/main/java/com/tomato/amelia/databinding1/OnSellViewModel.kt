package com.tomato.amelia.databinding1

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomato.amelia.databinding1.api.RetrofitClient
import com.tomato.amelia.databinding1.api.bean.OnShellApiBean
import com.tomato.amelia.databinding1.api.bean.OnShellItem
import com.tomato.amelia.databinding1.bean.LoadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * author: created by yuqiaodan on 2023/11/6 16:26
 * description:
 */
class OnSellViewModel : ViewModel() {

    private var currentPage = 1

    val loadState by lazy {
        MutableLiveData<LoadState>()
    }

    val contentList by lazy {
        MutableLiveData<MutableList<OnShellItem>>()
    }


    //加载数据
    fun loadContent() {
        Log.d("OnSellTAG", "loadContent")
        val page = currentPage
        loadState.value = LoadState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = getOnShellList(page)
                val resultList=data.tbk_dg_optimus_material_response.result_list.map_data
                if(resultList.isEmpty()){
                    loadState.postValue(LoadState.EMPTY)
                }else{
                    contentList.postValue(resultList)
                    loadState.postValue(LoadState.SUCCESS)
                    Log.d("OnSellTAG", "loadContent 数量:${resultList.size} ")
                }
                currentPage++
            }catch (e:Exception){
                loadState.postValue(LoadState.ERROR)

            }

        }
    }

    suspend fun getOnShellList(page: Int): OnShellApiBean {
        return RetrofitClient.apiService.getOnSellList(page).data
    }

}