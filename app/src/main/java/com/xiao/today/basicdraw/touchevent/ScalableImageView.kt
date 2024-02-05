package com.xiao.today.basicdraw.touchevent

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import com.xiao.today.basicdraw.R
import com.xiao.today.basicdraw.dp2px
import com.xiao.today.basicdraw.loadAvatar

class ScalableImageView(context: Context, attrs: AttributeSet?) : View(context, attrs),
    GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private val defaultWidth = 300f.dp2px

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val bitmap: Bitmap

    private var offsetX: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var offsetY: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var smallScale: Float = 0f
    private var bigScale: Float = 0f

    var currentScale = 1f
        set(value) {
            field = value
            invalidate()
        }

    private var enabledMove: Boolean = true

    /**
     * GestureDetectorCompat：手势监听器，监听用户各种操作（点击、滑动等）
     *
     */
    private val gestureDetectorCompat = GestureDetectorCompat(context, this).apply {
        setOnDoubleTapListener(this@ScalableImageView)
    }

    private val overScroller = OverScroller(context, AccelerateDecelerateInterpolator())

    init {
        bitmap = context.loadAvatar(R.drawable.avatar, defaultWidth.toInt())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        resetOffset()

        calculateScale()
    }

    private fun calculateScale() {
        val bitmapRadio = bitmap.width / bitmap.height.toFloat()
        val viewRadio = width / height.toFloat()
        if (bitmapRadio > viewRadio) {
            smallScale = width / bitmap.width.toFloat()
            bigScale = height / bitmap.height.toFloat()
        } else {
            smallScale = height / bitmap.height.toFloat()
            bigScale = width / bitmap.width.toFloat()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val save = canvas.save()
        canvas.translate(offsetX, offsetY)
        canvas.scale(currentScale, currentScale, bitmap.width / 2f, bitmap.height / 2f)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.restoreToCount(save)
    }

    private fun resetOffset() {
        offsetX = (width - bitmap.width) / 2f
        offsetY = (height - bitmap.height) / 2f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetectorCompat.onTouchEvent(event)
    }

    /**
     * ACTION_DOWN事件时回调，返回值表示是否消耗事件
     */
    override fun onDown(e: MotionEvent): Boolean {
        return e.actionMasked == MotionEvent.ACTION_DOWN
    }

    /**
     * 按下，100ms后触发
     */
    override fun onShowPress(e: MotionEvent) {
    }

    /**
     * 点击（有长按监听时400ms以内触发）
     */
    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    /**
     * 快速滑动
     * velocityX、velocityY：速率
     */
    override fun onFling(
        downEvent: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    /**
     * 相当于onMove：手指移动时调用
     *
     * distanceX，distanceY：移动距离（move1 - move2）
     */
    override fun onScroll(
        downEvent: MotionEvent?,
        currentEvent: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return if (enabledMove){
            offsetX -= distanceX
            offsetY -= distanceY
            offsetX = when{
                offsetX <= 0 -> 0f
                offsetX >= width - bitmap.width -> (width - bitmap.width).toFloat()
                else -> offsetX
            }
            offsetY = when{
                offsetY <= 0 -> 0f
                offsetY >= height - bitmap.height -> (height - bitmap.height).toFloat()
                else -> offsetY
            }
            true
        }else{
            false
        }
    }

    /**
     * 长按：
     */
    override fun onLongPress(e: MotionEvent) {
        if (enabledMove){
            val scaleAnimator =
                ObjectAnimator.ofFloat(this, "currentScale", currentScale, 1f)
            val offsetXAnimator =
                ObjectAnimator.ofFloat(this, "offsetX", offsetX, (width - bitmap.width) / 2f)
            val offsetYAnimator =
                ObjectAnimator.ofFloat(this, "offsetY", offsetY, (height - bitmap.height) / 2f)
            AnimatorSet().apply {
                playTogether(
                    scaleAnimator, offsetXAnimator, offsetYAnimator
                )
            }.start()
        }
    }




    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        return false
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        val destinationScale = when (currentScale) {
            1f -> smallScale
            smallScale -> bigScale
            else -> {
                enabledMove = true
                bigScale * 1.5f
            }
        }
        val animator =
            ObjectAnimator.ofFloat(this, "currentScale", currentScale, destinationScale)
        animator.start()
        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
        return false
    }
}