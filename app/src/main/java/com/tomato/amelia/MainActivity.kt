package com.tomato.amelia

import android.content.Intent
import android.os.Build
import android.view.MotionEvent
import android.view.View
import com.tomato.amelia.base.databinding.BaseVMActivity
import com.tomato.amelia.customviewstudy.CustomViewActivity
import com.tomato.amelia.databinding.ActivityMainBinding
import com.tomato.amelia.databinding1.TaobaoActivity
import com.tomato.amelia.utils.MyUtils
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs


/**Demo代码验证结论
 *
 * 1.CountDownTimer cancel不会调用onFinish
 * 2.ValueAnimator  cancel会调用onAnimationEnd
 * 3.LiveData postValue后立刻取value取不到
 * **/
class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>(), View.OnClickListener {


    override fun getViewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }


    override fun initView() {
        startActivity(Intent(this, CustomViewActivity::class.java))

        setLog(
            "brand:${Build.BRAND}\n" +
                    "os_ver:${Build.VERSION.RELEASE}\n"
        )

        binding.viewModel = viewModel
        binding.btnTest.setOnClickListener(this)
        binding.btnDatabinding1.setOnClickListener(this)
        binding.btnDatabinding2.setOnClickListener(this)
        binding.btnCustomView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnTest -> {

                MyUtils.dip2px(5f)

                //A应用中唤醒部分代码逻辑
                /*val lIntent = packageManager.getLaunchIntentForPackage("com.zyzsy.wqccc")
                if (lIntent != null) {
                    //inten可用来在两个APP间传递数据
                    lIntent.putExtra("testdata", "getLaunchIntentForPackage")
                    //setFlags看自己情况使用，也可以不调用
                    lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(lIntent)
                }*/
                //A应用唤醒部分代码逻辑
                /*val intent2 = Intent(Intent.ACTION_MAIN)
                */
                /**知道要跳转应用的包命与目标Activity *//*
                val componentName = ComponentName("com.zyzsy.wqccc", "com.idiom.pure.MainActivity")
                intent2.setComponent(componentName)
                intent2.putExtra("testdata", "指定activity") //这里Intent传值
                startActivity(intent2)*/
            }

            binding.btnDatabinding1 -> {
                startActivity(Intent(this, TaobaoActivity::class.java))
            }

            binding.btnDatabinding2 -> {
                //startActivity(Intent(this, TemperatureActivity::class.java))
            }

            binding.btnCustomView -> {
                //自定义view
                startActivity(Intent(this, CustomViewActivity::class.java))

            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }

    fun test(time1: Long, time2: Long): Int {
        val formatDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString1 = "${formatDate.format(time1)} 00:00:00"
        val dateString2 = "${formatDate.format(time2)} 00:00:00"
        val formatTimeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val timestamp1 = formatTimeStamp.parse(dateString1)?.time ?: 0L
        val timestamp2 = formatTimeStamp.parse(dateString2)?.time ?: 0L
        if (dateString1 == dateString2) {
            return 0
        } else {
            return (abs(timestamp2 - timestamp1) / 86400000L).toInt()
        }
    }


    private fun setLog(text: String) {
        binding.tvLog.text = "${binding.tvLog.text}\n${text}"
    }

}