package com.xiao.today.basicdraw.touchevent

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import android.widget.OverScroller
import androidx.annotation.DrawableRes
import androidx.core.animation.doOnEnd
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.getDrawableOrThrow
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.core.view.ViewCompat
import com.xiao.today.basicdraw.R
import com.xiao.today.basicdraw.dp2px

/**
 * 手势处理
 * GestureDetector：单击、双击、移动、快速滑动等
 * ScaleGestureDetector：双指缩放
 *
 * OverScroller：滑动位移计算工具
 */
class ScalableImageView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val defaultWidth = 300f.dp2px.toInt()

    private var bitmap: Bitmap? = null

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var originalOffsetX = 0f
    private var offsetX = 0f
    private var originalOffsetY = 0f
    private var offsetY = 0f

    private var smallScale = 0f
    private var bigScale = 0f

    //是否支持缩放
    private var scalable: Boolean = true

    private val onGestureListener = OnGestureListener()

    //手势检测工具类（双击、滑动等）
    private val gestureDetector = GestureDetector(context, onGestureListener)
    private val scaleGestureDetector = ScaleGestureDetector(context, onGestureListener)
    private val flingRunnable = FlingRunnable()

    private val overScroller = OverScroller(context)

    //是否是大图显示
    private var isBig = false

    private var currentScale: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val scaleAnimator = ObjectAnimator.ofFloat(
        this, "currentScale", smallScale, bigScale
    ).apply {
        doOnEnd {
            if (!isBig){
                offsetX = 0f
                offsetY = 0f
            }
        }
    }

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.ScalableImageView)
        val drawable = array.getDrawable(R.styleable.ScalableImageView_src)
        if (drawable != null){
            //图片宽度
            val width =
                array.getDimensionPixelSize(R.styleable.ScalableImageView_image_width, defaultWidth)
            scalable = array.getBoolean(R.styleable.ScalableImageView_scalable, true)
            //转换为bitmap
            bitmap = drawable.toBitmapOrNull(
                width = width,
                height = (drawable.intrinsicHeight / drawable.intrinsicWidth.toFloat() * width).toInt()
            )
        }
        array.recycle()
    }

    fun setImageResource(@DrawableRes res: Int){
        val drawable = ResourcesCompat.getDrawable(resources, res, null)
        bitmap = drawable?.toBitmapOrNull(width = defaultWidth,
            height = (drawable.intrinsicHeight / drawable.intrinsicWidth.toFloat() * defaultWidth).toInt())
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val bitmap = bitmap ?: return
        //原始偏移，将bitmap绘制到view的中心
        originalOffsetX = (width - bitmap.width) / 2f
        originalOffsetY = (height - bitmap.height) / 2f

        //横图还是竖图
        val bitmapRatio = bitmap.width / bitmap.height.toFloat()
        val screenRatio = width / height.toFloat()
        if (bitmapRatio > screenRatio) {
            //横图
            bigScale = height.toFloat() / bitmap.height * 1.5f
            smallScale = width.toFloat() / bitmap.width
        } else {
            //竖图
            bigScale = width.toFloat() / bitmap.width * 1.5f
            smallScale = height.toFloat() / bitmap.height
        }

        currentScale = smallScale
        scaleAnimator.setFloatValues(smallScale, bigScale)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bitmap = bitmap ?: return
        canvas.save()
        val fraction = (currentScale - smallScale) / (bigScale - smallScale)
        canvas.translate(offsetX * fraction, offsetY * fraction)
        if (scalable) canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
        canvas.restore()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchEvent = scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress){
            return gestureDetector.onTouchEvent(event)
        }
        return touchEvent
    }

    /**
     * 控制最大最小偏移
     */
    private fun fixOffset() {
        val bitmap = bitmap?: return
        val maxWidth = (bigScale * bitmap.width - width) / 2f
        val maxHeight = (bigScale * bitmap.height - height) / 2f
        offsetX = minOf(offsetX, maxWidth)
        offsetX = maxOf(offsetX, -maxWidth)
        offsetY = minOf(offsetY, maxHeight)
        offsetY = maxOf(offsetY, -maxHeight)
        invalidate()
    }

    private inner class OnGestureListener : SimpleOnGestureListener(), OnScaleGestureListener {
        /**
         * 快速滑动
         */
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (isBig) {
                val bitmap = bitmap?: return false
                overScroller.fling(
                    offsetX.toInt(), offsetY.toInt(), velocityX.toInt(), velocityY.toInt(),
                    (-(bitmap.width * bigScale - width) / 2f).toInt(),
                    ((bitmap.width * bigScale - width) / 2f).toInt(),
                    (-(bitmap.height * bigScale - height) / 2f).toInt(),
                    ((bitmap.height * bigScale - height) / 2f).toInt(),
                    bitmap.width / 2, bitmap.height / 2
                )
                //下一帧调用（相当于延时message）
                ViewCompat.postOnAnimation(this@ScalableImageView, flingRunnable)
            }
            return true
        }

        /**
         * 移动
         */
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (isBig) {
                offsetX -= distanceX
                offsetY -= distanceY
                fixOffset()
            }
            return true
        }

        /**
         * 双击
         */
        override fun onDoubleTap(event: MotionEvent): Boolean {
            isBig = !isBig
            if (isBig) {
                //点击时初始位移
                //防止因为offsetX和offsetY突然改变跳动
                //双指撑开后，双击时，isBig为true，添加判断避免重新改变偏移
                if (offsetX == 0f && offsetY == 0f){
                    offsetX = (event.x - width / 2f) * (1 - bigScale / smallScale)
                    offsetY = (event.y - height / 2f) * (1 - bigScale / smallScale)
                    fixOffset()
                }
                scaleAnimator.setFloatValues(currentScale, bigScale)
                scaleAnimator.start()
            } else {
                scaleAnimator.setFloatValues(smallScale, bigScale)
                scaleAnimator.reverse()
            }
            return true
        }

        override fun onDown(event: MotionEvent): Boolean {
            return event.actionMasked == MotionEvent.ACTION_DOWN
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val tempScale = currentScale * detector.scaleFactor
            return if (tempScale > bigScale || tempScale < smallScale){
                false
            }else{
                currentScale = tempScale
                true
            }
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            if (currentScale == smallScale){
                offsetX = (detector.focusX - width / 2f) * (1 - bigScale / smallScale)
                offsetY = (detector.focusY - height / 2f) * (1 - bigScale / smallScale)
                fixOffset()
            }
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {

        }
    }

    private inner class FlingRunnable : Runnable {
        override fun run() {
            if (overScroller.computeScrollOffset()) {
                //计算距离，设置偏移
                offsetX = overScroller.currX.toFloat()
                offsetY = overScroller.currY.toFloat()
                invalidate()
                ViewCompat.postOnAnimation(this@ScalableImageView, this)
            }
        }
    }
}