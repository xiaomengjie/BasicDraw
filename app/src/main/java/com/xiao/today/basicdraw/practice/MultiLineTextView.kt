package com.xiao.today.basicdraw.practice

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.getDrawableOrThrow
import androidx.core.graphics.drawable.toBitmap
import com.xiao.today.basicdraw.R
import com.xiao.today.basicdraw.dp2px

class MultiLineTextView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 20f.dp2px
    }

    private val fontMetrics = Paint.FontMetrics()

    private val defaultImageWidth = 160f.dp2px

    //每行文字的实际占用宽度，保存在measuredWidth[0]处
    private val measuredWidth = floatArrayOf(0f)

    private val bitmap: Bitmap
    private val paddingTop: Int
    private val paddingEnd: Int
    private val textBounds: Rect = Rect()

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.MultiLineTextView)
        val imageDrawable = array.getDrawableOrThrow(R.styleable.MultiLineTextView_imageSrc)
        val imageSize = array.getDimension(R.styleable.MultiLineTextView_imageSize, defaultImageWidth)
        paddingTop = array.getDimensionPixelSize(R.styleable.MultiLineTextView_imagePaddingTop, 0)
        paddingEnd = array.getDimensionPixelSize(R.styleable.MultiLineTextView_imagePaddingEnd, 0)
        bitmap = imageDrawable.toBitmap(imageSize.toInt(), imageSize.toInt())
        array.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /*获取textBounds和fontMetrics*/
        paint.getTextBounds(CONTENT, 0, CONTENT.length, textBounds)
        paint.getFontMetrics(fontMetrics)
        /*画图*/
        canvas.drawBitmap(
            bitmap,
            (width - bitmap.width - paddingEnd).toFloat(),
            paddingTop.toFloat(),
            paint
        )
        /*画文字*/
        var startTextIndex = 0
        var startBaseline = -fontMetrics.top
        while (startTextIndex < CONTENT.length) {
            /*判断文字可绘制最大宽度*/
            val maxWidth = if (
                startBaseline + textBounds.bottom > paddingTop && startBaseline + textBounds.top < paddingTop + bitmap.height
            ) width - bitmap.width - paddingEnd else width
            /*breakText：得出maxWidth下可绘制文字数*/
            var count = paint.breakText(
                CONTENT,
                startTextIndex,
                CONTENT.length,
                true,
                maxWidth.toFloat(),
                measuredWidth
            )
            /*保证内容换行为完整单词*/
            while (CONTENT[startTextIndex + count - 1].isLetter()){
                count--
            }
            canvas.drawText(CONTENT, startTextIndex, startTextIndex + count, 0f, startBaseline, paint)
            startTextIndex += count
            startBaseline += paint.fontSpacing
            println(measuredWidth[0])
        }
        println(width)
    }
}

private const val CONTENT =
    "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?"