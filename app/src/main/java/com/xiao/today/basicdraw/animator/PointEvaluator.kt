package com.xiao.today.basicdraw.animator

import android.animation.TypeEvaluator
import android.graphics.Point

class PointEvaluator: TypeEvaluator<Point> {

    private val point = Point()
    override fun evaluate(fraction: Float, startValue: Point, endValue: Point): Point {
        point.x = (startValue.x + (endValue.x - startValue.x) * fraction).toInt()
        point.y = (startValue.y + (endValue.y - startValue.y) * fraction).toInt()
        return point
    }
}