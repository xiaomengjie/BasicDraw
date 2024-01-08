package com.xiao.today.basicdraw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.xiao.today.basicdraw.view.paint.XfermodeAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dashboard)
//        useXfermode()
    }

    private fun useXfermode() {
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = XfermodeAdapter(
            resources.getStringArray(R.array.xfermode_name).toList()
        )
    }
}