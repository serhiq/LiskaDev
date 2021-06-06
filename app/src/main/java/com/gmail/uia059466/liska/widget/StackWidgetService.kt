package com.gmail.uia059466.liska.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.RemoteViewsService.RemoteViewsFactory
import androidx.core.content.ContextCompat
import com.gmail.uia059466.liska.LiskaApplication
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.data.HolderResult
import com.gmail.uia059466.liska.data.database.ListItem
import com.gmail.uia059466.liska.domain.ListRepository
import com.gmail.uia059466.liska.domain.UserPreferencesRepositoryImpl
import com.gmail.uia059466.liska.widget.listwidget.WidgetConfig
import kotlinx.coroutines.runBlocking

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory? {
        if (intent == null) {
            return null
        }

        val extras = intent.extras ?: return null

        val widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)

        return StackRemoteViewsFactory(this.applicationContext, widgetId)
    }
}

class StackRemoteViewsFactory(private val mContext: Context, val widgetId: Int) :
    RemoteViewsFactory {
    private val listRepository: ListRepository =
        (mContext.applicationContext as LiskaApplication).listRepository
    private val configRepo = UserPreferencesRepositoryImpl.getInstance(mContext)
    var config: WidgetConfig?

    init {
        config = configRepo.getById(widgetId)
    }

    private var listItem = mutableListOf<ListItem>()

    override fun onDataSetChanged() {
        config = configRepo.getById(widgetId)
        if (config != null && !config!!.isListDeleted) {
            runBlocking {
                val result = listRepository.getById(config!!.idList)
                if (result is HolderResult.Success) {
                    listItem.clear()
                    listItem.addAll(result.data.data)
                }
            }
        } else {
            listItem.clear()
        }
    }

    override fun onCreate() {
    }

    override fun onDestroy() {
        // In onDestroy() you should tear down anything that was setup for your data source,
        // eg. cursors, connections, etc.
        listItem.clear()
    }

    override fun getCount(): Int {
        return listItem.count()
    }

    override fun getViewAt(position: Int): RemoteViews {
        val row = RemoteViews(mContext.packageName, R.layout.widget_row_light)

        if (config != null) {
            row.setTextViewText(R.id.widget_text, listItem[position].title)

            val cm = ContextCompat.getColor(mContext, config!!.theme.textColor)
            row.setTextColor(R.id.widget_text, cm)

            val isComplected = listItem[position].isChecked
            val checkBox = config!!.requestCheckbox(isComplected)


            row.setImageViewResource(
                R.id.widget_complete_box,
                checkBox
            )

        }

        val extras = Bundle()
        extras.putInt(ListWidget.EXTRA_ITEM, position)
        extras.putLong(ListWidget.EXTRA_LIST_ID, config?.idList ?: 0)
        extras.putString(ListWidget.EXTRA_TITLE_ITEM, listItem[position].title)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        row.setOnClickFillInIntent(R.id.widget_row, fillInIntent)
        return row
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}