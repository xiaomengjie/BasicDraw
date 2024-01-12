package com.xiao.today.basicdraw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.xiao.today.basicdraw.animator.useViewPropertyAnimator
import com.xiao.today.basicdraw.draw.paint.XfermodeAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_circle_progress)
//        useAnimator()
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