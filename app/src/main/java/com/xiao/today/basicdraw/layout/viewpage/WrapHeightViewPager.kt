package com.xiao.today.basicdraw.layout.viewpage

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.view.children
import androidx.viewpager.widget.ViewPager
import kotlin.math.max

class WrapHeightViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var maxHeight = 0
        children.withIndex().forEach {
            val view = it.value
            measureChild(view, widthMeasureSpec, heightMeasureSpec)
            println(view.measuredHeight)
            maxHeight = max(maxHeight, view.measuredHeight)
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY))
    }
}