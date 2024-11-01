package com.theme.lambda.launcher.widget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.android.launcher3.R
import com.android.launcher3.databinding.ItemHowToSetWidgetBinding

class WidgetGuideAdapter(context: Context) : PagerAdapter() {

    private val views = ArrayList<View>()

    init {
        views.add(ItemHowToSetWidgetBinding.inflate(LayoutInflater.from(context)).apply {
            tipIv.setImageResource(R.drawable.ic_widget_tip_1)
            tipTv.setText(R.string.how_to_set_widget_tip1)
        }.root)
        views.add(ItemHowToSetWidgetBinding.inflate(LayoutInflater.from(context)).apply {
            tipIv.setImageResource(R.drawable.ic_widget_tip_2)
            tipTv.setText(R.string.how_to_set_widget_tip2)
        }.root)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(views[position])
        return views[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(views[position])
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return views.size
    }
}