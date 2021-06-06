package com.gmail.uia059466.liska.listdetail

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.data.database.ListItem
import com.gmail.uia059466.liska.data.database.getSelectedCount
import com.gmail.uia059466.liska.utils.getThemeColor
import java.util.Collections

class DisplayListAdapter(
        val listener: ItemAdapterListener,
        private val itemDragListener: ItemDragListener
                        ) :
        RecyclerView.Adapter<DisplayListAdapter.BaseViewHolder<*>>(), ItemTouchHelperListener {
  
  private var mode = VIEW
  
  //  перемещать или нет при тянучке
  var isMovedChecked: Boolean = true
  
  val data = mutableListOf<ListItem>()
  
  fun setupData(new: List<ListItem>) {
    data.clear()
    data.addAll(new)
    notifyDataSetChanged()
  }
  
  fun setupAdapterMode(newMode: Int) {
    when (mode) {
      SELECT -> {
        clearSelected(); mode = newMode
      }
      
      else -> mode = newMode
    }
    notifyDataSetChanged()
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
    val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_setting_patch, parent,
            false
                                                          )
    return MainViewHolder(view)
  }
  
  override fun getItemCount(): Int {
    return data.size
  }
  
  override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
    val element = data[position]
    when {
      holder is MainViewHolder -> holder.bind(element)
    }
  }
  
  abstract class BaseViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    
    abstract fun bind(item: T)
  }
  
  inner class MainViewHolder(itemView: View) : BaseViewHolder<ListItem>(itemView) {
    
    private lateinit var item: ListItem
    
    private var checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
    private var radio: RadioButton = itemView.findViewById(R.id.radio)
    private var tvName: TextView = itemView.findViewById(R.id.name_tv)
    private var linearLayout = itemView.findViewById<LinearLayout>(R.id.layout)
    
    private var imgHandle = itemView.findViewById<ImageView>(R.id.handle_img)
    private var llHandle = itemView.findViewById<LinearLayout>(R.id.handle_ll)
    private var delete = itemView.findViewById<FrameLayout>(R.id.delete_fl)
    
    private val colorBackground = itemView.getThemeColor(R.attr.big_space)
    private val colorSelected = itemView.getThemeColor(R.attr.color_secondary_alpha)
    
    override fun bind(item: ListItem) {
      this.item = item
      
      when (mode) {
        EDIT -> renderEdit()
        VIEW -> renderView()
        SELECT -> renderSelect()
      }
      tvName.text = item.title
    }
    
    private fun renderSelect() {
      imgHandle.visibility = View.GONE
      delete.visibility = View.GONE
      
      configureRadioButton()
    }
    
    private fun renderView() {
      imgHandle.visibility = View.GONE
      delete.visibility = View.GONE
      llHandle.setOnTouchListener(null)
      
      radio.visibility = View.GONE
      
      itemView.setBackgroundColor(colorBackground)
      
      checkBox.isChecked = item.isChecked
      checkBox.isEnabled = true
      checkBox.visibility = View.VISIBLE
      
      itemView.setOnClickListener {
        checkBox.isChecked = !checkBox.isChecked
        configureListener()
      }
      
      checkBox.setOnClickListener {
        configureListener()
      }
      
      linearLayout.setOnLongClickListener {
        deleteSelected()
        mode = SELECT
        notifyDataSetChanged()
        listener.setupSelectedMode(true)
        return@setOnLongClickListener true
      }
    }
    
    private fun renderEdit() {
      radio.visibility = View.GONE
      checkBox.visibility = View.GONE
      
      imgHandle.visibility = View.VISIBLE
      delete.visibility = View.VISIBLE
      
      llHandle.setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
          itemDragListener.onItemDrag(this)
          listener.hideKeyboardFragment()
        }
        
        false
      }
      
      delete.setOnClickListener {
        listener.hideKeyboardFragment()
        data.removeAt(position)
        notifyItemRemoved(position)
        
      }
      itemView.setBackgroundColor(colorBackground)
      
      checkBox.isChecked = item.isChecked
      checkBox.visibility = View.GONE
      
      itemView.setOnClickListener {
        listener.onItemClickedEdit(item.title, adapterPosition)
      }
      
      linearLayout.setOnLongClickListener {
        deleteSelected()
        mode = SELECT
        notifyDataSetChanged()
        listener.setupSelectedMode(true)
        return@setOnLongClickListener true
      }
    }
    
    private fun configureRadioButton() {
      tvName.text = item.title
      radio.visibility = View.VISIBLE
      checkBox.visibility = View.GONE
      
      radio.isChecked = item.isSelected
      
      
      val background =
              when (item.isSelected) {
                true -> colorSelected
                false -> colorBackground
              }
      
      itemView.setBackgroundColor(background)
      
      itemView.setOnClickListener {
        radio.isChecked = !radio.isChecked
        item.isSelected = !item.isSelected
        notifyItemChanged(position)
        listener.changeSelectedCount(data.getSelectedCount())
      }
      
      radio.setOnClickListener {
        item.isSelected = !item.isSelected
        notifyItemChanged(position)
        listener.changeSelectedCount(data.getSelectedCount())
      }
    }
    
    private fun configureListener() {
      if (isMovedChecked) {
        changeCurrentPosition()
      }
      item.isChecked = !item.isChecked
    }
    
    private fun changeCurrentPosition() {
      if (!item.isChecked) {
        onItemMove(position, data.lastIndex)
      } else {
        val needIndex = findNeedIndex()
        if (needIndex > 0 && needIndex != position) {
          onItemMove(position, needIndex)
        }
      }
    }
    
    private fun findNeedIndex(): Int {
      for ((position, item) in data.withIndex()) {
        if (item.isChecked) {
          return position
        }
      }
      return -1
    }
    
    private fun onItemMove(
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
  }
  
  interface ItemAdapterListener {
    
    fun setupSelectedMode(isSelectedMode: Boolean)
    fun changeSelectedCount(selected: Int)
    fun onItemClickedEdit(name: String, position: Int)
    fun hideKeyboardFragment()
  }
  
  override fun onItemMove(recyclerView: RecyclerView, fromPosition: Int, toPosition: Int): Boolean {
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
  
  override fun onItemDismiss(viewHolder: RecyclerView.ViewHolder, position: Int) {
  }
  
  fun deleteSelected() {
    
    val iter: MutableIterator<ListItem> = data.iterator()
    while (iter.hasNext()) {
      val str = iter.next()
      if (str.isSelected) {
        iter.remove()
      }
    }
    notifyDataSetChanged()
  }
  
  fun copySelected() {
    val selected = data.filter { (it.isSelected) }
    
    selected.forEach {
      data.add(it)
      
    }
    notifyDataSetChanged()
  }
  
  fun addUnit(str: String): Int {
    val add = ListItem(title = str)
    data.add(add)
    val insertIndex = data.lastIndexOf(add)
    notifyItemInserted(insertIndex)
    return insertIndex
  }
  
  fun getSelected(): List<ListItem> {
    return data.filter { it.isSelected }
  }
  
  fun clearSelected() {
    for (item in data) {
      if (item.isSelected)
        item.isSelected = false
    }
  }
  
  fun getItems(): List<ListItem> {
    return data
  }
  
  fun changePosition(newTitle: String, position: Int) {
    data[position].title = newTitle
    notifyItemChanged(position)
    
  }
  
  companion object {
    const val SELECT = 0
    const val EDIT = 1
    const val VIEW = 2
  }
}
