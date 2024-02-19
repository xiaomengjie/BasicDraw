package com.xiao.today.basicdraw.touchevent

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.OverScroller
import androidx.core.view.children
import kotlin.math.abs

class TwoPageViewGroup(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private val viewConfiguration = ViewConfiguration.get(context)
    private val pagingSlop = viewConfiguration.scaledPagingTouchSlop
    private val scaledMaximumFlingVelocity = viewConfiguration.scaledMaximumFlingVelocity.toFloat()
    private val scaledMinimumFlingVelocity = viewConfiguration.scaledMinimumFlingVelocity.toFloat()

    private val velocityTracker = VelocityTracker.obtain()

    private var isScrolling = false

    private var downX: Float = 0f
    private var downY: Float = 0f
    private var downScrollX: Float = 0f

    private val overScroller = OverScroller(context)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        val top = 0
        for (view in children) {
            view.layout(left, top, left + width, top + height)
            left += width
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        //事件开始时，重置速度追踪器
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear()
        }
        //事件添加到追踪器
        velocityTracker.addMovement(event)

        //拦截事件判断，当滑动距离大于了页面切换距离时，拦截事件
        var result = false
        when (event.actionMasked) {
            //在未拦截事件时，父view的onTouchEvent方法不回调用
            //所以需要在onInterceptTouchEvent中记录按下的位置
            //down时记录下按下的位置和已经滑动的距离
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                downScrollX = scrollX.toFloat()
            }
            //是否拦截事件判断
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - downX
                //大于系统定义的页面切换距离，拦截事件并且让父View不要拦截事件
                if (abs(dx) > pagingSlop) {
                    parent.requestDisallowInterceptTouchEvent(true)
                    result = true
                }
            }
        }
        return result
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //如果是按在父View上，事件会调用父View的onTouchEvent方法
        //所以在父View中也需要在down事件记录位置
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear()
        }
        velocityTracker.addMovement(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                //记录按下时已经滑动了多少距离
                downScrollX = scrollX.toFloat()
            }

            MotionEvent.ACTION_MOVE -> {
                //计算滑动距离，并控制在0 - width之间
                val dx = (downX - event.x + downScrollX).toInt()
                    .coerceAtLeast(0).coerceAtMost(width)
                scrollTo(dx, 0)
            }

            //抬起时，根据滑动速度，判断道理该显示哪个View
            MotionEvent.ACTION_UP -> {
                //计算速度，得到水平速度xVelocity
                velocityTracker.computeCurrentVelocity(1000, scaledMaximumFlingVelocity)
                val xVelocity = velocityTracker.xVelocity
                //判断显示哪一个page（因为支持两个View scrollX在0到width之间）
                val targetPage = if (abs(xVelocity) < scaledMinimumFlingVelocity) {
                    //速度小于最小快速滑动速度时，根据已经滑动的距离判断
                    if (scrollX < width / 2) 0 else 1
                } else {
                    //速度大于最小滑动速度时，根据滑动方向判断
                    if (xVelocity < 0) 1 else 0
                }
                //根据目标view，得到还需要滑动的距离
                val scrollDistance = if (targetPage == 1) width - scrollX else -scrollX
                //开始滑动
                overScroller.startScroll(scrollX, 0, scrollDistance, 0)
                postInvalidateOnAnimation()
            }
        }
        return true
    }

    override fun computeScroll() {
        if (overScroller.computeScrollOffset()){
            scrollTo(overScroller.currX, overScroller.currY)
            postInvalidateOnAnimation()
        }
    }
}