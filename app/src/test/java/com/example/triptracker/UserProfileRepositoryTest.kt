import com.example.triptracker.model.profile.UserProfile
import com.example.triptracker.model.repository.UserProfileRepository
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.Date
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserProfileRepositoryTest {

  @Mock private lateinit var mockFirestore: FirebaseFirestore

  @Mock private lateinit var mockCollection: CollectionReference

  @Mock private lateinit var mockTask: Task<QuerySnapshot>

  @Mock private lateinit var mockQuerySnapshot: QuerySnapshot

  private lateinit var userProfileRepository: UserProfileRepository

  @Before
  fun setUp() {
    // Initialize mocks
    `when`(mockFirestore.collection(anyString())).thenReturn(mockCollection)
    `when`(mockCollection.get()).thenReturn(mockTask)
    `when`(mockTask.isSuccessful).thenReturn(true)
    `when`(mockTask.result).thenReturn(mockQuerySnapshot)
    userProfileRepository = UserProfileRepository(mockFirestore)
  }

  @Test
  fun testGetAllUserProfiles() {
    // This is where you setup the behavior for when the success listener is triggered
    doAnswer { invocation ->
          val callback = invocation.getArgument<OnSuccessListener<QuerySnapshot>>(0)
          callback.onSuccess(mockQuerySnapshot)
          null
        }
        .`when`(mockTask)
        .addOnSuccessListener(any())

    // Now when getAllUserProfiles is called, it should trigger the onSuccess listener with the
    // mocked QuerySnapshot
    userProfileRepository.getAllUserProfiles()

    // Verification steps here...
    verify(mockFirestore.collection("user_profiles")).get()
    assertNotNull(userProfileRepository.getAllUserProfiles())
  }

  @Test
  fun testAddNewUserProfile() {
    val userProfile = UserProfile("id123", "John", "Doe", Date(2024, 4, 11), "Johnny", "url")
    doNothing()
        .`when`(
            mockFirestore.collection("user_profiles").document(userProfile.mail).set(userProfile))
    userProfileRepository.addNewUserProfile(userProfile)
    verify(mockFirestore.collection("user_profiles").document(userProfile.mail)).set(userProfile)
  }
}
