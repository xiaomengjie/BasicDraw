package com.xiao.today.basicdraw.layout.viewpage

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xiao.today.basicdraw.R
import com.xiao.today.basicdraw.dp2px
import kotlin.random.Random

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ViewPageFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val colors = arrayOf(
        Color.parseColor("#ff0000"),
        Color.parseColor("#00ff00")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_view_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewById = view.findViewById<TextView>(R.id.content)
        viewById.textSize = if (param2 == "one") 40f.dp2px else 20f.dp2px
        viewById.text = "$param1 = $param2"
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}