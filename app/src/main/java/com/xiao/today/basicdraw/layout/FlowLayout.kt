package com.xiao.today.basicdraw.layout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.xiao.today.basicdraw.dp2px
import kotlin.math.max

class FlowLayout(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 100f.dp2px
        alpha = (0xff * 0.5).toInt()
        textAlign = Paint.Align.CENTER
        style = Paint.Style.STROKE
    }

    var flowAdapter: FlowAdapter<out Any>? = null
        set(value) {
            field = value
            value?.let {
                for (i in 0 until it.count()) {
                    val view = it.onCreateView(i, this)
                    addView(view)
                }
            }
        }

    private val viewBounds = mutableMapOf<View, Rect>()

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        /**
         * 测量时考虑自己的padding和子view的margin（会影响当前ViewGroup的可用宽度）
         */

        val parentMaxWidth = MeasureSpec.getSize(widthMeasureSpec) - (paddingStart + paddingEnd)

        val adapter = flowAdapter
        if (adapter == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        } else {
            //已经使用了多少宽度
            var widthUsed = 0
            //最大宽度
            var maxWidth = 0
            //已经使用的高度
            var heightUsed = 0
            //每行最大高度
            var lineMaxHeight = 0
            //每个View的left
            var viewLeft: Int
            //每个view的top
            var viewTop: Int

            var firstLine = true

            for (index in 0 until childCount) {
                val view = getChildAt(index)
                if (index >= viewBounds.size) {
                    viewBounds[view] = Rect()
                }
                val rect = viewBounds[view]
                measureChild(view, widthMeasureSpec, heightMeasureSpec)

                val childNeedWidth = view.measuredWidth + view.marginStart + view.marginEnd
                val childNeedHeight = view.measuredHeight + view.marginTop + view.marginBottom

                if (widthUsed + childNeedWidth >= parentMaxWidth) {
                    maxWidth = max(maxWidth, widthUsed)
                    widthUsed = childNeedWidth

                    heightUsed += lineMaxHeight
                    lineMaxHeight = childNeedHeight

                    viewLeft = paddingStart + view.marginStart
                    firstLine = false
                } else {
                    widthUsed += childNeedWidth
                    lineMaxHeight = max(lineMaxHeight, childNeedHeight)
                    viewLeft = paddingStart + widthUsed - (view.measuredWidth + view.marginEnd)
                }
                viewTop = if (firstLine){
                    paddingTop + view.marginTop
                }else{
                    paddingTop + heightUsed + view.marginTop
                }
                rect?.run {
                    left = viewLeft
                    top = viewTop
                    right = left + view.measuredWidth
                    bottom = top + view.measuredHeight
                }

                if (index == childCount - 1) {
                    maxWidth = max(maxWidth, widthUsed)
                    heightUsed += lineMaxHeight
                }
            }
            setMeasuredDimension(
                maxWidth + paddingStart + paddingEnd,
                heightUsed + paddingTop + paddingBottom
            )
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        viewBounds.forEach {
            it.key.layout(it.value.left, it.value.top, it.value.right, it.value.bottom)
        }
    }

    override fun onDrawForeground(canvas: Canvas) {
        super.onDrawForeground(canvas)
        canvas.drawText(childCount.toString(), width / 2f, height / 2f, textPaint)
        textPaint.color = Color.YELLOW
        canvas.drawRect(
            paddingStart.toFloat(),
            paddingTop.toFloat(), (width - paddingEnd).toFloat(),
            (height - paddingBottom).toFloat(), textPaint
        )
    }

    /**
     * LayoutParams：保存那些以 layout_xxx 开头的属性值
     *
     * ViewGroup.LayoutParams：只会从attrs中取出基本的 layout_width 和 layout_height的值
     * 如果想要拿到layout_margin相关的值，用 ViewGroup.MarginLayoutParams
     */
    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}