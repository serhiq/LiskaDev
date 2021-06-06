package com.gmail.uia059466.liska.main

sealed class AppBarUiState {
  object EmptyApp:AppBarUiState()
  class  IconNavigationWithTitle(val title:String):AppBarUiState()
  class  ArrayWithTitle(val title:String):AppBarUiState()
}