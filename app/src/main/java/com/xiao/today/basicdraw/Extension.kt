package com.xiao.today.basicdraw

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.annotation.DrawableRes
import androidx.core.view.GestureDetectorCompat
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

inline fun GestureDetectorCompat.addListener(
//    crossinline onDown: (event: MotionEvent) -> Boolean = {_ -> false},
//    crossinline onShowPress: (event: MotionEvent) -> Unit = {_ -> },
//    crossinline onSingleTapUp: (event: MotionEvent) -> Boolean = {_ -> false},
//    crossinline onScroll: (downEvent: MotionEvent?, currentEvent: MotionEvent, distanceX: Float, distanceY: Float) -> Boolean = {_, _, _, _ -> false},
//    crossinline onFling: (downEvent: MotionEvent?, currentEvent: MotionEvent, velocityX: Float, velocityY: Float) -> Boolean = {_, _, _, _ -> false},
//    crossinline onLongPress: (event: MotionEvent) -> Unit = {_ -> },
    crossinline onDoubleTap: (event: MotionEvent) -> Boolean = {_ -> false},
    crossinline onDoubleTapEvent: (event: MotionEvent) -> Boolean = {_ -> false},
    crossinline onSingleTapConfirmed: (event: MotionEvent) -> Boolean = {_ -> false}
){
//    val onGestureListener = object : GestureDetector.OnGestureListener{
//        override fun onDown(e: MotionEvent): Boolean {
//            return onDown.invoke(e)
//        }
//
//        override fun onShowPress(e: MotionEvent) {
//            return onShowPress.invoke(e)
//        }
//
//        override fun onSingleTapUp(e: MotionEvent): Boolean {
//            return onSingleTapUp.invoke(e)
//        }
//
//        override fun onScroll(
//            e1: MotionEvent?,
//            e2: MotionEvent,
//            distanceX: Float,
//            distanceY: Float
//        ): Boolean {
//            return onScroll.invoke(e1, e2, distanceX, distanceY)
//        }
//
//        override fun onLongPress(e: MotionEvent) {
//            return onLongPress.invoke(e)
//        }
//
//        override fun onFling(
//            e1: MotionEvent?,
//            e2: MotionEvent,
//            velocityX: Float,
//            velocityY: Float
//        ): Boolean {
//            return onFling.invoke(e1, e2, velocityX, velocityY)
//        }
//
//    }
    val onDoubleTapListener = object : GestureDetector.OnDoubleTapListener{
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return onSingleTapConfirmed.invoke(e)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            return onDoubleTap.invoke(e)
        }

        override fun onDoubleTapEvent(e: MotionEvent): Boolean {
            return onDoubleTapEvent.invoke(e)
        }
    }
    setOnDoubleTapListener(onDoubleTapListener)
}