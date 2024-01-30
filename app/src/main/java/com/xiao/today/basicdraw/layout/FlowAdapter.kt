package com.xiao.today.basicdraw.layout

import android.view.View
import android.view.ViewGroup

abstract class FlowAdapter<T>(val dataList: List<T>) {

    abstract fun onCreateView(position: Int, parent: ViewGroup): View

    abstract fun count(): Int
}