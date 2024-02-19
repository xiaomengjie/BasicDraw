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
 * 多指触摸：接力型（单根手指起作用，多指接力）
 */
class PointsRelayMoveView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

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

    private var pointId = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(offsetX, offsetY, offsetX + size, offsetY + size, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        /**
         * index：用于遍历所有手指
         * id：用于确定具体是哪根手指
         */
        when (event.actionMasked){
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                val actionIndex = event.actionIndex
                pointId = event.getPointerId(actionIndex)
                downX = event.getX(actionIndex)
                downY = event.getY(actionIndex)
                originalOffsetX = offsetX
                originalOffsetY = offsetY
            }
            MotionEvent.ACTION_MOVE -> {
                val findPointerIndex = event.findPointerIndex(pointId)
                offsetX = event.getX(findPointerIndex) - downX + originalOffsetX
                offsetY = event.getY(findPointerIndex) - downY + originalOffsetY
                invalidate()
            }
            MotionEvent.ACTION_POINTER_UP -> {
                /**
                 * 1、判断是否是正在跟踪的手指
                 * 2、接棒
                 */
                //抬起的是正在跟踪的手指时才进行替换
                val actionIndex = event.actionIndex
                if (event.getPointerId(actionIndex) == pointId){
                    //如果抬起的手指是最后一根手指，选倒数第二根，否则选倒数第一根
                    val newIndex = if (actionIndex == event.pointerCount - 1){
                        event.pointerCount - 2
                    }else{
                        event.pointerCount - 1
                    }
                    //重新记录
                    pointId = event.getPointerId(newIndex)
                    downX = event.getX(newIndex)
                    downY = event.getY(newIndex)
                    originalOffsetX = offsetX
                    originalOffsetY = offsetY
                }
            }
        }
        return true
    }
}