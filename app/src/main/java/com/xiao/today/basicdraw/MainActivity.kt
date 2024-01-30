package com.xiao.today.basicdraw

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SimpleAdapter
import androidx.viewpager2.widget.ViewPager2
import com.xiao.today.basicdraw.animator.useViewPropertyAnimator
import com.xiao.today.basicdraw.draw.drawsequence.HighlightTextView
import com.xiao.today.basicdraw.draw.paint.XfermodeAdapter
import com.xiao.today.basicdraw.layout.FlowAdapter
import com.xiao.today.basicdraw.layout.FlowLayout
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_flow_layout)
        flowLayout()
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

    private fun flowLayout(){
        val dataList = mutableListOf<Int>()
        for (i in 1..Random.nextInt(20, 100)) {
            dataList.add(i)
        }

        val findViewById = findViewById<FlowLayout>(R.id.flowLayout)
        findViewById.flowAdapter = object : FlowAdapter<Int>(dataList){
            override fun onCreateView(position: Int, parent: ViewGroup): View {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_flow_layout, parent, false)
                val string = "${randomString(Random.nextInt(5, 10))}${dataList[position]}"
                view.findViewById<Button>(R.id.button).text = string
                return view
            }

            override fun count(): Int {
                return dataList.size
            }
        }
    }

    private fun randomString(size: Int): String{
        val lowercase = ('a' .. 'z').toList()
        val stringBuilder = StringBuilder()
        for (i in 0 until size) {
            stringBuilder.append(lowercase[Random.nextInt(1, lowercase.size)])
        }
        return stringBuilder.toString()
    }
}