package com.gmail.uia059466.liska.listdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ShareCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.uia059466.liska.R
import com.gmail.uia059466.liska.data.ListDisplay
import com.gmail.uia059466.liska.domain.UserPreferencesRepositoryImpl
import com.gmail.uia059466.liska.main.AppBarUiState
import com.gmail.uia059466.liska.main.MainActivityImpl
import com.gmail.uia059466.liska.selectfromcatalog.CatalogSelectFragment
import com.gmail.uia059466.liska.setting.selectcatalog.CatalogDisplayOption
import com.gmail.uia059466.liska.utils.InjectorUtils
import com.gmail.uia059466.liska.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar

class DisplayListFragment : Fragment(), ItemDragListener, DisplayListAdapter.ItemAdapterListener  {
  
  private lateinit var viewModel: DisplayListViewModel
  private lateinit var itemTouchHelper: ItemTouchHelper
  private lateinit var adapter: DisplayListAdapter
  private lateinit var listRv:RecyclerView
  
  private lateinit var divider:View
  private lateinit var editText:EditText
  private lateinit var saveButton:ImageButton
  
  private lateinit var caption:TextView
  private lateinit var optionCatalogSelect:CatalogDisplayOption
  private lateinit var content:ConstraintLayout

  override fun onCreateView(
          inflater: LayoutInflater, container: ViewGroup?,
          savedInstanceState: Bundle?
                           ): View? {
    
    val view = inflater.inflate(
            R.layout.addedit_list_fragment,
            container,
            false)
  
    divider=view.findViewById(R.id.divider)
    editText=view.findViewById(R.id.enter_word_edit_text)
    saveButton=view.findViewById(R.id.save_word_button)
    caption=view.findViewById(R.id.text)
    listRv = view.findViewById(R.id.list)
    content = view.findViewById(R.id.list_detailed_content)

    val pref= UserPreferencesRepositoryImpl.getInstance(requireContext())
    optionCatalogSelect=pref.catalogDisplayOption

    setupViewModel()
    setupRecyclerView()
    setupObservers()
    setupOnBackPressed()
    setHasOptionsMenu(true)
    return view
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    requireActivity().invalidateOptionsMenu()
  }
  
  private fun switchAdapter(adapterMode: Int) {
    when(adapterMode){
      DisplayListAdapter.SELECT -> enableSelectMode()
      DisplayListAdapter.EDIT -> enableEditMode()
      DisplayListAdapter.VIEW ->enableViewMode()
    }
  }
  
  private fun enableViewMode() {
    (activity as MainActivityImpl).hideCustomTitle()

    caption.visibility = View.GONE
    divider   .visibility =View.GONE
    editText  .visibility =View.GONE
    saveButton.visibility =View.GONE
  
    adapter.setupAdapterMode(DisplayListAdapter.VIEW)
    
    viewModel.takeAction(DisplayListAction.displayObjectTitle)
   
    val title=viewModel.title.value?:""
    renderAppbar(AppBarUiState.IconNavigationWithTitle(title))
    requireActivity().invalidateOptionsMenu()
  }
  
  private fun enableEditMode()
  {
    caption.visibility = View.VISIBLE
    divider   .visibility =View.VISIBLE
    editText  .visibility =View.VISIBLE
    saveButton.visibility =View.VISIBLE
  
    setupEditText()
  
    adapter.setupAdapterMode(DisplayListAdapter.EDIT)

    renderAppbar(AppBarUiState.ArrayWithTitle(""))
  
    viewModel.takeAction(DisplayListAction.displayEditTitle)
    (activity as MainActivityImpl).setupCustomTitle(viewModel.title.value?:"")
    (activity as MainActivityImpl).setupClickListener {
  
      val dialog = EditItemDialog.newInstance(text = viewModel.copyTitle, title = getString(R.string.list_name_dialog))
      dialog.onOk = {
        val text = dialog.editText.text.toString()
        if (text.isNotBlank()) {
          viewModel.takeAction(DisplayListAction.ChangeTitleObject(text))
        }
      }
      dialog.show(requireActivity().supportFragmentManager, null)
    }
     requireActivity().invalidateOptionsMenu()
  }
  
  private fun enableSelectMode() {
    hideKeyboardFragment()

    (activity as MainActivityImpl).hideCustomTitle()
    val title="0"

    renderAppbar(AppBarUiState.ArrayWithTitle(title))

    divider   .visibility =View.GONE
    editText  .visibility =View.GONE
    saveButton.visibility =View.GONE
    
    adapter.setupAdapterMode(DisplayListAdapter.SELECT)

    requireActivity().invalidateOptionsMenu()
  }
  
  private fun setupTitle(title: String) {
    if(viewModel.adapterMode.value== DisplayListAdapter.EDIT){
      (activity as MainActivityImpl).setupCustomTitle(title)
    }else{
      (activity as MainActivityImpl).setTitleAppBar(title)
    }
  }
  
  private fun renderAppbar(state:AppBarUiState){
    val mainActivity = activity as MainActivityImpl
    mainActivity.renderAppbar(state)
  }
  
  private fun setupRecyclerView() {
    if (view!=null){
      listRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
          if (dy != 0) {
            hideKeyboardFragment()
            listRv.requestFocus()

          }
        }
      })
    
    }
    adapter = DisplayListAdapter(this, this)
    listRv.layoutManager = LinearLayoutManager(activity)
    listRv.adapter = adapter

    itemTouchHelper = ItemTouchHelper(
            ItemTouchHelperCallback(
                    adapter
                                   )
                                     )
    itemTouchHelper.attachToRecyclerView(listRv)
  
  }

 override fun hideKeyboardFragment() {
    hideKeyboard()
  }

  override fun onItemDrag(viewHolder: RecyclerView.ViewHolder) {
    itemTouchHelper.startDrag(viewHolder)
  }
  

  private fun navigateToBack() {
    (activity as MainActivityImpl).refreshWidgetDelay()
    viewModel.takeAction(DisplayListAction.RunBack(adapterList = adapter.getItems()))
  }
  private fun setupEditText() {
    editText.setOnEditorActionListener { _, actionId: Int, _ ->
      if (actionId == EditorInfo.IME_ACTION_NEXT) {
        val text = editText.text.toString()
        if (text.isNotBlank()) {
          addItem(text)
        }
        return@setOnEditorActionListener true
      }
      false
    }

    val btnSave = view?.findViewById<ImageButton>(R.id.save_word_button)
    btnSave?.setOnClickListener {
      val text = editText.text.toString()
      if (text.isNotBlank()) {
        addItem(text)
      }
    }
  }

  private fun addItem(text: String) {
    val insertedIndex=adapter.addUnit(text.trim())
    listRv.scrollToPosition(insertedIndex)
    editText.text?.clear()
  }

  override fun setupSelectedMode(isSelectedMode: Boolean) {
    viewModel.takeAction(DisplayListAction.ChangeMode(DisplayListAdapter.SELECT))
  }
  
  override fun changeSelectedCount(selected: Int) {
    viewModel.takeAction(DisplayListAction.ChangeTitle(selected.toString()))
  }

  override fun onItemClickedEdit(title: String, position: Int) {
    val dialog = EditItemDialog.newInstance(text = title, title = "Редактировать элемент")
    dialog.onOk = {
      val newTitle = dialog.editText.text.toString()
      if (newTitle.isNotBlank()&&title!=newTitle) {
        adapter.changePosition(newTitle,position)
      }
    }
    dialog.show(requireActivity().supportFragmentManager, null)
  }
  
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    menu.clear()
    inflater.inflate(R.menu.add_edit_list, menu)
  }
  
  override fun onPrepareOptionsMenu(menu: Menu) {
    when(viewModel.adapterMode.value){
      DisplayListAdapter.VIEW ->{configureMenuForView(menu)}
      DisplayListAdapter.EDIT ->{configureMenuForEdit(menu)}
      DisplayListAdapter.SELECT ->{configureMenuForSelect(menu)}
    }
  }
  
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> {
        hideKeyboard()
        if ((activity as MainActivityImpl).isOpenNavigationDrawer()){
          viewModel.takeAction(DisplayListAction.SaveItems(adapter.getItems()))
        }else{
          navigateToBack()
        }
        return true

      }
      R.id.menu_add_from_catalog -> {
        hideKeyboard()
        viewModel.takeAction(DisplayListAction.NavigateToCatalog(adapter.getItems()))
        return true

      }

//        <!--    mode edit-->
      R.id.menu_add_mode -> {
        viewModel.isNew=false
        viewModel.takeAction(DisplayListAction.ChangeMode(DisplayListAdapter.EDIT))
        return true
      }
      R.id.menu_delete_list -> {
        viewModel.takeAction(DisplayListAction.DeleteList)
        (activity as MainActivityImpl).refreshWidgetDelay()
        return true
      }
      R.id.menu_send_list -> {
        val message=createMessage(adapter.getItems())
         showShare(message)
  
        return true
      }
      R.id.menu_copy_list -> {
        viewModel.takeAction(DisplayListAction.CopyList(adapter.getItems()))
          return true
      }
//        <!--mode selected-->
      R.id.menu_move_selected -> {
        viewModel.takeAction(DisplayListAction.DisplayDialogMoveSelected)
        return true
      }
      
      R.id.menu_delete_selected -> {
        adapter.deleteSelected()
        resetSelected()
        return true
      }
      R.id.menu_send_selected -> {
        val selected=adapter.getSelected()
        val message=createMessage(selected)
        showShare(message)
        
        return true
      }
      R.id.menu_copy_selected -> {
        adapter.copySelected()
        resetSelected()
          return true
      }
      
      R.id.menu_add_with_quantity -> {
        viewModel.takeAction(DisplayListAction.NavigateToCatalogQuantity(adapter.getItems()))
        return true
      }
    }
    return true
  }
  
  private fun configureMenuForView(menu: Menu) {
    val editModeMenu=menu.findItem(R.id.menu_add_mode)
    editModeMenu.isVisible = true
    
    val deleteListMenu=menu.findItem(R.id.menu_delete_list)
    deleteListMenu.isVisible = true
    
    val sendListMenu=menu.findItem(R.id.menu_send_list)
    sendListMenu.isVisible = true
    
    val copyListMenu=menu.findItem(R.id.menu_copy_list)
    copyListMenu.isVisible = true
//  and other is hide
    
    val moveSelected=menu.findItem(R.id.menu_move_selected)
    moveSelected.isVisible = false
    
    val deleteSelected=menu.findItem(R.id.menu_delete_selected)
    deleteSelected.isVisible = false
    
    val sendSelected=menu.findItem(R.id.menu_send_selected)
    sendSelected.isVisible = false
    
    val copySelected=menu.findItem(R.id.menu_copy_selected)
    copySelected.isVisible = false
    
  }
  
  private fun configureMenuForEdit(menu: Menu) {
    if (optionCatalogSelect!=null){

      when(optionCatalogSelect){

        CatalogDisplayOption.CHECKBOX ->{
          val addFromCatalog=menu.findItem(R.id.menu_add_from_catalog)
          addFromCatalog.isVisible = true

          val addWithQuantity=menu.findItem(R.id.menu_add_with_quantity)
          addWithQuantity.isVisible = false

        }
        CatalogDisplayOption.SELECT ->{
          val addFromCatalog=menu.findItem(R.id.menu_add_from_catalog)
          addFromCatalog.isVisible = false

          val addWithQuantity=menu.findItem(R.id.menu_add_with_quantity)
          addWithQuantity.isVisible = true

        }
        CatalogDisplayOption.TWO_VAR->{
          val addFromCatalog=menu.findItem(R.id.menu_add_from_catalog)
          addFromCatalog.isVisible = true

          val addWithQuantity=menu.findItem(R.id.menu_add_with_quantity)
          addWithQuantity.isVisible = true

        }
      }
    }

    val editModeMenu=menu.findItem(R.id.menu_add_mode)
    editModeMenu.isVisible = false
    
    val deleteListMenu=menu.findItem(R.id.menu_delete_list)
    deleteListMenu.isVisible = false
    
    val sendListMenu=menu.findItem(R.id.menu_send_list)
    sendListMenu.isVisible = false
    
    val copyListMenu=menu.findItem(R.id.menu_copy_list)
    copyListMenu.isVisible = false

//    hide mode
    val moveSelected=menu.findItem(R.id.menu_move_selected)
    moveSelected.isVisible = false
    
    val deleteSelected=menu.findItem(R.id.menu_delete_selected)
    deleteSelected.isVisible = false
    
    val sendSelected=menu.findItem(R.id.menu_send_selected)
    sendSelected.isVisible = false
    
    val copySelected=menu.findItem(R.id.menu_copy_selected)
    copySelected.isVisible = false
  }
  
  private fun configureMenuForSelect(menu: Menu) {
//    set visible
    val moveSelected=menu.findItem(R.id.menu_move_selected)
    moveSelected.isVisible = true
    
    val deleteSelected=menu.findItem(R.id.menu_delete_selected)
    deleteSelected.isVisible = true
    
    val sendSelected=menu.findItem(R.id.menu_send_selected)
    sendSelected.isVisible = true
    
    val copySelected=menu.findItem(R.id.menu_copy_selected)
    copySelected.isVisible = true
    
    
    //  and other is hide
//
    val editModeMenu=menu.findItem(R.id.menu_add_mode)
    editModeMenu.isVisible = false
    
    val deleteListMenu=menu.findItem(R.id.menu_delete_list)
    deleteListMenu.isVisible = false
    
    val sendListMenu=menu.findItem(R.id.menu_send_list)
    sendListMenu.isVisible = false
    
    val copyListMenu=menu.findItem(R.id.menu_copy_list)
    copyListMenu.isVisible = false
  }
  
  
  private fun displayDialogSendSelected(listDisplay:List<ListDisplay>) {
    val dialog = SendSelectedDialogFragment.newInstance(listDisplay)
    dialog.onOk = {
      val selectedId=dialog.selectedNow
     
      viewModel.takeAction(DisplayListAction.MoveToList(id = selectedId,selected = adapter.getSelected(),adapterList = adapter.getItems()))
      adapter.deleteSelected()
      resetSelected()
      dialog.dismiss()
    }
    dialog.show(requireActivity().supportFragmentManager, null)
  }

  private fun navigateToCatalog(id: Long) {
    val action=R.id.action_displayListFragment_to_catalogSelectFragment

    val bundle = bundleOf("listId" to id, CatalogSelectFragment.CATALOG_MODE to CatalogSelectFragment.CATALOG_MODE_SELECT)

    findNavController().navigate(action, bundle)
  }
  
  private fun showShare(message: String) {
    val title=viewModel.title.value?:createCurrentDataTitle()
    ShareCompat.IntentBuilder.from(requireActivity())
            .setType("text/plain")
            .setChooserTitle(title)
            .setText(message)
            .startChooser()
   
  }
  
  private fun setupViewModel() {
    val application = requireNotNull(this.activity).application
    val viewModelFactory = InjectorUtils.provideViewModelFactory(application)
    viewModel = ViewModelProvider(this, viewModelFactory)[DisplayListViewModel::class.java]
  
    val isEditMode = arguments?.getBoolean("isEditMode")
    val listUuid = arguments?.getLong("listId")
    val isNew = arguments?.getBoolean("isNew")?:false

    if (isEditMode!=null&&listUuid!=null) viewModel.start(isEditMode, listUuid,isNew)
  }
  
  private fun setupObservers() {
    viewModel.title.observe(viewLifecycleOwner, Observer {
      it?.let {
        setupTitle(it)
      }
    })
    
    viewModel.dataLoading.observe(viewLifecycleOwner, Observer {
      it?.let {
        adapter.setupData(viewModel.requestItems())
        viewModel.takeAction(DisplayListAction.displayObjectTitle)
        val title=viewModel.title.value?:""
        renderAppbar(AppBarUiState.IconNavigationWithTitle(title))
        val pref= UserPreferencesRepositoryImpl.getInstance(requireContext())
        adapter.isMovedChecked=pref.isMovedChecked
      }
    })
    
    viewModel.runBack.observe(viewLifecycleOwner, Observer {
      it?.let {isRunBack->
        if (isRunBack){
          findNavController().navigateUp()
        }
      }
    })
    
    viewModel.adapterMode.observe(viewLifecycleOwner, Observer {
      it?.let {
        switchAdapter(it)
      }
    })

    viewModel.refreshNavigation.observe(viewLifecycleOwner, Observer {
      it?.let {
        (activity as MainActivityImpl).updateNavigationDrawer()
      }
    })
    
    viewModel.navigateToCatalog.observe(viewLifecycleOwner, Observer {
      it?.let {
        navigateToCatalog(it)
      }
    })
    
    viewModel.navigateToCatalogWithQuantity.observe(viewLifecycleOwner, Observer {
      it?.let {
        navigateToCatalogWitchQuantity(it)
      }
    })
    
    viewModel.listForMoveDialog.observe(viewLifecycleOwner, Observer {
      it?.let {
        displayDialogSendSelected(it)
      }
    })
  
    viewModel.snackbarText.observe(viewLifecycleOwner, Observer {
      it?.let {text->
                displaySnackBar(text)
      }
    })
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



  private fun navigateToCatalogWitchQuantity(id:Long) {

    val action=R.id.action_displayListFragment_to_catalogSelectFragment

    val bundle = bundleOf("listId" to id, CatalogSelectFragment.CATALOG_MODE to CatalogSelectFragment.CATALOG_MODE_QUANTITY)

    findNavController().navigate(action, bundle)
  }
  
  private fun setupOnBackPressed() {
    requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner,
                         object : OnBackPressedCallback(true) {
                           override fun handleOnBackPressed() {

                             navigateToBack()                         }
                         }
                        )
  }
  
  private fun resetSelected(){
    adapter.clearSelected()
    viewModel.takeAction(DisplayListAction.ChangeMode(DisplayListAdapter.VIEW))
  }

  override fun onStop() {
    super.onStop()
    (activity as MainActivityImpl).hideCustomTitle()
  }

  override fun onPause() {
    super.onPause()
    viewModel.takeAction(DisplayListAction.SaveItems(adapter.getItems()))
  }
}