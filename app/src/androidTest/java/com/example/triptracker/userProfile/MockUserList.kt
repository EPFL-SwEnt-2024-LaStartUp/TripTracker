package com.example.triptracker.userProfile

import com.example.triptracker.model.profile.UserProfile

class MockUserList {
  private val mockUser1 =
      UserProfile(
          "schifferlitheo@gmail.com",
          "Theo",
          "Schifferli",
          "15-October-1946",
          "Tete la malice",
          "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcS5n-VN2sv2jYgbMF3kVQWkYZQtdlQzje7_-9SYrgFe6w6gUQmL",
          listOf("cleorenaud38@gmail.com"),
          emptyList())

  private val mockUser2 =
      UserProfile(
          "barghornjeremy@gmail.com",
          "Jeremy",
          "Barghorn",
          "02-April-1939",
          "GEHREMY",
          "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQKS6BRrbH2Pj2QZeMJX_EVsnQcO89myNj1cxHeh2KRLMiamzmL",
          emptyList(),
          emptyList())

  private val mockUser3 =
      UserProfile(
          "polfuentescam@gmail.com",
          "Pol",
          "Fuentes",
          "27-September-1922",
          "Polfuegoooo",
          "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Maradona-Mundial_86_con_la_copa.JPG/1200px-Maradona-Mundial_86_con_la_copa.JPG",
          emptyList(),
          emptyList())

  private val mockUser4 =
      UserProfile(
          "leopold.galhaud@gmail.com",
          "Leopold",
          "Galhaud",
          "20-December-2017",
          "LeopoldinhoDoBrazil",
          "https://encrypted-tbn0.gstatic.com/licensed-image?q=tbn:ANd9GcRviVquGNsci2DsyGkO0Wf8vlR9m_L0mT2jskinyr7YD6L3CKw_kO6Vdv0D7gFmmFena5SNPDdB0J9R6x8",
          emptyList(),
          emptyList())

  private val mockUser5 =
      UserProfile(
          "misentaloic@gmail.com",
          "Loic",
          "Misenta",
          "10-December-1936",
          "Lomimi",
          "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcRrfhlD13rGYJRmde04FRLNn2AT3uApwbvOcEfa5pAdMkQA3q8w",
          emptyList(),
          emptyList())

  private val mockUser6 =
      UserProfile(
          "chaverotjrmy7@gmail.com",
          "Jeremy",
          "Chaverot",
          "17-November-1948",
          "JeremyyyTheTigerKing",
          "https://cloudfront-us-east-1.images.arcpublishing.com/advancelocal/6AREHOEFLBGB3EHADZJ4AQCD4A.jpg",
          emptyList(),
          emptyList())

  private val mockUser7 =
      UserProfile(
          mail = "cleorenaud38@gmail.com",
          name = "Cleo",
          surname = "Renaud",
          birthdate = "08-February-2004",
          username = "Cleoooo",
          profileImageUrl =
              "https://hips.hearstapps.com/hmg-prod/images/portrait-of-a-red-fluffy-cat-with-big-eyes-in-royalty-free-image-1701455126.jpg",
          followers = listOf("schifferlitheo@gmail.com", "barghornjeremy@gmail.com"),
          following = emptyList())

  private val mockUsers =
      listOf(mockUser1, mockUser2, mockUser3, mockUser4, mockUser5, mockUser6, mockUser7)

  fun getUserProfiles(): List<UserProfile> {
    return mockUsers
  }

  fun getNewMockUser(): UserProfile {
    return UserProfile(
        "4", "David", "Doe", "08-February-2021", "DavidD", null, emptyList(), emptyList())
  }
}
