package com.gmail.uia059466.liska.warehouse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.warehouse.data.WarehouseDisplay

class WarehouseAdapter(private val listener: Listener) : RecyclerView.Adapter<WarehouseAdapter.ViewHolder>() {

  private val data = mutableListOf<WarehouseDisplay>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_warehouse, parent, false)).apply {
      this.itemView.setOnClickListener {
        listener.onClick(data[adapterPosition].warehouse.uuid)
      }
    }
  }
  
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(data[position])
  }
  
  override fun getItemCount(): Int {
    return data.size
  }

  fun setData(newData: List<WarehouseDisplay>) {
    val postDiffCallback = DiffCallback(data, newData)
    val diffResult = DiffUtil.calculateDiff(postDiffCallback)
    data.clear()
    data.addAll(newData)
    diffResult.dispatchUpdatesTo(this)
  }
  
  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val photo = itemView.findViewById<ImageView>(R.id.photo)
    private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
    private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)

    fun bind(item: WarehouseDisplay) {
      tvTitle.text = item.warehouse.title
      
      tvDescription.text = item.warehouse.description
      tvDescription.isVisible = item.warehouse.description != null

      if (item.photo?.isNotBlank() == true) {
        Glide
          .with(itemView.context)
          .load(item.photo)
          .centerCrop()
          .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
          .transition(DrawableTransitionOptions.withCrossFade())
          .into(photo)
      } else {
        Glide
          .with(itemView.context)
          .clear(photo)
      }
    }
  }
  
  interface Listener {
    fun onClick(uuidWarehouse: String)
  }

  internal inner class DiffCallback(
    private val old: List<WarehouseDisplay>,
    private val new: List<WarehouseDisplay>
                                   ) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
      return old.size
    }

    override fun getNewListSize(): Int {
      return new.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return old[oldItemPosition].warehouse.uuid == new[newItemPosition].warehouse.uuid
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return old[oldItemPosition].warehouse.dataUpdated == new[newItemPosition].warehouse.dataUpdated
    }
  }
}