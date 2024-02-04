package com.tomato.amelia.utils

import android.app.Application
import android.content.Context
import android.util.Log
import com.tomato.amelia.AppStorage
import java.util.Locale

/**
 * author: created by tomato on 2024/1/29 17:50
 * description:多语言工具
 */
object LanguageUtils {

    /**
     * 判断两个语言local是否一致
     * @return true 一致 false 不一致
     * */
    fun checkIsSameLanguageLocal(local1: Locale, local2: Locale): Boolean {
        //先判断语言代码是否一致 不一致则直接return false
        return if (local1.language == local2.language) {
            if (local1.language == Locale.CHINA.language) {
                //如果是中文 则判断地区 如果都为CN则表示 采用简体中文 ，如果都不为CN则表示采用繁体中文， 都return true
                //如果一个为CN但另一个不是CN 则return false
                (local1.country == "CN" && local2.country == "CN") || (local1.country != "CN" && local2.country != "CN")
            } else {
                true
            }
        } else {
            false
        }
    }


    /**
     * 从本地读取当前用户选择的语言local
     * 如果没有选择则使用默认
     * */
    fun getSaveLanguageLocale(): Locale {
        var locale = Locale.getDefault()
        if (AppStorage.chooseLanguageCode.isNotEmpty()) {
            locale = if (AppStorage.chooseLanguageCountry.isNotEmpty()) {
                Locale(AppStorage.chooseLanguageCode, AppStorage.chooseLanguageCountry)
            } else {
                Locale(AppStorage.chooseLanguageCode)
            }
        }
        Log.d("AppTag", "getCurrentLocale:language:  ${locale.language} country: ${locale.country} ")
        return locale
    }


    /**
     * 保存选择的语言
     * */
    fun saveLanguageLocale(locale: Locale) {
        AppStorage.chooseLanguageCode = locale.language
        if (locale.language == Locale.CHINA.language) {
            AppStorage.chooseLanguageCountry = locale.country
        } else {
            AppStorage.chooseLanguageCountry = ""
        }
    }


    /**
     *
     * 设置activity语言
     * 在收到 activity onCreate 回调后 super.onCreate之前 立刻调用
     * */
    fun setActivityLanguageLocal(context: Context, chooseLocal: Locale) {
        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(chooseLocal)
        //必须使用updateConfiguration 可recreate后实时生效
        resources.updateConfiguration(configuration, resources.displayMetrics)
        //如果使用context.createConfigurationContext(configuration)则只在重启应用后生效
        //createConfigurationContext(configuration)
    }

    /**
     * 设置应用语言
     * 在收到 Application onCreate 回调后 super.onCreate之前 立刻调用
     * */
    fun setAppLanguageLocale(app: Application, chooseLocal: Locale) {
        Locale.setDefault(chooseLocal)
        val resources = app.resources
        val configuration = resources.configuration
        configuration.setLocale(chooseLocal)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        app.createConfigurationContext(configuration)
    }


}