package com.tomato.amelia.databinding1.api.bean

/**
 * author: created by yuqiaodan on 2022/12/28 18:06
 * description:
 */
data class BaseBean<T>(
    val code: Int,
    //@SerializedName("data")
    val data: T,
    val message: String,
    val success: Boolean
) {
  /*  companion object {
        const val SUCCESS_CODE = 10000
    }*/

    /*fun apiData(): T {
        if (code == SUCCESS_CODE) {
            return data
        } else {
            throw ApiException(code, message)
        }
    }*/
}