package com.gmail.uia059466.liska.warehouse

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.databinding.ListsFragmentBinding
import com.gmail.uia059466.liska.lists.sortorder.SortDialog
import com.gmail.uia059466.liska.main.AppBarUiState.*
import com.gmail.uia059466.liska.main.MainActivity
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.utils.InjectorUtils
import com.google.android.material.snackbar.Snackbar

class WarehousesFragment : Fragment(), WarehouseAdapter.Listener{

    private lateinit var viewModel: WarehouseViewModel

    private var _binding: ListsFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter = WarehouseAdapter(this )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListsFragmentBinding.inflate(inflater, container, false)

        binding.addFab.setOnClickListener { (activity as MainActivityImpl).openNewList()}

        setupAppBar()
        setupAdapter()
        setupViewModel()
        setupObservers()
        setupOnBackPressed()
        setHasOptionsMenu(true)
        return binding.root
    }
    private fun setupAdapter() {
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = adapter
    }

    private fun setupAppBar() {
        (activity as MainActivity).renderAppbar(IconNavigationWithTitle(getString(R.string.warehouses_title)))
    }

    private fun setupObservers() {
        viewModel.navigateToEditWarehouse.observe(viewLifecycleOwner, { id->
            if (id != null) {
//                TODO
            }
        })

        viewModel.lists.observe(viewLifecycleOwner, { list ->
            if (list != null) {
                adapter.setData(list)
            }
        })

        viewModel.snackbarText.observe(viewLifecycleOwner, { idText ->
            if (idText != null) {
                Snackbar.make(binding.listContent,idText, Snackbar.LENGTH_LONG).show()
             }
        })

        viewModel.navigateToManualSort.observe(viewLifecycleOwner, { navigateToSort->
            if (navigateToSort == true) {
//                TODO
            }
        })
    }

    private fun setupViewModel() {
        val viewModelFactory = InjectorUtils.provideViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory)[WarehouseViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.lists_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().finish()
                return true
            }
            R.id.menu_sort -> {
                displayDialogSort()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayDialogSort() {
        val dialog = SortDialog.newInstance(viewModel._sortOrder,R.string.title_sort_list)
        dialog.onOk = {
            val sort = dialog.selected
            viewModel.takeAction(WarehouseAction.Sort(sort!!))
        }
        dialog.show(requireActivity().supportFragmentManager, null)
    }

    override fun onClick(uuidWarehouse: String) {
//        todo
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        requireActivity().finish()
                    }
                }
            )
    }

    override fun onResume() {
        super.onResume()
        viewModel.takeAction(WarehouseAction.UpdateMessages)
        viewModel.refreshList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}