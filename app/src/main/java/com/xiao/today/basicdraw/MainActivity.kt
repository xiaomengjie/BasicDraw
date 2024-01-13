package com.xiao.today.basicdraw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.xiao.today.basicdraw.animator.HsvView
import com.xiao.today.basicdraw.animator.useCustomTypeEvaluator
import com.xiao.today.basicdraw.animator.useViewPropertyAnimator
import com.xiao.today.basicdraw.draw.drawsequence.HighlightTextView
import com.xiao.today.basicdraw.draw.paint.XfermodeAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_custom_type_evaluator)
    }

    private fun highLightView() {
        val highlightTextView = findViewById<HighlightTextView>(R.id.high_light)
        highlightTextView.text = "我最喜欢的电视是陪你一起好好吃饭以及繁华另外还有上海滩"
        highlightTextView.startChar = '是'
        highlightTextView.endChar = '华'
    }

    private fun useAnimator() {
        useViewPropertyAnimator(findViewById<Button>(R.id.button))
    }

    private fun useXfermode() {
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = XfermodeAdapter(
            resources.getStringArray(R.array.xfermode_name).toList()
        )
    }
}