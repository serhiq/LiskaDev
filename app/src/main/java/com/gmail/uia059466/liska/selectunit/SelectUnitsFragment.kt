package com.gmail.uia059466.liska.selectunit

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.databinding.UnitsFragmentBinding
import com.gmail.uia059466.liska.listdetail.EditItemDialog
import com.gmail.uia059466.liska.listdetail.ItemDragListener
import com.gmail.uia059466.liska.listdetail.ItemTouchHelperCallback
import com.gmail.uia059466.liska.main.AppBarUiState
import com.gmail.uia059466.liska.main.MainActivity
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.utils.InjectorUtils
import com.gmail.uia059466.liska.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar

class SelectUnitsFragment : Fragment(), UnitsAdapter.Listener, ItemDragListener {

    private var _binding: UnitsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SelectUnitsViewModel
    private lateinit var itemTouchHelper: ItemTouchHelper

    private val adapter = UnitsAdapter(this, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UnitsFragmentBinding.inflate(inflater, container, false)

        (requireActivity() as MainActivityImpl).supportActionBar?.apply {
            show()
            setHomeAsUpIndicator(null)
        }

        setupViewModel()
        setupObservers()
        setupOnBackPressed()
        setupAdapter()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setupAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
        adapter.setData(viewModel.stateAdapter)

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) {
                    hideKeyboard()
                    binding.recyclerView.requestFocus()

                }
            }
        })

        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun setupObservers() {
        viewModel.runBack.observe(viewLifecycleOwner, { runBack ->
            if (runBack == true) findNavController().navigateUp()
           })

        viewModel.adapterMode.observe(viewLifecycleOwner, { mode ->
            if (mode != null) switchAdapter(mode)
        })
    }

    private fun switchAdapter(mode: UnitsAdapter.Mode) {
        when (mode) {
            UnitsAdapter.Mode.EDIT -> setupEditMode()
            UnitsAdapter.Mode.FAVORITES ->setupFavoritesMode()
            UnitsAdapter.Mode.SELECT ->  setupSelectMode()
        }
    }

    private fun displaySnackBar(id: Int) {
        val snackBar = Snackbar.make(binding.listDetailedContent,
                id,
                Snackbar.LENGTH_LONG)
        snackBar.show()
    }

    private fun setupSelectMode() {
        val title = getString(R.string.units_select_appbar)
        renderAppbar(AppBarUiState.ArrayWithTitle(title))
        binding.divider.visibility = View.GONE
        binding.editText.visibility = View.GONE
        binding.editText.visibility = View.GONE
        binding.include.text.visibility = View.GONE

        adapter.setupMode(UnitsAdapter.Mode.SELECT)
        requireActivity().invalidateOptionsMenu();
    }

    private fun setupFavoritesMode() {
        hideKeyboard()
        val title = getString(R.string.units_favs_appbar)
        renderAppbar(AppBarUiState.ArrayWithTitle(title))

        binding.divider.visibility = View.GONE
        binding.editText.visibility = View.GONE
        binding.saveImageBtn.visibility = View.GONE

        binding.include.text.visibility = View.VISIBLE
        updateCaption()

        adapter.setupMode(UnitsAdapter.Mode.FAVORITES)
        requireActivity().invalidateOptionsMenu(); }

    private fun setupEditMode() {
        val title = getString(R.string.units_edit_appbar)
        renderAppbar(AppBarUiState.ArrayWithTitle(title))

        binding.divider.visibility = View.VISIBLE
        binding.editText.visibility = View.VISIBLE
        binding.saveImageBtn.visibility = View.VISIBLE
        binding.include.text.visibility = View.VISIBLE
        binding.include.text.text = getString(R.string.units_edit_caption)

        setupEditText()
        adapter.setupMode(UnitsAdapter.Mode.EDIT)
        requireActivity().invalidateOptionsMenu()
    }

    private fun renderAppbar(state: AppBarUiState) {
        val mainActivity = activity as MainActivity
        mainActivity.renderAppbar(state)
    }

    private fun setupViewModel() {
        val viewModelFactory = InjectorUtils.provideViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory)[SelectUnitsViewModel::class.java]

        val mode = arguments?.getString("mode")?.let { UnitsAdapter.Mode.valueOf(it) } ?: throw  Exception("Не задан режим отображения mode")
        viewModel.start(mode)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.units, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        when (viewModel.adapterMode.value) {
            UnitsAdapter.Mode.EDIT ->  configureMenuForEdit(menu)
            UnitsAdapter.Mode.FAVORITES -> configureMenuFavorites(menu)
            UnitsAdapter.Mode.SELECT -> configureMenuSelect(menu)
        }
    }

    private fun configureMenuSelect(menu: Menu) {
        menu.findItem(R.id.menu_add_mode).isVisible = true
        menu.findItem(R.id.menu_select_favorites).isVisible = true
    }

    private fun configureMenuFavorites(menu: Menu) {
        menu.findItem(R.id.menu_add_mode).isVisible = true
        menu.findItem(R.id.menu_select_favorites).isVisible = false
    }

    private fun configureMenuForEdit(menu: Menu) {
        menu.findItem(R.id.menu_add_mode).isVisible = false
        menu.findItem(R.id.menu_select_favorites).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                hideKeyboard()
                viewModel.runBack(adapter.requestState())
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
                        viewModel.runBack(adapter.requestState())
                    }
                }
            )
    }

    private fun setupEditText() {
        binding.editText.setOnEditorActionListener { _, actionId: Int, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                saveText(binding.editText.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }

        binding.saveImageBtn.setOnClickListener {
            saveText(binding.editText.text.toString())
        }
    }

    private fun saveText(text: String) {
        when {
            text.isBlank()               -> {}
            adapter.isContainUnits(text) -> displaySnackBar(R.string.units_equals_unit_snakbar)
            else                         -> addItem(text)
        }
    }

    private fun addItem(text: String) {
        adapter.addUnit(text.trim())
        binding.recyclerView.scrollToPosition(adapter.getLastIndex())
        binding.editText.text?.clear()
    }

    private fun updateCaption() {
        binding.include.text.text = getString(R.string.units_favs_caption,adapter.getSizeFavorites())
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
        dialog.show(requireActivity().supportFragmentManager, null)
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