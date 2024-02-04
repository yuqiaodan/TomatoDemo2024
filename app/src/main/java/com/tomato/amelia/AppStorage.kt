package com.tomato.amelia

import com.tencent.mmkv.MMKV

/**
 * author: created by tomato on 2024/2/4 15:42
 * description:
 */
object AppStorage {
    val spUtils = MMKV.mmkvWithID("drama_app")

    var chooseLanguageCode: String
        get() = spUtils.decodeString("chooseLanguageCode")?:""
        set(value) {
            spUtils.encode("chooseLanguageCode", value)
        }

    var chooseLanguageCountry: String
        get() = spUtils.decodeString("chooseLanguageCountry")?:""
        set(value) {
            spUtils.encode("chooseLanguageCountry", value)
        }

}