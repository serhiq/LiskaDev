package com.gmail.uia059466.liska.widget

import android.app.Application
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import com.gmail.uia059466.liska.LiskaApplication
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.domain.ListRepository
import com.gmail.uia059466.liska.domain.UserPreferencesRepositoryImpl
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.widget.listwidget.WidgetConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListWidget : AppWidgetProvider() {

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        val widgetRepository =
            UserPreferencesRepositoryImpl.getInstance(context.applicationContext as LiskaApplication)
        for (id in appWidgetIds) {
            widgetRepository.deleteById(id)
        }
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        onResive(context, intent)
    }

    private fun onResive(context: Context, intent: Intent) {
        if (intent.action == COMPLECTED_TASK) {
            val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
            val listId = intent.getLongExtra(EXTRA_LIST_ID, 0)
            val itemStr = intent.getStringExtra(EXTRA_TITLE_ITEM)

            val listRepository: ListRepository =
                (context.applicationContext as LiskaApplication).listRepository

            GlobalScope.launch {
                listRepository.complectedTask(listId, viewIndex, itemStr ?: "")
            }

            updateAllWidget(context)
        } else {
            super.onReceive(context, intent)
        }
    }

    private fun updateAllWidget(context: Context) {
        val thisAppWidget = ComponentName(context.packageName, javaClass.name)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val ids = appWidgetManager.getAppWidgetIds(thisAppWidget)
        for (appWidgetID in ids) {
            updateAppWidget(context, appWidgetManager, appWidgetID)
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.layout.list_widget)
    }

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

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {

        val widgetRepository =
            UserPreferencesRepositoryImpl.getInstance(context.applicationContext as LiskaApplication)
        val config = widgetRepository.getById(appWidgetId)
        val row = RemoteViews(context.packageName, R.layout.list_widget)
        if (config != null && config.isShortcurt) {
        } else if (config != null && !config.isListDeleted) {
            configureWiget(row, context, appWidgetId, config)
            appWidgetManager.updateAppWidget(appWidgetId, row)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view)
        } else {
            configureTapToSetting(row, context, appWidgetId)
            appWidgetManager.updateAppWidget(appWidgetId, row)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view)
        }
    }

    private fun configureWiget(
        row: RemoteViews,
        context: Context,
        appWidgetId: Int,
        config: WidgetConfig
    ) {
        configureWidget(row, config, context)

        configureRemoteAdapter(context, row, appWidgetId)
        configureClickOnAdapter(row, appWidgetId, context)
    }

    private fun configureTapToSetting(row: RemoteViews, context: Context, idWidget: Int) {

        val theme = WidgetTheme.DARK
        val bgColor = theme.background
        row.setInt(R.id.widget, "setBackgroundResource", bgColor)

        val cm = ContextCompat.getColor(context, theme.textColor)
        row.setTextColor(R.id.setting_tv, cm)

        row.setOnClickPendingIntent(
            R.id.widget, getSettingIntent(context, idWidget)
        )

        row.setViewVisibility(R.id.widget_header, View.GONE)
        row.setViewVisibility(R.id.divider, View.GONE)
        row.setViewVisibility(R.id.list_view, View.GONE)
        row.setViewVisibility(R.id.empty_view, View.GONE)

        row.setViewVisibility(R.id.setting_icon, View.VISIBLE)
        row.setViewVisibility(R.id.setting_tv, View.VISIBLE)
    }

    private fun configureClickOnAdapter(row: RemoteViews, appWidgetId: Int, context: Context) {
        val toastIntent = Intent(context, ListWidget::class.java)
        toastIntent.action = COMPLECTED_TASK
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        val toastPendingIntent = PendingIntent.getBroadcast(
            context, 0, toastIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        row.setPendingIntentTemplate(R.id.list_view, toastPendingIntent)
    }

    private fun configureWidget(row: RemoteViews, config: WidgetConfig?, context: Context) {
        if (config != null) {
            applyRowSetting(row, config, context)
            if(config.theme==WidgetTheme.DARK){
                row.setViewVisibility(R.id.divider, View.GONE)

            }else{
                row.setViewVisibility(R.id.divider, View.VISIBLE)

            }
            row.setViewVisibility(R.id.widget_header, View.VISIBLE)
            row.setViewVisibility(R.id.list_view, View.VISIBLE)
            row.setViewVisibility(R.id.empty_view, View.VISIBLE)

            row.setViewVisibility(R.id.setting_icon, View.GONE)
            row.setViewVisibility(R.id.setting_tv, View.GONE)
        }
    }

    private fun configureRemoteAdapter(
        context: Context,
        row: RemoteViews,
        appWidgetId: Int
    ) {
        val intent = Intent(context, StackWidgetService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

        row.setRemoteAdapter(R.id.list_view, intent)
        row.setEmptyView(R.id.list_view, R.id.empty_view)
    }

    private fun getSettingIntent(context: Context, appWidgetId: Int): PendingIntent? {
        val intent = Intent(context, ListWidgetConfigureActivity::class.java)
        intent.action = WidgetContastant.SETTING_WIDGET
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        return PendingIntent.getActivity(context, appWidgetId, intent, 0)
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

    private fun applyRowSetting(row: RemoteViews, config: WidgetConfig, context: Context) {
//       title
        row.setOnClickPendingIntent(
            R.id.title_container,
            getIntentOpenList(
                context,
                config.idList
            )
        )

        row.setTextViewText(R.id.title_container, config.displayList)

        val cm = ContextCompat.getColor(context, config.theme.textColor)
        row.setTextColor(R.id.title_container, cm)

//       icon setting
        row.setOnClickPendingIntent(
            R.id.widget_button_fl, getSettingIntent(context, config.idWidget)
        )
        row.setOnClickPendingIntent(
            R.id.widget_button, getSettingIntent(context, config.idWidget)
        )

        if (config.isDisplayIcon) {
            row.setViewVisibility(R.id.widget_button, View.VISIBLE)
            val icon = config.theme.iconSetting
            row.setInt(
                R.id.widget_button,
                "setImageResource", icon
            )
        } else {
            row.setViewVisibility(R.id.widget_button, View.INVISIBLE)
        }

        //            фон
        val bgColor = config.theme.background
        row.setInt(
            R.id.widget,
            "setBackgroundResource", bgColor
        )
    }

    companion object {
        const val COMPLECTED_TASK = "com.example.android.stackwidget.TOAST_ACTION"
        const val EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM"
        const val EXTRA_LIST_ID = "iddM"
        const val EXTRA_TITLE_ITEM = "titleitem"

        fun notifyAllChanged(context: Context) {
            val application: Application = context.applicationContext as Application
            val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(
                ComponentName(application, ListWidget::class.java)
            )
            if (ids.isEmpty()) {
                return
            }
            AppWidgetManager.getInstance(application).notifyAppWidgetViewDataChanged(ids, R.id.list)
        }
    }
}
