package com.tomato.amelia.databinding1.api

import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.tomato.amelia.databinding1.api.bean.BaseBean
import com.tomato.amelia.databinding1.api.bean.OnShellApiBean
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * author: created by yuqiaodan on 2022/12/28 17:56
 * description:
 */
object RetrofitClient {


    val TAG = "RetrofitClient"

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            LoggingInterceptor.Builder()
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request(TAG)
                .response(TAG)
                .build()
        )
        .callTimeout(30, TimeUnit.SECONDS)
        .build()


    val retrofit = Retrofit.Builder()
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val apiService= retrofit.create(ApiService::class.java)



    fun getContentList(page:Int){

        apiService.getOnSellListByCall(page).enqueue(object : Callback<BaseBean<OnShellApiBean>>{
            override fun onResponse(call: Call<BaseBean<OnShellApiBean>>, response: Response<BaseBean<OnShellApiBean>>) {

            }

            override fun onFailure(call: Call<BaseBean<OnShellApiBean>>, t: Throwable) {

            }
        })


    }



}