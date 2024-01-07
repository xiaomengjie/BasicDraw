package com.xiao.today.basicdraw.view.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.xiao.today.basicdraw.dp2px

class PaintStrokeView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()

    private val path: Path = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.moveTo(width/2f, 4 * height/10f)
        path.rLineTo(width/4f, 0f)
        path.rLineTo(0f, 40.dp2px)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /* isAntiAlias 抗锯齿设置 */
        paint.isAntiAlias = true
        /*
        * Paint.Style.FILL：填充（默认模式）
        * Paint.Style.STROKE：线条
        * Paint.Style.FILL_AND_STROKE：填充+线条
        * */
        paint.style = Paint.Style.FILL
        /*
        * 线条宽度（默认0像素）
        * 0像素：绘制时宽度固定（恒为1像素），不受canvas缩放影响
        * 1像素：受缩放影响
        * */
        canvas.save()
        canvas.scale(10f, 10f)
        paint.strokeWidth = 0f
        canvas.drawLine(width / 100f, 0f, width / 100f, height / 2f, paint)
        paint.strokeWidth = 1f
        canvas.drawLine(width / 50f, 0f, width / 50f, height / 2f, paint)
        canvas.restore()

        /*
        * 线条线头形状
        * Paint.Cap.ROUND：圆头
        * Paint.Cap.SQUARE：方头
        * Paint.Cap.BUTT：平头
        * */
        paint.strokeWidth =20.dp2px
        paint.strokeCap = Paint.Cap.SQUARE
        canvas.drawLine(width/10f, height/10f, 9 * width/10f, height/10f, paint)
        paint.strokeCap = Paint.Cap.BUTT
        canvas.drawLine(width/10f, height/5f, 9 * width/10f, height/5f, paint)
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawLine(width/10f, 3 * height/10f, 9 * width/10f, 3 * height/10f, paint)

        /*
        * 线条拐角形状
        * Paint.Join.ROUND：圆角
        * Paint.Join.BEVEL：平角
        * Paint.Join.MITER：尖角
        * */
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.SQUARE
        paint.strokeJoin = Paint.Join.MITER
        canvas.drawPath(path, paint)

        /*
        * 当线条类型为：Paint.Join.MITER
        * 拐角延长线最大值
        * */
        paint.strokeMiter = 10.dp2px

        /* 图像抖动 */
        paint.isDither = true

        /* 双线性过滤绘制bitmap */
        paint.isFilterBitmap = true
    }
}