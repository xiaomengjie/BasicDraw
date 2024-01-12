package com.xiao.today.basicdraw.draw.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.graphics.withSave
import com.xiao.today.basicdraw.dp2px

/*
* 范围裁切
* 限制图形的绘制范围
* */
class ClipView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val WIDTH = 200f.dp2px
    private val RADIUS = 100f.dp2px

    private val path = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path.addRect(
            width / 2f - RADIUS,
            height / 2f - RADIUS,
            width/2f + RADIUS,
            height/2f, Path.Direction.CW
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /*
        * Canvas.clipRect：裁切矩形范围
        * clipOutRect：矩形区域内裁切掉
        * */
        canvas.withSave {
            clipRect(0f, 0f, WIDTH, WIDTH / 2f)
//            clipOutRect(0f, 0f, WIDTH, WIDTH / 2f)
            drawRect(0f, 0f, WIDTH, WIDTH, paint)
        }

        /*
        * Canvas.clipPath：裁切自定义的path
        * clipOutPath：path区域内裁切掉
        * */
        canvas.withSave {
            clipPath(path)
//            clipOutPath(path)
            drawCircle(width / 2f, height / 2f, RADIUS, paint)
        }
    }
}