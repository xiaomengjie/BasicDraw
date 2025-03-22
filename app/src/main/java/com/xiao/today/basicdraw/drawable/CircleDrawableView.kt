package com.xiao.today.basicdraw.drawable

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.xiao.today.basicdraw.dp2px

class CircleDrawableView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val circleDrawable = CircleDrawable()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        circleDrawable.setBounds(
            (width/2f - 100f.dp2px).toInt(),
            (height/2f - 100f.dp2px).toInt(),
            (width/2f + 100f.dp2px).toInt(),
            (height/2f + 100f.dp2px).toInt()
        )
        circleDrawable.draw(canvas)
    }
}