package com.gmail.uia059466.liska.setting

import com.gmail.uia059466.liska.lists.sortorder.SortOrder

sealed class SettingGeneralAction {
    class SortList(val sort: SortOrder) : SettingGeneralAction()
    class SortCatalog(val sort: SortOrder) : SettingGeneralAction()
}