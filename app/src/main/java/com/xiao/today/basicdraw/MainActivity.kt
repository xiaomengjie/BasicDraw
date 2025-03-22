package com.xiao.today.basicdraw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.xiao.today.basicdraw.animator.useViewPropertyAnimator
import com.xiao.today.basicdraw.draw.drawsequence.HighlightTextView
import com.xiao.today.basicdraw.draw.paint.XfermodeAdapter
import com.xiao.today.basicdraw.layout.FlowAdapter
import com.xiao.today.basicdraw.layout.FlowLayout
import com.xiao.today.basicdraw.layout.viewpage.ItemFragment
import com.xiao.today.basicdraw.layout.viewpage.ViewPageFragment
import com.xiao.today.basicdraw.layout.viewpage.WrapHeightViewPager
import com.xiao.today.basicdraw.touchevent.ScalableImageView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_scalable_image_view)
        findViewById<ScalableImageView>(R.id.scalable_image).setImageResource(R.drawable.avatar)
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
        val dataList = randomIntList(20, 25)
        val findViewById = findViewById<FlowLayout>(R.id.flowLayout)
        findViewById.flowAdapter = object : FlowAdapter<Int>(dataList){
            override fun onCreateView(position: Int, parent: ViewGroup): View {
                return LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_flow_layout, parent, false).apply {
                        findViewById<Button>(R.id.button).text =  randomString(Random.nextInt(5, 10)){
                            position.toString()
                        }
                    }
            }

            override fun count(): Int {
                return dataList.size
            }
        }
    }

    private fun setViewPager(){
        val fragments = arrayOf(
            ViewPageFragment.newInstance("fragment", "one"),
            ViewPageFragment.newInstance("fragment", "two"))
        val wrapHeightViewPager = findViewById<WrapHeightViewPager>(R.id.viewPager)
        wrapHeightViewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getCount(): Int {
                return fragments.size
            }

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

        }
    }

    private fun itemViewPager(){
        val fragments = arrayOf(
            ItemFragment.newInstance(1),
            ItemFragment.newInstance(1),
            ItemFragment.newInstance(1))
        val wrapHeightViewPager = findViewById<WrapHeightViewPager>(R.id.viewPager)
        wrapHeightViewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getCount(): Int {
                return fragments.size
            }

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

        }
    }
}