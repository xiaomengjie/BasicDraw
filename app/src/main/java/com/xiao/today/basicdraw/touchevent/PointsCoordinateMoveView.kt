package com.xiao.today.basicdraw.touchevent

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.xiao.today.basicdraw.dp2px

/**
 * 多指触摸：协作型（多根手指同时起作用）
 */
class PointsCoordinateMoveView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
    }

    private val size = 200f.dp2px.toInt()

    //已经存在的偏移
    private var originalOffsetX = 0f
    private var originalOffsetY = 0f
    //当前事件中的偏移
    private var offsetX = 0f
    private var offsetY = 0f

    private var downX: Float = 0f
    private var downY: Float = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(offsetX, offsetY, offsetX + size, offsetY + size, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var sumX = 0f
        var sumY = 0f
        var pointerCount = event.pointerCount
        //POINT_UP时，需要减掉一根手指
        val isPointUp = event.actionMasked == MotionEvent.ACTION_POINTER_UP
        for (index in 0 until pointerCount){
            if (isPointUp && index == event.actionIndex){
                continue
            }
            sumX += event.getX(index)
            sumY += event.getY(index)
        }
        if (isPointUp) pointerCount--
        val focusX = sumX / pointerCount
        val focusY = sumY / pointerCount

        when(event.actionMasked){
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN,
            MotionEvent.ACTION_POINTER_UP -> {
                downX = focusX
                downY = focusY
                originalOffsetX = offsetX
                originalOffsetY = offsetY
            }
            MotionEvent.ACTION_MOVE -> {
                offsetX = focusX - downX + originalOffsetX
                offsetY = focusY - downY + originalOffsetY
                invalidate()
            }
        }

        return true
    }
}