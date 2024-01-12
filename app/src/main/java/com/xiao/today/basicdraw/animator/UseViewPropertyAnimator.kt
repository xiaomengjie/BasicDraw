package com.xiao.today.basicdraw.animator

import android.view.View

/*
* 使用ViewPropertyAnimator中已有的动画方法
*
* 1、使用view.animator方法得到viewPropertyAnimator类型对象
* 2、调用动画对应的方式实现动画
*
* By后缀：增量版本的方法
* translationX(100f)：将View的translationX值渐变为100
* translationXBy((100f)：将View的translationX值渐变的增加100
* */
fun useViewPropertyAnimator(view: View){
    val animator = view.animate().setStartDelay(1_000).setDuration(1_000)
    /*
    * 平移
    *  */
    animator.translationX(100f)
    animator.translationXBy(100f)

    animator.translationY(100f)
    animator.translationYBy(100f)

    animator.x(100f)
    animator.xBy(100f)

    animator.y(100f)
    animator.yBy(100f)

    /*
    * 旋转
    * */
    animator.rotation(100f) // 平面旋转
    animator.rotationBy(100f) // 平面旋转

    animator.rotationX(100f) // 沿x轴旋转
    animator.rotationXBy(100f) // 沿x轴旋转

    animator.rotationY(100f) // 沿y轴旋转
    animator.rotationYBy(100f) // 沿y轴旋转

    /*
    * 缩放
    * */
    animator.scaleX(2f)
    animator.scaleXBy(2f)

    animator.scaleY(2f)
    animator.scaleYBy(2f)

    /*
    * 透明度
    * */
    animator.alpha(0.5f)

    /*
    * ViewPropertyAnimator独有监听
    *
    * withStartAction/withEndAction：是一次性的，在动画执行结束后就自动弃掉了，
    * 就算之后再重用 ViewPropertyAnimator 来做别的动画，用它们设置的回调也不会再被调用
    *
    * withEndAction()：只在结束时调用，cancel不调用
    * */
    animator.withStartAction {
        /*
        * 动画开始
        * */
    }

    animator.withEndAction {
        /*
        * 动画结束
        * */
    }
}