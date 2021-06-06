package com.gmail.uia059466.liska.widget.small

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.gmail.uia059466.liska.LiskaApplication
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.data.HolderResult
import com.gmail.uia059466.liska.data.ListDisplay
import com.gmail.uia059466.liska.domain.UserPreferencesRepositoryImpl
import com.gmail.uia059466.liska.listdetail.EditItemDialog
import com.gmail.uia059466.liska.lists.sortorder.SelectThemeWidgetDialogFragment
import com.gmail.uia059466.liska.widget.WidgetContastant
import com.gmail.uia059466.liska.widget.WidgetTheme
import com.gmail.uia059466.liska.widget.listwidget.DisplayListsDialogFragment
import com.gmail.uia059466.liska.widget.listwidget.WidgetConfig
import com.gmail.uia059466.liska.widget.listwidget.WidgetConfigRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SmallWigentConfigureActivity : AppCompatActivity() {
    private lateinit var rvOpenList: RelativeLayout
    private lateinit var tvOpenList: TextView
    private lateinit var rvDisplayList: RelativeLayout
    private lateinit var tvDisplayList: TextView
    private lateinit var rvIsDisplayIcon: RelativeLayout
    private lateinit var swIsDisplayIcon: Switch
    private lateinit var rvThemeWidget: RelativeLayout
    private lateinit var tvThemeWidget: TextView
    private var toolbarTitle = R.string.widget_list_title_create

    private var selctedIndex = 0

    val isLoading = MutableLiveData<Boolean>()

    var config = WidgetConfig(isShortcurt = true, isDisplayIcon = true)

    lateinit var widgetRepository: WidgetConfigRepository

    val lists = mutableListOf<ListDisplay>()
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    //    preview
    private lateinit var previewIcon: ImageView
    private lateinit var previewLayout: LinearLayout
    private lateinit var previewTextView: TextView
    private lateinit var previewBackground: LinearLayout

    public override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        setResult(RESULT_CANCELED)
        setContentView(R.layout.small_widget_configure)

        widgetRepository = UserPreferencesRepositoryImpl.getInstance(context = this)

        val intent = intent
        val extras = intent.extras

        if (intent != null && intent.action == "android.appwidget.action.APPWIDGET_CONFIGURE") {
            if (extras != null) {
                appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
                )
                config.idWidget = appWidgetId
            }
        }

        if (intent != null && intent.action == WidgetContastant.SETTING_WIDGET) {
            if (extras != null) {
                appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
                )
                toolbarTitle = R.string.widget_list_title_setting

                val loadConfig = widgetRepository.getById(appWidgetId)

                if (loadConfig != null) {
                    config = loadConfig
                }
            }
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        val listRepository = (this.application as LiskaApplication).listRepository

        GlobalScope.launch {
            val result = listRepository.getAll()
            if (result is HolderResult.Success) {

                lists.addAll(result.data)
                isLoading.postValue(true)
            }
        }
        
        previewLayout = findViewById(R.id.widget_ll)
        previewIcon = findViewById(R.id.imageView)
        previewTextView = findViewById(R.id.appwidget_text)
        previewBackground = findViewById(R.id.preview_back)

        rvOpenList = findViewById(R.id.open_list_rv)
        tvOpenList = findViewById(R.id.open_list_current_tv)
        rvDisplayList = findViewById(R.id.name_shortcut_rv)
        tvDisplayList = findViewById(R.id.name_shortcut_current)
        rvIsDisplayIcon = findViewById(R.id.icon_is_display_rv)
        swIsDisplayIcon = findViewById(R.id.is_display_icon)
        rvThemeWidget = findViewById(R.id.theme_widget_text_rl)
        tvThemeWidget = findViewById(R.id.theme_widget_current_tv)

        isLoading.observe(
            this,
            Observer { isLoading ->
                if (isLoading) {
                    if (lists.isEmpty()) {
                        val snackBar =
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                "No lists",
                                Snackbar.LENGTH_LONG
                            )
                        snackBar.show()

                        Handler().postDelayed(
                            {
                                finish()
                            },
                            1000
                        )
                    } else {
                        setupUI()
                    }
                }
            }
        )

        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(toolbarTitle)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.list_widget, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
                trySavingWidgetData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun trySavingWidgetData() {
        if (config.isCorrect()) {
            saveWidgetAndFinish()
        }
    }

    private fun saveWidgetAndFinish() {
        widgetRepository.save(config)
        setResult(
            RESULT_OK,
            Intent().putExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetId
            )
        )
        refreshWidget()
        finish()
    }

    private fun refreshWidget() {
        val man = AppWidgetManager.getInstance(this)
        val ids = man.getAppWidgetIds(ComponentName(this, SmallWidget::class.java))
        val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(updateIntent)
    }

    private fun setupUI() {
        if (config.idList == 0L && lists.isNotEmpty()) {
            config.nameList = lists[0].title
            config.displayList = lists[0].title
            config.idList = lists[0].id
        }

        if (config.isListDeleted && lists.isNotEmpty()) {
            config.nameList = lists[0].title
            config.displayList = lists[0].title
            config.idList = lists[0].id
            config.isListDeleted = false
        }

        val findList = lists.find { it.id == config.idList }
        if (findList != null) {
            selctedIndex = lists.indexOf(findList)
        }

        updateTitleOpenList()

        rvOpenList.setOnClickListener { displayLists(lists, selctedIndex) }

        rvDisplayList.setOnClickListener {
            displayDialog()
        }

        swIsDisplayIcon.isChecked = config.isDisplayIcon

        swIsDisplayIcon.setOnCheckedChangeListener { _, isVisible ->
            config.isDisplayIcon = isVisible
            updateIconVisibillityInPreview(isVisible)
        }

        updateTheme()
        rvThemeWidget.setOnClickListener {
            displaySelectTheme(config.theme)
        }
        updateThemeInPreview()
    }

    private fun updateTitleOpenList() {
        tvOpenList.text = config.nameList
        tvDisplayList.text = config.displayList
    }

    private fun displayLists(listDisplay: List<ListDisplay>, selected: Int) {
        val dialog = DisplayListsDialogFragment.newInstance(listDisplay, selected)
        dialog.onOk = {
            selctedIndex = dialog.selectedIndex
            val newTitle = lists[selctedIndex].title
            config.nameList = newTitle
            config.displayList = newTitle
            config.idList = lists[selctedIndex].id

            config.isListDeleted = false

            previewTextView.text = newTitle

            updateTitleOpenList()
            dialog.dismiss()
        }
        supportFragmentManager.let { dialog.show(it, "editWord") }
    }

    private fun displayDialog() {
        val dialog =
            EditItemDialog.newInstance(text = config.displayList, title = "Title")
        dialog.onOk = {
            val text = dialog.editText.text.toString()
            if (text.isNotBlank()) {
                config.displayList = text
                tvDisplayList.text = config.displayList
                previewTextView.text = text
            }
        }
        supportFragmentManager.let { dialog.show(it, "editWord") }
    }

    private fun updateTheme() {
        tvThemeWidget.text = getString(WidgetTheme.requestNameTheme(config.theme))
    }

    private fun displaySelectTheme(currentTheme: WidgetTheme) {
        val dialog = SelectThemeWidgetDialogFragment.newInstance(currentTheme)
        dialog.onOk = {
            val theme = dialog.selected

            config.theme = theme
            updateThemeInPreview()
            updateTheme()
            dialog.dismiss()
        }
        supportFragmentManager.let { dialog.show(it, "widgetTheme") }
    }

    private fun updateThemeInPreview() {
        val cm = ContextCompat.getColor(this, config.theme.textColor)
        previewTextView.setTextColor(cm)

        val icon = config.theme.iconList
        previewIcon.setImageResource(icon)

        val bgColor = config.theme.background
        previewLayout.setBackgroundResource(bgColor)
    }

    private fun updateIconVisibillityInPreview(isVisible: Boolean) {
        if (isVisible) {
            previewIcon.visibility = View.VISIBLE
        } else {
            previewIcon.visibility = View.GONE
        }
    }
}