package com.example.triptracker.userProfile

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.profile.UserProfileList
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class UserProfileListTest {
    private val userProfileList1 = UserProfileList(listOf())
    private val userProfileList2 = UserProfileList(
        listOf(
            UserProfile(
                id = "1",
                name = "Alice",
                surname = "Smith",
                birthdate = Date(),
                pseudo = "AliceS"
            ),
            UserProfile(
                id = "2",
                name = "Bob",
                surname = "Johnson",
                birthdate = Date(),
                pseudo = "BobJ"
            ),
            UserProfile(
                id = "3",
                name = "Bob",
                surname = "Brown",
                birthdate = Date(),
                pseudo = "BobB"
            )
        )
    )

//    @Test
//    fun getAllUserProfilesTest() {
//        val userList1 = userProfileList1.getAllUserProfiles()
//        assertEquals(userProfileList1, userList1)
//
//        val userList2 = userProfileList2.getAllUserProfiles()
//        assertEquals(userProfileList2, userList2)
//    }

    @Test
    fun getFilteredUserProfileTest() {
        val filteredList1 = userProfileList1.getFilteredUserProfile("Alice")
        assertEquals(0, filteredList1.size)

        val filteredList2 = userProfileList2.getFilteredUserProfile("Alice")
        assertEquals(1, filteredList2.size)
        assertEquals("Alice", filteredList2[0].name)

        val filteredList3 = userProfileList2.getFilteredUserProfile("Bob")
        assertEquals(2, filteredList3.size)
        assertEquals("Bob", filteredList3[0].name)
        assertEquals("Bob", filteredList3[1].name)
    }

    @Test
    fun getUserProfileTest() {
//        val userProfile1 = userProfileList1.getUserProfile("1")
//        assertEquals(null, userProfile1)

        val userProfile2 = userProfileList2.getUserProfile("1")
        assertEquals("1", userProfile2.id)

        val userProfile3 = userProfileList2.getUserProfile("2")
        assertEquals("2", userProfile3.id)

        val userProfile4 = userProfileList2.getUserProfile("3")
        assertEquals("3", userProfile4.id)

//        val userProfile5 = userProfileList2.getUserProfile("4")
//        assertEquals(null, userProfile5)
    }

    @Test
    fun sizeOfUserProfileList() {
        assertEquals(0, userProfileList1.size())

        assertEquals(3, userProfileList2.size())
    }
}