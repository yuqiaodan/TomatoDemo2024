package com.tomato.amelia.databinding1.api

import com.tomato.amelia.databinding1.api.bean.BaseBean
import com.tomato.amelia.databinding1.api.bean.OnShellApiBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * author: created by yuqiaodan on 2022/12/28 17:56
 * description:
 */
interface ApiService {

    companion object {
        const val BASE_URL = "https://api.sunofbeaches.com/shop/"
    }


    @GET("onSell/{page}")
    suspend fun getOnSellList(@Path("page") page: Int): BaseBean<OnShellApiBean>


    @GET("onSell/{page}")
    fun getOnSellListByCall(@Path("page") page: Int): Call<BaseBean<OnShellApiBean>>


}