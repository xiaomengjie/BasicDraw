package com.xiao.today.basicdraw.animator

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.addListener

/*
* 使用方式：
* 如果是自定义控件，需要添加 setter / getter 方法；
* 用 ObjectAnimator.ofXXX() 创建 ObjectAnimator 对象；
* 用 start() 方法执行动画。
*/

class UseObjectAnimator{

    var value: Float = 0f
}
fun useObjectAnimator(){
    val objectAnimator = ObjectAnimator.ofFloat(UseObjectAnimator(), "value", 100f)

    /*
    * setDuration：设置动画执行时间
    * */
    objectAnimator.duration = 1_000

    /*
    * setInterpolator：插值器（速度模型）
    *
    * AccelerateDecelerateInterpolator：默认模型。先加速后减速
    * LinearInterpolator：匀速
    * AccelerateInterpolator：持续加速（离场效果使用）
    * DecelerateInterpolator：持续减速（入场效果使用）
    * AnticipateInterpolator：先回拉一下再进行正常动画轨迹（投掷效果）
    * OvershootInterpolator：超过目标弹回
    * AnticipateOvershootInterpolator：AnticipateInterpolator+OvershootInterpolator。先拉，超过，还原
    * BounceInterpolator：在目标值处弹跳（玻璃球掉在地板上的效果）
    * CycleInterpolator：
    * PathInterpolator：通过path自定义时间曲线
    *   注意：同一段时间完成度上不能有两段不同的动画完成度
    *        每一个时间完成度的点上都必须要有对应的动画完成度
    * FastOutLinearInInterpolator：加速运动
    * FastOutSlowInInterpolator：先加速再减速
    * LinearOutSlowInInterpolator：持续减速
    * */
    objectAnimator.interpolator = AccelerateDecelerateInterpolator()

    /*
    * setStartDelay：延迟启动
    * */
    objectAnimator.startDelay = 1_000

    /*
    * setRepeatMode：重复执行模式
    * ValueAnimator.RESTART：从头开始
    * ValueAnimator.REVERSE：反着执行
    * */
    objectAnimator.repeatMode = ValueAnimator.REVERSE

    /*
    * setRepeatCount：重复数（-1一直重复）
    * ValueAnimator.INFINITE
    * */
    objectAnimator.repeatCount = ValueAnimator.INFINITE

    objectAnimator.start()
}

fun addListener(viewPropertyAnimator: ViewPropertyAnimator, objectAnimator: ObjectAnimator){
    /*
    * ViewPropertyAnimator设置监听
    * */
    viewPropertyAnimator.setListener(object : Animator.AnimatorListener{
        override fun onAnimationStart(animation: Animator) {
            /* 当动画开始执行时，这个方法被调用 */
        }

        override fun onAnimationEnd(animation: Animator) {
            /* 当动画结束时，这个方法被调用 */
        }

        override fun onAnimationCancel(animation: Animator) {
            /*
            * 当动画被通过 cancel() 方法取消时，这个方法被调用
            *
            * onAnimationEnd也会被调用。会晚于onAnimationCancel执行
            *  */
        }

        override fun onAnimationRepeat(animation: Animator) {
            /*
            * 当动画通过 setRepeatMode() / setRepeatCount() 或 repeat() 方法重复执行时，这个方法被调用
            *
            * 使用ViewPropertyAnimator设置动画，不支持无法设置重复效果，所有onAnimationRepeat无效
            * */
        }
    })
    viewPropertyAnimator.setUpdateListener(object : ValueAnimator.AnimatorUpdateListener{
        override fun onAnimationUpdate(animation: ValueAnimator) {
            /*
            * 当动画的属性更新时，这个方法被调用。
            * */
        }
    })

    /*
    * ObjectAnimator设置监听
    * */
    objectAnimator.addListener(onStart = {}, onEnd = {}, onRepeat = {}, onCancel = {})
    objectAnimator.addUpdateListener {

    }
}
