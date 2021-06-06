package com.gmail.uia059466.liska.listdetail
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class ItemTouchHelperCallback(private val listener: ItemTouchHelperListener) : ItemTouchHelper.Callback() {

  override fun isLongPressDragEnabled() = false

  override fun isItemViewSwipeEnabled() = false

  override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
    return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.START or ItemTouchHelper.END)
  }

  override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
    return listener.onItemMove(recyclerView, viewHolder.adapterPosition, target.adapterPosition)
  }

  override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    listener.onItemDismiss(viewHolder, viewHolder.adapterPosition)
  }

}