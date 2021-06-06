package com.gmail.uia059466.liska.selectunit

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.gmail.uia059466.liska.main.AppBarUiState
import com.gmail.uia059466.liska.main.MainActivity
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.utils.InjectorUtils
import com.gmail.uia059466.liska.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar

class SelectUnitsFragment : Fragment(), SelectAdapter.SelectListener, ItemDragListener {

    private lateinit var viewModel: SelectUnitsFFViewModel
    private lateinit var listRv: RecyclerView
    private lateinit var itemTouchHelper: ItemTouchHelper

    private lateinit var caption: TextView
    private lateinit var divider: View
    private lateinit var editText: EditText
    private lateinit var saveButton: ImageButton
    private lateinit var content: ConstraintLayout

    private val adapter = SelectAdapter(this, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(
            R.layout.addedit_list_fragment,
            container,
            false
        )

        listRv = view.findViewById<RecyclerView>(R.id.list)
        content = view.findViewById(R.id.list_detailed_content)

        divider = view.findViewById(R.id.divider)
        editText = view.findViewById(R.id.enter_word_edit_text)
        saveButton = view.findViewById(R.id.save_word_button)
        caption = view.findViewById(R.id.text)

        val activity = requireActivity() as MainActivityImpl
        activity.supportActionBar?.show()
        activity.supportActionBar?.setHomeAsUpIndicator(null)

        setupViewModel()
        setupObservers()
        setupOnBackPressed()
        setupAdapter()
        setHasOptionsMenu(true)
        return view
    }

    private fun setupAdapter() {
        listRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) {
                    hideKeyboard()
                    listRv.requestFocus()

                }
            }
        })
        listRv.layoutManager = LinearLayoutManager(activity)
        listRv.adapter = adapter
        adapter.setData(viewModel.stateAdapter)

        itemTouchHelper = ItemTouchHelper(
            ItemTouchHelperCallback(
                adapter
            )
        )
        itemTouchHelper.attachToRecyclerView(listRv)
    }

    private fun setupObservers() {
        viewModel.runBack.observe(viewLifecycleOwner, Observer {
            it?.let { isRunBack ->
                if (isRunBack) {
                    findNavController().navigateUp()
                }
            }
        })

        viewModel.adapterMode.observe(viewLifecycleOwner, Observer {
            it?.let {
                switchAdapter(it)
            }
        })
    }

    private fun switchAdapter(mode: Int) {
        when (mode) {
            SelectAdapter.MODE_EDIT -> setupEditMode()
            SelectAdapter.MODE_FAVORITES -> setupFavoritesMode()
            SelectAdapter.MODE_SELECT -> setupSelectMode()
        }
    }

    private fun displaySnackBar(id: Int) {
        val snackBar =
            Snackbar.make(
                content,
                id,
                Snackbar.LENGTH_LONG
                         )
        snackBar.show()
    }

    private fun setupSelectMode() {

        val title = getString(R.string.units_select_appbar)
        renderAppbar(AppBarUiState.ArrayWithTitle(title))

        divider.visibility = View.GONE
        editText.visibility = View.GONE
        saveButton.visibility = View.GONE

        caption.visibility = View.GONE

        adapter.displaySelect()
        adapter.notifyDataSetChanged()
        requireActivity().invalidateOptionsMenu();
    }

    private fun setupFavoritesMode() {
        hideKeyboard()

        val title = getString(R.string.units_favs_appbar)
        renderAppbar(AppBarUiState.ArrayWithTitle(title))

        divider.visibility = View.GONE
        editText.visibility = View.GONE
        saveButton.visibility = View.GONE

        caption.visibility = View.VISIBLE
        updateCaption()

        adapter.displayFavorites()
        requireActivity().invalidateOptionsMenu(); }

    private fun setupEditMode() {
        val title = getString(R.string.units_edit_appbar)
        renderAppbar(AppBarUiState.ArrayWithTitle(title))

        divider.visibility = View.VISIBLE
        editText.visibility = View.VISIBLE
        saveButton.visibility = View.VISIBLE
        caption.visibility = View.VISIBLE
        val captionText = getString(R.string.units_edit_caption)

        caption.text = captionText

        setupEditText()
        adapter.displayEdit()
        requireActivity().invalidateOptionsMenu()
    }

    private fun renderAppbar(state: AppBarUiState) {
        val mainActivity = activity as MainActivity
        mainActivity.renderAppbar(state)
    }

    private fun setupViewModel() {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = InjectorUtils.provideViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[SelectUnitsFFViewModel::class.java]

        val mode = arguments?.getInt("mode")

        if (mode != null) {
            viewModel.start(mode)
            adapter.setupMode(mode)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.units, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        when (viewModel.adapterMode.value) {
            SelectAdapter.MODE_EDIT -> {
                configureMenuForEdit(menu)
            }
            SelectAdapter.MODE_FAVORITES -> {
                configureMenuFavorites(menu)
            }
            SelectAdapter.MODE_SELECT -> {
                configureMenuSelect(menu)
            }
        }
    }

    private fun configureMenuSelect(menu: Menu) {
        val editMenu = menu.findItem(R.id.menu_add_mode)
        editMenu.isVisible = true

        val selectFavorites = menu.findItem(R.id.menu_select_favorites)
        selectFavorites.isVisible = true
    }

    private fun configureMenuFavorites(menu: Menu) {
        val editMenu = menu.findItem(R.id.menu_add_mode)
        editMenu.isVisible = true

        val selectFavorites = menu.findItem(R.id.menu_select_favorites)
        selectFavorites.isVisible = false
    }

    private fun configureMenuForEdit(menu: Menu) {
        val editMenu = menu.findItem(R.id.menu_add_mode)
        editMenu.isVisible = false

        val selectFavorites = menu.findItem(R.id.menu_select_favorites)
        selectFavorites.isVisible = false

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                hideKeyboard()
                val state = adapter.requestState()
                viewModel.runBack(state)
                return true
            }
            R.id.menu_add_mode -> {
                viewModel.enableEdit()
                return true
            }
            R.id.menu_select_favorites -> {
                viewModel.enableFavoritesSelect()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        val state = adapter.requestState()
                        viewModel.runBack(state)
                    }
                }
            )
    }

    private fun setupEditText() {
        editText.setOnEditorActionListener { _, actionId: Int, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                val text = editText.text.toString()

                when {
                    text.isBlank()               -> {
                    }
                    adapter.isContainUnits(text) -> {
                      displaySnackBar(R.string.units_equals_unit_snakbar)
                    }
                    else                         -> {
                        addItem(text)
                    }
                }
                return@setOnEditorActionListener true
            }
            false
        }

        val btnSave = view?.findViewById<ImageButton>(R.id.save_word_button)
        btnSave?.setOnClickListener {
            val text = editText.text.toString()
            when {
                text.isBlank()               -> {
                }
                adapter.isContainUnits(text) -> {
                    displaySnackBar(R.string.units_equals_unit_snakbar)
                }
                else                         -> {
                    addItem(text)
                }
            }
        }
    }

    private fun addItem(text: String) {
        adapter.addUnit(text.trim())
        listRv.scrollToPosition(adapter.getLastIndex())
        editText.text?.clear()
    }

    private fun updateCaption() {
        val title=getString(R.string.units_favs_caption,adapter.getSizeFavorites())
        val favsTitle = title
        caption.text = favsTitle
    }

    override fun onLimited() {
        displaySnackBar(R.string.units_no_more_four_snakbar)
    }

    override fun onItemClickedEdit(text: String, position: Int) {
        val title = getString(R.string.units_edit_title_dialog)
        val dialog = EditItemDialog.newInstance(text = text, title = title)
        dialog.onOk = {
            val newText = dialog.editText.text.toString()
            if (adapter.isContainUnits(newText)) {

                displaySnackBar(R.string.units_equals_unit_snakbar)

            } else if (newText.isNotBlank()) {
                adapter.changePosition(oldText = text, newText = newText)
            }
        }
        requireActivity().supportFragmentManager.let { dialog.show(it, "editUnit") }
    }



    override fun hideKeyboardFragment() {
        hideKeyboard()
    }

    override fun onSelectClicked(unit: String) {
        viewModel.saveSelected(unit)
    }

    override fun onItemDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun updateFavs() {
        updateCaption()
    }
}