package com.lambda.news.widget.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.lambda.common.utils.CommonUtil
import com.lambda.common.utils.noDoubleClick
import com.lambda.news.data.LocalManager
import com.lambda.news.data.model.LanguageBean
import com.lambda.news.databinding.NewsItemLanguageSelectBinding

class LanguageSelectAdapter : RecyclerView.Adapter<ViewHolder>() {

    var data = LocalManager.allLanguageBeans

    var clickItemCallback: ((LanguageBean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return MyViewHolder(NewsItemLanguageSelectBinding.inflate(LayoutInflater.from(parent.context)).root.apply {
            layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    CommonUtil.dp2px(50f)
                )
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as? MyViewHolder)?.bind(data[position])
        holder.itemView.noDoubleClick {
            clickItemCallback?.invoke(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private inner class MyViewHolder(view: View) : ViewHolder(view) {

        val viewBinding = NewsItemLanguageSelectBinding.bind(view)

        fun bind(bean: LanguageBean) {
            viewBinding.languageTv.setText(bean.language)
        }
    }
}