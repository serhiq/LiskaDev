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

class UnitsAdapter(val listener: Listener, private val itemDragListener: ItemDragListener
) : RecyclerView.Adapter<UnitsAdapter.ViewHolder>(), ItemTouchHelperListener {

    private var data: MutableList<String> = ArrayList()
    private var favorites: MutableList<String> = ArrayList()

    private var mode = Mode.SELECT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_units, parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(state: SelectUnitsAdapterState) {
        data.clear()
        data.addAll(state.data)

        favorites.clear()
        favorites.addAll(state.fav)
        notifyDataSetChanged()
    }

    fun setupMode(mode: Mode) {
        this.mode = mode
        notifyDataSetChanged()
    }

    fun getSizeFavorites() = favorites.size

    fun getLastIndex()  = data.lastIndex

    fun addUnit(str: String) {
        data.add(str)
    }

    fun isContainUnits(str: String): Boolean {
        return data.contains(str)
    }

    fun requestState(): SelectUnitsAdapterState {
        return SelectUnitsAdapterState(data, favorites)
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
        private var radio: RadioButton = itemView.findViewById(R.id.radio)
        private var imgHandle = itemView.findViewById<ImageView>(R.id.handle_img)
        private var llHandle = itemView.findViewById<LinearLayout>(R.id.handle_ll)
        private var delete = itemView.findViewById<FrameLayout>(R.id.delete_fl)
        private val tvTitle: TextView = itemView.findViewById(R.id.title_tv)

        private var isFavorite = false

        fun bind(item: String) {
            tvTitle.text = item
            when (mode) {
                Mode.EDIT -> renderEdit(item)
                Mode.FAVORITES -> renderSelectFavorites(item)
                Mode.SELECT -> renderSelect(item)
            }
        }

        private fun renderSelect(item: String) {
            imgHandle.visibility = View.GONE
            delete.visibility = View.GONE
            radio.visibility = View.VISIBLE
            checkBox.visibility = View.GONE

            radio.setOnClickListener { listener.onSelectClicked(item) }
            itemView.setOnClickListener { listener.onSelectClicked(item) }
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

            checkBox.setOnClickListener { configureListener(item) }

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
                data.remove(item)
                favorites.remove(item)
                notifyDataSetChanged()
            }
            tvTitle.setOnClickListener { listener.onItemClickedEdit(item, adapterPosition) }

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

    interface Listener {
        fun onLimited()
        fun onItemClickedEdit(name: String, position: Int)
        fun hideKeyboardFragment()
        fun onSelectClicked(unit: String)
        fun updateFavs()
    }

    enum class Mode {
        EDIT, FAVORITES, SELECT
    }
}