package com.gmail.uia059466.liska.catalog

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.listdetail.EditItemDialog
import com.gmail.uia059466.liska.listdetail.ItemDragListener
import com.gmail.uia059466.liska.listdetail.ItemTouchHelperCallback
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import com.gmail.uia059466.liska.main.AppBarUiState
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.utils.InjectorUtils
import com.gmail.uia059466.liska.utils.hideKeyboard
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class CatalogViewFragment : Fragment(), CatalogViewAdapter.ListListener, ItemDragListener {

    private lateinit var viewModel: CatalogViewRootViewModel
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var listRv: RecyclerView

    private lateinit var caption: TextView
    private lateinit var divider: View
    private lateinit var saveButton: ImageButton
    private lateinit var addCatalogFab: FloatingActionButton

    private val adapter: CatalogViewAdapter = CatalogViewAdapter(this, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View? {

        val view = inflater.inflate(
            R.layout.catalog_fragment,
            container,
            false
                                   )

        listRv = view.findViewById(R.id.list)

        divider = view.findViewById(R.id.divider)
        saveButton = view.findViewById(R.id.save_word_button)
        caption = view.findViewById(R.id.text)
        addCatalogFab = view.findViewById(R.id.add_fab)
        enterEditText = view.findViewById<EditText>(R.id.enter_word_edit_text)

        val activity = requireActivity() as MainActivityImpl
        activity.supportActionBar?.setHomeAsUpIndicator(null)
        activity.hamburgerIconDisplay()

        listRv.layoutManager = LinearLayoutManager(activity)
        listRv.adapter = adapter

        listRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) {
                    hideKeyboard()
                    listRv.requestFocus()
                }
            }
        })


        itemTouchHelper = ItemTouchHelper(
            ItemTouchHelperCallback(
                adapter
                                   )
                                         )
        itemTouchHelper.attachToRecyclerView(listRv)


        setupViewModel()
        setupObservers()
        setupOnBackPressed()
        setHasOptionsMenu(true)
        requireActivity().invalidateOptionsMenu()

        return view
    }

    //  menu
    override fun onPrepareOptionsMenu(menu: Menu) {
        when (viewModel.displayListAdapter.value?.mode) {
            AdapterViewCatalogMode.ItemEdit -> configureMenuForEdit(menu)
            AdapterViewCatalogMode.RootFolder -> configureMenuForFoldersView(menu)
            AdapterViewCatalogMode.ItemsView -> configureMenuForSelect(menu)
        }
    }

    private fun configureMenuForEdit(menu: Menu) {
        val deleteList = menu.findItem(R.id.menu_delete_list)
        deleteList.isVisible = true

        val editMode = menu.findItem(R.id.menu_add_mode)
        editMode.isVisible = false
    }

    private fun configureMenuForFoldersView(menu: Menu) {

        val editMode = menu.findItem(R.id.menu_add_mode)
        editMode.isVisible = false

        //  and other is hide

        val deleteList = menu.findItem(R.id.menu_delete_list)
        deleteList.isVisible = false
    }

    private fun configureMenuForSelect(menu: Menu) {

        val editMode = menu.findItem(R.id.menu_add_mode)
        editMode.isVisible = true
        //  and other is hide

        val deleteList = menu.findItem(R.id.menu_delete_list)
        deleteList.isVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.catalog_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupObservers() {
        viewModel.runBack.observe(viewLifecycleOwner, Observer {
            it?.let { isRunBack ->
                if (isRunBack) {
                    findNavController().navigateUp()
                }
            }
        })

        viewModel.displayListAdapter.observe(viewLifecycleOwner, Observer {
            it?.let { state ->
                renderUi(state)
            }
        }
                                            )
    }

    override fun onResume() {
        super.onResume()
        val isRootFolder =
            viewModel.displayListAdapter.value?.mode == AdapterViewCatalogMode.RootFolder
        val isManualSort = viewModel._sortOrder == SortOrder.MANUAL_SORT

        if (isManualSort && isRootFolder) {
            viewModel.applyManualsortAndUpdate()
        }

        if (viewModel.displayListAdapter.value?.mode == AdapterViewCatalogMode.ItemsView)
            viewModel.refreshList()
    }

    private fun renderUi(state: CatalogStateAdapter) {
        when (state.mode) {
            AdapterViewCatalogMode.ItemEdit -> enableItemEditMode()
            AdapterViewCatalogMode.RootFolder -> renderRootFolderView(state.displayList)
            AdapterViewCatalogMode.ItemsView -> renderItemsView(state.displayList, state.currentId)
        }
    }

    private fun renderRootFolderView(displayList: MutableList<DisplayCatalog>) {
        hideKeyboard()
        addCatalogFab.visibility = View.VISIBLE
        val title = getString(R.string.catalog_app_bar_label)
        renderAppbar(AppBarUiState.IconNavigationWithTitle(title))
        divider.visibility = View.GONE
        enterEditText.visibility = View.GONE
        saveButton.visibility = View.GONE

        caption.visibility = View.GONE
        adapter.updateState(AdapterViewCatalogMode.RootFolder, displayList)
        adapter.currentCatalogId = 0L

        addCatalogFab.setOnClickListener {
            val dialog = EditItemDialog.newInstance(text = "", title = "Добавить каталог")
            dialog.onOk = {
                val text = dialog.editText.text.toString()
                if (text.isNotBlank()) {
                    viewModel.takeAction(CatalogViewAction.CreateCatalog(text))
                    withDelay(1000) {
                        try {
                            val index =
                                adapter.displayList.indexOfFirst { it is DisplayCatalog.FolderDisplay && it.title == text }
                           if (index>0){
                               listRv.smoothScrollToPosition(index)
                           }
                        }catch (e:Exception){
                        }
                    }
                }
            }
            dialog.show(requireActivity().supportFragmentManager, null)
        }
        requireActivity().invalidateOptionsMenu()
    }

    fun withDelay(delay: Long, block: () -> Unit) {
        Handler().postDelayed(Runnable(block), delay)
    }

    private fun renderItemsView(displayList: MutableList<DisplayCatalog>, currentId: Long) {
        (activity as MainActivityImpl).hideCustomTitle()
        addCatalogFab.visibility = View.GONE

        renderAppbar(AppBarUiState.ArrayWithTitle(viewModel.title))
        hideKeyboard()

        divider.visibility = View.GONE
        enterEditText.visibility = View.GONE
        saveButton.visibility = View.GONE

        caption.visibility = View.GONE
        adapter.updateState(AdapterViewCatalogMode.ItemsView, displayList)
        adapter.currentCatalogId = currentId

        adapter.notifyDataSetChanged()
        requireActivity().invalidateOptionsMenu()
    }

    private fun enableItemEditMode() {

        addCatalogFab.visibility = View.GONE
        renderAppbar(AppBarUiState.ArrayWithTitle(viewModel.title))

        (activity as MainActivityImpl).setupCustomTitle("${viewModel.title}*")
        (activity as MainActivityImpl).setupClickListener {

            val dialog = EditItemDialog.newInstance(
                text = viewModel.title,
                title = getString(R.string.list_name_dialog)
                                                   )
            dialog.onOk = {
                val text = dialog.editText.text.toString()
                if (text.isNotBlank()) {
                    (activity as MainActivityImpl).setupCustomTitle("${text}*")
                    viewModel.takeAction(CatalogViewAction.ChangeTitleCatalog(text))
                }
            }
            dialog.show(requireActivity().supportFragmentManager, null)
        }



        divider.visibility = View.VISIBLE
        enterEditText.visibility = View.VISIBLE
        saveButton.visibility = View.VISIBLE

        caption.visibility = View.VISIBLE
        adapter.mode = AdapterViewCatalogMode.ItemEdit

        setupEditText()

        adapter.notifyDataSetChanged()

        requireActivity().invalidateOptionsMenu()
    }

    private lateinit var enterEditText: EditText

    private fun setupEditText() {
        enterEditText.setOnEditorActionListener { _, actionId: Int, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                val text = enterEditText.text.toString()
                if (text.isNotBlank()) {
                    addItem(text)
                }
                return@setOnEditorActionListener true
            }
            false
        }
        val btnSave = view?.findViewById<ImageButton>(R.id.save_word_button)
        btnSave?.setOnClickListener {

            val text = enterEditText.text.toString()
            if (text.isNotBlank()) {
                addItem(text)
            }
        }
    }

    private fun addItem(text: String) {
        adapter.addUnit(text.trim())
        listRv.scrollToPosition(adapter.displayList.lastIndex)
        enterEditText.text?.clear()
    }

    private fun renderAppbar(state: AppBarUiState) {
        val mainActivity = activity as MainActivityImpl
        mainActivity.renderAppbar(state)
    }

    private fun setupViewModel() {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = InjectorUtils.provideViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[CatalogViewRootViewModel::class.java]
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
        val isItemEditMode =
            viewModel.displayListAdapter.value?.mode == AdapterViewCatalogMode.ItemEdit

        val isNeedRefreshItems = isItemEditMode && adapter.isEdited

        when {
            isNeedRefreshItems -> {
                val items = adapter.requestItems()
                viewModel.takeAction(CatalogViewAction.RunBack(items = items))
            }
            else               -> {
                viewModel.takeAction(CatalogViewAction.RunBack())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (!(activity as MainActivityImpl).isOpenNavigationDrawer()) {
                    hideKeyboard()
                    navigateToOnBack()
                }
                return true
            }

            R.id.menu_add_mode -> {
                viewModel.takeAction(CatalogViewAction.ChangeModeToEdit)
                return true
            }

            R.id.menu_delete_list -> {
                (activity as MainActivityImpl).hideCustomTitle()
                viewModel.takeAction(CatalogViewAction.DeleteCatalog(adapter.currentCatalogId))

                return true
            }
        }
        return true
    }

    override fun deleteFolder() {
        viewModel.takeAction(CatalogViewAction.ChangeModeToFolderRoot)
        displaySnackBar(R.string.delete)
    }

    override fun onCatalogClicked(catalogId: Long, title: String) {
        viewModel.takeAction(CatalogViewAction.ChangeModeToViewFolder(catalogId, title))
    }

    override fun onItemClickedEdit(currentCatalogId: Long, adapterPosition: Int) {
        val action = R.id.action_catalogFragment_to_addEditCatalogFragment

        val bundle = bundleOf("idCatalog" to currentCatalogId, "indexSelected" to adapterPosition)

        findNavController().navigate(action, bundle)
    }

    override fun onItemDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun hideKeyboardFragment() {
        hideKeyboard()
    }

    private fun displaySnackBar(id: Int) {
        val snackBar =
            Snackbar.make(
                view?.findViewById(android.R.id.content)!!,
                id,
                Snackbar.LENGTH_LONG
                         )
        snackBar.show()
    }

}