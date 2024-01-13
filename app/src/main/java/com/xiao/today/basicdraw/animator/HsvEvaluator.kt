package com.xiao.today.basicdraw.animator

import android.animation.TypeEvaluator
import android.graphics.Color

class HsvEvaluator: TypeEvaluator<Int> {

    private val startHsvColor = FloatArray(3)
    private val endHsvColor = FloatArray(3)

    override fun evaluate(fraction: Float, startValue: Int, endValue: Int): Int {
        /*
        * 通过起始值、终点值和完成进度，计算当前进度下的属性值
        * */
        Color.colorToHSV(startValue, startHsvColor)
        Color.colorToHSV(endValue, endHsvColor)
        val h = startHsvColor[0] + (endHsvColor[0] - startHsvColor[0]) * fraction
        val s = startHsvColor[1] + (endHsvColor[1] - startHsvColor[1]) * fraction
        val v = startHsvColor[2] + (endHsvColor[2] - startHsvColor[2]) * fraction
        return Color.HSVToColor(floatArrayOf(h, s, v))
    }
}