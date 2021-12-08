package com.gmail.uia059466.liska.addeditcatalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.addeditcatalog.units.UnitPanelState
import com.gmail.uia059466.liska.data.database.CatalogItem
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.selectunit.UnitsAdapter
import com.gmail.uia059466.liska.utils.InjectorUtils
import com.gmail.uia059466.liska.utils.getThemeColor

class AddEditCatalogFragment : Fragment() {
  
  private lateinit var viewModel: AddEditCatalogViewModel
  
  private lateinit var title: EditText
  private lateinit var increaseFl: FrameLayout
  private lateinit var decreaseFl: FrameLayout
  private lateinit var quantityEt: EditText
  private lateinit var unit_1: TextView
  private lateinit var unit_2: TextView
  private lateinit var unit_3: TextView
  private lateinit var unit_4: TextView
  private lateinit var unit_5: TextView
  private lateinit var moreFL: FrameLayout
  private var colorOnSecondary = 0
  private var colorDarkGray = 0
  
  private val EMPTY_PLACE = 0
  
  override fun onCreateView(
          inflater: LayoutInflater, container: ViewGroup?,
          savedInstanceState: Bundle?
                           ): View? {
    
    val view = inflater.inflate(
            R.layout.add_edit_catalog_list_fragment,
            container,
            false
                               )
    title = view.findViewById(R.id.title_et)
    increaseFl = view.findViewById(R.id.increase_fl)
    decreaseFl = view.findViewById(R.id.decrease_fl)
    quantityEt = view.findViewById(R.id.quantity_et)
    unit_1 = view.findViewById(R.id.units_1_tv)
    unit_2 = view.findViewById(R.id.units_2_tv)
    unit_3 = view.findViewById(R.id.units_3_tv)
    unit_4 = view.findViewById(R.id.units_4_tv)
    unit_5 = view.findViewById(R.id.units_5_tv)
    moreFL = view.findViewById(R.id.units_6_fl)
    
    colorOnSecondary = view.getThemeColor(R.attr.colorOnSecondary)
    colorDarkGray = view.getThemeColor(R.attr.text_dark_onSecondaryAlpha)
    
    unit_1.setOnClickListener {
      highLight(EMPTY_PLACE)
      viewModel.unitSelected("")
    }
    unit_2.setOnClickListener {
      highLight(1)
      viewModel.unitSelected(unit_2.text.toString())
    }
    unit_3.setOnClickListener {
      highLight(2)
      viewModel.unitSelected(unit_3.text.toString())
      
    }
    unit_4.setOnClickListener {
      highLight(3)
      viewModel.unitSelected(unit_4.text.toString())
    }
    
    unit_5.setOnClickListener {
      highLight(4)
      viewModel.unitSelected(unit_5.text.toString())
      
    }
    moreFL.setOnClickListener {
      navigateToSelectUnit()
    }
    
    setupViewModel()
    setupObservers()
    
    val activity = requireActivity() as MainActivityImpl
    activity.supportActionBar?.show()
    activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_clear_in_app_bar)
    
    activity.setTitleAppBar(" ")
    setupOnBackPressed()
    setHasOptionsMenu(true)
    return view
  }
  
  private fun setupObservers() {
    viewModel.runBack.observe(viewLifecycleOwner, Observer {
      it?.let { isRunBack ->
        if (isRunBack) {
          findNavController().navigateUp()
        }
      }
    })
    
    viewModel.catalogItem.observe(viewLifecycleOwner, Observer {
      it?.let {
        render(it)
        
      }
    })
    
    viewModel.panel.observe(viewLifecycleOwner, Observer {
      it?.let {
        renderPanelUnit(it)
      }
    })
  }
  
  private fun renderPanelUnit(state: UnitPanelState) {
    unit_2.text = state.units[1]
    unit_3.text = state.units[2]
    unit_4.text = state.units[3]
    unit_5.text = state.units[4]
    
    highLight(state.currentIndex)
  }
  
  private fun render(item: CatalogItem) {
    title.setText(item.title)
    title.setSelection(item.title.length)
    
    quantityEt.setText(item.quantity.toString())
    
    increaseFl.setOnClickListener {
      viewModel.increaseQuantity()
    }
    
    decreaseFl.setOnClickListener {
      viewModel.decreaseQuantity()
    }
  }
  
  
  private fun highLight(place: Int) {
    when (place) {
      EMPTY_PLACE -> {
        selectedTv(unit_1)
        defaultStyleTv(unit_2)
        defaultStyleTv(unit_3)
        defaultStyleTv(unit_4)
        defaultStyleTv(unit_5)
      }
      
      1 -> {
        defaultStyleTv(unit_1)
        selectedTv(unit_2)
        defaultStyleTv(unit_3)
        defaultStyleTv(unit_4)
        defaultStyleTv(unit_5)
      }
      
      2 -> {
        defaultStyleTv(unit_1)
        defaultStyleTv(unit_2)
        selectedTv(unit_3)
        defaultStyleTv(unit_4)
        defaultStyleTv(unit_5)
      }
      
      3 -> {
        defaultStyleTv(unit_1)
        defaultStyleTv(unit_2)
        defaultStyleTv(unit_3)
        selectedTv(unit_4)
        defaultStyleTv(unit_5)
      }
      
      4 -> {
        defaultStyleTv(unit_1)
        defaultStyleTv(unit_2)
        defaultStyleTv(unit_3)
        defaultStyleTv(unit_4)
        selectedTv(unit_5)
      }
    }
  }
  
  private fun selectedTv(tv: TextView) {
    val height = R.drawable.rounded_corner
    
    tv.setBackgroundResource(height)
    tv.setTextColor(colorOnSecondary)
  }
  
  private fun defaultStyleTv(tv: TextView) {
    val default = R.drawable.rounded_corner_gray
    
    tv.setBackgroundResource(default)
    tv.setTextColor(colorDarkGray)
  }
  
  private fun setupOnBackPressed() {
    requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner,
                         object : OnBackPressedCallback(true) {
                           override fun handleOnBackPressed() {
                             goBack()
                           }
              
                         }
                        )
  }
  
  private fun goBack() {
    findNavController().navigateUp()
  }
  
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    menu.clear()
    inflater.inflate(R.menu.catalog_item_menu, menu)
  }
  
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        if (!(activity as MainActivityImpl).isOpenNavigationDrawer()) {
          goBack()
        }
        return true
      }
      
      R.id.menu_save -> {
        viewModel.saveItem(title.text.toString(), quantityEt.text.toString())
        return true
      }
      
    }
    return super.onOptionsItemSelected(item)
  }
  
  private fun navigateToSelectUnit() {
    viewModel.isNeedUpdate = true
    val action = R.id.action_addEditCatalogFragment_to_selectUnitsFragment
    val bundle = bundleOf("mode" to UnitsAdapter.Mode.SELECT.name)
    findNavController().navigate(action, bundle)
  }
  
  override fun onResume() {
    super.onResume()
    viewModel.updateIfNeedUpdated()
  }
  
  private fun setupViewModel() {
    val application = requireNotNull(this.activity).application
    val viewModelFactory = InjectorUtils.provideViewModelFactory(application)
    viewModel = ViewModelProvider(this, viewModelFactory)[AddEditCatalogViewModel::class.java]
    
    val idCatalog = arguments?.getLong("idCatalog") ?: 0
    val indexSelected = arguments?.getInt("indexSelected") ?: 0
    
    viewModel.start(id = idCatalog, indexI = indexSelected)
  }
}