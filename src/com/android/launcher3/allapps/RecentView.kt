package com.android.launcher3.allapps

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.android.launcher3.Launcher
import com.android.launcher3.databinding.LayoutRecentViewBinding
import com.theme.lambda.launcher.ui.search.SearchActivity

class RecentView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    var viewBinding = LayoutRecentViewBinding.inflate(LayoutInflater.from(context), this, true)

    val recentAdapter = RecentAdapter()

    var columns = Launcher.getLauncher(context).getDeviceProfile().inv.numColumns

    init {

        viewBinding.recentAppRv.apply {
            layoutManager = GridLayoutManager(context, columns).apply {
                orientation = GridLayoutManager.VERTICAL
            }
            adapter = recentAdapter
        }

        SearchActivity.recentAppLiveData.removeObservers(context as Launcher)
        SearchActivity.recentAppLiveData.observe(context as Launcher, Observer {
            val layoutParams = viewBinding.root.layoutParams as ViewGroup.LayoutParams
            if (it.isNotEmpty()) {
                layoutParams.height =
                    Launcher.getLauncher(context).getDeviceProfile().getCellSize().y
            } else {
                layoutParams.height = 0
            }
            viewBinding.root.requestLayout()

            if (it.size > columns) {
                recentAdapter.upData(it.subList(0, columns).toMutableList())
            } else {
                recentAdapter.upData(it)
            }
        })
    }

    fun notifyDataSetChanged(){
        recentAdapter.notifyDataSetChanged()
    }

}