package com.gmail.uia059466.liska.domain

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.gmail.uia059466.liska.data.Mode
import com.gmail.uia059466.liska.addeditcatalog.units.UnitsRepository
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import com.gmail.uia059466.liska.setting.selectcatalog.CatalogDisplayOption
import com.gmail.uia059466.liska.setting.themes.Theme
import com.gmail.uia059466.liska.widget.listwidget.WidgetConfig
import com.gmail.uia059466.liska.widget.listwidget.WidgetConfigRepository


class UserPreferencesRepositoryImpl private constructor(context: Context) : UserRepository,
                                                                            UnitsRepository,
                                                                            WidgetConfigRepository {
  
  private val PREFS_FILENAME = "simplelist"
  
  private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
  
  override fun readSortOrderList(): SortOrder {
    return sortOrderList
  }
  
  override fun saveSortOrderList(newSortOrder: SortOrder) {
    sortOrderList = newSortOrder
  }

  var sortOrderList: SortOrder
    get() = prefs.getString(PREFS_SORT_ORDER_LIST,null) ?.let {  SortOrder.valueOf(it) } ?: SortOrder.A_Z
    set(value) = prefs.edit { putString(PREFS_SORT_ORDER_LIST, value.name) }

  var sortOrderCatalog: SortOrder
    get() = prefs.getString(PREFS_SORT_ORDER_CATALOG,null) ?.let {  SortOrder.valueOf(it) } ?: SortOrder.A_Z
    set(value) = prefs.edit { putString(PREFS_SORT_ORDER_CATALOG, value.name) }


  var catalogDisplayOption: CatalogDisplayOption
    get() = prefs.getString(PREFS_CATALOG_DISPLAY_OPTION, null)?.let { CatalogDisplayOption.fromString(it) } ?: CatalogDisplayOption.TWO_VAR
    set(value) = prefs.edit { putString(PREFS_CATALOG_DISPLAY_OPTION, value.rawValue) }


  override fun readSortOrderCatalog(): SortOrder {
    return sortOrderCatalog
  }
  
  override fun saveSortOrderCatalog(newSortOrder: SortOrder) {
    sortOrderCatalog = newSortOrder
  }
  
  private val PREF_IS_MOVING_CHECKED = "moving_checked"
  
  var isMovedChecked: Boolean
    get() = prefs.getBoolean(PREF_IS_MOVING_CHECKED, false)
    set(value) = prefs.edit().putBoolean(PREF_IS_MOVING_CHECKED, value).apply()
  
  private val PREF_IS_OPEN_LAST_LIST = "is_open_last_list"
  var isOpenLastList: Boolean
    get() = prefs.getBoolean(PREF_IS_OPEN_LAST_LIST, false)
    set(value) = prefs.edit().putBoolean(PREF_IS_OPEN_LAST_LIST, value).apply()
  
  var lastId: Long
    get() = prefs.getLong("last_id", 0L)
    set(value) = prefs.edit().putLong("last_id", value).apply()
  
  val PREF_LIST_UNITS = "pref_list_units"
  
  private var listUnits: String
    get() = prefs.getString(PREF_LIST_UNITS, "").toString()
    set(value) {
      prefs.edit().putString(PREF_LIST_UNITS, value).apply()
    }
  
  override fun saveUnits(list: List<String>) {
    listUnits = saveList(list)
  }
  
  fun readUnits(): List<String> {
    return readList(listUnits)
  }
  
  val PREF_FAV_LIST_UNITS = "pref_fav_list_units"
  
  private var listFavUnits: String
    get() = prefs.getString(PREF_FAV_LIST_UNITS, "").toString()
    set(value) {
      prefs.edit().putString(PREF_FAV_LIST_UNITS, value).apply()
    }
  
  fun saveFavUnits(list: List<String>) {
    listFavUnits = saveList(list)
  }
  
  fun readFavUnits(): List<String> {
    return readList(listFavUnits)
  }
  
  val PREF_SELECTED_UNITS = "pref_selected_units"
  
  var selectedUnit: String
    get() = prefs.getString(PREF_SELECTED_UNITS, "").toString()
    set(value) {
      prefs.edit().putString(PREF_SELECTED_UNITS, value).apply()
    }
  
  private fun saveList(list: List<String>): String {
    return when (list.isEmpty()) {
      true -> ""
      false -> list.joinToString(",")
    }
  }
  
  private fun readList(str: String): List<String> {
    return when (str.isEmpty()) {
      true -> ArrayList()
      false -> str.split(",")
    }
  }
  
  override fun units(): List<String> {
    return readList(listUnits)
  }
  
  override fun favorites(): List<String> {
    return readList(listFavUnits)
  }
  
  override fun saveFavorites(list: List<String>) {
    saveFavUnits(list)
  }
  
  override fun saveSelected(selected: String) {
    selectedUnit = selected
  }
  
  override fun selected(): String {
    return selectedUnit
  }
  
  val PREF_IS_DISPLAY_FOLDER_IN_CATALOG = "pref_is_display_folder"
  
  private var isDisplayFolderInCatalog: Boolean
    get() = prefs.getBoolean(PREF_IS_DISPLAY_FOLDER_IN_CATALOG, true)
    set(value) {
      prefs.edit().putBoolean(PREF_IS_DISPLAY_FOLDER_IN_CATALOG, value).apply()
    }
  
  val PREF_IS_LIST_MODE = "pref_is_list"
  
  var isListMode: Boolean
    get() = prefs.getBoolean(PREF_IS_LIST_MODE, true)
    set(value) {
      prefs.edit().putBoolean(PREF_IS_LIST_MODE, value).apply()
    }
  
  override fun readIsDisplayFolder(): Boolean {
    return isDisplayFolderInCatalog
  }
  
  override fun saveIsDisplayFolder(isDisplayFolder: Boolean) {
    isDisplayFolderInCatalog = isDisplayFolder
  }


  var currentTheme: Theme
    get() = prefs.getString(PREFS_CURRENT_THEME, null)?.let { Theme.fromString(it) } ?: Theme.BLUE
    set(value) = prefs.edit { putString(PREFS_CURRENT_THEME, value.rawValue) }

  var nightMode: Mode
    get() = prefs.getString(PREF_NIGHT_MODE, null)?.let { Mode.fromString(it) } ?: Mode.SYSTEM
    set(value) = prefs.edit { putString(PREF_NIGHT_MODE, value.rawValue) }


//    widget setting  START
  override fun setupDeletedListForConfig(idList: Long) {
    val configs = requestWidgetConfig()
    for (config in configs) {
      if (config.idList == idList) {
        config.isListDeleted = true
        update(config)
      }
    }
  }
  
  private fun requestWidgetConfig(): List<WidgetConfig> {
    val widgets = readWidgetList()
    val configs = mutableListOf<WidgetConfig>()
    for (id in widgets) {
      val result = getById(id)
      if (result != null) {
        configs.add(result)
      }
    }
    return configs
  }
  
  override fun getBigWidget(): IntArray {
    val configs = requestWidgetConfig()
    return configs.filter { !it.isShortcurt }.map { it.idWidget }.toIntArray()
  }
  
  override fun getSmallWidget(): IntArray {
    val configs = requestWidgetConfig()
    return configs.filter { it.isShortcurt }.map { it.idWidget }.toIntArray()
  }
  
  val PREF_WIDGET_ID = "widget_ids"
  
  private var listWidgetIds: String
    get() = prefs.getString(PREF_WIDGET_ID, "").toString()
    set(value) {
      prefs.edit().putString(PREF_WIDGET_ID, value).apply()
    }
  
  fun addWidgetToList(idWidget: Int) {
    val ids = readWidgetList().toMutableSet()
    ids.add(idWidget)
    saveWidgetList(ids)
  }
  
  fun deleteWidgetFromList(idWidget: Int) {
    val ids = readWidgetList().toMutableSet()
    ids.remove(idWidget)
    saveWidgetList(ids)
  }
  
  fun saveWidgetList(list: Set<Int>) {
    listWidgetIds = saveListInt(list)
  }
  
  fun readWidgetList(): Set<Int> {
    return readListInt(listWidgetIds)
  }
  
  private fun saveListInt(list: Set<Int>): String {
    return when (list.isEmpty()) {
      true -> ""
      false -> list.joinToString(",")
    }
  }
  
  private fun readListInt(str: String): Set<Int> {
    return when (str.isEmpty()) {
      true -> emptySet<Int>()
      false -> trySplit(str)
    }
  }
  
  private fun trySplit(str: String): Set<Int> {
    return try {
      str.split(",").map { it.toInt() }.toSet()
    } catch (e: Exception) {
      return emptySet()
    }
  }
  
  override fun getById(widgetId: Int): WidgetConfig? {
    val wd = prefs.getString(PREF_WIDGET + widgetId, "").toString()
    return if (wd.isBlank()) {
      null
    } else {
      WidgetConfig.fromString(wd)
    }
  }
  
  override fun save(config: WidgetConfig) {
    addWidgetToList(config.idWidget)
    
    prefs.edit().putString(PREF_WIDGET + config.idWidget, config.toStingStr()).apply()
  }
  
  override fun update(config: WidgetConfig) {
    prefs.edit().putString(PREF_WIDGET + config.idWidget, config.toStingStr()).apply()
  }
  
  override fun deleteById(widgetId: Int) {
    deleteWidgetFromList(widgetId)
    prefs.edit().remove(PREF_WIDGET + widgetId).apply()
  }
  
  
  private val PREF_IS_FIRST_LAUNCH = "is_first_launch"
  
  var isFirstLaunch: Boolean
    get() = prefs.getBoolean(PREF_IS_FIRST_LAUNCH, true)
    set(value) = prefs.edit().putBoolean(PREF_IS_FIRST_LAUNCH, value).apply()
  
  
  private val PREF_WIDGET = "PREF_WIDGET"
  
  ///Rate app
  val IS_SHOW_RATE_APP = "is_show_rate_app"
  
  var isShowRateAppDialog: Boolean
    get() = prefs.getBoolean(IS_SHOW_RATE_APP, false)
    set(value) = prefs.edit().putBoolean(IS_SHOW_RATE_APP, value).apply()
  
  val IS_NOT_SHOW_RATE = "is_show_rate_app_disabled"
  var isShowRateDissabled: Boolean
    get() = prefs.getBoolean(IS_NOT_SHOW_RATE, false)
    set(value) = prefs.edit().putBoolean(IS_NOT_SHOW_RATE, value).apply()
  
  val LAUNCH_COUNT = "launch_count"
  
  var launchCount: Int
    get() = prefs.getInt(LAUNCH_COUNT, 0)
    set(value) = prefs.edit().putInt(LAUNCH_COUNT, value).apply()
  
  val FIRST_LAUNCH = "first_launch"
  
  var firstLaunch: Long
    get() = prefs.getLong(FIRST_LAUNCH, 0)
    set(value) = prefs.edit().putLong(FIRST_LAUNCH, value).apply()
  
  companion object {
    
    @Volatile
    private var INSTANCE: UserPreferencesRepositoryImpl? = null
    
    fun getInstance(context: Context): UserPreferencesRepositoryImpl {
      return INSTANCE ?: synchronized(this) {
        val instance = UserPreferencesRepositoryImpl(context)
        INSTANCE = instance
        instance
      }
    }

    private const val PREFS_CATALOG_DISPLAY_OPTION = "select_sort_key"
    private const val PREFS_CURRENT_THEME = "themes"
    private const val PREF_NIGHT_MODE = "night_mode"

    private const val PREFS_SORT_ORDER_LIST = "sort_order"
    private const val PREFS_SORT_ORDER_CATALOG = "sort_order_catalog"
  }
}