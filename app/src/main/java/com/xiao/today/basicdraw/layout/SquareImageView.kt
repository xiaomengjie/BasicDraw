package com.xiao.today.basicdraw.layout

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min

class SquareImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    /*
    * layout中改变控件大小
    * 由于父View此时无法知道你的改变
    * 所以还是会按照测量时的大小进行布局
    * */
//    override fun layout(l: Int, t: Int, r: Int, b: Int) {
//        val size = min((b - t), (r - l))
//        super.layout(l, t, l + size, t + size)
//    }
}