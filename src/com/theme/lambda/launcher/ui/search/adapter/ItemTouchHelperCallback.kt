package com.theme.lambda.launcher.ui.search.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemTouchHelperCallback : ItemTouchHelper.Callback() {

    var onSwapListen: ((Int, Int) -> Unit)? = null

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val canMove = (viewHolder.itemView.tag as? Boolean) ?: false

        val dragFlags =
            if (canMove)
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT else 0
        val swipeFlags = 0; // 这里设为0，表示不可滑动删除
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        onSwapListen?.invoke(from, to)
        return true;
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }
}