package com.xiao.today.basicdraw.view.path

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.xiao.today.basicdraw.dp2px

/*
* path填充规则
* */

class PathFillTypeView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /* 两圆相交宽度 */
    private val intersectWidth = 100.dp2px

    private val path = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        /* 圆直径 */
        val diameter = (width + intersectWidth) / 2f
        path.addCircle(
            diameter / 2, height / 2f, diameter / 2, Path.Direction.CW
        )
        path.addCircle(
            width - diameter / 2, height / 2f, diameter / 2, Path.Direction.CCW
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /* 填充规则
        * Path.FillType.WINDING（默认规则）：非零环绕数原则
        *   任意一点射出射线，与图形相交。图形顺时针+1，图形逆时针-1。
        *   最后结果不等于0，则填充；等于0，则不填充
        * Path.FillType.EVEN_ODD：奇偶原则
        *   任意一点射出射线，与图形相交。次数为奇数，则填充；偶数则不填充
        * Path.FillType.INVERSE_EVEN_ODD：EVEN_ODD规则取反
        * */
        path.fillType = Path.FillType.WINDING
        path.fillType = Path.FillType.INVERSE_WINDING
        path.fillType = Path.FillType.EVEN_ODD
        path.fillType = Path.FillType.INVERSE_EVEN_ODD
        canvas.drawPath(path, paint)
    }
}

