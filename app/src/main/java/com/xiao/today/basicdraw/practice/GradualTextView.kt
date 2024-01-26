package com.xiao.today.basicdraw.practice

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.withSave

class GradualTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    private val textBounds = Rect()

    var rightPercent: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    var leftPercent: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    init {
        val rightAnimator = ObjectAnimator.ofFloat(this, "rightPercent", 0f, 1f)
        rightAnimator.duration = 1_500
        val leftLAnimator = ObjectAnimator.ofFloat(this, "leftPercent", 0f, 1f)
        leftLAnimator.duration = 1_500
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(rightAnimator, leftLAnimator)
        animatorSet.startDelay = 1_000
        animatorSet.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!text.isNullOrEmpty()){
            paint.getTextBounds(text.toString(), 0, text.length, textBounds)
            canvas.withSave {
                canvas.clipRect(
                    textBounds.left.toFloat() + (textBounds.width()) * leftPercent,
                    (baseline + textBounds.top).toFloat(),
                    textBounds.left.toFloat() + (textBounds.width()) * rightPercent,
                    (baseline + textBounds.bottom).toFloat())
                val color = paint.color
                paint.color = Color.RED
                canvas.drawText(text.toString(), 0f, baseline.toFloat(), paint)
                paint.color = color
            }
        }
    }
}