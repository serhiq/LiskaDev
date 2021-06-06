package com.gmail.uia059466.liska.manualsortlist
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class ItemTouchHelperCallbackManual(private val listener: ItemTouchHelperListenerManual) : ItemTouchHelper.Callback() {

  override fun isLongPressDragEnabled() = true

  override fun isItemViewSwipeEnabled() = false
  override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
  }

  override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
    return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.START or ItemTouchHelper.END)
  }

  override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
    return listener.onItemMove(recyclerView, viewHolder.adapterPosition, target.adapterPosition)
  }
}