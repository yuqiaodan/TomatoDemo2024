package com.tomato.amelia.databinding1

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.tomato.amelia.R
import com.tomato.amelia.databinding.FragmentOnSellBinding
import com.tomato.amelia.base.databinding.BaseVMFragment

/**
 * author: created by yuqiaodan on 2023/11/6 16:25
 * description:
 */
class OnSellFragment : BaseVMFragment<FragmentOnSellBinding, OnSellViewModel>() {
    override fun getViewModelClass(): Class<OnSellViewModel> {
        return OnSellViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_on_sell
    }

    private val mAdapter = OnSellListAdapter()

    override fun initView() {
        viewModel.loadContent()
        binding.onShellListView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        binding.onShellListView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = SizeUtils.dp2px(5f)
            }
        })
    }

    override fun observerData() {
        viewModel.loadState.observe(this) { state ->
            Log.d("OnSellTAG", "obseverData:loadState->${state} ")
        }
        viewModel.contentList.observe(this) { list ->
            Log.d("OnSellTAG", "obseverData:contentList->${list} ")
            mAdapter.setData(list)
        }
    }
}