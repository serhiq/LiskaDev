package com.gmail.uia059466.liska.lists

import com.gmail.uia059466.liska.lists.sortorder.SortOrder

sealed class ListsAction {
  object UpdateMessages : ListsAction()
  class SortList(val sort: SortOrder) : ListsAction()
  object NewList : ListsAction()
}
