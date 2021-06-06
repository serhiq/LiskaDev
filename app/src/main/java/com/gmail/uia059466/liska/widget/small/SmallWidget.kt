package com.gmail.uia059466.liska.widget.small

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import com.gmail.uia059466.liska.LiskaApplication
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.domain.UserPreferencesRepositoryImpl
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.widget.WidgetContastant
import com.gmail.uia059466.liska.widget.WidgetTheme
import com.gmail.uia059466.liska.widget.listwidget.WidgetConfig

class SmallWidget : AppWidgetProvider() {
  
  override fun onUpdate(
          context: Context,
          appWidgetManager: AppWidgetManager,
          appWidgetIds: IntArray
                       ) {
    
    for (id in appWidgetIds) {
      try {
        updateAppWidget(context, appWidgetManager, id)
      } catch (e: Exception) {
      }
    }
  }
  
  override fun onDeleted(context: Context, appWidgetIds: IntArray) {
    val widgetRepository =
            UserPreferencesRepositoryImpl.getInstance(context.applicationContext as LiskaApplication)
    for (id in appWidgetIds) {
      widgetRepository.deleteById(id)
    }
  }
  
  override fun onEnabled(context: Context) {
    super.onEnabled(context)
  }
  
  override fun onDisabled(context: Context) {
    super.onEnabled(context)
  }
}

internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
                            ) {
  
  val widgetRepository =
          UserPreferencesRepositoryImpl.getInstance(context.applicationContext as LiskaApplication)
  val config = widgetRepository.getById(appWidgetId)
  val row = RemoteViews(context.packageName, R.layout.small_widget)
  if (config != null && !config.isShortcurt) {
  } else if (config != null && !config.isListDeleted) {
    configureWiget(row, context, appWidgetId, config)
  } else {
    configureTapToSetting(row, context, appWidgetId)
  }
  appWidgetManager.updateAppWidget(appWidgetId, row)
}

fun configureTapToSetting(row: RemoteViews, context: Context, idWidget: Int) {
  val theme = WidgetTheme.DARK
  //            фон
  val bgColor = theme.background
  row.setInt(
          R.id.widget_ll,
          "setBackgroundResource", bgColor
            )
  
  row.setOnClickPendingIntent(
          R.id.widget_ll, getSettingIntent(context, idWidget)
                             )
  
  row.setViewVisibility(R.id.imageView, View.GONE)
  row.setViewVisibility(R.id.appwidget_text, View.GONE)
  
  row.setViewVisibility(R.id.setting_icon, View.VISIBLE)
}

private fun getSettingIntent(context: Context, appWidgetId: Int): PendingIntent? {
  val intent = Intent(context, SmallWigentConfigureActivity::class.java)
  intent.action = WidgetContastant.SETTING_WIDGET
  intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
  return PendingIntent.getActivity(context, appWidgetId, intent, 0)
}

fun configureWiget(row: RemoteViews, context: Context, appWidgetId: Int, config: WidgetConfig) {
  if (config != null) {
    row.setViewVisibility(R.id.imageView, View.VISIBLE)
    row.setViewVisibility(R.id.appwidget_text, View.VISIBLE)
    
    row.setViewVisibility(R.id.setting_icon, View.GONE)
    
    applyRowSetting(row, config, context)
  }
}

fun applyRowSetting(row: RemoteViews, config: WidgetConfig, context: Context) {

  row.setOnClickPendingIntent(
          R.id.widget_ll,
          getIntentOpenList(
                  context,
                  config.idList
                           )
                             )
  
  row.setTextViewText(R.id.appwidget_text, config.displayList)
  
  val cm = ContextCompat.getColor(context, config.theme.textColor)
  row.setTextColor(R.id.appwidget_text, cm)
  
  if (config.isDisplayIcon) {
    row.setViewVisibility(R.id.imageView, View.VISIBLE)
    val icon = config.theme.iconList
    row.setInt(
            R.id.imageView,
            "setImageResource", icon
              )
  } else {
    row.setViewVisibility(R.id.imageView, View.INVISIBLE)
  }
  
  //            фон
  val bgColor = config.theme.background
  row.setInt(
          R.id.widget_ll,
          "setBackgroundResource", bgColor
            )
}

private fun getIntentOpenList(
        context: Context,
        value: Long
                             ): PendingIntent {
  val intent = Intent(context, MainActivityImpl::class.java)
  intent.action = WidgetContastant.OPENLIST_WIDGET
  intent.putExtra(WidgetContastant.LIST_ID, value)
  return PendingIntent.getActivity(context, value.toInt(), intent, 0)
}