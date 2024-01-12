package com.xiao.today.basicdraw.draw.paint

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Xfermode
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xiao.today.basicdraw.R
import com.xiao.today.basicdraw.dp2px

private val BASIC_WIDTH = 40.dp2px

class XfermodeAdapter(private val modeNameList: List<String>): RecyclerView.Adapter<XfermodeAdapter.XfermodeVH>() {

    inner class XfermodeVH(view: View): RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.tv_mode_name)
        val xfermode = view.findViewById<XfermodeView>(R.id.xfermode)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XfermodeVH {
        return XfermodeVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_xfermode, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return modeNameList.size
    }

    override fun onBindViewHolder(holder: XfermodeVH, position: Int) {
        holder.name.text = modeNameList[position]
        holder.xfermode.setXfermode(modeNameList[position])
    }
}

/*
 *
 * Xfermode：合成多次绘制
 *
 * 步骤：
 * 1、Canvs.saveLayer() 把绘制区域拉到单独的离屏缓冲⾥
 * 2、绘制 destination 图形
 * 3、⽤ Paint.setXfermode() 设置 Xfermode（PorterDuffXfermode）
 * 4、绘制 source 图形
 * 5、⽤ Paint.setXfermode(null) 恢复 Xfermode
 * 6、⽤ Canvas.restoreToCount() 把离屏缓冲中的合成后的图形放回绘制区域
 *
 * 为什么需要saveLayer() 才能正确绘制？？？
 * 答：为了把需要互相作⽤的图形放在单独的位置来绘制，不会受 View 本身的影响。
 * 如果不使⽤saveLayer()，destination将总是整个 View 的范围，两个图形的交叉区域就错误了
 *
 * PorterDuffXfermode：取出source（源）图和destination（目标）图中相对应的每个像素点值，根据计算规则，得出新的像素点的值（alpha值和color值）
 */
class XfermodeView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val destinationBitmap = Bitmap.createBitmap(
        (BASIC_WIDTH * 8).toInt(),
        (BASIC_WIDTH * 8).toInt(), Bitmap.Config.ARGB_8888
    )

    private val sourceBitmap = Bitmap.createBitmap(
        (BASIC_WIDTH * 8).toInt(),
        (BASIC_WIDTH * 8).toInt(), Bitmap.Config.ARGB_8888
    )

    private var xfermode: Xfermode? = null

    fun setXfermode(modeName: String){
        xfermode = PorterDuffXfermode(
            when(modeName){
                "CLEAR" -> PorterDuff.Mode.CLEAR
                "SRC" -> PorterDuff.Mode.SRC
                "DST" -> PorterDuff.Mode.DST
                "SRC_OVER" -> PorterDuff.Mode.SRC_OVER
                "DST_OVER" -> PorterDuff.Mode.DST_OVER
                "SRC_IN" -> PorterDuff.Mode.SRC_IN
                "DST_IN" -> PorterDuff.Mode.DST_IN
                "SRC_OUT" -> PorterDuff.Mode.SRC_OUT
                "DST_OUT" -> PorterDuff.Mode.DST_OUT
                "SRC_ATOP" -> PorterDuff.Mode.SRC_ATOP
                "DST_ATOP" -> PorterDuff.Mode.DST_ATOP
                "XOR" -> PorterDuff.Mode.XOR
                "ADD" -> PorterDuff.Mode.ADD
                "MULTIPLY" -> PorterDuff.Mode.MULTIPLY
                "SCREEN" -> PorterDuff.Mode.SCREEN
                "OVERLAY" -> PorterDuff.Mode.OVERLAY
                "DARKEN" -> PorterDuff.Mode.DARKEN
                "LIGHTEN" -> PorterDuff.Mode.LIGHTEN
                else -> PorterDuff.Mode.SRC_OVER
            })
    }

    init {
        val canvas = Canvas(destinationBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.parseColor("#d63864")
        canvas.drawOval(
            BASIC_WIDTH * 2.5f,
            BASIC_WIDTH * 0.5f,
            BASIC_WIDTH * 7.5f,
            BASIC_WIDTH * 5.5f,
            paint
        )
        canvas.setBitmap(sourceBitmap)
        paint.color = Color.parseColor("#4994ec")
        canvas.drawRect(
            BASIC_WIDTH * 0.5f,
            BASIC_WIDTH * 3.0f,
            BASIC_WIDTH * 5.0f,
            BASIC_WIDTH * 7.5f,
            paint
        )
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.XfermodeView)
        val xfermodeType = attributes.getString(R.styleable.XfermodeView_xfermode)
        if (xfermodeType != null) setXfermode(xfermodeType)
        attributes.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val left = (width - (BASIC_WIDTH * 8)) / 2f
        val top = (height - (BASIC_WIDTH * 8)) / 2f
        val right = (width + (BASIC_WIDTH * 8)) / 2f
        val bottom = (height + (BASIC_WIDTH * 8)) / 2f
        val saveLayer = canvas.saveLayer(left, top, right, bottom, null)
        canvas.drawBitmap(destinationBitmap, left, top, paint)
        paint.xfermode = xfermode
        canvas.drawBitmap(sourceBitmap, left, top, paint)
        paint.xfermode = null
        canvas.restoreToCount(saveLayer)
    }
}

