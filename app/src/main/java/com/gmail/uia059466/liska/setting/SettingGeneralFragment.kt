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
import com.gmail.uia059466.liska.databinding.SettingMainFragmentBinding
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

    private var _binding: SettingMainFragmentBinding? = null
    private val binding get() = _binding!!

    private val prefs by lazy { UserPreferencesRepositoryImpl.getInstance(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View {
        _binding = SettingMainFragmentBinding.inflate(inflater, container, false)

        val activity = requireActivity() as MainActivityImpl

        setupAppBar()
        setupObservers()


        binding.content.isMovedCheckedSwitch.isChecked = prefs.isMovedChecked
        binding.content.isMovedCheckedSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.isMovedChecked = isChecked
        }

        binding.content.isOpenLastListSwitch.isChecked = prefs.isOpenLastList
        binding.content.isOpenLastListSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.isOpenLastList = isChecked
        }

        binding.content.favoriteUnitsRv.setOnClickListener { displaySelectFavoriteUnits() }
        binding.content.favoriteUnitsDescription.text = FavoriteUnitsFormatter().createDescription(prefs.readFavUnits())

        binding.content.feedbackLl.setOnClickListener {
            (requireActivity() as MainActivity).navigateTo(R.id.action_settingGeneralFragment_to_feedbackFragment)
        }

        binding.content.shareLl.setOnClickListener { showShareApp() }

        binding.content.aboutRv.setOnClickListener {
            (requireActivity() as MainActivity).navigateTo(R.id.action_settingGeneralFragment_to_aboutFragment)
        }

        val currentOption=prefs.current_option

        binding.content.catalogOptionDescription.text = getTitleForOption(currentOption)

        binding.content.catalogOptionRv.setOnClickListener {

            val dialog = SelectCatalogDialogFragment.newInstance(currentOption)
            dialog.onOk = {
                prefs.updateCurrentOption(dialog.selectedNow)
                binding.content.catalogOptionDescription.text = getTitleForOption(dialog.selectedNow)

                dialog.dismiss()
            }
            requireActivity().supportFragmentManager.let { dialog.show(it, "selectCatalog") }
        }

        binding.content.themesRl.setOnClickListener { showThemes() }

        binding.content.currentThemeTv.text = getTitleForTheme(activity.currentTheme)

        val currentNightMode = prefs.getCurrentNightMode()

        binding.content.nightModeRl.setOnClickListener {
            val dialog = SelectNightModeDialogFragment.newInstance(currentNightMode)
            dialog.onOk = {
                switchToMode(dialog.selectedNow)
                prefs.nightMode = dialog.selectedNow.rawValue
                dialog.dismiss()
            }
            requireActivity().supportFragmentManager.let { dialog.show(it, "night_mode_dialog") }
       }

        binding.content.nightModeTv.text = getTitle(currentNightMode)

        setupObservers()
        setupOnBackPressed()
        setHasOptionsMenu(true)

        return binding.root
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



              binding.content.sortListRv.setOnClickListener {
              val dialog = SelectSortAlertDialogFragment.newInstance(sortOrder, R.string.title_sort_list)
              dialog.onOk = {
                val sort = dialog.selected
                viewModel.takeAction(SettingGeneralAction.SortList(sort))
              }
              requireActivity().supportFragmentManager.let { dialog.show(it, "sort") }
            }

              binding.content.sortListDescriptionTv.text = getTitleForSortOption(sortOrder)

          }
        })

        viewModel.sortOrderCatalog.observe(viewLifecycleOwner, Observer {
            it?.let { sortOrder ->

                binding.content.sortCatalogRv.setOnClickListener {
                    val dialog = SelectSortAlertDialogFragment.newInstance(sortOrder, R.string.title_sort_catalog)
                    dialog.onOk = {
                        val sort = dialog.selected
                        viewModel.takeAction(SettingGeneralAction.SortCatalog(sort))
                    }
                    requireActivity().supportFragmentManager.let { dialog.show(it, "sort") }
                }
                binding.content.sortCatalogDescriptionTv.text = getTitleForSortOption(sortOrder)
            }
        })

        viewModel.favUnits.observe(viewLifecycleOwner, Observer {
          it?.let { units ->
            val description = FavoriteUnitsFormatter().createDescription(units)
              binding.content.favoriteUnitsDescription.text = description
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}