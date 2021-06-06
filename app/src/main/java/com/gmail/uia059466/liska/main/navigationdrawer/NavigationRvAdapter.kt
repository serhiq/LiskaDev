package com.gmail.uia059466.liska.main.navigationdrawer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.uia059466.liska.R

class NavigationRVAdapter(val listener: Listener) :RecyclerView.Adapter<NavigationRVAdapter.NavigationItemViewHolder>() {

    private lateinit var context: Context

    private val data= mutableListOf<NavigationItemModel>()

    private var selectedId=-1L

    fun setData(list:List<NavigationItemModel>){
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun currentListPosition(idSelected: Long){
        selectedId=idSelected
        notifyDataSetChanged()
    }


    class NavigationItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
         var navigation_title: TextView = itemView.findViewById(R.id.navigation_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationItemViewHolder {
        context = parent.context
        val navItem = LayoutInflater.from(parent.context).inflate(R.layout.row_nav_drawer, parent, false)
        return NavigationItemViewHolder(navItem)
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    override fun onBindViewHolder(holder: NavigationItemViewHolder, position: Int) {
        val element = data[position]

        holder.itemView.setOnClickListener {
            listener.click(data[position].idList)
            notifyDataSetChanged()
        }

        val selected = R.drawable.background_round_padded_selected
        val unSelected = 0

        val drawable=if (selectedId==element.idList){
            selected
        }else{
            unSelected
        }
        holder.itemView.setBackgroundResource(drawable)
        holder.navigation_title.text = data[position].title
    }

    interface Listener{
        fun click(id:Long)
    }
}