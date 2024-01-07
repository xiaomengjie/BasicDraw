package com.xiao.today.basicdraw.view.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class DrawApiView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val rectF = RectF()

    private val rect = Rect()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF.left = 0f
        rectF.top = (height - width) / 2f
        rectF.right = width.toFloat()
        rectF.bottom = (height + width) / 2f

        rect.left = 0
        rect.top = (height - width) / 2
        rect.right = width
        rect.bottom = (height + width) / 2

        drawPointsArray[0] = width / 2f
        drawPointsArray[1] = height / 2f

        drawLinesArray[0] = width / 2f
        drawLinesArray[1] = 0f
        drawLinesArray[2] = width / 2f
        drawLinesArray[3] = height / 2f

        arcRectF.left = 0f
        arcRectF.top = (height - width) / 2f
        arcRectF.right = width.toFloat()
        arcRectF.bottom = (height + width) / 2f

        /* 心形path */
        path.addArc(
            width / 2f - 200f, height / 2f - 200f, width / 2f, height / 2f, -225f, 225f
        )
        path.arcTo(
            width / 2f, height / 2f - 200f, width / 2f + 200f, height / 2f, -180f, 225f, false
        )
        path.lineTo(width / 2f, height / 2f + 142)
    }

    private val drawPointsArray = FloatArray(2)

    private val drawLinesArray = FloatArray(4)

    private val arcRectF = RectF()

    private val path = Path()

    /* 绘制时单位都是：像素*/
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)/* drawColor：整个绘制区域涂色 */
        canvas.drawColor(Color.WHITE) // Color.WHITE = 0xFFFFFFFF
        canvas.drawRGB(255, 255, 255)
        canvas.drawARGB(255, 255, 255, 255)

        /* drawCircle：画圆 *//* (cx, xy)：圆心坐标；radius：半径 */
        paint.style = Paint.Style.STROKE // 画线描边
        canvas.drawCircle(width / 2f, height / 2f, width / 2f, paint)

        /* drawRect：画矩形
        * left,top,right,bottom：矩形左上右下
        * */
        paint.style = Paint.Style.STROKE // 画线描边
        canvas.drawRect(
            0f, (height - width) / 2f, width.toFloat(), (height + width) / 2f, paint
        )
        canvas.drawRect(rectF, paint)
        canvas.drawRect(rect, paint)

        /* drawPoint：画点 (x, y)：点的位置 */
        paint.strokeWidth = 100f // 点的大小
        paint.strokeCap = Paint.Cap.ROUND // 点的形状 SQUARE = 方形，ROUND = 圆形
        canvas.drawPoint(width / 2f, height / 2f, paint)

        /* drawPoints：画多个点
        * pts：点的坐标数组
        * offset：从数组的哪个地方开始取点坐标
        * count：从数组中取多少数
        *  */
        canvas.drawPoints(drawPointsArray, paint)
        canvas.drawPoints(drawPointsArray, 0, 2, paint)

        /* drawOval：画椭圆 类似 drawRect */
        paint.reset()
        paint.style = Paint.Style.STROKE // 画线描边
        canvas.drawOval(
            0f, (height - width) / 2f, width.toFloat(), (height + width) / 2f, paint
        )
        canvas.drawOval(rectF, paint)

        /* drawLine：画线
        * (startX, startY)：线条起点坐标
        * (stopX, stopY)：线条终点坐标
        * */
        canvas.drawLine(
            width / 2f, 0f, width / 2f, height / 2f, paint
        )

        /* drawLines：画多条线 类似 drawPoints */
        canvas.drawLines(drawLinesArray, paint)
        canvas.drawLines(drawLinesArray, 0, 4, paint)

        /* drawRoundRect：画圆角矩形
        * left，top，right，bottom：矩形4条边坐标
        * rx，ry：圆角的横向半径和纵向半径
        *  */
        canvas.drawRoundRect(
            0f, (height - width) / 2f, width.toFloat(), (height + width) / 2f, 100f, 100f, paint
        )
        canvas.drawRoundRect(rectF, 100f, 100f, paint)

        /* drawArc：画弧形
        * left，top，right，bottom：弧形所在矩形的4条边坐标
        * startAngle：起始角度（x 轴的正向，即正右的方向，是 0 度的位置；顺时针为正角度，逆时针为负角度）
        * sweepAngle：弧形划过角度（顺时针绘制为正角度，逆时针绘制为负角度）
        * useCenter：是否连接中心点（连接为扇形，不连接为弧形）
        * */
        paint.style = Paint.Style.FILL
        canvas.drawArc(
            0f, (height - width) / 2f, width.toFloat(), (height + width) / 2f, 0f, 100f, true, paint
        )
        canvas.drawArc(arcRectF, 0f, 100f, true, paint)

        /* drawPath：画自定义组合图形
        * path：描述路径 */
        paint.style = Paint.Style.STROKE
        drawPath(path)
        canvas.drawPath(path, paint)
    }

    private fun drawPath(path: Path) {
        // TODO: 添加完整图形，参数中包含方向
        /* addCircle：添加圆形到path
        * (x, y)：圆心坐标
        * radius：半径
        * dir：绘制方向（Path.Direction.CCW - 逆时针，Path.Direction.CW - 顺时针）
        * */
        path.addCircle(width / 2f, height / 2f, width / 2f, Path.Direction.CCW)

        /* addRect：添加矩形path */
        path.addRect(rectF, Path.Direction.CW)
        path.addRect(
            0f, (height - width) / 2f, width.toFloat(), (height + width) / 2f, Path.Direction.CCW
        )

        /* addRect：添加圆角矩形矩形path */
        path.addRoundRect(rectF, 100f, 100f, Path.Direction.CW)
        path.addRoundRect(
            0f,
            (height - width) / 2f,
            width.toFloat(),
            (height + width) / 2f,
            100f,
            100f,
            Path.Direction.CCW
        )
        /* radii：4个角不同的圆角半径*/
        path.addRoundRect(
            rectF,
            floatArrayOf(50f, 100f, 100f, 50f, 50f, 100f, 100f, 50f),
            Path.Direction.CCW
        )

        /* addOval：添加椭圆path */
        path.addOval(rectF, Path.Direction.CW)
        path.addOval(
            0f, (height - width) / 2f, width.toFloat(), (height + width) / 2f, Path.Direction.CCW
        )

        // TODO: 添加线
        /* lineTo/rLineTo：直线
        * (x, y)：终点坐标
        * (rx, ry)：终点相对位移
        * */
        path.lineTo(0f, 0f)
        path.rLineTo(10f, 10f)

        /* moveTo/rMoveTo：移动当前点位（指定起点） */
        path.moveTo(0f, 0f)
        path.rMoveTo(10f, 10f)

        /* arcTo：添加弧线
        * forceMoveTo：
        *   true - 移动过去，没有移动线条
        *   false - 滑过去，会有移动线条
        *  */
        path.arcTo(rectF, 0f, -90f, false)
        path.arcTo(0f, (height - width) / 2f, width.toFloat(), (height + width) / 2f, 0f, -90f, false)

        /* addArc：添加弧线 == forceMoveTo为true的arcTo */
        path.addArc(rectF, 0f, -90f)
        path.addArc(0f, (height - width) / 2f, width.toFloat(), (height + width) / 2f, 0f, -90f)

        /* close：封闭子图形，向起点绘制一条直线 */
        path.close()

        /* quadTo/rQuadTo：二次贝塞尔曲线
        * (x1, y1)：控制点坐标
        * (x2, y2)：终点坐标
        * (dx1, dy1)：控制点相对位移
        * (dx2, dy2)：终点相对位移
        * */
        path.quadTo(0f, 0f, 0f, 0f)
        path.rQuadTo(10f, 10f, 10f, 10f)

        /* cubicTo/rCubicTo：三次贝塞尔曲线
        * (x1, y1)：控制点坐标
        * (x2, y2)：控制点坐标
        * (x3, y3)：终点坐标
        * (x1, y1)：控制点相对位移
        * (x2, y2)：控制点相对位移
        * (x3, y3)：终点相对位移
        * */
        path.cubicTo(0f, 0f, 0f, 0f, 0f, 0f)
        path.rCubicTo(10f, 10f, 10f, 10f, 10f, 10f)
    }
}