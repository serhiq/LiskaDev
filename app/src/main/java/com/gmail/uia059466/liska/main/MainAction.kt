package com.gmail.uia059466.liska.main

sealed class MainAction {
  object NewList : MainAction()
  class ToList(val id:Long) : MainAction()
  object hightLightList:MainAction()
  object hightLightCatalog:MainAction()
  class  SelectedList(val id:Long):MainAction()
}

enum class DrawerList{
  LISTS,Catalog,Empty
}