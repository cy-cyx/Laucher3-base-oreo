package com.theme.lambda.launcher.widget.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.launcher3.R
import com.android.launcher3.databinding.ItemUrlShortcutTagBinding
import com.theme.lambda.launcher.urlshortcut.UrlShortcutManager

class UrlShortcutTagAdapter : RecyclerView.Adapter<ViewHolder>() {

    var data = UrlShortcutManager.shortCutTag
    var curTag = 0

    var clickItemListen: ((Int) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setCurSelectTag(position: Int) {
        curTag = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return UrlShortcutTagViewHolder(ItemUrlShortcutTagBinding.inflate(LayoutInflater.from(parent.context)).root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is UrlShortcutTagViewHolder) {
            holder.bind(data[position], position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class UrlShortcutTagViewHolder(view: View) : ViewHolder(view) {

        val viewBinding = ItemUrlShortcutTagBinding.bind(view)

        fun bind(tag: String, position: Int) {
            viewBinding.tagTv.text = tag
            viewBinding.logoIv.setImageResource(UrlShortcutManager.getIconByTag(tag))
            if (curTag == position) {
                viewBinding.containerFl.setBackgroundResource(R.drawable.shape_2081f4_radius_17)
                viewBinding.tagTv.setTextColor(Color.WHITE)
            } else {
                viewBinding.containerFl.setBackgroundResource(R.drawable.shape_f6f6f6_radius_17)
                viewBinding.tagTv.setTextColor(Color.BLACK)
            }
            viewBinding.root.setOnClickListener {
                clickItemListen?.invoke(position)
                setCurSelectTag(position)
            }
        }
    }
}