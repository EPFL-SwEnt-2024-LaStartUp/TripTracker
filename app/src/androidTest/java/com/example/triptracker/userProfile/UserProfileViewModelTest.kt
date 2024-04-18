// package com.example.triptracker.userProfile
//
// import com.example.triptracker.model.profile.UserProfile
// import com.example.triptracker.model.profile.UserProfileList
// import com.example.triptracker.model.repository.UserProfileRepository
// import com.example.triptracker.viewmodel.UserProfileViewModel
// import io.mockk.*
// import org.junit.Before
// import org.junit.Test
// import org.junit.Assert.*
//
// class UserProfileViewModelTest {
//    private lateinit var viewModel: UserProfileViewModel
//    private val userProfileRepository = mockk<UserProfileRepository>(relaxed = true)
//    private val userProfileList = UserProfileList(mutableListOf())
//
//    @Before
//    fun setUp() {
//        // Mock the repository to prevent real database interaction
//        mockkConstructor(UserProfileRepository::class)
//        every { anyConstructed<UserProfileRepository>().getAllUserProfiles() } returns
// mutableListOf(
//            UserProfile("1", "Alice", "Smith", java.util.Date(), "AliceS", "imageURL")
//        )
//        viewModel = UserProfileViewModel().apply {
//            userProfileRepository = this@UserProfileViewModelTest.userProfileRepository
//            userProfileList = this@UserProfileViewModelTest.userProfileList
//        }
//    }
//
//    @Test
//    fun getAllUserProfilesFromDbTest() {
//        viewModel.getAllUserProfilesFromDb()
//        verify { userProfileRepository.getAllUserProfiles() }
//        assertEquals(1, viewModel.userProfileList.size())
//    }
//
//    @Test
//    fun addNewUserProfileToDbTest() {
//        val newUserProfile = UserProfile("2", "Bob", "Johnson", java.util.Date(), "BobJ",
// "imageURL2")
//        viewModel.addNewUserProfileToDb(newUserProfile)
//        verify { userProfileRepository.addNewUserProfile(newUserProfile) }
//    }
//
//    @Test
//    fun updateUserProfileInDbTest() {
//        val userProfileToUpdate = UserProfile("1", "Alice", "Smith", java.util.Date(),
// "AliceUpdated", "imageURL")
//        viewModel.updateUserProfileInDb(userProfileToUpdate)
//        verify { userProfileRepository.updateUserProfile(userProfileToUpdate) }
//    }
//
//    @Test
//    fun removeUserProfileInDbTest() {
//        viewModel.removeUserProfileInDb("1")
//        verify { userProfileRepository.removeUserProfile("1") }
//    }
// }
