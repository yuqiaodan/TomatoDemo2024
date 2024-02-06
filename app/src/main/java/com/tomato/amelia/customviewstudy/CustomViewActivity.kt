package com.tomato.amelia.customviewstudy

import com.tomato.amelia.R
import com.tomato.amelia.base.databinding.BaseActivity
import com.tomato.amelia.databinding.ActivityCustomViewBinding
import com.tomato.amelia.utils.MyUtils

/**
 * 自定义viwe步骤：
 * 1.确定使用自定义组合view还是自定义view
 * 2.1 如果使用自定义view
 *      a.创建画笔 根据属性 绘制UI
 *      b.处理用户交互事件 点击/滑动/拖动
 * 2.2 如果使用自定义组合view
 *      a.获取相关属性 测量自身 以及child大小
 *      b.布局 摆放child
 *      c.处理用户交互事件 点击/滑动/拖动
 * */
class CustomViewActivity : BaseActivity<ActivityCustomViewBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_custom_view
    }
    override fun initView() {
        binding.inputView.setNumberListener(object :InputNumberView.NumberChangeListener{
            override fun onNumberChanged(number: Int) {
                MyUtils.showToast(this@CustomViewActivity,number.toString())
            }
        })
        binding.textFlowLayout.setTextList(listOf("键盘","显示器","鼠标","春夏秋冬男装","iPad","Air Pod","Macbook Pro","耳机","男鞋","女装","忽如一夜春风来","李白","千树万树梨花开","登高杜甫","上月下月中月","寂寂无名","僧推月下门","无语柳树","切记切记冷冷清清"))
    }
}