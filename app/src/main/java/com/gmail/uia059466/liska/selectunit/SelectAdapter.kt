package com.gmail.uia059466.liska.selectunit

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.listdetail.ItemDragListener
import com.gmail.uia059466.liska.listdetail.ItemTouchHelperListener
import java.util.*

class SelectAdapter(
    val listener: SelectListener,
    private val itemDragListener: ItemDragListener
) :
    RecyclerView.Adapter<SelectAdapter.BaseViewHolder<*>>(), ItemTouchHelperListener {

    private var data: MutableList<String> = ArrayList()
    private var favorites: MutableList<String> = ArrayList()
    private var mode = 0

    fun setupMode(newMode: Int) {
        mode = newMode
    }

    fun getSizeFavorites() = favorites.size
    fun getLastIndex(): Int {
        return data.lastIndex
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_units, parent,
            false
        )
        return LessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = data[position]
        when (holder) {
            is LessonViewHolder -> holder.bind(element)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(state: SelectUnitsAdapterState) {
        data.clear()
        data.addAll(state.data)

        favorites.clear()
        favorites.addAll(
            state.fav
        )
        notifyDataSetChanged()
    }

    fun addUnit(str: String) {
        data.add(str)
    }

    fun isContainUnits(str: String): Boolean {
        return data.contains(str)
    }

    fun displayEdit() {
        mode = MODE_EDIT
        notifyDataSetChanged()
    }

    fun requestState(): SelectUnitsAdapterState {
        return SelectUnitsAdapterState(data, favorites)
    }

    fun displayFavorites() {
        mode = MODE_FAVORITES
        notifyDataSetChanged()
    }

    fun changePosition(oldText: String, newText: String) {
        val indexFavs = favorites.indexOf(oldText)
        if (indexFavs >= 0) {
            favorites[indexFavs] = newText
        }
        val indexOfUnits = data.indexOf(oldText)
        if (indexOfUnits >= 0) {
            data[indexOfUnits] = newText
        }
        notifyDataSetChanged()
    }

    fun displaySelect() {
        mode = MODE_SELECT
    }

    abstract class BaseViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class LessonViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {
        private var checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
        private var radio: RadioButton = itemView.findViewById(R.id.radio)
        private var imgHandle = itemView.findViewById<ImageView>(R.id.handle_img)
        private var llHandle = itemView.findViewById<LinearLayout>(R.id.handle_ll)
        private var delete = itemView.findViewById<FrameLayout>(R.id.delete_fl)
        private val tvTitle: TextView = itemView.findViewById(R.id.title_tv)

        private var isFavorite = false

        lateinit var currentItem: String

        override fun bind(item: String) {
            this.currentItem = item
            tvTitle.text = currentItem

            when (mode) {
                MODE_EDIT -> renderEdit(item)
                MODE_FAVORITES -> renderSelectFavorites(item)
                MODE_SELECT -> renderSelect()
            }
        }

        private fun renderSelect() {
            imgHandle.visibility = View.GONE
            delete.visibility = View.GONE
            radio.visibility = View.VISIBLE
            checkBox.visibility = View.GONE

            radio.setOnClickListener {
                listener.onSelectClicked(currentItem)
            }

            itemView.setOnClickListener {
                listener.onSelectClicked(currentItem)
            }
        }

        private fun renderSelectFavorites(item: String) {
            tvTitle.setOnClickListener(null)
            isFavorite = favorites.contains(item)

            checkBox.isChecked = isFavorite
            checkBox.visibility = View.VISIBLE

            itemView.setOnClickListener {
                checkBox.isChecked = !checkBox.isChecked
                configureListener(item)
            }

            checkBox.setOnClickListener {
                configureListener(item)
            }

            delete.visibility = View.GONE
            imgHandle.visibility = View.GONE
            radio.visibility = View.GONE

        }

        private fun configureListener(item: String) {
            if (isFavorite) {
                deleteFromFavorites(item)
            } else {
                addToFavorites(item)
            }
            isFavorite = !isFavorite
            listener.updateFavs()
        }

        private fun renderEdit(item: String) {
            radio.visibility = View.GONE
            checkBox.visibility = View.GONE
            imgHandle.visibility = View.VISIBLE
            delete.visibility = View.VISIBLE
            delete.setOnClickListener {
                listener.hideKeyboardFragment()
                data.remove(currentItem)
                favorites.remove(currentItem)
                notifyDataSetChanged()
            }
            tvTitle.setOnClickListener {
                listener.onItemClickedEdit(item, adapterPosition)
            }

            llHandle.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    itemDragListener.onItemDrag(this)
                    listener.hideKeyboardFragment()
                }

                false
            }
        }

        private fun addToFavorites(item: String) {
            if (favorites.size < 4) {
                favorites.add(item)
                notifyDataSetChanged()
            } else {
                listener.onLimited()
                checkBox.isChecked = false
            }
        }

        private fun deleteFromFavorites(item: String) {
            favorites.remove(item)
            notifyItemChanged(adapterPosition)
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

    override fun onItemDismiss(viewHolder: RecyclerView.ViewHolder, position: Int) {
    }

    interface SelectListener {
        fun onLimited()
        fun onItemClickedEdit(name: String, position: Int)
        fun hideKeyboardFragment()
        fun onSelectClicked(unit: String)
        fun updateFavs()
    }

    companion object {
        const val MODE_EDIT = 1
        const val MODE_FAVORITES = 2
        const val MODE_SELECT = 3
    }
}