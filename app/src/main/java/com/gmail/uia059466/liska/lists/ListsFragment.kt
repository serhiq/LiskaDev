package com.gmail.uia059466.liska.lists

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.VisibleForTesting
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.databinding.ListsFragmentBinding
import com.gmail.uia059466.liska.databinding.SettingMainFragmentBinding
import com.gmail.uia059466.liska.domain.UserPreferencesRepositoryImpl
import com.gmail.uia059466.liska.lists.sortorder.SortDialog
import com.gmail.uia059466.liska.main.AppBarUiState
import com.gmail.uia059466.liska.main.MainActivity
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.setting.rateus.RateUsDialog
import com.gmail.uia059466.liska.utils.InjectorUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ListsFragment :Fragment(), ListsAdapter.ListListener{

    @VisibleForTesting
    private lateinit var viewModel: ListsViewModel

    lateinit var  prefs:UserPreferencesRepositoryImpl
    private var isListMode=true
    private var isDisplayRateUs=false

    private var _binding: ListsFragmentBinding? = null
    private val binding get() = _binding!!


    private val adapter = ListsAdapter(this )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListsFragmentBinding.inflate(inflater, container, false)

        prefs=UserPreferencesRepositoryImpl.getInstance(requireActivity())
        isListMode=prefs.isListMode

        isDisplayRateUs=prefs.isShowRateAppDialog

        binding.addFab.setOnClickListener { navigateToNewList() }

        (activity as MainActivityImpl).displayListInNavigationDrawer()

        setupAdapter()
        setupContainerFragmentUi()
        setupViewModel()
        setupObservers()
        setupOnBackPressed()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun navigateToNewList() {
        (activity as MainActivityImpl).openNewList()
    }

    private fun setupAdapter() {
        val spanCount=if (isListMode) 1 else 2
        layoutManager = StaggeredGridLayoutManager(spanCount, GridLayoutManager.VERTICAL)

        binding.list.layoutManager = layoutManager
        binding.list.adapter = adapter
    }

    private fun setupContainerFragmentUi() {
        val mainActivity = activity as MainActivity
        val toolbar =
            AppBarUiState.IconNavigationWithTitle("")
        mainActivity.renderAppbar(toolbar)
    }

    private fun setupObservers() {

        viewModel.navigateToEditList.observe(viewLifecycleOwner, Observer {
            it?.let {id->
                val action = R.id.action_global_display_list
                val bundle = bundleOf("isEditMode" to true, "listId" to id)
                findNavController().navigate(action,bundle)
            }
        })

        viewModel.lists.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.setData(it.toMutableList(),isListMode)
                (activity as MainActivityImpl).updateNavigationDrawer()
            }
        })

        viewModel.snackbarText.observe(viewLifecycleOwner, Observer {
            it?.let {text->
                val snackBar = Snackbar.make(binding.listContent,it, Snackbar.LENGTH_LONG)
                snackBar.show()
            }
        })
        viewModel.navigateToManualSort.observe(viewLifecycleOwner, Observer {
            it?.let {navigateToSort->
                if (navigateToSort){
                    val action=R.id.action_listsFragment_to_manualSortFragment
                    (activity as MainActivity).navigateTo(action)
                }
            }
        })
    }

    private fun setupViewModel() {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = InjectorUtils.provideViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[ListsViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.lists_fragment, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val menuRateAs = menu.findItem(R.id.menu_rate_us)
        menuRateAs.isVisible = isDisplayRateUs


        val menuSort = menu.findItem(R.id.menu_sort)
        menuSort.isVisible = true

        val menuTile = menu.findItem(R.id.menu_tile)
        menuTile.isVisible = isListMode

        val menuList = menu.findItem(R.id.menu_list)
        menuList.isVisible = !isListMode

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                runOnBack()
                return true
            }
            R.id.menu_sort -> {
                displayDialogSort()
                return true
            }
            R.id.menu_tile -> {
                showGridView()
                return true
            }
            R.id.menu_list -> {
                showListView()
                return true
            }
            R.id.menu_rate_us -> {
                val dialog = RateUsDialog()
                dialog.onOk = {
                    UserPreferencesRepositoryImpl.getInstance(requireContext()).isShowRateAppDialog=false
                    UserPreferencesRepositoryImpl.getInstance(requireContext()).isShowRateDissabled=true
                    isDisplayRateUs=false
                    requireActivity().invalidateOptionsMenu()

                }

                val fragmentManager = activity?.supportFragmentManager
                fragmentManager?.let { dialog.show(it, "rateUs") }

                return true
            }


        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayDialogSort() {
        val dialog = SortDialog.newInstance(viewModel._sortOrder,R.string.title_sort_list)
        dialog.onOk = {
            val sort = dialog.selected
            viewModel.takeAction(ListsAction.SortList(sort!!))
        }
        dialog.show(requireActivity().supportFragmentManager, null)
    }

    override fun onListClicked(id:Long) {
        (activity as MainActivityImpl).displayHightList(id)
        (activity as MainActivityImpl).toDisplayList(id)
    }

    override fun onLongClicked(id: Long) {
        (activity as MainActivityImpl).displayHightList(id)

        val action = R.id.action_global_display_list
        val bundle = bundleOf("isEditMode" to true, "listId" to id)
        findNavController().navigate(action,bundle)
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        runOnBack()
                    }
                }
            )
    }

    private fun runOnBack() {
        requireActivity().finish()
    }

    override fun onResume() {
        super.onResume()
        viewModel.takeAction(ListsAction.UpdateMessages)
        viewModel.refreshList()
    }

    private lateinit var layoutManager: StaggeredGridLayoutManager


    private fun showListView() {
        adapter.enableListMode()

        prefs.isListMode=true
        isListMode=true

        layoutManager.spanCount = 1
        requireActivity().invalidateOptionsMenu()

    }

    private fun showGridView() {
        adapter.enableGridMode()
        prefs.isListMode=false
        isListMode=false
        layoutManager.spanCount = 2
        requireActivity().invalidateOptionsMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}