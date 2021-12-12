package com.gmail.uia059466.liska.main

sealed class MainAction {
  object NewList : MainAction()
  class ToList(val id:Long) : MainAction()
//  todo refactor with enum
  object hightLightList:MainAction()
  object hightLightCatalog:MainAction()
  object hightLightWareHouse:MainAction()
  class  SelectedList(val id:Long):MainAction()
}

enum class DrawerList{
  LISTS,Catalog,Empty, WAREHOUSE
}