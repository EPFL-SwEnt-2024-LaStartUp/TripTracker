package com.example.triptracker.userProfile

import com.example.triptracker.model.profile.UserProfile
import java.util.Date

class MockUserList {
    private val mockUser1 =
        UserProfile(
            "1",
            "Alice",
            "Smith",
            Date(2021, 1, 1),
            "AliceS",
            "stupid-image-url.com",
            emptyList(),
            emptyList())

    private val mockUser2 =
        UserProfile("2", "Bob", "Johnson", Date(2021, 1, 1), "BobJ", null, emptyList(), emptyList())

    private val mockUser3 =
        UserProfile(
            "3",
            "Charlie",
            "Brown",
            Date(2021, 1, 1),
            "CharlieB",
            null,
            listOf(mockUser1, mockUser2),
            listOf(mockUser1, mockUser2))

    private val mockUsers = listOf(mockUser1, mockUser2, mockUser3)

    fun getUserProfiles(): List<UserProfile> {
        return mockUsers
    }

    fun getNewMockUser(): UserProfile {
        return UserProfile("4", "David", "Doe", Date(2021, 1, 1), "DavidD", null, emptyList(), emptyList())
    }
}
