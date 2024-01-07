package com.xiao.today.basicdraw

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue
import androidx.annotation.DrawableRes
import kotlin.math.cos
import kotlin.math.sign
import kotlin.math.sin

val Float.dp2px
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Float.sp2px
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )

val Int.dp2px
    get() = this.toFloat().dp2px

val Int.sp2px
    get() = this.toFloat().sp2px

fun Float.offsetX(angle: Float): Float =
    (cos(Math.toRadians(angle.toDouble())) * this).toFloat()

fun Float.offsetY(angle: Float): Float =
    (sin(Math.toRadians(angle.toDouble())) * this).toFloat()

/*加载资源图片*/
fun Context.loadAvatar(@DrawableRes drawableRes: Int, width: Int): Bitmap {
    val option = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeResource(resources, drawableRes, option)
    option.inDensity = option.outWidth
    option.inTargetDensity = width
    option.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(resources, drawableRes, option)
}