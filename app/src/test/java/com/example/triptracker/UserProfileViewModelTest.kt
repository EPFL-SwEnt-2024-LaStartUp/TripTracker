package com.example.triptracker

import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.profile.UserProfileList
import com.example.triptracker.model.repository.UserProfileRepository
import com.example.triptracker.viewmodel.UserProfileViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.Date
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class UserProfileViewModelTest {

  @Mock private lateinit var firestore: FirebaseFirestore

  private lateinit var userProfileRepository: UserProfileRepository
  private lateinit var userProfileList: UserProfileList
  private lateinit var viewModel: UserProfileViewModel

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)

    // Create mocks for Firestore interactions
    val mockCollection = mock<CollectionReference>()
    val mockQuerySnapshot = mock<QuerySnapshot>()
    val mockTask =
        Tasks.forResult(mockQuerySnapshot) // Create a successful task with your mock QuerySnapshot

    // Setup the mocks to return when methods are called
    whenever(firestore.collection(anyString())).thenReturn(mockCollection)
    whenever(mockCollection.get()).thenReturn(mockTask as Task<QuerySnapshot>)

    // Now you can safely create your UserProfileRepository with the mocked Firestore
    userProfileRepository = UserProfileRepository(firestore)
    userProfileList =
        UserProfileList(
            mutableListOf(
                UserProfile(
                    mail = "1",
                    name = "Alice",
                    surname = "Smith",
                    birthdate = Date(2021, 1, 1),
                    pseudo = "AliceS",
                    profileImageUrl = "image-url.com")))
    viewModel = UserProfileViewModel(userProfileRepository, userProfileList)
  }

  @Test
  fun testGetAllUserProfilesFromDb() {
    whenever(firestore.collection(anyString())).thenReturn(mock()) // Continue to mock the behavior
    viewModel.getAllUserProfilesFromDb()
    verify(userProfileList).setUserProfileList(anyList())
  }

  @Test
  fun testAddNewUserProfileToDb() {
    val userProfile =
        UserProfile(
            mail = "1",
            name = "Alice",
            surname = "Smith",
            birthdate = Date(2021, 1, 1),
            pseudo = "AliceS",
            profileImageUrl = "image-url.com",
            followers = mutableListOf(),
            following = mutableListOf())
    viewModel.addNewUserProfileToDb(userProfile)
    verify(userProfileRepository).addNewUserProfile(userProfile)
  }
}
