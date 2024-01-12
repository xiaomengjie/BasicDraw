package com.xiao.today.basicdraw.draw.drawsequence

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class DrawSequenceView(context: Context, attrs: AttributeSet): View(context, attrs){

    /*
    * 内容绘制的顺序：背景，主体，子View，滑动边缘渐变和滑动条，前景
    *
    * draw(Canvas canvas)：所有的绘制过程都在draw方法中完成
    *   1、drawBackground：绘制背景（不能重写）
    *   2、onDraw：绘制主体（本身view需要绘制的内容）
    *   3、dispatchDraw：绘制子view
    *   4、onDrawForeground：绘制滑动相关和前景
    *
    *
    * 重写draw方法：
    *   1、super.draw(canvas)的上方：会被draw绘制内容覆盖
    *       如：保留EditText的下横线背景，又想改变颜色
    *   2、super.draw(canvas)的下方：绘制内容会覆盖所有内容
    * */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
    }

    /*
    * 重写onDraw方法：
    *   1、super.onDraw(canvas)的下方：绘制内容会覆盖原来控件的内容，可以为控件增加点缀内容
    *       如：在imageview中绘制图片的尺寸信息（400 * 400）
    *   2、super.onDraw(canvas)的上方：绘制的内容会被覆盖
    *       如：在Textview中文字的下方绘制颜色，突出某部分文字
    * */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    /*
    * 重写dispatchDraw方法：
    *   1、super.dispatchDraw(canvas)上方：绘制效果和重写onDraw在super.onDraw(canvas)的下方绘制效果一样
    *   2、super.dispatchDraw(canvas)下方：绘制内容覆盖子View的内容
    * */
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

    }

    /*
    * 重写onDrawForeground方法：
    *   1、super.onDrawForeground(canvas)下方：绘制内容会盖住滑动边缘渐变、滑动条和前景
    *   2、super.onDrawForeground(canvas)上方：绘制内容盖住子View内容，但是会被滑动边缘渐变、滑动条以及前景盖住
    * */
    override fun onDrawForeground(canvas: Canvas) {
        super.onDrawForeground(canvas)
    }

    /*
    * ViewGroup绘制时，会绕过draw方法，而时直接执行dispatchDraw方法，简化绘制流程
    *
    * 如果自定义ViewGroup，并且希望在除dispatchDraw方法之外的绘制方法中进行绘制，需要调用View.setWillNotDraw(false)
    * 以切换到完整的绘制流程。
    *
    * 如果继承的ViewGroup已经设置了View.setWillNotDraw(false)，就不需要额外调用
    * */

    /*
    * android对于onDraw的重复执行效率有优化，所以在进行自定义绘制时，尽量将绘制代码写在onDraw方法中
    * */
}