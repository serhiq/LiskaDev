package com.gmail.uia059466.liska.selectfromcatalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.catalog.CatalogSelectState
import com.gmail.uia059466.liska.catalog.fufu
import com.gmail.uia059466.liska.data.database.CatalogDatabase
import com.gmail.uia059466.liska.selectfromcatalogquantity.CatalogSelect
import com.gmail.uia059466.liska.selectfromcatalogquantity.CatalogSelectListener

class CatalogSelectAdapter(
    private val listener: CatalogSelectListener,
) : CatalogSelect, RecyclerView.Adapter<CatalogSelectAdapter.BaseViewHolder<*>>() {
    private val displayList = mutableListOf<fufu>()
    val rawData = CatalogSelectState()
    override var currentId = 0L
    private var queryString = ""

    override fun fillAdapter(raw: List<CatalogDatabase>) {
        rawData.start(raw)
        displayList.addAll(rawData.folders)
        notifyDataSetChanged()
    }

    override fun setupRootFolder() {
        currentId = 0L
        displayList.clear()
        displayList.addAll(rawData.folders)
        notifyDataSetChanged()
    }

    override fun search(query: String) {
        queryString = query
        displayList.clear()
        displayList.addAll(rawData.getSearchResult(query))
        notifyDataSetChanged()
    }

    private fun currentSearch() {
        search(queryString)
    }

    override fun setupViewCurrentFolder() {
        displayList.clear()
        displayList.addAll(rawData.getItems(currentId))
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
                    R.layout.catalog_select_item, parent,
                    false
                )
                ItemViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    private val ITEM_VIEW_TYPE_ITEM_CATALOG = 1
    private val ITEM_VIEW_TYPE_ITEM = 2


    override fun getItemViewType(position: Int): Int {
        return when {
            displayList[position] is fufu.Catalog -> ITEM_VIEW_TYPE_ITEM_CATALOG
            else -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return displayList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = displayList[position]

        when {
            holder is FolderViewHolder && element is fufu.Catalog -> holder.bind(
                element
            )
            holder is ItemViewHolder && element is fufu.Item -> holder.bind(element)
            else -> throw IllegalArgumentException()
        }
    }

    override fun addItem(title: String) {
        rawData.addNewSearch(currentId, title)
        currentSearch()
    }

    override fun addItem(title: String, selectedId: Long) {
        rawData.addNewSearch(selectedId, title)
        currentSearch()
    }

    override fun getCatalogCommand() = rawData.commandAdd
    override fun getSelected() = rawData.requestSelectedForAdding()

    abstract class BaseViewHolder<in T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bind(item: T)
    }

    inner class FolderViewHolder(itemView: View) :
        BaseViewHolder<fufu.Catalog>(itemView) {

        private val tvName: TextView = itemView.findViewById(R.id.name_tv)

        private lateinit var folder: fufu.Catalog

        override fun bind(item: fufu.Catalog) {
            this.folder = item
            tvName.text = folder.title
            itemView.setOnClickListener {
                currentId = item.id
                listener.onCatalogClicked(folder.id, folder.title)
            }
        }
    }

    inner class ItemViewHolder(itemView: View) :
        BaseViewHolder<fufu.Item>(itemView) {
        lateinit var item: fufu.Item

        private var linearLayout = itemView.findViewById<LinearLayout>(R.id.layout)
        private var title: TextView = itemView.findViewById(R.id.title_tv)
        private var add: FrameLayout = itemView.findViewById(R.id.add_fl)
        private var checkBoxLl = itemView.findViewById<LinearLayout>(R.id.checkbox_ll)
        private var checkBox = itemView.findViewById<CheckBox>(R.id.checkbox)


        override fun bind(item: fufu.Item) {
            this.item = item

            if (item.isNew) {
                displayNewItem()
            } else {
                displayItem()
            }
        }

        private fun displayItem() {
            val fulltitle = when (item.unit.isBlank()) {
                true -> item.title
                else -> "${item.title}, ${item.unit}"
            }
            title.text = fulltitle
            checkBoxLl.visibility = View.VISIBLE
            checkBox.isChecked = rawData.isSelected(item.idFufu)

            add.visibility = View.GONE

            itemView.setOnTouchListener(null)
            itemView.setOnClickListener {

                val currentChecked = rawData.isSelected(item.idFufu)
                checkBox.isChecked = !currentChecked
                if (currentChecked) {
                    rawData.unSelected(item.idFufu)
                } else {
                    rawData.selected(item.idFufu)
                }
            }

            checkBox.setOnClickListener {
                val currentChecked = rawData.isSelected(item.idFufu)
                if (currentChecked) {
                    rawData.unSelected(item.idFufu)
                } else {
                    rawData.selected(item.idFufu)
                }
            }
        }

        private fun displayNewItem() {
            val fulltitle ="создать \"${item.title}\""
            title.text = fulltitle
            checkBoxLl.visibility = View.GONE

            itemView.setOnTouchListener(null)

            add.visibility = View.VISIBLE
            linearLayout.setOnClickListener {
                listener.onCreateItemClicked(item.title)
            }
        }
    }
}