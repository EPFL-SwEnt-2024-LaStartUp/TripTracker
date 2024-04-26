package com.example.triptracker.userProfile

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.profile.UserProfileList
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserProfileListTest {

  val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
  private val date1 = LocalDate.of(2012, 1, 1).format(formatter)

  private val userProfileList1 = UserProfileList(listOf())
  private val userProfileList2 =
      UserProfileList(
          listOf(
              UserProfile(
                  mail = "1",
                  name = "Alice",
                  surname = "Smith",
                  birthdate = date1,
                  username = "AliceS"),
              UserProfile(
                  mail = "2",
                  name = "Bob",
                  surname = "Johnson",
                  birthdate = date1,
                  username = "BobJ"),
              UserProfile(
                  mail = "3",
                  name = "Bob",
                  surname = "Brown",
                  birthdate = date1,
                  username = "BobB")))

  private val userProfileList3 =
      UserProfileList(
          listOf(
              UserProfile(
                  mail = "4",
                  name = "Alice",
                  surname = "Smith",
                  birthdate = date1,
                  username = "AliceS"),
              UserProfile(
                  mail = "5",
                  name = "Bob",
                  surname = "Johnson",
                  birthdate = date1,
                  username = "BobJ"),
              UserProfile(
                  mail = "6",
                  name = "Charlie",
                  surname = "Brown",
                  birthdate = date1,
                  username = "CharlieB")))

  // TODO: complete this test

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

    val filteredList4 = userProfileList2.getFilteredUserProfile("Brown")
    assertEquals(1, filteredList4.size)

    val filteredList5 = userProfileList2.getFilteredUserProfile("BobB")
    assertEquals(1, filteredList5.size)
  }

  @Test
  fun getUserProfileTest() {
    //        val userProfile1 = userProfileList1.getUserProfile("1")
    //        assertEquals(null, userProfile1)

    val userProfile2 = userProfileList2.getUserProfile("1")
    assertEquals("1", userProfile2.mail)

    val userProfile3 = userProfileList2.getUserProfile("2")
    assertEquals("2", userProfile3.mail)

    val userProfile4 = userProfileList2.getUserProfile("3")
    assertEquals("3", userProfile4.mail)

    //        val userProfile5 = userProfileList2.getUserProfile("4")
    //        assertEquals(null, userProfile5)
  }

  @Test
  fun sizeOfUserProfileList() {
    assertEquals(0, userProfileList1.size())

    assertEquals(3, userProfileList2.size())
  }

  @Test
  fun setUserProfileListTest() {
    userProfileList1.setUserProfileList(userProfileList3.getAllUserProfiles())
    assertEquals(userProfileList3, userProfileList1)
  }

  @Test
  fun addUserProfileTest() {
    userProfileList1.addUserProfile(userProfileList3.getUserProfile("4"))
    userProfileList1.addUserProfile(userProfileList3.getUserProfile("5"))
    userProfileList1.addUserProfile(userProfileList3.getUserProfile("6"))
    assertEquals(userProfileList3, userProfileList1)
  }
}
