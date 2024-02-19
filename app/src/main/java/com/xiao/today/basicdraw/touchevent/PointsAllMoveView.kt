package com.xiao.today.basicdraw.touchevent

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import com.xiao.today.basicdraw.dp2px

/**
 * 多指触摸：各自为战
 */
class PointsAllMoveView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paths = SparseArray<Path>()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f.dp2px
        strokeCap = Paint.Cap.ROUND
        color = Color.RED
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (index in 0 until paths.size()){
            canvas.drawPath(paths.valueAt(index), paint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.actionMasked){
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN-> {
                val actionIndex = event.actionIndex
                val pointerId = event.getPointerId(actionIndex)
                val path = paths.get(pointerId)?: Path()
                path.moveTo(event.getX(actionIndex), event.getY(actionIndex))
                paths.append(pointerId, path)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                for (index in 0 until event.pointerCount){
                    val pointerId = event.getPointerId(index)
                    val path = paths[pointerId]
                    path.lineTo(event.getX(index), event.getY(index))
                }
                invalidate()
            }
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP -> {
//                val pointId = event.getPointerId(event.actionIndex)
//                paths.remove(pointId)
//                invalidate()
            }
        }
        return true
    }
}

// TODO: 三指时好像有点小问题