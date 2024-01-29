package com.xiao.today.basicdraw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.xiao.today.basicdraw.animator.useViewPropertyAnimator
import com.xiao.today.basicdraw.draw.drawsequence.HighlightTextView
import com.xiao.today.basicdraw.draw.paint.XfermodeAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_material_edit_text)
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

    private fun layoutInflater(root: ViewGroup){
        val inflater = LayoutInflater.from(this)
        val mergeLayoutId = "merge_layout.xml".toInt()
        //merge布局必须要有root
        inflater.inflate(mergeLayoutId, root)

        val normalLayoutId = "normal_layout.xml".toInt()
        //root==null时，xml布局中根控件的属性无效
        inflater.inflate(mergeLayoutId, null)

        //root != null，attachToRoot=false时，宽高有效，且不会添加到root
        inflater.inflate(normalLayoutId, root, false)

        //root != null，attachToRoot=true时，宽高有效，且会添加到root
        inflater.inflate(normalLayoutId, root, true)
    }
}