package com.tomato.amelia.utils

import android.content.Context
import android.content.SharedPreferences
import com.tomato.amelia.App

/**
 * author: created by tomato on 2024/1/29 17:35
 * description:
 * object AppStorage : SpUtils() {
 *
 *     val spUtils = initWithName("drama_app")
 *
 *     var isGuideChooseLanguage:Boolean
 *         get() = spUtils.decodeBool("isGuideChooseLanguage")
 *         set(value) {
 *             spUtils.encode("isGuideChooseLanguage", value)
 *         }
 *
 * }
 *
 */
open class SpUtils {

    private var sp: SharedPreferences? = null

    /**
     * 初始化sp
     */
    fun initWithName(name: String): SpUtils {
        sp = App.instance.getSharedPreferences(name, Context.MODE_PRIVATE)
        return this
    }

    fun encode(key: String, any: Any?) {
        when (any) {
            is Int -> {
                sp?.edit()?.putInt(key, any)?.apply()
            }
            is Long -> {
                sp?.edit()?.putLong(key, any)?.apply()
            }
            is String -> {
                sp?.edit()?.putString(key, any)?.apply()
            }
            is Boolean -> {
                sp?.edit()?.putBoolean(key, any)?.apply()
            }
            is Float -> {
                sp?.edit()?.putFloat(key, any)?.apply()
            }
            else -> {
                //MyAppUtils.showToast(App.instance,"SpUtils error type")
               // throw IllegalStateException("error type")
            }
        }

    }

    fun decodeBool(key: String, defaultValue: Boolean = false): Boolean {
        return sp?.getBoolean(key, defaultValue) ?: defaultValue
    }

    fun decodeLong(key: String, defaultValue: Long = 0L): Long {
        return sp?.getLong(key, defaultValue) ?: defaultValue
    }

    fun decodeInt(key: String, defaultValue: Int = 0): Int {
        return sp?.getInt(key, defaultValue) ?: defaultValue
    }

    fun decodeFloat(key: String, defaultValue: Float = 0f): Float {
        return sp?.getFloat(key, defaultValue) ?: defaultValue
    }

    fun decodeString(key: String, defaultValue: String = ""): String {
        return sp?.getString(key, defaultValue) ?: defaultValue
    }

    fun clear() {
        sp?.edit()?.clear()?.apply()
    }




}