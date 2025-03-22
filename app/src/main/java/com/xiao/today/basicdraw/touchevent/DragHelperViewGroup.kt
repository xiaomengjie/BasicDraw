package com.xiao.today.basicdraw.touchevent

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper

class DragHelperViewGroup(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private val row = 2
    private val column = 4

    private val viewDragHelper = ViewDragHelper.create(this, DragCallback())

    private var capturedLeft: Float = 0f
    private var capturedTop: Float = 0f

    private var capturedView: View? = null

    private val sortChildren = mutableListOf<View?>()

    private var insertPosition = -1

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        /**
         * 使用ViewDragHelper处理拦截
         */
        return viewDragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        /**
         * 使用ViewDragHelper处理事件
         */
        viewDragHelper.processTouchEvent(event)
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        measureChildren(
            MeasureSpec.makeMeasureSpec(width / row, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height / column, MeasureSpec.EXACTLY),
        )
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left: Int
        var top: Int
        val viewWidth = width / row
        val viewHeight = height / column
        for ((index, view) in children.withIndex()) {
            sortChildren.add(view)
            left = index % 2 * viewWidth
            top = index / 2 * viewHeight
            view.layout(left, top, left + viewWidth, top + viewHeight)
        }
    }

    override fun computeScroll() {
        /**
         * continueSettling：偏移处理
         */
        if (viewDragHelper.continueSettling(true)) {
            postInvalidateOnAnimation()
        } else {
            capturedView?.elevation = 0f
        }
    }

    private inner class DragCallback : ViewDragHelper.Callback() {
        /**
         * 手指触摸到view时触发，true表示view跟着手指移动
         * false不移动
         */
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return true
        }

        /**
         * ViewCompat.offsetLeftAndRight(mCapturedView, clampedX - oldLeft);
         * 控制左右的偏移量
         */
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            //将左右移动范围限制在ViewGroup之内
            val maxWidth = width - child.width
            return left.coerceAtLeast(0).coerceAtMost(maxWidth)
        }

        /**
         * ViewCompat.offsetTopAndBottom(mCapturedView, clampedY - oldTop);
         * 控制上下的偏移量
         */
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            //将上下移动范围限制在ViewGroup之内
            val maxHeight = height - child.height
            return top.coerceAtLeast(0).coerceAtMost(maxHeight)
        }

        /**
         * view被拖起来时调用
         */
        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            capturedView = capturedChild
//            capturedChild.elevation = elevation + 1
            //记录被拖动的view的初始位置
            capturedLeft = capturedChild.left.toFloat()
            capturedTop = capturedChild.top.toFloat()
        }

        /**
         * view的位置改变时调用（重排）
         */
        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            val tempChildren = sortChildren
            insertPosition = calculatePosition(left, top, changedView.width, changedView.height)
            val changeViewPosition = tempChildren.indexOf(changedView)
            if (changeViewPosition < insertPosition){
                //前移
                val view = tempChildren[changeViewPosition]
                for (index in changeViewPosition + 1 ..  insertPosition){
                    tempChildren[index - 1] = tempChildren[index]
                }
                tempChildren[insertPosition] = view
            }else if (changeViewPosition > insertPosition){
                //后移
                val view = tempChildren[changeViewPosition]
                for (index in changeViewPosition downTo insertPosition + 1){
                    tempChildren[index] = tempChildren[index - 1]
                }
                tempChildren[insertPosition] = view
            }
            if (changeViewPosition != insertPosition){
                var left: Int
                var top: Int
                val viewWidth = width / row
                val viewHeight = height / column
                for ((index, view) in tempChildren.withIndex()) {
                    left = index % 2 * viewWidth
                    top = index / 2 * viewHeight
                    if (changedView == view) continue
                    view?.layout(left, top, left + viewWidth, top + viewHeight)
                }
            }
        }

        private fun calculatePosition(left: Int, top: Int, width: Int, height: Int): Int {
            val layer = top / height
            val remainder = top % height
            return if (left < width / 2){
                if (remainder < height / 2){
                    layer * 2
                }else{
                    (layer + 1) * 2
                }
            }else{
                if (remainder < height / 2){
                    layer * 2 + 1
                }else{
                    (layer + 1) * 2 + 1
                }
            }
        }

        /**
         * view释放，松手后调用
         * xvel，yvel：速率值
         */
        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            /**
             * settleCapturedViewAt：就是调用 startScroll 开启滑动
             */
            val left = insertPosition % 2 * (width / row)
            val top = insertPosition / 2 * (height / column)
            viewDragHelper.settleCapturedViewAt(left, top)
            postInvalidateOnAnimation()
        }
    }
}