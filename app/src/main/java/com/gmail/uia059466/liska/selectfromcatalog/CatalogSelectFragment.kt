package com.gmail.uia059466.liska.selectfromcatalog

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.main.AppBarUiState
import com.gmail.uia059466.liska.main.MainActivity
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.selectfromcatalogquantity.CatalogSelect
import com.gmail.uia059466.liska.selectfromcatalogquantity.CatalogSelectAdapterQuantity
import com.gmail.uia059466.liska.selectfromcatalogquantity.CatalogSelectListener
import com.gmail.uia059466.liska.utils.InjectorUtils
import com.gmail.uia059466.liska.utils.hideKeyboard


class CatalogSelectFragment : Fragment(), CatalogSelectListener {

    companion object {
        const val CATALOG_MODE_SELECT = 1
        const val CATALOG_MODE_QUANTITY = 2
        const val CATALOG_MODE = "catalog_mode"
    }

    private lateinit var viewModel: FufuCatalogSelectViewModel
    private lateinit var listRv: RecyclerView
    private lateinit var adapter: CatalogSelect
    private lateinit var caption: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(
            R.layout.catalog_fragment_select,
            container,
            false
        )

        val catalogMode = arguments?.getInt(CATALOG_MODE)

        adapter = when (catalogMode) {
            CATALOG_MODE_SELECT -> CatalogSelectAdapter(this)
            CATALOG_MODE_QUANTITY -> CatalogSelectAdapterQuantity(this)
            else -> throw Exception("")
        }

        listRv = view.findViewById(R.id.list)

        caption = view.findViewById(R.id.text)

        listRv.layoutManager = LinearLayoutManager(activity)
        listRv.adapter =
            adapter as RecyclerView.Adapter<CatalogSelectAdapterQuantity.BaseViewHolder<*>>

        listRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) {
                    hideKeyboard()
                    listRv.requestFocus()

                }
            }
        })
        setupViewModel()
        setupObservers()
        setupOnBackPressed()
        setHasOptionsMenu(true)
        return view
    }

    //  menu
    override fun onPrepareOptionsMenu(menu: Menu) {
        when (viewModel.modeAdapter.value) {
            AdapterCatalogMode.RootFolder -> displayRoot(menu)
            AdapterCatalogMode.ItemsView -> configureMenuForViewInFolder(menu)
        }
    }

    private fun displayRoot(menu: Menu) {
        (activity as MainActivityImpl).displaySearchView {
            if (it.isBlank()) {

            } else {
                adapter.search(it)
            }
        }
        //  and other is hide
        val clearFind = menu.findItem(R.id.menu_clear_search)
        clearFind.isVisible = true
    }

    private fun configureMenuForViewInFolder(menu: Menu) {
        //  and other is hide
        val clearInSearch = menu.findItem(R.id.menu_clear_search)
        clearInSearch.isVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//   search 
        menu.clear()
        inflater.inflate(R.menu.catalog_select_menu, menu)
    }

    private fun setupObservers() {

        viewModel.runBack.observe(viewLifecycleOwner, Observer {
            it?.let { isRunBack ->
                if (isRunBack) {
                    findNavController().navigateUp()
                }
            }
        })

        viewModel.modeAdapter.observe(viewLifecycleOwner, Observer {
            it?.let { mode ->
                renderUi(mode)
            }
        }
        )

        viewModel.dataLoading.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    adapter.fillAdapter(viewModel.rawList)

                }
            }
        })
    }

    private fun renderUi(mode: AdapterCatalogMode) {
        when (mode) {
            AdapterCatalogMode.ItemsView -> renderItemsView()
            AdapterCatalogMode.RootFolder -> renderRoot()
        }
    }

    private fun renderRoot() {
        val title = getString(R.string.catalog_app_bar_label)
        renderAppbar(AppBarUiState.ArrayWithTitle(title))

        caption.visibility = View.GONE

        adapter.setupRootFolder()
        requireActivity().invalidateOptionsMenu()
    }

    private fun renderItemsView() {
        caption.visibility = View.VISIBLE
        caption.text = viewModel.title

        adapter.setupViewCurrentFolder()
        requireActivity().invalidateOptionsMenu()
    }

    private fun renderAppbar(state: AppBarUiState) {
        val mainActivity = activity as MainActivity
        mainActivity.renderAppbar(state)
    }


    private fun setupViewModel() {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = InjectorUtils.provideViewModelFactory(application)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[FufuCatalogSelectViewModel::class.java]
        val listId = arguments?.getLong("listId")
        if (listId != null) viewModel.start(listId)
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        navigateToOnBack()
                    }
                }
            )
    }

    private fun navigateToOnBack() {
        val text = (activity as MainActivityImpl).getTextSearchView()
        if (text.isNotBlank()) {
            (activity as MainActivityImpl).clearSearchView()
            viewModel.resetModelToView()
        } else if (viewModel.modeAdapter.value == AdapterCatalogMode.RootFolder) {
            viewModel.takeAction(
                CatalogSelectAction.RunBackAndSave(
                    adapter.getCatalogCommand(),
                    adapter.getSelected()
                )
            )
        } else {
            viewModel.takeAction(CatalogSelectAction.RunBack)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                hideKeyboard()
                navigateToOnBack()
                return true
            }
            R.id.menu_clear_search -> {
                (activity as MainActivityImpl).clearSearchView()
                hideKeyboard()
                adapter.setupRootFolder()
                return true
            }
        }
        return true
    }

    override fun onCatalogClicked(catalogId: Long, title: String) {
        hideKeyboard()
        viewModel.takeAction(CatalogSelectAction.ChangeModeToItemView(title))
    }

    override fun onCreateItemClicked(title: String) {
        if (adapter.currentId == 0L) {
            displayDialogSendSelected(title)
        } else if (viewModel.rawList.size == 1) {
            adapter.addItem(title, viewModel.rawList[0].id)
        } else {
            adapter.addItem(title)
        }
    }

    private fun displayDialogSendSelected(title: String) {
        val dialog = SelectCatalogDialogFragment.newInstance(viewModel.rawList)
        dialog.onOk = {
            val selectedId = dialog.selectedNow
            adapter.addItem(title, selectedId)

            dialog.dismiss()
        }
        requireActivity().supportFragmentManager.let { dialog.show(it, "editWord") }
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivityImpl).hideSearchView()
    }
}