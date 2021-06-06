package com.gmail.uia059466.liska.catalog

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.data.database.CatalogItem
import com.gmail.uia059466.liska.listdetail.ItemDragListener
import com.gmail.uia059466.liska.listdetail.ItemTouchHelperListener
import com.gmail.uia059466.liska.utils.ItemFormatter.Companion.createStringItem
import java.util.Collections

class CatalogViewAdapter(
        private val listener: ListListener,
        private val itemDragListener: ItemDragListener
                        ) : RecyclerView.Adapter<CatalogViewAdapter.BaseViewHolder<*>>(),
                            ItemTouchHelperListener {
  
  var isEdited: Boolean = false
  val displayList = mutableListOf<DisplayCatalog>()
  var mode = AdapterViewCatalogMode.RootFolder
  var currentCatalogId = 0L
  
  private val ITEM_VIEW_TYPE_ITEM_CATALOG = 1
  private val ITEM_VIEW_TYPE_ITEM = 2
  
  fun updateState(newMode: AdapterViewCatalogMode, newData: List<DisplayCatalog>) {
    mode = newMode
    displayList.clear()
    displayList.addAll(newData)
    notifyDataSetChanged()
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
    return when (viewType) {
      ITEM_VIEW_TYPE_ITEM_CATALOG -> {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_folder, parent,
                false
                                                              )
        FolderViewHolder(view)
      }
      
      ITEM_VIEW_TYPE_ITEM -> {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_folder_item, parent,
                false
                                                              )
        ItemViewHolder(view)
      }
      
      else -> throw IllegalArgumentException("Invalid view type")
    }
  }
  
  override fun getItemViewType(position: Int): Int {
    return when {
      displayList[position] is DisplayCatalog.FolderDisplay -> ITEM_VIEW_TYPE_ITEM_CATALOG
      else -> ITEM_VIEW_TYPE_ITEM
    }
  }
  
  override fun getItemCount(): Int {
    return displayList.size
  }
  
  override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
    val element = displayList[position]
    
    when {
      holder is FolderViewHolder && element is DisplayCatalog.FolderDisplay -> holder.bind(element)
      holder is ItemViewHolder && element is DisplayCatalog.ItemDisplay -> holder.bind(element)
      else -> throw IllegalArgumentException()
    }
  }
  
  abstract class BaseViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    abstract fun bind(item: T)
  }
  
  inner class FolderViewHolder(itemView: View) : BaseViewHolder<DisplayCatalog.FolderDisplay>(
          itemView
                                                                                             ) {
    
    private val tvName: TextView = itemView.findViewById(R.id.name_tv)
    
    private lateinit var folder: DisplayCatalog.FolderDisplay
    
    override fun bind(item: DisplayCatalog.FolderDisplay) {
      this.folder = item
      tvName.text = folder.title
      itemView.setOnClickListener {
        when (mode) {
          AdapterViewCatalogMode.RootFolder -> displayFolderOnClick(item)
        }
      }
    }
    
    private fun displayFolderOnClick(item: DisplayCatalog.FolderDisplay) {
      currentCatalogId = item.id
      listener.onCatalogClicked(currentCatalogId, folder.title)
    }
  }
  
  inner class ItemViewHolder(itemView: View) : BaseViewHolder<DisplayCatalog.ItemDisplay>(itemView) {
    
    private var indicator: FrameLayout = itemView.findViewById(R.id.indicator_fl)
    private var handle: FrameLayout = itemView.findViewById(R.id.handle_fl)
    private var delete: FrameLayout = itemView.findViewById(R.id.delete_fl)
    private var name: TextView = itemView.findViewById(R.id.name_tv)
    private var linearLayout = itemView.findViewById<LinearLayout>(R.id.layout)
    
    lateinit var item: DisplayCatalog.ItemDisplay
    
    override fun bind(item: DisplayCatalog.ItemDisplay) {
      this.item = item
      
      name.text = createStringItem(title = item.title, quantity = item.quantity, unit = item.unit)
      when (mode) {
        AdapterViewCatalogMode.RootFolder -> {
        }
        
        AdapterViewCatalogMode.ItemEdit -> renderEdit()
        AdapterViewCatalogMode.ItemsView -> renderView()
      }
    }
    
    
    private fun renderView() {
      indicator.visibility = View.VISIBLE
      handle.visibility = View.GONE
      delete.visibility = View.GONE
      
      linearLayout.setOnClickListener {
        
        listener.onItemClickedEdit(currentCatalogId, adapterPosition)
      }
      itemView.setOnTouchListener(null)
      
    }
    
    private fun renderEdit() {
      handle.setOnTouchListener { _, event ->
        listener.hideKeyboardFragment()
        if (event.action == MotionEvent.ACTION_DOWN) {
          itemDragListener.onItemDrag(this)
        }
        
        false
      }
      
      delete.setOnClickListener {
        listener.hideKeyboardFragment()
        isEdited = true
        displayList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
      }
      
      indicator.visibility = View.GONE
      handle.visibility = View.VISIBLE
      delete.visibility = View.VISIBLE
      
    }
  }
  
  override fun onItemMove(recyclerView: RecyclerView, fromPosition: Int, toPosition: Int): Boolean {
    
    if (fromPosition < toPosition) {
      for (i in fromPosition until toPosition) {
        Collections.swap(displayList, i, i + 1)
      }
    } else {
      for (i in fromPosition downTo toPosition + 1) {
        Collections.swap(displayList, i, i - 1)
      }
    }
    notifyItemMoved(fromPosition, toPosition)
    isEdited = true
    return true
  }
  
  override fun onItemDismiss(viewHolder: RecyclerView.ViewHolder, position: Int) {
  }
  
  fun addUnit(title: String) {
    when {
      mode == AdapterViewCatalogMode.ItemEdit -> insertNewItems(title)
    }
  }
  
  private fun insertNewItems(title: String) {
    isEdited = true
    displayList.add(
            DisplayCatalog.ItemDisplay(
                    id = currentCatalogId,
                    title = title,
                    quantity = 0,
                    unit = ""
                                      )
                   )
  }
  
  fun requestItems(): List<CatalogItem> {
    return displayList.requestSelected()
  }
  
  interface ListListener {
    
    fun onCatalogClicked(catalogId: Long, title: String)
    fun onItemClickedEdit(currentCatalogId: Long, adapterPosition: Int)
    fun deleteFolder()
    fun hideKeyboardFragment()
  }
}

enum class AdapterViewCatalogMode {
  ItemEdit, RootFolder, ItemsView
}