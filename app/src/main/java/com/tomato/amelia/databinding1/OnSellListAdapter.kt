package com.tomato.amelia.databinding1

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tomato.amelia.R
import com.tomato.amelia.databinding.ItemOnShellListBinding
import com.tomato.amelia.databinding1.api.bean.OnShellItem

/**
 * author: created by yuqiaodan on 2023/1/3 17:46
 * description:促销商品列表UI适配器
 */
class OnSellListAdapter : RecyclerView.Adapter<OnSellListAdapter.InnerHolder>() {

    companion object {
        /**
         * BindingAdapter必须是静态方法或者全局方法
         * 静态方法：java需要写为static方法
         * kotlin需要写在伴生对象中
         * 全局方法:写在class外
         * */
        @JvmStatic
        @BindingAdapter("goodsImage")
        fun setUpImage(iv: ImageView, goodsImage: String?) {
            if (goodsImage != null) {
                //加载图片
                Glide.with(iv).load("https:${goodsImage}").into(iv)
            } else {
                //错误站位图
            }
        }
    }


    private val mContent = arrayListOf<OnShellItem>()

    fun setData(newList: List<OnShellItem>) {
        mContent.clear()
        mContent.addAll(newList)
        notifyDataSetChanged()
    }

    class InnerHolder(val binding: ItemOnShellListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {

        val binding = DataBindingUtil.inflate<ItemOnShellListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_on_shell_list,
            parent,
            false
        )
        return InnerHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val item = mContent[position]
        //绑定填充数据
        holder.binding.itemData = item
        //绑定事件 例如点击、长按、触摸等
        holder.binding.eventHandler = EventHandler(item)

        holder.binding.tvOriginPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG

    }

    override fun getItemCount(): Int {
        return mContent.size
    }

    inner class EventHandler(val itemData: OnShellItem) {
        /**
         * 监听式事件
         * 监听式必须有原设置监听相同的固定参数 不能多也不能少
         * 例如：这里的点击事件参数就必须只有一个view：View
         * public interface OnClickListener {
         *         /**
         *          * Called when a view has been clicked.
         *          *
         *          * @param v The view that was clicked.
         *          */
         *         void onClick(View v);
         *     }
         *
         * 可以通过给EventHandler构造函数添加参数itemData 确定数据
         * */
        fun onItemClick(view: View) {
            Log.d("OnSellListAdapter", "onItemClick:${itemData.title}")

            //var a: Int? = null
            //val b = if (a == null) 1.also { a = it } else a
        }

        /**
         * 引用式事件 可以传任意参数
         * 比较灵活 推荐此方式
         * */
        fun onImageClick(imageView: View, title: String?, id: String?) {
            Log.d("OnSellListAdapter", "onImageClick id: ${id}  title:${title} ")
        }

        /**
         * 引用式事件 可以传任意参数
         * 长按事件需要返回一个布尔类型
         * */
        fun onImageLongClick(title: String?, id: String?):Boolean{
            Log.d("OnSellListAdapter", "onImageLongClick id: ${id}  title:${title}")
            return true
        }
    }
}