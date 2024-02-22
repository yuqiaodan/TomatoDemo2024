package com.tomato.amelia.customviewstudy.viewcomposite

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewpager.widget.PagerAdapter
import com.tomato.amelia.R
import com.tomato.amelia.databinding.ItemPagerBinding
import com.tomato.amelia.databinding.LayoutViewpagerBannerBinding

/**
 * author: created by tomato on 2024/2/22 19:59
 * description:
 */
class PagerBanner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    companion object {
        const val PIC_1 =
            "https://image.baidu.com/search/detail?ct=503316480&z=undefined&tn=baiduimagedetail&ipn=d&word=%E5%A3%81%E7%BA%B8&step_word=&lid=7707670699925578335&ie=utf-8&in=&cl=2&lm=-1&st=undefined&hd=undefined&latest=undefined&copyright=undefined&cs=1567327732,2965245476&os=2739840508,1630862&simid=3298166794,272062549&pn=0&rn=1&di=7308398814245683201&ln=1819&fr=&fmq=1708604096451_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&is=0,0&istype=0&ist=&jit=&bdtype=0&spn=0&pi=0&gsm=1e&objurl=https%3A%2F%2Fp4.itc.cn%2Fq_70%2Fimages03%2F20230512%2F32c7ad09b5904bea8506d74f96483000.png&rpstart=0&rpnum=0&adpicid=0&nojc=undefined&dyTabStr=MCwzLDEsMiw2LDQsNSw4LDcsOQ%3D%3D"

        const val PIC_2 =
            "https://image.baidu.com/search/detail?ct=503316480&z=undefined&tn=baiduimagedetail&ipn=d&word=%E5%A3%81%E7%BA%B8&step_word=&lid=7707670699925578335&ie=utf-8&in=&cl=2&lm=-1&st=undefined&hd=undefined&latest=undefined&copyright=undefined&cs=284390840,2897661255&os=1257643011,2555827&simid=3351446363,3946517789&pn=1&rn=1&di=7308398814245683201&ln=1819&fr=&fmq=1708604096451_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&is=0,0&istype=0&ist=&jit=&bdtype=0&spn=0&pi=0&gsm=1e&objurl=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2019-11-22%2F5dd7afa7df0fc.jpg&rpstart=0&rpnum=0&adpicid=0&nojc=undefined&dyTabStr=MCwzLDEsMiw2LDQsNSw4LDcsOQ%3D%3D"

        const val PIC_3 =
            "https://image.baidu.com/search/detail?ct=503316480&z=undefined&tn=baiduimagedetail&ipn=d&word=%E5%A3%81%E7%BA%B8&step_word=&lid=7707670699925578335&ie=utf-8&in=&cl=2&lm=-1&st=undefined&hd=undefined&latest=undefined&copyright=undefined&cs=346036330,4218769453&os=2799283359,9032211&simid=3530660283,424283833&pn=3&rn=1&di=7308398814245683201&ln=1819&fr=&fmq=1708604096451_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&is=0,0&istype=0&ist=&jit=&bdtype=0&spn=0&pi=0&gsm=1e&objurl=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F583d31f09fab4.jpg&rpstart=0&rpnum=0&adpicid=0&nojc=undefined&dyTabStr=MCwzLDEsMiw2LDQsNSw4LDcsOQ%3D%3D"

        const val PIC_4 =
            "https://image.baidu.com/search/detail?ct=503316480&z=undefined&tn=baiduimagedetail&ipn=d&word=%E5%A3%81%E7%BA%B8&step_word=&lid=7707670699925578335&ie=utf-8&in=&cl=2&lm=-1&st=undefined&hd=undefined&latest=undefined&copyright=undefined&cs=2436577790,1813052259&os=3263232915,9394116&simid=3335830669,117843114&pn=5&rn=1&di=7308398814245683201&ln=1819&fr=&fmq=1708604096451_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&is=0,0&istype=0&ist=&jit=&bdtype=0&spn=0&pi=0&gsm=1e&objurl=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fa%2F53900ad1cb8b7.jpg%3Fdown&rpstart=0&rpnum=0&adpicid=0&nojc=undefined&dyTabStr=MCwzLDEsMiw2LDQsNSw4LDcsOQ%3D%3D"

        const val PIC_5 =
            "https://image.baidu.com/search/detail?ct=503316480&z=undefined&tn=baiduimagedetail&ipn=d&word=%E5%A3%81%E7%BA%B8&step_word=&lid=7707670699925578335&ie=utf-8&in=&cl=2&lm=-1&st=undefined&hd=undefined&latest=undefined&copyright=undefined&cs=1357726446,2215687506&os=1292383456,3588541960&simid=1357726446,2215687506&pn=7&rn=1&di=7308398814245683201&ln=1819&fr=&fmq=1708604096451_R&fm=&ic=undefined&s=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&is=0,0&istype=0&ist=&jit=&bdtype=0&spn=0&pi=0&gsm=1e&objurl=https%3A%2F%2Fs2.best-wallpaper.net%2Fwallpaper%2F1920x1200%2F1909%2FCanada-Peyto-Lake-mountains-trees-snow-clouds-winter_1920x1200.jpg&rpstart=0&rpnum=0&adpicid=0&nojc=undefined&dyTabStr=MCwzLDEsMiw2LDQsNSw4LDcsOQ%3D%3D"

    }

    val picList:ArrayList<Int>
    init {
        picList = arrayListOf()
        initView()
    }

    lateinit var binding: LayoutViewpagerBannerBinding

    var adapter: PagerAdapter? = null

    fun initView() {
        binding = LayoutViewpagerBannerBinding.inflate(LayoutInflater.from(context), this, true)
        adapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return picList.size
            }

            override fun isViewFromObject(view: View, obj: Any): Boolean {
                return view == obj
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val itemBinding = ItemPagerBinding.inflate(LayoutInflater.from(context),container,false)
                itemBinding.ivCover.setImageResource(picList[position])
                container.addView(itemBinding.root)
                return itemBinding.root
            }

            override fun destroyItem(container: ViewGroup, position: Int,obj: Any) {
                container.removeView(obj as View)
            }
        }
        binding.viewPager.adapter = adapter

        setData()
    }


    fun setData(){
        picList.add(R.mipmap.pager_pic_1)
        picList.add(R.mipmap.pager_pic_2)
        picList.add(R.mipmap.pager_pic_3)
        picList.add(R.mipmap.pager_pic_4)
        picList.add(R.mipmap.pager_pic_5)
        picList.add(R.mipmap.pager_pic_6)
        picList.add(R.mipmap.pager_pic_7)

        adapter?.notifyDataSetChanged()
    }

}