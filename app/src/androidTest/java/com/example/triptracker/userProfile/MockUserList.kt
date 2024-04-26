package com.example.triptracker.userProfile

import com.example.triptracker.model.profile.UserProfile
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MockUserList {
  val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
  private val mockUser1 =
      UserProfile(
          "1",
          "Alice",
          "Smith",
          LocalDate.of(2021, 1, 1).format(formatter),
          "AliceS",
          "stupid-image-url.com",
          emptyList(),
          emptyList())

  private val mockUser2 =
      UserProfile(
          "2",
          "Bob",
          "Johnson",
          LocalDate.of(2021, 1, 1).format(formatter),
          "BobJ",
          null,
          emptyList(),
          emptyList())

  private val mockUser3 =
      UserProfile(
          "3",
          "Charlie",
          "Brown",
          LocalDate.of(2021, 1, 1).format(formatter),
          "CharlieB",
          null,
          listOf(mockUser1, mockUser2),
          listOf(mockUser1, mockUser2))

  private val mockUsers = listOf(mockUser1, mockUser2, mockUser3)

  fun getUserProfiles(): List<UserProfile> {
    return mockUsers
  }

  fun getNewMockUser(): UserProfile {
    return UserProfile(
        "4",
        "David",
        "Doe",
        LocalDate.of(2021, 1, 1).format(formatter),
        "DavidD",
        null,
        emptyList(),
        emptyList())
  }
}
