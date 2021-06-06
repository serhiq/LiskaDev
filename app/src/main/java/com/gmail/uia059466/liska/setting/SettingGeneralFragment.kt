package com.gmail.uia059466.liska.setting

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ShareCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gmail.uia059466.liska.Mode
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.domain.UserPreferencesRepositoryImpl
import com.gmail.uia059466.liska.lists.sortorder.SelectSortAlertDialogFragment
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import com.gmail.uia059466.liska.main.AppBarUiState
import com.gmail.uia059466.liska.main.MainActivity
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.selectunit.SelectAdapter
import com.gmail.uia059466.liska.setting.selectcatalog.SelectCatalogDialogFragment
import com.gmail.uia059466.liska.setting.selectcatalog.SelectCatalogOption
import com.gmail.uia059466.liska.setting.themes.SelectNightModeDialogFragment
import com.gmail.uia059466.liska.setting.themes.Theme
import com.gmail.uia059466.liska.utils.InjectorUtils

class SettingGeneralFragment : Fragment() {

    lateinit var viewModel: SettingGeneralViewModel

    private lateinit var currentSortOrderList: TextView
    private lateinit var rvSortList: RelativeLayout

    private lateinit var rvSortCatalogList: RelativeLayout
    private lateinit var currentSortOrderCatalog: TextView

    private lateinit var tvFavoriteUnitsDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
          R.layout.setting_main_fragment,
          container,
          false
        )

        val activity = requireActivity() as MainActivityImpl

        setupAppBar()
        setupObservers()

        val pref = UserPreferencesRepositoryImpl.getInstance(requireContext())

        val isMoveChecked = view.findViewById<Switch>(R.id.is_moved_checked_switch)
        isMoveChecked.isChecked = pref.isMovedChecked
        isMoveChecked.setOnCheckedChangeListener { _, isChecked ->
            pref.isMovedChecked = isChecked
        }

        val isOpenLastList = view.findViewById<Switch>(R.id.is_open_last_list_switch)
        isOpenLastList.isChecked = pref.isOpenLastList
        isOpenLastList.setOnCheckedChangeListener { _, isChecked ->
            pref.isOpenLastList = isChecked
        }

        currentSortOrderList = view.findViewById<TextView>(R.id.sort_list_description_tv)
        rvSortList = view.findViewById<RelativeLayout>(R.id.sort_list_rv)

        rvSortCatalogList = view.findViewById<RelativeLayout>(R.id.sort_catalog_rv)
        currentSortOrderCatalog = view.findViewById<TextView>(R.id.sort_catalog_description_tv)

        val rvFavoriteUnits = view.findViewById<RelativeLayout>(R.id.favorite_units_rv)
        rvFavoriteUnits.setOnClickListener {
            displaySelectFavoriteUnits()
        }

        tvFavoriteUnitsDescription = view.findViewById(R.id.favorite_units_description)


        val llFeedback = view.findViewById<LinearLayout>(R.id.feedback_ll)
        llFeedback.setOnClickListener {
            val action = R.id.action_settingGeneralFragment_to_feedbackFragment
            activity.navigateTo(action)
        }

        val llShareUs = view.findViewById<LinearLayout>(R.id.share_ll)
        llShareUs.setOnClickListener {
            showShareApp()
        }

        val rvAbout = view.findViewById<RelativeLayout>(R.id.about_rv)
        rvAbout.setOnClickListener {
            val action = R.id.action_settingGeneralFragment_to_aboutFragment
            activity.navigateTo(action)
        }

        val rlCatalogOption = view.findViewById<RelativeLayout>(R.id.catalog_option_rv)
        val currentOption=pref.current_option
        val tvCatalogOption = view.findViewById<TextView>(R.id.catalog_option_description)

        tvCatalogOption.text = getTitleForOption(currentOption)

        rlCatalogOption.setOnClickListener {

            val dialog = SelectCatalogDialogFragment.newInstance(currentOption)
            dialog.onOk = {
                pref.updateCurrentOption(dialog.selectedNow)
                tvCatalogOption.text = getTitleForOption(dialog.selectedNow)

                dialog.dismiss()
            }
            requireActivity().supportFragmentManager.let { dialog.show(it, "selectCatalog") }
        }



        val rlThemes = view.findViewById<RelativeLayout>(R.id.themes_rl)

        rlThemes.setOnClickListener {
            showThemes()
        }

        val tvThemes = view.findViewById<TextView>(R.id.current_theme_tv)
        val currentThemes = activity.currentTheme

        tvThemes.text = getTitleForTheme(currentThemes)


        val rlNightMode = view.findViewById<RelativeLayout>(R.id.night_mode_rl)
        val currentNightMode = pref.getCurrentNightMode()

        rlNightMode.setOnClickListener {
            val dialog = SelectNightModeDialogFragment.newInstance(currentNightMode)
            dialog.onOk = {
                switchToMode(dialog.selectedNow)
                pref.nightMode = dialog.selectedNow.rawValue
                dialog.dismiss()
            }
            requireActivity().supportFragmentManager.let { dialog.show(it, "night_mode_dialog") }
       }

        val tvNightMode = view.findViewById<TextView>(R.id.night_mode_tv)

        tvNightMode.text = getTitle(currentNightMode)

        setupObservers()
        setupOnBackPressed()
        setHasOptionsMenu(true)

        return view
    }

    private fun switchToMode(selectedNow: Mode) {
       val mode= when(selectedNow){
           Mode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
           Mode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
           Mode.SYSTEM -> if (isPreAndroid10()) {
              AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
           } else {
               AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
           }
       }
        AppCompatDelegate.setDefaultNightMode(mode)

    }
    private fun isPreAndroid10() = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q

    private fun getTitle(nightMode: Mode): String {
        return when(nightMode){
            Mode.LIGHT -> getString(R.string.setting_night_mode_disabled)
            Mode.DARK -> getString(R.string.setting_night_mode_enabled)
            Mode.SYSTEM -> getString(R.string.setting_night_mode_system)
        }
    }

    private fun displaySelectFavoriteUnits() {
        val action=R.id.action_settingGeneralFragment_to_selectUnitsFragment
        val bundle = bundleOf("mode" to SelectAdapter.MODE_FAVORITES)
        findNavController().navigate(action, bundle)
    }

    private fun setupAppBar() {
        val mainActivity = activity as MainActivityImpl
        val title = getString(R.string.setting_app_bar_label)
        mainActivity.renderAppbar(AppBarUiState.ArrayWithTitle(title))
    }

    private fun getTitleForOption(theme: SelectCatalogOption) = when (theme) {
        SelectCatalogOption.CHECKBOX -> getString(R.string.dialog_select)
        SelectCatalogOption.SELECT-> getString(R.string.dialog_quantity)
        SelectCatalogOption.TWO_VAR-> getString(R.string.dialog_two)
    }

    private fun getTitleForTheme(theme: Theme) = when (theme) {
      Theme.BLUE -> getString(R.string.theme_light)
      Theme.RED -> getString(R.string.theme_dark)
      Theme.MINT -> getString(R.string.theme_mint)
      Theme.GRAY -> getString(R.string.theme_gray)
    }

    private fun showThemes() {
        (activity as MainActivityImpl).showThemes()
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
              override fun handleOnBackPressed() {
                goBack()
              }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
          android.R.id.home -> {
              if (!(activity as MainActivityImpl).isOpenNavigationDrawer()){
                  goBack()
              }
            return true
          }
        }
        return true
    }

    private fun setupObservers() {
        viewModel.navigateToManualSortList.observe(viewLifecycleOwner, Observer {
          it?.let { navigateToSort ->
            if (navigateToSort) {
              val action = R.id.action_settingGeneralFragment_to_manualSortFragment
              (activity as MainActivity).navigateTo(action)
            }
          }
        })

        viewModel.navigateToManualCatalogSort.observe(viewLifecycleOwner, Observer {
          it?.let { navigateToSort ->
            if (navigateToSort) {
              val action = R.id.action_settingGeneralFragment_to_manualSortCatalogFragment
              (activity as MainActivity).navigateTo(action)
            }
          }
        })




        viewModel.sortOrderList.observe(viewLifecycleOwner, Observer {
          it?.let { sortOrder ->

            rvSortList.setOnClickListener {
              val dialog = SelectSortAlertDialogFragment.newInstance(sortOrder, R.string.title_sort_list)
              dialog.onOk = {
                val sort = dialog.selected
                viewModel.takeAction(SettingGeneralAction.SortList(sort))
              }
              requireActivity().supportFragmentManager.let { dialog.show(it, "sort") }
            }
            currentSortOrderList.text = getTitleForSortOption(sortOrder)

          }
        })

        viewModel.sortOrderCatalog.observe(viewLifecycleOwner, Observer {
            it?.let { sortOrder ->

                rvSortCatalogList.setOnClickListener {
                    val dialog = SelectSortAlertDialogFragment.newInstance(sortOrder, R.string.title_sort_catalog)
                    dialog.onOk = {
                        val sort = dialog.selected
                        viewModel.takeAction(SettingGeneralAction.SortCatalog(sort))
                    }
                    requireActivity().supportFragmentManager.let { dialog.show(it, "sort") }
                }
                currentSortOrderCatalog.text = getTitleForSortOption(sortOrder)

            }
        })

        viewModel.favUnits.observe(viewLifecycleOwner, Observer {
          it?.let { units ->
            val description = FavoriteUnitsFormatter().createDescription(units)
            tvFavoriteUnitsDescription.text = description

          }
        })
    }

    private fun getTitleForSortOption(sortOption: SortOrder) = when (sortOption) {
      SortOrder.LAST_MODIFIED -> getString(R.string.last_modificated)
      SortOrder.A_Z -> getString(R.string.sort_alphabetical)
      SortOrder.NEWEST_FIRST ->getString(R.string.sort_date_added)
      SortOrder.MANUAL_SORT -> getString(R.string.sort_by_human)
    }

    private fun showShareApp() {
        val title=getString(R.string.app_name)
        val message = getString(R.string.share_us_message)

        ShareCompat.IntentBuilder.from(requireActivity())
            .setType("text/plain")
            .setChooserTitle(title)
            .setText(message + activity?.packageName)
            .startChooser()
    }

    private fun setupViewModel() {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = InjectorUtils.provideViewModelFactory(application)
        viewModel = ViewModelProvider(
          this,
          viewModelFactory
        )[SettingGeneralViewModel::class.java]
    }

    private fun goBack() {
        findNavController().navigateUp()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFavsUnits()
    }
}