package com.xiao.today.basicdraw.touchevent

import android.animation.ValueAnimator
import android.content.ClipData
import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

class DragListenerViewGroup(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs), View.OnDragListener{

    private val row = 2
    private val column = 4

    private var dragView: View? = null

    private val sortChildren = mutableListOf<View>()

    private var enterView: View? = null

    init {
        isChildrenDrawingOrderEnabled = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        measureChildren(
            MeasureSpec.makeMeasureSpec(width/row, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height/column, MeasureSpec.EXACTLY),
        )
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left: Int
        var top: Int
        val viewWidth = width / row
        val viewHeight = height / column
        for ((index, view) in children.withIndex()){
            left = index % 2 * viewWidth
            top = index / 2 * viewHeight
            view.layout(0, 0, viewWidth, viewHeight)
            view.translationX = left.toFloat()
            view.translationY = top.toFloat()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        for (view in children){
            sortChildren.add(view)
            view.setOnLongClickListener {
                dragView = it
                //长按开启拖拽
                it.startDragAndDrop(
                    ClipData.newPlainText("drag_success", "drag_success"),
                    DragShadowBuilder(it),
                    view,
                    0)
                true
            }
            view.setOnDragListener(this)
        }
    }

    override fun onDrag(view: View, event: DragEvent): Boolean {
        //每一个设置了拖拽监听器的View都会收到事件
        when(event.action){
            //拖拽开始
            DragEvent.ACTION_DRAG_STARTED -> {
                if (view == event.localState){
                    view.visibility = View.INVISIBLE
                }
            }
            //拖拽结束
            DragEvent.ACTION_DRAG_ENDED -> {
                if (view == event.localState){
                    view.visibility = View.VISIBLE
                }
            }
            //推拽点进入view
            DragEvent.ACTION_DRAG_ENTERED -> {
                if (view != event.localState && enterView == null){
                    enterView = view
                    sort(view)
                }
            }
            //推拽点进退出view
            DragEvent.ACTION_DRAG_EXITED -> {
                if (view == enterView){
                    enterView = null
                }
            }
            //放下
            DragEvent.ACTION_DROP -> {

            }
        }
        return true
    }

    private fun sort(view: View) {
        var targetIndex = -1
        var removeIndex = -1
        for ((index, child) in sortChildren.withIndex()){
            if (child == dragView){
                removeIndex = index
            }else if (child == view){
                targetIndex = index
            }
        }
        sortChildren.removeAt(removeIndex)
        sortChildren.add(targetIndex, dragView!!)
        var left: Int
        var top: Int
        val viewWidth = width / row
        val viewHeight = height / column
        for ((index, child) in sortChildren.withIndex()){
            left = index % 2 * viewWidth
            top = index / 2 * viewHeight
            child.animate().translationX(left.toFloat()).translationY(top.toFloat()).setDuration(100)
        }
    }
}