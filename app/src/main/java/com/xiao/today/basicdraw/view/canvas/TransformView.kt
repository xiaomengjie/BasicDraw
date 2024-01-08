package com.xiao.today.basicdraw.view.canvas

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withSave
import com.xiao.today.basicdraw.R
import com.xiao.today.basicdraw.dp2px
import com.xiao.today.basicdraw.loadAvatar

/*
* 几何变换
*
* 二维变换：canvas，matrix
* 三维变换：camera
* */
class TransformView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val BITMAP_WIDTH = 150f.dp2px

    private val bitmap: Bitmap = context.loadAvatar(R.drawable.avatar, BITMAP_WIDTH.toInt())

    private val matrix = Matrix()

    private val camera = Camera()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        /*
        * Canvas.translate：将canvas坐标系移动一段距离
        * dx，dy：横向和纵向位移
        * */
        canvas.withSave {
            /*
            * withSave：canvas扩展函数，添加执行save和restore方法
            * */
            canvas.translate(BITMAP_WIDTH, BITMAP_WIDTH)
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
        }

        /*
        * Canvas.rotate：旋转
        * degrees：旋转角度，大于0，顺时针旋转
        * px，py：旋转轴心
        * */
        canvas.withSave {
            /*
            * 指定轴心就是先位移到轴心，再旋转，再位移回原来位置
            * */
            canvas.rotate(45f, bitmap.width / 2f, bitmap.height / 2f)
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
        }

        /*
        * Canvas.scale：缩放
        * sx，sy：缩放倍数
        * px，py：缩放轴心
        * */
        canvas.withSave {
            /*
            * 指定轴心就是先位移到轴心，再缩放，再位移回原来位置
            * */
            canvas.scale(2f, 2f, 0f, 0f)
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
        }

        /*
        * Canvas.skew：倾斜
        * sx，sy：倾斜系数
        * */
        val left = 100f.dp2px
        val top = 100f.dp2px
        val sx = -0.1f
        val sy = -0.1f
        canvas.withSave {
            canvas.skew(sx, sy)
            canvas.drawBitmap(bitmap, left, top, paint)
        }

        /*
        * 使用matrix，进行平移、旋转、缩放、倾斜变换
        *
        * matrix中系列方法：
        * setXXX，preXXX，postXXX
        *
        * 想象matrix中有个队列，变换的执行顺序从头到尾
        *
        * setXXX：清空队列，只方这一个变换
        * preXXX：向队列前插变换
        * postXXX：向队列后插变换
        *
        * https://blog.csdn.net/sinat_31057219/article/details/77320680
        *
        * canvas.concat(matrix)：将变换应用到canvas
        *
        * 绘制 -> 移动 -> 旋转（倒着想，不考虑坐标系）
        * */
        matrix.reset()
        matrix.preTranslate((width - bitmap.width) / 2f, (height - bitmap.height) / 2f)
        matrix.postRotate(45f, width / 2f, height / 2f)
        canvas.withSave {
            concat(this@TransformView.matrix)
            drawBitmap(bitmap, 0f, 0f, paint)
        }

        /*
        * 使用Camera，模拟三维变换
        *
        * camera三维坐标系：右为x轴正向，上为y轴正向，里为z轴正向
        *
        * camera.applyToCanvas(canvas)：把旋转投影到Canvas
        * */
        canvas.save()
        camera.save()
        camera.rotateX(30f)
        canvas.translate(width / 2f, height / 2f)
        camera.applyToCanvas(canvas)
        canvas.translate(-bitmap.width / 2f, -bitmap.height / 2f)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        camera.restore()
        canvas.restore()

        /*
        * 实战
        * */
        practiceCompose(canvas)
    }

    private var topCameraRotate = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var bottomCameraRotate = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var canvasRotate = -90f
        set(value) {
            field = value
            invalidate()
        }

    private val topCameraRotateAnimator = ObjectAnimator.ofFloat(this, "topCameraRotate", 0f, -45f)

    private val bottomCameraRotateAnimator = ObjectAnimator.ofFloat(this, "bottomCameraRotate", 0f, 45f)

    private val canvasRotateAnimator = ObjectAnimator.ofFloat(this, "canvasRotate", 90f, 360f)

    init {
        bottomCameraRotateAnimator.duration = 1_000
        canvasRotateAnimator.duration = 1_000
        topCameraRotateAnimator.duration = 1_000
        AnimatorSet().apply {
            playSequentially(
                bottomCameraRotateAnimator, canvasRotateAnimator, topCameraRotateAnimator
            )
        }.start()
    }

    private fun practiceCompose(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)

        /* 绘制左边部分 */
        canvas.withSave {
            canvas.translate(width / 2f, height / 2f)
            canvas.rotate(-canvasRotate)
            camera.save()
            camera.rotateX(topCameraRotate)
            camera.applyToCanvas(this)
            camera.restore()
            clipRect(
                -bitmap.width.toFloat(),
                -bitmap.height.toFloat(),
                bitmap.width.toFloat(),
                0f
            )
            canvas.rotate(canvasRotate)
            canvas.translate(-width / 2f, -height / 2f)
            drawBitmap(
                bitmap, (width - bitmap.width) / 2f, (height - bitmap.height) / 2f, paint
            )
        }

        /* 绘制右边部分 */
        canvas.withSave {
            canvas.translate(width / 2f, height / 2f)
            canvas.rotate(-canvasRotate)
            camera.save()
            camera.rotateX(bottomCameraRotate)
            camera.applyToCanvas(this)
            camera.restore()
            clipRect(
                -bitmap.width.toFloat(),
                0f,
                bitmap.width.toFloat(),
                bitmap.height.toFloat()
            )
            canvas.rotate(canvasRotate)
            canvas.translate(-width / 2f, -height / 2f)
            drawBitmap(
                bitmap, (width - bitmap.width) / 2f, (height - bitmap.height) / 2f, paint
            )
        }

    }
}