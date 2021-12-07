package com.gmail.uia059466.liska.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ShareCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gmail.uia059466.liska.LiskaApplication
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.databinding.SettingMainFragmentBinding
import com.gmail.uia059466.liska.domain.UserPreferencesRepositoryImpl
import com.gmail.uia059466.liska.lists.sortorder.SortDialog
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import com.gmail.uia059466.liska.main.AppBarUiState
import com.gmail.uia059466.liska.main.MainActivity
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.selectunit.SelectAdapter
import com.gmail.uia059466.liska.setting.selectcatalog.CatalogDisplayOptionDialog
import com.gmail.uia059466.liska.setting.themes.SelectNightModeDialogFragment

class SettingMainFragment : Fragment() {

    private val prefs by lazy { UserPreferencesRepositoryImpl.getInstance(requireContext()) }

    private var _binding: SettingMainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View {
        _binding = SettingMainFragmentBinding.inflate(inflater, container, false)

        setupAppBar()
        configureViews()
        setupOnBackPressed()
        setHasOptionsMenu(true)
        return binding.root
    }
    private fun setupAppBar() {
        val title = getString(R.string.setting_app_bar_label)
        (activity as MainActivityImpl).renderAppbar(AppBarUiState.ArrayWithTitle(title))
    }

    private fun configureViews() {
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

        binding.content.catalogOptionRv.setOnClickListener { showDialogCatalogOption() }
        binding.content.catalogOptionDescription.text = getString(prefs.catalogDisplayOption.title)

        binding.content.themesRl.setOnClickListener {(activity as MainActivityImpl).showThemes() }
        binding.content.currentThemeTv.text = getString((activity as MainActivityImpl).currentTheme.title)

        binding.content.nightModeRl.setOnClickListener { showDialogNightMode() }
        binding.content.nightModeTv.text = getString(prefs.nightMode.title)

        binding.content.sortListRv.setOnClickListener { showSortListDialog() }
        binding.content.sortListDescriptionTv.text = getString(prefs.sortOrderList.title)

        binding.content.sortCatalogRv.setOnClickListener { showSortCatalogDialog() }
        binding.content.sortCatalogDescriptionTv.text = getString(prefs.sortOrderCatalog.title)

        val description = FavoriteUnitsFormatter().createDescription(prefs.readFavUnits())
        binding.content.favoriteUnitsDescription.text = description
    }

    private fun showSortCatalogDialog() {
        val dialog = SortDialog.newInstance(
            prefs.sortOrderCatalog,
            R.string.title_sort_catalog
        )

        dialog.onOk = {
            prefs.saveSortOrderCatalog(dialog.selected!!)
            if (dialog.selected == SortOrder.MANUAL_SORT) navigateToManualSortCatalog()
            binding.content.sortCatalogDescriptionTv.text = getString(prefs.sortOrderCatalog.title)
        }
        dialog.show(requireActivity().supportFragmentManager, null)
    }

    private fun showSortListDialog() {
        val dialog = SortDialog.newInstance(
            prefs.sortOrderCatalog,
            R.string.title_sort_list
        )

        dialog.onOk = {
            prefs.saveSortOrderList(dialog.selected!!)
            if (dialog.selected == SortOrder.MANUAL_SORT) navigateToManualSortList()
            binding.content.sortListDescriptionTv.text = getString(prefs.sortOrderList.title)
        }
        dialog.show(requireActivity().supportFragmentManager, null)
    }

    private fun showDialogNightMode() {
        val dialog = SelectNightModeDialogFragment.newInstance(prefs.nightMode)
        dialog.onOk = {
            dialog.selected?.let {
                (requireActivity().application as LiskaApplication).switchToMode(it)
                prefs.nightMode = it
            }
            dialog.dismiss()
        }
        dialog.show(requireActivity().supportFragmentManager, null)
    }

    private fun showDialogCatalogOption() {
        val dialog = CatalogDisplayOptionDialog.newInstance(prefs.catalogDisplayOption)
        dialog.onOk = {
            dialog.selected?.let { prefs.catalogDisplayOption = it }
            binding.content.catalogOptionDescription.text = getString(prefs.catalogDisplayOption.title)
            dialog.dismiss()
        }
        dialog.show(requireActivity().supportFragmentManager, null)
    }

    private fun displaySelectFavoriteUnits() {
        val action=R.id.action_settingGeneralFragment_to_selectUnitsFragment
        val bundle = bundleOf("mode" to SelectAdapter.MODE_FAVORITES)
        findNavController().navigate(action, bundle)
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
              override fun handleOnBackPressed() {
                  findNavController().navigateUp()
              }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
          android.R.id.home -> {
              if (!(activity as MainActivityImpl).isOpenNavigationDrawer()){
                  findNavController().navigateUp()
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

    private fun showShareApp() {
        ShareCompat.IntentBuilder(requireContext())
            .setType("text/plain")
            .setChooserTitle(getString(R.string.app_name))
            .setText(getString(R.string.share_us_message) + activity?.packageName)
            .startChooser()
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