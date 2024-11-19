package com.theme.lambda.launcher.widget.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.launcher3.R
import com.android.launcher3.databinding.ItemUrlShortcutItemBinding
import com.theme.lambda.launcher.data.model.ShortCut
import com.theme.lambda.launcher.utils.GlideUtil

class UrlShortcutItemAdapter : RecyclerView.Adapter<ViewHolder>() {

    val data = ArrayList<ShortCut>()

    @SuppressLint("NotifyDataSetChanged")
    fun upData(d: ArrayList<ShortCut>) {
        data.clear()
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return UrlShortcutItemViewHolder(
            ItemUrlShortcutItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            ).root
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is UrlShortcutItemViewHolder) {
            holder.bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class UrlShortcutItemViewHolder(view: View) : ViewHolder(view) {

        val viewBinding = ItemUrlShortcutItemBinding.bind(view)

        fun bind(shortCut: ShortCut) {
            GlideUtil.load(viewBinding.logoIv, shortCut.iconUrl)
            viewBinding.titleTv.text = shortCut.name
            if (shortCut.isSelect) {
                viewBinding.addIv.setImageResource(R.drawable.ic_check)
            } else {
                viewBinding.addIv.setImageResource(R.drawable.ic_add)
            }
            viewBinding.addIv.setOnClickListener {
                if (shortCut.isSelect) {
                    viewBinding.addIv.setImageResource(R.drawable.ic_add)
                } else {
                    viewBinding.addIv.setImageResource(R.drawable.ic_check)
                }
                shortCut.isSelect = !shortCut.isSelect
            }

        }
    }
}