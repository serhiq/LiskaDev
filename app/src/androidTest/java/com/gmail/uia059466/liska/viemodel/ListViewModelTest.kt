package com.gmail.uia059466.liska.viemodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gmail.uia059466.liska.data.ListDisplay
import com.gmail.uia059466.liska.domain.UserRepository
import com.gmail.uia059466.liska.domain.usecase.GetAllListUseCase
import com.gmail.uia059466.liska.lists.ListsAction
import com.gmail.uia059466.liska.lists.ListsViewModel
import com.gmail.uia059466.liska.lists.sortorder.SortOrder
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class ListViewModelTest {
  
  @get:Rule
  var instantTaskExecutorRule = InstantTaskExecutorRule()
  
  @Mock
  lateinit var userRepository: UserRepository

  @Mock
  lateinit var usecases: GetAllListUseCase

  lateinit var viewModel: ListsViewModel
  

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  suspend fun sort_a_z_work()= runBlocking {

    val list = listOf<ListDisplay>(
      ListDisplay(
        id = 0, title = "1", description = "", order = 0, dataCreate = 1, dataModification = 0,veryLongDescription = ""
      ),
      ListDisplay(
        id = 2, title = "2", description = "", order = 0, dataCreate = 2, dataModification = 0,veryLongDescription = ""
      ),
      ListDisplay(
        id = 3, title = "3", description = "", order = 0, dataCreate = 3, dataModification = 0,veryLongDescription = ""
      ),

      )

    Mockito.`when`(usecases.invoke()).thenReturn(list)
    Mockito.`when`(userRepository.readSortOrderList()).thenReturn(SortOrder.A_Z)

//    viewModel = ListsViewModel(usecases, userRepository)
//    assertTrue(viewModel.listUi.size == 3)
//    assertTrue(viewModel.listUi[0].title == "1")
//    assertTrue(viewModel.listUi[1].title == "2")
//    assertTrue(viewModel.listUi[2].title == "3")
//
////    Mockito.`when`(userRepository.saveSortOrder(SortOrder.A_Z))
//    viewModel.takeAction(ListsAction.SortList(SortOrder.A_Z))
//
//    assertTrue(viewModel.listUi.size == 3)

  }

  @Test
  fun sort_data_create_work()= runBlocking {

    val list = listOf<ListDisplay>(
      ListDisplay(
        id = 0, title = "1", description = "", order = 0, dataCreate = 1, dataModification = 0
        ,veryLongDescription = ""),
      ListDisplay(
        id = 2, title = "2", description = "", order = 0, dataCreate = 2, dataModification = 0,veryLongDescription = ""
      ),
      ListDisplay(
        id = 3, title = "3", description = "", order = 0, dataCreate = 3, dataModification = 0,veryLongDescription = ""
      ),

      )

    Mockito.`when`(usecases.invoke()).thenReturn(list)
    Mockito.`when`(userRepository.readSortOrderList()).thenReturn(SortOrder.A_Z)

//    viewModel = ListsViewModel(usecases, userRepository)
//    assertTrue(viewModel.listUi.size == 3)
//    assertTrue(viewModel.listUi[0].title == "1")
//    assertTrue(viewModel.listUi[1].title == "2")
//    assertTrue(viewModel.listUi[2].title == "3")

//    Mockito.`when`(userRepository.saveSortOrder(SortOrder.A_Z))
    Mockito.`when`(userRepository.readSortOrderList()).thenReturn(SortOrder.NEWEST_FIRST)

    viewModel.takeAction(ListsAction.SortList(SortOrder.NEWEST_FIRST))

//    assertEquals("3",viewModel.listUi[0].title)

//    assertTrue(viewModel.listUi.size == 3)
//    assertTrue(viewModel.listUi[0].title == "3")
//    assertTrue(viewModel.listUi[1].title == "2")
//    assertTrue(viewModel.listUi[2].title == "1")

  }

}