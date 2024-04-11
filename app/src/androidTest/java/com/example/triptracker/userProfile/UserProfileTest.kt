package com.example.triptracker.userProfile

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.triptracker.model.profile.UserProfile
import java.util.Date
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserProfileTest {
  private val date1 = Date(2021, 1, 1)

  private val userProfile1 =
      UserProfile(
          id = "1",
          name = "Alice",
          surname = "Smith",
          birthdate = date1,
          pseudo = "AliceS",
          profileImageUrl = "stupid-image-url.com")

  private val userProfile2 = UserProfile(id = "2")

  @Test
  fun testUserProfile1() {
    assertEquals("1", userProfile1.id)
    assertEquals("Alice", userProfile1.name)
    assertEquals("Smith", userProfile1.surname)
    assertEquals(date1, userProfile1.birthdate)
    assertEquals("AliceS", userProfile1.pseudo)
    assertEquals("stupid-image-url.com", userProfile1.profileImageUrl)
  }

  @Test
  fun testUserProfile2() {
    assertEquals("2", userProfile2.id)
    assertEquals("", userProfile2.name)
    assertEquals("", userProfile2.surname)
    assertEquals(date1, userProfile2.birthdate)
    assertEquals("", userProfile2.pseudo)
    assertEquals(null, userProfile2.profileImageUrl)
  }

  @Test
  fun modifyUserProfile() {
    userProfile2.name = "Bob"
    userProfile2.surname = "Johnson"
    userProfile2.birthdate = date1
    userProfile2.pseudo = "BobJ"
    userProfile2.profileImageUrl = "another-stupid-image-url.com"

    assertEquals("2", userProfile2.id)
    assertEquals("Bob", userProfile2.name)
    assertEquals("Johnson", userProfile2.surname)
    assertEquals(date1, userProfile2.birthdate)
    assertEquals("BobJ", userProfile2.pseudo)
    assertEquals("another-stupid-image-url.com", userProfile2.profileImageUrl)
  }
}
