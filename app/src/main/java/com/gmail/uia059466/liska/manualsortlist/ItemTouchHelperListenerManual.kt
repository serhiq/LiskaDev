package com.gmail.uia059466.liska.manualsortlist

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperListenerManual {
  fun onItemMove(recyclerView: RecyclerView, fromPosition: Int, toPosition: Int): Boolean
}