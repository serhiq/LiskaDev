package com.gmail.uia059466.liska.selectfromcatalogquantity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.catalog.CatalogSelectState
import com.gmail.uia059466.liska.catalog.fufu
import com.gmail.uia059466.liska.data.database.CatalogDatabase
import com.gmail.uia059466.liska.utils.getThemeColor

class CatalogSelectAdapterQuantity(
    private val listener: CatalogSelectListener,
) : CatalogSelect, RecyclerView.Adapter<CatalogSelectAdapterQuantity.BaseViewHolder<*>>() {
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
                    R.layout.item_catalog_quantity, parent,
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

        private var linearLayout = itemView.findViewById<LinearLayout>(R.id.item_ll)
        private var title: TextView = itemView.findViewById(R.id.title_tv)
        private var decription_tv: TextView = itemView.findViewById(R.id.decription_tv)
        private var decreaseImg: ImageView = itemView.findViewById(R.id.decrease_img)
        private var quantityEditText: EditText = itemView.findViewById(R.id.quantity_tv)
        private var addImg: ImageView = itemView.findViewById(R.id.add_img)

        private val quantityColorDefault=itemView.getThemeColor(R.attr.color_quantity_default)
        private val quantityColorSelect=itemView.getThemeColor(R.attr.color_quantity_select)

        override fun bind(item: fufu.Item) {

            this.item = item

            if (item.isNew) {
                val str=itemView.context.getString(R.string.create_in_recycler)

                displayNewItem(str)
            } else {
                displayItem()
            }
        }

        private fun displayItem() {
            addImg.setImageDrawable(AppCompatResources.getDrawable(addImg.context, R.drawable.ic_add_dissabled_24dp))
            val text=if (item.unit.isNotBlank()){
                "${item.title}, ${item.unit}"
            }else{
                item.title
            }
            title.text = text

            renderQuantity(selectedQuantity = rawData.quantity(item.idFufu))

            itemView.setOnTouchListener(null)
            linearLayout.setOnClickListener(null)

            addImg.setOnClickListener {
                val newVal = rawData.increase(item.idFufu, item.quantity)
                renderQuantity(newVal)
            }

            decreaseImg.setOnClickListener {
                val newVal = rawData.decrease(item.idFufu)
                renderQuantity(newVal)
            }
        }



        private fun renderQuantity(selectedQuantity: Int) {
            if (selectedQuantity==0&&item.quantity!=0){
                quantityEditText.setText(item.quantity.toString())
                quantityEditText.setTextColor(quantityColorDefault)
                decreaseImg.visibility = View.GONE
                addImg.visibility = View.VISIBLE

            }
            else if (selectedQuantity == 0) {
                decreaseImg.visibility = View.GONE
                addImg.visibility = View.VISIBLE
                quantityEditText.visibility = View.GONE

            } else {
                quantityEditText.setTextColor(quantityColorSelect)

                decreaseImg.visibility = View.VISIBLE
                addImg.visibility = View.VISIBLE
                quantityEditText.visibility = View.VISIBLE
                quantityEditText.setText(selectedQuantity.toString())
            }
        }

        private fun displayNewItem(str: String) {
            val fulltitle = str+"\"${item.title}\""
            title.text = fulltitle
            addImg.setImageDrawable(AppCompatResources.getDrawable(addImg.context, R.drawable.ic_add_secondary_24dp))
            addImg.visibility = View.VISIBLE
            addImg.setOnClickListener {
                listener.onCreateItemClicked(item.title)
            }


            decreaseImg.visibility = View.GONE
            quantityEditText.visibility = View.GONE

            if (item.unit.isNotBlank()) {
                decription_tv.visibility = View.VISIBLE
                decription_tv.text = item.unit
            } else {
                decription_tv.visibility = View.GONE

            }
            itemView.setOnTouchListener(null)
            linearLayout.setOnClickListener {
                listener.onCreateItemClicked(item.title)
            }
        }
    }
}






