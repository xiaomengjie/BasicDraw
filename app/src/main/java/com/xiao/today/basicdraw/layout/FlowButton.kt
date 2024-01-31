package com.xiao.today.basicdraw.layout

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.xiao.today.basicdraw.R
import com.xiao.today.basicdraw.dp2px
import com.xiao.today.basicdraw.randomString
import kotlin.random.Random
import kotlin.random.nextInt

class FlowButton(context: Context, attrs: AttributeSet): AppCompatButton(context, attrs){

    private val colors = arrayOf(
        R.color.color0,
        R.color.color1,
        R.color.color2,
        R.color.color3,
        R.color.color4,
        R.color.color5,
        R.color.color6,
        R.color.color7,
    )

    override fun setLayoutParams(params: ViewGroup.LayoutParams) {
        if (params is MarginLayoutParams){
            params.setMargins(4f.dp2px.toInt())
        }
        super.setLayoutParams(params)
    }

    init {
        val random = Random.nextInt(colors.size)
        setBackgroundResource(colors[random])
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val random = Random.nextDouble(1.0, 1.5)
        setMeasuredDimension(measuredWidth, (measuredHeight * random).toInt())
    }
}