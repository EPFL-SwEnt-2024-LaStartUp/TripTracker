// package com.example.triptracker.userProfile
//
// import androidx.test.ext.junit.runners.AndroidJUnit4
// import com.example.triptracker.model.profile.UserProfile
// import com.example.triptracker.model.repository.UserProfileRepository
// import com.google.android.gms.tasks.Task
// import com.google.firebase.Firebase
// import com.google.firebase.firestore.CollectionReference
// import com.google.firebase.firestore.DocumentReference
// import com.google.firebase.firestore.DocumentSnapshot
// import com.google.firebase.firestore.FirebaseFirestore
// import com.google.firebase.firestore.QuerySnapshot
// import com.google.firebase.firestore.firestore
// import io.mockk.Runs
// import io.mockk.every
// import io.mockk.just
// import io.mockk.mockk
// import io.mockk.mockkStatic
// import io.mockk.verify
// import java.util.Date
// import org.junit.Before
// import org.junit.Test
// import org.junit.runner.RunWith
//
// @RunWith(AndroidJUnit4::class)
// class UserProfileRepositoryTest {
//    private lateinit var repository: UserProfileRepository
//    private val db = mockk<FirebaseFirestore>()
//    private val userProfileCollection = mockk<CollectionReference>()
//    private val documentReference = mockk<DocumentReference>()
//    private val querySnapshot = mockk<QuerySnapshot>()
//    private val taskSnapshot = mockk<Task<QuerySnapshot>>()
//    private val taskVoid = mockk<Task<Void>>()
//
//    @Before
//    fun setUp() {
//        mockkStatic(Firebase::class)
//        every { Firebase.firestore } returns db
//        every { db.collection("user_profiles") } returns userProfileCollection
//        repository = UserProfileRepository()
//    }
//
//    @Test
//    fun getAllUserProfilesTest() {
//        // Prepare a QuerySnapshot
//        val documentSnapshot = mockk<DocumentSnapshot>()
//        every { documentSnapshot.id } returns "1"
//        every { documentSnapshot.data } returns
//                mapOf(
//                    "name" to "Alice",
//                    "surname" to "Smith",
//                    "birthdate" to Date(),
//                    "pseudo" to "AliceS",
//                    "profileImageUrl" to "url")
//        every { querySnapshot.documents } returns listOf(documentSnapshot)
//        every { taskSnapshot.isSuccessful } returns true
//        every { taskSnapshot.result } returns querySnapshot
//        every { userProfileCollection.get() } returns taskSnapshot
//
//        // Run
//        val profiles = repository.getAllUserProfiles()
//
//        // Assert
//        verify { userProfileCollection.get() }
//        assert(profiles.size == 1)
//        assert(profiles.first().name == "Alice")
//    }
//
// }
