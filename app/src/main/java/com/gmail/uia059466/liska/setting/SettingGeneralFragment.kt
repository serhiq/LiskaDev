package com.gmail.uia059466.liska.setting

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ShareCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
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
import com.gmail.uia059466.liska.setting.selectcatalog.CatalogDisplayOptionDialog
import com.gmail.uia059466.liska.setting.themes.SelectNightModeDialogFragment
import com.gmail.uia059466.liska.setting.themes.Theme

class SettingGeneralFragment : Fragment() {

    private var _binding: SettingMainFragmentBinding? = null
    private val binding get() = _binding!!

    private val prefs by lazy { UserPreferencesRepositoryImpl.getInstance(requireContext()) }

    override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View {
        _binding = SettingMainFragmentBinding.inflate(inflater, container, false)

        setupAppBar()

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

        binding.content.catalogOptionDescription.text = getString(prefs.catalogDisplayOption.title)
        binding.content.catalogOptionRv.setOnClickListener { showDialogCatalogOption() }

        binding.content.themesRl.setOnClickListener {(activity as MainActivityImpl).showThemes() }
        binding.content.currentThemeTv.text = getTitleForTheme((activity as MainActivityImpl).currentTheme)

        binding.content.nightModeRl.setOnClickListener {
            val dialog = SelectNightModeDialogFragment.newInstance(prefs.getCurrentNightMode())
            dialog.onOk = {
                switchToMode(dialog.selectedNow)
                prefs.nightMode = dialog.selectedNow.rawValue
                dialog.dismiss()
            }
            requireActivity().supportFragmentManager.let { dialog.show(it, "night_mode_dialog") }
       }

        binding.content.nightModeTv.text = getTitle(prefs.getCurrentNightMode())

        binding.content.sortListRv.setOnClickListener {
            val dialog = SelectSortAlertDialogFragment.newInstance(
                prefs.sortOrderCatalog,
                R.string.title_sort_catalog
            )

            dialog.onOk = {
                prefs.saveSortOrderList(dialog.selectedSort)
                if (dialog.selectedSort == SortOrder.MANUAL_SORT) navigateToManualSortList()
                binding.content.sortCatalogDescriptionTv.text = getTitleForSortOption(dialog.selectedSort)
            }
            requireActivity().supportFragmentManager.let { dialog.show(it, "sort_catalog_dialog") }
        }

        binding.content.sortListDescriptionTv.text = getTitleForSortOption(prefs.sortOrder)

        binding.content.sortCatalogRv.setOnClickListener {
            val dialog = SelectSortAlertDialogFragment.newInstance(
                prefs.sortOrderCatalog,
                R.string.title_sort_catalog
            )

            dialog.onOk = {
                prefs.saveSortOrderCatalog(dialog.selectedSort)
                if (dialog.selectedSort == SortOrder.MANUAL_SORT) navigateToManualSortCatalog()
                binding.content.sortCatalogDescriptionTv.text = getTitleForSortOption(dialog.selectedSort)
            }
            requireActivity().supportFragmentManager.let { dialog.show(it, "sort_catalog_dialog") }
        }
        binding.content.sortCatalogDescriptionTv.text = getTitleForSortOption(prefs.sortOrderCatalog)
        val description = FavoriteUnitsFormatter().createDescription(prefs.readFavUnits())
        binding.content.favoriteUnitsDescription.text = description
        setupOnBackPressed()
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun showDialogCatalogOption() {
        val dialog = CatalogDisplayOptionDialog.newInstance(prefs.catalogDisplayOption)
        dialog.onOk = {
            dialog.selected?.let { prefs.catalogDisplayOption = it }
            binding.content.catalogOptionDescription.text = getString(prefs.catalogDisplayOption.title)
            dialog.dismiss()
        }
        requireActivity().supportFragmentManager.let { dialog.show(it, null) }
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
        val title = getString(R.string.setting_app_bar_label)
        (activity as MainActivityImpl).renderAppbar(AppBarUiState.ArrayWithTitle(title))
    }

    private fun getTitleForTheme(theme: Theme) = when (theme) {
      Theme.BLUE -> getString(R.string.theme_light)
      Theme.RED -> getString(R.string.theme_dark)
      Theme.MINT -> getString(R.string.theme_mint)
      Theme.GRAY -> getString(R.string.theme_gray)
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

    private fun navigateToManualSortList() {
        (activity as MainActivity).navigateTo(R.id.action_settingGeneralFragment_to_manualSortFragment)
    }

    private fun navigateToManualSortCatalog() {
        (activity as MainActivity).navigateTo(R.id.action_settingGeneralFragment_to_manualSortCatalogFragment)
    }

    private fun getTitleForSortOption(sortOption: SortOrder) = when (sortOption) {
      SortOrder.LAST_MODIFIED -> getString(R.string.last_modificated)
      SortOrder.A_Z -> getString(R.string.sort_alphabetical)
      SortOrder.NEWEST_FIRST ->getString(R.string.sort_date_added)
      SortOrder.MANUAL_SORT -> getString(R.string.sort_by_human)
    }

    private fun showShareApp() {
        ShareCompat.IntentBuilder(requireContext())
            .setType("text/plain")
            .setChooserTitle(getString(R.string.app_name))
            .setText(getString(R.string.share_us_message) + activity?.packageName)
            .startChooser()
    }

    private fun goBack() {
        findNavController().navigateUp()
    }

    override fun onResume() {
        super.onResume()
        binding.content.favoriteUnitsDescription.text = FavoriteUnitsFormatter().createDescription(prefs.readFavUnits())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}