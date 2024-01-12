package com.xiao.today.basicdraw.draw.canvas

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.xiao.today.basicdraw.R
import com.xiao.today.basicdraw.dp2px
import com.xiao.today.basicdraw.loadAvatar
import kotlin.math.sqrt

class ClipCameraView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val bitmapWidth = 200.dp2px

    private val bitmap = context.loadAvatar(R.drawable.avatar, bitmapWidth.toInt())

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val matrix = Matrix()

    private val camera = Camera().apply {
        setLocation(0f, 0f, -6 * resources.displayMetrics.density)
    }

    private var canvasRotate = 0f
        set(value) {
            field = value
            invalidate()
        }

    private var cameraRotateX = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val cameraRotateXAnimator =
        ObjectAnimator.ofFloat(this, "cameraRotateX", 0f, 45f).apply {
            duration = 1_000
        }
    private val cameraRotateXRecoverAnimator =
        ObjectAnimator.ofFloat(this, "cameraRotateX", 45f, 0f).apply {
            duration = 1_000
        }
    private val canvasRotateAnimator = ObjectAnimator.ofFloat(this, "canvasRotate", 360f).apply {
        duration = 1_000
    }

    init {
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(
            cameraRotateXAnimator,
            canvasRotateAnimator,
            cameraRotateXRecoverAnimator
        )
        animatorSet.start()
    }

    /*
    *
    * */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /*
        * 不考虑坐标轴的变换（倒着写，倒着想）
        *
        * 例子：将图片移动到中央，并旋转45度
        * */

        /*
        * （0，0）绘制，移到中央，旋转（先移动，后旋转）
        * */
        postTranslatePostRotate(canvas)

        preRotatePreTranslate(canvas)

        preTranslatePostRotate(canvas)

        /*
        * （0，0）绘制，旋转，移到中央（先旋转，再移动）
        * */
        preTranslatePreRotate(canvas)

        postRotatePostTranslate(canvas)

        preRotatePostTranslate(canvas)

        if (false) {
            translateRotate(canvas)

            transformUseMatrix(canvas)

            transformUseCanvasWithAnimator(canvas)

            transformUseCanvas(canvas)
        }
    }

    private fun preRotatePostTranslate(canvas: Canvas) {
        canvas.save()
        matrix.reset()
        matrix.preRotate(
            45f, bitmap.width / 2f, bitmap.height / 2f
        )
        matrix.postTranslate(
            (width - bitmap.width) / 2f, (height - bitmap.height) / 2f
        )
        canvas.concat(matrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.restore()
    }

    private fun postRotatePostTranslate(canvas: Canvas) {
        canvas.save()
        matrix.reset()
        matrix.postRotate(
            45f, bitmap.width / 2f, bitmap.height / 2f
        )
        matrix.postTranslate(
            (width - bitmap.width) / 2f, (height - bitmap.height) / 2f
        )
        canvas.concat(matrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.restore()
    }

    private fun preTranslatePreRotate(canvas: Canvas) {
        canvas.save()
        matrix.reset()
        matrix.preTranslate(
            (width - bitmap.width) / 2f, (height - bitmap.height) / 2f
        )
        matrix.preRotate(
            45f, bitmap.width / 2f, bitmap.height / 2f
        )
        canvas.concat(matrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.restore()
    }

    private fun preTranslatePostRotate(canvas: Canvas) {
        canvas.save()
        matrix.reset()
        matrix.preTranslate(
            (width - bitmap.width) / 2f, (height - bitmap.height) / 2f
        )
        matrix.postRotate(
            45f, width / 2f, height / 2f
        )
        canvas.concat(matrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.restore()
    }

    private fun preRotatePreTranslate(canvas: Canvas) {
        canvas.save()
        matrix.reset()
        matrix.preRotate(
            45f, width / 2f, height / 2f
        )
        matrix.preTranslate(
            (width - bitmap.width) / 2f, (height - bitmap.height) / 2f
        )
        canvas.concat(matrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.restore()
    }

    private fun postTranslatePostRotate(canvas: Canvas) {
        canvas.save()
        matrix.reset()
        matrix.postTranslate(
            (width - bitmap.width) / 2f, (height - bitmap.height) / 2f
        )
        matrix.postRotate(
            45f, width / 2f, height / 2f
        )
        canvas.concat(matrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.restore()
    }

    private fun transformUseMatrix(canvas: Canvas) {
        /*
        *
        * matrix.reset()：重置matrix
        *
        * preXXX：新的变换会在之前的变换基础上进行
        *
        * postXXX：新的变换会在之后的变换基础上进行
        *
        * 可以这么理解：
        * matrix.preTranslate
        * matrix.preRotate
        * 在（0，0）绘制bitmap，preRotate，preTranslate
        *
        * matrix.postTranslate
        * matrix.postRotate
        * 在（0，0）绘制bitmap，postTranslate，postRotate
        *
        * 绘制
        * 旋转
        * 裁切
        * 旋转
        * 平移
        * */
        canvas.save()
        matrix.reset()
        matrix.preTranslate(
            width / 2f, height / 2f
        )
        matrix.preRotate(-canvasRotate)
        canvas.concat(matrix)
        canvas.clipRect(
            -bitmap.width.toFloat(),
            -bitmap.height.toFloat(),
            bitmap.width.toFloat(),
            0f
        )
        canvas.rotate(canvasRotate)
        canvas.drawBitmap(bitmap, -bitmap.width / 2f, -bitmap.height / 2f, paint)
        canvas.restore()

        /*
        * 将camera和matrix结合
        * camera.getMatrix(matrix)：matrix复制camera中的转换
        *
        * 绘制
        * 旋转
        * 裁切
        * 三维变换
        * 旋转
        * 平移
        * */
        canvas.save()
        camera.save()
        camera.rotateX(cameraRotateX)
        matrix.reset()
        camera.getMatrix(matrix)
        camera.restore()
        matrix.postRotate(-canvasRotate)
        matrix.postTranslate(width / 2f, height / 2f)
        canvas.concat(matrix)
        canvas.clipRect(
            -bitmap.width.toFloat(),
            0f,
            bitmap.width.toFloat(),
            bitmap.height.toFloat()
        )
        canvas.rotate(canvasRotate)
        canvas.drawBitmap(bitmap, -bitmap.width / 2f, -bitmap.height / 2f, paint)
        canvas.restore()
    }

    private fun translateRotate(canvas: Canvas) {
        /*
        * 绘制
        * 旋转
        * 平移
        * */
        canvas.save()
        canvas.translate(bitmap.width / 2f, 0f)
        canvas.rotate(45f, bitmap.width / 2f, bitmap.height / 2f)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.restore()
    }

    /*
    * 斜着切
    * */
    private fun transformUseCanvasWithAnimator(canvas: Canvas) {
        /*
        * 绘制
        * 旋转
        * 裁切
        * 旋转
        * 平移
        * */
        canvas.save()
        canvas.translate(width / 2f, height / 2f)
        canvas.rotate(canvasRotate)
        canvas.clipRect(
            -sqrt(2f) * bitmap.width / 2f,
            -sqrt(2f) * bitmap.height / 2f,
            sqrt(2f) * bitmap.width / 2f,
            0f
        )
        canvas.rotate(-canvasRotate)
        canvas.drawBitmap(bitmap, -bitmap.width / 2f, -bitmap.height / 2f, paint)
        canvas.restore()

        /*
        * 加入动画后每次绘制重复使用camera
        * 用camera.save()和camera.restore()重置camera的状态，避免叠加
        *
        * 绘制
        * 旋转
        * 裁切
        * 三维变换
        * 旋转
        * 平移
        * */
        canvas.save()
        camera.save()
        canvas.translate(width / 2f, height / 2f)
        canvas.rotate(canvasRotate)
        /* rotateX：沿X轴旋转 */
        camera.rotateX(cameraRotateX)
        camera.applyToCanvas(canvas)
        canvas.clipRect(
            -sqrt(2f) * bitmap.width / 2f,
            0f,
            sqrt(2f) * bitmap.width / 2f,
            sqrt(2f) * bitmap.height / 2f
        )
        canvas.rotate(-canvasRotate)
        canvas.drawBitmap(bitmap, -bitmap.width / 2f, -bitmap.height / 2f, paint)
        camera.restore()
        canvas.restore()
    }

    private fun transformUseCanvas(canvas: Canvas) {
        /*
        * 想法：
        * 1、在（0，0）处绘制图片
        * 2、切出上半部分
        * 3、移动到中心
        * */
        canvas.save()
        canvas.translate((width - bitmap.width) / 2f, (height - bitmap.height) / 2f)
        canvas.clipRect(0f, 0f, bitmap.width.toFloat(), bitmap.height / 2f)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.restore()

        /*
        * 想法：
        * 1、在（0，0）处绘制图片
        * 2、切出下半部分
        * 3、移动到左上角
        * 4、camerax轴旋转
        * 5、移动到中心
        * */
        canvas.save()
        canvas.translate(width / 2f, height / 2f)
        camera.applyToCanvas(canvas)
        canvas.translate(-bitmap.width / 2f, -bitmap.height / 2f)
        canvas.clipRect(0f, bitmap.height / 2f, bitmap.width.toFloat(), bitmap.height.toFloat())
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        canvas.restore()
    }
}