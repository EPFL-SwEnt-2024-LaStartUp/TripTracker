package com.example.triptracker.userProfile

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.profile.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserProfileTest {
  private val date1 = "2021-01-01"

  private val userProfile1 =
      UserProfile(
          mail = "1",
          name = "Alice",
          surname = "Smith",
          birthdate = date1,
          username = "AliceS",
          profileImageUrl = "stupid-image-url.com",
          following = emptyList(),
          followers = emptyList())

  private val userProfile2 =
      UserProfile(
          mail = "2",
          name = "Bob",
          surname = "Johnson",
          birthdate = date1,
          username = "BobJ",
          profileImageUrl = null,
      )

  private val userProfile3 =
      UserProfile(
          mail = "3",
          birthdate = date1,
          username = "CharlieB",
      )

  @Test
  fun testUserProfile1() {
    assertEquals("1", userProfile1.mail)
    assertEquals("Alice", userProfile1.name)
    assertEquals("Smith", userProfile1.surname)
    assertEquals(date1, userProfile1.birthdate)
    assertEquals("AliceS", userProfile1.username)
    assertEquals("stupid-image-url.com", userProfile1.profileImageUrl)
    assertEquals(emptyList<UserProfile>(), userProfile1.following)
    assertEquals(emptyList<UserProfile>(), userProfile1.followers)
  }

  @Test
  fun testUserProfile2() {
    assertEquals("2", userProfile2.mail)
    assertEquals("Bob", userProfile2.name)
    assertEquals("Johnson", userProfile2.surname)
    assertEquals(date1, userProfile2.birthdate)
    assertEquals("BobJ", userProfile2.username)
    assertEquals(null, userProfile2.profileImageUrl)
    assertEquals(emptyList<UserProfile>(), userProfile2.following)
    assertEquals(emptyList<UserProfile>(), userProfile2.followers)
  }

  @Test
  fun testUserProfile3() {
    assertEquals("3", userProfile3.mail)
    assertEquals("", userProfile3.name)
    assertEquals("", userProfile3.surname)
    assertEquals(date1, userProfile3.birthdate)
    assertEquals("CharlieB", userProfile3.username)
    assertEquals(null, userProfile3.profileImageUrl)
    assertEquals(emptyList<UserProfile>(), userProfile3.following)
    assertEquals(emptyList<UserProfile>(), userProfile3.followers)
  }
}
