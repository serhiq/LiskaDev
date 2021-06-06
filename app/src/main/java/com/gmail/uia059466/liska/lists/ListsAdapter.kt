package com.gmail.uia059466.liska.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.data.ListDisplay
import java.util.ArrayList

class ListsAdapter(private val listener: ListListener,
                  ) : RecyclerView.Adapter<ListsAdapter.BaseViewHolder<*>>(){

    private var data: MutableList<ListDisplay> = ArrayList()
    private var modeAdapter=0

    fun enableListMode(){
        modeAdapter=0
        notifyDataSetChanged()
    }
    fun enableGridMode(){
        modeAdapter=1
        notifyDataSetChanged()
    }

    private val LIST_ITEM = 1
    private val GRID_ITEM = 2

    override fun getItemViewType(position: Int): Int {
        return when(modeAdapter) {
            0-> LIST_ITEM
            else -> GRID_ITEM
        }
    }


    interface ListListener {
        fun onListClicked(id: Long)
        fun onLongClicked(id: Long)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            LIST_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_list, parent,
                    false
                )
                LessonViewHolder(view)
            }

            GRID_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.row_layout, parent,
                    false
                )
                GridViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = data[position]
        when (holder) {
            is LessonViewHolder -> holder.bind(element)
            is GridViewHolder -> holder.bind(element)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(newData: MutableList<ListDisplay>, isListMode: Boolean) {
        modeAdapter=if (isListMode) 0 else 1
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
    
    abstract class BaseViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class LessonViewHolder(itemView: View) : BaseViewHolder<ListDisplay>(itemView) {

        private val tvName: TextView = itemView.findViewById(R.id.title_tv)
        private val tvDescription: TextView = itemView.findViewById(R.id.description_tv)

        lateinit var currentItem:ListDisplay
    
        override fun bind(item: ListDisplay) {
            this.currentItem = item
            renderView()
            tvName.text = currentItem.title
        }
    
        private fun renderView() {
            tvDescription.visibility=View.VISIBLE
            tvDescription.text = currentItem.description
            
            itemView.setOnLongClickListener {
                listener.onLongClicked(currentItem.id)
                return@setOnLongClickListener true
            }
    
            itemView.setOnClickListener {
                listener.onListClicked(currentItem.id)
            }
        }
    }

    inner class GridViewHolder(itemView: View) : BaseViewHolder<ListDisplay>(itemView) {

        private val tvName: TextView = itemView.findViewById(R.id.title_tv)
        private val tvDescription: TextView = itemView.findViewById(R.id.description_tv)

        lateinit var currentItem:ListDisplay

        override fun bind(item: ListDisplay) {
            this.currentItem = item
            renderView()
            tvName.text = currentItem.title
        }

        private fun renderView() {
            tvDescription.visibility=View.VISIBLE
            tvDescription.text = currentItem.veryLongDescription

            itemView.setOnLongClickListener {
                listener.onLongClicked(currentItem.id)
                return@setOnLongClickListener true
            }

            itemView.setOnClickListener {
                listener.onListClicked(currentItem.id)
            }
        }
    }
}