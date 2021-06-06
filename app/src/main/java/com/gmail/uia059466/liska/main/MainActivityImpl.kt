package com.gmail.uia059466.liska.main

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.widget.doAfterTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.ServiceLocator
import com.gmail.uia059466.liska.domain.ListRepository
import com.gmail.uia059466.liska.domain.UserPreferencesRepositoryImpl
import com.gmail.uia059466.liska.main.navigationdrawer.NavigationRVAdapter
import com.gmail.uia059466.liska.setting.themes.SelectThemeDialogFragment
import com.gmail.uia059466.liska.setting.themes.Theme
import com.gmail.uia059466.liska.utils.InjectorUtils
import com.gmail.uia059466.liska.widget.WidgetContastant
import com.gmail.uia059466.liska.widget.listwidget.WidgetConfigRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivityImpl : AppCompatActivity(), MainActivity, NavigationRVAdapter.Listener {
    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout

    private lateinit var adapter: NavigationRVAdapter
    private lateinit var viewModel: MainActivityModel

    private lateinit var searchEt: EditText

    lateinit var navLists: LinearLayout
    lateinit var navCatalog: LinearLayout

    lateinit var navigation_rv: RecyclerView

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setupAppTheme()
        setupToolbar()
        setupViewModel()


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navigation_rv = findViewById(R.id.navigation_rv)
        adapter = NavigationRVAdapter(this)
        navigation_rv.adapter = adapter

        navLists = findViewById<LinearLayout>(R.id.nav_lists)
        navCatalog = findViewById<LinearLayout>(R.id.nav_catalog)
        val navAddList = findViewById<LinearLayout>(R.id.nav_add_list)
        val navSetting = findViewById<ImageView>(R.id.setting_img)

        searchEt = findViewById<EditText>(R.id.search_et)

        navLists.setOnClickListener {
            viewModel.takeAction(MainAction.hightLightList)
            navigateWithClearBackStack(R.id.action_global_lists)
        }

        navCatalog.setOnClickListener {
            viewModel.takeAction(MainAction.hightLightCatalog)
            val action = R.id.action_global_catalog
            navigateWithClearBackStack(action)
        }

        navAddList.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            viewModel.takeAction(MainAction.NewList)
        }

        navSetting.setOnClickListener {
            val action = R.id.action_global_setting
            navigateWithClearBackStack(action)
        }

        setupObservers()

        if (intent != null && intent.action == WidgetContastant.OPENLIST_WIDGET) {

            val listId = intent.getLongExtra(WidgetContastant.LIST_ID, 0)

            val action = R.id.action_global_display_list
            val bundle = bundleOf("isEditMode" to true, "listId" to listId, "isNew" to false)
            navigateWithClearBackStack(action, bundle)
        }

        val prefs = UserPreferencesRepositoryImpl.getInstance(this)
        val lastId = prefs.lastId
        if (lastId !== 0L && prefs.isOpenLastList) {
            viewModel.takeAction(MainAction.ToList(lastId))
        }

        if (prefs.isFirstLaunch){
            insertUnits(prefs)
        }
        initRatingApp(prefs)

//        insertTestUnits()

        drawerLayout = findViewById(R.id.drawer)
        navigation_rv.layoutManager = LinearLayoutManager(this)
        navigation_rv.setHasFixedSize(true)

        drawerToggle = object : ActionBarDrawerToggle(
            this, drawerLayout,
            R.string.open,
            R.string.close
                                                     ) {

            override fun onDrawerClosed(drawerView: View) {
                // Triggered once the drawer closes
                super.onDrawerClosed(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }

        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private val LAUNCHES_UNTIL_PROMPT = 10
    private val DAYS_UNTIL_PROMPT = 3

    private fun initRatingApp(prefs: UserPreferencesRepositoryImpl) {
        if (prefs.isShowRateDissabled) return


        // Increment launch counter
        val launchCount = prefs.launchCount++
        prefs.launchCount=launchCount

        // Get date of first launch
        var dateFirstlaunch = prefs.firstLaunch
        if (dateFirstlaunch == 0L) {
            dateFirstlaunch = System.currentTimeMillis()
            prefs.firstLaunch=dateFirstlaunch
        }

        val dayWhenShow=dateFirstlaunch + DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000;
        if
            (launchCount >= LAUNCHES_UNTIL_PROMPT
            && System.currentTimeMillis() >= dayWhenShow ) {
            prefs.isShowRateAppDialog=true
        }
    }

    private fun insertUnits(prefs: UserPreferencesRepositoryImpl) {

        val isRussian=applicationContext.resources.getBoolean(R.bool.isRussian)
        val units=if (isRussian){
            listOf<String>("шт","кг","г","л")
        }else{
            listOf<String>("kg","g")
        }
           prefs.saveUnits(units)
            prefs.saveFavUnits(units)
    }


    private fun navigateWithClearBackStack(@IdRes resId: Int, args: Bundle? = null) {
        drawer.closeDrawer(GravityCompat.START)
        navController.navigateSingleTop(resId, args)
    }

    fun hamburgerIconDisplay() {
        drawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupObservers() {
        viewModel.lists.observe(
            this,
            Observer {
                it?.let {
                    adapter.setData(it.toMutableList())
                }
            }
                               )
        viewModel.toNewList.observe(
            this,
            Observer {
                it?.let { id ->
                    val action = R.id.action_global_display_list
                    val bundle = bundleOf("isEditMode" to true, "listId" to id, "isNew" to true)
                    navigateWithClearBackStack(action, bundle)
                }
            }
                                   )

        viewModel.toDisplayList.observe(
            this,
            Observer {
                it?.let { id ->
                    val action = R.id.action_global_display_list
                    val bundle = bundleOf("isEditMode" to false, "listId" to id, "isNew" to false)
                    navigateWithClearBackStack(action, bundle)
                }
            }
                                       )

        viewModel.toEditList.observe(
            this,
            Observer {
                it?.let { id ->
                    val action = R.id.action_global_display_list
                    val bundle = bundleOf("isEditMode" to true, "listId" to id, "isNew" to false)
                    navigateWithClearBackStack(action, bundle)
                }
            }
                                    )

        viewModel.currentRvPosition.observe(
            this,
            Observer {
                it?.let { mumu ->
                    adapter.currentListPosition(mumu)
                }
            }
                                           )

        viewModel.currentDrawerPosition.observe(
            this,
            Observer {
                it?.let { li ->
                    light(li)
                }
            }
                                               )
    }

    fun light(rList: DrawerList) {
        val selected = R.drawable.background_round_padded_selected
        val unSelected = 0
        when (rList) {
            DrawerList.LISTS -> {
                navLists.setBackgroundResource(selected)
                navCatalog.setBackgroundResource(unSelected)
            }
            DrawerList.Catalog -> {
                navLists.setBackgroundResource(unSelected)
                navCatalog.setBackgroundResource(selected)
            }
            DrawerList.Empty -> {
                navLists.setBackgroundResource(unSelected)
                navCatalog.setBackgroundResource(unSelected)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when {
            (item.itemId == android.R.id.home && drawerToggle.isDrawerIndicatorEnabled) -> {
                drawerLayout.openDrawer(GravityCompat.START)
//                todo  итак, поставить false, для обраотки сохранения
//              todo  во все фрагментов поставить проверку открыть или нет navigation drawer

//                делаю фалс чтобы событие отправлялась фрагмен ам
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun renderAppbar(state: AppBarUiState) {
        when (state) {
            AppBarUiState.EmptyApp -> hideActionBar()
            is AppBarUiState.IconNavigationWithTitle -> renderTitleNav(state.title)
            is AppBarUiState.ArrayWithTitle -> renderBackArrayAndTitle(state.title)
        }
    }

    private fun hideActionBar() {
        val isHide = supportActionBar?.isShowing
        isHide.let {
            if (isHide == true) supportActionBar?.hide()
        }
    }

    private fun renderBackArrayAndTitle(title: String) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawerToggle.isDrawerIndicatorEnabled = false
        renderTitle(title)
    }

    private fun renderTitleNav(title: String) {
        drawerToggle.isDrawerIndicatorEnabled = true
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        renderTitle(title)
    }

    private fun renderTitle(title: String) {
        val isShow = supportActionBar?.isShowing
        isShow.let {
            if (isShow != true) supportActionBar?.show()
        }
        supportActionBar?.title = title
    }

    fun setTitleAppBar(title: String) {
        val titleCustom = findViewById<TextView>(R.id.title_app_bar)
        titleCustom.visibility = View.GONE
        supportActionBar?.title = title
        supportActionBar?.show()
    }

    override fun navigateTo(action: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(action, null)
    }

    @SuppressLint("VisibleForTests")
    fun setupFake(repository: ListRepository) {
        ServiceLocator.listRepository = repository
    }

    fun hideCustomTitle() {
        val title = findViewById<TextView>(R.id.title_app_bar)
        title.visibility = View.GONE
        setupClickListener()
    }

    fun setupClickListener(onClick: (() -> Unit)? = null) {
        val title = findViewById<TextView>(R.id.title_app_bar)
        title.setOnClickListener {
            onClick?.invoke()
        }
    }
    fun setupCustomTitle(titleStr: String) {
        val title = findViewById<TextView>(R.id.title_app_bar)
        title.visibility = View.VISIBLE
        title.text = titleStr
        supportActionBar?.title = ""
    }

    private fun setupViewModel() {
        val application = requireNotNull(this).application
        val viewModelFactory = InjectorUtils.provideViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainActivityModel::class.java]
    }

    override fun click(id: Long) {
        Handler().postDelayed(
            {
                drawerLayout.closeDrawer(GravityCompat.START)
            },
            200
                             )
        viewModel.takeAction(MainAction.ToList(id))
//        viewModel.takeAction(MainAction.removeHightListh)
    }

    fun updateNavigationDrawer() {
        viewModel.refreshList()
    }

    fun displayListInNavigationDrawer() {
        viewModel.takeAction(MainAction.hightLightList)
    }

    fun openNewList() {
        viewModel.takeAction(MainAction.NewList)
    }

    fun toDisplayList(id: Long) {
        viewModel.takeAction(MainAction.ToList(id))
    }

    fun displayHightList(id: Long) {
        viewModel.takeAction(MainAction.SelectedList(id))
    }

    fun displaySearchView(onTextChange: ((text: String) -> Unit)? = null) {
        searchEt.doAfterTextChanged {
            onTextChange?.invoke(it.toString())
        }
        searchEt.visibility = View.VISIBLE
        searchEt.requestFocus()
    }

    fun getTextSearchView(): String {
        return searchEt.text.toString()
    }

    fun clearSearchView() {
        searchEt.setText("")
    }

    fun hideSearchView() {
        searchEt.visibility = View.GONE
    }

    /*
    **  Theme selection
     */

    lateinit var currentTheme: Theme

    private fun setupAppTheme() {
        val g = UserPreferencesRepositoryImpl.getInstance(this)
        currentTheme = g.getCurrentTheme()
        setTheme(currentTheme)
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshList()
        val g = UserPreferencesRepositoryImpl.getInstance(this)
        val selectedTheme = g.getCurrentTheme()
        if (currentTheme != selectedTheme) recreate()
    }

    private fun setTheme(currentTheme: Theme) {
        when (currentTheme) {
            Theme.BLUE -> setTheme(R.style.Theme_App_Blue)
            Theme.RED -> setTheme(R.style.Theme_App_Red)
            Theme.MINT -> setTheme(R.style.Theme_App_Mint)
            Theme.GRAY -> setTheme(R.style.Theme_App_Gray)
        }
    }

    fun setThemeAndRecreateActivity(theme: Theme) {
        setTheme(theme)
        recreate()
    }

    fun showThemes() {
        val dialog = SelectThemeDialogFragment.newInstance(currentTheme)
        dialog.onOk = {
            UserPreferencesRepositoryImpl.getInstance(this).current_theme = dialog.selectedNow.rawValue
            setTheme(dialog.selectedNow)
            recreate()
            dialog.dismiss()
        }
        supportFragmentManager.let { dialog.show(it, "editWord") }
    }

    fun isOpenNavigationDrawer(): Boolean {
        return drawerLayout.isDrawerOpen(GravityCompat.START)
    }

    private fun refreshWidgetList() {
        val widgetRepository: WidgetConfigRepository =
            UserPreferencesRepositoryImpl.getInstance(this)
        val ids = widgetRepository.getBigWidget()
        val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(updateIntent)
    }

    fun refreshWidgetDelay() {
        Handler().postDelayed(
            {
                refreshWidgetList()
                refreshWidgetSmall()
            },
            200
                             )
    }

    private fun refreshWidgetSmall() {
        val widgetRepository: WidgetConfigRepository =
            UserPreferencesRepositoryImpl.getInstance(this)
        val ids = widgetRepository.getSmallWidget()

        val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(updateIntent)
    }
}
