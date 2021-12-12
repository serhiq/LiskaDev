package com.gmail.uia059466.liska.warehouse

import com.gmail.uia059466.liska.lists.sortorder.SortOrder

sealed class WarehouseAction {

  object UpdateMessages : WarehouseAction()
  class Sort(val sort: SortOrder) : WarehouseAction()
  object New : WarehouseAction()
}