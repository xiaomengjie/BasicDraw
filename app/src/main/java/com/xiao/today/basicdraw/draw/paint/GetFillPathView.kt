package com.xiao.today.basicdraw.draw.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

@Deprecated("作用不太清楚")
class GetFillPathView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val linePath = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        linePath.rMoveTo(width/10f, width/10f)
        linePath.rLineTo(8 * width/10f, 0f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /*
        * Paint.getFillPath：根据paint的设置，计算出绘制Path或文字时的实际Path
        *
        * 实际path：指drawPath()的绘制内容的轮廓，要算上线条宽度和设置的PathEffect
        *
        * 默认情况下，线条宽度为0，没有pathEffect，原path和实际path一样
        *
        * getFillPath(Path src, Path dst)
        * src：原path
        * dst：实际path的保存位置
        * */
    }
}


enum class Op {

    DIFFERENCE, // difference：保留第一个 Path 中不与第二个 Path 重叠的部分

    INTERSECT, // intersect：保留两个 Path 重叠的部分。

    UNION, // union：合并两个 Path 的所有部分

    XOR, // xor：保留两个 Path 不重叠的部分，去除重叠的部分

    REVERSE_DIFFERENCE // reverse_difference：保留第二个 Path 中不与第一个 Path 重叠的部分

}

