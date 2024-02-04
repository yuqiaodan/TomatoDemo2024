package com.tomato.amelia.storage

import com.tomato.amelia.storage.BaseSp


/**
 * Created by qiaodan on 2021/8/30
 * Description:
 */
open class BaseStorage(private val id: String) {
    protected val sp = BaseSp().initWithID(id)
    fun clear() = sp.clear()

}