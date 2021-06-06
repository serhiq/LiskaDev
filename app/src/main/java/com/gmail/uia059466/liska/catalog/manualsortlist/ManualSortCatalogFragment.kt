package com.gmail.uia059466.liska.catalog.manualsortlist

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.catalog.asFolders
import com.gmail.uia059466.liska.catalog.toDatabaseForOrder
import com.gmail.uia059466.liska.main.AppBarUiState
import com.gmail.uia059466.liska.main.MainActivity
import com.gmail.uia059466.liska.manualsortlist.ItemDragListenerManual
import com.gmail.uia059466.liska.manualsortlist.ItemTouchHelperCallbackManual
import com.gmail.uia059466.liska.utils.InjectorUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ManualSortCatalogFragment :Fragment(), ItemDragListenerManual {

    private lateinit var viewModel: ManualSortCatalogViewModel
    private lateinit var listRv: RecyclerView
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var itemTouchHelper: ItemTouchHelper

    lateinit var  adapter : ManualSortCatalogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(
            R.layout.lists_fragment,
            container,
            false
        )

        val fab = view.findViewById<FloatingActionButton>(R.id.add_fab)
        fab.visibility=View.GONE


        listRv = view.findViewById<RecyclerView>(R.id.list)
        coordinatorLayout = view.findViewById<CoordinatorLayout>(R.id.list_content)

        setupContainerFragmentUi()
        setupViewModel()
        setupObservers()
        setupOnBackPressed()
        setHasOptionsMenu(true)
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                navigateToBack()
                return true
            }

        }
        return true
    }
    private fun setupContainerFragmentUi() {
        val mainActivity = activity as MainActivity
        val title = requireContext().getString(R.string.title_sort_catalog)
        val toolbar =
            AppBarUiState.ArrayWithTitle(title)
        mainActivity.renderAppbar(toolbar)
    }

    private fun setupObservers() {
        viewModel.lists.observe(viewLifecycleOwner, Observer {
            it?.let {

                adapter=ManualSortCatalogAdapter(it.asFolders().toMutableList(),this)
                listRv.layoutManager = LinearLayoutManager(activity)
                listRv.adapter = adapter
                itemTouchHelper = ItemTouchHelper(
                    ItemTouchHelperCallbackManual(
                        adapter
                    )
                )
                itemTouchHelper.attachToRecyclerView(listRv)
            }
        })

        viewModel.runBack.observe(viewLifecycleOwner, Observer {
            it?.let {isRunBack->
                if (isRunBack){
                    findNavController().navigateUp()
                }
            }
        })
    }

    private fun setupViewModel() {
        val application = requireNotNull(this.activity).application
        val viewModelFactory = InjectorUtils.provideViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[ManualSortCatalogViewModel::class.java]
    }


    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        navigateToBack()
                    }
                }
            )
    }

    private fun navigateToBack() {
        val sortedList=adapter.getList()
        viewModel.runBackAndSaveSorted(sortedList.toDatabaseForOrder())
    }

    override fun onItemDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }
}