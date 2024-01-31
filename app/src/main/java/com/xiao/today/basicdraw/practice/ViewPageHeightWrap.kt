package com.xiao.today.basicdraw.practice

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

class ViewPageHeightWrap(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}