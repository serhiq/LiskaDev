package com.gmail.uia059466.liska.manualsortlist

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.data.ListDisplay
import java.util.*

class ManualSortAdapter(var data: MutableList<ListDisplay>, private val itemDragListener: ItemDragListenerManual) : RecyclerView.Adapter<ManualSortAdapter.MainViewHolder>(),
                                                                                                                                   ItemTouchHelperListenerManual {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MainViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(
                        R.layout.item_sorting, parent,
                        false
                )
            return    MainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class MainViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

        private lateinit var item: ListDisplay
        private var imgHandle: ImageView = itemView.findViewById(R.id.handle_img)
        private var tvName: TextView = itemView.findViewById(R.id.name_tv)

        fun bind(item: ListDisplay) {
            tvName.text=item.title
            this.item = item
            imgHandle.visibility= View.VISIBLE
            imgHandle.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    itemDragListener.onItemDrag(this)
                }
                false
            }
        }

    }

    override fun onItemMove(
        recyclerView: RecyclerView,
        fromPosition: Int,
        toPosition: Int
    ): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(data, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(data, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    fun getList(): List<ListDisplay> {
        return data
    }
}