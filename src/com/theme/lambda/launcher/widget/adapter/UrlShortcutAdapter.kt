package com.theme.lambda.launcher.widget.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.launcher3.databinding.ItemUrlShortcutBinding
import com.theme.lambda.launcher.data.model.ShortCuts
import com.theme.lambda.launcher.urlshortcut.UrlShortcutManager

class UrlShortcutAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val data = arrayListOf<ShortCuts>()

    @SuppressLint("NotifyDataSetChanged")
    fun upData(d: ArrayList<ShortCuts>) {
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return UrlShortcutViewHolder(ItemUrlShortcutBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
            layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is UrlShortcutViewHolder) {
            holder.bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class UrlShortcutViewHolder(view: View) : ViewHolder(view) {

        val viewBinding = ItemUrlShortcutBinding.bind(view)

        private val urlShortcutItemAdapter = UrlShortcutItemAdapter()

        fun bind(shortCuts: ShortCuts) {
            viewBinding.logoIv.setImageResource(UrlShortcutManager.getIconByTag(shortCuts.category))
            viewBinding.tagTv.text = shortCuts.category
            viewBinding.itemRv.apply {
                layoutManager = GridLayoutManager(viewBinding.root.context, 2)
                adapter = urlShortcutItemAdapter
            }
            urlShortcutItemAdapter.upData(shortCuts.shortcuts)
        }
    }
}